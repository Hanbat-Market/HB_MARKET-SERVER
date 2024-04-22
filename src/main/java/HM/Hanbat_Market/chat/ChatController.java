package HM.Hanbat_Market.chat;

import HM.Hanbat_Market.chat.dto.ChatResponseDto;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.exception.member.NotExistUuidException;
import HM.Hanbat_Market.fcm.FcmSendDto;
import HM.Hanbat_Market.fcm.FcmService;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.member.MemberService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@RestController // 데이터 리턴 서버
public class ChatController {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final ChatService chatService;
    private final FcmService fcmService;

    private final ConcurrentHashMap<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Disposable> heartbeats = new ConcurrentHashMap<>();

    @CrossOrigin
    @Hidden
    @GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> getMsg(@PathVariable(value = "receiver") String sender, @PathVariable String receiver) {
        Flux<Chat> chatFlux = chatRepository.mFindBySender(sender, receiver);
        Flux<String> heartbeatFlux = Flux.interval(Duration.ofSeconds(30)) // 30초마다
                .map(tick -> "heartbeat"); // 하트비트 메시지 생성
        return Flux.merge(chatFlux, heartbeatFlux) // 채팅 메시지와 하트비트 메시지를 병합하여 전송
                .subscribeOn(Schedulers.boundedElastic());
    }

    // /chat/roomNum/{roomNum} 엔드포인트에서도 하트비트 메시지를 보내도록 수정
    @CrossOrigin
    @GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> findByRoomNum(@PathVariable(value = "roomNum") String roomNum) {
        SseEmitter sseEmitter = new SseEmitter();

        // 연결 이벤트를 보냅니다.
        try {
            sseEmitter.send(SseEmitter.event().name("open").data("Connection established."));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Flux<Chat> chatFlux = chatRepository.mFindByRoomNum(roomNum);
        Flux<String> heartbeatFlux = Flux.interval(Duration.ofSeconds(30)) // 30초마다
                .map(tick -> "heartbeat"); // 하트비트 메시지 생성
        Flux<Object> mergedFlux = Flux.merge(chatFlux, heartbeatFlux) // 채팅 메시지와 하트비트 메시지를 병합하여 전송
                .subscribeOn(Schedulers.boundedElastic());

        // 연결 이벤트와 병합된 Flux를 반환합니다.
        return Flux.concat(Flux.just(sseEmitter), mergedFlux);
    }


    @GetMapping(value = "/chat/rooms/{senderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ChatResponseDto> getLatestChatsBySender(@PathVariable(value = "senderId") String senderId) {
        return chatService.findLatestChatsBySender(senderId)
                .subscribeOn(Schedulers.boundedElastic());
    }


    @CrossOrigin
    @PostMapping("/chat")
    public Mono<Chat> setMsg(@RequestBody Chat chat) throws IOException {
        Member sender;
        Member receiver;

        try {
            log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Receiver");
            log.info(chat.getReceiver());
            log.info(chat.getSender());
            log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Sender");
            //UUID로 유저 식별
            sender = memberRepository.findByUUID(chat.getSender()).get();
            receiver = memberRepository.findByUUID(chat.getReceiver()).get();

            chat.setSenderNickName(sender.getNickname());
            chat.setReceiverNickName(receiver.getNickname());
            chat.setCreatedAt(LocalDateTime.now());

        } catch (NoSuchElementException e) {
            throw new NotExistUuidException();
        }
        Mono<Chat> saveChat = chatRepository.save(chat);

        //푸시알림
        String fcmToken = receiver.getFcmToken();
        int result = fcmService.sendMessageTo(new FcmSendDto(fcmToken, chat.getSenderNickName(), chat.getMsg()));
        chat.setFcmOk(result);

        return saveChat;
    }
}
