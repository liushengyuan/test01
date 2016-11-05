package com.enation.app.b2b2c.core.action.api.member;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.member.StoreMemberComment;
import com.enation.app.b2b2c.core.service.goods.IStoreGoodsManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberCommentManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.shop.core.model.MemberOrderItem;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.IMemberOrderItemManager;
import com.enation.app.tradeease.core.service.smsmobile.IsmsMobileManager;
import com.enation.eop.SystemSetting;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.StringUtil;

/**
 * 店铺商品评论API
 * 
 * @author LiFenLong
 *
 */
@Component
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("storeCommentApi")
public class StoreCommentApiAction extends WWAction {
	private IGoodsManager goodsManager;
	private IMemberOrderItemManager memberOrderItemManager;
	private IStoreMemberCommentManager storeMemberCommentManager;
	private IStoreMemberManager storeMemberManager;
	private IStoreGoodsManager storeGoodsManager;
	private IsmsMobileManager smsMobileManager;
	private File file;
	private String fileFileName;
	private int commenttype;
	private int goods_id;
	private String content;
	private int grade;

	private int store_desccredit;
	private int store_servicecredit;
	private int store_deliverycredit;

	private Integer status;
	private String reply;
	private Integer comment_id;
	private Integer store_id;
	private String  picturefile;
	private String img = "";
    public String cleanpicturePath(){
    	try {
    	     String	subFolder="comment";
    		String filePath = "";
    		String static_server_path= SystemSetting.getStatic_server_path();
    		
    		filePath =  static_server_path + "/attachment/";
    		if(subFolder!=null){
    			filePath+=subFolder +"/";
    		}
    		if(picturefile!=null && !StringUtils.isEmpty(picturefile)){
				File delfile = new File(filePath + picturefile);
				if(delfile.exists()){
					FileUtil.delete(filePath + picturefile);
				}
			}
			this.showSuccessJson("删除成功");
		} catch (Exception e) {
			this.showErrorJson("删除失败");
		}
    	return this.JSON_MESSAGE;
    }
	/**
	 * 添加评论或者咨询
	 * 
	 * @param fileFileName
	 *            上传图片名称,String
	 * @param allowTYpe
	 *            上传类型,String
	 * @param file
	 *            上传对象,File
	 * @param memberComment
	 *            会员评论(咨询),StoreMemberComment
	 * @param commenttype
	 *            类型，int
	 * @param content
	 *            评论内容,String
	 * @param grade
	 *            评价等级,int
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	public String add() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		/*HttpSession session = request.getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		
		try {
			String alert=this.getText("storeComment.only");
			String sorry=this.getText("storeComment.sorry");
			String no=this.getText("storeComment.budayu");
			String canshu=this.getText("storeComment.xitong");
			String noexit=this.getText("storeComment.nocunzai");
			String notNull=this.getText("storeComment.notNull");
			String shuru=this.getText("storeComment.shuru");
			String xuanzeqingjia=this.getText("storeComment.qingxuanze");
			String fabiao=this.getText("storeComment.fabiao");
			String youkehu=this.getText("storeComment.youkehu");
			
			/*if (language == "zh") {
				alert = "只有登录且成功购买过此商品的用户才能发表评论和咨询！";
				sorry = "对不起,只能上传gif,jpg,bmp,png格式的图片！";
				geshi="对不起,图片不能大于200K!";
				canshu="系统参数错误！";
				noexit="此商品不存在!";
				comment="评论或咨询内容不能为空";
				shuru="请输入1000以内的内容!";
				xuanze="请选择对商品的评价!";
				fabiao="发表成功";
				huifu="有客户咨询或评论了您店铺的商品，请及时登录网店进行回复";
				wrongString="发表评论出错";
				commentsno="此评论或咨询不存在";
				noempty="回复不能为空";
				replyString="回复成功";
				failure="回复失败";
			} else {
				alert = "Только зарегистрированные и купленные пользователи могут оставлять оценки и вопросы.";
				sorry = "Извините вы только можете загрузить изображения в GIF, jpg, bmp, PNG формат";
				geshi="Извиниете картинка не может быть больше чем 200K!";
				canshu="Системный параметр ошибка!";
				noexit="Этот товар не существует!";
				comment="Оценки или вопросы не может быть пустым";
				shuru="Введите, пожалуйста, меньше, чем 1000!";
				xuanze="Пожалуйста, выберите товар и комментируйте!";
				fabiao="Успешно опубликован";
				huifu="Клиент задал вопросы или оценуи вашем магазину, пожалуйста, войдите магазин и ответите";
				wrongString="ошибка опубликовать";
				commentsno="Этот оценка или вопрос не существует";
				noempty="Ответы не может быть пустым";
				replyString="Успешно ответить";
				failure="Не удалось ответить";
			}*/
			StoreMemberComment memberComment = new StoreMemberComment();
			// 先上传图片
			String subFolder = "comment";
			if (file != null) {

				// 判断文件类型
				String allowTYpe = "gif,jpg,bmp,png";
				if (!fileFileName.trim().equals("")
						&& fileFileName.length() > 0) {
					String ex = fileFileName.substring(
							fileFileName.lastIndexOf(".") + 1,
							fileFileName.length());
					if (allowTYpe.toString().indexOf(ex.toLowerCase()) < 0) {
						this.showErrorJson(sorry);
						return this.JSON_MESSAGE;
					}
				}

				// 判断文件大小

				if (file.length() > 200 * 1024) {
					this.showErrorJson(no);
					return this.JSON_MESSAGE;
				}

				String imgPath = UploadUtil.upload(file, fileFileName,
						subFolder);
				memberComment.setImg(imgPath);
			}

