<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="经方名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入经方名"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['app:typhoidtheoryprescription:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['app:typhoidtheoryprescription:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['app:typhoidtheoryprescription:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['app:typhoidtheoryprescription:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="typhoidtheoryprescriptionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" width="80" v-if="true"/>
      <el-table-column label="经方名" align="center" prop="name" />
      <el-table-column label="出处" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.app_typhoidtheory_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="经方组成" align="center" prop="compose" :show-overflow-tooltip="true" />
      <el-table-column label="炮制方法" align="center" prop="preparationMethod" :show-overflow-tooltip="true" />
      <el-table-column label="相关条文" align="center" prop="workCites[0].content" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['app:typhoidtheoryprescription:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['app:typhoidtheoryprescription:remove']"
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

    <!-- 添加或修改经方对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="经方名" prop="name">
          <el-input v-model="form.name" placeholder="请输入经方名" />
        </el-form-item>
        <el-form-item label="出处" prop="type">
          <el-select v-model="form.type" placeholder="请选择出处">
            <el-option
              v-for="dict in dict.type.app_typhoidtheory_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="经方组成" prop="compose">
          <el-input v-model="form.compose" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="炮制方法" prop="preparationMethod">
          <el-input v-model="form.preparationMethod" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="相关条文" prop="workCites">
          <el-row v-for="(item, index) in form.workCites" :key="index" style="margin-bottom: 10px;">
            <el-form-item style="margin-bottom: 10px;">
              <el-input v-model="form.workCites[index].content" type="textarea" placeholder="请输入相关条文内容" />
            </el-form-item>
            <el-form-item style="margin-bottom: 10px;">
              <el-input v-model="form.workCites[index].provenance" placeholder="请输入相关条文出处" />
            </el-form-item>
            <el-form-item style="margin-bottom: 10px;">
              <el-select v-model="form.workCites[index].type" placeholder="请选择出处">
                <el-option
                  v-for="dict in dict.type.app_typhoidtheory_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-button
              v-if="form.workCites.length != 0"
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              @click="delWorkCites(index)"
            >删除</el-button>
            <el-button
              v-if="index == form.workCites.length-1"
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="addWorkCites()"
            >新增</el-button>
          </el-row>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
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
import { listTyphoidtheoryprescription, getTyphoidtheoryprescription, delTyphoidtheoryprescription, addTyphoidtheoryprescription, updateTyphoidtheoryprescription } from "@/api/app/typhoidtheoryprescription";

export default {
  name: "Typhoidtheoryprescription",
  dicts: ['app_typhoidtheory_type'],
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
      // 经方表格数据
      typhoidtheoryprescriptionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
      },
      // 表单参数
      form: {
        workCites:[{}],
      },
      // 表单校验
      rules: {
        name: [
          { required: true, message: "经方名不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    addWorkCites() {
		  this.form.workCites.push({});
	  },
	  delWorkCites(index){
      if (this.form.workCites.length > 1) {
        this.form.workCites.splice(index, 1);
      } else {
        this.form.workCites = [{}];
      }
	  },
    /** 查询经方列表 */
    getList() {
      this.loading = true;
      listTyphoidtheoryprescription(this.queryParams).then(response => {
        this.typhoidtheoryprescriptionList = response.rows;
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
        workCites:[{}],
        id: undefined,
        name: undefined,
        type: undefined,
        compose: undefined,
        preparationMethod: undefined
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加经方";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getTyphoidtheoryprescription(id).then(response => {
        this.loading = false;
        this.form = response.data;
        if (this.form.workCites.length == 0) this.form.workCites = [{}];
        this.open = true;
        this.title = "修改经方";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateTyphoidtheoryprescription(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addTyphoidtheoryprescription(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除经方编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delTyphoidtheoryprescription(ids);
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
      this.download('app/typhoidtheoryprescription/export', {
        ...this.queryParams
      }, `typhoidtheoryprescription_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
