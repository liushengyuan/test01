package com.enation.app.shop.core.tag.detail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.model.GoodLogisDetail;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.LogisModel;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.app.shop.core.service.impl.LogiManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
public class GoodsGetLogisModelTag extends BaseFreeMarkerTag{
	private ILogiManager logiManager;
	private IGoodsManager goodsManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer goods_id=Integer.parseInt(params.get("goods_id").toString());
		Goods goods=this.goodsManager.getGoods(goods_id);
		//Integer store_id=Integer.parseInt(params.get("store_id").toString());
		HttpServletRequest request =ThreadContextHolder.getHttpRequest();
		HttpSession session =request.getSession();
		String sessionid  = session.getId();
		List<GoodLogisDetail> details=this.logiManager.findLogisDetail1(goods_id, sessionid);
		
		Integer fright=0;
		if(goods.getFreightType()==null){
			fright=1;
		}else{
			fright=goods.getFreightType();
		}
		LogisModel logisModel=null;
		if(details.size()!=0){
			 logisModel=this.logiManager.selectLogiModelByStore(fright,details.get(0).getFreight_id());
		}else{
			logisModel=new LogisModel();
		}
		if(logisModel.getLogis_model_id()==null){
			return logisModel;
		}
		return logisModel.getLogis_model_id() ;
	}
	public ILogiManager getLogiManager() {
		return logiManager;
	}
	public void setLogiManager(ILogiManager logiManager) {
		this.logiManager = logiManager;
	}
	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}
	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

}
