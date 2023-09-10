<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="药膳名称" prop="medicatedDietName">
        <el-input
          v-model="queryParams.medicatedDietName"
          placeholder="请输入药膳名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="食材" prop="foodMaterial">
        <el-input
          v-model="queryParams.foodMaterial"
          placeholder="请输入食材"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="功效" prop="efficacy">
        <el-input
          v-model="queryParams.efficacy"
          placeholder="请输入功效"
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
          v-hasPermi="['app:medicateddiet:add']"
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
          v-hasPermi="['app:medicateddiet:edit']"
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
          v-hasPermi="['app:medicateddiet:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['app:medicateddiet:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="medicateddietList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" width="80" v-if="true"/>
      <el-table-column label="药膳名称" align="center" prop="medicatedDietName" />
      <el-table-column label="出处" align="center" prop="derivation" />
      <el-table-column label="食材" align="center" prop="foodMaterial" :show-overflow-tooltip="true" />
      <el-table-column label="制法" align="center" prop="preparationMethod" :show-overflow-tooltip="true" />
      <el-table-column label="功效" align="center" prop="efficacy" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['app:medicateddiet:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['app:medicateddiet:remove']"
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

    <!-- 添加或修改药膳数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="药膳名称" prop="medicatedDietName">
          <el-input v-model="form.medicatedDietName" placeholder="请输入药膳名称" />
        </el-form-item>
        <el-form-item label="出处" prop="derivation">
          <el-input v-model="form.derivation" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="食材" prop="foodMaterial">
          <el-input v-model="form.foodMaterial" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="制法" prop="preparationMethod">
          <el-input v-model="form.preparationMethod" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="功效" prop="efficacy">
          <el-input v-model="form.efficacy" type="textarea" placeholder="请输入内容" />
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
import { listMedicateddiet, getMedicateddiet, delMedicateddiet, addMedicateddiet, updateMedicateddiet } from "@/api/app/medicateddiet";

export default {
  name: "Medicateddiet",
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
      // 药膳数据表格数据
      medicateddietList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        medicatedDietName: undefined,
        foodMaterial: undefined,
        efficacy: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        medicatedDietName: [
          { required: true, message: "药膳名称不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询药膳数据列表 */
    getList() {
      this.loading = true;
      listMedicateddiet(this.queryParams).then(response => {
        this.medicateddietList = response.rows;
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
        medicatedDietName: undefined,
        derivation: undefined,
        foodMaterial: undefined,
        preparationMethod: undefined,
        efficacy: undefined
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
      this.title = "添加药膳数据";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getMedicateddiet(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改药膳数据";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateMedicateddiet(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addMedicateddiet(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除药膳数据编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delMedicateddiet(ids);
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
      this.download('app/medicateddiet/export', {
        ...this.queryParams
      }, `medicateddiet_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
