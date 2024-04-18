package HM.Hanbat_Market.chat;

import HM.Hanbat_Market.chat.dto.ChatResponseDto;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.exception.member.NotExistUuidException;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.member.MemberService;
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

    private final ConcurrentHashMap<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Disposable> heartbeats = new ConcurrentHashMap<>();

    // /sender/{sender}/receiver/{receiver} 엔드포인트에서도 하트비트 메시지를 보내도록 수정
    @CrossOrigin
    @GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> getMsg(@PathVariable String sender, @PathVariable String receiver) {
        Flux<Chat> chatFlux = chatRepository.mFindBySender(sender, receiver);
        Flux<String> heartbeatFlux = Flux.interval(Duration.ofSeconds(30)) // 30초마다
                .map(tick -> "heartbeat"); // 하트비트 메시지 생성
        return Flux.merge(chatFlux, heartbeatFlux) // 채팅 메시지와 하트비트 메시지를 병합하여 전송
                .subscribeOn(Schedulers.boundedElastic());
    }

    // /chat/roomNum/{roomNum} 엔드포인트에서도 하트비트 메시지를 보내도록 수정
    @CrossOrigin
    @GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> findByRoomNum(@PathVariable Integer roomNum) {
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
    public Flux<ChatResponseDto> getLatestChatsBySender(@PathVariable String senderId) {
        return chatService.findLatestChatsBySender(senderId)
                .subscribeOn(Schedulers.boundedElastic());
    }


    @CrossOrigin
    @PostMapping("/chat")
    public Mono<Chat> setMsg(@RequestBody Chat chat) {
        try {
            //UUID로 유저 식별
            Member sender = memberRepository.findByUUID(chat.getSender()).get();
            Member receiver = memberRepository.findByUUID(chat.getReceiver()).get();

            chat.setSenderNickName(sender.getNickname());
            chat.setReceiverNickName(receiver.getNickname());
            chat.setCreatedAt(LocalDateTime.now());

        }catch (NoSuchElementException e){
            throw new NotExistUuidException();
        }
        return chatRepository.save(chat);
    }

    // 더미 데이터를 보내는 메소드 추가
    private Flux<Chat> initialHeartbeat() {
        return Flux.interval(Duration.ofSeconds(30)) // 30초마다
                .map(ignore -> new Chat()) // 하트비트 메시지 생성
                .onBackpressureDrop() // 백프레셔로 통해 요소가 버려지지 않도록 설정
                .doOnError(error -> log.error("하트비트 에러: {}", error)); // 에러 발생 시 로깅
    }

//    @CrossOrigin
//    @GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter getMsg(@PathVariable String sender, @PathVariable String receiver) {
//        String identifier = sender + "-" + receiver;
//        SseEmitter sseEmitter = createSseEmitter(identifier);
//
//        return sseEmitter;
//    }
//
//    @CrossOrigin
//    @GetMapping(value = "/chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter findByRoomNum(@PathVariable Integer roomNum) {
//        String identifier = "room-" + roomNum;
//        SseEmitter sseEmitter = createSseEmitter(identifier);
//
//        // 해당 roomNum에 대한 채팅을 조회하여 Flux로 변환 후 클라이언트에게 전달
//        chatRepository.mFindByRoomNum(roomNum)
//                .subscribe(chat -> {
//                    try {
//                        sseEmitter.send(chat);
//                    } catch (IOException e) {
//                        sseEmitter.completeWithError(e);
//                    }
//                });
//
//        return sseEmitter;
//    }
//
//    private SseEmitter createSseEmitter(String identifier) {
//        SseEmitter sseEmitter = new SseEmitter();
//        sseEmitters.put(identifier, sseEmitter);
//        sseEmitter.onCompletion(() -> sseEmitters.remove(identifier));
//        sseEmitter.onTimeout(() -> sseEmitters.remove(identifier));
//
//        // 주기적으로 하트비트를 보내는 작업을 수행
//        Disposable heartbeatDisposable = Flux.interval(Duration.ofSeconds(30)) // 30초마다
//                .subscribe(tick -> sendHeartbeat(sseEmitter));
//        heartbeats.put(identifier, heartbeatDisposable);
//
//        return sseEmitter;
//    }
//
//    private void sendHeartbeat(SseEmitter sseEmitter) {
//        try {
//            sseEmitter.send("heartbeat");
//        } catch (IOException e) {
//            // 클라이언트와의 연결이 끊어진 경우 처리
//            sseEmitter.complete();
//        }
//    }
//
//    @CrossOrigin
//    @PostMapping("/chat")
//    public Mono<Chat> setMsg(@RequestBody Chat chat) {
//        try {
//            //UUID로 유저 식별
//            Member sender = memberRepository.findByUUID(chat.getSender()).get();
//            Member receiver = memberRepository.findByUUID(chat.getReceiver()).get();
//
//            chat.setSenderNickName(sender.getNickname());
//            chat.setReceiverNickName(receiver.getNickname());
//            chat.setCreatedAt(LocalDateTime.now());
//
//        } catch (NoSuchElementException e) {
//            throw new NotExistUuidException();
//        }
//        return chatRepository.save(chat);
//    }
}
