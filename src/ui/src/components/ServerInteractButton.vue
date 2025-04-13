<script setup lang="ts">
import {getServerStatus, stopServer, startServer} from "@/views/api.ts";
import {ref} from "vue";

const props = defineProps(["serverName"])

const types = ["Start Server", "Stop Server"];

const serverName = props.serverName

let serverStatus : string = Object((await getServerStatus(serverName).then(data => data.json())))["serverStatus"]

const serverStatusRef = ref(serverStatus === "false" ? types[0] : types[1])

function changeButton() {
  if (serverStatus === "false") {
    startServer(serverName)
    serverStatus = "true"
    serverStatusRef.value = types[1]
  } else if (serverStatus === "true") {
    stopServer(serverName)
    serverStatus = "false"
    serverStatusRef.value = types[0]
  } else {
    // idk explode man
  }

}

</script>

<template>
<button class="button" @click="changeButton">{{serverStatusRef}}</button>
</template>

<style scoped>

button {
  padding: 7px;
  border-style: groove;
  border-color: gray;
  border-radius: 5px;
}

button:hover {
  background: dimgray;
}

</style>