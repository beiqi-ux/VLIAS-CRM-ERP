# 通用性别字典使用指南

## 概述

系统提供了一个通用的性别字典，适用于所有需要性别信息的场景，包括：
- 系统用户
- 客户信息
- 联系人
- 员工档案
- 其他任何需要性别字段的业务场景

## 字典配置

### 字典基本信息
- **字典编码**: `gender`
- **字典名称**: 性别
- **描述**: 通用性别字典，适用于用户、客户、联系人等所有需要性别信息的场景

### 字典值定义
| 值 | 文本 | 描述 | 排序 |
|----|------|------|------|
| 0 | 未知 | 性别未知或不愿透露 | 1 |
| 1 | 男 | 男性 | 2 |
| 2 | 女 | 女性 | 3 |

## 前端使用方式

### 1. 在表单中使用

```vue
<template>
  <!-- 下拉选择 -->
  <el-form-item label="性别" prop="gender">
    <DictSelect 
      v-model="form.gender" 
      dict-code="gender" 
      placeholder="请选择性别"
      value-type="number"
    />
  </el-form-item>

  <!-- 单选框 -->
  <el-form-item label="性别" prop="gender">
    <DictRadio 
      v-model="form.gender" 
      dict-code="gender" 
      value-type="number"
    />
  </el-form-item>
</template>
```

### 2. 在列表中显示

```vue
<template>
  <el-table-column prop="gender" label="性别" width="80">
    <template #default="{ row }">
      <!-- 使用工具函数 -->
      <el-tag :color="getGenderColor(row.gender)">
        {{ getGenderTextSync(row.gender) }}
      </el-tag>
      
      <!-- 或者带图标显示 -->
      <span>
        <el-icon :name="getGenderIcon(row.gender)" />
        {{ getGenderTextSync(row.gender) }}
      </span>
    </template>
  </el-table-column>
</template>

<script setup>
import { getGenderTextSync, getGenderColor, getGenderIcon } from '@/utils/genderUtils'
</script>
```

### 3. 使用工具函数

```javascript
import { 
  getGenderOptions,
  getGenderText,
  getGenderTextSync,
  GENDER_VALUES,
  GENDER_LABELS,
  isValidGender
} from '@/utils/genderUtils'

// 获取性别选项列表
const genderOptions = await getGenderOptions()

// 根据值获取文本
const genderText = await getGenderText('1') // 返回 "男"

// 同步获取文本（需预加载字典）
const genderTextSync = getGenderTextSync('2') // 返回 "女"

// 使用枚举值
if (user.gender === GENDER_VALUES.MALE) {
  console.log('这是男性用户')
}

// 验证性别值
if (isValidGender(inputValue)) {
  // 有效的性别值
}
```

## 后端使用方式

### 1. 实体类定义

```java
@Entity
public class User {
    /**
     * 性别 0-未知 1-男 2-女
     * 使用通用性别字典 gender
     */
    @Column(name = "gender")
    private Integer gender;
    
    // 其他字段...
}

@Entity 
public class Customer {
    /**
     * 性别 0-未知 1-男 2-女
     * 使用通用性别字典 gender
     */
    @Column(name = "gender")
    private Integer gender;
    
    // 其他字段...
}
```

### 2. 服务层使用

```java
@Service
public class UserService {
    
    @Autowired
    private SysDictItemService dictItemService;
    
    /**
     * 获取性别显示文本
     */
    public String getGenderText(Integer gender) {
        return dictItemService.findByDictCodeAndItemValue("gender", String.valueOf(gender))
                .map(SysDictItem::getItemText)
                .orElse("未知");
    }
    
    /**
     * 验证性别值是否有效
     */
    public boolean isValidGender(Integer gender) {
        return dictItemService.existsByDictCodeAndItemValue("gender", String.valueOf(gender));
    }
}
```

## 应用场景示例

### 1. 用户管理
```vue
<!-- 用户信息表单 -->
<el-form-item label="性别" prop="gender">
  <DictSelect v-model="userForm.gender" dict-code="gender" />
</el-form-item>
```

### 2. 客户管理
```vue
<!-- 客户信息表单 -->
<el-form-item label="性别" prop="gender">
  <DictRadio v-model="customerForm.gender" dict-code="gender" />
</el-form-item>
```

### 3. 联系人管理
```vue
<!-- 联系人信息表单 -->
<el-form-item label="性别" prop="gender">
  <DictSelect v-model="contactForm.gender" dict-code="gender" />
</el-form-item>
```

### 4. 员工档案
```vue
<!-- 员工档案表单 -->
<el-form-item label="性别" prop="gender">
  <DictRadio v-model="employeeForm.gender" dict-code="gender" />
</el-form-item>
```

## 扩展说明

### 1. 国际化支持
如果需要支持多语言，可以在 `sys_translation` 表中添加对应的翻译：

```sql
INSERT INTO sys_translation (language_code, translation_key, translation_value) VALUES
('en', 'gender.unknown', 'Unknown'),
('en', 'gender.male', 'Male'),
('en', 'gender.female', 'Female');
```

### 2. 自定义样式
可以根据性别值设置不同的显示样式：

```css
.gender-unknown { color: #909399; }
.gender-male { color: #409EFF; }
.gender-female { color: #E6A23C; }
```

### 3. 数据统计
在报表中可以很方便地进行性别统计：

```sql
SELECT 
  sdi.item_text as gender_text,
  COUNT(*) as count
FROM user u
LEFT JOIN sys_dict_item sdi ON sdi.item_value = u.gender 
LEFT JOIN sys_dict sd ON sd.id = sdi.dict_id AND sd.dict_code = 'gender'
GROUP BY u.gender, sdi.item_text;
```

## 最佳实践

1. **统一使用**: 所有涉及性别的字段都使用同一个字典，保持数据一致性
2. **值类型**: 建议使用数字类型存储性别值，提高查询性能
3. **默认值**: 新记录的性别字段可以默认设置为 0（未知）
4. **验证规则**: 在前后端都要验证性别值的有效性
5. **显示优化**: 在列表中使用颜色或图标区分不同性别，提升用户体验

通过这个通用的性别字典，可以确保整个系统中性别相关的数据和显示保持统一和一致。 