package HM.Hanbat_Market.chat;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "chat")
public class Chat {
    @Hidden @Id
    private String id;
    private String msg;
    private String sender; // 보내는 사람
//    @Hidden
    private String senderNickName;
    private String receiver; // 받는 사람 (귓속말)
//    @Hidden
    private String receiverNickName;
    private String roomNum; // 방 번호
    @Hidden
    private LocalDateTime createdAt;
    private int fcmOk;
}
