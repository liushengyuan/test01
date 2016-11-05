package com.enation.app.shop.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.enation.app.shop.core.model.CustomerLogi;
import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.model.GoodLogisDetail;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.Logi;
import com.enation.app.shop.core.model.LogiOrderDTO;
import com.enation.app.shop.core.model.LogisModel;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;
 
/**
 * 物流公司管理类
 * @author LiFenLong 2014-4-2;4.0改版修改delete方法参数为integer[]
 *
 */
public class LogiManager extends BaseSupport implements ILogiManager{
	
	public void delete(Integer[] logi_id) {
		
		String id =StringUtil.implode(",", logi_id);
		if(id==null || id.equals("")){return ;}
		String sql = "delete from logi_company where id in (" + id + ")";
		this.baseDaoSupport.execute(sql);
	}

	
	public Logi getLogiById(Integer id) {
		String sql  = "select * from logi_company where id=?";
		Logi a =  (Logi) this.baseDaoSupport.queryForObject(sql, Logi.class, id);
		return a;
	}

	
	public Page pageLogi(String order, Integer page, Integer pageSize) {
		order = order == null ? " id desc" : order;
		String sql = "select * from logi_company";
		sql += " order by  " + order;
		Page webpage = this.baseDaoSupport.queryForPage(sql, page, pageSize);
		return webpage;
	}

	
	public void saveAdd(Logi logi) {
		this.baseDaoSupport.insert("logi_company", logi);
	}

	
	public void saveEdit(Logi logi ) {
		this.baseDaoSupport.update("logi_company", logi, "id="+logi.getId());
	}
	
	public List list() {
		String sql = "select * from logi_company";
		return this.baseDaoSupport.queryForList(sql);
	}


	@Override
	public Page pagefreight(String order, Integer page, Integer pageSize) {
		order = order == null ? " freight_id desc" : order;
		String sql = "select * from es_freight_standard";
		sql += " order by  " + order;
		Page webpage = this.baseDaoSupport.queryForPage(sql, page, pageSize);
		return webpage;
	}
   public int selectDistinctFreightName(String product_name){
	   String sql="SELECT  count(*)  from es_freight_standard where product_name=?";
	   int num=this.baseDaoSupport.queryForInt(sql, product_name);
	    return num;
   }
  public int selectDistinctLogisName(String name){
	  String sql="SELECT  count(*)  from es_logi_company where name=?";
	  int num=this.baseDaoSupport.queryForInt(sql, name);
	    return num;
  }
  public int selectDistinctLogisEname(String name){
	  String sql="SELECT  count(*)  from es_logi_company where binary ename=?";
	  int num=this.baseDaoSupport.queryForInt(sql, name);
	    return num;
  }
	@Override
	public void freightAdd(FreightStandard freightStandard) {
		// TODO Auto-generated method stub
        String sql="select count(*) from es_freight_standard";
        if(this.baseDaoSupport.queryForInt(sql)==0){
        	freightStandard.setFreight_id(1);
        	this.baseDaoSupport.insert("es_freight_standard",freightStandard);
        	String date=Long.toString(new Date().getTime())
					+ Integer.toString(new Random().nextInt(100));
    		LogisModel logisModel=new LogisModel();
    		logisModel.setLogis_model_id(date);
    		logisModel.setModel_name("customerName");
    		logisModel.setLogis_price_type(0);
    		logisModel.setFreight_id(freightStandard.getFreight_id());
            logisModel.setStore_id(0);
            logisModel.setIs_name(2);
            this.baseDaoSupport.insert("es_logis_model",logisModel);
        }else{
        	int max=this.baseDaoSupport.queryForInt("SELECT MAX(freight_id) from es_freight_standard");
        	freightStandard.setFreight_id(max+1);
        	this.baseDaoSupport.insert("es_freight_standard",freightStandard);
        	String date=Long.toString(new Date().getTime())
					+ Integer.toString(new Random().nextInt(100));
    		LogisModel logisModel=new LogisModel();
    		logisModel.setLogis_model_id(date);
    		logisModel.setModel_name("customerName");
    		logisModel.setLogis_price_type(0);
    		logisModel.setFreight_id(freightStandard.getFreight_id());
            logisModel.setStore_id(0);
            logisModel.setIs_name(2);
            this.baseDaoSupport.insert("es_logis_model",logisModel);
        }
		
	}


