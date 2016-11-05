/* 在 es_helpcenter表中添加字段if_public  dengzhandong 20160922 */
alter table es_helpcenter add if_public int default 0 
/* 添加优惠活动管理表 es_gift  ljq 2015/9/7 */
DROP TABLE IF EXISTS `es_gift`;
CREATE TABLE `es_gift` (
  `id` int(8) NOT NULL auto_increment,
  `name` varchar(100) default NULL,
  `start_time` bigint(20) default NULL,
  `end_time` bigint(20) default NULL,
  `rule` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*Member表  添加卖家所在国member_country字段   王凯智   */
ALTER TABLE es_member ADD  member_country  varchar(255) ;
/*Store表  添加店铺所在国store_country,店铺市场选择store_market字段   王凯智   */
ALTER TABLE es_store ADD  store_country  varchar(255) default 'CHN';
ALTER TABLE es_store ADD  store_market  varchar(255)  default 'RUS';
/*es_bonus_type表  添加优惠券俄文名称，卢布金额，订单最低卢布金额   jfw   */
alter table es_bonus_type add type_name_ru varchar(255);
alter table es_bonus_type add type_money_ru decimal(10,2) DEFAULT 0 ;
alter table es_bonus_type add min_goods_amount_ru decimal(10,2) DEFAULT 0 ;
/*es_member_bonus表  添加会员优惠券俄文名称   jfw   */
alter table es_member_bonus add type_name_ru varchar(255);


alter table es_member add nation  varchar(255) default NULL;

alter table es_point_history add endtime bigint(20) default 0;
alter table es_point_history add order_id int(20) default 0;
/*es_payment_cfg表  添加支付方式locale字段   王凯智   */
ALTER TABLE es_payment_cfg ADD  locale  varchar(255) ;
/*es_goods表  添加运费类型freightType和运费freight字段   王凯智   */
ALTER TABLE es_goods ADD  freightType  varchar(255) ;
ALTER TABLE es_goods ADD  freight  decimal(20,2) DEFAULT 0  ;
ALTER TABLE es_goods ADD  freightru  decimal(20,2) DEFAULT 0  ;
/*   增加注册赠送积分规则 es_register_point jfw */
DROP TABLE IF EXISTS `es_register_point`;
CREATE TABLE `es_register_point` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `point` int(10) DEFAULT NULL,
  `mp` int(10) DEFAULT NULL,
  `status` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/* 积分消费价值设置es_point_consume 表 王凯智 2015/09/02    */
DROP TABLE IF EXISTS `es_point_consume`;
CREATE TABLE `es_point_consume` (
  `consume_id` int(11) NOT NULL auto_increment,
  `consume_num` int(11) default NULL,
  `consume_currency` varchar(255) default NULL,
  `consume_amount` decimal(20,2) default NULL,
  PRIMARY KEY  (`consume_id`)

) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/* 首页大屏广告表es_ad_index表 王凯智 2015/09/03 */
DROP TABLE IF EXISTS `es_ad_index`;
CREATE TABLE `es_ad_index` (
  `adindex_id` int(11) NOT NULL auto_increment,
  `adindex_disable` int(11) default NULL,
  `adindex_url` varchar(255) default NULL,
  `outer_css` text,
  `close_css` text,
  `jump_css` text,
  PRIMARY KEY  (`adindex_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*给商品推荐类表添加一个字段代表“市场”，,1表示中国市场，2代表俄罗斯市场 */
alter table es_team add market int(10) default 1;

/*增加会员等级表中  积分有效期，消费次数（卢布），消费次数（人民币）消费抵值比例等字段 es_member_lv 2015/9/4 ljq */
alter table es_member_lv add validity int(11) default 0;
alter table es_member_lv add con_ru int(11) default 0;
alter table es_member_lv add con_zh int(11) default 0;
alter table es_member_lv add proportion double(11,2) default 0.00;


/*增加国家表    2015/9/19  WKZ  */
DROP TABLE IF EXISTS `es_country`;
CREATE TABLE `es_country` (
  `country_id` int(11) NOT NULL auto_increment,
  `country_name` varchar(255) default NULL,
  `country_code` varchar(255) default NULL,
  PRIMARY KEY  (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*增加物流国家关联表    2015/9/19  WKZ  */
DROP TABLE IF EXISTS `es_logi_country`;
CREATE TABLE `es_logi_country` (
  `logi_id` int(11) NOT NULL,
  `country_id` int(11) NOT NULL,
  PRIMARY KEY  (`logi_id`,`country_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*增加商品折扣信息字段 2015/9/18 jfw  更新代码后请在本地数据库里运行sql */
alter table es_goods add cost_price DECIMAL(20,2);
alter table es_goods add cost_price_ru DECIMAL(20,2);
alter table es_goods add is_discount INT(10)  default '0';

/*增加会员地址es_member_address表  地址类型字段add_type 为卖家使用 1为发货2为退货    2015/9/19  WKZ  */
alter table es_member_address add add_type INT(11) ;

/*增加卖家物流地址es_seller_express表    2015/9/19  WKZ  */

DROP TABLE IF EXISTS `es_seller_express`;
CREATE TABLE `es_seller_express` (
  `express_id` int(11) NOT NULL auto_increment,
  `express_name` varchar(255) default NULL,
  `express_deliver_time` varchar(255) default NULL,
  `express_storeid` varchar(255) default NULL,
  PRIMARY KEY  (`express_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

/*增加订单es_order表  发货地区ship_send_area和发货地址ship_send_address字段    2015/9/20  WKZ  */
ALTER TABLE `es_order` ADD COLUMN `ship_send_area`  varchar(255) NULL ;
ALTER TABLE `es_order` ADD COLUMN `ship_send_address`  varchar(255) NULL ;
ALTER TABLE `es_order` ADD COLUMN `ship_send_zip`  varchar(255) NULL ;
ALTER TABLE `es_order` ADD COLUMN `ship_send_name`  varchar(255) NULL ;
ALTER TABLE `es_order` ADD COLUMN `ship_send_tel`  varchar(255) NULL ;

/* 买赠优惠活动表   es_gift表   ljq    2015/09/20 */
DROP TABLE IF EXISTS `es_gift`;
CREATE TABLE `es_gift` (
  `gift_id` int(8) NOT NULL auto_increment,
  `name` varchar(100) default NULL,
  `start_time` bigint(20) default NULL,
  `end_time` bigint(20) default NULL,
  `rule` varchar(255) default NULL,
  `state` smallint(1) default NULL,
  `type` smallint(1) default NULL,
  `save_time` bigint(20) default NULL,
  `send_time` bigint(20) default NULL,
  PRIMARY KEY  (`gift_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/* 买赠优惠活动发布信息表   es_eventmessage 表   ljq    2015/09/20 */
DROP TABLE IF EXISTS `es_eventmessage`;
CREATE TABLE `es_eventmessage` (
  `event_id` int(8) NOT NULL auto_increment,
  `member_id` int(8) default NULL,
  `gift_id` int(8) default NULL,
  `state` smallint(1) default NULL,
  `send_time` bigint(20) default NULL,
  `rule` varchar(255) default NULL,
  `eventname` varchar(255) default NULL,
  PRIMARY KEY  (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `es_route` (
  `id` bigint(20) NOT NULL auto_increment,
  `mailno`  varchar(100)  NOT NULL,
  `remark` varchar(100) default NULL,
  `accept_time` varchar(100) default NULL,
  `accept_address` varchar(100) default NULL,
  `opcode` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
CREATE TABLE `es_route_order` (
  `id` bigint(20) NOT NULL auto_increment,
  `orderid`  varchar(100)  NOT NULL,
  `mailno` varchar(100) default NULL,
  `origincode` varchar(100) default NULL,
  `destcode` varchar(100) default NULL,
  `filter_result` varchar(100) default NULL,
  `agent_mailno` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

/* goods表新增最小发货量设置功能 jfw   2015/10/13 */
alter table es_goods add min_number INT(10)  default '1';
/*新增大宗交易意向表 jfw 2015/10/14*/
DROP TABLE IF EXISTS `es_large_order`;
CREATE TABLE `es_large_order` (
  `mind_id` int(10) NOT NULL auto_increment,
  `store_name` varchar(255) default NULL,
  `goods_id` int(10) default NULL,
  `goods_name` varchar(255) default NULL,
  `price` varchar(255) default NULL,
  `goods_num` int(10) default NULL,
  `order_text` varchar(255) default NULL,
  `buyer_name` varchar(255) default NULL,
  `company` varchar(255) default NULL,
  `email` varchar(255) default NULL,
  `phone` varchar(255) default NULL,
  `request_time` bigint(20) default NULL,
  `handle_time` bigint(20) default NULL,
  `handle_person` varchar(255) default NULL,
  `handle_result` varchar(255) default NULL,
  `status` int(10) default NULL,
  PRIMARY KEY  (`mind_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*新增大宗交易意订单表 jfw 2015/10/16*/
DROP TABLE IF EXISTS `es_big_order`;
CREATE TABLE `es_big_order` (
  `order_id` int(10) NOT NULL auto_increment,
  `sn` varchar(255) default NULL,
  `status` smallint(1) default NULL,
  `pay_status` smallint(1) default NULL,
  `ship_status` smallint(1) default NULL,
  `goods` varchar(255) default NULL,
  `goods_num` int(10) default NULL,
  `create_time` bigint(20) default NULL,
  `ship_name` varchar(255) default NULL,
  `ship_addr` varchar(255) default NULL,
  `ship_email` varchar(255) default NULL,
  `ship_tel` varchar(255) default NULL,
  `currency` varchar(255) default NULL,
  `order_amount` decimal(20,2) default NULL,
  `remark` varchar(255) default NULL,
  `payment_type` smallint(1) default NULL,
  `store_name` varchar(255) default NULL,
  PRIMARY KEY  (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;


/*添加查看访问数 2015/10/15 by 张明明*/
DROP TABLE IF EXISTS `es_visituser`;
CREATE TABLE `es_visituser` (
  `visit_id` int(11) NOT NULL auto_increment,
  `visit_time` varchar(255) default NULL,
  `visit_message` varchar(255) default NULL,
  `visit_num` int(11) default NULL,
  `visit_onlinenum` int(11) default NULL,
  PRIMARY KEY  (`visit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/**修改个人信息国籍问题**/
UPDATE  es_member set member_country='CHN' where member_country is NULL;
/**俄速通轨迹10.19 ngq**/
DROP TABLE IF EXISTS `es_route_ru`;
CREATE TABLE `es_route_ru` (
  `id` bigint(20) NOT NULL auto_increment,
  `track_code`  varchar(100)  default NULL,
  `track_content` varchar(100) default NULL,
  `occur_date` varchar(100) default NULL,
  `occur_address` varchar(100) default NULL,
  `tracking_number` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

ALTER TABLE `es_route_ru` ADD COLUMN `reference_Number`  varchar(255) NULL ;
/**俄速通轨迹10.19 ngq**/

/*es_goods表增加原始价格jfw团购活动*/
alter table es_goods add original_price double(20,2) default 0.00;
alter table es_goods add original_price_ru double(20,2) default 0.00;
/*es_groupbuy_goods表增加表增加卢布团购价和卢布原价jfw团购活动*/
alter table es_groupbuy_goods add price_ru double(20,2) default 0.00;
alter table es_groupbuy_goods add original_price_ru double(20,2) default 0.00;
/*添加商品详情页运费表lxy*/
DROP TABLE IF EXISTS `es_good_logis_detail`;
CREATE TABLE `es_good_logis_detail` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `session_id` varchar(222) DEFAULT NULL,
  `freight_id` int(8) DEFAULT NULL,
  `goods_id` int(8) DEFAULT NULL,
  `sendprice` decimal(8,2) DEFAULT NULL,
  `store_id` int(8) DEFAULT NULL,
  `currency` varchar(47) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*创建es_freight_standard 表为运费模板表lxy*/
DROP TABLE IF EXISTS `es_freight_standard`;
CREATE TABLE `es_freight_standard` (
  `freight_id` int(8) NOT NULL,
  `logis_name` varchar(255) default NULL,
  `product_name` varchar(255) default NULL,
  `max_weight` double(8,4) default NULL,
  `min_weight` double(8,4) default NULL,
  `start_price` decimal(8,4) default NULL,
  `every_price_kg` decimal(8,4) default NULL,
  `start_price_kg` decimal(8,4) default NULL,
  `extra_price` decimal(8,4) default NULL,
  `start_price_ru` decimal(8,0) default NULL,
  `every_price_kg_ru` decimal(8,0) default NULL,
  `start_price_kg_ru` decimal(8,0) default NULL,
  `extra_price_ru` decimal(8,0) default NULL,
  `max_long` double(8,4) default NULL,
  `min_long` double(8,4) default NULL,
  `max_width` double(8,4) default NULL,
  `min_width` double(8,4) default NULL,
  `max_high` double(8,4) default NULL,
  `min_high` double(8,4) default NULL,
  `validatedays` varchar(255) default NULL,
  PRIMARY KEY  (`freight_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*创建es_logis_model表为商家模板表lxy*/
DROP TABLE IF EXISTS `es_logis_model`;
CREATE TABLE `es_logis_model` (
  `logis_model_id` varchar(88) NOT NULL default '',
  `freight_id` int(8) default NULL,
  `model_name` varchar(77) default NULL,
  `logis_price_type` int(6) default NULL,
  `store_id` int(8) default NULL,
  `is_name` int(8) default NULL,
  PRIMARY KEY  (`logis_model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*创建es_logicompany表，增加字段lxy*/
DROP TABLE IF EXISTS `es_logi_company`;
CREATE TABLE `es_logi_company` (
  `id` int(9) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `ename` varchar(255) default NULL,
  `type` varchar(255) default NULL,
  `url` varchar(255) default NULL,
  `time` varchar(43) default NULL,
  `remark` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*添加商品详情页运费表lxy*/
DROP TABLE IF EXISTS `es_good_logis_detail`;
CREATE TABLE `es_good_logis_detail` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `session_id` varchar(222) DEFAULT NULL,
  `freight_id` int(8) DEFAULT NULL,
  `goods_id` int(8) DEFAULT NULL,
  `sendprice` decimal(8,2) DEFAULT NULL,
  `store_id` int(8) DEFAULT NULL,
  `currency` varchar(47) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*增加order_item表2个字段 lxy*/
alter table es_order_items add sendprice DECIMAL(20,2);
alter table es_order_items add freight_id INT(10);
/*增加order表一个字段 lxy*/
alter table es_order add shiping_freight DECIMAL(20,2);
/**2016-01-20 zmm 用户把产品添加到购物车后进行保留**/
alter table es_cart add member_id int(8) default 0;
/*聊天所需的聊天记录chat表和联系人recent表   lmn*/
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `es_chat`;
CREATE TABLE `es_chat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sender` bigint(20) DEFAULT NULL,
  `sendee` bigint(20) DEFAULT NULL,
  `sendtime` varchar(50) DEFAULT NULL,
  `state` int(2) DEFAULT NULL,
  `message_begin` varchar(10000) DEFAULT NULL,
  `message_end` varchar(10000) DEFAULT NULL,
  `translation_front` varchar(50) DEFAULT NULL,
  `translation_back` varchar(50) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=505 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `es_recent`;
CREATE TABLE `es_recent` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `member_id` int(10) DEFAULT NULL,
  `recent_id` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*新建es_count_action 刘宏宇 1.29*/
DROP TABLE IF EXISTS `es_count_action`;
CREATE TABLE `es_count_action` (
  `id` int(11) NOT NULL auto_increment,
  `address` varchar(50) default NULL,
  `session_id` varchar(50) default NULL,
  `come_time` varchar(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*新建es_count_address 刘宏宇 1.29*/
DROP TABLE IF EXISTS `es_count_address`;
CREATE TABLE `es_count_address` (
  `id` int(11) NOT NULL auto_increment,
  `address` varchar(50) default NULL,
  `action` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of es_count_address
-- ----------------------------
INSERT INTO `es_count_address` VALUES ('1', '/login.html', '登录');
INSERT INTO `es_count_address` VALUES ('2', '/member/member.html', '会员首页');
INSERT INTO `es_count_address` VALUES ('3', '/', '首页');
INSERT INTO `es_count_address` VALUES ('4', '/register.html', '注册');
INSERT INTO `es_count_address` VALUES ('5', '/member/order.html', '我的订单');
INSERT INTO `es_count_address` VALUES ('6', '/member/return_list.html', '退货申请');
INSERT INTO `es_count_address` VALUES ('7', '/member/ask.html', '我的咨询');
INSERT INTO `es_count_address` VALUES ('8', '/member/comments.html', '我的评价');
INSERT INTO `es_count_address` VALUES ('9', '/member/point_list.html', '会员积分');
INSERT INTO `es_count_address` VALUES ('10', '/member/message_accept_list.html', '站内信-收到信息');
INSERT INTO `es_count_address` VALUES ('11', '/member/message_send_list.html', '站内信-已发信息');
INSERT INTO `es_count_address` VALUES ('12', '/member/security.html', '修改密码');
INSERT INTO `es_count_address` VALUES ('13', '/member/address.html', '收货地址管理');
INSERT INTO `es_count_address` VALUES ('14', '/member/favorite.html', '收藏的商品');
INSERT INTO `es_count_address` VALUES ('15', '/member/collect_store.html', '收藏的店铺');
INSERT INTO `es_count_address` VALUES ('16', '/cart.html', '我的购物车');
INSERT INTO `es_count_address` VALUES ('17', '/index.html', '首页');
INSERT INTO `es_count_address` VALUES ('18', '/goods_list.html', '商品列表');
INSERT INTO `es_count_address` VALUES ('19', '/checkout/new_large_order.html', '大宗交易');
INSERT INTO `es_count_address` VALUES ('20', '/home.html', '商家店铺');
INSERT INTO `es_count_address` VALUES ('21', '/ask/chat.html', '咨询卖家');
INSERT INTO `es_count_address` VALUES ('22', '/checkout.html', '核对订单信息');
INSERT INTO `es_count_address` VALUES ('23', '/checkout/new_address.html', '新增收获地址');
INSERT INTO `es_count_address` VALUES ('24', '/order_create_success.html', '成功提交订单');
/*新建es_count_order 刘宏宇 1.29*/
CREATE TABLE `es_count_order` (
  `order_count_id` int(11) NOT NULL auto_increment,
  `member_id` int(11) default NULL,
  `session_id` varchar(50) default NULL,
  `order_id` int(11) default NULL,
  PRIMARY KEY  (`order_count_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*新建es_flow_count 刘宏宇 1.29*/
CREATE TABLE `es_flow_count` (
  `flow_id` int(11) NOT NULL auto_increment,
  `session_id` varchar(50) default NULL,
  `user_ip` varchar(50) default NULL,
  `user_addr` varchar(50) default NULL,
  `come_time` varchar(50) default '',
  `go_time` varchar(50) default NULL,
  `user_name` varchar(50) default NULL,
  `order_status` varchar(10) default '否',
  PRIMARY KEY  (`flow_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*增加goods_move,一键搬家抓取过来的数据,刘宏宇,2.26*/
DROP TABLE IF EXISTS `es_goods_move`;
CREATE TABLE `es_goods_move` (
  `goods_move_id` int(11) NOT NULL auto_increment,
  `name` varchar(200) default NULL,
  `meta_keywords` varchar(1000) default NULL,
  `price` decimal(20,2) default NULL,
  `big` varchar(255) default NULL,
  `small` longtext,
  `intro` longtext,
  `store_id` int(11) default NULL,
  PRIMARY KEY  (`goods_move_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

UPDATE es_goods set freightType=0 WHERE freightType=2;
/*添加goods表*/ 
alter table es_goods add city varchar(255) default null;
alter table es_goods add province varchar(255) default null;
alter table es_goods add region varchar(255) default null;
alter table es_goods add region_id int(8) default 0;
alter table es_goods add province_id int(8) default 0;
alter table es_goods add city_id int(8) default 0;
alter table es_goods add is_belong int(8) default 0;
alter table es_goods add porigin varchar(255) default null;
/*添加es_brand表*/ 
alter table es_brand add region varchar(255) default null;
/*添加es_brand_tag表*/
DROP TABLE IF EXISTS `es_brand_tag`;
CREATE TABLE `es_brand_tag` (
  `brand_id` int(8) NOT NULL AUTO_INCREMENT,
  `name_zh` varchar(233) DEFAULT NULL,
  `name_ru` varchar(233) DEFAULT NULL,
  `is_show` int(8) DEFAULT NULL,
  `market` int(8) DEFAULT NULL,
  PRIMARY KEY (`brand_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*添加es_brand_tag_rel表*/
DROP TABLE IF EXISTS `es_brand_tag_rel`;
CREATE TABLE `es_brand_tag_rel` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `brand_id` int(8) DEFAULT NULL,
  `rel_id` int(8) DEFAULT NULL,
  `rel_name` varchar(233) DEFAULT NULL,
  `rel_logo` varchar(233) DEFAULT NULL,
  `brand_index` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/**es_goods_cat表添加颜色字段**/
alter table es_goods_cat add font_color varchar(255) default '#333';
/**es_brand 表添加关注数 张明明 2016-06-09**/
alter table es_brand add attention int(8) default 0;
/**lxy关注品牌建表**/
DROP TABLE IF EXISTS `es_attration`;
CREATE TABLE `es_attration` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `memberid` int(8) DEFAULT NULL,
  `brandid` int(8) DEFAULT NULL,
  `starttime` bigint(20) DEFAULT NULL,
  `endtime` bigint(20) DEFAULT NULL,
  `is_true` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/**es_payment_cfg 表添加app支付方式 孙新强 2016-6-12**/
INSERT INTO `es_payment_cfg` VALUES ('3', '首信易支付APP网上支付', '{\"v_moneytype\":\"0\",\"v_orderstatus\":\"1\",\"v_action_url\":\"https://pay.yizhifubj.com/prs/user_payment.checkit\",\"v_card\":\"302\",\"ajax\":\"yes\",\"v_pmode\":\"904\",\"v_signature\":\"test\",\"v_mid\":\"8819\"}', null, null, 'payeasePluginApp', '');

/**es_member 表 2016-06-17 zmm **/
alter table es_member add registype int(8) default 0;
/**给es_cart添加字段**/
alter table es_cart add is_select int(11) default 0;
/**添加秒杀活动es_allactivity活动表**/
DROP TABLE IF EXISTS `es_allactivity`;
CREATE TABLE `es_allactivity` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `index` int(8) DEFAULT NULL,
  `start_time` bigint(20) DEFAULT NULL,
  `end_time` bigint(20) DEFAULT NULL,
  `is_show` int(8) DEFAULT NULL,
  `line_color` varchar(40) DEFAULT NULL,
  `open` int(8) DEFAULT '0',
  `type` int(8) DEFAULT NULL,
  `limitbuy` int(8) DEFAULT NULL,
  `limitnumber` int(8) DEFAULT NULL,
  `virtual` int(8) DEFAULT NULL,
  `virtualcount` int(8) DEFAULT NULL,
  `bonus_select` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/**添加秒杀活动es_allactivity_product活动产品表**/
DROP TABLE IF EXISTS `es_allactivity_product`;
CREATE TABLE `es_allactivity_product` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `goods_id` int(8) DEFAULT NULL,
  `original_price` decimal(10,2) DEFAULT NULL,
  `activity_price` decimal(10,2) DEFAULT NULL,
  `count` int(10) DEFAULT NULL,
  `allactivity_id` int(8) DEFAULT NULL,
  `index` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
/**添加es_goods表字段发货地 **/
alter table es_goods add deliveryregion int(8) DEFAULT 7 ;
/**添加store_initiallist表字段是否获取初始单 **/
alter table es_store add store_initiallist int(11) default 1;





/**添加活动限购表es_check_memberlogin*/
DROP TABLE IF EXISTS `es_check_memberlogin`;
CREATE TABLE `es_check_memberlogin` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `member_id` int(9) DEFAULT NULL,
  `address_id` varchar(233) DEFAULT NULL,
  `active_id` varchar(9) DEFAULT NULL,
  `order_id` int(9) DEFAULT NULL,
  `goods_id` int(9) DEFAULT NULL,
  `nowtime` bigint(20) DEFAULT NULL,
  `is_order` int(10) DEFAULT NULL,
  `active_name` varchar(244) DEFAULT NULL,
  `between_time` varchar(244) DEFAULT NULL,
  `reason` varchar(244) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/**添加es_order_items表字段发货地 **/
alter table es_order_items add is_skill int(8) DEFAULT 0;
/**添加店铺字段信息 2016-07-06 by zmm**/
ALTER TABLE `es_store` ADD `account_manager` varchar(255) default NULL;
ALTER TABLE `es_store` ADD `account_area` int(8) default 0;
ALTER TABLE `es_store` ADD `modify_persion` varchar(255) default NULL;
ALTER TABLE `es_store` ADD `init_commission1`  decimal(20,2) DEFAULT 0; 
ALTER TABLE `es_store` ADD `init_pic` varchar(255) default NULL;
/**活动展示商品 2016-07-07 by lxy**/
INSERT INTO `es_tags` (`tag_id`, `tag_name`, `rel_count`, `store_id`, `is_parent`, `mark`, `is_groupbuy`, `name_ru`) 
	VALUES ('125', '7月1号爆款区', '0', NULL, NULL, NULL, NULL, '7月1号');
INSERT INTO `es_tags` (`tag_id`, `tag_name`, `rel_count`, `store_id`, `is_parent`, `mark`, `is_groupbuy`, `name_ru`) 
	VALUES ('126', '7月1号糖果必买区', '0', NULL, NULL, NULL, NULL, '7月1号');
INSERT INTO `es_tags` (`tag_id`, `tag_name`, `rel_count`, `store_id`, `is_parent`, `mark`, `is_groupbuy`, `name_ru`) 
	VALUES ('127', '7月1号天然杂粮区', '0', NULL, NULL, NULL, NULL, '7月1号');
INSERT INTO `es_tags` (`tag_id`, `tag_name`, `rel_count`, `store_id`, `is_parent`, `mark`, `is_groupbuy`, `name_ru`) 
	VALUES ('128', '7月1号美酒专区', '0', NULL, NULL, NULL, NULL, '7月1号');
INSERT INTO `es_tags` (`tag_id`, `tag_name`, `rel_count`, `store_id`, `is_parent`, `mark`, `is_groupbuy`, `name_ru`) 
	VALUES ('129', '7月1号首页爆款区', '0', NULL, NULL, NULL, NULL, '7月1号');
/**添加历史纪录表**/
DROP TABLE IF EXISTS `es_history`;
CREATE TABLE `es_history` (
  `id` int(5) NOT NULL auto_increment,
  `member_id` int(7) default NULL,
  `goods_id` int(8) default NULL,
  `createtime` bigint(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*2016/07/16添加es_order和es_bonus_type表字段*/
ALTER TABLE `es_order` ADD `is_bonus` int(8) default 0;
ALTER TABLE `es_bonus_type` ADD `sendplat` int(8) default 0;
/**添加优惠表**/
DROP TABLE IF EXISTS `es_check_bonus`;
CREATE TABLE `es_check_bonus` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `member_id` int(8) DEFAULT NULL,
  `address_id` varchar(244) DEFAULT NULL,
  `usetime` bigint(20) DEFAULT NULL,
  `order_id` int(8) DEFAULT NULL,
  `reason` varchar(244) DEFAULT NULL,
  `bonus_id` int(8) DEFAULT NULL,
  `bonus_name` varchar(233) DEFAULT NULL,
  `bonus_money` decimal(13,2) DEFAULT NULL,
  `min_bonus_money` decimal(12,2) DEFAULT NULL,
  `is_bonus` int(10) DEFAULT NULL,
  `is_cancle` int(10) DEFAULT NULL,
  `cancletime` bigint(20) DEFAULT NULL,
  `platform` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/**20160725添加es_register_bonus和es_register_bonus_rel表**/
DROP TABLE IF EXISTS `es_register_bonus`;
CREATE TABLE `es_register_bonus` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `name` varchar(244) DEFAULT NULL,
  `active_start_time` bigint(20) DEFAULT NULL,
  `active_end_time` bigint(20) DEFAULT NULL,
  `is_true` int(10) DEFAULT NULL,
  `active_now_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `es_register_bonus_rel`;
CREATE TABLE `es_register_bonus_rel` (
  `rel_id` int(10) NOT NULL AUTO_INCREMENT,
  `registerid` int(10) DEFAULT NULL,
  `type_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`rel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/**添加es_allactivity表字段**/
ALTER TABLE es_allactivity ADD  discountnumber  int(10) default 1 ;
/**添加member表字段 **/
ALTER TABLE es_member ADD  is_mobile  int(10) default 0 ;
/**添加es_savexml表**/
DROP TABLE IF EXISTS `es_savexml`;
CREATE TABLE `es_savexml` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `order_id` int(10) DEFAULT NULL,
  `filename` varchar(244) DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `sn` varchar(244) DEFAULT NULL,
  `is_true` int(12) DEFAULT NULL,
  `is_count` int(12) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/**添加es_goodsinformation表**/
DROP TABLE IF EXISTS `es_goodsinformation`;
CREATE TABLE `es_goodsinformation` (
  `NO` int(10) NOT NULL AUTO_INCREMENT,
  `GOODSNO` varchar(244) DEFAULT NULL,
  `GOODSNAME` varchar(244) DEFAULT NULL,
  `GOODSMODEL` varchar(244) DEFAULT NULL,
  `BARCODE` varchar(244) DEFAULT NULL,
  `CODETS` varchar(244) DEFAULT NULL,
  `COUNTRY` varchar(244) DEFAULT NULL,
  `CURRENCY` varchar(244) DEFAULT NULL,
  `UNIT` varchar(244) DEFAULT NULL,
  `QUANTITY` int(10) DEFAULT NULL,
  `PRICE` decimal(10,2) DEFAULT NULL,
  `DISCOUNT` decimal(10,2) DEFAULT NULL,
  `FLAG` varchar(244) DEFAULT NULL,
  `TAXRATE` decimal(10,2) DEFAULT NULL,
  `order_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`NO`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/**添加es_orderfeeinformation表**/
DROP TABLE IF EXISTS `es_orderfeeinformation`;
CREATE TABLE `es_orderfeeinformation` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `order_id` int(10) DEFAULT NULL,
  `CHARGE` decimal(12,2) DEFAULT NULL,
  `GOODSVALUE` decimal(12,2) DEFAULT NULL,
  `OTHERVALUE` decimal(12,2) DEFAULT NULL,
  `TAX` decimal(12,2) DEFAULT NULL,
  `CONSIGNEE` varchar(244) DEFAULT NULL,
  `CONSIGNEEADDRESS` varchar(244) DEFAULT NULL,
  `CONSIGNEETELEPHONE` varchar(244) DEFAULT NULL,
  `CONSIGNEECOUNTRY` varchar(244) DEFAULT NULL,
  `PAYMENTCODE` varchar(244) DEFAULT NULL,
  `PAYMENTNAME` varchar(244) DEFAULT NULL,
  `PAYMENTNO` varchar(244) DEFAULT NULL,
  `parent_id` int(12) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/**添加es_orderinformation表**/
DROP TABLE IF EXISTS `es_orderinformation`;
CREATE TABLE `es_orderinformation` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `CUSTOMSCODE` varchar(244) NOT NULL,
  `BIZTYPE` varchar(244) NOT NULL,
  `BIZTIME` bigint(20) NOT NULL,
  `IEFLAG` varchar(244) NOT NULL,
  `ECPCODE` varchar(244) DEFAULT NULL,
  `ECPNAME` varchar(244) DEFAULT NULL,
  `CBECODE` varchar(244) DEFAULT NULL,
  `CBENAME` varchar(244) DEFAULT NULL,
  `ORDERNO` varchar(244) DEFAULT NULL,
  `CUSTOMER` varchar(244) DEFAULT NULL,
  `CUSTOMERID` varchar(244) DEFAULT NULL,
  `NOTE` varchar(244) DEFAULT NULL,
  `order_id` int(12) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/**添加es_orderpaymentinformation表**/
DROP TABLE IF EXISTS `es_orderpaymentinformation`;
CREATE TABLE `es_orderpaymentinformation` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `order_id` int(10) DEFAULT NULL,
  `SHIPPER` varchar(244) DEFAULT NULL,
  `SHIPPERADDRESS` varchar(244) DEFAULT NULL,
  `SHIPPERTELEPHONE` varchar(244) DEFAULT NULL,
  `SHIPPERCOUNTRY` varchar(244) DEFAULT NULL,
  `LOGISTICSCODE` varchar(244) DEFAULT NULL,
  `LOGISTICSNAME` varchar(244) DEFAULT NULL,
  `LOGISTICSNO` varchar(244) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*添加后台商品列表添加关键字记录表es_metal_keywords_logs*/
DROP TABLE IF EXISTS `es_metal_keywords_logs`;
CREATE TABLE `es_metal_keywords_logs` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `admin` varchar(244) DEFAULT NULL,
  `original_keywords_ru` varchar(244) DEFAULT NULL,
  `update_keywords_ru` varchar(244) DEFAULT NULL,
  `original_metal_keywords` varchar(244) DEFAULT NULL,
  `nowtime` bigint(13) DEFAULT NULL,
  `goods_id` int(13) DEFAULT NULL,
  `name` varchar(133) DEFAULT NULL,
  `sn` varchar(122) DEFAULT NULL,
  ` goods_translation` int(13) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*在表es_hot_keyword中添加字段if_public  dengzhand 2016 09 23*/
alter table es_hot_keyword add if_public int default 0;
/*在表es_contact中添加字段if_public  dengzhand 2016 09 23*/
alter table es_contact add if_public int default 0;
/*在表es_hot_keyword中添加字段content  dengzhand 2016 09 29*/
alter table es_hot_keyword add content varchar(100) default NULL;