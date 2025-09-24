<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import HelloWorld from './components/HelloWorld.vue'
import CurrentUserDisplay from "@/components/CurrentUserDisplay.vue";
import {useUserStore} from "@/stores/user.ts";


const savedUserStr = sessionStorage.getItem('token');

const store = useUserStore();

if (savedUserStr != null) {
  const savedUser : Object = JSON.parse(savedUserStr)

  store.$patch({
    username: Object(savedUser)["username"],
    token: Object(savedUser)["token"]
  })
}

</script>

<template>
  <header>
    <div class="wrapper">
      <HelloWorld />

      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/about">About</RouterLink>
        <RouterLink to="/servers">View your servers</RouterLink>
      </nav>
    </div>

    <CurrentUserDisplay />
  </header>

  <RouterView />
</template>

<style scoped>
header {
  line-height: 1.5;
  font-size: 24px;
  max-height: 200vh;
  width: 100%;
}

nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }


}
</style>
