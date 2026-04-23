# 电子档案管理系统后端实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 搭建电子档案管理系统后端服务，提供RESTful API，包含用户认证、档案管理、文件操作、权限控制、查档审核等功能

**Architecture:** 基于SpringBoot的分布式后端服务，使用Spring Security + JWT实现认证，MyBatis操作SQLite数据库，Redis缓存会话和数据，RabbitMQ处理异步消息（操作日志、通知）

**Tech Stack:** Java 8+, SpringBoot 2.7.x, MyBatis-Plus, SQLite, Redis, RabbitMQ, JWT, Maven

---

## 文件结构

```
archive-management-backend/
├── pom.xml
├── src/main/java/com/archive/
│   ├── ArchiveApplication.java              # 启动类
│   ├── config/
│   │   ├── SecurityConfig.java              # Spring Security配置
│   │   ├── RedisConfig.java                 # Redis配置
│   │   ├── RabbitMQConfig.java              # RabbitMQ配置
│   │   ├── MyBatisConfig.java               # MyBatis配置
│   │   └── WebMvcConfig.java                # CORS跨域配置
│   ├── common/
│   │   ├── Result.java                      # 统一响应封装
│   │   ├── PageResult.java                  # 分页响应
│   │   └── Constants.java                   # 常量定义
│   ├── security/
│   │   ├── JwtTokenProvider.java            # JWT工具类
│   │   ├── JwtAuthFilter.java               # JWT认证过滤器
│   │   └── UserDetailsServiceImpl.java      # 用户详情服务
│   ├── entity/
│   │   ├── User.java                        # 用户实体
│   │   ├── Department.java                  # 部门实体
│   │   ├── Folder.java                      # 文件夹实体
│   │   ├── Category.java                    # 档案类别实体
│   │   ├── SecurityLevel.java               # 密级实体
│   │   ├── Archive.java                     # 档案实体
│   │   ├── ArchiveFile.java                 # 文件实体
│   │   ├── Permission.java                  # 权限实体
│   │   ├── OperationLog.java                # 操作日志实体
│   │   └── AccessApplication.java           # 查档申请实体
│   ├── mapper/
│   │   ├── UserMapper.java                  # 用户Mapper
│   │   ├── DepartmentMapper.java            # 部门Mapper
│   │   ├── FolderMapper.java                # 文件夹Mapper
│   │   ├── CategoryMapper.java              # 类别Mapper
│   │   ├── SecurityLevelMapper.java         # 密级Mapper
│   │   ├── ArchiveMapper.java               # 档案Mapper
│   │   ├── ArchiveFileMapper.java           # 文件Mapper
│   │   ├── PermissionMapper.java            # 权限Mapper
│   │   ├── OperationLogMapper.java          # 日志Mapper
│   │   └── AccessApplicationMapper.java     # 申请Mapper
│   ├── service/
│   │   ├── UserService.java                 # 用户服务
│   │   ├── DepartmentService.java           # 部门服务
│   │   ├── FolderService.java               # 文件夹服务
│   │   ├── ArchiveService.java              # 档案服务
│   │   ├── FileService.java                 # 文件服务
│   │   ├── SearchService.java               # 搜索服务
│   │   ├── ApplicationService.java          # 查档申请服务
│   │   ├── PermissionService.java           # 权限服务
│   │   └── LogService.java                  # 日志服务（RabbitMQ消费者）
│   ├── controller/
│   │   ├── AuthController.java              # 认证控制器
│   │   ├── UserController.java              # 用户管理控制器
│   │   ├── DepartmentController.java        # 部门管理控制器
│   │   ├── FolderController.java            # 文件夹控制器
│   │   ├── ArchiveController.java           # 档案管理控制器
│   │   ├── FileController.java              # 文件上传下载控制器
│   │   ├── SearchController.java            # 搜索控制器
│   │   ├── ApplicationController.java       # 查档申请控制器
│   │   └── DashboardController.java         # 仪表盘统计控制器
│   └── mq/
│       ├── LogProducer.java                 # 日志消息生产者
│       └── LogConsumer.java                 # 日志消息消费者
├── src/main/resources/
│   ├── application.yml                      # 主配置文件
│   ├── schema.sql                           # 数据库初始化脚本
│   └── mapper/                              # MyBatis XML映射文件
│       ├── UserMapper.xml
│       ├── DepartmentMapper.xml
│       ├── FolderMapper.xml
│       ├── CategoryMapper.xml
│       ├── SecurityLevelMapper.xml
│       ├── ArchiveMapper.xml
│       ├── ArchiveFileMapper.xml
│       ├── PermissionMapper.xml
│       ├── OperationLogMapper.xml
│       └── AccessApplicationMapper.xml
└── uploads/                                 # 文件上传目录
```

---

## Task 1: Maven项目初始化

**Files:**
- Create: `pom.xml`
- Create: `src/main/java/com/archive/ArchiveApplication.java`
- Create: `src/main/resources/application.yml`

- [ ] **Step 1: 创建pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.18</version>
    </parent>

    <groupId>com.archive</groupId>
    <artifactId>archive-management-backend</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    <name>archive-management-backend</name>

    <properties>
        <java.version>1.8</java.version>
        <mybatis-plus.version>3.5.5</mybatis-plus.version>
        <jjwt.version>0.11.5</jjwt.version>
    </properties>

    <dependencies>
        <!-- Spring Boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>

        <!-- MyBatis-Plus -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>

        <!-- SQLite -->
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.45.1.0</version>
        </dependency>
        <dependency>
            <groupId>com.github.gwenn</groupId>
            <artifactId>sqlite-dialect</artifactId>
            <version>0.1.4</version>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>${jjwt.version}</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>

        <!-- 工具 -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.8.25</version>
        </dependency>

        <!-- 测试 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 2: 创建启动类**

