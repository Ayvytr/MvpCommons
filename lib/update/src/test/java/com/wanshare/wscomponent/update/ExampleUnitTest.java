package com.wanshare.wscomponent.update;


import com.wanshare.wscomponent.update.update.UpdateHelper;

import org.junit.Test;

//使用通配符先导入

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals("断言传入的预期值与实际值是相等的",4, 2 + 2);
    }

    @Test(timeout = 1000)
    public void assertNotEqualsTest(){
        try {
            Thread.sleep(900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotEquals("断言传入的预期值与实际值是不相等的", 2, 1);
    }

    @Test
    public void assertArrayEqualsTest(){
        String[] strings1 = new String[2];
        String[] strings2 = new String[2];
        assertArrayEquals("断言传入的预期数组与实际数组是相等的", strings1, strings2);
    }

    @Test
    public void assertNullTest(){
        String s = null;
        assertNull("为空",s);
    }

    @Test
    public void assertNotNullTest(){
        String s = "";
        assertNotNull("不为空",s);
    }

    @Test
    public void assertTrueTest(){
        boolean s = true;
        assertTrue("断言条件为真", s);
    }

    @Test
    public void assertFalseTest(){
        boolean s = false;
        assertFalse( "断言条件为假",s);
    }

    @Test
    public void assertSameTest(){
        UpdateHelper helper = new UpdateHelper();
        UpdateHelper helper1 = helper;
        assertSame("断言两个对象引用同一个对象，相当于“==”", helper, helper1);
    }

    @Test
    public void assertNotSameTest(){
        UpdateHelper helper = new UpdateHelper();
        UpdateHelper helper1 = new UpdateHelper();
        assertNotSame("比较两个对象引用的不是同一个对象", helper, helper1);
    }

    /**
     * 匹配器
     * */
    @Test
    public void assertThatTest(){
        int i = 2;
        assertThat("不等于",i, not(1));
        assertThat("等于", i, is(2));
        String s = "1";
        assertThat("字符串比较", s, equalTo("1"));
        assertThat("", "sss", endsWith("s"));
    }

}