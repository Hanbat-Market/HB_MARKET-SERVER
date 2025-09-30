## 💎 한밭마켓
- 기간: 2024.01.01 - 2024.06.01

- 교내 중고거래가 오픈채팅방이나 에브리타임 어플의 쪽지 기능으로 진행되는 것에 불편함을 느껴 학생들의 생활의 질을 높이고자 진행한 프로젝트입니다.

<div align="center">
  <img src="https://github.com/WAFO-WaveInfo/WAFO-SERVER/assets/101490157/2b7e0d7b-67c8-4794-b5e7-9e76d82e8058" width="200" height="355">
  <img src="https://github.com/WAFO-WaveInfo/WAFO-SERVER/assets/101490157/d4faf186-90eb-4529-948b-a4730947b60a" width="200" height="355">
  <img src="https://github.com/WAFO-WaveInfo/WAFO-SERVER/assets/101490157/a888d11f-d428-4371-b409-cef8dcbb17fa" width="200" height="355">
</div>

<div align="center">
  <img src="https://github.com/WAFO-WaveInfo/WAFO-SERVER/assets/101490157/446d1cb6-c6a1-4896-82dd-c2232ba395d0" width="200" height="355">
  <img src="https://github.com/WAFO-WaveInfo/WAFO-SERVER/assets/101490157/e6b2d874-cb5d-46c8-bb6c-4f44e2da3f2e" width="200" height="355">
  <img src="https://github.com/WAFO-WaveInfo/WAFO-SERVER/assets/101490157/fa18b074-8e71-481f-9a6f-c7cf1e66c57d" width="200" height="355">
</div>

## 👬 Team
- Back-End: 김주찬
- IOS: 진동규
- Designer: 전지우

<br>

## 🛠️ 리팩토링 & 성능개선

### 1. reactive stream 활용 선언적, 비동기로 리팩토링 (1097ms -> 290ms)

<details>
    <summary>자세히</summary>

#### 1-1

원래 코드는 아래처럼 작성되어 있었습니다.

```java
      try {
            chat.setSenderNickName(sender.getNickname());
            chat.setReceiverNickName(receiver.getNickname());
            chat.setCreatedAt(LocalDateTime.now());

        } catch (NoSuchElementException e) {
            throw new NotExistUuidException();
        }
        Mono<Chat> saveChat = chatRepository.save(chat);

        String fcmToken = receiver.getFcmToken();
        int result = fcmService.sendMessageTo(new FcmSendDto(fcmToken, chat.getSenderNickName(), chat.getMsg()));
        chat.setFcmOk(result);
        return saveChat;
```
- 위 코드를 보면 반환은 Mono를 반환해주지만 정작 메소드 내부는 동기적으로 작동하고 있었습니다.
- 채팅과 푸시알림 로직이 동기적으로 진행되는 것이 이유였습니다.

![](https://velog.velcdn.com/images/jckim22/post/006e266a-e4cd-4e77-83eb-c0dc7a007033/image.png)
1097ms 정도의 채팅으로는 꽤나 불편한 시간이 걸리는 것은 큰 문제로 다가왔습니다.

코드가 동기적으로 진행되고 있음을 인식하고 리팩토링에 들어갔습니다.

#### 1-2

```java
        return Mono.just(chat)
                .map(c -> {
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
                .onErrorMap(e -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "채팅 푸시알림 에러", e));
```
내부에서 chat 자체를 Mono로 감싸고 Mono를 이용해서 비동적으로 푸시 알림 또한 전송했습니다.
또한 map 및 flatMap 연산자를 사용하여 데이터의 흐름을 선언적으로 표현했습니다.


#### 1-3 - 결과

![](https://velog.velcdn.com/images/jckim22/post/9e8cc6b2-54ae-4806-8842-aeca897bfa26/image.png)

- 채팅 + 푸시알림: 1096ms -> 280ms 으로 약 4배 개선

</details>



### 2. 양방향 연관관계의 필요성 따져보기 (유지보수 용이성⬆️)

<details>
    <summary>자세히</summary>

>양방향으로 하면 복잡도가 높아지는 단점이 있지만 성능상 이점을 얻을 수 있습니다. <br>
>정말 성능이 너무 중요해서 쿼리 하나를 줄이는게 꼭 필요한 상황이라면 복잡해지더라도 최적화를 해야합니다. <br>
>반면에 쿼리가 하나 더 나가더라도 시스템 자원이 충분해서 성능에 영향을 미치는 것이 미미하다면 코드 복잡도를 낮게 유지하는 것이 더 중요합니다. <br>
>                                            - 전 B기업 CTO -

위 말처럼 쿼리 하나를 위해서 양방향 연관관계를 설정할 필요는 없습니다.

```java
    @OneToOne(mappedBy = "member")
    private ImageFile imageFile;
```


```java
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;
```

하지만 한밭마켓에서는 위와 같이 Member와 ImageFile 간의 연관관계가 설정되어 있어서
프로필 이미지를 바꾸거나 할 때 기능에 비해 많은 복잡도가 생겼었습니다.

생각해보면 굳이 ImageFile 쪽에서 Member를 찾을 일은 없을 것 같았습니다.

Article과 ImageFile은 일대다의 관계이기 때문에 ImageFile 쪽에서 참조해야하기도 하고
Article에 들어가는 컬럼들이 많아서 양방향 연관관계를 복잡하지만 체감상 편리하게 사용하고 있었습니다.

하지만 Member와 ImageFile 간의 관계에서는 쿼리 수는 거의 그대론데 복잡도만 증가시키고 있었습니다.
Member에서 ImageFile을 참조하는 단방향 관계로 변경하니 복잡도가 많이 줄어들 수 있었습니다.

### 결과

MemberService의 복잡도 감소 -> 유지보수 용이성⬆️

</details>

<br>

## 💎 System Architecture
![한밭마켓 drawio](https://github.com/Hanbat-Market/HB_MARKET-SERVER/assets/101490157/eb1eb60d-3f0d-4518-98af-4f909a6c1036)


<br>

## 💎 Entity-Relationship Diagram


![스크린샷 2024-05-24 오전 4 48 00](https://github.com/Hanbat-Market/HB_MARKET-SERVER/assets/101490157/ea3ae8e1-25db-40c3-8497-3743fb7c739f)


<br>

## 💎 Service

> 교내 중고거래 플랫폼 어플리케이션


- 아래는 4월 초에 캡처된 영상이며 현재는 OAuth 로그인, 채팅 등 다른 기능들이 구현되었습니다.

![한밭마켓 리얼 회원가입](https://github.com/Hanbat-Market/HB_MARKET-SERVER/assets/101490157/1f836cff-48e4-4c55-aa8d-2d30dc712e32)

![한밭마켓 리얼 로그인 게시글](https://github.com/Hanbat-Market/HB_MARKET-SERVER/assets/101490157/127eb0e3-6234-49d5-9ce5-c320b75eb7c5)

![한밭마켓 리얼 구매, 판매내역](https://github.com/Hanbat-Market/HB_MARKET-SERVER/assets/101490157/07e312e7-56dd-4a0d-8e09-80a795812eb1)
