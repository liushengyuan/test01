package com.enation.app.b2b2c.core.action.api.goods.move;

import com.enation.eop.SystemSetting;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.StringUtil;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.BrowserVersion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public  class MoveDetail{
	/**
	 * 商品价格
	 * 
	 * @param url
	 * @param tagId
	 * 			标签ID
	 * @return
	 * @throws Exception
	 */
    public String getPrice(String url, String otherWeb) throws Exception{
    	LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
    	java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
    	java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
    	String price = "0";
    	//可以选择浏览器内核
        WebClient webClient = new WebClient(BrowserVersion.getDefault());
        //是否加载js/css
        webClient.setJavaScriptEnabled(true);
        webClient.setCssEnabled(true);
        webClient.setThrowExceptionOnScriptError(false);
        HtmlPage page = webClient.getPage(url);
        if(otherWeb.equals("京东")){
        	String pageText=page.getElementById("jd-price").asText();
        	price = pageText.length() > 2 ? pageText.substring(1) : "0";
        }
        // 淘宝价格暂时抓取不到,因为淘宝使用的js不同
        if(otherWeb.equals("淘宝")){
        }
        /********************* 速卖通是美元，要注意，没时间转成RMB了，留给有缘人。。。***********************************/
        if(otherWeb.equals("速卖通")){
        	String pageText = page.getElementById("product-price").asText();
        	price = pageText.substring(4,pageText.length()-8);
        }
        /*************************************** 敦煌网也是美元 ************************************************/
        if(otherWeb.equals("敦煌网")){
        	String pageText = page.getElementById("_init_buyer_buyernewprice").asText();
        	price = pageText.length() > 2 ? pageText.substring(4) : "0";
        }
        //获取子元素,感觉这个方法很有用,暂且留着吧
        /**
	        java.util.Iterator<HtmlElement> iter = div.getChildElements().iterator();
	        while(iter.hasNext()){
	        	//System.out.println(iter.next().getTextContent());
	        }
        */
        //关闭
        webClient.closeAllWindows();
        return price;
    }
    
    /**
     * 商品明细
     * 
     * @param url
     * @return
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getDetails(String url, String otherWeb) {
    	Document doc = null;
    	try {
    		doc = Jsoup.connect(url).get();
    	} catch (IOException e1) {
    		e1.printStackTrace();
    	}
    	Map map = new HashMap();
    	if(otherWeb.equals("京东")){
    		// 商品名称
        	String name = doc.select("div#itemInfo > div#name").text();
        	map.put("name", name);
        	// 关键字
        	String keywords = doc.select("meta[name=keywords]").get(0).attr("content");
        	map.put("keywords", keywords);
    		// 商品图片
        	String mainImg = doc.select("div#product-intro > div#preview > div#spec-n1 > img").attr("src");
        	map.put("mainImg", mainImg);
        	Elements imgs = doc.select("div#product-intro > div#preview > div#spec-list > div > ul > li > img");
    		List imgList = new ArrayList();
    		for(Element e: imgs){
    			imgList.add(e.attr("src").toString());
    		}
    		map.put("imgList", imgList);
    		// 商品详情
    		Elements details = doc.select("div#product-detail > div > div > div.p-parameter");
    		map.put("details", details);
    	}
    	if(otherWeb.equals("淘宝")){
    		// 商品名称
        	String name = doc.select("div#J_Title").text();
        	map.put("name", name);
        	// 关键字
        	String keywords = doc.select("meta[name=keywords]").get(0).attr("content");
        	map.put("keywords", keywords);
        	// 商品图片
    		String mainImg = doc.select("img[id=J_ImgBooth]").attr("src");
    		map.put("mainImg", mainImg);
    		Elements imgs = doc.select("div.tb-gallery > ul#J_UlThumb > li > div > a > img");
    		List imgList = new ArrayList();
    		for(Element e: imgs){
    			imgList.add(e.attr("data-src").toString());
    		}
    		map.put("imgList", imgList);
    		// 商品详情
    		Elements details = doc.select("div#attributes");
    		map.put("details", details);
    	}
    	if(otherWeb.equals("速卖通")){
    		// 商品名称
    		String name = doc.select("div.product-info-main > div > h1").text();
    		map.put("name", name);
    		// 关键字
    		String keywords = doc.select("meta[name=keywords]").get(0).attr("content");
    		map.put("keywords", keywords);
    		// 商品图片
    		String mainImg = doc.select("div.ui-image-viewer-thumb-wrap > a > img").attr("src");
    		map.put("mainImg", mainImg);
    		Elements imgs = doc.select("#img > ul > li > span > img");
    		List imgList = new ArrayList();
    		for(Element e: imgs){
    			imgList.add(e.attr("src").toString());
    		}
    		map.put("imgList", imgList);
    		// 商品详情
    		Elements details = doc.select("div#product-desc");
    		map.put("details", details);
    	}
    	if(otherWeb.equals("敦煌网")){
    		// 商品名称
    		String name = doc.select("div.hinfo > h1").text();
    		map.put("name", name);
    		// 关键字
    		String keywords = doc.select("meta[name=keywords]").get(0).attr("content");
    		map.put("keywords", keywords);
    		// 商品图片
    		String mainImg = doc.select("div#bimgContainer > div > span > img").attr("src");
    		map.put("mainImg", mainImg);
    		Elements imgs = doc.select("div.simg-warp > div > ul > li");
    		List imgList = new ArrayList();
    		for(Element e: imgs){
    			imgList.add(e.attr("s-init").toString());
    		}
    		map.put("imgList", imgList);
    		// 商品详情
    		Elements details01 = doc.select("div.item-specifics");
    		// 下面的详情里面包含规格，咱们可以参考，如果拿过来用的话，还要解析String，留给有缘人^.^
    		Elements details02 = doc.select("div.description");
    		String details = details01.toString() + details02.toString();
    		map.put("details", details);
    	}
    	return map;
    }
	/*public static void getUrl(String url){
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
    	java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
    	java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
    	//可以选择浏览器内核
        WebClient webClient = new WebClient(BrowserVersion.getDefault());
        //是否加载js/css
        webClient.setJavaScriptEnabled(false);
        webClient.setCssEnabled(false);
        webClient.setThrowExceptionOnScriptError(false);
        String str;
        HtmlPage page;
		try {
			page = webClient.getPage(url);
			str = page.getTitleText();
	        System.out.println(str);
	        //获取页面的XML代码
	        str = page.asXml();
	        System.out.println(str);
	        //获取页面的文本
	        str = page.asText();
	        System.out.println(str);
	    	webClient.closeAllWindows();
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = null;
    	try {
    		doc = Jsoup.connect(url).get();
    	} catch (IOException e1) {
    		e1.printStackTrace();
    	}
    	String name=doc.select("div.saleouter > h1").text();
    	System.out.println(name);
    	String price=doc.select("i[class=ltprice big]").text();
    	System.out.println(price);
        System.out.println(doc.select("img#img_0").get(0).attr("src"));
        Elements imgs=doc.select("ul#imgpoint > li >img ");
        for (Element element : imgs) {
			byte[] btImg = getImageFromNetByUrl(element.attr("src"));  
	        if(null != btImg && btImg.length > 0){  
	            System.out.println("读取到：" + btImg.length + " 字节");  
	            String fileName = DateUtil.toString(new Date(), "yyyyMMddHHmmss") + StringUtil.getRandStr(4) + ".png";
	            writeImageToDisk(btImg, fileName);  
	        }else{  
	            System.out.println("没有从该连接获得内容");  
	        }  
		}
       System.out.println(doc.select("table#shoe_property > tbody ").text()); 
	}*/
	/*public static void main(String[] args) {
		MoveDetailUtil.moveMainMethod("http://www.letao.com/shoe-kuaiman-328196964-K9185%E8%93%9D%E8%89%B2",21211);
	}*/
	
	public static  String getUrlCode(String productName){  
        String retUrlCode = "";  
        try {  
            retUrlCode = URLDecoder.decode(productName, "utf8");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        return retUrlCode;  
    }  
	
   
}