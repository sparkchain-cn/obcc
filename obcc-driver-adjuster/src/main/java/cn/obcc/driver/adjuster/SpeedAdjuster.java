package cn.obcc.driver.adjuster;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.fn.ITransferFn;
import cn.obcc.driver.tech.ISpeedAdjuster;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.config.ReqConfig;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName SpeedAdjuster
 * @desc TODO
 * @date 2019/8/25 0025  13:47
 **/
public class SpeedAdjuster<T> extends BaseHandler<T> implements ISpeedAdjuster<T> {

    private boolean flag = true;
    public Queue<Object> queues = new LinkedList<>();


    @Override
    public void start() throws Exception {
        new Thread(() -> {
            while (flag) {
                Map map = (Map) queues.poll();
                if (map != null) {
                    try {
                        getDriver().getAccountHandler().transfer((String) map.get("bizId"),
                                (SrcAccount) map.get("account"),
                                (BigInteger) map.get("amount"),
                                (String) map.get("destAddress"),
                                (ReqConfig<T>) map.get("config"),
                                (ITransferFn) map.get("callback")
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void offer(Object obj) throws Exception {
        queues.offer(obj);
    }

    @Override
    public void poll() throws Exception {
        queues.poll();
    }

    @Override
    public void close() throws Exception {
        flag = false;

    }
}
