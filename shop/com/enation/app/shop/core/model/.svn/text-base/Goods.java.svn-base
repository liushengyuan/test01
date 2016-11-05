package com.enation.app.shop.core.model;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 商品实体
 * 
 * @author kingapex 2010-4-25下午09:40:24
 */
public class Goods implements java.io.Serializable {

	private Integer goods_id;
	private String name;
	private String sn;
	private Integer brand_id;
	private Integer cat_id;
	private Integer type_id;
	private String goods_type; // enum('normal', 'bind') default 'normal'
	private String unit;
	private Double weight;
	private Integer market_enable;//0下架，1上架，2添加商品，-1保存草稿状态,4为审核未通过已删除5.已下架删除
	// private String image_default;
	// private String image_file;
	private String thumbnail;
	private String big;
	private String small;
	private String original;
	private String brief;
	private String intro;
	private Double price;
	private Double mktprice;
	private Integer store;
	private String adjuncts;
	private String params;
	private String specs;
	private Long create_time;
	private Long last_modify;
	private Integer view_count;
	private Integer buy_count;
	private Integer disabled;
	private String page_title;
	private String meta_keywords;
	private String meta_description;
	private Integer point; // 积分
	private Integer sord;
	
	private Integer goods_translation;//商品是否被翻译。0为未翻译，1为已经翻译
	private String goods_russion;//商品对应的俄文名称
	
	private String ru_keyword;//俄文关键字
	private String china_keyword;//中文关键字
	private String ru_content;//俄文详细内容
	/**
	 * 商品状态描述：
	 * 商品已上架：audit_status 1 market_enable 1
	 * 商品待审核(商品发布)：audit_status 0 market_enable 2
	 * 商品审核未通过：audit_status 2 market_enable -1
	 * 商品已下架：audit_status 1 market_enable 0
	 * 商品草稿：audit_status 0 market_enable -1
	 * 商品审核未通过已删除：audit_status 4 market_enable 4
	 * 商品已下架删除：audit_status 5 market_enable 5
	 * 商品编辑状态：audit_status 2 market_enable -1
	 * 商品规格审核通过：audit_status 3 market_enable -1
	 * 商品规格审核不通过：audit_status 2 market_enable -1
	 */
	private Integer audit_status;//0为待审核，1为通过，2为不通过，3为符合要求，4为审核未通过已删除5.已下架删除，-1为草稿
	private String audit_discribe;
	private String name_ru; //商品俄文名称
	private Double original_price;//商品原始价格
	private Double original_price_ru;//商品原始卢布价格
	private Integer enable_store;//商品的可用库存
	/**
	 * 运费类型  1.包邮  0.自定义运费  //2.平台物流运费计算
	 */
	private Integer freightType;
	private Double freight;//为自定义运费时的运费值中文
	private Double freightru;//为自定义运费时的运费值俄文
	public Double getFreightru() {
		return freightru;
	}

	public void setFreightru(Double freightru) {
		this.freightru = freightru;
	}

	public String getName_ru() {
		return name_ru;
	}

	public void setName_ru(String name_ru) {
		this.name_ru = name_ru;
	}

	public String getRu_keyword() {
		return ru_keyword;
	}

	public void setRu_keyword(String ru_keyword) {
		this.ru_keyword = ru_keyword;
	}

	public String getChina_keyword() {
		return china_keyword;
	}

	public void setChina_keyword(String china_keyword) {
		this.china_keyword = china_keyword;
	}

	public String getRu_content() {
		return ru_content;
	}

	public void setRu_content(String ru_content) {
		this.ru_content = ru_content;
	}

	public Integer getGoods_translation() {
		return goods_translation;
	}

	public void setGoods_translation(Integer goods_translation) {
		this.goods_translation = goods_translation;
	}

	public String getGoods_russion() {
		return goods_russion;
	}

	public void setGoods_russion(String goods_russion) {
		this.goods_russion = goods_russion;
	}

	public Integer getBrand_id() {
		if (brand_id == null)
			brand_id = 0;
		return brand_id;
	}

	public void setBrand_id(Integer brand_id) {
		this.brand_id = brand_id;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public Integer getBuy_count() {
		return buy_count;
	}

	public void setBuy_count(Integer buy_count) {
		this.buy_count = buy_count;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	@PrimaryKeyField
	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	// public String getImage_default() {
	// return image_default;
	// }
	// public void setImage_default(String image_default) {
	// this.image_default = image_default;
	// }
	// public String getImage_file() {
	// return image_file;
	// }
	// public void setImage_file(String image_file) {
	// this.image_file = image_file;
	// }

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getBig() {
		return big;
	}

	public void setBig(String big) {
		this.big = big;
	}

	public String getSmall() {
		return small;
	}

	public void setSmall(String small) {
		this.small = small;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Integer getMarket_enable() {
		return market_enable;
	}

	public void setMarket_enable(Integer market_enable) {
		this.market_enable = market_enable;
	}

	public String getMeta_description() {
		return meta_description;
	}

	public void setMeta_description(String meta_description) {
		this.meta_description = meta_description;
	}

	public String getMeta_keywords() {
		return meta_keywords;
	}

	public void setMeta_keywords(String meta_keywords) {
		this.meta_keywords = meta_keywords;
	}

	public Double getMktprice() {
		return mktprice;
	}

	public void setMktprice(Double mktprice) {
		this.mktprice = mktprice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPage_title() {
		return page_title;
	}

	public void setPage_title(String page_title) {
		this.page_title = page_title;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Integer getType_id() {
		return type_id;
	}

	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}

	public String getGoods_type() {
		return goods_type;
	}

	public void setGoods_type(String goodsType) {
		goods_type = goodsType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getView_count() {
		return view_count;
	}

	public void setView_count(Integer view_count) {
		this.view_count = view_count;
	}

	public Double getWeight() {
		weight = weight == null ? weight = 0D : weight;
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Integer getCat_id() {
		return cat_id;
	}

	public void setCat_id(Integer cat_id) {
		this.cat_id = cat_id;
	}

	public Integer getStore() {
		return store;
	}

	public void setStore(Integer store) {
		this.store = store;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public Long getLast_modify() {
		return last_modify;
	}

	public void setLast_modify(Long last_modify) {
		this.last_modify = last_modify;
	}

	public String getSpecs() {
		return specs;
	}

	public void setSpecs(String specs) {
		this.specs = specs;
	}

	public String getAdjuncts() {
		return adjuncts;
	}

	public void setAdjuncts(String adjuncts) {
		this.adjuncts = adjuncts;
	}

	public Integer getPoint() {
		point = point == null ? 0 : point;
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public Integer getSord() {
		return sord;
	}

	public void setSord(Integer sord) {
		this.sord = sord;
	}

	public Integer getAudit_status() {
		return audit_status;
	}

	public void setAudit_status(Integer audit_status) {
		this.audit_status = audit_status;
	}

	public String getAudit_discribe() {
		return audit_discribe;
	}

	public void setAudit_discribe(String audit_discribe) {
		this.audit_discribe = audit_discribe;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(Double original_price) {
		this.original_price = original_price;
	}

	public Double getOriginal_price_ru() {
		return original_price_ru;
	}

	public void setOriginal_price_ru(Double original_price_ru) {
		this.original_price_ru = original_price_ru;
	}

	public Integer getEnable_store() {
		return enable_store;
	}

	public void setEnable_store(Integer enable_store) {
		this.enable_store = enable_store;
	}

	public Integer getFreightType() {
		return freightType;
	}

	public void setFreightType(Integer freightType) {
		this.freightType = freightType;
	}
}