var Cart={
		init:function(staticserver){
			var self=this;			
			this.bindEvent();
		},
		bindEvent:function(){
			var self=this;
			var numprice1=self.changeTwoDecimal_f(0);
			var locale=$("#currencyId").val();
			//购物数量调整
			$(".Numinput .increase,.Numinput .decrease").click(function(){
				$this = $(this);
				var number = $this.parents(".Numinput");
				var itemid =number.attr("itemid");
				var productid =number.attr("productid");
				var objipt = number.find("input");
				var num=objipt.val();
				var min_number=number.attr("mini_number");
				num =parseInt(num);
				
				var store_id=number.attr("store_id");
				var freight_id=number.attr("freight_id");
				var goods_id=number.attr("goods_id");
				var sendprice=number.attr("sendprice");
				var limitbuy=number.attr("limitbuy");
					if (!isNaN(num)){
						if($this.hasClass("increase")){
							if(limitbuy==1){
								var limitmber=number.attr("limitnumber");
								var nowtime=number.attr("nowtime");
								var end_time=number.attr("end_time");
								var start_time=number.attr("start_time");
					 			  if(start_time < nowtime  && nowtime <end_time){
					 				  if(num>=limitmber){
					 					 objipt.val(limitmber);
					 					 num=limitmber;
					 				  }else{
					 					 num++;
					 				  }
					 			  }else{
					 				 num++;
					 			  }
							}else{
								num++;
							}	
						}
						if($this.hasClass("decrease")){
							 if(num == 1 ){
								 if(confirm("确定要删除该商品?")){
									 self.deleteGoodsItem(itemid);
								 }
								 return false;
							} 
							 if(num<=min_number){
								 alert("商品最小购买量不能低于"+min_number);
								 return false;
							 }
								if(limitbuy==1){
									var limitmber=number.attr("limitnumber");
									var nowtime=number.attr("nowtime");
									var end_time=number.attr("end_time");
									var start_time=number.attr("start_time");
						 			  if(start_time < nowtime  && nowtime <end_time){
						 				  if(num>=limitmber){
						 					 objipt.val(limitmber);
						 					num--;
						 				  }else{
						 					 num--;
						 				  }
						 			  }else{
						 				 num--; 
						 			  }
								}else{
									num--;
								}
						}
						 num = (num <=1 || num > 100000) ? 1 : num;
						 self.updateNum(itemid, num, productid,objipt);
					     self.updateLogisprice(freight_id,goods_id,num,store_id,sendprice);
						
					}
			});
			
			//购物数量手工输入
            $(".Numinput input").keydown(function(e){
                var kCode = $.browser.msie ? event.keyCode : e.which;
                //判断键值  
                if (((kCode > 47) && (kCode < 58)) 
                    || ((kCode > 95) && (kCode < 106)) 
                    || (kCode == 8) || (kCode == 39) 
                    || (kCode == 37)) { 
                    return true;
                } else{ 
                    return false;  
                }
            }).focus(function() {
                this.style.imeMode='disabled';// 禁用输入法,禁止输入中文字符
            }).keyup(function(){
                var pBuy   = $(this).parent();//获取父节点
                var itemid  = pBuy.attr("itemid");
                var productid  = pBuy.attr("productid");
                var numObj = pBuy.find("input[name='num']");//获取当前商品数量
                var num    = parseInt(numObj.val());
                
                var store_id=pBuy.attr("store_id");
				var freight_id=pBuy.attr("freight_id");
				var goods_id=pBuy.attr("goods_id");
				var sendprice=pBuy.attr("sendprice");
				var limitbuy=pBuy.attr("limitbuy");
                if (!isNaN(num)) {
                    var numObj = $(this);
                    var num    = parseInt(numObj.val());
                    if(limitbuy==1){
						var limitmber=pBuy.attr("limitnumber");
						var nowtime=pBuy.attr("nowtime");
						var end_time=pBuy.attr("end_time");
						var start_time=pBuy.attr("start_time");
			 			  if(start_time < nowtime  && nowtime <end_time){
			 				  if(num>=limitmber){
			 					 numObj.val(limitmber);
			 					 num=limitmber;
			 				  }
			 			  }
					}	
                    num = (num <=1 || num > 100000) ? 1 : num;
                    self.updateNum(itemid, num, productid,numObj);
                    self.updateLogisprice(freight_id,goods_id,num,store_id,sendprice);
                }
            });
            
            //输入积分进行计算
            $("#pointInputId").keydown(function(e){
                var kCode = $.browser.msie ? event.keyCode : e.which;
                //判断键值  
                if (((kCode > 47) && (kCode < 58)) 
                    || ((kCode > 95) && (kCode < 106)) 
                    || (kCode == 8) || (kCode == 39) 
                    || (kCode == 37)) { 
                    return true;
                } else{ 
                    return false;  
                }
            }).focus(function() {
                this.style.imeMode='disabled';// 禁用输入法,禁止输入中文字符
            }).keyup(function(){
            	if(locale=="CNY"){
            		var money = $("#goodsPrice").attr("value");
            		 var pointNum = parseInt($("#pointSpanId").val());//获取总共有多少积分
                     var num = parseInt($("#pointInputId").val());//获取输入的积分
                     var consume = parseFloat($("#consumeId").val()).toFixed(2);//获取当前积分数比值
                   /* alert(consume);
                     alert(money);*/
                     var maxpoint = money/consume;             
                     if(num<1){
                     	$("#pointInputId").val(0);
                     }
                     if(pointNum>0 && num>pointNum){
                     	$("#pointInputId").val(pointNum);
                     }
                     if(num>0 && num>maxpoint){
                     	alert("积分兑换总值大于了总值！");
                     	$("#pointInputId").val(1);
                     }
                     var numprice=0.00;
                     if(parseInt($("#pointInputId").val())>0){
                     	numprice = self.changeTwoDecimal_f( consume* parseInt($("#pointInputId").val()));
                         $("#pointMoneyId").html("-￥"+numprice);
                     }else{
                     	 $("#pointMoneyId").html("-￥0.00");
                     }
                     
                     //alert($("#goodsPrice").val());
                     money1 = self.changeTwoDecimal_f(money);
                     numprice1 = self.changeTwoDecimal_f(numprice);
                     finalprice = self.changeTwoDecimal_f(money1-numprice1);
                     $("#MoneyId").html("￥"+finalprice);
            	}else{
            		 var pointNum = parseInt($("#pointSpanId").val());//获取总共有多少积分
                     var num = parseInt($("#pointInputId").val());//获取输入的积分
                     var consume = parseFloat($("#consumerubId").val()).toFixed(2);//获取当前积分数比值
                     var maxpoint = money/consume;
                     if(num<1){
                     	$("#pointInputId").val(0);
                     }
                     if(pointNum>0 && num>pointNum){
                     	$("#pointInputId").val(pointNum);
                     }
                     if(num>0 && num>maxpoint){
                     	alert("Активировать стоит больше, чем валовой!");
                     	$("#pointInputId").val(1);
                     }
                     var numprice=0.00;
                     if(parseInt($("#pointInputId").val())>0){
                     	numprice = self.changeTwoDecimal_f( consume* parseInt($("#pointInputId").val()));
                         $("#pointMoneyId").html(numprice+"p.");
                     }else{
                     	 $("#pointMoneyId").html("-0.00p.");
                     }
                     
                     //alert($("#goodsPrice").val());
                     money1 = self.changeTwoDecimal_f(money);
                     numprice1 = self.changeTwoDecimal_f(numprice);
                     finalprice = self.changeTwoDecimal_f(money1-numprice1);
                     $("#MoneyId").html(finalprice+"p.");
            	}
              }
            );
            
			//删除商品
			$(".border .delete").click(function(){
				var cartid = $(this).parents("tr").attr("itemid");
				if(confirm("您确实要把该商品移出购物车吗？") ){
					self.deleteGoodsItem(cartid);
				}
			});
			
			//清空购物车
			$(".border .clean_btn").click(function(){
				if(confirm("您确认要清空购物车吗？") ){
					self.clean();
				}
			});
			
			//继续购物
			$(".border .returnbuy_btn").click(function(){
				location.href="index.html";
			});
			
			//去结算
			$(".border .checkout_btn").click(function(){
				var count=$("#totalchoose").html();
				if(count==0){
					alert("请至少选择一件商品");
					return false;
				}else{
					if(isLogin){
						var checkboxs=$("input:checked[name=orderIndexs][type='checkbox']");
						var propertys="";
						for ( var i = 0; ci = checkboxs[i]; i++) {
							if (i >= 0) {
							   propertys +=ci.value+",";
							}
						}
						self.checkkillsecgoods(propertys,numprice1);
						//window.open ("checkout.html?integratedprice="+numprice1,'','newHtml','width='+ (screen.availWidth - 10) +',height='+ (screen.availHeight-50) +'top=0,left=0,toolbar=yes,menubar=yes,location=no, status=yes,scrollbars=yes,resizable=yes,fullscreen=1"'); 
						
					}else{
						self.showLoginWarnDlg();					
					}
				}
			});
		},
		
		//提示登录信息
		showLoginWarnDlg:function(btnx,btny){
			var html = $("#login_tip").html();
			$.dialog({ title:'提示信息',content:html,lock:true,width:330,init:function(){
				
				$(".ui_content input").jbtn();
				$(".ui_content .to_login_btn").click(function(){
					 location.href=ctx+"/login.html?forward=checkout.html";
				});

				$(".ui_content .to_checkout_btn").click(function(){
					location.href=ctx+"/register.html?forward=checkout.html";
				});
				
			}});
		},
		//检查活动商品是否一个人购买
		checkkillsecgoods:function(check_id,numprice1){
			$.Loading.show("请稍候...");
			var self=this;
			$.ajax({
				url:"api/shop/cart!checkSkillSecGoods.do?ajax=yes&check_id="+check_id,
				dataType:"json",
				success:function(result){
					$.Loading.hide();
					if(result.result==1){
						if(result.data==0){
							location.href="checkout.html?integratedprice="+numprice1;
						}else if(result.data==1){
							alert("每个用户只能限购一件秒杀商品");
						}else{
							alert("同IP或同用户不能购买！");
						}
					}else{
						$.alert("失败:"+result.message);
					}				 
				},
				error:function(){
					$.Loading.hide();
					$.alert("出错了:(");
				}
			});		
		},
		//删除一个购物项
		deleteGoodsItem:function(itemid){
			var self=this;
			$.Loading.show("请稍候...");
			$.ajax({
				url:"api/shop/cart!delete.do?ajax=yes",
				data:"cartid="+itemid,
				dataType:"json",
				success:function(result){
					if(result.result==1){
						self.refreshTotal(0);
						//self.removeItem(itemid);
					}else{
						$.alert(result.message);
					}	
					$.Loading.hide();
					
				},
				error:function(){
					$.Loading.hide();
					$.alert("出错了:(");
				}
			});
		},
		
		//移除商品项
		removeItem:function(itemid){
			$(".border tr[itemid="+itemid+"]").remove();
		},
		
		//清空购物车
		clean:function(){
			$.Loading.show("请稍候...");
			var self=this;
			$.ajax({
				url:"api/shop/cart!clean.do?ajax=yes",
				dataType:"json",
				success:function(result){
					$.Loading.hide();
					if(result.result==1){
						location.href='cart.html';
					}else{
						$.alert("清空失败:"+result.message);
					}				 
				},
				error:function(){
					$.Loading.hide();
					$.alert("出错了:(");
				}
			});		
		},
		
		//更新数量
		updateNum:function(itemid,num,productid,num_input){
			var self = this;
			$.ajax({
				url:"api/shop/cart!updateNum.do?ajax=yes",
				data:"cartid="+itemid +"&num="+num +"&productid="+productid,
				dataType:"json",
				success:function(result){
					if(result.result==1){
						if(result.store>=num){
							self.refreshTotal(1);
							var wholeNum=0;
							wholeNum = $("#whoId_"+itemid).val();
							var price=0;
							if(num>=wholeNum && wholeNum>0){
								price = parseFloat($("#whole_"+itemid).val());
							}else{
								price = parseFloat($("tr[itemid="+itemid+"]").attr("price"));
							}
							location.href=ctx+"/cart.html";
							var currency1=$("#cart_currency_p").val();
							//price =price* num;
							price1 =self.changeTwoDecimal_f(price* num);
							//alert(price);
							if(currency1=="CNY"){
								$("tr[itemid="+itemid+"] .itemTotal_jg").html("￥"+price);
								$("tr[itemid="+itemid+"] .itemTotal").html("￥"+price1);
							}else if(currency1=="RUB"){
								$("tr[itemid="+itemid+"] .itemTotal_jg").html(price+"p.");
								$("tr[itemid="+itemid+"] .itemTotal").html(price1+"р.");
							}
							num_input.val(num);
						}else{
							num_input.val(result.store);
							alert("抱歉！您所选择的货品库存不足。");
						}
					}else{
						alert("更新失败");
					}
				},
				error:function(){
					alert("出错了:(");
				}
			});		
		},
		updateLogisprice:function(freight_id,goods_id,num,store_id,sendprice){
			var self = this;
			$.ajax({
	 			url: "api/shop/logis!caculateLogisPrice1.do?ajax=yes",
	 			data:"freight_id="+freight_id+"&goods_id="+goods_id+"&num="+num+"&store_id="+store_id+"&sendprice="+sendprice,
	 			dataType : "json",
	 			success : function(result) {
	 				if(result!=null){
	 					if(result.result==1){
	 					     
	 					}else{
	 						alert("失败");
	 					}
	 				}
	 			}
	 		});
		},	
		//刷新价格
		refreshTotal:function(type){
			if(type==0){
				location.href=ctx+"/cart.html";
				return false;
			}
			var self = this;
			$.ajax({
				url:ctx+"cart/cartTotal.html",
				dataType:"html",
				success:function(html){
					$(".total_wrapper").html(html);
				},
				error:function(){
					//alert("糟糕，出错了:(");
				}
			});
		},
		
		changeTwoDecimal_f:function(x) {
	        var f_x = parseFloat(x);
	        if (isNaN(f_x)) {
	            alert('参数为非数字，无法转换！');
	            return false;
	        }
	        var f_x = Math.round(x * 100) / 100;
	        var s_x = f_x.toString();
	        var pos_decimal = s_x.indexOf('.');
	        if (pos_decimal < 0) {
	            pos_decimal = s_x.length;
	            s_x += '.';
	        }
	        while (s_x.length <= pos_decimal + 2) {
	            s_x += '0';
	        }
	        return s_x;
	    }
};

$(function(){
	Cart.init();
});