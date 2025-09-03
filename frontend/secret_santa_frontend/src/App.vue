<template>
  <router-view />
</template>

<script>
import { onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useWishlistStore } from '@/stores/wishlist'

export default {
  name: 'App',
  setup() {
    const authStore = useAuthStore()
    const wishlistStore = useWishlistStore()

    onMounted(async () => {
      const token = localStorage.getItem('token')
      if (token) {
        authStore.token = token
        await authStore.fetchUser()
        if (authStore.user) {
          await wishlistStore.fetchWishlist()
        }
      }
    })
  }
}
</script>

<style>
@import '@/assets/styles/main.css';
</style>
