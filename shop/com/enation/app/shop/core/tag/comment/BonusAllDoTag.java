package com.enation.app.shop.core.tag.comment;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class BonusAllDoTag extends BaseFreeMarkerTag {

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map bonusAllDo=new HashMap();
		bonusAllDo.put("bonusAlldo_zh", "优惠卷识别码");
		bonusAllDo.put("bonusAlldo_ru", "Код купона");
		return bonusAllDo;
	}
}
