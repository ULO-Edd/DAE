<script setup>
import { ref, reactive, watch } from 'vue';

const props = defineProps({
    isOpen: Boolean,
    api: String,
    token: String,
    postToEdit: Object
});

const emit = defineEmits(['close', 'refresh']);

const notifications = useNotificationStore();
const isSubmitting = ref(false);
const availableTags = ref([]);
const errors = reactive({
    title: '',
    abstract: ''
});

const uploadForm = reactive({
    title: '',
    abstract: '',
    type: 'CONFERENCE_PAPER',
    newAuthorName: '',
    authors: [],
    selectedTagIds: [],
    file: null
});

const validate = () => {
    let isValid = true;
    errors.title = '';
    errors.abstract = '';

    if (!uploadForm.title.trim()) {
        errors.title = 'Title is required';
        isValid = false;
    } else if (uploadForm.title.length > 50) {
        errors.title = 'Title cannot exceed 50 characters';
        isValid = false;
    }

    if (!uploadForm.abstract.trim()) {
        errors.abstract = 'Abstract is required';
        isValid = false;
    } else if (uploadForm.abstract.length > 2000) {
        errors.abstract = 'Abstract cannot exceed 2000 characters';
        isValid = false;
    }

    return isValid;
};

async function fetchTags() {
    if (!props.token) return;
    try {
        const data = await $fetch(`${props.api}/tags`, {
            headers: { Authorization: `Bearer ${props.token}` }
        });
        availableTags.value = data || [];
    } catch (e) { console.error("Tags fetch failed", e); }
}

async function submitPublication() {
    if (!validate()) return;

    isSubmitting.value = true;
    try {
        const metadata = {
            title: uploadForm.title,
            authors: uploadForm.authors,
            type: uploadForm.type,
            abstract: uploadForm.abstract,
            tags: uploadForm.selectedTagIds
        };

        const isEditing = !!props.postToEdit;
        const url = isEditing
            ? `${props.api}/publications/${props.postToEdit.id}`
            : `${props.api}/publications`;

        const headers = {
            Authorization: `Bearer ${props.token}`
        };

        const formData = new FormData();
        if (uploadForm.file) formData.append('file', uploadForm.file);
        formData.append('data', JSON.stringify(metadata));

        await $fetch(url, {
            method: isEditing ? 'PATCH' : 'POST',
            headers,
            body: formData
        });

        notifications.success(`Publication ${isEditing ? "updated" : "created"}`);
        emit('refresh');
        closeModal();
    } catch (error) {
        console.error("Submission error:", error);
        notifications.error("Operation failed");
    } finally {
        isSubmitting.value = false;
    }
}

function addAuthor() {
    if (uploadForm.newAuthorName.trim()) {
        uploadForm.authors.push(uploadForm.newAuthorName.trim());
        uploadForm.newAuthorName = '';
    }
}

function removeAuthor(index) { uploadForm.authors.splice(index, 1); }

function handleFileChange(event) {
    const input = event.target;
    if (input.files?.[0]) uploadForm.file = input.files[0];
}

function closeModal() {
    uploadForm.title = '';
    uploadForm.abstract = '';
    uploadForm.type = 'CONFERENCE_PAPER';
    uploadForm.newAuthorName = '';
    uploadForm.authors = [];
    uploadForm.selectedTagIds = [];
    uploadForm.file = null;
    emit('close');
}

watch(() => props.isOpen, (newVal) => {
    if (newVal && availableTags.value.length === 0) fetchTags();
    if (!newVal) {
        errors.title = '';
        errors.abstract = '';
    }
});

watch(() => props.postToEdit, (newPost) => {
    if (newPost) {
        uploadForm.title = newPost.title;
        uploadForm.abstract = newPost.abstractText || newPost.abstract;
        uploadForm.type = newPost.type;
        uploadForm.authors = [...newPost?.authors];
        uploadForm.selectedTagIds = newPost?.tags.map((t) => t.id);
    }
}, { immediate: true });

