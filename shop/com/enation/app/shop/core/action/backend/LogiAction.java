package com.enation.app.shop.core.action.backend;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.model.Logi;
import com.enation.app.shop.core.model.LogiOrderDTO;
import com.enation.app.shop.core.model.LogisModel;
import com.enation.app.shop.core.model.Route;
import com.enation.app.shop.core.model.RouteRu;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.app.shop.core.service.IRouteManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 物流公司Action
 * 
 * @author LiFenLong 2014-4-2;4.0改版
 * 
 */
@Component
@Scope("prototype")
@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Action("logi")
@Results({
		@Result(name = "edit_freight", type = "freemarker", location = "/shop/admin/setting/freight_edit.html"),
		@Result(name = "add_freight", type = "freemarker", location = "/shop/admin/setting/freight_add.html"),
		@Result(name = "add_logi", type = "freemarker", location = "/shop/admin/setting/logi_add.html"),
		@Result(name = "edit_logi", type = "freemarker", location = "/shop/admin/setting/logi_edit.html"),
		@Result(name = "list_logi", type = "freemarker", location = "/shop/admin/setting/logi_list.html"),
		@Result(name = "details_logi", type = "freemarker", location = "/shop/admin/setting/logi_edit.html"),
		@Result(name = "details_logiOrder", type = "freemarker", location = "/shop/admin/setting/logi_order/details_logi_order.html"),
		@Result(name = "logi_order_list", type = "freemarker", location = "/shop/admin/setting/logi_order/logi_order_list.html"),
		@Result(name = "list_freight", type = "freemarker", location = "/shop/admin/setting/list_freight.html"), })
public class LogiAction extends WWAction {
	private ILogiManager logiManager;
	private String name;
	private Integer cid;
	private Integer[] id;
	private Integer[] freight_id;
	private String order;
	private Logi logi;
	private String ename;
	private String remark;
	private String time;
	private String url;
	private String type;
	private File pic;
	private Integer fid;
	private String picFileName;
	private String logis_name;
	private String product_name;
	private String validatedays;
	private double max_weight;// 最大重量限制
	private double min_weight;// 最小重量限制
	private Integer ffid;
	// 价格规则
	private double start_price;// 起步价
	private double every_price_kg;// 超出起步价中包含的重量收取的费用

	private double start_price_kg;// 起步价中包含的重量kg
	private double extra_price;// 附加单费

	private double start_price_ru;// 俄起步价
	private double every_price_kg_ru;// 俄每千克重量费用
	private double start_price_kg_ru;// 俄起步价中包含的重量kg
	private double extra_price_ru;// 俄附加单费
	// 尺寸规定
	private double max_long;// 最大长
	private double min_long;// 最小长
	private double max_width;// 最大宽
	private double min_width;// 最小宽
	private double max_high;// 最大高
	private double min_high;// 最小高
	// 平台模板
	private FreightStandard freightStandard;

	private String optype="no";
	private Map logiOrderMap;
	private Integer stype;
	private String keyword;
	protected String order_sn;
	protected String store_name;
	protected String ship_no;
	protected String logi_name;
	private String start_time;
	private String end_time;
	private String my_ship_no; // 发货单
	private IRouteManager routeManager;
	private List<RouteRu> listRouteEST;
	private List<Route> listRouteSF;
	/**
	 * 跳转至运费标准添加页面
	 * 
	 * @return 运费标准添加页面
	 */
	public String add_freight() {
		return "add_freight";
	}

	/**
	 * 跳转至物流公司添加页面
	 * 
	 * @return 物流公司添加页面
	 */
	public String add_logi() {
		return "add_logi";
	}

	/**
	 * 跳转至物流公司修改页面
	 * 
	 * @return 流公司修改页面
	 */
	public String edit_logi() {
		this.logi = this.logiManager.getLogiById(cid);
		return "edit_logi";
	}

	/**
	 * 跳转至运费模板修改页面
	 * 
	 * @return 运费模板修改页面
	 */
	public String edit_freight() {
		this.setFreightStandard(this.logiManager.getFreightById(fid));
		return "edit_freight";
	}

	/**
	 * 物流详细列表
	 * 
	 * @return 物流页面
	 */
	public String details_logi() {
		this.logi = this.logiManager.getLogiById(cid);
		return "details_logi";
	}

	/**
	 * 跳转至物流公司列表
	 * 
	 * @return 物流公司列表
	 */
	public String list_logi() {
		return "list_logi";
	}

	/**
	 * 获取物流公司列表Json
	 * 
	 * @author LiFenLong
	 * @param order
	 *            排序,String
	 * @return 物流公司列表Json
	 */
	public String list_logiJson() {
		this.webpage = this.logiManager.pageLogi(order, this.getPage(),
				this.getPageSize());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
	}

