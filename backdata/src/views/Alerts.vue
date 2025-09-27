<template>
  <div class="page">
    <el-card>
      <div slot="header">预警中心</div>
      <el-table :data="alerts" size="small">
        <el-table-column prop="dataType" label="类型" width="120" />
        <el-table-column prop="measuredValue" label="数值" width="120" />
        <el-table-column prop="alertMessage" label="消息" />
        <el-table-column prop="severity" label="级别" width="100" />
        <el-table-column prop="triggeredAt" label="时间" width="180" />
        <el-table-column label="状态" width="100">
          <template v-slot="scope">
            <el-tag :type="scope.row.isRead ? 'success' : 'danger'">{{ scope.row.isRead ? '已读' : '未读' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140">
          <template v-slot="scope">
            <el-button type="text" @click="markRead(scope.row.id)" :disabled="scope.row.isRead">标记已读</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import * as api from '@/api'

export default {
  name:'AlertIndex',
  data() {
    return { alerts: [] }
  },
  async mounted() {
    await this.load()
  },
  methods: {
    async load() {
      const userId = Number(localStorage.getItem('userId'))
      if (!userId) return this.$router.push('/login')
      const { data } = await api.listAlertsByUser(userId)
      this.alerts = data
    },
    async markRead(id) {
      await api.markAlertRead(id)
      this.$message.success('已标记为已读')
      this.load()
    }
  }
}
</script>

<style scoped>
.page { padding: 12px; }
</style>
