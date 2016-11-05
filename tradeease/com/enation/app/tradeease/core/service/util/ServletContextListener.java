package com.enation.app.tradeease.core.service.util;

import javax.servlet.ServletContextEvent;

public class ServletContextListener implements
		javax.servlet.ServletContextListener {
	SocketService service;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		service.closeServer();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		service = new SocketService();
		service.initSocketServer();
	}

}
