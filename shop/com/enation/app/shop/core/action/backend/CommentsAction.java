package com.enation.app.shop.core.action.backend;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.MemberComment;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.IMemberCommentManager;
import com.enation.app.shop.core.service.IMemberOrderItemManager;
import com.enation.app.shop.core.service.IMemberPointManger;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.jms.EmailModel;
import com.enation.framework.jms.EmailProducer;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 产品评论管理
 * 
 * @author Dawei
 * @author LiFenLong 2014-4-1; 4.0版本改造
 */


@Component
@Scope("prototype")
@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Action("comments")
@Results({
	@Result(name="bglist", type="freemarker", location="/shop/admin/comments/list.html"),
	@Result(name="gmlist", type="freemarker", location="/shop/admin/comments/gm_list.html"),
	@Result(name="detail", type="freemarker", location="/shop/admin/comments/detail.html"),
	@Result(name="detail2", type="freemarker", location="/shop/admin/comments/detail2.html"),
	@Result(name="auditCommentsPage", type="freemarker", location="/shop/admin/comments/audit_comments.html"),
})
public class CommentsAction extends WWAction {

	private IMemberCommentManager memberCommentManager;
	private IMemberPointManger memberPointManger;
	private IMemberOrderItemManager memberOrderItemManager;
	private IMemberManager memberManager;
	private IGoodsManager goodsManager;

	// 评论类型:1为评论，2为咨询
	private int type;
	private int status;
	

	private int commentId;

	private MemberComment memberComment;

	private Member member;

	private String reply;

	private Integer[] comment_id;
	
	private String goodsName;
	

	private Map commentsMap;
	
	private int searchtype;
	
	private String auditDiscribe;//审核理由
	
	// 发送邮件
	private EmailProducer mailMessageProducer;
	/**
	 * 评论(咨询)列表
	 * @author LiFenLong 
	 * @param type 状态,2为咨询,1为评论,Integer
	 * @return 评论、咨询列表页面
	 */
	public String list() {
		if(type==2){
			return "gmlist";
		}
		return "bglist";
	}
	/**
	 * 评论(咨询)列表json
	 * @param pageNo 分页页数,Integer
	 * @param pageSize  每页分页的数量,Integer
	 * @param type 状态,2为咨询,1为评论,Integer
	 * @return  评论、咨询列表json
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String listJson() {
		commentsMap = new HashMap();
		commentsMap.put("goodsName", goodsName);
		commentsMap.put("searchtype", searchtype);
		if(searchtype==0 && status==0){
			status=3;
		}
		commentsMap.put("status", status);
		//setPageSize(10);多此一举
		//webpage = memberCommentManager.getAllComments(getPage(), pageSize, type);
		webpage =this.memberCommentManager.searchComments(commentsMap, this.getPage(), getPage(), this.getPageSize(), type);
		this.showGridJson(webpage);
		return JSON_MESSAGE;
	}
	/**
	 * 查看所有带状态的评论或问答
	 * @param pageNo 分页页数,Integer
	 * @param pageSize  每页分页的数量,Integer
	 * @param type 状态,2为咨询,1为评论,Integer
	 * @param status 回复评论
	 * @return 评论列表
	 */
	 public String msgList(){
	 this.webpage = memberCommentManager.getCommentsByStatus(getPage(), getPageSize(), type, status);
	 return "bglist";
	 }
	/**
	 * 查看评论、咨询详细
	 * @param commentId 评论、咨询Id,Integer
	 * @param memberComment 会员评论对象,MemberComment
	 * @return 查看评论、咨询详细页面
	 */
	public String detail() {
	 
		memberComment = memberCommentManager.get(commentId);
		if (memberComment.getMember_id() != null&& memberComment.getMember_id().intValue() != 0) {
			member = memberManager.get(memberComment.getMember_id());
		}
		if (memberComment != null && !StringUtil.isEmpty(memberComment.getImg())) {
			memberComment.setImgPath(UploadUtil.replacePath(memberComment.getImg()));
		}
		return "detail";
	}
	/*
	 * 查看评论
	 */
	public String detail2() {
		 
		memberComment = memberCommentManager.get(commentId);
		if (memberComment.getMember_id() != null&& memberComment.getMember_id().intValue() != 0) {
			member = memberManager.get(memberComment.getMember_id());
		}
		if (memberComment != null && !StringUtil.isEmpty(memberComment.getImg())) {
			memberComment.setImgPath(UploadUtil.replacePath(memberComment.getImg()));
		}
		return "detail2";
	}

