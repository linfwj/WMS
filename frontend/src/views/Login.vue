<template>
  <div class="min-h-screen bg-white flex items-center justify-center">
    <div class="max-w-md w-full space-y-8 p-8">
      <div class="flex justify-center">
        <IAMLogo />
      </div>
      
      <!-- Login Tabs -->
      <div class="flex justify-center space-x-8 border-b border-gray-200">
        <button 
          v-for="tab in loginTabs" 
          :key="tab.id"
          @click="currentTab = tab.id"
          :class="[
            'pb-2 px-1 text-base font-medium border-b-2 -mb-px',
            currentTab === tab.id
              ? 'text-gray-900 border-blue-500'
              : 'text-gray-500 border-transparent hover:text-gray-700 hover:border-gray-300'
          ]"
        >
          {{ tab.name }}
        </button>
      </div>

      <!-- Login Form -->
      <form class="mt-8 space-y-6" @submit.prevent="handleLogin">
        <div class="space-y-4">
          <div>
            <input
              id="username"
              v-model="username"
              name="username"
              type="text"
              required
              class="appearance-none relative block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
              :placeholder="currentTab === 'password' ? '请输入用户名' : currentTab === 'email' ? '请输入邮箱' : '请输入手机号'"
            />
          </div>
          <div v-if="currentTab === 'password'">
            <input
              id="password"
              v-model="password"
              name="password"
              type="password"
              required
              class="appearance-none relative block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
              placeholder="请输入密码"
            />
          </div>
        </div>

        <div class="flex items-center justify-between">
          <div class="flex items-center">
            <input
              id="remember-me"
              name="remember-me"
              type="checkbox"
              class="h-4 w-4 text-blue-500 focus:ring-blue-500 border-gray-300 rounded"
            />
            <label for="remember-me" class="ml-2 block text-sm text-gray-900">
              下次自动登录
            </label>
          </div>
          <div class="text-sm">
            <router-link
              to="/forgot-password"
              class="font-medium text-blue-500 hover:text-blue-400"
            >
              忘记密码
            </router-link>
          </div>
        </div>

        <div>
          <button
            type="submit"
            class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-500 hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
          >
            登录
          </button>
        </div>

        <div v-if="error" class="text-red-500 text-center text-sm">
          {{ error }}
        </div>

        <!-- Register Link -->
        <div class="text-center text-sm">
          <span class="text-gray-500">没有账号?</span>
          <a href="#" class="font-medium text-blue-500 hover:text-blue-400 ml-1">立即注册</a>
        </div>

        <!-- Social Login -->
        <div class="mt-6">
          <div class="relative">
            <div class="absolute inset-0 flex items-center">
              <div class="w-full border-t border-gray-300"></div>
            </div>
            <div class="relative flex justify-center text-sm">
              <span class="px-2 bg-white text-gray-500">或</span>
            </div>
          </div>

          <div class="mt-6 grid grid-cols-2 gap-3">
            <button
              type="button"
              class="w-full inline-flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
            >
              <span class="sr-only">Sign in with QQ</span>
              <svg class="w-5 h-5 text-[#3B9CFF]" fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z"/>
              </svg>
            </button>
            <button
              type="button"
              class="w-full inline-flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
            >
              <span class="sr-only">Sign in with WeChat</span>
              <svg class="w-5 h-5 text-[#07C160]" fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z"/>
              </svg>
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import IAMLogo from '../assets/iam-logo.vue'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')
const error = ref('')
const currentTab = ref('password')

const loginTabs = [
  { id: 'password', name: '密码登录' },
  { id: 'email', name: '邮箱登录' },
  { id: 'mobile', name: '手机登录' }
]

const handleLogin = async () => {
  error.value = ''
  if (currentTab.value !== 'password') {
    error.value = '该登录方式暂未开放'
    return
  }
  
  const result = await authStore.login(username.value, password.value)
  if (result.success) {
    router.push('/')
  } else {
    error.value = result.error || '用户名或密码错误'
  }
}
</script>
