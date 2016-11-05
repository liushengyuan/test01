<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<#assign studentList=newTag("exportStudTag")>
<#assign studList=studentList("{'ids':'${ids}'}")>
<tables>
<table name="学生列表">
	<tr>
		<th>序号</th>
		<th>学生姓名</th>
		<th>学生年龄</th>
		<th>学生成绩</th>
	</tr>
	<tbody id="data">
	<#list studList as stud>
		<tr id="" >
			<td >${stud_index+1}</td>
			<td>${stud.name}</td>
			<td>${stud.age}</td>
			<td >${stud.score}</td>
		</tr>
	</#list>
	</tbody>
</table>
</tables>