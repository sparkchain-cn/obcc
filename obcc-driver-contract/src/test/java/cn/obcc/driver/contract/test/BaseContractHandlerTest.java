package cn.obcc.driver.contract.test;

import cn.obcc.config.ObccConfig;
import cn.obcc.config.ExConfig;
import cn.obcc.driver.contract.BaseContractHandler;
import cn.obcc.driver.vo.CompileResult;
import cn.obcc.driver.vo.ContractRec;
import cn.obcc.exception.enums.EContractType;
import cn.obcc.vo.driver.ContractInfo;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class BaseContractHandlerTest {

    private BaseContractHandler handler;

    private String testSolContent = null;

    @BeforeClass
    public void before() throws Exception {
        handler = new BaseContractHandler() {
            @Override
            protected Map<String, String> buildMethodNameIdMap(ContractInfo contractInfo) {
                return null;
            }

            @Override
            public ContractInfo getContract(String contractAddr) throws Exception {
                return null;
            }

            @Override
            public String query(String srcAddr, ContractInfo contractInfo, ExConfig config, String methodName, List params) throws Exception {
                return null;
            }


            @Override
            public ContractRec parseTxInfo(ContractInfo contractInfo, String input) throws Exception {
                return null;
            }

            @Override
            public String getInvokeHexData(String abi, String fnName, List inputValues) throws Exception {
                return null;
            }

            @Override
            public String encodeConstructor(String abi, List params) throws Exception {
                return null;
            }
        };
        ObccConfig config = new ObccConfig();
        config.setContractType(EContractType.SOLC);
        handler.initObccConfig(config, null);

        //读取sol文件(读取合约内容)
        StringBuilder builder = new StringBuilder();
        InputStream resourceAsStream = BaseContractHandlerTest.class.getClassLoader().getResourceAsStream("Test.sol");
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
        CompileResult compile = handler.compile(System.currentTimeMillis() + "", testSolContent, new ExConfig());
        System.out.println(compile);
    }

}
