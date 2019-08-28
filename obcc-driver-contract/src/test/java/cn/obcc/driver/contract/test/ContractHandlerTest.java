package cn.obcc.driver.contract.test;

import cn.obcc.config.ObccConfig;
import cn.obcc.config.ReqConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.IChainHandler;
import cn.obcc.driver.contract.ContractHandler;
import cn.obcc.exception.enums.EContractType;
import cn.obcc.vo.RetData;
import cn.obcc.vo.driver.ContractInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ContractHandlerTest {

    private ContractHandler handler;

    private String testSolContent = null;

    @Before
    public void before() {
        handler = new ContractHandler() {
            @Override
            protected ContractInfo deploy(String abi, String bin, ReqConfig config) {
                return null;
            }
        };
        ObccConfig config = new ObccConfig();
        config.setContractType(EContractType.SOLC);
        handler.initObccConfig(config, null);

        //读取sol文件(读取合约内容)
        StringBuilder builder = new StringBuilder();
        InputStream resourceAsStream = ContractHandlerTest.class.getClassLoader().getResourceAsStream("Test.sol");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (resourceAsStream == null) {
            System.out.println("Do not read file : Test.sol");
        } else {
            System.out.println("Read File successfully!");
        }
        try {
            int i;
            assert resourceAsStream != null;
            while ((i = resourceAsStream.read()) != -1) {
                baos.write(i);
            }
            builder.append(baos.toString());
            testSolContent = builder.toString();
            System.out.println(testSolContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testCompiler() throws Exception {
        RetData compile = handler.compile(System.currentTimeMillis() + "", testSolContent, new ReqConfig());
        System.out.println(compile);
    }

}
