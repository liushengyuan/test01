package com.enation.app.tradeease.core.service.cordova;

import java.util.List;

import com.enation.app.shop.core.model.Favorite;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.framework.database.Page;

/**
 * 根据会员id获取购物车中该会员最后记录的session_id
 * 
 * @author lzf<br/>
 *         2010-3-24 下午02:39:25<br/>
 *         version 1.0<br/>
 */
public interface ICartManagerApp {

	/**
	 * 列表我的收藏
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public String getSessionId(int member_id);

	public List<CartItem> listGoods(String sessionid, Integer goods_id);
}
	