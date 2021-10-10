${r'<link rel="stylesheet" href="${basePath!}/plugins/materialize/css/materialize.min.css" >'}
${r'<link rel="stylesheet" href="${basePath!}/css/system_material.css">'}
${r'<script type="text/javascript" src="${basePath!}/js/jquery-1.12.4.min.js"></script>'}
${r'<script type="text/javascript" src="${basePath!}/js/jquery.formautofill.min.js"></script>'}
${r'<script type="text/javascript" src="${basePath!}/plugins/jquery-validation-1.17.0/jquery.validate.min.js"></script>'}
${r'<script type="text/javascript" src="${basePath!}/plugins/jquery-validation-1.17.0/localization/messages_zh.min.js"></script>'}
${r'<script type="text/javascript" src="${basePath!}/js/system.js"></script>'}
${r'<script type="text/javascript" src="${basePath!}/plugins/materialize/js/materialize.min.js"></script>'}
<form id="form_edit" class="col s12" action="">
	<input type="hidden" name="id" id="id"/>
	<div class="row">
		<#list table.fields as field>
		<#if field.propertyName!='id'>
		<#if field.propertyName=='state'>
		<div class="input-field switch-field col s12">
        	<label><#if field.comment!?length gt 0>${(field.comment)!}<#else>${(field.propertyName)}</#if></label>
	        <div class="switch">
			    <label>
			      	关
			      <input checked type="checkbox" name="state" value="1">
			      <span class="lever"></span>
			      	开
			    </label>
		     </div>
		</div>
		<#else>
		<div class="input-field col s12">
			<input type="${(field.propertyType=='Integer')?string('number','text')}" id="${field.propertyName}" name="${field.propertyName}" required >
			<label for="${field.propertyName}" ><#if field.comment!?length gt 0>${(field.comment)!}<#else>${(field.propertyName)}</#if></label>
		</div>
		</#if>
		</#if>
		</#list>
	</div>
</form>

<script type="text/javascript">
	$(function(){
		$("#form_edit").validate({
			showErrors:showErrors
		});
		$('select').material_select();
	})
	
	function init(id) {
		$.get('${r"${basePath!}"}/${table.entityPath}/info/'+id,function(result){
			$('#form_edit').autofill(result.data);
			Materialize.updateTextFields();
		});
		
	}
	
	function getData(){
		if ($('#form_edit').valid()) {
			return $('#form_edit').serialize();
		} else {
			return null;
		}
  	}
	
</script>