			// 判断是不是评论或咨询
			if (commenttype != 1 && commenttype != 2) {
				this.showErrorJson(canshu);
				return this.JSON_MESSAGE;
			}
			memberComment.setType(commenttype);

			// 判断是否存在此商品
			if (goodsManager.get(goods_id) == null) {
				this.showErrorJson(noexit);
				return this.JSON_MESSAGE;
			}
			memberComment.setGoods_id(goods_id);
			// 判断评论内容
			if (StringUtil.isEmpty(content)) {
				this.showErrorJson(notNull);
				return this.JSON_MESSAGE;
			} else if (content.length() > 1000) {
				this.showErrorJson(shuru);
				return this.JSON_MESSAGE;
			}
			content = StringUtil.htmlDecode(content);
			memberComment.setContent(content);

			StoreMember member = storeMemberManager.getStoreMember();
			if (member == null) {
				this.showErrorJson(alert);
				return this.JSON_MESSAGE;
			}
			memberComment.setStore_id(store_id);
			if (commenttype == 1) {
				int buyCount = memberOrderItemManager.count(
						member.getMember_id(), goods_id);
				int commentCount = memberOrderItemManager.count(
						member.getMember_id(), goods_id, 1);
				// 评论不审核全部通过
				// memberComment.setStatus(1);
				memberComment.setStatus(0);
				if (grade < 1 || grade > 3) {
					this.showErrorJson(xuanzeqingjia);
					return this.JSON_MESSAGE;
				} else {
					memberComment.setGrade(grade);
				}
			}else{
				memberComment.setStatus(1);
			}
			memberComment.setMember_id(member == null ? 0 : member
					.getMember_id());
			memberComment.setDateline(System.currentTimeMillis() / 1000);
			memberComment.setIp(request.getRemoteHost());
			if(StringUtil.isEmpty(img) && img.trim().equalsIgnoreCase(img)){
				img=null;
			}
            memberComment.setImg(img);
			memberComment.setStore_deliverycredit(store_deliverycredit);
			memberComment.setStore_desccredit(store_desccredit);
			memberComment.setStore_servicecredit(store_servicecredit);

