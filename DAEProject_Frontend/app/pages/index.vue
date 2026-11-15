<template>
  <div
    class="min-h-screen bg-gray-100 dark:bg-gray-950 text-gray-900 dark:text-gray-100 transition-colors duration-300">
    <div class="max-w-6xl mx-auto flex gap-6 py-8 px-4">

      <aside v-if="!selectedPost" class="hidden lg:block w-fit min-w-[160px] py-24">
        <div class="sticky top-24">
          <div class="flex flex-col gap-2">
            <button v-if="user?.role !== 'COLLABORATOR'"
              class="mb-2 px-3 py-2 rounded-lg text-xs font-bold bg-blue-600 hover:bg-blue-700 text-white shadow-md transition-all active:scale-95 flex items-center justify-center gap-1"
              @click="showCreateInput = !showCreateInput">
              <span>+</span> NEW TAG
            </button>

            <div v-if="showCreateInput" class="mb-2">
              <input ref="createInputRef" v-model="newTagName" placeholder="Tag name..."
                class="w-full px-3 py-2 rounded-lg text-xs font-bold bg-white dark:bg-gray-800 border-2 border-blue-500 outline-none"
                autofocus @keyup.enter="handleCreateTag" @blur="showCreateInput = false">
            </div>
            <div v-for="tag in availableTags" :key="tag.id" class="relative group">
              <button v-if="editingTagId !== tag.id" :class="[
                'w-full px-3 py-2 rounded-lg text-xs font-bold transition-all text-left shadow-sm cursor-pointer flex justify-between items-center pr-8',
                subscribedTags.includes(tag.id)
                  ? `${tag.color} border-current ring-1 ring-inset ring-black/5`
                  : 'bg-gray-50 dark:bg-gray-800 dark:border-gray-800 border opacity-60 hover:opacity-100'
              ]" @click="toggleTagSubscription(tag)">
                <span class="truncate">
                  <span class="opacity-70 mr-1">{{ subscribedTags.includes(tag.id) ? '✓' : '#' }}</span>
                  {{ tag.name }}
                </span>
              </button>

              <input v-else v-model="editTagName"
                class="w-full px-3 py-2 rounded-lg text-xs font-bold bg-white dark:bg-gray-900 border-2 border-blue-500 outline-none"
                autofocus @keyup.enter="handleEditTag(tag.id)" @blur="editingTagId = null">

              <button v-if="user?.role !== 'COLLABORATOR' && editingTagId !== tag.id"
                class="absolute right-2 top-1/2 -translate-y-1/2 opacity-0 group-hover:opacity-100 p-1 hover:text-blue-500 transition-all"
                @click.stop="startEditing(tag)">
                <svg class="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </aside>

      <main :class="[
        'py-8 flex-1 transition-all duration-500 ease-in-out',
        selectedPost ? 'max-w-3xl mx-auto' : 'max-w-2xl'
      ]">

        <div v-show="!selectedPost" class="space-y-4">
          <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
            <div class="flex flex-wrap items-center gap-2 w-full sm:w-auto">
              <div class="relative flex-1 sm:flex-none">
                <input v-model.lazy="searchQuery" type="text" placeholder="Search publications..."
                  class="w-full sm:w-48 bg-white dark:bg-gray-900 border dark:border-gray-800 rounded-lg pl-3 pr-3 py-2 text-xs font-bold outline-none focus:ring-2 focus:ring-blue-500"
                  @keyup.enter="$el.blur()">
              </div>
              <div class="flex items-center">
                <select v-model="sortBy"
                  class="bg-white dark:bg-gray-900 border dark:border-gray-800 rounded-l-lg px-3 py-2 text-xs font-bold uppercase outline-none focus:ring-1 focus:ring-blue-500 cursor-pointer border-r-0">
                  <option value="date">Date</option>
                  <option value="rating">Rating</option>
                  <option value="comment">Comments</option>
                </select>
                <button
                  class="bg-white dark:bg-gray-900 border dark:border-gray-800 rounded-r-lg px-3 py-2 text-xs font-bold hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors"
                  :title="sortDesc ? 'Descending' : 'Ascending'" @click="sortDesc = !sortDesc">
                  <span class="inline-block transition-transform duration-200" :class="{ 'rotate-180': !sortDesc }">
                    ▼
                  </span>
                </button>
              </div>

              <select v-model="filterType"
                class="bg-white dark:bg-gray-900 border dark:border-gray-800 rounded-lg px-3 py-2 text-xs font-bold uppercase outline-none focus:ring-1 focus:ring-blue-500 cursor-pointer">
                <option value="ALL">All Types</option>
                <option value="CONFERENCE_PAPER">Conference Paper</option>
                <option value="RESEARCH_POST">Research Post</option>
                <option value="THESIS">Thesis</option>
                <option value="ARTICLE">Article</option>
                <option value="REPORT">Report</option>
                <option value="OTHER">Other</option>
              </select>
            </div>

            <button
              class="w-10 h-10 bg-blue-600 hover:bg-blue-700 text-white rounded-full shadow-lg transition-all active:scale-95 flex items-center justify-center group"
              title="New Post" @click="isModalOpen = true">
              <svg viewBox="0 0 24 24" class="w-5 h-5 fill-none stroke-current stroke-[2.5]"
                xmlns="http://www.w3.org/2000/svg">
                <line x1="12" y1="5" x2="12" y2="19" />
                <line x1="5" y1="12" x2="19" y2="12" />
              </svg>
            </button>
          </div>
          <div v-if="filteredPosts.length === 0 && !loading"
            class="relative overflow-hidden rounded-2xl border-2 border-dashed border-slate-300 dark:border-slate-700 bg-slate-50 dark:bg-slate-900/50 p-12 text-center hover:border-blue-400 dark:hover:border-blue-500 transition-all duration-300 group">

            <div
              class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-48 h-48 bg-blue-500/10 dark:bg-blue-400/10 rounded-full blur-3xl opacity-0 group-hover:opacity-100 transition-opacity duration-500 pointer-events-none" />

            <div class="relative z-10 flex flex-col items-center">
              <div
                class="mb-4 p-4 bg-white dark:bg-slate-800 rounded-full shadow-sm ring-1 ring-slate-100 dark:ring-slate-700 group-hover:scale-110 group-hover:ring-blue-200 dark:group-hover:ring-blue-800 transition-all duration-300">
                <svg class="w-8 h-8 text-blue-500 dark:text-blue-400" fill="none" viewBox="0 0 24 24"
                  stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                    d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                </svg>
              </div>

              <h3 class="text-lg font-bold text-slate-900 dark:text-white mb-1">
                No publications yet
              </h3>

              <button
                class="inline-flex items-center gap-2 px-6 py-2.5 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-xl shadow-lg shadow-blue-500/20 hover:shadow-blue-500/30 transform active:scale-95 transition-all duration-200 cursor-pointer"
                @click="isModalOpen = true">
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                </svg>
                Submit your first post
              </button>
            </div>
          </div>
          <TransitionGroup v-else appear enter-active-class="transition duration-500 ease-out"
            enter-from-class="translate-y-4 opacity-0" enter-to-class="translate-y-0 opacity-100">
            <div v-for="post in filteredPosts" :key="post.id"
              :class="['relative group bg-white dark:bg-gray-900 border dark:border-gray-800 rounded-xl p-5 shadow-sm hover:border-blue-500/50 transition-all cursor-pointer mb-4 overflow-hidden', post.hidden ? 'opacity-60 bg-gray-50' : 'bg-white']"
              @click="fetchPostDetail(post.id)">

              <div class="flex gap-4">
                <div
                  class="flex flex-col items-center gap-1 text-gray-500 dark:text-gray-400 bg-gray-50 dark:bg-gray-800/50 p-2 rounded-lg h-fit">

                  <button class="hover:text-blue-500 transition-colors text-lg"
                    :class="{ 'text-orange-500': post.my_vote === 'UP' }" @click.stop="votePost(post, 1)">▲</button>

                  <span class="text-xs font-bold">{{ post.rating }}</span>

                  <button class="hover:text-red-500 transition-colors text-lg"
                    :class="{ 'text-blue-500': post.my_vote === 'DOWN' }" @click.stop="votePost(post, -1)">▼</button>
                </div>

                <div class="flex-1">
                  <div class="flex items-center gap-2 mb-1">
                    <span
                      class="text-[10px] font-bold uppercase tracking-wider px-2 py-0.5 rounded bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-400">
                      {{ post.type.replace('_', ' ') }}
                    </span>
                    <span class="text-xs text-gray-500">{{ new Date(post.publicationDate).toLocaleDateString('en-US', {
                      year:
                        'numeric', month: 'long', day: 'numeric'
                    }) }}</span>
                    <button v-if="authStore.user.id == post.submitter.id"
                      class="p-1 text-slate-400 hover:text-blue-600 hover:bg-blue-50 dark:hover:bg-blue-900/20 rounded-lg transition-all transform hover:scale-110 active:scale-95 cursor-pointer"
                      title="Edit" @click.stop="openEditModal(post)">
                      <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                      </svg>
                    </button>
                    <button v-if="authStore.user.id == post.submitter.id || authStore.user.role != 'COLLABORATOR'"
                      class="p-1 text-slate-400 hover:text-amber-600 hover:bg-amber-50 dark:hover:bg-amber-900/20 rounded-lg transition-all transform hover:scale-110 active:scale-95 cursor-pointer"
                      :title="post.hidden ? 'Unhide' : 'Hide'" @click.stop="toggleHide(post)">
                      <svg width="16px" height="16px" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg"
                        fill="currentColor">
                        <path
                          d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.133 13.133 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5c2.12 0 3.879 1.168 5.168 2.457A13.133 13.133 0 0 1 14.828 8c-.058.087-.122.183-.195.288-.335.48-.83 1.12-1.465 1.755C11.879 11.332 10.119 12.5 8 12.5c-2.12 0-3.879-1.168-5.168-2.457A13.134 13.134 0 0 1 1.172 8z" />
                        <path
                          d="M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5zM4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0z" />

                        <line v-if="post.hidden" x1="3" y1="3" x2="13" y2="13" stroke="currentColor" stroke-width="1.5"
                          stroke-linecap="round" />
                      </svg>
                    </button>
                  </div>
                  <h2 class="text-lg font-bold text-gray-800 dark:text-white mb-2">{{ post.title }}
                  </h2>
                  <p class="text-sm text-gray-600 dark:text-gray-400 mb-4 truncate max-w-[400px]">
                    {{ post.authors.length > 0 ? `By ${post.authors.join(', ')}` : 'No Authors' }}
                  </p>
                  <div class="flex items-center gap-4 text-xs font-bold text-gray-500 uppercase min-w-0">
                    <span class="shrink-0">💬 {{ post.commentCount }} Comments</span>

                    <div class="max-w-[300px] truncate">
                      <span v-for="tag in post.tags" :key="tag.id" class="text-blue-600 dark:text-blue-400 mr-2">
                        #{{ tag.name }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </TransitionGroup>

          <div v-if="loading && filteredPosts.length === 0" class="space-y-4">
            <div v-for="i in 6" :key="i"
              class="bg-white dark:bg-gray-900 border dark:border-gray-800 rounded-xl p-5 animate-pulse">
              <div class="flex gap-4">
                <div class="w-10 h-16 bg-gray-100 dark:bg-gray-800 rounded-lg" />
                <div class="flex-1 space-y-3">
                  <div class="h-3 w-24 bg-gray-100 dark:bg-gray-800 rounded" />
                  <div class="h-5 w-3/4 bg-gray-100 dark:bg-gray-800 rounded" />
                  <div class="h-3 w-full bg-gray-100 dark:bg-gray-800 rounded" />
                </div>
              </div>
            </div>
          </div>

          <div ref="loadMoreTrigger" class="h-20 w-full flex flex-col items-center justify-center gap-2">
            <div v-if="loading"
              class="animate-spin h-6 w-6 border-2 border-blue-600 border-t-transparent rounded-full" />
          </div>
        </div>

        <PostDetail v-if="selectedPost" :post="selectedPost" :api="api" :token="token" @back="selectedPost = null"
          @vote="(dir) => votePost(selectedPost, dir)" @comment-added="() => selectedPost.commentCount++" />

        <CreatePostModal :is-open="isModalOpen" :post-to-edit="postToEdit" :api="api" :token="token"
          @close="closeModal()" @refresh="handleRefresh" />
      </main>
    </div>
  </div>