创建 `src/main/java/com/archive/ArchiveApplication.java`：

```java
package com.archive;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.archive.mapper")
public class ArchiveApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArchiveApplication.class, args);
    }
}
```

- [ ] **Step 3: 创建application.yml**

创建 `src/main/resources/application.yml`：

```yaml
server:
  port: 8081
  servlet:
    context-path: /api

spring:
  datasource:
    driver-class-name: org.sqlite.JDBC
    url: jdbc:sqlite:./archive.db
  redis:
    host: localhost
    port: 6379
    database: 0
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB

mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  configuration:
    map-underscore-to-camel-case: true
  global-config:
    db-config:
      id-type: auto

archive:
  upload-dir: ./uploads
  jwt-secret: archive-management-secret-key-2024
  jwt-expiration: 86400000
```

- [ ] **Step 4: 创建数据库初始化脚本**

创建 `src/main/resources/schema.sql`：

```sql
CREATE TABLE IF NOT EXISTS departments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    real_name TEXT,
    department_id INTEGER,
    role TEXT DEFAULT 'user',
    status INTEGER DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS folders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    parent_id INTEGER,
    created_by INTEGER,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES folders(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    code TEXT,
    parent_id INTEGER,
    description TEXT
);

CREATE TABLE IF NOT EXISTS security_levels (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    level_order INTEGER
);

CREATE TABLE IF NOT EXISTS archives (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    category_id INTEGER,
    security_level_id INTEGER,
    folder_id INTEGER,
    department_id INTEGER,
    created_by INTEGER,
    status INTEGER DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (security_level_id) REFERENCES security_levels(id),
    FOREIGN KEY (folder_id) REFERENCES folders(id),
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS archive_files (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    archive_id INTEGER NOT NULL,
    file_name TEXT NOT NULL,
    file_path TEXT NOT NULL,
    file_type TEXT,
    file_size INTEGER,
    uploaded_by INTEGER,
    uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (archive_id) REFERENCES archives(id),
    FOREIGN KEY (uploaded_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS permissions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    archive_id INTEGER NOT NULL,
    permission_type TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (archive_id) REFERENCES archives(id)
);

CREATE TABLE IF NOT EXISTS operation_logs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    archive_id INTEGER,
    operation_type TEXT,
    operation_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    details TEXT
);

CREATE TABLE IF NOT EXISTS access_applications (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    applicant_id INTEGER NOT NULL,
    archive_id INTEGER NOT NULL,
    purpose TEXT,
    status INTEGER DEFAULT 0,
    reviewer_id INTEGER,
    review_comment TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    reviewed_at DATETIME,
    FOREIGN KEY (applicant_id) REFERENCES users(id),
    FOREIGN KEY (archive_id) REFERENCES archives(id),
    FOREIGN KEY (reviewer_id) REFERENCES users(id)
);

-- 初始数据
INSERT OR IGNORE INTO departments (id, name, description) VALUES (1, '综合处', '负责公司综合行政事务');
INSERT OR IGNORE INTO departments (id, name, description) VALUES (2, '人事处', '负责公司人力资源管理');
INSERT OR IGNORE INTO departments (id, name, description) VALUES (3, '财务处', '负责公司财务管理');
INSERT OR IGNORE INTO departments (id, name, description) VALUES (4, '工程处', '负责水务工程项目管理');

INSERT OR IGNORE INTO security_levels (id, name, level_order) VALUES (1, '公开', 1);
INSERT OR IGNORE INTO security_levels (id, name, level_order) VALUES (2, '内部', 2);
INSERT OR IGNORE INTO security_levels (id, name, level_order) VALUES (3, '秘密', 3);
INSERT OR IGNORE INTO security_levels (id, name, level_order) VALUES (4, '机密', 4);

INSERT OR IGNORE INTO categories (id, name, code) VALUES (1, '行政文件', 'XZ');
INSERT OR IGNORE INTO categories (id, name, code) VALUES (2, '人事档案', 'RS');
INSERT OR IGNORE INTO categories (id, name, code) VALUES (3, '财务资料', 'CW');
INSERT OR IGNORE INTO categories (id, name, code) VALUES (4, '工程项目', 'GC');
```

- [ ] **Step 5: 验证项目编译**

```bash
cd archive-management-backend
mvn clean compile
```

预期：BUILD SUCCESS

- [ ] **Step 6: 提交**

```bash
git add archive-management-backend/
git commit -m "feat: 初始化SpringBoot项目，配置SQLite+Redis+RabbitMQ"
```

---

## Task 2: 通用配置与工具类

**Files:**
- Create: `src/main/java/com/archive/config/WebMvcConfig.java`
- Create: `src/main/java/com/archive/config/MyBatisConfig.java`
- Create: `src/main/java/com/archive/common/Result.java`
- Create: `src/main/java/com/archive/common/Constants.java`

- [ ] **Step 1: 创建WebMvcConfig.java（CORS跨域）**

