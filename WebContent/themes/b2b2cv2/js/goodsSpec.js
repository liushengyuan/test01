function zuhe_ar(ar1,ar2){
	var new_ar = new Array();
	for(var i=0;i<ar1.length;i++){
		for(var j=0;j<ar2.length;j++){
			//可能是白，也可能是[白,x]
			var temp = cloneAr(ar1[i]);
			if(temp.value){
				new_ar.push([temp,ar2[j]]); //白的情况
			}else{
				temp.push(ar2[j]);
				new_ar.push(temp);
			}

		}
	}

	return new_ar;
}

function cloneAr(ar){
	var new_ar=[];
	for(var i in ar){
		new_ar[i]=ar[i];
	}
	return new_ar;
}

var SpecOper={
	specProp:{},

	init:function(){
		var self= this;
		this.syncSpecFromTable();
		this.bindTableEvent();
		self.buttonBind();

		$("#spec-input .spec .chk").click(function(){
			self.createSpec();
			self.createSpecPicture(this);
		});

		$("#spec-input .spec .chkAll").click(function() {
			var specid = $(this).attr("specid");
			$("#spec-input .spec .chk").each(function(){
				if($(this).attr("specid")==specid){
					$(this).attr("checked", !$(this).attr("checked"));
					self.createSpec();
					self.createSpecPictureForAll();
				}
			});
		});


	},

	/**
	 * 开启或关闭规格按钮事件
	 */
	buttonBind:function(){

		var self = this;
		$("#specOpenBtn").click(function(){
			self.openSpec();
		});

		$("#specCloseBtn").click(function(){
			var goodsid = $(this).attr("goodsid");
			if("0"!=goodsid){
				$.ajax({
					url:ctx+"/shop/admin/goodsSpec!closeSpec.do?action=check-goods-in-order&ajax=yes&goodsid="+goodsid,
					dataType:"json",
					success:function(result){
						if(result.result==1){
							if(confirm("此商品已经有客户购买，如果关闭规格此订单将无法配货发货，请谨慎操作!\n点击确定关闭规格，点击取消保留此规格。")){
								self.closeSpec();
							}
						}else{
							self.closeSpec();
						}
					},
					error:function(){
						alert("检测出错");
					}
				});
			}else{
				self.closeSpec();
			}

		});

	}
	,

	/**
	 * 开启规格
	 */
	closeSpec:function(){

		$("#spec-input").hide();
		$("#no-spec-input").show();
		$("#haveSpec").val(0);

	}

	/**
	 * 关闭规格
	 */
	,
	openSpec:function(){
		$("#spec-input").show();
		$("#no-spec-input").hide();
		$("#haveSpec").val(1);
	}


	,
	createSpec:function(){
		var self = this;
		var specUl= $("#spec-input ul");
		var specs=[];
		var specnames=[];
		//	specs.push([]);
		$.each( specUl,function(i,ul){
			var chks = $(ul).find(".chk:checked");
			if(chks.size()>0){
				var valueAr =self.createSpecFromChk(chks); //根据chk生成的规格值数组
				specs.push(valueAr);
				specnames.push($(ul).attr("specname"));
			}
		} );
		if(specs.length>1){
			$(".spec-table").show();
			var temp =specs[0];
			for(var i=1;i<specs.length;i++){
				temp = zuhe_ar(temp,specs[i]);
			}
			self.createSpecHead(specnames);
			self.createSpecBody(temp);
			self.bindTableEvent();
		}else{
			$(".spec-table").show();
			if(specs.length==1){
				$("#haveSpec").val(1);
				var one  = specs[0];
				var temp =new Array();
				for( tempi in one){
					temp[tempi]=[ one[tempi] ];
				}
				self.createSpecHead(specnames);
				self.createSpecBody(temp);
				self.bindTableEvent();
			}
		}
		if(specs.length==0){
			$(".spec-table").hide();
			$("#haveSpec").val(0);
		}
	},
	bindTableEvent:function(){
		var self = this;
		$(".ipt").unbind("blur").bind("blur",function(){
			var $this= $(this);

			var prop = $this.attr("prop");
			var value = $this.val();
			if( ("price"==prop || "weight"==prop || "cost"== prop || "price_ru"==prop) &&  ( !SpecOper.isNum(value)) ){

				alert("请输入数字");

				$this.select();
				return ;
			}else{

				var propid = $this.attr("propid");
				if(!self.specProp[propid]){
					self.specProp[propid] =[];
				}

				self.specProp[propid][prop]=value;
			}
		});

		$(".spec_table .delete").unbind('click').bind('click',function(){
			self.deleteProRow($(this));
		});

	},
	isNum:function(num){
		var reg=/^(-?\d*)\.?\d{1,4}$/;
		return reg.test(num);
	},
	createSpecHead:function(specnames){
		var getLanguageValue=$("#setLanguageForGoodsOption").val();
		var thead=$(".spec_table thead tr").empty();

		for(var i in specnames){
			thead.append("<th class='w1'>"+specnames[i]+"</th>");
		}
		var ru_price = "";
		var rmb_price = "";
		
		if(getLanguageValue=='zh'){
			if($("#is_discount").val() == 1){//折扣销售
				 ru_price = "卢布折扣价";
				 rmb_price = "人民币折扣价";
			}else{
				 ru_price = "卢布价格";
				 rmb_price = "人民币价格";
			}
			thead.append("<th  class='w2'>货号<span class='red'></span></th>" +
					"<th class='w3'>重量<span class='red'>*</span></th>" +
					"<th class='w2'>"+rmb_price+"<span class='red'>*</span></th>" +
					"<th class='w2'>人民币批发价</th>" +
					"<th class='w2'>起批量</th>");
		}else{
			if($("#is_discount").val() == 1){//折扣销售
				 ru_price = "Скидка Рублей";
				 rmb_price = "Скидка юаней";
			}else{
				 ru_price = "Рубль";
				 rmb_price = "Жэньминьби";
			}
			thead.append("<th  class='w2'>Номер товара<span class='red'></span></th>" +
					"<th class='w3'>Вес<span class='red'>*</span></th>" +
					"<th class='w2'>"+ru_price+"<span class='red'>*</span></th>" +
					"<th class='w2'>Оптовая цена рублей</th>" +
					"<th class='w2'>"+rmb_price+"<span class='red'>*</span></th>" +
					"<th class='w2'>Оптовая цена жэньминьби</th>" +
					"<th class='w2'>Доллар<span class='red'>*</span></th>" +
					"<th class='w2'>Оптовая цена толларов</th>"+
					"<th class='w2'>Начальное оптовое количество</th>");
		}

	}

	,

	/**
	 * 生成货品表格
	 */
	createSpecBody:function(specAr){

		var self = this;
		var body=$(".spec_table tbody");
		body.empty();
		var storePrice_ru=$("input[name='storeGoods.price_ru']").val();
		var storePrice_zh=$("input[name='storeGoods.price']").val();
		var storePrice_en=$("input[name='storeGoods.price_en']").val();
		var storeWhprice_ru=$("input[name='storeGoods.whprice_ru']").val();
		var storeWhprice=$("input[name='storeGoods.whprice']").val();
		var storeWhprice_en=$("input[name='storeGoods.whprice_en']").val();
		var storeWholesale_volume=$("input[name='storeGoods.wholesale_volume']").val();
		var goods_weight=$("input[name='weight']").val();
		for(i in specAr){

			var childAr=specAr[i];//这是一行
			var tr=$("<tr></tr>");
			var propid="";
			var specvids="";
			var specids ="";

			for(j in childAr){ //这是一列
				var spec = childAr[j];
				if(propid!="")propid+="_";
				propid+=spec.valueid;
				tr.append($("<td>"+spec.value+"</td>"));

				if(j!=0){
					specvids+=",";
					specids+=",";
				}
				specvids+=spec.valueid;
				specids +=spec.specid;

			}
			var specProp = self.specProp[propid];
			var price =storePrice_zh;
			var price_ru =storePrice_ru;
			var price_en =storePrice_en;
			var whprice =storeWhprice;
			var whprice_ru =storeWhprice_ru;
			var whprice_en =storeWhprice_en;
			var weight= goods_weight;
			var cost = '';
			var sn="";
			var productid="";
			var wholesale_volume=storeWholesale_volume;

			if(specProp){
				price = specProp["price"];
				price_ru = specProp["price_ru"];
				price_en = specProp["price_en"];
				whprice = specProp["whprices"];
				whprice_ru = specProp["whprices_ru"];
				whprice_en = specProp["whprices_en"];
				wholesale_volume=specProp["wholesale_volumes"];
				weight= specProp["weight"];
				cost = specProp["cost"];
				sn = specProp["sn"];
				productid=specProp["productid"];
				if(!price || price=="0")price =storePrice_zh;
				if(!price_ru || price_ru=="0")price_ru =storePrice_ru;
				if(!price_en || price_en=="0")price_en =storePrice_en;
				if(!whprice || whprice=="0")whprice =storeWhprice;
				if(!whprice_ru || whprice_ru=="0")whprice_ru =storeWhprice_ru;
				if(!whprice_en || whprice_en=="0")whprice_en =storeWhprice_en;
				if(!weight || weight=="0")weight =goods_weight;
				if(!cost)cost ="0";
				if(!sn)sn="";
				if(!productid)productid="";
				if(!wholesale_volume)wholesale_volume=storeWholesale_volume;

			}


			var hidden ='<input type="hidden" value="'+specvids+'" name="specvids">';
			hidden+='<input type="hidden" value="'+specids+'" name="specids">';

			var td ='<td><input type="text" class="ipt" style="width:150px" name="sns" value="'+sn+'" autocomplete="off" propid="'+propid+'" prop="sn">';
			td+='<input type="hidden"  name="productids" value="'+productid+'" class="ipt" propid="'+propid+'" prop="productid"></td>';
			td+='<td><input type="text" size="10" name="weights" value="'+weight+'" autocomplete="off" class="ipt" propid="'+propid+'" prop="weight"></td>';
			td+="<td style='display:none'><input class='ipt price' propid='"+propid+"' prop='price_ru' size='8'  value='"+price_ru+"' type='hidden'  name='prices_ru' />";
			td+="<td style='display:none'><input class='ipt price' propid='"+propid+"' prop='whprices_ru' size='8'  value='"+whprice_ru+"' type='hidden'  name='whprices_ru' />";
			td+="<td>"+hidden+"<input class='ipt price' propid='"+propid+"' prop='price' size='8'  value='"+price+"' type='text'  name='prices' />";
			td+="<td><input class='ipt price' propid='"+propid+"' prop='whprices' size='8'  value='"+whprice+"' type='text'  name='whprices' />";
			td+="<td style='display:none'><input class='ipt price' propid='"+propid+"' prop='price_en' size='8'  value='"+price_en+"' type='hidden'  name='prices_en' />";
			td+="<td style='display:none'><input class='ipt price' propid='"+propid+"' prop='whprices_en' size='8'  value='"+whprice_en+"' type='hidden'  name='whprices_en' />";
			td+='<td><input type="text" size="10" name="wholesale_volumes" value="'+wholesale_volume+'" autocomplete="off" class="ipt" propid="'+propid+'" prop="wholesale_volumes" dataType="int"></td>';
			//td+='<td><input type="text" size="8" name="costs" value="'+cost+'" autocomplete="off"  class="ipt" propid="'+propid+'" prop="cost"></td>';
			tr.append($(td));

			body.append(tr);
		}
	}
	,

	/**
	 * 根据checkbox生成规格数组
	 */
	createSpecFromChk:function(chks){
		var ar=[];
		$.each(chks,function(i,c){
			var chk=$(c);
			var spec={};
			spec.valueid=parseInt(chk.val());
			spec.specid=parseInt(chk.attr("specid"));
			spec.value=chk.attr("spec_value");
			ar.push(spec);

		});
		return ar;
	}
	,

	/**
	 * 由规格表格同步规格
	 * 1.价格、重量等属性至specProp对象
	 * 2.选中checkbox
	 */
	syncSpecFromTable:function(){
		var self = this;
		$(".spec_table tbody tr").each(function(i,v){

			var tr=$(this);
			var inputs=tr.find(".ipt");
			var propid =inputs.attr("propid");
			self.specProp[propid]=[];

			//同步各个属性
			inputs.each(function(){
				$this= $(this);
				var propname= $this.attr("prop");
				self.specProp[propid][propname]=$this.val();

			});



			//同步规格复选框 
			var propidAr  = propid.split("_");
			for(var i in propidAr){
				$("input[value="+propidAr[i]+"]").attr("checked",true);
			}

		});
	},


	/**
	 * 删除一行规格
	 */
	deleteProRow:function(link){
		if(confirm("确定删除此货品吗？删除后不可恢复")){
			var productid = link.attr("productid");
			if("0"!=productid && "0"!=productid){
				$.ajax({
					url:"../../admin/plugin?action=check-pro-in-order&ajax=yes&beanid=goodsSpecPlugin&productid="+productid,
					dataType:"json",
					success:function(result){
						if(result.result==1){
							if(confirm("此货品已经有顾客购买，如果删除此订单将不能配货发货，请谨慎操作!\n点击确定删除此货品，点击取消保留此货品。")){
								link.parents("tr").remove();
							}
						}else{
							link.parents("tr").remove();
						}
					},
					error:function(){

					}
				});
			}else{
				link.parents("tr").remove();
			}

		}

	},
	createSpecPicture:function(obj){//单选时
		var body=$(".color_picture_table tbody");
		specid=$(obj).attr("specid");
		if(specid==1000){
			if($(obj).attr("checked")=='checked'){
				//body.append("<tr id="+$(obj).val()+"><td>"+$(obj).attr("spec_value")+"</td><td><input type='file' id='c"+$(obj).val()+"'></td></tr>");
				body.append("<tr id="+$(obj).val()+"><td>"+$(obj).attr("spec_value")+"<div id='s"+$(obj).val()+"'></div></td><td><input type='file' id='c"+$(obj).val()+"' fs='"+$(obj).val()
						+"'><input type='hidden' name='c_"+$(obj).val()+"' id='fs_img"+$(obj).val()+"'></td></tr>");
				bindFileEventForColor($("#c"+$(obj).val()));
			}else{
				//alert($(obj).attr("checked"));
				$(".color_picture_table tbody tr[id='"+$(obj).val()+"']").remove();
			}
		}
	},
	createSpecPictureForAll:function(){
		var body=$(".color_picture_table tbody").empty();
		$("#spec-input .spec input[specid='1000']:checked").each(function(){
			body.append("<tr id="+$(this).val()+"><td>"+$(this).attr("spec_value")+"</td><td><div id='s"+$(this).val()+"'></div><input type='file' id='c"+$(this).val()+"' fs='"+$(this).val()
					+"'><input type='hidden' name='c"+$(this).val()+"' id='fs_img"+$(this).val()+"'></td></tr>");
			bindFileEventForColor($("#c"+$(this).val()));
				
		});
		$(".color_picture_table tbody tr[id='on']").remove();
	}


};

$(function(){
	SpecOper.init();
});