<script setup lang="ts">

import ServerInteractButton from "@/components/ServerInteractButton.vue";
import type Server from "@/types/Server.ts";

const props = defineProps<{
  server: Server
}>();

</script>

<template>
  <div>
      <span>{{ "Server ID: " + server.serverId }}</span>
      <span>{{ "Server name: " + server.serverName }}</span>
      <ServerInteractButton :server-name="server.serverName" />
  </div>
</template>

<style scoped>

</style>