DROP TABLE IF EXISTS books;
create table books
(
    book_id bigint      not null
        AUTO_INCREMENT,
    name    varchar(255) not null,
    rating  int          not null,
    summary varchar(255) not null,

    primary key(book_id)
);

-- DROP TABLE IF EXISTS hibernate_sequence;
-- create table hibernate_sequence
-- (
--     next_val bigint null
-- );
--
-- INSERT INTO hibernate_sequence (next_val) VALUES (3);