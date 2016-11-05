package com.enation.app.shop.core.action.backend;

import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.order.StoreOrder;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberLv;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.base.core.service.IRegionsManager;
import com.enation.app.shop.core.model.AdvanceLogs;
import com.enation.app.shop.core.model.PointConsume;
import com.enation.app.shop.core.model.PointHistory;
import com.enation.app.shop.core.model.RegisterSendPoint;
import com.enation.app.shop.core.plugin.member.MemberPluginBundle;
import com.enation.app.shop.core.service.IAdvanceLogsManager;
import com.enation.app.shop.core.service.IMemberCommentManager;
import com.enation.app.shop.core.service.IMemberLvManager;
import com.enation.app.shop.core.service.IPointConsumeManager;
import com.enation.app.shop.core.service.IPointHistoryManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.eop.resource.IUserManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.HashCrypt;
import com.enation.framework.util.StringUtil;
/**
 * 会员管理Action
 * @author LiFenLong 2014-4-1;4.0版本改造   
 *
 */
@Component
@Scope("prototype")
@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Action("member")
@Results({
	@Result(name="add_lv", type="freemarker", location="/shop/admin/member/lv_add.html"),
	@Result(name="edit_lv", type="freemarker", location="/shop/admin/member/lv_edit.html"),
	@Result(name="list_lv", type="freemarker", location="/shop/admin/member/lv_list.html"),
	@Result(name="add_member", type="freemarker", location="/shop/admin/member/member_add.html"),
	@Result(name="edit_member", type="freemarker", location="/shop/admin/member/member_edit.html"),
	@Result(name="list_member", type="freemarker", location="/shop/admin/member/member_list.html"),
	@Result(name="detail", type="freemarker", location="/shop/admin/member/member_detail.html"),
	@Result(name="base",  location="/shop/admin/member/member_base.jsp"),
	@Result(name="edit",  location="/shop/admin/>member/member_edit.jsp"),
	@Result(name="orderLog",  location="/shop/admin/member/member_orderLog.jsp"),
	@Result(name="editPoint", location="/shop/admin/member/member_editPoint.jsp"),
	@Result(name="pointLog",  location="/shop/admin/member/member_pointLog.jsp"),
	@Result(name="advance",   location="/shop/admin/member/member_advance.jsp"),
	@Result(name="comments",  location="/shop/admin/member/member_comments.jsp"),
	@Result(name="remark",  location="/shop/admin/member/member_remark.jsp"),
	@Result(name="syslogin",  location="/shop/admin/member/syslogin.jsp"),
	@Result(name="consume_list", type="freemarker", location="/core/admin/consume/point_consume_list.html"),
	@Result(name="consume_edit", type="freemarker", location="/core/admin/consume/point_consume_edit.html"),
	@Result(name="consume_add", type="freemarker", location="/core/admin/consume/point_consume_add.html"),
	@Result(name="register_point_list", type="freemarker", location="/core/admin/consume/register_point_list.html"),
	@Result(name="addregisterPoint", type="freemarker", location="/core/admin/consume/add_register_point.html"),
	@Result(name="ditRegisterPoint", type="freemarker", location="/core/admin/consume/edit_register_point.html")
})
@SuppressWarnings({ "rawtypes", "unchecked", "serial","static-access" })
public class MemberAction extends WWAction {

	private IMemberManager memberManager;
	private IMemberLvManager memberLvManager;
	private IRegionsManager regionsManager;
 
	private IPointHistoryManager pointHistoryManager;
	private IAdvanceLogsManager advanceLogsManager;
	private IMemberCommentManager memberCommentManager;
	private MemberPluginBundle memberPluginBundle;
	
	private IUserManager userManager ;
	private Member member;
	private MemberLv lv;
	private String birthday;
	private Integer[] lv_id;
	private Integer memberId;
	private Integer[] member_id;
	private List lvlist;
	private List provinceList;
	private List cityList;
	private List regionList;
 
	private List listPointHistory;
	private List listAdvanceLogs;
	private List listComments;
	private int mp;
	private int point;
	private int pointtype; //积分类型
	private Double modify_advance;
	private String modify_memo;
	private String object_type;
	private String name;
	private String uname;
	private Integer mobile;
	private String email;
	private Integer sex=2;
	private Integer lvId;
	private List catDiscountList;
	private int[] cat_ids;
	private int[] cat_discounts;
	private Map memberMap;
	private String start_time;
	private String end_time;
	private Integer stype;
	private String keyword;
	private Integer province_id;
	private Integer city_id;
	private Integer region_id;
	private Integer is_store;
	private Integer member_status;//用户账号的状态
	private String member_country;
	//详细页面插件返回的数据 
	protected Map<Integer,String> pluginTabs;
	
	protected Map<Integer,String> pluginHtmls;
	
	private Map statusMap;
	private String status_Json;
	private double proportion;//消费抵值比例
	

	private String validity;//积分有效期
	
