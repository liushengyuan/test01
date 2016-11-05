package com.enation.app.shop.core.service;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.core.model.ordermanifest.Goodsinformation;
import com.enation.app.shop.core.model.ordermanifest.OrderPaymentInformation;
import com.enation.app.shop.core.model.ordermanifest.Savexml;
import com.enation.framework.database.Page;

public interface IOrderInformationManager {
	 public Page getInformation(Map map,Integer page,Integer pageSize);
	 public Page getOffineInformation(Map map, Integer page, Integer pageSize);
	 public Page getXmlInformation(Map map, Integer page, Integer pageSize);
	 public List getInformtionByXml(Integer id);
	 public List<Goodsinformation> getGoodsInformation(Integer order_id);
	 public OrderPaymentInformation getPaymentInformation(Integer order_id);
	 public Savexml getXmlInformation(Integer id);
	 public void deleteXmlInformation(Integer id);
	 public void saveXmlInformation(Savexml savexml);
	 public Integer getXmlCount();
}
