${r'<#include "common/common.html" />'}
<form id="form_add" lay-filter="form_add" class="layui-form " action="">
	<#list table.fields as field>
	<#if field.propertyName!='id'>
	<#if field.propertyName=='state'>
	<div class="layui-form-item">
		<label class="layui-form-label">状态</label>
	    <div class="layui-input-block">
	      <input type="checkbox" value=1 lay-text="开启|关闭" name="state" lay-skin="switch">
	    </div>
	</div>
	<#elseif field.propertyName=='remark'>
	<div class="layui-form-item">
		<label class="layui-form-label">备注</label>
	    <div class="layui-input-block">
	    	<textarea name="remark" placeholder="请输入内容" class="layui-textarea" lay-verify="textarea" lay-verType="tips"></textarea>
	    </div>
	</div>
	<#else>
	<div class="layui-form-item">
		<label class="layui-form-label"><#if field.comment!?length gt 0>${(field.comment)!}<#else>${(field.propertyName)}</#if></label>
	    <div class="layui-input-block" >
	      <input type="${(field.propertyType=='Integer')?string('number','text')}" id="${field.propertyName}" name="${field.propertyName}"  lay-verify="required" lay-verType="tips" class="layui-input" placeholder="请输入内容">
	    </div>
	</div>
	</#if>
	</#if>
	</#list>
	<button class="layui-hide" lay-submit  id="btn_submit" lay-filter="btn_submit">保存</button>
</form>

<script type="text/javascript">
	var is_verify = false;
	layui.use([ 'form'],function() {
		var form = layui.form;
		form.verify({
			  length: function(value,item){
				  if(value.length<2 || value.length>30){
				      return '请输入2至30个字符';
				  }
			  },
			  textarea: function(value,item){
				  if(value.length>250){
				      return '长度不能超过250';
				  }
			  }
			})  
		form.on('submit(btn_submit)', function(data){
			is_verify = true;
			return false;
		})
	});
	
	function init (type,typeCase) {
		
	}
	
  	
	function getData(){
		$('#btn_submit').click();
		if (is_verify) {
			return $('#form_add').serialize();
		} else {
			return null;
		}
  	}
	
	
</script>
