package com.enation.app.shop.core.utils;

import java.util.Currency;

import com.enation.app.shop.core.model.FreightStandard;
import com.enation.framework.util.CurrencyUtil;
/**
 * 运费计算工具类
 * @author jfw
 *
 */
public class FreightUtls {
	/**
	 * 根据当前的运费规则，计算重量
	 * @param freightStandard 运费规则
	 * @param weight 重量
	 * @return
	 */
	public static Double getFreight(FreightStandard freightStandard,Double weight){
		
		Double start_price_kg = freightStandard.getStart_price_kg();//起步价中包含的重量
		Double every_price_kg = freightStandard.getEvery_price_kg();//超出起步价中包含的重量,每kg收取的费用
		Double start_price = freightStandard.getStart_price();//起步价
		Double extra_price = freightStandard.getExtra_price();//附加费用
		Double price=0.0;
		if(weight<=start_price_kg){
			price=CurrencyUtil.add(start_price, extra_price);
		}else{
			Double d1 = CurrencyUtil.sub(weight, start_price_kg);
			Double d2 = CurrencyUtil.mul(d1, every_price_kg);
			Double d3 = CurrencyUtil.add(d2, start_price);
			price = CurrencyUtil.add(d3, extra_price);
		}
		return price;
	}
	
}
