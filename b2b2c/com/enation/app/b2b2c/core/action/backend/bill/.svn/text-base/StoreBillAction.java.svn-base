package com.enation.app.b2b2c.core.action.backend.bill;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.bill.BillDetail;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.bill.IBillManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.framework.action.WWAction;

/**
 * 结算管理
 * @author fenlongli
 *
 */
@Component
@ParentPackage("eop_default")
@Namespace("/b2b2c/admin")
@Results({
	 @Result(name="list",type="freemarker", location="/b2b2c/admin/bill/list.html"),
	 @Result(name="detail_list",type="freemarker", location="/b2b2c/admin/bill/detail_list.html"),
	 @Result(name="detail",type="freemarker", location="/b2b2c/admin/bill/detail.html")
})
@Action("storeBill")
public class StoreBillAction extends WWAction{
	private Integer id;
	private Integer bill_id;
	private Store store;
	private BillDetail billDetail;
	private IBillManager billManager;
	private IStoreManager storeManager;
	/**
	 * 结算单列表
	 * @return
	 */
	public String list(){
		return "list";
	}
	/**
	 * 获取结算列表JSON
	 * @return
	 */
	public String list_json(){
		this.webpage=billManager.bill_list(this.getPage(), this.getPageSize());
		this.showGridJson(this.webpage);
		return this.JSON_MESSAGE;
	}
	/**
	 * 结算详细列表
	 * @return 结算详细列表页
	 */
	public String detail_list(){
		return "detail_list";
	}
	/**
	 * 获取结算详细列表JSON
	 * 
	 * @return 结算详细列表页JSON
	 */
	public String detail_list_json(){
		this.webpage=billManager.bill_detail_list( this.getPage(), this.getPageSize(),bill_id);
		this.showGridJson(this.webpage);
		return this.JSON_MESSAGE;
	}
	/**
	 * 获取结算单详细
	 * @return
	 */
	public String detail(){
		billDetail=this.billManager.get_bill_detail(id);
		store=storeManager.getStore(billDetail.getStore_id());
		return "detail";
	}
	public Integer getBill_id() {
		return bill_id;
	}
	public void setBill_id(Integer bill_id) {
		this.bill_id = bill_id;
	}
	public IBillManager getBillManager() {
		return billManager;
	}
	public void setBillManager(IBillManager billManager) {
		this.billManager = billManager;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public BillDetail getBillDetail() {
		return billDetail;
	}
	public void setBillDetail(BillDetail billDetail) {
		this.billDetail = billDetail;
	}
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	public IStoreManager getStoreManager() {
		return storeManager;
	}
	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}
}
