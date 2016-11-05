package com.enation.app.shop.core.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.enation.framework.util.HashCrypt;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberLv;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.component.gift.api.Eventmessage;
import com.enation.app.shop.core.model.Attration;
import com.enation.app.shop.core.model.Cart;
import com.enation.app.shop.core.model.LogisModel;
import com.enation.app.shop.core.plugin.member.MemberPluginBundle;
import com.enation.app.shop.core.service.IAllianceCountManager;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IMemberLvManager;
import com.enation.app.shop.core.service.IMemberPointManger;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.database.BaseSupport;


import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 会员管理
 * 
 * @author kingapex 2010-4-30上午10:07:24
 */
@SuppressWarnings({ "rawtypes", "unchecked"})
public class MemberManager extends BaseSupport implements IMemberManager {
	private IAllianceCountManager AllianceCountManager;
	protected IMemberLvManager memberLvManager;
	private IMemberPointManger memberPointManger;
	private MemberPluginBundle memberPluginBundle;
	private ICartManager cartManager;
	@Transactional(propagation = Propagation.REQUIRED)
	public void logout() {
		Member member = UserConext.getCurrentMember();
		member = this.get(member.getMember_id());
		ThreadContextHolder.getSessionContext().removeAttribute(UserConext.CURRENT_MEMBER_KEY);
		this.memberPluginBundle.onLogout(member);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int register(Member member) {
		int result = add(member);
		try {
			this.memberPluginBundle.onRegister(member);
			this.updateCartforMember(member.getMember_id());//更新购物车，使得保存购物车信息保留历史记录
			//ThreadContextHolder.getSessionContext().setAttribute(UserConext.CURRENT_MEMBER_KEY, member);//注册后立即登录
		} catch (Exception e) {
			System.out.println(e);
		}

		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int add(Member member) {
		if (member == null)
			throw new IllegalArgumentException("member is null");
		if (member.getUname() == null)
			throw new IllegalArgumentException("member' uname is null");
		if (member.getPassword() == null)
			throw new IllegalArgumentException("member' password is null");
//		if (member.getEmail() == null)
//			throw new IllegalArgumentException("member'email is null");

		if (this.checkname(member.getUname()) == 1) {
			return 0;
		}

		Integer lvid = memberLvManager.getDefaultLv();
		member.setLv_id(lvid);
		if(member.getName()==null)
			member.setName(member.getUname());

		member.setPoint(0);
		member.setAdvance(0D);
		
		if(member.getRegtime()==null){
			member.setRegtime(DateUtil.getDateline());
		}
		
		member.setLastlogin(DateUtil.getDateline());
		member.setLogincount(0);
//		member.setPassword(StringUtil.md5(member.getPassword()));
		member.setPassword(HashCrypt.cryptUTF8("SHA", null, member.getPassword()));

		// Dawei Add
		member.setMp(0);
		member.setFace("");
		member.setMidentity(0);
		//ljs
		//member.setIs_cheked(1);//默认注册之后，即审核过，之后再添加邮件激活时再改

		this.baseDaoSupport.insert("member", member);
		int memberid = this.baseDaoSupport.getLastId("member");
		member.setMember_id(memberid);

		return 1;
	}

	public void checkEmailSuccess(Member member) {
		int memberid = member.getMember_id();

		String sql = "update member set is_cheked = 1 where member_id =  " + memberid;
		this.baseDaoSupport.execute(sql);
		this.memberPluginBundle.onEmailCheck(member);
	}

	public Member get(Integer memberId) {
		String sql = "select m.*,l.name as lvname from "
				+ this.getTableName("member") + " m left join "
				+ this.getTableName("member_lv")
				+ " l on m.lv_id = l.lv_id where member_id=?";
		Member m = (Member) this.daoSupport.queryForObject(sql, Member.class, memberId);
		return m;
	}
	public Member getmember(Integer memberId){
		String sql="select * from es_member where member_id= ?";
		Member m = (Member) this.daoSupport.queryForObject(sql, Member.class, memberId);
		return m;		
	}

	public Member getMemberByUname(String uname) {
		String sql = "select * from es_member where uname=?";
		List list = this.baseDaoSupport.queryForList(sql, Member.class, uname);
		Member m = null;
		if (list != null && list.size() > 0) {
			m = (Member) list.get(0);
		}
		return m;
	}

	public Member getMemberByEmail(String email) {
		String sql = "select * from member where email=?";
		List list = this.baseDaoSupport.queryForList(sql, Member.class, email);
		Member m = null;
		if (list != null && list.size() > 0) {
			m = (Member) list.get(0);
		}
		return m;
	}

	public Member edit(Member member) {
		// 前后台用到的是一个edit方法，请在action处理好
		this.baseDaoSupport.update("member", member, "member_id=" + member.getMember_id());
		Integer memberpoint = member.getPoint();
		
		//改变会员等级
		if(memberpoint!=null ){
			MemberLv lv =  this.memberLvManager.getByPoint(memberpoint);
			if(lv!=null ){
				if((member.getLv_id()==null ||lv.getLv_id().intValue()>member.getLv_id().intValue())){
					this.updateLv(member.getMember_id(), lv.getLv_id());
				} 
			}
		}
		
		ThreadContextHolder.getSessionContext().setAttribute(UserConext.CURRENT_MEMBER_KEY, member);
		return null;
	}

	public int checkname(String name) {
		String sql = "select count(0) from member where uname=?";
		int count = this.baseDaoSupport.queryForInt(sql, name);
		count = count > 0 ? 1 : 0;
		return count;
	}

	public int checkemail(String email,int member_id) {
		String sql = "select count(*) from es_member where member_id!="+member_id+" and email=? ";
		int result = this.daoSupport.queryForInt(sql,email);
		return result> 0 ? 1 : 0;
	}
	
	@Override
	public int checkMobile(String mobile,int member_id) {
		String sql = "select count(*) from es_member where member_id!="+member_id+" and mobile=? ";
		int result = this.daoSupport.queryForInt(sql,mobile);
		return result> 0 ? 1 : 0;
	}

	public void delete(Integer[] id) {
		if (id == null || id.equals(""))
			return;
		String id_str = StringUtil.arrayToString(id, ",");
		String sql = "delete from member where member_id in (" + id_str + ")";
		this.baseDaoSupport.execute(sql);
	}

	public void updatePassword(String password) {
		Member member = UserConext.getCurrentMember();
		this.updatePassword(member.getMember_id(), password);
//		member.setPassword(StringUtil.md5(password));
		member.setPassword(HashCrypt.cryptUTF8("SHA", null, password));
		ThreadContextHolder.getSessionContext().setAttribute(UserConext.CURRENT_MEMBER_KEY, member);

	}

	public void updatePassword(Integer memberid, String password) {
		String md5password = password == null ? HashCrypt.cryptUTF8("SHA", null, "") : HashCrypt.cryptUTF8("SHA", null, password);
		String sql = "update member set password = ? where member_id =? ";
		this.baseDaoSupport.execute(sql, md5password, memberid);
		this.memberPluginBundle.onUpdatePassword(password, memberid);
	}

	public void updateEmail(Integer memberid, String email) {
		String sql = "update member set email = ? where member_id =? ";
		this.baseDaoSupport.execute(sql, email, memberid);
	}

	public void updateFindCode(Integer memberid, String code) {
		String sql = "update member set find_code = ? where member_id =? ";
		this.baseDaoSupport.execute(sql, code, memberid);
	}
	//用户名登陆
	public String getUnameByName(String name){
		String sql="select uname from es_member where name='"+name+"'";
		return  this.baseDaoSupport.queryForString(sql);
		
	}
	
	
	public String getUnameByEmail(String name){
		String sql="select uname from es_member where email=?";
		List<Map> list = this.baseDaoSupport.queryForList(sql, name);
		String uname = "";
		if(list.size()>0){
			uname = list.get(list.size()-1).get("uname").toString();
		}
		return uname;
		
	}
	
	public String getUnameByMobile(String name){
		String sql="select uname from es_member where mobile=?";
		List<Map> list = this.baseDaoSupport.queryForList(sql, name);
		String uname = "";
		if(list.size()>0){
			uname = list.get(list.size()-1).get("uname").toString();
		}
		return uname;
		 
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int login(String username, String password) {
		String sql = "select m.*,l.name as lvname from "
				+ this.getTableName("member") + " m left join "
				+ this.getTableName("member_lv")
				+ " l on m.lv_id = l.lv_id where m.uname=?";
		// 用户名中包含@，说明是用邮箱登录的
/*		if (username.contains("@")) {
			sql = "select m.*,l.name as lvname from "
					+ this.getTableName("member") + " m left join "
					+ this.getTableName("member_lv")
					+ " l on m.lv_id = l.lv_id where m.email=? and password=?";
		}*/

//		String pwdmd5 = com.enation.framework.util.StringUtil.md5(password);
//		String pwdsha = HashCrypt.cryptUTF8("SHA" , null , password);
		List < Member > list = this.daoSupport.queryForList(sql, Member.class, username);
		if (list == null || list.isEmpty()) {
			return 0;
		}

		Member member = list.get(0);
		if(!checkPassword(member.getPassword() , password)){
			return 0;
		}
		if(member.getIs_cheked()==1){
			long ldate = ((long) member.getLastlogin()) * 1000;
			Date date = new Date(ldate);
			Date today = new Date();
			int logincount = member.getLogincount();
			if (DateUtil.toString(date, "yyyy-MM").equals(DateUtil.toString(today, "yyyy-MM"))) {// 与上次登录在同一月内
				logincount++;
			} else {
				logincount = 1;
			}
			Long upLogintime = member.getLastlogin();// 登录积分使用
			member.setLastlogin(DateUtil.getDateline());
			member.setLogincount(logincount);
			this.edit(member);
			ThreadContextHolder.getSessionContext().setAttribute(UserConext.CURRENT_MEMBER_KEY, member);
			//HttpCacheManager.sessionChange();
			this.updateCartforMember(member.getMember_id());//更新购物车，使得保存购物车信息保留历史记录
			this.memberPluginBundle.onLogin(member, upLogintime);
			return 1;
		}else{
			return -1;
		}

	}
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateCartforMember(Integer memberId){
		String sessionid = ThreadContextHolder.getHttpRequest().getSession().getId();
		List<Cart>cartlist = this.cartManager.queryCartlist(sessionid);
		if(cartlist!=null && cartlist.size()>0){
			this.updateCart(sessionid,memberId);
		}
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateCart(String sessiongid,Integer memberId){
		StringBuffer sql = new StringBuffer();
		sql.append(" update es_cart set member_id=? where session_id=? ");
		this.baseDaoSupport.execute(sql.toString(),memberId,sessiongid);
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public int loginWithCookie(String username, String password) {
		String sql = "select m.*,l.name as lvname from "
				+ this.getTableName("member") + " m left join "
				+ this.getTableName("member_lv")
				+ " l on m.lv_id = l.lv_id where m.uname=? ";
		// 用户名中包含@，说明是用邮箱登录的
		if (username.contains("@")) {
			sql = "select m.*,l.name as lvname from "
					+ this.getTableName("member") + " m left join "
					+ this.getTableName("member_lv")
					+ " l on m.lv_id = l.lv_id where m.email=? ";
		}
		List<Member> list = this.daoSupport.queryForList(sql, Member.class,	username);
		if (list == null || list.isEmpty()) {
			return 0;
		}
		
		Member member = list.get(0);
		if(!checkPassword(member.getPassword() , password)){
			return 0;
		}
		long ldate = ((long) member.getLastlogin()) * 1000;
		Date date = new Date(ldate);
		Date today = new Date();
		int logincount = member.getLogincount();
		if (DateUtil.toString(date, "yyyy-MM").equals(DateUtil.toString(today, "yyyy-MM"))) {// 与上次登录在同一月内
			logincount++;
		} else {
			logincount = 1;
		}
		Long upLogintime = member.getLastlogin();// 登录积分使用
		member.setLastlogin(DateUtil.getDateline());
		member.setLogincount(logincount);
		this.edit(member);
		ThreadContextHolder.getSessionContext().setAttribute(UserConext.CURRENT_MEMBER_KEY, member);

		this.memberPluginBundle.onLogin(member, upLogintime);

		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		HttpSession session = request.getSession();
		//判断该session_id是否存在，存在则把member_id存入es_flow_count
		if(AllianceCountManager.haveSession(session.getId()).size() > 0){
			AllianceCountManager.editMember_id(username, session.getId());
		}
		return 1;
	}

	/**
	 * 系统管理员作为某个会员登录
	 */
	public int loginbysys(String username) {
		 
		if (UserConext.getCurrentAdminUser()==null) {
			throw new RuntimeException("您无权进行此操作，或者您的登录已经超时");
		}

		String sql = "select m.*,l.name as lvname from "
				+ this.getTableName("member") + " m left join "
				+ this.getTableName("member_lv")
				+ " l on m.lv_id = l.lv_id where m.uname=?";
		List<Member> list = this.daoSupport.queryForList(sql, Member.class,	username);
		if (list == null || list.isEmpty()) {
			return 0;
		}

		Member member = list.get(0);
		ThreadContextHolder.getSessionContext().setAttribute(UserConext.CURRENT_MEMBER_KEY, member);
//		HttpCacheManager.sessionChange();
		return 1;
	}


	public void addMoney(Integer memberid, Double num) {
		String sql = "update member set advance=advance+? where member_id=?";
		this.baseDaoSupport.execute(sql, num, memberid);

	}

	public void cutMoney(Integer memberid, Double num) {
		Member member = this.get(memberid);
		if (member.getAdvance() < num) {
			throw new RuntimeException("预存款不足:需要[" + num + "],剩余["
					+ member.getAdvance() + "]");
		}
		String sql = "update member set advance=advance-? where member_id=?";
		this.baseDaoSupport.execute(sql, num, memberid);
	}
	

	@Override
	public Page searchMember(Map memberMap, Integer page, Integer pageSize,String other,String order) {
		String sql = createTemlSql(memberMap);
		sql+=" order by "+other+" "+order;
//		System.out.println(sql);
		Page webpage = this.daoSupport.queryForPage(sql, page, pageSize);
		
		return webpage;
	}
	
	@Override
	public List<Member> search(Map memberMap) {
		String sql = createTemlSql(memberMap);
		return this.baseDaoSupport.queryForList(sql, Member.class);
	}
	
	public void updateLv(int memberid, int lvid) {
		String sql = "update member set lv_id=? where member_id=?";
		this.baseDaoSupport.execute(sql, lvid, memberid);
	}
	
	
	private String createTemlSql(Map memberMap){

		Integer stype = (Integer) memberMap.get("stype");
		String keyword = (String) memberMap.get("keyword");
		String uname =(String) memberMap.get("uname");
		Integer mobile = (Integer) memberMap.get("mobile");
		Integer  lv_id = (Integer) memberMap.get("lvId");
		String email = (String) memberMap.get("email");
		String start_time = (String) memberMap.get("start_time");
		String end_time = (String) memberMap.get("end_time");
		Integer sex = (Integer) memberMap.get("sex");
	
		
		Integer province_id = (Integer) memberMap.get("province_id");
		Integer city_id = (Integer) memberMap.get("city_id");
		Integer region_id = (Integer) memberMap.get("region_id");
		Integer is_store=(Integer)memberMap.get("is_store");
		
		String sql = "select m.*,lv.name as lv_name from "
			+ this.getTableName("member") + " m left join "
			+ this.getTableName("member_lv")
			+ " lv on m.lv_id = lv.lv_id where 1=1 ";
		
		if(stype!=null && keyword!=null){			
			if(stype==0){
				sql+=" and (m.uname like '%"+keyword+"%'";
				sql+=" or m.mobile like '%"+keyword+"%')";
			}
		}
		
		if(lv_id!=null && lv_id!=0){
			sql+=" and m.lv_id="+lv_id;
		}
		
		if (uname != null && !uname.equals("")) {
			sql += " and m.name like '%" + uname + "%'";
			sql += " or m.uname like '%" + uname + "%'";
		}
		if(mobile!=null){
			sql += " and m.mobile like '%" + mobile + "%'";
		}
		
		if(email!=null && !StringUtil.isEmpty(email)){
			sql+=" and m.email = '"+email+"'";
		}
		
		if(sex!=null&&sex!=2){
			sql+=" and m.sex = "+sex;
		}
		
		if(start_time!=null&&!StringUtil.isEmpty(start_time)){			
			long stime = DateUtil.getDateline(start_time+" 00:00:00");
			sql+=" and m.regtime>"+stime;
		}
		if(end_time!=null&&!StringUtil.isEmpty(end_time)){			
			long etime = DateUtil.getDateline(end_time +" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql+=" and m.regtime<"+etime;
		}
		if(province_id!=null&&province_id!=0){
			sql+=" and province_id="+province_id;
		}
		if(city_id!=null&&city_id!=0){
			sql+=" and city_id="+city_id;
		}
		if(region_id!=null&&region_id!=0){
			sql+=" and region_id="+region_id;
		}
		if(is_store!=null){
			sql+="and is_store="+is_store;
		}
		
		return sql;
	}
	
	//setter getter
	
	public IMemberPointManger getMemberPointManger() {
		return memberPointManger;
	}

	public MemberPluginBundle getMemberPluginBundle() {
		return memberPluginBundle;
	}

	public void setMemberPluginBundle(MemberPluginBundle memberPluginBundle) {
		this.memberPluginBundle = memberPluginBundle;
	}

	public IMemberLvManager getMemberLvManager() {
		return memberLvManager;
	}

	public void setMemberLvManager(IMemberLvManager memberLvManager) {
		this.memberLvManager = memberLvManager;
	}

	public void setMemberPointManger(IMemberPointManger memberPointManger) {
		this.memberPointManger = memberPointManger;
	}

	
	public ICartManager getCartManager() {
		return cartManager;
	}

	public void setCartManager(ICartManager cartManager) {
		this.cartManager = cartManager;
	}

	
	public IAllianceCountManager getAllianceCountManager() {
		return AllianceCountManager;
	}

	public void setAllianceCountManager(IAllianceCountManager allianceCountManager) {
		AllianceCountManager = allianceCountManager;
	}

	@Override
	public Member getMemberByMobile(String mobile) {
		String sql = "select * from es_member where mobile=?";
		List list = this.baseDaoSupport.queryForList(sql, Member.class, mobile);
		Member m = null;
		if (list != null && list.size() > 0) {
			m = (Member) list.get(0);
		}
		return m;
	}


	private static boolean checkPassword(String oldPassword, String currentPassword) {
		boolean passwordMatches = false;
		if (oldPassword != null) {
				passwordMatches = HashCrypt.comparePassword(oldPassword, "SHA", currentPassword);
		}
		if (!passwordMatches) {
			passwordMatches = currentPassword.equals(oldPassword);
		}
		return passwordMatches;
	}
	public Member   getmembert(int memberId) {
		String  sqlString="select * from es_member where member_id=? ";
		
		Member  m=(Member) this.daoSupport.queryForObject(sqlString, Member.class, memberId);
		return m;
	}
	public List<MemberLv> memberlvlist() {
		String  sql="select * from  es_member_lv  order by  point";
		
	 List list= this.daoSupport.queryForList(sql, MemberLv.class);
		
		return list;
	}
	@Override
	public Member editNoLogin(Member member) {
		// 前后台用到的是一个edit方法，请在action处理好
		this.baseDaoSupport.update("member", member, "member_id=" + member.getMember_id());
		Integer memberpoint = member.getPoint();
		
		//改变会员等级
		if(memberpoint!=null ){
			MemberLv lv =  this.memberLvManager.getByPoint(memberpoint);
			if(lv!=null ){
				if((member.getLv_id()==null ||lv.getLv_id().intValue()>member.getLv_id().intValue())){
					this.updateLv(member.getMember_id(), lv.getLv_id());
				} 
			}
		}
		return null;
	}
	
	@Override
	public void updateMemberStatus(Integer[] id,Integer m) {
		if (id == null || id.equals(""))
			return;
		String id_str = StringUtil.arrayToString(id, ",");
		String sql = "update member set is_cheked="+m+" where member_id in (" + id_str + ")";
		this.baseDaoSupport.execute(sql);
	}
	
	/**
	 * 查询所有卖家
	 */
	@Override
	public List<Member> memberList(){
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" select * from es_member m where m.is_store=? ");
		
		List<Member>memberlist=this.baseDaoSupport.queryForList(sqlBuffer.toString(), Member.class, 1);
		if(memberlist.size()>0){
			return memberlist;
		}else{
			return null;
		}
		
	}

	@Override
	public List<Member> searchMember(Map memberMap) {
		// TODO Auto-generated method stub
		String sql = createTemlSql(memberMap);
		sql+=" order by "+""+"m.member_id ";
		//System.out.println(sql);
		return this.baseDaoSupport.queryForList(sql,Member.class);
	}
	/**
	 * 修改会员备注信息
	 */
	@Override
	public void editMember(Member oldMember) {
		this.daoSupport.update("es_member", oldMember, "member_id="+oldMember.getMember_id());
		
	}
	
	 public static String getJsonContent(String urlStr)
	    {
	        try
	        {// 获取HttpURLConnection连接对象
	            URL url = new URL(urlStr);
	            HttpURLConnection httpConn = (HttpURLConnection) url
	                    .openConnection();
	            // 设置连接属性
	            httpConn.setConnectTimeout(3000);
	            httpConn.setDoInput(true);
	            httpConn.setRequestMethod("GET");
	            System.out.println("httpConn:"+httpConn);
	            // 获取相应码
	            int respCode = httpConn.getResponseCode();
	            if (respCode == 200)
	            {
	                return ConvertStream2Json(httpConn.getInputStream());
	            }
	        }
	        catch (MalformedURLException e)
	        {
	            e.printStackTrace();
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	        return "";
	    }
	 
	    
	    private static String ConvertStream2Json(InputStream inputStream)
	    {
	        String jsonStr = "";
	        // ByteArrayOutputStream相当于内存输出流
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int len = 0;
	        // 将输入流转移到内存输出流中
	        try
	        {
	            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1)
	            {
	                out.write(buffer, 0, len);
	            }
	            // 将内存流转换为字符串
	            jsonStr = new String(out.toByteArray());
	            System.out.println("jsonStr"+jsonStr);
	            
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	        return jsonStr;
	    }


		@Override
		public List<Member> queryMemberList() {
			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT * from es_member ");
			sql.append(" order by member_id desc ");
			List<Member> mlist = this.baseDaoSupport.queryForList(sql.toString(), Member.class);
			if(mlist!=null && mlist.size()>0){
				return mlist;
			}else{
				return null;
			}
		}


		@Override
		public String GetAddressByIp(String IP) {

	        String resout = "";
	        try{
	         String str = getJsonContent("http://ip.taobao.com/service/getIpInfo.php?ip="+IP);
	         System.out.println("str"+str);
	          
	         JSONObject obj = JSONObject.fromObject(str);
	         System.out.println("obj"+obj);
	         JSONObject obj2 =  (JSONObject) obj.get("data");
	         Integer code = (Integer) obj.get("code");
	         if(code==0){
	          
	             resout =  obj2.get("country")+"--" +obj2.get("area")+"--" +obj2.get("city")+"--" +obj2.get("isp");
	             System.out.println("resout:"+resout);
	         }else{
	             resout =  "IP地址有误";
	         }
	        }catch(Exception e){
	             
	            e.printStackTrace();
	             resout = "获取IP地址异常："+e.getMessage();
	        }
	        return resout;
		}

		/**
		 * 通过Cookie获取member
		 */
		@Override
		public Member getmemberWithCookie(String username, String password) {
			String sql= null;
			if (username.contains("@")) {
				sql = "select m.*,l.name as lvname from "
						+ this.getTableName("member") + " m left join "
						+ this.getTableName("member_lv")
						+ " l on m.lv_id = l.lv_id where m.email=?  ";
			}
			List<Member> list = this.daoSupport.queryForList(sql, Member.class,	username);
			if (list == null || list.isEmpty()) {
				return null;
			}
			
			Member member = list.get(0);
			if(checkPassword(member.getPassword() , password)){
				return member;
			}else{
				return null;
			}
		}

		@Override
		public int getMemberBrandCount(Integer memberid) {
			List<Attration> list=this.baseDaoSupport.queryForList("select * from es_attration a ,es_brand b where a.brandid=b.brand_id and b.disabled=0 and is_true=1 and memberid=?", Attration.class,memberid);
			return list.size();
		}
		@Override
		@Transactional(propagation = Propagation.REQUIRED)
		public void updateFace(Integer member_id, String imgPath) {
			Member member = this.get(member_id);
			member.setFace(imgPath);
			this.daoSupport.update("es_member",member,"member_id="+member_id);
			
		}

		@Override
		public int checkemail(String email) {
			String sql = "select count(0) from member where email=?";
			int count = this.baseDaoSupport.queryForInt(sql, email);
			count = count > 0 ? 1 : 0;
			return count;
		}

		@Override
		public int checkMobile(String mobile) {
			String sql = "select count(0) from member where mobile=?";
			int count = this.baseDaoSupport.queryForInt(sql, mobile);
			count = count > 0 ? 1 : 0;
			return count;
		}

		public int checkmobile(String mobile) {
			// TODO Auto-generated method stub
			String sql = "select count(0) from member where mobile=?";
			int count = this.baseDaoSupport.queryForInt(sql, mobile);
			count = count > 0 ? 1 : 0;
			return count;
		}

		@Override
		public int checkemailByMobile(String email, int member) {
			String sql = "select count(*) from es_member where member_id="+member+" and email=? ";
			int result = this.daoSupport.queryForInt(sql,email);
			return result;
		}

		@Override
		public int checkMobileByMobile(String mobile, int member_id) {
			String sql = "select count(*) from es_member where member_id="+member_id+" and mobile=? ";
			int result = this.daoSupport.queryForInt(sql,mobile);
			return result;
		}

		@Override
		public int checkMobileBy(String mobile) {
			String str="select count(1) from es_member where mobile=?";
			int result = this.daoSupport.queryForInt(str,mobile);
			return result;
		}

		@Override
		public int checkemailBy(String email) {
			String sql = "select count(*) from es_member where  email=? ";
			int result = this.daoSupport.queryForInt(sql,email);
			return result;
		}

		@Override
		public int checkSendIsCheck(Integer member_id) {
		String sql="select * from es_member where member_id=?";
		Member member=(Member) this.baseDaoSupport.queryForObject(sql, Member.class, member_id);
			return member.getIs_mobile();
		}
}
