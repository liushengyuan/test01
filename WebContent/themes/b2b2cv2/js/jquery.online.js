/*
此插件基于Jquery
插件名：jquery.Sonline(在线客服插件)
作者 似懂非懂
版本 2.0
Blog：www.haw86.com
*/
(function($){
	$.fn.Sonline = function(options){
        var opts = $.extend({}, $.fn.Sonline.defualts, options); 
		$.fn.setList(opts); //调用列表设置
		$.fn.Sonline.styleType(opts);
		if(opts.DefaultsOpen == false){
			$.fn.Sonline.closes(opts.Position,0);
		}
		//展开
		$("#SonlineBox > .openTrigger").live("click",function(){$.fn.Sonline.opens(opts);});
		//关闭
		$("#SonlineBox > .contentBox > .closeTrigger").live("click",function(){$.fn.Sonline.closes(opts.Position,"fast");});
		
		//Ie6兼容或滚动方式显示
		if ($.browser.msie && ($.browser.version == "6.0") && !$.support.style||opts.Effect==true) {$.fn.Sonline.scrollType();}
		else if(opts.Effect==false){$("#SonlineBox").css({position:"fixed"});}
	}
	//plugin defaults
	$.fn.Sonline.defualts ={
		Position:"left",//left或right
		Top:300,//顶部距离，默认200px
	   
		Effect:true, //滚动或者固定两种方式，布尔值：true或
		Width:80,//顶部距离，默认200px
		DefaultsOpen:true, //默认展开：true,默认收缩：false
		Style:1,//图标的显示风格，默认显示:1
		Tel:"",//服务热线
		Title:"",//服务热线
		FooterText:'',
		Website:'',
		Qqlist:"" //多个QQ用','隔开，QQ和客服名用'|'隔开
	}
	
	//展开
	$.fn.Sonline.opens = function(opts){
		var positionType = opts.Position;
		$("#SonlineBox").css({width:opts.Width+4});
		if(positionType=="left"){$("#SonlineBox > .contentBox").animate({left: 0},"fast");}
		else if(positionType=="right"){$("#SonlineBox > .contentBox").animate({right: 0},"fast");}
		$("#SonlineBox > .openTrigger").hide();
	}

	//关闭
	$.fn.Sonline.closes = function(positionType,speed){
		$("#SonlineBox > .openTrigger").show();
		var widthValue =$("#SonlineBox > .openTrigger").width();
		var allWidth =(-($("#SonlineBox > .contentBox").width())-6);
		if(positionType=="left"){$("#SonlineBox > .contentBox").animate({left: allWidth},speed);}
		else if(positionType=="right"){$("#SonlineBox > .contentBox").animate({right: allWidth},speed);}
		$("#SonlineBox").animate({width:widthValue},speed);
		
	}
	
	//风格选择
	$.fn.Sonline.styleType = function(opts){
		var typeNum = 1;
		switch(opts.Style)
	   　　{ case 1:
				typeNum = 41;
	 　　    break
	 		 case 2:
				typeNum = 42;
	　　     break
	 		 case 3:
				typeNum = 44;
	　　     break
	 		 case 4:
				typeNum = 45;
	　　     break
	 		 case 5:
				typeNum = 46;
	　　     break
	 		 case 6:
				typeNum = 47;
	　　     break
	　　     default:
				typeNum = 41;
	　　   }
		return typeNum;
	}

	//子插件：设置列表参数
	$.fn.setList = function(opts){
		$("body").append("<div class='SonlineBox' id='SonlineBox' style='top:-600px; position:absolute;'><div class='openTrigger' style='display:none' title=''></div><div class='contentBox'><div class='closeTrigger' title=''></div><div class='titleBox'><span>"+opts.Title+"</span></div><div class='listBox'></div><div class='tels'>"+opts.FooterText+"</div></div></div>");
		$("#SonlineBox > .contentBox").width(opts.Width)
		if(opts.Qqlist==""){$("#SonlineBox > .contentBox > .listBox").append("<p style='padding:0px'></p>")}
		else{var qqListHtml = $.fn.Sonline.splitStr(opts);$("#SonlineBox > .contentBox > .listBox").append(qqListHtml);	}
		if(opts.Position=="left"){$("#SonlineBox").css({left:0});}
		else if(opts.Position=="right"){$("#SonlineBox").css({right:0})}
		$("#SonlineBox").css({top:opts.Top,width:30});
		var allHeights=0;
		if($("#SonlineBox > .contentBox").height() < $("#SonlineBox > .openTrigger").height()){
			allHeights = $("#SonlineBox > .openTrigger").height()+4;
		} else{allHeights = $("#SonlineBox > .contentBox").height()+40;}
		$("#SonlineBox").height(204);
		if(opts.Position=="left"){$("#SonlineBox > .openTrigger").css({left:0});}
		else if(opts.Position=="right"){$("#SonlineBox > .openTrigger").css({right:0});}
	}
	
	//滑动式效果
	$.fn.Sonline.scrollType = function(){
		$("#SonlineBox").css({position:"absolute"});
		var topNum = parseInt($("#SonlineBox").css("top")+"");
		$(window).scroll(function(){
			var scrollTopNum = $(window).scrollTop();//获取网页被卷去的高
			$("#SonlineBox").stop(true,false).delay(200).animate({top:scrollTopNum+topNum},"slow");
		});
	}
	
	//分割QQ
	$.fn.Sonline.splitStr = function(opts){
		
		var strs= new Array(); //定义一数组
		var QqlistText = opts.Qqlist;
		strs=QqlistText.split(","); //字符分割
		var alt = "";
		var msn = opts.Website+"/Public/Images/online/msn.gif";
		var QqHtml=""
		for (var i=0;i<strs.length;i++){	
			var subStrs= new Array(); //定义一数组
			var subQqlist = strs[i];
			subStrs = subQqlist.split("|"); //字符分割
			var type = parseInt(subStrs[2]);
			QqHtml += "<div class='QQList'><span>"+subStrs[1]+"：</span><div class='ico'>";
			switch(type){
				case 2://淘宝旺旺
					QqHtml += "<a target='_blank' href='http://amos1.taobao.com/msg.ww?v=2&uid="+subStrs[0]+"&s=1' >";
					QqHtml += "<img border='0' src='http://amos1.taobao.com/online.ww?v=2&uid="+subStrs[0]+"&s=1' alt='"+alt+"' title='"+alt+"' />";
					QqHtml += "</a>";
					break;
				case 3://阿里旺旺
					QqHtml += "<a target='_blank' href='http://amos.im.alisoft.com/msg.aw?v=2&amp;uid="+subStrs[0]+"&amp;site=cnalichn&amp;s=4'>";
					QqHtml += "<img alt='"+alt+"' title='"+alt+"' border='0' src='http://amos.im.alisoft.com/online.aw?v=2&amp;uid="+subStrs[0]+"&amp;site=cnalichn&amp;s=4' />";
					QqHtml += "</a>";
					break;
				case 6://阿里旺旺国际版
					QqHtml += "<a target='_blank' href='http://amos.alicdn.com/msg.aw?v=2&amp;uid="+subStrs[0]+"&amp;site=enaliint&amp;s=24&amp;charset=UTF-8' ";
					QqHtml += "  style='text-align:center;' data-uid='"+subStrs[0]+"'>";
					QqHtml += "<img style='border:none;vertical-align:middle;margin-right:5px;' ";
					QqHtml += " src='http://amos.alicdn.com/online.aw?v=2&amp;uid="+subStrs[0]+"&amp;site=enaliint&amp;s=22&amp;charset=UTF-8'>";
					QqHtml += ""+subStrs[0]+"</a>";
					break;
				case 4://微软MSN
					QqHtml += "<a target=blank href='msnim:chat?contact="+subStrs[0]+"&Site="+subStrs[0]+"'>";
					QqHtml += "<img src='"+msn+"' alt='"+alt+"' title='"+alt+"'/>";
					QqHtml += "</a>";
					break;
				case 5://Skype
					QqHtml += "<a target=blank href='callto://"+subStrs[0]+"'>";
					QqHtml += "<img border='0' src='http://mystatus.skype.com/smallclassic/"+subStrs[0]+"' alt='"+alt+"' title='"+alt+"'/>";
					QqHtml += "</a>";
					break;
				case 7://自定义类型
					QqHtml +=  subStrs[0];
					break;
				case 1: //QQ
				default:
					QqHtml += "<a target='_blank' href='http://wpa.qq.com/msgrd?v=3&uin="+subStrs[0]+"&site=qq&menu=yes'>";
					QqHtml += "<img border='0' src='http://wpa.qq.com/pa?p=2:"+subStrs[0]+":"+$.fn.Sonline.styleType(opts)+" &amp;r=0.22914223582483828' alt='"+alt+"'  title='"+alt+"'>";
					QqHtml += "</a>";
			}
			QqHtml += "</div><div style='clear:both;'></div></div>";
		}
		return QqHtml;
	}
})(jQuery);    


 