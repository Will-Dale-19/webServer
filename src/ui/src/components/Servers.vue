<script setup lang="ts">

import {getServers} from "@/views/api.ts";
import ServerInteractButton from "@/components/ServerInteractButton.vue";

const servers = await getServers().then(data => data.json())

console.log(servers)

</script>

<template>
  <div>
    <li v-for="server in servers">
      <span>{{ "Server ID: " + server.serverId }}</span>
      <span>{{ "Server name: " + server.serverName }}</span>
      <ServerInteractButton :server-name="server.serverName"/>
    </li>
  </div>
</template>

<style scoped>

</style>