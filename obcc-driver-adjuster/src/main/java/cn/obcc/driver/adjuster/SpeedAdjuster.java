package cn.obcc.driver.adjuster;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.IChainHandler;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.tech.ISpeedAdjuster;
import cn.obcc.driver.vo.ChainPipe;
import cn.obcc.exception.enums.EChainTxType;
import cn.obcc.vo.KeyValue;
import com.alibaba.fastjson.JSON;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName SpeedAdjuster
 * @desc TODO
 * @date 2019/8/25 0025  13:47
 **/
public class SpeedAdjuster<T> extends BaseHandler<T> implements ISpeedAdjuster<T> {
    public static final Logger logger = LoggerFactory.getLogger(SpeedAdjuster.class);

    private boolean flag = true;
    //bizId-->srcAddr
    public Queue<KeyValue<String>> queues = new LinkedList<>();
    //srcAddr-->[....ChainPipe2,ChainPipe1]
    public ExpiringMap<String, Queue<ChainPipe>> map = ExpiringMap.builder()
            .variableExpiration()
            .build();

    @Override
    public IChainHandler initObccConfig(ObccConfig config, IChainDriver<T> driver) throws Exception {
        super.initObccConfig(config, driver);
        start();
        return this;
    }

    Runnable runnable = () -> {
        while (flag) {
            try {
                KeyValue<String> bizIdAccount = (KeyValue<String>) queues.poll();
                if (bizIdAccount == null) {
                    continue;
                }

                String account = bizIdAccount.getVal();
                ChainPipe pipe = poll(account);
                invoke(pipe);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };


    private ChainPipe poll(String account) {
        Queue<ChainPipe> queue = map.get(account);
        if (queue == null) {
            logger.warn("account:" + account + "在排队中没有找到相关的对象，但是在Bizid队列存在，数据对不上。");
            return null;
        }
        return queue.poll();
    }


    private void invoke(ChainPipe pipe) throws Exception {
        logger.debug("排队出列：" + JSON.toJSONString(pipe));
        if (pipe.getChainTxType() == EChainTxType.Orign) {
            getDriver().getAccountHandler().doTransfer(pipe);
        }
    }

    @Override
    public void start() throws Exception {
        new Thread(runnable).start();
    }

    @Override
    public void offer(ChainPipe pipe) throws Exception {
        String account = pipe.getSrcAccount().getSrcAddr();

        //每个account中多次交易进行队列排序
        if (!map.containsKey(account)) {
            map.put(account, new LinkedList<ChainPipe>());
        }
        map.get(account).offer(pipe);

        //不然会有时间差导致queues出队时，map中还没有写入
        //构建bizId-->account的队列
        queues.offer(new KeyValue<String>() {{
            setKey(pipe.getBizId());
            setVal(account);
        }});
    }


    @Override
    public void close() throws Exception {
        flag = false;

    }
}
