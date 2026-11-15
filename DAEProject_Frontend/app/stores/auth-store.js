import { defineStore } from "pinia";

export const useAuthStore = defineStore("authStore", () => {
  const token = useCookie("auth_token", { maxAge: 60 * 15 });
  const user = useCookie("auth_user", { maxAge: 60 * 15 });

  if (process.client) {
    token.value = localStorage.getItem("auth_token");

    const savedUser = localStorage.getItem("auth_user");
    if (savedUser) {
      try {
        user.value = JSON.parse(savedUser);
      } catch (e) {
        console.error("Failed to parse user from storage", e);
      }
    }
  }

  watch([token, user], ([newToken, newUser]) => {
    if (!process.client) return;

    if (newToken) {
      localStorage.setItem("auth_token", newToken);
    } else {
      localStorage.removeItem("auth_token");
    }

    if (newUser) {
      localStorage.setItem("auth_user", JSON.stringify(newUser));
    } else {
      localStorage.removeItem("auth_user");
    }
  }, { deep: true });

  function logout() {
    token.value = null;
    user.value = null;
  }

  return { token, user, logout };
});