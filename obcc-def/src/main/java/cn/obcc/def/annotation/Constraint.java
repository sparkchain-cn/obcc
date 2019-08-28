package cn.obcc.def.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 * @author pengrk
 * @email:sjkjs155@126.com
 * @wetsite:www.mgicode.com
 * @license:GPL
 * 用来约束访问该地址的上一个地址 
 * 主要用来权限系统中，
 * 用来设定某一个地址只能通过上一个地址来进行访问
 * </pre>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Constraint {

	/**
	 * 采用,分隔,允许访问该地址的上一个地址
	 * 
	 * @return
	 */
	String allow() default "";

	/**
	 * 采用,分隔，forbid访问该地址的上一个地址
	 * 
	 * @return
	 */
	String forbid() default "";

	String allowPermissionIds() default "";
}
