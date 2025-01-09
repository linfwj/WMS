import { defineStore } from 'pinia'
import axios from 'axios'

interface AuthState {
  user: any | null
  isAuthenticated: boolean
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    user: null,
    isAuthenticated: false
  }),
  
  actions: {
    async login(username: string, password: string): Promise<{ success: boolean; error?: string }> {
      try {
        const response = await axios.post(`${import.meta.env.VITE_API_GATEWAY_URL}/api/auth/login`, 
          {
            username,
            password
          },
          {
            withCredentials: true,
            headers: {
              'Content-Type': 'application/json',
              'Accept': 'application/json'
            }
          }
        )
        this.user = response.data
        this.isAuthenticated = true
        return { success: true }
      } catch (error: any) {
        console.error('Login failed:', error)
        const errorMessage = error.response?.data || 'Login failed. Please try again.'
        return { success: false, error: errorMessage }
      }
    },
    
    logout() {
      this.user = null
      this.isAuthenticated = false
    }
  }
})