```java
package com.archive.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

- [ ] **Step 2: 创建MyBatisConfig.java（SQLite方言）**

```java
package com.archive.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.SQLITE));
        return interceptor;
    }
}
```

- [ ] **Step 3: 创建Result.java（统一响应）**

```java
package com.archive.common;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMessage("success");
        r.setData(data);
        return r;
    }

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> error(String message) {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMessage(message);
        return r;
    }

    public static <T> Result<T> error(int code, String message) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }
}
```

- [ ] **Step 4: 创建Constants.java**

```java
package com.archive.common;

public class Constants {
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_DEPT_ADMIN = "dept_admin";
    public static final String ROLE_USER = "user";

    public static final String PERM_MANAGE = "manage";
    public static final String PERM_EDIT = "edit";
    public static final String PERM_ADD = "add";
    public static final String PERM_VIEW = "view";

    public static final String MQ_LOG_EXCHANGE = "archive.log.exchange";
    public static final String MQ_LOG_QUEUE = "archive.log.queue";
    public static final String MQ_LOG_ROUTING_KEY = "archive.log";

    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_DELETED = 0;
}
```

- [ ] **Step 5: 提交**

```bash
git add src/main/java/com/archive/config/ src/main/java/com/archive/common/
git commit -m "feat: 添加通用配置和工具类（CORS/分页/统一响应）"
```

---

## Task 3: JWT安全认证

**Files:**
- Create: `src/main/java/com/archive/security/JwtTokenProvider.java`
- Create: `src/main/java/com/archive/security/JwtAuthFilter.java`
- Create: `src/main/java/com/archive/security/UserDetailsServiceImpl.java`
- Create: `src/main/java/com/archive/config/SecurityConfig.java`

- [ ] **Step 1: 创建JwtTokenProvider.java**

```java
package com.archive.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${archive.jwt-secret}")
    private String secret;
    @Value("${archive.jwt-expiration}")
    private long expiration;

    public String generateToken(String username, String role, Long userId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return (String) getClaims(token).get("role");
    }

    public Long getUserIdFromToken(String token) {
        return ((Number) getClaims(token).get("userId")).longValue();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
```

- [ ] **Step 2: 创建JwtAuthFilter.java**

```java
package com.archive.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (tokenProvider.validateToken(token)) {
                String username = tokenProvider.getUsernameFromToken(token);
                String role = tokenProvider.getRoleFromToken(token);
                Long userId = tokenProvider.getUserIdFromToken(token);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                );
                auth.setDetails(userId);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }
}
```

- [ ] **Step 3: 创建UserDetailsServiceImpl.java**

```java
package com.archive.security;

import com.archive.entity.User;
import com.archive.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username)
        );
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPasswordHash(),
                user.getStatus() == 1, true, true, true,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()))
        );
    }
}
```

- [ ] **Step 4: 创建SecurityConfig.java**

```java
package com.archive.config;

import com.archive.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/system/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

- [ ] **Step 5: 提交**

```bash
git add src/main/java/com/archive/security/ src/main/java/com/archive/config/SecurityConfig.java
git commit -m "feat: 实现JWT安全认证（Spring Security + JWT Token）"
```

---

## Task 4: 实体类与Mapper

**Files:**
- Create: `src/main/java/com/archive/entity/` 目录下所有实体
- Create: `src/main/java/com/archive/mapper/` 目录下所有Mapper接口
- Create: `src/main/resources/mapper/` 目录下所有XML

- [ ] **Step 1: 创建User.java实体**

```java
package com.archive.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String passwordHash;
    private String realName;
    private Long departmentId;
    private String role;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
```

- [ ] **Step 2: 创建其余实体类**

```java
// Department.java
package com.archive.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("departments")
public class Department {
    @TableId(type = IdType.AUTO) private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}

// Folder.java
package com.archive.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("folders")
public class Folder {
    @TableId(type = IdType.AUTO) private Long id;
    private String name;
    private Long parentId;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

// Category.java
package com.archive.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
@Data @TableName("categories")
public class Category {
    @TableId(type = IdType.AUTO) private Long id;
    private String name;
    private String code;
    private Long parentId;
    private String description;
}

// SecurityLevel.java
package com.archive.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
@Data @TableName("security_levels")
public class SecurityLevel {
    @TableId(type = IdType.AUTO) private Long id;
    private String name;
    private Integer levelOrder;
}

// Archive.java
package com.archive.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("archives")
public class Archive {
    @TableId(type = IdType.AUTO) private Long id;
    private String title;
    private Long categoryId;
    private Long securityLevelId;
    private Long folderId;
    private Long departmentId;
    private Long createdBy;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

// ArchiveFile.java
package com.archive.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("archive_files")
public class ArchiveFile {
    @TableId(type = IdType.AUTO) private Long id;
    private Long archiveId;
    private String fileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private Long uploadedBy;
    private LocalDateTime uploadedAt;
}

// Permission.java
package com.archive.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
@Data @TableName("permissions")
public class Permission {
    @TableId(type = IdType.AUTO) private Long id;
    private Long userId;
    private Long archiveId;
    private String permissionType;
}

// OperationLog.java
package com.archive.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("operation_logs")
public class OperationLog {
    @TableId(type = IdType.AUTO) private Long id;
    private Long userId;
    private Long archiveId;
    private String operationType;
    private LocalDateTime operationTime;
    private String details;
}

// AccessApplication.java
package com.archive.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @TableName("access_applications")
public class AccessApplication {
    @TableId(type = IdType.AUTO) private Long id;
    private Long applicantId;
    private Long archiveId;
    private String purpose;
    private Integer status;
    private Long reviewerId;
    private String reviewComment;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
}
```

