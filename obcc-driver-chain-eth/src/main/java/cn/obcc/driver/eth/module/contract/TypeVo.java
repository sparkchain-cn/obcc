package cn.obcc.driver.eth.module.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName TypeVo
 * @desc TODO
 * @date 2019/9/18 0018  17:02
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
 class TypeVo {

    private String length;
    private String content;
    private Class type;

    private boolean isDync = false;

}