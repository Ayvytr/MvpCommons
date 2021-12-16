package com.wanshare.wscomponent.update.dialog;


public interface IDialogListener {

    boolean confirm();

    void cancel();


    class SubDialogListener implements IDialogListener {

        @Override
        public boolean confirm() {
          return true;
        }

        @Override
        public void cancel() {

        }
    }


}
