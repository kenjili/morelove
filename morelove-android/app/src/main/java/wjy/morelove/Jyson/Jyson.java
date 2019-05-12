package wjy.morelove.Jyson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 将json字符串转化为指定类型的对象
 * 且该类的字段的名称必须与json中的键一致(或使用JFName注解)
 * 该工具已经打包成jar包上传至github:https://github.com/wujiuye/Jyson，欢迎stars
 *
 * @author wujiuye
 */
public final class Jyson {

    public Jyson() {

    }

    /**
     * 将json字符串转化为指定类型的对象
     *
     * @param json
     * @param toClass
     * @return 返回指定对象的类型
     */
    public <T> Object parseJson(String json, Class<T> toClass) throws Exception {

        //获取类上的注解值
        JCName jcNameObj = toClass.getAnnotation(JCName.class);
        String jcName = null;
        int jcType = 0;
        if (jcNameObj != null && !jcNameObj.value().equals("")) {
            jcName = jcNameObj.value();
            jcType = jcNameObj.type();
        }

        if (jcName != null) {
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(json);
            if (jsonObject == null)
                return null;

            if (jcType == 0) {
                // 根据类对象的注解在json获取表示该对象的字符串
                // 使用递归（深度遍历）
                //findResult = findClassAtJsonField(jsonObject, jcName);
                // 使用队列（广度遍历）
                JSONObject findResult = findClassAtJsonField_Queue(jsonObject, jcName);
                if (findResult != null) {
                    T obj = toClass.newInstance();
                    parseJson(findResult, obj);
                    return obj;
                } else
                    return null;
            } else if (jcType == 1) {
                JSONArray findResult = findArrayAtJsonField_Queue(jsonObject, jcName);
                if (findResult == null)
                    return null;
                else {
                    List<T> objArray = new ArrayList<>();
                    for (int i = 0; i < findResult.length(); i++) {
                        JSONObject object = findResult.getJSONObject(i);
                        T obj = toClass.newInstance();
                        parseJson(object, obj);
                        objArray.add(obj);
                    }
                    return objArray;
                }
            } else if (jcType == 2) {
                JSONArray findResult = findArrayAtJsonField_Queue(jsonObject, jcName);
                if (findResult != null) {
                    List<T> objArray = new ArrayList<>();
                    for (int i = 0; i < findResult.length(); i++) {
                        JSONObject object = findResult.getJSONObject(i);
                        T obj = toClass.newInstance();
                        parseJson(object, obj);
                        objArray.add(obj);
                    }
                    return objArray;
                } else {
                    JSONObject findResult2 = findClassAtJsonField_Queue(jsonObject, jcName);
                    if (findResult2 != null) {
                        T obj = toClass.newInstance();
                        parseJson(findResult2, obj);
                        return obj;
                    }
                }
                return null;
            }
        } else {
            //当不指定类的注解时，默认当做是传过来的json字符串就是要解析的类对象
            JSONObject jsobj = new JSONObject(json);
            T obj = toClass.newInstance();
            parseJson(jsobj, obj);
            return obj;
        }
        return null;
    }


