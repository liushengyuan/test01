package com.enation.app.shop.core.action.api;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.enation.framework.util.*;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.base.core.service.IRegionsManager;
import com.enation.app.base.core.service.ISmsManager;
import com.enation.app.base.core.service.ISmtpManager;
import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.app.shop.component.bonus.model.BonusType;
import com.enation.app.shop.component.bonus.service.IBonusManager;
import com.enation.app.shop.component.bonus.service.IBonusTypeManager;
import com.enation.app.shop.core.service.IAllianceCountManager;
import com.enation.app.shop.core.service.IMemberPointManger;
import com.enation.app.tradeease.core.service.smsmobile.IsmsMobileManager;
import com.enation.eop.SystemSetting;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.eop.sdk.utils.ValidCodeServlet;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.jms.EmailModel;
import com.enation.framework.jms.EmailProducer;

@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/shop")
@Action("member")
@SuppressWarnings({ "rawtypes", "unchecked", "serial", "static-access" })
public class MemberApiAction extends WWAction {

	private IAllianceCountManager AllianceCountManager;
	private IMemberManager memberManager;
	private IAdminUserManager adminUserManager;
	private IBonusTypeManager bonusTypeManager;
	private IBonusManager bonusManager;
	private IsmsMobileManager smsMobileManager;
	private IRegionsManager regionsManager;
	private String username;
	private String password;
	private String validcode;// 验证码
	private String remember;// 两周内免登录
	private Map memberMap;
	private String license;// 同意注册协议
	private String email;
	private String friendid;

	/**
	 * 上传头像变量
	 */
	private File faceFile;
	private File face; // 633行有单独的上传头像，没动，酌情处理，需要这里，
	private String faceFileName;

	private String faceFileFileName;

	private String photoServer;
	private String photoId;
	private String type;
	private String turename;

	/**
	 * 更改密码
	 */
	private String oldpassword;
	private String newpassword;
	private String re_passwd;

	private Integer province_id;
	private Integer city_id;
	private Integer region_id;
	private String province;
	private String city;
	private String region;
	private String address;
	private String zip;
	private String mobile;
	private String tel;
	private String nickname;
	private String sex;
	private String mybirthday;
	/**
	 * 搜索
	 */
	private Integer lvid; // 会员级别id
	private String keyword; // 关键词
	/**
	 * 重新发送激活邮件
	 */
	private JavaMailSender mailSender;
	private EmailProducer mailMessageProducer;
	private IMemberPointManger memberPointManger;

	private File file;
	private String fileFileName;

	private ISmsManager smsManager;
	// 发送邮件
	// private EmailProducer mailMessageProducer;
	private ISmtpManager smtpManager;
	private Integer member_id;

	public ISmtpManager getSmtpManager() {
		return smtpManager;
	}

	public void setSmtpManager(ISmtpManager smtpManager) {
		this.smtpManager = smtpManager;
	}
	public IsmsMobileManager getSmsMobileManager() {
		return smsMobileManager;
	}

	public void setSmsMobileManager(IsmsMobileManager smsMobileManager) {
		this.smsMobileManager = smsMobileManager;
	}
	/**
	 * 会员登录
	 * 
	 * @param username
	 *            :用户名,String型
	 * @param password
	 *            :密码,String型
	 * @param validcode
	 *            :验证码,String型
	 * @param remember
	 *            :两周内免登录,String型
	 * @return json字串 result 为1表示登录成功，0表示失败 ，int型 message 为提示信息 ，String型
	 */
	public String login() {
		/*
		 * HttpSession session =
		 * ThreadContextHolder.getHttpRequest().getSession(); Locale locale =
		 * (Locale) session.getAttribute("locale"); String language =
		 * locale.getLanguage();
		 */
		String LoginSuccess = this.getText("login.LoginSuccess");
		String AccountNo = this.getText("login.AccountNo");
		String PasswordError = this.getText("login.PasswordError");
		String VerificationError = this.getText("login.VerificationError");
		String IsCheck = this.getText("login.IsCheck");
		if (this.validcode(validcode, "memberlogin") == 1) {
			int result = this.memberManager.login(username, password);
			if (result == 1) {
				// 两周内免登录
				if (remember != null && remember.equals("1")) {
					String cookieValue = EncryptionUtil1.authcode(
							"{username:\"" + username + "\",password:\""
									+ password + "\"}",
							"ENCODE", "", 0);
					HttpUtil.addCookie(ThreadContextHolder.getHttpResponse(),
							"JavaShopUser", cookieValue, 60 * 24 * 7 * 60);
				}else{
					this.removeCookie();
				}
				this.showSuccessJson(LoginSuccess);
				/*************************** 流量统计 *****************************/
				HttpServletRequest request  = this.getRequest();
				HttpSession session = request.getSession();
				//判断该session_id是否存在，存在则把member_id存入es_flow_count
				if(AllianceCountManager.haveSession(session.getId()).size() > 0){
					AllianceCountManager.editMember_id(username, session.getId());
				}
			} else if (result == -1) {
				this.showErrorJson(IsCheck);
			} else {
				Member member = memberManager.getMemberByUname(username);
				if (member == null) {
					this.showErrorJson(AccountNo);
				} else {
					this.showErrorJson(PasswordError);
				}
			}
		} else {
			this.showErrorJson(VerificationError);
		}
		return WWAction.JSON_MESSAGE;
	}
	public String sendCode1() {
        try {
            String mobileCode = "" + (int) ((Math.random() * 9 + 1) * 100000);
            ThreadContextHolder.getSessionContext().setAttribute("mobileCode", mobileCode);
            String content ="尊敬的用户，您的验证码为" + mobileCode + ",120秒后失效,请尽快验证";
            this.smsMobileManager.sendMobile(this.mobile, content);
            this.showSuccessJson(mobileCode);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            this.showErrorJson("发送失败");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.showErrorJson("发送失败");
        }
        return this.JSON_MESSAGE;
    }
	/**
	 * 注销会员登录
	 * 
	 * @param 无
	 * @return json字串 result 为1表示注销成功，0表示失败 ，int型 message 为提示信息 ，String型
	 */
	public String logout() {
		/*
		 * HttpSession session =
		 * ThreadContextHolder.getHttpRequest().getSession(); Locale locale =
		 * (Locale) session.getAttribute("locale"); String language =
		 * locale.getLanguage();
		 */
		String zhuxiao = this.getText("login.logout");
		this.memberManager.logout();
		this.removeCookie();
		this.showSuccessJson(zhuxiao);
		return WWAction.JSON_MESSAGE;
	}
	
