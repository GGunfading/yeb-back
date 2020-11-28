<template>
  <div>
    <div>
      <el-input size="small" v-model="jl.name" style="width: 300px" prefix-icon="el-icon-plus"
                placeholder="添加职称..."></el-input>
      <el-select v-model="jl.titleLevel" palaceholder="职称等级" size="small" style="margin-left: 6px;margin-right: 6px">
        <el-option v-for="item in titleLevels"
                   :key="item"
                   :label="item"
                   :value="item">
        </el-option>
      </el-select>
      <el-button type="primary" icon="el-icon-plus" size="small" @click="addJl">添加</el-button>
    </div>
    <div style="margin-top: 10px">
      <el-table :data="jls"
                border
                stripe
                size="small"
                @selection-change="handleSelectionChange"
                style="width: 80%">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          prop="id"
          label="编号"
          width="55">
        </el-table-column>
        <el-table-column
          prop="name"
          label="职称名称"
          width="150">
        </el-table-column>
        <el-table-column
          prop="titleLevel"
          label="职称级别"
          width="150">
        </el-table-column>
        <el-table-column
          prop="createDate"
          label="创建时间"
          width="150">
        </el-table-column>
        <el-table-column prop="enabled"
                         label="是否启用"
                         width="150">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.enabled" type="success">已启用</el-tag>
            <el-tag v-else type="danger">未启用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="small" @click="showEditView(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteHandler(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button type="danger" size="small" style="margin-top: 10px" :disabled="multipleSelection.length == 0"
                 @click="deleteMany">批量删除
      </el-button>
    </div>
    <el-dialog
      title="编辑职称"
      :visible.sync="dialogVisible"
      width="30%">
      <div>
        <table>
          <tr>
            <td>
              <el-tag>职称名</el-tag>
            </td>
            <td>
              <el-input v-model="updateJl.name" size="small"></el-input>
            </td>
          </tr>
          <tr>
            <td>
              <el-tag>职称级别</el-tag>
            </td>
            <td>
              <el-select v-model="updateJl.titleLevel"
                         placeholder="职称等级" size="small"
                         style="margin-left: 6px;margin-right:6px">
                <el-option
                  v-for="item in titleLevels"
                  :key="item"
                  :label="item"
                  :value="item">
                </el-option>
              </el-select>
            </td>
          </tr>
          <tr>
            <td>
              <el-tag>是否启用</el-tag>
            </td>
            <td>
              <el-switch
                v-model="updateJl.enabled"
                activecolor="#13ce66"
                inactive-color="#ff4949"
                active-text="启用"
                inactive-text="禁用">
              </el-switch>
            </td>
          </tr>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="doUpdate">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "JoblevelMana",
    data() {
      return {
        jl: {
          name: '',
          titleLevel: ''
        },
        jls: [],
        updateJl: {
          name: '',
          titleLevel: '',
          enabled: false
        },
        dialogVisible: false,
        titleLevels: [
          '正高级',
          '副高级',
          '中级',
          '初级',
          '员级',
        ],
        multipleSelection: []
      }
    },
    mounted() {
      this.initJls();
    },
    methods: {
      //批量删除
      deleteMany() {
        this.$confirm('此操作将永久删除【' + this.multipleSelection.length + '】条记录，是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids = '?';
          this.multipleSelection.forEach(item => {
            ids += 'ids=' + item.id + '&';
          })
          this.deleteRequest('/system/basic/joblevel/' + ids).then(resp => {
            if (resp) {

              this.initJls();
            }
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      //修改职称
      doUpdate() {
        this.putRequest('/system/basic/joblevel/', this.updateJl).then(resp => {
          if (resp) {
            this.initJls();
            this.updateJl.name = '';
            this.updateJl.titleLevel = '';
            this.dialogVisible = false;
          }
        })
      },
      showEditView(data) {
        Object.assign(this.updateJl, data);
        this.dialogVisible = true;
      },
      deleteHandler(data) {
        //删除
        this.$confirm('此操作将永久删除【' + data.name + '】职称, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteRequest('/system/basic/joblevel/' +
            data.id).then(resp => {
            if (resp) {
              this.initJls();
            }
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      addJl() {
        //添加职称
        if (this.jl.name && this.jl.titleLevel) {
          this.postRequest('/system/basic/joblevel/', this.jl).then(resp => {
            if (resp) {
              this.initJls();
              this.jl.name = '';
              this.jl.titleLevel = '';
            }
          })
        } else {
          this.$message.error("字段不能为空！");
        }

      },
      initJls() {
        this.getRequest('/system/basic/joblevel/').then(resp => {
          if (resp) {
            this.jls = resp;
            /*this.jl = {
              name: '',
              titleLevel: ''
            }*/
          }
        })
      }
    }
  }
</script>

<style>

</style>
