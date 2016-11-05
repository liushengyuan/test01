package com.enation.app.shop.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.enation.app.shop.core.model.AllActivity;
import com.enation.app.shop.core.model.AllActivityProduct;
import com.enation.app.shop.core.model.CheckMemberLogin;
import com.enation.app.shop.core.service.IAllActivityManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

public class AllActivityManager extends BaseSupport implements IAllActivityManager {
private static final String str="20分钟内没有付款,订单失效";
    @Override
    public void addActivity(AllActivity allActivity) {
        String sql = " INSERT INTO es_allactivity VALUES (null,?,?,?,?,?,?,0,?,?,?,?,?,?,?)";
        this.baseDaoSupport.execute(sql, allActivity.getName(),allActivity.getIndex(),allActivity.getStart_time(),allActivity.getEnd_time(),allActivity.getIs_show(),allActivity.getLine_color(),allActivity.getType(),allActivity.getLimitbuy(),allActivity.getLimitnumber(),allActivity.getVirtual(),allActivity.getVirtualcount(),allActivity.getBonus_select(),allActivity.getDiscountnumber());
    }

    @Override
    public Page getAllActivity(Map map, Integer page, Integer pageSize) {
        String sql = this.createAllActivitySql(map);
        return this.baseDaoSupport.queryForPage(sql, page, pageSize);
    }
    
    private String createAllActivitySql(Map map){
        StringBuffer sql = new StringBuffer();
        StringBuffer _sql = new StringBuffer();
        sql.append(" SELECT a.id,a.name,a.index,a.start_time,a.end_time,a.is_show,a.line_color,a.open,a.type FROM es_allactivity as a WHERE 1=1 ");
        String name = (String) map.get("name");
        String start_time = (String)map.get("start_time");
        String end_time = (String)map.get("end_time");
        if(name!=null){
            _sql.append(" AND name LIKE '%"+name+"%' ");
        }
        /*if(start_time!=null){
            _sql.append(" AND START_TIME<"+start_time);
        }
        if(end_time!=null){
            _sql.append(" AND END_TIME>"+end_time);
        }*/
        return sql.toString();
    }

    @Override
    public List get(Integer type) {
        String sql = "SELECT * FROM es_allactivity AS e WHERE  e.type =?  ORDER BY e.`index` asc ";
        return this.baseDaoSupport.queryForList(sql,type);
    }
    @Override
    public List<AllActivity> getAllActivity(){
        String sql = "SELECT id,name,start_time,end_time,line_color,open FROM es_allactivity WHERE is_show=1 and type=1";
        return this.baseDaoSupport.queryForList(sql,AllActivity.class);
    }
    @Override
    public AllActivity getForId(Integer id) {
    	 String sql = "SELECT * FROM es_allactivity WHERE id=?";
        return (AllActivity)this.baseDaoSupport.queryForObject(sql, AllActivity.class, id);
    }

    @Override
    public void edit(AllActivity allActivity) {
        String sql ="UPDATE es_allactivity SET `name`=?,`index`=?,start_time=?,end_time=?,is_show=?,line_color=?,limitbuy=?,limitnumber=?,virtual=?,virtualcount=?,bonus_select=?,type=?,discountnumber=? WHERE id=?";
        this.baseDaoSupport.execute(sql, allActivity.getName(),allActivity.getIndex(),allActivity.getStart_time(),allActivity.getEnd_time(),allActivity.getIs_show(),allActivity.getLine_color(),allActivity.getLimitbuy(),allActivity.getLimitnumber(),allActivity.getVirtual(),allActivity.getVirtualcount(),allActivity.getBonus_select(),allActivity.getType(),allActivity.getDiscountnumber(),allActivity.getId());
    }

    @Override
    public void del(Integer[] id) {
        String sql = "DELETE FROM es_allactivity WHERE id=?";
        for(int i=0;i<id.length;i++){
            this.baseDaoSupport.execute(sql,id[i]);
        }
    }

    @Override
    public void editOpen(Integer id,Integer identification) {
        if(identification==1){
            String sql = "UPDATE es_allactivity SET open=1 WHERE id=?";
            this.baseDaoSupport.execute(sql,id);
            this.getAllActivityMarket(id);
        }
        if(identification==0){
            String sql = "UPDATE es_allactivity SET open=2,is_show=0 WHERE id=?";
            this.baseDaoSupport.execute(sql,id);
            List<AllActivity> list=this.getAlltivity();
       		 this.delActivityId(id,list);
        }
        if(identification==2){
        	String sql = "UPDATE es_allactivity SET open=0 WHERE id=?";
        	this.baseDaoSupport.execute(sql,id);
        	this.getAllActivityById(id);
        }
    }

