import { defineStore } from 'pinia'
import AuthService from '@/services/auth.service'
import api from '@/services/fetch-api'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: localStorage.getItem('token') || null,
    loadingUser: false,
    isAdmin: false,
  }),
  actions: {
    async login(credentials) {
      const response = await AuthService.login(credentials)
      this.token = response.token
      localStorage.setItem('token', this.token)
      await this.fetchUser()
      return response
    },
    async register(userData) {
      const response = await AuthService.register(userData)
      this.token = response.token
      localStorage.setItem('token', this.token)
      await this.fetchUser()
      return response
    },
    logout() {
      this.token = null
      this.user = null
      localStorage.removeItem('token')
    },
    async fetchUser() {
      if (!this.token) return
      try {
        this.loadingUser = true
        const response = await api.get('/me')
        this.user = response // {id, name, role}
        this.isAdmin = response.role == 'ADMIN'
      } catch (err) {
        console.error('Error fetching user data', err)
        this.logout()
      } finally {
        this.loadingUser = false
      }
    },
  },
  persist: true,
})
