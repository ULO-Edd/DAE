<template>
    <div
        class="min-h-screen bg-slate-50 dark:bg-slate-950 font-sans text-slate-900 dark:text-slate-200 selection:bg-blue-100 dark:selection:bg-blue-900 pb-12 transition-colors duration-300">

        <CreatePostModal :is-open="isModalOpen" :post-to-edit="postToEdit" :api="api" :token="token" @close="closeModal"
            @refresh="fetchProfileData" />

        <Transition enter-active-class="transition duration-200 ease-out" enter-from-class="opacity-0"
            enter-to-class="opacity-100" leave-active-class="transition duration-150 ease-in"
            leave-from-class="opacity-100" leave-to-class="opacity-0">
            <div v-if="selectedPost" class="fixed inset-0 bg-black/60 backdrop-blur-sm z-50 overflow-y-auto"
                @click.self="selectedPost = null">

                <div class="min-h-screen px-4 py-8 flex items-start justify-center">
                    <div
                        class="self-center w-full max-w-3xl bg-white dark:bg-gray-900 rounded-2xl shadow-2xl border dark:border-gray-800 relative">

                        <PostDetail :post="selectedPost" :api="api" :token="token" @back="selectedPost = null"
                            @comment-added="handleCommentAdded" />

                    </div>
                </div>
            </div>
        </Transition>

        <div class="container mx-auto max-w-7xl px-4 py-8 md:py-12">
            <div class="grid grid-cols-1 md:grid-cols-12 gap-8 lg:gap-12">

                <div class="md:col-span-4 lg:col-span-3 flex flex-col gap-6">

                    <div
                        class="bg-white dark:bg-slate-900 rounded-2xl p-6 shadow-sm border border-slate-200 dark:border-slate-800 transition-colors duration-300">
                        <div class="flex flex-col items-center text-center">
                            <div class="relative inline-block mb-4">
                                <div
                                    class="h-32 w-32 rounded-full border-4 border-white dark:border-slate-800 bg-slate-100 dark:bg-slate-800 overflow-hidden shadow-md ring-1 ring-slate-200 dark:ring-slate-700">
                                    <img :src="userProfile.avatarUrl" :alt="userProfile.name"
                                        class="h-full w-full object-cover transition-transform hover:scale-105">
                                </div>
                                <div class="absolute bottom-2 right-2 h-5 w-5 rounded-full border-[3px] border-white dark:border-slate-900 shadow-sm"
                                    :class="userProfile.is_active ? 'bg-emerald-500' : 'bg-gray-400'"
                                    title="Online Status" />
                            </div>

                            <h1 class="text-2xl font-bold text-slate-900 dark:text-white tracking-tight">{{
                                userProfile.name }}
                            </h1>
                            <p
                                class="text-sm font-semibold text-slate-500 dark:text-slate-400 mb-6 uppercase tracking-wide">
                                {{ userProfile.role }}
                            </p>

                            <div class="w-full space-y-3">
                                <button
                                    class="w-full py-2.5 px-4 bg-slate-900 dark:bg-blue-600 hover:bg-slate-800 dark:hover:bg-blue-500 text-white font-medium rounded-lg text-sm transition-all shadow-sm active:scale-95 cursor-pointer"
                                    @click=" activeTab = 'settings'">
                                    Edit Profile
                                </button>
                            </div>
                        </div>

                        <div class="mt-8 pt-6 border-t border-slate-100 dark:border-slate-800 space-y-4">
                            <div class="flex items-center gap-3 text-sm text-slate-600 dark:text-slate-400">
                                <svg class="w-4 h-4 text-slate-400 dark:text-slate-500 shrink-0" fill="none"
                                    viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                                </svg>
                                <span class="truncate">{{ userProfile.email }}</span>
                            </div>
                            <div class="flex items-center gap-3 text-sm text-slate-600 dark:text-slate-400">
                                <svg class="w-4 h-4 text-slate-400 dark:text-slate-500 shrink-0" fill="none"
                                    viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                                </svg>
                                <span>Polytechnic of Leiria</span>
                            </div>
                        </div>
                    </div>

                    <div class="grid grid-cols-2 gap-4">
                        <div
                            class="bg-white dark:bg-slate-900 p-4 rounded-xl shadow-sm border border-slate-200 dark:border-slate-800 text-center hover:border-slate-300 dark:hover:border-slate-700 transition-colors">
                            <div class="text-2xl font-bold text-slate-900 dark:text-white">{{ totalCitations }}</div>
                            <div
                                class="text-[10px] font-bold text-slate-400 dark:text-slate-500 uppercase tracking-widest mt-1">
                                Citations
                            </div>
                        </div>
                        <div
                            class="bg-white dark:bg-slate-900 p-4 rounded-xl shadow-sm border border-slate-200 dark:border-slate-800 text-center hover:border-slate-300 dark:hover:border-slate-700 transition-colors">
                            <div class="text-2xl font-bold text-slate-900 dark:text-white">{{
                                userProfile.publications.length
                            }}</div>
                            <div
                                class="text-[10px] font-bold text-slate-400 dark:text-slate-500 uppercase tracking-widest mt-1">
                                Publications
                            </div>
                        </div>
                    </div>
                </div>

                <div class="md:col-span-8 lg:col-span-9 space-y-6">

                    <div
                        class="bg-white dark:bg-slate-900 p-1.5 rounded-xl border border-slate-200 dark:border-slate-800 shadow-sm flex gap-1 overflow-x-auto transition-colors">
                        <button
                            :class="activeTab === 'overview' ? 'bg-slate-100 dark:bg-slate-800 text-slate-900 dark:text-white shadow-sm ring-1 ring-black/5 dark:ring-white/5' : 'text-slate-500 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white hover:bg-slate-50 dark:hover:bg-slate-800'"
                            class="flex-1 min-w-[100px] py-2.5 px-4 rounded-lg text-lg font-semibold cursor-pointer transition-colors"
                            @click="activeTab = 'overview'">
                            Overview
                        </button>
                        <button
                            :class="activeTab === 'activity' ? 'bg-slate-100 dark:bg-slate-800 text-slate-900 dark:text-white shadow-sm ring-1 ring-black/5 dark:ring-white/5' : 'text-slate-500 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white hover:bg-slate-50 dark:hover:bg-slate-800'"
                            class="flex-1 min-w-[100px] py-2.5 px-4 rounded-lg text-lg font-medium transition-colors cursor-pointer"
                            @click="activeTab = 'activity'">
                            Activity Log
                        </button>
                        <button
                            :class="activeTab === 'settings' ? 'bg-slate-100 dark:bg-slate-800 text-slate-900 dark:text-white shadow-sm ring-1 ring-black/5 dark:ring-white/5' : 'text-slate-500 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white hover:bg-slate-50 dark:hover:bg-slate-800'"
                            class="flex-1 min-w-[100px] py-2.5 px-4 rounded-lg text-lg font-medium transition-colors cursor-pointer"
                            @click="activeTab = 'settings'">
                            Settings
                        </button>
                    </div>

                    <Transition mode="out-in" enter-active-class="transition duration-300 ease-out"
                        enter-from-class="transform opacity-0 translate-y-2"
                        enter-to-class="transform opacity-100 translate-y-0"
                        leave-active-class="transition duration-200 ease-in"
                        leave-from-class="transform opacity-100 translate-y-0"
                        leave-to-class="transform opacity-0 translate-y-2">
                        <div v-if="activeTab === 'overview'" key="overview" class="space-y-6">
                            <div class="flex items-center justify-between pt-2 px-1">
                                <h2 class="text-lg font-bold text-slate-900 dark:text-white flex items-center gap-2">
                                    Recent Publications
                                    <span
                                        class="bg-slate-200 dark:bg-slate-800 text-slate-700 dark:text-slate-300 text-xs font-bold px-2 py-0.5 rounded-full">
                                        {{ userProfile.publications.length }} Publications
                                    </span>
                                </h2>
                            </div>

                            <div class="flex flex-col gap-4">
                                <div v-if="userProfile.publications.length === 0 && !loading"
                                    class="relative overflow-hidden rounded-2xl border-2 border-dashed border-slate-300 dark:border-slate-700 bg-slate-50 dark:bg-slate-900/50 p-12 text-center hover:border-blue-400 dark:hover:border-blue-500 transition-all duration-300 group">

                                    <div
                                        class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-48 h-48 bg-blue-500/10 dark:bg-blue-400/10 rounded-full blur-3xl opacity-0 group-hover:opacity-100 transition-opacity duration-500 pointer-events-none" />

                                    <div class="relative z-10 flex flex-col items-center">
                                        <div
                                            class="mb-4 p-4 bg-white dark:bg-slate-800 rounded-full shadow-sm ring-1 ring-slate-100 dark:ring-slate-700 group-hover:scale-110 group-hover:ring-blue-200 dark:group-hover:ring-blue-800 transition-all duration-300">
                                            <svg class="w-8 h-8 text-blue-500 dark:text-blue-400" fill="none"
                                                viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                                                    d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                                            </svg>
                                        </div>

                                        <h3 class="text-lg font-bold text-slate-900 dark:text-white mb-1">
                                            No publications yet
                                        </h3>

                                        <button
                                            class="inline-flex items-center gap-2 px-6 py-2.5 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-xl shadow-lg shadow-blue-500/20 hover:shadow-blue-500/30 transform active:scale-95 transition-all duration-200 cursor-pointer"
                                            @click="goToCreatePost()">
                                            <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M12 4v16m8-8H4" />
                                            </svg>
                                            Submit your first publication
                                        </button>
                                    </div>
                                </div>

                                <div v-for="post in userProfile.publications" :key="post.id"
                                    :class="['relative group bg-white dark:bg-gray-900 border dark:border-gray-800 rounded-xl p-5 shadow-sm hover:border-blue-500/50 transition-all cursor-pointer mb-4 overflow-hidden', post.hidden ? 'opacity-60 bg-gray-50' : 'bg-white']"
                                    @click="fetchPostDetail(post.id)">

                                    <div class="flex gap-4">
                                        <div
                                            class="flex flex-col items-center gap-1 text-gray-500 dark:text-gray-400 bg-gray-50 dark:bg-gray-800/50 p-2 rounded-lg h-fit">
                                            <button class="hover:text-blue-500 transition-colors text-lg"
                                                :class="{ 'text-orange-500': post.my_vote === 'UP' }"
                                                @click.stop="votePost(post, 1)">▲</button>
                                            <span class="text-xs font-bold">{{ post.rating }}</span>
                                            <button class="hover:text-red-500 transition-colors text-lg"
                                                :class="{ 'text-blue-500': post.my_vote === 'DOWN' }"
                                                @click.stop="votePost(post, -1)">▼</button>
                                        </div>
                                        <div class="flex-1">
                                            <div class="flex items-center gap-2 mb-1">
                                                <span
                                                    class="text-[10px] font-bold uppercase tracking-wider px-2 py-0.5 rounded bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-400">
                                                    {{ post.type.replace('_', ' ') }}
                                                </span>
                                                <span class="text-xs text-gray-500">{{ new
                                                    Date(post.publicationDate).toLocaleDateString('en-US', {
                                                        year:
                                                            'numeric', month: 'long', day: 'numeric'
                                                    }) }}</span>
                                                <button
                                                    class="p-1 text-slate-400 hover:text-blue-600 hover:bg-blue-50 dark:hover:bg-blue-900/20 rounded-lg transition-all transform hover:scale-110 active:scale-95 cursor-pointer"
                                                    title="Edit" @click.stop="openEditModal(post)">
                                                    <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24"
                                                        stroke="currentColor">
                                                        <path stroke-linecap="round" stroke-linejoin="round"
                                                            stroke-width="2"
                                                            d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                                                    </svg>
                                                </button>
                                                <button
                                                    class="p-1 text-slate-400 hover:text-amber-600 hover:bg-amber-50 dark:hover:bg-amber-900/20 rounded-lg transition-all transform hover:scale-110 active:scale-95 cursor-pointer"
                                                    :title="post.hiddenidden ? 'Unhide' : 'Hide'"
                                                    @click.stop="toggleHide(post)">
                                                    <svg width="16px" height="16px" viewBox="0 0 16 16"
                                                        xmlns="http://www.w3.org/2000/svg" fill="currentColor">
                                                        <path
                                                            d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.133 13.133 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5c2.12 0 3.879 1.168 5.168 2.457A13.133 13.133 0 0 1 14.828 8c-.058.087-.122.183-.195.288-.335.48-.83 1.12-1.465 1.755C11.879 11.332 10.119 12.5 8 12.5c-2.12 0-3.879-1.168-5.168-2.457A13.134 13.134 0 0 1 1.172 8z" />
                                                        <path
                                                            d="M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5zM4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0z" />

                                                        <line v-if="post.hidden" x1="3" y1="3" x2="13" y2="13"
                                                            stroke="currentColor" stroke-width="1.5"
                                                            stroke-linecap="round" />
                                                    </svg>
                                                </button>
                                            </div>
                                            <h2 class="text-lg font-bold text-gray-800 dark:text-white mb-2">{{
                                                post.title }}</h2>
                                            <p class="text-sm text-gray-600 dark:text-gray-400 mb-4 line-clamp-2">By {{
                                                post.authors.join(', ') }}</p>
                                            <div
                                                class="flex items-center gap-4 text-xs font-bold text-gray-500 uppercase">
                                                <span>💬 {{ post.commentCount }} Comments</span>
                                                <div class="flex gap-2">
                                                    <span v-for="tag in post.tags" :key="tag.id"
                                                        class="text-blue-600 dark:text-blue-400">#{{ tag.name }}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div v-else-if="activeTab === 'activity'" key="activity" class="space-y-4">
                            <h2 class="text-lg font-bold text-slate-900 dark:text-white px-1">User History</h2>

                            <div v-if="activityLog.length === 0"
                                class="text-center py-12 border border-dashed border-slate-200 dark:border-slate-800 rounded-xl">
                                <p class="text-slate-500">No activity recorded.</p>
                            </div>

                            <div
                                class="bg-white dark:bg-slate-900 rounded-xl shadow-sm border border-slate-200 dark:border-slate-800 overflow-hidden">
                                <div v-for="item in activityLog" :key="item.id"
                                    class="group p-4 flex gap-5 border-b border-slate-100 dark:border-slate-800 last:border-0 hover:bg-slate-50/80 dark:hover:bg-slate-800/30 transition-all duration-200">

                                    <div
                                        class="flex flex-col items-end min-w-[90px] pt-1 border-r border-slate-100 dark:border-slate-800 pr-4">
                                        <span
                                            class="text-[11px] font-semibold text-slate-600 dark:text-slate-400 tabular-nums">
                                            {{ new Date(item.timestamp).toLocaleDateString('en-US', {
                                                month: 'short',
                                            day: 'numeric', year: 'numeric' }) }}
                                        </span>
                                        <span class="text-[10px] text-slate-400 tabular-nums mt-0.5">
                                            {{ new Date(item.timestamp).toLocaleTimeString([], {
                                                hour: '2-digit',
                                            minute: '2-digit' }) }}
                                        </span>
                                    </div>

                                    <div class="flex-1">
                                        <div class="flex items-center gap-3 mb-1.5">
                                            <span
                                                class="px-2 py-0.5 rounded-full text-[10px] font-bold tracking-tight border"
                                                :class="{
                                                    'bg-emerald-50 text-emerald-700 border-emerald-100 dark:bg-emerald-500/10 dark:text-emerald-400 dark:border-emerald-500/20': item.actionType.includes('UPLOAD'),
                                                    'bg-blue-50 text-blue-700 border-blue-100 dark:bg-blue-500/10 dark:text-blue-400 dark:border-blue-500/20': item.actionType.includes('COMMENT'),
                                                    'bg-indigo-50 text-indigo-700 border-indigo-100 dark:bg-indigo-500/10 dark:text-indigo-400 dark:border-indigo-500/20': item.actionType.includes('EDIT'),
                                                    'bg-amber-50 text-amber-700 border-amber-100 dark:bg-amber-500/10 dark:text-amber-400 dark:border-amber-500/20': item.actionType.includes('RATED') || item.actionType.includes('CORRECT'),
                                                    'bg-purple-50 text-purple-700 border-purple-100 dark:bg-purple-500/10 dark:text-purple-400 dark:border-purple-500/20': item.actionType.includes('TAG')
                                                }">
                                                {{ item.actionType.replace('_', ' ') }}
                                            </span>

                                            <span class="text-[11px] text-slate-400 font-medium">
                                                {{ item.targetType }} #{{ item.targetId }}
                                            </span>
                                        </div>

                                        <p class="text-sm text-slate-600 dark:text-slate-300 leading-relaxed">
                                            {{ item.description }}
                                        </p>
                                    </div>

                                    <div class="flex items-center opacity-0 group-hover:opacity-100 transition-opacity">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-slate-300"
                                            fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="9 5l7 7-7 7" />
                                        </svg>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div v-else-if="activeTab === 'settings'" key="settings"
                            class="bg-white dark:bg-slate-900 rounded-2xl shadow-sm border border-slate-200 dark:border-slate-800 transition-colors">
                            <div class="px-6 py-5 border-b border-slate-200 dark:border-slate-800">
                                <h2 class="text-lg font-semibold text-slate-900 dark:text-white">
                                    Edit Profile
                                </h2>
                                <p class="text-sm text-slate-500 dark:text-slate-400 mt-1">
                                    Update your personal information and account details.
                                </p>
                            </div>

                            <div class="p-6 space-y-6">
                                <div>
                                    <label for="name"
                                        class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                                        Name
                                    </label>
                                    <input id="name" v-model="newUsername" type="text"
                                        placeholder="Your first name and last name" class="w-full px-4 py-2.5 bg-white dark:bg-slate-800
               border border-slate-300 dark:border-slate-700
               rounded-xl text-slate-900 dark:text-white
               placeholder-slate-400
               focus:ring-2 focus:ring-blue-500 focus:border-blue-500
               outline-none transition-all">
                                </div>

                                <div>
                                    <label for="email"
                                        class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                                        Email address
                                    </label>
                                    <input id="email" v-model="newEmail" type="email" placeholder="you@example.com"
                                        class="w-full px-4 py-2.5 bg-white dark:bg-slate-800
               border border-slate-300 dark:border-slate-700
               rounded-xl text-slate-900 dark:text-white
               placeholder-slate-400
               focus:ring-2 focus:ring-blue-500 focus:border-blue-500
               outline-none transition-all">
                                    <p class="mt-1 text-xs text-slate-500 dark:text-slate-400">
                                        Dont worry, Your email is going to be sold to russian hackers.
                                    </p>
                                </div>

                                <a class="text-sm text-red dark:text-red-600 hover:underline"
                                    @click="openResetPassword">
                                    Reset Password
                                </a>
                            </div>

                            <div class="px-6 py-4 bg-slate-50 dark:bg-slate-800/50
           border-t border-slate-200 dark:border-slate-800
           flex justify-end">

                                <button class="inline-flex items-center gap-2
             bg-blue-600 hover:bg-blue-700
             text-white font-semibold
             px-6 py-2.5 rounded-xl
             shadow-sm transition-all
             active:scale-95" @click="updateProfile">
                                    Save changes
                                </button>
                            </div>
                        </div>
                    </Transition>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from "~/stores/auth-store.js";
