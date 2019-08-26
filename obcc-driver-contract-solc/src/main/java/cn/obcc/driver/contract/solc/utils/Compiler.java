package cn.obcc.driver.contract.solc.utils;

import cn.obcc.utils.shell.ShellUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Compiler {
    //region contants
//    static String solcPath = "E:\\work\\Research\\solidity\\solc\\run\\";
//    static String tempPath = "E:\\work\\Research\\solidity\\solc\\run\\result\\";
    final static String SolcFile = "solc.exe";
    final static String SolExtension = ".sol";
    final static String BinExtension = ".bin";
    final static String AbiExtension = ".abi";
    //endregion

    public static Map<String, Object> compile(String sourceCode, String solcPath, String tempPath, Boolean deleteTemp){
        //output
        Map<String, Object> result = new HashMap<>();
        result.put("source", sourceCode);

        //create temp dir
        File path;
        path = new File(tempPath);
        if(!path.exists()){
            path.mkdir();
        }

        //define sol file name
        String fileNamePrefix = getFileName();

        //save sol file
        String solFilePath = tempPath + fileNamePrefix + SolExtension;
        try {
            saveFile(solFilePath, sourceCode);
        }
        catch(Exception exception){
            System.out.println(exception);
        }

        //create result path
        String resultPath = tempPath + fileNamePrefix + "\\";
        path = new File(resultPath);
        if (!path.exists()) {
            path.mkdirs();
        }

        //compile sol file
        String solcFilePath = solcPath + SolcFile;
        try {
            String compileResult = ShellUtils.runShell(solcFilePath + " " + solFilePath
                    + " --bin --abi --overwrite --optimize -o " + resultPath);
            System.out.println(compileResult);
        }
        catch(Exception exception){
            System.out.println(exception);
        }

        //read all bin and abi file
        String[] resultFileNames = path.list();
        List<String> contractList = getContractList(resultFileNames);
        contractList.forEach(contract ->{
            result.put(contract, readContent(contract, resultPath));
        });

        //remove temp files
        if(deleteTemp) {
            path = new File(tempPath);
            delFile(path);
        }

        return result;
    }

    //region utilities
    private static Map<String, String> readContent(String contractName, String path){
        Map<String, String> content = new HashMap<String, String>();

        String filePath = path + contractName + BinExtension;
        String file = readFile(filePath);
        content.put("binary", file);

        filePath = path + contractName + AbiExtension;
        file = readFile(filePath);
        content.put("abi", file);

        return content;
    }

    private static List<String> getContractList(String[] fileList){
        List<String> list = new ArrayList<>();

        for(String fileName : fileList){
            String name = "";
            if(fileName.endsWith(BinExtension)){
                name = fileName.substring(0, fileName.length() - BinExtension.length());
            }
            else if(fileName.endsWith(AbiExtension)){
                name = fileName.substring(0, fileName.length() - AbiExtension.length());
            }

            if(!name.isEmpty() && !list.contains(name)){
                list.add(name);
            }
        }

        return list;
    }

    private static String getFileName(){
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
