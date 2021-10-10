${r'<#include "common/common.html" />'}
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
		<input type="hidden" name="id" value="${(record.id)!}"/>
		<div class="layui-form-item">
			<label class="layui-form-label" style="width:100px">课件名称</label>
		    <div class="layui-input-block" >
		      <input type="text" id="name" name="name" value="${(record.name)!}"  autocomplete="off" class="layui-input wide-input">
		    </div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label" style="width:100px">课件描述</label>
		    <div class="layui-input-block">
		    	<textarea name="desc" placeholder="请输入内容" class="layui-textarea wide-input">${(record.desc)!}</textarea>
		    </div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label" style="width:100px">开放</label>
		    <div class="layui-input-inline">
		    	<input type="radio" name="status" value="0" title="开放" ${r"${(record.status==0)?string('checked','')}"} />
      			<input type="radio" name="status" value="1" title="不开放" ${r"${(record.status==1)?string('checked','')}"}  />
		    </div>
		</div>
	</div>
</form>

<script type="text/javascript">

	function init () {
	}
	
	function check() {
		var selectors = ["#name"];
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
