package cn.obcc.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
/**
 * <pre>
 * author:pengrk
 * email:sjkjs155@126.com
 *   @wetsite:www.mgicode.com
 * @license:GPL
 * 主要在Model的Get方法上进行标识。
 * 页面可以直接取出该值形成下拉框等
 * 	@Desc(enums = "#{1:自评,2:审核,3:审定}")
 *  @Column(name = "state")
 *   public Integer getState() {
 *   return state;
 *   }
 *   如果不使用desc进行，可以在Resources中的configData.xml中
 *   进行xml的配置，如下所示：
 *   <?xml version="1.0" encoding="UTF-8"?>
 <data>
 <com.morik.mgicode.domain.TProject>
 <state>
 <value name="1">立项</value>
 <value name="2">分配人员</value>
 <value name="21">开始审计</value>
 <value name="22">主审结束审计</value>
 <value name="3">进入考评</value>
 <value name="31">审计员确认</value>
 <value name="312">进行审批</value>
 <value name="32">进行审定</value>
 <value name="4">完成</value>
 </state>
 </com.morik.mgicode.domain.TProject>
 </data>	
 * 相关类 <code>com.kuiren.common.CommonEnum</code>
 * </pre>
 * 
 */
public @interface Desc {

	String value() default ("");

	String desc() default ("");

	String aliasName() default ("");

	String enums() default ("");

	boolean listDisplay() default (true);

	boolean editDisplay() default (true);

	boolean searchDisplay() default (true);

	int order() default (0);

	int logicDelete() default (0);// 逻辑删除的标志

	//Class<?> relativeEntity() default (Object.class);

	String relativeDropDown() default ("");// 关连下拉的列表

	String inputType() default ("");

	String genType() default ("");// 生成的需要的类型

}
