<template>
  <div class="app-container">
    <AppHeader />
    <div class="main-content">
      <AppSidebar />
      <main class="content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script>
import AppHeader from '@/components/Header.vue'
import AppSidebar from '@/components/Sidebar.vue'

export default {
  name: 'DashboardView',
  components: {
    AppHeader,
    AppSidebar,
  },
}
</script>
