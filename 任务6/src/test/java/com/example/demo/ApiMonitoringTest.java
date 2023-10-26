package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApiMonitoringApplication.class)
public class ApiMonitoringTest {
    @Autowired
    private SampleController sampleController;

    @Autowired
    private ApiMonitoringAspect apiMonitoringAspect;

    @Test
    public void testSampleApi() {
        // 测试目的：验证 sampleApi 方法的正常执行和监控统计

        // 发起 API 请求
        String response = sampleController.sampleApi();

        // 验证 API 响应是否符合预期
        assertEquals("Sample API Response", response);

        // 获取 API 统计数据
        Map<String, ApiStats> apiStatsMap = apiMonitoringAspect.getApiStatsMap();

        // 验证 API 统计数据是否正确
        ApiStats sampleApiStats = apiStatsMap.values().iterator().next();
        assertNotNull(sampleApiStats);
        assertEquals(1, sampleApiStats.getTotalCalls()); // 预期总调用次数为1
        assertTrue(sampleApiStats.getTotalResponseTime() > 0); // 预期总响应时间大于0
        assertTrue(sampleApiStats.getMaxResponseTime() > 0); // 预期最长响应时间大于0
        assertTrue(sampleApiStats.getMinResponseTime() > 0); // 预期最短响应时间大于0
        assertEquals(0, sampleApiStats.getExceptionCount()); // 预期异常次数为0
    }
}
