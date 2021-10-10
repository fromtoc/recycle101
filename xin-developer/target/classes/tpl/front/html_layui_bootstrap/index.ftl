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
            url: '${r"${basePath!}"}/${table.entityPath}/page',         //请求后台的URL（*）
            contentType : 'application/x-www-form-urlencoded',
            method: 'get',                      //请求方式（*）
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
            uniqueId:'id',
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
            <#if cfg.hasUpdate || cfg.hasDelete>
            {
                field: 'opt',
                title: '操作',
                halign: 'center',
                align: 'center',
                valign:'middle',
                formatter:opeFormatter
            }
            </#if>
            ],
        });
	  	
  	});
  	
  	function queryParams (params) {
  		return params;
  	}
  	
  	<#if cfg.hasUpdate || cfg.hasDelete>
  	function opeFormatter(value,row,index){
  		var v = '<div class="layui-btn-group layui-btn-group-xs" >'
  		<#if cfg.hasUpdate>
  				+ '<button type="button" class="layui-btn layui-btn-normal layui-btn-sm" title="编辑" onclick="javascript:edit${entity}(\'%\')"><i class="layui-icon layui-icon-edit"></i></button>'
  		</#if>
  		<#if cfg.hasDelete>
  				+ '<button type="button" class="layui-btn layui-btn-normal layui-btn-sm" title="删除" onclick="javascript:delete${entity}(\'%\')" ><i class="layui-icon layui-icon-delete"></i></button>'
  		</#if>
  			  + '</div>';
  		return v.replace(new RegExp(/(%)/g),row.id);
		
  	} 
  	</#if>
	
  	<#if cfg.hasSave>
  	function add${entity}() {
  		var isSubmit = false;
    	parent.layer.open({
			title: '新建',
			type:2,
			content:'${r"${basePath!}"}/${table.entityPath}/add',
			area: ['60%', '70%'],
			btn:['确定','取消'],
			success: function(layero){
				var iframeWin = top.window[layero.find('iframe')[0]['name']]; 
				iframeWin.init();
			},
			yes:function(index,layero){
				if (!isSubmit) {
					isSubmit = true;
	    			var iframeWin = top.window[layero.find('iframe')[0]['name']]; 
					var data = iframeWin.getData();
					if (data!=null) {
						$.ajax({
			                type: "POST",
			                url:'${r"${basePath!}"}/${table.entityPath}/save',
			                data:data,
			                error: function(request) {
			                    parent.layer.msg('添加失败！');
			                    isSubmit = false;
			                },
			                success: function(result) {
			                	if(result.code==0){
			                		parent.layer.close(index);
									parent.layer.msg('添加成功！');
									reloadTable();
			                	}else{
			                		parent.layer.msg(result.msg);
			                		isSubmit = false;
			                	}
			                }
			            });
					} else {
						isSubmit = false;
					}
				}
    		}
		});
    }
    </#if>
    
    <#if cfg.hasUpdate>
    function edit${entity}(id) {
    	var isSubmit = false;
    	parent.layer.open({
			title: '编辑',
			type:2,
			content:'${r"${basePath!}"}/${table.entityPath}/edit',
			area: ['60%', '70%'],
			btn:['确定','取消'],
			success: function(layero){
				//得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
				var iframeWin = top.window[layero.find('iframe')[0]['name']]; 
				iframeWin.init($('#table').bootstrapTable('getRowByUniqueId',id));
			},
			yes:function(index,layero){
				if (!isSubmit) {
					isSubmit = true;
	    			var iframeWin = top.window[layero.find('iframe')[0]['name']]; 
					var data = iframeWin.getData();
					if (data!=null) {
		   		    	$.ajax({
			                type: "POST",
			                url:'${r"${basePath!}"}/${table.entityPath}/update',
			                data:data,
			                error: function(request) {
			                    parent.layer.msg('编辑失败！');
			                    isSubmit = false;
			                },
			                success: function(result) {
			                	if(result.code==0){
			                		parent.layer.close(index);
									parent.layer.msg('编辑成功！');
									reloadTable();
			                	}else{
			                		parent.layer.msg(result.msg);
			                		isSubmit = false;
			                	}
			                	
			                }
			            });
		            } else {
						isSubmit = false;
					}
				}
    		}
		});
    }
    </#if>
    
    <#if cfg.hasDelete>
    function delete${entity}(id){
		parent.layer.confirm('确定删除？',{
			btn : ['确定','取消' ],
			yes : function(index,layero) {
				$.ajax({
						url : '${r"${basePath!}"}/${table.entityPath}/delete/'+id,
						type : 'DELETE',
						success : function(result) {
							if (result.code == 0) {
								parent.layer.msg('删除成功');
								reloadTable();
							} else {
								parent.layer.msg('删除失败');
							}
							layer.close(index);
						}
				});
			}
		});
	}
    </#if>
    
    function reloadTable() {
    	$('#table').bootstrapTable('refresh', {pageNumber:1});
    }
  </script>
</html>
