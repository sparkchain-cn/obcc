package cn.obcc.utils;
//package com.sparkchain.chain.junction.utils;
//
//import com.sparkchain.framework.os.OSinfo;
//
//import java.io.InputStreamReader;
//import java.io.LineNumberReader;
//
//public class ShellUtils {
//
//    public static String runShell(String shStr) throws Exception {
//        StringBuilder sb = new StringBuilder();
//        Process process;
//        if (OSinfo.isWindows()) {
//            process = Runtime.getRuntime().exec(shStr);
//        } else if (OSinfo.isLinux()) {
//            process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", shStr}, null, null);
//        } else {
//            throw new RuntimeException(" no support!");
//        }
//
//        InputStreamReader ir = new InputStreamReader(process
//                .getInputStream());
//
//        LineNumberReader input = new LineNumberReader(ir);
//        String line;
//        boolean flag = false;
//        System.out.println(shStr + "'s result:");
//
//        while ((line = input.readLine()) != null) {
//            System.out.println(line);
//            if (line != null && line.startsWith("{")) {
//                flag = true;
//            }
//            if (flag == true) {
//                sb.append(line);
//            }
//
//        }
//
//        //Start MOAC nuwa 1.0.4-stable ...
//        //Fatal: This is a testnet version, cannot connect with mainnet
//        return sb.toString();
//    }
//
//    public static void main(String[] arge) throws Exception {
//        // StatusMonitor.runShell("/home/ubuntu/soft/tomcat/bin/startup.sh");
//        // String str = runShell("moac attach ws://47.98.144.109:8756/ ");
//
//
//        String str = runShell("moac attach ws://47.98.144.109:8756/  --exec txpool.inspect");
//        String str1 = runShell("moac attach ws://47.98.144.109:8756/ --exec txpool.inspect.pending");
//        String str2 = runShell("moac attach ws://47.98.144.109:8756/ --exec txpool.inspect.queued");
//        String str3 = runShell("moac attach ws://47.98.144.109:8756/ --exec txpool.inspect.pending.xxxxxxxx");
//        String str4 = runShell("moac attach ws://47.98.144.109:8756/ --exec txpool.inspect.queued.xxxxxxxx");
//
//
//        str = FormatUtils.formatJson(str);
//        System.out.println(str);
//    }
//}
