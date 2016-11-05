package com.enation.app.shop.core.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.core.model.PointConsume;
import com.enation.app.shop.core.model.RegisterSendPoint;
import com.enation.framework.database.Page;
/**
 * TODO
 * 积分消费管理类
 * @author WKZ
 * @date 2015-9-1 下午2:09:22
 */
public interface IPointConsumeManager {
	/**
	 * 查询积分消费分页
	 * @author WKZ
	 */
	public Page getConsumePage(Integer pageNo, Integer pageSize);
	
	/**
	 * 获取单个消费积分实体
	 * @author WKZ
	 */
	public PointConsume getSingleConsume(Integer id);
	
	/**
	 * 修改积分抵押金额
	 * @author WKZ
	 */
	public void modifyAcount(PointConsume consume);

	
	/**
	 * 添加积分消费
	 * @author WKZ
	 */
	public void addAcount(PointConsume consume);
	
	/**
	 * 删除积分抵押
	 * @author WKZ
	 */
	public void deleteAcount(Integer[] id);
	
	/**
	 * 是否唯一币种
	 * @author WKZ
	 */
	public boolean isOnlyCurrency(String currencyName);
	
	/**
	 * 查询积分消费分页
	 * @author jfw
	 */
	public Page getRegisterPoint(Integer pageNo, Integer pageSize);
	
	/**
	 * 添加注册赠送积分规则
	 * @author jfw
	 */
	public void addRegisterSendPoint(RegisterSendPoint registerSendPoint);
	/**
	 * 获取注册赠送积分规则
	 * @author jfw
	 */
	public RegisterSendPoint get(Integer id);
	/**
	 * 修改注册赠送积分规则
	 * @author jfw
	 */
	public void updateRegisterSendPoint(RegisterSendPoint registerSendPoint);
	/**
	 * 删除注册赠送积分规则
	 * @author jfw
	 */
	@Transactional(propagation = Propagation.REQUIRED) 
	public void deleteRegisterSendPoint(Integer id);
	/**
	 * 检验现有,启用的的注册赠送积分规则状态的个数
	 * @author jfw
	 */
	public Integer checkOutStatus();
	
	/**
	 * 获取人民币下的对象，主要是获取积分比值
	 * @return
	 */
	public PointConsume getCNYconsume();

	/**
	 * 获取俄文下的对象，主要是获取积分比值
	 * @return
	 */
	public PointConsume getRUBconsume();

}
