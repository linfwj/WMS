<template>
  <div class="min-h-screen bg-gray-100 flex items-center justify-center">
    <div class="max-w-md w-full space-y-8 p-8 bg-white rounded-lg shadow">
      <div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">重置密码</h2>
        <p class="mt-2 text-center text-sm text-gray-600">
          请输入您的新密码
        </p>
      </div>
      <form v-if="token" class="mt-8 space-y-6" @submit.prevent="handleSubmit">
        <div>
          <label for="password" class="sr-only">新密码</label>
          <input
            id="password"
            v-model="newPassword"
            name="password"
            type="password"
            required
            class="appearance-none rounded relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
            placeholder="新密码"
          />
        </div>

        <div>
          <button
            type="submit"
            :disabled="loading"
            class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50"
          >
            {{ loading ? '处理中...' : '重置密码' }}
          </button>
        </div>

        <div v-if="message" class="text-center text-sm text-green-600">
          {{ message }}
          <div class="mt-2">
            <router-link
              to="/login"
              class="font-medium text-indigo-600 hover:text-indigo-500"
            >
              返回登录
            </router-link>
          </div>
        </div>
        <div v-if="error" class="text-center text-sm text-red-600">
          {{ error }}
        </div>
      </form>

      <div v-else class="text-center text-red-600">
        无效的重置链接。请重新申请密码重置。
        <div class="mt-4">
          <router-link
            to="/forgot-password"
            class="font-medium text-indigo-600 hover:text-indigo-500"
          >
            忘记密码
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const token = ref('')
const newPassword = ref('')
const message = ref('')
const error = ref('')
const loading = ref(false)

onMounted(() => {
  token.value = route.query.token as string || ''
})

const handleSubmit = async () => {
  loading.value = true
  message.value = ''
  error.value = ''

  try {
    const response = await axios.post(
      `${import.meta.env.VITE_API_GATEWAY_URL}/api/auth/reset-password`,
      {
        token: token.value,
        newPassword: newPassword.value
      },
      {
        headers: {
          'Content-Type': 'application/json',
        },
      }
    )
    message.value = response.data
    // Clear password field after successful reset
    newPassword.value = ''
  } catch (err: any) {
    error.value = err.response?.data || '密码重置失败，请重试'
  } finally {
    loading.value = false
  }
}
</script>
