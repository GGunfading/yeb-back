<template>
  <div>
    <div style="margin-top: 10px;display: flex;justify-content: center">
      <el-input placeholder="通过用户名搜索用户..." prefix-icon="el-icon-search" v-model="keywords"
                style="width: 400px;margin-right: 10px" @keydown.enter.native="doSearch"></el-input>
      <el-button type="primary" icon="wl-icon-search" @click="doSearch">搜索</el-button>
    </div>
    <div class="admin-container">
      <el-card class="admin-card" v-for="(admin,index) in admins" :key="index">
        <div slot="header" class="clearfix">
          <span>{{admin.name}}</span>
          <el-button style="float: right;padding: 3px 0;color: #ff0000" type="text" icon="el-icon-delete"
                     @click="deleteAdmin(admin)"></el-button>
        </div>
        <div>
          <div class="img-container">
            <img :src="admin.userFace" :alt="admin.name" :title="admin.name" class="userface-img">
          </div>
        </div>
        <div class="userinfo-container">
          <div>用户名:{{admin.name}}</div>
          <div>手机号码:{{admin.phone}}</div>
          <div>电话号码:{{admin.telephone}}</div>
          <div>地址:{{admin.address}}</div>
          <div>用户状态:
            <el-switch
              v-model="admin.enabled"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="enabledChange(admin)"
              active-text="启用"
              inactive-text="禁用">
            </el-switch>
          </div>
          <div>用户角色:
            <el-tag type="success" style="margin-right: 4px" v-for="(role,indexj) in admin.roles" :key="indexj">
              {{role.nameZh}}
            </el-tag>
            <el-popover
              placement="right"
              title="角色列表"
              @show="showPop(admin)"
              @hide="hidePop(admin)"
              width="200"
              trigger="click">
              <el-select v-model="selectedRoles" multiple placeholder="请选择...">
                <el-option
                  v-for="(r,indexk) in allRoles"
                  :key="indexk"
                  :label="r.nameZh"
                  :value="r.id"></el-option>
              </el-select>
              <el-button slot="reference" icon="el-icon-more" type="text"></el-button>
            </el-popover>
            <!--<el-button icon="el-icon-more" type="text"></el-button>-->
          </div>
          <div>备注:{{admin.remark}}</div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
  export default {
    name: "SysAdmin",
    data() {
      return {
        keywords: '',
        admins: [],
        allRoles: [],
        selectedRoles: []
      }
    },
    mounted() {
      this.initAdmins();
    },
    methods: {
      deleteAdmin(admin) {
        this.$confirm('此操作将永久删除该【' + admin.name + '】用户, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteRequest('/system/admin/' + admin.id).then(resp => {
            this.initAdmins();
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      doSearch() {
        this.initAdmins();
      },
      hidePop(admin) {
        let roles = [];
        Object.assign(roles, admin.roles);
        let flag = false;
        if (roles.length != this.selectedRoles.length) {
          flag = true;
        } else {
          for (let i = 0; i < roles.length; i++) {
            let role = roles[i];
            for (let j = 0; j < this.selectedRoles.length; j++) {
              let sr = this.selectedRoles[j];
              if (role.id == sr) {
                roles.splice(i, 1);
                i--;
                break;
              }
            }
          }
          if (roles.length != 0) {
            flag = true;
          }
        }
        if (flag) {
          let url = '/system/admin/role?adminId=' + admin.id;
          this.selectedRoles.forEach(sr => {
            url += '&rids=' + sr;
          })
          this.putRequest(url).then(resp => {
            if (resp) {
              this.initAdmins();
            }
          })
        }
      },
      showPop(admin) {
        this.initAllRoles();
        let roles = admin.roles;
        this.selectedRoles = [];
        roles.forEach(r => {
          this.selectedRoles.push(r.id);
        })
      },
      initAllRoles() {
        this.getRequest('/system/admin/roles').then(resp => {
          if (resp) {
            this.allRoles = resp;
          }
        })
      },
      //更新用户状态
      enabledChange(admin) {
        this.putRequest('/system/admin/', admin).then(resp => {
          if (resp) {
            this.initAdmins();
          }
        })
      },
      initAdmins() {
        this.getRequest('/system/admin/?keywords=' + this.keywords).then(resp => {
          if (resp) {
            this.admins = resp;
          }
        })
      }
    }
  }
</script>

<style>
  .userinfo-container div {
    font-size: 12px;
    color: #409eff;
  }

  .userinfo-container {
    margin-top: 20px;
  }

  .img-container {
    width: 100%;
    display: flex;
    justify-content: center;
  }

  .userface-img {
    width: 72px;
    height: 72px;
    border-radius: 72px;
  }

  .admin-container {
    margin-top: 10px;
    display: flex;
    justify-content: space-around;
    flex-wrap: wrap;
  }

  .admin-card {
    width: 300px;
    margin-bottom: 20px;
  }
</style>
