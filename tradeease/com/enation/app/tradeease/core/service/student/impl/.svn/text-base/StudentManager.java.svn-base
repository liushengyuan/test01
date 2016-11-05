package com.enation.app.tradeease.core.service.student.impl;

import java.util.List;
import java.util.Map;

import com.enation.app.tradeease.core.model.student.Student;
import com.enation.app.tradeease.core.service.student.IstudentManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;

@SuppressWarnings("rawtypes")
public class StudentManager extends BaseSupport implements IstudentManager {

	@Override
	public List<Student> qureryList(String name) {
		StringBuffer sql = new StringBuffer();
		if(name==null){
			sql.append(" SELECT * from es_student ");
		}else{
			sql.append(" SELECT * from es_student where name like '%" +name+ "%'");
		}
		List<Student> studentList = this.baseDaoSupport.queryForList(sql.toString());
		return studentList;
	}
	@Override
	public void addStudent(Student student){
		this.baseDaoSupport.insert("es_student", student);
	}
	/**
	 * 获取此表中最大的一个id加1
	 * @return
	 */
	@Override
	public int studentId(){
		int studentid = this.baseDaoSupport.getLastId("es_student");
		return studentid+1;
	}
	/**
	 * 根据id查找对象
	 */
	@Override
	public List<Student> queryStudent(String id){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from es_student where id="+id);
		List<Student> studList = this.baseDaoSupport.queryForList(sql.toString());
		if(studList.isEmpty()){
			return null;
		}else{
			
			return studList;
		}
		
	}
	/**
	 * 修改学生信息
	 */
	public void updateStud(Student stud){
		this.baseDaoSupport.update("es_student", stud, " id="+stud.getId());
	}
	/**
	 * 删除学生信息
	 */
	public void deletStud(String ids){
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from es_student where id in (  ");
		sql.append( ids +" ) ");
		this.baseDaoSupport.execute(sql.toString());
	}
	
	public Page qureryPage(int pageNo, int pageSize, Map map){
		StringBuffer sql = new StringBuffer();
		String name = (String) map.get("name");
		
		if(name==null){
			sql.append(" SELECT * from es_student ");
		}else{
			sql.append(" SELECT * from es_student where name like '%" +name+ "%'");
		}
		Page webpage = this.daoSupport.queryForPage(sql.toString(), pageNo, pageSize);
		return webpage;
	}
	@Override
	public List<Student> queryStudentWihtIds(String ids) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from es_student where id in ( " +ids + " ) ");
		List<Student> studList = this.baseDaoSupport.queryForList(sql.toString());
		if(studList.isEmpty()){
			return null;
		}else{
			return studList;
		}
	}
}