	private Date startTime; //活动开始日期
	private Date endTime;//活动结束日期
	public double getProportion() {
		return proportion;
	}
	public void setProportion(double proportion) {
		this.proportion = proportion;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public Integer getCon_ru() {
		return con_ru;
	}
	public void setCon_ru(Integer con_ru) {
		this.con_ru = con_ru;
	}
	public Integer getCon_zh() {
		return con_zh;
	}
	public void setCon_zh(Integer con_zh) {
		this.con_zh = con_zh;
	}

	private Integer con_ru;		//消费次数（卢布）
	private Integer con_zh;		//消费次数（人民币）

	@Autowired
	private IPointConsumeManager pointConsumeManager;
	private Integer[] consume_id;
	private PointConsume consume;
	private Integer consumeId;
	
	private RegisterSendPoint registerSendPoint;
	private String sendTimeStart;
	private String sendTimeEnd;
	private Integer id;
	

	/**
	 * 跳转至添加会员等级页面
	 * @return 添加会员等级页面
	 */
	public String add_lv() {
		return "add_lv";
	}
	/**
	 * 跳转至修改会员等级页面
	 * @param lvId 会员等级Id
	 * @param lv 会员等级
	 * @return 修改会员等级页面
	 */
	public String edit_lv() {
		lv = this.memberLvManager.get(lvId);
		return "edit_lv";
	}
	/**
	 * 跳转至会员等级列表
	 * @return 会员等级列表
	 */
	public String list_lv() {
		return "list_lv";
	}
	/**
	 * 获取会员等级列表Json
	 * @return 会员等级列表Json
	 */
	public String list_lvJson() {
		this.webpage = memberLvManager.list(this.getSort(), this.getPage(), this.getPageSize());
		this.showGridJson(webpage);
		return JSON_MESSAGE;
	}
	/**
	 * 添加会员等级
	 * @param lv 会员等级,MemberLv
	 * @return result
	 * result 1.操作成功.0.操作失败
	 */
	public String saveAddLv() {
		memberLvManager.add(lv);
		this.showSuccessJson("会员等级添加成功");
		return JSON_MESSAGE;
	}
	/**
	 * 修改会员等级
	 * @param lv 会员等级,MemberLv
	 * @return result
	 * result 1.操作成功.0.操作失败
	 */
	public String saveEditLv() {
		
		try{
			memberLvManager.edit(lv);
			this.showSuccessJson("会员等级修改成功");
		}catch (Exception e) {
			this.showErrorJson("非法参数");
		}
		return JSON_MESSAGE;
	}
	/**
	 * 删除会员等级
	 * @param lv_id,会员等级Id,Integer
	 * @return  result
	 * result 1.操作成功.0.操作失败
	 */
	public String deletelv() {
		try {
			this.memberLvManager.delete(lv_id);
			this.showSuccessJson("删除成功");
		} catch (RuntimeException e) {
			this.showErrorJson("删除失败");
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 跳转至添加会员页面
	 * @param lvlist  会员等级列表,List
	 * @return 添加会员页面
	 */
	public String add_member() {
		if(lvlist==null){			
			lvlist = this.memberLvManager.list();
		}
		provinceList = this.regionsManager.listProvince();
		return "add_member";
	}
	/**
	 * 跳转至修改会员页面
	 * @param memberId 会员Id,Integer
	 * @param member 会员,Member
	 * @param lvlist 会员等级列表,List
	 * @return 修改会员页面
	 */
	public String edit_member() {
		member = this.memberManager.get(memberId);
		if(lvlist==null){			
			lvlist = this.memberLvManager.list();
		}
		return "edit_member";
	}
	/**
	 * 跳转至会员列表
	 * @param lvlist 会员等级列表,List
	 * @return 会员列表
	 */
	public String memberlist() {
		lvlist = this.memberLvManager.list();
		return "list_member";
	}
	/**
	 * 获取会员列表Json
	 * @param stype 搜索类型,Integer
	 * @param keyword 搜索关键字,String
	 * @param uname 会员名称,String
	 * @param mobile 联系方式,String
	 * @param lvId 会员等级,Integer
	 * @param email 邮箱,String
	 * @param sex 性别,Integer
	 * @param start_time 注册开始时间,String
	 * @param end_time 注册最后时间,String
	 * @param province_id 省份Id,Integer
	 * @param city_id	城市Id,Integer
	 * @param region_id 地区Id,Integer
	 * @return 会员列表Json
	 */
	public String memberlistJson() {
		
		memberMap = new HashMap();
		memberMap.put("stype", stype);
		memberMap.put("keyword", keyword);
		memberMap.put("uname", uname);
		memberMap.put("mobile", mobile);
		memberMap.put("lvId", lvId);
		memberMap.put("email", email);
		memberMap.put("sex", sex);
		memberMap.put("start_time", start_time);
		memberMap.put("end_time", end_time);
		memberMap.put("province_id", province_id);
		memberMap.put("city_id", city_id);
		memberMap.put("region_id", region_id);
		memberMap.put("is_store", is_store);
		this.webpage = this.memberManager.searchMember(memberMap, this.getPage(), this.getPageSize(), this.getSort(),this.getOrder());
		this.showGridJson(webpage);
		
		return JSON_MESSAGE;
	}
	
	/**
	 * 导出会员信息
	 */
	public String exportMember() {
		
		memberMap = new HashMap();
		memberMap.put("stype", stype);
		memberMap.put("keyword", keyword);
		memberMap.put("is_store", is_store);
		
		List<Member> memberList = this.memberManager.searchMember(memberMap);
		
//		this.webpage = this.memberManager.searchMember(memberMap, this.getPage(), this.getPageSize(), this.getSort(),this.getOrder());
//		this.showGridJson(webpage);
		
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook(); 
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("会员明细表"); 
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("会员ID");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 1);  
        cell.setCellValue("会员登录名");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 2);  
        cell.setCellValue("注册时间");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 3);  
        cell.setCellValue("真实姓名");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("注册IP");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 5);  
        cell.setCellValue("身份证号");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 6);  
        cell.setCellValue("所在市");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 7);  
        cell.setCellValue("所在省");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 8);  
        cell.setCellValue("详细地址");  
        cell.setCellStyle(style); 
        
