<template>
  <div class="home-container">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
      <div class="banner-content">
        <div class="welcome-text">
          <h1>欢迎使用 VLIAS 企业级全栈管理系统</h1>
          <p class="subtitle">一站式企业数字化解决方案，助力企业高效运营</p>
        </div>
        <div class="banner-stats">
          <div class="stat-card">
            <div class="stat-number">{{ new Date().getFullYear() }}</div>
            <div class="stat-label">服务年份</div>
          </div>
          <div class="stat-card">
            <div class="stat-number">100+</div>
            <div class="stat-label">企业客户</div>
          </div>
          <div class="stat-card">
            <div class="stat-number">99.9%</div>
            <div class="stat-label">系统稳定性</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 核心功能模块 -->
    <div class="modules-section">
      <h2 class="section-title">核心业务模块</h2>
      <el-row :gutter="24">
        <el-col :span="8" :md="8" :sm="12" :xs="24">
          <div class="module-card" @click="goToModule('crm')">
            <div class="module-icon crm-icon">
              <el-icon><UserFilled /></el-icon>
            </div>
            <h3>CRM客户管理</h3>
            <p>客户档案管理、线索跟进、销售机会管理、客户分析报表</p>
            <div class="module-features">
              <span class="feature-tag">客户档案</span>
              <span class="feature-tag">线索管理</span>
              <span class="feature-tag">销售统计</span>
            </div>
          </div>
        </el-col>
        <el-col :span="8" :md="8" :sm="12" :xs="24">
          <div class="module-card" @click="goToModule('inventory')">
                         <div class="module-icon inventory-icon">
               <el-icon><Goods /></el-icon>
             </div>
            <h3>进销存管理</h3>
            <p>商品管理、库存控制、采购销售、门店管理、实时库存预警</p>
            <div class="module-features">
              <span class="feature-tag">商品管理</span>
              <span class="feature-tag">库存控制</span>
              <span class="feature-tag">门店销售</span>
            </div>
          </div>
        </el-col>
        <el-col :span="8" :md="8" :sm="12" :xs="24">
          <div class="module-card" @click="goToModule('finance')">
            <div class="module-icon finance-icon">
              <el-icon><Money /></el-icon>
            </div>
            <h3>财务管理</h3>
            <p>账单管理、应收应付、发票管理、财务报表、资金流水统计</p>
            <div class="module-features">
              <span class="feature-tag">账单管理</span>
              <span class="feature-tag">财务报表</span>
              <span class="feature-tag">资金统计</span>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 快速操作 -->
    <div class="quick-actions-section">
      <h2 class="section-title">快速操作</h2>
      <el-row :gutter="16">
        <el-col :span="6" :md="6" :sm="12" :xs="24">
          <div class="quick-action-card" @click="goToPage('/users')">
            <el-icon class="action-icon"><User /></el-icon>
            <span>用户管理</span>
          </div>
        </el-col>
        <el-col :span="6" :md="6" :sm="12" :xs="24">
          <div class="quick-action-card" @click="goToPage('/organizations')">
            <el-icon class="action-icon"><OfficeBuilding /></el-icon>
            <span>组织管理</span>
          </div>
        </el-col>
        <el-col :span="6" :md="6" :sm="12" :xs="24">
          <div class="quick-action-card" @click="goToPage('/roles')">
            <el-icon class="action-icon"><Lock /></el-icon>
            <span>角色权限</span>
          </div>
        </el-col>
        <el-col :span="6" :md="6" :sm="12" :xs="24">
          <div class="quick-action-card" @click="goToPage('/dicts')">
            <el-icon class="action-icon"><Setting /></el-icon>
            <span>系统配置</span>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 系统优势 -->
    <div class="advantages-section">
      <h2 class="section-title">系统优势</h2>
      <el-row :gutter="24">
        <el-col :span="6" :md="6" :sm="12" :xs="24">
          <div class="advantage-card">
                         <div class="advantage-icon">
               <el-icon><Timer /></el-icon>
             </div>
            <h4>高效便捷</h4>
            <p>全业务流程数字化，提升工作效率</p>
          </div>
        </el-col>
        <el-col :span="6" :md="6" :sm="12" :xs="24">
          <div class="advantage-card">
            <div class="advantage-icon">
              <el-icon><DataAnalysis /></el-icon>
            </div>
                        <h4>数据驱动</h4>
            <p>智能分析报表，数据驱动决策支持</p>
          </div>
        </el-col>
        <el-col :span="6" :md="6" :sm="12" :xs="24">
          <div class="advantage-card">
                         <div class="advantage-icon">
               <el-icon><Connection /></el-icon>
             </div>
            <h4>一体化协作</h4>
            <p>多部门协同办公，信息实时共享</p>
          </div>
        </el-col>
        <el-col :span="6" :md="6" :sm="12" :xs="24">
          <div class="advantage-card">
                         <div class="advantage-icon">
               <el-icon><Check /></el-icon>
             </div>
            <h4>安全可靠</h4>
            <p>企业级安全保障，数据加密存储</p>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { 
  UserFilled, 
  Goods, 
  Money, 
  User, 
  OfficeBuilding, 
  Lock, 
  Setting,
  Timer,
  DataAnalysis,
  Connection,
  Check
} from '@element-plus/icons-vue';

