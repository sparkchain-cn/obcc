package com.sparkchain.aries.chain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sparkchain.framework.RetData;
import com.sparkchain.framework.utils.StringUtils;
import com.sparkchain.framework.vo.KeyValue;
import com.sparkchain.redission.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @athor pengrk
 * @create at 2019-01-24 18:01
 */
@Component
public class SyncWaitDeal {

    @Autowired
    RedisUtils redisUtils;

    public RetData waitSync(RetData oldRetData) {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        // HttpServletResponse response = servletRequestAttributes.getResponse();
        String apiPath = request.getRequestURI();
        switch (apiPath) {
            case "/v1/account/transfer":
            case "/v1/account/charge":
            case "/v1/wallet/transfer":
            case "/v1/text/record":
            case "/v1/file/record":
            case "/v1/account/record":
            case "/v1/wallet/file":
            case "/v1/file":
                return writeRetData(new RetData(), oldRetData);
        }
        return oldRetData;
    }


    private RetData writeRetData(RetData retData, RetData oldRetData) {

        if (!oldRetData.getCode().equals("200")) {
            return oldRetData;
        }
        if (!(oldRetData.getData() instanceof Map)) {
            return oldRetData;
        }

        Map<String, Object> map = ((Map<String, Object>) oldRetData.getData());

        if (!map.containsKey("bizId")) {
            return oldRetData;
        }
        String bizId = (String) map.get("bizId");

        int times = 0;
        String hash = null;
        while (times <= 20) {
            hash = redisUtils.get(bizId);
            if (hash == null) {
                sleep(2000);
            } else {
                break;
            }
            times++;
        }

        //20次没有找到Hash
        if (hash == null) {
            writeTimeoutRetData(retData, bizId);
        } else {
            writeSuccRetData(retData, hash);
        }

        redisUtils.delete(bizId);
        System.out.println(hash);
        return retData;
    }

    private void writeSuccRetData(RetData retData, String hash) {
        JSONObject chainRetData = JSON.parseObject(hash);
        retData.setCode(chainRetData.getString("code"));
        retData.setMessage(StringUtils.defaultValue(chainRetData.getString("message"), ""));
        retData.setSuccess(chainRetData.getBoolean("success"));
        retData.setData(chainRetData.get("data"));
    }

    private void writeTimeoutRetData(RetData retData, String bizId) {
        retData.setCode("GW509");
        retData.setMessage("chain return data timeout,use later,use bizId:'"
                + bizId + "' query tradeInfo check the result!");
        retData.setSuccess(false);
        retData.setData(new KeyValue("bizId", bizId));
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
