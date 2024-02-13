insert into member (mail,nickname,passwd,phone_number,user_id) values ('jckim229@gmail.com','김주찬','123','01028564221',1);
insert into member (mail,nickname,passwd,phone_number,user_id) values ('jckim2@gmail.com','김승찬','123','01086544221',2);
insert into article (article_status, created_at, description, user_id, title, trading_place, article_id) values ('OPEN', '2024-02-10 19:50:35', 'PS5 ' ||
                                                                                                                                                '싸게 ' ||
                                                                                                                                                '급처합니다.', 1, 'PS5 팝니다.', '대전 서구 ???아파튼', 1);
insert into item (article_id,item_name,item_status,user_id,preemption_count,price,item_id) values (1,'PS5','SALE',1,0,123,1);

insert into article (article_status, created_at, description, user_id, title, trading_place, article_id) values ('OPEN', '2024-02-10 19:50:36', 'PS3 ' ||
                                                                                                                                                '싸게 ' ||
                                                                                                                                                '급처합니다.', 1, 'PS3 팝니다.', '대전 서구 ???아파튼', 2);
insert into item (article_id,item_name,item_status,user_id,preemption_count,price,item_id) values (2,'PS3','SALE',1,0,123,2);

insert into article (article_status, created_at, description, user_id, title, trading_place, article_id) values ('OPEN', '2024-02-10 19:50:37', 'PS4 ' ||
                                                                                                                                                '싸게 ' ||
                                                                                                                                                '급처합니다.', 1, 'PS4 팝니다.', '대전 서구 ???아파튼', 3);
insert into item (article_id,item_name,item_status,user_id,preemption_count,price,item_id) values (3,'PS4','SALE',2,0,123,3);

insert into article (article_status, created_at, description, user_id, title, trading_place, article_id) values ('OPEN', '2024-02-10 19:50:38', 'PS2 ' ||
                                                                                                                                                '싸게 ' ||
                                                                                                                                                '급처합니다.', 1, 'PS2 팝니다.', '대전 서구 ???아파튼', 4);
insert into item (article_id,item_name,item_status,user_id,preemption_count,price,item_id) values (4,'PS2','SALE',2,0,123,4);

insert into article (article_status, created_at, description, user_id, title, trading_place, article_id) values ('OPEN', '2024-02-10 19:50:39', 'PS1 ' ||
                                                                                                                                                '싸게 ' ||
                                                                                                                                                '급처합니다.', 1, 'PS1 팝니다.', '대전 서구 ???아파튼', 5);
insert into item (article_id,item_name,item_status,user_id,preemption_count,price,item_id) values (5,'PS1','SALE',2,0,123,5);

INSERT INTO trade (trade_id,user_id, item_id, trade_date, trade_status)
VALUES (
            1,
           1,
           3,
           CURRENT_TIMESTAMP,
           'RESERVATION'
       );

INSERT INTO trade (trade_id, user_id, item_id, trade_date, trade_status)
VALUES (
            2,
           1,
           4,
           CURRENT_TIMESTAMP,
           'COMP'
       );

INSERT INTO trade (trade_id,user_id, item_id, trade_date, trade_status)
VALUES (
        3,
           1,
           5,
           CURRENT_TIMESTAMP,
           'RESERVATION'
       );

INSERT INTO trade (trade_id, user_id, item_id, trade_date, trade_status)
VALUES (
        4,
           2,
           1,
           CURRENT_TIMESTAMP,
           'RESERVATION'
       );

INSERT INTO trade (trade_id, user_id, item_id, trade_date, trade_status)
VALUES (
        5,
           2,
           2,
           CURRENT_TIMESTAMP,
           'COMP'
       );