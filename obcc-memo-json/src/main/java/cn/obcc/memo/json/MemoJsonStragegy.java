package cn.obcc.memo.json;

import cn.obcc.def.memo.IMemoStrategy;
import cn.obcc.driver.IChainDriver;
import cn.obcc.stragegy.BaseStrategy;
import cn.obcc.utils.HexUtils;
import cn.obcc.vo.BcMemo;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName JsonStragegy
 * @desc TODO
 * @date 2019/8/27 0027  9:15
 **/
public class MemoJsonStragegy extends BaseStrategy<IChainDriver> implements IMemoStrategy<IChainDriver> {
    public static Logger logger = LoggerFactory.getLogger(MemoJsonStragegy.class);

    @Override
    public Long getMaxSize() throws Exception {
        return driver.getMemoParser().getMaxSize();
    }

    @Override
    public List<BcMemo> parse(String bizId, String memo) throws Exception {
        if (memo == null) {
            memo = "";
        }
        Long len = memo.getBytes().length * 2L;
        long duanshu = new Double(Math.ceil(len / getMaxSize())).longValue();//总段数

        List<BcMemo> result = new ArrayList<>();
        //空的memo也需要返回对象
        if (duanshu == 0) {
            BcMemo vo = new BcMemo(bizId, "");
            result.add(vo);
            return result;
        }

        int duan = Math.round(memo.length() / duanshu);//每段字数

        for (int i = 0; i < duanshu; i++) {
            String segment = (i == duanshu - 1) ? memo.substring(i * duan) : memo.substring(i * duan, (i + 1) * duan);
            BcMemo vo = new BcMemo(bizId, segment);
            vo.setSeq(i + 1L);
            vo.setSum(duanshu);
            result.add(vo);
        }
        return result;
    }

    @Override
    public String hex(BcMemo memo) throws Exception {
        String json = JSON.toJSONString(memo);
        return HexUtils.str2Hex(json);
    }

    @Override
    public List<String> encode(String bizId, String preHex, String memo) throws Exception {
        List<BcMemo> list = parse(bizId, memo);
        List<String> ret = new ArrayList<>();
        for (BcMemo m : list) {
            ret.add(preHex + hex(m));
        }
        return ret;
    }

    @Override
    public String encodeOne(String bizId, String preHex, String memo) throws Exception {
        Long len = memo.getBytes().length * 2L;
        //todo:200 must测算一下
        if (len + 200 > getMaxSize()) {
            throw new RuntimeException("bizid:" + bizId + "的memo长度过大，其长度为:" + len);
        }
        List<String> list = encode(bizId, preHex, memo);
        return list.get(0);
    }

    @Override
    public BcMemo decode(String memoPre, String hex) throws Exception {
        String orig = HexUtils.hexStr2Str(hex);
        logger.debug("decode memo:" + orig);
        if (orig.startsWith(memoPre)) {
            orig = orig.substring(memoPre.length());
            BcMemo memo = JSON.parseObject(orig, BcMemo.class);
            return memo;
        }

        return null;
    }


//    private List<String> split() {
//
//        String testString = "把这段文字按三个一组进行分段，看看分段效果如何";//测试文本
//        int duanshu = 0;//总段数
//        int duan = 3;//每段字数
//        int zongchangdu = testString.length();//测试文本长度
//        int yushu = zongchangdu % duan;//余数
//        duanshu = zongchangdu / duan + (yushu > 0 ? 1 : 0);
//        for (int i = 0; i < duanshu; i++) {
//            System.out.println((i == duanshu - 1) ? testString.substring(i * duan) : testString.substring(i * duan, (i + 1) * duan));
//        }
//    }
}
