import api from './fetch-api'

export default {
  getAllEvents() {
    return api.get('/events')
  },
  getUserEvents(username) {
    return api.get(`/events/user/${username}`)
  },
  addEvent(eventData) {
    return api.post('/events', eventData)
  },
  addParticipant(participantData) {
    return api.post('/events/participants', participantData)
  },
  deactivateEvent(eventId) {
    return api.post('/events/disactive', { eventId })
  },
  generateAssignments(eventId) {
    return api.post(`/events/${eventId}/generate-assignments`)
  },
  getMyReceiver(eventId) {
    return api.get(`/events/${eventId}/my-receiver`)
  }
}