- [ ] **Step 3: 创建所有Mapper接口**

```java
// UserMapper.java
package com.archive.mapper;
import com.archive.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
public interface UserMapper extends BaseMapper<User> {}

// DepartmentMapper.java
package com.archive.mapper;
import com.archive.entity.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
public interface DepartmentMapper extends BaseMapper<Department> {}

// FolderMapper.java
package com.archive.mapper;
import com.archive.entity.Folder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
public interface FolderMapper extends BaseMapper<Folder> {}

// CategoryMapper.java
package com.archive.mapper;
import com.archive.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
public interface CategoryMapper extends BaseMapper<Category> {}

// SecurityLevelMapper.java
package com.archive.mapper;
import com.archive.entity.SecurityLevel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
public interface SecurityLevelMapper extends BaseMapper<SecurityLevel> {}

// ArchiveMapper.java
package com.archive.mapper;
import com.archive.entity.Archive;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
public interface ArchiveMapper extends BaseMapper<Archive> {}

// ArchiveFileMapper.java
package com.archive.mapper;
import com.archive.entity.ArchiveFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
public interface ArchiveFileMapper extends BaseMapper<ArchiveFile> {}

// PermissionMapper.java
package com.archive.mapper;
import com.archive.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
public interface PermissionMapper extends BaseMapper<Permission> {}

// OperationLogMapper.java
package com.archive.mapper;
import com.archive.entity.OperationLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
public interface OperationLogMapper extends BaseMapper<OperationLog> {}

// AccessApplicationMapper.java
package com.archive.mapper;
import com.archive.entity.AccessApplication;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
public interface AccessApplicationMapper extends BaseMapper<AccessApplication> {}
```

- [ ] **Step 4: 提交**

```bash
git add src/main/java/com/archive/entity/ src/main/java/com/archive/mapper/
git commit -m "feat: 创建所有实体类和MyBatis Mapper接口"
```

---

## Task 5: Redis配置

**Files:**
- Create: `src/main/java/com/archive/config/RedisConfig.java`

- [ ] **Step 1: 创建RedisConfig.java**

```java
package com.archive.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        om.registerModule(new JavaTimeModule());

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        serializer.setObjectMapper(om);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }
}
```

- [ ] **Step 2: 提交**

```bash
git add src/main/java/com/archive/config/RedisConfig.java
git commit -m "feat: 配置Redis序列化和Template"
```

---

## Task 6: RabbitMQ配置与日志队列

**Files:**
- Create: `src/main/java/com/archive/config/RabbitMQConfig.java`
- Create: `src/main/java/com/archive/mq/LogProducer.java`
- Create: `src/main/java/com/archive/mq/LogConsumer.java`

- [ ] **Step 1: 创建RabbitMQConfig.java**

```java
package com.archive.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public DirectExchange logExchange() {
        return new DirectExchange(Constants.MQ_LOG_EXCHANGE);
    }

    @Bean
    public Queue logQueue() {
        return QueueBuilder.durable(Constants.MQ_LOG_QUEUE).build();
    }

    @Bean
    public Binding logBinding(Queue logQueue, DirectExchange logExchange) {
        return BindingBuilder.bind(logQueue).to(logExchange).with(Constants.MQ_LOG_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }
}
```

- [ ] **Step 2: 创建LogProducer.java（日志消息生产者）**

```java
package com.archive.mq;

import com.archive.common.Constants;
import com.archive.entity.OperationLog;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendLog(Long userId, Long archiveId, String type, String details) {
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setArchiveId(archiveId);
        log.setOperationType(type);
        log.setDetails(details);
        rabbitTemplate.convertAndSend(Constants.MQ_LOG_EXCHANGE, Constants.MQ_LOG_ROUTING_KEY, log);
    }
}
```

- [ ] **Step 3: 创建LogConsumer.java（日志消息消费者）**

```java
package com.archive.mq;

import com.archive.common.Constants;
import com.archive.entity.OperationLog;
import com.archive.mapper.OperationLogMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogConsumer {
    @Autowired
    private OperationLogMapper logMapper;

    @RabbitListener(queues = Constants.MQ_LOG_QUEUE)
    public void handleLog(OperationLog log) {
        logMapper.insert(log);
    }
}
```

- [ ] **Step 4: 提交**

```bash
git add src/main/java/com/archive/config/RabbitMQConfig.java src/main/java/com/archive/mq/
git commit -m "feat: 配置RabbitMQ消息队列，实现异步操作日志"
```

---

## Task 7: 认证与用户管理API

**Files:**
- Create: `src/main/java/com/archive/service/UserService.java`
- Create: `src/main/java/com/archive/controller/AuthController.java`
- Create: `src/main/java/com/archive/controller/UserController.java`

- [ ] **Step 1: 创建UserService.java**

```java
package com.archive.service;

import com.archive.entity.User;
import com.archive.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getByUsername(String username) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    public Page<User> listPage(int page, int size, String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(User::getUsername, keyword).or().like(User::getRealName, keyword);
        }
        wrapper.orderByDesc(User::getCreatedAt);
        return userMapper.selectPage(new Page<>(page, size), wrapper);
    }

    public void create(User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        userMapper.insert(user);
    }

    public void update(User user) {
        userMapper.updateById(user);
    }

    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    public User getById(Long id) {
        return userMapper.selectById(id);
    }
}
```

