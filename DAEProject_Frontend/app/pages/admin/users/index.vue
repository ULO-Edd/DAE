<template>
    <div
        class="min-h-screen bg-slate-50 dark:bg-slate-950 p-4 md:p-8 font-sans text-slate-900 dark:text-slate-200 transition-colors duration-300">

        <div class="max-w-7xl mx-auto space-y-8">

            <!-- Header Section -->
            <div class="flex flex-col md:flex-row justify-between items-start md:items-center gap-6">
                <div>
                    <h1 class="text-3xl md:text-4xl font-extrabold tracking-tight text-slate-900 dark:text-white mb-2">
                        Team Management
                    </h1>
                    <p class="text-slate-600 dark:text-slate-400">
                        Manage user accounts, roles, and system access permissions
                    </p>
                </div>
                <button
                    class="group relative inline-flex items-center gap-2.5 px-6 py-3 bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white font-semibold rounded-xl shadow-lg shadow-blue-500/30 hover:shadow-blue-500/50 transform hover:-translate-y-0.5 active:translate-y-0 transition-all duration-200"
                    @click="openCreateModal">
                    <div
                        class="absolute -inset-0.5 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-xl blur opacity-30 group-hover:opacity-50 transition duration-200" />
                    <svg class="relative w-5 h-5 transition-transform group-hover:rotate-90 duration-300" fill="none"
                        viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                    </svg>
                    <span class="relative">Add Team Member</span>
                </button>
            </div>

            <!-- Stats Grid -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div class="relative group">
                    <div
                        class="absolute -inset-0.5 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-2xl blur opacity-0 group-hover:opacity-20 transition duration-300" />
                    <div
                        class="relative bg-white dark:bg-slate-900 p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm hover:shadow-md transition-all duration-300">
                        <div class="flex items-center gap-4">
                            <div class="relative">
                                <div class="absolute -inset-1 bg-blue-500/20 rounded-xl blur" />
                                <div
                                    class="relative p-3 rounded-xl bg-gradient-to-br from-blue-500 to-blue-600 text-white shadow-lg shadow-blue-500/30">
                                    <svg class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                                    </svg>
                                </div>
                            </div>
                            <div>
                                <div
                                    class="text-3xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
                                    {{ users.length }}
                                </div>
                                <div
                                    class="text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider">
                                    Total Users</div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="relative group">
                    <div
                        class="absolute -inset-0.5 bg-gradient-to-r from-emerald-600 to-teal-600 rounded-2xl blur opacity-0 group-hover:opacity-20 transition duration-300" />
                    <div
                        class="relative bg-white dark:bg-slate-900 p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm hover:shadow-md transition-all duration-300">
                        <div class="flex items-center gap-4">
                            <div class="relative">
                                <div class="absolute -inset-1 bg-emerald-500/20 rounded-xl blur" />
                                <div
                                    class="relative p-3 rounded-xl bg-gradient-to-br from-emerald-500 to-emerald-600 text-white shadow-lg shadow-emerald-500/30">
                                    <svg class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                                    </svg>
                                </div>
                            </div>
                            <div>
                                <div
                                    class="text-3xl font-bold bg-gradient-to-r from-emerald-600 to-teal-600 bg-clip-text text-transparent">
                                    {{ activeCount }}
                                </div>
                                <div
                                    class="text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider">
                                    Active Accounts</div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="relative group">
                    <div
                        class="absolute -inset-0.5 bg-gradient-to-r from-purple-600 to-pink-600 rounded-2xl blur opacity-0 group-hover:opacity-20 transition duration-300" />
                    <div
                        class="relative bg-white dark:bg-slate-900 p-6 rounded-2xl border border-slate-200 dark:border-slate-800 shadow-sm hover:shadow-md transition-all duration-300">
                        <div class="flex items-center gap-4">
                            <div class="relative">
                                <div class="absolute -inset-1 bg-purple-500/20 rounded-xl blur" />
                                <div
                                    class="relative p-3 rounded-xl bg-gradient-to-br from-purple-500 to-purple-600 text-white shadow-lg shadow-purple-500/30">
                                    <svg class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                                    </svg>
                                </div>
                            </div>
                            <div>
                                <div
                                    class="text-3xl font-bold bg-gradient-to-r from-purple-600 to-pink-600 bg-clip-text text-transparent">
                                    {{ adminCount }}
                                </div>
                                <div
                                    class="text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider">
                                    Administrators</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Search Bar -->
            <div class="relative group">
                <div
                    class="absolute -inset-0.5 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-2xl blur opacity-0 group-hover:opacity-10 transition duration-300" />
                <div
                    class="relative bg-white dark:bg-slate-900 p-1.5 rounded-2xl shadow-sm border border-slate-200 dark:border-slate-800 hover:border-blue-300 dark:hover:border-blue-700/50 transition-all">
                    <div class="flex items-center gap-3 px-4">
                        <svg class="w-5 h-5 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                        </svg>
                        <input v-model="searchQuery" type="text" placeholder="Search by name or email..."
                            class="flex-1 py-3 bg-transparent border-none text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none text-base">
                    </div>
                </div>
            </div>

            <!-- Users Table -->
            <div class="relative group">
                <div
                    class="absolute -inset-0.5 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-2xl blur opacity-0 group-hover:opacity-5 transition duration-500" />
                <div
                    class="relative bg-white dark:bg-slate-900 rounded-2xl shadow-lg border border-slate-200 dark:border-slate-800 overflow-hidden">
                    <div class="overflow-x-auto">
                        <table class="w-full text-left border-collapse">
                            <thead>
                                <tr
                                    class="bg-gradient-to-r from-slate-50 to-slate-100/50 dark:from-slate-800/50 dark:to-slate-800/30 border-b border-slate-200 dark:border-slate-800">
                                    <th
                                        class="p-6 pl-8 text-xs font-bold text-slate-600 dark:text-slate-400 uppercase tracking-wider">
                                        User</th>
                                    <th
                                        class="p-6 text-xs font-bold text-slate-600 dark:text-slate-400 uppercase tracking-wider">
                                        Role</th>
                                    <th
                                        class="p-6 text-xs font-bold text-slate-600 dark:text-slate-400 uppercase tracking-wider">
                                        Status</th>
                                    <th
                                        class="p-6 pr-8 text-right text-xs font-bold text-slate-600 dark:text-slate-400 uppercase tracking-wider">
                                        Actions</th>
                                </tr>
                            </thead>
                            <tbody class="divide-y divide-slate-100 dark:divide-slate-800">
                                <tr v-if="loading" class="animate-pulse">
                                    <td colspan="4" class="p-12 text-center text-slate-400">Loading users...</td>
                                </tr>
                                <tr v-else-if="filteredUsers.length === 0">
                                    <td colspan="4" class="p-12 text-center">
                                        <div class="flex flex-col items-center justify-center text-slate-500">
                                            <svg class="w-12 h-12 text-slate-300 mb-3" fill="none" viewBox="0 0 24 24"
                                                stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                                            </svg>
                                            <p>No users found matching your search.</p>
                                        </div>
                                    </td>
                                </tr>
                                <tr v-for="user in filteredUsers" :key="user.id"
                                    class="group/row hover:bg-slate-50/50 dark:hover:bg-slate-800/30 transition-colors duration-150 cursor-pointer"
                                    @click="openUserDetails(user)">
                                    <td class="p-6 pl-8">
                                        <div class="flex items-center gap-4">
                                            <div class="relative">
                                                <div
                                                    class="absolute -inset-1 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-full blur opacity-0 group-hover/row:opacity-30 transition duration-300" />
                                                <div
                                                    class="relative w-11 h-11 rounded-full bg-gradient-to-br from-blue-500 to-indigo-600 text-white flex items-center justify-center font-bold text-sm shadow-md shadow-blue-500/20">
                                                    {{ getInitials(user.name) }}
                                                </div>
                                            </div>
                                            <div>
                                                <div
                                                    class="font-semibold text-slate-900 dark:text-white group-hover/row:text-blue-600 dark:group-hover/row:text-blue-400 transition-colors">
                                                    {{ user.name }}
                                                </div>
                                                <div class="text-sm text-slate-500 dark:text-slate-400">{{ user.email }}
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                    <td class="p-6">
                                        <span
                                            class="inline-flex items-center px-3 py-1.5 rounded-lg text-xs font-bold border capitalize"
                                            :class="{
                                                'bg-purple-100 border-purple-200 text-purple-700 dark:bg-purple-900/20 dark:border-purple-800 dark:text-purple-300': user.role === 'ADMINISTRATOR',
                                                'bg-emerald-100 border-emerald-200 text-emerald-700 dark:bg-emerald-900/20 dark:border-emerald-800 dark:text-emerald-300': user.role === 'MODERATOR',
                                                'bg-amber-100 border-amber-200 text-amber-700 dark:bg-amber-900/20 dark:border-amber-800 dark:text-amber-300': user.role === 'COLLABORATOR'
                                            }">
                                            {{ user.role.toLowerCase() }}
                                        </span>
                                    </td>
                                    <td class="p-6">
                                        <span
                                            class="inline-flex items-center gap-2 px-3 py-1.5 rounded-full text-xs font-semibold shadow-sm border"
                                            :class="user.is_active ? 'bg-emerald-100 border-emerald-200 text-emerald-700 dark:bg-emerald-900/30 dark:border-emerald-800 dark:text-emerald-400' : 'bg-red-100 border-red-200 text-red-700 dark:bg-red-900/30 dark:border-red-800 dark:text-red-400'">
                                            <span class="w-1.5 h-1.5 rounded-full"
                                                :class="user.is_active ? 'bg-emerald-500' : 'bg-red-500'" />
                                            {{ user.is_active ? 'Active' : 'Suspended' }}
                                        </span>
                                    </td>
                                    <td class="p-6 pr-8 text-right">
                                        <div
                                            class="flex items-center justify-end gap-2 opacity-0 group-hover/row:opacity-100 transition-opacity duration-200">
                                            <button
                                                class="p-2.5 text-slate-400 hover:text-blue-600 hover:bg-blue-50 dark:hover:bg-blue-900/20 rounded-lg transition-all transform hover:scale-110 active:scale-95"
                                                title="Edit" @click.stop="openEditModal(user)">
                                                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24"
                                                    stroke="currentColor">
                                                    <path stroke-linecap="round" stroke-linejoin="round"
                                                        stroke-width="2"
                                                        d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                                                </svg>
                                            </button>
                                            <button
                                                class="p-2.5 text-slate-400 hover:text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition-all transform hover:scale-110 active:scale-95"
                                                title="Delete" @click.stop="confirmDelete(user)">
                                                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24"
                                                    stroke="currentColor">
                                                    <path stroke-linecap="round" stroke-linejoin="round"
                                                        stroke-width="2"
                                                        d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                                </svg>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal -->
        <Transition enter-active-class="duration-300 ease-out" enter-from-class="opacity-0" enter-to-class="opacity-100"
            leave-active-class="duration-200 ease-in" leave-from-class="opacity-100" leave-to-class="opacity-0">
            <div v-if="showModal" class="fixed inset-0 z-[100] overflow-y-auto" role="dialog" aria-modal="true">
                <div class="fixed inset-0 bg-slate-900/60 backdrop-blur-sm transition-opacity" @click="closeModal" />
                <div class="flex min-h-full items-center justify-center p-4">
                    <Transition enter-active-class="duration-300 ease-out"
                        enter-from-class="opacity-0 scale-95 translate-y-4"
                        enter-to-class="opacity-100 scale-100 translate-y-0" leave-active-class="duration-200 ease-in"
                        leave-from-class="opacity-100 scale-100 translate-y-0"
                        leave-to-class="opacity-0 scale-95 translate-y-4">
                        <div class="relative w-full max-w-2xl">
                            <div
                                class="absolute -inset-1 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-3xl blur opacity-20" />
                            <div
                                class="relative overflow-hidden rounded-2xl bg-white dark:bg-slate-900 shadow-2xl border border-slate-200 dark:border-slate-800">

                                <div
                                    class="relative bg-gradient-to-r from-slate-50 to-slate-100/50 dark:from-slate-800/50 dark:to-slate-800/30 px-8 py-6 border-b border-slate-200 dark:border-slate-800 flex justify-between items-center">
                                    <div class="flex items-center gap-3">
                                        <div
                                            class="w-1.5 h-8 bg-gradient-to-b from-blue-600 to-indigo-600 rounded-full" />
                                        <h3 class="text-xl font-bold text-slate-900 dark:text-white">{{ isEditing ?
                                            'Edit User Details' : 'Create' }}</h3>
                                    </div>
                                    <button class="p-2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-300"
                                        @click="closeModal"><svg class="w-5 h-5" fill="none" viewBox="0 0 24 24"
                                            stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M6 18L18 6M6 6l12 12" />
                                        </svg></button>
                                </div>

                                <div class="p-8 space-y-6">
                                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                                        <div>
                                            <label
                                                class="block text-xs font-bold text-slate-600 dark:text-slate-400 uppercase tracking-wider mb-2">Full
                                                Name</label>
                                            <input v-model="formData.name" :disabled="isEditing" type="text"
                                                class="w-full px-4 py-3 rounded-xl border-2 border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white disabled:opacity-60 disabled:cursor-not-allowed transition-all"
                                                placeholder="John Doe">
                                        </div>
                                        <div>
                                            <label
                                                class="block text-xs font-bold text-slate-600 dark:text-slate-400 uppercase tracking-wider mb-2">Email
                                                Address</label>
                                            <input v-model="formData.email" :disabled="isEditing" type="email"
                                                class="w-full px-4 py-3 rounded-xl border-2 border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white disabled:opacity-60 disabled:cursor-not-allowed transition-all"
                                                placeholder="john@example.com">
                                        </div>
                                    </div>

                                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                                        <div>
                                            <label
                                                class="block text-xs font-bold text-slate-600 dark:text-slate-400 uppercase tracking-wider mb-2">Role</label>
                                            <div class="relative">
                                                <select v-model="formData.role"
                                                    class="w-full appearance-none px-4 py-3 rounded-xl border-2 border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white transition-all cursor-pointer">
                                                    <option value="COLLABORATOR">Collaborator</option>
                                                    <option value="MODERATOR">Moderator</option>
                                                    <option value="ADMIN">Admin</option>
                                                </select>
                                                <svg class="absolute right-4 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400 pointer-events-none"
                                                    fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                    <path stroke-linecap="round" stroke-linejoin="round"
                                                        stroke-width="2" d="M19 9l-7 7-7-7" />
                                                </svg>
                                            </div>
                                        </div>

                                        <div v-if="!isEditing">
                                            <label
                                                class="block text-xs font-bold text-slate-600 dark:text-slate-400 uppercase tracking-wider mb-2">Password</label>
                                            <input v-model="formData.password" type="password"
                                                class="w-full px-4 py-3 rounded-xl border-2 border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white transition-all"
                                                placeholder="••••••••">
                                        </div>

                                        <div v-if="isEditing">
                                            <label
                                                class="block text-xs font-bold text-slate-600 dark:text-slate-400 uppercase tracking-wider mb-2">Account
                                                Status</label>
                                            <label
                                                class="flex items-center gap-3 p-4 rounded-xl border-2 border-slate-200 dark:border-slate-700 cursor-pointer hover:bg-slate-50 dark:hover:bg-slate-800/50 transition-all h-[52px]">
                                                <div class="relative inline-flex items-center">
                                                    <input v-model="formData.is_active" type="checkbox"
                                                        class="sr-only peer">
                                                    <div
                                                        class="w-11 h-6 bg-slate-200 rounded-full peer peer-checked:bg-blue-600 dark:bg-slate-700 peer-checked:after:translate-x-full after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all" />
                                                </div>
                                                <span class="text-sm font-semibold transition-colors"
                                                    :class="formData.is_active ? 'text-slate-900 dark:text-white' : 'text-slate-500'">{{
                                                        formData.is_active ? 'Active' : 'Suspended' }}</span>
                                            </label>
                                        </div>
                                    </div>

                                    <div v-if="isEditing" class="pt-2 border-t border-slate-100 dark:border-slate-800">
                                        <button type="button"
                                            class="text-sm text-red-500 hover:text-red-600 font-semibold flex items-center gap-2"
                                            @click="sendPasswordRecovery">
                                            <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M15 7a2 2 0 012 2m4 0a6 6 0 01-7.743 5.743L11 17H9v2H7v2H4a1 1 0 01-1-1v-2.586a1 1 0 01.293-.707l5.964-5.964A6 6 0 1121 9z" />
                                            </svg>
                                            Send Password Recovery
                                        </button>
                                    </div>
                                </div>

                                <div
                                    class="bg-gradient-to-r from-slate-50 to-slate-100/50 dark:from-slate-800/50 dark:to-slate-800/30 px-8 py-6 border-t border-slate-200 dark:border-slate-800 flex flex-row-reverse gap-3">
                                    <button
                                        class="px-8 py-3 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-xl shadow-lg transition-all"
                                        @click="handleSubmit">{{
                                            isEditing ? 'Save Changes' : 'Create' }}</button>
                                    <button
                                        class="px-8 py-3 bg-white dark:bg-slate-800 border-2 border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-700 text-slate-700 dark:text-slate-300 font-semibold rounded-xl transition-all"
                                        @click="closeModal">Cancel</button>
                                </div>
                            </div>
                        </div>
                    </Transition>
                </div>
            </div>
        </Transition>

        <!-- User Details Modal -->
        <Transition enter-active-class="duration-300 ease-out" enter-from-class="opacity-0" enter-to-class="opacity-100"
            leave-active-class="duration-200 ease-in" leave-from-class="opacity-100" leave-to-class="opacity-0">
            <div v-if="showDetailsModal" class="fixed inset-0 z-[100] overflow-y-auto" role="dialog" aria-modal="true">
                <div class="fixed inset-0 bg-slate-900/60 backdrop-blur-sm transition-opacity"
                    @click="closeDetailsModal" />
                <div class="flex min-h-full items-center justify-center p-4">
                    <Transition enter-active-class="duration-300 ease-out"
                        enter-from-class="opacity-0 scale-95 translate-y-4"
                        enter-to-class="opacity-100 scale-100 translate-y-0" leave-active-class="duration-200 ease-in"
                        leave-from-class="opacity-100 scale-100 translate-y-0"
                        leave-to-class="opacity-0 scale-95 translate-y-4">

                        <div v-if="selectedUser"
                            class="relative w-full max-w-4xl max-h-[85vh] flex flex-col bg-white dark:bg-slate-900 rounded-2xl shadow-2xl border border-slate-200 dark:border-slate-800 overflow-hidden">

                            <div
                                class="flex-none relative bg-gradient-to-r from-slate-50 to-slate-100/50 dark:from-slate-800/50 dark:to-slate-800/30 px-8 py-6 border-b border-slate-200 dark:border-slate-800">
                                <div class="flex justify-between items-start">
                                    <div class="flex items-center gap-4">
                                        <div
                                            class="w-16 h-16 rounded-full bg-gradient-to-br from-blue-500 to-indigo-600 text-white flex items-center justify-center font-bold text-xl">
                                            {{ getInitials(selectedUser.name) }}</div>
                                        <div>
                                            <h3 class="text-2xl font-bold text-slate-900 dark:text-white">{{
                                                selectedUser.name }}</h3>
                                            <p class="text-sm text-slate-500 dark:text-slate-400 mt-1">{{
                                                selectedUser.email }}</p>
                                            <div class="flex items-center gap-2 mt-2">
                                                <span
                                                    class="inline-flex items-center px-2.5 py-1 rounded-lg text-xs font-bold border capitalize bg-slate-100 dark:bg-slate-800 dark:text-slate-300 border-slate-200 dark:border-slate-700">{{
                                                        selectedUser.role.toLowerCase() }}</span>
                                            </div>
                                        </div>
                                    </div>
                                    <button class="p-2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-300"
                                        @click="closeDetailsModal"><svg class="w-5 h-5" fill="none" viewBox="0 0 24 24"
                                            stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M6 18L18 6M6 6l12 12" />
                                        </svg></button>
                                </div>
                            </div>

                            <div class="flex-1 overflow-y-auto p-8">
                                <div class="flex items-center justify-between mb-6">
                                    <h4
                                        class="text-lg font-bold text-slate-900 dark:text-white flex items-center gap-2">
                                        Activity History</h4>
                                    <span class="text-sm text-slate-500 font-medium">{{ userHistory.length }}
                                        events</span>
                                </div>
                                <div v-if="userHistory.length === 0"
                                    class="text-center py-12 border border-dashed border-slate-200 dark:border-slate-800 rounded-xl">
                                    <p class="text-slate-500">No activity recorded.</p>
                                </div>
                                <div v-else class="space-y-4">
                                    <div v-for="(item, index) in userHistory" :key="item.id"
                                        class="relative bg-slate-50 dark:bg-slate-800/50 rounded-xl p-5 border border-slate-200 dark:border-slate-800">
                                        <div class="flex gap-4">
                                            <div class="flex flex-col items-center min-w-[90px] text-center">
                                                <span class="text-xs font-bold text-slate-500 uppercase mb-1">
                                                    {{ new Date(item.timestamp).toLocaleDateString('en-US', {
                                                        month:
                                                    'short', day: 'numeric' }) }}
                                                </span>
                                                <span class="text-xs text-slate-400">
                                                    {{ new Date(item.timestamp).toLocaleTimeString([], {
                                                        hour:
                                                    '2-digit', minute: '2-digit' }) }}
                                                </span>
                                            </div>
                                            <div class="flex-1">
                                                <div class="flex items-center gap-2 mb-2">
                                                    <span
                                                        class="px-2.5 py-1 rounded-md text-xs font-bold uppercase tracking-wide bg-slate-200 dark:bg-slate-700 text-slate-700 dark:text-slate-300">
                                                        {{ item.actionType.replace('_', ' ') }}
                                                    </span>
                                                    <span class="text-xs text-slate-400 font-medium">
                                                        {{ item.targetType }} #{{ item.targetId }}
                                                    </span>
                                                </div>
                                                <p class="text-sm text-slate-700 dark:text-slate-300 leading-relaxed">
                                                    {{ item.description }}
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div
                                class="flex-none bg-gradient-to-r from-slate-50 to-slate-100/50 dark:from-slate-800/50 dark:to-slate-800/30 px-8 py-4 border-t border-slate-200 dark:border-slate-800">
                                <button
                                    class="w-full px-6 py-3 bg-white dark:bg-slate-800 border-2 border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-700 text-slate-700 dark:text-slate-300 font-semibold rounded-xl transition-all"
                                    @click="closeDetailsModal">Close</button>
                            </div>

                        </div>
                    </Transition>
                </div>
            </div>
        </Transition>

    </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import { useAuthStore } from "~/stores/auth-store.js";
