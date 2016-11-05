package com.enation.app.b2b2c.core.model;

import java.io.Serializable;

/**
 * 卖家自定义配送商
 * @author WKZ
 * @date 2015-9-17 下午5:56:01
 */
public class SellerExpress implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3611072385708540242L;
	
	private Integer express_id; //ID
	private String express_name; //配送商名称
	private String express_deliver_time; //预计物流时间
	private String express_storeid;//关联的店铺
	
	public Integer getExpress_id() {
		return express_id;
	}
	public void setExpress_id(Integer express_id) {
		this.express_id = express_id;
	}
	public String getExpress_name() {
		return express_name;
	}
	public void setExpress_name(String express_name) {
		this.express_name = express_name;
	}
	public String getExpress_deliver_time() {
		return express_deliver_time;
	}
	public void setExpress_deliver_time(String express_deliver_time) {
		this.express_deliver_time = express_deliver_time;
	}
	public String getExpress_storeid() {
		return express_storeid;
	}
	public void setExpress_storeid(String express_storeid) {
		this.express_storeid = express_storeid;
	}
	
	
	
}
