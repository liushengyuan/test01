package com.enation.app.shop.core.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.AdIndex;
import com.enation.app.base.core.service.IAdIndexManager;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 首页大屏广告
 * @author WKZ
 * @date 2015-9-2 下午2:14:08
 */
@Component
@Scope("prototype")
public class AdIndexTag extends BaseFreeMarkerTag {
	
	@Autowired
	private IAdIndexManager adIndexManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		AdIndex adIndex = adIndexManager.getSingleAdIndex(1);
		if(adIndex == null) {
			return new AdIndex();
		}
		if(adIndex.getAdindex_url()!=null&&!StringUtil.isEmpty(adIndex.getAdindex_url())){			
			adIndex.setAdindex_url(UploadUtil.replacePath(adIndex.getAdindex_url()));
		}
		String back = "background-image:url(" + adIndex.getAdindex_url() + "); ";
		String outerCss = adIndex.getOuter_css();
		outerCss += back;
		adIndex.setOuter_css(outerCss);
		return adIndex;
	}
	
	
	 
}