import { storeToRefs } from "pinia";

const config = useRuntimeConfig();
const api = config.public.apiBase;
const authStore = useAuthStore();
const { token } = storeToRefs(authStore);
const notifications = useNotificationStore();

const users = ref([]);
const loading = ref(false);
const showModal = ref(false);
const showDetailsModal = ref(false);
const isEditing = ref(false);
const searchQuery = ref('');
const selectedUser = ref(null);
const userHistory = ref([]);

const formData = reactive({
    id: 0,
    name: '',
    email: '',
    password: '',
    role: 'COLLABORATOR',
    is_active: true
});

const filteredUsers = computed(() => {
    if (!searchQuery.value) return users.value;
    const query = searchQuery.value.toLowerCase();
    return users.value.filter(u =>
        u.name.toLowerCase().includes(query) ||
        u.email.toLowerCase().includes(query)
    );
});

const activeCount = computed(() => users.value.filter(u => u.is_active).length);
const adminCount = computed(() => users.value.filter(u => u.role === 'ADMINISTRATOR').length);

const fetchUsers = async () => {
    loading.value = true;
    try {
        const data = await $fetch(`${api}/admin/users`, {
            method: 'GET',
            headers: { Authorization: `Bearer ${token.value}` }
        });
        users.value = data || [];
    } catch (error) {
        console.error("Error fetching users", error);
    } finally {
        loading.value = false;
    }
};

