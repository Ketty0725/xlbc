<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="药品名称" prop="productId">
        <el-select v-model="queryParams.productId" placeholder="请选择药品" filterable clearable
          @keyup.enter.native="handleQuery">
          <el-option v-for="(item, index) in productList" :key="index" :label="item.name"
            :value="item.id"></el-option>
        </el-select>  
      </el-form-item>
      <el-form-item label="通用名" prop="commonName">
        <el-input
          v-model="queryParams.commonName"
          placeholder="请输入通用名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="品牌" prop="brand">
        <el-input
          v-model="queryParams.brand"
          placeholder="请输入品牌"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="生产企业" prop="company">
        <el-input
          v-model="queryParams.company"
          placeholder="请输入生产企业"
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
          v-hasPermi="['app:productbasicinfo:add']"
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
          v-hasPermi="['app:productbasicinfo:edit']"
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
          v-hasPermi="['app:productbasicinfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['app:productbasicinfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="productbasicinfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="通用名" align="center" prop="commonName" />
      <el-table-column label="品牌" align="center" prop="brand" />
      <el-table-column label="生产企业" align="center" prop="company" />
      <el-table-column label="批准文号" align="center" prop="approvalNumber" />
      <el-table-column label="包装规格" align="center" prop="specification" />
      <el-table-column label="产品剂型" align="center" prop="dosageForm" />
      <el-table-column label="用法" align="center" prop="useMethod" />
      <el-table-column label="使用剂量" align="center" prop="useDose" />
      <el-table-column label="有效期" align="center" prop="expirationDate" />
      <el-table-column label="适用人群" align="center" prop="targetUser" />
      <el-table-column label="详细说明书" width="90" align="center" prop="instructionBook" :show-overflow-tooltip="true" />
      <el-table-column label="图片" align="center" prop="images" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.images.image" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['app:productbasicinfo:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['app:productbasicinfo:remove']"
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

    <!-- 添加或修改药品说明对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="选择药品" prop="productId">
          <el-select v-model="form.productId" placeholder="请选择药品" filterable clearable
            :style="{width: '100%'}" :disabled="disabled">
            <el-option v-for="(item, index) in productList" :key="index" :label="item.name"
              :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="通用名" prop="commonName">
          <el-input v-model="form.commonName" placeholder="请输入通用名" />
        </el-form-item>
        <el-form-item label="品牌" prop="brand">
          <el-input v-model="form.brand" placeholder="请输入品牌" />
        </el-form-item>
        <el-form-item label="生产企业" prop="company">
          <el-input v-model="form.company" placeholder="请输入生产企业" />
        </el-form-item>
        <el-form-item label="批准文号" prop="approvalNumber">
          <el-input v-model="form.approvalNumber" placeholder="请输入批准文号" />
        </el-form-item>
        <el-form-item label="包装规格" prop="specification">
          <el-input v-model="form.specification" placeholder="请输入包装规格" />
        </el-form-item>
        <el-form-item label="产品剂型" prop="dosageForm">
          <el-input v-model="form.dosageForm" placeholder="请输入产品剂型" />
        </el-form-item>
        <el-form-item label="用法" prop="useMethod">
          <el-input v-model="form.useMethod" placeholder="请输入用法" />
        </el-form-item>
        <el-form-item label="使用剂量" prop="useDose">
          <el-input v-model="form.useDose" placeholder="请输入使用剂量" />
        </el-form-item>
        <el-form-item label="有效期" prop="expirationDate">
          <el-input v-model="form.expirationDate" placeholder="请输入有效期" />
        </el-form-item>
        <el-form-item label="适用人群" prop="targetUser">
          <el-input v-model="form.targetUser" placeholder="请输入适用人群" />
        </el-form-item>
        <el-form-item label="详细说明书" prop="instructionBook">
          <el-input v-model="form.instructionBook" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="图片">
          <image-upload v-model="form.images.ossIdList"/>
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
import { listProductbasicinfo, getProductbasicinfo, delProductbasicinfo, addProductbasicinfo, updateProductbasicinfo } from "@/api/app/productbasicinfo";
import { listAllProduct } from "@/api/app/product";

export default {
  name: "Productbasicinfo",
  data() {
    return {
      disabled: false,
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
      productList: [],
      // 药品说明表格数据
      productbasicinfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        productId: undefined,
        commonName: undefined,
        brand: undefined,
        company: undefined,
      },
      // 表单参数
      form: {
        images: {}
      },
      // 表单校验
      rules: {
        commonName: [
          { required: true, message: "通用名不能为空", trigger: "blur" }
        ],
        brand: [
          { required: true, message: "品牌不能为空", trigger: "blur" }
        ],
        company: [
          { required: true, message: "生产企业不能为空", trigger: "blur" }
        ],
        approvalNumber: [
          { required: true, message: "批准文号不能为空", trigger: "blur" }
        ],
        expirationDate: [
          { required: true, message: "有效期不能为空", trigger: "blur" }
        ],
        productId: [{
          required: true,
          message: '请选择药品',
          trigger: 'change'
        }],
      }
    };
  },
  created() {
    this.getList();
    this.getProductList();
  },
  methods: {
    getProductList() {
      listAllProduct().then(response => {
        this.productList = response.data;
      });
    },
    /** 查询药品说明列表 */
    getList() {
      this.loading = true;
      listProductbasicinfo(this.queryParams).then(response => {
        this.productbasicinfoList = response.rows;
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
        productId: undefined,
        commonName: undefined,
        brand: undefined,
        company: undefined,
        approvalNumber: undefined,
        specification: undefined,
        dosageForm: undefined,
        useMethod: undefined,
        useDose: undefined,
        expirationDate: undefined,
        targetUser: undefined,
        instructionBook: undefined
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
      this.title = "添加药品说明";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.disabled = true;
      this.reset();
      const id = row.id || this.ids
      getProductbasicinfo(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改药品说明";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateProductbasicinfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addProductbasicinfo(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除药品说明编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delProductbasicinfo(ids);
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
      this.download('app/productbasicinfo/export', {
        ...this.queryParams
      }, `productbasicinfo_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
