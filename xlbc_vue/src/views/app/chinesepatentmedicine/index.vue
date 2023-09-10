<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="药名" prop="drugName">
        <el-input
          v-model="queryParams.drugName"
          placeholder="请输入药名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="处方" prop="prescription">
        <el-input
          v-model="queryParams.prescription"
          placeholder="请输入处方"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="主治" prop="attending">
        <el-input
          v-model="queryParams.attending"
          placeholder="请输入主治"
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
          v-hasPermi="['app:chinesepatentmedicine:add']"
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
          v-hasPermi="['app:chinesepatentmedicine:edit']"
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
          v-hasPermi="['app:chinesepatentmedicine:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['app:chinesepatentmedicine:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="chinesepatentmedicineList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" width="80" v-if="true"/>
      <el-table-column label="药名" align="center" prop="drugName" />
      <el-table-column label="处方" align="center" prop="prescription" :show-overflow-tooltip="true" />
      <el-table-column label="制法" align="center" prop="preparationMethod" :show-overflow-tooltip="true" />
      <el-table-column label="主治" align="center" prop="attending" :show-overflow-tooltip="true" />
      <el-table-column label="性状" align="center" prop="characters" :show-overflow-tooltip="true" />
      <el-table-column label="用法与用量" align="center" prop="usageAndDosage" :show-overflow-tooltip="true" />
      <el-table-column label="注意事项" align="center" prop="notes" :show-overflow-tooltip="true" />
      <el-table-column label="规格" align="center" prop="specification" :show-overflow-tooltip="true" />
      <el-table-column label="贮藏" align="center" prop="storeUp" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['app:chinesepatentmedicine:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['app:chinesepatentmedicine:remove']"
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

    <!-- 添加或修改中成药数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="药名" prop="drugName">
          <el-input v-model="form.drugName" placeholder="请输入药名" />
        </el-form-item>
        <el-form-item label="处方" prop="prescription">
          <el-input v-model="form.prescription" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="制法" prop="preparationMethod">
          <el-input v-model="form.preparationMethod" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="主治" prop="attending">
          <el-input v-model="form.attending" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="性状" prop="characters">
          <el-input v-model="form.characters" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="用法与用量" prop="usageAndDosage">
          <el-input v-model="form.usageAndDosage" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="注意事项" prop="notes">
          <el-input v-model="form.notes" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="规格" prop="specification">
          <el-input v-model="form.specification" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="贮藏" prop="storeUp">
          <el-input v-model="form.storeUp" type="textarea" placeholder="请输入内容" />
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
import { listChinesepatentmedicine, getChinesepatentmedicine, delChinesepatentmedicine, addChinesepatentmedicine, updateChinesepatentmedicine } from "@/api/app/chinesepatentmedicine";

export default {
  name: "Chinesepatentmedicine",
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
      // 中成药数据表格数据
      chinesepatentmedicineList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        drugName: undefined,
        prescription: undefined,
        attending: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        drugName: [
          { required: true, message: "药名不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询中成药数据列表 */
    getList() {
      this.loading = true;
      listChinesepatentmedicine(this.queryParams).then(response => {
        this.chinesepatentmedicineList = response.rows;
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
        drugName: undefined,
        prescription: undefined,
        preparationMethod: undefined,
        attending: undefined,
        characters: undefined,
        usageAndDosage: undefined,
        notes: undefined,
        specification: undefined,
        storeUp: undefined
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
      this.title = "添加中成药数据";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getChinesepatentmedicine(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改中成药数据";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateChinesepatentmedicine(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addChinesepatentmedicine(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除中成药数据编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delChinesepatentmedicine(ids);
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
      this.download('app/chinesepatentmedicine/export', {
        ...this.queryParams
      }, `chinesepatentmedicine_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
