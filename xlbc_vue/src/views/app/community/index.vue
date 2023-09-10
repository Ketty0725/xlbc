<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户名" prop="uName">
        <el-input
          v-model="queryParams.uName"
          placeholder="请输入用户名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="ip归属地" prop="ipAddress">
        <el-input
          v-model="queryParams.ipAddress"
          placeholder="请输入ip归属地"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="逻辑删除" prop="deleted">
            <el-select
              v-model="queryParams.deleted"
              placeholder="逻辑删除"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in dict.type.app_deleted_status"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          plain
          icon="el-icon-document"
          size="mini"
          :disabled="single"
          @click="handleInfo"
        >详情</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['app:community:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['app:community:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="communityList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="社区笔记id" align="center" prop="id" v-if="false"/>
      <el-table-column label="封面" align="center" prop="images" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.images[0].imageUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="标题" align="center" prop="title" :show-overflow-tooltip="true" />
      <el-table-column label="内容" align="center" prop="content" :show-overflow-tooltip="true" />
      <el-table-column label="发布时间" align="center" prop="createTime" />
      <el-table-column label="ip归属地" align="center" prop="ipAddress" />
      <el-table-column label="点赞数量" align="center" prop="likeCount" />
      <el-table-column label="收藏数量" align="center" prop="collectCount" />
      <el-table-column label="用户名" align="center" prop="user.uName" />
      <el-table-column label="逻辑删除" align="center" prop="deleted">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.app_deleted_status" :value="scope.row.deleted"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-document"
            @click="handleInfo(scope.row)"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['app:community:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="图片" prop="images">
          <el-row>
            <el-col :span="8" v-for="(item, index) in form.images" :key="index">
              <image-preview :src="item.imageUrl" :width="100" :height="100"/>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input :value="form.title" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input :value="form.content" type="textarea" />
        </el-form-item>
        <el-form-item label="发布时间" prop="createTime">
          <el-input :value="form.createTime" />
        </el-form-item>
        <el-form-item label="ip归属地" prop="ipAddress">
          <el-input :value="form.ipAddress" />
        </el-form-item>
        <el-form-item label="点赞数量" prop="likeCount">
          <el-input :value="form.likeCount" />
        </el-form-item>
        <el-form-item label="收藏数量" prop="collectCount">
          <el-input :value="form.collectCount" />
        </el-form-item>
        <el-form-item label="用户名" prop="user.uName">
          <el-input :value="form.user.uName" />
        </el-form-item>
        <el-form-item label="逻辑删除" prop="deleted">
          <dict-tag :options="dict.type.app_deleted_status" :value="form.deleted"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="cancel">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style>
  .el-tooltip__popper{
    max-width:50%
  }
</style>

<script>
import { listCommunity, getCommunity, delCommunity, addCommunity, updateCommunity } from "@/api/app/community";

export default {
  name: "Community",
  dicts: ['app_deleted_status'],
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 笔记表格数据
      communityList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        ipAddress: undefined,
        deleted: undefined,
        uName: undefined
      },
      // 表单参数
      form: {
        user: {},
        images: [{}]
      },
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询笔记列表 */
    getList() {
      this.loading = true;
      listCommunity(this.queryParams).then(response => {
        this.communityList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        title: undefined,
        content: undefined,
        time: undefined,
        ipAddress: undefined,
        likeCount: undefined,
        collectCount: undefined,
        userId: undefined,
        deleted: undefined,
        createTime: undefined,
        updateTime: undefined,
        user: {},
        images: [{}]
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 详情按钮操作 */
    handleInfo(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getCommunity(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "笔记详情";
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除编号为"' + ids + '"的笔记？').then(() => {
        this.loading = true;
        return delCommunity(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('app/community/export', {
        ...this.queryParams
      }, `community_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
