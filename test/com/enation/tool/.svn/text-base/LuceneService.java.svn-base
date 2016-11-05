package com.enation.tool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.base.core.dic.SytConfig;
import com.enation.app.base.core.service.impl.TokeniserService;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
@Component
public class LuceneService  implements ApplicationListener<ApplicationEvent> {
	private static final String ID = "id";
	private static final String PRJID = "prjId";
	private static final String PRJTYPE = "prjType";
	private static final String CONTENT = "content";

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private TokeniserService tokeniserService;
	private static IndexWriter writer = null;
	private Directory luceneDir;
	private Analyzer analyzer;
	
	private Version version = Version.LUCENE_5_0_0;
	private volatile byte status=LUCENE_NOT_INITED;
	private static final byte LUCENE_NOT_INITED=0;
	private static final byte LUCENE_INITING=1;
	private static final byte LUCENE_INITITED=2;
	private static final byte LUCENE_STOPPING=3;
	private static final byte LUCENE_STOPPED=4;
	private static final byte LUCENE_INITERROR=5;
	@Override
	public void onApplicationEvent(ApplicationEvent evt) {
		if(evt instanceof ContextStartedEvent || evt instanceof ContextRefreshedEvent){
			if(status==LUCENE_NOT_INITED){
				this.init();
			}
			
	}
	else if(evt instanceof ContextStoppedEvent || evt instanceof ContextClosedEvent){
		if(status==LUCENE_INITITED){
			this.stop();
		}
		
	}
	
}
	private void init(){
		try{
			if(status!=LUCENE_NOT_INITED){
				return;
			}
			this.status=LUCENE_INITING;
			File lockFile = new File(SytConfig.getProperty("luceneDir", "/var/sytdirs/luceneDir")+"/write.lock");
			if(lockFile.exists()){
				lockFile.delete();
			}
			File dir = new File(SytConfig.getProperty("luceneDir", "/var/sytdirs/luceneDir"));
			//System.out.println(dir.toPath());
			//System.out.println(dir.getAbsolutePath());
			luceneDir= FSDirectory.open(Paths.get(dir.getAbsolutePath()));
			boolean createIndexBase = true;
		 	if(dir.exists() && dir.listFiles()!=null && dir.listFiles().length>0){
	        	createIndexBase = false;
		 	}
		 	analyzer = new StandardAnalyzer();
		 	IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
		 	if(createIndexBase){
		 		cfg.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		 	}else{
		 		cfg.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
		 	}
		 	cfg.setMaxBufferedDocs(100000);
		 	cfg.setRAMBufferSizeMB(500);
			writer = new IndexWriter(luceneDir,cfg);
			this.status = LUCENE_INITITED;
			logger.info("成功启动Lucene服务!");
			System.out.println("成功启动Lucene服务!");
		}catch(Exception e){
			
			this.status=LUCENE_INITERROR;
			throw new RuntimeException(e);
			
			
		}
	}
	