const createUser = async () => {
    try {
        await $fetch(`${api}/admin/users`, {
            method: 'POST',
            headers: { Authorization: `Bearer ${token.value}` },
            body: {
                name: formData.name,
                email: formData.email,
                password: formData.password,
                role: formData.role
            }
        });
        notifications.success("User created successfully");
        closeModal();
        fetchUsers();
    } catch (error) {
        notifications.error("Failed to create user");
    }
};

const updateUser = async () => {
    try {
        await $fetch(`${api}/admin/users/${formData.id}`, {
            method: 'PATCH',
            headers: { Authorization: `Bearer ${token.value}` },
            body: {
                role: formData.role,
                is_active: formData.is_active
            }
        });
        notifications.success("User updated successfully");
        closeModal();
        fetchUsers();
    } catch (error) {
        notifications.error("Failed to update user");
    }
};

const sendPasswordRecovery = async () => {
    if (!confirm(`Are you sure you want to send a password recovery to ${formData.email}?`)) return;

    try {
        await $fetch(`${api}/auth/recover-password`, {
            method: 'POST',
            body: {
                email: formData.email
            }
        });
        notifications.success(`Recovery sent to ${formData.email}`);
    } catch (error) {
        console.error(error);
        notifications.error("Failed to send recovery");
    }
};

