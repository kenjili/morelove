##################################
#           创建数据库
##################################
drop database if exists morelove;
create database morelove default char set utf8;

use morelove;

#####################################
#           用户相关
#####################################

-- 创建用户表
drop table if exists tb_user;
create table tb_user(
	id int(11) unsigned not null auto_increment,
	username varchar(50) not null unique,#用户名，unique唯一性约束
	password varchar(50) not null,#登录密码
    realname varchar(50) not null,#真实姓名
    head_thumb varchar(255),#头像
    sex int default 0,#性别
    birthday TIMESTAMP not null DEFAULT '1997-1-1 12:00:00',#生日
    email varchar(50),#邮箱，保留字段
    phone_number varchar(50) not null unique,#手机号码,一个手机号码只能注册一个账号
    disable boolean default true,#是否禁用
    create_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    update_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,#在记录更新的时候自动更新为当前时间
	primary key (id),
    check(sex in (0,1))#约束性别只能取0或1
)engine=InnoDB default charset=utf8;

 -- 创建角色表
drop table if exists tb_role;
create table tb_role(
	id int(11) unsigned not null auto_increment,
	role_name varchar(50) not null,
	role_author varchar(50) not null,
	primary key (id)
)engine=InnoDB default charset=utf8;


-- 固定角色
insert into tb_role(role_name,role_author) values
('情侣','lover'),
('管理员','admin');


-- 创建用户角色映射表
drop table if exists tb_user_role;
create table tb_user_role(
	user_id int(11) unsigned not null,
	role_id int(11) unsigned not null,
    CONSTRAINT `tb_user_role_fk_uid` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`),
    CONSTRAINT `tb_user_role_fk_rid` FOREIGN KEY (`role_id`) REFERENCES `tb_role` (`id`)
)engine=InnoDB default charset=utf8;

    
-- 创建用户登录信息表
drop table if exists tb_user_login;
create table tb_user_login(
	user_id int(11) unsigned not null,#用户的id
	last_login_device varchar(50),#最近一次登录设备信息
    last_login_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,#最近一次登录时间
    password_error_count int not null default 0,#密码错误次数
    disable_state boolean not null default false,#是否被禁用登录功能
    disable_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `tb_user_login_fk_uid` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
)engine=InnoDB default charset=utf8;


##############################
#       业务相关
##############################

-- 创建情侣表
drop table if exists tb_love;
create table tb_love(
	id int(11) unsigned not null auto_increment,
	man_id int(11) unsigned not null,#男生id
    women_id int(11) unsigned not null,#女生id
    details text,#情侣介绍，详情
    love_img_thumb varchar(255),#情侣照片一张
    state int default -1,#等于-1为删除状态,该状态说明记录不存在，等于0为绑定成功,等于userId则userId为等待确认的一方。
    create_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    update_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,#在记录更新的时候自动更新为当前时间
	CONSTRAINT `tb_love_fk_manid` FOREIGN KEY (`man_id`) REFERENCES `tb_user` (`id`),
	CONSTRAINT `tb_love_fk_womenid` FOREIGN KEY (`women_id`) REFERENCES `tb_user` (`id`),
    primary key (id)
)engine=InnoDB default charset=utf8;

-- 给表添加联合索引，避免并发问题
ALTER TABLE `tb_love` ADD UNIQUE mw_index ( `man_id`,`women_id`);


-- 创建纪念日表
drop table if exists tb_memorialday;
create table tb_memorialday(
	id int(11) unsigned not null auto_increment,
    love_id int(11) unsigned not null,#情侣
    memorial_name varchar(50) not null,#纪念日名称
    memorial_date TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,#纪念日日期
    priority tinyint unsigned not null default 0,#重要等级，值越小越重要
    image varchar(255) not null,#纪念日背景图片
    create_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    update_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,#在记录更新的时候自动更新为当前时间
	CONSTRAINT `tb_love_fk_loveid` FOREIGN KEY (`love_id`) REFERENCES `tb_love` (`id`),
    primary key (id)
)engine=InnoDB default charset=utf8;


-- 创建打卡活动主题表
drop table if exists tb_cardsubj;
create table tb_cardsubj(
	id int(11) unsigned not null auto_increment,
    love_id int(11) unsigned not null,#情侣主机
    `subject` varchar(50) not null,#打卡类型名称
    period int not null default 0,# 周期，默认周期为一天，即每天打卡一次
    subj_img varchar(255) default null,#主题海报
    create_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    update_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,#在记录更新的时候自动更新为当前时间
	CONSTRAINT `tb_card_fk_loveid` FOREIGN KEY (`love_id`) REFERENCES `tb_love` (`id`),
    primary key (id)
)engine=InnoDB default charset=utf8;


-- 创建打卡记录表
drop table if exists tb_clockin;
create table tb_clockin(
	id int(11) unsigned not null auto_increment,
    subject_id int(11) unsigned not null,#打卡主题id
    user_id int(11) unsigned not null,
    create_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    update_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,#在记录更新的时候自动更新为当前时间
	CONSTRAINT `tb_clockin_fk_subjectid` FOREIGN KEY (`subject_id`) REFERENCES `tb_cardsubj` (`id`),
    CONSTRAINT `tb_clockin_fk_userid` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`),
    primary key (id)
)engine=InnoDB default charset=utf8;


