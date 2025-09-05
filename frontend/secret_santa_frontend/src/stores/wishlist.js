// stores/wishlist.js
import { defineStore } from 'pinia'
import WishlistService from '@/services/wishlist.service'

export const useWishlistStore = defineStore('wishlist', {
  state: () => ({
    items: [],
    error: null,
  }),

  actions: {
    async fetchWishlist() {
      try {
        const data = await WishlistService.getWishlist()
        this.items = data
      } catch (err) {
        console.error('Error fetching wishlist', err)
        this.error = 'Не удалось загрузить список'
      }
    },

    async addItem(item) {
      try {
        const savedItem = await WishlistService.addItem(item)
        this.items.push(savedItem)
        return savedItem
      } catch (err) {
        console.error('Error adding item', err)
        throw err
      }
    },

    async updateItem(itemId, item) {
      try {
        const updatedItem = await WishlistService.updateItem(itemId, item)
        const index = this.items.findIndex((i) => i.id === itemId)
        if (index !== -1) this.items[index] = updatedItem
      } catch (err) {
        console.error('Error updating item', err)
        throw err
      }
    },

    async deleteItem(itemId) {
      try {
        await WishlistService.deleteItem(itemId)
        this.items = this.items.filter((i) => i.id !== itemId)
      } catch (err) {
        console.error('Error deleting item', err)
        throw err
      }
    },
  },

  persist: true,
})
