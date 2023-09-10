<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="手机号" prop="uPhone">
        <el-input
          v-model="queryParams.uPhone"
          placeholder="请输入手机号"
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
      <el-form-item label="性别" prop="uSex">
        <el-select v-model="queryParams.uSex" placeholder="请选择性别" clearable>
          <el-option
            v-for="dict in dict.type.app_sex_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="地址" prop="uAddress">
        <el-input
          v-model="queryParams.uAddress"
          placeholder="请输入地址"
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
          v-hasPermi="['app:user:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['app:user:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户编号" align="center" prop="uId" v-if="true"/>
      <el-table-column label="手机号" align="center" prop="uPhone" />
      <el-table-column label="用户名" align="center" prop="uName" />
      <el-table-column label="性别" align="center" prop="uSex">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.app_sex_type" :value="scope.row.uSex"/>
        </template>
      </el-table-column>
      <el-table-column label="头像" align="center" prop="uHeadicon" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.uHeadicon" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="生日" align="center" prop="uBirthday" />
      <el-table-column label="地址" align="center" prop="uAddress" />
      <el-table-column label="简介" align="center" prop="uAbout" />
      <el-table-column label="QQid" align="center" prop="uQqId" />
      <el-table-column label="QQ昵称" align="center" prop="uQqName" />
      <el-table-column label="背景图" align="center" prop="backgroundImage" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.backgroundImage" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="ip归属地" align="center" prop="ipAddress" />
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
            v-hasPermi="['app:user:remove']"
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
        <el-form-item label="用户编号" prop="uId">
          <el-input :value="form.uId" />
        </el-form-item>
        <el-form-item label="手机号" prop="uPhone">
          <el-input :value="form.uPhone" />
        </el-form-item>
        <el-form-item label="用户名" prop="uName">
          <el-input :value="form.uName" />
        </el-form-item>
        <el-form-item label="性别" prop="uSex">
          <dict-tag :options="dict.type.app_sex_type" :value="form.uSex"/>
        </el-form-item>
        <el-form-item label="头像" prop="uHeadicon">
          <image-preview :src="form.uHeadicon" :width="100" :height="100"/>
        </el-form-item>
        <el-form-item label="生日" prop="uBirthday">
          <el-input :value="form.uBirthday" />
        </el-form-item>
        <el-form-item label="地址" prop="uAddress">
          <el-input :value="form.uAddress" />
        </el-form-item>
        <el-form-item label="简介" prop="uAbout">
          <el-input :value="form.uAbout" type="textarea" />
        </el-form-item>
        <el-form-item label="QQid" prop="uQqId">
          <el-input :value="form.uQqId" />
        </el-form-item>
        <el-form-item label="QQ昵称" prop="uQqName">
          <el-input :value="form.uQqName" />
        </el-form-item>
        <el-form-item label="背景图" prop="backgroundImage">
          <image-preview :src="form.backgroundImage" :width="100" :height="100"/>
        </el-form-item>
        <el-form-item label="ip归属地" prop="ipAddress">
          <el-input :value="form.ipAddress" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="cancel">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUser, getUser, delUser } from "@/api/app/user";

export default {
  name: "User",
  dicts: ['app_sex_type'],
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
      // 用户表格数据
      userList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        uPhone: undefined,
        uName: undefined,
        uSex: undefined,
        uAddress: undefined,
        ipAddress: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listUser(this.queryParams).then(response => {
        this.userList = response.rows;
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
        uId: undefined,
        uPhone: undefined,
        uName: undefined,
        uSex: undefined,
        uPassword: undefined,
        uHeadicon: undefined,
        uBirthday: undefined,
        uAddress: undefined,
        uAbout: undefined,
        uQqId: undefined,
        uQqName: undefined,
        backgroundImage: undefined,
        ipAddress: undefined,
        createTime: undefined,
        updateTime: undefined
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
      this.ids = selection.map(item => item.uId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 详情按钮操作 */
    handleInfo(row) {
      this.loading = true;
      this.reset();
      const uId = row.uId || this.ids
      getUser(uId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "用户信息详情";
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const uIds = row.uId || this.ids;
      this.$modal.confirm('是否确认删除用户编号为"' + uIds + '"的数据项？').then(() => {
        this.loading = true;
        return delUser(uIds);
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
      this.download('app/user/export', {
        ...this.queryParams
      }, `user_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
