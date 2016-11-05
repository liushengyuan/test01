package com.enation.app.shop.core.tag;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.enation.app.shop.core.service.IAllianceCountManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.DateUtil;
import com.enation.tool.IpToCity;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class FlowCountTag extends BaseFreeMarkerTag {
	private IAllianceCountManager AllianceCountManager;
	private IpToCity ipToCity;
	HttpServletRequest request = this.getRequest();
	String session_id = request.getSession().getId();

	@SuppressWarnings({ "rawtypes", "static-access" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		if (this.AllianceCountManager.haveSession(session_id).size() > 0) {
			if (this.AllianceCountManager.haveTime(session_id).toString().length() <= 14) {
				// String user_ip = request.getRemoteAddr();
				String user_ip = getIpAddr(request);
				//System.out.println("IP:..." + user_ip);
				Long come_time = DateUtil.getDateline();
				String ipAddress = this.ipToCity.GetAddressByIp(user_ip);
				this.AllianceCountManager.editFlowCount(user_ip, ipAddress, come_time, session_id);
			}
		}
		return params;
	}

	private String getIpAddr(HttpServletRequest request) {
		String ipAddress = null;
		// ipAddress = this.getRequest().getRemoteAddr();
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}

		}

		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	public IpToCity getIpToCity() {
		return ipToCity;
	}

	public void setIpToCity(IpToCity ipToCity) {
		this.ipToCity = ipToCity;
	}

	public IAllianceCountManager getAllianceCountManager() {
		return AllianceCountManager;
	}

	public void setAllianceCountManager(
			IAllianceCountManager allianceCountManager) {
		AllianceCountManager = allianceCountManager;
	}

}
