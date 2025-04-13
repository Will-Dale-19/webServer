<script setup lang="ts">

import {getServers} from "@/views/api.ts";
import ServerInteractButton from "@/components/ServerInteractButton.vue";

const servers = await getServers().then(data => data.json())

const serverRef = (servers === null)

</script>

<template>
  <div>
    <ol>
      <li v-for="server in servers">
        <span>{{ "Server ID: " + server.serverId }}</span>
        <span>{{ "Server name: " + server.serverName }}</span>
        <ServerInteractButton :server-name="server.serverName"/>
      </li>
    </ol>
  </div>
</template>

<style scoped>

li {
  margin: 10px;
}

span {
  padding: 4px;
  border-style: groove;
  border-color: gray;
  border-radius: 5px;
}

</style>