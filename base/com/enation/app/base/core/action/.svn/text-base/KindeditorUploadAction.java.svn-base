package com.enation.app.base.core.action;

import com.enation.framework.action.WWAction;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * kindeditor 文件上传
 */
public class KindeditorUploadAction extends WWAction {

	public String execute() throws IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		PrintWriter out = response.getWriter();

		String savePath = request.getRealPath("/")+"attached/";
		File test = new File(savePath);
		if(!test.exists()){
			test.mkdirs();
		}
		//文件保存目录URL
		String saveUrl  = request.getContextPath() + "/attached/";

		//定义允许上传的文件扩展名
		Map<String , String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");

		//最大文件大小
		long maxSize = 1000000;

		response.setContentType("text/html; charset=UTF-8");

		if(!ServletFileUpload.isMultipartContent(request)){
			this.showErrorJson("请选择文件");
			out.print(this.getJson());
			return "err";
		}
		//检查目录
		File uploadDir = new File(savePath);
		if(!uploadDir.isDirectory()){
			this.showErrorJson("上传目录不存在");
			out.print(this.getJson());
			return "err";
		}
		//检查目录写权限
		if(!uploadDir.canWrite()){
			this.showErrorJson("上传目录没有写权限");
			out.print(this.getJson());
			return "err";
		}

		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		if(!extMap.containsKey(dirName)){
			this.showErrorJson("目录名不正确");
			out.print(this.getJson());
			return "err";
		}
		//创建文件夹
		savePath += dirName + "/";
		saveUrl += dirName + "/";
		File saveDirFile = new File(savePath);
		if (!saveDirFile.exists()) {
			saveDirFile.mkdirs();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		savePath += ymd + "/";
		saveUrl += ymd + "/";
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper)request;
		String fileName = wrapper.getFileNames("imgFile")[0];
		File file = wrapper.getFiles("imgFile")[0];

		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		if(file.length()>maxSize){
			this.showErrorJson("上传文件大小超出限制");
			return "err";
		}
		if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)){

			this.showErrorJson("上传文件扩展名是不允许的扩展名。\n" + "只允许\" + extMap.get(dirName) + \"格式。");
			out.print(this.getJson());
			return "err";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;

		saveUrl += newFileName;

		FileOutputStream fos = new FileOutputStream(savePath + newFileName);
		byte[] buffer = new byte[1024];
		InputStream in = new FileInputStream(file);

		try {
			int num = 0;
			while ((num = in.read(buffer)) > 0) {
				fos.write(buffer, 0, num);
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			try{
				if(in != null)
					in.close();
				if(fos != null)
					fos.close();
			}catch(IOException e){}
		}
		//System.out.println(saveUrl);
		out.println("{\"error\":0,\"url\":\""+saveUrl+"\"}");
		return null;
	}
}
