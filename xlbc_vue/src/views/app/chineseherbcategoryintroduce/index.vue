<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="中药分类" prop="categoryId">
        <el-select v-model="queryParams.categoryId" placeholder="请选择中药分类" filterable clearable
          @keyup.enter.native="handleQuery">
          <el-option v-for="(item, index) in categoryList" :key="index" :label="item.categoryName"
            :value="item.categoryId"></el-option>
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['app:chineseherbcategoryintroduce:add']"
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
          v-hasPermi="['app:chineseherbcategoryintroduce:edit']"
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
          v-hasPermi="['app:chineseherbcategoryintroduce:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['app:chineseherbcategoryintroduce:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="chineseherbcategoryintroduceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" width="80" v-if="true"/>
      <el-table-column label="释义" align="center" prop="paraphrase" :show-overflow-tooltip="true" />
      <el-table-column label="分类" align="center" prop="classify" :show-overflow-tooltip="true" />
      <el-table-column label="功用" align="center" prop="efficacy" :show-overflow-tooltip="true" />
      <el-table-column label="配伍特点" align="center" prop="matchingFeatures" :show-overflow-tooltip="true" />
      <el-table-column label="用药注意" align="center" prop="medicationAttention" :show-overflow-tooltip="true" />
      <el-table-column label="现代研究" align="center" prop="modernResearch" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['app:chineseherbcategoryintroduce:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['app:chineseherbcategoryintroduce:remove']"
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

    <!-- 添加或修改分类介绍对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="中药分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择中药分类" filterable clearable
            :style="{width: '100%'}" :disabled="disabled">
            <el-option v-for="(item, index) in categoryList" :key="index" :label="item.categoryName"
              :value="item.categoryId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="释义" prop="paraphrase">
          <el-input v-model="form.paraphrase" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="分类" prop="classify">
          <el-input v-model="form.classify" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="功用" prop="efficacy">
          <el-input v-model="form.efficacy" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="配伍特点" prop="matchingFeatures">
          <el-input v-model="form.matchingFeatures" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="用药注意" prop="medicationAttention">
          <el-input v-model="form.medicationAttention" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="现代研究" prop="modernResearch">
          <el-input v-model="form.modernResearch" type="textarea" placeholder="请输入内容" />
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
import { listChineseherbcategoryintroduce, getChineseherbcategoryintroduce, delChineseherbcategoryintroduce, addChineseherbcategoryintroduce, updateChineseherbcategoryintroduce } from "@/api/app/chineseherbcategoryintroduce";
import { listChineseherbcategoryByParent } from "@/api/app/chineseherbcategory";

export default {
  name: "Chineseherbcategoryintroduce",
  data() {
    return {
      disabled: false,
      parentId: 0,
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
      categoryList: [],
      // 分类介绍表格数据
      chineseherbcategoryintroduceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        categoryId: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        categoryId: [{
          required: true,
          message: '请选择中药分类',
          trigger: 'change'
        }],
      }
    };
  },
  created() {
    this.getList();
    this.getCategoryList();
  },
  methods: {
    getCategoryList() {
      listChineseherbcategoryByParent(this.parentId).then(response => {
        this.categoryList = response.data;
      });
    },
    /** 查询分类介绍列表 */
    getList() {
      this.loading = true;
      listChineseherbcategoryintroduce(this.queryParams).then(response => {
        this.chineseherbcategoryintroduceList = response.rows;
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
        categoryId: undefined,
        paraphrase: undefined,
        classify: undefined,
        efficacy: undefined,
        matchingFeatures: undefined,
        medicationAttention: undefined,
        modernResearch: undefined
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
      this.disabled = false;
      this.reset();
      this.open = true;
      this.title = "添加分类介绍";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.disabled = true;
      this.reset();
      const id = row.id || this.ids
      getChineseherbcategoryintroduce(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改分类介绍";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateChineseherbcategoryintroduce(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addChineseherbcategoryintroduce(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除分类介绍编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delChineseherbcategoryintroduce(ids);
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
      this.download('app/chineseherbcategoryintroduce/export', {
        ...this.queryParams
      }, `chineseherbcategoryintroduce_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
