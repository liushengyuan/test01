package com.enation.app.tradeease.core.service.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.enation.app.base.core.model.Member;
import com.enation.app.tradeease.core.model.chat.Chat;
import com.enation.app.tradeease.core.service.chat.IChatManager;
import com.enation.app.tradeease.core.service.chat.impl.ChatManager;
import com.enation.app.tradeease.core.service.translation.MicroTranslate;
import com.enation.app.tradeease.core.service.util.MessageUtil;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.memetix.mst.language.Language;

@Component
public class MyMessageInbound extends MessageInbound implements ApplicationContextAware {
	private static ApplicationContext appContext;
	private String name;
	private String Msg;
	private String translation_end;
	private Integer goodsid;
	private Language end_from = null;
	private Language end_to = null;
	private String notLine;
	public MyMessageInbound() {
		super();
	}

	public MyMessageInbound(String name) {
		super();
		this.name = name;
	}

	@Override  
	protected void onBinaryMessage(ByteBuffer arg0) throws IOException {  

	}  

	
	@Override  
	protected void onTextMessage(CharBuffer msg) {
		HashMap<String,String> messageMap = MessageUtil.getMessage(msg);    //处理消息类
		String fromName = messageMap.get("fromName");    //消息来自人 的userId
		String toName = messageMap.get("toName");       //消息发往人的 userId
		String mapContent = messageMap.get("content");
		String[] content = mapContent.split("#@@@#");
		this.goodsid = null;
		if(!content[1].equals("-1")&&!content[1].equals("-1n")){
			if(!content[1].substring(content[1].length()-1).equals("n")){
				this.goodsid = Integer.parseInt(content[1].substring(0));
			}else{
				this.goodsid = Integer.parseInt(content[1].substring(0,content[1].length()-1));
				this.notLine=content[1].substring(content[1].length()-1);
			}
		}else{
			if(content[1].substring(content[1].length()-1).equals("n")){
				notLine="n";
			}
		}
		mapContent = content[0];
		int Msg_from = Integer.parseInt(mapContent.substring(0,1));
		ChatManager chatManager = (ChatManager)appContext.getBean("chatManager");
		List<Chat> language = chatManager.selectFromLanguage(Integer.parseInt(toName));
		if(language.size()!=0){
			String to = language.get(0).getTranslation_front();
			this.end_to = Language.fromString(to);
		}
		Msg = mapContent.substring(fromName.length()+1,mapContent.length());
		switch(Msg_from){
			case 1: this.end_from = Language.CHINESE_SIMPLIFIED;break;
			case 2: this.end_from = Language.RUSSIAN;break;
			case 3: this.end_from = Language.ENGLISH;break;
		}
//		if(this.end_to!=null&&this.end_to!=this.end_from){
//			this.translation_end = MicroTranslate.translate(this.Msg, this.end_from, this.end_to);
//			try {
//				singleChat(fromName,toName,fromName + this.Msg +"<br/>"+ this.translation_end,notLine);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}else{
			this.end_to = this.end_from;
			this.translation_end = Msg;
			try {
				singleChat(fromName,toName,fromName + this.translation_end,notLine);
			} catch (IOException e) {
				e.printStackTrace();
			}
//		}
	}  