import { storeToRefs } from "pinia";
import CreatePostModal from '~/components/ui/modal/CreatePostModal.vue';

const config = useRuntimeConfig();
const api = config.public.apiBase;
const authStore = useAuthStore();
const { token } = storeToRefs(authStore);
const router = useRouter();
const notifications = useNotificationStore();

const isModalOpen = ref(false);
const postToEdit = ref(null);

const selectedPost = ref(null);

const newUsername = ref('');
const newEmail = ref('');

const loading = ref(true);
const activeTab = ref('overview');
const activityLog = ref([]);

const userProfile = ref({
    id: 0,
    name: 'Loading...',
    email: '',
    role: '',
    is_active: false,
    avatarUrl: '',
    publications: []
});


const toggleHide = async (post) => {
    try {
        const newState = !post.hidden;

        await $fetch(`${api}/publications/${post.id}/hide`, {
            method: 'PATCH',
            headers: { Authorization: `Bearer ${token.value}` },
            body: { is_hidden: newState }
        });

        post.hidden = newState;
        notifications.success(`Post ${newState ? 'hidden' : 'restored'}`);

    } catch (err) {
        notifications.error("Failed to update post visibility");
    }
};

const openEditModal = (post) => {
    postToEdit.value = post;
    isModalOpen.value = true;
};

