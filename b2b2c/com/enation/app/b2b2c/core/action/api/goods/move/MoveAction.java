package com.enation.app.b2b2c.core.action.api.goods.move;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.goods.IStoreGoodsManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.shop.component.gallery.model.GoodsGallery;
import com.enation.app.shop.component.gallery.service.IGoodsGalleryManager;
import com.enation.app.shop.core.model.GoodsLvPrice;
import com.enation.app.shop.core.model.GoodsMove;
import com.enation.app.shop.core.model.Product;
import com.enation.app.shop.core.model.SpecValue;
import com.enation.app.shop.core.service.IDepotManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.IProductManager;
import com.enation.app.shop.core.service.SnDuplicateException;
import com.enation.app.tradeease.core.util.ImageMarkLogoUtil;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.jms.IJmsProcessor;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

@SuppressWarnings("serial")
@Component
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("move")
public class MoveAction extends WWAction {
	private IProductManager productManager;
	private IGoodsManager goodsManager;
	private IJmsProcessor goodsGalleryProcessor;
	private IStoreGoodsManager storeGoodsManager;
	private IStoreMemberManager storeMemberManager;
	private IDepotManager depotManager;
	private StoreGoods storeGoods;
	private IGoodsGalleryManager goodsGalleryManager;
	private IStoreManager storeManager;
	/*private String url;*/
	private String otherWeb;
	private double price;
	private String catid;
	@SuppressWarnings("rawtypes")
	private Map map;
	@SuppressWarnings("rawtypes")
	private List list;

	@SuppressWarnings({ "rawtypes", "static-access" })
	public String move() throws Exception{
		try {
			if(!otherWeb.contains("http://www.letao.com")){
				 this.showErrorJson("请输入乐淘的网址");
				 return this.JSON_MESSAGE;
			}
			List<String> list=new ArrayList<String>();
			Integer[] spec_ids=new Integer[3];
			spec_ids[0]=this.goodsManager.getSpecId("颜色");
			spec_ids[1]=this.goodsManager.getSpecId("男女鞋尺寸");
			spec_ids[2]=this.goodsManager.getSpecId("衣服尺寸");
			List<String> haveSpec=this.goodsManager.getCatNumber(Integer.parseInt(catid));
			System.out.println("选择的分类规则为："+JSONArray.fromObject(haveSpec));
			StoreMember storeMember = storeMemberManager.getStoreMember();
			int storeid = storeMember.getStore_id();
			Map<String,Object> map=MoveDetailUtil.moveMainMethod(otherWeb, storeid,haveSpec,spec_ids,goodsManager);
			if(map.get("Read timed out")!=null){
				this.showErrorJson("读取超时,请重试!");
				return this.JSON_MESSAGE;
			}else if (map.get("connect timed out")!=null){
				this.showErrorJson("连接超时,请重试!");
				return this.JSON_MESSAGE;
			}else if(map.get("www.letao.com")!=null){
				this.showErrorJson("您输入的访问地址不能连通,请重试!");
				return this.JSON_MESSAGE;
			}
			storeGoods=new StoreGoods();
			storeGoods.setStore_id(storeid);
			storeGoods.setDeliveryregion(0);
			storeGoods.setAudit_status(0);
			String orignalprice="";
			String discountprice="";
			String keyword="";
			String name="";
			if(map.get("orignalprice")!=null){
				orignalprice=(String) map.get("orignalprice");
			}
			if(map.get("discountprice")!=null){
				discountprice=(String) map.get("discountprice");
			}
			if(map.get("name")!=null){
				name=(String) map.get("name");
			}
			if(map.get("keyword")!=null){
				keyword=(String) map.get("keyword");
			}
			if(orignalprice!=null && !StringUtil.isEmpty(orignalprice) && discountprice!=null && !StringUtil.isEmpty(discountprice)){
				storeGoods.setIs_discount(1);
				storeGoods.setCost_price(Double.valueOf(orignalprice));
				storeGoods.setPrice(Double.valueOf(discountprice));
			}else if(orignalprice!=null && !StringUtil.isEmpty(orignalprice) && discountprice==null && StringUtil.isEmpty(discountprice)){
				storeGoods.setPrice(Double.valueOf(orignalprice));
				storeGoods.setIs_discount(0);
			}else if(orignalprice==null && StringUtil.isEmpty(orignalprice) && discountprice!=null && !StringUtil.isEmpty(discountprice)){
				storeGoods.setPrice(Double.valueOf(discountprice));
				storeGoods.setIs_discount(0);
			}
			
			List<String> listdetail=(List<String>) map.get("listdetail");
			String pinimage="";
			if(listdetail.size()>0){
				pinimage+="<p style='text-align:center;'>";
				for (String string : listdetail) {
					 pinimage+="<img alt='' src='"+string+"' />";
				}
				pinimage+="</p>";
			}
			String detailcontent=(String) map.get("detailcontent");	
			String pintotal="";
			if(detailcontent!=null && !StringUtil.isEmpty(detailcontent)){
				pintotal="<p align='center'><table style='width:50%;' bordercolor='#000000' cellspacing='0' cellpadding='2' border='1'>"+detailcontent+"</table></p>"+pinimage;
			}else{
				pintotal=pinimage;
			}
			storeGoods.setIntro(pintotal);
			System.out.println(pintotal);
			if (storeGoods.getPrice_ru()==null || storeGoods.getPrice_ru().equals("")) {
				storeGoods.setPrice_ru(0.0);
			}
			if(name!=null && !StringUtil.isEmpty(name)){
				storeGoods.setName(name);
			}
			if(keyword!=null && !StringUtil.isEmpty(keyword)){
				storeGoods.setMeta_keywords(keyword);
			}
			if(catid!=null && !StringUtil.isEmpty(catid)){
				storeGoods.setCat_id(Integer.parseInt(catid));
			}
			if(storeGoods.getWhprice()==null ){
				storeGoods.setWhprice(0D);
			}
			if(storeGoods.getWhprice_ru()==null){
				storeGoods.setWhprice_ru(0D);
			}
			if(storeGoods.getWhprice_en()==null){
				storeGoods.setWhprice_en(0D);
			}
			if(storeGoods.getWholesale_volume()==null){
				storeGoods.setWholesale_volume(0);
			}
			storeGoods.setMarket_enable(-1);
			Store store = this.storeManager.getStore(storeid);
			storeGoods.setStore_name(store.getStore_name());
			storeGoods.setSn(Long.toString(new Date().getTime())
					+ Integer.toString(new Random().nextInt(100)));
			storeGoods.setExpire_time(DateUtil.getDateline(DateUtil
					.getDateAfterForGoods(365)));
			storeGoods.setBrand_id(0);
			storeGoods.setIs_belong(6);
			storeGoods.setDeliveryregion(0);
			storeGoods.setFreightType(1);
			storeGoods.setMin_number(1);
			storeGoods.setGoods_transfee_charge(1);
			storeGoods.setVaild_day(365);
			storeGoods.setDepth("0");
			storeGoods.setWidth("0");
			storeGoods.setHeight("0");
			storeGoods.setType_id(1);
			storeGoods.setPoint(0);
			Map goodsMap = po2Map(storeGoods);
			goodsMap.put("disabled", 0);
			goodsMap.put("create_time", DateUtil.getDateline());
			goodsMap.put("view_count", 0);
			goodsMap.put("buy_count", 0);
			goodsMap.put("last_modify", DateUtil.getDateline());
			goodsMap.put("buy_num", 0) ;	//购买数量
			goodsMap.put("comment_num", 0);	//评论次数
			goodsMap.put("is_groupbuy", 0);  //是否为团购商品
			goodsMap.put("params",JSONArray.fromObject(list));
			goodsMap.put("store", 0);
			goodsMap.put("enable_store", 0);
			goodsMap.put("goods_type", "normal");
			if(haveSpec.size()>0){
				System.out.println(map.toString());
			      Integer have_spec=this.addSpec(map, haveSpec);
			      if(have_spec==0){
			    	  goodsMap.put("have_spec", 0);
			      }else{
			    	  goodsMap.put("have_spec", 1);
			      }
			}else{
				goodsMap.put("have_spec", 0);
			}
			Integer goods_id = this.goodsManager.getGoodsId(goodsMap);
			goodsMap.put("goods_id", goods_id);
			this.goodsManager.addDepot(goods_id);
			List<String> picnames=(List<String>) map.get("smallimgsList");
			System.out.println(picnames.toString());
			String[] str=new String[picnames.size()];
			for (int i = 0; i < picnames.size(); i++) {
				str[i]=picnames.get(i);
			}
			String image_default =(String) map.get("bigimgs");
			proessPhoto(str, goodsMap, image_default);
			/*transIntro(goodsMap);*/
			addSpecProduct(map, goodsMap,goods_id);
			this.showSuccessJson("发布成功");
		} catch (Exception e) {
			this.showErrorJson("发布失败");
			e.printStackTrace();
		}
		/*this.goodsManager.addGoodsMove(goodsMove);*/
		return this.JSON_MESSAGE;
	}
	/**
	 * 将po对象中有属性和值转换成map
	 * 
	 * @param po
	 * @return
	 */
	protected Map po2Map(Object po) {
		Map poMap = new HashMap();
		Map map = new HashMap();
		try {
			map = BeanUtils.describe(po);
		} catch (Exception ex) {
		}
		Object[] keyArray = map.keySet().toArray();
		for (int i = 0; i < keyArray.length; i++) {
			String str = keyArray[i].toString();
			if (str != null && !str.equals("class")) {
				if (map.get(str) != null) {
					poMap.put(str, map.get(str));
				}
			}
		}
		return poMap;
	}
	public void transIntro(Map goods){
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		String[] list=goods.get("intro").toString().split("src=\"");
		String app_domain=request.getSession().getServletContext().getRealPath("/");
		////System.out.println(list.length);
		for(int i=0;i<list.length;i++){
			if(list[i].contains("/attached/image/")){
				//ImageMarkLogoUtil.markImageByText("TradeEase",app_domain+list[i].split("\"")[0].replace("/", "\\"),app_domain+list[i].split("\"")[0].replace("/", "\\"));
				ImageMarkLogoUtil.markImageByIcon(app_domain+"themes\\b2b2cv2\\images\\logo-1x.png",app_domain+list[i].split("\"")[0].replace("/", "\\"),app_domain+list[i].split("\"")[0].replace("/", "\\"));
			}
		}			
	}
	/**
	 * 处理图片，包括商品新增时和修改时的图片数据<br/>
	 * 处理原则为向页面输出为全地址，保存在库里的为相对地址，以fs:开头，以区分网络远程图片。<br/>
	 * 在显示的时候将fs:替换为静态资源服务器地址 页面中传递过来的图片地址为:http://<staticserver>/<image path>如:<br/>
	 * http://static.enationsoft.com/attachment/goods/1.jpg<br/>
	 * 存在库中的为/attachment/goods/1.jpg
	 */
 
