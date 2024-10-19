//package com.zuofw.zuofwgateway.auth;
//
//import cn.dev33.satoken.stp.StpInterface;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//import java.util.List;
//
///**
// * @author: zuofw
// * @date: 2024/4/5 18:04
// * @version: v1.0.0
// * @description: 自定义权限验证接口扩展
// **/
///**
// * 自定义权限验证接口扩展
// * checkPermission("user") , 检查是否拥有 user 标识符的权限。SaToken 实际上会主动调用 StpInterfaceImpl.getPermissionList() 方法
// * 在 SaToken 框架中，是通过 StpInterface 的实现类来拉取相关数据的，即之前创建的 StpInterfaceImpl 类。
// */
//
//@Component
//public class StpInterfaceImpl implements StpInterface {
//
//    @Override
//    public List<String> getPermissionList(Object loginId, String loginType) {
//        // 返回此 loginId 拥有的权限列表
//        System.out.println("权限");
//        // todo 从 redis 获取
//
//        return Collections.emptyList();
//    }
//
//    @Override
//    public List<String> getRoleList(Object loginId, String loginType) {
//        // 返回此 loginId 拥有的角色列表
//
//        // todo 从 redis 获取
//
//        return Collections.emptyList();
//    }
//
//}
