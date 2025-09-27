import Vue from 'vue'
import Router from 'vue-router'

import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import Dashboard from '@/views/Dashboard.vue'
import History from '@/views/History.vue'
import MapView from '@/views/Map.vue'
import Devices from '@/views/Devices.vue'
import Alerts from '@/views/Alerts.vue'
import Profile from '@/views/Profile.vue'

Vue.use(Router)

const router = new Router({
  mode: 'hash',
  routes: [
    { path: '/', redirect: '/dashboard' },
    { path: '/login', component: Login },
    { path: '/register', component: Register },
    { path: '/dashboard', component: Dashboard },
    { path: '/history', component: History },
    { path: '/map', component: MapView },
    { path: '/devices', component: Devices },
    { path: '/alerts', component: Alerts },
    { path: '/profile', component: Profile },
  ]
})

export default router