	@Override
	public void deleteFreight(Integer id) {
		// TODO Auto-generated method stub
		String sql = "delete from es_freight_standard where freight_id="+id;
		this.baseDaoSupport.execute(sql);
		
	}
	@Override
	public void deleteLogisModel(String id) {
		// TODO Auto-generated method stub
		////System.out.println(id);
		String sql = "delete from es_logis_model where logis_model_id="+id;
		this.baseDaoSupport.execute(sql);
		
	}
     

	@Override
	public FreightStandard getFreightById(Integer id) {
		String sql  = "select * from es_freight_standard where freight_id=?";
	    return (FreightStandard) this.baseDaoSupport.queryForObject(sql, FreightStandard.class, id);
	
	}
	
	@Override
	public LogisModel getModelNameById(String id) {
		String sql  = "select * from es_logis_model where logis_model_id=?";
	    return  (LogisModel) this.baseDaoSupport.queryForObject(sql, LogisModel.class, id);
	
	}
    public int selectModelByFreightId(Integer id){
    	String sql="select count(*) from es_logis_model where freight_id=?";
    	int num=this.baseDaoSupport.queryForInt(sql, id);
    	return num;
    }

	@Override
	public void saveEditFreight(FreightStandard freightStandard) {
		// TODO Auto-generated method stub
		this.baseDaoSupport.update("es_freight_standard", freightStandard, "freight_id="+freightStandard.getFreight_id());
	}


	@Override
	public List<CustomerLogi> getLogiModel() {
		List<CustomerLogi> logis=this.baseDaoSupport.queryForList("SELECT  l.logis_price_type , f.validatedays,f.freight_id,f.product_name from es_logis_model as l left JOIN es_freight_standard as f on l.freight_id=f.freight_id where l.model_name='customername';");
		return logis;
	}
	@Override
	public void addLogiModel(LogisModel logiManager){
		 this.baseDaoSupport.insert("es_logis_model",logiManager);
		 
	}
	@Override
	public int checkModelName(String modelname,Integer store_id){
		String sql="select count(*) from es_logis_model where model_name=? and store_id=?";
		int count=this.baseDaoSupport.queryForInt(sql,modelname,store_id);
		count = count > 0 ? 1 : 0;
		return count;
	}
	@Override
	public void updateLogiModel(LogisModel logisModel){
		this.baseDaoSupport.update("es_logis_model", logisModel,"logis_model_id="+logisModel.getLogis_model_id());
	}
	@Override
	public List<LogisModel> selectLogisModel(Integer id){
		return this.baseDaoSupport.queryForList("select DISTINCT e.model_name,e.is_name  from es_logis_model as e where store_id=? or store_id=0", LogisModel.class,id);
	}
    
	@Override
	public List<CustomerLogi> getLogiModel(String model_name,Integer storeid) {
		//System.out.println(model_name);
		List<CustomerLogi> customerLogis=null;
	  List<LogisModel> list=this.baseDaoSupport.queryForList("select * from es_logis_model where model_name=? and store_id=?",LogisModel.class,model_name,storeid);
		if(list.size()>0){
			customerLogis=new ArrayList<CustomerLogi>();
			//System.out.println("11111");
			  for (LogisModel logisModel : list) {
				  //System.out.println("22222");
			   CustomerLogi customerLogi=new CustomerLogi();
			   //System.out.println("3333");
			   Integer fright_id=logisModel.getFreight_id();
			   //System.out.println(fright_id);
			   FreightStandard freightStandard=(FreightStandard) this.baseDaoSupport.queryForObject("select * from es_freight_standard where freight_id=?",FreightStandard.class, fright_id);
			   Integer price_type=logisModel.getLogis_price_type();
			   customerLogi.setModel_name(logisModel.getModel_name());
			   customerLogi.setLogis_price_type(price_type);
			   customerLogi.setProduct_name(freightStandard.getProduct_name());
			   customerLogi.setValidatedays(freightStandard.getValidatedays());
			   customerLogi.setFreight_id(fright_id);
			   customerLogi.setLogis_model_id(logisModel.getLogis_model_id());
			   customerLogis.add(customerLogi);
		}
	  }
		return customerLogis;
	}


