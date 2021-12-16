package com.wanshare.wscomponent.update.utils;

import com.wanshare.wscomponent.update.update.UpdateHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class UpdateHelperTest{

    String mVersionName;

    @Test
    public void getPackageName() {
    }

    @Test
    public void getVersionCode() {
    }

    @Test
    public void getPackInfo() {
    }

    @Test
    public void parseVersionName() {
        //该方法没有断言
        UpdateHelper.parseVersionName("1.0.1");
        //正确的做法
        String versionName = UpdateHelper.parseVersionName("1.0.1");
        assertEquals("101", versionName);
    }

    @Test
    public void checkVersion() {
        //错误做法，这种只测出一种情况，应当还有一种为false情况
        assertTrue(UpdateHelper.checkVersion("1.0.1", "1.0.2"));
    }

    //正确做法
    @Test
    public void checkVersionTrue() {
        //为true情况
        assertTrue(UpdateHelper.checkVersion("1.0.1", "1.0.2"));
    }

    @Test
    public void checkVersionFalse() {
        //false情况
        assertFalse(UpdateHelper.checkVersion("1.0.2", "1.0.2"));
    }


    @Before
    public void setUp(){
        //可以初始化一些参数，或者资源
        //测试方法之前执行
        mVersionName = "1.0.1";
    }

    @After
    public void after(){
        //释放资源
        //测试方式之后执行
        mVersionName = null;
    }

    @BeforeClass
    public static void beforeClass(){
        //测试类运行最先执行，并且只执行一次
    }

    @AfterClass
    public static void afterClass(){
        //测试类执行完毕之后执行，只执行一次
    }
}