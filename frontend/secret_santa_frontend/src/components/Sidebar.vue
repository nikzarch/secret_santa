<template>
  <nav class="sidebar">
    <div class="user-info" v-if="authStore.user">
      <h3>{{ authStore.user.name }}</h3>
      <span class="user-role">{{ authStore.user.role }}</span>
    </div>

    <ul>
      <li>
        <router-link to="/profile" :class="{ active: $route.path === '/profile' }">
          Профиль
        </router-link>
      </li>
      <li>
        <router-link to="/wishlist" :class="{ active: $route.path === '/wishlist' }">
          Вишлист
        </router-link>
      </li>
      <li>
        <router-link to="/events" :class="{ active: $route.path === '/events' }">
          Мои события
        </router-link>
      </li>

      <li v-if="authStore.isAdmin">
        <div class="admin-section">
          <h4>Админ-панель</h4>
          <ul class="admin-menu">
            <li>
              <router-link
                to="/admin/events"
                :class="{ active: $route.path.startsWith('/admin/events') }"
              >
                События
              </router-link>
            </li>
            <li>
              <router-link
                to="/admin/tokens"
                :class="{ active: $route.path.startsWith('/admin/tokens') }"
              >
                Токены
              </router-link>
            </li>
          </ul>
        </div>
      </li>
    </ul>
  </nav>
</template>

<script>
import { useAuthStore } from '@/stores/auth'

export default {
  name: 'AppSidebar',
  setup() {
    const authStore = useAuthStore()

    return {
      authStore,
    }
  },
}
</script>

<style scoped>
.user-info {
  padding: 1rem 0;
  margin-bottom: 1rem;
  border-bottom: 1px solid #eee;
}

.user-info h3 {
  margin: 0 0 0.5rem 0;
  color: var(--text-color);
}

.user-role {
  font-size: 0.8rem;
  color: var(--text-light);
  background-color: var(--primary-light);
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
}

.admin-section {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #eee;
}

.admin-section h4 {
  color: var(--primary-color);
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
  font-weight: 600;
}

.admin-menu {
  list-style: none;
  padding-left: 1rem;
}

.admin-menu li {
  margin-bottom: 0.5rem;
}

.admin-menu a {
  font-size: 0.9rem;
  padding: 0.25rem 0.5rem;
}
</style>
