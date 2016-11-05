package com.enation.app.tradeease.core.tag.student;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.tradeease.core.model.student.Student;
import com.enation.app.tradeease.core.service.student.IstudentManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
@Scope("prototype")
@SuppressWarnings("rawtypes")
public class ExportStudTag extends BaseFreeMarkerTag {
	private IstudentManager studentManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String ids = request.getParameter("ids");
		request.setAttribute("export", true);
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=export_" + System.currentTimeMillis() + ".xls");
		List<Student> studList = this.studentManager.queryStudentWihtIds(ids);
		return studList;
	}
	public IstudentManager getStudentManager() {
		return studentManager;
	}
	public void setStudentManager(IstudentManager studentManager) {
		this.studentManager = studentManager;
	}

	
}
