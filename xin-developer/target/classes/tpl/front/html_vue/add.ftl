${r'<#include "common/common.html" />'}
 //第一步：编写模版。你可以使用一个script标签存放模板，如：
<script id="template_select" type="text/html">
	<label class="layui-form-label" style="width:100px">{{ d.title }}</label>
    <div class="layui-input-inline">
      <select name="interest" lay-filter="aihao">
        <option value=""></option>
{{#  layui.each(d.list, function(index, item){ }}
        <option value="{{item.itemValue}}">{{item.itemName}}</option>
{{#  }); }}
      </select>
    </div>
</script>

 <style>
 	.my-label {
 		width:100px!important;
 	}
 	.wide-input {
 		width:85%;
 	}
 </style>

<form id="form_add" class="layui-form" action=""
	style="padding-top:10px;">
	<div>
		<div class="layui-form-item">
			<label class="layui-form-label" style="width:100px">课件名称</label>
		    <div class="layui-input-inline" >
		      <input type="text" id="name" name="name"  autocomplete="off" class="layui-input">
		    </div>
		</div>
		<div class="layui-form-item">
			<div id="type"></div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label" style="width:100px">备注</label>
		    <div class="layui-input-inline" style="width: 340px;">
		    	<textarea placeholder="请输入内容" class="layui-textarea" name="remark" id= "remark" ></textarea>
		    </div>
		</div>
	</div>
</form>

<script type="text/javascript">
	layui.use([ 'form', 'laytpl' ],function() {
		var form = layui.form, 
			laytpl = layui.laytpl;
	});
	
	function init (type,typeCase) {
		/第三步：渲染模版
		var data = { //数据
		  "title":"Layui常用模块"
		  ,"list":type
		}
		var getTpl = template_select_dict.innerHTML
		,view = document.getElementById('type');
		laytpl(getTpl).render(data, function(html){
		  view.innerHTML = html;
		});
		
	}
	
	function check() {
		var selectors = ["#dictName","#dictCode"];
		var result = true;
		$.each(selectors,function(index, rec){
			if (!checkField(rec)) {
				result = false;
				return false;//调出循环
			}
			
		});
  		return result;
  	}
  	
  	function checkField(selector){
  		var value = $(selector).val();
  		if (value==null || value == '') {
  			$(selector).addClass("layui-form-danger");
  			$(selector).focus();
  			layer.tips('请输入内容', selector);
  			return false;
  		} else {
  			return true;
  		}
  	}
	
	function getData(){
		if (check()) {
			return $('#form_add').serialize();
		} else {
			return null;
		}
  	}
	
	
</script>
