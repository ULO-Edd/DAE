<template>
    <div class="animate-fade-in-up">
        <button
            class="my-6 mx-6 flex items-center gap-2 text-sm font-bold text-blue-600 dark:text-blue-400 hover:underline"
            @click="$emit('back')">
            ← BACK TO FEED
        </button>

        <article class="bg-white dark:bg-gray-900 border dark:border-gray-800 rounded-2xl shadow-xl overflow-hidden">
            <div
                class="relative bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-800 dark:to-slate-900 p-8 border-b dark:border-gray-800">

                <div class="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-4">
                    <span
                        class="bg-blue-100 text-blue-700 dark:bg-blue-900/30 dark:text-blue-300 px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider shadow-sm border border-blue-200 dark:border-blue-800">
                        {{ post.type.replace('_', ' ') }}
                    </span>
                    <span class="text-sm font-medium text-gray-500 dark:text-gray-400 flex items-center gap-2">
                        <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                        </svg>
                        {{ new Date(post.publicationDate).toLocaleDateString('en-US', {
                            year:
                                'numeric', month: 'long', day: 'numeric'
                        }) }}
                    </span>
                </div>

                <div class="flex gap-6 mb-6">
                    <div>
                        <span class="text-[10px] font-bold text-gray-400 uppercase tracking-widest mb-1">Rating</span>
                        <div
                            class="flex items-center gap-2 px-4 py-1.5 bg-white/80 dark:bg-gray-800/80 backdrop-blur shadow-sm rounded-xl border border-gray-200 dark:border-gray-700">
                            <span class="text-xl font-black text-blue-600 dark:text-blue-400">{{ post.rating }}</span>
                        </div>
                    </div>
                    <h1
                        class="content-end text-3xl md:text-4xl font-extrabold text-gray-900 dark:text-white leading-tight">
                        {{
                            post.title }}</h1>
                </div>

                <div
                    class="grid grid-cols-1 md:grid-cols-2 gap-6 p-4 bg-white/50 dark:bg-gray-800/50 rounded-xl border border-gray-200 dark:border-gray-700 backdrop-blur-sm">

                    <div>
                        <h4 class="text-xs font-bold text-gray-400 uppercase tracking-wider mb-2">Authors
                        </h4>
                        <div class="flex flex-wrap flex-col gap-2">
                            <span v-for="(author, idx) in post.authors" :key="idx"
                                class="inline-flex items-center gap-1.5 text-sm font-semibold text-gray-700 dark:text-gray-200">
                                <span
                                    class="w-6 h-6 rounded-full bg-indigo-100 text-indigo-600 dark:bg-indigo-900 dark:text-indigo-300 flex items-center justify-center text-xs">
                                    {{ author.charAt(0) }}
                                </span>
                                {{ author }}
                            </span>
                        </div>
                    </div>

                    <div>
                        <h4 class="text-xs font-bold text-gray-400 uppercase tracking-wider mb-2">Submitted
                            By</h4>
                        <div class="flex items-center gap-3">
                            <div
                                class="w-8 h-8 rounded-full bg-blue-600 text-white flex items-center justify-center font-bold text-xs">
                                {{ post.submitter.name.charAt(0) }}
                            </div>
                            <div>
                                <div class="text-sm font-bold text-gray-900 dark:text-white">{{
                                    post.submitter.name }}</div>
                                <div class="text-xs text-gray-500">{{ post.submitter.role }} • {{
                                    post.submitter.email }}</div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="p-8">
                    <div class="mb-10">
                        <h3 class="text-lg font-bold text-gray-900 dark:text-white mb-4 flex items-center gap-2">
                            <svg class="w-5 h-5 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M4 6h16M4 12h16M4 18h7" />
                            </svg>
                            Abstract
                        </h3>
                        <p
                            class="text-gray-700 dark:text-gray-300 leading-relaxed text-lg text-justify whitespace-pre-line break-normal">
                            {{ post.abstractText }}
                        </p>
                    </div>

                    <div
                        class="flex flex-col md:flex-row justify-between items-start md:items-center gap-6 mb-8 pb-8 border-b dark:border-gray-800">
                        <div v-if="post.tags && post.tags.length > 0">
                            <h4 class="text-xs font-bold text-gray-400 uppercase tracking-wider mb-2">Tags</h4>
                            <div class="flex flex-wrap gap-2">
                                <span v-for="tag in post.tags" :key="tag.id"
                                    class="px-3 py-1 rounded-lg bg-gray-100 dark:bg-gray-800 text-gray-600 dark:text-gray-400 text-sm font-medium border border-gray-200 dark:border-gray-700">
                                    #{{ tag.name }}
                                </span>
                            </div>
                        </div>
                        <div v-else class="text-sm text-gray-400 italic">No tags assigned</div>
                    </div>

                    <div v-if="post.attachedFile"
                        class="mb-10 bg-slate-50 dark:bg-slate-800/50 border border-slate-200 dark:border-slate-700 rounded-xl p-5 hover:border-blue-400 transition-colors group">
                        <div class="flex items-center justify-between">
                            <div class="flex items-center gap-4">
                                <div
                                    class="p-3 bg-red-100 text-red-600 rounded-lg group-hover:scale-110 transition-transform">
                                    <svg class="w-8 h-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M7 21h10a2 2 0 002-2V9.414a1 1 0 00-.293-.707l-5.414-5.414A1 1 0 0012.586 3H7a2 2 0 00-2 2v14a2 2 0 002 2z" />
                                    </svg>
                                </div>
                                <div>
                                    <div class="font-bold text-gray-900 dark:text-white mb-0.5">Attachment
                                        Available</div>
                                    <div class="text-xs text-gray-500 font-mono truncate max-w-[200px] md:max-w-md">
                                        {{ getFileName(post.attachedFile) }}
                                    </div>
                                </div>
                            </div>
                            <button
                                class="flex items-center gap-2 px-5 py-2.5 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-lg shadow-lg shadow-blue-500/30 transform hover:-translate-y-0.5 transition-all"
                                @click="downloadFile(post)">
                                <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
                                </svg>
                                Download
                            </button>
                        </div>
                    </div>

                    <section class="border-t dark:border-gray-800 pt-8">
                        <div class="flex justify-between items-center mb-6">
                            <h3 class="text-xl font-bold flex items-center gap-2">
                                Comments <span class="text-xs bg-gray-100 dark:bg-gray-800 px-2 py-1 rounded-full">{{
                                    totalComments }}</span>
                            </h3>
                            <button
                                class="w-10 h-10 bg-blue-600 hover:bg-blue-700 text-white rounded-full shadow-lg transition-all active:scale-95 flex items-center justify-center group"
                                title="New Post" @click="showCommentInput = !showCommentInput">
                                <svg viewBox="0 0 24 24" class="w-5 h-5 fill-none stroke-current stroke-[2.5]"
                                    xmlns="http://www.w3.org/2000/svg">
                                    <line x1="12" y1="5" x2="12" y2="19" />
                                    <line x1="5" y1="12" x2="19" y2="12" />
                                </svg>
                            </button>
                        </div>

                        <Transition enter-active-class="transition duration-200 ease-out"
                            enter-from-class="transform scale-95 opacity-0"
                            enter-to-class="transform scale-100 opacity-100"
                            leave-active-class="transition duration-150 ease-in"
                            leave-from-class="transform scale-100 opacity-100"
                            leave-to-class="transform scale-95 opacity-0">
                            <div v-if="showCommentInput" class="mb-8 space-y-3">
                                <textarea v-model="commentText" rows="3"
                                    class="w-full px-4 py-3 bg-gray-50 dark:bg-gray-800 border dark:border-gray-700 rounded-lg outline-none text-sm focus:ring-2 focus:ring-blue-500"
                                    placeholder="What are your thoughts?" />
                                <div class="flex justify-end">
                                    <button :disabled="!commentText.trim() || submitting"
                                        class="bg-blue-600 hover:bg-blue-700 disabled:bg-gray-400 text-white font-bold px-6 py-2 rounded-lg transition-all"
                                        @click="handleCommentSubmit">
                                        {{ submitting ? 'POSTING...' : 'COMMENT' }}
                                    </button>
                                </div>
                            </div>
                        </Transition>

                        <div class="space-y-6">
                            <div v-for="comment in comments" :key="comment.id" class="flex gap-4">
                                <div
                                    class="w-8 h-8 rounded-full bg-gradient-to-br from-gray-200 to-gray-300 dark:from-gray-700 dark:to-gray-600 shrink-0 flex items-center justify-center text-[10px] font-bold">
                                    {{ comment.author.name.charAt(0) }}
                                </div>
                                <div class="flex-1">
                                    <div
                                        class="bg-gray-50 dark:bg-gray-800/50 p-4 rounded-xl rounded-tl-none border dark:border-gray-800">
                                        <div class="flex justify-between mb-2">
                                            <div>
                                                <span class="mr-3 font-bold text-sm text-blue-600 dark:text-blue-400">{{
                                                    comment.author.name }}</span>
                                                <button v-if="authStore.user.id == comment.author.id"
                                                    class="p-1 text-slate-400 hover:text-blue-600 hover:bg-blue-50 dark:hover:bg-blue-900/20 rounded-lg transition-all transform hover:scale-110 active:scale-95 cursor-pointer"
                                                    title="Edit" @click.stop="startEdit(comment)">
                                                    <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24"
                                                        stroke="currentColor">
                                                        <path stroke-linecap="round" stroke-linejoin="round"
                                                            stroke-width="2"
                                                            d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                                                    </svg>
                                                </button>
                                                <button
                                                    v-if="authStore.user.id == comment.author.id || authStore.user.role != 'COLLABORATOR'"
                                                    class="p-1 text-slate-400 hover:text-amber-600 hover:bg-amber-50 dark:hover:bg-amber-900/20 rounded-lg transition-all transform hover:scale-110 active:scale-95 cursor-pointer"
                                                    :title="post.hidden ? 'Unhide' : 'Hide'"
                                                    @click.stop="toggleHide(comment)">
                                                    <svg width="16px" height="16px" viewBox="0 0 16 16"
                                                        xmlns="http://www.w3.org/2000/svg" fill="currentColor">
                                                        <path
                                                            d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.133 13.133 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5c2.12 0 3.879 1.168 5.168 2.457A13.133 13.133 0 0 1 14.828 8c-.058.087-.122.183-.195.288-.335.48-.83 1.12-1.465 1.755C11.879 11.332 10.119 12.5 8 12.5c-2.12 0-3.879-1.168-5.168-2.457A13.134 13.134 0 0 1 1.172 8z" />
                                                        <path
                                                            d="M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5zM4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0z" />

                                                        <line v-if="comment.hidden" x1="3" y1="3" x2="13" y2="13"
                                                            stroke="currentColor" stroke-width="1.5"
                                                            stroke-linecap="round" />
                                                    </svg>
                                                </button>
                                            </div>

                                            <span class="text-[10px] text-gray-400 uppercase">{{ new
                                                Date(comment.createdAt).toLocaleDateString('en-US', {
                                                    year:
                                                        'numeric', month: 'long', day: 'numeric'
                                                }) }}</span>
                                        </div>
                                        <div class="mt-2">
                                            <div v-if="editingCommentId === comment.id" class="space-y-2">
                                                <textarea v-model="editText"
                                                    class="w-full px-3 py-2 text-sm bg-white dark:bg-gray-800 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                                    rows="2" />
                                                <div class="flex gap-2 justify-end">
                                                    <button class="text-xs font-bold text-gray-500 hover:text-gray-700"
                                                        @click="cancelEdit">CANCEL</button>
                                                    <button class="text-xs font-bold text-blue-600 hover:text-blue-700"
                                                        :disabled="submitting" @click="handleEditSubmit(comment)">
                                                        {{ submitting ? 'SAVING...' : 'SAVE' }}
                                                    </button>
                                                </div>
                                            </div>

                                            <p v-else
                                                :class="['text-sm leading-relaxed whitespace-pre-wrap break-normal', comment.hidden ? 'text-gray-400 italic opacity-50' : 'text-gray-700 dark:text-gray-300']">
                                                {{ comment.hidden ? 'This comment has been hidden.' : comment.content }}
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="h-20 flex flex-col items-center justify-center">
                                <p v-if="comments.length === 0 && !loadingComments"
                                    class="text-sm text-gray-500 italic py-4">No comments yet. Be the first!</p>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </article>
    </div>
