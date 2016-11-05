package com.enation.app.tradeease.core.tag.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.tradeease.core.model.chat.Chat;
import com.enation.app.tradeease.core.service.chat.IChatManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;


@Component
@Scope("prototype")
public class GoodsIdTag extends BaseFreeMarkerTag {
	
	private IGoodsManager goodsManager;
	private IChatManager chatManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Integer store_id = Integer.parseInt(request.getParameter("storeId"));
		Member member = UserConext.getCurrentMember();
		List<Chat> chat = chatManager.getGoodsId(member.getMember_id(), store_id);
		Integer goods_id = null;
		if(request.getParameter("goodsid")!=null){
			goods_id = Integer.parseInt(request.getParameter("goodsid"));
		}
		else if(chat.size()!=0&&chat.get(0).getGoods_id()!=null){
			goods_id = chat.get(0).getGoods_id();
		}else{
			goods_id=null;
		}
		Goods good = null;
		List<Goods> goods = new ArrayList<Goods>();
		if(goods_id!=null){
			good = this.goodsManager.getGoods(goods_id);
			if(good!=null){
				goods.add(good);
			}else{
				Goods g = new Goods();
				g.setGoods_id(-1);
				goods.add(g);
			}
		}else{
			Goods g = new Goods();
			g.setGoods_id(-1);
			goods.add(g);
		}
		return goods;
	}

	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

	public IChatManager getChatManager() {
		return chatManager;
	}

	public void setChatManager(IChatManager chatManager) {
		this.chatManager = chatManager;
	}

}
