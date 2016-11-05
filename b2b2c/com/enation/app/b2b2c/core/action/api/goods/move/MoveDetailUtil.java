package com.enation.app.b2b2c.core.action.api.goods.move;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.json.simple.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.enation.app.shop.component.search.plugin.Price;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.eop.SystemSetting;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.StringUtil;
public class MoveDetailUtil {
	private static Map<String, Object> map;
	private static String fileName;
	private static String subFolder;
	private static String subFolderColor;

	public static Map<String, Object> moveMainMethod(String url,
			Integer store_id,List<String> list,Integer[] spec_ids,IGoodsManager goodsManager) {
		subFolder = "/store/" + store_id + "/goods";
		subFolderColor="store";
		String newurl = getUrlCode(url);
		Document doc = null;
		try {
			doc = Jsoup.connect(newurl).get();
			map = new HashMap<String, Object>();
		String keyword= doc.select("div#ltlinknav a").get(2).text();
			if(keyword!=null && !StringUtil.isEmpty(keyword)){
				map.put("keyword", keyword);
				System.out.println(keyword);
			}
			String name = doc.select("div.saleouter > h1").text();
			if (name != null && !StringUtil.isEmpty(name)) {
				map.put("name", name);
				System.out.println(name);
			}
			String orignalprice = doc.select("del[class=black]").text()
					.split("元")[0];
			if (orignalprice != null && !StringUtil.isEmpty(orignalprice)) {
				map.put("orignalprice", orignalprice);
				System.out.println(orignalprice);
			}
			String discountprice = doc.select("i[class=ltprice big]").text()
					.split("元")[0];
			if (discountprice != null && !StringUtil.isEmpty(discountprice)) {
				map.put("discountprice", discountprice);
				System.out.println(discountprice);
			}
			Elements smallimgs = doc.select("div#simgouter >img ");
			List<String> list1=new ArrayList<String>();
			if(smallimgs.size()>0){
				for (Element element : smallimgs) {
					byte[] btImg = getImageFromNetByUrl(element.attr("focussrc"));
					System.out.println(element.attr("focussrc"));
					String smallimgsurl=folderName(btImg, subFolder);
					if(smallimgsurl != null && !StringUtil.isEmpty(smallimgsurl)){
							list1.add(smallimgsurl);
					}
				}
				map.put("bigimgs", list1.get(0));
				map.put("smallimgsList",list1);
				System.out.println(net.sf.json.JSONArray.fromObject(list1));
			}
			/*Elements detailimages = doc.select("div#shoeimages >div >p >img ");*/
			Elements detailimages = doc.select("div#shoeimages >div >img ");
			List<String> listdetail=new ArrayList<String>();
			if(detailimages.size()>0){
				for (Element element : detailimages) {
					byte[] detailImg = getImageFromNetByUrl(element.attr("lazyload"));
					System.out.println(element.attr("lazyload"));
					String detailurl=detailUpload(detailImg);
					if(detailurl != null && !StringUtil.isEmpty(detailurl)){
						listdetail.add(detailurl);
					}
				}
				map.put("listdetail", listdetail);
				System.out.println(net.sf.json.JSONArray.fromObject(listdetail));
			}
			String detailcontent=doc.select("table#shoe_property  ").html();
			if(detailcontent != null && !StringUtil.isEmpty(detailcontent)){
				map.put("detailcontent", detailcontent);
				System.out.println(detailcontent);
			}
			Integer resultcolor=0;
			Integer resultsize1=0;
			Integer resultsize2=0;
			if(list.size()>0){
				String color=doc.select("dl.ltOtherColorV2 dt strong").text();
				if(color!=null && !StringUtil.isEmpty(color)){
					if(color.equalsIgnoreCase("选择颜色：")){
						color="颜色";
						for (String string : list) {
							if(string.equalsIgnoreCase(color)){
								resultcolor=1;
							}
						}
					}
				}else{
			       color=doc.select("div.saleouter p strong").text();
			       String colorss=doc.select("div.saleouter p  i.pink").text();
			       if(colorss!=null && !StringUtil.isEmpty(colorss)){
			    	   if(colorss.contains("/")){
			    		   colorss="黑色";
			    	   }
			    	   Integer spec_value_id=goodsManager.getSpecValue(colorss.trim());
			    	   if(spec_value_id!=null){
			    		   if(color!=null && !StringUtil.isEmpty(color)){
								if(color.contains("选择颜色：")){
									color="颜色";
									for (String string : list) {
										if(string.equalsIgnoreCase(color)){
											resultcolor=1;
										}
									}
								}
							}
			    	   }
			       }
				}
				String[] sizes=new String[2];
				String size=doc.select("dl#shoe_size_list dt strong").text();
				if(size!=null && !StringUtil.isEmpty(size)){
					if(size.equalsIgnoreCase("有货尺码：")){
						sizes[0]="男女鞋尺寸";
						sizes[1]="衣服尺寸";
						for (String string : list) {
							if(string.equals(sizes[0])){
								resultsize1=1;
							}else if(string.equals(sizes[1])){
								resultsize2=1;
							}
						}
					}
				}
				if(resultcolor!=0 && resultsize1==0 && resultsize2==0){
					Integer catagory=1;
					Integer[] specs=new Integer[1];
					specs[0]=spec_ids[0];
					map.put("specs", specs);
					Elements colors=doc.select("dl.ltOtherColorV2 > dd >a >img");
					List<Map<String, String>> allcolor=new ArrayList<Map<String,String>>();
					Integer numbercolor=0;
					if(colors.size()>0){
						for (Element element : colors) {
							String colorvalue=element.attr("alt");
							String colorimage=element.attr("src");
							if(colorvalue!=null && !StringUtil.isEmpty(colorvalue) && StringUtil.isEmpty(colorimage)){
								String n1=colorvalue.substring(colorvalue.indexOf("颜色组成：")+5);
							  Integer spec_value_id=goodsManager.getSpecValue(n1.trim());
							  if(spec_value_id!=null){
								  numbercolor++;
							    }
							}else{
								String n1=colorvalue.substring(colorvalue.indexOf("颜色组成：")+5);
								  Integer spec_value_id=goodsManager.getSpecValue(n1.trim());
								  if(spec_value_id!=null){
									  numbercolor++;
							    }
							}
						}
					}
					if(colors.size()>0){
						Integer[] spec_values=new Integer[numbercolor];
						int count=0;
						for (Element element : colors) {
							Map<String, String> map=new HashMap<String, String>();
							String colorvalue=element.attr("alt");
							String colorimage=element.attr("src");
							if(colorvalue!=null && !StringUtil.isEmpty(colorvalue) && StringUtil.isEmpty(colorimage)){
								String n1=colorvalue.substring(colorvalue.indexOf("颜色组成：")+5);
							  Integer spec_value_id=goodsManager.getSpecValue(n1.trim());
							  if(spec_value_id!=null){
								  spec_values[count]=spec_value_id;
								  count++;
							  }
							  map.put(n1, null);
							  allcolor.add(map);
							}else{
								String n1=colorvalue.substring(colorvalue.indexOf("颜色组成：")+5);
								Integer spec_value_id=goodsManager.getSpecValue(n1.trim());
								  if(spec_value_id!=null){
									  spec_values[count]=spec_value_id;
									  count++;
								  }
								  byte[] btImg = getImageFromNetByUrl(colorimage);
								  System.out.println(colorimage);
								 String colorurl=folderName(btImg, subFolderColor);
								map.put(n1, colorurl);
								allcolor.add(map);
							}
						}
					   map.put("spec_values", spec_values);	
					}else{
						Integer[] spec_values=new Integer[1];
						int count=0;
						String colorss=doc.select("div.saleouter p  i.pink").text();
						   if(colorss.contains("/")){
				    		   colorss="黑色";
				    	   }
						if(!StringUtil.isEmpty(colorss) && colorss!=null){
							Map<String, String> map=new HashMap<String, String>();
							Integer spec_value_id=goodsManager.getSpecValue(colorss.trim());
							 if(spec_value_id!=null){
								  spec_values[count]=spec_value_id;
								  count++;
							  }
							  map.put(colorss, null);
							  allcolor.add(map);
						}
						 map.put("spec_values", spec_values);	
					}
					map.put("allcolor", allcolor);
					map.put("catagory", catagory);
				}else if(resultcolor==0 && resultsize1!=0 && resultsize2==0){
					Integer catagory=2;
					Integer[] specs=new Integer[1];
					specs[0]=spec_ids[1];
					map.put("specs", specs);
					Elements sizemax=doc.select("dl#shoe_size_list >dd");
					Integer number=0;
					if(sizemax.size()>0){
						for (Element element : sizemax) {
							String colorvalue=element.text();
							if(colorvalue!=null && !StringUtil.isEmpty(colorvalue)){
								Integer spec_value_id=goodsManager.getSpecValueSize(colorvalue.trim());
								if(spec_value_id!=null){
									number++;
								}
							}
						}
					}
					if(sizemax.size()>0){
						Integer count=0;
						Integer[] spec_values=new Integer[number];
						for (Element element : sizemax) {
							String colorvalue=element.text();
							if(colorvalue!=null && !StringUtil.isEmpty(colorvalue)){
								 Integer spec_value_id=goodsManager.getSpecValueSize(colorvalue.trim());
								 if(spec_value_id!=null){
									  spec_values[count]=spec_value_id;
									  count++;
								  }
							}
						}
						map.put("spec_values", spec_values);	
					}
					map.put("catagory", catagory);
				}else if(resultcolor==0 && resultsize1==0 && resultsize2!=0){
					Integer catagory=3;
					Integer[] specs=new Integer[1];
					specs[0]=spec_ids[2];
					map.put("specs", specs);
					Elements sizemax=doc.select("dl#shoe_size_list >dd");
					Integer number=0;
					if(sizemax.size()>0){
						for (Element element : sizemax) {
							String colorvalue=element.text();
							if(colorvalue!=null && !StringUtil.isEmpty(colorvalue)){
								Integer spec_value_id=goodsManager.getSpecValueSize(colorvalue.trim());
								if(spec_value_id!=null){
									number++;
								}
							}
						}
					}
					if(sizemax.size()>0){
						Integer count=0;
						Integer[] spec_values=new Integer[number];
						for (Element element : sizemax) {
							String colorvalue=element.text();
							if(colorvalue!=null && !StringUtil.isEmpty(colorvalue)){
								 Integer spec_value_id=goodsManager.getSpecValueSize1(colorvalue.trim());
								 if(spec_value_id!=null){
									  spec_values[count]=spec_value_id;
									  count++;
								  }
							}
						}
						map.put("spec_values", spec_values);	
					}
					map.put("catagory", catagory);
				}else if(resultcolor!=0 && resultsize1==0 && resultsize2!=0){
					Integer catagory=4;
					Integer[] specs=new Integer[2];
					specs[0]=spec_ids[0];
					specs[1]=spec_ids[2];
					map.put("specs", specs);
					Elements colors=doc.select("dl.ltOtherColorV2 > dd >a >img");
					List<Map<String, String>> allcolor=new ArrayList<Map<String,String>>();
					Integer numbercolor=0;
					if(colors.size()>0){
						for (Element element : colors) {
							String colorvalue=element.attr("alt");
							String colorimage=element.attr("src");
							if(colorvalue!=null && !StringUtil.isEmpty(colorvalue) && StringUtil.isEmpty(colorimage)){
								String n1=colorvalue.substring(colorvalue.indexOf("颜色组成：")+5);
							  Integer spec_value_id=goodsManager.getSpecValue(n1.trim());
							  if(spec_value_id!=null){
								  numbercolor++;
							    }
							}else{
								String n1=colorvalue.substring(colorvalue.indexOf("颜色组成：")+5);
								  Integer spec_value_id=goodsManager.getSpecValue(n1.trim());
								  if(spec_value_id!=null){
									  numbercolor++;
							    }
							}
						}
					}
					if(colors.size()>0){
						Integer[] spec_values=new Integer[numbercolor];
						int count=0;
						for (Element element : colors) {
							Map<String, String> map=new HashMap<String, String>();
							String colorvalue=element.attr("alt");
							String colorimage=element.attr("src");
							if(colorvalue!=null && !StringUtil.isEmpty(colorvalue) && StringUtil.isEmpty(colorimage)){
								String n1=colorvalue.substring(colorvalue.indexOf("颜色组成：")+5);
							  Integer spec_value_id=goodsManager.getSpecValue(n1.trim());
							  if(spec_value_id!=null){
								  spec_values[count]=spec_value_id;
								  count++;
							  }
							  map.put(n1, null);
							  allcolor.add(map);
							}else{
								String n1=colorvalue.substring(colorvalue.indexOf("颜色组成：")+5);
								Integer spec_value_id=goodsManager.getSpecValue(n1.trim());
								  if(spec_value_id!=null){
									  spec_values[count]=spec_value_id;
									  count++;
								  }
								  byte[] btImg = getImageFromNetByUrl(colorimage);
								  System.out.println(colorimage);
								 String colorurl=folderName(btImg, subFolderColor);
								map.put(n1, colorurl);
								allcolor.add(map);
							}
						}
					   map.put("spec_values", spec_values);	
					}else{
						Integer[] spec_values=new Integer[1];
						int count=0;
						String colorss=doc.select("div.saleouter p  i.pink").text();
						if(colorss.contains("/")){
				    		   colorss="黑色";
				    	   }
						if(!StringUtil.isEmpty(colorss) && colorss!=null){
							Map<String, String> map=new HashMap<String, String>();
							Integer spec_value_id=goodsManager.getSpecValue(colorss.trim());
							 if(spec_value_id!=null){
								  spec_values[count]=spec_value_id;
								  count++;
							  }
							  map.put(colorss, null);
							  allcolor.add(map);
						}
						 map.put("spec_values", spec_values);	
					}
					Elements sizemax=doc.select("dl#shoe_size_list >dd");
					Integer number=0;
					if(sizemax.size()>0){
						for (Element element : sizemax) {
							String colorvalue=element.text();
							if(colorvalue!=null && !StringUtil.isEmpty(colorvalue)){
								Integer spec_value_id=goodsManager.getSpecValueSize(colorvalue.trim());
								if(spec_value_id!=null){
									number++;
								}
							}
						}
					}
					if(sizemax.size()>0){
						Integer count=0;
						Integer[] spec_values_size=new Integer[number];
						for (Element element : sizemax) {
							String colorvalue=element.text();
							if(colorvalue!=null && !StringUtil.isEmpty(colorvalue)){
								 Integer spec_value_id=goodsManager.getSpecValueSize1(colorvalue.trim());
								 if(spec_value_id!=null){
									 spec_values_size[count]=spec_value_id;
									  count++;
								  }
							}
						}
						map.put("spec_values_size", spec_values_size);	
					}
					map.put("allcolor", allcolor);
					map.put("catagory", catagory);
				}else if(resultcolor!=0 && resultsize1!=0 && resultsize2==0){
					Integer catagory=5;
					Integer[] specs=new Integer[2];
					specs[0]=spec_ids[0];
					specs[1]=spec_ids[1];
					map.put("specs", specs);
					Elements colors=doc.select("dl.ltOtherColorV2 > dd >a >img");
					List<Map<String, String>> allcolor=new ArrayList<Map<String,String>>();
					Integer numbercolor=0;
					if(colors.size()>0){
						for (Element element : colors) {
							String colorvalue=element.attr("alt");
							String colorimage=element.attr("src");
							if(colorvalue!=null && !StringUtil.isEmpty(colorvalue) && StringUtil.isEmpty(colorimage)){
								String n1=colorvalue.substring(colorvalue.indexOf("颜色组成：")+5);
							  Integer spec_value_id=goodsManager.getSpecValue(n1.trim());
							  if(spec_value_id!=null){
								  numbercolor++;
							    }
							}else{
								String n1=colorvalue.substring(colorvalue.indexOf("颜色组成：")+5);
								  Integer spec_value_id=goodsManager.getSpecValue(n1.trim());
								  if(spec_value_id!=null){
									  numbercolor++;
							    }
							}
						}
					}
					if(colors.size()>0){
						Integer[] spec_values=new Integer[numbercolor];
						int count=0;
						for (Element element : colors) {
							Map<String, String> map=new HashMap<String, String>();
							String colorvalue=element.attr("alt");
							String colorimage=element.attr("src");
							if(colorvalue!=null && !StringUtil.isEmpty(colorvalue) && StringUtil.isEmpty(colorimage)){
								String n1=colorvalue.substring(colorvalue.indexOf("颜色组成：")+5);
							  Integer spec_value_id=goodsManager.getSpecValue(n1.trim());
							  if(spec_value_id!=null){
								  spec_values[count]=spec_value_id;
								  count++;
							  }
							  map.put(n1, null);
							  allcolor.add(map);
							}else{
								String n1=colorvalue.substring(colorvalue.indexOf("颜色组成：")+5);
								Integer spec_value_id=goodsManager.getSpecValue(n1.trim());
								  if(spec_value_id!=null){
									  spec_values[count]=spec_value_id;
									  count++;
								  }
								  byte[] btImg = getImageFromNetByUrl(colorimage);
								  System.out.println(colorimage);
								 String colorurl=folderName(btImg, subFolderColor);
								map.put(n1, colorurl);
								allcolor.add(map);
							}
						}
					   map.put("spec_values", spec_values);	
					}else{
						Integer[] spec_values=new Integer[1];
						int count=0;
						String colorss=doc.select("div.saleouter p  i.pink").text();
						if(colorss.contains("/")){
				    		   colorss="黑色";
				    	   }
						if(!StringUtil.isEmpty(colorss) && colorss!=null){
							Map<String, String> map=new HashMap<String, String>();
							Integer spec_value_id=goodsManager.getSpecValue(colorss.trim());
							 if(spec_value_id!=null){
								  spec_values[count]=spec_value_id;
								  count++;
							  }
							  map.put(colorss, null);
							  allcolor.add(map);
						}
						 map.put("spec_values", spec_values);	
					}
					Elements sizemax=doc.select("dl#shoe_size_list >dd");
					Integer number=0;
					if(sizemax.size()>0){
						for (Element element : sizemax) {
							String colorvalue=element.text();
							if(colorvalue!=null && !StringUtil.isEmpty(colorvalue)){
								Integer spec_value_id=goodsManager.getSpecValueSize(colorvalue.trim());
								if(spec_value_id!=null){
									number++;
								}
							}
						}
					}
					if(sizemax.size()>0){
						Integer count=0;
						Integer[] spec_values_size=new Integer[number];
						for (Element element : sizemax) {
							String colorvalue=element.text();
							if(colorvalue!=null && !StringUtil.isEmpty(colorvalue)){
								 Integer spec_value_id=goodsManager.getSpecValueSize(colorvalue.trim());
								 if(spec_value_id!=null){
									 spec_values_size[count]=spec_value_id;
									  count++;
								  }
							}
						}
						map.put("spec_values_size", spec_values_size);	
					}
					map.put("allcolor", allcolor);
					map.put("catagory", catagory);
				}
			}
		} catch (IOException e1) {
			String result=e1.getMessage();
			if(result.contains("Read timed out")){
				System.out.println("读取超时");
				map.put("Read timed out", "读取超时");
			}else if(result.contains("connect timed out")){
				map.put("connect timed out", "连接超时");
				System.out.println("连接超时");
			}else if(result.contains("www.letao.com")){
				map.put("www.letao.com", "您输入的访问地址不能连通");
				System.out.println("您输入的访问地址不能连通");
			}else{
				e1.printStackTrace();
			}
		}
		return map;
	}

