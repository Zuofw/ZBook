//package com.zuofw.zuofwgateway.auth;
//
//import cn.dev33.satoken.context.SaHolder;
//import cn.dev33.satoken.reactor.filter.SaReactorFilter;
//import cn.dev33.satoken.router.SaRouter;
//import cn.dev33.satoken.stp.StpUtil;
//import cn.dev33.satoken.util.SaResult;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author: 犬小哈
// * @date: 2024/6/13 14:48
// * @version: v1.0.0
// * @description: [Sa-Token 权限认证] 配置类
// **/
//@Configuration
//public class SaTokenConfigure {
//    // 注册 Sa-Token全局过滤器
//    @Bean
//    public SaReactorFilter getSaReactorFilter() {
//        return new SaReactorFilter()
//                // 拦截地址
//                .addInclude("/**")    /* 拦截全部path */
//                // 鉴权方法：每次访问进入
//                .setAuth(obj -> {
//                    // 登录校验
//                    SaRouter.match("/**") // 拦截所有路由
//                            .notMatch("/auth/admin/login") // 排除登录接口，这里只是懒得改user了，而且admin和user都是走这个登录的
//                            .check(r -> StpUtil.checkLogin()) // 校验是否登录
//                    ;
//
//                    // 权限认证 -- 不同模块, 校验不同权限
//                    // SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
//                    // SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
//                    // SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
//                    // SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
//
//                    // 更多匹配 ...  */
//                })
//                // 异常处理方法：每次setAuth函数出现异常时进入
//                .setError(e -> {
//                    return SaResult.error(e.getMessage());
//                })
//                ;
//    }
//}
//
