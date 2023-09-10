<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户名" prop="uName">
        <el-input
          v-model="queryParams.uName"
          placeholder="请输入用户名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品名称" prop="pName">
        <el-input
          v-model="queryParams.pName"
          placeholder="请输入商品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="state">
        <el-select v-model="queryParams.state" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.app_orderform_status"
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
          type="success"
          plain
          icon="el-icon-position"
          size="mini"
          :disabled="send"
          @click="handleSend"
          v-hasPermi="['app:product:edit']"
        >发货</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['app:orderform:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table style="width: 100%" v-loading="loading" :data="orderformList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单编号" align="center" prop="id" width="170" v-if="true"/>
      <el-table-column label="用户名" align="center" prop="user.uName" />
      <el-table-column label="商品名称" align="center" prop="product.name" :show-overflow-tooltip="true" />
      <el-table-column label="商品图片" align="center" prop="product.oss" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.product.oss.url" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="收货地址" align="center" prop="address" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>{{"收货人:" + scope.row.address.name + "\n"
                + "联系电话:" + scope.row.address.phone + "\n"
                + "所在地区:" + scope.row.address.area + "\n"
                + "详细地址:" + scope.row.address.address}}</span>
        </template>
      </el-table-column>
      <el-table-column label="数量" align="center" prop="number">
        <template slot-scope="scope">
          <span>{{scope.row.number + "件"}}</span>
        </template>
      </el-table-column>
      <el-table-column label="总金额" align="center" prop="price">
        <template slot-scope="scope">
          <span>{{"￥" + scope.row.price}}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="state">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.app_orderform_status" :value="scope.row.state"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发货时间" align="center" prop="shipmentTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.shipmentTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="成交时间" align="center" prop="finishTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.finishTime) }}</span>
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
            v-if="scope.row.state == 0"
            size="mini"
            type="text"
            icon="el-icon-position"
            @click="handleSend(scope.row)"
            v-hasPermi="['app:orderform:edit']"
          >发货</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['app:orderform:remove']"
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

    <!-- 查看订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单编号" prop="id">
          <el-input :value="form.id" />
        </el-form-item>
        <el-form-item label="用户名" prop="user.uName">
          <el-input :value="form.user.uName" />
        </el-form-item>
        <el-form-item label="商品名称" prop="product.name">
          <el-input :value="form.product.name" />
        </el-form-item>
        <el-form-item label="商品图片" prop="product.oss">
          <image-preview :src="form.product.oss.url" :width="100" :height="100"/>
        </el-form-item>
        <el-form-item label="收货地址" prop="address">
          <el-input  type="textarea" :value="
                  '收货人:' + form.address.name + '\n'
                + '联系电话:' + form.address.phone + '\n'
                + '所在地区:' + form.address.area + '\n'
                + '详细地址:' + form.address.address" />
        </el-form-item>
        <el-form-item label="数量" prop="number">
          <el-input :value="form.number + '件'" />
        </el-form-item>
        <el-form-item label="总金额" prop="price">
          <el-input :value="'￥' + form.price" />
        </el-form-item>
        <el-form-item label="状态" prop="state">
          <dict-tag :options="dict.type.app_orderform_status" :value="form.state"/>
        </el-form-item>
        <el-form-item label="创建时间" prop="createTime">
          <el-input :value="form.createTime" />
        </el-form-item>
        <el-form-item label="发货时间" prop="shipmentTime">
          <el-input :value="form.shipmentTime" />
        </el-form-item>
        <el-form-item label="成交时间" prop="finishTime">
          <el-input :value="form.finishTime" />
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
import { listOrderform, getOrderform, delOrderform, addOrderform, updateOrderform } from "@/api/app/orderform";

export default {
  name: "Orderform",
  dicts: ['app_orderform_status'],
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
      send: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 订单管理表格数据
      orderformList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: undefined,
        productId: undefined,
        state: undefined,
        uName: undefined,
        pName: undefined,
      },
      // 表单参数
      form: {
        product: {
          oss: {}
        },
        user: {},
        address: {}
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
    /** 查询订单管理列表 */
    getList() {
      this.loading = true;
      listOrderform(this.queryParams).then(response => {
        this.orderformList = response.rows;
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
        userId: undefined,
        productId: undefined,
        addressId: undefined,
        number: undefined,
        price: undefined,
        state: undefined,
        createTime: undefined,
        updateTime: undefined,
        shipmentTime: undefined,
        finishTime: undefined,
        product: {
          oss: {}
        },
        user: {},
        address: {}
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
      if (selection.length!==1) {
        this.send = true
      } else {
        this.send = false
        for (let item of selection) {
          if (item.state !== 0) this.send = true
        }
      }
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 详情按钮操作 */
    handleInfo(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getOrderform(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "订单详情";
      });
    },
    /** 发货按钮操作 */
    handleSend(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('订单编号："' + ids + '"，是否确认发货？').then(() => {
        this.loading = true;
        return updateOrderform(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("发货成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除订单管理编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delOrderform(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
  }
};
</script>
