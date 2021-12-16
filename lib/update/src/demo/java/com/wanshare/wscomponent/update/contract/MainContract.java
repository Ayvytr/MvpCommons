package com.wanshare.wscomponent.update.contract;

import com.wanshare.wscomponent.update.model.VersionEntity;

public class MainContract {

    public interface View{
        void showVersion(VersionEntity version);
    }

    public interface Presenter{
        void getVersion(Integer equipType);
    }
}
