import api from './fetch-api'

export default {
  getWishlist() {
    return api.get('/wishlist')
  },
  addItem(item) {
    return api.post('/wishlist', item)
  },
  updateItem(itemId, item) {
    return api.put(`/wishlist/${itemId}`, item)
  },
  deleteItem(itemId) {
    return api.delete(`/wishlist/${itemId}`)
  }
}
