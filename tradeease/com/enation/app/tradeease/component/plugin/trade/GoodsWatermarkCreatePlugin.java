package com.enation.app.tradeease.component.plugin.trade;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import com.enation.app.shop.core.plugin.goods.IGoodsAfterAddEvent;
import com.enation.app.tradeease.core.util.ImageMarkLogoUtil;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;


/**
 * 给商品详情中的图片添加水印
 * @author ljs
 *
 */
@Component
public class GoodsWatermarkCreatePlugin extends AutoRegisterPlugin implements
		IGoodsAfterAddEvent{
 
	@Override
	public void onAfterGoodsAdd(Map goods, HttpServletRequest request)
			throws RuntimeException {
		String[] list=goods.get("intro").toString().split("src=\"");
		String app_domain=request.getSession().getServletContext().getRealPath("/");
		////System.out.println(list.length);
		for(int i=0;i<list.length;i++){
			if(list[i].contains("/attached/image/")){
				//ImageMarkLogoUtil.markImageByText("TradeEase",app_domain+list[i].split("\"")[0].replace("/", "\\"),app_domain+list[i].split("\"")[0].replace("/", "\\"));
				ImageMarkLogoUtil.markImageByIcon(app_domain+"themes\\b2b2cv2\\images\\logo-1x.png",app_domain+list[i].split("\"")[0].replace("/", "\\"),app_domain+list[i].split("\"")[0].replace("/", "\\"));
			}
		}			
	}
}