-- 创建恋爱时光表
drop table if exists tb_time;
create table tb_time(
	id int(11) unsigned not null auto_increment,
    user_id int(11) unsigned not null,#发表的用户
    details text not null,#内容
    img1_thumb varchar(255),#缩略图
    img1 varchar(255),#原图
    img2_thumb varchar(255),
    img2 varchar(255),
    img3_thumb varchar(255),
    img3 varchar(255),
    state int not null default 0,#-1为删除,0为私密模式,1为公开模式
    access_count int not null default 0,#点击查看详情的次数
    create_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    update_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,#在记录更新的时候自动更新为当前时间
	CONSTRAINT `tb_time_fk_userid` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`),
    primary key (id)
)engine=InnoDB default charset=utf8;


-- 创建相册表
drop table if exists tb_album;
create table tb_album(
	id int(11) unsigned not null auto_increment,
    love_id int(11) unsigned not null,#情侣id
    album_name varchar(50) not null,#相册名称
    create_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    update_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,#在记录更新的时候自动更新为当前时间
	CONSTRAINT `tb_album_fk_loveid` FOREIGN KEY (`love_id`) REFERENCES `tb_love` (`id`),
    primary key (id)
)engine=InnoDB default charset=utf8;

-- 创建旅行日记，一个旅行日记对应一个相册
drop table if exists tb_itinerary;
create table tb_itinerary(
	id int(11) unsigned not null auto_increment,
    love_id int(11) unsigned not null,
    user_id int(11) unsigned not null,#是谁发表的
    title varchar(255) not null,#日记标题
    details text not null,#日记内容
    album_id int(11) unsigned not null,#相册id
    address varchar(50),#位置，保存经纬度字符串
    create_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    update_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,#在记录更新的时候自动更新为当前时间
	CONSTRAINT `tb_itinerary_fk_loveid` FOREIGN KEY (`love_id`) REFERENCES `tb_love` (`id`),
    CONSTRAINT `tb_itinerary_fk_albumid` FOREIGN KEY (`album_id`) REFERENCES `tb_album` (`id`),
    primary key (id)
)engine=InnoDB default charset=utf8;

-- 创建照片表
drop table if exists tb_photos;
create table tb_photos(
	id int(11) unsigned not null auto_increment,
    user_id int(11) unsigned not null,#上传的用户
    album_id int(11) unsigned not null,#相册id
    img varchar(255),#原图
    img_thumb varchar(255),#缩略图
    img_details varchar(255),#图片描述
    create_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    update_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,#在记录更新的时候自动更新为当前时间
	CONSTRAINT `tb_photos_fk_albumid` FOREIGN KEY (`album_id`) REFERENCES `tb_album` (`id`),
    CONSTRAINT `tb_photos_fk_userid` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`),
    primary key (id)
)engine=InnoDB default charset=utf8;


#############################################
#      创建触发器
#############################################


-- 创建触发器（在数据插入完成之后触发），在新记录添加之后，如果create_datetime为0则修改为当前时间
drop trigger if exists tb_user_trigger_update_create_datetime;
delimiter &
create trigger tb_user_trigger_update_create_datetime after insert on tb_user for each row
begin
	declare ctime TIMESTAMP;
    set ctime = new.create_datetime;
    if ctime=0 then
		update tb_user set create_datetime = CURRENT_TIMESTAMP where id=new.id;
    end if;
end &
delimiter ;


-- 创建触发器（在记录更新之前触发），在密码输入错误次数达到4次时，禁用登录功能
-- 对本表操作不能使用update，应使用set，避免循环触发导致死循环
drop trigger if exists tb_user_login_trigger_update_disable_state;
delimiter &
create trigger tb_user_login_trigger_update_disable_state before update on tb_user_login for each row
begin	
	declare pecount int default 0;
    set pecount = new.password_error_count;
    if pecount>=4 then
		#设置半小时后可登录
		set new.disable_state = true;
        #date_sub(当前时间,minute/second/...)取当前时间多少分钟/秒钟之前的时间
        #date_add(当前时间,minute/second/...)取当前时间多少分钟/秒钟之后的时间
        set new.disable_datetime = (select date_add(now(), interval 30 minute));
	elseif pecount<4 then
		set new.disable_state = false;
    end if;
