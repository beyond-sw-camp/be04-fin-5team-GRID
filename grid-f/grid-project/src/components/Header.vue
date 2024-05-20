<template>
  <header class="header">
    <div class="logo">
            <img src="@/assets/logo.png" @click="main()" class="logoimage">
    </div>
    <div>

    </div>
    <input class="search" type="text" placeholder="Search" />
    <div class="icons">
      <button class="icon-button">Button 1</button>
      <button class="icon-button">Button 2</button>
      <img class="profile" src="@/assets/logo.png" alt="Profile Picture" />
    </div>
  </header>
</template>

<script setup>
// 필요한 스크립트가 있으면 여기에 작성
</script>

<style scoped>
.header {
  background: #002366;
  color: white;
  padding: 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
}

.logo img {
  height: 40px;
  margin-right: 10px;
}

.search {
  padding: 5px;
  border-radius: 10px;
  border: none;
  width: 400px;
}

.icons {
  display: flex;
  align-items: center;
}

.icons i {
  font-size: 18px;
  margin: 0 10px;
  cursor: pointer;
  
}

.profile {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
}
</style>
