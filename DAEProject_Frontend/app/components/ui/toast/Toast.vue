<script setup>
const store = useNotificationStore();
</script>

<template>
    <div class="fixed bottom-6 right-6 z-[9999] flex flex-col gap-3 w-80">
        <TransitionGroup name="toast">
            <div v-for="toast in store.notifications" :key="toast.id" :class="[
                'p-4 rounded-lg shadow-xl border text-sm font-medium transition-all duration-300',
                toast.type === 'error'
                    ? 'bg-red-50 border-red-200 text-red-800'
                    : 'bg-green-50 border-green-200 text-green-800'
            ]">
                <div class="flex justify-between items-center">
                    <span>{{ toast.message }}</span>
                    <button class="ml-4 opacity-50 hover:opacity-100" @click="store.remove(toast.id)">×</button>
                </div>
            </div>
        </TransitionGroup>
    </div>
</template>

<style scoped>
.toast-enter-from {
    opacity: 0;
    transform: translateX(50px);
}

.toast-leave-to {
    opacity: 0;
    transform: scale(0.9);
}
</style>