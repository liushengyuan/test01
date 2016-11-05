var Spec={
	init:function(haveSpec){
		var isclick = false;
		var self = this;
 
		
		$("#buyNow").click(function(){	
			var $this= $(this); 
			self.addToCart($this);
			
		});
		$("#addCart").click(function(){	
			var $this= $(this); 
			self.addToCart($this);
		});
		$(".addGroupbuyGoods").click(function(){
			var $this=$(this);
			self.addGroupBuy($this);
		});
		
		
	 
	 
		this.refresh();
		$("a[name='goods_spec'][specvid!='']").click(function(){
			isclick = true;
			var link = $(this);
			if(link.attr("class")!='hovered' && link.attr("class")!='disabled' ){
				self.specClick($(this));
			}
			return false;
		});
		if(isclick==false){
			isclick = true;
			var link = $(".spec-item").find("li:first a:first-child");
			if(link.attr("class")!='hovered' && link.attr("class")!='disabled' ){
				self.specClick(link);
			}
			return false;
		}
		$("#goodsform [name='action']").val("addProduct");
	 
	},
	specClick:function(specLink){
		specLink.parents("ul").find("a[specvid!='']").removeClass("hovered");
		specLink.parent().parent().parent().parent().find("em").addClass("checked");
		specLink.addClass("hovered");
 		
		this.refresh(specLink);
	},
	findGoodsImg:function(vid){
		for(i in  spec_imgs){
			var specimg = spec_imgs[i];
			if(specimg.specvid==parseInt(vid)){
				return specimg.goods_img;
			}
		}
	},
	
	//根据当前选择的规格找到货品
	findProduct:function(vidAr){
		var pros =[];
		//判断两个数组元素值是否相同，不判断位置情况
		function arraySame(ar1,ar2){
			//if(ar1.length!=ar2.length) return false;
			
			for(i in ar1){
				if($.inArray(ar1[i],ar2)==-1){ //不存在
					return false;
				}
			}
			return true;
		}
		
		var self = this;
	 
		for(i in Products){
			var product= Products[i];
			if(arraySame(vidAr,product.specs)){
				pros[pros.length] =product; 
			}
		}	
		 
		return pros;
	}
	,
	refresh:function(specLink){
		var self = this;
		var product_ar=[];
		$(".spec-item a.hovered").each(function(){
			var link = $(this);
			product_ar[product_ar.length]=parseInt(link.attr("specvid"));
		});
				
		var pro =this.findProduct(product_ar);
		for(i in Refresh){
			Refresh[i].refresh(pro,specLink,product_ar);
		}
		if(pro.length==1){
			var groupbuy = $("#groupbuy").val();
			if(groupbuy==1){//不更改
				
			}else{//更改
				$("strong[nctype='goods_stock']").html(pro[0].enable_store);
			}
			if(pro[0].wholesale_volume>0){
				$("#pfjId").show();
				$("#pfqpsId").html(pro[0].wholesale_volume);
			}else{
				$("#pfjId").hide();
			}
			
			$("#productid").val(pro[0].product_id);
		}
	}
,
	 
	addToCart:function(btn){
		var self = this;
		$.Loading.show("пожалуйста, подождите...");
		btn.attr("disabled",true);
		var id = btn.attr("id");
		var action = $("#goodsform [name='action']").val();
		var options={
			url:ctx+"/api/shop/cart!" + action + ".do?ajax=yes&store_id="+$("#storeid").val(),
			dataType:"json",
			async:false,
			cache: false,             //清楚缓存，暂时测试，如果产生冲突，请优先考虑是否是这条语句。
			success:function(result){
				$.Loading.hide();
				if(result.result==1){
					if(id!="buyNow"){
						self.showAddSuccess();
					}else{
						window.location.href="cart.html";
					}
				}else{
					$.dialog({title:'подсказка',content: "произошла ошибка:"+result.message,lock: true});
				}
				btn.attr("disabled",false);
			},
			error:function(){
				$.Loading.hide();
				$.dialog({title:'подсказка',content: "К сожалению, произошла ошибка",lock: true});
				btn.attr("disabled",false);
			}
		};
		$("#goodsform").ajaxSubmit(options);		
	},
	showCartCount:function(){
		$.ajax({
			url:ctx+"/api/shop/cart!getCartData.do",   //获取购物车数据api
			dataType:"json",
			cache: false,             //清楚缓存，暂时测试，如果产生冲突，请优先考虑是否是这条语句。
			success:function(result){
				if(result.result==1){				
						$(".cart-number").text(result.data.count);   //将得到的结果放入到头部的购物车数量中。
					 
				}else{
					$.alert(result.message);
				}	
			},
			error:function(){
				$.Loading.hide();
				$.alert("неправильно:(");
			}
		});
	}
	,
	showAddSuccess:function(){
		var myself = this;
		var html = $(".add_success_msg").html();
		$.dialog({ title:'сообщение',content:html,lock:true,init:function(){
			var self = this;
			$(".ui_content .btn").jbtn();
			$(".ui_content .returnbuy_btn").click(function(){
				self.close();     //关闭自己
				myself.showCartCount();
			});
	
			$(".ui_content .checkout_btn").click(function(){
				location.href=ctx+"/cart.html";

			});
			
			$(".ui_close").click(function(){
				myself.showCartCount();
			})
			
		}});
	},addGroupBuy:function(btn){
		var self = this;
		$.Loading.show("пожалуйста, подождите...");
		btn.attr("disabled",true);
		var options={
			url:ctx+"/api/shop/cart!addGoods.do?ajax=yes&store_id="+$("#storeid").val(),
			dataType:"json",
			async:false,
			cache: false,             //清楚缓存，暂时测试，如果产生冲突，请优先考虑是否是这条语句。
			success:function(result){
				$.Loading.hide();
				if(result.result==1){
					self.showAddSuccess();
				}else{
					$.dialog({title:'подсказка',content: "произошла ошибка:"+result.message,lock: true});
				}
				btn.attr("disabled",false);
			},
			error:function(){
				$.Loading.hide();
				$.dialog({title:'подсказка',content: "К сожалению, произошла ошибка",lock: true});
				btn.attr("disabled",false);
			}
		};
		$("#goodsform").ajaxSubmit(options);		
	}
};
var StateRefresh={
	ArrrRemove:function( ar,obj) {  
		var new_ar =[];
		for( var i in ar ){
			if(obj!= ar[i]){
				new_ar.push(ar[i]);
			}
		}
		return new_ar;
	},

	refresh:function(pro,specLink,product_ar){
		//pro:找到的product [{sprc:{}},{}]
		//product_ar:选中的规格[1,2]
		
		var self  = this;
		if(product_ar.length>0){
		//从目前未选中的规格中循环
			$(".spec-item").not( specLink.parents(".spec-item") ).find("a").each(function(){
				var link = $(this);
				var proar=product_ar;
				link.parents(".spec-item").find("a").not(this).each(function(){
					var specvid = parseInt($(this).attr("specvid"));
					proar= self.ArrrRemove(proar,specvid);
				});
				
				var specvid = parseInt(link.attr("specvid"));
				proar.push(specvid);
				
				var result =Spec.findProduct(proar);
				if(!result || result.length==0){
					link.addClass("disabled");
				}else{
					link.removeClass("disabled");
				}
				proar.pop();
				
			});
		}
	}
};
var SelectTipRefresh={
	refresh:function(pro){
		var i=0;
		var specHtml="";
		var specimage = "";
		$(".spec-item a.hovered").each(function(){
			if(i==0) specHtml="";
			if(i!=0) specHtml+="、";
			if($(this).attr("specimage").length>0){
				specimage += $(this).attr("specimage")+""; 
			}
			specHtml +=$(this).attr("title")+"";
			i++;
		});	
		if(i>0){
			if(specimage.length>0){
				 var $this = $("#detail_wrapper .gallery .thumblist li p");
				 $(".MagicZoom img").attr("src" , specimage);
			     $this.addClass("selected").siblings().removeClass("selected");
			}
			specHtml="<dt>Вы выбрали：</dt>"+"<dd><font color='red'>"+specHtml+"</font></dd>";
		}else{
			//specHtml="<dt>выберите：</dt><dd>следующая спецификация</dd>";
		}
		$(".spec-tip").html(specHtml);
	}
};
var PriceRefresh={
	refresh:function(pro){
		//alert(language);
		if(pro.length==1){
			if(language=="zh"){
				$("#goods_price strong").text("￥"+pro[0].price );
				$("#goods_whprice strong").text("￥"+pro[0].whprice );
			}else if(language=="ru"){
				$("#goods_price strong").text(pro[0].price_ru+"р." );
				$("#goods_whprice strong").text(pro[0].whprice_ru+"р." );
			}
			//$("#goods_price strong").text("￥"+pro[0].price );
			$("#productid").val(pro[0].product_id);
		}
		else{
			var maxPrice=0,minPrice=-1;
			var maxWhprice=0,minWhprice=-1;
			for(i in pro){
				if( maxPrice<pro[i].price_ru){
					maxPrice = pro[i].price_ru;
				}
				if( maxWhprice<pro[i].whprice_ru){
					maxWhprice = pro[i].whprice_ru;
				}
				if(minPrice==-1|| minPrice>pro[i].price_ru){
					minPrice = pro[i].price_ru;
				}
				if(minWhprice==-1|| minWhprice>pro[i].whprice_ru){
					minWhprice = pro[i].whprice_ru;
				}
			}
			if(minPrice==maxPrice){
				$("#goods_price strong").text(minPrice+"р.");
			}else{
				$("#goods_price strong").text(minPrice+"р.-" +maxPrice+"р.");
			}
			if(minWhprice==maxWhprice){
				$("#goods_whprice strong").text(minWhprice+"р.");
			}else{
				$("#goods_whprice strong").text(minWhprice+"р.-" +maxWhprice+"р.");
			}
			
		}
	}
};
function canBuy(){
	$("input[name=action]").val("addProduct");
	$("#buyNow").unbind('click');
	$("#buyNow").bind('click',function(){
		Spec.addToCart($(this));
	});
	
	$("#addCart").unbind('click');
	$("#addCart").bind('click',function(){
		Spec.addToCart($(this));
		return false;
	});
	
	$("#buyNow").css("cursor","pointer");
	$("#buyNow").tip({'disable':true});
	
	$("#addCart").css("cursor","pointer");
	$("#addCart").tip({'disable':true});
	
	
	$("#buyNow").removeClass('disabled');
	$("#addCart").removeClass('disabled');
}

