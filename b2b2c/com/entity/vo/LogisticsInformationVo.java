package com.entity.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author zks
 * 
 */
public class LogisticsInformationVo {
	/** 订单id */
	private String order_id;
	/** 仓库id */
	private String warehouse_id;
	/** 引用单号，一般为客户单号 */
	private String referenceNumber;
	/** 服务商跟踪号码 */
	private String trackingNumber;
	/** 物流信息创建实时间 */
	private Timestamp createtime;
	/** 俄速通确认收货时间 */
	private Timestamp cofirmtime;
	/** 物流费用 */
	private BigDecimal amount;
	/** 状态 */
	private String status;

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getWarehouse_id() {
		return warehouse_id;
	}

	public void setWarehouse_id(String warehouse_id) {
		this.warehouse_id = warehouse_id;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Date getCofirmtime() {
		return cofirmtime;
	}

	public void setCofirmtime(Timestamp cofirmtime) {
		this.cofirmtime = cofirmtime;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
