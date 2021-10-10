<template>
  <div class="mod-menu">
  	<#if cfg.hasSave>
    <el-form :inline="true" :model="dataForm">
      <el-form-item style="margin-bottom:5px">
        <el-button size="mini" v-if="hasPermission('${(cfg.module)!}:${table.entityPath}:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
    </el-form>
    </#if>
    <el-table
      v-loading.body="dataLoading"
      :data="dataList"
      border
      style="width: 100%;">
      <el-table-column
        type="index"
        width="50">
      </el-table-column>
      <#list table.fields as field>
      <#if field.propertyName!='id'>
      <el-table-column
        prop="${field.propertyName}"
        header-align="center"
        align="center"
        label="<#if field.comment!?length gt 0>${(field.comment)!}<#else>${(field.propertyName)}</#if>">
        <#if field.propertyName=='state'>
        <template slot-scope="scope">
          <el-tag v-if="scope.row.state === 0" size="small"  type="danger">禁用</el-tag>
          <el-tag v-else-if="scope.row.state === 1" size="small" type="success">启用</el-tag>
        </template>
        </#if>
      </el-table-column>
      </#if>
      </#list>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
        <#if cfg.hasUpdate>
          <el-button v-if="hasPermission('${(cfg.module)!}:${table.entityPath}:update')" size="mini" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
        </#if>
        <#if cfg.hasDelete>
          <el-button v-if="hasPermission('${(cfg.module)!}:${table.entityPath}:delete')" size="mini" type="danger" @click="deleteHandle(scope.row.id)">删除</el-button>
        </#if>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNumber"
        :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
    <#if cfg.hasSave || cfg.hasUpdate>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    </#if>
  </div>
</template>

<script>
<#if cfg.hasSave || cfg.hasUpdate>
  import AddOrUpdate from './${table.entityPath}-add-or-update'
</#if>
  export default {
    data () {
      return {
        dataForm: {},
        dataList: [],
        dataLoading: false,
        addOrUpdateVisible: false,
        total: 0,
        listQuery: {
          pageNumber: 1,
          pageSize: 10
        }
      }
    },
<#if cfg.hasSave || cfg.hasUpdate>
    components: {
      AddOrUpdate
    },
</#if>
    mounted() {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataLoading = true
        this.Api.${table.entityPath}Page(this.listQuery).then(result => {
          if (result.data.code===0 && result.data.total!=null) {
            this.total = result.data.total
            this.dataList = result.data.rows
            this.dataLoading = false
          }
        })
      },
      handleSizeChange(val) {
        this.listQuery.pageSize = val
        this.getDataList()
      },
      handleCurrentChange(val) {
        this.listQuery.pageNumber = val
        this.getDataList()
      },
      <#if cfg.hasSave || cfg.hasUpdate>
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      </#if>
      <#if cfg.hasDelete>
      // 删除
      deleteHandle (id, name) {
        this.$confirm('确定删除?', '提示', {
          cancelButtonClass: 'btn-custom-cancel',
          cancelButtonText: '取消',
          confirmButtonText: '确定',
          type: 'warning'
        }).then(() => {
          this.Api.${table.entityPath}Delete(id).then(result => {
            if (result.data && result.data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }).catch(() => {})
      }
      </#if>
    }
  }
</script>