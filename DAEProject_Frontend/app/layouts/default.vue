<template>
    <nav
        class="sticky top-0 z-50 w-full border-b border-slate-200/80 dark:border-slate-800/80 bg-white/80 dark:bg-slate-900 backdrop-blur-xl transition-colors duration-300">
        <div class="w-full px-6 md:px-8">
            <div class="flex items-center justify-between h-16">

                <nuxt-link to="/" class="flex items-center gap-3 group flex-shrink-0">
                    <div class="relative">
                        <div
                            class="absolute -inset-1 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-lg blur opacity-25 group-hover:opacity-40 transition duration-300" />
                        <div
                            class="relative w-10 h-10 rounded-lg bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white font-bold text-lg shadow-lg shadow-blue-500/30">
                            A
                        </div>
                    </div>
                    <span class="text-xl font-bold text-slate-900 dark:text-white tracking-tight hidden md:block">
                        Academic<span
                            class="bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">Sys</span>
                    </span>
                </nuxt-link>

                <div class="flex items-center gap-4 md:gap-8">
                    <div class="flex items-center gap-3">

                        <button
                            class="p-2.5 rounded-full text-slate-500 hover:bg-slate-100 dark:hover:bg-slate-800 hover:text-slate-700 dark:hover:text-slate-300 transition-all focus:outline-none focus:ring-2 focus:ring-blue-500/50"
                            @click="toggleDark">
                            <svg v-if="!isDark" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z" />
                            </svg>
                            <svg v-else class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
                            </svg>
                        </button>

                        <div v-if="authStore?.token" ref="userMenuRef" class="relative">
                            <button
                                class="flex items-center gap-3 pl-3 pr-2 py-1.5 rounded-full border border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-800 hover:border-slate-300 dark:hover:border-slate-600 transition-all focus:outline-none focus:ring-2 focus:ring-blue-500/50"
                                :class="{ 'bg-slate-50 dark:bg-slate-800 border-slate-300 dark:border-slate-600': isUserMenuOpen }"
                                @click.stop="isUserMenuOpen = !isUserMenuOpen">
                                <span class="text-sm font-semibold text-slate-700 dark:text-slate-200 hidden sm:block">
                                    {{ authStore.user.name.split(' ')[0] }}
                                </span>
                                <div class="relative">
                                    <div
                                        class="absolute -inset-0.5 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-full blur opacity-30" />
                                    <img :src="authStore.user.avatarUrl || `https://ui-avatars.com/api/?name=${authStore.user.name}&background=0D8ABC&color=fff`"
                                        alt="Avatar"
                                        class="relative w-9 h-9 rounded-full border-2 border-white dark:border-slate-800 object-cover shadow-md">
                                </div>
                            </button>

                            <transition enter-active-class="transition ease-out duration-100"
                                enter-from-class="transform opacity-0 scale-95"
                                enter-to-class="transform opacity-100 scale-100"
                                leave-active-class="transition ease-in duration-75"
                                leave-from-class="transform opacity-100 scale-100"
                                leave-to-class="transform opacity-0 scale-95">
                                <div v-if="isUserMenuOpen"
                                    class="absolute right-0 mt-2 w-64 bg-white dark:bg-slate-900 rounded-xl shadow-xl border border-slate-200 dark:border-slate-800 py-2 focus:outline-none z-50 ring-1 ring-black/5 dark:ring-white/5">
                                    <div class="px-4 py-3 border-b border-slate-100 dark:border-slate-800 mb-1">
                                        <p
                                            class="text-xs text-slate-500 dark:text-slate-400 font-semibold uppercase tracking-wide">
                                            Signed in as
                                        </p>
                                        <p class="text-sm font-bold text-slate-900 dark:text-white truncate mt-1">{{
                                            authStore.user.email }}</p>
                                    </div>
                                    <button
                                        class="w-full text-left px-4 py-2.5 text-sm font-medium text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-800 flex items-center gap-3 transition-colors"
                                        @click="navigateTo('/profile')">
                                        <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                                        </svg>
                                        Your Profile
                                    </button>
                                    <div v-if="authStore.user.role == 'ADMINISTRATOR'">
                                        <button
                                            class="w-full text-left px-4 py-2.5 text-sm font-medium text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-800 flex items-center gap-3 transition-colors"
                                            @click="navigateTo('/admin/users')">
                                            <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-10c1.657 0 3 .895 3 2s-1.343 2-3 2-3 .895-3 2 1.343 2 3 2m0-10v10m0 0l3-3m-3 3l-3-3" />
                                            </svg>
                                            Admin Dashboard
                                        </button>
                                    </div>
                                    <div class="border-t border-slate-100 dark:border-slate-800 mt-1 pt-1">
                                        <button
                                            class="w-full text-left px-4 py-2.5 text-sm font-medium text-red-600 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-900/20 flex items-center gap-3 transition-colors"
                                            @click="handleLogout">
                                            <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                                            </svg>
                                            Sign out
                                        </button>
                                    </div>
                                </div>
                            </transition>
                        </div>

                        <template v-else>
                            <nuxt-link to="/auth/login"
                                class="hidden sm:inline-flex items-center justify-center px-6 py-2.5 text-sm font-semibold text-white bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 rounded-lg shadow-lg shadow-blue-500/30 hover:shadow-blue-500/40 transition-all duration-200 transform hover:-translate-y-0.5 active:translate-y-0">
                                Sign In
                            </nuxt-link>
                        </template>

                        <button
                            class="md:hidden p-2 rounded-lg text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
                            @click="isMobileMenuOpen = !isMobileMenuOpen">
                            <svg v-if="!isMobileMenuOpen" class="w-7 h-7" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M4 6h16M4 12h16M4 18h16" />
                            </svg>
                            <svg v-else class="w-7 h-7" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                    </div>
                </div>

                <transition enter-active-class="transition duration-200 ease-out"
                    enter-from-class="transform -translate-y-4 opacity-0"
                    enter-to-class="transform translate-y-0 opacity-100"
                    leave-active-class="transition duration-150 ease-in"
                    leave-from-class="transform translate-y-0 opacity-100"
                    leave-to-class="transform -translate-y-4 opacity-0">
                    <div v-if="isMobileMenuOpen"
                        class="md:hidden absolute left-0 right-0 top-16 border-t border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900 shadow-xl">
                        <div class="px-4 pt-2 pb-6 space-y-2">
                            <nuxt-link v-for="link in navLinks" :key="link.path" :to="link.path"
                                class="block px-4 py-3 rounded-lg text-base font-semibold text-slate-700 dark:text-slate-200 hover:bg-slate-50 dark:hover:bg-slate-800 hover:text-blue-600 dark:hover:text-blue-400 transition-colors"
                                active-class="bg-gradient-to-r from-blue-50 to-indigo-50 dark:from-blue-900/30 dark:to-indigo-900/30 text-blue-600 dark:text-blue-400"
                                @click="isMobileMenuOpen = false">
                                {{ link.name }}
                            </nuxt-link>
                            <template v-if="!authStore?.token">
                                <nuxt-link to="/auth/login"
                                    class="block w-full text-center mt-6 px-5 py-3 text-base font-bold text-white bg-gradient-to-r from-blue-600 to-indigo-600 rounded-lg shadow-lg shadow-blue-500/30">
                                    Sign In
                                </nuxt-link>
                            </template>
                        </div>
                    </div>
                </transition>
            </div>
        </div>
    </nav>

    <main class="w-full min-h-screen bg-slate-50 dark:bg-slate-950">
        <slot />
    </main>
