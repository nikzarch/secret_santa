<template>
  <div class="admin-page">
    <div class="card">
      <h2>🎅 Управление событиями</h2>

      <!-- Создание события -->
      <form @submit.prevent="createEvent" class="form">
        <h3>Создать событие</h3>
        <div class="form-group">
          <label>Название</label>
          <input v-model="newEvent.name" type="text" required />
        </div>
        <div class="form-group">
          <label>Описание</label>
          <input v-model="newEvent.description" type="text" />
        </div>
        <div class="form-group">
          <label>Дата события</label>
          <input v-model="newEvent.date" type="date" required />
        </div>
        <button type="submit" class="btn btn-primary">Создать</button>
      </form>

      <p v-if="error" class="error">{{ error }}</p>
      <p v-if="success" class="success">{{ success }}</p>
    </div>

    <!-- Список событий -->
    <div class="card">
      <h3>Список событий</h3>
      <ul v-if="events.length > 0" class="events-list">
        <li v-for="event in events" :key="event.id" class="event-item">
          <div class="event-info">
            <h4>{{ event.name }} ({{ event.eventDate }})</h4>
            <p>{{ event.description }}</p>
            <p>Статус: {{ event.isActive ? 'Активно' : 'Неактивно' }}</p>
            <p>
              Assignments: {{ event.assignmentsGenerated ? 'Сгенерированы' : 'Не сгенерированы' }}
            </p>
          </div>
          <div class="event-actions">
            <input v-model="participants[event.id]" placeholder="username участника" />
            <button @click="addParticipant(event.id)" class="btn btn-outline">
              Добавить участника
            </button>
            <button @click="generateAssignments(event.id)" class="btn btn-outline">
              Сгенерировать пары
            </button>
            <button @click="disactiveEvent(event.id)" class="btn btn-danger">Деактивировать</button>
          </div>
        </li>
      </ul>
      <p v-else>Событий пока нет.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/services/fetch-api'

const newEvent = ref({ name: '', description: '', date: '' })
const events = ref([])
const participants = ref({})
const error = ref(null)
const success = ref(null)

const fetchEvents = async () => {
  try {
    events.value = await api.get('/events')
  } catch (e) {
    error.value = 'Не удалось загрузить события'
  }
}

const createEvent = async () => {
  try {
    error.value = null
    success.value = null
    await api.post('/events', newEvent.value)
    success.value = 'Событие создано'
    newEvent.value = { name: '', description: '', date: '' }
    await fetchEvents()
  } catch (e) {
    error.value = 'Ошибка при создании события'
  }
}

const addParticipant = async (eventId) => {
  try {
    error.value = null
    success.value = null
    await api.post('/events/participants', {
      event_id: eventId,
      username: participants.value[eventId],
    })
    success.value = 'Участник добавлен'
    participants.value[eventId] = ''
    await fetchEvents()
  } catch (e) {
    error.value = 'Ошибка при добавлении участника'
  }
}

const generateAssignments = async (eventId) => {
  try {
    error.value = null
    success.value = null
    await api.post(`/events/${eventId}/generate-assignments`)
    success.value = 'Пары сгенерированы'
    await fetchEvents()
  } catch (e) {
    error.value = 'Ошибка при генерации пар'
  }
}

const disactiveEvent = async (eventId) => {
  try {
    error.value = null
    success.value = null
    await api.post('/events/disactive', { eventId })
    success.value = 'Событие деактивировано'
    await fetchEvents()
  } catch (e) {
    error.value = 'Ошибка при деактивации события'
  }
}

onMounted(fetchEvents)
</script>

<style scoped>
.form {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 1rem;
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
.events-list {
  list-style: none;
  padding: 0;
}
.event-item {
  border-bottom: 1px solid #ccc;
  padding: 1rem 0;
}
.event-actions {
  display: flex;
  gap: 0.5rem;
  margin-top: 0.5rem;
}
</style>
