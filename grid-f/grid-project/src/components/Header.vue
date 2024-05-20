<template>
  <header class="header">
    <div class="logo">
            <img src="@/assets/logo.png" @click="main()" class="logoimage">
    </div>
    <div>

    </div>
    <input class="search" type="text" placeholder="Search" />
    <div class="icons">
      <button class="icon-button">
        <img src="@/assets/icon1.png" alt="Button 1" class="icon-image" />
      </button>
      <button class="icon-button">
        <img src="@/assets/icon2.png" alt="Button 2" class="icon-image" />
      </button>
      <img class="profile" src="@/assets/profile.png" alt="Profile Picture" />
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

.profile {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
}

.icon-button {
  background: none;
  border: none;
  margin: 0 5px;
  padding: 5px;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.icon-image {
  width: 24px;
  height: 24px;
}
</style>
