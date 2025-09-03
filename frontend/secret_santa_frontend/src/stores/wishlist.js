import { defineStore } from 'pinia'
import WishlistService from '@/services/wishlist.service'

export const useWishlistStore = defineStore('wishlist', {
  state: () => ({
    items: []
  }),
  actions: {
    async fetchWishlist() {
      try {
        this.items = await WishlistService.getWishlist()
      } catch (err) {
        console.error('Ошибка загрузки вишлиста', err)
        this.items = []
      }
    },
    async addItem(item) {
      await WishlistService.addItem(item)
      await this.fetchWishlist()
    },
    async updateItem(itemId, item) {
      await WishlistService.updateItem(itemId, item)
      await this.fetchWishlist()
    },
    async deleteItem(itemId) {
      await WishlistService.deleteItem(itemId)
      await this.fetchWishlist()
    }
  }
})
