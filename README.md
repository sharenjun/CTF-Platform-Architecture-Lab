# Project Aegis

Project Aegis 是一个长期维护的 Java CTF 平台项目。

这个项目的目标不是最快完成一个 Demo，而是构建一套可演进、可理解、可维护的软件系统，并在这个过程中把计算机基础、Java、软件架构、Linux、Docker 和分布式系统串联起来。

## 开发环境

1. Ubuntu
2. IDEA
3. JDK 21 LTS
4. Node.js（NVM 管理）
5. Docker
6. Docker Compose
7. PostgreSQL
8. Redis

## 项目原则

每增加一个功能，都要回答四个问题：

1. 业务上为什么需要它？
2. 为什么选择这个技术实现？
3. 这里运用了哪些 Java 知识？
4. 这些 Java 知识最终对应哪些计算机底层原理？

## 仓库结构

```text
ctf-platform/
├── docs/       # 路线图、架构、领域、API、学习笔记和架构决策
├── backend/    # Spring Boot 后端
├── frontend/   # Vue 3 前端
├── deploy/     # Docker、Nginx、Compose 和部署文件
├── scripts/    # 自动化脚本
├── tools/      # 开发工具
└── tests/      # 集成测试和端到端测试
```
