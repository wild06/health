<template>
  <div class="page">
    <el-card>
      <div style="margin-bottom:8px">
        <span>设备：</span>
        <el-select v-model="deviceId" placeholder="选择设备" style="width:240px" @change="loadLatest">
          <el-option v-for="d in devices" :key="d.id" :label="d.deviceName || d.deviceId" :value="d.id" />
        </el-select>
        <span style="margin-left:16px">日期：</span>
        <el-date-picker v-model="date" type="date" @change="loadHistory" />
        <el-button type="primary" size="mini" style="margin-left:8px" @click="loadHistory">轨迹回放</el-button>
      </div>
      <div ref="map" class="map"></div>
    </el-card>
  </div>
</template>

<script>
import * as api from '@/api'
import AMapLoader from '@amap/amap-jsapi-loader'
import dayjs from 'dayjs'

export default {
  name:'MapIndex',
  data() {
    return {
      map: null,
      marker: null,
      polyline: null,
      devices: [],
      deviceId: null,
      date: new Date(),
    }
  },
  async mounted() {
    const userId = Number(localStorage.getItem('userId'))
    if (!userId) return this.$router.push('/login')
    const { data: ds } = await api.listDevicesByUser(userId)
    this.devices = ds
    if (ds.length) this.deviceId = ds[0].id
    await this.initMap()
    await this.loadLatest()
  },
  methods: {
    async initMap() {
      const key = window._AMAP_KEY || 'YOUR_AMAP_KEY'
      const AMap = await AMapLoader.load({ key, version: '2.0', plugins: ['AMap.ToolBar'] })
      this._AMap = AMap
      this.map = new AMap.Map(this.$refs.map, { zoom: 13, center: [116.397428, 39.90923] })
      this.map.addControl(new AMap.ToolBar())
    },
    async loadLatest() {
      if (!this.deviceId || !this.map) return
      const { data } = await api.latestLocation(this.deviceId)
      if (!data || !data.longitude) return
      const pos = [data.longitude, data.latitude]
      if (!this.marker) {
        this.marker = new this._AMap.Marker({ position: pos })
        this.map.add(this.marker)
      } else {
        this.marker.setPosition(pos)
      }
      this.map.setCenter(pos)
    },
    async loadHistory() {
      if (!this.deviceId || !this.date) return
      const params = { deviceId: this.deviceId, date: dayjs(this.date).format('YYYY-MM-DD') }
      const { data } = await api.historyLocation(params)
      if (!data || !data.length) return
      const path = data.map(p => [p.longitude, p.latitude])
      if (this.polyline) this.map.remove(this.polyline)
      this.polyline = new this._AMap.Polyline({ path, strokeColor: '#409EFF', strokeWeight: 4 })
      this.map.add(this.polyline)
      this.map.setFitView([this.polyline])
    }
  }
}
</script>

<style scoped>
.page { padding: 12px; }
.map { height: 520px; width: 100%; }
</style>
