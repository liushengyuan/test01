package com.enation.app.b2b2c.core.tag.logistics;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.warehouse.IWarehouse;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.entity.vo.LogisticsInformationVo;

import freemarker.template.TemplateModelException;

@Component
public class LogisticsTag extends BaseFreeMarkerTag {
	private IWarehouse warehouse;
	private IStoreMemberManager storeMemberManager;

	@SuppressWarnings("rawtypes")
	@Override
	protected Object exec(Map params) throws TemplateModelException {

		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		// session中获取会员信息,判断用户是否登录
		StoreMember member = storeMemberManager.getStoreMember();
		if (member == null) {
			HttpServletResponse response = ThreadContextHolder
					.getHttpResponse();
			try {
				response.sendRedirect("login.html");
			} catch (IOException e) {
				throw new UrlNotFoundException();
			}
		}
		String ship_no = request.getParameter("ship_no");
		LogisticsInformationVo logistics = warehouse.queryLogistics(ship_no);
		return logistics;

	}

	public IWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(IWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}

}
