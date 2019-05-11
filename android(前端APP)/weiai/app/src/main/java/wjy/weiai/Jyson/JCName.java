package wjy.weiai.Jyson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类注解  json字段名对应类的名称
 * Created by wujiiuye on 2016/12/3.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JCName {
    /**
     * 类所对应的json中的字段名
     * @return
     */
    String value();
    /**
     * 类型,默认值为0
     * 0为对应json字符串中的Object
     * 1为对应json字符串中的Array
     * 2为自探索类型
     * @return
     */
    int type() default(0);
}