const closeModal = () => {
    isModalOpen.value = false;
    postToEdit.value = null;
};

const getFileName = (fullPath) => {
    if (!fullPath) return null;
    return fullPath.split(/[/\\]/).pop();
};

const downloadFile = async (post) => {
    if (!post.attachedFile) return;
    const filename = getFileName(post.attachedFile);
    const downloadUrl = `${api}/publications/download/${filename}`;

    try {
        const link = document.createElement('a');
        link.href = downloadUrl;
        link.setAttribute('download', filename || 'download');
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    } catch (e) {
        console.error("Download error:", e);
    }
};

const fetchPostDetail = async (id) => {
    try {
        const postDetail = await $fetch(`${api}/publications/${id}`, {
            headers: { Authorization: `Bearer ${token.value}` }
        });
        selectedPost.value = postDetail;
        document.body.style.overflow = 'hidden';
    } catch (err) {
        notifications.error("Failed to load publication details");
        console.error(err);
    }
};

const totalCitations = computed(() =>
    userProfile.value.publications.reduce((sum, pub) => sum + (pub.citations || 0), 0)
);

async function fetchActivityHistory() {
    if (!token.value) return;

    try {
        const data = await $fetch(`${api}/users/me/history`, {
            method: 'GET',
            headers: { Authorization: `Bearer ${token.value}` }
        });
        activityLog.value = data;
    } catch (error) {
        console.error("Error fetching history:", error);
    }
}

