package com.example.demo;


import lombok.Data;

@Data
public class ApiStats {
    private long totalCalls = 0;  // 记录API调用总次数
    private long totalResponseTime = 0;  // 记录所有API调用的总响应时间
    private long maxResponseTime = Long.MIN_VALUE;  // 记录最长的API响应时间，初始化为最小可能值
    private long minResponseTime = Long.MAX_VALUE;  // 记录最短的API响应时间，初始化为最大可能值
    private long exceptionCount = 0;  // 记录API调用中发生异常的次数
    private long startTime;  // 记录API调用的开始时间

    public void incrementTotalCalls() {
        totalCalls++;  // 每次调用增加总调用次数
    }

    public void incrementExceptionCount() {
        exceptionCount++;  // 每次发生异常增加异常次数
    }

    public void recordStartTime() {
        startTime = System.currentTimeMillis();  // 记录API调用的开始时间，使用当前时间戳
    }

    public void updateResponseTime(long responseTime) {
        totalResponseTime += responseTime;  // 增加总响应时间
        maxResponseTime = Math.max(maxResponseTime, responseTime);  // 更新最长响应时间
        minResponseTime = Math.min(minResponseTime, responseTime);  // 更新最短响应时间
    }



    // 这里通常会包含其他统计数据的Getter方法，例如获取总调用次数、总响应时间、最长响应时间、最短响应时间、异常次数的Getter方法。
}