/*        cell = row.createCell((short) 5);  
        cell.setCellValue("等级积分");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 6);  
        cell.setCellValue("消费积分");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 7);  
        cell.setCellValue("会员等级");  
        cell.setCellStyle(style);*/
        
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到。
        int count = 0;
        for(Member member : memberList){

        	row = sheet.createRow(count + 1); 
        	try {
				
				row.createCell((short) 0).setCellValue(member.getMember_id());  
		        row.createCell((short) 1).setCellValue(member.getUname()); 
		        row.createCell((short) 2).setCellValue(this.getTime(member.getRegtime())); 		        
		        row.createCell((short) 3).setCellValue(member.getName()); 
		        row.createCell((short) 4).setCellValue(member.getRegisterip()); 
		        row.createCell((short) 5).setCellValue(member.getId_number()); 
		        row.createCell((short) 6).setCellValue(member.getCity()); 
		        row.createCell((short) 7).setCellValue(member.getProvince()); 
		        row.createCell((short) 8).setCellValue(member.getAddress()); 
		        
		        
		        
			} catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
				 this.showErrorJson("导出数据失败" + e.getMessage());
				 this.logger.error("导出数据失败", e);
			} 
        	
        	
        	count ++;
        	
        }
        // 第六步，将文件存到指定位置  
        try{
            String filename = "huiyuan.xls";//设置下载时客户端Excel的名称    
            // 请见：http://zmx.iteye.com/blog/622529 
            //http://lancijk.iteye.com/blog/1390341   
            //filename = Util.encodeFilename(filename, request);    
            response.setContentType("application/vnd.ms-excel");    
            response.setHeader("Content-disposition", "attachment;filename=" + filename);    
            OutputStream ouputStream = response.getOutputStream();    
            wb.write(ouputStream);    
            ouputStream.flush();    
            ouputStream.close(); 
        }catch (Exception e){  
        	this.showErrorJson("导出失败" + e.getMessage());
			this.logger.error("导出失败", e);
			e.printStackTrace();
          
        } 
        
        this.showSuccessJson("成功导出！");
		
		return null;
	}
	
	/*
	 * 日期转换
	 */
	public String getTime (Long time){
		String text = "";
		if(time!=null&&time>0){
			text = DateUtil.toString(time,"yyyy-MM-dd");
		}		
		return text;
	}
	
	/**
	 * 修改会员
	 * @param birthday 生日,String
	 * @param oldMember 修改前会员,Member
	 * @param member 修改后会员,Member
	 * @param province 省份,String
	 * @param city 城市,String
	 * @param region 地区,String
	 * @param province_id 省份Id,Integer
	 * @param city_id 城市Id,Integer
	 * @param region_id 地区Id,Integer
	 * @return result
	 * result 1.操作成功.0.操作失败
	 */
	public String saveEditMember() {
		if(!StringUtil.isEmpty(birthday)){
			member.setBirthday(DateUtil.getDateline(birthday));
		}
		try {
			HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
			Member oldMember = new Member();
			if(memberId!=null){
				oldMember = this.memberManager.get(memberId);
				String remark = request.getParameter("remark");
				oldMember.setRemark(remark);
				this.memberManager.editMember(oldMember);
				this.showSuccessJson("修改成功");
			}else{
			oldMember = this.memberManager.get(member.getMember_id());
			String province = request.getParameter("province");
			String city = request.getParameter("city");
			String region = request.getParameter("region");
			
			String province_id = request.getParameter("province_id");
			String city_id = request.getParameter("city_id");
			String region_id = request.getParameter("region_id");
			
			if(!StringUtil.isEmpty(province)){
				oldMember.setProvince(province);
			}
			if(!StringUtil.isEmpty(province)){
				oldMember.setProvince(city);
			}
			if(!StringUtil.isEmpty(province)){
				oldMember.setProvince(region);
			}
			
			if(!StringUtil.isEmpty(province_id)){
				oldMember.setProvince_id(StringUtil.toInt(province_id,true));
			}
			
			if(!StringUtil.isEmpty(city_id)){
				oldMember.setCity_id(StringUtil.toInt(city_id,true));
			}
			
			if(!StringUtil.isEmpty(province_id)){
				oldMember.setRegion_id(StringUtil.toInt(region_id,true));
			}
			if(!StringUtil.isEmpty(member.getPassword())){
				//oldMember.setPassword(StringUtil.md5(member.getPassword()));
				oldMember.setPassword(HashCrypt.cryptUTF8("SHA", null, member.getPassword()));
			}
			oldMember.setName(member.getName());
			oldMember.setSex(member.getSex());
			oldMember.setBirthday(member.getBirthday());
			if(!StringUtil.isEmpty(member.getEmail())){
				oldMember.setEmail(member.getEmail());
			}
			oldMember.setTel(member.getTel());
			oldMember.setMobile(member.getMobile());
			if(member.getLv_id()!=null){
				oldMember.setLv_id(member.getLv_id());
			}
			oldMember.setZip(member.getZip());
			oldMember.setAddress(member.getAddress());
			if(!StringUtil.isEmpty(member.getQq())){
				oldMember.setQq(member.getQq());
			}
			if(!StringUtil.isEmpty(member.getMsn())){
				oldMember.setMsn(member.getMsn());
			}
			if(!StringUtil.isEmpty(member.getPw_answer())){
				oldMember.setPw_answer(member.getPw_answer());
			}
			if(!StringUtil.isEmpty(member.getPw_question())){
				oldMember.setPw_question(member.getPw_question());
			}
			this.memberManager.edit(oldMember);
			this.showSuccessJson("修改成功");
			}
		} catch (Exception e) {
			this.showErrorJson("修改失败");
		}
		return this.JSON_MESSAGE;

	}
	/**
	 * 删除会员
	 * @param member_id 会员Id,Integer
	 * @return result
	 * result 1.操作成功.0.操作失败
	 */
	public String delete() {
		try {
			this.memberManager.delete(member_id);
			this.showSuccessJson("删除成功");
		} catch (RuntimeException e) {
			this.showErrorJson("删除失败"+e.getMessage());
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 启用或禁用会员
	 * @param member_id 会员Id,Integer
	 * @return result
	 * result 1.操作成功.0.操作失败
	 */
	public String updateMemberStatus() {
		try {
			//this.memberManager.delete(member_id);
			this.memberManager.updateMemberStatus(member_id, member_status);
			if(member_status!=null && member_status==0){
				this.showSuccessJson("禁用成功");
			}else{
				this.showSuccessJson("启用成功");
			}
			
		} catch (RuntimeException e) {
			this.showErrorJson("操作失败"+e.getMessage());
		}
		return this.JSON_MESSAGE;
	}

	
	/**
	 * 跳转至会员详细页面
	 * @param memberId 会员Id,Integer
	 * @param member 会员,Member
	 * @param pluginTabs tab列表,List<Map>
	 * @param pluginHtmls tab页Html内容,List<Map>
	 * @return 会员详细页面
	 */
	public String detail() {
		
		this.member = this.memberManager.get(memberId);
		pluginTabs = this.memberPluginBundle.getTabList(member);
		pluginHtmls=this.memberPluginBundle.getDetailHtml(member);
		return "detail";
	}
	
	  

	public String editPoint() {
		member = this.memberManager.get(memberId);
		return "editPoint";
	}

	public String editSavePoint() {
		member = this.memberManager.get(memberId);
		member.setPoint(member.getPoint() + point);
		PointHistory pointHistory = new PointHistory();
		pointHistory.setMember_id(memberId);
		pointHistory.setOperator("管理员");
		pointHistory.setPoint(point);
		pointHistory.setReason("管理员手工修改");
		pointHistory.setTime(DateUtil.getDateline());
		long endtime = DateUtil.getDateline() + 60*60*24*365;
		pointHistory.setEndtime(endtime);
		pointHistory.setType(1);
		try {
			memberManager.edit(member);
			pointHistoryManager.addPointHistory(pointHistory);
			this.showSuccessJson("会员等级积分修改成功");
		} catch (Exception e) {
			this.showErrorJson("修改失败");
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
	}

	public String editSaveMp() {
		member = this.memberManager.get(memberId);
		member.setMp(member.getMp() + mp);
		PointHistory pointHistory = new PointHistory();
		pointHistory.setMember_id(memberId);
		pointHistory.setOperator("管理员");
		pointHistory.setMp(mp);
		//pointHistory.setPoint_type(1);//类型1为消费积分 暂时不设置  页面统一显示 
		pointHistory.setReason("管理员手工修改");
		pointHistory.setTime(DateUtil.getDateline());
		long endtime = DateUtil.getDateline() + 60*60*24*365;
		pointHistory.setEndtime(endtime);
		pointHistory.setType(1);//1为获得 0为消费
		try {
			memberManager.edit(member);
			pointHistoryManager.addPointHistory(pointHistory);
			this.showSuccessJson("会员消费积分修改成功");
		} catch (Exception e) {
			this.showErrorJson("修改失败");
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
	}

	public String pointLog() {
		listPointHistory = pointHistoryManager.listPointHistory(memberId,pointtype);
		member = this.memberManager.get(memberId);
		return "pointLog";
	}

	public String advance() {
		member = this.memberManager.get(memberId);
		listAdvanceLogs = this.advanceLogsManager
				.listAdvanceLogsByMemberId(memberId);
		return "advance";
	}

	public String comments() {
		Page page = memberCommentManager.getMemberComments(1, 100, StringUtil.toInt(object_type), memberId);
		if(page != null){
			listComments = (List)page.getResult();
		}
		return "comments";
	}
 
 

	/**
	 * 预存款充值
	 * 
	 * @return
	 */
	public String editSaveAdvance() {
		member = this.memberManager.get(memberId);
		member.setAdvance(member.getAdvance() + modify_advance);
		try {
			memberManager.edit(member);
		} catch (Exception e) {
			this.json = "{'result':1, 'message':'操作失败'}";
			e.printStackTrace();
		}

		AdvanceLogs advanceLogs = new AdvanceLogs();
		advanceLogs.setMember_id(memberId);
		advanceLogs.setDisabled("false");
		advanceLogs.setMtime(DateUtil.getDateline());
		advanceLogs.setImport_money(modify_advance);
		advanceLogs.setMember_advance(member.getAdvance());
		advanceLogs.setShop_advance(member.getAdvance());// 此字段很难理解
		advanceLogs.setMoney(modify_advance);
		advanceLogs.setMessage(modify_memo);
		advanceLogs.setMemo("admin代充值");
		try {
			advanceLogsManager.add(advanceLogs);
		} catch (Exception e) {
			this.json = "{'result':1, 'message':'操作失败'}";
			e.printStackTrace();
		}
		this.json = "{'result':0,'message':'操作成功'}";

		return this.JSON_MESSAGE;
	}

	private String message;

	/**
	 * 预存款充值
	 * 
	 * @return
	 */
	// public String addMoney(){
	// //this.memberManager.addMoney(member.getBiz_money(),
	// member.getMember_id(),message);
	// this.msgs.add("充值成功");
	// this.urls.put("返回此会员预存款信息", "member/member_money.jsp?member_id="+
	// member.getMember_id());
	// return this.MESSAGE;
	// }
	
	/**
	 * 保存添加会员
	 * @author xulipeng
	 * @param member 会员,Member
	 * @param province 省份,String
	 * @param city 城市,String
	 * @param region 地区,String
	 * @param province_id  省份Id,Integer
	 * @param city_id 城市Id,Integer
	 * @param region_id 地区Id,Integer
	 * @param birthday 生日,String
	 * @return result
	 * 2014年4月1日18:22:50
	 */
	public String saveMember() {
		int result = memberManager.checkname(member.getUname());
		if (result == 1) {
			this.showErrorJson("用户名已存在");
			return JSON_MESSAGE;
		}
		if (member != null) {
			HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
			String province = request.getParameter("province");
			String city = request.getParameter("city");
			String region = request.getParameter("region");
			
			String province_id = request.getParameter("province_id");
			String city_id = request.getParameter("city_id");
			String region_id = request.getParameter("region_id");
			
			
			member.setProvince(province);
			member.setCity(city);
			member.setRegion(region);
			
			if(!StringUtil.isEmpty(province_id)){
				member.setProvince_id( StringUtil.toInt(province_id,true));
			}
			
			if(!StringUtil.isEmpty(city_id)){
				member.setCity_id(StringUtil.toInt(city_id,true));
			}
			
			if(!StringUtil.isEmpty(province_id)){
				member.setRegion_id(StringUtil.toInt(region_id,true));
			}
			
			member.setBirthday(DateUtil.getDateline(birthday));
			member.setPassword(member.getPassword());
			member.setRegtime(DateUtil.getDateline());// lzf add
			memberManager.add(member);
			this.showSuccessJson("保存会员成功",member.getMember_id());
			
		} 
		return JSON_MESSAGE;
	}
	
	public String sysLogin(){
		try{
			name = StringUtil.toUTF8(name);
			int r  = this.memberManager.loginbysys(name);
//			if(r==1){
//				EopUser user  = this.userManager.get(name);
//				if(user!=null){
//					WebSessionContext<EopUser> sessonContext = ThreadContextHolder
//					.getSessionContext();	
//					sessonContext.setAttribute(IUserManager.USER_SESSION_KEY, user);
//				}
//				return "syslogin";
//			}
			this.msgs.add("登录失败");
			return this.MESSAGE;
		}catch(RuntimeException e){
			this.msgs.add(e.getMessage());
			return this.MESSAGE;
		}
	}
	
	/**
	 * 获取订单状态的json
	 * @param OrderStatus 订单状态
	 * @return
	 */
	private Map getStatusJson(){
		Map orderStatus = new  HashMap();
		orderStatus.put(""+OrderStatus.ORDER_NOT_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_NOT_PAY));
		orderStatus.put(""+OrderStatus.ORDER_NOT_CONFIRM, OrderStatus.getOrderStatusText(OrderStatus.ORDER_NOT_CONFIRM));
		orderStatus.put(""+OrderStatus.ORDER_PAY_CONFIRM, OrderStatus.getOrderStatusText(OrderStatus.ORDER_PAY_CONFIRM));
		orderStatus.put(""+OrderStatus.ORDER_ALLOCATION_YES, OrderStatus.getOrderStatusText(OrderStatus.ORDER_ALLOCATION_YES));
		orderStatus.put(""+OrderStatus.ORDER_SHIP, OrderStatus.getOrderStatusText(OrderStatus.ORDER_SHIP));
		orderStatus.put(""+OrderStatus.ORDER_ROG, OrderStatus.getOrderStatusText(OrderStatus.ORDER_ROG));
		orderStatus.put(""+OrderStatus.ORDER_CANCEL_SHIP, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CANCEL_SHIP));
		orderStatus.put(""+OrderStatus.ORDER_COMPLETE, OrderStatus.getOrderStatusText(OrderStatus.ORDER_COMPLETE));
		orderStatus.put(""+OrderStatus.ORDER_CANCEL_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CANCEL_PAY));
		orderStatus.put(""+OrderStatus.ORDER_CANCELLATION, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CANCELLATION));
		orderStatus.put(""+OrderStatus.ORDER_CHANGED, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CHANGED));
		orderStatus.put(""+OrderStatus.ORDER_CHANGE_APPLY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CHANGE_APPLY));
		orderStatus.put(""+OrderStatus.ORDER_RETURN_APPLY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_RETURN_APPLY));
		orderStatus.put(""+OrderStatus.ORDER_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_PAY));
		return orderStatus;
	}
	
	