</template>


<script setup>
import { useAuthStore } from "~/stores/auth-store.js";
import { storeToRefs } from "pinia";
import { useRoute } from 'vue-router';
import CreatePostModal from "~/components/ui/modal/CreatePostModal.vue";

const authStore = useAuthStore();
const { user, token } = storeToRefs(authStore);
const route = useRoute();
const config = useRuntimeConfig();
const notifications = useNotificationStore();
const api = config.public.apiBase;

const posts = ref([]);
const selectedPost = ref(null);
const loading = ref(false);
const loadMoreTrigger = ref(null);
const isModalOpen = ref(false);
const searchQuery = ref('');
const page = ref(1);
const hasMore = ref(true);

const sortBy = ref('date');
const sortDesc = ref(true);
const filterType = ref('ALL');

const subscribedTags = ref([]);
const availableTags = ref([]);
const showCreateInput = ref(false);
const newTagName = ref('');
const editingTagId = ref(null);
const editTagName = ref('');

const postToEdit = ref(null);

const checkQueryForModal = () => {
  if (route.query.openCreatePostModal === 'true') {
    isModalOpen.value = true;
  }
}

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

const handleCreateTag = async () => {
  if (!newTagName.value.trim()) return;

  try {
    await $fetch(`${api}/tags`, {
      method: 'POST',
      headers: { Authorization: `Bearer ${token.value}` },
      body: { name: newTagName.value }
    });
    newTagName.value = '';
    showCreateInput.value = false;
    notifications.success("Tag created successfully");
    await fetchTags();
  } catch (err) {
    notifications.error("Failed to create tag");
  }
};

