drop table if exists BOOK;

CREATE TABLE BOOK(
        id BIGINT NOT NULL AUTO_INCREMENT,
        author VARCHAR(20) NOT NULL,
        title VARCHAR(50) NOT NULL,
        PRIMARY KEY (id)
);