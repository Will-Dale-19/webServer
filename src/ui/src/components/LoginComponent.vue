<template>
  <div class="post">

    <div>
      <h1> Please log in here </h1>
        <h3>Username</h3>
        <input type="text" required v-model="usernameRef">
        <h3>Password</h3>
        <input type="password" required v-model="passwordRef">
        <button type="submit" @click="fetchData">Login</button>
    </div>

    <div v-if="loading" class="loading">Loading...</div>

    <div v-if="error" class="error">{{ error }}</div>

    <div v-if="post" class="content">
      <h2>Successfully logged in.</h2>
    </div>
  </div>
</template>

<script setup lang="ts">

import { ref } from 'vue';
import {useUserStore} from "@/stores/user.js";
import {login} from "@/views/api.ts";

const usernameRef = ref('');
const passwordRef = ref('');

const loading = ref(false);
const post = ref(null);
const error = ref(null);

// watch the params of the route to fetch the data again
//watch(() => usernameRef, fetchData, { immediate: true });

async function fetchData() {
  let username : string = usernameRef.value
  let password : string = passwordRef.value

    error.value = post.value = null;
    loading.value = true;

    try {
      post.value = await login({username, password})

      const store = useUserStore();

      if (post.value != null) {
        const tokenObj: Object = post.value;

        const tokenStr: string = Object(tokenObj)["token"]

        store.$patch((state) => {
          state.username = username;
          state.token = tokenStr;
          sessionStorage.setItem('token', JSON.stringify(state));
        })
        console.log("logged in new user", username)
      }

    } catch (err: any) {
      error.value = err.toString()
    } finally {
      loading.value = false
    }

}

</script>