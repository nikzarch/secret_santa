<template>
  <div class="admin-page">
    <div class="card">
      <h2>🔑 Создать приглашение</h2>
      <form @submit.prevent="createInvite" class="invite-form">
        <div class="form-group">
          <label for="username">Имя пользователя</label>
          <input id="username" v-model="username" type="text" placeholder="Введите имя" required />
        </div>
        <button type="submit" class="btn btn-primary">Создать токен</button>
      </form>

      <div v-if="inviteToken" class="invite-result">
        <h3>✅ Ссылка для приглашения:</h3>
        <input
          type="text"
          :value="inviteLink"
          readonly
          class="invite-link"
          @click="$event.target.select()"
        />
        <button class="btn btn-outline" @click="copyLink">Скопировать</button>
      </div>

      <p v-if="error" class="error">{{ error }}</p>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import api from '@/services/fetch-api'

const username = ref('')
const inviteToken = ref(null)
const error = ref(null)

const inviteLink = computed(() =>
  inviteToken.value ? `${window.location.origin}/register?token=${inviteToken.value}` : '',
)

const createInvite = async () => {
  try {
    error.value = null
    inviteToken.value = null
    const res = await api.post('/invites?username=' + encodeURIComponent(username.value))
    inviteToken.value = res.token
  } catch (e) {
    error.value = e.message || 'Ошибка при создании приглашения'
    console.error(e)
  }
}

const copyLink = () => {
  navigator.clipboard.writeText(inviteLink.value)
}
</script>

<style scoped>
.invite-form {
  margin-bottom: 1rem;
}
.invite-result {
  margin-top: 1rem;
}
.invite-link {
  width: 100%;
  margin: 0.5rem 0;
}
.error {
  color: red;
}
</style>