	@Override
	public List getGoodsById(Integer goods_id) {
		String sql="select * from es_allactivity_product AS p LEFT JOIN es_allactivity AS a ON p.allactivity_id=a.id WHERE p.goods_id=? AND a.is_show=1 AND a.type=1 AND  a.open!=2";
		return this.baseDaoSupport.queryForList(sql, goods_id);
	}

	@Override
	public List checkGoodsIsExists(Integer goods_id) {
		String sql="select * from es_allactivity_product AS p LEFT JOIN es_allactivity AS a ON p.allactivity_id=a.id WHERE p.goods_id=? AND a.is_show=1 AND a.type=1 AND  a.open=1";
		List list=this.baseDaoSupport.queryForList(sql,goods_id);
		return list;
	}

	@Override
	public List<CheckMemberLogin> isCheck(Integer active_id, String address_id) {
		String str="select * from es_check_memberlogin where active_id=? AND address_id=? and is_order=0";
		List<CheckMemberLogin> list=this.baseDaoSupport.queryForList(str, CheckMemberLogin.class, active_id,address_id);
		return list;
	}

	@Override
	public void addMemberCheckLogin(CheckMemberLogin checkMemberLogin) {
		this.baseDaoSupport.insert("es_check_memberlogin", checkMemberLogin);
	}

	@Override
	public void delActivityId(Integer id,List<AllActivity> list) {
		if(list.size()==3){
			for (AllActivity allActivity : list) {
				 String sql = "DELETE FROM es_allactivity WHERE id=?";
				 String sql1 = "DELETE FROM es_allactivity_product WHERE allactivity_id=?";
				 this.baseDaoSupport.execute(sql,allActivity.getId());
				 this.baseDaoSupport.execute(sql1,allActivity.getId());
			}
		}else{
			 /*String sql = "DELETE FROM es_allactivity WHERE id=?";*/
			 String sql1 = "DELETE FROM es_allactivity_product WHERE allactivity_id=?";
			/* this.baseDaoSupport.execute(sql,id);*/
			 this.baseDaoSupport.execute(sql1,id);
		}
	}
	
	/**
	 * 每天10秒一次
	 * 订单10分钟之内没有进行付款的，系统自动取消，并恢复库存；其中600表示10分钟的总秒数，1398873600表示2014-05-01 00:00:00
	 */
	@Override
	public List<Map> getOrderMap() {
		String sql="select o.order_id,e.id from es_check_memberlogin AS e LEFT JOIN es_order AS o ON o.order_id=e.order_id where o.disabled=0 AND o.create_time+?<? AND (o.`status`=? or o.`status`=?) AND o.create_time>? AND o.store_id IS NOT NULL";
		List<Map> list= daoSupport.queryForList(sql,1200,DateUtil.getDateline(),OrderStatus.ORDER_NOT_PAY,OrderStatus.ORDER_NOT_CONFIRM,1398873600);
		if(list.size()>0){
			for (Map map : list) {
				Integer id=(Integer) map.get("id");
				String sql1="update es_check_memberlogin set is_order=1,reason=? where id=?";
				this.baseDaoSupport.execute(sql1,str,id);
			}
		}
		return list;
	}

	@Override
	public void getAllActivityById(int id) {
		String sql="select * from es_allactivity_product where allactivity_id=?";
		List<AllActivityProduct> list=this.baseDaoSupport.queryForList(sql, AllActivityProduct.class, id);
		if(list.size()>0){
			for (AllActivityProduct allActivityProduct : list) {
				String sql1="UPDATE es_goods SET market_enable=0 where goods_id=?";
				this.baseDaoSupport.execute(sql1, allActivityProduct.getGoods_id());
			}
		}
	}

	@Override
	public void getAllActivityMarket(int id) {
		String sql="select * from es_allactivity_product where allactivity_id=?";
		List<AllActivityProduct> list=this.baseDaoSupport.queryForList(sql, AllActivityProduct.class, id);
		if(list.size()>0){
			for (AllActivityProduct allActivityProduct : list) {
				String sql1="UPDATE es_goods SET market_enable=1 where goods_id=?";
				this.baseDaoSupport.execute(sql1, allActivityProduct.getGoods_id());
			}
		}
		
	}

