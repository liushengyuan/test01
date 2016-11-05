package com.enation.app.tradeease.core.action.api.student;


import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.tradeease.core.model.sms.SmsMobile;
import com.enation.app.tradeease.core.model.student.Student;
import com.enation.app.tradeease.core.service.smsmobile.IsmsMobileManager;
import com.enation.app.tradeease.core.service.student.IstudentManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;

@SuppressWarnings("serial")
@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("student")
public class StudentApiAction extends WWAction {
	private IstudentManager studentManager;
	private int id;
	private IsmsMobileManager smsMobileManager;
	private  HSSFWorkbook wb = new HSSFWorkbook();  
	  
	private  HSSFSheet sheet = wb.createSheet();  
	/**
	 * 添加学生信息
	 * @return
	 */
	public String addstudent(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String name = request.getParameter("name");
		String age = request.getParameter("age");
		String score = request.getParameter("score");
		Student student = new Student();
		student.setName(name);
		student.setAge(Integer.parseInt(age));
		student.setScore(Double.parseDouble(score));
		this.smsMobileManager.sendMobile( "13522163282", "测试信息，请勿回复！");
		try {
			this.studentManager.addStudent(student);
			this.showSuccessJson("添加成功！");
		} catch (Exception e) {
			
			this.showErrorJson("添加失败！");
			this.logger.error("添加出错", e);
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 修改学生信息
	 * @return
	 */
	public String updatestudent(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		
		String id = request.getParameter("id");
		Student stud = new Student();
		String name = request.getParameter("name");
		String age = request.getParameter("age");
		String score = request.getParameter("score");
		stud.setName(name);
		stud.setAge(Integer.parseInt(age));
		stud.setId(Integer.parseInt(id));
		stud.setScore(Double.parseDouble(score));
		try {
			this.studentManager.updateStud(stud);
			this.showSuccessJson("更新成功！");
		} catch (Exception e) {
			this.showErrorJson("更新失败!");
			this.logger.error("更新出错", e);
		}
		return this.JSON_MESSAGE;
	}
	
	public String deletestudent(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String ids = request.getParameter("ids");
		try {
			this.studentManager.deletStud(ids);
			this.showSuccessJson("删除成功!");
		} catch (Exception e) {
			this.showErrorJson("删除失败!");
			this.logger.error("删除出错", e);
		}
		return this.JSON_MESSAGE;
	}

	@SuppressWarnings("deprecation")
	public void export(){
		  
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String ids = request.getParameter("ids");
		 // 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("学生信息表");  
        
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("序号");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 1);  
        cell.setCellValue("姓名");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("年龄");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 3);  
        cell.setCellValue("分数");  
        cell.setCellStyle(style);  
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，  
        List<Student> studeList=this.studentManager.queryStudentWihtIds(ids);
      
        	for(int i=0;i<studeList.size();i++){

        		 row = sheet.createRow((int) i + 1);  
        		 //System.out.println(studeList.get(i).getName());
                 Student stu = (Student)studeList.get(i);  
           
           
			try {
				 	row.createCell((short) 0).setCellValue((Integer)stu.getId());  
		            row.createCell((short) 1).setCellValue( stu.getName()); 
		            row.createCell((short) 2).setCellValue((Integer)stu.getAge()); 
		            row.createCell((short) 3).setCellValue( (double)stu.getScore()); 
			} catch (Exception e) {
				 e.printStackTrace();
				 this.showErrorJson("导出数据失败" + e.getMessage());
					this.logger.error("导出数据失败", e);
			}  
        	}
         
        // 第六步，将文件存到指定位置  
        try{   
            FileOutputStream fout = new FileOutputStream("d:student.xls"); 
            wb.write(fout);
            fout.close(); 
        }catch (Exception e){  
        	this.showErrorJson("导出失败" + e.getMessage());
			this.logger.error("导出失败", e);
          
        }  

	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public IstudentManager getStudentManager() {
		return studentManager;
	}
	public void setStudentManager(IstudentManager studentManager) {
		this.studentManager = studentManager;
	}
	public IsmsMobileManager getSmsMobileManager() {
		return smsMobileManager;
	}
	public void setSmsMobileManager(IsmsMobileManager smsMobileManager) {
		this.smsMobileManager = smsMobileManager;
	}

	

}