end &
delimiter ;


-- 添加角色，为已经绑定成功为情侣的用户添加情侣角色
drop trigger if exists tb_love_trigger_update_role_and_userrole;
delimiter &
create trigger tb_love_trigger_update_role_and_userrole after update on tb_love for each row
begin
	declare lover_role_id int(11) default 0;
    if new.state = 0 then #0为绑定成功
		#获取角色id
		select id into lover_role_id from tb_role where role_name='情侣';
		#为用户添加角色
		insert into tb_user_role set user_id = new.man_id,role_id = lover_role_id;
		insert into tb_user_role set user_id = new.women_id,role_id = lover_role_id;
	elseif new.state = -1 then
		#获取角色id
		select id into lover_role_id from tb_role where role_name='情侣';
		#删除角色
        delete from tb_user_role where user_id in (new.man_id,new.women_id) and role_id = lover_role_id;
    end if;
end &
delimiter ;


-- 创建触发器，当情侣绑定成功后，为这对情侣创建系统默认纪念日
--  drop trigger if exists tb_love_trigger_insert_commemorate;
-- delimiter &
-- create trigger tb_love_trigger_insert_commemorate after update on tb_love for each row
-- begin
-- 	if new.state = 0 then 
-- 		insert into tb_commemorate (love_id,commemorate_name)
-- 		values(new.id,'在一起'),(new.id,'第一次牵手'),(new.id,'第一次接吻');
-- 	end if;
-- end &
-- delimiter ;





#############################################
#      业务扩展
#############################################

-- 创建省份表
drop table if exists tb_address_province;
create table tb_address_province(
	id int(11) unsigned not null,
    pro_name varchar(255) not null unique,
    primary key (id)
)engine=InnoDB default charset=utf8;


-- 创建城市表
drop table if exists tb_address_city;
create table tb_address_city(
	id int(11) unsigned not null,
    pro_id int(11) unsigned not null,
    city_name varchar(255) not null,
    CONSTRAINT `tb_province_fk_proid` FOREIGN KEY (`pro_id`) REFERENCES `tb_address_province` (`id`),
    primary key (id)
)engine=InnoDB default charset=utf8;



-- 创建地区表，有的省份直接是地区的，没有城市的，所以需要加个省份id
drop table if exists tb_address_area;
create table tb_address_area(
	id int(11) unsigned not null,
    pro_id int(11) unsigned not null,
    city_id int(11) unsigned,
    area_name varchar(255) not null,
    CONSTRAINT `tb_city_fk_cityid` FOREIGN KEY (`city_id`) REFERENCES `tb_address_city` (`id`),
     CONSTRAINT `tb_pro_fk_proid` FOREIGN KEY (`pro_id`) REFERENCES `tb_address_province` (`id`),
    primary key (id)
)engine=InnoDB default charset=utf8;


-- 创建旅游景点表，后台可管理上传
drop table if exists tb_scenic_area;
create table tb_scenic_area(
	id int(11) unsigned not null,
    area_id int(11) unsigned not null,#所在地区id
    scenic_name varchar(255) not null,#景点名称
    address varchar(255) not null,#景点地址
    location_x float,#地图上的经纬度坐标
    location_y float,#地图上的经纬度坐标
    summary	varchar(255),#景点描述
    CONSTRAINT `tb_scenic_area_fk_areaid` FOREIGN KEY (`area_id`) REFERENCES `tb_address_area` (`id`),
    primary key (id)
)engine=InnoDB default charset=utf8;

-- 创建旅游景点的图片表
drop table if exists tb_scenic_area_images;
create table tb_scenic_area_images(
	id int(11) unsigned not null auto_increment,
    scenic_area_id int(11) unsigned not null,
    pic_url_small varchar(255),#小图
    pic_url varchar(255),#大图
    CONSTRAINT `tb_scenic_area_images_fk_scenic_area_id` FOREIGN KEY (`scenic_area_id`) REFERENCES `tb_scenic_area` (`id`),
    primary key (id)
)engine=InnoDB default charset=utf8;


#############################
#      聊天业务
############################
drop table if exists tb_chat_message;
create table tb_chat_message(
	id int(11) unsigned not null auto_increment,
    send_uid int(11) unsigned not null,
    read_uid int(11) unsigned not null,
    msg_type tinyint unsigned not null,
    message text not null,
    state tinyint unsigned not null default 0,#消息状态、0为未读、1为已读、2为过期
    create_datetime TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `tb_chat_message_fk_send_uid` FOREIGN KEY (`send_uid`) REFERENCES `tb_user` (`id`),
    CONSTRAINT `tb_chat_message_fk_read_uid` FOREIGN KEY (`read_uid`) REFERENCES `tb_user` (`id`),
    primary key (id)
)engine=InnoDB default charset=utf8;
