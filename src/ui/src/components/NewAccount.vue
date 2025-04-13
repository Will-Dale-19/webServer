<script setup lang="ts">


import {ref} from "vue";
import {useUserStore} from "@/stores/user.ts";
import {createNewAccount, login} from "@/views/api.ts";

const usernameRef = ref('');
const passwordRef = ref('');

const loading = ref(false);
const post = ref(null);
const error = ref(null);

async function createNewAccountClick() {
  let username : string = usernameRef.value
  let password : string = passwordRef.value

  error.value = post.value = null;
  loading.value = true;

  try {
    post.value = await createNewAccount({username, password})

    const store = useUserStore();

    if (post.value != null) {
      const tokenObj: Object = post.value;

      const tokenStr: string = Object(tokenObj)["token"]

      store.$patch((state) => {
        state.username = username;
        state.token = tokenStr;
        sessionStorage.setItem('token', JSON.stringify(state));
      })
    }

  } catch (err: any) {
    error.value = err.toString()
  } finally {
    loading.value = false
  }
}

</script>

<template>
  <div>

    <div v-if="!post">
      <h3>new account here</h3>
      <h3>Username</h3>
      <input type="text" required v-model="usernameRef">
      <h3>Password</h3>
      <input type="password" required v-model="passwordRef">
      <button type="submit" @click="createNewAccountClick">Create new account</button>
    </div>

    <div v-if="post" class="content">
      <h2>Successfully created a new account.</h2>
    </div>

    <div v-if="loading" class="loading">Loading...</div>

    <div v-if="error" class="error">{{ error }}</div>
  </div>
</template>

<style scoped>

</style>