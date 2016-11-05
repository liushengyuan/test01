package com.enation.app.tradeease.component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;
import org.springframework.stereotype.Component;

@Component
/**
 * tradeease×é¼þ
 */
public class TradeeaseComponent implements IComponent{

	@Override
	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/tradeease/component/tradeease_install.xml", "es_");
	}

	@Override
	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/tradeease/component/tradeease_uninstall.xml", "es_");
	}

}
