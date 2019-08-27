package cn.obcc.vo.driver;

import cn.obcc.vo.Entity;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountInfo
 * @desc TODO
 * @date 2019/8/27 0027  16:06
 **/
public class RecordInfo extends Entity {
    //重发不在driver,driver只有网络IOException才重试
    private String bizId;
    private String hashs;
    private String userName;


    private int state;//


}