	private void stop(){
		if(status!=LUCENE_INITITED){
			return;
		}
		this.status=LUCENE_STOPPING;
		if(writer!=null){
			try {
				writer.close();
				//System.out.println("成功关闭Lucene服务!");
			} catch (CorruptIndexException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		this.status=LUCENE_STOPPED;
	}
	public void commit(){
		try {
			writer.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public void optimize(){
		try {
			writer.commit();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressWarnings("deprecation")
	public void updateInformation(String prjId, String prjType, StoreGoods g, boolean commit) {
		
		try {
			if(!writer.isLocked(luceneDir)){
				File lockFile = new File(SytConfig.getProperty("luceneDir", "/var/sytdirs/luceneDir")+"/write.lock");
				if(lockFile.exists()){
					lockFile.delete();
				}
				File dir = new File(SytConfig.getProperty("luceneDir", "/var/sytdirs/luceneDir"));
				//System.out.println(dir.toPath());
				//System.out.println(dir.getAbsolutePath());
				luceneDir= FSDirectory.open(Paths.get(dir.getAbsolutePath()));
				boolean createIndexBase = true;
			 	if(dir.exists() && dir.listFiles()!=null && dir.listFiles().length>0){
		        	createIndexBase = false;
			 	}
			 	analyzer = new StandardAnalyzer();
			 	IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
			 	if(createIndexBase){
			 		cfg.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			 	}else{
			 		cfg.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
			 	}
			 	cfg.setMaxBufferedDocs(100000);
			 	cfg.setRAMBufferSizeMB(500);
				writer = new IndexWriter(luceneDir,cfg);
			}
			
			StringBuffer content = new StringBuffer();
			//商品中文名称
			content.append(StringUtils.isNotEmpty(g.getName()) ? g.getName() : "").append(" ");
			//商品英文名称
			content.append(StringUtils.isNotEmpty(g.getName_en()) ? g.getName_en() : "").append(" ");
			//商品俄文名称
			content.append(StringUtils.isNotEmpty(g.getName_ru()) ? g.getName_ru() : "").append(" ");
			content.append(StringUtils.isNotEmpty(g.getMeta_keywords()) ? g.getMeta_keywords() : "").append(" ");
			content.append(StringUtils.isNotEmpty(g.getRu_keyword()) ? g.getRu_keyword() : "").append(" ");
			content.append(g.getIs_belong()!=null ? g.getIs_belong(): "0").append(" ");
			// 删除原有记录
			writer.deleteDocuments(new Term(ID, prjType + "__" + prjId));
			writer.deleteDocuments(new Term(PRJID,prjId));
			writer.deleteDocuments(new Term("name",StringUtils.isNotEmpty(g.getName()) ? g.getName() : ""));
			writer.deleteDocuments(new Term("meta_keywords",StringUtils.isNotEmpty(g.getMeta_keywords()) ? g.getMeta_keywords() : ""));
			writer.deleteDocuments(new Term("ru_keywords",StringUtils.isNotEmpty(g.getRu_keyword()) ? g.getRu_keyword() : ""));
			writer.deleteDocuments(new Term("name_en",StringUtils.isNotEmpty(g.getName_en()) ? g.getName_en() : ""));
			writer.deleteDocuments(new Term("content",StringUtils.isNotEmpty(content.toString()) ? content.toString() : ""));
			writer.deleteDocuments(new Term("name_ru",StringUtils.isNotEmpty(g.getName_ru()) ? g.getName_ru() : ""));
			writer.deleteDocuments(new Term("is_belong",g.getIs_belong()!=null ? g.getIs_belong().toString() : "0"));
			//生成新记录(StringUtils.isNotEmpty(g.getName()) ? g.getName() : "").append(" ");
			Document doc = new Document();
			doc.add( new TextField(ID,prjType + "__" + prjId , Store.YES));
			doc.add( new TextField(PRJID,prjId, Store.YES));
			doc.add( new TextField(PRJTYPE,prjType, Store.YES));
			doc.add( new TextField("name",StringUtils.isNotEmpty(g.getName()) ? g.getName() : "", Store.YES));
			doc.add( new TextField("meta_keywords",StringUtils.isNotEmpty(g.getMeta_keywords()) ? g.getMeta_keywords() : "", Store.YES));
			doc.add( new TextField("ru_keywords",StringUtils.isNotEmpty(g.getRu_keyword()) ? g.getRu_keyword() : "", Store.YES));
			doc.add( new TextField("name_en",StringUtils.isNotEmpty(g.getName_en()) ? g.getName_en() : "", Store.YES));
			doc.add( new TextField("name_ru",StringUtils.isNotEmpty(g.getName_ru()) ? g.getName_ru() : "", Store.YES));
			doc.add( new TextField("is_belong", g.getIs_belong()!=null ? g.getIs_belong().toString() : "0", Store.YES));
			if(g.getMarket_enable()!=null){
				doc.add( new TextField("market_enable",StringUtils.isNotEmpty(g.getMarket_enable().toString()) ? g.getMarket_enable().toString() : "", Store.YES));
			}else{
				doc.add( new TextField("market_enable","0", Store.YES));
			}
			doc.add( new TextField("content",StringUtils.isNotEmpty(content.toString()) ? content.toString() : "", Store.YES));
			writer.addDocument(doc);
			//System.out.println(doc.toString());
			if(commit){
				writer.commit();
				writer.close();
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	public void addindex(String prjId, String prjType, StoreGoods g, boolean commit) {
		try {
			
			StringBuffer content = new StringBuffer();
			//商品中文名称
			content.append(StringUtils.isNotEmpty(g.getName()) ? g.getName() : "").append(" ");
			//商品英文名称
			content.append(StringUtils.isNotEmpty(g.getName_en()) ? g.getName_en() : "").append(" ");
			//商品俄文名称
			content.append(StringUtils.isNotEmpty(g.getName_ru()) ? g.getName_ru() : "").append(" ");
			content.append(StringUtils.isNotEmpty(g.getMeta_keywords()) ? g.getMeta_keywords() : "").append(" ");
			content.append(StringUtils.isNotEmpty(g.getRu_keyword()) ? g.getRu_keyword() : "").append(" ");
			content.append(g.getIs_belong()!=null ? g.getIs_belong(): "0").append(" ");
			// 删除原有记录
			writer.deleteDocuments(new Term(ID, prjType + "__" + prjId));
			
			//生成新记录(StringUtils.isNotEmpty(g.getName()) ? g.getName() : "").append(" ");
			Document doc = new Document();
			doc.add( new TextField(ID,prjType + "__" + prjId , Store.YES));
			doc.add( new TextField(PRJID,prjId, Store.YES));
			doc.add( new TextField(PRJTYPE,prjType, Store.YES));
			doc.add( new TextField("name",StringUtils.isNotEmpty(g.getName()) ? g.getName() : "", Store.YES));
			doc.add( new TextField("meta_keywords",StringUtils.isNotEmpty(g.getMeta_keywords()) ? g.getMeta_keywords() : "", Store.YES));
			doc.add( new TextField("ru_keywords",StringUtils.isNotEmpty(g.getRu_keyword()) ? g.getRu_keyword() : "", Store.YES));
			doc.add( new TextField("name_en",StringUtils.isNotEmpty(g.getName_en()) ? g.getName_en() : "", Store.YES));
			doc.add( new TextField("name_ru",StringUtils.isNotEmpty(g.getName_ru()) ? g.getName_ru() : "", Store.YES));
			doc.add( new TextField("is_belong", g.getIs_belong()!=null ? g.getIs_belong().toString() : "0", Store.YES));
			if(g.getMarket_enable()!=null){
				doc.add( new TextField("market_enable",StringUtils.isNotEmpty(g.getMarket_enable().toString()) ? g.getMarket_enable().toString() : "", Store.YES));
			}else{
				doc.add( new TextField("market_enable","0", Store.YES));
			}
			doc.add( new TextField("content",StringUtils.isNotEmpty(content.toString()) ? content.toString() : "", Store.YES));
			writer.addDocument(doc);
//			//System.out.println(doc.toString());
			if(commit){
				writer.commit();
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	public String queryGoodsLucene(String content,String language){

        //Date date1 = new Date();
        StringBuffer goods_name = new StringBuffer();
        StringBuffer goods_name_ru = new StringBuffer();
        try {
			IndexSearcher searcher = null;
			QueryParser parser = null;
			parser = new QueryParser("content", analyzer); 
			DirectoryReader ireader = DirectoryReader.open(luceneDir);
			Query query = parser.parse(QueryParser.escape(content));
			searcher = new IndexSearcher(ireader);
			ScoreDoc[] hits = searcher.search(query, null, 20).scoreDocs;
			if(language.equalsIgnoreCase("zh")){
				for (int i = 0; i < hits.length; i++) {
	                Document hitDoc = searcher.doc(hits[i].doc);
	                ////System.out.println(hitDoc.get("name")+"  "+hitDoc.get("market_enable"));
	                if(hitDoc.get("market_enable").equalsIgnoreCase("1")){
	                	goods_name.append(","+hitDoc.get("name").replace("\"", "\\\"")) ;
	                }
	            }
			}else if(language.equalsIgnoreCase("ru")){
				for (int i = 0; i < hits.length; i++) {
					 Document hitDoc = searcher.doc(hits[i].doc);
					 ////System.out.println(hitDoc.get("market_enable"));
					 if(hitDoc.get("market_enable").equalsIgnoreCase("1")){
						 goods_name_ru.append(","+hitDoc.get("name_ru").replace("\"", "\\\"")) ;
		             }        
	            }
			}
		 
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
	        
        //Date date2 = new Date();
//        //System.out.println("查看索引-----耗时：" + (date2.getTime() - date1.getTime()) + "ms\n");
//        //System.out.println("查看索引-----结果："+goods_name.toString());
//        //System.out.println("查看索引-----结果："+goods_name_ru.toString());
        
        if(language.equals("zh")){
        	return goods_name.toString();
        }else{
        	return goods_name_ru.toString();
        }
       
	}
	
	public String queryGoodsLucene(String content,String language,String is_belong){

        //Date date1 = new Date();
        StringBuffer goods_name = new StringBuffer();
        StringBuffer goods_name_ru = new StringBuffer();
        try {
			IndexSearcher searcher = null;
			QueryParser parser = null;
			parser = new QueryParser("content", analyzer); 
			DirectoryReader ireader = DirectoryReader.open(luceneDir);
			Query query = parser.parse(QueryParser.escape(content));
			searcher = new IndexSearcher(ireader);
			ScoreDoc[] hits = searcher.search(query, null, 20).scoreDocs;
			if(language.equalsIgnoreCase("zh")){
				for (int i = 0; i < hits.length; i++) {
	                Document hitDoc = searcher.doc(hits[i].doc);
	                ////System.out.println(hitDoc.get("name")+"  "+hitDoc.get("market_enable"));
	                if(hitDoc.get("market_enable").equalsIgnoreCase("1") && hitDoc.get("is_belong").equalsIgnoreCase(is_belong)){
	                	goods_name.append(","+hitDoc.get("name").replace("\"", "\\\"")) ;
	                }
	            }
			}else if(language.equalsIgnoreCase("ru")){
				for (int i = 0; i < hits.length; i++) {
					 Document hitDoc = searcher.doc(hits[i].doc);
					 ////System.out.println(hitDoc.get("market_enable"));
					 if(hitDoc.get("market_enable").equalsIgnoreCase("1")){
						 goods_name_ru.append(","+hitDoc.get("name_ru").replace("\"", "\\\"")) ;
		             }        
	            }
			}
		 
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
	        
        //Date date2 = new Date();
//        //System.out.println("查看索引-----耗时：" + (date2.getTime() - date1.getTime()) + "ms\n");
//        //System.out.println("查看索引-----结果："+goods_name.toString());
//        //System.out.println("查看索引-----结果："+goods_name_ru.toString());
        
        if(language.equals("zh")){
        	return goods_name.toString();
        }else{
        	return goods_name_ru.toString();
        }
       
	}
	
	public String queryGoodsSearch(String keyword,String language){

        StringBuffer goods_id = new StringBuffer();
        try {
			IndexSearcher searcher = null;
			QueryParser parser = null;
			parser = new QueryParser("content", analyzer); 
			DirectoryReader ireader = DirectoryReader.open(luceneDir);
			Query query = parser.parse(QueryParser.escape(keyword));
			searcher = new IndexSearcher(ireader);
			ScoreDoc[] hits = searcher.search(query, null, 20).scoreDocs;
			for (int i = 0; i < hits.length; i++) {
				Document hitDoc = searcher.doc(hits[i].doc);
	            ////System.out.println(hitDoc.get("name")+"  "+hitDoc.get("market_enable"));
	            if(hitDoc.get("market_enable").equalsIgnoreCase("1")){
	               goods_id.append(","+hitDoc.get("prjId")) ;
	            }
	        }
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
        return goods_id.toString();
	}
	
	
	public Logger getLogger() {
		return logger;
	}
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	public TokeniserService getTokeniserService() {
		return tokeniserService;
	}
	public void setTokeniserService(TokeniserService tokeniserService) {
		this.tokeniserService = tokeniserService;
	}
	public static IndexWriter getWriter() {
		return writer;
	}
	public static void setWriter(IndexWriter writer) {
		LuceneService.writer = writer;
	}
	public Directory getLuceneDir() {
		return luceneDir;
	}
	public void setLuceneDir(Directory luceneDir) {
		this.luceneDir = luceneDir;
	}
	public Analyzer getAnalyzer() {
		return analyzer;
	}
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	public Version getVersion() {
		return version;
	}
	public void setVersion(Version version) {
		this.version = version;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public static String getId() {
		return ID;
	}
	public static String getPrjid() {
		return PRJID;
	}
	public static String getPrjtype() {
		return PRJTYPE;
	}
	public static String getContent() {
		return CONTENT;
	}
	public static byte getLuceneNotInited() {
		return LUCENE_NOT_INITED;
	}
	public static byte getLuceneIniting() {
		return LUCENE_INITING;
	}
	public static byte getLuceneInitited() {
		return LUCENE_INITITED;
	}
	public static byte getLuceneStopping() {
		return LUCENE_STOPPING;
	}
	public static byte getLuceneStopped() {
		return LUCENE_STOPPED;
	}
	public static byte getLuceneIniterror() {
		return LUCENE_INITERROR;
	}
	
}
