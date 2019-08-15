

alter table USER_INFO
	add MOBILE varchar2(20)
/

comment on column USER_INFO.MOBILE is '用户手机号码'
/create table ROLE
(
	ROLE_ID VARCHAR2(36) not null,
	NAME VARCHAR2(100)
)
/

comment on table ROLE is '角色信息'
/

comment on column ROLE.ROLE_ID is '角色ID'
/

create unique index ROLE_ROLE_ID_uindex
	on ROLE (ROLE_ID)
/

alter table ROLE
	add constraint ROLE_pk
		primary key (ROLE_ID)
/

-- auto-generated definition
create table ROLE_USER
(
    ROLE_ID VARCHAR2(36),
    USER_ID VARCHAR2(36)
)
/

comment on table ROLE_USER is '角色和用户关联表'
/

comment on column ROLE_USER.ROLE_ID is '角色ID'
/

comment on column ROLE_USER.USER_ID is '用户ID'
/


create table MENU
(
	MENU_ID VARCHAR2(36) not null,
	TITLE VARCHAR2(100),
	CODE VARCHAR2(200),
	MENU_PAR_ID VARCHAR2(36),
	ORDER_ID NUMBER
)
/

comment on table MENU is '菜单信息'
/

comment on column MENU.MENU_ID is '菜单ID'
/

comment on column MENU.TITLE is '菜单名称'
/

comment on column MENU.CODE is '菜单code'
/

comment on column MENU.MENU_PAR_ID is '父级菜单ID'
/

comment on column MENU.ORDER_ID is '排序ID'
/

create unique index MENU_MENU_ID_uindex
	on MENU (MENU_ID)
/

alter table MENU
	add constraint MENU_pk
		primary key (MENU_ID)
/
ALTER TABLE USER_INFO ADD CRT_TIME DATE NULL;
ALTER TABLE USER_INFO ADD ALT_TIME DATE NULL;
