import { defineStore } from 'pinia'
import EventService from '@/services/event.service'

export const useEventsStore = defineStore('events', {
  state: () => ({
    events: [],
    userEvents: [],
  }),
  actions: {
    async fetchAllEvents() {
      const response = await EventService.getAllEvents()
      this.events = response
    },
    async fetchUserEvents(username) {
      const response = await EventService.getUserEvents(username)
      this.userEvents = response
    },
    async addEvent(eventData) {
      await EventService.addEvent(eventData)
    },
    async addParticipant(participantData) {
      await EventService.addParticipant(participantData)
    },
    async deactivateEvent(eventId) {
      await EventService.deactivateEvent(eventId)
    },
    async generateAssignments(eventId) {
      await EventService.generateAssignments(eventId)
    },
    async getMyReceiver(eventId) {
      const response = await EventService.getMyReceiver(eventId)
      return response
    },
  },
  persist: true,
})
