package HM.Hanbat_Market.chat;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {

    @Tailable // 커서를 안닫고 계속 유지한다.
    @Query("{ sender : ?0, receiver : ?1}")
    Flux<Chat> mFindBySender(String sender, String receiver); // Flux (흐름) response를 유지하면서 데이터를 계속 흘려보내기

    @Tailable
    @Query("{ roomNum: ?0 }")
    Flux<Chat> mFindByRoomNum(String roomNum);

    @Tailable
    @Query("{ $or: [ { sender: ?0, receiver: ?1 }, { sender: ?1, receiver: ?0 } ] }")
    Flux<Chat> mFindBySenderOrReceiver(String sender, String receiver);

    @Aggregation(pipeline = {
            "{ $match: { $or: [ { sender: ?0 }, { receiver: ?0 } ] } }",
            "{ $sort: { createdAt: -1 } }",
            "{ $group: { _id: '$roomNum', msg: { $first: '$msg' }, sender: { $first: '$sender' }, senderNickName: { $first: '$senderNickName' }, receiver: { $first: '$receiver' }, receiverNickName: { $first: '$receiverNickName' }, roomNum: { $first: '$roomNum' }, createdAt: { $first: '$createdAt' }, fcmOk: { $first: '$fcmOk' } } }"
    })
    Flux<Chat> findLatestChatBySenderOrReceiver(String user);
}