package com.enation.app.tradeease.core.service.translation;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import com.google.gson.Gson;
 
public class TestBaiduTranslate {
 
    private static String url = "http://openapi.baidu.com/public/2.0/bmt/translate";
    
    private static String api_key = "QBcQnvZ79BOIh7NnbVcf2103";
    
    public static void main(String[] args){
    	TestBaiduTranslate tbt = new TestBaiduTranslate();
    	//System.out.println(tbt.translate("我叫刘宏宇，我是一名程序员，我今年23岁，毕业于云南大学", "zh", "en"));
    }
 
    public static String translate(String text, String from, String to) {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        method.setQueryString(new NameValuePair[] {
                new NameValuePair("from", from),
                new NameValuePair("to", to),
                new NameValuePair("client_id", api_key),
                // 多条内容用\n分隔
                new NameValuePair("q", text) });
 
        try {
			client.executeMethod(method);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String response = null;
		try {
			response = new String(method.getResponseBodyAsString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //System.out.println(response);
        method.releaseConnection();
 
        Gson gson = new Gson();
        BaiduTrans bt = gson.fromJson(response, BaiduTrans.class);
        String st = null;
        for (TransResult tr : bt.getTrans_result()) {
            st = tr.getDst();
//            //System.out.println(tr.getDst());
        }
		return st;
    }
 
    class BaiduTrans {
        private String from;
        private String to;
        private List<TransResult> trans_result;
 
        public String getFrom() {
            return from;
        }
 
        public void setFrom(String from) {
            this.from = from;
        }
 
        public String getTo() {
            return to;
        }
 
        public void setTo(String to) {
            this.to = to;
        }
 
        public List<TransResult> getTrans_result() {
            return trans_result;
        }
 
        public void setTrans_result(List<TransResult> trans_result) {
            this.trans_result = trans_result;
        }
    }
     
    class TransResult {
        public String getSrc() {
            return src;
        }
 
        public void setSrc(String src) {
            this.src = src;
        }
 
        public String getDst() {
            return dst;
        }
 
        public void setDst(String dst) {
            this.dst = dst;
        }
 
        private String src;
        private String dst;
    }
}
