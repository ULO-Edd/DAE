export default defineNuxtRouteMiddleware((to, from) => {
    const authStore = useAuthStore();
    const isLoggedIn = !!(authStore.user || authStore.token);
    const mustChangePassword = authStore.user?.mustChangePassword === true;

    const isAuthPage = to.path.startsWith('/auth');
    const isUpdatePasswordPage = to.path === '/profile/update-password';

    if (!isLoggedIn && !isAuthPage) {
        return navigateTo('/auth/login');
    }

    if (isLoggedIn && mustChangePassword && !isUpdatePasswordPage) {
        return navigateTo('/profile/update-password');
    }

    if (isLoggedIn && isAuthPage) {
        return navigateTo('/');
    }
});