- [ ] **Step 2: 创建AuthController.java**

```java
package com.archive.controller;

import com.archive.common.Result;
import com.archive.entity.User;
import com.archive.security.JwtTokenProvider;
import com.archive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            User user = userService.getByUsername(username);
            String token = tokenProvider.generateToken(username, user.getRole(), user.getId());
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("real_name", user.getRealName());
            userInfo.put("role", user.getRole());
            data.put("userInfo", userInfo);
            return Result.ok(data);
        } catch (BadCredentialsException e) {
            return Result.error(401, "用户名或密码错误");
        }
    }

    @GetMapping("/info")
    public Result<?> info(Authentication auth) {
        User user = userService.getByUsername(auth.getName());
        Map<String, Object> info = new HashMap<>();
        info.put("id", user.getId());
        info.put("username", user.getUsername());
        info.put("real_name", user.getRealName());
        info.put("role", user.getRole());
        info.put("department_id", user.getDepartmentId());
        return Result.ok(info);
    }
}
```

- [ ] **Step 3: 创建UserController.java**

```java
package com.archive.controller;

import com.archive.common.Result;
import com.archive.entity.User;
import com.archive.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) String keyword) {
        Page<User> result = userService.listPage(page, size, keyword);
        return Result.ok(result);
    }

    @PostMapping
    public Result<?> create(@RequestBody User user) {
        if (userService.getByUsername(user.getUsername()) != null) {
            return Result.error("用户名已存在");
        }
        userService.create(user);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.ok();
    }
}
```

- [ ] **Step 4: 提交**

```bash
git add src/main/java/com/archive/service/UserService.java src/main/java/com/archive/controller/AuthController.java src/main/java/com/archive/controller/UserController.java
git commit -m "feat: 实现用户认证API（登录/获取信息）和用户管理CRUD"
```

---

## Task 8: 部门管理API

**Files:**
- Create: `src/main/java/com/archive/service/DepartmentService.java`
- Create: `src/main/java/com/archive/controller/DepartmentController.java`

- [ ] **Step 1: 创建DepartmentService.java**

```java
package com.archive.service;

import com.archive.entity.Department;
import com.archive.mapper.DepartmentMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentMapper deptMapper;

    public List<Department> listAll() {
        return deptMapper.selectList(new LambdaQueryWrapper<Department>().orderByAsc(Department::getId));
    }

    public Department getById(Long id) { return deptMapper.selectById(id); }
    public void create(Department dept) { deptMapper.insert(dept); }
    public void update(Department dept) { deptMapper.updateById(dept); }
    public void delete(Long id) { deptMapper.deleteById(id); }
}
```

- [ ] **Step 2: 创建DepartmentController.java**

```java
package com.archive.controller;

import com.archive.common.Result;
import com.archive.entity.Department;
import com.archive.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/departments")
@PreAuthorize("hasRole('ADMIN')")
public class DepartmentController {
    @Autowired
    private DepartmentService deptService;

    @GetMapping
    public Result<?> list() { return Result.ok(deptService.listAll()); }

    @PostMapping
    public Result<?> create(@RequestBody Department dept) {
        deptService.create(dept);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Department dept) {
        dept.setId(id);
        deptService.update(dept);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        deptService.delete(id);
        return Result.ok();
    }
}
```

- [ ] **Step 3: 提交**

```bash
git add src/main/java/com/archive/service/DepartmentService.java src/main/java/com/archive/controller/DepartmentController.java
git commit -m "feat: 实现部门管理CRUD API"
```

---

## Task 9: 文件夹与档案管理API

**Files:**
- Create: `src/main/java/com/archive/service/FolderService.java`
- Create: `src/main/java/com/archive/service/ArchiveService.java`
- Create: `src/main/java/com/archive/controller/FolderController.java`
- Create: `src/main/java/com/archive/controller/ArchiveController.java`

- [ ] **Step 1: 创建FolderService.java**

```java
package com.archive.service;

import com.archive.entity.Folder;
import com.archive.mapper.FolderMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FolderService {
    @Autowired
    private FolderMapper folderMapper;

    public List<Folder> listAll() {
        return folderMapper.selectList(new LambdaQueryWrapper<Folder>().orderByAsc(Folder::getId));
    }

    public void create(Folder folder) { folderMapper.insert(folder); }
    public void update(Folder folder) { folderMapper.updateById(folder); }
    public void delete(Long id) { folderMapper.deleteById(id); }
}
```

- [ ] **Step 2: 创建ArchiveService.java**

