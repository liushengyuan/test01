package com.enation.app.shop.core.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.MemberAddress;

/**
 * 会员中心-接收地址
 * @author lzf<br/>
 * 2010-3-17 下午02:49:23<br/>
 * version 1.0<br/>
 */
public interface IMemberAddressManager {
	
	/**
	 * 列表接收地址
	 * @return
	 */
	public List<MemberAddress> listAddress();
	
	/**
	 * 取得地址详细信息
	 * @param addr_id
	 * @return
	 */
	public MemberAddress getAddress(int addr_id);
	/**
	 * 添加接收地址
	 * @param address 接收地址
	 * @return 接收地址Id
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int addAddress(MemberAddress address);
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int addAddress2(MemberAddress address);
	/**
	 * 修改接收地址
	 * @param address
	 */
	public void updateAddress(MemberAddress address);
	/**
	 * 修改会员默认收货地址
	 */
	public void updateAddressDefult();
	/**
	 * 修改会员默认收货地址
	 * @param addr_id 收货地址Id
	 */
	public void addressDefult(String addr_id);
	/**
	 * 删除会员默认收货地址
	 * @param addr_id 收货地址Id
	 */
	public void deleteAddress(int addr_id);
	/**
	 * 显示会员有杜少的收货地址
	 * @param member_id 会员Id
	 * @return
	 */
	public int addressCount(int member_id);
	/**
	 * 获取会员的默认收货地址
	 * @param memberid 会员Id
	 * @return MemberAddress
	 */
	public MemberAddress getMemberDefault(Integer memberid);

	public List<MemberAddress> listAddress2();

	public  MemberAddress getMemberDefault2(Integer memberid);

	public  int addressCount2(int member_id);
	
	/**
	 * 卖家根据发退货地址  更新默认地址
	 * @author WKZ
	 */
	public void updateAddressDefultByType(Integer add_type);
	
	/**
	 * 卖家发货地址列表
	 * @author WKZ
	 */
	public List<MemberAddress> listAddressBySend();
	//获得买家的默认地址
	public List<MemberAddress> listDefaultAddress();
	//将所有卖家地址默认状态修改为0
	public void updateAddressByMer();
}
