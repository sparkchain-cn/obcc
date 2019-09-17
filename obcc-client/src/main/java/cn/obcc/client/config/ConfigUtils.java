package cn.obcc.client.config;

import cn.obcc.config.ObccConfig;
import cn.obcc.utils.base.StringUtils;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ConfigUtils
 * @desc TODO
 * @date 2019/9/10 0010  11:45
 **/
public class ConfigUtils {

    public static ObccConfig initConfig(String clientId, String nodeurl, ObccConfig config) {
        if (config == null) {
            config = new ObccConfig();
        }
        config.setClientId(clientId);

        if (StringUtils.isNotNullOrEmpty(nodeurl)) {
            config.setNodeUrl(nodeurl);
        }
        return config;

        // this.config = config;
    }

}