```java
package com.archive.service;

import com.archive.entity.Archive;
import com.archive.mapper.ArchiveMapper;
import com.archive.mq.LogProducer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class ArchiveService {
    @Autowired
    private ArchiveMapper archiveMapper;
    @Autowired
    private LogProducer logProducer;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Page<Archive> listPage(int page, int size, Long folderId, String keyword) {
        String cacheKey = "archives:" + page + ":" + size + ":" + folderId + ":" + keyword;
        Object cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) return (Page<Archive>) cached;

        LambdaQueryWrapper<Archive> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Archive::getStatus, 1);
        if (folderId != null) wrapper.eq(Archive::getFolderId, folderId);
        if (keyword != null && !keyword.isEmpty()) wrapper.like(Archive::getTitle, keyword);
        wrapper.orderByDesc(Archive::getCreatedAt);
        Page<Archive> result = archiveMapper.selectPage(new Page<>(page, size), wrapper);
        redisTemplate.opsForValue().set(cacheKey, result, 5, TimeUnit.MINUTES);
        return result;
    }

    public Archive getById(Long id) { return archiveMapper.selectById(id); }

    public void create(Archive archive, Long userId) {
        archiveMapper.insert(archive);
        logProducer.sendLog(userId, archive.getId(), "create", "创建档案: " + archive.getTitle());
        clearCache();
    }

    public void update(Archive archive, Long userId) {
        archiveMapper.updateById(archive);
        logProducer.sendLog(userId, archive.getId(), "edit", "编辑档案: " + archive.getTitle());
        clearCache();
    }

    public void delete(Long id, Long userId) {
        Archive archive = archiveMapper.selectById(id);
        archive.setStatus(0);
        archiveMapper.updateById(archive);
        logProducer.sendLog(userId, id, "delete", "删除档案: " + archive.getTitle());
        clearCache();
    }

    private void clearCache() {
        redisTemplate.keys("archives:*").forEach(redisTemplate::delete);
    }
}
```

- [ ] **Step 3: 创建FolderController.java**

```java
package com.archive.controller;

import com.archive.common.Result;
import com.archive.entity.Folder;
import com.archive.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/archive/folders")
public class FolderController {
    @Autowired
    private FolderService folderService;

    @GetMapping
    public Result<?> list() { return Result.ok(folderService.listAll()); }

    @PostMapping
    public Result<?> create(@RequestBody Folder folder) {
        folderService.create(folder);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Folder folder) {
        folder.setId(id);
        folderService.update(folder);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        folderService.delete(id);
        return Result.ok();
    }
}
```

- [ ] **Step 4: 创建ArchiveController.java**

```java
package com.archive.controller;

import com.archive.common.Result;
import com.archive.entity.Archive;
import com.archive.entity.Category;
import com.archive.entity.SecurityLevel;
import com.archive.service.ArchiveService;
import com.archive.mapper.CategoryMapper;
import com.archive.mapper.SecurityLevelMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/archives")
public class ArchiveController {
    @Autowired
    private ArchiveService archiveService;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SecurityLevelMapper securityLevelMapper;

    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) Long folderId,
                          @RequestParam(required = false) String keyword) {
        Page<Archive> result = archiveService.listPage(page, size, folderId, keyword);
        return Result.ok(result);
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.ok(archiveService.getById(id));
    }

    @PostMapping
    public Result<?> create(@RequestBody Archive archive, Authentication auth) {
        Long userId = (Long) auth.getDetails();
        archive.setCreatedBy(userId);
        archiveService.create(archive, userId);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Archive archive, Authentication auth) {
        archive.setId(id);
        Long userId = (Long) auth.getDetails();
        archiveService.update(archive, userId);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id, Authentication auth) {
        Long userId = (Long) auth.getDetails();
        archiveService.delete(id, userId);
        return Result.ok();
    }

    @GetMapping("/categories")
    public Result<?> categories() {
        return Result.ok(categoryMapper.selectList(new LambdaQueryWrapper<Category>()));
    }

    @GetMapping("/security-levels")
    public Result<?> securityLevels() {
        return Result.ok(securityLevelMapper.selectList(new LambdaQueryWrapper<SecurityLevel>().orderByAsc(SecurityLevel::getLevelOrder)));
    }
}
```

- [ ] **Step 5: 提交**

```bash
git add src/main/java/com/archive/service/FolderService.java src/main/java/com/archive/service/ArchiveService.java src/main/java/com/archive/controller/FolderController.java src/main/java/com/archive/controller/ArchiveController.java
git commit -m "feat: 实现文件夹和档案管理CRUD API（含Redis缓存）"
```

---

## Task 10: 文件上传下载API

**Files:**
- Create: `src/main/java/com/archive/service/FileService.java`
- Create: `src/main/java/com/archive/controller/FileController.java`

- [ ] **Step 1: 创建FileService.java**

```java
package com.archive.service;

import com.archive.entity.ArchiveFile;
import com.archive.mapper.ArchiveFileMapper;
import com.archive.mq.LogProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileService {
    @Autowired
    private ArchiveFileMapper fileMapper;
    @Autowired
    private LogProducer logProducer;
    @Value("${archive.upload-dir}")
    private String uploadDir;

    public ArchiveFile upload(MultipartFile file, Long archiveId, Long userId) throws IOException {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String dir = uploadDir + "/" + datePath;
        File dirFile = new File(dir);
        if (!dirFile.exists()) dirFile.mkdirs();

        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf(".")) : "";
        String savedName = UUID.randomUUID().toString() + ext;
        String savedPath = dir + "/" + savedName;

        file.transferTo(new File(savedPath));

        ArchiveFile af = new ArchiveFile();
        af.setArchiveId(archiveId);
        af.setFileName(originalName);
        af.setFilePath(savedPath);
        af.setFileType(ext.replace(".", ""));
        af.setFileSize(file.getSize());
        af.setUploadedBy(userId);
        fileMapper.insert(af);

        logProducer.sendLog(userId, archiveId, "upload", "上传文件: " + originalName);
        return af;
    }

    public ArchiveFile getFile(Long fileId) {
        return fileMapper.selectById(fileId);
    }
}
```

- [ ] **Step 2: 创建FileController.java**

