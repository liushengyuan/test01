package com.enation.app.b2b2c.core.tag.goods;

import java.text.NumberFormat;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.member.IStoreMemberCommentManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 店铺商品好评率信息标签
 * @author jfw
 *
 */
@Component
public class StoreGoodsPraiseInfoTag extends BaseFreeMarkerTag{
	private IStoreMemberCommentManager storeMemberCommentManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer goods_id=Integer.parseInt(params.get("goods_id").toString());
		//1、查询当前商品的全部评价数量
		Integer total = storeMemberCommentManager.getCommentCount(goods_id);
		//2、查询当前商品的好评数量
		Integer praise = storeMemberCommentManager.getCommentCountPraise(goods_id);
		//3、好评率=好评数量/全部评价数量
		Double percent = 1.00;
		//获取格式化对象
		NumberFormat nt = NumberFormat.getPercentInstance();
		//设置百分数精确度2即保留两位小数
		nt.setMinimumFractionDigits(0);
		if(total!=0){
			percent = (praise*1.0)/(total*1.0);
			return nt.format(percent);
		}else {
			return nt.format(percent);
		}
		
	}
	public IStoreMemberCommentManager getStoreMemberCommentManager() {
		return storeMemberCommentManager;
	}
	public void setStoreMemberCommentManager(
			IStoreMemberCommentManager storeMemberCommentManager) {
		this.storeMemberCommentManager = storeMemberCommentManager;
	}
	
//	public static void main(String[] args) {
//		Double percent = 0.5878;
//		//获取格式化对象
//		NumberFormat nt = NumberFormat.getPercentInstance();
//		//设置百分数精确度2即保留两位小数
//		nt.setMinimumFractionDigits(2);
//		//System.out.println(nt.format(percent));
//	}
}
