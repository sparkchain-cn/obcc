package cn.obcc.driver.contract.solc.test;

import cn.obcc.driver.contract.solc.core.AbiParser;
import cn.obcc.driver.contract.solc.core.SolcCompiler;
import cn.obcc.driver.contract.solc.vo.ContractInfo;
import cn.obcc.driver.vo.ContractBin;
import cn.obcc.utils.FileUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TestContract
 *
 * @author ecasona
 * @version 1.4
 * @date 2019/8/27 13:37
 * @details 合约编译
 */

public class TestContractCompiler {
    private String testSolContent = null;

    @Before
    public void before() {
        //读取sol文件(读取合约内容)
        StringBuilder builder = new StringBuilder();
        InputStream resourceAsStream = TestContractCompiler.class.getClassLoader().getResourceAsStream("Test.sol");
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

    private ContractInfo compile() {
        String source = testSolContent;
        //solc  编译文件路径
        String solcPath = "E:\\_eth_solid\\run\\";
        //编译缓存路径
        String tempPath = FileUtils.getRootPath() + "\\result\\";
        ContractInfo vo = SolcCompiler.compile(source, solcPath, tempPath, true);

        System.out.println(vo.toString());
        return vo;
    }

    @Test
    public void compileSol() {
        compile();
    }

    @Test
    public void getFunctions() {
        parseAbi();
    }

    private List<String> parseAbi() {
        ContractInfo vo = compile();

        ContractBin ecas = vo.getMap().get("ecas");
        System.out.println(ecas.toString());

        List<String> methods = new ArrayList<>();

        JSONArray jsonArray = JSON.parseArray(ecas.getAbi());
        for (Object o : jsonArray) {
            Map<String, Object> function = (Map<String, Object>) o;
            String name = function.get("name") + "";
            System.out.println(name);
            methods.add(name);
        }

        return methods;
    }

    @Test
    public void getAbiFuncInSize() {
        for (String m : parseAbi()) {
            int ecas = AbiParser.abiFuncInSize(compile().getMap().get("ecas").getAbi(), m);
            System.out.println(String.format("mehtod:%s has %d parameters", m, ecas));
        }
    }

}