</template>

<script setup>
import { useAuthStore } from "~/stores/auth-store.js";

const authStore = useAuthStore();
const router = useRouter();

const isDark = ref(false);
const isUserMenuOpen = ref(false);
const isMobileMenuOpen = ref(false);
const userMenuRef = ref(null);

function toggleDark() {
    isDark.value = !isDark.value;
    if (isDark.value) {
        document.documentElement.classList.add('dark');
        localStorage.theme = 'dark';
    } else {
        document.documentElement.classList.remove('dark');
        localStorage.theme = 'light';
    }
}

const navLinks = [
    { name: 'Profile', path: '/profile' },
];

function handleLogout() {
    isUserMenuOpen.value = false;
    isMobileMenuOpen.value = false;
    authStore.logout();
    router.push("/auth/login");
}

function navigateTo(path) {
    isUserMenuOpen.value = false;
    isMobileMenuOpen.value = false;
    router.push(path);
}

function closeDropdowns(event) {
    if (userMenuRef.value && !userMenuRef.value.contains(event.target)) {
        isUserMenuOpen.value = false;
    }
}

onMounted(() => {
    if (localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
        isDark.value = true;
        document.documentElement.classList.add('dark');
    }

    window.addEventListener('click', closeDropdowns);
});

onUnmounted(() => {
    window.removeEventListener('click', closeDropdowns);
});
</script>