async function fetchProfileData() {
    if (!token.value) return;
    loading.value = true;

    try {
        const userData = await $fetch(`${api}/users/me`, {
            method: 'GET',
            headers: { Authorization: `Bearer ${token.value}` }
        });
        const publicationsData = await $fetch(`${api}/publications/me`, {
            method: 'GET',
            headers: { Authorization: `Bearer ${token.value}` },
        });

        userProfile.value = {
            ...userProfile.value,
            id: userData.id,
            name: userData.name,
            email: userData.email,
            role: userData.role,
            is_active: userData.is_active,
            avatarUrl: `https://ui-avatars.com/api/?name=${encodeURIComponent(userData.name)}&background=0D8ABC&color=fff`
        };

        if (authStore.user) {
            authStore.user.name = userData.name;
            authStore.user.email = userData.email;
        }

        userProfile.value.publications = (publicationsData).map(pub => {
            const pubDate = new Date(pub.publicationDate);

            return {
                ...pub,
                publishedDate: pubDate.toLocaleDateString(),
                publishedYear: pubDate.getFullYear().toString(),
                citations: pub.rating || 0,
                authors: pub.authors || [],
                tags: pub.tags || [],
                comment_count: pub.comment_count || 0,
                rating: pub.rating || 0
            };
        });

    } catch (error) {
        console.error("Error fetching profile:", error);
    } finally {
        loading.value = false;
    }
}

