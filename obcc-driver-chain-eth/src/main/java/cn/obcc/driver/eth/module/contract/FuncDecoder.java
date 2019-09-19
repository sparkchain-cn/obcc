package cn.obcc.driver.eth.module.contract;

import cn.obcc.vo.KeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.ObccTypeDecoder;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName FuncDecode
 * @desc TODO
 * @date 2019/9/18 0018  22:21
 **/
public class FuncDecoder {
    public static Logger logger = LoggerFactory.getLogger(FuncDecoder.class);
    public static List<KeyValue> decodeParameters(String input, List<String> inputNames, List<Class<? extends Type>> parameters) {
        List<KeyValue> map = new ArrayList<>();

        input = input.substring(12);
        //解析长度
        List<TypeVo> list = new ArrayList<>();
        for (Class<? extends Type> parameter : parameters) {
            TypeVo vo = new TypeVo();
            String p = input.substring(0, 64);
            input = input.substring(64);
            Type v = null;
            vo.setType(parameter.getClass());
            if (isDynamic(parameter)) {
                vo.setLength(p);
                vo.setDync(true);
            } else {
                //v = ObccTypeDecoder.decode(p, parameter.getClass());
                vo.setContent(p);
            }
            list.add(vo);
        }

        //找到动态内容
        for (TypeVo vo : list) {
            if (vo.isDync()) {
                Type type = ObccTypeDecoder.decodeNumeric(vo.getLength(), vo.getType());
                int length = (int) type.getValue();
                String v = input.substring(0, length);
                vo.setContent(v);
                input = input.substring(length);
            }
        }
        if (input.length() != 0) {
            logger.error("解析出错。");
        }

        int i = 0;
        for (TypeVo vo : list) {
            Type type = ObccTypeDecoder.decode(vo.getLength() + vo.getContent(), vo.getType());
            map.add(new KeyValue(inputNames.get(i), type.getValue()));
            i++;
        }
        return map;
    }


    static boolean isDynamic(Class parameter) {
        return parameter.isAssignableFrom(DynamicBytes.class)
                || parameter.isAssignableFrom(Utf8String.class)
                || parameter.isAssignableFrom(DynamicArray.class);
    }

}
