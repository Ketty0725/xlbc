<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="药材名称" prop="medicinalName">
        <el-input
          v-model="queryParams.medicinalName"
          placeholder="请输入药材名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属分类" prop="categoryId">
        <treeselect
          class="treeselect-main"
          v-model="queryParams.categoryId"
          :disable-branch-nodes="true"
          :options="chineseherbcategoryOptions"
          :normalizer="normalizer"
          :default-expand-level="1"
          clearable
          placeholder="请选择所属分类"/>    
      </el-form-item>
      <el-form-item label="性味归经" prop="sexualTaste">
        <el-input
          v-model="queryParams.sexualTaste"
          placeholder="请输入性味归经"
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
          v-hasPermi="['app:chineseherb:add']"
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
          v-hasPermi="['app:chineseherb:edit']"
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
          v-hasPermi="['app:chineseherb:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['app:chineseherb:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="chineseherbList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="中药编号" align="center" prop="id" v-if="true"/>
      <el-table-column label="预览图" align="center" prop="images" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.images.image" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="药材名称" align="center" prop="medicinalName" />
      <el-table-column label="拼音" align="center" prop="pinYin" />
      <el-table-column label="始载于" align="center" prop="startedWith" />
      <el-table-column label="所属分类" align="center" prop="category" />
      <el-table-column label="别名" align="center" prop="aliases" />
      <el-table-column label="性味归经" align="center" prop="sexualTaste" />
      <el-table-column label="功效" align="center" prop="efficacy" :show-overflow-tooltip="true" />
      <el-table-column label="主治" align="center" prop="attending" :show-overflow-tooltip="true" />
      <el-table-column label="药材简介" align="center" prop="briefIntroduction" :show-overflow-tooltip="true" />
      <el-table-column label="用法用量" align="center" prop="usageAndDosage" :show-overflow-tooltip="true" />
      <el-table-column label="注意事项" align="center" prop="notes" :show-overflow-tooltip="true" />
      <el-table-column label="歌诀" align="center" prop="songTips" :show-overflow-tooltip="true" />
      <el-table-column label="临床应用" align="center" prop="clinicalApplication" :show-overflow-tooltip="true" />
      <el-table-column label="相关配伍" align="center" prop="relatedMatching" :show-overflow-tooltip="true" />
      <el-table-column label="现代炮制" align="center" prop="modernConcocted" :show-overflow-tooltip="true" />
      <el-table-column label="古法炮制" align="center" prop="ancientConcocted" :show-overflow-tooltip="true" />
      <el-table-column label="性状" align="center" prop="characters" :show-overflow-tooltip="true" />
      <el-table-column label="鉴别" align="center" prop="differential" :show-overflow-tooltip="true" />
      <el-table-column label="各家论述" align="center" prop="treatise" :show-overflow-tooltip="true" />
      <el-table-column label="药籍摘要" align="center" prop="summary" :show-overflow-tooltip="true" />
      <el-table-column label="分布区域" align="center" prop="distributionArea" :show-overflow-tooltip="true" />
      <el-table-column label="道地产区" align="center" prop="daoRealEstateDistrict" :show-overflow-tooltip="true" />
      <el-table-column label="生长环境" align="center" prop="growingEnvironment" :show-overflow-tooltip="true" />
      <el-table-column label="化学成分" align="center" prop="chemicalComposition" :show-overflow-tooltip="true" />
      <el-table-column label="药理作用" align="center" prop="pharmacologicalAction" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['app:chineseherb:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['app:chineseherb:remove']"
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

    <!-- 添加或修改中药数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="图片">
          <image-upload v-model="form.images.ossIdList"/>
        </el-form-item>
        <el-form-item label="药材名称" prop="medicinalName">
          <el-input v-model="form.medicinalName" placeholder="请输入药材名称" />
        </el-form-item>
        <el-form-item label="拼音" prop="pinYin">
          <el-input v-model="form.pinYin" placeholder="请输入拼音" />
        </el-form-item>
        <el-form-item label="始载于" prop="startedWith">
          <el-input v-model="form.startedWith" placeholder="请输入始载于" />
        </el-form-item>
        <el-form-item label="所属分类" prop="categoryId">
            <treeselect
                v-model="form.categoryId"
                :disable-branch-nodes="true"
                :options="chineseherbcategoryOptions"
                :normalizer="normalizer"
                :default-expand-level="1"
                placeholder="请选择所属分类"/>
        </el-form-item>
        <el-form-item label="别名" prop="aliases">
          <el-input v-model="form.aliases" placeholder="请输入别名" />
        </el-form-item>
        <el-form-item label="性味归经" prop="sexualTaste">
          <el-input v-model="form.sexualTaste" placeholder="请输入性味归经" />
        </el-form-item>
        <el-form-item label="功效" prop="efficacy">
          <el-input v-model="form.efficacy" placeholder="请输入功效" />
        </el-form-item>
        <el-form-item label="主治" prop="attending">
          <el-input v-model="form.attending" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="药材简介" prop="briefIntroduction">
          <el-input v-model="form.briefIntroduction" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="用法用量" prop="usageAndDosage">
          <el-input v-model="form.usageAndDosage" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="注意事项" prop="notes">
          <el-input v-model="form.notes" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="歌诀" prop="songTips">
          <el-input v-model="form.songTips" type="textarea" placeholder="请输入歌诀" />
        </el-form-item>
        <el-form-item label="临床应用" prop="clinicalApplication">
          <el-input v-model="form.clinicalApplication" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="相关配伍" prop="relatedMatching">
          <el-input v-model="form.relatedMatching" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="现代炮制" prop="modernConcocted">
          <el-input v-model="form.modernConcocted" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="古法炮制" prop="ancientConcocted">
          <el-input v-model="form.ancientConcocted" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="性状" prop="characters">
          <el-input v-model="form.characters" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="鉴别" prop="differential">
          <el-input v-model="form.differential" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="各家论述" prop="treatise">
          <el-input v-model="form.treatise" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="药籍摘要" prop="summary">
          <el-input v-model="form.summary" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="分布区域" prop="distributionArea">
          <el-input v-model="form.distributionArea" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="道地产区" prop="daoRealEstateDistrict">
          <el-input v-model="form.daoRealEstateDistrict" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="生长环境" prop="growingEnvironment">
          <el-input v-model="form.growingEnvironment" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="化学成分" prop="chemicalComposition">
          <el-input v-model="form.chemicalComposition" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="药理作用" prop="pharmacologicalAction">
          <el-input v-model="form.pharmacologicalAction" type="textarea" placeholder="请输入内容" />
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
import { listChineseherb, getChineseherb, delChineseherb, addChineseherb, updateChineseherb } from "@/api/app/chineseherb";
import { listChineseherbcategory } from "@/api/app/chineseherbcategory";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Chineseherb",
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
      // 中药数据表格数据
      chineseherbList: [],
      // 中药分类树选项
      chineseherbcategoryOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        medicinalName: undefined,
        categoryId: undefined,
        sexualTaste: undefined,
        efficacy: undefined,
      },
      // 表单参数
      form: {
        images: {}
      },
      // 表单校验
      rules: {
        medicinalName: [
          { required: true, message: "药材名称不能为空", trigger: "blur" }
        ],
        categoryId: [
          { required: true, message: "所属分类不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getTreeselect();
  },
  methods: {
    /** 转换中药分类数据结构 */
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
	/** 查询中药分类下拉树结构 */
    getTreeselect() {
      listChineseherbcategory().then(response => {
        this.chineseherbcategoryOptions = [];
        const data = { categoryId: 0, categoryName: '请选择分类', children: [] };
        data.children = this.handleTree(response.data, "categoryId", "parentId");
        this.chineseherbcategoryOptions.push(data);
      });
    },
    /** 查询中药数据列表 */
    getList() {
      this.loading = true;
      listChineseherb(this.queryParams).then(response => {
        this.chineseherbList = response.rows;
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
        images: {},
        id: undefined,
        categoryId: undefined,
        medicinalName: undefined,
        pinYin: undefined,
        startedWith: undefined,
        category: undefined,
        aliases: undefined,
        sexualTaste: undefined,
        efficacy: undefined,
        attending: undefined,
        briefIntroduction: undefined,
        usageAndDosage: undefined,
        notes: undefined,
        songTips: undefined,
        clinicalApplication: undefined,
        relatedMatching: undefined,
        modernConcocted: undefined,
        ancientConcocted: undefined,
        characters: undefined,
        differential: undefined,
        treatise: undefined,
        summary: undefined,
        distributionArea: undefined,
        daoRealEstateDistrict: undefined,
        growingEnvironment: undefined,
        chemicalComposition: undefined,
        pharmacologicalAction: undefined
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
      this.title = "添加中药数据";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      this.getTreeselect();
      const id = row.id || this.ids
      getChineseherb(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改中药数据";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateChineseherb(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addChineseherb(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除中药数据编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delChineseherb(ids);
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
      this.download('app/chineseherb/export', {
        ...this.queryParams
      }, `chineseherb_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
