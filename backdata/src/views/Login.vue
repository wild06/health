<template>
  <div class="page">
    <el-card class="card">
      <h2>登录</h2>
      <el-form :model="form" :rules="rules" ref="form" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" autocomplete="off" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="onSubmit">登录</el-button>
          <el-button type="text" @click="$router.push('/register')">去注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { login } from '@/api'

export default {
  name:'LoginIndex',
  data() {
    return {
      loading: false,
      form: { username: '', password: '' },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
      }
    }
  },
  methods: {
    onSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.loading = true
        try {
          const { data } = await login(this.form)
          window.localStorage.setItem('userId', data.userId)
          window.localStorage.setItem('role', data.role)
          window.localStorage.setItem('username', this.form.username)
          this.$message.success('登录成功')
          this.$router.push('/dashboard')
        } catch (e) {
          this.$message.error(e?.response?.data?.message || '登录失败')
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.page { display:flex; justify-content:center; align-items:center; height: 100vh; }
.card { width: 380px; }
</style>

