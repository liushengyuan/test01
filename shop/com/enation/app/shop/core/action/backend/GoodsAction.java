package com.enation.app.shop.core.action.backend;

/**
 * @author LiFenLong 4.0版本改造  2014-4-1
 */
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.b2b2c.core.model.order.StoreOrder;
import com.enation.app.b2b2c.core.service.goods.IStoreGoodsManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.Tag;
import com.enation.app.shop.core.model.support.GoodsEditDTO;
import com.enation.app.shop.core.plugin.goods.GoodsPluginBundle;
import com.enation.app.shop.core.service.IBrandManager;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IGoodsCatManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.ITagManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;
import com.enation.tool.LuceneService;
@Component
@Scope("prototype")
@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Action("goods")
@Results({
	@Result(name="list", type="freemarker", location="/shop/admin/goods/goods_list.html"),
	@Result(name="cat_tree", type="freemarker", location="/shop/admin/cat/select.html"),
	@Result(name="trash_list", type="freemarker", location="/shop/admin/goods/goods_trash.html"),
	@Result(name="input", type="freemarker", location="/shop/admin/goods/goods_input.html"),
	@Result(name="auditGoodsPage", type="freemarker", location="/shop/admin/goods/audit_goods.html"),
	@Result(name="auditStyleGoods", type="freemarker", location="/shop/admin/goods/audit_style_goods.html"),
	@Result(name="goodsInfo", type="freemarker", location="/shop/admin/goods/goods_info.html"),
	@Result(name="select_cat", type="freemarker", location="/shop/admin/goods/select_cat.html")
})
@SuppressWarnings({ "rawtypes", "unchecked", "serial","static-access" })
public class GoodsAction extends WWAction {

	protected String name;
	protected String sn;
	protected String order;
	private Integer catid;
	protected Integer[] goods_id;
	protected List brandList;
	protected Integer brand_id;
	protected Integer is_market;
	protected Goods goods;
	protected Map goodsView;
	protected Integer goodsId;
	protected List catList; // 所有商品分类
	protected IGoodsCatManager goodsCatManager;
	protected IBrandManager brandManager;
	protected IGoodsManager goodsManager;
	private ICartManager cartManager;
	private IStoreGoodsManager storeGoodsManager;
	private IOrderManager orderManager;
	protected Boolean is_edit;
	protected String actionName;
	protected Integer market_enable;
	private Integer[] tagids;

	private GoodsPluginBundle goodsPluginBundle;

	private ITagManager tagManager;
	private LuceneService luceneService;
	protected Map<Integer, String> pluginTabs;
	protected Map<Integer, String> pluginHtmls;

	private List<Tag> tagList;
	
	private String is_other;
	private Integer stype;
	private String keyword;
	private Map goodsMap;
	private String optype="no";
	private String storeName;
	private Integer auditStatus;
	private String auditDiscribe;
	private Integer searchTranslation;//是否被翻译
	private String store_market;
	private Integer store_initiallist;
	private Integer c;
	public Integer getStore_initiallist() {
		return store_initiallist;
	}
	public void setStore_initiallist(Integer store_initiallist) {
		this.store_initiallist = store_initiallist;
	}
	private Integer storeStatus;
	/**
	 * 获取店铺信息
	 * @return
	 */
	public String getProductInfo(){
		goodsMap=this.goodsManager.get(goodsId);
		//System.out.println(goodsMap.toString());
		return "goodsInfo";
	}
	/**
	 * 跳转选择添加商品的分类
	 * @return 选择添加商品的分类页面
	 */
	public String selectCat() {
		  return "select_cat";
	}
	/**
	 * 商品搜索
	 * @author xulipeng 2014年5月14日16:22:13
	 * @param stype 搜索类型,Integer
	 * @param keyword 关键字,String
	 * @param name 商品名称,String
	 * @param sn 商品编号,String
	 * @param catid 商品分类Id,Integer
	 * @param page 页码,Integer
	 * @param PageSize 每页显示数量,Integer
	 * @param sort 排序,String
	 * @return 商品搜索json
	 */
	public String searchGoods() {
		Map goodsMap = new HashMap();
		if(stype!=null){
			if(stype==0){
				goodsMap.put("stype", stype);
				goodsMap.put("keyword", keyword);
				goodsMap.put("storeName", storeName);
			}else if(stype==1){
				goodsMap.put("auditStatus", auditStatus);
				goodsMap.put("stype", stype);
				goodsMap.put("name", name);
				goodsMap.put("sn", sn);
				goodsMap.put("catid", catid);
				goodsMap.put("storeName", storeName);
			}
		}
		this.webpage =this.goodsManager.searchGoods(goodsMap, this.getPage(), this.getPageSize(), null,this.getSort(),this.getOrder());
		String s=  JSONArray.fromObject(webpage.getResult()).toString();
		this.json=s;
		return JSON_MESSAGE;
	}