const getTagClass = (id) => {
    const colors = [
        'bg-pink-100 text-pink-700 border-pink-200 dark:bg-pink-900/30 dark:text-pink-300',
        'bg-purple-100 text-purple-700 border-purple-200 dark:bg-purple-900/30 dark:text-purple-300',
        'bg-indigo-100 text-indigo-700 border-indigo-200 dark:bg-indigo-900/30 dark:text-indigo-300',
        'bg-cyan-100 text-cyan-700 border-cyan-200 dark:bg-cyan-900/30 dark:text-cyan-300',
        'bg-emerald-100 text-emerald-700 border-emerald-200 dark:bg-emerald-900/30 dark:text-emerald-300',
        'bg-amber-100 text-amber-700 border-amber-200 dark:bg-amber-900/30 dark:text-amber-300',
    ];
    return colors[id % colors.length];
};
</script>

<template>
    <Transition enter-active-class="transition duration-200 ease-out" enter-from-class="opacity-0"
        enter-to-class="opacity-100" leave-active-class="transition duration-150 ease-in" leave-from-class="opacity-100"
        leave-to-class="opacity-0">
        <div v-if="props.isOpen" class="fixed inset-0 z-50 overflow-y-auto">
            <div class="fixed inset-0 bg-slate-900/75 backdrop-blur-sm" @click="closeModal" />

            <div class="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
                <div
                    class="relative transform overflow-hidden rounded-2xl bg-white dark:bg-slate-900 text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-2xl border border-slate-200 dark:border-slate-800">

                    <div
                        class="bg-slate-50 dark:bg-slate-800/50 px-6 py-4 border-b border-slate-100 dark:border-slate-800 flex justify-between items-center">
                        <h3 class="text-lg font-bold text-slate-900 dark:text-white">Submit New Publication</h3>
                        <button class="text-slate-400 hover:text-slate-500 dark:hover:text-slate-300"
                            @click="closeModal">
                            <svg class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                    </div>

                    <div class="px-6 py-6 max-h-[80vh] overflow-y-auto space-y-6">
                        <div
                            class="flex justify-center rounded-lg border-2 border-dashed border-slate-300 dark:border-slate-700 px-6 py-8 hover:bg-slate-50 dark:hover:bg-slate-800/50 transition-colors">
                            <div class="text-center">
                                <svg class="mx-auto h-12 w-12 text-slate-400" fill="none" viewBox="0 0 24 24"
                                    stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                                        d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
                                </svg>
                                <div
                                    class="mt-4 flex text-sm leading-6 text-slate-600 dark:text-slate-400 justify-center">
                                    <label for="file-upload"
                                        class="relative cursor-pointer rounded-md font-semibold text-blue-600 hover:text-blue-500">
                                        <span>Upload a file</span>
                                        <input id="file-upload" type="file" class="sr-only" accept=".pdf,.zip"
                                            @change="handleFileChange">
                                    </label>
                                    <p class="pl-1">or drag and drop</p>
                                </div>
                                <p v-if="uploadForm.file"
                                    class="mt-2 text-sm font-bold text-green-600 dark:text-green-400 text-center">
                                    Selected: {{ uploadForm.file.name }}
                                </p>
                            </div>
                        </div>

                        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                            <div class="md:col-span-2">
                                <label
                                    class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">Title</label>
                                <input v-model="uploadForm.title" type="text"
                                    :class="[errors.title ? 'border-red-500' : 'border-slate-300 dark:border-slate-700']"
                                    class="w-full rounded-lg bg-white dark:bg-slate-800 text-slate-900 dark:text-white shadow-sm focus:ring-blue-500 sm:text-sm py-2.5 px-3 border"
                                    placeholder="e.g. A New Technique for Data Science">
                                <p v-if="errors.title" class="mt-1 text-xs text-red-500">{{ errors.title }}</p>
                            </div>
                            <div>
                                <label
                                    class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">Type</label>
                                <select v-model="uploadForm.type"
                                    class="w-full rounded-lg border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white shadow-sm focus:ring-blue-500 sm:text-sm py-2.5 px-3 border">
                                    <option value="CONFERENCE_PAPER">Conference Paper</option>
                                    <option value="RESEARCH_POST">Research Post</option>
                                    <option value="THESIS">Thesis</option>
                                    <option value="ARTICLE">Article</option>
                                    <option value="REPORT">Report</option>
                                    <option value="OTHER">Other</option>
                                </select>
                            </div>
                        </div>

                        <div>
                            <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                                Abstract ({{ uploadForm.abstract.length }}/255)
                            </label>
                            <textarea v-model="uploadForm.abstract" rows="4"
                                :class="[errors.abstract ? 'border-red-500' : 'border-slate-300 dark:border-slate-700']"
                                class="w-full rounded-lg bg-white dark:bg-slate-800 text-slate-900 dark:text-white shadow-sm focus:ring-blue-500 sm:text-sm py-2 px-3 border"
                                placeholder="Enter a brief summary..." />
                            <p v-if="errors.abstract" class="mt-1 text-xs text-red-500">{{ errors.abstract }}</p>
                        </div>

                        <div class="flex flex-col md:flex-row gap-8">

                            <div class="w-full">
                                <label
                                    class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">Authors</label>
                                <div class="relative flex items-center">
                                    <input v-model="uploadForm.newAuthorName" type="text"
                                        class="w-full rounded-lg border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white shadow-sm text-sm py-2 pl-3 pr-10 border"
                                        placeholder="Name" @keyup.enter="addAuthor">
                                    <button type="button"
                                        class="absolute right-2 p-1 text-blue-600 hover:text-blue-700 transition-colors"
                                        @click="addAuthor">
                                        <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"
                                            stroke-width="3">
                                            <path stroke-linecap="round" stroke-linejoin="round"
                                                d="M12 4.5v15m7.5-7.5h-15" />
                                        </svg>
                                    </button>
                                </div>
                                <div :class="['flex flex-wrap gap-2', uploadForm.authors.length != 0 ? 'mt-3' : '']">
                                    <span v-for="(author, index) in uploadForm.authors" :key="index"
                                        class="inline-flex items-center gap-1 px-2.5 py-1 rounded-md bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 text-xs font-medium border border-slate-200 dark:border-slate-700">
                                        {{ author }}
                                        <button @click="removeAuthor(index)">
                                            <svg class="w-3 h-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M6 18L18 6M6 6l12 12" />
                                            </svg>
                                        </button>
                                    </span>
                                </div>
                            </div>

                            <div class="flex-1 md:w-auto min-w-[300px]">
                                <label
                                    class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">Tags</label>
                                <div
                                    class="overflow-y-auto border border-slate-200 dark:border-slate-700 rounded-lg p-3 bg-slate-50/50 dark:bg-slate-800/30 flex flex-wrap gap-2 content-start">
                                    <label v-if="availableTags.length == 0"
                                        class="relative rounded-md font-semibold text-blue-600">
                                        <span>No tags</span>
                                    </label>
                                    <button v-for="tag in availableTags" :key="tag.id" type="button" :class="[
                                        uploadForm.selectedTagIds.includes(tag.id)
                                            ? getTagClass(tag.id) + ' border-transparent'
                                            : 'bg-white dark:bg-slate-800 border-slate-200 dark:border-slate-700 text-slate-600 dark:text-slate-400'
                                    ]" class="px-3 py-1.5 rounded-full border text-xs font-bold uppercase tracking-tight transition-all shadow-sm whitespace-nowrap"
                                        @click="() => {
                                            const idx = uploadForm.selectedTagIds.indexOf(tag.id);
                                            if (idx > -1) uploadForm.selectedTagIds.splice(idx, 1);
                                            else uploadForm.selectedTagIds.push(tag.id);
                                        }">
                                        {{ tag.name }}
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div
                        class="bg-slate-50 dark:bg-slate-800/50 px-6 py-4 flex flex-row-reverse gap-3 border-t border-slate-100 dark:border-slate-800">
                        <button :disabled="isSubmitting"
                            class="inline-flex w-full justify-center rounded-lg bg-blue-600 px-6 py-2.5 text-sm font-bold text-white shadow-sm hover:bg-blue-500 disabled:opacity-50 sm:w-auto transition-all"
                            @click="submitPublication">
                            {{ isSubmitting ? 'Uploading...' : 'Submit' }}
                        </button>
                        <button
                            class="inline-flex w-full justify-center rounded-lg bg-white dark:bg-slate-800 px-6 py-2.5 text-sm font-bold text-slate-700 dark:text-slate-300 shadow-sm ring-1 ring-inset ring-slate-300 dark:ring-slate-700 hover:bg-slate-50 sm:w-auto transition-all"
                            @click="closeModal">
                            Cancel
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </Transition>
</template>