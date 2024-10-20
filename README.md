# 前言
本仓库只公开了部分代码。
# 项目介绍
一个社区项目，主要包括笔记发布、点赞、收藏、关注、关键字检索等功能。平台需要满足海量用户的高并发读写和数据一致性要求，确保用户操作的实时响应，并通过分布式架构实现高可用和高扩展性。
# 主要技术
- SpringBoot：简化Spring应用的初始搭建以及开发过程
- SpringCloud&SpringCloudAlibaba：基于Spring Boot实现的云原生应用开发工具
- MyBatis-Plus：持久层框架，也依赖mybatis
- Redis：内存做缓存
- MySQL：关系型数据库
- RabbitMQ：消息中间件；大型分布式项目是标配；分布式事务最终一致性
- Seata：分布式事务
- Drools：规则引擎，计算预估费用、取消费用等等
- XXL-JOB: 分布式定时任务调用中心，这里用来定时备份ES中的数据和进行一些聚合运算。
- Docker&Docker Compose：容器化技术; 生产环境Redis（运维人员）；快速搭建环境
- MinIO（私有化对象存储集群）：分布式文件存储 类似于OSS（公有）
- ES：搜索引擎，用于关键词搜索文章
- Canal：监听MySQL搭配MQ将数据增量同步到ES中
ThreadPoolExecutor+CompletableFuture：自定义线程池搭配异步编排CompletableFuture来执行批量任务或者耗时任务，从而降低耗时。