function cantbuy(){
	$("#buyNow").unbind('click');
	$("#buyNow").bind('click',function(){return false;});
	$("#buyNow").css("cursor","not-allowed");
	
	$("#addCart").unbind('click');
	$("#addCart").bind('click',function(){return false;});
	$("#addCart").css("cursor","not-allowed");
}

var BtnTipRefresh = {
	refresh:function(pro){
		$("#buyNow").attr('tip','');
		$("#addCart").attr('tip','');
		
		$("#buyNow").addClass('disabled');
		$("#addCart").addClass('disabled');
		
		if(pro.length==1){
			if(pro[0].enable_store==0){
				cantbuy();
				$("#addCart,#buyNow").tip({'disable':false,className:"cantbuy",text:"Это товар нехватка инвентаря"});

			}else{
				canBuy();
			 
			}
		}else{
			
			var i=0;
			var tip='';
			$("#goodsform .spec-item em").each(function(){
				var em = $(this);
				
				if(em.attr("class")!='checked'){
					if(i!=0)tip+="、";
					tip+=em.text();
					i++;
				}
			});
			$("#addCart,#buyNow").tip({'disable':false,className:"cantbuy",text:"пожалуйста выберите:"+tip});
		}
	}	
};
var Refresh=[SelectTipRefresh,PriceRefresh,BtnTipRefresh,StateRefresh];

//tip插件
(function($) {
	$.fn.tip = function(options) {
		 
		var opts = $.extend({}, $.fn.tip.defaults, options);
		var tipEl= $(".tipbox");
		if(tipEl.size()==0){
			var html="<div class='tipbox' style='position: absolute;z-index:99'>";
			html+='<div class="tip-top"></div>';
			html+='<div class="tip">';
			html+='<div class="tip-text"></div>';
			html+='</div>';
			html+='<div class="tip-bottom"></div>';
			html+='</div>';
			tipEl=$(html).appendTo($("body"));
			tipEl.addClass(opts.className);
			tipEl.hide();
		}
		 tipEl.find(".tip>.tip-text").html(opts.text);
		 if( opts.disable){
			 $(this).unbind("mouseover").unbind("mousemove").unbind("mouseout");
		 }else{
			 $(this).bind("mouseover",function(e){
				 tipEl.show(); 
			 }).bind("mousemove",function(e){
				 tipEl.css('top',e.pageY+15).css('left',e.pageX+15);
			 }).bind("mouseout",function(){
				tipEl.hide();
			 });
		 }
	};
	
    $.fn.tip.defaults = {
    	className:"tip",
        text:"", 
        disable:false
    };
    
})(jQuery);



