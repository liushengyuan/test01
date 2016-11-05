var totalAmount;
var weight;
var trsSize;
function reset() {
	document.getElementById("weight").value = "";
	document.getElementById("length").value = "";
	document.getElementById("size").value = "";
	document.getElementById("hight").value = "";
}
/**
 * 计算费用
 * 
 * @param obj
 * @returns {Boolean}
 */
function editFee() {
	document.getElementById("acountFee").style.display = "";
	document.getElementById("result").style.display = "none";
}
/**
 * next.html请求提交订单
 */
function next() {
	var productCode = document.getElementById("productCode").value;
	var checked = true;
	var warehouse_id = "";
	var radio = document.getElementsByName("chose");
	for ( var i = 0; i < radio.length; i++) {
		if (radio[i].checked) {
			warehouse_id = radio[i].value;
			checked = false;
		}
	}
	if (checked) {
		alert("请选择一个仓库");
		return;
	}
	var orderNo = document.getElementById('orderNo').innerHTML;
//	alert(productCode);
	window.location.href = "next.html?orderNo=" + orderNo + "&amount="
			+ totalAmount + "&weight=" + weight + "&warehouse_id=" + warehouse_id+ "&productCode=" + productCode;
}
function next2() {
	var checked = true;
	var yewu = "";
	weight = document.getElementById("weight").value;
	var length = document.getElementById("length").value;
	var size = document.getElementById("size").value;
	var hight = document.getElementById("hight").value;
	
	var reg = /^(([1-9][0-9]{0,2}((\.[0-9]{1,3})?))|0\.[0-9]{0,2}[1-9]|(0\.(([0-9][1-9])|([1-9][0-9]))[0-9]{0,1}))$/;
	if (!reg.test(weight)) {
		document.getElementById("weight").focus();
		alert("请正确填入重量");//请正确填入重量
		return false;
	}
	 if (weight >= 20) {
		document.getElementById("weight").focus();
		alert("重量不可大于等于2KG");//重量不可大于等于2KG
		return false;
	} 
	reg = /^(([1-9][0-9]{0,2}((\.[0-9])?))|0\.[0-9])$/;
	if (!reg.test(length) || length > 60) {
		alert("长度输入有误，请输入小于60cm长度,小数后保留一位");//长度输入有误，请输入小于150cm长度,小数后保留一位
		return false;
	}
	if (!reg.test(size) || size > 60) {
		alert("宽度输入有误，请输入小于60cm宽度,小数后保留一位");//宽度输入有误，请输入小于150cm宽度,小数后保留一位
		return false;
	}
	if (!reg.test(hight) || hight > 60) {
		alert("高度输入有误，请输入小于60cm高度,小数后保留一位");//高度输入有误，请输入小于150cm高度,小数后保留一位
		return false;
	}
	
	var fuwu = document.getElementById("fuwu").value;
	var radio = document.getElementsByName("chose");
	for ( var i = 0; i < radio.length; i++) {
		if (radio[i].checked) {
			yewu = radio[i].value;
			checked = false;
		}
	}
	if (checked) {
		alert("请选择一个业务");
		return;
	}
	var orderNo = document.getElementById('orderNo').innerHTML;
	if(fuwu=="1"){//国内业务
		window.location.href = "next2.html?orderNo=" + orderNo + "&fuwu=" + fuwu + "&weight=" + weight + "&yewu=" + yewu+ "&amount=25";
	}else{//出口  俄罗斯业务
		window.location.href = "next2ru.html?orderNo=" + orderNo + "&fuwu=" + fuwu + "&weight=" + weight + "&yewu=" + yewu+ "&amount=25";
	}
}

