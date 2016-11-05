package com.enation.app.b2b2c.core.tag.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
public class DemoTag extends BaseFreeMarkerTag{

	@Override
	protected Object exec(Map parem) throws TemplateModelException {
		List list = new ArrayList();
		list.add("aa");
		list.add("bb");
		return list;
	}

}