	private void singleChat(String fromName, String toName, String mapContent,String logo) throws IOException {
		HashMap<String, MessageInbound> userMsgMap = InitServlet.getSocketList();
		MessageInbound messageInbound = userMsgMap.get(toName);    //在仓库中取出发往人的MessageInbound
		MessageInbound messageFromInbound = userMsgMap.get(fromName);
		if(messageInbound!=null && messageFromInbound!=null){     //如果发往人 存在进行操作
			WsOutbound outbound = messageInbound.getWsOutbound(); 
			WsOutbound outFromBound = messageFromInbound.getWsOutbound();
			
			ChatManager chatManager = (ChatManager)appContext.getBean("chatManager");
			Chat chat = new Chat(Integer.parseInt(fromName), Integer.parseInt(toName), 1, this.Msg, this.translation_end, this.end_from.toString(), this.end_to.toString(),this.goodsid);
			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sendtime = sf.format(date);
			chat.setSendtime(sendtime);
			chatManager.addMessage(chat);
			
			
			
			String msgContentString = mapContent;   //构造发送的消息
			int length = fromName.length();
			msgContentString = msgContentString.substring(length);
			msgContentString = fromName+"*@%&$"+msgContentString;
			String contentTemp = MessageUtil.sendContent(MessageUtil.MESSAGE,msgContentString);

			outFromBound.writeTextMessage(CharBuffer.wrap(contentTemp.toCharArray()));
			outbound.writeTextMessage(CharBuffer.wrap(contentTemp.toCharArray()));
			
			outFromBound.flush();
			outbound.flush();
		}else{
			ChatManager chatManager = (ChatManager)appContext.getBean("chatManager");
			Chat chat = new Chat(Integer.parseInt(fromName), Integer.parseInt(toName), 2, this.Msg, this.translation_end, this.end_from.toString(), this.end_to.toString(),this.goodsid);
			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sendtime = sf.format(date);
			chat.setSendtime(sendtime);
			
//			RobotManager robotManager = (RobotManager)appContext.getBean("robotManager");
//			List<Robot> robots = robotManager.robotMsg(this.Msg, this.end_from.toString());
			this.Msg = "";
//			if(robots.size()!=0&&robots!=null){
//				if(this.end_from.toString().equals("zh-CHS")){
//					this.Msg = robots.get(0).getOutput_ch();
//				}else if(this.end_from.toString().equals("ru")){
//					this.Msg = robots.get(0).getOutput_ru();
//				}
//			}
			for(int i = 0;i<fromName.length();i++){
				this.Msg ="n"+this.Msg;
			}
			if(logo!=null){
				if(logo.equals("n")){
					this.Msg="n"+this.Msg;
					chatManager.addMessage(chat);
				}
			}
			WsOutbound outFromBound = messageFromInbound.getWsOutbound();
			String contentTemp = MessageUtil.sendContent(MessageUtil.MESSAGE,Msg);
			outFromBound.writeTextMessage(CharBuffer.wrap(contentTemp.toCharArray()));
			outFromBound.flush();//向发件人推送消息
			//broadcastAll(content);
		}
	}

	@Override  
	protected void onClose(int status) { 
		if(name!=null){
			InitServlet.getSocketList().remove(name);//删除客服ID与用户
		}
		String names = getNames();
		String content = MessageUtil.sendContent(MessageUtil.USER,names);
		super.onClose(status);
	}  

	@Override
	protected void onOpen(WsOutbound outbound) { 
		super.onOpen(outbound);
		if(name!=null){
			InitServlet.getSocketList().put(name, this);//存放客服ID与用户
		} 
		String names = getNames();
		String content = MessageUtil.sendContent(MessageUtil.USER,names);
	}
	
	private String getNames() {
		Map<String,MessageInbound> exitUser = InitServlet.getSocketList();
		Iterator<String> it=exitUser.keySet().iterator();
		String names = "";
		while(it.hasNext()){
			String key=it.next();
			names += key + ",";
		}
		String namesTemp = "";
		if(names.length()!=0){
			namesTemp = names.substring(0,names.length()-1);
		}
		return namesTemp;
	}

	
	
	public static void broadcastAll(String message){
		Set<Map.Entry<String,MessageInbound>> set = InitServlet.getSocketList().entrySet();
		WsOutbound outbound = null;
		for(Map.Entry<String,MessageInbound> messageInbound: set){
			try {
				outbound = messageInbound.getValue().getWsOutbound();
				outbound.writeTextMessage(CharBuffer.wrap(message));
				outbound.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getReadTimeout() {
		return 0;
	}

	@Override
	public void setApplicationContext(ApplicationContext paramApplicationContext)
			throws BeansException {
		appContext = paramApplicationContext;		
	}

	public String getNotLine() {
		return notLine;
	}

	public void setNotLine(String notLine) {
		this.notLine = notLine;
	}

}