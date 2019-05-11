package com.wujiuye.weiai7lv.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

/**
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 * <p>
 * 微信公众号id：code_skill
 * QQ邮箱：419611821@qq.com
 * 微信号：www_wujiuye_com
 * <p>
 * ======================^^^^^^^==============^^^^^^^============
 *
 * @ 作者       |   吴就业 www.wujiuye.com
 * ======================^^^^^^^==============^^^^^^^============
 * @ 创建日期      |   Created in 2018年12月31日
 * ======================^^^^^^^==============^^^^^^^============
 * @ 所属项目   |   weiai7lv(spring boot版本)
 * ======================^^^^^^^==============^^^^^^^============
 * @ 类功能描述    |
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${version}
 * ======================^^^^^^^==============^^^^^^^============
 */
@Intercepts({
        @Signature(
                //拦截哪种类型的对象
                type = StatementHandler.class,
                //拦截的目标方法
                //method = "prepare",
                method = "update",
                //目标方法的参数类型，这是一个数组，对应用目标方法的每个参数的类型，必须按照参数的顺序
//                args = {Connection.class,Integer.class}
                args = {Statement.class}
        )
})
public class MyPlugin implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //执行目标方法之前
        Object[] args = invocation.getArgs();
        System.err.println("执行目标方法之前。。。。。");
        for (Object obj:args){
            if(obj!=null) {
                System.err.println("参数：[类型:" + obj.getClass().getName() + ",值:" + obj + "]");
            }
        }
        //执行目标方法并获取返回值
        Object targetResult = invocation.proceed();
        //执行目标方法之后要做的事情
        System.err.println("执行目标方法之后。。。。。");
        //返回目标方法的返回值
        return targetResult;
    }

    @Override
    public Object plugin(Object target) {
        //使用Plugin的wrap方法保证目标对象
        return Plugin.wrap(target,this);
    }

    /**
     * 在实例化插件之后被调用
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        //获取插件注册时配置的属性的值
        String my = properties.getProperty("my");
        System.out.println("配置的参数是：[my="+my+"]");
    }
}
