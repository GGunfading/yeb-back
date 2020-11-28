<template>
  <div>
    <div>
      <el-input
        size="small"
        class="addPosInput"
        placeholder="添加职位..."
        prefix-icon="el-icon-plus"
        @keydown.enter.native="addPosition"
        v-model="pos.name">
      </el-input>
      <el-button icon="el-icon-plus" size="small" type="primary" @click="addPosition">添加</el-button>
    </div>
    <div class="posManaMain">
      <el-table
        :data="positions"
        border
        stripe
        size="small"
        @selection-change="handleSelectionChange"
        style="width: 70%">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="编号" width="55"></el-table-column>
        <el-table-column prop="name" label="职位名称" width="120"></el-table-column>
        <el-table-column prop="createDate" label="创建时间"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="showEditView(scope.$index,scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.$index,scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button type="danger" size="small" style="margin-top: 8px" :disabled="multipleSelection.length == 0"
                 @click="deleteMany">批量删除
      </el-button>
    </div>
    <el-dialog title="编辑职位" :visible.sync="dialogVisible" width="30%">
      <div>
        <el-tag>职位名称</el-tag>
        <el-input v-model="updatePos.name" size="small" class="updatePosInput"></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">取消</el-button>
        <el-button size="small" type="primary" @click="doUpdate">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "PosMana",
    data() {
      return {
        pos: {
          name: ''
        },
        dialogVisible: false,
        updatePos: {
          name: ''
        },
        multipleSelection: [],
        positions: []
      }
    },
    mounted() {
      this.initPositions();
    },
    methods: {
      deleteMany() {
        this.$confirm('此操作将永久删除【' + this.multipleSelection.length
          + '】条记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids = '?';
          this.multipleSelection.forEach(item => {
            ids += 'ids=' + item.id + '&';
          })
          this.deleteRequest('/system/basic/pos/' + ids).then(resp => {
            if (resp) {
              this.initPositions();
            }
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      handleSelectionChange(val){
        this.multipleSelection = val;
      },
      addPosition() {
        //添加职位
        if (this.pos.name) {
          this.postRequest('/system/basic/pos/', this.pos).then(resp => {
            if (resp) {
              this.initPositions();
              this.pos.name = '';
            }
          })
        } else {
          this.$message.error("职位名称不可以为空！");
        }
      },
      showEditView(index, data) {
        Object.assign(this.updatePos, data);
        this.updatePos.createDate = '';
        this.dialogVisible = true;
      },
      doUpdate() {
        //修改职位
        this.putRequest('/system/basic/pos/', this.updatePos).then(resp => {
          if (resp) {
            this.initPositions();
            this.updatePos.name = '';
            this.dialogVisible = false;
          }
        })
      },
      handleDelete(index, data) {
        //删除职位
        this.$confirm('此操作将永久删除该【' + data.name + '】职位, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteRequest('/system/basic/pos/' + data.id).then(resp => {
            if (resp) {
              this.initPositions();
            }
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      initPositions() {
        this.getRequest('/system/basic/pos/').then(resp => {
          if (resp) {
            this.positions = resp;
          }
        })
      }
    }
  }
</script>

<style scoped>
  .addPosInput {
    width: 300px;
    margin-right: 8px;
  }

  .posManaMain {
    margin-top: 10px;
  }

  .updatePosInput {
    width: 200px;
    margin-left: 8px;
  }
</style>
