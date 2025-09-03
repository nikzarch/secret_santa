<template>
  <div>
    <div class="card">
      <h2>Мой вишлист</h2>
      <form @submit.prevent="addItem" class="wishlist-form">
        <div class="form-group">
          <label for="itemName">Название предмета</label>
          <input id="itemName" v-model="newItem.name" type="text" required>
        </div>
        <div class="form-group">
          <label for="itemDescription">Описание (необязательно)</label>
          <input id="itemDescription" v-model="newItem.description" type="text">
        </div>
        <button type="submit" class="btn btn-primary">Добавить предмет</button>
      </form>

      <p v-if="error" class="error">{{ error }}</p>
    </div>

    <div class="card">
      <h3>Список желаний</h3>
      <div v-if="wishlistItems.length === 0" class="empty-state">
        Ваш список желаний пуст
      </div>
      <ul v-else class="wishlist-items">
        <li v-for="item in wishlistItems" :key="item.id" class="wishlist-item">
          <div class="item-info">
            <h4>{{ item.name }}</h4>
            <p v-if="item.description">{{ item.description }}</p>
          </div>
          <button class="btn btn-outline" @click="deleteItem(item.id)">Удалить</button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useWishlistStore } from '@/stores/wishlist'

export default {
  name: 'WishlistView',
  setup() {
    const wishlistStore = useWishlistStore()
    const newItem = ref({ name: '', description: '' })
    const error = ref(null)

    const fetchWishlist = async () => {
      try {
        error.value = null
        await wishlistStore.fetchWishlist()
      } catch (e) {
        error.value = e.message || 'Ошибка при загрузке списка желаний'
      }
    }

    const addItem = async () => {
      try {
        error.value = null
        await wishlistStore.addItem(newItem.value)
        newItem.value = { name: '', description: '' }
      } catch (e) {
        if (e.message) {
          try {
            const data = JSON.parse(e.message.split('body: ')[1])
            error.value = data.message || 'Ошибка при добавлении предмета'
          } catch {
            error.value = e.message
          }
        }
      }
    }

    const deleteItem = async (itemId) => {
      try {
        error.value = null
        await wishlistStore.deleteItem(itemId)
      } catch (e) {
        console.log(e.message)
        error.value = 'Ошибка при удалении предмета'
      }
    }

    onMounted(fetchWishlist)

    return {
      newItem,
      wishlistItems: wishlistStore.items,
      addItem,
      deleteItem,
      error
    }
  }
}
</script>

<style scoped>
.error {
  color: red;
  margin-top: 0.5rem;
}
</style>
