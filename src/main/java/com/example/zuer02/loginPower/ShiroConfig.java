package com.example.zuer02.loginPower;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.springframework.context.annotation.Bean;
import org.apache.shiro.mgt.SecurityManager;//这个类需要手动导入，目前不知道原因
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

    @Bean
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        return userRealm;
    }


    @Bean
    public SecurityManager securityManager(UserRealm userRealm) {
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(userRealm);

        //设置无状态SubjectFactory，屏蔽session创建
        StatelessDefaultSubjectFactory statelessDefaultSubjectFactory = new StatelessDefaultSubjectFactory();
        defaultSecurityManager.setSubjectFactory(statelessDefaultSubjectFactory);

        //设置无状态SubjectDao，屏蔽session保存
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);

        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        defaultSubjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);

        defaultSecurityManager.setSubjectDAO(defaultSubjectDAO);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        return defaultSecurityManager;
    }

}
