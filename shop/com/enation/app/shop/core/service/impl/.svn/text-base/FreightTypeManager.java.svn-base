package com.enation.app.shop.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.service.IRegionsManager;
import com.enation.app.shop.core.model.DlyType;
import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.model.mapper.TypeAreaMapper;
import com.enation.app.shop.core.model.support.DlyTypeConfig;
import com.enation.app.shop.core.model.support.TypeArea;
import com.enation.app.shop.core.model.support.TypeAreaConfig;
import com.enation.app.shop.core.service.IDlyTypeManager;
import com.enation.app.shop.core.service.IFreightTypeManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

/**
 * 运费计算管理
 * 
 * @author kingapex 2010-4-1上午10:11:01
 */
public class FreightTypeManager extends BaseSupport implements IFreightTypeManager {

	@Override
	public List<FreightStandard> getFreightStandardOfSF(String productname) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM es_freight_standard WHERE product_name = ? ";
		return this.baseDaoSupport.queryForList(sql, FreightStandard.class,productname);
	}

	

	

}
