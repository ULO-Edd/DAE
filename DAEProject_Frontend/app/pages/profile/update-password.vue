<script setup>
const config = useRuntimeConfig();
const api = config.public.apiBase;
const authStore = useAuthStore();
const notifications = useNotificationStore();
const router = useRouter();

const passwordData = reactive({
    oldPassword: "",
    newPassword: "",
    confirmNewPassword: ""
});

const error = ref(null);
const loading = ref(false);

async function handleUpdatePassword() {
    error.value = null;

    if (passwordData.newPassword !== passwordData.confirmNewPassword) {
        error.value = "Passwords do not match.";
        return;
    }

    loading.value = true;

    try {
        await $fetch(`${api}/users/me/password`, {
            method: "PUT",
            headers: {
                Authorization: `Bearer ${authStore.token}`
            },
            body: {
                oldPassword: passwordData.oldPassword,
                newPassword: passwordData.newPassword,
                confirmNewPassword: passwordData.confirmNewPassword
            },
            onResponse({ response }) {
                if (response.status === 200) {
                    notifications.success("Password updated successfully.");
                    authStore.user.mustChangePassword = false;
                    router.push("/");
                }
            }
        });
    } catch (e) {
        error.value = "Failed to update password. Please check your current password";
    } finally {
        loading.value = false;
    }
}
</script>

<template>
    <div
        class="min-h-screen bg-gray-100 dark:bg-gray-950 flex flex-col items-center justify-center p-6 transition-colors duration-300">
        <Transition appear enter-active-class="transition duration-700 ease-out"
            enter-from-class="translate-y-8 opacity-0" enter-to-class="translate-y-0 opacity-100">

            <div
                class="w-full max-w-md bg-white dark:bg-gray-900 rounded-xl shadow-lg p-8 border dark:border-gray-800 transition-colors">
                <h1 class="text-2xl font-bold text-gray-800 dark:text-white mb-2 text-center">Password Update</h1>
                <p class="text-sm text-gray-500 dark:text-gray-400 mb-8 text-center">Please update your password to
                    secure your account.</p>

                <form class="space-y-5" @submit.prevent="handleUpdatePassword">
                    <div>
                        <label class="block text-sm font-semibold text-gray-600 dark:text-gray-400 mb-1.5 ml-1">Current
                            Password</label>
                        <input v-model="passwordData.oldPassword" type="password" required
                            class="w-full px-4 py-2.5 bg-gray-50 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-all text-gray-900 dark:text-white"
                            placeholder="••••••••">
                    </div>

                    <hr class="border-gray-100 dark:border-gray-800 my-2">

                    <div>
                        <label class="block text-sm font-semibold text-gray-600 dark:text-gray-400 mb-1.5 ml-1">New
                            Password</label>
                        <input v-model="passwordData.newPassword" type="password" required
                            class="w-full px-4 py-2.5 bg-gray-50 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-all text-gray-900 dark:text-white"
                            placeholder="••••••••">
                    </div>

                    <div>
                        <label class="block text-sm font-semibold text-gray-600 dark:text-gray-400 mb-1.5 ml-1">Confirm
                            New Password</label>
                        <input v-model="passwordData.confirmNewPassword" type="password" required
                            class="w-full px-4 py-2.5 bg-gray-50 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none transition-all text-gray-900 dark:text-white"
                            placeholder="••••••••">
                    </div>

                    <div v-if="error"
                        class="p-3 bg-red-100 dark:bg-red-900/30 border border-red-200 dark:border-red-800 rounded-lg text-red-700 dark:text-red-400 text-xs text-center">
                        {{ error }}
                    </div>

                    <div class="flex flex-col items-center gap-4 pt-4">
                        <button type="submit" :disabled="loading"
                            class="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-blue-400 text-white font-bold py-3 rounded-lg shadow-md hover:shadow-lg transform active:scale-95 transition-all duration-200 capitalize">
                            {{ loading ? 'Updating...' : 'Update Password' }}
                        </button>
                    </div>
                </form>
            </div>
        </Transition>
    </div>
</template>