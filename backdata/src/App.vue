<template>
  <div id="app">
    <!-- 独立认证页面：登录/注册，不显示头部和侧栏 -->
    <div v-if="isAuthRoute" class="auth-wrapper">
      <router-view />
    </div>

    <!-- 正常业务布局：头部 + 侧栏 + 内容区（浅色主题） -->
    <el-container v-else style="height: 100vh">
      <el-header height="60px" class="header light">
        <div class="brand" @click="$router.push('/dashboard')">老年人健康与防丢失系统</div>
        <div class="spacer"></div>
        <template v-if="isLoggedIn">
          <el-dropdown>
            <span class="el-dropdown-link">
              <i class="el-icon-user"></i> {{ username || '用户' }} <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="$router.push('/profile')">个人中心</el-dropdown-item>
              <el-dropdown-item divided @click.native="onLogout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button type="text" @click="$router.push('/login')">登录</el-button>
          <el-button type="primary" @click="$router.push('/register')">注册</el-button>
        </template>
      </el-header>
      <el-container>
        <el-aside width="208px" class="aside light">
          <el-menu :default-active="$route.path" class="el-menu-vertical-demo" @select="onSelect" :router="false">
            <el-menu-item index="/dashboard"><i class="el-icon-data-analysis"></i><span slot="title">仪表盘</span></el-menu-item>
            <el-menu-item index="/history"><i class="el-icon-date"></i><span slot="title">历史数据</span></el-menu-item>
            <el-menu-item index="/map"><i class="el-icon-location"></i><span slot="title">地图</span></el-menu-item>
            <el-menu-item index="/devices"><i class="el-icon-cpu"></i><span slot="title">设备管理</span></el-menu-item>
            <el-menu-item index="/alerts"><i class="el-icon-bell"></i><span slot="title">预警中心</span></el-menu-item>
            <el-menu-item index="/profile"><i class="el-icon-user"></i><span slot="title">个人中心</span></el-menu-item>
          </el-menu>
        </el-aside>
        <el-main class="main light">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App',
  methods: {
    onSelect(path) {
      if (this.$route.path !== path) this.$router.push(path)
    },
    async onLogout() {
      try {
        if (this.$http?.get) {
          await this.$http.get('/api/users/logout')
        }
      } catch (e) { /* 忽略后端异常，确保前端退出 */ }
      window.localStorage.removeItem('userId')
      window.localStorage.removeItem('role')
      window.localStorage.removeItem('username')
      this.$message.success('已退出登录')
      this.$router.push('/login')
    }
  },
  computed: {
    isAuthRoute() {
      return this.$route.path === '/login' || this.$route.path === '/register'
    },
    isLoggedIn() {
      return !!window.localStorage.getItem('userId')
    },
    username() {
      return window.localStorage.getItem('username')
    }
  }
}
</script>

<style>
body, html, #app { height: 100%; margin: 0; }
.auth-wrapper { min-height: 100vh; display:flex; align-items:center; justify-content:center; background:#f5f7fa; padding: 24px; }
.header.light { display:flex; align-items:center; padding:0 16px; background:#ffffff; color:#1f2937; border-bottom:1px solid #e5e7eb; }
.brand { font-weight:600; letter-spacing: 0.5px; cursor:pointer; }
.spacer { flex:1; }
.aside.light { background:#ffffff; border-right:1px solid #e5e7eb; }
.main.light { background:#f5f7fa; color:#111827; }
</style>

