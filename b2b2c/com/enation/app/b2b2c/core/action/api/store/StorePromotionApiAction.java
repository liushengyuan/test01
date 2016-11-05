package com.enation.app.b2b2c.core.action.api.store;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.StoreBonus;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.IStorePromotionManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;

/**
 * 店铺促销管理
 * @author xulipeng
 *	2015年1月12日23:02:42
 */

@Component
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("promotion")
@Results({
	 @Result(name="edit", type="freemarker", location="/themes/default/b2b2c/storesite/navication_edit.html") 
})
public class StorePromotionApiAction extends WWAction {

	private IStorePromotionManager storePromotionManager;
	private IStoreMemberManager storeMemberManager;
	private String type_name;
	private String type_name_ru;
	private Double min_goods_amount;
	private Double min_goods_amount_ru;
	private Double type_money;
	private Double type_money_ru;
	private String useTimeStart;
	private String useTimeEnd;
	private String img_bonus;
	private Integer store_id;
	private Integer type_id;
	private Integer limit_num;
	private Integer is_given;
	private Integer create_num;
	private String recognition;
	
	/**
	 * 添加优惠卷
	 * @param member 店铺会员,StoreMember
	 * @param type_name
	 * @param type_money
	 * @param min_goods_amount
	 * @param useTimeStart
	 * @param useTimeEnd
	 * @param bonus StoreBonus
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	public String add_fullSubtract(){
		StoreBonus bonus = this.setParam();
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		String success=this.getText("storePromotion.addsuccess");
		String fail=this.getText("storePromotion.addfail");
		/*if (language=="zh") {
			success="添加成功";
			fail="添加失败";
		}else {
			success="Успешно добавить";
			fail="Не удалось добавить";
		}*/
		try {
			this.storePromotionManager.add_FullSubtract(bonus);
			this.showSuccessJson(success);
		} catch (Exception e) {
			e.printStackTrace();
			this.showSuccessJson(fail);
		}
		return this.JSON_MESSAGE;
	}
	
	
	/**
	 * 修改优惠劵
	 * @return
	 */
	public String edit_fullSubtract(){
		StoreBonus bonus = this.setParam();
		bonus.setType_id(type_id);
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String success=this.getText("storePromotion.changesuccess");
		String fail=this.getText("storePromotion.changefail");
		/*if (language=="zh") {
			success="修改成功";
			fail="修改失败";
		}else {
			success="успешно изменить";
			fail="неудача  исправления";
		}*/
		try {
			this.storePromotionManager.edit_FullSubtract(bonus);
			this.showSuccessJson(success);
		} catch (Exception e) {
			this.showSuccessJson(fail);
		}
		return JSON_MESSAGE;
	}
	
	/**
	 * 用户领取优惠卷
	 * @return
	 */
	public String receiveBonus(){
		StoreMember member= this.storeMemberManager.getStoreMember();
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		String success=this.getText("storePromotion.lingquSuccess");
		String fail=this.getText("storePromotion.lingqufail");
		/*if (language=="zh") {
			success="领取成功!";
			fail="您不能领自己店铺的优惠劵!";
		}else {
			success="успешно получить!";
			fail=" Вы не можете получить  купоны своего магазина!";
		}*/
		try {
			if(member.getStore_id()!=store_id){
				this.storePromotionManager.receive_bonus(member.getMember_id(), store_id, type_id);
				this.showSuccessJson(success);
			}else if(member.getStore_id().intValue()==store_id){
				this.showErrorJson(fail);

			}
		} catch (Exception e) {
			this.showErrorJson(e.getMessage());
		}
		
		return JSON_MESSAGE;
	}
	
	/**
	 * 用户删除优惠劵
	 * @return
	 */
	public String deleteBonus(){
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		String success=this.getText("storePromotion.deleteSuccess");
		String fail=this.getText("storePromotion.deletefail");
		/*if (language=="zh") {
			success="删除成功!";
			fail="删除失败!";
		}else {
			success="Успешно удалить!";
			fail="Не удалось удалить!";
		}*/
		try {
			this.storePromotionManager.deleteBonus(type_id);
			this.showSuccessJson(success);
		} catch (RuntimeException e) {
			this.showErrorJson(e.getMessage());
		} catch (Exception e) {
			this.showErrorJson(fail);
		}
		return JSON_MESSAGE;
	}
	
	/**
	 * 设置优惠卷参数
	 * @return
	 */
	private StoreBonus setParam(){
		
		StoreMember member= this.storeMemberManager.getStoreMember();
		StoreBonus bonus = new StoreBonus();
		bonus.setRecognition(recognition);
		bonus.setType_money(type_money);
		bonus.setType_money_ru(type_money_ru);
		bonus.setType_name(type_name);
		bonus.setType_name_ru(type_name_ru);
		bonus.setMin_goods_amount(min_goods_amount);
		bonus.setMin_goods_amount_ru(min_goods_amount_ru);
		bonus.setSend_start_date(DateUtil.getDateline(useTimeStart+" 00:00:00", "yyyy-MM-dd HH:mm:ss"));//发放开始、结束时间同使用时间相同
		bonus.setSend_end_date(DateUtil.getDateline(useTimeEnd+" 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		bonus.setUse_start_date(DateUtil.getDateline(useTimeStart+" 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		bonus.setUse_end_date(DateUtil.getDateline(useTimeEnd+" 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		bonus.setStore_id(member.getStore_id());
		bonus.setCreate_num(create_num);
		bonus.setLimit_num(limit_num);
	//	bonus.setIs_given(is_given);                页面已经被注销，不明原因。   本处注销，如果需要，请优先处理本处。whj  2015-05-22
		
		return bonus;
	}
	
	//set get

	public IStorePromotionManager getStorePromotionManager() {
		return storePromotionManager;
	}

	public void setStorePromotionManager(
			IStorePromotionManager storePromotionManager) {
		this.storePromotionManager = storePromotionManager;
	}


	public String getType_name() {
		return type_name;
	}


	public void setType_name(String type_name) {
		this.type_name = type_name;
	}


	public Double getMin_goods_amount() {
		return min_goods_amount;
	}


	public void setMin_goods_amount(Double min_goods_amount) {
		this.min_goods_amount = min_goods_amount;
	}


	public Double getType_money() {
		return type_money;
	}


	public void setType_money(Double type_money) {
		this.type_money = type_money;
	}


	public String getUseTimeStart() {
		return useTimeStart;
	}


	public void setUseTimeStart(String useTimeStart) {
		this.useTimeStart = useTimeStart;
	}


	public String getUseTimeEnd() {
		return useTimeEnd;
	}


	public void setUseTimeEnd(String useTimeEnd) {
		this.useTimeEnd = useTimeEnd;
	}


	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}


	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}


	public String getImg_bonus() {
		return img_bonus;
	}


	public void setImg_bonus(String img_bonus) {
		this.img_bonus = img_bonus;
	}

	public Integer getType_id() {
		return type_id;
	}

	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}

	public Integer getLimit_num() {
		return limit_num;
	}

	public void setLimit_num(Integer limit_num) {
		this.limit_num = limit_num;
	}

	public Integer getIs_given() {
		return is_given;
	}

	public void setIs_given(Integer is_given) {
		this.is_given = is_given;
	}

	public Integer getCreate_num() {
		return create_num;
	}

	public void setCreate_num(Integer create_num) {
		this.create_num = create_num;
	}

	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}


	public String getType_name_ru() {
		return type_name_ru;
	}


	public void setType_name_ru(String type_name_ru) {
		this.type_name_ru = type_name_ru;
	}


	public Double getType_money_ru() {
		return type_money_ru;
	}


	public void setType_money_ru(Double type_money_ru) {
		this.type_money_ru = type_money_ru;
	}


	public String getRecognition() {
		return recognition;
	}


	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}


	public Double getMin_goods_amount_ru() {
		return min_goods_amount_ru;
	}


	public void setMin_goods_amount_ru(Double min_goods_amount_ru) {
		this.min_goods_amount_ru = min_goods_amount_ru;
	}
	
	
}