const votePost = async (targetPost, direction) => {
    if (!targetPost) return;

    let voteTypeToSend = 'NONE';
    const currentVote = targetPost.my_vote || 'NONE';

    if (direction === 1) {
        voteTypeToSend = currentVote === 'UP' ? 'NONE' : 'UP';
    } else if (direction === -1) {
        voteTypeToSend = currentVote === 'DOWN' ? 'NONE' : 'DOWN';
    }

    try {
        const response = await $fetch(`${api}/publications/${targetPost.id}/vote`, {
            method: 'PUT',
            headers: { Authorization: `Bearer ${token.value}` },
            body: { vote_type: voteTypeToSend }
        });

        targetPost.rating = response.score;
        targetPost.my_vote = response.my_vote;

        if (selectedPost.value && selectedPost.value.id === targetPost.id) {
            selectedPost.value.rating = response.score;
            selectedPost.value.my_vote = response.my_vote;
        }

        notifications.success("Voted successfully");
    } catch (error) {
        console.error("Vote failed", error);
        notifications.error("Failed to voted");
    }
};

const goToCreatePost = () => {
    router.push({
        path: '/',
        query: { openCreatePostModal: 'true' }
    });
};

const updateProfile = async () => {
    if (!token.value) {
        return;
    }

    try {
        const name = newUsername.value.trim();
        const email = newEmail.value.trim();


        await $fetch(`${api}/users/me`, {
            method: 'PATCH',
            headers: { Authorization: `Bearer ${token.value}` },
            body: {
                name: name || userProfile.value.name,
                email: email || userProfile.value.email
            }
        });
        notifications.success("Profile updated successfully");

        if (authStore.user) {
            authStore.user.name = name || userProfile.value.name;
            authStore.user.email = email || userProfile.value.email;
        }

    } catch (error) {
        console.error("Profile update failed", error);
        notifications.error("Failed to update profile");
    }

    fetchProfileData();
};

const openResetPassword = () => {
    router.push({ path: '/profile/update-password' });
};

watch(selectedPost, (newVal) => {
    if (!newVal) document.body.style.overflow = 'auto';
});

onMounted(() => {
    fetchProfileData();
    fetchActivityHistory();
});

</script>

<style scoped>
@keyframes fadeInUp {

    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.animate-fade-in-up {
    animation: fadeInUp 0.5s ease-out forwards;
}

.line-clamp-2 {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}
</style>
