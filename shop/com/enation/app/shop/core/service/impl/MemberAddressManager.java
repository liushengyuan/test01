package com.enation.app.shop.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.shop.core.plugin.member.MemberPluginBundle;
import com.enation.app.shop.core.service.IMemberAddressManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.database.BaseSupport;



/**
 * 会员中心-收货地址
 * 
 * @author lzf<br/>
 *         2010-3-17 下午03:03:56<br/>
 *         version 1.0<br/>
 */
/**
 * @author sks
 *
 */
public class MemberAddressManager extends BaseSupport<MemberAddress> implements
		IMemberAddressManager {

	
	private MemberPluginBundle memberPluginBundle;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int addAddress(MemberAddress address) {
		Member member = UserConext.getCurrentMember();
		address.setMember_id(member.getMember_id());
		int addressid  = this.baseDaoSupport.getLastId("member_address");
		//System.out.println(addressid+"111");
		address.setAddr_id(addressid);
	
		//address.setType(0);
		this.baseDaoSupport.insert("member_address", address);
		int addressid2  = this.baseDaoSupport.getLastId("member_address");
		//System.out.println(addressid2+"222");
	
	//	memberPluginBundle.onAddressAdd(address);
		//Map  map =  new HashMap();
		
		//this.baseDaoSupport.insert("member_address", address);
		return addressid;
	}
	public int addAddress2(MemberAddress address) {
		Member member = UserConext.getCurrentMember();
		address.setMember_id(member.getMember_id());
		int addressid  = this.baseDaoSupport.getLastId("member_address");
		address.setAddr_id(addressid);
		address.setType(1);
		this.baseDaoSupport.insert("member_address", address);
	
	//	memberPluginBundle.onAddressAdd(address);
		//Map  map =  new HashMap();
		
		//this.baseDaoSupport.insert("member_address", address);
		return addressid;
	}

	
	public void deleteAddress(int addr_id) {
		this.baseDaoSupport.execute(
				"delete from member_address where addr_id = ?", addr_id);
	}

	
	public MemberAddress getAddress(int addr_id) {
		MemberAddress address = this.baseDaoSupport.queryForObject(
				"select * from member_address where addr_id = ?",
				MemberAddress.class, addr_id);
		return address;
	}

	
	/* (non-Javadoc)
	 * @see com.enation.app.shop.core.service.IMemberAddressManager#listAddress()
	 * 买家地址
	 */
	public List<MemberAddress> listAddress() {
		Member member = UserConext.getCurrentMember();
		List<MemberAddress> list = this.baseDaoSupport.queryForList(
				"select * from member_address where type!=1 and member_id = ?", MemberAddress.class,  member.getMember_id());
		return list;
	}
	
	/**卖家地址
	 * @return
	 */
	public List<MemberAddress> listAddress2() {
		Member member = UserConext.getCurrentMember();
		List<MemberAddress> list = this.baseDaoSupport.queryForList(
				"select * from member_address where type=1 and member_id = ?", MemberAddress.class,  member.getMember_id());
		return list;
	}

	
	public void updateAddress(MemberAddress address) {
		this.baseDaoSupport.update("member_address", address, "addr_id="+ address.getAddr_id());
	}


	@Override
	public void updateAddressDefult() {
		Member member = UserConext.getCurrentMember();
		this.baseDaoSupport.execute(
				"update member_address set def_addr = 0 where member_id = ?", member.getMember_id());
	}


	@Override
	public void addressDefult(String addr_id) {
		this.baseDaoSupport.execute(
				"update member_address set def_addr = 1 where addr_id = ?",addr_id);
	}
	
	public MemberPluginBundle getMemberPluginBundle() {
		return memberPluginBundle;
	}


	public void setMemberPluginBundle(MemberPluginBundle memberPluginBundle) {
		this.memberPluginBundle = memberPluginBundle;
	}


	@Override
	public int addressCount(int member_id) {
		return baseDaoSupport.queryForInt("select count(*) from member_address  where type!=1 and member_id=?", member_id);
	}
	public int addressCount2(int member_id) {
		return baseDaoSupport.queryForInt("select count(*) from member_address where type=1 and member_id=?", member_id);
	}


	@Override
	public MemberAddress getMemberDefault(Integer memberid) {
		String sql = "select * from es_member_address where type!=1 and member_id=? and def_addr=1 ";
		MemberAddress address = this.baseDaoSupport.queryForObject(sql, MemberAddress.class, memberid);
		return address;
	}
	@Override
	public MemberAddress getMemberDefault2(Integer memberid) {
		String sql = "select * from es_member_address where type=1 and member_id=? and def_addr=1";
		MemberAddress address = this.baseDaoSupport.queryForObject(sql, MemberAddress.class, memberid);
		return address;
	}
	@Override
	public void updateAddressDefultByType(Integer add_type) {
		Member member = UserConext.getCurrentMember();
		String sql = "UPDATE member_address SET def_addr = 0 WHERE member_id = ? AND add_type = ?";
		this.baseDaoSupport.execute(sql, member.getMember_id(), add_type);
	}
	@Override
	public List<MemberAddress> listAddressBySend() {
		Member member = UserConext.getCurrentMember();
		String sql = "select * from member_address where type=1 and member_id = ? AND add_type = 1 ";
		List<MemberAddress> list = this.baseDaoSupport.queryForList(sql, MemberAddress.class,  member.getMember_id());
		return list;
	}
	@Override
	public List<MemberAddress> listDefaultAddress() {
		Member member = UserConext.getCurrentMember();
		List<MemberAddress> list = this.baseDaoSupport.queryForList(
				"select * from member_address where type!=1 and member_id = ? and def_addr=1", MemberAddress.class,  member.getMember_id());
		return list;
	}
	//将所有卖家地址默认状态修改为0
	@Override
	public void updateAddressByMer() {
		Member member = UserConext.getCurrentMember();
		String sql = "UPDATE member_address SET def_addr = 0 WHERE member_id = ? ";
		this.baseDaoSupport.execute(sql, member.getMember_id());
		
	}
	
	

}
