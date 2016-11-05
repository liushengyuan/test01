package com.enation.app.tradeease.core.service.socket;
import java.io.IOException;  

import java.nio.ByteBuffer;  
import java.nio.CharBuffer;  
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
import java.util.UUID;  
import java.util.concurrent.ConcurrentHashMap;  
  
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;
  
import org.apache.catalina.websocket.MessageInbound;  
import org.apache.catalina.websocket.StreamInbound;  
import org.apache.catalina.websocket.WebSocketServlet;  
import org.apache.catalina.websocket.WsOutbound;  

import com.enation.app.base.core.model.Member;
import com.enation.framework.context.webcontext.ThreadContextHolder;
//@WebServlet(urlPatterns = { "/message"})
public class ServerSocket extends WebSocketServlet {  
	  
    private static final long serialVersionUID = -4853540828121130946L;  
    private static Map< String , MyMessageInbound> mmiList = new ConcurrentHashMap< String , MyMessageInbound >();  
    private String message_to ;   
    private String message_me ;   
      
    @Override  
    protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest request) {
        message_me = request.getParameter( "message_me" );  
        message_to = request.getParameter( "message_to" );  
        return new MyMessageInbound(this.getUser(request));  
    }  
    public String getUser(HttpServletRequest request){
    	String userName = null;
		if(message_me!=null){
			userName =this.message_me;
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
}  