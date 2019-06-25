package com.example.zuer02.loginPower;

import org.apache.shiro.mgt.DefaultSubjectFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;

public class StatelessDefaultSubjectFactory  extends DefaultSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        //不创建session
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }

}
