<template>
  <nav class="sidebar">
    <ul>
      <li>
        <router-link to="/profile" :class="{ active: $route.path === '/profile' }">
          Профиль
        </router-link>
      </li>
      <li>
        <router-link to="/wishlist" :class="{ active: $route.path === '/wishlist' }">
          Вишлист
        </router-link>
      </li>
      <li>
        <router-link to="/events" :class="{ active: $route.path === '/events' }">
          Мои события
        </router-link>
      </li>
    </ul>
  </nav>
</template>

<script>
export default {
  name: 'AppSidebar'
}
</script>
