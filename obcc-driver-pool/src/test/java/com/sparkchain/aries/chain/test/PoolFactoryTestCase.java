//package com.sparkchain.chain.test;
//
//import com.blink.jtblc.client.Remote;
//import com.blink.jtblc.client.bean.LedgerInfo;
//import com.sparkchain.chain.jingtum.JingtumBuilder;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.Random;
//
//import static JintumConfigTest.getPoolConfig;
//
//@RunWith(SpringRunner.class)
//@ActiveProfiles("unittest")
//@SpringBootTest(classes = PoolFactoryTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@ContextConfiguration(classes = PoolFactoryTestApplication.class)
////@TestPropertySource(properties = "spring.cloud.discovery.enabled=false")
//public class PoolFactoryTestCase {
//
//    Random random = new Random();
//    private MockMvc mockMvc;
//    @Autowired
//    private WebApplicationContext context;
//
//    @Before
//    public void setupMockMvc() throws Exception {
//        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//        //this.testCommon = new TestCommon(mockMvc);
//        //testCommon.clearAllData();
//    }
//
//
//    @Test
//    public void requestLedgerInfo() throws Exception {
//
//        int size = 1000;
//        for (int i = 0; i < size; i++) {
//            Remote remote = JingtumBuilder.getClient("jingtum", getPoolConfig("jingtum"));
//
//            LedgerInfo info = remote.requestLedgerInfo();
//          //  System.out.println(JSON.toJSONString(info));
//
//        }
//
//    }
//
//
//    @Before
//    public void destory() throws Exception {
//        // testCommon.clearAllData();
//    }
//}
//
//
//
//// @BeforeClass – 表示在类中的任意public static void方法执行之前执行
//// @AfterClass – 表示在类中的任意public static void方法执行之后执行
//// @Before – 表示在任意使用@Test注解标注的public void方法执行之前执行
//// @After – 表示在任意使用@Test注解标注的public void方法执行之后执行
//// @Test – 使用该注解标注的public void方法会表示为一个测试方法
