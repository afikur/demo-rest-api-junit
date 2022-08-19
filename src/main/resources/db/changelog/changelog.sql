-- liquibase formatted sql

-- changeset afikur:1660897432864-1
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- preconditions-sql-check expectedResult:0 select count(*) from information_schema.tables where table name = 'books'
CREATE TABLE books (book_id BIGINT AUTO_INCREMENT NOT NULL, name VARCHAR(255) NOT NULL, rating INT NOT NULL, summary VARCHAR(255) NOT NULL, CONSTRAINT PK_BOOKS PRIMARY KEY (book_id));
-- rollback drop table books;