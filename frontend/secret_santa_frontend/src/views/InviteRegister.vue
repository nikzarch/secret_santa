<template>
  <div class="card" v-if="token">
    <h2>Регистрация по приглашению</h2>
    <form @submit.prevent="handleRegister" class="form">
      <div class="form-group">
        <label for="password">Пароль</label>
        <input
          id="password"
          type="password"
          v-model="password"
          required
          placeholder="Введите пароль"
        />
      </div>

      <div class="form-group">
        <label for="confirmPassword">Повторите пароль</label>
        <input
          id="confirmPassword"
          type="password"
          v-model="confirmPassword"
          required
          placeholder="Повторите пароль"
        />
      </div>

      <button type="submit" class="btn btn-primary">Зарегистрироваться</button>

      <p v-if="error" class="error">{{ error }}</p>
      <p v-if="success" class="success">успешно, перенаправляю</p>
    </form>
  </div>

  <div v-else class="card">
    <p class="error">❌ Токен не указан в ссылке</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/services/fetch-api'

const route = useRoute()
const router = useRouter()

const password = ref('')
const confirmPassword = ref('')
const error = ref(null)
const success = ref(false)
const token = ref(null)

onMounted(() => {
  token.value = route.query.token
  if (!token.value) {
    error.value = 'Токен не указан'
  }
})

const handleRegister = async () => {
  error.value = null

  if (!token.value) return
  if (password.value !== confirmPassword.value) {
    error.value = 'Пароли не совпадают'
    return
  }

  try {
    const response = await api.post(
      `/auth/register?token=${token.value}&password=${encodeURIComponent(password.value)}`,
    )
    if (response?.message) {
      success.value = true
      setTimeout(() => router.push('/login'), 1500)
    } else {
      error.value = 'Не удалось зарегистрировать пользователя'
    }
  } catch (e) {
    try {
      const data = await e?.response?.json()
      error.value = data?.error || e.message
    } catch {
      error.value = e.message || 'Ошибка при регистрации'
    }
  }
}
</script>

<style scoped>
.form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.form-group {
  display: flex;
  flex-direction: column;
}
.error {
  color: red;
  margin-top: 0.5rem;
}
.success {
  color: green;
  margin-top: 0.5rem;
}
</style>
