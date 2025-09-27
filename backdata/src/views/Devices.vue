<template>
  <div class="page">
    <el-card>
      <div slot="header" class="clearfix">
        <span>设备管理</span>
        <el-button style="float: right" size="mini" type="primary" @click="dialogVisible = true">添加设备</el-button>
      </div>
      <el-table :data="devices" size="small">
        <el-table-column prop="deviceName" label="名称" />
        <el-table-column prop="deviceId" label="设备ID" />
        <el-table-column prop="deviceType" label="类型" />
        <el-table-column prop="macAddress" label="MAC" />
        <el-table-column prop="connectionStatus" label="连接状态" width="120" />
        <el-table-column prop="lastConnected" label="最后连接时间" width="180" />
        <el-table-column label="操作" width="220">
          <template v-slot="scope">
            <el-button size="mini" @click="connect(scope.row.id)">连接</el-button>
            <el-button size="mini" @click="disconnect(scope.row.id)">断开</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="添加设备" :visible.sync="dialogVisible" width="480px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="设备ID"><el-input v-model="form.deviceId" /></el-form-item>
        <el-form-item label="设备名称"><el-input v-model="form.deviceName" /></el-form-item>
        <el-form-item label="设备类型"><el-input v-model="form.deviceType" /></el-form-item>
        <el-form-item label="MAC 地址"><el-input v-model="form.macAddress" /></el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/api'

export default {
  name:'DeviceIndex',
  data() {
    return {
      devices: [],
      dialogVisible: false,
      form: { deviceId: '', deviceName: '', deviceType: '', macAddress: '' }
    }
  },
  mounted() { this.load() },
  methods: {
    async load() {
      const userId = Number(localStorage.getItem('userId'))
      if (!userId) return this.$router.push('/login')
      const { data } = await api.listDevicesByUser(userId)
      this.devices = data
    },
    async submit() {
      const userId = Number(localStorage.getItem('userId'))
      const payload = { ...this.form, userId }
      try {
        await api.addDevice(payload)
        this.$message.success('添加成功')
        this.dialogVisible = false
        this.form = { deviceId: '', deviceName: '', deviceType: '', macAddress: '' }
        this.load()
      } catch (e) {
        this.$message.error(e?.response?.data?.message || '添加失败')
      }
    },
    async connect(id) { await api.connectDevice(id); this.$message.success('已连接'); this.load() },
    async disconnect(id) { await api.disconnectDevice(id); this.$message.success('已断开'); this.load() },
  }
}
</script>

<style scoped>
.page { padding: 12px; }
</style>
