package com.enation.app.shop.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.core.model.PointConsume;
import com.enation.app.shop.core.model.RegisterSendPoint;
import com.enation.app.shop.core.service.IPointConsumeManager;
import com.enation.eop.sdk.database.BaseSupport;


import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

/**
 * 会员积分抵押管理
 * @author WKZ
 * @date 2015-9-4 上午8:22:04
 */
public class PointConsumeManager extends BaseSupport implements IPointConsumeManager {

	@Override
	public Page getConsumePage(Integer pageNo, Integer pageSize) {
		String sql = " SELECT * FROM point_consume ";
		Page page = this.baseDaoSupport.queryForPage(sql, pageNo, pageSize);
		return page;
	}
	
	@Override
	public PointConsume getSingleConsume(Integer id) {
		String sql = " SELECT * FROM point_consume WHERE consume_id = ? ";
		PointConsume consume = (PointConsume) this.baseDaoSupport.queryForObject(sql, PointConsume.class, id);
		return consume;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void modifyAcount(PointConsume consume) {
		if (consume == null)
			throw new NullPointerException();
		String sql = " UPDATE point_consume SET consume_amount = ? WHERE consume_id = ? ";
		this.baseDaoSupport.execute(sql, consume.getConsume_amount(), consume.getConsume_id());
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void addAcount(PointConsume consume) {
		Map<String,Object> fields = new HashMap<String, Object>();
		fields.put("consume_amount", consume.getConsume_amount());
		fields.put("consume_currency", consume.getConsume_currency());
		fields.put("consume_num", consume.getConsume_num());
		this.baseDaoSupport.insert("point_consume", fields);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteAcount(Integer[] ids) {
		if (ids == null || ids.equals(""))
			return;
		String id_str = StringUtil.arrayToString(ids, ",");
		String sql = "delete from point_consume where consume_id in (" + id_str
				+ ")";
		this.baseDaoSupport.execute(sql);
	}

	@Override
	public boolean isOnlyCurrency(String currencyName) {
		if(StringUtil.isEmpty(currencyName)) {
			throw new RuntimeException("币种名称不能为空");
		}
		String sql = "SELECT count(1) FROM point_consume WHERE consume_currency IN (?,'人民币','卢布')";
		int result = this.baseDaoSupport.queryForInt(sql, currencyName);
		if(result == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public Page getRegisterPoint(Integer pageNo, Integer pageSize) {
		String sql = "SELECT * FROM es_register_point";
		Page page = this.baseDaoSupport.queryForPage(sql, pageNo, pageSize);
		return page;
	}

	@Override
	public void addRegisterSendPoint(RegisterSendPoint registerSendPoint) {
		this.baseDaoSupport.insert("es_register_point", registerSendPoint);		
	}

	@Override
	public RegisterSendPoint get(Integer id) {
		String sql = "SELECT * FROM es_register_point where id = ?";
		RegisterSendPoint registerSendPoint = (RegisterSendPoint) this.baseDaoSupport.queryForObject(sql, RegisterSendPoint.class, id);
		return registerSendPoint;
	}

	@Override
	public void updateRegisterSendPoint(RegisterSendPoint registerSendPoint) {
		this.baseDaoSupport.update("es_register_point", registerSendPoint," id="+registerSendPoint.getId());		
	}

	@Override
	public void deleteRegisterSendPoint(Integer id) {
		this.baseDaoSupport.execute("delete from es_register_point where id=?", id);
	}

	@Override
	public Integer checkOutStatus() {
		String sql = "SELECT COUNT(*) FROM es_register_point WHERE status = 1 ";
		return this.baseDaoSupport.queryForInt(sql);
	}

	/**
	 * 获取中文下的积分与价格的比值
	 */
	@Override
	public PointConsume getCNYconsume() {
		String sql = " SELECT * FROM point_consume WHERE consume_currency=? ";
		List<PointConsume>consumeList = this.baseDaoSupport.queryForList(sql, PointConsume.class, "CNY");
		if(consumeList.size()>0){
			return consumeList.get(0);
		}else{
			return null;
		}
	}

	/**
	 * 获取俄文下的基本与与价格的比值
	 */
	@Override
	public PointConsume getRUBconsume() {
		String sql = " SELECT * FROM point_consume WHERE consume_currency=? ";
		List<PointConsume>consumeList = this.baseDaoSupport.queryForList(sql, PointConsume.class, "RUB");
		if(consumeList.size()>0){
			return consumeList.get(0);
		}else{
			return null;
		}
	}
}
