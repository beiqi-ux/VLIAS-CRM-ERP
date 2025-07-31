<template>
  <div class="home-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="welcome-card">
          <template #header>
            <div class="welcome-header">
              <h2>欢迎使用 VLIAS CRM系统</h2>
            </div>
          </template>
          <div class="welcome-content">
            <h3>您好，欢迎使用VLIAS CRM系统</h3>
            <p>这里是系统首页</p>
            <el-button type="primary" @click="goToUserManagement">用户管理</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

function goToUserManagement() {
  router.push("/users");
}
</script>

<style scoped>
.home-container {
  padding: 20px;
}
.welcome-card {
  margin-bottom: 20px;
}
.welcome-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.welcome-content {
  padding: 20px 0;
  text-align: center;
}
</style>
