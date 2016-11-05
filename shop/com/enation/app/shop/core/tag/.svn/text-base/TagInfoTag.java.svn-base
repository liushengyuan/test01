package com.enation.app.shop.core.tag;


import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Tag;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.service.ITagManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 标签信息标签
 * @author ljs
 *2015-7-8上午11:00:20
 */
@Component
@Scope("prototype") 
public class TagInfoTag extends BaseFreeMarkerTag {
	private ITagManager tagManager;
	
	/**
	 * 返回购物车中的购物列表
	 * @param 无 
	 * @return 购物列表 类型List<CartItem>
	 * {@link CartItem}
	 */
	@Override
	public Object exec(Map params) throws TemplateModelException {
		String tagid = (String)params.get("tagid");
		Tag tag=tagManager.getById(Integer.valueOf(tagid));
		return tag;
	}

	public ITagManager getTagManager() {
		return tagManager;
	}

	public void setTagManager(ITagManager tagManager) {
		this.tagManager = tagManager;
	}
}