const confirmDelete = async (user) => {
    if (!confirm(`Are you sure you want to delete ${user.name}?`)) return;
    try {
        await $fetch(`${api}/admin/users/${user.id}`, {
            method: 'DELETE',
            headers: { Authorization: `Bearer ${token.value}` }
        });
        notifications.success("Deleted user");
        fetchUsers();
    } catch (error) {
        notifications.error("Failed to delete user");
    }
};

const openUserDetails = async (user) => {
    selectedUser.value = user;
    showDetailsModal.value = true;

    try {
        const data = await $fetch(`${api}/users/${user.id}/history`, {
            method: 'GET',
            headers: { Authorization: `Bearer ${token.value}` }
        });

        if (data && typeof data === 'object') {
            userHistory.value = Object.values(data).sort((a, b) => b.timestamp - a.timestamp);
        } else {
            userHistory.value = [];
        }
    } catch (error) {
        console.error("Error fetching user history", error);
        notifications.error("Failed to load activity history");
        userHistory.value = [];
    }
};

const closeDetailsModal = () => {
    showDetailsModal.value = false;
    selectedUser.value = null;
    userHistory.value = [];
};

const openCreateModal = () => {
    isEditing.value = false;
    resetForm();
    showModal.value = true;
};

const openEditModal = (user) => {
    isEditing.value = true;
    formData.id = user.id;
    formData.name = user.name;
    formData.email = user.email;
    formData.role = user.role;
    formData.is_active = user.is_active;
    showModal.value = true;
};

const closeModal = () => {
    showModal.value = false;
    resetForm();
};

const handleSubmit = () => {
    if (isEditing.value) updateUser();
    else createUser();
};

const resetForm = () => {
    formData.id = 0;
    formData.name = '';
    formData.email = '';
    formData.password = '';
    formData.role = 'COLLABORATOR';
    formData.is_active = true;
};

const getInitials = (name) => {
    if (!name) return '??';
    return name.split(' ').map(n => n[0]).join('').substring(0, 2).toUpperCase();
};

onMounted(() => {
    fetchUsers();
});
</script>
