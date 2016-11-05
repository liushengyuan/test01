package com.enation.app.b2b2c.core.tag.goods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.Country;
import com.enation.app.b2b2c.core.service.ICountryManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取国家列表
 * @author WKZ
 * @date 2015-9-11 上午10:47:48
 */
@Component
public class SelectCountryTag extends BaseFreeMarkerTag {
	
	@Autowired
	private ICountryManager countryManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		List<Country> list =  countryManager.getList();
		if(list == null) {
			list = new ArrayList<Country>();
		}
		return list;
	}
}