    /**
     * 当JCName  的type=1时   在JSONObject中查找JSONArray
     *
     * @param jsonObject
     * @param jcName
     * @return
     */
    private JSONArray findArrayAtJsonField_Queue(JSONObject jsonObject,
                                                 String jcName) {
        if (jsonObject == null)
            return null;
        LinkQueue<JSONObject> queue = new LinkQueue<JSONObject>();
        queue.enqueue(jsonObject);
        do {
            JSONObject newjson = queue.dnqueue();
            Iterator<?> iterator = newjson.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                if (key.equals(jcName)) {
                    JSONArray value = null;
                    try {
                        value = newjson.getJSONArray(key);
                        queue.clean();
                        return value;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    JSONArray value = null;
                    try {
                        value = newjson.getJSONArray(key);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (value == null)
                        continue;
                    for (int index = 0; index < value.length(); index++) {
                        JSONObject item = value.optJSONObject(index);
                        if (item != null) {
                            queue.enqueue(item);
                        }
                    }
                }
            }
        } while (!queue.isNull());
        return null;
    }

    /**
     * 递归
     * 查找json是否存在要转换的类  存在则返回对应的json字符串值
     *
     * @param json
     * @param toObjJCName
     * @param <T>
     * @return
     */
    @Deprecated
    private <T extends Serializable> JSONObject findClassAtJsonField(JSONObject json, String toObjJCName) {
        if (json == null)
            return null;
        Iterator<?> iterator = json.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (key.equals(toObjJCName)) {
                JSONObject value = null;
                try {
                    value = json.getJSONObject(key);
                    return value;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                JSONObject value = null;
                try {
                    value = json.getJSONObject(key);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (value == null)
                    continue;
                JSONObject result = findClassAtJsonField(value, toObjJCName);
                if (result == null)
                    continue;
                else
                    return result;
            }
        }
        return null;
    }

    /**
     * 广度遍历
     * 查找json是否存在要转换的类  存在则返回对应的json字符串值
     *
     * @param json
     * @param toObjJCName
     * @return
     */
    private <T extends Serializable> JSONObject findClassAtJsonField_Queue(JSONObject json, String toObjJCName) {
        if (json == null)
            return null;
        LinkQueue<JSONObject> queue = new LinkQueue<JSONObject>();
        queue.enqueue(json);
        do {
            JSONObject newjson = queue.dnqueue();
            Iterator<?> iterator = newjson.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                if (key.equals(toObjJCName)) {
                    JSONObject value = null;
                    try {
                        value = newjson.getJSONObject(key);
                        return value;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    JSONObject value = null;
                    try {
                        value = newjson.getJSONObject(key);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (value == null)
                        continue;
                    queue.enqueue(value);
                }
            }
        } while (!queue.isNull());
        return null;
    }


    /**
     * 递归实现字段是类类型还是list类型的对象的转换
     *
     * @param json
     * @param obj
     */
    private <T> void parseJson(JSONObject json, T obj) {
        List<Field[]> fields = ReflectUtils.getSuperclassFields(obj);
        for (int j = 0, n = fields.size(); j < n; j++) {
            Field[] fs = fields.get(j);
            if (fs == null)
                continue;
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                //如果存在注解 则获取注解的字段名
                String jsonFiledName = f.getName();
                if (f.isAnnotationPresent(JFName.class)) {
                    JFName jfName = f.getAnnotation(JFName.class);
                    if (jfName.value() != null && !jfName.value().equals(""))
                        jsonFiledName = jfName.value();
                }

                String type = f.getType().toString();
                if (!json.has(jsonFiledName)) {
                    continue;
                }
                try {
                    if (type.endsWith("String")) {
                        String string = json.getString(jsonFiledName);
                        ReflectUtils.setFieldValue(obj, f, string == null ? "" : string);
                    } else if (type.endsWith("int") || type.endsWith("Integer")) {
                        ReflectUtils.setFieldValue(obj, f, json.getInt(jsonFiledName));
                    } else if (type.endsWith("long") || type.endsWith("Long")) {
                        ReflectUtils.setFieldValue(obj, f, json.getLong(jsonFiledName));
                    } else if (type.endsWith("boolean") || type.endsWith("Boolean")) {
                        ReflectUtils.setFieldValue(obj, f, json.getBoolean(jsonFiledName));
                    } else if (type.endsWith("char") || type.endsWith("Char")) {
                        ReflectUtils.setFieldValue(obj, f, json.getString(jsonFiledName).charAt(0));
                    } else if (type.endsWith("float") || type.endsWith("Float")) {
                        ReflectUtils.setFieldValue(obj, f, (float) json.optDouble(jsonFiledName));
                    } else if (type.endsWith("double") || type.endsWith("Double")) {
                        ReflectUtils.setFieldValue(obj, f, json.optDouble(jsonFiledName));
                    } else if (f.getType().isAssignableFrom(List.class)) {
                        JSONArray jsArray = json.getJSONArray(jsonFiledName);
                        if (jsArray == null || jsArray.length() == 0)
                            continue;

                        ParameterizedType ptParameterizedType = (ParameterizedType) f.getGenericType();
                        Type trueType = ptParameterizedType.getActualTypeArguments()[0];

                        List<Object> list = new ArrayList<Object>();
                        ReflectUtils.setFieldValue(obj, f, list);

                        if (trueType == null || ptParameterizedType.getActualTypeArguments().length > 1)
                            continue;
                        formatJSONArray(jsArray, list, trueType);
                    } else if (type.endsWith("java.util.Date")) {
                        String tmpDate = json.getString(jsonFiledName);
                        if (tmpDate == null || tmpDate.equals(""))
                            continue;
                        if (tmpDate.indexOf("-") > 0) {
                            SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date dt = null;
                            try {
                                if (tmpDate.indexOf(":") > 0) {
                                    dt = sdfDateTime.parse(tmpDate + " 00:00:00");
                                } else {
                                    dt = sdfDateTime.parse(tmpDate);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            } finally {
                                ReflectUtils.setFieldValue(obj, f, dt);
                            }
                        } else {
                            try {
                                //时间戳
                                Date dt = new Date(Long.valueOf(tmpDate));
                                ReflectUtils.setFieldValue(obj, f, dt);
                            }catch (NumberFormatException e){
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Class<?> cla = Class.forName(f.getType().getName());
                        Object sobj;
                        sobj = cla.newInstance();
                        ReflectUtils.setFieldValue(obj, f, sobj);
                        parseJson(json.getJSONObject(jsonFiledName), sobj);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 处理list类型对象的转换
     *
     * @param jsArray
     * @param list
     * @param type
     * @throws Exception
     */
    private void formatJSONArray(JSONArray jsArray, List<Object> list, Type type) {

        try {
            String strType = ((Class<?>) type).getName();
            if (strType.endsWith("String")) {
                for (int k = 0, n = jsArray.length(); k < n; k++) {
                    String string = jsArray.getString(k);
                    list.add(string);
                }
            } else if (strType.endsWith("int") || strType.endsWith("Integer")) {
                for (int k = 0, n = jsArray.length(); k < n; k++) {
                    list.add(jsArray.getInt(k));
                }
            } else if (strType.endsWith("long") || strType.endsWith("Long")) {
                for (int k = 0, n = jsArray.length(); k < n; k++) {
                    list.add(jsArray.getLong(k));
                }
            } else if (strType.endsWith("boolean")
                    || strType.endsWith("Boolean")) {
                for (int k = 0, n = jsArray.length(); k < n; k++) {
                    list.add(jsArray.getBoolean(k));
                }
            } else if (strType.endsWith("char") || strType.endsWith("Char")) {
                for (int k = 0, n = jsArray.length(); k < n; k++) {
                    list.add(jsArray.getString(k).charAt(0));
                }
            } else {
                for (int k = 0, n = jsArray.length(); k < n; k++) {
                    Object cobj = ((Class<?>) type).newInstance();
                    list.add(cobj);
                    parseJson(jsArray.getJSONObject(k), cobj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断指定类型是否实现Serializable接口
     *
     * @param c
     * @return
     */
    @Deprecated
    private boolean isInterface(Class<?> c) {
        Class<?>[] face = c.getInterfaces();
        for (int i = 0, j = face.length; i < j; i++) {
            if (face[i].getName().equals(Serializable.class.getName())) {
                return true;
            } else {
                Class<?>[] face1 = face[i].getInterfaces();
                for (int x = 0; x < face1.length; x++) {
                    if (face1[x].getName().equals(Serializable.class.getName())) {
                        return true;
                    } else if (isInterface(face1[x])) {
                        return true;
                    }
                }
            }
        }
        if (null != c.getSuperclass()) {
            return isInterface(c.getSuperclass());
        }
        return false;
    }
}
