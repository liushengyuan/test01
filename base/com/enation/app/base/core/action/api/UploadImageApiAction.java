package com.enation.app.base.core.action.api;

import java.io.File;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/base")
@Action("upload-image")
@Results({
})
public class UploadImageApiAction extends WWAction {
	HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
	Locale locale = (Locale) session.getAttribute("locale");
	String language = locale.getLanguage();
		private File image ;
	 	private String imageFileName;
	 	private String subFolder;
	 	
	 	public String execute(){
	 		String shangchuan=this.getText("uploadImage.shangchuan");
	 		try{
	 			String fsImgPath =UploadUtil.upload(image, imageFileName,  subFolder);
	 			String path="{\"img\":\""+UploadUtil.replacePath(fsImgPath)+"\",\"fsimg\":\""+fsImgPath+"\"}";
	 			this.json=path;
	 		}catch(Throwable e){
	 				this.showErrorJson(shangchuan+e.getLocalizedMessage());
	 		}
	 		return this.JSON_MESSAGE;
	 	}
		public File getImage() {
			return image;
		}
		public void setImage(File image) {
			this.image = image;
		}
		public String getImageFileName() {
			return imageFileName;
		}
		public void setImageFileName(String imageFileName) {
			this.imageFileName = imageFileName;
		}
		public String getSubFolder() {
			return subFolder;
		}
		public void setSubFolder(String subFolder) {
			this.subFolder = subFolder;
		}
}
