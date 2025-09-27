<template>
  <div class="page">
    <el-row :gutter="16">
      <el-col :span="6">
        <el-card><div class="kpi"><div>血氧</div><div class="val">{{ latest.BLOOD_OXYGEN?.value || '--' }} <small>%</small></div></div></el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="kpi">
            <div>血压</div>
            <div class="val">
              {{ latest.BLOOD_PRESSURE_SYS?.value || '--' }}/{{ latest.BLOOD_PRESSURE_DIA?.value || '--' }}
              <small>mmHg</small>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card><div class="kpi"><div>步数</div><div class="val">{{ latest.STEPS?.value || '--' }}</div></div></el-card>
      </el-col>
      <el-col :span="6">
        <el-card><div class="kpi"><div>心率</div><div class="val">{{ latest.HEART_RATE?.value || '--' }} <small>bpm</small></div></div></el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top:16px">
      <div style="margin-bottom:8px">
        <span>选择设备：</span>
        <el-select v-model="deviceId" placeholder="选择设备" @change="loadAll" style="width:240px">
          <el-option v-for="d in devices" :key="d.id" :label="d.deviceName || d.deviceId" :value="d.id" />
        </el-select>
      </div>
      <div ref="chart" style="height:360px"></div>
    </el-card>

    <el-card style="margin-top:16px">
      <div slot="header">最新预警</div>
      <el-table :data="alerts" size="small">
        <el-table-column prop="alertMessage" label="消息" />
        <el-table-column prop="severity" label="级别" width="100" />
        <el-table-column prop="triggeredAt" label="时间" width="180" />
        <el-table-column label="操作" width="120">
          <template v-slot="scope">
            <el-button type="text" @click="markRead(scope.row.id)" v-if="!scope.row.isRead">标记已读</el-button>
            <span v-else>已读</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import * as api from '@/api'
import * as echarts from 'echarts'
import dayjs from 'dayjs'

export default {
  name:'DashboardIndex',
  data() {
    return {
      devices: [],
      deviceId: null,
      latest: {},
      alerts: [],
      chart: null,
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    async init() {
      const userId = Number(localStorage.getItem('userId'))
      if (!userId) {
        this.$message.info('请先登录')
        this.$router.push('/login')
        return
      }
      const { data: ds } = await api.listDevicesByUser(userId)
      this.devices = ds
      if (ds.length) {
        this.deviceId = ds[0].id
        await this.loadAll()
      }
      this.chart = echarts.init(this.$refs.chart)
    },
    async loadAll() {
      if (!this.deviceId) return
      // 顶部KPI：按类型获取最新值（含血压的收缩/舒张）
      const types = ['HEART_RATE', 'BLOOD_OXYGEN', 'STEPS', 'BLOOD_PRESSURE_SYS', 'BLOOD_PRESSURE_DIA']
      for (const t of types) {
        try {
          const { data } = await api.latestByType(this.deviceId, t)
          this.$set(this.latest, t, data || {})
        } catch (e) {
          this.$set(this.latest, t, {})
        }
      }

      // 折线图：近7天心率
      const end = dayjs().format('YYYY-MM-DD')
      const start = dayjs().subtract(6, 'day').format('YYYY-MM-DD')
      const { data: history } = await api.historyHealth({ deviceId: this.deviceId, start, end })
      const hr = history.filter(i => i.dataType === 'HEART_RATE')
      this.renderLine(hr)
      // 最新预警
      const userId = Number(localStorage.getItem('userId'))
      const { data: alerts } = await api.listAlertsByUser(userId)
      this.alerts = alerts.slice(0, 10)
    },
    renderLine(items) {
      const x = items.map(i => i.timestamp?.replace('T',' ').slice(5,16))
      const y = items.map(i => i.value)
      const opt = {
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: x },
        yAxis: { type: 'value' },
        series: [{ type: 'line', data: y, smooth: true }]
      }
      this.chart && this.chart.setOption(opt)
    },
    async markRead(id) {
      await api.markAlertRead(id)
      const userId = Number(localStorage.getItem('userId'))
      const { data } = await api.listAlertsByUser(userId)
      this.alerts = data.slice(0, 10)
    }
  }
}
</script>

<style scoped>
.page { padding: 12px; }
.kpi { display:flex; flex-direction:column; align-items:center; }
.kpi .val { font-size: 24px; font-weight: 600; margin-top: 8px; }
</style>
