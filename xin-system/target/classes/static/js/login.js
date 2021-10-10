var basePath = $('#basePath').val();
var flag = false;//是否点击登录的标志 
if(top.window.location.href != window.location.href){
		    top.location.replace(basePath + '/login');
		}
		//是否提交的标志
		function submit(){
			if(!flag){
				document.getElementById('loginForm').submit();
				flag = true;
			}
		}
		 
		function keyLogin(e){
			var e= e || window.event || arguments.callee.caller.arguments[0]; 
			var currKey=e.keyCode||e.which||e.charCode; 
			if (currKey==13)  //回车键的键值为13
				// submit(); //调用登录按钮的登录事件
				$("#btn_login").trigger("click");
		} 
		function changeImg() {
	        var imgSrc = document.getElementById("checkcode_img");
	        var src = imgSrc.src;
	        imgSrc.src=src+"?random="+Math.random();
	    };
	    $("#btn_login").on("click",function(){
	    	if(!flag){
	    		flag = true;
	    		if($('input[name="username"]').val()==null||$('input[name="username"]').val()==""){
	    			//$(".error_msg").text("用户名不能为空111");
                    $(".error_msg").text(usernameCannotBeEmpty);
	    			$(".error_msg").show();
	    			flag = false;
	    			return;
	    		}
	    		if($('#passwordInput').val()==null||$('#passwordInput').val()==""){
	    			$(".error_msg").text(passwordCannotBeEmpty); 
	    			$(".error_msg").show();
	    			flag = false;
	    			return;
	    		}
	    		if($('#passwordInput').val().length>64){
	    			$(".error_msg").text(passwordCannotMoreThan64); 
	    			$(".error_msg").show();
	    			flag = false;
	    			return;
	    		}
	    		
	    		if (!$('#check_code_div').is(":hidden")) {
	    			if( $('input[name="verifyCode"]').val()==null||$('input[name="verifyCode"]').val()==""){
	    				$(".error_msg").text(captchaCannotBeEmpty); 
	    				$(".error_msg").show();
	    				flag = false;
	    				return;
	    			}
	    		}
	    		$.ajax({
	    			type: "POST",
	    			url:basePath + '/encrypt/publicKey',
	    			async: false,
	    			error: function(request) {
	    				$(".error_msg").html(publicKeyCannotFind); 
	    				flag = false;
	    			},
	    			success: function(data) {
	    				var pubexponent =data.pubexponent;  
	    				var pubmodules =data.pubmodules;  
	    				setMaxDigits(200);    
	    				var key = new RSAKeyPair(pubexponent, "", pubmodules);   
	    				var password=$('#passwordInput').val();
	    				var encrypedPwd = encryptedString(key, encodeURIComponent(password));
	    				$('input[name="password"]').val(encrypedPwd);
	    				//开始登陆
	    				$("#loginForm").submit();
	    				flag = true;
	    			}
	    		});
	    	}
	    	 
	    })