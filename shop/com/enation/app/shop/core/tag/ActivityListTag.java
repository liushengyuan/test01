package com.enation.app.shop.core.tag;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.service.IAllActivityProductManager;
import com.enation.app.shop.core.service.impl.AllActivityManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
@Scope("prototype")
public class ActivityListTag extends BaseFreeMarkerTag {
	private IAllActivityProductManager allActivityProductManager;
	private AllActivityManager allActivityManager;
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
    	//活动类型
    	String id = (String) params.get("id");
		java.util.Map<String, Object> data =new HashMap();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpSession session = request.getSession();
		long time=com.enation.framework.util.DateUtil.getDateline();
		try{
			List<Map> activityList=(List<Map>)allActivityManager.get(Integer.parseInt(id));
			for (Map map : activityList) {
				  map.put("nowtime", time);
			}
			data.put("activityList", activityList);//广告位详细信息
		}catch(RuntimeException e){
			if(this.logger.isDebugEnabled()){
				this.logger.error(e.getStackTrace());
			}
		}
		return data;
	}
	public IAllActivityProductManager getAllActivityProductManager() {
	    return allActivityProductManager;
	}
	public void setAllActivityProductManager(IAllActivityProductManager allActivityProductManager) {
	    this.allActivityProductManager = allActivityProductManager;
	}
    public AllActivityManager getAllActivityManager() {
        return allActivityManager;
    }
    public void setAllActivityManager(AllActivityManager allActivityManager) {
        this.allActivityManager = allActivityManager;
    }
	
}
