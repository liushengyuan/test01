package com.enation.app.tradeease.core.service.student;

import java.util.List;
import java.util.Map;

import com.enation.app.tradeease.core.model.student.Student;
import com.enation.framework.database.Page;


public interface IstudentManager {
	public List<Student> qureryList(String name);

	public void addStudent(Student student);

	public int studentId();

	public List<Student> queryStudent(String string);

	public void updateStud(Student stud);

	public void deletStud(String ids);

	@SuppressWarnings("rawtypes")
	public Page qureryPage(int page, int pageSize, Map map);

	public List<Student> queryStudentWihtIds(String ids);
}
