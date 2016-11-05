package com.enation.app.tradeease.core.service.socket;
/*
import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

import com.enation.app.base.core.model.Member;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
public class MyWebSocketServlet extends WebSocketServlet {


	private static final long serialVersionUID = -6488889268352650321L;
	
	
	public String getUser(HttpServletRequest request){
		Member member = (Member) ThreadContextHolder.getSessionContext().getAttribute("curr_member");
		String userName = null;
		if(member!=null){
			userName = member.getMember_id().toString();
			request.setAttribute("user", userName);
		}else{
			HttpServletResponse response = ThreadContextHolder.getHttpResponse();
			try {
				response.sendRedirect("../login.html");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return userName;  
	} 
	
	protected StreamInbound createWebSocketInbound(String arg0,
			HttpServletRequest request) {
		Member member = null;
		if(UserConext.getCurrentMember()!=null){
			member = UserConext.getCurrentMember();
			//System.out.println("用户" + member.getMember_id() + "登录");
		}
		return new MyMessageInbound(this.getUser(request));
	}
}*/