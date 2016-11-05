package com.enation.app.b2b2c.core.tag.warehouse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.warehouse.IWarehouse;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.entity.vo.WarehouseVo;

import freemarker.template.TemplateModelException;

/**
 * 查询仓库信息
 * 
 * @author LiFenLong
 * 
 */
@Component
public class WarehouseTag extends BaseFreeMarkerTag {
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

		// 获取订单列表参数
		int pageSize = 10;
		String page = request.getParameter("page") == null ? "1" : request
				.getParameter("page");

		Map result = new HashMap();

		Page warehoustList = warehouse.warehouseList(Integer.parseInt(page),
				pageSize, result);
		// 获取总记录数
		Long totalCount = warehoustList.getTotalCount();

		result.put("page", page);
		result.put("pageSize", pageSize);
		result.put("totalCount", totalCount);
		result.put("storeOrder", warehoustList);
		return result;

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