	/**
	 * 商品列表
	 * @param brand_id 品牌Id,Integer
	 * @param catid 商品分类Id,Integer
	 * @param name 商品名称,String
	 * @param sn 商品编号,String 
	 * @param tagids 商品标签Id,Integer[]
	 * @return 商品列表页
	 */
	public String list() {
		goodsMap = new HashMap();

		goodsMap.put("brand_id", brand_id);
		goodsMap.put("is_market", is_market);
		goodsMap.put("catid", catid);
		goodsMap.put("name", name);
		goodsMap.put("sn", sn);
		goodsMap.put("tagids", tagids);
		
		this.brandList=this.brandManager.list();
		tagList = this.tagManager.list();

		this.webpage = goodsManager.searchGoods(goodsMap, this.getPage(),this.getPageSize(), is_other,"goods_id","desc");
		return "list";
	}

	/**
	 * @author LiFenLong
	 * 2014年5月14日18:09  xulipeng  修改
	 * @param stype 搜索类型,Integer
	 * @param keyword 搜索关键字,String
	 * @param catid 商品分类Id,Integer
	 * @param name 商品名称,String
	 * @param sn 商品编号,String 
	 * @return 商品列表页json
	 */
	public String listJson() {
		goodsMap = new HashMap();
		if(market_enable!=null && market_enable==1){
			goodsMap.put("market_enable", 1);
		}
		if(stype!=null){
			if(stype==0){
				goodsMap.put("stype", stype);
				goodsMap.put("keyword", keyword);
				goodsMap.put("storeName", storeName);
				goodsMap.put("store_initiallist", store_initiallist);
				goodsMap.put("auditStatus", auditStatus);
				goodsMap.put("searchTranslation", searchTranslation);
				goodsMap.put("store_market",store_market);
				goodsMap.put("storeStatus",storeStatus);
				goodsMap.put("c",c);
			}else if(stype==1){
				goodsMap.put("stype", stype);
				goodsMap.put("name", name);
				goodsMap.put("sn", sn);
				goodsMap.put("catid", catid);
				goodsMap.put("storeName", storeName);
				goodsMap.put("store_initiallist", store_initiallist);
				goodsMap.put("auditStatus", auditStatus);
				goodsMap.put("searchTranslation", searchTranslation);
				goodsMap.put("storeStatus",storeStatus);
				goodsMap.put("c",c);
			}
		}
		
		this.webpage =this.goodsManager.searchGoods(goodsMap, this.getPage(), this.getPageSize(), null,this.getSort(),this.getOrder());
		this.showGridJson(this.webpage);
		return JSON_MESSAGE;
	}
	public Integer getC() {
		return c;
	}
	public void setC(Integer c) {
		this.c = c;
	}
	/**
	 * 导出商品详情页的信息
	 * @return
	 */
	public String exportGoods() {
		goodsMap = new HashMap();
		if(market_enable!=null && market_enable==1){
			goodsMap.put("market_enable", 1);
		}
		if(stype!=null){
			if(stype==0){
				goodsMap.put("stype", stype);
				goodsMap.put("keyword", keyword);
				goodsMap.put("storeName", storeName);
				goodsMap.put("auditStatus", auditStatus);
				goodsMap.put("searchTranslation", searchTranslation);
				goodsMap.put("store_market",store_market);
			}else if(stype==1){
				goodsMap.put("stype", stype);
				goodsMap.put("keyword", keyword);
				goodsMap.put("name", name);
				goodsMap.put("sn", sn);
				goodsMap.put("catid", catid);
				goodsMap.put("storeName", storeName);
				goodsMap.put("auditStatus", auditStatus);
				goodsMap.put("searchTranslation", searchTranslation);
			}
		}
		List<StoreGoods> goodsList = this.goodsManager.exportGoods(goodsMap);
//		this.webpage =this.goodsManager.searchGoods(goodsMap, this.getPage(), this.getPageSize(), null,this.getSort(),this.getOrder());
//		this.showGridJson(this.webpage);
		
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook(); 
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("商品明细表"); 
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("商品编号");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 1);  
        cell.setCellValue("商品名称");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 2);  
        cell.setCellValue("店铺名称");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 3);  
        cell.setCellValue("店铺投向市场 ");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("分类 ");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 5);  
        cell.setCellValue("人民币价格 ");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 6);  
        cell.setCellValue("商品状态");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 7);  
        cell.setCellValue("上架");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 8);  
        cell.setCellValue("翻译");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 9);  
        cell.setCellValue("创建时间");  
        cell.setCellStyle(style);
        
     // 第五步，写入实体数据 实际应用中这些数据从数据库得到。
        int count = 0;
        for(StoreGoods goods : goodsList){

        	row = sheet.createRow(count + 1); 
        	try {
				
				row.createCell((short) 0).setCellValue(goods.getSn());  
		        row.createCell((short) 1).setCellValue(goods.getName()); 
		        row.createCell((short) 2).setCellValue(goods.getStore_name()); 		        
		        row.createCell((short) 3).setCellValue(this.getCountry(goods.getStore_market())); 
		        row.createCell((short) 4).setCellValue(goods.getCat_name()); 
		        row.createCell((short) 5).setCellValue(goods.getPrice()); 
		        
		        row.createCell((short) 6).setCellValue(this.getAuditStatus(goods.getAudit_status())); 
		        row.createCell((short) 7).setCellValue(this.getStatus(goods.getMarket_enable())); 
		        row.createCell((short) 8).setCellValue(this.getStatus(goods.getGoods_translation()));		        
		        row.createCell((short) 9).setCellValue(this.getTime(goods.getCreate_time())); 
		    
			} catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
				 this.showErrorJson("导出数据失败" + e.getMessage());
				 this.logger.error("导出数据失败", e);
			} 
        	
        	
        	count ++;
        	
        }
        
        // 第六步，将文件存到指定位置  
        try{
            String filename = "shangpin.xls";//设置下载时客户端Excel的名称    
            // 请见：http://zmx.iteye.com/blog/622529 
            //http://lancijk.iteye.com/blog/1390341   
            //filename = Util.encodeFilename(filename, request);    
            response.setContentType("application/vnd.ms-excel");    
            response.setHeader("Content-disposition", "attachment;filename=" + filename);    
            OutputStream ouputStream = response.getOutputStream();    
            wb.write(ouputStream);    
            ouputStream.flush();    
            ouputStream.close(); 
        }catch (Exception e){  
        	this.showErrorJson("导出失败" + e.getMessage());
			this.logger.error("导出失败", e);
			e.printStackTrace();
          
        } 
        
        this.showSuccessJson("成功导出！");
		
		return null;
	}
	
	/**
	 * 日期转换
	 */
	public String getTime (Long time){
		String text = "";
		if(time!=null&&time>0){
			text = DateUtil.toString(time,"yyyy-MM-dd");
		}		
		return text;
	}
	/**
	 * 店铺国家、市场转换
	 */
	public String getCountry(String name){
		String text = "";
		if(name.equals("CHN")){
			text = "中国";
		}else {
			text = "俄罗斯";
		}
		return text;
	}
	public String getAuditStatus(Integer status){
		String text = "";
		switch (status) {
		case 0:
			text = "待审核";
			break;
		case 1:
			text = "通过";
			break;
		case 2:
			text = "不通过";
			break;
		case 3:
			text = "符合要求";
			break;
		default:
			text = "状态错误";
			break;
		}
		return text;
	}
	public String getStatus(Integer status){
		String text = "";
		switch (status) {
		case 0:
			text = "否";
			break;
		case 1:
			text = "是";
			break;
		default:
			text = "否";
			break;
		}
		return text;
	}
	/**
	 * @author jfw
	 * 2015年10月16日18:09
	 * @param stype 搜索类型,Integer
	 * @param keyword 搜索关键字,String
	 * @param catid 商品分类Id,Integer
	 * @param name 商品名称,String
	 * @param sn 商品编号,String 
	 * @return 商品列表页json(用于出口商品审核)
	 */
	public String listRUSJson() {
		goodsMap = new HashMap();
		if(market_enable!=null && market_enable==1){
			goodsMap.put("market_enable", 1);
		}
		if(stype!=null){
			if(stype==0){
				goodsMap.put("stype", stype);
				goodsMap.put("keyword", keyword);
				goodsMap.put("storeName", storeName);
				goodsMap.put("auditStatus", auditStatus);
				goodsMap.put("searchTranslation", searchTranslation);
				goodsMap.put("store_market",store_market);
			}else if(stype==1){
				goodsMap.put("stype", stype);
				goodsMap.put("name", name);
				goodsMap.put("sn", sn);
				goodsMap.put("catid", catid);
				goodsMap.put("storeName", storeName);
				goodsMap.put("auditStatus", auditStatus);
			}
		}
		goodsMap.put("store_market", "RUS");
		this.webpage =this.goodsManager.searchGoods(goodsMap, this.getPage(), this.getPageSize(), null,this.getSort(),this.getOrder());
		this.showGridJson(this.webpage);
		return JSON_MESSAGE;
	}
	/**
	 * @author jfw
	 * 2015年10月16日18:09
	 * @param stype 搜索类型,Integer
	 * @param keyword 搜索关键字,String
	 * @param catid 商品分类Id,Integer
	 * @param name 商品名称,String
	 * @param sn 商品编号,String 
	 * @return 商品列表页json(用于进口商品审核)
	 */
	public String listCHNJson() {
		goodsMap = new HashMap();
		if(market_enable!=null && market_enable==1){
			goodsMap.put("market_enable", 1);
		}
		if(stype!=null){
			if(stype==0){
				goodsMap.put("stype", stype);
				goodsMap.put("keyword", keyword);
				goodsMap.put("storeName", storeName);
				goodsMap.put("auditStatus", auditStatus);
				goodsMap.put("store_initiallist", store_initiallist);
				goodsMap.put("searchTranslation", searchTranslation);
				goodsMap.put("store_market",store_market);
				goodsMap.put("storeStatus",storeStatus);
			}else if(stype==1){
				goodsMap.put("stype", stype);
				goodsMap.put("name", name);
				goodsMap.put("sn", sn);
				goodsMap.put("catid", catid);
				goodsMap.put("storeName", storeName);
				goodsMap.put("store_initiallist", store_initiallist);
				goodsMap.put("auditStatus", auditStatus);
				goodsMap.put("storeStatus",storeStatus);
			}
		}
		goodsMap.put("store_market", "CHN");
		this.webpage =this.goodsManager.searchGoods(goodsMap, this.getPage(), this.getPageSize(), null,this.getSort(),this.getOrder());
		this.showGridJson(this.webpage);
		return JSON_MESSAGE;
	}
	
	/**
	 * 所有的商品分类
	 * @param catList 商品分类列表,List
	 * @return 商品分类页面
	 */
	public String getCatTree() {
		this.catList = this.goodsCatManager.listAllChildren(0);
		return "cat_tree";
	}

	/**
	 * 跳转至商品回收站列表
	 * @return 商品回收站列表
	 */
	public String trash_list() {
		return "trash_list";
	}
	/**
	 * 获取商品回收站列表json
	 * @param name 商品名称,String
	 * @param sn 商品编号,String
	 * @param order 排序,String
	 * @return 商品回收站列表json
	 */
	public String trash_listJson(){
		this.webpage = this.goodsManager.pageTrash(name, sn, order,
				this.getPage(), this.getPageSize());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
	}

	/**
	 * 删除商品
	 * @author LiFenLong
	 * @param goods_id 商品Id数组,Integer[]
	 * @return json
	 * result 1.操作成功。0.操作失败
	 */
	public String delete() {
		if(EopSetting.IS_DEMO_SITE){
			for(Integer gid : goods_id){
				if(gid<=261){
					this.showErrorJson("抱歉，当前为演示站点，以不能修改这些示例数据，请下载安装包在本地体验这些功能！");
					return JSON_MESSAGE;
				}
			}
		}
		try {
			if (goods_id != null)
				for (Integer goodsid : goods_id) {
					if (cartManager.checkGoodsInCart(goodsid)) {
						this.showErrorJson("删除失败，此商品已加入购物车");
						return this.JSON_MESSAGE;
					}
					if (orderManager.checkGoodsInOrder(goodsid)) {
						this.showErrorJson("删除失败，此商品已经下单");
						return this.JSON_MESSAGE;
					}
				}
			this.goodsManager.delete(goods_id);
			this.showSuccessJson("删除成功");
		} catch (RuntimeException e) {
			this.showErrorJson("删除失败");
			logger.error("商品删除失败", e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 还原商品
	 * @param goods_id 商品Id数组,Integer[]
	 * @return json
	 * result 1.操作成功。0.操作失败
	 */
	public String revert() {
		try {
			this.goodsManager.revert(goods_id);
			this.showSuccessJson("还原成功");
		} catch (RuntimeException e) {
			this.showErrorJson("还原失败");
			logger.error("商品还原失败", e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 清除商品
	 * @param goods_id 商品Id数组,Integer[]
	 * @return json
	 * result 1.操作成功。0.操作失败
	 */
	public String clean() {
		try {
			this.goodsManager.clean(goods_id);
			this.showSuccessJson("清除成功");
		} catch (RuntimeException e) {
			this.showErrorJson("清除失败");
			logger.error("商品清除失败",e);
		}
		return this.JSON_MESSAGE;
	}

	


	/**
	 * 跳转到商品添加页
	 * @param actionName 添加商品方法,String
	 * @param is_edit 是否为修改商品,boolean
	 * @param pluginTabs 商品tab标题List,List
	 * @param pluginHtmls 商品添加内容List,List
	 * @return 商品添加页
	 */
	public String add() {
		actionName = "goods!saveAdd.do";
		is_edit = false;

		this.pluginTabs = this.goodsPluginBundle.getTabList();
		this.pluginHtmls = this.goodsPluginBundle.onFillAddInputData();

		return this.INPUT;
	}


	/**
	 * 跳转到商品修改页
	 * @param catList 商品分类列表,List
	 * @param actionName 修改商品方法,String
	 * @param is_edit 是否为修改商品,boolean
	 * @param goodsView 商品信息,Map
	 * @param pluginTabs 商品tab标题List,List
	 * @param pluginHtmls 商品添加内容List,List
	 * @return 商品编辑页
	 */
	public String edit() {
		actionName = "goods!saveEdit.do";
		is_edit = true;

		catList = goodsCatManager.listAllChildren(0);
		GoodsEditDTO editDTO = this.goodsManager.getGoodsEditData(goodsId);
		goodsView = editDTO.getGoods();

		this.pluginTabs = this.goodsPluginBundle.getTabList();
		this.pluginHtmls = editDTO.getHtmlMap();

		return this.INPUT;
	}

	/**
	 * 保存商品添加
	 * @param goods 商品,Goods
	 * @return json
	 * result 1.操作成功。0.操作失败
	 */
	public String saveAdd() {
		try {
			if(goods.getBrand_id().intValue()==0){
				this.showErrorJson("商品添加失败，请在属性中选择品牌!");
				return JSON_MESSAGE;
			}
			goodsManager.add(goods);
			Map data  = new HashMap();
			data.put("goodsid",  goods.getGoods_id());
			data.put("message", "商品添加成功！已经为跳转至修改页面，您可以继续修改此商品。");
			this.json=JsonMessageUtil.getObjectJson(data);

		} catch (RuntimeException e) {
			this.logger.error("添加商品出错", e);
			this.showErrorJson("添加商品出错" + e.getMessage());
		}

		return this.JSON_MESSAGE;
	}
	/**
	 * 保存商品修改
	 * @param goods 商品,Goods
	 * @return json
	 * result 1.操作成功。0.操作失败
	 */
	public String saveEdit() {
		try {
			Map data  = new HashMap();
			
			if(goods.getBrand_id().intValue()==0){
				this.showErrorJson("商品添加失败，请在属性中选择品牌!");
				return JSON_MESSAGE;
			}

			goodsManager.edit(goods);
			data.put("goodsid",  goods.getGoods_id());
			data.put("message", "商品修改成功");
			this.json=JsonMessageUtil.getObjectJson(data);

		} catch (RuntimeException e) {
			this.logger.error("修改商品出错", e);
			showErrorJson("修改商品出错" + e.getMessage());
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 更新商品上架状态
	 * @param goodsId 商品Id,Integer
	 * @return json
	 * result 1.操作成功。0.操作失败
	 */
	public String updateMarketEnable() {
		try {
			this.goodsManager.updateField("market_enable", 1, goodsId);
			this.showSuccessJson("更新上架状态成功");
		} catch (RuntimeException e) {
			this.showErrorJson("更新上架状态失败");
			logger.error("商品更新上架失败", e);
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 跳转商品发布审核页面
	 * @return
	 */
	public String auditGoodsPage(){
		this.goods=this.storeGoodsManager.getGoods(goodsId);
		return "auditGoodsPage";
	}
	/**
	 * 跳转商品规格审核页面
	 * @return
	 */
	public String auditStyleGoods(){
		this.goods=this.storeGoodsManager.getGoods(goodsId);
		return "auditStyleGoods";
	}
	/**
	 * 商品发布审核
	 * @return
	 */
	public String saveAuditGoods(){
		try {
			//Goods good=(Goods)this.goodsManager.get(goodsId);
/*			GoodsEditDTO dto = this.goodsManager.getGoodsEditData(goodsId);
			Goods goods = (Goods) dto.getGoods();
			if (goods != null) {
				goods.setAudit_status(auditStatus);
				goods.setAudit_discribe(auditDiscribe);
				this.goodsManager.edit(goods);
			}*/
			if(auditStatus==1){
				this.goodsManager.updateField("market_enable", 1, goodsId);
			}else if(auditStatus==2){
				this.goodsManager.updateField("market_enable", -1, goodsId);
			}
			this.goodsManager.updateField("audit_status", auditStatus, goodsId);
			this.goodsManager.updateField("audit_discribe", auditDiscribe, goodsId);
			this.storeGoodsManager.setGoodsNum(goodsId);//设置商品的上架数量（即出售数量）
			StoreGoods storeGoods=this.storeGoodsManager.getGoods(goodsId);
			this.luceneService.updateInformation(storeGoods.getGoods_id().toString(), storeGoods.getClass().getName(), storeGoods, true);
			this.showSuccessJson("审核成功");
		} catch (Exception e) {
			// TODO: handle exception
			this.showErrorJson("审核失败");
			logger.error("审核失败", e);
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 商品规格审核
	 * @return
	 */
	public String saveAuditStyleGoods(){
		try {
			if(auditStatus==1){
				this.goodsManager.updateField("market_enable", 1, goodsId);
			}else if(auditStatus==2){
				this.goodsManager.updateField("market_enable", -1, goodsId);
			}else if(auditStatus==3){
				this.goodsManager.updateField("market_enable", -1, goodsId);
			}
			this.goodsManager.updateField("audit_status", auditStatus, goodsId);
			this.goodsManager.updateField("audit_discribe", auditDiscribe, goodsId);
			this.storeGoodsManager.setGoodsNum(goodsId);//设置商品的上架数量（即出售数量）
			this.showSuccessJson("审核成功");
		} catch (Exception e) {
			// TODO: handle exception
			this.showErrorJson("审核失败");
			logger.error("审核失败", e);
		}
		return this.JSON_MESSAGE;
	}
	// set get
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Integer getCatid() {
		return catid;
	}
	public void setCatid(Integer catid) {
		this.catid = catid;
	}
	public Integer[] getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(Integer[] goods_id) {
		this.goods_id = goods_id;
	}
	public List getBrandList() {
		return brandList;
	}
	public void setBrandList(List brandList) {
		this.brandList = brandList;
	}
	public Integer getBrand_id() {
		return brand_id;
	}
	public void setBrand_id(Integer brand_id) {
		this.brand_id = brand_id;
	}
	public Integer getIs_market() {
		return is_market;
	}
	public void setIs_market(Integer is_market) {
		this.is_market = is_market;
	}
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public Map getGoodsView() {
		return goodsView;
	}
	public void setGoodsView(Map goodsView) {
		this.goodsView = goodsView;
	}
	public Integer getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	public List getCatList() {
		return catList;
	}
	public void setCatList(List catList) {
		this.catList = catList;
	}
	public IGoodsCatManager getGoodsCatManager() {
		return goodsCatManager;
	}
	public void setGoodsCatManager(IGoodsCatManager goodsCatManager) {
		this.goodsCatManager = goodsCatManager;
	}
	public IBrandManager getBrandManager() {
		return brandManager;
	}
	public void setBrandManager(IBrandManager brandManager) {
		this.brandManager = brandManager;
	}
	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}
	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}
	public ICartManager getCartManager() {
		return cartManager;
	}
	public void setCartManager(ICartManager cartManager) {
		this.cartManager = cartManager;
	}
	public IOrderManager getOrderManager() {
		return orderManager;
	}
	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}
	public Boolean getIs_edit() {
		return is_edit;
	}
	public void setIs_edit(Boolean is_edit) {
		this.is_edit = is_edit;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public Integer getMarket_enable() {
		return market_enable;
	}
	public void setMarket_enable(Integer market_enable) {
		this.market_enable = market_enable;
	}
	public Integer[] getTagids() {
		return tagids;
	}
	public void setTagids(Integer[] tagids) {
		this.tagids = tagids;
	}
	public GoodsPluginBundle getGoodsPluginBundle() {
		return goodsPluginBundle;
	}
	public void setGoodsPluginBundle(GoodsPluginBundle goodsPluginBundle) {
		this.goodsPluginBundle = goodsPluginBundle;
	}
	public ITagManager getTagManager() {
		return tagManager;
	}
	public void setTagManager(ITagManager tagManager) {
		this.tagManager = tagManager;
	}
	public Map<Integer, String> getPluginTabs() {
		return pluginTabs;
	}
	public void setPluginTabs(Map<Integer, String> pluginTabs) {
		this.pluginTabs = pluginTabs;
	}
	public Map<Integer, String> getPluginHtmls() {
		return pluginHtmls;
	}
	public void setPluginHtmls(Map<Integer, String> pluginHtmls) {
		this.pluginHtmls = pluginHtmls;
	}
	public List<Tag> getTagList() {
		return tagList;
	}
	public void setTagList(List<Tag> tagList) {
		this.tagList = tagList;
	}
	public String getIs_other() {
		return is_other;
	}
	public void setIs_other(String is_other) {
		this.is_other = is_other;
	}
	public Integer getStype() {
		return stype;
	}
	public void setStype(Integer stype) {
		this.stype = stype;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Map getGoodsMap() {
		return goodsMap;
	}
	public void setGoodsMap(Map goodsMap) {
		this.goodsMap = goodsMap;
	}
	public String getOptype() {
		return optype;
	}
	public void setOptype(String optype) {
		this.optype = optype;
	}
	public Integer getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getAuditDiscribe() {
		return auditDiscribe;
	}
	public void setAuditDiscribe(String auditDiscribe) {
		this.auditDiscribe = auditDiscribe;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public Integer getSearchTranslation() {
		return searchTranslation;
	}
	public void setSearchTranslation(Integer searchTranslation) {
		this.searchTranslation = searchTranslation;
	}
	public IStoreGoodsManager getStoreGoodsManager() {
		return storeGoodsManager;
	}
	public void setStoreGoodsManager(IStoreGoodsManager storeGoodsManager) {
		this.storeGoodsManager = storeGoodsManager;
	}
	public String getStore_market() {
		return store_market;
	}
	public void setStore_market(String store_market) {
		this.store_market = store_market;
	}
	public LuceneService getLuceneService() {
		return luceneService;
	}
	public void setLuceneService(LuceneService luceneService) {
		this.luceneService = luceneService;
	}
	public Integer getStoreStatus() {
		return storeStatus;
	}
	public void setStoreStatus(Integer storeStatus) {
		this.storeStatus = storeStatus;
	}
	
	
	
}
