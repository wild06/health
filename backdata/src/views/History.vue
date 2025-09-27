<template>
  <div class="page">
    <el-card>
      <div style="margin-bottom:8px">
        <span>设备：</span>
        <el-select v-model="deviceId" placeholder="选择设备" style="width:220px" @change="load">
          <el-option v-for="d in devices" :key="d.id" :label="d.deviceName || d.deviceId" :value="d.id" />
        </el-select>
        <span style="margin-left:16px">时间段：</span>
        <el-date-picker v-model="range" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" @change="load" />
      </div>
      <div class="charts">
        <div class="chart" ref="hr"></div>
        <div class="chart" ref="spo2"></div>
        <div class="chart" ref="steps"></div>
        <div class="chart" ref="temp"></div>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as api from '@/api'
import * as echarts from 'echarts'
import dayjs from 'dayjs'

export default {
  name:'HistoryIndex',
  data() {
    return {
      devices: [],
      deviceId: null,
      range: [dayjs().subtract(6,'day').toDate(), new Date()],
      charts: {},
    }
  },
  async mounted() {
    const userId = Number(localStorage.getItem('userId'))
    if (!userId) return this.$router.push('/login')
    const { data: ds } = await api.listDevicesByUser(userId)
    this.devices = ds
    if (ds.length) this.deviceId = ds[0].id
    this.charts.hr = echarts.init(this.$refs.hr)
    this.charts.spo2 = echarts.init(this.$refs.spo2)
    this.charts.steps = echarts.init(this.$refs.steps)
    this.charts.temp = echarts.init(this.$refs.temp)
    this.load()
  },
  methods: {
    async load() {
      if (!this.deviceId || !this.range || this.range.length !== 2) return
      const start = dayjs(this.range[0]).format('YYYY-MM-DD')
      const end = dayjs(this.range[1]).format('YYYY-MM-DD')
      const { data } = await api.historyHealth({ deviceId: this.deviceId, start, end })
      const group = type => data.filter(i => i.dataType === type)
      this.render(this.charts.hr, '心率(bpm)', group('HEART_RATE'))
      this.render(this.charts.spo2, '血氧(%)', group('BLOOD_OXYGEN'))
      this.render(this.charts.steps, '步数', group('STEPS'))
      this.render(this.charts.temp, '体温(℃)', group('TEMPERATURE'))
    },
    render(chart, name, items) {
      const x = items.map(i => i.timestamp?.replace('T',' ').slice(5,16))
      const y = items.map(i => i.value)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        title: { text: name, textStyle: { color: '#e2e8f0' } },
        xAxis: { type: 'category', data: x, axisLabel: { color: '#94a3b8' } },
        yAxis: { type: 'value', axisLabel: { color: '#94a3b8' } },
        series: [{ type: 'line', data: y, smooth: true }]
      })
    }
  }
}
</script>

<style scoped>
.page { padding: 12px; }
.charts { display:grid; grid-template-columns: 1fr 1fr; gap: 12px; }
.chart { height: 320px; background: #0f172a; }
</style>
