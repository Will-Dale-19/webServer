<script setup lang="ts">
import {ref, onMounted} from "vue";
import {getServers} from "@/views/api.ts";
import {useUserStore} from "@/stores/user.ts";
import type Server from "@/types/Server.ts"
import ServerView from "@/views/ServerView.vue";
import router from "@/router";

const store = useUserStore();
const servers = ref<Server[]>([]);

function hasActiveUser(): boolean {
  return store.username !== "";
}

onMounted(async () => {
  if (hasActiveUser()) {
    const response = await getServers();
    servers.value = await response.json();
  }
});

function goToServer(server: Server) {
  router.push({
    name: 'ServerDetail',
    params: { id: server.serverId },
  });
}


</script>

<template>
  <div v-if="hasActiveUser()">
    <ol>
      <li v-for="server in servers" :key="server.serverId">/
        <button @click="goToServer(server)">View Full Page</button>
      </li>
    </ol>
  </div>
  <div v-else>
    <p>Log in or create an account to view servers.</p>
  </div>
</template>