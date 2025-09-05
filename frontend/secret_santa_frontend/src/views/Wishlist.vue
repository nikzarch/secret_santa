<template>
  <div>
    <div class="card">
      <h2>Мой вишлист</h2>
      <form @submit.prevent="addItem" class="wishlist-form">
        <div class="form-group">
          <label for="itemName">Название предмета</label>
          <input id="itemName" v-model="newItem.name" type="text" required />
        </div>

        <div class="form-group">
          <label for="itemDescription">Описание (необязательно)</label>
          <input id="itemDescription" v-model="newItem.description" type="text" />
        </div>

        <div class="form-group">
          <label for="itemLink">Ссылка (необязательно)</label>
          <input id="itemLink" v-model="newItem.link" type="url" />
        </div>

        <div class="form-group">
          <label for="itemPriority">Приоритет (1–10)</label>
          <input
            id="itemPriority"
            v-model.number="newItem.priority"
            type="number"
            required
            min="1"
            max="10"
          />
        </div>

        <button type="submit" class="btn btn-primary">Добавить предмет</button>
      </form>

      <p v-if="error" class="error">{{ error }}</p>
    </div>

    <div class="card">
      <h3>Список желаний</h3>
      <div v-if="wishlistItems.length === 0" class="empty-state">Ваш список желаний пуст</div>
      <ul v-else class="wishlist-items">
        <li v-for="item in wishlistItems" :key="item.id" class="wishlist-item">
          <div class="item-info">
            <h4>{{ item.name }}</h4>
            <p v-if="item.description">{{ item.description }}</p>
            <a v-if="item.link" :href="item.link" target="_blank">Ссылка</a>
            <p>Приоритет: {{ item.priority }}</p>
          </div>
          <button class="btn btn-outline" @click="deleteItem(item.id)">Удалить</button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { useWishlistStore } from '@/stores/wishlist'

export default {
  name: 'WishlistView',
  setup() {
    const wishlistStore = useWishlistStore()
    const { items: wishlistItems } = storeToRefs(wishlistStore)
    const newItem = ref({ name: '', description: '', link: '', priority: 1 })
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

        if (!newItem.value.name.trim()) {
          error.value = 'Название предмета обязательно'
          return
        }
        if (newItem.value.priority < 1 || newItem.value.priority > 10) {
          error.value = 'Приоритет должен быть от 1 до 10'
          return
        }

        await wishlistStore.addItem({
          name: newItem.value.name,
          description: newItem.value.description || null,
          link: newItem.value.link || null,
          priority: newItem.value.priority,
        })

        newItem.value = { name: '', description: '', link: '', priority: 1 }
      } catch (e) {
        try {
          const errData = JSON.parse(e.message.split('body: ')[1])
          error.value = errData.message || 'Ошибка при добавлении предмета'
        } catch {
          error.value = e.message || 'Ошибка при добавлении предмета'
        }
      }
    }

    const deleteItem = async (itemId) => {
      try {
        error.value = null
        await wishlistStore.deleteItem(itemId)
      } catch (e) {
        console.error(e.message)
        error.value = 'Ошибка при удалении предмета'
      }
    }

    onMounted(fetchWishlist)

    return {
      newItem,
      wishlistItems,
      addItem,
      deleteItem,
      error,
    }
  },
}
</script>

<style scoped>
.error {
  color: red;
  margin-top: 0.5rem;
}
</style>