	public void proessPhoto(String[] picnames, Map goods, String image_default) {
		if (picnames == null) {
			return;
		}		

		// 生成相册列表，待jsm处理器使用
		List<GoodsGallery> galleryList = new ArrayList<GoodsGallery>();

		for (int i = 0; i < picnames.length; i++) {
			GoodsGallery gallery = new GoodsGallery();

			String filepath = picnames[i];

			// 生成小缩略图
			String tiny = getThumbPath(filepath, "_tiny");
			// 生成缩略图
			String thumbnail = getThumbPath(filepath, "_thumbnail");
			// 生成小图
			String small = getThumbPath(filepath, "_small");
			// 生成大图
			String big = getThumbPath(filepath, "_big");

			gallery.setOriginal(filepath); // 相册原始路径
			gallery.setBig(big);
			gallery.setSmall(small);
			gallery.setThumbnail(thumbnail);
			gallery.setTiny(tiny);
			galleryList.add(gallery);
			
			//设置为默认图片
			if(!StringUtil.isEmpty(image_default) && image_default.equals(filepath)){
				gallery.setIsdefault(1);
			}
		}
		Map<String, Object> param = new HashMap<String, Object>(2);
		param.put("galleryList", galleryList);
		param.put("goods", goods);

		//改非异步的方式-2015-06-09
		this.goodsGalleryProcessor.process(param);
	}
	public  void addProduct(Map goods,Integer goodsId){
		Product product = this.productManager.getByGoodsId(goodsId);
		if (product == null) {
			product = new Product();
		}

		product.setGoods_id(goodsId);
		if(null!=goods.get("cost") && !"".equals(goods.get("cost"))) {
			product.setCost(Double.valueOf("" + goods.get("cost")));
		}else{
			product.setCost(0D);
		}
		product.setPrice(Double.valueOf("" + goods.get("price")));
		product.setPrice_ru(Double.valueOf("" + goods.get("price_ru")));
		if(goods.get("price_en")!=null){
			product.setPrice_en(Double.valueOf("" + goods.get("price_en")));
		}
		//product.setPrice_en(Double.valueOf("" + goods.get("price_en")));
		//product.setWhprice(Double.valueOf("" + goods.get("whprice")));
		if(goods.get("whprice")!=null){
			product.setWhprice(Double.valueOf("" + goods.get("whprice")));
		}else{
			product.setWhprice(0.00);
		}
		if(goods.get("whprice_ru")!=null){
			product.setWhprice_ru(Double.valueOf("" + goods.get("whprice_ru")));
		}else{
			product.setWhprice_ru(0.00);
		}
		
		if(goods.get("whprice_en")!=null){
			product.setWhprice_en(Double.valueOf("" + goods.get("whprice_en")));
		}else{
			product.setWhprice_en(0.00);
		}
		if(goods.get("wholesale_volume")!=null){
			product.setWholesale_volume(Integer.valueOf(""+goods.get("wholesale_volume")));
		}else{
			product.setWholesale_volume(0);
		}
		product.setSn((String) goods.get("sn"));
		product.setWeight(Double.valueOf("" + goods.get("weight")));
		product.setName((String) goods.get("name"));
		product.setStore(0);                                              //如果添加是不默认是0，那么添加时候eclicp报错，如果有问题，请高手们优先chu'li
		List<Product> productList = new ArrayList<Product>();
		productList.add(product);
		this.productManager.add(productList);
	}
	public  Integer addSpec(Map<String,Object> map,List<String> list){
		Integer count=0;
		if(map.get("catagory")!=null){
			String catagory=map.get("catagory").toString();
			if(Integer.parseInt(catagory)==1){
				Integer[] specs=(Integer[]) map.get("specs");
				Integer[] spec_values=(Integer[]) map.get("spec_values");
				if(specs.length>0 && spec_values.length>0){
					count++;
				}
			}else if(Integer.parseInt(catagory)==2){
				Integer[] specs=(Integer[]) map.get("specs");
				Integer[] spec_values=(Integer[]) map.get("spec_values");
				if(specs.length>0 && spec_values.length>0){
					count++;
				}
			}else if(Integer.parseInt(catagory)==3){
				Integer[] specs=(Integer[]) map.get("specs");
				Integer[] spec_values=(Integer[]) map.get("spec_values");
				if(specs.length>0 && spec_values.length>0){
					count++;
				}
			}else if(Integer.parseInt(catagory)==4){
				Integer[] specs=(Integer[]) map.get("specs");
				Integer[] spec_values=(Integer[]) map.get("spec_values");
				Integer[] spec_values_size=(Integer[]) map.get("spec_values_size");
				if(specs.length>0 && spec_values.length>0 && spec_values_size.length>0){
					count++;
				}
			}else if(Integer.parseInt(catagory)==5){
				Integer[] specs=(Integer[]) map.get("specs");
				Integer[] spec_values=(Integer[]) map.get("spec_values");
				Integer[] spec_values_size=(Integer[]) map.get("spec_values_size");
				if(specs.length>0 && spec_values.length>0 && spec_values_size.length>0){
					count++;
				}
			}
		}
		return count;
	}
	public void addSpecProduct(Map<String,Object> map,Map goods,Integer goods_id){
		if(goods.get("have_spec")!=null){
			Integer have_spec=Integer.parseInt(goods.get("have_spec").toString());
			if(have_spec==1){
				if(map.get("catagory")!=null){
					String catagory=map.get("catagory").toString();
					if(Integer.parseInt(catagory)==1){
						Integer[] specs=(Integer[]) map.get("specs");
						Integer[] spec_values=(Integer[]) map.get("spec_values");
						if(specs.length>0 && spec_values.length>0){
							Integer lenght=specs.length*spec_values.length;
							String[] specidsAr =new String[lenght];
							for (int i = 0; i < specidsAr.length; i++) {
								specidsAr[i]=specs[0]+"";
							}
							String[] specvidsAr=new String[spec_values.length];
							for (int i = 0; i < specvidsAr.length; i++) {
								specvidsAr[i]=spec_values[i]+"";
							}
							String[] productids=new String[lenght];
							for (int i = 0; i < productids.length; i++) {
								productids[i]="";
							}
							String[] sns=new String[lenght];
							for (int i = 0; i < sns.length; i++) {
								sns[i]="";
							}
							String[] prices=new String[lenght];
							if(goods.get("price")!=null){
								for (int i = 0; i < prices.length; i++) {
									prices[i]=goods.get("price").toString();
								}
							}else{
								for (int i = 0; i < prices.length; i++) {
									prices[i]="";
								}
							}
							String[] prices_ru=new String[lenght];
							for (int i = 0; i < prices_ru.length; i++) {
								prices_ru[i]="";
							}
							String[] prices_en=new String[lenght];
							for (int i = 0; i < prices_en.length; i++) {
								prices_en[i]="";
							}
							String[] whprices=new String[lenght];
							for (int i = 0; i < whprices.length; i++) {
								whprices[i]="";
							}
							String[] whprices_ru=new String[lenght];
							for (int i = 0; i < whprices_ru.length; i++) {
								whprices_ru[i]="";
							}
							String[] whprices_en=new String[lenght];
							for (int i = 0; i < whprices_en.length; i++) {
								whprices_en[i]="";
							}
							String[] wholesale_volumes=new String[lenght];
							for (int i = 0; i < wholesale_volumes.length; i++) {
								wholesale_volumes[i]="";
							}
							String[] weights=new String[lenght];
							if(goods.get("weight")!=null){
								for (int i = 0; i < weights.length; i++) {
									weights[i]=goods.get("weight").toString();
								}
							}else{
								for (int i = 0; i < weights.length; i++) {
									weights[i]="";
								}
							}
							List<Product> productList = new ArrayList<Product>();
							int i = 0;
							int snIndex = this.getSnsSize(sns);
							for (String sn : sns) {
								Integer productId = StringUtil.isEmpty(productids[i]) ? null : Integer.valueOf(productids[i]);
								if (sn == null || sn.equals("")) {
									sn = goods.get("sn") + "-" + (snIndex + 1);
									snIndex++;
								}
							
								/*
								 * 组合商品、货品、规格值、规格对应关系
								 */
								List<SpecValue> valueList = new ArrayList<SpecValue>();
								int j = 0;
								String[] specids = specidsAr[i].split(","); // 此货品的规格
								String[] specvids = specvidsAr[i].split(","); // 此货品的规格值

								//此货品的规格值list
								String colorSpecPic="";
								List<Map<String, String>> allcolor=null;
								if(map.get("allcolor")!=null){
									allcolor=(List<Map<String, String>>) map.get("allcolor");
								}else{
									allcolor=new ArrayList<Map<String,String>>();
								}
								for (String specid : specids) {
									SpecValue specvalue = new SpecValue();
									specvalue.setSpec_value_id(Integer.valueOf(specvids[j].trim()));
									specvalue.setSpec_id(Integer.valueOf(specid.trim()));
									valueList.add(specvalue);
									if(specid.equals("1000")){
										if(allcolor.size()>0){
											for (Map<String, String> map2 : allcolor) {
												Set<String> keySet = map2.keySet();
												  Iterator<String> it = keySet.iterator();
												  while (it.hasNext()) {
													 String key = it.next();
												     SpecValue specValue2=this.goodsManager.getSpecValuePictureUrl(key.trim());
												     if((specValue2.getSpec_value_id()+"").trim().equalsIgnoreCase(specvids[j].trim())){
												    	 String value = map2.get(key);
												    	 if(value!=null && !StringUtil.isEmpty(value)){
												    		 specvalue.setSpec_image(value);
												    	 }
												     }
												  }
											  }
										}
									}
									j++;	
								}
								// 生成货品对象
								Product product = new Product();
								product.setGoods_id(goods_id);
								product.setSpecList(valueList);// 设置此货品的规格list
								product.setName((String) goods.get("name"));
								product.setSn(sn);
								product.setProduct_id(productId); // 2010-1-12新增写入货品id，如果是新货品，则会是null
								product.setStore(0);               // 修改添加是product表里库存为空，添加时默认为0，   如果有问题，请高手优先处理此处  whj  2015-05-21

								String[] specvalues ={};
								product.setSpecs(StringUtil.arrayToString(specvalues, "、"));
								// 价格
								if (null == prices[i] || "".equals(prices[i]))
									product.setPrice(0D);
								else
									product.setPrice(Double.valueOf(prices[i]));
								// 卢布价格
								if (null == prices_ru[i] || "".equals(prices_ru[i]))
									product.setPrice_ru(0D);
								else
									product.setPrice_ru(Double.valueOf(prices_ru[i]));
								// 美元价格
								if (null == prices_en[i] || "".equals(prices_en[i]))
									product.setPrice_en(0D);
								else
									product.setPrice_en(Double.valueOf(prices_en[i]));
								// 批发价
								if (null == whprices[i] || "".equals(whprices[i]))
									product.setWhprice(0D);
								else
									product.setWhprice(Double.valueOf(whprices[i]));

								if (null == whprices_en[i] || "".equals(whprices_en[i]))
									product.setWhprice_en(0D);
								else
									product.setWhprice_en(Double.valueOf(whprices_en[i]));

								if (null == whprices_ru[i] || "".equals(whprices_ru[i]))
									product.setWhprice_ru(0D);
								else
									product.setWhprice_ru(Double.valueOf(whprices_ru[i]));

//								if (!"yes".equals(httpRequest.getParameter("isedit"))) { // 添加时默认为0，修改时不处理
//									product.setStore(0);
//								}
								if(null == wholesale_volumes[i] || "".equals(wholesale_volumes[i])){
									product.setWholesale_volume(0);
								}else{
									product.setWholesale_volume(Integer.valueOf(wholesale_volumes[i]));
								}
								// 成本价
								product.setCost(0D);
//								暂不启用成本价，默认设置为 0
				/*				if (null == costs[i] || "".equals(costs[i]))
									product.setCost(0D);
								else
									product.setCost(Double.valueOf(costs[i]));*/

								// 重量
								if (null == weights[i] || "".equals(weights[i]))
									product.setWeight(0D);
								else
									product.setWeight(Double.valueOf(weights[i]));

								productList.add(product);
								i++;
							}
							this.productManager.add(productList);
						}
					}else if(Integer.parseInt(catagory)==2){
						Integer[] specs=(Integer[]) map.get("specs");
						Integer[] spec_values=(Integer[]) map.get("spec_values");
						if(specs.length>0 && spec_values.length>0){
							Integer lenght=specs.length*spec_values.length;
							String[] specidsAr =new String[lenght];
							for (int i = 0; i < specidsAr.length; i++) {
								specidsAr[i]=specs[0]+"";
							}
							String[] specvidsAr=new String[spec_values.length];
							for (int i = 0; i < specvidsAr.length; i++) {
								specvidsAr[i]=spec_values[i]+"";
							}
							String[] productids=new String[lenght];
							for (int i = 0; i < productids.length; i++) {
								productids[i]="";
							}
							String[] sns=new String[lenght];
							for (int i = 0; i < sns.length; i++) {
								sns[i]="";
							}
							String[] prices=new String[lenght];
							if(goods.get("price")!=null){
								for (int i = 0; i < prices.length; i++) {
									prices[i]=goods.get("price").toString();
								}
							}else{
								for (int i = 0; i < prices.length; i++) {
									prices[i]="";
								}
							}
							String[] prices_ru=new String[lenght];
							for (int i = 0; i < prices_ru.length; i++) {
								prices_ru[i]="";
							}
							String[] prices_en=new String[lenght];
							for (int i = 0; i < prices_en.length; i++) {
								prices_en[i]="";
							}
							String[] whprices=new String[lenght];
							for (int i = 0; i < whprices.length; i++) {
								whprices[i]="";
							}
							String[] whprices_ru=new String[lenght];
							for (int i = 0; i < whprices_ru.length; i++) {
								whprices_ru[i]="";
							}
							String[] whprices_en=new String[lenght];
							for (int i = 0; i < whprices_en.length; i++) {
								whprices_en[i]="";
							}
							String[] wholesale_volumes=new String[lenght];
							for (int i = 0; i < wholesale_volumes.length; i++) {
								wholesale_volumes[i]="";
							}
							String[] weights=new String[lenght];
							if(goods.get("weight")!=null){
								for (int i = 0; i < weights.length; i++) {
									weights[i]=goods.get("weight").toString();
								}
							}else{
								for (int i = 0; i < weights.length; i++) {
									weights[i]="";
								}
							}
							List<Product> productList = new ArrayList<Product>();
							int i = 0;
							int snIndex = this.getSnsSize(sns);
							for (String sn : sns) {
								Integer productId = StringUtil.isEmpty(productids[i]) ? null : Integer.valueOf(productids[i]);
								if (sn == null || sn.equals("")) {
									sn = goods.get("sn") + "-" + (snIndex + 1);
									snIndex++;
								}
							
								/*
								 * 组合商品、货品、规格值、规格对应关系
								 */
								List<SpecValue> valueList = new ArrayList<SpecValue>();
								int j = 0;
								String[] specids = specidsAr[i].split(","); // 此货品的规格
								String[] specvids = specvidsAr[i].split(","); // 此货品的规格值

								//此货品的规格值list
								String colorSpecPic="";
								List<Map<String, String>> allcolor=null;
								if(map.get("allcolor")!=null){
									allcolor=(List<Map<String, String>>) map.get("allcolor");
								}else{
									allcolor=new ArrayList<Map<String,String>>();
								}
								for (String specid : specids) {
									SpecValue specvalue = new SpecValue();
									specvalue.setSpec_value_id(Integer.valueOf(specvids[j].trim()));
									specvalue.setSpec_id(Integer.valueOf(specid.trim()));
									valueList.add(specvalue);
									if(specid.equals("1000")){
										if(allcolor.size()>0){
											for (Map<String, String> map2 : allcolor) {
												Set<String> keySet = map2.keySet();
												  Iterator<String> it = keySet.iterator();
												  while (it.hasNext()) {
													 String key = it.next();
												     SpecValue specValue2=this.goodsManager.getSpecValuePictureUrl(key.trim());
												     if((specValue2.getSpec_value_id()+"").trim().equalsIgnoreCase(specvids[j].trim())){
												    	 String value = map2.get(key);
												    	 if(value!=null && !StringUtil.isEmpty(value)){
												    		 specvalue.setSpec_image(value);
												    	 }
												     }
												  }
											  }
										}
									}
									j++;	
								}
								// 生成货品对象
								Product product = new Product();
								product.setGoods_id(goods_id);
								product.setSpecList(valueList);// 设置此货品的规格list
								product.setName((String) goods.get("name"));
								product.setSn(sn);
								product.setProduct_id(productId); // 2010-1-12新增写入货品id，如果是新货品，则会是null
								product.setStore(0);               // 修改添加是product表里库存为空，添加时默认为0，   如果有问题，请高手优先处理此处  whj  2015-05-21

								String[] specvalues ={};
								product.setSpecs(StringUtil.arrayToString(specvalues, "、"));
								// 价格
								if (null == prices[i] || "".equals(prices[i]))
									product.setPrice(0D);
								else
									product.setPrice(Double.valueOf(prices[i]));
								// 卢布价格
								if (null == prices_ru[i] || "".equals(prices_ru[i]))
									product.setPrice_ru(0D);
								else
									product.setPrice_ru(Double.valueOf(prices_ru[i]));
								// 美元价格
								if (null == prices_en[i] || "".equals(prices_en[i]))
									product.setPrice_en(0D);
								else
									product.setPrice_en(Double.valueOf(prices_en[i]));
								// 批发价
								if (null == whprices[i] || "".equals(whprices[i]))
									product.setWhprice(0D);
								else
									product.setWhprice(Double.valueOf(whprices[i]));

								if (null == whprices_en[i] || "".equals(whprices_en[i]))
									product.setWhprice_en(0D);
								else
									product.setWhprice_en(Double.valueOf(whprices_en[i]));

								if (null == whprices_ru[i] || "".equals(whprices_ru[i]))
									product.setWhprice_ru(0D);
								else
									product.setWhprice_ru(Double.valueOf(whprices_ru[i]));

//								if (!"yes".equals(httpRequest.getParameter("isedit"))) { // 添加时默认为0，修改时不处理
//									product.setStore(0);
//								}
								if(null == wholesale_volumes[i] || "".equals(wholesale_volumes[i])){
									product.setWholesale_volume(0);
								}else{
									product.setWholesale_volume(Integer.valueOf(wholesale_volumes[i]));
								}
								// 成本价
								product.setCost(0D);
//								暂不启用成本价，默认设置为 0
				/*				if (null == costs[i] || "".equals(costs[i]))
									product.setCost(0D);
								else
									product.setCost(Double.valueOf(costs[i]));*/

								// 重量
								if (null == weights[i] || "".equals(weights[i]))
									product.setWeight(0D);
								else
									product.setWeight(Double.valueOf(weights[i]));

								productList.add(product);
								i++;
							}
							this.productManager.add(productList);
						}
					}else if(Integer.parseInt(catagory)==3){
						Integer[] specs=(Integer[]) map.get("specs");
						Integer[] spec_values=(Integer[]) map.get("spec_values");
						if(specs.length>0 && spec_values.length>0){
							Integer lenght=specs.length*spec_values.length;
							String[] specidsAr =new String[lenght];
							for (int i = 0; i < specidsAr.length; i++) {
								specidsAr[i]=specs[0]+"";
							}
							String[] specvidsAr=new String[spec_values.length];
							for (int i = 0; i < specvidsAr.length; i++) {
								specvidsAr[i]=spec_values[i]+"";
							}
							String[] productids=new String[lenght];
							for (int i = 0; i < productids.length; i++) {
								productids[i]="";
							}
							String[] sns=new String[lenght];
							for (int i = 0; i < sns.length; i++) {
								sns[i]="";
							}
							String[] prices=new String[lenght];
							if(goods.get("price")!=null){
								for (int i = 0; i < prices.length; i++) {
									prices[i]=goods.get("price").toString();
								}
							}else{
								for (int i = 0; i < prices.length; i++) {
									prices[i]="";
								}
							}
							String[] prices_ru=new String[lenght];
							for (int i = 0; i < prices_ru.length; i++) {
								prices_ru[i]="";
							}
							String[] prices_en=new String[lenght];
							for (int i = 0; i < prices_en.length; i++) {
								prices_en[i]="";
							}
							String[] whprices=new String[lenght];
							for (int i = 0; i < whprices.length; i++) {
								whprices[i]="";
							}
							String[] whprices_ru=new String[lenght];
							for (int i = 0; i < whprices_ru.length; i++) {
								whprices_ru[i]="";
							}
							String[] whprices_en=new String[lenght];
							for (int i = 0; i < whprices_en.length; i++) {
								whprices_en[i]="";
							}
							String[] wholesale_volumes=new String[lenght];
							for (int i = 0; i < wholesale_volumes.length; i++) {
								wholesale_volumes[i]="";
							}
							String[] weights=new String[lenght];
							if(goods.get("weight")!=null){
								for (int i = 0; i < weights.length; i++) {
									weights[i]=goods.get("weight").toString();
								}
							}else{
								for (int i = 0; i < weights.length; i++) {
									weights[i]="";
								}
							}
							List<Product> productList = new ArrayList<Product>();
							int i = 0;
							int snIndex = this.getSnsSize(sns);
							for (String sn : sns) {
								Integer productId = StringUtil.isEmpty(productids[i]) ? null : Integer.valueOf(productids[i]);
								if (sn == null || sn.equals("")) {
									sn = goods.get("sn") + "-" + (snIndex + 1);
									snIndex++;
								}
							
								/*
								 * 组合商品、货品、规格值、规格对应关系
								 */
								List<SpecValue> valueList = new ArrayList<SpecValue>();
								int j = 0;
								String[] specids = specidsAr[i].split(","); // 此货品的规格
								String[] specvids = specvidsAr[i].split(","); // 此货品的规格值

								//此货品的规格值list
								String colorSpecPic="";
								List<Map<String, String>> allcolor=null;
								if(map.get("allcolor")!=null){
									allcolor=(List<Map<String, String>>) map.get("allcolor");
								}else{
									allcolor=new ArrayList<Map<String,String>>();
								}
								for (String specid : specids) {
									SpecValue specvalue = new SpecValue();
									specvalue.setSpec_value_id(Integer.valueOf(specvids[j].trim()));
									specvalue.setSpec_id(Integer.valueOf(specid.trim()));
									valueList.add(specvalue);
									if(specid.equals("1000")){
										if(allcolor.size()>0){
											for (Map<String, String> map2 : allcolor) {
												Set<String> keySet = map2.keySet();
												  Iterator<String> it = keySet.iterator();
												  while (it.hasNext()) {
													 String key = it.next();
												     SpecValue specValue2=this.goodsManager.getSpecValuePictureUrl(key.trim());
												     if((specValue2.getSpec_value_id()+"").trim().equalsIgnoreCase(specvids[j].trim())){
												    	 String value = map2.get(key);
												    	 if(value!=null && !StringUtil.isEmpty(value)){
												    		 specvalue.setSpec_image(value);
												    	 }
												     }
												  }
											  }
										}
									}
									j++;	
								}
								// 生成货品对象
								Product product = new Product();
								product.setGoods_id(goods_id);
								product.setSpecList(valueList);// 设置此货品的规格list
								product.setName((String) goods.get("name"));
								product.setSn(sn);
								product.setProduct_id(productId); // 2010-1-12新增写入货品id，如果是新货品，则会是null
								product.setStore(0);               // 修改添加是product表里库存为空，添加时默认为0，   如果有问题，请高手优先处理此处  whj  2015-05-21

								String[] specvalues ={};
								product.setSpecs(StringUtil.arrayToString(specvalues, "、"));
								// 价格
								if (null == prices[i] || "".equals(prices[i]))
									product.setPrice(0D);
								else
									product.setPrice(Double.valueOf(prices[i]));
								// 卢布价格
								if (null == prices_ru[i] || "".equals(prices_ru[i]))
									product.setPrice_ru(0D);
								else
									product.setPrice_ru(Double.valueOf(prices_ru[i]));
								// 美元价格
								if (null == prices_en[i] || "".equals(prices_en[i]))
									product.setPrice_en(0D);
								else
									product.setPrice_en(Double.valueOf(prices_en[i]));
								// 批发价
								if (null == whprices[i] || "".equals(whprices[i]))
									product.setWhprice(0D);
								else
									product.setWhprice(Double.valueOf(whprices[i]));

								if (null == whprices_en[i] || "".equals(whprices_en[i]))
									product.setWhprice_en(0D);
								else
									product.setWhprice_en(Double.valueOf(whprices_en[i]));

								if (null == whprices_ru[i] || "".equals(whprices_ru[i]))
									product.setWhprice_ru(0D);
								else
									product.setWhprice_ru(Double.valueOf(whprices_ru[i]));

//								if (!"yes".equals(httpRequest.getParameter("isedit"))) { // 添加时默认为0，修改时不处理
//									product.setStore(0);
//								}
								if(null == wholesale_volumes[i] || "".equals(wholesale_volumes[i])){
									product.setWholesale_volume(0);
								}else{
									product.setWholesale_volume(Integer.valueOf(wholesale_volumes[i]));
								}
								// 成本价
								product.setCost(0D);
//								暂不启用成本价，默认设置为 0
				/*				if (null == costs[i] || "".equals(costs[i]))
									product.setCost(0D);
								else
									product.setCost(Double.valueOf(costs[i]));*/

								// 重量
								if (null == weights[i] || "".equals(weights[i]))
									product.setWeight(0D);
								else
									product.setWeight(Double.valueOf(weights[i]));

								productList.add(product);
								i++;
							}
							this.productManager.add(productList);
						}
					}else if(Integer.parseInt(catagory)==4){
						Integer[] specs=(Integer[]) map.get("specs");
						Integer[] spec_values=(Integer[]) map.get("spec_values");
						Integer[] spec_values_size=(Integer[]) map.get("spec_values_size");
						if(specs.length>0 && spec_values.length>0 && spec_values_size.length>0){
							Integer lenght=0;
							for (int j = 0; j < spec_values.length; j++) {
								lenght+=spec_values_size.length;
							} 
							String[] specidsAr =new String[lenght];
							for (int j = 0; j < specidsAr.length; j++) {
								specidsAr[j]=specs[0]+","+specs[1];
							}
							String[] specvidsAr=new String[lenght];
							List<String> list=new ArrayList<String>();
							for (int j = 0; j < spec_values.length; j++) {
								for (int i = 0; i < spec_values_size.length; i++) {
									list.add(spec_values[j]+","+spec_values_size[i]);
								}
							}
							
							for (int j = 0; j < specvidsAr.length; j++) {
									specvidsAr[j]=list.get(j);
							}
							String[] productids=new String[lenght];
							for (int i = 0; i < productids.length; i++) {
								productids[i]="";
							}
							String[] sns=new String[lenght];
							for (int i = 0; i < sns.length; i++) {
								sns[i]="";
							}
							String[] prices=new String[lenght];
							if(goods.get("price")!=null){
								for (int i = 0; i < prices.length; i++) {
									prices[i]=goods.get("price").toString();
								}
							}else{
								for (int i = 0; i < prices.length; i++) {
									prices[i]="";
								}
							}
							String[] prices_ru=new String[lenght];
							for (int i = 0; i < prices_ru.length; i++) {
								prices_ru[i]="";
							}
							String[] prices_en=new String[lenght];
							for (int i = 0; i < prices_en.length; i++) {
								prices_en[i]="";
							}
							String[] whprices=new String[lenght];
							for (int i = 0; i < whprices.length; i++) {
								whprices[i]="";
							}
							String[] whprices_ru=new String[lenght];
							for (int i = 0; i < whprices_ru.length; i++) {
								whprices_ru[i]="";
							}
							String[] whprices_en=new String[lenght];
							for (int i = 0; i < whprices_en.length; i++) {
								whprices_en[i]="";
							}
							String[] wholesale_volumes=new String[lenght];
							for (int i = 0; i < wholesale_volumes.length; i++) {
								wholesale_volumes[i]="";
							}
							String[] weights=new String[lenght];
							if(goods.get("weight")!=null){
								for (int i = 0; i < weights.length; i++) {
									weights[i]=goods.get("weight").toString();
								}
							}else{
								for (int i = 0; i < weights.length; i++) {
									weights[i]="";
								}
							}
							List<Product> productList = new ArrayList<Product>();
							int i = 0;
							int snIndex = this.getSnsSize(sns);
							for (String sn : sns) {
								Integer productId = StringUtil.isEmpty(productids[i]) ? null : Integer.valueOf(productids[i]);
								if (sn == null || sn.equals("")) {
									sn = goods.get("sn") + "-" + (snIndex + 1);
									snIndex++;
								}
							
								/*
								 * 组合商品、货品、规格值、规格对应关系
								 */
								List<SpecValue> valueList = new ArrayList<SpecValue>();
								int j = 0;
								String[] specids = specidsAr[i].split(","); // 此货品的规格
								String[] specvids = specvidsAr[i].split(","); // 此货品的规格值

								//此货品的规格值list
								String colorSpecPic="";
								List<Map<String, String>> allcolor=null;
								if(map.get("allcolor")!=null){
									allcolor=(List<Map<String, String>>) map.get("allcolor");
								}else{
									allcolor=new ArrayList<Map<String,String>>();
								}
								for (String specid : specids) {
									SpecValue specvalue = new SpecValue();
									specvalue.setSpec_value_id(Integer.valueOf(specvids[j].trim()));
									specvalue.setSpec_id(Integer.valueOf(specid.trim()));
									valueList.add(specvalue);
									if(specid.equals("1000")){
										if(allcolor.size()>0){
											for (Map<String, String> map2 : allcolor) {
												Set<String> keySet = map2.keySet();
												  Iterator<String> it = keySet.iterator();
												  while (it.hasNext()) {
													 String key = it.next();
												     SpecValue specValue2=this.goodsManager.getSpecValuePictureUrl(key.trim());
												     if((specValue2.getSpec_value_id()+"").trim().equalsIgnoreCase(specvids[j].trim())){
												    	 String value = map2.get(key);
												    	 if(value!=null && !StringUtil.isEmpty(value)){
												    		 specvalue.setSpec_image(value);
												    	 }
												     }
												  }
											  }
										}
									}
									j++;	
								}
								// 生成货品对象
								Product product = new Product();
								product.setGoods_id(goods_id);
								product.setSpecList(valueList);// 设置此货品的规格list
								product.setName((String) goods.get("name"));
								product.setSn(sn);
								product.setProduct_id(productId); // 2010-1-12新增写入货品id，如果是新货品，则会是null
								product.setStore(0);               // 修改添加是product表里库存为空，添加时默认为0，   如果有问题，请高手优先处理此处  whj  2015-05-21

								String[] specvalues ={};
								product.setSpecs(StringUtil.arrayToString(specvalues, "、"));
								// 价格
								if (null == prices[i] || "".equals(prices[i]))
									product.setPrice(0D);
								else
									product.setPrice(Double.valueOf(prices[i]));
								// 卢布价格
								if (null == prices_ru[i] || "".equals(prices_ru[i]))
									product.setPrice_ru(0D);
								else
									product.setPrice_ru(Double.valueOf(prices_ru[i]));
								// 美元价格
								if (null == prices_en[i] || "".equals(prices_en[i]))
									product.setPrice_en(0D);
								else
									product.setPrice_en(Double.valueOf(prices_en[i]));
								// 批发价
								if (null == whprices[i] || "".equals(whprices[i]))
									product.setWhprice(0D);
								else
									product.setWhprice(Double.valueOf(whprices[i]));

								if (null == whprices_en[i] || "".equals(whprices_en[i]))
									product.setWhprice_en(0D);
								else
									product.setWhprice_en(Double.valueOf(whprices_en[i]));

								if (null == whprices_ru[i] || "".equals(whprices_ru[i]))
									product.setWhprice_ru(0D);
								else
									product.setWhprice_ru(Double.valueOf(whprices_ru[i]));

//								if (!"yes".equals(httpRequest.getParameter("isedit"))) { // 添加时默认为0，修改时不处理
//									product.setStore(0);
//								}
								if(null == wholesale_volumes[i] || "".equals(wholesale_volumes[i])){
									product.setWholesale_volume(0);
								}else{
									product.setWholesale_volume(Integer.valueOf(wholesale_volumes[i]));
								}
								// 成本价
								product.setCost(0D);
//								暂不启用成本价，默认设置为 0
				/*				if (null == costs[i] || "".equals(costs[i]))
									product.setCost(0D);
								else
									product.setCost(Double.valueOf(costs[i]));*/

								// 重量
								if (null == weights[i] || "".equals(weights[i]))
									product.setWeight(0D);
								else
									product.setWeight(Double.valueOf(weights[i]));

								productList.add(product);
								i++;
							}
							this.productManager.add(productList);
						}
					}else if(Integer.parseInt(catagory)==5){
						Integer[] specs=(Integer[]) map.get("specs");
						Integer[] spec_values=(Integer[]) map.get("spec_values");
						Integer[] spec_values_size=(Integer[]) map.get("spec_values_size");
						if(specs.length>0 && spec_values.length>0 && spec_values_size.length>0){
							Integer lenght=0;
							for (int j = 0; j < spec_values.length; j++) {
								lenght+=spec_values_size.length;
							} 
							String[] specidsAr =new String[lenght];
							for (int j = 0; j < specidsAr.length; j++) {
								specidsAr[j]=specs[0]+","+specs[1];
							}
							String[] specvidsAr=new String[lenght];
							List<String> list=new ArrayList<String>();
							for (int j = 0; j < spec_values.length; j++) {
								for (int i = 0; i < spec_values_size.length; i++) {
									list.add(spec_values[j]+","+spec_values_size[i]);
								}
							}
							
							for (int j = 0; j < specvidsAr.length; j++) {
									specvidsAr[j]=list.get(j);
							}
							String[] productids=new String[lenght];
							for (int i = 0; i < productids.length; i++) {
								productids[i]="";
							}
							String[] sns=new String[lenght];
							for (int i = 0; i < sns.length; i++) {
								sns[i]="";
							}
							String[] prices=new String[lenght];
							if(goods.get("price")!=null){
								for (int i = 0; i < prices.length; i++) {
									prices[i]=goods.get("price").toString();
								}
							}else{
								for (int i = 0; i < prices.length; i++) {
									prices[i]="";
								}
							}
							String[] prices_ru=new String[lenght];
							for (int i = 0; i < prices_ru.length; i++) {
								prices_ru[i]="";
							}
							String[] prices_en=new String[lenght];
							for (int i = 0; i < prices_en.length; i++) {
								prices_en[i]="";
							}
							String[] whprices=new String[lenght];
							for (int i = 0; i < whprices.length; i++) {
								whprices[i]="";
							}
							String[] whprices_ru=new String[lenght];
							for (int i = 0; i < whprices_ru.length; i++) {
								whprices_ru[i]="";
							}
							String[] whprices_en=new String[lenght];
							for (int i = 0; i < whprices_en.length; i++) {
								whprices_en[i]="";
							}
							String[] wholesale_volumes=new String[lenght];
							for (int i = 0; i < wholesale_volumes.length; i++) {
								wholesale_volumes[i]="";
							}
							String[] weights=new String[lenght];
							if(goods.get("weight")!=null){
								for (int i = 0; i < weights.length; i++) {
									weights[i]=goods.get("weight").toString();
								}
							}else{
								for (int i = 0; i < weights.length; i++) {
									weights[i]="";
								}
							}
							List<Product> productList = new ArrayList<Product>();
							int i = 0;
							int snIndex = this.getSnsSize(sns);
							for (String sn : sns) {
								Integer productId = StringUtil.isEmpty(productids[i]) ? null : Integer.valueOf(productids[i]);
								if (sn == null || sn.equals("")) {
									sn = goods.get("sn") + "-" + (snIndex + 1);
									snIndex++;
								}
							
								/*
								 * 组合商品、货品、规格值、规格对应关系
								 */
								List<SpecValue> valueList = new ArrayList<SpecValue>();
								int j = 0;
								String[] specids = specidsAr[i].split(","); // 此货品的规格
								String[] specvids = specvidsAr[i].split(","); // 此货品的规格值

								//此货品的规格值list
								String colorSpecPic="";
								List<Map<String, String>> allcolor=null;
								if(map.get("allcolor")!=null){
									allcolor=(List<Map<String, String>>) map.get("allcolor");
								}else{
									allcolor=new ArrayList<Map<String,String>>();
								}
								for (String specid : specids) {
									SpecValue specvalue = new SpecValue();
									specvalue.setSpec_value_id(Integer.valueOf(specvids[j].trim()));
									specvalue.setSpec_id(Integer.valueOf(specid.trim()));
									valueList.add(specvalue);
									if(specid.equals("1000")){
										if(allcolor.size()>0){
											for (Map<String, String> map2 : allcolor) {
												Set<String> keySet = map2.keySet();
												  Iterator<String> it = keySet.iterator();
												  while (it.hasNext()) {
													 String key = it.next();
												     SpecValue specValue2=this.goodsManager.getSpecValuePictureUrl(key.trim());
												     if((specValue2.getSpec_value_id()+"").trim().equalsIgnoreCase(specvids[j].trim())){
												    	 String value = map2.get(key);
												    	 if(value!=null && !StringUtil.isEmpty(value)){
												    		 specvalue.setSpec_image(value);
												    	 }
												     }
												  }
											  }
										}
									}
									j++;	
								}
								// 生成货品对象
								Product product = new Product();
								product.setGoods_id(goods_id);
								product.setSpecList(valueList);// 设置此货品的规格list
								product.setName((String) goods.get("name"));
								product.setSn(sn);
								product.setProduct_id(productId); // 2010-1-12新增写入货品id，如果是新货品，则会是null
								product.setStore(0);               // 修改添加是product表里库存为空，添加时默认为0，   如果有问题，请高手优先处理此处  whj  2015-05-21

								String[] specvalues ={};
								product.setSpecs(StringUtil.arrayToString(specvalues, "、"));
								// 价格
								if (null == prices[i] || "".equals(prices[i]))
									product.setPrice(0D);
								else
									product.setPrice(Double.valueOf(prices[i]));
								// 卢布价格
								if (null == prices_ru[i] || "".equals(prices_ru[i]))
									product.setPrice_ru(0D);
								else
									product.setPrice_ru(Double.valueOf(prices_ru[i]));
								// 美元价格
								if (null == prices_en[i] || "".equals(prices_en[i]))
									product.setPrice_en(0D);
								else
									product.setPrice_en(Double.valueOf(prices_en[i]));
								// 批发价
								if (null == whprices[i] || "".equals(whprices[i]))
									product.setWhprice(0D);
								else
									product.setWhprice(Double.valueOf(whprices[i]));

								if (null == whprices_en[i] || "".equals(whprices_en[i]))
									product.setWhprice_en(0D);
								else
									product.setWhprice_en(Double.valueOf(whprices_en[i]));

								if (null == whprices_ru[i] || "".equals(whprices_ru[i]))
									product.setWhprice_ru(0D);
								else
									product.setWhprice_ru(Double.valueOf(whprices_ru[i]));

//								if (!"yes".equals(httpRequest.getParameter("isedit"))) { // 添加时默认为0，修改时不处理
//									product.setStore(0);
//								}
								if(null == wholesale_volumes[i] || "".equals(wholesale_volumes[i])){
									product.setWholesale_volume(0);
								}else{
									product.setWholesale_volume(Integer.valueOf(wholesale_volumes[i]));
								}
								// 成本价
								product.setCost(0D);
//								暂不启用成本价，默认设置为 0
				/*				if (null == costs[i] || "".equals(costs[i]))
									product.setCost(0D);
								else
									product.setCost(Double.valueOf(costs[i]));*/

								// 重量
								if (null == weights[i] || "".equals(weights[i]))
									product.setWeight(0D);
								else
									product.setWeight(Double.valueOf(weights[i]));

								productList.add(product);
								i++;
							}
							this.productManager.add(productList);
						}
					}
				}	
			}else{
				addProduct(goods, goods_id);
			}
		}
	}
	/**
	 * 获取已有的货号数量
	 * @param sns
	 * @return
	 */
	public int getSnsSize(String[] sns) {
		int i = 0;
		for (String sn : sns) {
			if (!StringUtil.isEmpty(sn)) {
				i++;
			}
		}
		return i;
	}
	private String getThumbPath(String filePath, String shortName) {
		return UploadUtil.getThumbPath(filePath, shortName);
	}
	public String getCatid() {
		return catid;
	}


