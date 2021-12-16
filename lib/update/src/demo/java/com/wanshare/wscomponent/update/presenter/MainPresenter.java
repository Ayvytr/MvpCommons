package com.wanshare.wscomponent.update.presenter;

import com.wanshare.wscomponent.http.ApiClient;
import com.wanshare.wscomponent.update.contract.MainContract;
import com.wanshare.wscomponent.update.model.UpdateServer;
import com.wanshare.wscomponent.update.model.VersionEntity;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    public MainPresenter(MainContract.View view) {
        mView = view;
    }

    public MainPresenter() {

    }

    @Override
    public void getVersion(Integer equipType) {
        UpdateServer server = ApiClient.getInstance().create(UpdateServer.class);
        server.getVersion(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<VersionEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VersionEntity versionEntity) {
                        if (versionEntity == null) {
                            return;
                        }
                        mView.showVersion(versionEntity);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
