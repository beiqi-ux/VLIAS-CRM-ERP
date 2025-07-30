# 全面的VLIAS CRM系统开发规范

## 目录

1. [架构规范](#1-架构规范)
2. [项目结构规范](#2-项目结构规范)
3. [命名规范](#3-命名规范)
4. [代码风格与注释规范](#4-代码风格与注释规范)
5. [数据库设计规范](#5-数据库设计规范)
6. [API设计规范](#6-api设计规范)
7. [安全规范](#7-安全规范)
8. [测试规范](#8-测试规范)
9. [性能优化规范](#9-性能优化规范)
10. [部署与运维规范](#10-部署与运维规范)
11. [版本控制规范](#11-版本控制规范)
12. [多租户设计规范](#12-多租户设计规范)
13. [国际化与本地化规范](#13-国际化与本地化规范)
14. [前端开发规范](#14-前端开发规范)
15. [可观测性规范](#15-可观测性规范)
16. [业务领域模型设计](#16-业务领域模型设计)
17. [权限控制规范](#17-权限控制规范)
18. [多端开发规范](#18-多端开发规范)
19. [第三方集成规范](#19-第三方集成规范)
20. [数据迁移与备份规范](#20-数据迁移与备份规范)

## 1. 架构规范

### 1.1 总体架构

VLIAS CRM系统采用分层架构设计，包括：
- **表示层(UI/Controller)** - 处理用户请求，返回响应
- **业务层(Service)** - 实现业务逻辑
- **数据访问层(Repository)** - 负责数据持久化
- **领域模型层(Entity)** - 定义业务实体和关系

### 1.2 技术栈选择

- **后端**：Spring Boot 3.1.x, Spring Security, Spring Data JPA, MyBatis-Plus
- **数据库**：MySQL 8.0，Redis，MongoDB（用于非结构化数据）
- **消息队列**：RabbitMQ
- **缓存**：Redis + Caffeine多级缓存
- **搜索引擎**：Elasticsearch（可选）
- **文件存储**：腾讯云COS
- **前端**：Vue3、uni-app（多端适配）

### 1.3 分层架构详细说明

#### 1.3.1 表示层(Controller)

- 负责处理HTTP请求和返回响应
- 进行输入验证
- 调用服务层完成业务逻辑
- 不包含业务逻辑
- 处理异常并返回统一的响应格式

#### 1.3.2 业务层(Service)

- 实现具体业务逻辑
- 事务管理
- 数据校验和业务规则验证
- 调用数据访问层完成数据持久化
- 处理业务异常

#### 1.3.3 数据访问层(Repository)

- 实现数据的CRUD操作
- 不包含业务逻辑
- 处理ORM映射
- 优化数据库查询性能

#### 1.3.4 领域模型层(Entity)

- 定义业务实体和关系
- 实现实体的业务行为
- 保持对象的完整性和一致性

### 1.4 微服务架构（如需）

如果系统规模扩大，可考虑采用微服务架构：

- **服务拆分**：按业务域拆分为独立服务
- **服务通信**：使用REST API或消息队列
- **服务发现**：Eureka或Nacos
- **API网关**：Spring Cloud Gateway
- **配置中心**：Spring Cloud Config或Nacos
- **断路器**：Resilience4j
- **分布式事务**：Seata

### 1.5 设计原则

- **单一职责原则(SRP)**：一个类只负责一项职责
- **开闭原则(OCP)**：对扩展开放，对修改关闭
- **里氏替换原则(LSP)**：子类必须能够替换其基类
- **接口隔离原则(ISP)**：使用多个专门的接口，而不是单一的总接口
- **依赖倒置原则(DIP)**：高层模块不应该依赖低层模块
- **领域驱动设计(DDD)**：复杂业务逻辑使用DDD方法
- **CQRS**：适当场景下分离读写操作 

## 2. 项目结构规范

### 2.1 领域驱动的包结构

采用按领域模块划分的包结构，提高内聚性和可维护性：

```
com.example.vliascrm
├── common/               # 通用组件
│   ├── config/           # 通用配置
│   ├── exception/        # 异常定义
│   ├── util/             # 工具类
│   └── constant/         # 常量定义
├── config/               # 全局配置
├── module/               # 按领域划分的模块
│   ├── system/           # 系统管理模块
│   ├── customer/         # 客户管理模块
│   ├── product/          # 商品管理模块
│   ├── inventory/        # 库存管理模块
│   ├── order/            # 订单管理模块
│   └── purchase/         # 采购管理模块
└── Application.java      # 应用启动类
```

### 2.2 模块内部结构

每个业务模块内部再按技术层次划分：

```
module/customer/
├── controller/           # 控制器
├── service/              # 服务
│   ├── impl/             # 服务实现
│   └── dto/              # 数据传输对象
├── repository/           # 数据访问
├── entity/               # 实体
├── mapper/               # MyBatis映射
├── vo/                   # 视图对象
├── query/                # 查询参数对象
├── enums/                # 枚举定义
└── utils/                # 模块专用工具
```

### 2.3 资源文件组织

```
resources/
├── application.yml                 # 主配置文件
├── application-dev.yml             # 开发环境配置
├── application-test.yml            # 测试环境配置
├── application-prod.yml            # 生产环境配置
├── static/                         # 静态资源
├── templates/                      # 模板文件
├── mapper/                         # MyBatis映射XML
├── db/migration/                   # Flyway数据库脚本
│   ├── V1.0.0__init_schema.sql     # 初始化表结构
│   └── V1.0.1__init_data.sql       # 初始化数据
└── i18n/                           # 国际化资源
    ├── messages.properties
    ├── messages_zh_CN.properties
    └── messages_en.properties
```

## 3. 命名规范

### 3.1 包命名规范

- 全部小写
- 使用单数形式
- 包名要见名知意
- 避免使用复数
- 按照功能或模块划分

例如：
```
com.example.vliascrm.module.customer.service
com.example.vliascrm.common.exception
```

### 3.2 类命名规范

| 类型 | 命名规则 | 示例 |
|------|----------|------|
| 实体类 | 名词，单数，PascalCase | `Customer`, `Product` |
| DTO类 | 名词+DTO，PascalCase | `CustomerDTO`, `OrderDetailDTO` |
| VO类 | 名词+VO，PascalCase | `CustomerVO`, `ProductDetailVO` |
| 查询参数 | 名词+Query，PascalCase | `CustomerQuery`, `OrderQuery` |
| 控制器 | 名词+Controller，PascalCase | `CustomerController`, `ProductController` |
| 接口 | 形容词/动词+名词，PascalCase | `CustomerService`, `UserRepository` |
| 实现类 | 接口名+Impl，PascalCase | `CustomerServiceImpl`, `UserRepositoryImpl` |
| 工具类 | 名词+Util/Utils，PascalCase | `DateUtils`, `StringUtil` |
| 枚举类 | 名词+Enum，PascalCase | `StatusEnum`, `GenderEnum` |
| 常量类 | 名词+Constant(s)，PascalCase | `SystemConstants`, `ApiConstant` |
| 异常类 | 异常描述+Exception，PascalCase | `BusinessException`, `ResourceNotFoundException` |
| 配置类 | 名词+Config，PascalCase | `RedisConfig`, `WebSecurityConfig` |

### 3.3 方法命名规范

#### 3.3.1 控制器方法

| 操作类型 | 命名前缀 | 示例 |
|---------|---------|------|
| 查询列表 | list/query/page | `listCustomers()`, `pageOrders()` |
| 查询单个 | get/find/query | `getCustomerById()`, `findOrder()` |
| 创建 | create/add/save | `createCustomer()`, `addOrder()` |
| 更新 | update/modify | `updateCustomer()`, `modifyOrderStatus()` |
| 删除 | delete/remove | `deleteCustomer()`, `removeOrder()` |
| 批量操作 | batchXxx | `batchCreateCustomers()`, `batchDeleteOrders()` |
| 导出 | export | `exportCustomers()` |
| 导入 | import | `importCustomers()` |

#### 3.3.2 服务层方法

| 操作类型 | 命名前缀 | 示例 |
|---------|---------|------|
| 查询列表 | list/query/page | `listCustomers()`, `pageOrdersByCondition()` |
| 查询单个 | get/find/query | `getCustomerById()`, `findOrderByNo()` |
| 创建 | create/add/save | `createCustomer()`, `saveOrder()` |
| 更新 | update/modify | `updateCustomer()`, `modifyOrderStatus()` |
| 删除 | delete/remove | `deleteCustomerById()`, `removeOrder()` |
| 批量操作 | batchXxx | `batchSaveCustomers()`, `batchDeleteOrders()` |
| 检查/校验 | check/validate | `checkDuplicate()`, `validateOrderStatus()` |
| 处理/执行 | process/execute | `processOrder()`, `executePayment()` |
| 转换 | convert/transform | `convertToDTO()`, `transformToEntity()` |

#### 3.3.3 数据访问层方法

| 操作类型 | 命名前缀 | 示例 |
|---------|---------|------|
| 根据ID查询 | findById | `findById()` |
| 查询单个 | findBy... | `findByUsername()`, `findByOrderNo()` |
| 查询列表 | findAll/findBy... | `findAll()`, `findByStatus()` |
| 查询是否存在 | existsBy... | `existsByUsername()`, `existsByEmail()` |
| 统计 | countBy... | `countByStatus()`, `countByCreateTimeBetween()` |
| 删除 | deleteBy... | `deleteById()`, `deleteByUsername()` |
| 自定义查询 | 动词+介词+名词 | `findByStatusAndCreateTimeBetween()` |

### 3.4 变量命名规范

#### 3.4.1 成员变量

- 使用lowerCamelCase
- 避免使用单个字符名称
- 布尔类型变量使用is/has/can等前缀
- 集合类变量使用复数形式
- 避免缩写或特殊字符

```java
private String username;
private boolean isActive;
private List<Order> orders;
private Map<String, Object> attributes;
```

#### 3.4.2 常量

- 使用UPPER_SNAKE_CASE
- 尽量使用有意义的名称
- 避免使用模糊的缩写

```java
public static final String DEFAULT_PAGE_SIZE = "20";
public static final long TOKEN_EXPIRATION_TIME = 3600000L;
public static final String API_PREFIX = "/api/v1";
```

#### 3.4.3 方法参数与局部变量

- 使用lowerCamelCase
- 避免使用单个字符名称
- 临时变量可以使用简短名称
- 避免拼写错误或歧义名称

```java
public Customer updateCustomer(Long customerId, CustomerDTO customerDTO) {
    Customer existingCustomer = repository.findById(customerId)
        .orElseThrow(() -> new ResourceNotFoundException("客户不存在"));
    // ...
}
```

### 3.5 数据库命名规范

#### 3.5.1 表命名

- 使用小写字母和下划线
- 采用前缀表示模块或功能域
- 使用单数形式
- 避免数据库关键字

示例：
```
sys_user          # 系统用户表
crm_customer      # CRM客户表
prod_category     # 商品分类表
ord_order         # 订单表
ord_order_item    # 订单明细表
inv_warehouse     # 仓库表
```

#### 3.5.2 字段命名

- 使用小写字母和下划线
- 字段名见名知意
- 主键命名为`id`
- 外键命名为`表名_id`
- 创建时间命名为`create_time`
- 更新时间命名为`update_time`
- 创建人命名为`create_by`
- 更新人命名为`update_by`
- 删除标记命名为`is_deleted`
- 状态字段命名为`status`
- 排序字段命名为`sort`
- 布尔类型字段使用`is_xxx`形式

示例：
```sql
CREATE TABLE crm_customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '客户名称',
    contact_name VARCHAR(50) COMMENT '联系人',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '电子邮箱',
    customer_type_id BIGINT COMMENT '客户类型ID',
    is_vip BOOLEAN DEFAULT FALSE COMMENT '是否VIP',
    status TINYINT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    is_deleted BOOLEAN DEFAULT FALSE COMMENT '是否删除'
);
```

### 3.6 前端命名规范

#### 3.6.1 文件与文件夹命名

- 组件文件：PascalCase，如`CustomerForm.vue`
- 页面文件：PascalCase，如`CustomerList.vue`
- 工具/API文件：camelCase，如`httpRequest.js`
- 样式文件：kebab-case，如`common-style.scss`
- 路由文件：camelCase，如`customerRoutes.js`
- 文件夹：kebab-case，如`customer-management`

#### 3.6.2 组件命名

- 使用PascalCase，如`CustomerList`
- 基础组件使用`Base`前缀，如`BaseButton`
- 单例组件使用`The`前缀，如`TheHeader`
- 紧密耦合的组件使用父组件名作为前缀，如`CustomerListItem`

#### 3.6.3 CSS命名

采用BEM(Block, Element, Modifier)命名法：
```css
/* Block */
.customer-card { }

/* Element */
.customer-card__title { }

/* Modifier */
.customer-card--featured { }
``` 

## 4. 代码风格与注释规范

### 4.1 Java代码风格

#### 4.1.1 基本原则

- 使用4个空格缩进，不使用Tab
- 行宽不超过120个字符
- 文件末尾留一个空行
- 使用UTF-8编码
- 避免超过500行的大文件
- 避免超过50行的长方法
- 避免过深的嵌套结构(超过3层)
- 避免一行包含多个语句
- 避免魔法数字和字符串，使用常量或枚举
- 编译器警告应当处理，不应被忽略

#### 4.1.2 空格与换行

- 二元运算符两侧加空格：`a + b`，而不是`a+b`
- 括号内侧不加空格：`if (condition)`，而不是`if ( condition )`
- 逗号、冒号、分号后加空格
- 大括号使用K&R风格：开始大括号放在行尾，结束大括号独占一行
- 单行的if语句也需要使用大括号
- 多参数方法调用时，参数过长应换行
- 长条件判断，使用合理换行提高可读性
- 链式调用应每个方法一行

```java
// Good
if (condition) {
    doSomething();
}

// Bad
if (condition) doSomething();

// Good
someObject.methodA()
         .methodB()
         .methodC();
```

#### 4.1.3 类成员顺序

1. 静态变量(类变量)
2. 实例变量
3. 构造方法
4. 静态方法
5. 实例方法
6. 内部类/接口

```java
public class Customer {
    // 1. 静态变量
    private static final Logger log = LoggerFactory.getLogger(Customer.class);
    
    // 2. 实例变量
    private Long id;
    private String name;
    
    // 3. 构造方法
    public Customer() {}
    public Customer(String name) { this.name = name; }
    
    // 4. 静态方法
    public static Customer createDefault() { ... }
    
    // 5. 实例方法
    public String getName() { ... }
    
    // 6. 内部类
    class Address { ... }
}
```

#### 4.1.4 注解使用

- 类注解单独一行
- 方法注解单独一行
- 字段注解可以多个同行
- 参数注解与参数同行

```java
@Entity
@Table(name = "crm_customer")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name") @NotBlank
    private String name;
    
    @Transactional
    @Override
    public Customer save(Customer customer) {
        // ...
    }
}
```

### 4.2 注释规范

#### 4.2.1 Java注释类型

- 文档注释(JavaDoc)：用于生成API文档
- 实现注释：解释代码是如何实现的
- 块注释：提供对代码段的额外说明
- 行注释：简短说明或提示

#### 4.2.2 JavaDoc规范

- 所有公有类、接口、方法和字段都应有JavaDoc
- 简明扼要，避免冗余信息
- 使用HTML标签增强可读性
- 必须包含完整的参数、返回值和异常说明

**类/接口文档：**

```java
/**
 * 客户信息实体类，表示系统中的客户
 * 
 * <p>该类映射数据库表crm_customer，存储客户基本信息</p>
 * 
 * @author 开发者名字
 * @version 1.0
 * @since 1.0
 */
public class Customer {
    // ...
}
```

**方法文档：**

```java
/**
 * 根据条件分页查询客户信息
 * 
 * <p>支持按名称、电话、邮箱等条件模糊查询，并支持分页</p>
 * 
 * @param query 查询条件，包含查询参数
 * @param pageable 分页参数，包含页码、每页大小、排序信息
 * @return 客户信息分页结果
 * @throws IllegalArgumentException 当查询参数不合法时
 * @see CustomerQuery
 * @since 1.0
 */
public Page<CustomerDTO> pageCustomers(CustomerQuery query, Pageable pageable) {
    // ...
}
```

**字段文档：**

```java
/**
 * 客户状态：1-正常，0-禁用
 */
private Integer status;
```

#### 4.2.3 实现注释规范

- 用于解释复杂的实现逻辑
- 说明为什么这样实现，而不是简单重复代码
- 解释核心算法或业务逻辑

```java
// 使用二分查找快速定位客户级别，性能优于循环遍历
int calculateCustomerLevel(int points) {
    int[] levelPoints = {0, 1000, 3000, 10000, 50000};
    int left = 0, right = levelPoints.length - 1;
    
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (levelPoints[mid] <= points && points < levelPoints[mid + 1]) {
            return mid;
        } else if (levelPoints[mid] > points) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    
    return left;
}
```

#### 4.2.4 TODO和FIXME注释

- 使用统一格式的TODO和FIXME注释
- 包含日期和开发者标识
- 描述需要完成的工作
- 定期检查并处理TODO项

```java
// TODO(zhang.san, 2023-01-15): 优化查询性能，当数据量大时存在性能问题

// FIXME(li.si, 2023-02-01): 修复多线程并发问题，见Issue #123
```

### 4.3 日志规范

#### 4.3.1 日志级别使用

- **ERROR**：影响系统运行的错误，需立即关注
- **WARN**：潜在问题，不影响系统运行但需关注
- **INFO**：重要业务事件和状态变更
- **DEBUG**：调试信息，帮助定位问题
- **TRACE**：最详细的跟踪信息，一般只在开发环境启用

```java
// 错误日志：系统异常
log.error("订单处理失败: {}", orderId, exception);

// 警告日志：可能存在问题
log.warn("用户{}连续{}次登录失败", username, failCount);

// 信息日志：重要业务事件
log.info("订单[{}]状态变更: {} -> {}", orderId, oldStatus, newStatus);

// 调试日志：调试信息
log.debug("处理客户请求参数: {}", JsonUtil.toJson(request));

// 跟踪日志：详细流程
log.trace("方法执行时间: {}ms", executionTime);
```

#### 4.3.2 日志内容规范

- 包含关键上下文信息：对象ID、用户ID、操作类型
- 保持简洁，避免过长日志
- 敏感信息(密码、手机号等)需脱敏处理
- 异常日志需包含完整堆栈信息
- 批量操作需记录操作数量和耗时
- 避免在循环中频繁打印日志

```java
// Good
log.info("用户[{}]创建订单成功，订单号: {}, 金额: {}", userId, orderNo, amount);

// Bad - 敏感信息未脱敏
log.info("用户手机号: {}, 银行卡: {}", phone, cardNo);

// Good - 敏感信息脱敏
log.info("用户手机号: {}, 银行卡: {}", SensitiveUtil.maskPhone(phone), SensitiveUtil.maskBankCard(cardNo));

// Good - 批量操作
log.info("导入客户数据完成，总数: {}, 成功: {}, 失败: {}, 耗时: {}ms", 
    total, successCount, failCount, endTime - startTime);
```

#### 4.3.3 MDC使用规范

使用MDC(Mapped Diagnostic Context)记录请求上下文信息：

```java
// 在请求处理开始时设置MDC
MDC.put("requestId", UUID.randomUUID().toString());
MDC.put("userId", currentUser.getId());
MDC.put("clientIp", request.getRemoteAddr());

try {
    // 业务处理
} finally {
    // 请求结束时清理MDC
    MDC.clear();
}
```

配置logback将MDC信息包含在日志中：

```xml
<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level [%X{requestId}] [%X{userId}] %logger{36} - %msg%n</pattern>
    </encoder>
</appender>
``` 

## 5. 数据库设计规范

### 5.1 数据库设计原则

- **规范化**：遵循第三范式，避免数据冗余
- **完整性**：确保数据的完整性和一致性
- **可扩展性**：设计灵活，便于未来扩展
- **性能**：考虑查询性能和并发性能
- **安全性**：保护敏感数据，防止SQL注入

### 5.2 表设计规范

#### 5.2.1 主键设计

- 使用自增主键（如MySQL的AUTO_INCREMENT）
- 主键命名：`表名_id`
- 复合主键：用于多表关联，命名：`表1_字段1_表2_字段2`

#### 5.2.2 外键设计

- 外键命名：`表名_字段名`
- 外键约束：`ON DELETE CASCADE`或`ON UPDATE CASCADE`
- 外键索引：`KEY idx_字段名`

#### 5.2.3 索引设计

- 主键索引：`PRIMARY KEY`
- 唯一索引：`UNIQUE KEY`
- 普通索引：`KEY idx_字段名`
- 组合索引：`KEY idx_字段1_字段2`
- 覆盖索引：查询所需字段都在索引中

### 5.3 字段设计规范

#### 5.3.1 基本类型

- `VARCHAR`：可变字符串，长度可变
- `CHAR`：固定长度字符串，长度固定
- `INT`：整数类型
- `BIGINT`：大整数类型
- `FLOAT`：浮点数类型
- `DOUBLE`：双精度浮点数类型
- `DECIMAL`：精确小数类型
- `DATE`：日期类型
- `TIME`：时间类型
- `DATETIME`：日期时间类型
- `TIMESTAMP`：时间戳类型
- `BOOLEAN`：布尔类型

#### 5.3.2 字符串类型

- 使用UTF-8编码
- 避免使用`TEXT`类型，除非数据量特别大
- 字符串长度根据实际需求合理设置

#### 5.3.3 数值类型

- 使用`DECIMAL`存储精确小数
- 避免使用`FLOAT`和`DOUBLE`，除非需要浮点运算
- 整数类型选择合适的范围

#### 5.3.4 日期时间类型

- 使用`DATETIME`或`TIMESTAMP`存储完整时间
- 避免使用`DATE`和`TIME`，除非只存储日期或时间
- 时间戳类型用于排序和索引

#### 5.3.5 布尔类型

- 使用`BOOLEAN`或`TINYINT(1)`
- 0表示`FALSE`，1表示`TRUE`

#### 5.3.6 枚举类型

- 使用`TINYINT`或`VARCHAR`存储枚举值
- 枚举值命名规范：`表名_字段名_枚举值`
- 枚举值表：`表名_枚举值`

### 5.4 表关系设计

#### 5.4.1 一对一关系

- 使用外键关联
- 外键命名：`表1_字段1_表2_字段2`
- 索引：`KEY idx_字段名`

#### 5.4.2 一对多关系

- 使用外键关联
- 外键命名：`表1_字段1_表2_字段2`
- 索引：`KEY idx_字段名`

#### 5.4.3 多对多关系

- 使用中间表
- 中间表命名：`表1_表2`
- 外键：`表1_id`，`表2_id`
- 索引：`KEY idx_字段名`

### 5.5 数据库优化

#### 5.5.1 索引优化

- 频繁查询的字段应建立索引
- 索引列数不宜过多，一般不超过5个
- 组合索引要合理
- 避免对大字段建立索引

#### 5.5.2 查询优化

- 使用`LIMIT`限制返回结果数量
- 避免使用`SELECT *`，只查询需要的字段
- 使用`DISTINCT`避免重复数据
- 使用`GROUP BY`和`HAVING`优化分组查询

#### 5.5.3 事务优化

- 减少事务范围，尽量在业务层完成操作
- 批量操作使用`Batch`模式
- 大事务拆分为小事务

#### 5.5.4 缓存策略

- 使用Redis缓存热点数据
- 缓存过期时间合理设置
- 缓存穿透和雪崩处理

## 6. API设计规范

### 6.1 接口设计原则

- **RESTful**：使用HTTP方法（GET, POST, PUT, DELETE, PATCH）
- **统一资源标识符(URI)**：使用名词，复数形式
- **资源状态**：使用HTTP状态码表示资源状态
- **请求/响应**：JSON格式，统一响应格式
- **版本控制**：使用URL路径或HTTP头进行版本控制

### 6.2 接口命名规范

- **动词 + 名词**：`/api/v1/customers`
- **动词 + 名词 + ID**：`/api/v1/customers/{id}`
- **动词 + 名词 + 集合**：`/api/v1/customers/list`
- **动词 + 名词 + 集合 + 分页**：`/api/v1/customers/page`

### 6.3 请求/响应规范

#### 6.3.1 请求头

```
Content-Type: application/json
Accept: application/json
Authorization: Bearer <token>
```

#### 6.3.2 请求体

```json
{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "123-456-7890"
}
```

#### 6.3.3 响应体

```json
{
    "code": 200,
    "message": "Success",
    "data": {
        "id": 1,
        "name": "John Doe"
    }
}
```

### 6.4 错误处理规范

- **统一错误码**：定义全局错误码
- **错误信息**：清晰、简洁、国际化
- **堆栈信息**：生产环境禁用，便于调试
- **日志记录**：记录关键错误信息

### 6.5 接口文档规范

- **Swagger/OpenAPI**：自动生成API文档
- **Markdown**：手动编写API文档
- **版本说明**：每个版本API变更记录
- **接口示例**：提供完整的请求/响应示例

## 7. 安全规范

### 7.1 认证与授权

- **JWT**：JSON Web Token，用于身份认证和授权
- **OAuth2.0**：认证授权框架
- **Spring Security**：安全框架，提供认证、授权、CSRF保护等

### 7.2 密码安全

- **强密码策略**：密码必须包含大小写字母、数字和特殊字符
- **加盐加密**：使用随机盐值加密
- **密码加密**：使用SHA-256等哈希算法
- **敏感信息脱敏**：手机号、银行卡号等敏感信息脱敏

### 7.3 数据加密

- **敏感数据加密**：使用AES、RSA等加密算法
- **传输加密**：HTTPS
- **静态加密**：数据库加密

### 7.4 防止SQL注入

- **预编译语句**：使用`PreparedStatement`
- **参数化查询**：使用`?`或`${}`占位符
- **输入验证**：严格验证输入

### 7.5 防止XSS攻击

- **输出编码**：HTML、JavaScript、CSS等特殊字符转义
- **内容安全策略(CSP)**：限制资源加载
- **输入验证**：严格验证输入

### 7.6 防止CSRF攻击

- **CSRF Token**：在表单中添加随机Token
- **SameSite Cookie**：设置Cookie的SameSite属性
- **Referer检查**：验证请求来源

### 7.7 防止未授权访问

- **RBAC**：基于角色的访问控制
- **权限控制注解**：`@RequiresPermission`
- **权限拦截器**：拦截未授权请求
- **数据权限控制**：`@DataScope`

## 8. 测试规范

### 8.1 单元测试

- **Mock**：模拟依赖，隔离测试
- **断言**：清晰、可读的断言
- **测试覆盖率**：覆盖核心业务逻辑
- **测试驱动开发(TDD)**：先写测试，再写实现

### 8.2 集成测试

- **模拟真实环境**：使用Mock数据
- **测试完整流程**：从UI到数据库
- **性能测试**：模拟高并发

### 8.3 性能测试

- **基准测试**：记录系统性能指标
- **压力测试**：模拟大量用户
- **负载测试**：测试系统在不同负载下的表现

### 8.4 安全测试

- **渗透测试**：模拟黑客攻击
- **SQL注入测试**：测试SQL注入漏洞
- **XSS测试**：测试XSS漏洞
- **CSRF测试**：测试CSRF漏洞

## 9. 性能优化规范

### 9.1 数据库优化

- **索引**：合理使用索引
- **查询优化**：避免全表扫描
- **事务优化**：减少事务范围
- **缓存**：使用Redis缓存热点数据

### 9.2 代码优化

- **减少循环**：避免在循环中频繁查询数据库
- **批量操作**：使用`Batch`模式
- **懒加载**：按需加载数据
- **缓存**：使用Caffeine缓存

### 9.3 前端优化

- **组件优化**：减少不必要的DOM操作
- **图片优化**：使用CDN和压缩
- **资源加载**：使用`preload`和`prefetch`

## 10. 部署与运维规范

### 10.1 环境配置

- **开发环境**：`application-dev.yml`
- **测试环境**：`application-test.yml`
- **生产环境**：`application-prod.yml`

### 10.2 部署流程

1. 代码提交到Git仓库
2. CI/CD自动构建
3. 部署到测试环境
4. 手动测试
5. 部署到生产环境

### 10.3 监控与告警

- **日志监控**：ELK Stack
- **性能监控**：Prometheus + Grafana
- **告警**：钉钉、企业微信

### 10.4 备份与恢复

- **自动备份**：每天凌晨备份
- **数据恢复**：支持全量和增量恢复
- **备份加密**：备份文件加密

## 11. 版本控制规范

### 11.1 Git工作流

- **Feature Branch**：每个功能一个分支
- **Pull Request**：代码审查
- **Merge**：合并到主分支
- **Tag**：版本发布

### 11.2 版本命名

- **主版本号**：`MAJOR.MINOR.PATCH`
- **示例**：`1.0.0` -> `1.1.0` -> `1.1.1`

### 11.3 版本发布流程

1. 开发新功能
2. 测试通过
3. 更新版本号
4. 发布到生产环境

## 12. 多租户设计规范

### 12.1 租户隔离

- **数据库隔离**：每个租户一个数据库
- **表前缀**：`crm_` -> `crm_tenant_`
- **索引隔离**：租户索引前缀

### 12.2 数据隔离

- **租户ID**：`tenant_id`
- **租户字段**：`tenant_id`
- **租户索引**：`KEY idx_tenant_字段名`

### 12.3 权限隔离

- **租户权限**：租户管理员拥有租户内所有权限
- **租户数据**：租户数据只能由租户管理员访问

## 13. 国际化与本地化规范

### 13.1 国际化

- **资源文件**：`messages.properties`
- **语言包**：`messages_zh_CN.properties`，`messages_en.properties`
- **占位符**：`${messageKey}`

### 13.2 本地化

- **日期格式**：`yyyy-MM-dd HH:mm:ss`
- **货币格式**：`¥1,234.56`
- **数字格式**：`1,234.56`

## 14. 前端开发规范

### 14.1 技术栈

- **Vue3 + TypeScript**：统一前端技术栈
- **Pinia**：状态管理
- **Vite**：构建工具

### 14.2 项目结构

```
vliascrm-frontend/
├── src/
│   ├── pages/              # 页面文件
│   │   ├── admin/          # B端管理页面
│   │   └── mall/           # C端商城页面
│   ├── components/          # 公共组件
│   │   ├── base/           # 基础组件
│   │   └── business/       # 业务组件
│   ├── composables/        # 组合式函数
│   ├── stores/             # 状态管理
│   ├── utils/              # 工具函数
│   ├── api/                # API接口
│   ├── styles/             # 样式文件
│   └── types/              # 类型定义
├── platforms/              # 平台特定代码
│   ├── h5/                 # H5平台
│   ├── mp-weixin/          # 微信小程序
│   ├── app-plus/           # App平台
│   └── app-vue/            # Vue版本App
└── package.json
```

### 14.3 组件开发规范

#### 14.3.1 基础组件规范

```vue
<!-- 基础按钮组件 -->
<template>
  <button 
    :class="buttonClass"
    :disabled="disabled"
    @click="handleClick">
    <slot />
  </button>
</template>

<script setup lang="ts">
interface Props {
  type?: 'primary' | 'secondary' | 'danger'
  size?: 'small' | 'medium' | 'large'
  disabled?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  type: 'primary',
  size: 'medium',
  disabled: false
})

const emit = defineEmits<{
  click: [event: Event]
}>()

const buttonClass = computed(() => [
  'base-button',
  `base-button--${props.type}`,
  `base-button--${props.size}`
])

const handleClick = (event: Event) => {
  if (!props.disabled) {
    emit('click', event)
  }
}
</script>
```

#### 14.3.2 业务组件规范

```vue
<!-- 客户卡片组件 -->
<template>
  <div class="customer-card">
    <div class="customer-card__header">
      <h3 class="customer-card__title">{{ customer.name }}</h3>
      <span class="customer-card__level">{{ customer.levelName }}</span>
    </div>
    <div class="customer-card__content">
      <p class="customer-card__contact">
        <i class="icon-phone"></i>
        {{ customer.mobile }}
      </p>
      <p class="customer-card__address">
        <i class="icon-location"></i>
        {{ customer.address }}
      </p>
    </div>
    <div class="customer-card__actions">
      <slot name="actions" />
    </div>
  </div>
</template>

<script setup lang="ts">
interface Customer {
  id: number
  name: string
  mobile: string
  address: string
  levelName: string
}

defineProps<{
  customer: Customer
}>()
</script>
```

### 14.4 状态管理规范

#### 14.4.1 Pinia Store设计

```typescript
// 用户状态管理
export const useUserStore = defineStore('user', () => {
  // 状态
  const user = ref<User | null>(null)
  const token = ref<string>('')
  const permissions = ref<string[]>([])

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const hasPermission = computed(() => (permission: string) => 
    permissions.value.includes(permission)
  )

  // 方法
  const login = async (credentials: LoginCredentials) => {
    try {
      const response = await authApi.login(credentials)
      user.value = response.user
      token.value = response.token
      permissions.value = response.permissions
      
      // 保存到本地存储
      uni.setStorageSync('token', token.value)
      uni.setStorageSync('user', user.value)
    } catch (error) {
      throw error
    }
  }

  const logout = () => {
    user.value = null
    token.value = ''
    permissions.value = []
    
    // 清除本地存储
    uni.removeStorageSync('token')
    uni.removeStorageSync('user')
  }

  return {
    user,
    token,
    permissions,
    isLoggedIn,
    hasPermission,
    login,
    logout
  }
})
```

### 14.5 平台适配规范

#### 14.5.1 条件编译

```vue
<template>
  <div class="customer-list">
    <!-- #ifdef H5 -->
    <div class="h5-specific-content">
      <!-- H5特有内容 -->
    </div>
    <!-- #endif -->
    
    <!-- #ifdef MP-WEIXIN -->
    <div class="mp-specific-content">
      <!-- 小程序特有内容 -->
    </div>
    <!-- #endif -->
    
    <!-- #ifdef APP-PLUS -->
    <div class="app-specific-content">
      <!-- App特有内容 -->
    </div>
    <!-- #endif -->
  </div>
</template>
```

#### 14.5.2 平台API封装

```typescript
// 平台API统一封装
export const platformAPI = {
  // 获取系统信息
  getSystemInfo() {
    // #ifdef H5
    return uni.getSystemInfoSync()
    // #endif
    
    // #ifdef MP-WEIXIN
    return uni.getSystemInfoSync()
    // #endif
    
    // #ifdef APP-PLUS
    return plus.os.getInfo()
    // #endif
  },

  // 显示消息提示
  showToast(options: UniApp.ShowToastOptions) {
    // #ifdef H5
    uni.showToast(options)
    // #endif
    
    // #ifdef MP-WEIXIN
    uni.showToast(options)
    // #endif
    
    // #ifdef APP-PLUS
    plus.nativeUI.toast(options.title)
    // #endif
  }
}
```

## 15. 可观测性规范

### 15.1 日志规范

- **ELK Stack**：Elasticsearch, Logstash, Kibana
- **MDC**：Mapped Diagnostic Context
- **日志级别**：ERROR, WARN, INFO, DEBUG, TRACE
- **日志内容**：关键上下文信息，如请求ID、用户ID
- **日志格式**：`%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level [%X{requestId}] [%X{userId}] %logger{36} - %msg%n`

### 15.2 监控指标

- **性能指标**：响应时间、吞吐量、错误率
- **业务指标**：订单量、用户数、活跃度
- **资源指标**：CPU、内存、磁盘、网络
- **自定义指标**：业务特定指标

### 15.3 告警规则

- **错误告警**：系统异常、数据库错误
- **性能告警**：响应时间异常、吞吐量异常
- **业务告警**：订单异常、用户异常
- **资源告警**：资源使用率过高

## 16. 业务领域模型设计

### 16.1 实体设计

- **Customer**：客户信息
- **Product**：商品信息
- **Order**：订单信息
- **Inventory**：库存信息
- **User**：用户信息
- **Permission**：权限信息
- **Role**：角色信息
- **Tenant**：租户信息

### 16.2 关系设计

- **Customer <-> Order**：一对多
- **Product <-> OrderItem**：一对多
- **Inventory <-> Product**：一对多
- **User <-> Role**：多对多
- **Role <-> Permission**：多对多
- **Tenant <-> Customer**：一对多
- **Tenant <-> User**：一对多

## 17. 权限控制规范

### 17.1 RBAC权限模型设计

#### 17.1.1 权限分级设计

VLIAS CRM系统采用RBAC（基于角色的访问控制）模型，支持两级权限控制：

- **1级权限（模块级）**：控制用户能否看到/进入某个主模块菜单
- **2级权限（操作级）**：控制用户在模块下能否进行具体操作

```java
/**
 * 权限类型枚举
 */
public enum PermissionType {
    MODULE(1, "模块级权限"),
    OPERATION(2, "操作级权限");
    
    private final Integer code;
    private final String description;
}
```

#### 17.1.2 角色权限配置

系统预定义四种主要角色：

| 角色 | 权限范围 | 说明 |
|------|----------|------|
| 超级管理员 | 全部权限 | 系统最高权限，可进行所有操作 |
| 经理/主管 | 大部分权限 | 可进行业务操作，无高危权限 |
| 员工/操作员 | 基础权限 | 可进行日常业务操作 |
| 仓库管理员 | 库存相关权限 | 专注于库存管理操作 |

### 17.2 权限控制实现

#### 17.2.1 注解权限控制

```java
/**
 * 权限控制注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {
    String value();           // 权限标识
    PermissionType type() default PermissionType.OPERATION;
    String description() default "";
}

// 使用示例
@RequiresPermission("customer:create")
public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
    // 业务逻辑
}
```

#### 17.2.2 权限拦截器

```java
@Component
public class PermissionInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
                           Object handler) throws Exception {
        // 获取当前用户权限
        Set<String> userPermissions = getCurrentUserPermissions();
        
        // 检查方法权限
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            RequiresPermission permission = method.getMethodAnnotation(RequiresPermission.class);
            
            if (permission != null) {
                if (!userPermissions.contains(permission.value())) {
                    throw new AccessDeniedException("权限不足");
                }
            }
        }
        
        return true;
    }
}
```

#### 17.2.3 数据权限控制

```java
/**
 * 数据权限注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataScope {
    String deptAlias() default "";    // 部门表别名
    String userAlias() default "";    // 用户表别名
    String orgAlias() default "";     // 组织表别名
}

// 使用示例
@DataScope(deptAlias = "d", userAlias = "u")
public List<CustomerDTO> listCustomers(CustomerQuery query) {
    // 自动添加数据权限过滤条件
}
```

### 17.3 前端权限控制

#### 17.3.1 路由权限控制

```javascript
// 路由权限配置
const routes = [
  {
    path: '/customer',
    component: CustomerLayout,
    meta: { 
      title: '客户管理',
      permission: 'customer:view',
      icon: 'customer'
    },
    children: [
      {
        path: 'list',
        component: CustomerList,
        meta: { 
          title: '客户列表',
          permission: 'customer:list'
        }
      },
      {
        path: 'create',
        component: CustomerForm,
        meta: { 
          title: '新增客户',
          permission: 'customer:create'
        }
      }
    ]
  }
]
```

#### 17.3.2 按钮权限控制

```vue
<template>
  <div class="customer-actions">
    <el-button 
      v-if="hasPermission('customer:create')"
      @click="createCustomer">
      新增客户
    </el-button>
    <el-button 
      v-if="hasPermission('customer:edit')"
      @click="editCustomer">
      编辑
    </el-button>
    <el-button 
      v-if="hasPermission('customer:delete')"
      @click="deleteCustomer">
      删除
    </el-button>
  </div>
</template>

<script>
import { usePermission } from '@/composables/usePermission'

export default {
  setup() {
    const { hasPermission } = usePermission()
    
    return {
      hasPermission
    }
  }
}
</script>
```

## 18. 多端开发规范

### 18.1 技术栈统一

#### 18.1.1 跨端技术选型

- **uni-app**：主要跨端开发框架
- **Vue3 + TypeScript**：统一前端技术栈
- **Pinia**：状态管理
- **Vite**：构建工具

#### 18.1.2 项目结构规范

```
vliascrm-frontend/
├── src/
│   ├── pages/              # 页面文件
│   │   ├── admin/          # B端管理页面
│   │   └── mall/           # C端商城页面
│   ├── components/          # 公共组件
│   │   ├── base/           # 基础组件
│   │   └── business/       # 业务组件
│   ├── composables/        # 组合式函数
│   ├── stores/             # 状态管理
│   ├── utils/              # 工具函数
│   ├── api/                # API接口
│   ├── styles/             # 样式文件
│   └── types/              # 类型定义
├── platforms/              # 平台特定代码
│   ├── h5/                 # H5平台
│   ├── mp-weixin/          # 微信小程序
│   ├── app-plus/           # App平台
│   └── app-vue/            # Vue版本App
└── package.json
```

### 18.2 组件开发规范

#### 18.2.1 基础组件规范

```vue
<!-- 基础按钮组件 -->
<template>
  <button 
    :class="buttonClass"
    :disabled="disabled"
    @click="handleClick">
    <slot />
  </button>
</template>

<script setup lang="ts">
interface Props {
  type?: 'primary' | 'secondary' | 'danger'
  size?: 'small' | 'medium' | 'large'
  disabled?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  type: 'primary',
  size: 'medium',
  disabled: false
})

const emit = defineEmits<{
  click: [event: Event]
}>()

const buttonClass = computed(() => [
  'base-button',
  `base-button--${props.type}`,
  `base-button--${props.size}`
])

const handleClick = (event: Event) => {
  if (!props.disabled) {
    emit('click', event)
  }
}
</script>
```

#### 18.2.2 业务组件规范

```vue
<!-- 客户卡片组件 -->
<template>
  <div class="customer-card">
    <div class="customer-card__header">
      <h3 class="customer-card__title">{{ customer.name }}</h3>
      <span class="customer-card__level">{{ customer.levelName }}</span>
    </div>
    <div class="customer-card__content">
      <p class="customer-card__contact">
        <i class="icon-phone"></i>
        {{ customer.mobile }}
      </p>
      <p class="customer-card__address">
        <i class="icon-location"></i>
        {{ customer.address }}
      </p>
    </div>
    <div class="customer-card__actions">
      <slot name="actions" />
    </div>
  </div>
</template>

<script setup lang="ts">
interface Customer {
  id: number
  name: string
  mobile: string
  address: string
  levelName: string
}

defineProps<{
  customer: Customer
}>()
</script>
```

### 18.3 状态管理规范

#### 18.3.1 Pinia Store设计

```typescript
// 用户状态管理
export const useUserStore = defineStore('user', () => {
  // 状态
  const user = ref<User | null>(null)
  const token = ref<string>('')
  const permissions = ref<string[]>([])

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const hasPermission = computed(() => (permission: string) => 
    permissions.value.includes(permission)
  )

  // 方法
  const login = async (credentials: LoginCredentials) => {
    try {
      const response = await authApi.login(credentials)
      user.value = response.user
      token.value = response.token
      permissions.value = response.permissions
      
      // 保存到本地存储
      uni.setStorageSync('token', token.value)
      uni.setStorageSync('user', user.value)
    } catch (error) {
      throw error
    }
  }

  const logout = () => {
    user.value = null
    token.value = ''
    permissions.value = []
    
    // 清除本地存储
    uni.removeStorageSync('token')
    uni.removeStorageSync('user')
  }

  return {
    user,
    token,
    permissions,
    isLoggedIn,
    hasPermission,
    login,
    logout
  }
})
```

### 18.4 平台适配规范

#### 18.4.1 条件编译

```vue
<template>
  <div class="customer-list">
    <!-- #ifdef H5 -->
    <div class="h5-specific-content">
      <!-- H5特有内容 -->
    </div>
    <!-- #endif -->
    
    <!-- #ifdef MP-WEIXIN -->
    <div class="mp-specific-content">
      <!-- 小程序特有内容 -->
    </div>
    <!-- #endif -->
    
    <!-- #ifdef APP-PLUS -->
    <div class="app-specific-content">
      <!-- App特有内容 -->
    </div>
    <!-- #endif -->
  </div>
</template>
```

#### 18.4.2 平台API封装

```typescript
// 平台API统一封装
export const platformAPI = {
  // 获取系统信息
  getSystemInfo() {
    // #ifdef H5
    return uni.getSystemInfoSync()
    // #endif
    
    // #ifdef MP-WEIXIN
    return uni.getSystemInfoSync()
    // #endif
    
    // #ifdef APP-PLUS
    return plus.os.getInfo()
    // #endif
  },

  // 显示消息提示
  showToast(options: UniApp.ShowToastOptions) {
    // #ifdef H5
    uni.showToast(options)
    // #endif
    
    // #ifdef MP-WEIXIN
    uni.showToast(options)
    // #endif
    
    // #ifdef APP-PLUS
    plus.nativeUI.toast(options.title)
    // #endif
  }
}
```

## 19. 第三方集成规范

### 19.1 支付集成规范

#### 19.1.1 支付接口设计

```java
/**
 * 支付服务接口
 */
public interface PaymentService {
    
    /**
     * 创建支付订单
     */
    PaymentResult createPayment(PaymentRequest request);
    
    /**
     * 查询支付状态
     */
    PaymentStatus queryPaymentStatus(String paymentNo);
    
    /**
     * 处理支付回调
     */
    void handlePaymentCallback(PaymentCallback callback);
    
    /**
     * 申请退款
     */
    RefundResult refund(RefundRequest request);
}

/**
 * 微信支付实现
 */
@Service
public class WechatPaymentServiceImpl implements PaymentService {
    
    @Override
    public PaymentResult createPayment(PaymentRequest request) {
        // 调用微信支付API
        WechatPayRequest wechatRequest = convertToWechatRequest(request);
        WechatPayResponse response = wechatPayClient.createOrder(wechatRequest);
        
        return PaymentResult.builder()
            .paymentNo(response.getPrepayId())
            .paymentUrl(response.getCodeUrl())
            .build();
    }
}
```

#### 19.1.2 支付配置管理

```java
/**
 * 支付配置
 */
@Data
@ConfigurationProperties(prefix = "payment")
public class PaymentProperties {
    
    private WechatPay wechat = new WechatPay();
    private Alipay alipay = new Alipay();
    private UnionPay unionPay = new UnionPay();
    
    @Data
    public static class WechatPay {
        private String appId;
        private String mchId;
        private String apiKey;
        private String certPath;
        private String notifyUrl;
    }
}
```

### 19.2 物流集成规范

#### 19.2.1 物流接口设计

```java
/**
 * 物流服务接口
 */
public interface LogisticsService {
    
    /**
     * 创建物流订单
     */
    LogisticsOrder createLogisticsOrder(LogisticsRequest request);
    
    /**
     * 查询物流轨迹
     */
    List<LogisticsTrack> queryLogisticsTrack(String trackingNo);
    
    /**
     * 获取物流公司列表
     */
    List<LogisticsCompany> getLogisticsCompanies();
}

/**
 * 快递100实现
 */
@Service
public class Kuaidi100ServiceImpl implements LogisticsService {
    
    @Override
    public LogisticsOrder createLogisticsOrder(LogisticsRequest request) {
        // 调用快递100 API
        Kuaidi100Request kuaidiRequest = convertToKuaidiRequest(request);
        Kuaidi100Response response = kuaidi100Client.createOrder(kuaidiRequest);
        
        return LogisticsOrder.builder()
            .trackingNo(response.getTrackingNo())
            .logisticsCompany(response.getCompanyCode())
            .build();
    }
}
```

### 19.3 短信集成规范

#### 19.3.1 短信服务设计

```java
/**
 * 短信服务接口
 */
public interface SmsService {
    
    /**
     * 发送验证码
     */
    void sendVerificationCode(String mobile, String code);
    
    /**
     * 发送通知短信
     */
    void sendNotification(String mobile, String templateId, Map<String, String> params);
    
    /**
     * 发送营销短信
     */
    void sendMarketing(String mobile, String content);
}

/**
 * 阿里云短信实现
 */
@Service
public class AliyunSmsServiceImpl implements SmsService {
    
    @Override
    public void sendVerificationCode(String mobile, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        
        AliyunSmsRequest request = AliyunSmsRequest.builder()
            .phoneNumbers(mobile)
            .templateCode("SMS_VERIFICATION_CODE")
            .templateParam(JsonUtil.toJson(params))
            .build();
            
        aliyunSmsClient.sendSms(request);
    }
}
```

### 19.4 文件存储集成规范

#### 19.4.1 文件服务设计

```java
/**
 * 文件存储服务接口
 */
public interface FileStorageService {
    
    /**
     * 上传文件
     */
    FileUploadResult uploadFile(MultipartFile file, String directory);
    
    /**
     * 删除文件
     */
    void deleteFile(String fileUrl);
    
    /**
     * 获取文件访问URL
     */
    String getFileUrl(String fileKey);
}

/**
 * 腾讯云COS实现
 */
@Service
public class TencentCosServiceImpl implements FileStorageService {
    
    @Override
    public FileUploadResult uploadFile(MultipartFile file, String directory) {
        String fileName = generateFileName(file.getOriginalFilename());
        String fileKey = directory + "/" + fileName;
        
        // 上传到腾讯云COS
        COSObjectRequest request = new COSObjectRequest();
        request.setBucketName(cosProperties.getBucketName());
        request.setKey(fileKey);
        request.setInputStream(file.getInputStream());
        
        cosClient.putObject(request);
        
        return FileUploadResult.builder()
            .fileUrl(getFileUrl(fileKey))
            .fileKey(fileKey)
            .fileName(fileName)
            .fileSize(file.getSize())
            .build();
    }
}
```

## 20. 数据迁移与备份规范

### 20.1 数据库迁移规范

#### 20.1.1 Flyway迁移脚本规范

```sql
-- V1.0.0__init_schema.sql
-- 初始化数据库表结构

-- 系统基础表
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    -- ... 其他字段
    UNIQUE KEY (username)
) COMMENT='系统用户表';

-- 客户管理表
CREATE TABLE crm_customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '客户ID',
    customer_name VARCHAR(100) NOT NULL COMMENT '客户名称',
    -- ... 其他字段
    KEY idx_owner_user_id (owner_user_id)
) COMMENT='客户表';
```

```sql
-- V1.0.1__add_customer_fields.sql
-- 添加客户表字段

ALTER TABLE crm_customer 
ADD COLUMN customer_source VARCHAR(50) COMMENT '客户来源' AFTER customer_name,
ADD COLUMN customer_level VARCHAR(20) COMMENT '客户等级' AFTER customer_source;
```

#### 20.1.2 数据迁移脚本规范

```java
/**
 * 数据迁移服务
 */
@Service
public class DataMigrationService {
    
    /**
     * 执行数据迁移
     */
    @Transactional
    public void migrateData(String version) {
        switch (version) {
            case "1.0.1":
                migrateCustomerData();
                break;
            case "1.0.2":
                migrateOrderData();
                break;
            default:
                throw new IllegalArgumentException("未知的迁移版本: " + version);
        }
    }
    
    /**
     * 迁移客户数据
     */
    private void migrateCustomerData() {
        // 迁移逻辑
        List<Customer> customers = customerRepository.findAll();
        for (Customer customer : customers) {
            // 数据转换和迁移
            customer.setCustomerSource("未知");
            customer.setCustomerLevel("普通");
            customerRepository.save(customer);
        }
    }
}
```

### 20.2 数据备份规范

#### 20.2.1 自动备份策略

```java
/**
 * 数据备份服务
 */
@Service
public class DataBackupService {
    
    /**
     * 执行数据库备份
     */
    @Scheduled(cron = "0 2 * * *") // 每天凌晨2点执行
    public void backupDatabase() {
        String backupPath = generateBackupPath();
        
        // 执行MySQL备份
        String command = String.format(
            "mysqldump -h%s -P%s -u%s -p%s %s > %s",
            dbProperties.getHost(),
            dbProperties.getPort(),
            dbProperties.getUsername(),
            dbProperties.getPassword(),
            dbProperties.getDatabase(),
            backupPath
        );
        
        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                // 备份成功，上传到云存储
                uploadToCloudStorage(backupPath);
                log.info("数据库备份成功: {}", backupPath);
            } else {
                log.error("数据库备份失败，退出码: {}", exitCode);
            }
        } catch (Exception e) {
            log.error("数据库备份异常", e);
        }
    }
    
    /**
     * 清理过期备份文件
     */
    @Scheduled(cron = "0 3 * * 0") // 每周日凌晨3点执行
    public void cleanExpiredBackups() {
        // 删除30天前的备份文件
        LocalDate expireDate = LocalDate.now().minusDays(30);
        cleanBackupFiles(expireDate);
    }
}
```

#### 20.2.2 备份文件管理

```java
/**
 * 备份文件管理
 */
@Component
public class BackupFileManager {
    
    /**
     * 上传备份文件到云存储
     */
    public void uploadToCloudStorage(String localPath) {
        String fileName = Paths.get(localPath).getFileName().toString();
        String cloudPath = "backups/" + LocalDate.now().toString() + "/" + fileName;
        
        // 上传到腾讯云COS
        COSObjectRequest request = new COSObjectRequest();
        request.setBucketName(cosProperties.getBackupBucket());
        request.setKey(cloudPath);
        request.setInputStream(new FileInputStream(localPath));
        
        cosClient.putObject(request);
        
        // 删除本地备份文件
        Files.delete(Paths.get(localPath));
    }
    
    /**
     * 从云存储下载备份文件
     */
    public void downloadFromCloudStorage(String cloudPath, String localPath) {
        COSObjectResponse response = cosClient.getObject(
            cosProperties.getBackupBucket(), 
            cloudPath
        );
        
        try (InputStream inputStream = response.getObjectContent();
             FileOutputStream outputStream = new FileOutputStream(localPath)) {
            IOUtils.copy(inputStream, outputStream);
        }
    }
}
```

### 20.3 数据恢复规范

#### 20.3.1 数据恢复流程

```java
/**
 * 数据恢复服务
 */
@Service
public class DataRecoveryService {
    
    /**
     * 执行数据恢复
     */
    @Transactional
    public void recoverData(String backupFile, boolean isFullRecovery) {
        try {
            // 1. 停止应用服务
            stopApplication();
            
            // 2. 备份当前数据库
            backupCurrentDatabase();
            
            // 3. 执行数据恢复
            if (isFullRecovery) {
                performFullRecovery(backupFile);
            } else {
                performPartialRecovery(backupFile);
            }
            
            // 4. 验证数据完整性
            validateDataIntegrity();
            
            // 5. 重启应用服务
            startApplication();
            
            log.info("数据恢复完成");
        } catch (Exception e) {
            log.error("数据恢复失败", e);
            // 回滚到恢复前的状态
            rollbackRecovery();
            throw e;
        }
    }
    
    /**
     * 执行全量恢复
     */
    private void performFullRecovery(String backupFile) {
        String command = String.format(
            "mysql -h%s -P%s -u%s -p%s %s < %s",
            dbProperties.getHost(),
            dbProperties.getPort(),
            dbProperties.getUsername(),
            dbProperties.getPassword(),
            dbProperties.getDatabase(),
            backupFile
        );
        
        Process process = Runtime.getRuntime().exec(command);
        int exitCode = process.waitFor();
        
        if (exitCode != 0) {
            throw new RuntimeException("数据恢复失败，退出码: " + exitCode);
        }
    }
}
```

### 20.4 数据同步规范

#### 20.4.1 主从数据同步

```java
/**
 * 数据同步服务
 */
@Service
public class DataSyncService {
    
    /**
     * 同步数据到从库
     */
    @Async
    public void syncToSlave() {
        // 获取需要同步的数据
        List<SyncData> syncDataList = getSyncData();
        
        for (SyncData syncData : syncDataList) {
            try {
                // 同步到从库
                slaveDataSource.execute(syncData.getSql());
                
                // 标记同步完成
                markSyncCompleted(syncData.getId());
                
                log.info("数据同步完成: {}", syncData.getId());
            } catch (Exception e) {
                log.error("数据同步失败: {}", syncData.getId(), e);
                // 记录同步失败
                markSyncFailed(syncData.getId(), e.getMessage());
            }
        }
    }
    
    /**
     * 检查数据一致性
     */
    public void checkDataConsistency() {
        // 检查主从数据一致性
        List<DataInconsistency> inconsistencies = findDataInconsistencies();
        
        for (DataInconsistency inconsistency : inconsistencies) {
            log.warn("发现数据不一致: {}", inconsistency);
            // 发送告警通知
            sendInconsistencyAlert(inconsistency);
        }
    }
}
```

这些补充的规范内容涵盖了VLIAS CRM系统的核心开发要求，包括权限控制、多端开发、第三方集成和数据管理等方面。这些规范将帮助团队更好地进行系统开发和维护。 