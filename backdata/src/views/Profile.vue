<template>
  <div class="page">
    <el-card>
      <div slot="header">个人中心</div>
      <el-form :model="form" label-width="100px" style="max-width:520px">
        <el-form-item label="姓名"><el-input v-model="form.realName" /></el-form-item>
        <el-form-item label="年龄"><el-input-number v-model="form.age" :min="0" :max="120" /></el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.gender" placeholder="请选择">
            <el-option label="男" value="MALE" />
            <el-option label="女" value="FEMALE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="身高(cm)"><el-input-number v-model="form.height" :min="0" :max="250" :step="0.1" /></el-form-item>
        <el-form-item label="体重(kg)"><el-input-number v-model="form.weight" :min="0" :max="300" :step="0.1" /></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="save">保存</el-button>
        </el-form-item>
      </el-form>
      <el-alert title="演示：当前后端未提供用户信息更新接口，仅做前端表单示例。" type="info" show-icon style="max-width:520px" />
    </el-card>
  </div>
</template>

<script>
export default {
  name:'ProfileIndex',
  data() {
    return {
      form: { realName: '', age: 60, gender: 'MALE', height: 170, weight: 65 }
    }
  },
  methods: {
    save() {
      this.$message.success('示例：已保存（本地）')
    }
  }
}
</script>

<style scoped>
.page { padding: 12px; }
</style>
