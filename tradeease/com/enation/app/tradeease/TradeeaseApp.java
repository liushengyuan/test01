package com.enation.app.tradeease;

import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.App;
import org.springframework.stereotype.Component;

/**
 * tradeease应用
 */

@Component("tradeease")
public class TradeeaseApp extends App {


	@Override
	public String getId() {
		return "tradeease";
	}

	@Override
	public String getName() {
		return "tradeease应用";
	}

	@Override
	public String getNameSpace() {
		return "/tradeease";
	}

	@Override
	public void install() {
		this.doInstall("file:com/enation/app/tradeease/tradeease.xml");
	}

	@Override
	public void sessionDestroyed(String sessionid, EopSite site) {

	}



}