	/**
	 * 回复
	 * @param reply 回复内容,String
	 * @param commentId 评论、咨询Id
	 * @return json
	 * result 1.操作成功.0.操作失败
	 */
	public String add() {
		if (StringUtil.isEmpty(reply)) {
			this.showErrorJson("回复不能为空！");
			return JSON_MESSAGE;
		}
		MemberComment dbMemberComment = memberCommentManager.get(commentId);
		if (dbMemberComment == null) {
			this.showErrorJson("此评论或咨询不存在！");
			return JSON_MESSAGE;
		}
		dbMemberComment.setReply(reply);
		dbMemberComment.setReplystatus(1);
		dbMemberComment.setReplytime(DateUtil.getDateline());
		memberCommentManager.update(dbMemberComment);
		this.showSuccessJson("回复成功");
		return JSON_MESSAGE;
	}
	/**
	 * 删除
	 * @param comment_id 评论、咨询Id数组,Integer[]
	 * @return json
	 * result 1.操作成功.0.操作失败
	 */
	public String delete() {
		
		try {
			memberCommentManager.delete(comment_id);
			this.showSuccessJson("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson("操作失败");
		}
			return this.JSON_MESSAGE;
	}
	/**
	 * 评论、咨询不通过
	 * @param commentId 评论、咨询Id,Integer
	 * @return json
	 * result 1.操作成功.0.操作失败
	 */
	public String hide() {
		try {
			memberComment = memberCommentManager.get(commentId);
			memberComment.setStatus(2);
			memberCommentManager.update(memberComment);
			this.showSuccessJson("操作成功");
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.showErrorJson("操作失败");
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 评论、咨询通过
	 * @param commentId 评论、咨询Id,Integer
	 * @param cat_id 商品分类,Integer
	 * @param point 积分,Integer
	 * @param reson 评论类型,图片、文字,String
	 * @param mp 获取积分值,Integer 
	 * @return  json
	 * result 1.操作成功.0.操作失败
	 */
	public String show() {
		try {
			memberComment = memberCommentManager.get(commentId);
			boolean isFirst = false;

			// 首次评论额外50积分
			if (memberCommentManager.getGoodsCommentsCount(memberComment
					.getGoods_id()) == 0&&memberComment.getType()!=2) {
				isFirst = true;
			}

			memberComment.setStatus(1);
			memberCommentManager.update(memberComment);

			// 清除浏览器缓存
			Map goods = goodsManager.get(memberComment.getGoods_id());
			if (goods != null) {
				String url = "";
				if (goods.get("cat_id") != null) {
					switch (StringUtil.toInt(goods.get("cat_id").toString())) {
					case 1:
					case 2:
						url = "yxgoods";
						break;
					case 3:
					case 4:
					case 12:
					case 18:
						url = "jkgoods";
						break;
					case 6:
						url = "jpgoods";
						break;
					case 5:
					case 7:
					case 8:
					case 9:
						url = "goods";
						break;
					case 19:
						url = "gngoods";
						break;
					}
				}
//				HttpCacheManager.updateUrlModified("/" + url + "-"
//						+ memberComment.getGoods_id() + ".html");
			}

			/**
			 * ------------ 增加会员积分 ------------
			 */
			// 判断是否是带图片的评论
			String reson ="文字评论";
			String type = IMemberPointManger.TYPE_COMMENT;
			if (!StringUtil.isEmpty(memberComment.getImg())) {
				type = IMemberPointManger.TYPE_COMMENT_IMG;
				reson="上传图片评论";
			}

			if (memberPointManger.checkIsOpen(type)) {
				if (memberComment.getMember_id() != null && memberComment.getMember_id().intValue() != 0&&memberComment.getType()!=2) {
					int point = memberPointManger.getItemPoint(type + "_num");
					int mp = memberPointManger.getItemPoint(type + "_num_mp");
					memberPointManger.add(memberComment.getMember_id(), point, reson, null, mp);
				 
				}
			}

			// 首次评论额外50积分
			if (isFirst) {
				if (memberPointManger
						.checkIsOpen(IMemberPointManger.TYPE_FIRST_COMMENT)) {
					int point = memberPointManger
							.getItemPoint(IMemberPointManger.TYPE_FIRST_COMMENT
									+ "_num");
					int mp = memberPointManger
							.getItemPoint(IMemberPointManger.TYPE_FIRST_COMMENT
									+ "_num_mp");
					memberPointManger.add(memberComment.getMember_id(), point,
							"首次评论", null, mp);
				}
			}

			this.showSuccessJson("操作成功");
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.showErrorJson("操作失败");
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 删除一条信息
	 * @param commentId 评论、咨询Id
	 * @return json
	 * result 1.操作成功.0.操作失败
	 */
	public String deletealone() {
		memberComment = memberCommentManager.get(commentId);
		if (memberComment != null) {
			memberCommentManager.deletealone(commentId);
		}
		this.showSuccessJson("删除成功");
		return JSON_MESSAGE;
	}
	
	public String saveAuditComments(){
		try {
			this.memberCommentManager.updateField("status", status, commentId,auditDiscribe);
			//this.memberCommentManager.updateField("check_description", auditDiscribe, commentId);
			this.sendEmailToSeller(commentId, status, auditDiscribe);
			this.showSuccessJson("审核成功");
		} catch (Exception e) {
			this.showErrorJson("审核失败");
			logger.error("审核失败", e);
		}
		return this.JSON_MESSAGE;
	}
	//发送邮件给用户进行审核提醒
	@SuppressWarnings("unchecked")
	public String sendEmailToSeller(Integer  commentId,Integer status,String auditDiscribe) {
		try {
			MemberComment comment=this.memberCommentManager.get(commentId);
			Member member=memberManager.get(comment.getMember_id());
			Goods goods=this.goodsManager.getGoods(comment.getGoods_id());
			//member = this.memberManager.get(member_id);
			// 获取站点信息
			EopSite site = EopSite.getInstance();
			// 编辑邮件信息发送邮件
			EmailModel emailModel = new EmailModel();
			emailModel.getData().put("logo", site.getLogofile());
			emailModel.getData().put("sitename", site.getSitename());
			emailModel.getData().put("uname", member.getUname());
			emailModel.getData().put("sn", goods.getSn());
			emailModel.getData().put("send_time",
					DateUtil.toString(new Date(), "yyyy年MM月dd日  hh:mm:ss"));
			emailModel.getData().put("content", comment.getContent());
			emailModel.getData().put("goodsId", goods.getGoods_id());
			emailModel.getData().put("goodsName", goods.getName());
			emailModel.getData().put("pass", status);//审核是否通过
			emailModel.getData().put("check_description", auditDiscribe);//审核缘由
			emailModel.setTitle("审核评论结果--" + site.getSitename());
			//String toEmail = member.getUname();//用户名就是邮件目的地
			emailModel.setEmail("kamila@payeasenet.com");
			emailModel.setTemplate("verifyResult_email_template_comment.html");
			emailModel.setEmail_type("审核评论");
			mailMessageProducer.send(emailModel);
			//this.showSuccessJson("请登录查收邮件并完成店铺审核。");
		} catch (Exception e) {
			e.printStackTrace();
			//this.showErrorJson("店铺审核结果邮件发送失败！");
		}
		return this.JSON_MESSAGE;
	}
	
	/**
	 * 评论审核
	 * @return
	 */
	public String auditComments(){
		return "auditCommentsPage";
	}
	public IMemberPointManger getMemberPointManger() {
		return memberPointManger;
	}

	public void setMemberPointManger(IMemberPointManger memberPointManger) {
		this.memberPointManger = memberPointManger;
	}

	public void setMemberOrderItemManager(
			IMemberOrderItemManager memberOrderItemManager) {
		this.memberOrderItemManager = memberOrderItemManager;
	}

	public void setMemberCommentManager(
			IMemberCommentManager memberCommentManager) {
		this.memberCommentManager = memberCommentManager;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	

	public MemberComment getMemberComment() {
		return memberComment;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public Integer[] getComment_id() {
		return comment_id;
	}

	public void setComment_id(Integer[] comment_id) {
		this.comment_id = comment_id;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public IMemberCommentManager getMemberCommentManager() {
		return memberCommentManager;
	}
	public IMemberOrderItemManager getMemberOrderItemManager() {
		return memberOrderItemManager;
	}
	public IMemberManager getMemberManager() {
		return memberManager;
	}
	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}
	public void setMemberComment(MemberComment memberComment) {
		this.memberComment = memberComment;
	}
	public Map getCommentsMap() {
		return commentsMap;
	}
	public void setCommentsMap(Map commentsMap) {
		this.commentsMap = commentsMap;
	}
	public int getSearchtype() {
		return searchtype;
	}
	public void setSearchtype(int searchtype) {
		this.searchtype = searchtype;
	}
	public String getAuditDiscribe() {
		return auditDiscribe;
	}
	public void setAuditDiscribe(String auditDiscribe) {
		this.auditDiscribe = auditDiscribe;
	}
	public EmailProducer getMailMessageProducer() {
		return mailMessageProducer;
	}
	public void setMailMessageProducer(EmailProducer mailMessageProducer) {
		this.mailMessageProducer = mailMessageProducer;
	}
	
	
	
	
}
