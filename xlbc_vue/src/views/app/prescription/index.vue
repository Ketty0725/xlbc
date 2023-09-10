<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="方剂名称" prop="prescriptionName">
        <el-input
          v-model="queryParams.prescriptionName"
          placeholder="请输入方剂名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属分类" prop="categoryId">
        <treeselect
          class="treeselect-main"
          v-model="queryParams.categoryId"
          :disable-branch-nodes="true"
          :options="prescriptioncategoryOptions"
          :normalizer="normalizer"
          :default-expand-level="1"
          clearable
          placeholder="请选择所属分类"/>    
      </el-form-item>
      <el-form-item label="出处" prop="provenance">
        <el-input
          v-model="queryParams.provenance"
          placeholder="请输入出处"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="功用" prop="efficacy">
        <el-input
          v-model="queryParams.efficacy"
          placeholder="请输入功用"
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
          v-hasPermi="['app:prescription:add']"
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
          v-hasPermi="['app:prescription:edit']"
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
          v-hasPermi="['app:prescription:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['app:prescription:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="prescriptionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="方剂编号" align="center" prop="id" v-if="true"/>
      <el-table-column label="方剂名称" align="center" prop="prescriptionName" />
      <el-table-column label="出处" align="center" prop="provenance" />
      <el-table-column label="所属分类" align="center" prop="category" />
      <el-table-column label="功用" align="center" prop="efficacy" />
      <el-table-column label="组成" align="center" prop="compose" :show-overflow-tooltip="true" />
      <el-table-column label="用法" align="center" prop="usageMethod" :show-overflow-tooltip="true" />
      <el-table-column label="主治" align="center" prop="attending" :show-overflow-tooltip="true" />
      <el-table-column label="使用注意" align="center" prop="notes" :show-overflow-tooltip="true" />
      <el-table-column label="歌诀" align="center" prop="songTips" :show-overflow-tooltip="true" />
      <el-table-column label="方义" align="center" prop="fangYi" :show-overflow-tooltip="true" />
      <el-table-column label="配伍特点" align="center" prop="matchingFeatures" :show-overflow-tooltip="true" />
      <el-table-column label="运用" align="center" prop="wield" :show-overflow-tooltip="true" />
      <el-table-column label="加减化裁" align="center" prop="additionAndSubtraction" :show-overflow-tooltip="true" />
      <el-table-column label="化裁方之间的鉴别" width="130" align="center" prop="tailoringIdentification" :show-overflow-tooltip="true" />
      <el-table-column label="文献摘要" align="center" prop="literatureAbstracts" :show-overflow-tooltip="true" />
      <el-table-column label="各家论述" align="center" prop="variousDiscussions" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['app:prescription:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['app:prescription:remove']"
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

    <!-- 添加或修改方剂数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="方剂名称" prop="prescriptionName">
          <el-input v-model="form.prescriptionName" placeholder="请输入方剂名称" />
        </el-form-item>
        <el-form-item label="出处" prop="provenance">
          <el-input v-model="form.provenance" placeholder="请输入出处" />
        </el-form-item>
        <el-form-item label="所属分类" prop="categoryId">
            <treeselect
                v-model="form.categoryId"
                :disable-branch-nodes="true"
                :options="prescriptioncategoryOptions"
                :normalizer="normalizer"
                :default-expand-level="1"
                placeholder="请选择所属分类"/>
        </el-form-item>
        <el-form-item label="功用" prop="efficacy">
          <el-input v-model="form.efficacy" placeholder="请输入功用" />
        </el-form-item>
        <el-form-item label="组成" prop="compose">
          <el-input v-model="form.compose" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="用法" prop="usageMethod">
          <el-input v-model="form.usageMethod" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="主治" prop="attending">
          <el-input v-model="form.attending" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="使用注意" prop="notes">
          <el-input v-model="form.notes" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="歌诀" prop="songTips">
          <el-input v-model="form.songTips" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="方义" prop="fangYi">
          <el-input v-model="form.fangYi" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="配伍特点" prop="matchingFeatures">
          <el-input v-model="form.matchingFeatures" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="运用" prop="wield">
          <el-input v-model="form.wield" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="加减化裁" prop="additionAndSubtraction">
          <el-input v-model="form.additionAndSubtraction" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="化裁方之间的鉴别" prop="tailoringIdentification">
          <el-input v-model="form.tailoringIdentification" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="文献摘要" prop="literatureAbstracts">
          <el-input v-model="form.literatureAbstracts" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="各家论述" prop="variousDiscussions">
          <el-input v-model="form.variousDiscussions" type="textarea" placeholder="请输入内容" />
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
  .treeselect-main {
  width: 205px;
  line-height: 31px;
  }
  .vue-treeselect__placeholder {
    line-height: 31px;
  }
  .vue-treeselect__control {
    height: 31px !important;
  }
</style>

<script>
import { listPrescription, getPrescription, delPrescription, addPrescription, updatePrescription } from "@/api/app/prescription";
import { listPrescriptioncategory } from "@/api/app/prescriptioncategory";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Prescription",
  components: {
    Treeselect
  },
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
      // 方剂数据表格数据
      prescriptionList: [],
      // 方剂分类树选项
      prescriptioncategoryOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        prescriptionName: undefined,
        provenance: undefined,
        categoryId: undefined,
        efficacy: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        prescriptionName: [
          { required: true, message: "方剂名称不能为空", trigger: "blur" }
        ],
        categoryId: [
          { required: true, message: "所属分类不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getTreeselect();
  },
  methods: {
    /** 转换方剂分类数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.categoryId,
        label: node.categoryName,
        children: node.children
      };
    },
	/** 查询方剂分类下拉树结构 */
    getTreeselect() {
      listPrescriptioncategory().then(response => {
        this.prescriptioncategoryOptions = [];
        const data = { categoryId: 0, categoryName: '无', children: [] };
        data.children = this.handleTree(response.data, "categoryId", "parentId");
        this.prescriptioncategoryOptions.push(data);
      });
    },
    /** 查询方剂数据列表 */
    getList() {
      this.loading = true;
      listPrescription(this.queryParams).then(response => {
        this.prescriptionList = response.rows;
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
        prescriptionName: undefined,
        provenance: undefined,
        category: undefined,
        efficacy: undefined,
        compose: undefined,
        usageMethod: undefined,
        attending: undefined,
        notes: undefined,
        songTips: undefined,
        fangYi: undefined,
        matchingFeatures: undefined,
        wield: undefined,
        additionAndSubtraction: undefined,
        tailoringIdentification: undefined,
        literatureAbstracts: undefined,
        variousDiscussions: undefined
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
      this.getTreeselect();
      this.open = true;
      this.title = "添加方剂数据";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      this.getTreeselect();
      const id = row.id || this.ids
      getPrescription(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改方剂数据";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updatePrescription(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addPrescription(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除方剂数据编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delPrescription(ids);
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
      this.download('app/prescription/export', {
        ...this.queryParams
      }, `prescription_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
