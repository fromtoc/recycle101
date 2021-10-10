<!DOCTYPE html>
<html>
<head>
<title>${title!}</title>
<meta http-equiv="keywords" content="${keywords!}">
<meta http-equiv="description" content="${description!}">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
${r'<link rel="stylesheet" href="${basePath!}/plugins/layui-2.4.3/css/layui.css">'}
${r'<link rel="stylesheet" href="${basePath!}/css/system_layui.css">'}

</head>
<body>
  	<#if cfg.hasSave>
  	<div id="tools_${table.entityPath}">
  		<button type="button" id="btn_add_${table.entityPath}" title="add"
			  class="layui-btn layui-btn-sm">
			<i class="layui-icon layui-icon-add-1"></i>
		</button>
	</div>
	</#if>
	<div id="query_form">
	</div>
	<table id="table" lay-filter="table">

	</table>
</body>
${r'<script type="text/javascript" src="${basePath!}/js/jquery-1.12.4.min.js"></script>'}
${r'<script type="text/javascript" src="${basePath!}/plugins/layui-2.4.3/layui.js"></script>'}
<script type="text/html" id="operateBar">
	<div class="layui-btn-group" >
		<button type="button" class="layui-btn layui-btn-sm " title="edit" lay-event="edit"><i class="layui-icon layui-icon-edit"></i></button>
		<button type="button" class="layui-btn layui-btn-sm " title="delete" lay-event="delete" ><i class="layui-icon layui-icon-delete"></i></button>
	</div>
</script>
<script>
  	var table${entity};
  	layui.use(['form', 'layer', 'table'],function() {
		var form = layui.form, 
			layer = layui.layer,
			table = layui.table;
		
		<#if cfg.hasSave>
		$('#btn_add_${table.entityPath}').on('click',function(data){
			add${entity}();
		});
		</#if>
		
		table${entity} = table.render({
			elem : '#table',
			url : '${r"${basePath!}"}/${table.entityPath}/page',
			page : true,
			request : {
				pageName : 'pageNumber', //页码的参数名称，默认：page
				limitName : 'pageSize' //每页数据量的参数名，默认：limit
			},
			parseData : function(res) { //res 即为原始返回的数据
				return {
					"code" : res.code, //解析接口状态
					"msg" : res.msg, //解析提示文本
					"count" : res.total, //解析数据长度
					"data" : res.rows //解析数据列表
				};
			},
			cols : [[ {
				type : 'numbers',
				title : '序号'
			}, 
			<#list table.fields as field>
            {
            	field: '${field.propertyName}',
                title: '<#if field.comment!?length gt 0>${(field.comment)!}<#else>${(field.propertyName)}</#if>',
                align : 'center',
				style : 'text-align:center',
				sort : true
            },
            </#list>
            <#if cfg.hasUpdate || cfg.hasDelete>
			{
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#operateBar'
			} 
			</#if>
			] ],
		});

		<#if cfg.hasUpdate || cfg.hasDelete>
		//监听工具条
		table.on('tool(table)',function(obj) {
			var data = obj.data;
			if (obj.event === 'delete') {
				delete${entity}(data);
			} else if (obj.event === 'edit') {
				edit${entity}(data)
			}
		});
		</#if>
	  	
  	});
  	
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
			                url: '${r"${basePath!}"}/${table.entityPath}/save',
			                data:data,
			                async: false,
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
    function edit${entity}(record) {
    	parent.layer.open({
			title: '编辑',
			type:2,
			content:'${r"${basePath!}"}/${table.entityPath}/edit',
			area: ['60%', '70%'],
			btn:['确定','取消'],
			success: function(layero){
				//得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
				var iframeWin = top.window[layero.find('iframe')[0]['name']]; 
				iframeWin.init(record);
			},
			yes:function(index,layero){
    			var iframeWin = top.window[layero.find('iframe')[0]['name']]; 
				var data = iframeWin.getData();
				if (data!=null) {
	   		    	$.ajax({
		                type: "POST",
		                url:'${r"${basePath!}"}/${table.entityPath}/update',
		                data:data,
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
    </#if>
    
    <#if cfg.hasDelete>
    function delete${entity}(record) {
    	parent.layer.confirm('确定删除？',{
			btn : ['确定','取消' ],
			yes : function(index,layero) {
				$.ajax({
						url : '${r"${basePath!}"}/${table.entityPath}/delete/'+ record.id,
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
    	table${entity}.reload({page : {curr : 1}});
    }
</script>
</html>
