package com.enation.app.tradeease.core.service.bill;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.enation.app.shop.core.model.Order;
import com.enation.app.tradeease.core.util.FtpUtils;
import com.enation.eop.sdk.database.BaseSupport;

/**
 * 
 * @author zks
 * 
 */
@SuppressWarnings("rawtypes")
public class CheckBill extends BaseSupport {

	public void doCheckBill() {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		InputStream is = null;
		try {
			FtpUtils ftp = new FtpUtils(getFileName());// 后去ftp服务器文件
			is = ftp.getStream();
			XMLStreamReader reader = factory.createXMLStreamReader(is);

			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			Map<String, String> map = null;
			while (reader.hasNext()) {
				int type = reader.next();

				if (type == XMLStreamConstants.START_ELEMENT) {// 判断是否为开始元素
					String name = reader.getName().toString();
					String value;
					try {
						value = reader.getElementText();
					} catch (XMLStreamException e) {
						value = "";
					}
					if (value != null && value != "") { // 当value值不为空时
						// 以oid为开始的元素时，初始化一个map
						if (name.equals("oid")) {
							map = new HashMap<String, String>();
						}
						map.put(name, value);
					}

				}

				// 当以order元素结尾的时候将map放入list中
				if (type == XMLStreamConstants.END_ELEMENT) {
					String name = reader.getName().toString();
					if (name.equals("order"))
						list.add(map);
				}
			}
			checkData(list);// 数据对比
		} catch (XMLStreamException e) {
			e.printStackTrace();
			logger.info("对账失败");
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取文件名
	 * 
	 * @return
	 */
	private String getFileName() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String fileName = new SimpleDateFormat("yyyyMMdd")
				.format(cal.getTime()) + ".xml";
		return fileName;
	}

	/**
	 * 数据对比
	 * 
	 * @param list
	 */
	private void checkData(List<Map<String, String>> list) {
		try {
			for (Map<String, String> map : list) {
				String sql = "select * from es_order where sn = ?";
				Order order = null;
				try {
					order = (Order) this.baseDaoSupport.queryForObject(sql,
							Order.class, map.get("oid").split("-")[2]);
				} catch (Exception e) {
					compare(map, order);
				}
				compare(map, order);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param map
	 * @param order
	 */
	private void compare(Map<String, String> map, Order order) {
		if (order == null) {
			insertDiffe(map);
			return;
		}

		if (!order.getOrder_amount().equals(map.get("amount"))) {
			insertDiffe(map);
		}
	}

	private void insertDiffe(Map<String, String> map) {
		this.baseDaoSupport.insert("es_checkbills", map);

	}

}