</template>

<script setup>
const props = defineProps({
    post: Object,
    api: String,
    token: String
});

const emit = defineEmits(['back', 'comment-added']);

const authStore = useAuthStore();
const notifications = useNotificationStore();
const comments = ref([]);
const commentText = ref('');
const submitting = ref(false);
const loadingComments = ref(false);
const showCommentInput = ref(false);
const totalComments = ref(0);
const editingCommentId = ref(null);
const editText = ref('');

const toggleHide = async (comment) => {
    try {
        comment.hidden = !comment.hidden;

        await $fetch(`${props.api}/comments/${comment.id}`, {
            method: 'PATCH',
            headers: { Authorization: `Bearer ${props.token}` },
            body: { is_hidden: comment.hidden }
        });

        notifications.success(comment.hidden ? "Comment hidden" : "Comment visible");
    } catch (err) {
        comment.hidden = !comment.hidden;
        notifications.error("Failed to update comment visibility");
    }
};

const startEdit = (comment) => {
    editingCommentId.value = comment.id;
    editText.value = comment.content;
};

const cancelEdit = () => {
    editingCommentId.value = null;
    editText.value = '';
};

const handleEditSubmit = async (comment) => {
    if (!editText.value.trim() || submitting.value) return;

    submitting.value = true;
    try {
        await $fetch(`${props.api}/comments/${comment.id}`, {
            method: 'PUT',
            headers: { Authorization: `Bearer ${props.token}` },
            body: { content: editText.value }
        });

        comment.content = editText.value;
        editingCommentId.value = null;
        notifications.success("Comment updated!");
    } catch (err) {
        notifications.error("Failed to edit comment");
    } finally {
        submitting.value = false;
    }
};

