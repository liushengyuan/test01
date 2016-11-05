package com.enation.app.b2b2c.core.service.order.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.order.IB2B2cOrderReportManager;
import com.enation.app.shop.core.model.PaymentLogType;
import com.enation.app.shop.core.model.RefundLog;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;
@Component
public class B2B2cOrderReportManager extends BaseSupport implements IB2B2cOrderReportManager {

	@Override
	public Page listPayment(Map map, int pageNo, int pageSize, String order) {
		String sql = createTempSql(map);
		return this.baseDaoSupport.queryForPage(sql, pageNo, pageSize);	
	}
	@SuppressWarnings("unused")
	private String  createTempSql(Map map){
		
		Integer stype = (Integer) map.get("stype");
		String keyword = (String) map.get("keyword");
		String start_time = (String) map.get("start_time");
		String end_time = (String) map.get("end_time");
		Integer status = (Integer) map.get("status");
		String sn = (String) map.get("sn");
		Integer paystatus = (Integer) map.get("paystatus");
		Integer payment_id = (Integer) map.get("payment_id");
		
		String sql = "select * from payment_logs where payment_id>0 and type="+PaymentLogType.receivable.getValue();
		sql+=" and LENGTH(order_sn)>12 ";
		if(stype!=null && keyword!=null){			
			if(stype==0){
				sql+=" and order_sn like '%"+keyword+"%'";
				/*sql+=" or ship_name like '%"+keyword+"%'";*/
			}
		}
		
		if(sn!=null && !StringUtil.isEmpty(sn)){
			sql+=" and order_sn like '%"+sn+"%'";
		}
		
		if(paystatus!=null){
			sql+=" and status="+paystatus;
		}
		
		if(payment_id!=null){
			sql+="and order_id in(select order_id from es_order where payment_id="+payment_id+")";
		}
		
		if(start_time!=null&&!StringUtil.isEmpty(start_time)){			
			long stime = com.enation.framework.util.DateUtil.getDateline(start_time+" 00:00:00");
			sql+=" and create_time>"+stime;
		}
		if(end_time!=null&&!StringUtil.isEmpty(end_time)){			
			long etime = com.enation.framework.util.DateUtil.getDateline(end_time +" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql+=" and create_time<"+(etime*1000);
		}
		
		sql += " order by payment_id desc";
		//////System.out.println(sql);
		return sql;
	}
	@Override
	public Page listRefund(int pageNo, int pageSize, String order) {
		String sql = "select * from sellback_list order by id desc ";
		Page webpage = this.baseDaoSupport.queryForPage(sql, pageNo, pageSize);
		return webpage;
	}
	@Override
	public void updateStatus(Integer status, Integer id) {
		// TODO Auto-generated method stub
		if (status != null && id != null) {
			String sql = "update  sellback_list set tradestatus=" + status + "  where id=" + id;
			this.baseDaoSupport.execute(sql);
		}
	}
	@Override
	public Page listRefund(int pageNo, int pageSize, String order, Map map) {
		String keyword = (String) map.get("keyword");
		StringBuffer sql=new StringBuffer();
		sql.append("select * from sellback_list");
		if (keyword!=null) {
			sql.append(" where ordersn like '%"+keyword+"%'");
			sql.append(" or tradeno like '%"+keyword+"%'");
		}
		sql.append(" order by id desc");
		Page webpage = this.baseDaoSupport.queryForPage(sql.toString(), pageNo, pageSize);
		return webpage;
	}

}
