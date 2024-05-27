CREATE TABLE IF NOT EXISTS article (
                                       article_id BIGINT NOT NULL,
                                       created_at TIMESTAMP(6),
    user_id BIGINT,
    article_status VARCHAR(255) CHECK (article_status IN ('OPEN', 'HIDE')),
    description VARCHAR(255),
    title VARCHAR(255),
    trading_place VARCHAR(255),
    PRIMARY KEY (article_id),
    FOREIGN KEY (user_id) REFERENCES member(user_id)
    );

CREATE TABLE IF NOT EXISTS image_file (
                                          article_id BIGINT,
                                          file_id BIGINT NOT NULL,
                                          store_file_name VARCHAR(255),
    upload_file_name VARCHAR(255),
    PRIMARY KEY (file_id),
    FOREIGN KEY (article_id) REFERENCES article(article_id)
    );

CREATE TABLE IF NOT EXISTS item (
                                    article_id BIGINT UNIQUE,
                                    item_id BIGINT NOT NULL,
                                    preemption_count BIGINT,
                                    price BIGINT NOT NULL,
                                    user_id BIGINT,
                                    item_name VARCHAR(255) NOT NULL,
    item_status VARCHAR(255) CHECK (item_status IN ('SALE', 'RESERVATION', 'COMP', 'HIDE')),
    PRIMARY KEY (item_id),
    FOREIGN KEY (article_id) REFERENCES article(article_id),
    FOREIGN KEY (user_id) REFERENCES member(user_id)
    );

CREATE TABLE IF NOT EXISTS member (
                                      file_id BIGINT UNIQUE,
                                      user_id BIGINT NOT NULL,
                                      fcm_token VARCHAR(255),
    login_status VARCHAR(255) CHECK (login_status IN ('LOGIN', 'LOGOUT')),
    mail VARCHAR(255) NOT NULL UNIQUE,
    member_status VARCHAR(255) CHECK (member_status IN ('VERIFIED', 'UNVERIFIED')),
    name VARCHAR(255),
    nickname VARCHAR(255) NOT NULL UNIQUE,
    passwd VARCHAR(255) NOT NULL,
    role VARCHAR(255) CHECK (role IN ('ADMIN', 'USER')),
    uuid VARCHAR(255),
    verification_number VARCHAR(255),
    PRIMARY KEY (user_id),
    FOREIGN KEY (file_id) REFERENCES image_file(file_id)
    );

CREATE TABLE IF NOT EXISTS preemption_item (
                                               item_id BIGINT,
                                               preemption_item_id BIGINT NOT NULL,
                                               user_id BIGINT,
                                               preemption_item_status VARCHAR(255) CHECK (preemption_item_status IN ('PREEMPTION', 'CANCEL')),
    PRIMARY KEY (preemption_item_id),
    FOREIGN KEY (item_id) REFERENCES item(item_id),
    FOREIGN KEY (user_id) REFERENCES member(user_id)
    );

CREATE TABLE IF NOT EXISTS refresh_token (
                                             token_id BIGINT NOT NULL,
                                             user_id BIGINT UNIQUE,
                                             token VARCHAR(1000),
    uuid VARCHAR(255),
    PRIMARY KEY (token_id),
    FOREIGN KEY (user_id) REFERENCES member(user_id)
    );

CREATE TABLE IF NOT EXISTS trade (
                                     item_id BIGINT UNIQUE,
                                     reservation_date TIMESTAMP(6),
    trade_id BIGINT NOT NULL,
    transaction_appointment_date_time TIMESTAMP(6),
    user_id BIGINT,
    reservation_place VARCHAR(255),
    trade_status VARCHAR(255) CHECK (trade_status IN ('RESERVATION', 'COMP', 'CANCEL')),
    PRIMARY KEY (trade_id),
    FOREIGN KEY (item_id) REFERENCES item(item_id),
    FOREIGN KEY (user_id) REFERENCES member(user_id)
    );