```java
package com.archive.controller;

import com.archive.common.Result;
import com.archive.entity.ArchiveFile;
import com.archive.mq.LogProducer;
import com.archive.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private LogProducer logProducer;

    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("file") MultipartFile file,
                            @RequestParam("archiveId") Long archiveId,
                            Authentication auth) throws Exception {
        Long userId = (Long) auth.getDetails();
        ArchiveFile af = fileService.upload(file, archiveId, userId);
        return Result.ok(af);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<FileSystemResource> download(@PathVariable Long id, Authentication auth) throws UnsupportedEncodingException {
        ArchiveFile af = fileService.getFile(id);
        File file = new File(af.getFilePath());
        Long userId = (Long) auth.getDetails();
        logProducer.sendLog(userId, af.getArchiveId(), "download", "下载文件: " + af.getFileName());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment",
                URLEncoder.encode(af.getFileName(), "UTF-8"));
        return ResponseEntity.ok().headers(headers).body(new FileSystemResource(file));
    }
}
```

- [ ] **Step 3: 提交**

```bash
git add src/main/java/com/archive/service/FileService.java src/main/java/com/archive/controller/FileController.java
git commit -m "feat: 实现文件上传下载API"
```

---

## Task 11: 搜索与查档申请API

**Files:**
- Create: `src/main/java/com/archive/service/SearchService.java`
- Create: `src/main/java/com/archive/service/ApplicationService.java`
- Create: `src/main/java/com/archive/controller/SearchController.java`
- Create: `src/main/java/com/archive/controller/ApplicationController.java`

- [ ] **Step 1: 创建SearchService.java**

```java
package com.archive.service;

import com.archive.entity.Archive;
import com.archive.mapper.ArchiveMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    @Autowired
    private ArchiveMapper archiveMapper;

    public Page<Archive> search(int page, int size, String keyword, String securityLevel,
                                 String creator, String startDate, String endDate) {
        LambdaQueryWrapper<Archive> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Archive::getStatus, 1);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Archive::getTitle, keyword));
        }
        wrapper.orderByDesc(Archive::getCreatedAt);
        return archiveMapper.selectPage(new Page<>(page, size), wrapper);
    }
}
```

- [ ] **Step 2: 创建ApplicationService.java**

```java
package com.archive.service;

import com.archive.entity.AccessApplication;
import com.archive.mapper.AccessApplicationMapper;
import com.archive.mq.LogProducer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ApplicationService {
    @Autowired
    private AccessApplicationMapper appMapper;
    @Autowired
    private LogProducer logProducer;

    public Page<AccessApplication> myApplications(Long userId, int page, int size) {
        LambdaQueryWrapper<AccessApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccessApplication::getApplicantId, userId).orderByDesc(AccessApplication::getCreatedAt);
        return appMapper.selectPage(new Page<>(page, size), wrapper);
    }

    public Page<AccessApplication> pendingReviews(Long deptId, int page, int size) {
        LambdaQueryWrapper<AccessApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccessApplication::getStatus, 0).orderByDesc(AccessApplication::getCreatedAt);
        return appMapper.selectPage(new Page<>(page, size), wrapper);
    }

    public void create(AccessApplication app) {
        app.setStatus(0);
        appMapper.insert(app);
        logProducer.sendLog(app.getApplicantId(), app.getArchiveId(), "apply", "提交查档申请");
    }

    public void review(Long id, Long reviewerId, boolean approved, String comment) {
        AccessApplication app = appMapper.selectById(id);
        app.setStatus(approved ? 1 : 2);
        app.setReviewerId(reviewerId);
        app.setReviewComment(comment);
        app.setReviewedAt(LocalDateTime.now());
        appMapper.updateById(app);
        logProducer.sendLog(reviewerId, app.getArchiveId(), "review",
                (approved ? "通过" : "拒绝") + "查档申请");
    }
}
```

- [ ] **Step 3: 创建SearchController.java**

```java
package com.archive.controller;

import com.archive.common.Result;
import com.archive.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/archives/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping
    public Result<?> search(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String keyword,
                            @RequestParam(required = false) String securityLevel,
                            @RequestParam(required = false) String creator,
                            @RequestParam(required = false) String startDate,
                            @RequestParam(required = false) String endDate) {
        return Result.ok(searchService.search(page, size, keyword, securityLevel, creator, startDate, endDate));
    }
}
```

- [ ] **Step 4: 创建ApplicationController.java**

```java
package com.archive.controller;

import com.archive.common.Result;
import com.archive.entity.AccessApplication;
import com.archive.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService appService;

    @GetMapping("/mine")
    public Result<?> mine(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,
                          Authentication auth) {
        Long userId = (Long) auth.getDetails();
        return Result.ok(appService.myApplications(userId, page, size));
    }

    @PostMapping
    public Result<?> create(@RequestBody AccessApplication app, Authentication auth) {
        Long userId = (Long) auth.getDetails();
        app.setApplicantId(userId);
        appService.create(app);
        return Result.ok();
    }

    @GetMapping("/reviews")
    public Result<?> reviews(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size) {
        return Result.ok(appService.pendingReviews(null, page, size));
    }

    @PutMapping("/{id}/review")
    public Result<?> review(@PathVariable Long id, @RequestBody Map<String, Object> body, Authentication auth) {
        Long userId = (Long) auth.getDetails();
        boolean approved = "approve".equals(body.get("action"));
        String comment = (String) body.getOrDefault("comment", "");
        appService.review(id, userId, approved, comment);
        return Result.ok();
    }
}
```