const startEditing = (tag) => {
  editingTagId.value = tag.id;
  editTagName.value = tag.name;
};

const handleEditTag = async (tagId) => {
  const trimmedName = editTagName.value.trim();
  if (!trimmedName) {
    editingTagId.value = null;
    return;
  }

  try {
    await $fetch(`${api}/tags/${tagId}`, {
      method: 'PATCH',
      headers: { Authorization: `Bearer ${token.value}` },
      body: { name: trimmedName }
    });

    const tagIndex = availableTags.value.findIndex(t => t.id === tagId);
    if (tagIndex !== -1) availableTags.value[tagIndex].name = trimmedName;

    posts.value.forEach(post => {
      const postTag = post.tags.find(t => t.id === tagId);
      if (postTag) {
        postTag.name = trimmedName;
      }
    });

    if (selectedPost.value) {
      const selectedPostTag = selectedPost.value.tags.find(t => t.id === tagId);
      if (selectedPostTag) selectedPostTag.name = trimmedName;
    }

    editingTagId.value = null;
    notifications.success("Tag edited successfully");
  } catch (err) {
    notifications.error("Failed to edit tag");
  }
};

const closeModal = () => {
  const router = useRouter();
  isModalOpen.value = false;
  postToEdit.value = null;
  router.replace({ query: { ...route.query, openCreatePostModal: undefined } });
};

