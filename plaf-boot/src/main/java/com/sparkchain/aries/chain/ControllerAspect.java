package com.sparkchain.aries.chain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import com.sparkchain.framework.RetData;
import com.sparkchain.framework.utils.StringUtils;
import com.sparkchain.framework.vo.KeyValue;
import com.sparkchain.redission.RedisUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
public class ControllerAspect {
    public static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Value("${com.sparkchain.sync.wait:false}")
    private boolean wait;

    @Autowired
    SyncWaitDeal syncWaitDeal;

    @Around("execution(public com.sparkchain.framework.RetData com..*.controller..*.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        RetData retData = null;
        try {
            logger.info("start exec controller: " + pjp.getSignature() + " params："
                    + Lists.newArrayList(pjp.getArgs()).toString());
            retData = (RetData) pjp.proceed(pjp.getArgs());
            if (wait == true) {
                retData = syncWaitDeal.waitSync(retData);
            }
            logger.info("finish exec controller: " + pjp.getSignature() + "， return：" + retData.toString());
            logger.info("spend time：" + stopwatch.stop().elapsed(TimeUnit.MILLISECONDS) + "(ms).");
        } catch (Throwable throwable) {
            retData = handlerException(pjp, throwable);
        }

        return retData;
    }


    private RetData handlerException(ProceedingJoinPoint pjp, Throwable e) {
        e.printStackTrace();
        RetData retData = null;
//        if (e instanceof AssetsException) {
//            AssetsException ae = (AssetsException) e;
//            return RetData.error(ae.getErrorCode(), ae.getErrorMsg());
//        } else
        if (e instanceof RuntimeException) {
            String msg = "exception：" + e.getMessage() + "{method：" + pjp.getSignature() + "， parmas：" + pjp.getArgs()
                    + "}";
            logger.error(msg, e);
            retData = RetData.error("SA505", msg);

        } else {
            String msg = "exception：" + e.getMessage() + ",{method：" + pjp.getSignature() + "， parmas：" + pjp.getArgs()
                    + "}";
            logger.error(msg, e);
            retData = RetData.error("SA505", msg);
        }

        return retData;
    }
}