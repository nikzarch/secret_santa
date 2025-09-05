<template>
  <div class="card">
    <h2>{{ event?.name }}</h2>
    <p>{{ event?.description }}</p>
    <p>Дата события: {{ formatDate(event?.eventDate) }}</p>
    <p>Активно: {{ event?.isActive ? 'Да' : 'Нет' }}</p>
    <p v-if="receiver">
      Вы дарите подарок: <strong>{{ receiver }}</strong>
    </p>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import eventService from '@/services/event.service.js'

export default {
  name: 'EventDetailsView',
  setup() {
    const route = useRoute()
    const event = ref(null)
    const receiver = ref(null)

    const fetchEventDetails = async () => {
      const eventId = route.params.id
      try {
        const response = await eventService.getEventById(eventId)
        event.value = response
        console.log(response)
        console.log(event.value)
      } catch (err) {
        console.error('Error fetching event details', err)
      }
      try {
        const response = await eventService.getMyReceiver(eventId)
        receiver.value = response.receiver
      } catch (e) {
        console.error('Error fetching receiver', e)
      }
    }

    const formatDate = (dateString) =>
      dateString ? new Date(dateString).toLocaleDateString('ru-RU') : ''

    onMounted(fetchEventDetails)

    return { event, receiver, formatDate }
  },
}
</script>