const fetchComments = async () => {
    if (loadingComments.value) return;
    loadingComments.value = true;
    try {
        const data = await $fetch(`${props.api}/publications/${props.post.id}/comments`, {
            headers: { Authorization: `Bearer ${props.token}` }
        });

        comments.value = data.items || data;
        totalComments.value = data.total || comments.value.length;
    } catch (err) {
        console.error("Comments fetch error", err);
    } finally {
        loadingComments.value = false;
    }
};

const handleCommentSubmit = async () => {
    submitting.value = true;
    try {
        const newComment = await $fetch(`${props.api}/publications/${props.post.id}/comments`, {
            method: 'POST',
            headers: { Authorization: `Bearer ${props.token}` },
            body: { content: commentText.value }
        });

        comments.value.unshift(newComment);
        totalComments.value++;
        commentText.value = '';
        showCommentInput.value = false;
        emit('comment-added');
        notifications.success("Comment posted!");
    } catch (err) {
        notifications.error("Failed to post comment");
    } finally {
        submitting.value = false;
    }
};

const getFileName = (fullPath) => {
    if (!fullPath) return null;
    return fullPath.split(/[/\\]/).pop();
};

const downloadFile = async () => {
    if (!props.post.attachedFile) return;

    const filename = getFileName(props.post.attachedFile);

    const downloadUrl = `${props.api}/publications/download/${filename}`;

    try {
        const link = document.createElement('a');
        link.href = downloadUrl;
        link.setAttribute('download', filename);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    } catch (e) {
        notifications.error("Could not download file.");
    }
};

onMounted(() => {
    fetchComments();
});
</script>