	/**
	 * 删除物流公司
	 * 
	 * @param id
	 *            ,物流公司Id
	 * @return result result 1.操作成功.0.操作失败
	 */
	public String delete() {
		try {
			this.logiManager.delete(id);
			this.showSuccessJson("删除成功");
		} catch (RuntimeException e) {
			this.showErrorJson("快递公司删除失败");
			logger.error("物流公司删除失败", e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 添加运费管理
	 * @return 运费模板列表
	 */
	public String freightAdd() {
		try {
			if(min_long>=max_long){
				this.showErrorJson("最小尺寸的长不能大于等于最大尺寸的长");
				return this.JSON_MESSAGE;
			}
			if(min_width>=max_width){
				this.showErrorJson("最小尺寸的宽不能大于等于最大尺寸的宽");
				return this.JSON_MESSAGE;
			}
			if(min_high>=max_high){
				this.showErrorJson("最小尺寸的高不能大于等于最大尺寸的高");
				return this.JSON_MESSAGE;
			}
			if(min_weight>=max_weight){
				this.showErrorJson("最小重量不能大于等于最大重量");
				return this.JSON_MESSAGE;
			}
		/*	if(min_weight<start_price_kg){
				this.showErrorJson("最小重量不能小于首重");
				return this.JSON_MESSAGE;
			}*/
			int num=this.logiManager.selectDistinctFreightName(product_name);
			if(num>=1){
				this.showErrorJson("物流产品添加重复，请重新选择");
				return this.JSON_MESSAGE;
			}
			
			FreightStandard freightStandard = new FreightStandard();
			freightStandard.setLogis_name(logis_name);
			freightStandard.setProduct_name(product_name);
			freightStandard.setValidatedays(validatedays);
			freightStandard.setMax_high(max_high);
			freightStandard.setMin_high(min_high);
			freightStandard.setMax_long(max_long);
			freightStandard.setMin_long(min_long);
			freightStandard.setMax_width(max_width);
			freightStandard.setMin_width(min_width);
			freightStandard.setMin_weight(min_weight);
			freightStandard.setMax_weight(max_weight);
			freightStandard.setMin_weight(min_weight);
			freightStandard.setStart_price(start_price);
			freightStandard.setStart_price_ru(start_price_ru);
			freightStandard.setStart_price_kg(start_price_kg);
			freightStandard.setStart_price_kg_ru(start_price_kg_ru);
			freightStandard.setEvery_price_kg(every_price_kg);
			freightStandard.setEvery_price_kg_ru(every_price_kg_ru);
			freightStandard.setExtra_price(extra_price);
			freightStandard.setExtra_price_ru(extra_price_ru);
			this.logiManager.freightAdd(freightStandard);
			this.showSuccessJson("添加成功");
		} catch (Exception e) {
			this.showErrorJson("运费模板添加失败");
			logger.error("运费模板添加失败", e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 删除运费模板
	 * 
	 * @return 运费模板列表
	 */
	public String deleteFreight() {
		try {
			for (int i = 0; i < freight_id.length; i++) {
				int num=this.logiManager.selectModelByFreightId(freight_id[i]);
				if(num>1){
					this.showErrorJson("此模板已经被引用，不能删除");
					return this.JSON_MESSAGE;
				}
				this.logiManager.deleteFreight(freight_id[i]);
				this.logiManager.deleteLogisModelByFreightId(freight_id[i]);
			}
			this.showSuccessJson("删除成功");
		} catch (RuntimeException e) {
			this.showErrorJson("运费模板删除失败");
			logger.error("运费模板删除失败", e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 添加物流公司
	 * 
	 * @param code
	 *            物流公司代码,String
	 * @param name
	 *            物流公司名称,String
	 * @param logi
	 *            物流公司,Logi
	 * @return result result 1.操作成功.0.操作失败
	 */
	public String saveAdd() {
		try {
			if(url.equals("")){
				this.showErrorJson("上传图片没有选择");						
				return this.JSON_MESSAGE;
			}
			int num=this.logiManager.selectDistinctLogisName(name);
			int num1=this.logiManager.selectDistinctLogisEname(ename);
			if(num>=1){
				this.showErrorJson("物流中文名称已添加，请重新输入");						
				return this.JSON_MESSAGE;
			}
			if(num1>=1){
				this.showErrorJson("物流英文名称已添加，请重新输入");						
				return this.JSON_MESSAGE;
			}
			if(name.equals(ename)){
				this.showErrorJson("物流中文和英文名称不能一致");						
				return this.JSON_MESSAGE;
			}
			Logi logi = new Logi();
			logi.setUrl(url);
			logi.setName(name);
			logi.setEname(ename);
			logi.setTime(time);
			logi.setType(type);
			logiManager.saveAdd(logi);
			this.showSuccessJson("添加成功");
		} catch (Exception e) {
			this.showErrorJson("物流公司添加失败");
			logger.error("物流公司添加失败", e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 修改物流公司
	 * 
	 * @param cid
	 *            物流公司Id,Integer
	 * @param code
	 *            物流公司代码,String
	 * @param name
	 *            物流公司名称,String
	 * @return result result 1.操作成功.0.操作失败
	 */
	public String saveEdit() {
		try {
			Logi logi1=this.logiManager.getLogiById(cid);
			int num=this.logiManager.selectDistinctLogisName(name);
			int num1=this.logiManager.selectDistinctLogisEname(ename);
			if(logi1.getName().equals(name)){
				
			}else{
				if(num>=1){
					this.showErrorJson("物流中文名称已添加，请重新输入");						
					return this.JSON_MESSAGE;
				}
			}
			if(logi1.getEname().equals(ename)){
				
			}else{
				if(num1>=1){
					this.showErrorJson("物流英文名称已添加，请重新输入");						
					return this.JSON_MESSAGE;
				}
			}
			if(name.equals(ename)){
				this.showErrorJson("物流中文和英文名称不能一致");						
				return this.JSON_MESSAGE;
			}
			
			Logi logi = new Logi();
			logi.setId(cid);
			logi.setEname(ename);
			logi.setName(name);
			logi.setUrl(url);
			logi.setType(type);
			this.logiManager.saveEdit(logi);
			this.showSuccessJson("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson("物流公司修改失败");
			logger.error("物流公司修改失败", e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 跳转至列表
	 * 
	 * @return 运费模板列表
	 */
	public String list_freight() {
		return "list_freight";
	}

	/**
	 * 修改运费模板
	 * 
	 * @return 运费模板列表
	 */
	public String saveEditFreight() {
		try {
			if(min_long>=max_long){
				this.showErrorJson("最小尺寸的长不能大于等于最大尺寸的长");
				return this.JSON_MESSAGE;
			}
			if(min_width>=max_width){
				this.showErrorJson("最小尺寸的宽不能大于等于最大尺寸的宽");
				return this.JSON_MESSAGE;
			}
			if(min_high>=max_high){
				this.showErrorJson("最小尺寸的高不能大于等于最大尺寸的高");
				return this.JSON_MESSAGE;
			}
			if(min_weight>=max_weight){
				this.showErrorJson("最小重量不能大于等于最大重量");
				return this.JSON_MESSAGE;
			}
		/*	if(min_weight<start_price_kg){
				this.showErrorJson("最小重量不能小于首重");
				return this.JSON_MESSAGE;
			}*/
			
		      FreightStandard freightStandard1=this.logiManager.getFreightById(ffid);
		      if(freightStandard1.getProduct_name().equals(product_name)){
		    	  
		      }else{
		    	  int num=this.logiManager.selectDistinctFreightName(product_name);
					//System.out.println(num);
					if(num>=1){
						this.showErrorJson("物流产品添加重复，请重新选择");
						return this.JSON_MESSAGE;
					}
		      }
			
			FreightStandard freightStandard = new FreightStandard();
			freightStandard.setFreight_id(ffid);
			freightStandard.setLogis_name(logis_name);
			freightStandard.setProduct_name(product_name);
			freightStandard.setValidatedays(validatedays);
			freightStandard.setMax_high(max_high);
			freightStandard.setMin_high(min_high);
			freightStandard.setMax_long(max_long);
			freightStandard.setMin_long(min_long);
			freightStandard.setMax_width(max_width);
			freightStandard.setMin_width(min_width);
			freightStandard.setMin_weight(min_weight);
			freightStandard.setMax_weight(max_weight);
			freightStandard.setMin_weight(min_weight);
			freightStandard.setStart_price(start_price);
			freightStandard.setStart_price_ru(start_price_ru);
			freightStandard.setStart_price_kg(start_price_kg);
			freightStandard.setStart_price_kg_ru(start_price_kg_ru);
			freightStandard.setEvery_price_kg(every_price_kg);
			freightStandard.setEvery_price_kg_ru(every_price_kg_ru);
			freightStandard.setExtra_price(extra_price);
			freightStandard.setExtra_price_ru(extra_price_ru);
			this.logiManager.saveEditFreight(freightStandard);
			this.showSuccessJson("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson("物流公司修改失败");
			logger.error("物流公司修改失败", e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 获取运费模板列表Json
	 * 
	 * @author LiFenLong
	 * @param order
	 *            排序,String
	 * @return 运费模板列表Json
	 */
	public String list_freightJson() {
		this.webpage = this.logiManager.pagefreight(order, this.getPage(),
				this.getPageSize());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
	}

	public String list_logiOrder() {
		return "logi_order_list";
	}
	
	public String list_logiOrderJson() {
		logiOrderMap = new HashMap();
		
		if(stype!=null){
			if(stype==0){
				logiOrderMap.put("stype", stype);
				logiOrderMap.put("keyword", keyword);
			}else if(stype==1){
				logiOrderMap.put("stype", stype);
				logiOrderMap.put("order_sn", this.order_sn);
				logiOrderMap.put("store_name", store_name);
				logiOrderMap.put("ship_no", ship_no);
				logiOrderMap.put("logi_name", logi_name);
				
				logiOrderMap.put("start_time", start_time);
				logiOrderMap.put("end_time", end_time);
			}
		}
		this.webpage =this.logiManager.searchLogiOrder(logiOrderMap, this.getPage(), this.getPageSize(), null,this.getSort(),this.getOrder());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
	}
	
	/**
	 * 物流详细
	 * 
	 * @return 物流详细
	 */
	public String details_logiOrder() {
		if(my_ship_no!=null){
			//俄速通轨迹
			this.listRouteEST = this.routeManager.getListru(my_ship_no);
			this.listRouteEST = listRouteEST==null ? new ArrayList() : listRouteEST;
			//顺丰轨迹
			this.listRouteSF = this.routeManager.getList(my_ship_no);
			this.listRouteSF = listRouteSF==null ? new ArrayList() : listRouteSF;
		}
		return "details_logiOrder";
	}
	
	/**
	 * 导出物流订单的信息
	 * @return
	 */
	public String exportLogiOrder() {
		Map logiOrderMap = new HashMap();
		if(stype!=null){
			if(stype==0){
				logiOrderMap.put("stype", stype);
				logiOrderMap.put("keyword", keyword);
			}else if(stype==1){
				logiOrderMap.put("stype", stype);
				logiOrderMap.put("order_sn", this.order_sn);
				logiOrderMap.put("store_name", store_name);
				logiOrderMap.put("ship_no", ship_no);
				logiOrderMap.put("logi_name", logi_name);
				
				logiOrderMap.put("start_time", start_time);
				logiOrderMap.put("end_time", end_time);
			}
		}
		List<LogiOrderDTO> logiOrderList = this.logiManager.exportLogiOrder(logiOrderMap);

		
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
        cell.setCellValue("订单号");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 1);  
        cell.setCellValue("店铺名称");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 2);  
        cell.setCellValue("物流单号");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 3);  
        cell.setCellValue("物流商 ");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("下单日期 ");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 5);  
        cell.setCellValue("运费");  
        cell.setCellStyle(style);
        
     
     // 第五步，写入实体数据 实际应用中这些数据从数据库得到。
        int count = 0;
        for(LogiOrderDTO logiOrder : logiOrderList){

        	row = sheet.createRow(count + 1); 
        	try {
				
				row.createCell((short) 0).setCellValue(logiOrder.getSn());  
		        row.createCell((short) 1).setCellValue(logiOrder.getStore_name()); 
		        row.createCell((short) 2).setCellValue(logiOrder.getShip_no()); 		        
		        row.createCell((short) 3).setCellValue(logiOrder.getLogi_name());
		        row.createCell((short) 4).setCellValue(this.getTime(logiOrder.getCreate_time()));
		        //System.out.println(logiOrder.getShiping_freight());
		        if(logiOrder.getShiping_freight()==null){
		        	row.createCell((short) 5).setCellValue(0); 
		        }else{
		        	 row.createCell((short) 5).setCellValue(logiOrder.getShiping_freight()); 
		        }
		       
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
            String filename = "wuliu.xls";//设置下载时客户端Excel的名称    
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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ILogiManager getLogiManager() {
		return logiManager;
	}

	public void setLogiManager(ILogiManager logiManager) {
		this.logiManager = logiManager;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Integer[] getId() {
		return id;
	}

	public void setId(Integer[] id) {
		this.id = id;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Logi getLogi() {
		return logi;
	}

	public void setLogi(Logi logi) {
		this.logi = logi;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public File getPic() {
		return pic;
	}

	public void setPic(File pic) {
		this.pic = pic;
	}

	public String getPicFileName() {
		return picFileName;
	}

	public void setPicFileName(String picFileName) {
		this.picFileName = picFileName;
	}

	public String getLogis_name() {
		return logis_name;
	}

	public void setLogis_name(String logis_name) {
		this.logis_name = logis_name;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getValidatedays() {
		return validatedays;
	}

	public void setValidatedays(String validatedays) {
		this.validatedays = validatedays;
	}

	public double getMax_weight() {
		return max_weight;
	}

	public void setMax_weight(double max_weight) {
		this.max_weight = max_weight;
	}

	public double getMin_weight() {
		return min_weight;
	}

	public void setMin_weight(double min_weight) {
		this.min_weight = min_weight;
	}

	public double getStart_price() {
		return start_price;
	}

	public void setStart_price(double start_price) {
		this.start_price = start_price;
	}

	public double getEvery_price_kg() {
		return every_price_kg;
	}

	public void setEvery_price_kg(double every_price_kg) {
		this.every_price_kg = every_price_kg;
	}

	public double getStart_price_kg() {
		return start_price_kg;
	}

	public void setStart_price_kg(double start_price_kg) {
		this.start_price_kg = start_price_kg;
	}

	public double getExtra_price() {
		return extra_price;
	}

	public void setExtra_price(double extra_price) {
		this.extra_price = extra_price;
	}

	public double getStart_price_ru() {
		return start_price_ru;
	}

	public void setStart_price_ru(double start_price_ru) {
		this.start_price_ru = start_price_ru;
	}

	public double getEvery_price_kg_ru() {
		return every_price_kg_ru;
	}

	public void setEvery_price_kg_ru(double every_price_kg_ru) {
		this.every_price_kg_ru = every_price_kg_ru;
	}

	public double getStart_price_kg_ru() {
		return start_price_kg_ru;
	}

	public void setStart_price_kg_ru(double start_price_kg_ru) {
		this.start_price_kg_ru = start_price_kg_ru;
	}

	public double getExtra_price_ru() {
		return extra_price_ru;
	}

	public void setExtra_price_ru(double extra_price_ru) {
		this.extra_price_ru = extra_price_ru;
	}

	public double getMax_long() {
		return max_long;
	}

	public void setMax_long(double max_long) {
		this.max_long = max_long;
	}

	public double getMin_long() {
		return min_long;
	}

	public void setMin_long(double min_long) {
		this.min_long = min_long;
	}

	public double getMax_width() {
		return max_width;
	}

	public void setMax_width(double max_width) {
		this.max_width = max_width;
	}

	public double getMin_width() {
		return min_width;
	}

	public void setMin_width(double min_width) {
		this.min_width = min_width;
	}

	public double getMax_high() {
		return max_high;
	}

	public void setMax_high(double max_high) {
		this.max_high = max_high;
	}

	public double getMin_high() {
		return min_high;
	}

	public void setMin_high(double min_high) {
		this.min_high = min_high;
	}

	public Integer[] getFreight_id() {
		return freight_id;
	}

	public void setFreight_id(Integer[] freight_id) {
		this.freight_id = freight_id;
	}

	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public Integer getFfid() {
		return ffid;
	}

	public void setFfid(Integer ffid) {
		this.ffid = ffid;
	}

	public FreightStandard getFreightStandard() {
		return freightStandard;
	}

	public void setFreightStandard(FreightStandard freightStandard) {
		this.freightStandard = freightStandard;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getOptype() {
		return optype;
	}
	public void setOptype(String optype) {
		this.optype = optype;
	}

	public Map getLogiOrderMap() {
		return logiOrderMap;
	}

	public void setLogiOrderMap(Map logiOrderMap) {
		this.logiOrderMap = logiOrderMap;
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

	

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getShip_no() {
		return ship_no;
	}

	public void setShip_no(String ship_no) {
		this.ship_no = ship_no;
	}

	public String getLogi_name() {
		return logi_name;
	}

	public void setLogi_name(String logi_name) {
		this.logi_name = logi_name;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getMy_ship_no() {
		return my_ship_no;
	}

	public void setMy_ship_no(String my_ship_no) {
		this.my_ship_no = my_ship_no;
	}

	public IRouteManager getRouteManager() {
		return routeManager;
	}

	public void setRouteManager(IRouteManager routeManager) {
		this.routeManager = routeManager;
	}

	public List<RouteRu> getListRouteEST() {
		return listRouteEST;
	}

	public void setListRouteEST(List<RouteRu> listRouteEST) {
		this.listRouteEST = listRouteEST;
	}

	public List<Route> getListRouteSF() {
		return listRouteSF;
	}

	public void setListRouteSF(List<Route> listRouteSF) {
		this.listRouteSF = listRouteSF;
	}
	
}
