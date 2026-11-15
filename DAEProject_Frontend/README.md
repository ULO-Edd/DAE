# DAE Project – Frontend (Scientific Publications Platform)

Web client for a platform where researchers share scientific publications. Built for the **Enterprise Application Development (Desenvolvimento de Aplicações Empresariais, DAE)** course of the Computer Engineering degree at the Polytechnic of Leiria (ESTG, 2025/26).

This app uses the REST API from the **DAEProject_Backend** repository (Jakarta EE + WildFly + PostgreSQL).

## Features

- Login, logout and password recovery
- Feed of publications with a details view, file download and comments
- Create and edit publications with tags and an attached file
- Profile page and password change
- User administration page (administrators only)
- Route guards for guests and authenticated users
- Toast notifications

## Tech stack

- [Nuxt 4](https://nuxt.com/) and Vue 3
- [Pinia](https://pinia.vuejs.org/) for state (`auth-store`, `notification-store`)
- Tailwind CSS 4 and [shadcn-vue](https://www.shadcn-vue.com/) (Reka UI) components
- Nuxt modules: `@nuxt/icon`, `@nuxt/image`, `@nuxt/fonts`, `@nuxt/eslint`

## Project structure

```
app/
├── pages/
│   ├── index.vue                 # Publications feed
│   ├── auth/                     # login, forgot-password
│   ├── profile/                  # profile, update-password
│   └── admin/users/              # user management
├── components/
│   ├── post/                     # Publication components
│   └── ui/                       # shadcn-vue components
├── layouts/  middleware/  plugins/  stores/  lib/
public/                           # Static assets
```

## Running locally

Start the backend first (see the backend README). It serves the API at `http://localhost:8080/academics/api/v1`. To use a different URL, change `runtimeConfig.public.apiBase` in [`nuxt.config.ts`](nuxt.config.ts).

```bash
npm install
npm run dev
```

The app runs at <http://localhost:3000>.

Production build:

```bash
npm run build
npm run preview
```

## Authors

- Eduardo Amado
- Fernando Videira
- Afonso Sousa

Academic project. Polytechnic of Leiria, School of Technology and Management (ESTG).
