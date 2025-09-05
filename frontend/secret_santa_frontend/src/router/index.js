import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Dashboard from '@/views/Dashboard.vue'
import Profile from '@/views/Profile.vue'
import Wishlist from '@/views/Wishlist.vue'
import Events from '@/views/Events.vue'
import EventDetail from '@/views/EventDetails.vue'
import AdminEvents from '@/views/admin/Events.vue'
import AdminTokens from '@/views/admin/Tokens.vue'
import InviteRegister from '@/views/InviteRegister.vue'
import { useAuthStore } from '@/stores/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/register',
    name: 'InviteRegister',
    component: InviteRegister,
    meta: { requiresAuth: false },
  },
  {
    path: '/',
    component: Dashboard,
    meta: { requiresAuth: true },
    children: [
      {
        path: 'profile',
        name: 'Profile',
        component: Profile,
      },
      {
        path: 'wishlist',
        name: 'Wishlist',
        component: Wishlist,
      },
      {
        path: 'events',
        name: 'Events',
        component: Events,
      },
      {
        path: 'events/:id',
        name: 'EventDetail',
        component: EventDetail,
      },
      {
        path: 'admin/events',
        name: 'AdminEvents',
        component: AdminEvents,
        meta: { requiresAdmin: true },
      },
      {
        path: 'admin/tokens',
        name: 'AdminTokens',
        component: AdminTokens,
        meta: { requiresAdmin: true },
      },
      {
        path: '',
        redirect: '/profile',
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  const isAuthenticated = authStore.token !== null
  const isAdmin = authStore.isAdmin

  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login')
  } else if (to.meta.requiresAdmin && !isAdmin) {
    next('/profile')
  } else if (to.path === '/login' && isAuthenticated) {
    next('/profile')
  } else {
    next()
  }
})

export default router
