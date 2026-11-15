<template>
  <div
    class="min-h-screen bg-gray-100 dark:bg-gray-950 flex flex-col items-center justify-center p-6 transition-colors duration-300">
    <Transition appear enter-active-class="transition duration-700 ease-out" enter-from-class="translate-y-8 opacity-0"
      enter-to-class="translate-y-0 opacity-100">

      <div
        class="w-full max-w-md bg-white dark:bg-gray-900 rounded-xl shadow-lg p-8 border dark:border-gray-800 transition-colors">
        <h1 class="text-3xl font-bold text-gray-800 dark:text-white mb-8 text-center capitalize">Login</h1>

        <div v-if="globalError"
          class="mb-6 p-3 bg-red-100 dark:bg-red-900/30 border border-red-200 dark:border-red-800 rounded-lg text-red-700 dark:text-red-400 text-sm text-center">
          {{ globalError }}
        </div>

        <div class="space-y-5">
          <div>
            <label class="block text-sm font-semibold text-gray-600 dark:text-gray-400 mb-1.5 ml-1">Email</label>
            <input v-model="loginFormData.email" type="email" :class="[
              'w-full px-4 py-2.5 bg-gray-50 dark:bg-gray-800 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-all text-gray-900 dark:text-white',
              errors.email ? 'border-red-500 ring-1 ring-red-500' : 'border-gray-200 dark:border-gray-700'
            ]" placeholder="admin@mail.pt">
            <p v-if="errors.email" class="mt-1 text-xs text-red-500 ml-1">{{ errors.email }}</p>
          </div>

          <div>
            <label class="block text-sm font-semibold text-gray-600 dark:text-gray-400 mb-1.5 ml-1">Password</label>
            <input v-model="loginFormData.password" type="password" :class="[
              'w-full px-4 py-2.5 bg-gray-50 dark:bg-gray-800 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-all text-gray-900 dark:text-white',
              errors.password ? 'border-red-500 ring-1 ring-red-500' : 'border-gray-200 dark:border-gray-700'
            ]" placeholder="••••••••" @keyup.enter="handleLogin">
            <p v-if="errors.password" class="mt-1 text-xs text-red-500 ml-1">{{ errors.password }}</p>
          </div>

          <div class="flex flex-col items-center gap-4 pt-2">
            <button :disabled="loading"
              class="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-blue-400 text-white font-bold py-3 rounded-lg shadow-md hover:shadow-lg transform active:scale-95 transition-all duration-200"
              @click="handleLogin">
              {{ loading ? 'AUTHENTICATING...' : 'LOGIN' }}
            </button>

            <nuxt-link to="/auth/forgot-password" class="text-xs text-blue-600 hover:underline dark:text-blue-400">
              Forgot Password?
            </nuxt-link>
          </div>
        </div>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { useAuthStore } from "~/stores/auth-store.js";
import { storeToRefs } from "pinia";

const notifications = useNotificationStore();
const config = useRuntimeConfig();
const api = config.public.apiBase;
const authStore = useAuthStore();
const router = useRouter();
const { token, user } = storeToRefs(authStore);

const loginFormData = reactive({
  email: "",
  password: "",
});

const errors = reactive({ email: null, password: null });
const globalError = ref(null);
const loading = ref(false);

function validateForm() {
  errors.email = !loginFormData.email ? "Email is required" : null;
  errors.password = !loginFormData.password ? "Password is required" : null;

  if (loginFormData.email && !/^\S+@\S+\.\S+$/.test(loginFormData.email)) {
    errors.email = "Please enter a valid email address";
  }

  return !errors.email && !errors.password;
}

async function handleLogin() {
  globalError.value = null;
  if (!validateForm()) return;

  loading.value = true;

  try {
    await $fetch(`${api}/auth/login`, {
      method: "POST",
      body: loginFormData,
      onResponse({ response }) {
        if (response.status === 200) {
          token.value = response._data.token;
          user.value = response._data.user;
          if (response._data.user.mustChangePassword === true) {
            notifications.error("Please update your password.");
            router.push("/profile/update-password");
          } else {
            notifications.success("Logged in successfully!");
            router.push("/");
          }
        }
      },
      onResponseError({ response }) {
        if (response.status === 401) {
          globalError.value = "Invalid email or password.";
        } else {
          globalError.value = "An unexpected error occurred. Please try again.";
        }
      }
    });
  } catch (e) {
    if (!globalError.value) globalError.value = "Network error: Connection to server failed.";
  } finally {
    loading.value = false;
  }
}
</script>
<style scoped>
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-in-up {
  animation: fadeInUp 0.6s ease-out forwards;
}

.delay-200 {
  animation-delay: 0.2s;
  opacity: 0;
}
</style>