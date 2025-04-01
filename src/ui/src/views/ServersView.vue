<script setup lang="ts">

import Servers from "@/components/Servers.vue";

</script>

<template>

  <div>
    <Suspense>
      <Servers />

      <template #fallback>
        Loading...
      </template>
    </Suspense>
  </div>

</template>

<style scoped>

</style>