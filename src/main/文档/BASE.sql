-- auto-generated definition
create table LOGIN_INFO
(
    ID        VARCHAR2(36) not null,
    TYPE      NUMBER,
    USER_CODE VARCHAR2(20),
    PASSWORD  VARCHAR2(500),
    STATUE    NUMBER
)
/

comment on table LOGIN_INFO is '登录账号信息'
/

comment on column LOGIN_INFO.ID is '账号ID'
/

comment on column LOGIN_INFO.TYPE is '登录账号类型'
/

comment on column LOGIN_INFO.USER_CODE is '登录账号'
/

comment on column LOGIN_INFO.PASSWORD is '登录账号密码（加密）'
/

comment on column LOGIN_INFO.STATUE is '登录状态'
/

create unique index USER_INFO_ID_UINDEX
    on LOGIN_INFO (ID)
/

alter table LOGIN_INFO
    add constraint USER_INFO_PK
        primary key (ID)
/

-- auto-generated definition
create table USER_INFO
(
    ID       VARCHAR2(36) not null,
    NAME     VARCHAR2(100),
    NAME_BAK VARCHAR2(100)
)
/

comment on table USER_INFO is '用户信息'
/

comment on column USER_INFO.ID is '用户ID'
/

comment on column USER_INFO.NAME is '用户姓名'
/

comment on column USER_INFO.NAME_BAK is '用户备用姓名'
/

create unique index USER_INFO_ID_UINDEX
    on USER_INFO (ID)
/

alter table USER_INFO
    add constraint USER_INFO_PK
        primary key (ID)
/

