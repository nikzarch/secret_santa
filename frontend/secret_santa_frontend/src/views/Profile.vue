<template>
  <div class="card">
    <h2>Профиль пользователя</h2>
    <div v-if="user" class="profile-info">
      <p><strong>Имя:</strong> {{ user.name }}</p>
      <p><strong>Роль:</strong> {{ user.role }}</p>
    </div>
    <button class="btn btn-outline" @click="handleLogout">
      Выйти
    </button>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

export default {
  name: 'ProfileView',
  setup() {
    const authStore = useAuthStore()
    const router = useRouter()
    const user = computed(() => authStore.user)

    const handleLogout = () => {
      authStore.logout()
      router.push('/login')
    }

    return {
      user,
      handleLogout
    }
  }
}
</script>