	@Override
	public Page searchLogiOrder(Map logiOrderMap, int page, int pageSize,
			Object object, String sort, String order) {
		String sql = creatTempSql(logiOrderMap, object);
		StringBuffer _sql = new StringBuffer(sql);
		_sql.append(" order by " + sort + " " + order);
		Page webpage = this.daoSupport.queryForPage(_sql.toString(), page,
				pageSize);
		return webpage;
	}


	private String creatTempSql(Map logiOrderMap, Object object) {
		object = object == null ? "" : object;
		
		Integer stype = (Integer) logiOrderMap.get("stype");
		String keyword = (String) logiOrderMap.get("keyword");
		
		String order_sn = (String) logiOrderMap.get("order_sn");
		String store_name = (String) logiOrderMap.get("store_name");
		String ship_no = (String) logiOrderMap.get("ship_no");
		String logi_name = (String) logiOrderMap.get("logi_name");
		
		String start_time = (String) logiOrderMap.get("start_time");
		String end_time = (String) logiOrderMap.get("end_time");
		
		StringBuffer sql = new StringBuffer();
		sql.append("select o.*,s.store_name as store_name from es_order o inner join es_store s on o.store_id=s.store_id  where o.disabled=0 and NOT ISNULL(parent_id) AND NOT ISNULL(o.logi_name) ");
		
		if (stype != null && keyword != null) {
			if (stype == 0) {
				sql.append(" and (sn like '%" + keyword + "%'");
				sql.append(" or store_name like '%" + keyword + "%')");
			}
		}
		
		if (order_sn != null && !StringUtil.isEmpty(order_sn)) {
			sql.append(" and sn like '%" + order_sn + "%'");
		}

		if (store_name != null && !StringUtil.isEmpty(store_name)) {
			sql.append(" and store_name like '" + store_name + "'");
		}
		
		if (ship_no != null && !StringUtil.isEmpty(ship_no)) {
			sql.append(" and ship_no like '%" + ship_no + "%'");
		}

		if (logi_name != null && !StringUtil.isEmpty(logi_name)) {
			sql.append(" and logi_name like '" + logi_name + "'");
		}
		
		
		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = com.enation.framework.util.DateUtil
					.getDateline(start_time + " 00:00:00");
			sql.append(" and o.create_time>" + stime);
		}
		if (end_time != null && !StringUtil.isEmpty(end_time)) {
			long etime = com.enation.framework.util.DateUtil
					.getDateline(end_time + " 23:59:59");
			sql.append(" and o.create_time<" + etime);
		}
		//System.out.println(sql.toString());
		return sql.toString();
	}


	@Override
	public List<LogiOrderDTO> exportLogiOrder(Map logiOrderMap) {
		String sql = creatTempSql(logiOrderMap, null);
		//StringBuffer _sql = new StringBuffer(sql);
		return this.baseDaoSupport.queryForList(sql, LogiOrderDTO.class);
	}
	public List<LogisModel> findLogisModel(Integer id){
		return this.baseDaoSupport.queryForList("select * from es_logis_model where is_name=?",LogisModel.class,id);
	}
	public void updateGoodLogisPrice(Goods goods){
		   this.baseDaoSupport.update("es_goods", goods,"goods_id="+goods.getGoods_id());
	}
	public void addGoodLogisDetail(GoodLogisDetail detail){
		String sql="select * from es_good_logis_detail where session_id="+"'"+detail.getSession_id()+"'"+" and goods_id="+detail.getGoods_id();
		GoodLogisDetail goodLogisDetail=(GoodLogisDetail) this.baseDaoSupport.queryForObject(sql, GoodLogisDetail.class);
		if(goodLogisDetail!=null){
			String sql1="update es_good_logis_detail set freight_id="+detail.getFreight_id()+",sendprice="+detail.getSendprice()+",currency="+"'"+detail.getCurrency()+"'"+"where session_id="+"'"+detail.getSession_id()+"'"+" and goods_id="+detail.getGoods_id() ;
			this.baseDaoSupport.execute(sql1);
		}else{
			this.baseDaoSupport.insert("es_good_logis_detail",detail);
		}
	
	}
	public void addGoodLogisDetail1(GoodLogisDetail detail){
			this.baseDaoSupport.insert("es_good_logis_detail",detail);
	}
  public List<GoodLogisDetail> findLogisDetail(Integer store_id,String session_id ){
	  List<GoodLogisDetail> details=this.baseDaoSupport.queryForList("select * from es_good_logis_detail where store_id=? and session_id=?",GoodLogisDetail.class,store_id,session_id);
	  return details;
  }
  public void deleteLogisModelByFreightId(Integer id){
	  String sql = "delete from es_logis_model where freight_id="+id;
		this.baseDaoSupport.execute(sql);
	  
  }
  public int getMaxLogisModelNAME(){
	return  this.baseDaoSupport.queryForInt("select max(is_name) from es_logis_model");
  }
  public void updateLogisModel(Double price,String session_id,Integer goods_id){
	  String sql="update es_good_logis_detail set sendprice=? where session_id=? and goods_id=?";
	  this.baseDaoSupport.execute(sql, price,session_id,goods_id);
  }
  public List<GoodLogisDetail> findLogisDetail1(Integer good_id,String session_id ){
	  List<GoodLogisDetail> details=this.baseDaoSupport.queryForList("select * from es_good_logis_detail where goods_id=? and session_id=?",GoodLogisDetail.class,good_id,session_id);
	  return details;
  }
  public LogisModel selectLogiModelByStore(Integer store_id,Integer freight_id){
	  String sql="select * from es_logis_model where is_name=? and freight_id=?";
	 LogisModel logisModel=(LogisModel) this.baseDaoSupport.queryForObject(sql, LogisModel.class, store_id,freight_id);
	return  logisModel;
  }

  /**
   * 订单成功提交后，根据sessiongId删除es_good_logis_detail表中相关数据
   * @param sessionId
   */
	@Override
	public void deleteLogisBySessiongId(String sessionId) {
		String sql = " delete from es_good_logis_detail where session_id=? ";
		this.baseDaoSupport.execute(sql, sessionId);
		
	}


@Override
public List<FreightStandard> getFreightByIdByDays() {
	String sql  = "select * from es_freight_standard";
    return this.baseDaoSupport.queryForList(sql, FreightStandard.class);
}
/**
 * 获取购物车运费总价 for APP
 * */
	@Override
	public int getFreightTotalPrice(String session_id){
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(l.sendprice) from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ,"+this.getTableName("store")+" s ,"+this.getTableName("good_logis_detail")+" l ");
		sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=? and c.is_select=1  AND c.store_id=s.store_id and l.session_id=? and l.goods_id=c.goods_id");
		return this.baseDaoSupport.queryForInt(sql.toString(), session_id,session_id);
	}


@Override
public FreightStandard getFreightStanddard(Integer product_name) {
	String sql="select * from es_freight_standard where product_name=?";
	FreightStandard freightStandard=(FreightStandard) this.baseDaoSupport.queryForObject(sql, FreightStandard.class, product_name);
	return freightStandard;
}


@Override
public void deleteLogisBySessiong(String sessionId, Integer goods_id) {
	String sql = " delete from es_good_logis_detail where session_id=? and goods_id=? ";
	this.baseDaoSupport.execute(sql, sessionId,goods_id);
	
}
}
