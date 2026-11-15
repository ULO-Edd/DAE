<script setup>
const email = ref("");
const error = ref(null);
const message = ref("");
const loading = ref(false);

const config = useRuntimeConfig();
const api = config.public.apiBase;
const router = useRouter();
const notifications = useNotificationStore();

async function handleResetRequest() {
    error.value = null;
    message.value = "";

    if (!email.value) {
        error.value = "Email is required.";
        return;
    }

    if (!/^\S+@\S+\.\S+$/.test(email.value)) {
        error.value = "Please enter a valid email address.";
        return;
    }

    loading.value = true;

    try {
        await $fetch(`${api}/auth/recover-password`, {
            method: "POST",
            body: { email: email.value },
            onResponse({ response }) {
                if (response.status === 200) {
                    notifications.success("Password reset email sent.");
                    router.push("/auth/login")
                }
            },
            onResponseError() {
                error.value = "Email not found in our records.";
            }
        });
    } catch (e) {
        console.error(e);
        if (!error.value) error.value = "Network error: Connection to server failed.";
    } finally {
        loading.value = false;
    }
}
</script>

<template>
    <title>Reset Password - Reddit for Academic Papers</title>
    <div
        class="min-h-screen bg-gray-100 dark:bg-gray-950 flex flex-col items-center justify-center p-6 transition-colors duration-300">
        <Transition appear enter-active-class="transition duration-700 ease-out"
            enter-from-class="translate-y-8 opacity-0" enter-to-class="translate-y-0 opacity-100">

            <div
                class="w-full max-w-md bg-white dark:bg-gray-900 rounded-xl shadow-lg p-8 border dark:border-gray-800 transition-colors">
                <h1 class="text-3xl font-bold text-gray-800 dark:text-gray-100 mb-8 text-center">Reset Password</h1>

                <form class="space-y-6" @submit.prevent="handleResetRequest">
                    <div>
                        <label for="email"
                            class="block text-sm font-semibold text-gray-600 dark:text-gray-400 mb-1.5 ml-1">
                            Email Address
                        </label>
                        <input id="email" v-model="email" type="email" :class="[
                            'w-full px-4 py-2.5 bg-gray-50 dark:bg-gray-800 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-all text-gray-900 dark:text-white',
                            error ? 'border-red-500 ring-1 ring-red-500' : 'border-gray-200 dark:border-gray-700'
                        ]" placeholder="admin@mail.pt">
                        <p v-if="error" class="mt-1.5 text-xs text-red-500 ml-1 font-medium">{{ error }}</p>
                    </div>

                    <div class="flex flex-col items-center gap-4 pt-2">
                        <button type="submit" :disabled="loading"
                            class="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-blue-400 text-white font-bold py-3 rounded-lg shadow-md hover:shadow-lg transform active:scale-95 transition-all duration-200">
                            {{ loading ? 'SENDING...' : 'SEND RESET LINK' }}
                        </button>

                        <nuxt-link to="/auth/login"
                            class="text-sm text-gray-500 hover:text-blue-600 dark:text-gray-400 dark:hover:text-blue-400 transition-colors">
                            Back to Login
                        </nuxt-link>
                    </div>
                </form>
            </div>
        </Transition>
    </div>
</template>