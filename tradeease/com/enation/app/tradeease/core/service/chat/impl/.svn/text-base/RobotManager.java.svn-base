/*package com.enation.app.tradeease.core.service.chat.impl;

import java.util.List;

import com.enation.app.tradeease.core.model.chat.Robot;
import com.enation.app.tradeease.core.service.chat.IRobotManager;
import com.enation.eop.sdk.database.BaseSupport;

public class RobotManager extends BaseSupport<Robot> implements IRobotManager {

	@Override
	public List<Robot> robotMsg(String msg, String language) {
		String sql = "";
		if(language.equals("zh-CHS")){
			sql = "select output_ch from es_robot where input_ch like '%" + msg + "%'";
		}else if(language.equals("ru")){
			sql = "select output_ru from es_robot where input_ru like '%" + msg + "%'";
		}
		List<Robot> list = this.baseDaoSupport.queryForList(sql, Robot.class);
		return list;
	}

}
*/