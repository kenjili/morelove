package com.wujiuye.weiai7lv.config;

import com.wujiuye.weiai7lv.shiro.WeiAi7LvRolesAuthorizationFilter;
import com.wujiuye.weiai7lv.shiro.WeiAi7LvUserAuthcFilter;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Autowired
    private AuthorizingRealm realm;

    /**
     * 配置SecurityManager
     * @return
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        return securityManager;
    }

    /**
     * 被代理的shiroFilter
     * @param securityManager
     * @return
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactory(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //注入SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //注入过滤器
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("authc", new WeiAi7LvUserAuthcFilter());
        filterMap.put("roles", new WeiAi7LvRolesAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        //过滤器Chain定义,指定哪些资源（url）交给哪个过滤器过滤，责任链模式
        Map<String, String> map = new HashMap<>();
        map.put("/", "anon");
        map.put("/sms/send", "anon");
        map.put("/user/login", "anon");
        map.put("/user/regist", "anon");
        map.put("/user/logout", "authc");
        map.put("/lover/record", "roles[lover]");
        map.put("/lover/*", "authc");
        map.put("/found/*", "authc");
        map.put("/**", "roles[lover]");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

}
