var Order={
	init:function(){
		var self = this;
		//取消订单
		$(".cancelBtn").click(function(){
			var sn = $(this).attr("sn");
 			var html = $("#cancelForm").html();
 			var ordersn= $.trim("<div class='order_delsn'><span>номер заказа：</span><p>"+sn+"</p></div>");
			var dialog = $.dialog({
				title:"Отменить Заказ",content:ordersn+html,lock:true,
			});
			$(".ui_content .yellow_btn").jbtn().click(function(){
				var orderdelword = $(".ui_content").find("textarea ").val();
				$('#cancelForm').ajaxSubmit({
					url:"../api/shop/order!cancel.do?sn="+sn+"&reason="+orderdelword,
					type : "POST",
					dataType : 'json',
					success : function(data) {	
						if(data.result==1){
							alert("Отменить заказ успеха");
							location.href = "../member/member.html";
						}
						else{
							alert(data.message);
						}
					},
					 cache:false
				});	
	    	});
		});
		
		//确认收货
		$(".rogBtn").click(function(){
			var orderId = $(this).attr("orderid");
			if( confirm( "Пожалуйста, подтвердите, что вы получили товар, прежде чем выполнять эту операцию!" )){
				$.Loading.show("пожалуйста, подождите..."); 
				$.ajax({
					url:"../api/shop/order!rogConfirm.do?orderId="+orderId,
					dataType:"json",
					success:function(result){
						if(result.result==1){
							location.reload();
						}else{
							 
							$.alert(result.message);
							$.Loading.hide();
						}
						
					},
					error:function(){
						$.alert("неправильно:(");
					}
				});	
						
			}
		});
		//解冻积分
		$(".thawBtn").click(function(){
			var orderid = $(this).attr("orderid");
			$.confirm("После оттаивания заранее интеграции, интеграции, связанные с заказами замороженных продуктов, мы не сможем вернуть операций. Подтвердите, что вы хотите, чтобы растопить его?",
				function(){
					$.Loading.show("пожалуйста, подождите..."); 
					$.ajax({
						url:"../api/shop/returnorder!thaw.do?orderid="+orderid,
						dataType:"json",
						cache:false,
						success:function(result){
							if(result.result==1){
								location.reload();
							}else{
								$.Loading.hide(); 
								$.alert(result.message);
							}
						},error:function(){
							$.Loading.hide(); 
							$.alert("К сожалению, теперь не так тает непредвиденная ошибка");
						}
					});
				}	
			);
		
		});
		
	}

}