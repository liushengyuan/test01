package com.enation.app.shop.core.tag.comment;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;


/**
 * 购物新地址
 *
 *
 */
@Component
@Scope("prototype")
public class NewAddressTag  extends BaseFreeMarkerTag {

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map address = new HashMap();
		
		address.put("new_address_zh", "使用新地址作为默认收货地址");
		address.put("new_address_ru", "Пользоваться новым адресом как адрес получения поваров по умолчанию");
		
		address.put("ReceivingProvince_zh", "所在省");
		address.put("ReceivingProvince_ru", "провинция ");
		
		address.put("ReceivingCity_zh", "所在市");
		address.put("ReceivingCity_ru", "город ");
		
		address.put("ReceivingArea_zh", "详细地址");
		address.put("ReceivingArea_ru", "точный адрес");
		
		address.put("Sellercode_zh", "邮编");
		address.put("Sellercode_ru", "почтовый индекс");
		
		address.put("ConsigneeName_zh", "收货人姓名");
		address.put("ConsigneeName_ru", "Фамилия и имя получателя товара");
		
		address.put("TheRealName_zh", "请填写真实姓名，以免延误收货");
		address.put("TheRealName_ru", "Пожалуйста, назовите настоящее имя, во избежание задержек товара");

		address.put("AddressPhone_zh", "手机");
		address.put("AddressPhone_ru", "мобильный телефон");

		address.put("AddressTelePhone_zh", "电话");
		address.put("AddressTelePhone_ru", "телефон");

		address.put("OneMobilePhones_zh", "手机和电话填写一项即可");
		address.put("OneMobilePhones_ru", "заполнить один из мобильника или телефона");

		address.put("SaveTheAddress_zh", "保存地址");
		address.put("SaveTheAddress_ru", "Сохраните адрес");
		
		address.put("PhoneCallsMust_zh", "手机或电话必须填写一项");
		address.put("PhoneCallsMust_ru", "мобильный телефон  или  телефон  должны заполнить  одно ");

		address.put("MobilePhoneFormat_zh", "手机格式不正确");
		address.put("MobilePhoneFormat_ru", "телефон  не правильный формат ");
		
		address.put("ErroToTry_zh", "出现错误 ，请重试");
		address.put("ErroToTry_ru", "Произошла ошибка,  пожалуйста, попробуйте снова");
	
		return address;
	}

}
