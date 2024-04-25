package HM.Hanbat_Market.chat;

import HM.Hanbat_Market.chat.dto.ChatResponseDto;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.MemberStatus;
import HM.Hanbat_Market.exception.member.NotExistUuidException;
import HM.Hanbat_Market.fcm.FcmSendDto;
import HM.Hanbat_Market.fcm.FcmService;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.member.MemberService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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


    /**
     * 동기 방식
     */

//    @CrossOrigin
//    @PostMapping("/chat")
//    public Mono<Chat> setMsg(@RequestBody Chat chat) throws IOException {
//        Member sender;
//        Member receiver;
//
//        try {
//            log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Receiver");
//            log.info(chat.getReceiver());
//            log.info(chat.getSender());
//            log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Sender");
//            //UUID로 유저 식별
//            sender = memberRepository.findByUUID(chat.getSender()).get();
//            receiver = memberRepository.findByUUID(chat.getReceiver()).get();
//
//            chat.setSenderNickName(sender.getNickname());
//            chat.setReceiverNickName(receiver.getNickname());
//            chat.setCreatedAt(LocalDateTime.now());
//
//        } catch (NoSuchElementException e) {
//            throw new NotExistUuidException();
//        }
//        Mono<Chat> saveChat = chatRepository.save(chat);
//
//        //푸시알림
//        String fcmToken = receiver.getFcmToken();
//        int result = fcmService.sendMessageTo(new FcmSendDto(fcmToken, chat.getSenderNickName(), chat.getMsg()));
//        chat.setFcmOk(result);
//
//        return saveChat;
//    }

    /**
     * 비동기 방식
     */

//    @PostMapping("/chat")
//    public Mono<Chat> setMsg(@RequestBody Chat chat) {
//        return Mono.just(chat)
//                .map(c -> {
//                    // UUID로 유저 식별하여 세팅
//                    Member sender = memberRepository.findByUUID(c.getSender()).orElseThrow(NotExistUuidException::new);
//                    Member receiver = memberRepository.findByUUID(c.getReceiver()).orElseThrow(NotExistUuidException::new);
//
//                    c.setSenderNickName(sender.getNickname());
//                    c.setReceiverNickName(receiver.getNickname());
//                    c.setCreatedAt(LocalDateTime.now());
//                    return c;
//                })
//                .flatMap(chatRepository::save)  // 채팅 메시지 저장
//                .doOnSuccess(savedChat -> {
//                    // 저장 성공 후 푸시 알림 전송
//                    Member receiver = memberRepository.findByUUID(savedChat.getReceiver()).get();
//                    String fcmToken = receiver.getFcmToken();
//                    int result = 0;
//                    try {
//                        result = fcmService.sendMessageTo(new FcmSendDto(fcmToken, savedChat.getSenderNickName(), savedChat.getMsg()));
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    savedChat.setFcmOk(result);
//                    chatRepository.save(savedChat).subscribe();  // 푸시 알림 결과 업데이트
//                })
//                .onErrorMap(e -> {
//                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "채팅, 푸시알림 에러@@@@@@@@@@@", e);
//                });
//    }
//
//    Reactive Programming의 완전한 활용: 첫 번째 코드는 Mono.just(chat)와 같은 리액티브 스트림을 사용하지 않고, 기존의 명령형 프로그래밍 방식을 따릅니다. 이는 코드의 실행이 선형적이고 동기적인 구조를 갖게 됩니다. 그에 반해, 두 번째 코드는 Mono를 사용하여 데이터 흐름을 선언적으로 처리합니다. 이 방식은 비동기 처리에 있어서 더 효율적이며, 서버 자원을 더 적게 사용하고 확장성이 더 높습니다.
//    비동기 처리의 개선: 두 번째 코드에서는 flatMap을 사용하여 Mono<Chat> 결과를 다른 비동기 연산과 연결하고, doOnSuccess에서 비동기적으로 푸시 알림을 처리합니다. 이는 데이터베이스 작업과 네트워크 호출이 동시에 진행될 수 있도록 하여 전체 처리 속도를 향상시킵니다.
//    예외 처리: 두 번째 코드는 예외를 더 효율적으로 처리합니다. orElseThrow를 사용하여 명확하게 예외를 발생시키고, onErrorMap을 통해 에러 발생 시 ResponseStatusException을 리턴합니다. 이는 예외 처리를 스트림의 일부로 통합하여 코드의 가독성과 유지 보수성을 향상시킵니다.
//    반응형 플로우 제어: 첫 번째 코드에서는 데이터 저장과 푸시 알림 처리가 순차적으로 이루어집니다. 반면 두 번째 코드에서는 doOnSuccess 블록을 사용하여 데이터가 성공적으로 저장된 후에만 푸시 알림이 발생하도록 합니다. 이렇게 하면 각 단계에서 발생할 수 있는 에러를 더 잘 제어하고, 필요한 경우 적절한 롤백을 할 수 있습니다.
//    서브스크립션 관리: 두 번째 코드에서는 subscribe 메소드를 사용하여 비동기적으로 데이터를 저장합니다. 이는 데이터베이스 작업이 이벤트 루프를 블로킹하지 않고, 다른 작업을 계속 처리할 수 있게 해줍니다.

    /**
     *  푸시알림과 채팅 전송 병렬 진행
     */
    @PostMapping("/chat")
    public Mono<Chat> setMsg(@RequestBody Chat chat) {
        return Mono.just(chat)
                .map(c -> {
                    Member sender = memberRepository.findByUUID(c.getSender()).orElseThrow(NotExistUuidException::new);
                    Member receiver = memberRepository.findByUUID(c.getReceiver()).orElseThrow(NotExistUuidException::new);
                    c.setSenderNickName(sender.getNickname());
                    c.setReceiverNickName(receiver.getNickname());
                    c.setCreatedAt(LocalDateTime.now());
                    return c;
                })
                .flatMap(c -> {
                    Mono<Chat> savedChatMono = chatRepository.save(c); // 채팅 메시지 저장
                    Mono<Integer> sendNotificationMono = sendMessage(c); // 푸시 알림 전송
                    return Mono.zip(savedChatMono, sendNotificationMono, (savedChat, fcmResult) -> {
                        savedChat.setFcmOk(fcmResult);
                        return savedChat;
                    });
                })
                .onErrorMap(e -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in chat or push notification", e));
    }

    private Mono<Integer> sendMessage(Chat chat) {
        return Mono.fromCallable(() -> {
            Member receiver = memberRepository.findByUUID(chat.getReceiver()).orElseThrow(NotExistUuidException::new);

            // receiver의 MemberStatus가 logout인 경우 푸시 알림을 보내지 않음
            if (receiver.getMemberStatus() == MemberStatus.LOGOUT) {
                return 0;
            }

            String fcmToken = receiver.getFcmToken();
            return fcmService.sendMessageTo(new FcmSendDto(fcmToken, chat.getSenderNickName(), chat.getMsg()));
        }).subscribeOn(Schedulers.boundedElastic());
    }


//    이 코드는 Mono.zip()을 사용하여 채팅 저장 작업과 푸시 알림 전송 작업을 병렬로 수행합니다.
//    각 작업의 결과는 Mono.zip()의 람다에서 처리되며, 모든 작업이 완료된 후 최종 채팅 객체를 반환합니다.
//    sendMessage() 메소드는 별도의 스레드에서 실행되어 I/O 작업을 비동기적으로 처리하게 됩니다.
//    이렇게 함으로써, 서버의 응답성을 향상시키고 전체적인 처리 속도를 개선할 수 있습니다.
}
