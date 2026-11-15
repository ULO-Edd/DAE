import tailwindcss from "@tailwindcss/vite";

export default defineNuxtConfig({
    compatibilityDate: "2026-01-01",
    devtools: { enabled: true },
    app: {
        head: {
        link: [
            { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' },
        ]
        }
    },
    css: [
        "~/assets/css/main.css",
        "~/assets/css/tailwind.css"
    ],
    vite: {
        plugins: [tailwindcss()],
    },

    runtimeConfig: {
        public: {
            apiBase: "http://localhost:8080/academics/api/v1",
        },
    },

    modules: [
        "@nuxt/fonts",
        "@nuxt/icon",
        "@nuxt/image",
        "@nuxt/eslint",
        "shadcn-nuxt",
        "@pinia/nuxt",
    ],

    shadcn: {
        prefix: "",
        /**
         * Directory that the component lives in.
         * @default "./app/components/ui"
         */
        componentDir: "./app/components/ui",
    },
});
