<!DOCTYPE html>
<html>
<head>
<title>${title!}</title>
<meta http-equiv="keywords" content="${keywords!}">
<meta http-equiv="description" content="${description!}">
<meta content="initial-scale=1, shrink-to-fit=no, width=device-width" name="viewport">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
${r'<link href="${basePath!}/plugins/bootstrap-table-1.12.1/bootstrap.min.css" rel="stylesheet">'}
${r'<link href="${basePath!}/plugins/bootstrap-table-1.12.1/tether.min.css" rel="stylesheet">'}
${r'<link href="${basePath!}/plugins/materialize/css/roboto.css" rel="stylesheet">'}
${r'<link href="${basePath!}/plugins/materialize/css/icon.css" rel="stylesheet">'}
${r'<link href="${basePath!}/plugins/materialize/css/materialize.min.css" rel="stylesheet">'}
${r'<link href="${basePath!}/css/system_material.css" rel="stylesheet">'}
${r'<link href="${basePath!}/plugins/bootstrap-table-1.12.1/bootstrap-table.min.css" rel="stylesheet">'}
</head>
<body>
	<#if cfg.hasSave>
  	<div id="tools_${table.entityPath}">
  		<button type="button" id="btn_add_${table.entityPath}" title="add"
			class="btn btn-floating btn-sm">
			<i class="material-icons">add</i>
		</button>
  	</div>
  	</#if>
  	<div id="query_form">
  	
  	</div>
  	 <table id="table" >
           
     </table>
 ${r'<script type="text/javascript" src="${basePath!}/js/jquery-1.12.4.min.js"></script>'}
 <#if cfg.hasSave || cfg.hasUpdate || cfg.hasDelete>
 ${r'<script type="text/javascript" src="${basePath!}/plugins/layui-2.4.3/lay/modules/layer.js"></script>'}
 </#if>
 ${r'<script type="text/javascript" src="${basePath!}/js/system.js"></script>'}
 ${r'<script type="text/javascript" src="${basePath!}/plugins/materialize/js/materialize.min.js"></script>'}
 ${r'<script type="text/javascript" src="${basePath!}/plugins/bootstrap-table-1.12.1/tether.min.js"></script>'}
 ${r'<script type="text/javascript" src="${basePath!}/plugins/bootstrap-table-1.12.1/bootstrap.min.js"></script>'}
 ${r'<script type="text/javascript" src="${basePath!}/plugins/bootstrap-table-1.12.1/bootstrap-table.min.js"></script>'}
 ${r'<script type="text/javascript" src="${basePath!}/plugins/bootstrap-table-1.12.1/bootstrap-table-zh-CN.min.js"></script>'}
<script  type="text/javascript">
	$(function(){
		
		<#if cfg.hasSave>
		$('#btn_add_${table.entityPath}').on('click',function(data){
			add${entity}();
		});
		</#if>
		
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
            undefinedText:'',
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
                width:'100px',
                formatter:opeFormatter
            }],
            </#if>
        });
		
		
	  	
  	});
	
	
	function queryParams (params) {
  		return params;
  	}
  	
	<#if cfg.hasUpdate || cfg.hasDelete>
  	function opeFormatter(value,row,index){
  		var v = '<div class="btn-group btn-group-fluid center-align" role="group" >'
  		<#if cfg.hasUpdate>
  				+ '<button type="button" class="btn btn-flat btn-sm " title="edit" onclick="javascript:edit${entity}(\'%\')"><i class="material-icons">edit</i></button>'
  		</#if>
  		<#if cfg.hasDelete>
  				+ '<button type="button" class="btn btn-flat btn-sm " title="delete" onclick="javascript:delete${entity}(\'%\')" ><i class="material-icons">delete</i></button>'
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
				iframeWin.init(id);
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
  </body>
</html>
