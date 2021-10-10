<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    >
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <#list table.fields as field>
      <#if field.propertyName!='id'>
      <el-form-item label="<#if field.comment!?length gt 0>${(field.comment)!}<#else>${(field.propertyName)}</#if>" prop="${field.propertyName}">
        <#if field.propertyName=='state'>
        <el-radio-group v-model="dataForm.state">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>   
        </el-radio-group>
        <#elseif field.propertyName=='remark'>
        <el-input type="textarea" v-model="dataForm.remark"></el-input>
        <#else>
        	<#if field.propertyType == 'Integer' >
        	<el-input-number v-model="dataForm.${field.propertyName}" controls-position="right" :min="0" label="<#if field.comment!?length gt 0>${(field.comment)!}<#else>${(field.propertyName)}</#if>"></el-input-number>
        	<#else>
        	<el-input v-model="dataForm.${field.propertyName}" placeholder="请输入<#if field.comment!?length gt 0>${(field.comment)!}<#else>${(field.propertyName)}</#if>"></el-input>
        	</#if>
        </#if>
      </el-form-item>
      </#if>
      </#list>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button size="small" type="primary"  :disabled="isSubmit"  @click="dataFormSubmit()">确定</el-button>
      <el-button size="small" @click="visible = false">取消</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible:false,
        isSubmit: false,
        dataForm: {
          id: null,
<#list table.fields as field>
    <#if field.propertyName=='state'>
      	  ${field.propertyName}: 1,
    <#elseif field.propertyName!='id'>
      	  ${field.propertyName}: '',
    </#if>
</#list>
        },
        dataRule: {
          name: [
            { required: true, message: '名称不能为空', trigger: 'blur' }
          ],
        },
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || null
        this.visible = true
        this.$nextTick(() => {
            this.$refs['dataForm'].resetFields()
        })
        if (this.dataForm.id !== null) {
          // 修改
          this.Api.${table.entityPath}Info(this.dataForm.id).then(result => {
            this.dataForm= result.data.data
            
          })
        }

      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.isSubmit = true
            if (this.dataForm.id) {
              this.Api.${table.entityPath}Update(this.dataForm).then(result => {
                if (result.data && result.data.code === 0) {
                    this.$message({
                      message: '操作成功',
                      type: 'success',
                      duration: 1500,
                      onClose: () => {
                         this.visible = false
                         this.$emit('refreshDataList')
                         setTimeout(()=>{
                                this.isSubmit = false
                            },2000)
                     }
                   })
                 } else {
                    this.$message.error(result.data.msg)
                 }

              })
            } else {
              let params = this.dataForm
              delete params.id
              this.Api.${table.entityPath}Save(params).then(result => {
                if (result.data && result.data.code === 0) {
                   this.$message({
                     message: '操作成功',
                     type: 'success',
                     duration: 1500,
                     onClose: () => {
                       this.visible = false
                       this.$emit('refreshDataList')
                       setTimeout(()=>{
                                this.isSubmit = false
                            },2000)
                     }
                   })
                 } else {
                   this.$message.error(result.data.msg)
                 }
              })
            }
          }
        })
      }
    }
  }
</script>