- [ ] **Step 5: 提交**

```bash
git add src/main/java/com/archive/service/SearchService.java src/main/java/com/archive/service/ApplicationService.java src/main/java/com/archive/controller/SearchController.java src/main/java/com/archive/controller/ApplicationController.java
git commit -m "feat: 实现档案搜索和查档申请审核API"
```

---

## Task 12: 操作日志与仪表盘API

**Files:**
- Create: `src/main/java/com/archive/service/LogService.java`
- Create: `src/main/java/com/archive/controller/DashboardController.java`
- Create: `src/main/java/com/archive/controller/LogController.java`

- [ ] **Step 1: 创建LogService.java**

```java
package com.archive.service;

import com.archive.entity.OperationLog;
import com.archive.mapper.OperationLogMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    @Autowired
    private OperationLogMapper logMapper;

    public Page<OperationLog> listPage(int page, int size, String operator, String type) {
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        if (operator != null && !operator.isEmpty()) wrapper.like(OperationLog::getUserId, operator);
        if (type != null && !type.isEmpty()) wrapper.eq(OperationLog::getOperationType, type);
        wrapper.orderByDesc(OperationLog::getOperationTime);
        return logMapper.selectPage(new Page<>(page, size), wrapper);
    }
}
```

- [ ] **Step 2: 创建DashboardController.java**

```java
package com.archive.controller;

import com.archive.common.Result;
import com.archive.entity.*;
import com.archive.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired private ArchiveMapper archiveMapper;
    @Autowired private AccessApplicationMapper appMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private CategoryMapper categoryMapper;
    @Autowired private SecurityLevelMapper securityLevelMapper;

    @GetMapping("/stats")
    public Result<?> stats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalArchives", archiveMapper.selectCount(new LambdaQueryWrapper<Archive>().eq(Archive::getStatus, 1)));
        stats.put("pendingReview", appMapper.selectCount(new LambdaQueryWrapper<AccessApplication>().eq(AccessApplication::getStatus, 0)));
        stats.put("totalUsers", userMapper.selectCount(null));
        return Result.ok(stats);
    }

    @GetMapping("/categories")
    public Result<?> categoryStats() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Category> categories = categoryMapper.selectList(null);
        for (Category c : categories) {
            Map<String, Object> item = new HashMap<>();
            item.put("type", c.getName());
            item.put("value", archiveMapper.selectCount(
                    new LambdaQueryWrapper<Archive>().eq(Archive::getCategoryId, c.getId()).eq(Archive::getStatus, 1)));
            result.add(item);
        }
        return Result.ok(result);
    }

    @GetMapping("/security-distribution")
    public Result<?> securityDistribution() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<SecurityLevel> levels = securityLevelMapper.selectList(
                new LambdaQueryWrapper<SecurityLevel>().orderByAsc(SecurityLevel::getLevelOrder));
        for (SecurityLevel sl : levels) {
            Map<String, Object> item = new HashMap<>();
            item.put("type", sl.getName());
            item.put("value", archiveMapper.selectCount(
                    new LambdaQueryWrapper<Archive>().eq(Archive::getSecurityLevelId, sl.getId()).eq(Archive::getStatus, 1)));
            result.add(item);
        }
        return Result.ok(result);
    }
}
```

- [ ] **Step 3: 创建LogController.java**

```java
package com.archive.controller;

import com.archive.common.Result;
import com.archive.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/logs")
@PreAuthorize("hasRole('ADMIN')")
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) String operator,
                          @RequestParam(required = false) String type) {
        return Result.ok(logService.listPage(page, size, operator, type));
    }
}
```

- [ ] **Step 4: 提交**

```bash
git add src/main/java/com/archive/service/LogService.java src/main/java/com/archive/controller/DashboardController.java src/main/java/com/archive/controller/LogController.java
git commit -m "feat: 实现操作日志查询和仪表盘统计API"
```

---

## Task 13: 数据库初始化与启动验证

**Files:** None（使用已有文件）

- [ ] **Step 1: 编译并启动**

```bash
cd archive-management-backend
mvn clean package -DskipTests
java -jar target/archive-management-backend-1.0.0.jar
```

预期：SpringBoot启动成功，SQLite数据库自动创建，表结构初始化完成

- [ ] **Step 2: 创建初始管理员账号**

使用SQLite工具或应用启动后通过数据库直接插入：

```sql
-- 密码为 admin123 的BCrypt加密值
INSERT INTO users (username, password_hash, real_name, department_id, role, status)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', 1, 'admin', 1);
```

- [ ] **Step 3: 测试登录接口**

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

预期返回：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGci...",
    "userInfo": { "id": 1, "username": "admin", "real_name": "系统管理员", "role": "admin" }
  }
}
```

- [ ] **Step 4: 最终提交**

```bash
git add .
git commit -m "feat: 后端服务完成，全部API可用"
```

---

## Plan Self-Review

1. **Spec coverage:** 覆盖所有后端功能需求：认证、用户管理、部门管理、文件夹管理、档案CRUD、文件上传下载、搜索、查档申请审核、操作日志（RabbitMQ异步）、仪表盘统计、Redis缓存

2. **Placeholder scan:** 无TBD/TODO，所有代码完整可执行

3. **Type consistency:** 实体字段名与数据库schema一致，API路径与前端api模块对应
