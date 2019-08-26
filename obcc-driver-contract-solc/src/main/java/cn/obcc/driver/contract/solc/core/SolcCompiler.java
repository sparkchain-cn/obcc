package cn.obcc.driver.contract.solc.core;

import cn.obcc.driver.contract.solc.vo.ContractInfo;
import cn.obcc.driver.vo.ContractBin;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.utils.shell.ShellUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class SolcCompiler {
    public static final Logger logger = LoggerFactory.getLogger(SolcCompiler.class);
    //region contants
//    static String solcPath = "E:\\work\\Research\\solidity\\solc\\run\\";
//    static String tempPath = "E:\\work\\Research\\solidity\\solc\\run\\result\\";
    final static String SolcFile = "solc.exe";
    final static String SolExtension = ".sol";
    final static String BinExtension = ".bin";
    final static String AbiExtension = ".abi";
    //endregion

    private static File newFilePath(String tempPath) {
        File path;
        path = new File(tempPath);
        if (!path.exists()) {
            path.mkdir();
        }
        return path;
    }

    private static void createSolFile(String solFilePath, String sourceCode) {
        //save sol file
        try {
            saveFile(solFilePath, sourceCode);
        } catch (
                Exception exception) {
            System.out.println(exception);
        }

    }

    private static void compile(String solcCmdPath, String solFilePath, String resultPath, ContractInfo vo) {
        try {
            String compileResult = ShellUtils.runShell(solcCmdPath + " " + solFilePath
                    + " --bin --abi --overwrite --optimize -o " + resultPath);
            System.out.println(compileResult);
            vo.setCompileResult(compileResult);
        } catch (Exception exception) {
            System.out.println(exception);
            vo.setCompileResult(StringUtils.exception(exception));
        }

    }

    public static ContractInfo compile(String sourceCode, String solcPath, String tempPath, Boolean deleteTemp) {
        //output
        ContractInfo vo = new ContractInfo();
        // Map<String, Object> result = new HashMap<>();
        // result.put("source", sourceCode);
        vo.setSource(sourceCode);

        String fileNamePrefix = getFileName();
        String solcCmdPath = solcPath + SolcFile;
        String resultPath = tempPath + fileNamePrefix + "\\";
        String solFilePath = tempPath + fileNamePrefix + SolExtension;

        //create temp dir
        File tempFolder = newFilePath(tempPath);
        //create result path
        File resultFolder = newFilePath(resultPath);

        //define sol file name
        createSolFile(solFilePath, sourceCode);
        //compile sol file
        compile(solcCmdPath, solFilePath, resultPath, vo);


        //read all bin and abi file
        String[] resultFileNames = resultFolder.list();
        List<String> contractList = getContractList(resultFileNames);
        contractList.forEach(contract -> {
            vo.getMap().put(contract, readContent(contract, resultPath));
        });

        //remove temp files
        if (deleteTemp) {
            File path = new File(tempPath);
            delFile(path);
        }

        return vo;
    }

    //region utilities
    private static ContractBin readContent(String contractName, String path) {
        Map<String, String> content = new HashMap<String, String>();

        ContractBin vo = new ContractBin();
        String filePath = path + contractName + BinExtension;
        String binContent = readFile(filePath);
        //content.put("binary", file);
        vo.setBinary(binContent);
        filePath = path + contractName + AbiExtension;
        String abiContent = readFile(filePath);
        //  content.put("abi", abiContent);
        vo.setAbi(abiContent);
        return vo;
        // return content;
    }

    private static List<String> getContractList(String[] fileList) {
        List<String> list = new ArrayList<>();

        for (String fileName : fileList) {
            String name = "";
            if (fileName.endsWith(BinExtension)) {
                name = fileName.substring(0, fileName.length() - BinExtension.length());
            } else if (fileName.endsWith(AbiExtension)) {
                name = fileName.substring(0, fileName.length() - AbiExtension.length());
            }

            if (!name.isEmpty() && !list.contains(name)) {
                list.add(name);
            }
        }

        return list;
    }

    private static String getFileName() {
        return (new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")).format(new Date());
    }

    public static void saveFile(String filePath, String content) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        fw.write(content);
        fw.close();
    }

    public static String readFile(String filePath) {
        String file = "";
        try {
            FileReader reader = new FileReader(filePath);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                file = file + line;
            }
            br.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }
    //endregion

}
