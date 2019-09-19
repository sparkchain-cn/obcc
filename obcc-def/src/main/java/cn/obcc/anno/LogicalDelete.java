package cn.obcc.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * author:pengrk 
 * email:sjkjs155@126.com 
 *   @wetsite:www.mgicode.com
 * @license:GPL
 * 主要用于实体上和实体字段的注解，表明该实体采用逻辑删除，
 * 同时还需要在实体字段进行标注，如：
 *  <code>
 * @LogicalDelete
 * @Entity
 *   public class TUser implements java.io.Serializable {
 *             private Integer logicalDelete; 
 * @LogicalDelete 
 * public Integer getLogicalDelete() 
 * { 
 * return logicalDelete;  
 * } 
 *  </code> *  
 *  在应用时，把appconfig.propertis文件中logicalDelete设定为true,
 *  系统会自动进行逻辑删除,设定为其它，采用物理删除
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogicalDelete {

}
