 function comment(resourceId){
	top.layer.open({
		  type: 2,
		  title: "<@spring.message 'Comment_area'/>",
		  closeBtn: 1, //不显示关闭按钮
		  shade: [0.2, '#393D49'],
		  area: ['20%', '90%'],
		  maxmin: true, //开启最大化最小化按钮
		  shadeClose:true,
		  offset: 'rb', //右下角弹出
		  anim: 2,
		  content: [basePath+'/comment/index/'+resourceId], //iframe的url，no代表不显示滚动条
		  end: function(){ //此处用于演示
		  }
		});
}
 
function comment(resourceId,title){
	top.layer.open({
		  type: 2,
		  title: title,
		  closeBtn: 1, //不显示关闭按钮
		  shade: [0.2, '#393D49'],
		  area: ['20%', '90%'],
		  maxmin: true, //开启最大化最小化按钮
		  shadeClose:true,
		  offset: 'rb', //右下角弹出
		  anim: 2,
		  content: [basePath+'/comment/index/'+resourceId], //iframe的url，no代表不显示滚动条
		  end: function(){ //此处用于演示
		  }
		});
}
//status ： 是否取消
 function collect(resourceId,status,ele){
		if(status){
			layer.confirm('您已经收藏过了，要取消收藏吗？', {
				btn:['确定','取消'],
				yes: function(index, layero){
			  		$.post(basePath + '/collect/removeCollect',{resourceId:resourceId},function(result){
			    		if (result.code==0) {
			    			$(ele).children('i').addClass('fa-star-o').removeClass('fa-star');
			    			layer.close(index);
				    		parent.layer.msg("取消成功！");
			    		} else{
			    			layer.close(index);
				    		parent.layer.msg("取消失败！");
			    		}
				    });
				}
			});
			return;
		}
		var isSubmit = false;
 	parent.layer.open({
			title: '添加收藏',
			type:2,
			content:basePath + '/collect/addCollect',
			area: ['600px', '500px'],
			btn:['确定','取消'],
			success: function(layero){
				var iframeWin = top.window[layero.find('iframe')[0]['name']]; 
			},
			yes:function(index,layero){
				if(!isSubmit){
		  			isSubmit = true;
		  			var colName;
		  			var colType = "report";
		  			//查询到resourceId的报表内容
		  			$.ajax({
				            type: "POST",
				            url:basePath + '/resource/list',
				            data:{id:resourceId},
				            async: false,
				            error: function(request) {
				            	parent.layer.msg('添加失败！');
				            },
				            success: function(result) {
				            	colName = result[0].name;
				            }
				        });
			  		var iframeWin = top.window[layero.find('iframe')[0]['name']]; 
					var data = iframeWin.getData();
					if (data!=null) {
				  		$.ajax({
				            type: "POST",
				            url:basePath + '/collect/save',
				            data:{parentId:data,resourceId:resourceId,collectName:colName,collectType:colType},//参数 parentId  collectName  resourceId collectType
				            async: false,
				            error: function(request) {
				                parent.layer.msg('添加失败！');
				                isSubmit = false;
				            },
				            success: function(result) {
				            	if(result.code==0){
				            		parent.layer.close(index);
				            		$(ele).children('i').addClass('fa-star').removeClass('fa-star-o');
									parent.layer.msg('添加成功！');
				            	}else{
				            		parent.layer.msg('文件已经存在！');
				            		isSubmit = false;
				            	}
				            }
				        });
					}else{
						isSubmit = false;
					}
		  		}
 		},
		});
	}
function showPermissionData(resourceId,resourceName){
	parent.layer.open({
		title: "查看["+resourceName+"]权限用户",
		type:2,
		content: basePath + '/rolePermission/rolePermissionUser?resourceId='+resourceId,
		area: ['50%', '60%'],
		btn:['确定'],
		success: function(layero){
			var iframeWin = top.window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
			iframeWin.init(resourceId);
		},
	});
}