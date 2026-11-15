import { defineStore } from 'pinia'

export const useNotificationStore = defineStore('notifications', {
    state: () => ({
        notifications: []
    }),
    actions: {
        add(message, type = 'success', duration = 3000) {
            const id = Date.now()
            this.notifications.push({ id, message, type })

            setTimeout(() => {
                this.remove(id)
            }, duration)
        },
        remove(id) {
            this.notifications = this.notifications.filter(n => n.id !== id)
        },
        error(msg) { this.add(msg, 'error') },
        success(msg) { this.add(msg, 'success') }
    }
})