const fetchTags = async () => {
  try {
    const allTags = await $fetch(`${api}/tags`, {
      headers: { Authorization: `Bearer ${token.value}` }
    });

    const colors = [
      "bg-purple-100 dark:bg-purple-900/30 text-purple-700 dark:text-purple-400",
      "bg-green-100 dark:bg-green-900/30 text-green-700 dark:text-green-400",
      "bg-yellow-100 dark:bg-yellow-900/30 text-yellow-700 dark:text-yellow-400",
      "bg-pink-100 dark:bg-pink-900/30 text-pink-700 dark:text-pink-400",
      "bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-400"
    ];

    availableTags.value = allTags.map((tag, index) => ({
      ...tag,
      color: colors[index % colors.length]
    }));


    const myTags = await $fetch(`${api}/tags/me`, {
      headers: { Authorization: `Bearer ${token.value}` }
    });

    subscribedTags.value = myTags.map(t => t.id);
  } catch (err) {
    console.error("Error fetching tags:", err);
    notifications.error("Failed to subscribe to tag");
  }

};

const toggleTagSubscription = async (tag) => {
  const index = subscribedTags.value.indexOf(tag.id);
  const isSubscribing = index === -1;

  if (isSubscribing) subscribedTags.value.push(tag.id);
  else subscribedTags.value.splice(index, 1);

  try {
    const endpoint = isSubscribing
      ? `${api}/tags/${tag.id}/subscribe`
      : `${api}/tags/${tag.id}/unsubscribe`;

    await $fetch(endpoint, {
      method: 'POST',
      headers: { Authorization: `Bearer ${token.value}` }
    });

  } catch (err) {
    if (isSubscribing) subscribedTags.value.pop();
    else subscribedTags.value.push(tag.id);

    console.error("Sync failed:", err);
    notifications.error("Failed to subscribe to tag");
  }

};

