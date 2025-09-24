<script setup lang="ts">
import {ref, onMounted} from "vue";
import {getServers} from "@/views/api.ts";
import ServerInteractButton from "@/components/ServerInteractButton.vue";
import {useUserStore} from "@/stores/user.ts";
import type Server from "@/types/Server.ts"

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
</script>

<template>
  <div v-if="hasActiveUser()">
    <ol>
      <li v-for="server in servers" :key="server.serverId">
        <span>{{ "Server ID: " + server.serverId }}</span>
        <span>{{ "Server name: " + server.serverName }}</span>
        <ServerInteractButton :server-name="server.serverName" />
      </li>
    </ol>
  </div>
  <div v-else>
    <p>Log in or create an account to view servers.</p>
  </div>
</template>