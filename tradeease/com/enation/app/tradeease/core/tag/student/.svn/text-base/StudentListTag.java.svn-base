package com.enation.app.tradeease.core.tag.student;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.tradeease.core.model.student.Student;
import com.enation.app.tradeease.core.service.student.IstudentManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;
@Component
@Scope("prototype")
@SuppressWarnings("rawtypes")
public class StudentListTag extends BaseFreeMarkerTag{
	private IstudentManager studentManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String name = request.getParameter("name");
		List<Student> student = this.studentManager.qureryList(name);
		return student;
	}
	public IstudentManager getStudentManager() {
		return studentManager;
	}
	public void setStudentManager(IstudentManager studentManager) {
		this.studentManager = studentManager;
	}
	
	
}