	public void setCatid(String catid) {
		this.catid = catid;
	}
	public IJmsProcessor getGoodsGalleryProcessor() {
		return goodsGalleryProcessor;
	}
	public void setGoodsGalleryProcessor(IJmsProcessor goodsGalleryProcessor) {
		this.goodsGalleryProcessor = goodsGalleryProcessor;
	}

	public String getOtherWeb() {
		return otherWeb;
	}
	public IStoreManager getStoreManager() {
		return storeManager;
	}

	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}
	public void setOtherWeb(String otherWeb) {
		this.otherWeb = otherWeb;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}


	public IStoreGoodsManager getStoreGoodsManager() {
		return storeGoodsManager;
	}


	public void setStoreGoodsManager(IStoreGoodsManager storeGoodsManager) {
		this.storeGoodsManager = storeGoodsManager;
	}
	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}
	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}


	public StoreGoods getStoreGoods() {
		return storeGoods;
	}


	public void setStoreGoods(StoreGoods storeGoods) {
		this.storeGoods = storeGoods;
	}
	public IDepotManager getDepotManager() {
		return depotManager;
	}
	public void setDepotManager(IDepotManager depotManager) {
		this.depotManager = depotManager;
	}
	public IGoodsGalleryManager getGoodsGalleryManager() {
		return goodsGalleryManager;
	}
	public void setGoodsGalleryManager(IGoodsGalleryManager goodsGalleryManager) {
		this.goodsGalleryManager = goodsGalleryManager;
	}
	public IProductManager getProductManager() {
		return productManager;
	}
	public void setProductManager(IProductManager productManager) {
		this.productManager = productManager;
	}
}

