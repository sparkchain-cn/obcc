package cn.obcc.stmt.storage.base;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.vo.FromAccount;
import cn.obcc.utils.FileUtils;
import cn.obcc.utils.IpfsUtils;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.KeyValue;
import lombok.NonNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName FileStorage
 * @desc TODO
 * @date 2019/9/16 0016  8:25
 **/
public class IpfsStorage {


    public static void store(String bizId, String msg, ObccConfig config, IChainDriver driver) throws Exception {
        InputStream is = new ByteArrayInputStream(msg.getBytes());
        store(bizId, is, config, driver);
    }


    public static void store(String bizId, InputStream inputStream, ObccConfig config, IChainDriver driver) throws Exception {

        List<String> hashs = new ArrayList<>();
        for (String s : split(inputStream, config, driver)) {

            String hash = IpfsUtils.save(config.getIpfsPath(), s);
            hashs.add(hash);
        }

        String hashsStr = StringUtils.cat(hashs, ",");

        KeyValue<String> v = config.getStorageSrcAccount();
        KeyValue<String> d = config.getStorageDestAccount();
        FromAccount account = new FromAccount() {{
            setSrcAddr(v.getKey());
            setSecret(v.getVal());
            setMemos(hashsStr);
        }};

        driver.getAccountHandler().text(bizId, account, d.getKey());


    }

    private static List<String> split(InputStream inputStream, ObccConfig config, IChainDriver driver) throws Exception {
        String str = FileUtils.inputStreamToString(inputStream);
        // todo:if file is too big,then split it
        return new ArrayList<String>() {{
            add(str);
        }};
    }


    /**
     * 多个hash（，分隔）分别取得文件块，然后合并
     *
     * @param ipfsHashs
     * @return
     * @throws Exception
     */
    public static InputStream getFile(@NonNull String ipfsHashs, ObccConfig config, IChainDriver driver) throws Exception {

        StringBuffer sb = new StringBuffer();
        Arrays.stream(ipfsHashs.split(",")).forEach((hash) -> {
            try {
                sb.append(IpfsUtils.cat(config.getIpfsPath(), hash));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return FileUtils.stringToInputStream(sb.toString());
    }
}
