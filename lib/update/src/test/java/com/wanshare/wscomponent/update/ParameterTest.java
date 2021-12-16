package com.wanshare.wscomponent.update;

import android.util.Log;

import com.wanshare.wscomponent.update.update.UpdateHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParameterTest {

    private String mVersion;

    public ParameterTest(String version) {
        this.mVersion = version;
    }

    @Test
    public void parseVersion() {
        UpdateHelper.parseVersionName(mVersion);
    }

    @Parameterized.Parameters
    public static Collection primeNumbers(){
        return Arrays.asList(new String[]{
           "1.0.2", "1.0.2"
        });
    }

    @Test
    public void checkVersion(){
       boolean isUpdate = UpdateHelper.checkVersion("1.0.0", mVersion);
       assertEquals(true, isUpdate);
    }

    @Before
    public void setUp(){
        Log.d("test", "start");
//        Assert.assertNotNull(mContext);
    }

}
