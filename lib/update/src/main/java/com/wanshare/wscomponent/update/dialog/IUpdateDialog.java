package com.wanshare.wscomponent.update.dialog;


import com.wanshare.wscomponent.update.model.VersionEntity;

public interface IUpdateDialog {

    BaseDialog getNetTipsDialog();

    BaseDialog getInstallDialog();

    BaseDialog getProgressDialog(int progress, VersionEntity entity);

    BaseDialog getUpdateTipsDialog(VersionEntity entity);

    class SubUpdateDialog implements IUpdateDialog{


        @Override
        public BaseDialog getNetTipsDialog() {
            return null;
        }

        @Override
        public BaseDialog getInstallDialog() {
            return null;
        }

        @Override
        public BaseDialog getProgressDialog(int progress, VersionEntity entity) {
            return null;
        }

        @Override
        public BaseDialog getUpdateTipsDialog(VersionEntity entity) {
            return null;
        }
    }

}