//	//修改会员备注
//	public String editRemark(){
//		
//		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
//		String modify_memo = request.getParameter("modify_memo");
//		int memberid  = StringUtil.toInt(request.getParameter("memberid"),true);
//		Member member = this.memberManager.get(memberid);
//		member.setRemark(modify_memo);
//		try {
//			memberManager.edit(member);
//			this.showSuccessJson("会员备注修改成功");
//		} catch (Exception e) {
//			this.logger.error("修改会员备注",e);
//			this.showErrorJson("会员备注修改失败");
//		}
//		
//		return JSON_MESSAGE;
//	}
	
	
	//预付款
	public String editAdvance(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		double modify_advance = StringUtil.toDouble(request.getParameter("modify_advance"), true);
		String modify_memo = request.getParameter("modify_memo");
		int memberid = StringUtil.toInt(request.getParameter("memberid"), true);
		Member member = this.memberManager.get(memberid);
		member.setAdvance(member.getAdvance() + modify_advance);
		AdvanceLogs advanceLogs = new AdvanceLogs();
		advanceLogs.setMember_id(memberid);
		advanceLogs.setDisabled("false");
		advanceLogs.setMtime(DateUtil.getDateline());
		advanceLogs.setImport_money(modify_advance);
		advanceLogs.setMember_advance(member.getAdvance());
		advanceLogs.setShop_advance(member.getAdvance());// 此字段很难理解
		advanceLogs.setMoney(modify_advance);
		advanceLogs.setMessage(modify_memo);
		AdminUser user = (AdminUser)ThreadContextHolder.getSessionContext().getAttribute(UserConext.CURRENT_ADMINUSER_KEY);
		advanceLogs.setMemo(user.getUsername() + "代充值");
		try {
			memberManager.edit(member);
			advanceLogsManager.add(advanceLogs);
			this.showSuccessJson("会员预存款修改成功");
		} catch (Exception e) {
			this.logger.error("会员预存款修改失败", e);
			this.showErrorJson("修改失败");
		}
		return JSON_MESSAGE;
	}
	
	public String consumeList() {
		return "consume_list";
	}
	/**
	 * 查询积分消费抵押项
	 * @author WKZ
	 */
	public String consumeListJson() {
		this.webpage = pointConsumeManager.getConsumePage(this.getPage(), this.getPageSize());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
	}
	
	public String getSingleConsume() {
		try {
			consume = pointConsumeManager.getSingleConsume(consumeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "consume_edit";
	}
	
	/**
	 * 修改积分抵押金额
	 * @author WKZ
	 */
	public String editConsumeAmount() {
		try {
			pointConsumeManager.modifyAcount(consume);
			this.showSuccessJson("修改抵押金额成功!");
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson("修改抵押金额失败");
		}
		return this.JSON_MESSAGE;
	}
	
	public String addConsume() {
		return "consume_add";
	}
	
	/**
	 * 添加积分抵押
	 * @author WKZ
	 */
	public String addConsumeJson() {
		try {
			if(pointConsumeManager.isOnlyCurrency(consume.getConsume_currency())) {
				this.showErrorJson("币种重复");
				return this.JSON_MESSAGE;
			}
			consume.setConsume_num(1);
			pointConsumeManager.addAcount(consume);
			this.showSuccessJson("添加成功!");
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson("添加失败");
		}
		return this.JSON_MESSAGE;
	}
	
	/**
	 * 删除积分抵押
	 * @author WKZ
	 */
	public String deleteConsume() {
		try {
			pointConsumeManager.deleteAcount(consume_id);
			this.showSuccessJson("删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson("删除失败");
		}
		return this.JSON_MESSAGE;
	}
	
	/**
	 * 跳转赠送积分规则主页
	 * @return
	 */
	public String registerPointList(){
		return "register_point_list";
	}
	/**
	 * 获取赠送积分规则页面
	 * @return
	 */
	public String registerPointListJson(){
		this.webpage = pointConsumeManager.getRegisterPoint(this.getPage(), this.getPageSize());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
	}
	/**
	 * 跳转至添加赠送积分规则页面
	 * @return
	 */
	public String addRegisterPoint(){
		
		return "addregisterPoint";
	}
	/**
	 * 保存赠送积分规则页面
	 * @return
	 */
	public String saveAddRegisterPoint(){
		long startTime =  DateUtil.getDateline(sendTimeStart);
		long endTime =  DateUtil.getDateline(sendTimeEnd);
		if(endTime<startTime){
			this.showErrorJson("时间设置不正确，请重试...");
			return this.JSON_MESSAGE;
		}
		if(registerSendPoint.getPoint()<0){
			this.showErrorJson("积分不能为负数");
			return this.JSON_MESSAGE;
		}
		if( StringUtil.isEmpty( registerSendPoint.getName() )){
			this.showErrorJson("请输规则名称");
			return this.JSON_MESSAGE;
		}
		if( registerSendPoint.getPoint() == null){
			this.showErrorJson("请输会员等级积分");
			return this.JSON_MESSAGE;
		}
		if( registerSendPoint.getMp() == null){
			this.showErrorJson("请输会员消费积分");
			return this.JSON_MESSAGE;
		}
		if( StringUtil.isEmpty(sendTimeStart)){
			this.showErrorJson("请输入使用起始日期");
			return this.JSON_MESSAGE;
		}
		registerSendPoint.setStart_time(  DateUtil.getDateline(sendTimeStart));
		
		if( StringUtil.isEmpty(sendTimeEnd)){
			this.showErrorJson("请输入使用结束日期");
			return this.JSON_MESSAGE;
		}
		registerSendPoint.setEnd_time( DateUtil.getDateline(sendTimeEnd));
		
//		if(registerSendPoint.getStatus().equals(1)){
//			//检验现有的规则状态
//			Integer mum = this.pointConsumeManager.checkOutStatus();
//			if(mum>0){
//				this.showErrorJson("只能同时启用一种规则，请选择停用当前规则");
//				return this.JSON_MESSAGE;
//			}
//		}
		try {
			this.pointConsumeManager.addRegisterSendPoint(registerSendPoint);
			this.showSuccessJson("保存注册赠送积分类型成功");
		} catch (Throwable e) {
			this.logger.error("保存注册赠送积分类型出错", e);
			this.showErrorJson("保存注册赠送积分类型出错"+e.getMessage());
		}
		
		return this.JSON_MESSAGE;
	}
	/**
	 * 跳转赠送积分规则页面
	 * @return
	 */
	public String editRegisterPoint(){
		this.registerSendPoint = this.pointConsumeManager.get(id);
		return "editRegisterPoint";
	}
	/**
	 * 保存修改的赠送积分规则页面
	 * @return
	 */
	public String saveEditRegisterPoint(){	
		long startTime =  DateUtil.getDateline(sendTimeStart);
		long endTime =  DateUtil.getDateline(sendTimeEnd);
		if(endTime<startTime){
			this.showErrorJson("时间设置不正确，请重试...");
			return this.JSON_MESSAGE;
		}
		if(registerSendPoint.getPoint()<0){
			this.showErrorJson("赠送积分不能为负数");
			return this.JSON_MESSAGE;
		}
		if( StringUtil.isEmpty( registerSendPoint.getName() )){
			this.showErrorJson("请输规则名称");
			return this.JSON_MESSAGE;
		}
		if( registerSendPoint.getPoint() == null){
			this.showErrorJson("请输会员等级积分");
			return this.JSON_MESSAGE;
		}
		if( registerSendPoint.getMp() == null){
			this.showErrorJson("请输会员消费积分");
			return this.JSON_MESSAGE;
		}
		if( StringUtil.isEmpty(sendTimeStart)){
			this.showErrorJson("请输入使用起始日期");
			return this.JSON_MESSAGE;
		}
		registerSendPoint.setStart_time(  DateUtil.getDateline(sendTimeStart));
		
		if( StringUtil.isEmpty(sendTimeEnd)){
			this.showErrorJson("请输入使用结束日期");
			return this.JSON_MESSAGE;
		}
		registerSendPoint.setEnd_time( DateUtil.getDateline(sendTimeEnd));
		
//		if(registerSendPoint.getStatus().equals(1)){
//			//检验现有的规则状态
//			Integer mum = this.pointConsumeManager.checkOutStatus();
//			if(mum>0){
//				this.showErrorJson("只能同时启用一种规则，请选择停用当前规则");
//				return this.JSON_MESSAGE;
//			}
//		}
		try {
			this.pointConsumeManager.updateRegisterSendPoint(registerSendPoint);
			this.showSuccessJson("修改注册赠送积分类型成功");
		} catch (Throwable e) {
			this.logger.error("修改注册赠送积分类型出错", e);
			this.showErrorJson("修改注册赠送积分类型出错"+e.getMessage());
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 删除积分规则页面
	 * @return
	 */
	public String deleteRegisterPoint(){
		try {
			this.pointConsumeManager.deleteRegisterSendPoint(id);
			this.showSuccessJson("删除注册赠送积分类型成功");
		} catch (Throwable e) {
			this.logger.error("删除注册赠送积分类型出错", e);
			this.showErrorJson("删除注册赠送积分类型出错"+e.getMessage());
		}
		return this.JSON_MESSAGE;
	}
	
	
	//setter getter

	public MemberLv getLv() {
		return lv;
	}

	public void setLv(MemberLv lv) {
		this.lv = lv;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public IMemberLvManager getMemberLvManager() {
		return memberLvManager;
	}

	public void setMemberLvManager(IMemberLvManager memberLvManager) {
		this.memberLvManager = memberLvManager;
	}

	public Integer[] getLv_id() {
		return lv_id;
	}

	public void setLv_id(Integer[] lv_id) {
		this.lv_id = lv_id;
	}

	public Integer getLvId() {
		return lvId;
	}

	public void setLvId(Integer lvId) {
		this.lvId = lvId;
	}

	public List getLvlist() {
		return lvlist;
	}

	public void setLvlist(List lvlist) {
		this.lvlist = lvlist;
	}

	public IRegionsManager getRegionsManager() {
		return regionsManager;
	}

	public void setRegionsManager(IRegionsManager regionsManager) {
		this.regionsManager = regionsManager;
	}

	public List getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List provinceList) {
		this.provinceList = provinceList;
	}

	public List getCityList() {
		return cityList;
	}

	public void setCityList(List cityList) {
		this.cityList = cityList;
	}

	public List getRegionList() {
		return regionList;
	}

	public void setRegionList(List regionList) {
		this.regionList = regionList;
	}

 

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public IPointHistoryManager getPointHistoryManager() {
		return pointHistoryManager;
	}

	public void setPointHistoryManager(IPointHistoryManager pointHistoryManager) {
		this.pointHistoryManager = pointHistoryManager;
	}

	public List getListPointHistory() {
		return listPointHistory;
	}

	public void setListPointHistory(List listPointHistory) {
		this.listPointHistory = listPointHistory;
	}

	public IAdvanceLogsManager getAdvanceLogsManager() {
		return advanceLogsManager;
	}

	public void setAdvanceLogsManager(IAdvanceLogsManager advanceLogsManager) {
		this.advanceLogsManager = advanceLogsManager;
	}

	public List getListAdvanceLogs() {
		return listAdvanceLogs;
	}

	public void setListAdvanceLogs(List listAdvanceLogs) {
		this.listAdvanceLogs = listAdvanceLogs;
	}

	public Double getModify_advance() {
		return modify_advance;
	}

	public void setModify_advance(Double modifyAdvance) {
		modify_advance = modifyAdvance;
	}

	public String getModify_memo() {
		return modify_memo;
	}

	public void setModify_memo(String modifyMemo) {
		modify_memo = modifyMemo;
	}

	public List getListComments() {
		return listComments;
	}

	public void setListComments(List listComments) {
		this.listComments = listComments;
	}

	public String getObject_type() {
		return object_type;
	}

	public void setObject_type(String objectType) {
		object_type = objectType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}
 
	public IUserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(IUserManager userManager) {
		this.userManager = userManager;
	}

	public List getCatDiscountList() {
		return catDiscountList;
	}

	public void setCatDiscountList(List catDiscountList) {
		this.catDiscountList = catDiscountList;
	}

	public int[] getCat_ids() {
		return cat_ids;
	}

	public void setCat_ids(int[] cat_ids) {
		this.cat_ids = cat_ids;
	}

	public int[] getCat_discounts() {
		return cat_discounts;
	}

	public void setCat_discounts(int[] cat_discounts) {
		this.cat_discounts = cat_discounts;
	}

	public void setMemberCommentManager(IMemberCommentManager memberCommentManager) {
		this.memberCommentManager = memberCommentManager;
	}

	public int getPointtype() {
		return pointtype;
	}

	public void setPointtype(int pointtype) {
		this.pointtype = pointtype;
	}

	public Map<Integer, String> getPluginTabs() {
		return pluginTabs;
	}

	public void setPluginTabs(Map<Integer, String> pluginTabs) {
		this.pluginTabs = pluginTabs;
	}

	public Map<Integer, String> getPluginHtmls() {
		return pluginHtmls;
	}

	public void setPluginHtmls(Map<Integer, String> pluginHtmls) {
		this.pluginHtmls = pluginHtmls;
	}

	public MemberPluginBundle getMemberPluginBundle() {
		return memberPluginBundle;
	}

	public void setMemberPluginBundle(MemberPluginBundle memberPluginBundle) {
		this.memberPluginBundle = memberPluginBundle;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public Integer[] getMember_id() {
		return member_id;
	}

	public void setMember_id(Integer[] member_id) {
		this.member_id = member_id;
	}

	public Integer getMobile() {
		return mobile;
	}

	public void setMobile(Integer mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Map getMemberMap() {
		return memberMap;
	}

	public void setMemberMap(Map memberMap) {
		this.memberMap = memberMap;
	}

	public IMemberCommentManager getMemberCommentManager() {
		return memberCommentManager;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}



	public Integer getStype() {
		return stype;
	}

	public void setStype(Integer stype) {
		this.stype = stype;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getProvince_id() {
		return province_id;
	}

	public void setProvince_id(Integer province_id) {
		this.province_id = province_id;
	}

	public Integer getCity_id() {
		return city_id;
	}

	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}

	public Integer getRegion_id() {
		return region_id;
	}

	public void setRegion_id(Integer region_id) {
		this.region_id = region_id;
	}

	public Map getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map statusMap) {
		this.statusMap = statusMap;
	}

	public String getStatus_Json() {
		return status_Json;
	}

	public void setStatus_Json(String status_Json) {
		this.status_Json = status_Json;
	}
	public Integer getMember_status() {
		return member_status;
	}
	public void setMember_status(Integer member_status) {
		this.member_status = member_status;
	}
	public String getMember_country() {
		return member_country;
	}
	public void setMember_country(String member_country) {
		this.member_country = member_country;
	}
	public Integer[] getConsume_id() {
		return consume_id;
	}
	public void setConsume_id(Integer[] consume_id) {
		this.consume_id = consume_id;
	}
	public PointConsume getConsume() {
		return consume;
	}
	public void setConsume(PointConsume consume) {
		this.consume = consume;
	}
	public Integer getConsumeId() {
		return consumeId;
	}
	public void setConsumeId(Integer consumeId) {
		this.consumeId = consumeId;
	}
	public IPointConsumeManager getPointConsumeManager() {
		return pointConsumeManager;
	}
	public void setPointConsumeManager(IPointConsumeManager pointConsumeManager) {
		this.pointConsumeManager = pointConsumeManager;
	}
	public RegisterSendPoint getRegisterSendPoint() {
		return registerSendPoint;
	}
	public void setRegisterSendPoint(RegisterSendPoint registerSendPoint) {
		this.registerSendPoint = registerSendPoint;
	}
	public String getSendTimeStart() {
		return sendTimeStart;
	}
	public void setSendTimeStart(String sendTimeStart) {
		this.sendTimeStart = sendTimeStart;
	}
	public String getSendTimeEnd() {
		return sendTimeEnd;
	}
	public void setSendTimeEnd(String sendTimeEnd) {
		this.sendTimeEnd = sendTimeEnd;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIs_store() {
		return is_store;
	}
	public void setIs_store(Integer is_store) {
		this.is_store = is_store;
	}
	public int getMp() {
		return mp;
	}
	public void setMp(int mp) {
		this.mp = mp;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Author:zmm
	 * 以后可能会让转化IP地址为具体国家或城市调用此方法
	 * http://localhost:8080/shop/admin/member!memberIpArea.do
	 * 在Member实体对象中添加属性private String member_country;注册ip地址所在国家和地区
	 * 在表中添加字段alter table es_member add member_country VARCHAR(255) default NULL;
	 * 
	 * @return
	 */
	public String memberIpArea(){
		List<Member> memberList = this.memberManager.queryMemberList();
		if(memberList!=null){
			for(Member m : memberList){
				if(!m.getRegisterip().isEmpty()){
					//当实体对象中有member_country这个字段时调用下面的方法
					//m.setMember_country(this.memberManager.GetAddressByIp(m.getRegisterip()));
					this.memberManager.editMember(m);
				}
			}
			this.showSuccessJson("IP转换地址修改成功！");
		}else{
			this.showErrorJson("IP转换地址失败！");
		}
		return null;
	}
	
}
