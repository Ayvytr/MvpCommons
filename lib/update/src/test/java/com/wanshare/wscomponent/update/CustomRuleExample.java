package com.wanshare.wscomponent.update;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 *自定义Rule
 * */
public class CustomRuleExample implements TestRule {
    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                //想要在测试方法运行之前做一些事情，就在base.evaluate()之前做 相当于@before
                String className = description.getClassName();
                String methodName = description.getMethodName();
                
                base.evaluate();  //运行测试方法
                
                //想要在测试方法运行之后做一些事情，就在base.evaluate()之后做 相当于@After
                System.out.println("Class name: "+className +", method name: "+methodName);
            }
        };
    }
}