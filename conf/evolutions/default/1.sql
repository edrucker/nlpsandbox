# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table track (
  id                        bigint not null,
  title                     varchar(255),
  artist                    varchar(255),
  lyrics                    clob,
  constraint pk_track primary key (id))
;

create sequence track_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists track;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists track_seq;