const router = useRouter();

function goToPage(path) {
  router.push(path);
}

function goToModule(module) {
  // 根据模块类型跳转到对应页面
  switch(module) {
    case 'crm':
      // TODO: 跳转到CRM模块（暂时跳转到用户管理）
      router.push('/users');
      break;
    case 'inventory':
      // TODO: 跳转到进销存模块
      console.log('进销存模块开发中...');
      break;
    case 'finance':
      // TODO: 跳转到财务模块
      console.log('财务模块开发中...');
      break;
  }
}
</script>

<style scoped>
.home-container {
  padding: 0;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: calc(100vh - 60px);
}

/* 欢迎横幅 */
.welcome-banner {
  background: linear-gradient(135deg, #002FA7 0%, #0066FF 100%);
  color: white;
  padding: 60px 40px;
  margin-bottom: 40px;
}

.banner-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-text h1 {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 16px;
  background: linear-gradient(45deg, #ffffff, #e3f2fd);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.subtitle {
  font-size: 1.2rem;
  opacity: 0.9;
  margin: 0;
}

.banner-stats {
  display: flex;
  gap: 40px;
}

.stat-card {
  text-align: center;
}

.stat-number {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 0.9rem;
  opacity: 0.8;
}

/* 功能模块区域 */
.modules-section, .quick-actions-section, .advantages-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 40px 60px;
}

.section-title {
  font-size: 1.8rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 32px;
  text-align: center;
  position: relative;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background: linear-gradient(45deg, #002FA7, #0066FF);
  border-radius: 2px;
}

/* 模块卡片 */
.module-card {
  background: white;
  border-radius: 16px;
  padding: 32px 24px;
  text-align: center;
  box-shadow: 0 8px 32px rgba(0, 47, 167, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
  height: 280px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.module-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 48px rgba(0, 47, 167, 0.2);
}

.module-icon {
  width: 80px;
  height: 80px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  font-size: 36px;
  color: white;
}

.crm-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.inventory-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.finance-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.module-card h3 {
  font-size: 1.3rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 12px;
}

.module-card p {
  color: #7f8c8d;
  line-height: 1.6;
  margin-bottom: 20px;
  flex: 1;
}

.module-features {
  display: flex;
  justify-content: center;
  gap: 8px;
  flex-wrap: wrap;
}

.feature-tag {
  background: #f8f9fa;
  color: #495057;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 0.8rem;
  border: 1px solid #e9ecef;
}

/* 快速操作 */
.quick-action-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  text-align: center;
  box-shadow: 0 4px 16px rgba(0, 47, 167, 0.08);
  transition: all 0.3s ease;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.quick-action-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 47, 167, 0.15);
}

.action-icon {
  font-size: 32px;
  color: #002FA7;
}

.quick-action-card span {
  font-weight: 500;
  color: #2c3e50;
}

/* 系统优势 */
.advantage-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  text-align: center;
  box-shadow: 0 4px 16px rgba(0, 47, 167, 0.06);
  transition: all 0.3s ease;
  min-height: 160px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.advantage-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 47, 167, 0.12);
}

.advantage-icon {
  width: 60px;
  height: 60px;
  border-radius: 16px;
  background: linear-gradient(135deg, #002FA7 0%, #0066FF 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  font-size: 24px;
  color: white;
}

.advantage-card h4 {
  font-size: 1.1rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 8px;
}

.advantage-card p {
  color: #7f8c8d;
  line-height: 1.5;
  margin: 0;
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .welcome-banner {
    padding: 40px 20px;
  }
  
  .banner-content {
    flex-direction: column;
    gap: 32px;
  }
  
  .welcome-text h1 {
    font-size: 2rem;
  }
  
  .banner-stats {
    gap: 20px;
  }
  
  .modules-section, .quick-actions-section, .advantages-section {
    padding: 0 20px 40px;
  }
  
  .section-title {
    font-size: 1.5rem;
  }
}
</style>
