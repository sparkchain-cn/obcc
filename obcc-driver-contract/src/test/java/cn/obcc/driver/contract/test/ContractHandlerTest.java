package cn.obcc.driver.contract.test;

import cn.obcc.config.ObccConfig;
import cn.obcc.config.ExProps;
import cn.obcc.driver.contract.ContractHandler;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.ChainPipe;
import cn.obcc.driver.vo.CompileResult;
import cn.obcc.driver.vo.ContractRec;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.exception.enums.EContractType;
import cn.obcc.vo.driver.ContractInfo;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ContractHandlerTest {

    private ContractHandler handler;

    private String testSolContent = null;

    @BeforeClass
    public void before() throws Exception {
        handler = new ContractHandler() {
            @Override
            public ContractInfo getContract(String contractAddr) throws Exception {
                return null;
            }

            @Override
            public String doDeploy(ChainPipe pipe) throws Exception {
                return null;
            }

            @Override
            public String doInvoke(ChainPipe pipe) throws Exception {
                return null;
            }

            @Override
            protected void onDeploy(SrcAccount srcAccount, ContractInfo contractInfo, IUpchainFn fn, ExProps config) {
            }

            @Override
            public ContractRec parseTxInfo(ContractInfo contractInfo, String input) throws Exception {
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
        CompileResult compile = handler.doCompile(System.currentTimeMillis() + "", testSolContent, new ExProps());
        System.out.println(compile);
    }

}
