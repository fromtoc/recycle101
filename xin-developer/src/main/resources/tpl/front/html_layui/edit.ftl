${r'<link rel="stylesheet" href="${basePath!}/plugins/layui-2.4.3/css/layui.css">'}
${r'<link rel="stylesheet" href="${basePath!}/css/system_layui.css">'}
<script type="text/javascript" src="${r'${basePath!}'}/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="${r'${basePath!}'}/plugins/layui-2.4.3/layui.js"></script>
<form id="form_edit" lay-filter="form_edit" class="layui-form " action="">
	<input type="hidden" name="id" id="id"/>
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
	
	function init (record) {
		/* 
		$.get('${basePath!}/${table.entityPath}/info/'+record.id,function(result){
			layui.form.val('form_edit',result.data);
		});
		 */
		var obj = Object.assign({}, record); 
		layui.form.val('form_edit',obj);
	}
	
  	
	function getData(){
		$('#btn_submit').click();
		if (is_verify) {
			return $('#form_edit').serialize();
		} else {
			return null;
		}
  	}
	
	
</script>
