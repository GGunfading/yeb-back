<template>
  <div>
    <el-container>
      <el-header class="homeHeader">
        <div class="title">云E办系统</div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu router>
            <el-submenu index="1" v-for="(item,index) in this.$router.options.routes" v-if="!item.hidden"
                        :key="index">
              <template slot="title">
                <i class="el-icon-location"></i>
                <span>{{item.name}}</span>
              </template>
              <el-menu-item :index="children.path" v-for="(children,indexj) in item.children"
                            :key="indexj">{{children.name}}
              </el-menu-item>
            </el-submenu>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
  export default {
    name: "Home"
  }
</script>

<style scoped>

</style>
