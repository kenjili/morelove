package wjy.morelove.Jyson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * 字段赋值工具
 * @author wujiuye
 *
 */
public class ReflectUtils {
	
	/**
	 * 写入指定对象的指定字段的指定值
	 * @param obj
	 * @param field
	 * @param value
	 * @return
	 */
	public static boolean setFieldValue(Object obj,Field field,Object value)
	{
		field.setAccessible(true);
		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 获取指定类型的所有字段包括继承的基类的字段
	 * @param cObj
	 * @return
	 */
	public static List<Field[]> getSuperclassFields(Class<?> cObj)
	{
		List<Field[]> fields = new ArrayList<>();
		for(;cObj!=Object.class;cObj=cObj.getSuperclass())
		{
			Field[] field = cObj.getDeclaredFields();
			fields.add(field);
		}
		return fields;
	}

	/**
	 * 获取对象的所有字段包括所有父类的
	 * @param cObj
	 * @return
	 */
	public static <T> List<Field[]> getSuperclassFields(T cObj)
	{
		return getSuperclassFields(cObj.getClass());
	}
	
}
