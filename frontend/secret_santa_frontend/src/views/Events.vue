<template>
  <div>
    <div class="card">
      <h2>Мои события</h2>

      <div v-if="loading" class="loading">Загрузка событий...</div>
      <p v-else-if="error" class="error">{{ error }}</p>

      <div v-else-if="events.length === 0" class="empty-state">
        <p>У вас нет активных событий</p>
      </div>

      <table v-else class="table">
        <thead>
          <tr>
            <th>Название</th>
            <th>Дата</th>
            <th>Активно</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="event in events" :key="event.id">
            <td>{{ event.name }}</td>
            <td>{{ formatDate(event.eventDate) }}</td>
            <td>
              <span :class="['status', event.isActive ? 'active' : 'inactive']">
                {{ event.isActive ? 'Активно' : 'Неактивно' }}
              </span>
            </td>
            <td>
              <router-link :to="`/events/${event.id}`" class="btn btn-outline">
                Подробнее
              </router-link>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, watch } from 'vue'
import { storeToRefs } from 'pinia'
import { useEventsStore } from '@/stores/events'
import { useAuthStore } from '@/stores/auth'

export default {
  name: 'EventsView',
  setup() {
    const eventsStore = useEventsStore()
    const authStore = useAuthStore()

    const { user } = storeToRefs(authStore)
    const { userEvents: events } = storeToRefs(eventsStore)

    const loading = ref(true)
    const error = ref(null)

    const loadEvents = async () => {
      if (!user.value?.name) return
      try {
        error.value = null
        await eventsStore.fetchUserEvents(user.value.name)
      } catch (e) {
        error.value = 'Не удалось загрузить события'
        console.error(e)
      } finally {
        loading.value = false
      }
    }

    onMounted(async () => {
      if (!user.value && authStore.token) {
        try {
          await authStore.fetchUser()
        } catch (e) {
          console.error('fetchUser failed', e)
        }
      }
      await loadEvents()
    })

    watch(user, (val, oldVal) => {
      if (!oldVal && val?.name) {
        loadEvents()
      }
    })

    const formatDate = (dateString) => new Date(dateString).toLocaleDateString('ru-RU')

    return { events, loading, error, formatDate }
  },
}
</script>

<style scoped>
.status {
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 500;
}
.status.active {
  background-color: #e8f5e9;
  color: #2e7d32;
}
.status.inactive {
  background-color: #ffebee;
  color: #c62828;
}
.loading,
.empty-state,
.error {
  text-align: center;
  padding: 2rem;
  color: var(--text-light);
}
.error {
  color: #c62828;
}
</style>
