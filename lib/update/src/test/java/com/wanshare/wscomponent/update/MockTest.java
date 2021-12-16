package com.wanshare.wscomponent.update;

import android.content.Context;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MockTest {

    @Mock
    Context mContext;

//    @Before
//    public void setUp(){
//        MockitoAnnotations.initMocks(this);
//    }

    @Test
    public void mockTest() throws Exception {
        TestInterface testInterface = mock(TestInterface.class);
        Mockito.when(testInterface.getVersionName()).thenReturn("1.0.1");//虚拟该方法返回为1.0.1

        String versionName = testInterface.getVersionName();
        Assert.assertEquals("1.0.1", versionName);//断言expected = 1.0.1,实际为1.0.1

        String versionCode = testInterface.getVersionCode();//未实现该方法，返回为null
        verify(testInterface).getVersionCode();//校验是否调用了该方法
        Assert.assertEquals(null, versionCode);

//        verify(testInterface, never()).getVersionCode();//从未调用校验
        verify(testInterface, times(1)).getVersionCode();//调用次数校验
        verify(testInterface, atMost(1)).getVersionCode();//最多调用多少次校验
        verify(testInterface, atLeastOnce()).getVersionCode();//最少调用一次校验
    }
}
