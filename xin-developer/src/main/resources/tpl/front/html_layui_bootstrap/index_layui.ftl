<!DOCTYPE html>
<html>
  <head>
    <title>${title!}</title>
    <meta http-equiv="keywords" content="${keywords!}">
    <meta http-equiv="description" content="${description!}">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
     ${r'<#include "common/common.html" />'}
     ${r'<#include "common/common_table.html" />'}
  </head>
  <body style="padding:10px;">
  	 <div >
     	<div class="layui-inline">
     		<button id="btn_add_${table.entityPath}" class="layui-btn layui-btn-sm layui-btn-normal">
			  <i class="layui-icon">&#xe654;</i> 新建
			</button>
	    </div>
    </div>
    <table id="table"></table>
  </body>
  <script>
  	
  	layui.use([ 'form','layer'],function() {
		var form = layui.form, 
			layer = layui.layer;
		
		$('#btn_add_${table.entityPath}').on('click',function(data){
			add${entity}();
		});
		
		
		
	  	$('#table').bootstrapTable({
            url: basePath + '/${table.entityPath}/page',         //请求后台的URL（*）
            contentType : 'application/x-www-form-urlencoded',
            method: 'post',                      //请求方式（*）
            striped: false,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortOrder: "desc",                   //排序方式
            queryParamsType:"",
            queryParams: queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            columns: [{
                field:'',
                title:'序号',
                width:'5px',
                halign: 'center',
                align: 'center',
                valign:'middle',
                formatter:indexFormatter
            }, 
            <#list table.fields as field>
            {
                field: '${field.propertyName}',
                title: '<#if field.comment!?length gt 0>${(field.comment)!}<#else>${(field.propertyName)}</#if>',
                halign: 'center',
                align: 'center',
                valign:'middle',
            },
            </#list>
            {
                field: 'opt',
                title: '操作',
                halign: 'center',
                align: 'center',
                valign:'middle',
                formatter:opeFormatter
            }],
            
        });
	  	
  	});
  	
  	function queryParams (params) {
  		var param = {
                pageSize: params.pageSize,
                pageNumber: params.pageNumber,
            };
  		return param;
  	}
  	
  	function opeFormatter(value,row,index){
		var v = '<div class="layui-btn-group">'
                        		+'<button class="layui-btn layui-btn-normal layui-btn-sm btn-opt" data-tip="编辑" onclick="javascript:edit${entity}(%)"><i class="layui-icon">&#xe642;</i></button>'
                        		+'<button class="layui-btn layui-btn-normal layui-btn-sm btn-opt" data-tip="删除" onclick="javascript:del${entity}(%)"><i class="layui-icon">&#xe640;</i></button>'
                        		+'</div>'; 
        return v.replace(new RegExp(/(%)/g),row.id);
	}
	
  	function add${entity}() {
    	parent.layer.open({
			title: '新建',
			type:2,
			content:basePath + '/${modelName!}/add',
			area: ['40%', '50%'],
			btn:['确定','取消'],
			success: function(layero){
				var iframeWin = top.window[layero.find('iframe')[0]['name']]; 
				iframeWin.init(tplHtml);
			},
			yes:function(index,layero){
    			var iframeWin = top.window[layero.find('iframe')[0]['name']]; 
				var data = iframeWin.getData();
				if (data!=null) {
					$.ajax({
		                type: "POST",
		                url:basePath+'/${modelName!}/save',
		                data:data,
		                async: false,
		                error: function(request) {
		                    parent.layer.msg('添加失败！');
		                },
		                success: function(result) {
		                	if(result.code==0){
		                		parent.layer.close(index);
								parent.layer.msg('添加成功！');
								reloadTable();
		                	}else{
		                		parent.layer.msg(result.msg);
		                	}
		                }
		            });
				}
   		    	
    		}
		});
    }
    
    function del${entity}(id,name) {
        parent.layer.confirm('删除课件？', {
			btn:['确定','取消'],
			yes: function(index, layero){
		  		$.post(basePath + '/${modelName!}/delete',{id:id},function(result){
		    		if(result.code==0){
                		parent.layer.close(index);
						reloadTable();
                	}else{
                		parent.layer.msg(result.msg);
                	}
		    		
			    });
	                
			}
        });
    }
    
    
    function edit${entity}(id) {
    	parent.layer.open({
			title: '编辑',
			type:2,
			content:basePath+'/${modelName!}/edit?id='+id,
			area: ['40%', '50%'],
			btn:['确定','取消'],
			success: function(layero){
				//得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
				var iframeWin = top.window[layero.find('iframe')[0]['name']]; 
				iframeWin.init(courseType, courseTypeCase);
			},
			yes:function(index,layero){
    			var iframeWin = top.window[layero.find('iframe')[0]['name']]; 
				var data = iframeWin.getData();
				if (data!=null) {
	   		    	$.ajax({
		                type: "POST",
		                url:basePath+'/${modelName!}/update',
		                data:data,
		                async: false,
		                error: function(request) {
		                    parent.layer.msg('编辑失败！');
		                },
		                success: function(result) {
		                	if(result.code==0){
		                		parent.layer.close(index);
								parent.layer.msg('编辑成功！');
								reloadTable();
		                	}else{
		                		parent.layer.msg(result.msg);
		                	}
		                	
		                }
		            });
	            }
    		}
		});
    }
    
    function reloadTable() {
    	$('#table').bootstrapTable('refresh', null);
    }
  </script>
</html>
