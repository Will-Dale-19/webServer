<script setup lang="ts">
import {useUserStore} from "@/stores/user.ts";
import {ref, watch} from "vue";

const store = useUserStore();

let userRef = ref(store.username)

watch(() => store.username, () => {
  console.log("new user", store.username)
  userRef.value = store.username
})

</script>

<template>
  <div v-if="userRef">
    <h3>Logged in: {{userRef}}</h3>
  </div>
</template>

<style scoped>

</style>