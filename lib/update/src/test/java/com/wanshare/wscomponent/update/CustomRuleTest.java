package com.wanshare.wscomponent.update;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * 测试自定义Rule
 * */
public class CustomRuleTest {
    @Rule
    public CustomRuleExample mCustomRuleExample = new CustomRuleExample();


    @Test
    public void ruleMethodName() throws Exception {
        assertEquals(4, 2 + 2);
    }

}
