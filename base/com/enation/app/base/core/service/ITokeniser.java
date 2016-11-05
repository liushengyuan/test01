package com.enation.app.base.core.service;

import com.enation.app.base.core.model.TokenisedDoc;

/**
 * 分词接口
 * @author zmm 2016-03-30
 *
 */
public interface ITokeniser {

	public TokenisedDoc tokenise(String line,boolean ignoreVirtual);
}
