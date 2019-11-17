<template>
  <a-drawer
    :title="title"
    :maskClosable="true"
    width="950"
    placement="right"
    :closable="true"
    @close="close"
    :visible="visible"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;"
  >
    <a-card :bordered="false" class="card-area">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <!-- 搜索区域 -->
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="9" :sm="12">
              <a-form-item label="用户账号" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
                <a-input placeholder="请输入名称查询" v-model="queryParam.roleName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="9" :sm="12">
              <a-form-item label="用户名称" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
                <a-input placeholder="请输入名称查询" v-model="queryParam.roleName"></a-input>
              </a-form-item>
            </a-col>

            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-col :md="6" :sm="24">
                <a-button type="primary" @click="searchQuery">查询</a-button>
                <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
              </a-col>
            </span>
          </a-row>
        </a-form>
      </div>

      <!-- 操作按钮区域 -->
      <div class="table-operator" style="margin-top: 5px">
        <a-button @click="handleAdd" type="primary" icon="plus">用户录入</a-button>
        <a-button type="primary" icon="download" @click="handleExportXls('角色信息')">用户编辑</a-button>
        <a-upload
          name="file"
          :showUploadList="false"
          :multiple="false"
          :headers="tokenHeader"
          :action="importExcelUrl"
          @change="handleImportExcel"
        >
          <a-button type="primary" icon="import">添加已有客户</a-button>
        </a-upload>

        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="batchDel">
              <a-icon type="delete" />删除
            </a-menu-item>
          </a-menu>
          <a-button style="margin-left: 8px">
            批量操作
            <a-icon type="down" />
          </a-button>
        </a-dropdown>
      </div>

      <!-- table区域-begin -->
      <div>
        <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
          <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择&nbsp;
          <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项&nbsp;&nbsp;
          <a style="margin-left: 24px" @click="onClearSelected">清空</a>
        </div>

        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
          @change="handleTableChange"
        >
          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>

            <a-divider type="vertical" />
            <a-dropdown>
              <a class="ant-dropdown-link">
                更多
                <a-icon type="down" />
              </a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a @click="handlePerssion(record.id)">授权</a>
                </a-menu-item>
                <a-menu-item>
                  <a @click="handleUser(record.id)">用户</a>
                </a-menu-item>
                <!-- <a-menu-item>
                  <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                    <a>删除</a>
                  </a-popconfirm>
                </a-menu-item>-->
              </a-menu>
            </a-dropdown>
          </span>
        </a-table>
      </div>
      <!-- table区域-end -->

    </a-card>
  </a-drawer>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'

export default {
  name: 'RoleList',
  mixins: [JeecgListMixin],
  components: {
    
  },
  data() {
    return {
      visible: false,
      description: '角色管理页面',
      title:"用户",

      // 查询条件
      queryParam: { roleName: '' },
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '用户账号',
          align: 'center',
          dataIndex: 'roleName'
        },
        {
          title: '用户名称',
          align: 'center',
          dataIndex: 'roleCode'
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'description'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/sys/role/list',
        delete: '/sys/role/delete',
        deleteBatch: '/sys/role/deleteBatch',
        exportXlsUrl: '/sys/role/exportXls',
        importExcelUrl: 'sys/role/importExcel'
      }
    }
  },
  computed: {
    importExcelUrl: function() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    }
  },
  methods: {
    handlePerssion: function(roleId) {
      // alert(roleId);
      this.$refs.modalUserRole.show(roleId)
    },
    onChangeDate(date, dateString) {
      console.log(date, dateString)
    },
    handleUser() {
      this.$refs.modalUserEdit.show()
    },
    show(roleId) {
      this.visible = true
    },
    close() {
      this.reset()
      this.$emit('close')
      this.visible = false
    },
    reset() {
      //   this.expandedKeysss = []
      //   this.checkedKeys = []
      //   this.defaultCheckedKeys = []
      this.loading = false
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>