const handleRefresh = () => {
  isModalOpen.value = false;
  posts.value = [];
  page.value = 1;
  hasMore.value = true;
  loadPosts();
  window.scrollTo({ top: 0, behavior: 'smooth' })
};

const loadPosts = async () => {
  if (loading.value || !hasMore.value) return;
  loading.value = true;

  try {
    const data = await $fetch(`${api}/publications`, {
      method: 'GET',
      headers: { Authorization: `Bearer ${token.value}` },
      params: {
        page: page.value,
        sort: sortBy.value,
        order: sortDesc.value ? 'desc' : 'asc',
        type: filterType.value !== 'ALL' ? filterType.value : undefined,
        search: searchQuery.value ? searchQuery.value : ''
      }
    });

    if (data && data.length > 0) {
      posts.value = [...posts.value, ...data];
      page.value++;
    } else {
      hasMore.value = false;
    }
  } catch (err) {
    console.error("Failed to fetch posts:", err);
    notifications.error("Failed to load publications");
  } finally {
    loading.value = false;
  }
};

const fetchPostDetail = async (id) => {
  loading.value = true;
  try {
    const postDetail = await $fetch(`${api}/publications/${id}`, {
      headers: { Authorization: `Bearer ${token.value}` }
    });
    selectedPost.value = postDetail;
  } catch (err) {
    console.error("Failed to fetch post detail:", err);
    notifications.error("Failed to fetch details");
  } finally {
    loading.value = false;
  }
};

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

    notifications.success("Voted with success");

  } catch (error) {
    console.error("Vote failed", error);
    notifications.error("Failed to submit vote.");
  }
};

const filteredPosts = computed(() => {
  const result = posts.value.filter(p => {
    const matchesType = filterType.value === 'ALL' || p.type === filterType.value;
    const matchesTags = subscribedTags.value.length === 0 ||
      p.tags.some(t => subscribedTags.value.includes(t.id));
    return matchesType && matchesTags;
  });

  return result.sort((a, b) => {
    const modifier = sortDesc.value ? 1 : -1;

    if (sortBy.value === 'rating') {
      return (b.rating - a.rating) * modifier;
    } else if (sortBy.value === 'comment') {
      return (b.commentCount - a.commentCount) * modifier;
    } else {
      return (new Date(b.publicationDate).getTime() - new Date(a.publicationDate).getTime()) * modifier;
    }
  });
});

watch(posts, () => {
  if (filteredPosts.value.length < 3 && !loading.value && !selectedPost.value) {
    loadPosts();
  }
}, { deep: true });

watch([sortBy, sortDesc, filterType], () => {
  resetAndLoad();
});

watch(searchQuery, () => {
  resetAndLoad();
});

function resetAndLoad() {
  posts.value = [];
  page.value = 1;
  hasMore.value = true;
  loadPosts();
}

onMounted(async () => {
  checkQueryForModal();

  await Promise.all([
    loadPosts(),
    fetchTags()
  ]);

  const observer = new IntersectionObserver((entries) => {
    if (entries[0].isIntersecting && !loading.value && !selectedPost.value) {
      loadPosts();
    }
  }, {
    rootMargin: '300px',
    threshold: 0.1
  });

  if (loadMoreTrigger.value) {
    observer.observe(loadMoreTrigger.value);
  }
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
