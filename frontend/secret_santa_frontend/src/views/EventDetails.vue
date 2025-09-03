<template>
  <div>
    <div class="card">
      <h2>Мои события</h2>
      <div v-if="loading" class="loading">
        Загрузка событий...
      </div>
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
            <router-link
              :to="`/events/${event.id}`"
              class="btn btn-outline"
            >
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
import { ref, onMounted } from 'vue'
import { useEventsStore } from '@/stores/events'
import { useAuthStore } from '@/stores/auth'

export default {
  name: 'EventsView',
  setup() {
    const eventsStore = useEventsStore()
    const authStore = useAuthStore()
    const loading = ref(true)

    onMounted(async () => {
      try {
        await eventsStore.fetchUserEvents(authStore.user.name)
      } catch (error) {
        console.error('Error loading events:', error)
      } finally {
        loading.value = false
      }
    })

    const formatDate = (dateString) => {
      return new Date(dateString).toLocaleDateString('ru-RU')
    }

    return {
      events: eventsStore.userEvents,
      loading,
      formatDate
    }
  }
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

.loading, .empty-state {
  text-align: center;
  padding: 2rem;
  color: var(--text-light);
}
</style>
