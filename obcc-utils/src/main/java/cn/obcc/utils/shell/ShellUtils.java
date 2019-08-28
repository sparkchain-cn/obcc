package cn.obcc.utils.shell;


import cn.obcc.utils.FormatUtils;
import cn.obcc.utils.os.OsInfoUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class ShellUtils {

    public static String runShell(String shStr) throws IOException {
        StringBuilder sb = new StringBuilder();
        Process process;
        if (OsInfoUtils.isWindows()) {
            process = Runtime.getRuntime().exec(shStr);
        } else if (OsInfoUtils.isLinux()) {
            process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", shStr}, null, null);
        } else {
            throw new RuntimeException(" no support!");
        }

        InputStreamReader ir = new InputStreamReader(process.getInputStream());
        InputStreamReader errorReader = new InputStreamReader(process.getErrorStream());

        if (errorReader.read() != -1) {
            System.out.println(shStr + "'s error:");
            processStream(shStr, errorReader, sb);
            sb.append("\r\n");
            if (sb.toString().contains("error")) {
                process.destroy();
                throw new IOException(sb.toString());
            }
        }

        System.out.println(shStr + "'s result:");
        processStream(shStr, ir, sb);
        sb.append("\r\n");

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static void processStream(String shStr, InputStreamReader reader, StringBuilder sb) throws IOException {

        LineNumberReader input = new LineNumberReader(reader);
        String line;
        boolean flag = false;

        while ((line = input.readLine()) != null) {
            System.out.println(line);
            if (line != null && line.startsWith("{")) {
                flag = true;
            }
            if (flag == true) {
                sb.append(line);
            }
        }
    }

    public static void main(String[] arge) throws Exception {
        // StatusMonitor.runShell("/home/ubuntu/soft/tomcat/bin/startup.sh");
        // String str = runShell("moac attach ws://47.98.144.109:8756/ ");

//		String str = runShell("moac attach ws://47.98.144.109:8756/  --exec txpool.inspect");
//		String str1 = runShell("moac attach ws://47.98.144.109:8756/ --exec txpool.inspect.pending");
//		String str2 = runShell("moac attach ws://47.98.144.109:8756/ --exec txpool.inspect.queued");
//		String str3 = runShell("moac attach ws://47.98.144.109:8756/ --exec txpool.inspect.pending.xxxxxxxx");
//		String str4 = runShell("moac attach ws://47.98.144.109:8756/ --exec txpool.inspect.queued.xxxxxxxx");

        String str;
//		str = runShell("E:\\work\\Research\\solidity\\solc\\run\\solc.exe contracts/spcToken.sol --bin --abi -o result");
        str = runShell("E:\\work\\Research\\solidity\\solc\\run\\solc.exe E:\\work\\Research\\solidity\\solc\\run\\contracts\\spcToken.sol --bin --abi " +
                "-o E:\\work\\Research\\solidity\\solc\\run\\result");
//		str = runShell("dir");

        str = FormatUtils.formatJson(str);
        System.out.println(str);
    }
}