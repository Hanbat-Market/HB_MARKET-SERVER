package HM.Hanbat_Market.chat;

import HM.Hanbat_Market.chat.dto.ChatResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public Flux<ChatResponseDto> findLatestChatsBySender(String sender) {
        return chatRepository.findLatestChatBySenderOrReceiver(sender)
                .sort((chat1, chat2) -> chat2.getCreatedAt().compareTo(chat1.getCreatedAt()))  // 최신 순으로 재정렬
                .map(chat -> new ChatResponseDto(
                        chat.getSender(),
                        chat.getReceiver(),// UUID
                        chat.getSenderNickName(),  // 닉네임
                        chat.getReceiverNickName(),
                        chat.getMsg(),
                        chat.getRoomNum(),
                        chat.getCreatedAt()
                ));
    }
}
