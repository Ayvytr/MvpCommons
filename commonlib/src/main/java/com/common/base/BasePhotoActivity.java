package com.common.base;

import com.base.mvp.IPresenter;

/**
 * @author Xu wenxiang
 * create at 2018/9/12
 * description: 选择照片基类
 * */
public abstract class BasePhotoActivity<P extends IPresenter> extends BaseActivity<P> {
//
//
//    public void onStorage() {
//        XXPermissions.with(this)
//                     .permission(Permission.Group.STORAGE)
//                     .permission(Permission.CAMERA)
//                     .request(new OnPermissionCallback() {
//                         @Override
//                         public void onGranted(List<String> permissions, boolean all) {
//                             if(all) {
//                                 gotoPhoto();
//                             } else {
//                                 showLongToast(getString(R.string.common_permission_refuse));
//                             }
//                         }
//
//                         @Override
//                         public void onDenied(List<String> permissions, boolean never) {
//                             showLongToast(getString(R.string.common_permission_refuse));
//                         }
//                     });
//    }
//
//    private void gotoPhoto() {
//        new PhotoPickerIntent()
//                .setSelectModel(SelectModel.SINGLE)
//                .setShowCamera(true).start(this);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 101) {
//            resultAlbum(data == null ? null : data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
//        }
//    }
//
//    public abstract void resultAlbum(ArrayList<String> list);
}