			storeMemberCommentManager.add(memberComment);
			// 更新已经评论过的商品
			if (commenttype == 1) {
				MemberOrderItem memberOrderItem = memberOrderItemManager.get(
						member.getMember_id(), goods_id, 0);
				if (memberOrderItem != null) {
					memberOrderItem.setComment_time(System.currentTimeMillis());
					memberOrderItem.setCommented(1);
					memberOrderItemManager.update(memberOrderItem);
				}
			}
			// 添加商品评论次数
			storeGoodsManager.addStoreGoodsComment(goods_id);
			this.showSuccessJson(fabiao);
			// 通过store_id获取店铺的信息
			StoreMember storeMember = this.storeMemberManager
					.getStoreMember(store_id);
			String mobile = storeMember.getMobile();
			// 发送通知短信
			this.smsMobileManager.sendMobile(mobile,
					youkehu);
		} catch (Exception e) {
			String fail=this.getText("storeComment.fabiaoping");
			e.printStackTrace();
			this.logger.error("发表评论出错", e);
				this.showErrorJson(fail+" ：" + e.getMessage());
		}

		return this.JSON_MESSAGE;
	}
	/**
	 * 判断是否评论过（lsy）
	 * @return
	 */
	public String isComment(){
		System.out.println("ss");
		StoreMember member = storeMemberManager.getStoreMember();
		int buyCount = memberOrderItemManager.count(member.getMember_id(), goods_id);
		int commentCount = memberOrderItemManager.count(member.getMember_id(), goods_id,1);
		if(buyCount==0 || commentCount>=buyCount){
			this.showErrorJson("没有购买或已经评论过");
		}else {
			this.add();
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 回复评论、咨询
	 * 
	 * @param reply
	 *            回复内容,String
	 * @param comment_id
	 *            评论Id,Integer
	 * @param memberComment
	 *            会员评论,Map
	 * @param member
	 *            店铺会员,StoreMember
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	public String edit() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpSession session = request.getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String huifu=this.getText("storeComment.huifu");
		String cipinglun=this.getText("storeComment.cipinglun");
		String success=this.getText("storeComment.huifuSuccess");
		String fail=this.getText("storeComment.huifuFail");
		/*if (language=="zh") {
			commentsno="此评论或咨询不存在!";
			noempty="回复不能为空!";
			replyString="回复成功";
			failure="回复失败";
		}else {
			commentsno="Этот оценка или вопрос не существует!";
			noempty="Ответы не может быть пустым!";
			replyString="Успешно ответить";
			failure="Не удалось ответить";
		}*/
		if (StringUtil.isEmpty(reply)) {
			this.showErrorJson(huifu);
			return JSON_MESSAGE;
		}
		Map memberComment = storeMemberCommentManager.get(comment_id);
		if (memberComment == null) {
			this.showErrorJson(cipinglun);
			return JSON_MESSAGE;
		}
		try {
			StoreMember member = storeMemberManager.getStoreMember();

			memberComment.put("reply", reply);
			memberComment.put("replystatus", 1);
			memberComment.put("replytime", DateUtil.getDateline());
			memberComment.put("status", status);
			storeMemberCommentManager.edit(memberComment, comment_id);
			this.showSuccessJson(success);
		} catch (Exception e) {
			this.showErrorJson(fail);
			this.logger.error("回复失败:" + e);
		}
		return this.JSON_MESSAGE;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}

	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

	public IMemberOrderItemManager getMemberOrderItemManager() {
		return memberOrderItemManager;
	}

	public void setMemberOrderItemManager(
			IMemberOrderItemManager memberOrderItemManager) {
		this.memberOrderItemManager = memberOrderItemManager;
	}

	public IStoreMemberCommentManager getStoreMemberCommentManager() {
		return storeMemberCommentManager;
	}

	public void setStoreMemberCommentManager(
			IStoreMemberCommentManager storeMemberCommentManager) {
		this.storeMemberCommentManager = storeMemberCommentManager;
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

	public int getCommenttype() {
		return commenttype;
	}

	public void setCommenttype(int commenttype) {
		this.commenttype = commenttype;
	}

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getStore_desccredit() {
		return store_desccredit;
	}

	public void setStore_desccredit(int store_desccredit) {
		this.store_desccredit = store_desccredit;
	}

	public int getStore_servicecredit() {
		return store_servicecredit;
	}

	public void setStore_servicecredit(int store_servicecredit) {
		this.store_servicecredit = store_servicecredit;
	}

	public int getStore_deliverycredit() {
		return store_deliverycredit;
	}

	public void setStore_deliverycredit(int store_deliverycredit) {
		this.store_deliverycredit = store_deliverycredit;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public Integer getComment_id() {
		return comment_id;
	}

	public void setComment_id(Integer comment_id) {
		this.comment_id = comment_id;
	}

	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}

	public IStoreGoodsManager getStoreGoodsManager() {
		return storeGoodsManager;
	}

	public void setStoreGoodsManager(IStoreGoodsManager storeGoodsManager) {
		this.storeGoodsManager = storeGoodsManager;
	}

	public IsmsMobileManager getSmsMobileManager() {
		return smsMobileManager;
	}

	public void setSmsMobileManager(IsmsMobileManager smsMobileManager) {
		this.smsMobileManager = smsMobileManager;
	}
	public String getPicturefile() {
		return picturefile;
	}
	public void setPicturefile(String picturefile) {
		this.picturefile = picturefile;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}

}
