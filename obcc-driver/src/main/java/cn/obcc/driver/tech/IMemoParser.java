package cn.obcc.driver.tech;

import cn.obcc.driver.IDriverHandler;
import cn.obcc.vo.BcMemo;

import java.util.List;

/**
 * @author mgicode
 * @desc IMemoParser
 * @email 546711211@qq.com
 * @date 2019年8月27日 上午9:50:21
 */
public interface IMemoParser<T> extends IDriverHandler<T> {

    /**
     * 获取支持的大小，以byte为单位
     *
     * @return
     * @throws Exception
     */
    public Long getMaxSize() throws Exception;

    /**
     * 把原始的字符串memo解析成长度合适的BcMemo对象
     *
     * @param memo
     * @return
     * @throws Exception
     */
    public List<BcMemo> parse(String bizId, String memo) throws Exception;

    /**
     * 把长度合适的BcMemo对象转为十六进制的memo
     *
     * @param memo
     * @return
     * @throws Exception
     */
    public String hex(BcMemo memo) throws Exception;

    /**
     * 把原始的字符串memo解析多条长度合适的十六进制的memo
     *
     * @param memo
     * @return
     * @throws Exception
     */
    public List<String> encode(String bizId,  String memo) throws Exception;

    /**
     * 把原始的字符串memo解析一条长度合适的十六进制的memo，超过长度报错
     *
     * @param memo
     * @return
     * @throws Exception
     */
    public String encodeOne(String bizId,String memo) throws Exception;

    /**
     * 从链取回的hex转成原始的字符串
     *
     * @param hex
     * @return
     * @throws Exception
     */
    public BcMemo decode(String hex) throws Exception;

}
