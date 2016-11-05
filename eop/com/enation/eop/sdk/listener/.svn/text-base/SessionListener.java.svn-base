package com.enation.eop.sdk.listener;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.enation.app.shop.core.service.IAllianceCountManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.tradeease.core.model.sms.VistUserCount;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.util.DateUtil;

public class SessionListener extends BaseSupport implements HttpSessionActivationListener,
		HttpSessionListener, ServletContext, ServletRequestListener {
	ServletContext context;
	int onLineUserNum = 0;
	int visitNum = 0;
	HttpServletRequest request;
	public void contextDestroyed(ServletContextEvent sce){
		log("contextDestroyed-->ServletContext is destroyed");
		this.context = null;
		}
	public void contextInitialized(ServletContextEvent sce){
		this.context = sce.getServletContext();
		log("contextDestroyed-->ServletContext is initialized");
	}
	public int getUserOnline(){
		return this.onLineUserNum;
		}
	@Override
	public void requestDestroyed(ServletRequestEvent arg0) {

	}

	@Override
	public void requestInitialized(ServletRequestEvent event) {
		request = (HttpServletRequest)event.getServletRequest();

	}

	@Override
	public Dynamic addFilter(String arg0, String arg1) {
		
		return null;
	}

	@Override
	public Dynamic addFilter(String arg0, Filter arg1) {
		
		return null;
	}

	@Override
	public Dynamic addFilter(String arg0, Class<? extends Filter> arg1) {
		
		return null;
	}

	@Override
	public void addListener(Class<? extends EventListener> arg0) {
		

	}

	@Override
	public void addListener(String arg0) {
		

	}

	@Override
	public <T extends EventListener> void addListener(T arg0) {
		

	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
			String arg1) {
		
		return null;
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
			Servlet arg1) {
		
		return null;
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
			Class<? extends Servlet> arg1) {
		
		return null;
	}

	@Override
	public <T extends Filter> T createFilter(Class<T> arg0)
			throws ServletException {
		
		return null;
	}

	@Override
	public <T extends EventListener> T createListener(Class<T> arg0)
			throws ServletException {
		
		return null;
	}

	@Override
	public <T extends Servlet> T createServlet(Class<T> arg0)
			throws ServletException {
		
		return null;
	}

	@Override
	public void declareRoles(String... arg0) {
		

	}

	@Override
	public Object getAttribute(String arg0) {
		
		return null;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		
		return null;
	}

	@Override
	public ClassLoader getClassLoader() {
		
		return null;
	}

	@Override
	public ServletContext getContext(String arg0) {
		
		return null;
	}

	@Override
	public String getContextPath() {
		
		return null;
	}

	@Override
	public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
		
		return null;
	}

	@Override
	public int getEffectiveMajorVersion() {
		
		return 0;
	}

	@Override
	public int getEffectiveMinorVersion() {
		
		return 0;
	}

	@Override
	public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
		
		return null;
	}

	@Override
	public FilterRegistration getFilterRegistration(String arg0) {
		
		return null;
	}

	@Override
	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		
		return null;
	}

	@Override
	public String getInitParameter(String arg0) {
		
		return null;
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		
		return null;
	}

	@Override
	public JspConfigDescriptor getJspConfigDescriptor() {
		
		return null;
	}

	@Override
	public int getMajorVersion() {
		
		return 0;
	}

	@Override
	public String getMimeType(String arg0) {
		
		return null;
	}

	@Override
	public int getMinorVersion() {
		
		return 0;
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String arg0) {
		
		return null;
	}

	@Override
	public String getRealPath(String arg0) {
		
		return null;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		
		return null;
	}

	@Override
	public URL getResource(String arg0) throws MalformedURLException {
		
		return null;
	}

	@Override
	public InputStream getResourceAsStream(String arg0) {
		
		return null;
	}

	@Override
	public Set<String> getResourcePaths(String arg0) {
		
		return null;
	}

	@Override
	public String getServerInfo() {
		
		return null;
	}

	@Override
	public Servlet getServlet(String arg0) throws ServletException {
		
		return null;
	}

	@Override
	public String getServletContextName() {
		
		return null;
	}

	@Override
	public Enumeration<String> getServletNames() {
		
		return null;
	}

	@Override
	public ServletRegistration getServletRegistration(String arg0) {
		
		return null;
	}

	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		
		return null;
	}

	@Override
	public Enumeration<Servlet> getServlets() {
		
		return null;
	}

	@Override
	public SessionCookieConfig getSessionCookieConfig() {
		
		return null;
	}

	@Override
	public void log(String message) {
		String vistdate = new java.util.Date().toLocaleString();
		IGoodsManager goodsManager = SpringContextHolder.getBean("goodsManager");
		VistUserCount vistUserCount=goodsManager.queryvist(vistdate,message);
		if(vistUserCount!=null){
				vistUserCount.setVisit_message(message);
				String visittime1 = new java.util.Date().toLocaleString();
				vistUserCount.setVisit_time(visittime1);
				vistUserCount.setVisit_num(this.visitNum);
				vistUserCount.setVisit_onlinenum(onLineUserNum);
				goodsManager.updateVistCount(vistUserCount);
			}else{
				VistUserCount  vistUserCount1=new VistUserCount();
				vistUserCount1.setVisit_message(message);
				String visittime2 = new java.util.Date().toLocaleString();
				vistUserCount1.setVisit_time(visittime2);
				vistUserCount1.setVisit_num(this.visitNum);
				vistUserCount1.setVisit_onlinenum(onLineUserNum);
				goodsManager.addVisitUser(vistUserCount1);
			}
		
	}
	
	@Override
	public void log(Exception arg0, String arg1) {
		

	}

	@Override
	public void log(String arg0, Throwable arg1) {
		

	}

	@Override
	public void removeAttribute(String arg0) {
		

	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		

	}

	@Override
	public boolean setInitParameter(String arg0, String arg1) {
		
		return false;
	}

	@Override
	public void setSessionTrackingModes(Set<SessionTrackingMode> arg0)
			throws IllegalStateException, IllegalArgumentException {
		

	}

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		onLineUserNum++;
		visitNum ++;
		String ip = request.getRemoteAddr();
		log("sessionCreated('"+event.getSession().getId()+"'),-->IP:"+ip);
		//context.setAttribute("onLineUserNum", new Integer(onLineUserNum));
		//context.setAttribute("visitNum", new Integer(visitNum));

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
 		Long leaveTime = DateUtil.getDateline()-30*60;
//		IAllianceCountManager allianceCountManager = SpringContextHolder.getBean("allianceCountManager");
//		allianceCountManager.editLeaveTime(leaveTime, event.getSession().getId());
		if (onLineUserNum>0){
			onLineUserNum--;
			}
		String ip = request.getRemoteAddr();
		log("sessionDestroyed('"+event.getSession().getId()+"'),-->IP:"+ip);
		//context.setAttribute("onLineUserNum", new Integer(onLineUserNum));

	}

	@Override
	public void sessionDidActivate(HttpSessionEvent session) {
		log("seesionDidActive("+session.getSession().getId()+")");

	}

	@Override
	public void sessionWillPassivate(HttpSessionEvent session) {
		log("seesionWillPassivate("+session.getSession().getId()+")");

	}

}