	@Override
	public Page listSkillDetail(Map map, int page, int pageSize) {
		String sql = createTempSql(map);
		List list1 = this.daoSupport.queryForList(sql);
		List <Object>list2 = this.daoSupport.queryForListPage(sql.toString(), page, pageSize);
		Page webpage = new Page();
		webpage.setParam(0, list1.size(), 1, list2);
		return webpage;
	}

	private String createTempSql(Map map) {
		String keyword = (String) map.get("keyword");
		String orderstate = (String) map.get("order_state");// 订单状态
		String start_time = (String) map.get("start_time");
		String end_time = (String) map.get("end_time");
		Integer status = (Integer) map.get("status");
		Integer type = (Integer) map.get("type");
		StringBuffer sql = new StringBuffer();
		sql.append("select 	m.uname,e.*,g.goods_id,g.`name`,o.`status`,o.pay_status,o.ship_status,o.create_time,o.sn,o.need_pay_money,o.currency,m.member_id from es_check_memberlogin AS e LEFT JOIN es_order AS o ON e.order_id=o.order_id LEFT JOIN es_member AS m ON e.member_id=m.member_id LEFT JOIN es_goods AS g ON e.goods_id=g.goods_id ");
		if (!StringUtil.isEmpty(orderstate)) {
			if (orderstate.equals("wait_ship")) { // 对待发货的处理
				sql.append(" and ( ( payment_type!='cod' and payment_id!=8  and  status="
						+ OrderStatus.ORDER_PAY_CONFIRM + ") ");// 非货到付款的，要已结算才能发货
				sql.append(" or ( payment_type='cod' and  status="
						+ OrderStatus.ORDER_NOT_PAY + ")) ");// 货到付款的，新订单（已确认的）就可以发货
			} else if (orderstate.equals("wait_pay")) {
				sql.append(" and ( ( payment_type!='cod' and  status="
						+ OrderStatus.ORDER_NOT_PAY + ") ");// 非货到付款的，未付款状态的可以结算
				sql.append(" or ( payment_id=8 and status!="
						+ OrderStatus.ORDER_NOT_PAY + "  and  pay_status!="
						+ OrderStatus.PAY_CONFIRM + ")");
				sql.append(" or ( payment_type='cod' and  (status="
						+ OrderStatus.ORDER_SHIP + " or status="
						+ OrderStatus.ORDER_ROG + " )  ) )");// 货到付款的要发货或收货后才能结算

			} else if (orderstate.equals("wait_rog")) {
				sql.append(" and status=" + OrderStatus.ORDER_SHIP);
			} else {
				sql.append(" and status=" + orderstate);
			}

		}
		if(type!=null){
			if(type==0){
				//LEFT JOIN es_allactivity as a ON a.id=e.active_id WHERE a.type=0
				sql.append("LEFT JOIN es_allactivity as a ON a.id=e.active_id WHERE a.type=0");
			}else if(type==1){
				sql.append("LEFT JOIN es_allactivity as a ON a.id=e.active_id WHERE a.type=1");
			}
		}
		if (!StringUtil.isEmpty(keyword)) {
				sql.append(" and (o.sn like '%" + keyword + "%'");
				sql.append(" or m.uname like '%" + keyword + "%' or e.active_name like '%" + keyword + "%')");
		}
		if (status != null) {
			sql.append("and o.`status`=" + status);
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
		sql.append(" AND o.parent_id IS NOT NULL AND o.disabled=0 ");
		sql.append(" ORDER BY e.nowtime DESC");
		return sql.toString();
	}

	@Override
	public int listSkillDetailCount(Integer type) {
		String sql2="";
		String sql="SELECT count(DISTINCT(c.member_id))from es_check_memberlogin c,es_allactivity a WHERE  c.active_id=a.id";
		if(type!=null){
			 sql2=sql+" and type="+type;
		}
		sql.toString();
		Integer count=this.baseDaoSupport.queryForInt(sql2);
		return count;
	}

	@Override
	public int listSkillTotalCount(Integer type) {
		String sql2="";
		String sql="SELECT * from es_check_memberlogin c,es_allactivity a where c.active_id=a.id ";
		if(type!=null){
			sql2=sql+" and type="+type;
		}
		List list=this.baseDaoSupport.queryForList(sql2);
		return list.size();
	}

	@Override
	public List<AllActivity> getAlltivity() {
		String sql="select * from es_allactivity where open=2 and type=1";
		List<AllActivity> list=this.baseDaoSupport.queryForList(sql, AllActivity.class);
		return list;
	}

	@Override
	public List<AllActivity> getListALL(Integer[] id) {
		List<AllActivity> list=new ArrayList<AllActivity>();
		for (Integer integer : id) {
		    AllActivity allActivity=getForId(integer);
		    if(allActivity.getOpen()==1){
		    	 list.add(allActivity);
		    }
		}
		return list;
	}

	@Override
	public void updagePiceUrl() {
		StringBuffer sql1 = new StringBuffer();
		sql1.append(" UPDATE es_goods_gallery SET thumbnail = REPLACE ( thumbnail, 'fs:/attachment/', 'http://seller.tradeease.cn/statics/attachment/' ), small = REPLACE ( small, 'fs:/attachment/', 'http://seller.tradeease.cn/statics/attachment/' ), big = REPLACE ( big, 'fs:/attachment/', 'http://seller.tradeease.cn/statics/attachment/' ), original = REPLACE ( original, 'fs:/attachment/', 'http://seller.tradeease.cn/statics/attachment/' ), tiny = REPLACE ( tiny, 'fs:/attachment/', 'http://seller.tradeease.cn/statics/attachment/' ) ");
		StringBuffer sql2 = new StringBuffer();
		sql2.append(" UPDATE es_team_product set product_image=REPLACE(product_image,'http://www.tradeease.net/','http://seller.tradeease.cn/')");
		StringBuffer sql3 = new StringBuffer();
		sql3.append(" UPDATE es_team set team_image=REPLACE(team_image,'http://www.tradeease.net/','http://seller.tradeease.cn/'),pic_url=REPLACE(pic_url,'http://www.tradeease.net/','http://seller.tradeease.cn/') ");
		StringBuffer sql4 = new StringBuffer();
		sql4.append(" UPDATE es_goods SET intro = REPLACE ( intro, 'src=\"/attached', 'src=\"http://seller.tradeease.cn/attached' ), thumbnail = REPLACE ( thumbnail, 'fs:/attachment/', 'http://seller.tradeease.cn/statics/attachment/' ), small = REPLACE ( small, 'fs:/attachment/', 'http://seller.tradeease.cn/statics/attachment/' ), big = REPLACE ( big, 'fs:/attachment/', 'http://seller.tradeease.cn/statics/attachment/' ), original = REPLACE ( original, 'fs:/attachment/', 'http://seller.tradeease.cn/statics/attachment/' ) ");
		StringBuffer sql5 = new StringBuffer();
		sql5.append(" UPDATE es_adv set atturl=REPLACE(atturl,'fs:/','http://seller.tradeease.cn/statics/'),url=REPLACE(url,'http://www.tradeease.net/','http://seller.tradeease.cn/') ");
		StringBuffer sql6 = new StringBuffer();
		sql6.append(" UPDATE es_store set store_logo=REPLACE(store_logo,'http://www.tradeease.net/','http://seller.tradeease.cn/'), ");
		sql6.append(" id_img=REPLACE(id_img,'http://www.tradeease.net/','http://seller.tradeease.cn/'), ");
		sql6.append(" reverse_id_img=REPLACE(reverse_id_img,'http://www.tradeease.net/','http://seller.tradeease.cn/'), ");
		sql6.append(" bcard_img=REPLACE(bcard_img,'http://www.tradeease.net/','http://seller.tradeease.cn/') ");
		this.baseDaoSupport.execute(sql1.toString());
		this.baseDaoSupport.execute(sql2.toString());
		this.baseDaoSupport.execute(sql3.toString());
		this.baseDaoSupport.execute(sql4.toString());
		this.baseDaoSupport.execute(sql5.toString());
		this.baseDaoSupport.execute(sql6.toString());
	}

	@Override
	public List<AllActivity> getAllActivityDiscount() {
		 String sql = "SELECT id,name,start_time,end_time,line_color,open FROM es_allactivity WHERE is_show=1 and type=0";
	     return this.baseDaoSupport.queryForList(sql,AllActivity.class);
	}

	@Override
	public void editOpenByDiscount(Integer id, Integer identification) {
		if(identification==1){
            String sql = "UPDATE es_allactivity SET open=1 WHERE id=?";
            this.baseDaoSupport.execute(sql,id);
           /* this.getAllActivityMarket(id);*/
        }
        if(identification==0){
            String sql = "UPDATE es_allactivity SET open=2,is_show=0 WHERE id=?";
            this.baseDaoSupport.execute(sql,id);
        }
        if(identification==2){
        	String sql = "UPDATE es_allactivity SET open=0 WHERE id=?";
        	this.baseDaoSupport.execute(sql,id);
        	/*this.getAllActivityById(id);*/
        }
	}
}
