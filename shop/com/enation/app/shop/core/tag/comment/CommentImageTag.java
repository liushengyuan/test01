package com.enation.app.shop.core.tag.comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class CommentImageTag extends BaseFreeMarkerTag {

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String img=params.get("img").toString();
		List<String> list=new ArrayList<String>();
		if(!StringUtil.isEmpty(img)){
			String[] images=img.trim().split(",");
			if(images.length>0){
				for (String string : images) {
					list.add(UploadUtil.replacePath(string));
				}
			}
		}
		return list;
	}

}
