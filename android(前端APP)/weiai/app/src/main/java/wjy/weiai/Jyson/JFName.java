package wjy.weiai.Jyson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * json字段注解  将对象的字段映射到json中的字段
 * @author Administrator
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JFName {
	
	/**
	 * 字段所对应的json中的字段名
	 * @return
	 */
	String value();
	
}