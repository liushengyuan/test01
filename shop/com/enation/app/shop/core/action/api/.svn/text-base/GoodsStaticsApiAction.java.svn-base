package com.enation.app.shop.core.action.api;



import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.RouteOrder;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.IProductManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Element;
/**
 * 商品静态页标签
 * @author fenlongli
 *
 */
@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/shop")
@Action("goodsStatics")
public class GoodsStaticsApiAction extends WWAction{
	private IProductManager productManager;
	private IGoodsManager goodsManager;
	private Integer productid;	//货品Id
	private Integer goods_id;	//商品Id
	private IOrderManager orderManager;
	public static final String FILESEPARATOR = System.getProperty("file.separator");
	public IOrderManager getOrderManager() {
		return orderManager;
	}
	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}
	/**
	 * 根据商品Id获取货品库存
	 * @return
	 */
	public String getGoodsStore(){
		this.showSuccessJson(productManager.getByGoodsId(goods_id).getEnable_store()+"");
		//System.out.println(json);
		return this.JSON_MESSAGE;
	}
	/**
	 * 根据货品Id获取货品库存
	 * @param productid 货品Id
	 * @return
	 */
	public String getProductStore(){
		this.showSuccessJson(productManager.get(productid).getEnable_store()+"");
		return this.JSON_MESSAGE;
	}
	public     void    biaogepdf(){
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		Document document = new Document(PageSize.A4,0,0,0,0);
		 ByteArrayOutputStream ba = new ByteArrayOutputStream();
		PdfWriter writer=null;
		try{	

		//支持中文
		BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",false);

		//设置字体
		Font fontsize4 = new Font(bfChinese,4,Font.NORMAL);
		Font fontsize5 = new Font(bfChinese,5,Font.BOLD);
		Font fontsize6 = new Font(bfChinese,6,Font.BOLD);
		Font fontsize10 = new Font(bfChinese,10,Font.BOLD);
		Font fontsize8 = new Font(bfChinese,8,Font.BOLD);


		writer = PdfWriter.getInstance(document, ba);

		document.open();

		//主表格
		PdfPTable mainTable = new PdfPTable(2);     
		mainTable.setTotalWidth(320);
		mainTable.setLockedWidth(true);

		//默认无边框
	//	mainTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER); 



		//表格靠左对齐
		mainTable.setHorizontalAlignment(Element.ALIGN_LEFT);

		//****************左表格begin*********************S
		PdfPTable leftTable = new PdfPTable(1);

	//	leftTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		leftTable.setHorizontalAlignment(Element.ALIGN_LEFT);


		//==================左表格上部分 begin=================

		PdfPTable topTable = new PdfPTable(2);
	//	topTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

	//	topTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		topTable.setHorizontalAlignment(Element.ALIGN_LEFT);

		//设置表格宽度
		topTable.setTotalWidth(130);

		//锁定表格宽度
		topTable.setLockedWidth(true);

		topTable.addCell(new Paragraph("Return Mail Address : \nMAIL RETURNS PO BOX\n694 HAYES UB3 9PB\n\nUNITED KINGDOM",fontsize4));

		//===========左上角字符end=================


		//===============image begin===============

		Image img = Image.getInstance("C:/Users/sks/Desktop/1.jpg");

		//自动调整图片适应单元格
		topTable.addCell(new PdfPCell(img,true));

		//===============image end===============

		leftTable.addCell(topTable);
		//==================左表格上部分end=================



		//===========中间表格begin============
		PdfPTable amongTable = new PdfPTable(1);
	//	amongTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		amongTable.addCell(new Paragraph("Airmail",fontsize8));
		amongTable.addCell(new Paragraph("Attn:Reinhard Scheffler            DE",fontsize10));
		amongTable.addCell(new Paragraph("Adds：Hoiminger Str.4",fontsize8));
		amongTable.addCell(new Paragraph("Havetoft",fontsize8));
		amongTable.addCell(new Paragraph("24873",fontsize8));
		amongTable.addCell(new Paragraph("GERMANY"));

		//将中间部分添加到左表格中
		leftTable.addCell(amongTable);

		//==========中间部分end ============



		//===== 下部分begin=================

		//下部分的表格
		PdfPTable underTable = new PdfPTable(2);
//		underTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

		//=========添加条形码begin===================
		PdfContentByte cd = writer.getDirectContent();

		Barcode128 code128 = new Barcode128();

		code128.setCode("SHA201204A7073");

		Image image128 = code128.createImageWithBarcode(cd, null, null);

		//新建列
		PdfPCell barcodeCell = new PdfPCell(image128);
		barcodeCell.setColspan(2);	//垮2列
		barcodeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	//	barcodeCell.setBorder(PdfPCell.NO_BORDER);

		underTable.addCell(barcodeCell);
		//=========添加条形码end===================

		underTable.addCell(new Paragraph("LYTPY",fontsize4));
		underTable.addCell(new Paragraph("DC065653]  RefNo:SHA201204A7073",fontsize4));
		underTable.addCell(new Paragraph("CS:S0283  ",fontsize4));

		PdfPCell dateCell = new PdfPCell(new Paragraph("07/19 17:34:26",fontsize4));
		dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		dateCell.setBorder(PdfPCell.NO_BORDER);
		
		underTable.addCell(dateCell);

		//将下部分添加到左表格
		leftTable.addCell(underTable);

		//=====下部分end====================

		//****************左表格end****************



		//****************右表格begin****************
		PdfPTable rightTale =new PdfPTable(3);//3列

	//	rightTale.getDefaultCell().setBorder(PdfPCell.NO_BORDER); 

		rightTale.setHorizontalAlignment(Element.ALIGN_LEFT);


		PdfPCell cell1 = new PdfPCell(new Paragraph("配货单  （贴到包装外，邮政必扣件）",fontsize8));
		cell1.setColspan(3);//横跨3列
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	//	cell1.setBorder(PdfPCell.NO_BORDER);

		PdfPCell cell2 = new PdfPCell(new Paragraph("4PX联邮通平邮",fontsize5));
		cell2.setColspan(3);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	//	cell2.setBorder(PdfPCell.NO_BORDER);

		PdfPCell cell3= new PdfPCell(image128);
		cell3.setColspan(3);
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	//	cell3.setBorder(PdfPCell.NO_BORDER);



		rightTale.addCell(cell1);
		rightTale.addCell(cell2);
		rightTale.addCell(cell3);

		//============配货信息begin=========================
		rightTale.addCell(new Paragraph("序号",fontsize6));
		rightTale.addCell(new Paragraph("配货信息",fontsize6));
		rightTale.addCell(new Paragraph("数量",fontsize6));


		rightTale.addCell(new Paragraph("1",fontsize6));
		rightTale.addCell(new Paragraph("Computer Parts",fontsize6));
		rightTale.addCell(new Paragraph("1",fontsize6));

		//============配货信息end=========================


		//****************右表格end****************

		//组装表格
		mainTable.addCell(leftTable);
		mainTable.addCell(rightTale);

		//将表格写入PDF中

		document.add(mainTable);
		//System.out.println("SUCCESS");

		}catch (Exception e) {
		e.printStackTrace();
		}	
		document.close();
		 response.setContentType("application/pdf"); 
		 response.setHeader("Content-disposition","attachment; filename="  
			        +"ok.pdf" );
		 response.setContentLength(ba.size());  
		 try {
			ServletOutputStream out = response.getOutputStream(); 
			 ba.writeTo(out);  
			 out.flush();
			 out.close();
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	//国内
	public     void    biaogepdf2(){
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		Document document = new Document(PageSize.A4,0,0,0,0);
		 ByteArrayOutputStream ba = new ByteArrayOutputStream();
		PdfWriter writer=null;
		try{	

		//支持中文
		BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",false);

		//设置字体
		Font fontsize4 = new Font(bfChinese,4,Font.NORMAL);
		Font fontsize5 = new Font(bfChinese,5,Font.BOLD);
		Font fontsize6 = new Font(bfChinese,6,Font.BOLD);
		Font fontsize7 = new Font(bfChinese,8,Font.BOLD);
		Font fontsize10 = new Font(bfChinese,10,Font.BOLD);
		Font fontsize8 = new Font(bfChinese,24,Font.BOLD);

		Font fontsize9 = new Font(bfChinese,48,Font.BOLD);
		writer = PdfWriter.getInstance(document, ba);

		document.open();

		//主表格
		PdfPTable mainTable = new PdfPTable(2);     
		mainTable.setTotalWidth(450);
		mainTable.setLockedWidth(false);

		//默认无边框
	//	mainTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER); 



		//表格靠左对齐
		mainTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		
		
		
		//1
		PdfPTable leftTable = new PdfPTable(2);
		leftTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER); //放前面，先设置，再赋值
		leftTable.addCell(new Paragraph("POD",fontsize8));
		leftTable.addCell(new Paragraph("ED",fontsize9));
		
		
		mainTable.addCell(new Paragraph("",fontsize10));
		mainTable.addCell(leftTable);
		//1
		//2
		PdfContentByte cd = writer.getDirectContent();

		Barcode128 code128 = new Barcode128();

		code128.setCode("SHA201204A7073");

		Image image128 = code128.createImageWithBarcode(cd, null, null);

		//新建列
		PdfPCell barcodeCell = new PdfPCell(image128);
		//barcodeCell.setColspan(2);	//垮2列
		barcodeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		barcodeCell.setPadding(9);
		//barcodeCell.setBorderWidth(9);
	//	barcodeCell.setBorder(PdfPCell.NO_BORDER);
		
		mainTable.addCell(barcodeCell);
		
		PdfPTable Table3 = new PdfPTable(1);
		Table3.getDefaultCell().setBorder(PdfPCell.NO_BORDER); 
		PdfPCell Cell=new PdfPCell(new Paragraph("电商速配",fontsize10));
		Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		Table3.addCell(Cell);
		Table3.addCell(new Paragraph("目的地",fontsize7));
		Table3.addCell(new Paragraph("代码",fontsize10));
	
	//	Table3.setTotalWidth(136);
		mainTable.addCell(Table3);
		//2
		//3
		mainTable.addCell(new Paragraph("收件",fontsize8));
		PdfPTable Table4 = new PdfPTable(1);
		String sheng="qq";
		String shi="ww";
		String xian="ww";
		String addr="haodif";
		String  addr2="jia";
		String name="ren";
		String phone="123213213213";
		Table4.getDefaultCell().setBorder(PdfPCell.NO_BORDER); 
		Table4.addCell(new Paragraph(sheng+","+shi+","+xian,fontsize10));
		Table4.addCell(new Paragraph("addr",fontsize10));
		Table4.addCell(new Paragraph("addr2",fontsize10));
		Table4.addCell(new Paragraph(name+"  "+phone,fontsize10));
	
		Table4.setTotalWidth(320);
		mainTable.addCell(Table4);
		//3
		//4
	
		PdfPTable Table5 = new PdfPTable(1);
		String  baojia="123123";
		String  baofei="2222";
		String  zhanghao="5555555555";
		String  zl1="5kg";
		String  zl2="1kg";
		Table5.getDefaultCell().setBorder(PdfPCell.NO_BORDER); 
		Table5.addCell(new Paragraph("保价声明价格："+baojia+"保费："+baojia+"包装费：10元",fontsize7));
		Table5.addCell(new Paragraph("付款方式：寄付月结          月结账号"+zhanghao,fontsize7));
		Table5.addCell(new Paragraph("实际重量："+zl1+"计费重量"+zl2,fontsize7));
	
	//	Table4.setTotalWidth(264);
		mainTable.addCell(Table5);
		PdfPTable Table6 = new PdfPTable(1);
		PdfPTable Table7 = new PdfPTable(1);
		Table7.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		String dk="5元";
		Table6.addCell(new Paragraph("代收贷款："+dk,fontsize10));
		Table6.addCell(Table7);
		String  yf ="22元";
		String  fh ="42元";
		Table7.addCell(new Paragraph("运费："+yf,fontsize7));
		Table7.addCell(new Paragraph("费用合计："+fh,fontsize7));
	 
		mainTable.addCell(Table6);
		//4

		//5
		PdfPTable Table8 = new PdfPTable(1);
		Table8.addCell(new Paragraph("寄件："+fh,fontsize8));
		PdfPTable Table9 = new PdfPTable(1);
		String  sheng2="kk";
		String  shi2="ll";
		String  xian2="kl";
		String  dz="klklkl";
		String ren2="";
		String dh="324234234";
		Table9.addCell(new Paragraph(sheng2+","+shi2+","+xian2,fontsize7));
		Table9.addCell(new Paragraph(dz,fontsize7));
		Table9.addCell(new Paragraph(ren2+"   "+dh,fontsize7));
		PdfPTable Table10 = new PdfPTable(1);
		String yjd="324";
		Table10.addCell(new Paragraph("原寄地："+yjd,fontsize7));
		Table10.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table9.addCell(Table10);
		PdfPTable Table11 = new PdfPTable(2);
		Table11.getDefaultCell().setBorder(PdfPCell.NO_BORDER); 
		
		Table11.addCell(Table8);
		Table11.addCell(Table9);
	//	Table11.setTotalWidth(200);
		
		mainTable.addCell(Table11);
		PdfPTable Table12 = new PdfPTable(2);
		PdfPTable Table13 = new PdfPTable(1);
		Table13.getDefaultCell().setBorder(PdfPCell.NO_BORDER); 
		Table13.addCell(new Paragraph("收件员  ",fontsize7));
		Table13.addCell(new Paragraph("寄件日期  ",fontsize7));
		Table13.addCell(new Paragraph("派件员",fontsize7));
		PdfPTable Table14 = new PdfPTable(1);
		Table14.getDefaultCell().setBorder(PdfPCell.NO_BORDER); 
		Table14.addCell(new Paragraph("收方签署  ",fontsize7));
		Table14.addCell(new Paragraph("日期 ：          月                 日 ",fontsize7));
	//	Table13.getDefaultCell().setBorder(PdfPCell.NO_BORDER); 
	//	Table14.getDefaultCell().setBorder(PdfPCell.NO_BORDER); 
		Table12.addCell(Table13);
		Table12.addCell(Table14);
		mainTable.addCell(Table12);
		//5
		//6
		
		mainTable.addCell(new Paragraph("",fontsize8));
		mainTable.addCell(barcodeCell);
		//6
		//7
		mainTable.addCell(Table11);
		
		PdfPTable Table15 = new PdfPTable(2);
	
		Table15.addCell(Table4);
		Table15.addCell(new Paragraph("寄件日期：",fontsize7));
		Table15.addCell(new Paragraph("订单号：",fontsize7));
	//	Table15.setTotalWidth(216);
		mainTable.addCell(Table15);
		
		//7
		//8
		mainTable.addCell(new Paragraph("寄托物",fontsize7));
		mainTable.addCell(new Paragraph("",fontsize8));
		//8
		//9
		//mainTable.setWidths(new float[]{2f,0.4f});//每个单元格占多宽  
		mainTable.getRow(2).setWidths(new float[]{2f,0.4f});
	//	mainTable.getRow(2).setWidths();
		//System.out.println(mainTable.getNumberOfColumns()+"shul");
		//System.out.println(mainTable.getNumberOfColumns()+"shul");
		PdfPCell Cell2 = new PdfPCell(new Paragraph("备注",fontsize7));
	//	Cell2.setColumn(350);
		//  cellMain.setFixedHeight(30);//单元格高30px  
		//  repayTable.setWidths(new float[]{0.18f,0.4f,0.16f,0.26f});//每个单元格占多宽  
		//Cell2.;
		mainTable.addCell(Cell2);
		//System.out.println(Cell2.getWidth());
		PdfPTable Table16 = new PdfPTable(1);
		Table16.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table16.setTotalWidth(80);
		Table16.addCell(new Paragraph("费用合计：",fontsize7));
		Table16.addCell(new Paragraph(fh ,fontsize8));
		//Table16.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
	//	Table16.getDefaultCell().setPadding(PdfPCell.NO_BORDER);
	//	Table16.setTotalWidth(80);
		mainTable.addCell(Table16);
		//9
		

		document.add(mainTable);
		//System.out.println("SUCCESS");

		}catch (Exception e) {
		e.printStackTrace();
		}	
		document.close();
		 response.setContentType("application/pdf"); 
		 response.setHeader("Content-disposition","attachment; filename=ok.pdf" );
		 response.setContentLength(ba.size());  
		 try {
			ServletOutputStream out = response.getOutputStream(); 
			 ba.writeTo(out);  
			 out.flush();
			 out.close();
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	//sf国内的运送单（进口）
	public  void  sfpdf(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		String  orderid=request.getParameter("orderid");
		Order  order=  this.orderManager.get(orderid);
		RouteOrder  routeOrder = this.orderManager.getrouteorder(orderid);//订单轨迹实体
		Rectangle pageSize = new Rectangle(600, 400);
		Rectangle A42 = new RectangleReadOnly(560,580);
		Document document = new Document(pageSize,0,0,0,0);
		document.setPageSize(A42);
		ByteArrayOutputStream ba = new ByteArrayOutputStream();
		PdfWriter writer=null;
		//获取寄件人信息
		String  province=request.getParameter("province");
		String  city=request.getParameter("city");
		String  region=request.getParameter("region");
		String  area=request.getParameter("area");
		String  j_name=request.getParameter("j_name");
		String  j_mobile=request.getParameter("j_mobile");
		String  express_type=request.getParameter("express_type");
		//业务类型 1 标准快递  2 顺丰特惠 3 电商特惠 7 电商速配  28 电商专配
		String expressTypeText = "";
		String is_E = "  ";//是否打印E，只有电商特惠和顺丰特惠打印E
		if(express_type.equals("1")){
			expressTypeText = "标准快递";
		}else if (express_type.equals("2")) {
			expressTypeText = "顺丰特惠";
			is_E = "E";
		}else if (express_type.equals("3")) {
			expressTypeText = "电商特惠";
			is_E = "E";
		}else if (express_type.equals("7")) {
			expressTypeText = "电商速配";
		}else if (express_type.equals("28")) {
			expressTypeText = "电商专配";
		}
		try{	

		//支持中文
		BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",false);

		//设置字体
		Font fontsize4 = new Font(bfChinese,4,Font.NORMAL);
		Font fontsize5 = new Font(bfChinese,5,Font.BOLD);
		Font fontsize6 = new Font(bfChinese,17,Font.BOLD);
		Font fontsize7 = new Font(bfChinese,14,Font.BOLD);
		Font fontsize10 = new Font(bfChinese,10,Font.BOLD);
		Font fontsize8 = new Font(bfChinese,9,Font.BOLD);
		Font fontsize24 = new Font(bfChinese,24,Font.BOLD);
		Font fontsize36 = new Font(bfChinese,36,Font.BOLD);
		Font fontsize9 = new Font(bfChinese,48,Font.BOLD);
		writer = PdfWriter.getInstance(document, ba);

		document.open();

		//主表格
		PdfPTable mainTable = new PdfPTable(20);     
		
		mainTable.setTotalWidth(120);
	
		
	//	mainTable.setLockedWidth(false);

		//默认无边框
		mainTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER); 
		mainTable.getDefaultCell().setPadding(15);


		//表格靠左对齐
		mainTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		
		
		
		//1  第一行
		PdfPTable Table1 = new PdfPTable(1); 
		Table1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table1.addCell(new Paragraph("",fontsize7));
		
		PdfPCell Cell1 = new PdfPCell(Table1);
		Cell1.setColspan(5);
		Cell1.setBorderWidthRight(0);
		mainTable.addCell(Cell1);
		PdfPTable Table2 = new PdfPTable(1); 
		Table2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		//Table2.addCell(new Paragraph("COD",fontsize9));//COD表示代收款业务
		
		PdfPCell Cell2 = new PdfPCell(Table2);
		Cell2.setColspan(5);
		Cell2.setBorderWidthRight(0);
		Cell2.setBorderWidthLeft(0);
		mainTable.addCell(Cell2);
		PdfPTable Table3 = new PdfPTable(1); 
		Table3.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		//Table3.addCell(new Paragraph("ED",fontsize9));//只有电商特惠和顺丰特惠打印E
		Table3.addCell(new Paragraph(is_E,fontsize9));//只有电商特惠和顺丰特惠打印E
		PdfPCell Cell3 = new PdfPCell(Table3);
		Cell3.setColspan(5);
		Cell3.setBorderWidthRight(0);
		Cell3.setBorderWidthLeft(0);
		mainTable.addCell(Cell3);
		PdfPTable Table4 = new PdfPTable(1); 
		Table4.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		
		PdfPCell Cell4 = new PdfPCell(Table4);
		Cell4.setColspan(5);
		Cell4.setBorderWidthLeft(0);
		mainTable.addCell(Cell4);
		//1
		//2   第二行 设置顺丰的运单号
		String  mailno=routeOrder.getMailno();
		PdfContentByte cd = writer.getDirectContent();

		Barcode128 code128 = new Barcode128();
	
		code128.setCode(mailno);
		//code128.setSize(28);//底下数字变大
		Image image128 = code128.createImageWithBarcode(cd, null, null);
		image128.scaleAbsolute(180, 50);
		PdfPCell barcodeCell2 = new PdfPCell(image128);
		barcodeCell2.setColspan(9);	//垮2列
		barcodeCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		barcodeCell2.setPadding(9);
		barcodeCell2.setBorder(0);
		PdfPTable Table5 = new PdfPTable(1); 
		Table5.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table5.addCell(barcodeCell2);
		//Table5.addCell(new Paragraph("234234234324",fontsize7));
		PdfPCell Cell5 = new PdfPCell(Table5);
		Cell5.setColspan(13);
	//	Cell1.setBorderWidthRight(0);
		mainTable.addCell(Cell5);
		PdfPTable Table6 = new PdfPTable(1); 
		Table6.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table6.addCell(new Paragraph(expressTypeText,fontsize7));//1 标准快递  2 顺丰特惠 3 电商特惠 7 电商速配  28 电商专配
//		Table6.addCell(new Paragraph("顺丰特惠",fontsize7));
		Table6.addCell(new Paragraph("目的地",fontsize7));
		Table6.addCell(new Paragraph(routeOrder.getDestcode(),fontsize36));//目的地区域代码。这个代码需要动态获取。
		PdfPCell Cell6 = new PdfPCell(Table6);
		Cell6.setColspan(7);
	//	Cell1.setBorderWidthRight(0);
		mainTable.addCell(Cell6);
	
		//2
		//3第三行
		PdfPTable Table7 = new PdfPTable(1); 
		Table7.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table7.addCell(new Paragraph("\t收",fontsize7));
		Table7.addCell(new Paragraph("\t件",fontsize7));
		//Table7.addCell(new Paragraph("(自取)",fontsize7));暂时不需要自取
		PdfPCell Cell7 = new PdfPCell(Table7);
		Cell7.setColspan(4);
		Cell7.setBorderWidthRight(0);
		mainTable.addCell(Cell7);
		PdfPTable Table8 = new PdfPTable(1); 
		Table8.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table8.addCell(new Paragraph(order.getShipping_area(),fontsize7));//收件人地址信息
		Table8.addCell(new Paragraph(order.getShip_addr(),fontsize7));
		Table8.addCell(new Paragraph(order.getShip_name(),fontsize7));
		Table8.addCell(new Paragraph(order.getShip_mobile(),fontsize7));
		PdfPCell Cell8 = new PdfPCell(Table8);
		Cell8.setColspan(16);
		Cell8.setBorderWidthLeft(0);
		mainTable.addCell(Cell8);
		//3
		//4第四行
		PdfPTable Table9 = new PdfPTable(1); 
		Table9.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table9.addCell(new Paragraph("付款方式：寄付月结",fontsize7));
		Table9.addCell(new Paragraph("月结账号：0202918078",fontsize7));
		
		Table9.addCell(new Paragraph("自寄()",fontsize7));
		PdfPCell Cell9 = new PdfPCell(Table9);
		Cell9.setColspan(13);
	//	Cell1.setBorderWidthRight(0);
		mainTable.addCell(Cell9);
		PdfPTable Table10 = new PdfPTable(1);
		PdfPTable Table11 = new PdfPTable(1);
		Table11.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table10.addCell(new Paragraph("代收贷款：",fontsize7));
		Table11.addCell(new Paragraph("运费：",fontsize7));
		Table11.addCell(new Paragraph("费用合计：",fontsize7));
		Table10.addCell(Table11);
		PdfPCell Cell10 = new PdfPCell(Table10);
		Cell10.setColspan(7);
		mainTable.addCell(Cell10);
		//4
		//5第五行
		PdfPTable Table12 = new PdfPTable(1);
		PdfPTable Table13 = new PdfPTable(1);
		PdfPTable Table14 = new PdfPTable(1);
		Table12.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table13.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table14.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		PdfPCell Cell12 = new PdfPCell(new Paragraph("寄件",fontsize7));
		Table12.addCell(new Paragraph(province+","+city+","+region,fontsize7));
		Table12.addCell(new Paragraph(area,fontsize7));
		Table12.addCell(new Paragraph(j_name+"  "+j_mobile,fontsize7));
		Table12.addCell(new Paragraph("原寄地：\t\t\t"+routeOrder.getOrigincode(),fontsize7));
		Cell12.setColspan(2);
		Cell12.setBorderWidthRight(0);
		mainTable.addCell(Cell12);
		PdfPCell Cell13 = new PdfPCell(Table12);
		Cell13.setColspan(10);
		Cell13.setBorderWidthLeft(0);
		mainTable.addCell(Cell13);
		Table13.addCell(new Paragraph("收件员",fontsize7));
		Table13.addCell(new Paragraph("寄件日期",fontsize7));
		Table13.addCell(new Paragraph("派件员",fontsize7));
		PdfPCell Cell14 = new PdfPCell(Table13);
		Cell14.setColspan(4);
		mainTable.addCell(Cell14);
		Table14.addCell(new Paragraph("收方签署",fontsize7));
		Table14.addCell(new Paragraph("日期："+"  月"+"   日",fontsize7));
		PdfPCell Cell15 = new PdfPCell(Table14);
		Cell15.setColspan(4);
		mainTable.addCell(Cell15);
		//5
		//6
		Properties pro = System.getProperties();
        String osName = pro.getProperty("os.name");//获得当前操作系统的名称
        //System.out.println("当前操作系统的名称:"+osName);
		String  RealPath=request.getSession().getServletContext().getRealPath("/");
		//String ctx2 =this.getServletContext().getRealPath("/"); ;
		String ctx = ThreadContextHolder.getHttpRequest().getContextPath();
		////System.out.println("当前系统图片路径:"+RealPath+"themes\\b2b2cv2\\images\\member\\shunfeng.png");
		////System.out.println(request.getServerName());
		Image imageLogo = null;
		/*if("Linux".equals(osName) || "linux".equals(osName) || "LINUX".equals(osName)){
	          //filePath = session.getServletContext().getRealPath("/")+"/Image"; //linux环境下的路径 
	        imageLogo = Image.getInstance(RealPath+"/themes/b2b2cv2/images/member/shunfeng.png");
	    }else {
	    	imageLogo = Image.getInstance(RealPath+"themes\\b2b2cv2\\images\\member\\shunfeng.png");
		}*/
		imageLogo = Image.getInstance(RealPath+"themes"+FILESEPARATOR+"b2b2cv2"+FILESEPARATOR+"images"+FILESEPARATOR+"member"+FILESEPARATOR+"shunfeng.png");
		imageLogo.scaleAbsolute(100, 50); 
		//PdfPCell Cell16 = new PdfPCell(new Paragraph(" ",fontsize7));
		//Cell16.setColspan(9);
		//mainTable.addCell(Cell16);
		PdfPCell barcodeCell3 = new PdfPCell(imageLogo);
		barcodeCell3.setColspan(9);	//垮2列
		barcodeCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		barcodeCell3.setPadding(9);
		barcodeCell3.setBorder(0);
		mainTable.addCell(barcodeCell3);
		PdfPCell Cell17 = new PdfPCell(Table5);
		PdfPTable Table120 = new PdfPTable(1); 
		Table120.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table120.addCell(barcodeCell3);
		Cell17.setColspan(11);
		mainTable.addCell(Cell17);
		//6
		//7
		
		mainTable.addCell(Cell12);
		PdfPTable Table15 = new PdfPTable(1);
		Table15.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table15.addCell(new Paragraph(province+","+city+","+region,fontsize7));
		Table15.addCell(new Paragraph(area,fontsize7));
		Table15.addCell(new Paragraph(j_name+"  "+j_mobile,fontsize7));
		PdfPCell Cell18 = new PdfPCell(Table15);
		Cell18.setColspan(7);
		Cell18.setBorderWidthLeft(0);
		mainTable.addCell(Cell18);
	
		PdfPTable Table16 = new PdfPTable(1);
		PdfPTable Table17 = new PdfPTable(10);
		PdfPTable Table20 = new PdfPTable(1);
		Table17.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table20.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		PdfPCell Cell26 = new PdfPCell(new Paragraph(order.getShipping_area(),fontsize7));
		PdfPCell Cell27 = new PdfPCell(new Paragraph(order.getShip_addr(),fontsize7));
		PdfPCell Cell28 = new PdfPCell(new Paragraph(order.getShip_name()+"   "+order.getShip_mobile(),fontsize7));
		Cell26.setBorder(0);
		Cell26.setBorderWidthLeft(0);
		Cell27.setBorder(0);
		Cell27.setBorderWidthLeft(0);
		Cell28.setBorder(0);
		Cell28.setBorderWidthLeft(0);
		Cell28.setBorderWidthBottom(0);
		Table20.addCell(Cell26);
		
		Table20.addCell(Cell27);
		Table20.addCell(Cell28);
		PdfPCell Cell23 = new PdfPCell(new Paragraph("收件：",fontsize7));
		Cell23.setColspan(2);
		Cell23.setBorder(0);
		Table17.addCell(Cell23);
		PdfPCell Cell24 = new PdfPCell(Table20);
		Cell24.setBorder(0);
		Cell24.setColspan(8);
		Table17.addCell(Cell24);
		PdfPCell Cell25 = new PdfPCell(new Paragraph("寄件日期：",fontsize7));
		Cell25.setBorder(0);
		Cell25.setColspan(10);
		Table17.addCell(Cell25);
		PdfPCell Cell29 = new PdfPCell(Table17);
		Cell29.setBorder(0);
	//	Table17.addCell(Cell25);
		Table16.addCell(Cell29);
		
		Table16.addCell(new Paragraph("订单号："+order.getSn(),fontsize7));
		PdfPCell Cell19 = new PdfPCell(Table16);
		Cell19.setColspan(13);
		mainTable.addCell(Cell19);
		//7
		//8
		PdfPCell Cell20 = new PdfPCell(new Paragraph("拖寄物",fontsize7));
		Cell20.setColspan(20);
		mainTable.addCell(Cell20);
		//8
		//9
		PdfPCell Cell21 = new PdfPCell(new Paragraph("备注",fontsize7));
		Cell21.setColspan(16);
		mainTable.addCell(Cell21);
		PdfPTable Table18 = new PdfPTable(1);
		Table18.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table18.addCell(new Paragraph("费用合计：",fontsize7));
		Table18.addCell(new Paragraph("  ",fontsize7));
		PdfPCell Cell22 = new PdfPCell(Table18);
		Cell22.setColspan(4);
		mainTable.addCell(Cell22);
		//9
		
		
        document.add(mainTable);
		
		
		//System.out.println("SUCCESS");

		}catch (Exception e) {
		e.printStackTrace();
		}	
		document.close();
		 response.setContentType("application/pdf"); 
		 response.setHeader("Content-disposition","attachment; filename=sf-yundan.pdf" );
		 response.setContentLength(ba.size());  
		 try {
			ServletOutputStream out = response.getOutputStream(); 
			 ba.writeTo(out);  
			 out.flush();
			 out.close();
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//俄罗斯cn22  顺丰出口的一个单子
	public     void    biaogepdf5(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String  j_name = request.getParameter("j_name");
		String  yewu=request.getParameter("yewu");
		String  goodsname=request.getParameter("goodsname");
		String  num=request.getParameter("num");
		
		String  unitPrice=request.getParameter("unitPrice");
		String  orderid=request.getParameter("orderid");
		  Order  order=  this.orderManager.get(orderid);
		  RouteOrder  RouteOrder= this.orderManager.getrouteorder(orderid);
	
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		Document document = new Document(PageSize.A4,0,0,0,0);
		 ByteArrayOutputStream ba = new ByteArrayOutputStream();
		PdfWriter writer=null;
		try{	

		//支持中文
		BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",false);

		//设置字体
		Font fontsize4 = new Font(bfChinese,4,Font.NORMAL);
		Font fontsize5 = new Font(bfChinese,5,Font.BOLD);
		Font fontsize6 = new Font(bfChinese,17,Font.BOLD);
		Font fontsize7 = new Font(bfChinese,14,Font.BOLD);
		Font fontsize10 = new Font(bfChinese,10,Font.BOLD);
		Font fontsize8 = new Font(bfChinese,9,Font.BOLD);

		Font fontsize9 = new Font(bfChinese,48,Font.BOLD);
		writer = PdfWriter.getInstance(document, ba);

		document.open();

		//主表格
		PdfPTable mainTable = new PdfPTable(16);     
		
		mainTable.setTotalWidth(150);
	
		
	//	mainTable.setLockedWidth(false);

		//默认无边框
		mainTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER); 
		mainTable.getDefaultCell().setPadding(15);


		//表格靠左对齐
		mainTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		
		
		
		//1
		PdfPTable Table1 = new PdfPTable(1); 
		Table1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table1.addCell(new Paragraph("CUSTOMS",fontsize7));
		Table1.addCell(new Paragraph("DECLARATION",fontsize7));
		PdfPCell Cell1 = new PdfPCell(Table1);
		Cell1.setColspan(8);
		Cell1.setBorderWidthRight(0);
		mainTable.addCell(Cell1);
		PdfPTable Table2 = new PdfPTable(1); 
		Table2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table2.addCell(new Paragraph("IMay be opened",fontsize7));
		Table2.addCell(new Paragraph("officially",fontsize7));
		PdfPCell Cell2 = new PdfPCell(Table2);
		Cell2.setColspan(4);
		Cell2.setBorderWidthRight(0);
		Cell2.setBorderWidthLeft(0);
		mainTable.addCell(Cell2);
		PdfPTable Table3 = new PdfPTable(1);
		Table3.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table3.addCell(new Paragraph("CN 22",fontsize6));
		PdfPCell Cell3 = new PdfPCell(Table3);
		Cell3.setColspan(4);
		Cell3.setBorderWidthLeft(0);
		mainTable.addCell(Cell3);
		//1
		//2
		PdfPTable Table4 = new PdfPTable(1); 
		Table4.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table4.addCell(new Paragraph("Designated operator",fontsize7));
		PdfPCell Cell4 = new PdfPCell(Table4);
		Cell4.setColspan(12);
		Cell4.setBorderWidthRight(0);
		mainTable.addCell(Cell4);
		PdfPTable Table5 = new PdfPTable(1); 
		Table5.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table5.addCell(new Paragraph("Important!",fontsize6));
		Table5.addCell(new Paragraph("See instructions",fontsize7));
		Table5.addCell(new Paragraph("on the back",fontsize7));
		PdfPCell Cell5= new PdfPCell(Table5);
		Cell5.setColspan(4);
		Cell5.setBorderWidthLeft(0);
		mainTable.addCell(Cell5);
		//2
		//3
		PdfPTable Table6 = new PdfPTable(1);
		Table6.addCell(new Paragraph("",fontsize6));
		PdfPCell Cell24= new PdfPCell(new Paragraph("",fontsize6));
		Cell24.setPaddingTop(3);
		Table6.addCell(Cell24);
		PdfPCell Cell6= new PdfPCell(Table6);
		mainTable.addCell(Cell6);
		PdfPTable Table7 = new PdfPTable(1);
		Table7.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table7.addCell(new Paragraph("Gift",fontsize7));
		Table7.addCell(new Paragraph("Document",fontsize7));
		PdfPCell Cell7= new PdfPCell(Table7);
		Cell7.setColspan(4);
		mainTable.addCell(Cell7);
		PdfPTable Table8 = new PdfPTable(1);
		Table8.addCell(new Paragraph("",fontsize6));
		Table8.addCell(new Paragraph("x",fontsize7));
		PdfPCell Cell8= new PdfPCell(Table8);
		mainTable.addCell(Cell8);
		PdfPTable Table9 = new PdfPTable(1);
		Table9.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table9.addCell(new Paragraph("Commercial sample",fontsize7));
		Table9.addCell(new Paragraph("Other                      Tick one or more boxes",fontsize7));
		PdfPCell Cell9= new PdfPCell(Table9);
		Cell9.setColspan(10);
		mainTable.addCell(Cell9);
		//3
		//4
		PdfPTable Table11 = new PdfPTable(1);
		Table11.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		//应该加循环
		Table11.addCell(new Paragraph("Quantity and detailed description",fontsize7));
		Table11.addCell(new Paragraph("of contents (1)",fontsize7));
		//应该加循环
		PdfPCell Cell13= new PdfPCell(Table11);
		Cell13.setColspan(10);
		mainTable.addCell(Cell13);
		PdfPTable Table12 = new PdfPTable(1);
		Table12.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		//应该加循环
		Table12.addCell(new Paragraph("Weight",fontsize7));
		Table12.addCell(new Paragraph("(in kg)",fontsize7));
		//应该加循环
		PdfPCell Cell14= new PdfPCell(Table12);
		Cell14.setColspan(3);
		mainTable.addCell(Cell14);
		PdfPCell Cell15= new PdfPCell(new Paragraph("Value(3)",fontsize7));
		Cell15.setColspan(3);
		mainTable.addCell(Cell15);
		//4
		//5
		String  goodsname2=goodsname;
		//String    num="1";
		PdfPTable Table10 = new PdfPTable(1);
		Table10.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		//应该加循环
		Table10.addCell(new Paragraph(goodsname2+" X "+num,fontsize7));
		//应该加循环
		PdfPCell Cell10= new PdfPCell(Table10);
		Cell10.setColspan(10);
		mainTable.addCell(Cell10);
		String  zl=order.getWeight().toString();
		PdfPCell Cell11= new PdfPCell(new Paragraph(zl,fontsize7));
		
		Cell11.setColspan(3);
		mainTable.addCell(Cell11);
	//	String  val=unitPrice;
		PdfPCell Cell12= new PdfPCell(new Paragraph(unitPrice,fontsize7));
		
		Cell12.setColspan(3);
		mainTable.addCell(Cell12);
		//5
		//6
		PdfPTable Table13 = new PdfPTable(1);
		PdfPTable Table14 = new PdfPTable(1);
		Table14.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table14.addCell(new Paragraph("For commercial items only",fontsize7));
		Table14.addCell(new Paragraph("If known, HS HS tariff number (4)",fontsize7));
		Table14.addCell(new Paragraph("and country of origin of goods (5)",fontsize7));
		Table13.addCell(Table14);
		
		Table13.addCell(new Paragraph("CN",fontsize7));
		PdfPCell Cell16= new PdfPCell(Table13);
		Cell16.setColspan(10);
		mainTable.addCell(Cell16);
		String  totalw=order.getWeight().toString();
		PdfPTable Table15 = new PdfPTable(1);
		Table15.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table15.addCell(new Paragraph("Total",fontsize7));
		Table15.addCell(new Paragraph("weight",fontsize7));
		Table15.addCell(new Paragraph("(in kgX6)",fontsize7));
		Table15.addCell(new Paragraph(totalw,fontsize7));
		PdfPCell Cell17= new PdfPCell(Table15);
		Cell17.setColspan(3);
		mainTable.addCell(Cell17);
		Double  totaval=Double.parseDouble(unitPrice)*Double.parseDouble(num);
		PdfPTable Table16 = new PdfPTable(1);
		Table16.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table16.addCell(new Paragraph("Total",fontsize7));
		Table16.addCell(new Paragraph("values(7)",fontsize7));
		Table16.addCell(new Paragraph("",fontsize7));
		Table16.addCell(new Paragraph(totaval.toString(),fontsize7));
		PdfPCell Cell18= new PdfPCell(Table16);
		Cell18.setColspan(3);
		mainTable.addCell(Cell18);
		//6
		//7
		PdfPTable Table17 = new PdfPTable(1);
		Table17.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		String  jijianname ="Mr.TradeEase";
		Table17.addCell(new Paragraph("I,the undersigned,whose name and address are given on the item,",fontsize7));
		Table17.addCell(new Paragraph("certify that the partioulars given in this declaration are correct and",fontsize7));
		Table17.addCell(new Paragraph("that this item does not contain any dangerous article or articles prohibited",fontsize7));
		Table17.addCell(new Paragraph("by legislation or by postal or customs regulations",fontsize7));
		Table17.addCell(new Paragraph("Date and sender's signature \t\t\t\t\t\t\t\t\t"+j_name,fontsize7));

		PdfPCell Cell19= new PdfPCell(Table17);
		Cell19.setColspan(16);
		mainTable.addCell(Cell19);
		//7
		//8
		PdfPTable Table19 = new PdfPTable(1);
		Table17.addCell(new Paragraph("",fontsize7));

		PdfPCell Cell23 = new PdfPCell(Table19);
		Cell23.setColspan(16);
		Cell23.setBorderWidthLeft(0);
		Cell23.setBorderWidthRight(0);
		Cell23.setBorderWidthBottom(5);
		mainTable.addCell(Cell23);
		//8
		//9
		PdfContentByte cd = writer.getDirectContent();

		Barcode128 code128 = new Barcode128();
		String   agent_mailno=RouteOrder.getMailno();
		code128.setCode(agent_mailno);

		Image image128 = code128.createImageWithBarcode(cd, null, null);
		image128.scaleAbsolute(180, 50);

		//新建列
		PdfPCell barcodeCell2 = new PdfPCell(image128);
		barcodeCell2.setColspan(9);	//垮2列
		barcodeCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		barcodeCell2.setPadding(9);
		barcodeCell2.setBorder(0);
		mainTable.addCell(barcodeCell2);
		PdfPCell Cell20= new PdfPCell(new Paragraph("RU",fontsize7));
		Cell20.setColspan(2);
		Cell20.setBorder(0);
		mainTable.addCell(Cell20);
		//String  yewu="p";
		PdfPCell Cell21= new PdfPCell(new Paragraph(yewu,fontsize7));
		Cell21.setBorder(0);
		mainTable.addCell(Cell21);
		PdfPTable Table18 = new PdfPTable(1);
		Table18.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table18.addCell(new Paragraph("Electric",fontsize7));
		Table18.addCell(new Paragraph("N",fontsize6));
		PdfPCell Cell22 = new PdfPCell(Table18);
		Cell22.setColspan(4);	
		Cell22.setPadding(5);
		mainTable.addCell(Cell22);
		//9
		
		document.add(mainTable);
		
		
		//System.out.println("SUCCESS");

		}catch (Exception e) {
		e.printStackTrace();
		}	
		document.close();
		 response.setContentType("application/pdf"); 
		 response.setHeader("Content-disposition","attachment; filename=ok.pdf" );
		 response.setContentLength(ba.size());  
		 try {
			ServletOutputStream out = response.getOutputStream(); 
			 ba.writeTo(out);  
			 out.flush();
			 out.close();
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	//sf ---py-gh顺丰出口面单
	public    void    biaogepdf4(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String  yewu = request.getParameter("yewu");
		String  j_name = request.getParameter("j_name");
		String  orderid = request.getParameter("orderid");
		Order   order = this.orderManager.get(orderid);
		RouteOrder  RouteOrder = this.orderManager.getrouteorder(orderid);//订单轨迹实体
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		Document document = new Document(PageSize.A4,0,0,0,0);
		ByteArrayOutputStream ba = new ByteArrayOutputStream();
		PdfWriter writer=null;
		try{	

		//支持中文
		BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",false);

		//设置字体
		Font fontsize4 = new Font(bfChinese,4,Font.NORMAL);
		Font fontsize5 = new Font(bfChinese,5,Font.BOLD);
		Font fontsize6 = new Font(bfChinese,17,Font.BOLD);
		Font fontsize7 = new Font(bfChinese,14,Font.BOLD);
		Font fontsize10 = new Font(bfChinese,10,Font.BOLD);
		Font fontsize8 = new Font(bfChinese,9,Font.BOLD);

		Font fontsize9 = new Font(bfChinese,48,Font.BOLD);
		writer = PdfWriter.getInstance(document, ba);

		document.open();

		//主表格
		PdfPTable mainTable = new PdfPTable(3);     
		
		mainTable.setTotalWidth(150);
	
		
	//	mainTable.setLockedWidth(false);

		//默认无边框
		mainTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER); 
		mainTable.getDefaultCell().setPadding(15);


		//表格靠左对齐
		mainTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		
		
		
		//1
		PdfPTable Table1 = new PdfPTable(1);
		PdfPTable Table2 = new PdfPTable(1);
		PdfPTable Table3 = new PdfPTable(1);
		PdfPTable Table4 = new PdfPTable(1);
		PdfPTable Table5 = new PdfPTable(1);
		PdfPTable Table6 = new PdfPTable(1);
		
		
		Table3.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table5.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table6.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table3.setHorizontalAlignment(Element.ALIGN_MIDDLE);
		Table3.addCell(new Paragraph("POSTIMAKS",fontsize8));
		Table3.addCell(new Paragraph("TASUTUD  TAXE",fontsize8));
		Table3.addCell(new Paragraph("PERCUE ESTONIE",fontsize8));
		Table3.addCell(new Paragraph("No.199",fontsize8));
		
		Table4.addCell(Table3);
		String  jijianren="TradeEase";
		Table5.addCell(new Paragraph("from："+j_name,fontsize8));
		Table6.addCell(new Paragraph("Forward SF-EXPRESS",fontsize8));
		Table6.addCell(new Paragraph("P.O.Box 7023,14002 Tallinn,",fontsize8));
		Table6.addCell(new Paragraph("Estonia",fontsize8));
		
	//	Table1.getDefaultCell().setBorder(PdfPCell.NO_BORDER); //放前面，先设置，再赋值
		Table1.addCell(Table2);
		Table2.addCell(Table5);
		Table2.addCell(Table6);
		PdfPCell barcodeCell = new PdfPCell(Table1);
		barcodeCell.setColspan(2);
		barcodeCell.setPadding(10);
		barcodeCell.setBorder(0);
		mainTable.addCell(barcodeCell);
		PdfPCell Cell7 = new PdfPCell(Table4);
		Cell7.setPadding(10);
		Cell7.setBorder(0);
		mainTable.addCell(Cell7);
		//1
		//2
		PdfPTable Table7 = new PdfPTable(1);
		Table7.setHorizontalAlignment(Element.ALIGN_MIDDLE);
		Table7.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		PdfPCell Cell3 = new PdfPCell(new Paragraph("PRIORITY",fontsize7));
		//Cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		Cell3.setPadding(30);
		Table7.addCell(Cell3);
		
		PdfPCell Cell9 = new PdfPCell(Table7);
		Cell9.setPadding(18);
		Cell9.setBorder(0);
		mainTable.addCell(Cell9);
	
		String  shoujianren =order.getShip_name();
		String   shoudizhi=order.getShipping_area()+order.getShip_addr(); 
		String   ZIP=order.getShip_zip();
		String  Tel=order.getShip_mobile();
		//String   RU="";
		
		PdfPTable Table8 = new PdfPTable(1);
		PdfPTable Table9 = new PdfPTable(1);
		PdfPCell Cell4 = new PdfPCell(Table8);
		Table9.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		
		Table8.addCell(Table9);
		Table9.addCell(new Paragraph("To："+shoujianren,fontsize8));
		Table9.addCell(new Paragraph(shoudizhi,fontsize8));
		Table9.addCell(new Paragraph("ZIP："+ZIP,fontsize8));
		Table9.addCell(new Paragraph("Tel："+Tel,fontsize8));
		Table9.addCell(new Paragraph("RU",fontsize8));
		Cell4.setColspan(2);
		Cell4.setPadding(15);
		Cell4.setBorder(0);
		mainTable.addCell(Cell4);
		//2
		//3
		PdfContentByte cd = writer.getDirectContent();

		Barcode128 code128 = new Barcode128();
		
		String   agent_mailno=RouteOrder.getAgent_mailno();
		PdfPCell barcodeCell2=new PdfPCell();
		if (!StringUtil.isEmpty(agent_mailno)) {
			code128.setCode(agent_mailno);

			Image image128 = code128.createImageWithBarcode(cd, null, null);
			image128.scaleAbsolute(180, 50);
			//新建列
			 barcodeCell2 = new PdfPCell(image128);
			barcodeCell2.setColspan(3);	//垮2列
			barcodeCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			barcodeCell2.setPadding(9);
			barcodeCell2.setBorder(0);
			
		}else {
			 barcodeCell2 = new PdfPCell(new Paragraph("",fontsize7));
			 barcodeCell2.setPadding(9);
			 barcodeCell2.setColspan(3);	
			 barcodeCell2.setBorder(0);
		}
		
		mainTable.addCell(barcodeCell2);
		
		//3
		//4运单号
		String  mailno=RouteOrder.getMailno();
		Barcode128 code128_2 = new Barcode128();
		code128_2.setCode(mailno);
		Image image128_2 = code128_2.createImageWithBarcode(cd, null, null);
		image128_2.scaleAbsolute(180, 50);
		PdfPCell Cell5 = new PdfPCell(image128_2);
		Cell5.setColspan(2);	//垮2列
		Cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		//Cell5.setPadding(9);
		Cell5.setBorder(0);
		
		mainTable.addCell(Cell5);
		String  yewu2="";
		if(yewu.equals("P")){//平邮
			yewu2="5-EE-RU-"+yewu;
		}else {//挂号
			yewu2="6-EE-RU-"+yewu;
		}
		
		
		PdfPCell Cell6 = new PdfPCell(new Paragraph(yewu2,fontsize7));
		Cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		Cell6.setBorder(0);
		mainTable.addCell(Cell6);
		
		//4
	
		
		document.add(mainTable);
		
		
		//System.out.println("SUCCESS");

		}catch (Exception e) {
		e.printStackTrace();
		}	
		document.close();
		 response.setContentType("application/pdf"); 
		 response.setHeader("Content-disposition","attachment; filename=waybill.pdf" );
		 response.setContentLength(ba.size());  
		 try {
			ServletOutputStream out = response.getOutputStream(); 
			 ba.writeTo(out);  
			 out.flush();
			 out.close();
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	public     void    biaogepdf3(){
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		Document document = new Document(PageSize.A4,0,0,0,0);
		 ByteArrayOutputStream ba = new ByteArrayOutputStream();
		PdfWriter writer=null;
		try{	

		//支持中文
		BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",false);

		//设置字体
		Font fontsize4 = new Font(bfChinese,4,Font.NORMAL);
		Font fontsize5 = new Font(bfChinese,5,Font.BOLD);
		Font fontsize6 = new Font(bfChinese,17,Font.BOLD);
		Font fontsize7 = new Font(bfChinese,14,Font.BOLD);
		Font fontsize10 = new Font(bfChinese,10,Font.BOLD);
		Font fontsize8 = new Font(bfChinese,9,Font.BOLD);

		Font fontsize9 = new Font(bfChinese,48,Font.BOLD);
		writer = PdfWriter.getInstance(document, ba);

		document.open();

		//主表格
		PdfPTable mainTable = new PdfPTable(3);     
		mainTable.setTotalWidth(150);
		
	//	mainTable.setLockedWidth(false);

		//默认无边框
		mainTable.getDefaultCell().setBorder(20);
	//	mainTable.getDefaultCell().setExtraParagraphSpace(12);
	//	mainTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER); 



		//表格靠左对齐
		mainTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		
		
		
		//1
		PdfPTable Table1 = new PdfPTable(1);
		PdfPTable Table2 = new PdfPTable(1);
		PdfPTable Table3 = new PdfPTable(1);
		PdfPTable Table4 = new PdfPTable(1);
		PdfPTable Table5 = new PdfPTable(1);
		PdfPTable Table6 = new PdfPTable(1);
		Table3.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table5.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table6.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table3.setHorizontalAlignment(Element.ALIGN_MIDDLE);
		Table3.addCell(new Paragraph("POSTIMAKS",fontsize8));
		Table3.addCell(new Paragraph("TASUTUD  TAXE",fontsize8));
		Table3.addCell(new Paragraph("PERCUE ESTONIE",fontsize8));
		Table3.addCell(new Paragraph("No.199",fontsize8));
		
		Table4.addCell(Table3);
		String  jijianren="寄件人";
		Table5.addCell(new Paragraph("from："+jijianren,fontsize8));
		Table6.addCell(new Paragraph("Forward SF-EXPRESS",fontsize8));
		Table6.addCell(new Paragraph("P.O.Box 7023,14002 Tallinn,",fontsize8));
		Table6.addCell(new Paragraph("Estonia",fontsize8));
		Table1.getDefaultCell().setBorder(PdfPCell.NO_BORDER); //放前面，先设置，再赋值
		Table1.addCell(Table2);
		Table2.addCell(Table5);
		
		Table2.addCell(Table6);
		PdfPCell barcodeCell = new PdfPCell(Table1);
		barcodeCell.setColspan(2);
		
		mainTable.addCell(barcodeCell);
		
	
		mainTable.addCell(Table4);
		//1
		//2
		PdfPTable Table7 = new PdfPTable(1);
		Table7.setHorizontalAlignment(Element.ALIGN_MIDDLE);
		PdfPCell Cell3 = new PdfPCell(new Paragraph("PRIORITY",fontsize7));
		Cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		Cell3.setBorder(10);
		//Cell3.setPaddingLeft(12);
		//Cell3.setBorderWidthRight(12);
	//	Cell3.setBorderColor(Color.white);
	//	//System.out.println(Cell3.getBorderColorRight()+"颜色");
		Table7.addCell(Cell3);
		
		mainTable.addCell(Table7);
		//mainTable.set
		String  shoujianren ="收件人";
		String   shoudizhi="收件人地址"; 
		String   ZIP="123123";
		String  Tel="12321313";
		//String   RU="";
		
		PdfPTable Table8 = new PdfPTable(1);
		PdfPTable Table9 = new PdfPTable(1);
		PdfPCell Cell4 = new PdfPCell(Table8);
		Table9.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		Table8.addCell(Table9);
		Table9.addCell(new Paragraph("To："+shoujianren,fontsize8));
		Table9.addCell(new Paragraph(shoudizhi,fontsize8));
		Table9.addCell(new Paragraph("ZIP："+ZIP,fontsize8));
		Table9.addCell(new Paragraph("Tel："+Tel,fontsize8));
		Table9.addCell(new Paragraph("RU",fontsize8));
		Cell4.setColspan(2);
	//	Cell4.setBorderWidthLeft(12);
		mainTable.addCell(Cell4);
		//2
		//3
		PdfContentByte cd = writer.getDirectContent();

		Barcode128 code128 = new Barcode128();
		String   agent_mailno="RR24fd342345";
		code128.setCode(agent_mailno);

		Image image128 = code128.createImageWithBarcode(cd, null, null);

		//新建列
		PdfPCell barcodeCell2 = new PdfPCell(image128);
		barcodeCell2.setColspan(3);	//垮2列
		barcodeCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		barcodeCell2.setPadding(9);
		barcodeCell2.setBorder(0);
		mainTable.addCell(barcodeCell2);
		
		//3
		//4
		String  mailno="R234234234";
		Barcode128 code128_2 = new Barcode128();
		code128_2.setCode(mailno);
		Image image128_2 = code128.createImageWithBarcode(cd, null, null);
		PdfPCell Cell5 = new PdfPCell(image128_2);
		Cell5.setColspan(2);	//垮2列
		Cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		Cell5.setPadding(9);
		Cell5.setBorder(0);
		mainTable.addCell(Cell5);
		PdfPCell Cell6 = new PdfPCell(new Paragraph("6-EE-RU-G",fontsize7));
		Cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		Cell6.setBorder(0);
		mainTable.addCell(Cell6);
		
		//4
	

		document.add(mainTable);
		//System.out.println("SUCCESS");

		}catch (Exception e) {
		e.printStackTrace();
		}	
		document.close();
		 response.setContentType("application/pdf"); 
		 response.setHeader("Content-disposition","attachment; filename=ok.pdf" );
		 response.setContentLength(ba.size());  
		 try {
			ServletOutputStream out = response.getOutputStream(); 
			 ba.writeTo(out);  
			 out.flush();
			 out.close();
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	public IProductManager getProductManager() {
		return productManager;
	}
	public void setProductManager(IProductManager productManager) {
		this.productManager = productManager;
	}
	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}
	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}
	public Integer getProductid() {
		return productid;
	}
	public void setProductid(Integer productid) {
		this.productid = productid;
	}
	public Integer getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}
	
}
