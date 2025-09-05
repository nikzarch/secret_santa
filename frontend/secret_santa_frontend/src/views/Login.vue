<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-logo">🎅 Тайный Санта</div>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">Имя пользователя</label>
          <input
            id="username"
            v-model="credentials.name"
            type="text"
            required
            placeholder="Введите ваше имя"
          />
        </div>
        <div class="form-group">
          <label for="password">Пароль</label>
          <input
            id="password"
            v-model="credentials.password"
            type="password"
            required
            placeholder="Введите ваш пароль"
          />
        </div>
        <button type="submit" class="btn btn-primary" style="width: 100%">Войти</button>
        <p v-if="error" style="color: var(--primary-color); margin-top: 1rem">
          {{ error }}
        </p>
      </form>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

export default {
  name: 'LoginView',
  setup() {
    const authStore = useAuthStore()
    const router = useRouter()
    const credentials = ref({
      name: '',
      password: '',
    })
    const error = ref('')

    const handleLogin = async () => {
      try {
        error.value = ''
        await authStore.login(credentials.value)
        router.push('/profile')
      } catch (err) {
        error.value = 'Ошибка входа. Проверьте имя пользователя и пароль.'
        console.error('Login error:', err)
      }
    }

    return {
      credentials,
      error,
      handleLogin,
    }
  },
}
</script>