	public static String getUrlCode(String productName) {
		String retUrlCode = "";
		try {
			retUrlCode = URLDecoder.decode(productName, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return retUrlCode;
	}

	/**
	 * 根据地址获得数据的字节流
	 * 
	 * @param strUrl
	 *            网络连接地址
	 * @return
	 */
	public static byte[] getImageFromNetByUrl(String strUrl) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
			byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
			return btImg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从输入流中获取数据
	 * 
	 * @param inStream
	 *            输入流
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 将图片写入到磁盘
	 * 
	 * @param img
	 *            图片数据流
	 * @param fileName
	 *            文件保存时的名称
	 */
	public static void writeImageToDisk(byte[] img, String fileName,
			String subFolder) {
		try {
			String filePath = "";
			String static_server_path = SystemSetting.getStatic_server_path();

			filePath = static_server_path + "/attachment/";
			if (subFolder != null) {
				filePath += subFolder + "/";
			}
			filePath += fileName;
			int potPos = filePath.lastIndexOf('/') + 1;
			String folderPath = filePath.substring(0, potPos);
			FileUtil.createFolder(folderPath);
			FileOutputStream fops = new FileOutputStream(filePath);
			fops.write(img);
			fops.flush();
			fops.close();
			System.out.println("图片已经写入");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String transformHttp(String fileName, String subFolder) {
		String filePath = "";

		String static_server_path = SystemSetting.getStatic_server_path();

		filePath = static_server_path + "/attachment/";
		if (subFolder != null) {
			filePath += subFolder + "/";
		}
		String path = EopSetting.FILE_STORE_PREFIX + "/attachment/"
				+ (subFolder == null ? "" : subFolder) + "/" + fileName;
		return UploadUtil.replacePath(path);
	}

	public static String folderName(byte[] img, String subFolder) {
		if (null != img && img.length > 0) {
			System.out.println("读取到：" + img.length + " 字节");
			fileName = DateUtil.toString(new Date(), "yyyyMMddHHmmss")
					+ StringUtil.getRandStr(4) + ".jpg";
			writeImageToDisk(img, fileName, subFolder);
		} else {
			System.out.println("没有从该连接获得内容");
		}
		return transformHttp(fileName, subFolder);
	}
	public static String detailUpload(byte[] img){
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000)+".png";
		writeImageToDetail(img, newFileName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		String saveUrl  = request.getContextPath() + "/attached/image";
		return saveUrl+"/"+ymd+"/"+newFileName;
	}
	/**
	 * 将图片信息写入到服务器中
	 * 
	 * @param img
	 *            图片数据流
	 * @param fileName
	 *            文件保存时的名称
	 */
	public static void writeImageToDetail(byte[] img, String fileName) {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		try {
			String filePath = "";
			String static_server_path = request.getRealPath("/")+"attached/";
			File test = new File(static_server_path);
			if(!test.exists()){
				test.mkdirs();
			}
			//文件保存目录URL
			filePath = static_server_path + "/image";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String ymd = sdf.format(new Date());
			String path=filePath+"/"+ymd+"/";
			FileUtil.createFolder(path);
			FileOutputStream fops = new FileOutputStream(path+fileName);
			fops.write(img);
			fops.flush();
			fops.close();
			System.out.println("图片已经写入");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