	/**
	 * 退出删除Cookie
	 */
	public void removeCookie(){
		String username1=null;
		String cookieValue = HttpUtil.getCookieValue(
				ThreadContextHolder.getHttpRequest(), "JavaShopUser");
		if (cookieValue != null && !cookieValue.equals("")) {
			try {
				cookieValue = URLDecoder.decode(cookieValue,
						"UTF-8");
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
			}
			cookieValue = EncryptionUtil1.authcode(cookieValue,
					"DECODE", "", 0);
			Map map = (Map) JSONObject.toBean(
					JSONObject.fromObject(cookieValue),
					Map.class);
			if (map != null) {
				username1 = map.get("username")
						.toString();
				
			}
			cookieValue = EncryptionUtil1.authcode(
					"{username:\"" + username1 + "\",password:\""
							+ "" + "\"}",
					"ENCODE", "", 0);
			HttpUtil.addCookie(ThreadContextHolder.getHttpResponse(),
					"JavaShopUser", cookieValue, 60 * 24 * 7 * 60);
			
		}
		
	
	}
	/**
	 * 邮箱激活后直接进行登录
	 * @return
	 */
	public String loginForEmail() {
		/*
		 * HttpSession session =
		 * ThreadContextHolder.getHttpRequest().getSession(); Locale locale =
		 * (Locale) session.getAttribute("locale"); String language =
		 * locale.getLanguage();
		 */
		String LoginSuccess = this.getText("login.LoginSuccess");
		String AccountNo = this.getText("login.AccountNo");
		String PasswordError = this.getText("login.PasswordError");
		String VerificationError = this.getText("login.VerificationError");
		String IsCheck = this.getText("login.IsCheck");
			int result = this.memberManager.login(username, password);
			if (result == 1) {
				// 两周内免登录
				if (remember != null && remember.equals("1")) {
					String cookieValue = EncryptionUtil1.authcode(
							"{username:\"" + username + "\",password:\""
									+ password + "\"}",
							"ENCODE", "", 0);
					HttpUtil.addCookie(ThreadContextHolder.getHttpResponse(),
							"JavaShopUser", cookieValue, 60 * 24 * 7 * 60);
				}else{
					this.removeCookie();
				}
				this.showSuccessJson(LoginSuccess);
				/*************************** 流量统计 *****************************/
				HttpServletRequest request  = this.getRequest();
				HttpSession session = request.getSession();
				//判断该session_id是否存在，存在则把member_id存入es_flow_count
				if(AllianceCountManager.haveSession(session.getId()).size() > 0){
					AllianceCountManager.editMember_id(username, session.getId());
				}
			} else if (result == -1) {
				this.showErrorJson(IsCheck);
			} else {
				Member member = memberManager.getMemberByUname(username);
				if (member == null) {
					this.showErrorJson(AccountNo);
				} else {
					this.showErrorJson(PasswordError);
				}
			}
		return WWAction.JSON_MESSAGE;
	}
	/**
	 * 修改会员密码
	 * 
	 * @param oldpassword
	 *            :原密码,String类型
	 * @param newpassword
	 *            :新密码,String类型
	 * @return json字串 result 为1表示修改成功，0表示失败 ，int型 message 为提示信息 ，String型
	 */
	public String changePassword() {
/*
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		String nologin=this.getText("login.nologin");
		String same=this.getText("login.same");
		String changeSuccess=this.getText("login.changeSuccess");
		String changeFail=this.getText("login.changeFail");
		String twoString=this.getText("login.twoDifferent");
		String noSame=this.getText("login.noSame");
		Member member = UserConext.getCurrentMember();
		if (member == null) {
			this.showErrorJson(nologin);
			return this.JSON_MESSAGE;
		}
		String oldPassword = this.getOldpassword();
		// oldPassword = oldPassword == null ? "" : StringUtil.md5(oldPassword);

		if (checkPassword(member.getPassword(), oldPassword)) {
			String password = this.getNewpassword();
			String passwd_re = this.getRe_passwd();
			if (oldPassword.equals(password)) {
					this.showErrorJson(same);
					return this.JSON_MESSAGE;
			}

			if (passwd_re.equals(password)) {
				try {
					memberManager.updatePassword(password);
					this.showSuccessJson(changeSuccess);
				} catch (Exception e) {
					if (this.logger.isDebugEnabled()) {
						logger.error(e.getStackTrace());
					}
					this.showErrorJson(changeFail);
				}
			} else {
					this.showErrorJson(twoString);

			}
		} else {
				this.showErrorJson(noSame);
		}
		return WWAction.JSON_MESSAGE;
	}

	/**
	 * 验证原密码输入是否正确
	 * 
	 * @param oldpassword
	 *            :密码，String型
	 * @return json字串 result 为1表示原密码正确，0表示失败 ，int型 message 为提示信息 ，String型
	 */
	public String password() {
		String zhengque=this.getText("login.zhengque");
		String yuanshi=this.getText("login.yuanshi");
		Member member = UserConext.getCurrentMember();
		String old = oldpassword;
		String oldPassword = StringUtil.md5(old);
		if (oldPassword.equals(member.getPassword())) {
			this.showSuccessJson(zhengque);
		} else {
			this.showErrorJson(yuanshi);
		}
		return WWAction.JSON_MESSAGE;
	}

	/**
	 * 搜索会员(要求管理员身份)
	 * 
	 * @param lvid
	 *            :会员级别id，如果为空则搜索全部会员级别，int型
	 * @param keyword
	 *            :搜索关键字,可为空，String型
	 * @return json字串 result 为1表示搜索成功，0表示失败 ，int型 data: 会员列表 {@link Member}
	 */
	public String search() {
		String fangwen=this.getText("login.wuquan");
		String sousuo=this.getText("login.sousuo");
		try {
			if (UserConext.getCurrentAdminUser() == null) {
				this.showErrorJson(fangwen);
				return this.JSON_MESSAGE;
			}
			memberMap = new HashMap();
			memberMap.put("lvId", lvid);
			memberMap.put("keyword", keyword);
			memberMap.put("stype", 0);
			List memberList = this.memberManager.search(memberMap);
			this.json = JsonMessageUtil.getListJson(memberList);
		} catch (Throwable e) {
			this.logger.error("搜索会员出错", e);
			this.showErrorJson(sousuo);

		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 检测username是否存在，并生成json返回给客户端
	 */
	public String checkname() {
		String shiyong=this.getText("login.shiyong");
		String cunzai=this.getText("login.cunzai");
		int result = this.memberManager.checkname(username);
		if (result == 0) {
			this.showSuccessJson(shiyong);
		} else {
			this.showErrorJson(cunzai);
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 检测mobil是否存在，并生成json返回给客户端
	 * 
	 */
	public String checkmobile(){
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
	    String	mobile=request.getParameter("mobile");
		int result=this.memberManager.checkMobile(mobile);
		if(result==0){
			this.showSuccessJson("0");
		}else{
			this.showErrorJson("1");
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 检测email是否存在，并生成json返回给客户端
	 */
	public String checkemail() {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
	    String	email=request.getParameter("email");
		int result = this.memberManager.checkemail(email);
		if (result == 0) {
			this.showSuccessJson("0");
		} else {
			this.showErrorJson("1");
		}
		return this.JSON_MESSAGE;
	}

	public String sendCode() {
		String yanzheng=this.getText("login.yanzheng");
		String send=this.getText("login.send"); 
		String already=this.getText("login.already");
		String shibai=this.getText("login.sendFail");
		try {
			String mobileCode = "" + (int) ((Math.random() * 9 + 1) * 100000);
			ThreadContextHolder.getSessionContext().setAttribute("mobileCode",
					mobileCode);
			String content = yanzheng+"：【" + mobileCode + "】";
			int result = this.memberManager.checkMobile(mobile);
			if (result == 0) {
				this.smsManager.send(mobile, content);
				this.showSuccessJson(send);
			} else {
				this.showErrorJson(already);
			}
		} catch (RuntimeException e) {
			//System.out.println(e.getMessage());
			this.showErrorJson(shibai);
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			this.showErrorJson(shibai);
		}
		return this.JSON_MESSAGE;
	}

	public String regMobile() {
		String Fail=this.getText("login.yanzhengFail");
		String yizhuce=this.getText("login.yizhuce");
		String tongyi=this.getText("login.tongyi");
		String noNull=this.getText("login.noNull");
		String zhuceSuccess=this.getText("login.zhuceSuccess");
		String username=this.getText("login.username");
		String alreadyExist=this.getText("login.alreadyExist");
		if (this.validmobile_code(validcode, mobile) == 0) {
			this.showErrorJson(Fail);
			return this.JSON_MESSAGE;
		}

		if (this.memberManager.checkMobile(mobile) == 1) {
			this.showErrorJson(yizhuce);
			return this.JSON_MESSAGE;
		}

		if (!"agree".equals(license)) {
			this.showErrorJson(tongyi);
			return this.JSON_MESSAGE;
		}

		if (StringUtil.isEmpty(password)) {
			this.showErrorJson(noNull);
			return this.JSON_MESSAGE;
		}

		Member member = new Member();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String registerip = request.getRemoteAddr();

		member.setMobile("");
		member.setUname(mobile);
		member.setPassword(password);
		member.setEmail("");
		member.setRegisterip(registerip);

		int result = memberManager.register(member);
		if (result == 1) { // 添加成功
			this.memberManager.login(mobile, password);
			this.showSuccessJson(zhuceSuccess);
		} else {
			this.showErrorJson(username+"[" + member.getUname() + "]"+alreadyExist);
		}

		return JSON_MESSAGE;
	}

	/**
	 * 会员注册
	 */
	public String register() {
	/*	HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		String code = this.getText("login.code");
		String agree = this.getText("login.agree");
		String emailVerification = this.getText("login.emailVerification");
		String emailVerification2 =this.getText("login.emailVerification2");
		String emailVerification3 =this.getText("login.emailVerification3");
		String cicunzai =this.getText("login.cicunzai");
		String noNull =this.getText("login.noNull");
		if (this.validcode(validcode, "memberreg") == 0) {
			this.showErrorJson(code);
			return this.JSON_MESSAGE;
		}
		if (!"agree".equals(license)) {
			this.showErrorJson(agree);
			return this.JSON_MESSAGE;
		}

		Member member = new Member();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String registerip = request.getRemoteAddr();

		/*
		 * if (StringUtil.isEmpty(username)) { this.showErrorJson("用户名不能为空！");
		 * return this.JSON_MESSAGE; } if (username.length() < 4 ||
		 * username.length() > 20) { this.showErrorJson("用户名的长度为4-20个字符！");
		 * return this.JSON_MESSAGE; } if (username.contains("@")) {
		 * this.showErrorJson("用户名中不能包含@等特殊字符！"); return this.JSON_MESSAGE; }
		 */
		if (StringUtil.isEmpty(email)) {
			this.showErrorJson(emailVerification2);
			return this.JSON_MESSAGE;
		}
		if (!StringUtil.validEmail(email)) {
			this.showErrorJson(emailVerification3);
			return this.JSON_MESSAGE;
		}
		if (StringUtil.isEmpty(password)) {
			this.showErrorJson(noNull);
			return this.JSON_MESSAGE;
		}
		if (memberManager.checkname(username) > 0) {
			this.showErrorJson(cicunzai);
			return this.JSON_MESSAGE;
		}
		if (memberManager.checkemail(email) > 0) {
			this.showErrorJson(emailVerification);
			return this.JSON_MESSAGE;
		}

		member.setMobile("");
		member.setUname(email);
		member.setPassword(password);
		member.setEmail(email);
		member.setRegisterip(registerip);
		member.setMember_prove_type(1);
		if (!StringUtil.isEmpty(friendid)) {
			Member parentMember = this.memberManager.get(Integer
					.parseInt(friendid));
			if (parentMember != null) {
				member.setParentid(parentMember.getMember_id());
			}
		} else {
			// 不是推荐链接 检测是否有填写推荐人
			String tuijian =this.getText("login.tuijian");
			String tuijianNo =this.getText("login.tuijianNo");
			String reg_Recomm = request.getParameter("reg_Recomm");
			if (!StringUtil.isEmpty(reg_Recomm)
					&& reg_Recomm.trim().equals(email.trim())) {
				this.showErrorJson(tuijian);
				return this.JSON_MESSAGE;
			}
			if (!StringUtil.isEmpty(reg_Recomm)
					&& StringUtil.validEmail(reg_Recomm)) {
				Member parentMember = this.memberManager
						.getMemberByEmail(reg_Recomm);
				if (parentMember == null) {
					this.showErrorJson(tuijianNo);
					return this.JSON_MESSAGE;
				} else {
					member.setParentid(parentMember.getMember_id());
				}
			}
		}

		int result = memberManager.register(member);
		if (result == 1) { // 添加成功
			//0 根据优惠券发放类型发送优惠券
			List<BonusType> btlist = this.bonusTypeManager.queryForBonustype(0);
			if(btlist!=null && btlist.size()>0){
				for(BonusType bonusType : btlist){
					if (bonusType.getSend_start_date() < member.getRegtime()
							&& member.getRegtime() < bonusType.getSend_end_date()) {
						// 2发放红包
						Integer[] memberids = { member.getMember_id() };
						int count = this.bonusManager.sendForMember(
								bonusType.getType_id(), memberids);
						if (count > 0) {
							System.out.println("优惠券发放成功");
						}
					}
				}
			}
			// 1根据优惠券金额查询
			/*BonusType bonusType = this.bonusTypeManager.getBonusType(650.0);
			if (bonusType != null) {
				if (bonusType.getSend_start_date() < member.getRegtime()
						&& member.getRegtime() < bonusType.getSend_end_date()) {
					// 2发放红包
					Integer[] memberids = { member.getMember_id() };
					int count = this.bonusManager.sendForMember(
							bonusType.getType_id(), memberids);
					if (count > 0) {
						System.out.println("红包发放成功");
					}
				}
			}*/

			// this.memberManager.login(username, password);------注册完成后立即登录
			// String forward = request.getParameter("forward");
			String mailurl = "http://mail."
					+ StringUtils.split(member.getEmail(), "@")[1];
			/*
			 * if (forward != null && !forward.equals("")) { String message =
			 * request.getParameter("message"); this.setMsg(message); } else {
			 * this.setMsg("注册成功"); }
			 */

			//this.sendEmailToAdmin(member);  有了验证邮箱的链接，这里可以注掉
			
			/*************************** 流量统计 *****************************/
			HttpSession session = request.getSession();
			if(AllianceCountManager.haveSession(session.getId()).size() > 0){
				AllianceCountManager.editMember_id(username, session.getId());
			}
			this.json = JsonMessageUtil.getStringJson("mailurl", mailurl);
		} else {
			String username =this.getText("login.username");
			String alreadyExist =this.getText("login.alreadyExist");
			this.showErrorJson(username+"[" + member.getUname() + "]"+alreadyExist);
		}
		return this.JSON_MESSAGE;
	}

	@SuppressWarnings("unchecked")
	public void sendEmailToAdmin(Member member) {
		String ninhao=this.getText("login.ninhao");
		String huiyuanzhuce=this.getText("login.huiyuanzhuce");
		String zhuceSuccess=this.getText("login.zhuceSuccess");
		EopSite site = EopSite.getInstance();
		EmailModel emailModel = new EmailModel();
		String domain = RequestUtil.getDomain();
		emailModel.getData().put("logo", site.getLogofile());
		emailModel.getData().put("sitename", site.getSitename());
		emailModel.getData().put("username", member.getUname());
		emailModel.getData().put("send_time",
				DateUtil.toString(new Date(), "yyyy年MM月dd日  hh:mm:ss"));
		//System.out.println(emailModel.getData().get("time"));
		emailModel.getData().put("domain", domain);
		emailModel.setTitle(member.getUname() + ninhao + site.getSitename()
				+ huiyuanzhuce);
		emailModel.setEmail(member.getEmail());
		// emailModel.setEmail(this.smtpManager.get(2).getUsername());
		emailModel.setTemplate("zhuce_email_template.html");
		emailModel.setEmail_type(zhuceSuccess);
		mailMessageProducer.send(emailModel);
	}

	/**
	 * 重新发送激活邮件
	 */
	public String reSendRegMail() {
		String qingxiandenglu=this.getText("login.qingxiandenglu");
		String chongfa=this.getText("login.chongfa");
		String two=this.getText("login.two");
		try {
			// 重新发送激活邮件
			Member member = UserConext.getCurrentMember();
			if (member == null) {
				this.showErrorJson(qingxiandenglu);
				return this.JSON_MESSAGE;
			}
			member = memberManager.get(member.getMember_id());
			if (member == null) {
				this.showErrorJson(chongfa);
				return this.JSON_MESSAGE;
			}
			if (member.getLast_send_email() != null
					&& System.currentTimeMillis() / 1000
							- member.getLast_send_email().intValue() < 2 * 60 * 60) {
				this.showErrorJson(two);
				return this.JSON_MESSAGE;
			}

			EopSite site = EopSite.getInstance();
			String domain = RequestUtil.getDomain();
			String checkurl = domain
					+ "/memberemailcheck.html?s="
					+ EncryptionUtil1.authcode(member.getMember_id() + ","
							+ member.getRegtime(), "ENCODE", "", 0);
			EmailModel emailModel = new EmailModel();
			emailModel.getData().put("username", member.getUname());
			emailModel.getData().put("checkurl", checkurl);
			emailModel.getData().put("sitename", site.getSitename());
			emailModel.getData().put("logo", site.getLogofile());
			emailModel.getData().put("domain", domain);
			if (memberPointManger
					.checkIsOpen(IMemberPointManger.TYPE_EMIAL_CHECK)) {
				int point = memberPointManger
						.getItemPoint(IMemberPointManger.TYPE_EMIAL_CHECK
								+ "_num");
				int mp = memberPointManger
						.getItemPoint(IMemberPointManger.TYPE_EMIAL_CHECK
								+ "_num_mp");
				emailModel.getData().put("point", point);
				emailModel.getData().put("mp", mp);
			}
			String ninhao=this.getText("login.ninhao");
			String huiyuanzhuce=this.getText("login.huiyuanzhuce");
			String emailLive=this.getText("login.emailLive");
			String jihuo=this.getText("login.jihuo");
			String chashou=this.getText("login.chashou");
			emailModel.setTitle(member.getUname() + ninhao + site.getSitename()
					+huiyuanzhuce);
			emailModel.setEmail(member.getEmail());
			emailModel.setTemplate("reg_email_template.html");
			emailModel.setEmail_type(emailLive);
			mailMessageProducer.send(emailModel);
			member.setLast_send_email(DateUtil.getDateline());
			memberManager.edit(member);
			this.showSuccessJson(jihuo + member.getEmail()
					+chashou);
		} catch (RuntimeException e) {
			this.showErrorJson(e.getMessage());
		}
		return this.JSON_MESSAGE;
	}

	public String updateInfo() {
		String bianjiSuccess="恭喜您绑定手机成功，50个积分已存入您的账户，请注意查收！";
		String bianjiFail=this.getText("login.bianjiFail");
		Member member = UserConext.getCurrentMember();
		member.getMember_id();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		int member_id=member.getMember_id();
		String	mobile=request.getParameter("mobile");
		 String	email=request.getParameter("email");
			 int result = this.memberManager.checkMobileByMobile(mobile.trim(), member_id);
				if(result!=1){
					if(this.memberManager.checkMobileBy(mobile)>0){
						this.showErrorJson("手机号添加重复，请重新输入");
						return this.JSON_MESSAGE;
					}
				}
			 int result1 = this.memberManager.checkemailByMobile(email,member_id);
				if(result1!=1){
					if(this.memberManager.checkemailBy(email)>0){
						this.showErrorJson("邮箱添加重复，请重新输入");
						return this.JSON_MESSAGE;
					}
				}
		if (!StringUtil.isEmpty(request.getParameter("email"))) {
			member.setEmail(request.getParameter("email"));
			member.setUname(request.getParameter("email"));
		}
		if (!StringUtil.isEmpty(request.getParameter("name"))) {
		member.setName(request.getParameter("name"));
		}
		member.setSex(Integer.valueOf(request.getParameter("sex")));

		member.setMember_country(request.getParameter("member_country"));

		member.setMember_prove_type(Integer.valueOf(request
				.getParameter("member_prove_type")));
		if (!StringUtil.isEmpty(request.getParameter("id_number"))) {
			member.setId_number(request.getParameter("id_number"));
		}
		if (!StringUtil.isEmpty(request.getParameter("mobile"))) {
			member.setMobile(request.getParameter("mobile"));
		}
		
		Integer is_moblie=this.memberManager.checkSendIsCheck(member_id);
		if(is_moblie==1){
			bianjiSuccess="保存成功";
		}else{
			member.setIs_mobile(1);
			member.setMp(member.getMp()+50);
		}
		try {
			
			memberManager.edit(member);
			this.showSuccessJson(bianjiSuccess);
		} catch (Exception e) {

			this.showErrorJson(bianjiFail);
		}
		return this.JSON_MESSAGE;
	}

	public String saveInfo() {
		String sorryShang=this.getText("login.sorryShang");
		Member member = UserConext.getCurrentMember();

		member = memberManager.get(member.getMember_id());

		// 先上传图片
		String faceField = "faceFile";

		if (file != null) {

			// 判断文件类型
			String allowTYpe = "gif,jpg,bmp,png";
			if (!fileFileName.trim().equals("") && fileFileName.length() > 0) {
				String ex = fileFileName.substring(
						fileFileName.lastIndexOf(".") + 1,
						fileFileName.length());
				if (allowTYpe.toString().indexOf(ex.toLowerCase()) < 0) {
					this.showErrorJson(sorryShang);
					return this.JSON_MESSAGE;
				}
			}

			// 判断文件大小
			String nodayu=this.getText("login.nodayu");
			if (file.length() > 200 * 1024) {
				this.showErrorJson(nodayu);
				return this.JSON_MESSAGE;
			}

			String imgPath = UploadUtil.upload(file, fileFileName, faceField);
			member.setFace(imgPath);
		}

		HttpServletRequest request = ThreadContextHolder.getHttpRequest();

		if (StringUtil.isEmpty(mybirthday)) {
			member.setBirthday(0L);
		} else {
			member.setBirthday(DateUtil.getDateline(mybirthday));
		}

		member.setProvince_id(province_id);
		member.setCity_id(city_id);
		member.setRegion_id(region_id);
		member.setProvince(province);
		member.setCity(city);
		member.setRegion(region);
		member.setAddress(address);
		member.setZip(zip);
		if (mobile != null) {
			member.setMobile(mobile);
		}
		member.setTel(tel);
		if (nickname != null) {
			member.setNickname(nickname);
		}
		member.setSex(Integer.valueOf(sex));

		// 身份
		String midentity = request.getParameter("member.midentity");
		if (!StringUtil.isEmpty(midentity)) {
			member.setMidentity(StringUtil.toInt(midentity));
		} else {
			member.setMidentity(0);
		}
		// String pw_question = request.getParameter("member.pw_question");
		// member.setPw_question(pw_question);
		// String pw_answer = request.getParameter("member.pw_answer");
		// member.setPw_answer(pw_answer);
		try {
			// 判断否需要增加积分
			boolean addPoint = false;
			if (member.getInfo_full() == 0
					&& !StringUtil.isEmpty(member.getName())
					&& !StringUtil.isEmpty(member.getNickname())
					&& !StringUtil.isEmpty(member.getProvince())
					&& !StringUtil.isEmpty(member.getCity())
					&& !StringUtil.isEmpty(member.getRegion())
					&& (!StringUtil.isEmpty(member.getMobile()) || !StringUtil
							.isEmpty(member.getTel()))) {
				addPoint = true;
			}
			// 增加积分
			if (addPoint) {
				member.setInfo_full(1);
				memberManager.edit(member);
				String wanshan=this.getText("login.wanshan");
				if (memberPointManger
						.checkIsOpen(IMemberPointManger.TYPE_FINISH_PROFILE)) {
					int point = memberPointManger
							.getItemPoint(IMemberPointManger.TYPE_FINISH_PROFILE
									+ "_num");
					int mp = memberPointManger
							.getItemPoint(IMemberPointManger.TYPE_FINISH_PROFILE
									+ "_num_mp");
					memberPointManger.add(member.getMember_id(), point,
							wanshan, null, mp);
				}
			} else {
				memberManager.edit(member);
			}
			String success=this.getText("login.bianjiSuccess");
			this.showSuccessJson(success);
			return this.JSON_MESSAGE;
		} catch (Exception e) {
			String fail=this.getText("login.bianjiFail");
			if (this.logger.isDebugEnabled()) {
				logger.error(e.getStackTrace());
			}
			this.showErrorJson(fail);

			return this.JSON_MESSAGE;
		}
	}

	// ************to宏俊：以api先不用书写文档****************/
	protected String toUrl(String path) {
		String static_server_domain = SystemSetting.getStatic_server_domain();
		String urlBase = static_server_domain;
		return path.replaceAll("fs:", urlBase);
	}

	protected String makeFilename(String subFolder) {
		String ext = FileUtil.getFileExt(photoServer);
		String fileName = photoId + "_" + type + "." + ext;
		String static_server_path = SystemSetting.getStatic_server_path();

		String filePath = static_server_path + "/attachment/";
		if (subFolder != null) {
			filePath += subFolder + "/";
		}

		filePath += fileName;
		return filePath;
	}

	/**
	 * 保存从Flash编辑后返回的头像，保存二次，一大一小两个头像
	 * 
	 * @return
	 */
	public String saveAvatar() {
		String targetFile = makeFilename("avatar");

		int potPos = targetFile.lastIndexOf('/') + 1;
		String folderPath = targetFile.substring(0, potPos);
		FileUtil.createFolder(folderPath);

		try {
			File file = new File(targetFile);

			if (!file.exists()) {
				file.createNewFile();
			}

			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			FileOutputStream dos = new FileOutputStream(file);
			int x = request.getInputStream().read();
			while (x > -1) {
				dos.write(x);
				x = request.getInputStream().read();
			}
			dos.flush();
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("big".equals(type)) {
			Member member = UserConext.getCurrentMember();
			member.setFace("fs:/attachment/avatar/" + photoId + "_big."
					+ FileUtil.getFileExt(photoServer));
			memberManager.edit(member);
		}
		String save=this.getText("login.save");

		json = "{\"data\":{\"urls\":[\"" + targetFile
				+ "\"]},\"status\":1,\"statusText\":\""+save+"\"}";

		return WWAction.JSON_MESSAGE;
	}

	/**
	 * 上传头像文件
	 * 
	 * @return
	 */
	public String uploadAvatar() {
		JSONObject jsonObject = new JSONObject();
		String caozuoSuccess=this.getText("login.caozuoSuccess");
		String caozuoFail=this.getText("login.caozuoFail");

		try {
			if (faceFile != null) {
				String file = UploadUtil.upload(face, faceFileName, "avatar");
				Member member = UserConext.getCurrentMember();
				jsonObject.put("result", 1);
				jsonObject.put("member_id", member.getMember_id());
				jsonObject.put("url", toUrl(file));
				jsonObject.put("message", caozuoSuccess);
			}
		} catch (Exception e) {
			jsonObject.put("result", 0);
			jsonObject.put("message", caozuoFail);
		}

		this.json = jsonObject.toString();

		return WWAction.JSON_MESSAGE;
	}

	/**
	 * 校验验证码
	 * 
	 * @param validcode
	 * @param name
	 *            (1、memberlogin:会员登录 2、memberreg:会员注册)
	 * @return 1成功 0失败
	 */
	private int validcode(String validcode, String name) {
		// 管理员登录账号。不需要验证码
		if (name.equals("memberlogin") && username.equals("admin@qq.com")) {
			return 1;
		}
		if (validcode == null) {
			return 0;
		}

		String code = (String) ThreadContextHolder.getSessionContext()
				.getAttribute(ValidCodeServlet.SESSION_VALID_CODE + name);
		if (code == null) {
			return 0;
		} else {
			if (!code.equalsIgnoreCase(validcode)) {
				return 0;
			}
		}
		return 1;
	}

	private int validmobile_code(String validcode, String mobile) {
		if (validcode == null) {
			return 0;
		}
		String code = (String) ThreadContextHolder.getSessionContext()
				.getAttribute("mobileCode");
		if (code == null) {
			return 0;
		} else {
			if (!code.equalsIgnoreCase(validcode)) {
				return 0;
			}
		}
		return 1;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public File getFace() {
		return face;
	}

	public void setFace(File face) {
		this.face = face;
	}

	public String getFaceFileName() {
		return faceFileName;
	}

	public void setFaceFileName(String faceFileName) {
		this.faceFileName = faceFileName;
	}

	public String getPhotoServer() {
		return photoServer;
	}

	public void setPhotoServer(String photoServer) {
		this.photoServer = photoServer;
	}

	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOldpassword() {
		return oldpassword;
	}

	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	public String getRe_passwd() {
		return re_passwd;
	}

	public void setRe_passwd(String re_passwd) {
		this.re_passwd = re_passwd;
	}

	public Integer getLvid() {
		return lvid;
	}

	public void setLvid(Integer lvid) {
		this.lvid = lvid;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public IAdminUserManager getAdminUserManager() {
		return adminUserManager;
	}

	public void setAdminUserManager(IAdminUserManager adminUserManager) {
		this.adminUserManager = adminUserManager;
	}

	public String getValidcode() {
		return validcode;
	}

	public void setValidcode(String validcode) {
		this.validcode = validcode;
	}

	public String getRemember() {
		return remember;
	}

	public void setRemember(String remember) {
		this.remember = remember;
	}

	public Map getMemberMap() {
		return memberMap;
	}

	public void setMemberMap(Map memberMap) {
		this.memberMap = memberMap;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFriendid() {
		return friendid;
	}

	public void setFriendid(String friendid) {
		this.friendid = friendid;
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public EmailProducer getMailMessageProducer() {
		return mailMessageProducer;
	}

	public void setMailMessageProducer(EmailProducer mailMessageProducer) {
		this.mailMessageProducer = mailMessageProducer;
	}

	public IMemberPointManger getMemberPointManger() {
		return memberPointManger;
	}

	public void setMemberPointManger(IMemberPointManger memberPointManger) {
		this.memberPointManger = memberPointManger;
	}

	public File getFaceFile() {
		return faceFile;
	}

	public void setFaceFile(File faceFile) {
		this.faceFile = faceFile;
	}

	public String getTurename() {
		return turename;
	}

	public void setTurename(String turename) {
		this.turename = turename;
	}

	public IRegionsManager getRegionsManager() {
		return regionsManager;
	}

	public void setRegionsManager(IRegionsManager regionsManager) {
		this.regionsManager = regionsManager;
	}

	public String getFaceFileFileName() {
		return faceFileFileName;
	}

	public void setFaceFileFileName(String faceFileFileName) {
		this.faceFileFileName = faceFileFileName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMybirthday() {
		return mybirthday;
	}

	public void setMybirthday(String mybirthday) {
		this.mybirthday = mybirthday;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public ISmsManager getSmsManager() {
		return smsManager;
	}

	public void setSmsManager(ISmsManager smsManager) {
		this.smsManager = smsManager;
	}

	public IBonusTypeManager getBonusTypeManager() {
		return bonusTypeManager;
	}

	public void setBonusTypeManager(IBonusTypeManager bonusTypeManager) {
		this.bonusTypeManager = bonusTypeManager;
	}

	public IBonusManager getBonusManager() {
		return bonusManager;
	}

	public void setBonusManager(IBonusManager bonusManager) {
		this.bonusManager = bonusManager;
	}

	private static boolean checkPassword(String oldPassword,
			String currentPassword) {
		boolean passwordMatches = false;
		if (oldPassword != null) {
			passwordMatches = HashCrypt.comparePassword(oldPassword, "SHA",
					currentPassword);
		}
		if (!passwordMatches) {
			passwordMatches = currentPassword.equals(oldPassword);
		}
		return passwordMatches;
	}

	public IAllianceCountManager getAllianceCountManager() {
		return AllianceCountManager;
	}

	public void setAllianceCountManager(IAllianceCountManager allianceCountManager) {
		AllianceCountManager = allianceCountManager;
	}

}
