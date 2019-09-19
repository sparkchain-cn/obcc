package cn.obcc.anno;

import java.lang.annotation.*;

/**
 * @MappingId和@Transient需要同时使用
 * author:pengrk
 * email:sjkjs155@126.com
 *  @wetsite:www.mgicode.com
 * @license:GPL
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MappingId {

}
