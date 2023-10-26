package com.example.demo;

import com.example.demo.ApiStats;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class ApiMonitoringAspect {
    // 用于存储不同API的统计数据，使用线程安全的ConcurrentHashMap
    private static Map<String, ApiStats> apiStatsMap = new ConcurrentHashMap<>();


        // ... 其他部分不变 ...

        // 静态方法用于获取 API 统计数据
        public static Map<String, ApiStats> getApiStatsMap() {
            return apiStatsMap;
        }



    // 定义切点，匹配com.example.controller包中的所有方法
    @Pointcut("execution(* com.example.demo.*.*(..))")
    public void apiMethods() {}

    // 在执行匹配的方法之前执行的通知方法
    @Before("apiMethods()")
    public void beforeApiCall(JoinPoint joinPoint) {
        // 获取被调用方法的名称
        String methodName = joinPoint.getSignature().toShortString();

        // 如果apiStatsMap中不包含此方法的统计数据，创建一个新的ApiStats对象并放入map中
        if (!apiStatsMap.containsKey(methodName)) {
            apiStatsMap.put(methodName, new ApiStats());
        }

        // 增加此方法的总调用次数
        apiStatsMap.get(methodName).incrementTotalCalls();

        // 记录API调用的开始时间
        apiStatsMap.get(methodName).recordStartTime();
    }

    // 在执行匹配的方法之后执行的通知方法
    @After("apiMethods()")
    public void afterApiCall(JoinPoint joinPoint) {
        // 获取被调用方法的名称
        String methodName = joinPoint.getSignature().toShortString();

        // 获取当前时间作为API调用结束时间
        long endTime = System.currentTimeMillis();

        // 获取API调用的开始时间
        long startTime = apiStatsMap.get(methodName).getStartTime();

        // 计算API响应时间
        long responseTime = endTime - startTime;

        // 更新此方法的统计数据，包括总响应时间、最长响应时间、最短响应时间
        apiStatsMap.get(methodName).updateResponseTime(responseTime);
    }

    // 在匹配的方法抛出异常时执行的通知方法
    @AfterThrowing(pointcut = "apiMethods()", throwing = "ex")
    public void afterApiException(JoinPoint joinPoint, Exception ex) {
        // 获取被调用方法的名称
        String methodName = joinPoint.getSignature().toShortString();

        // 增加此方法的异常次数
        apiStatsMap.get(methodName).incrementExceptionCount();
    }
}

