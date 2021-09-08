package com.base.common.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.base.common.BuildConfig;
import com.base.common.app.BaseApp;
import com.base.common.app.BaseConstant;
import com.base.common.model.bean.BankBean;
import com.base.common.model.bean.BaseDataWrapper;
import com.base.common.model.bean.UploadImageBean;
import com.base.common.model.http.HttpServiceProxy;
import com.base.common.model.http.downLoad.DownloadUtils;
import com.base.common.model.http.downLoad.JsDownloadListener;
import com.base.common.netBeanPackage.LiveDataWrapper;
import com.base.common.model.http.upLoad.UploadImageRetrofit;
import com.base.common.model.http.upLoad.UploadOnSubscribe;
import com.base.common.model.http.upLoad.UploadService;
import com.base.common.utils.JavaMethod;
import com.base.common.utils.UIUtils;
import com.base.common.utils.VerifyUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.operators.observable.ObservableMap;


public class BaseViewModel extends AndroidViewModel {

    private CompositeDisposable mCompositeDisposable;

    private ArrayList<BaseViewModel> list = null;//缓存其他的VM

    public BaseViewModel(@NonNull Application application) {
        super(application);
        onCreate();
        list = new ArrayList<>();
    }

    private void onCreate() {
        this.mCompositeDisposable = new CompositeDisposable();
    }


    public void addBaseViewModel(BaseViewModel viewModel) {
        if (!list.contains(viewModel)) {
            list.add(viewModel);
        }
    }

    // 可以终止网络请求
    public void onDestroy() {
        if (null != mCompositeDisposable) {
            this.mCompositeDisposable.clear();
            this.mCompositeDisposable = null;
        }
        if (UIUtils.isNotEmpty(list)) {
            for (BaseViewModel baseViewModel : list) {
                baseViewModel.onDestroy();
            }
            list.clear();
        }

//        onCleared();
    }


    // 添加后，可以终止网络请求
    protected void addDisposable(Disposable disposable) {
        if (null != mCompositeDisposable) {
            mCompositeDisposable.add(disposable);
        }
    }


    /**
     * 串行请求用 flatMap  转换即可
     * .flatMap(new NetWorkFunction<BaseResponse, BaseResponse>() {
     * public Observable<BaseResponse> onSuccess(BaseResponse baseResponse) {
     * return HttpService.getHttpServer().verifyCodeLogin(phoneNum, code);
     * })
     */
    public <T> MutableLiveData<LiveDataWrapper<T>> send(Observable<T> observable) {

        String url = "";
        if (BuildConfig.DEBUG) {
            Object obj = null;
            if (observable instanceof ObservableMap) {
                obj = ((ObservableMap) observable).source();
            } else if (observable.getClass().getSimpleName().equals("BodyObservable")) {
                obj = observable;
            }

            if (obj != null) {
                Object upstream = JavaMethod.getFieldValue(obj, "upstream");
                if (upstream != null) {
                    Object originalCall = JavaMethod.getFieldValue(upstream, "originalCall");
                    if (originalCall != null) {
                        Object serviceMethod = JavaMethod.getFieldValue(originalCall, "serviceMethod");
                        if (serviceMethod != null) {
                            Object baseUrl = JavaMethod.getFieldValue(serviceMethod, "baseUrl");
                            String relativeUrl = JavaMethod.getFieldValue(serviceMethod, "relativeUrl");
                            url = baseUrl + relativeUrl;
                        }
                    }
                }
            }
        }


        NetWorkObserver<T> netWorkObserver = observable.compose(new RxSchedulerTransformer<T>())
                .subscribeWith(new NetWorkObserver<T>(url, 1));

        //对请求进行缓存，只有添加了@Cancel 的请求才会被缓存
        HttpServiceProxy.putNetWorkObserver(observable, netWorkObserver);
        addDisposable(netWorkObserver);

        return netWorkObserver.getWrapper();
    }


    //并行请求
    public MutableLiveData<LiveDataWrapper<Object>> sendMerge(Observable... observable) {
        Observable<Object> merge = Observable.mergeArray(observable);
        NetWorkObserver<Object> baseSubscriber = merge.compose(new RxSchedulerTransformer<>())
                .subscribeWith(new NetWorkObserver<>("", observable.length));
        addDisposable(baseSubscriber);
        return baseSubscriber.getWrapper();
    }

    //并行请求
    public MutableLiveData<LiveDataWrapper<BaseDataWrapper>> sendMerge2(Observable<BaseDataWrapper>... observable) {
        Observable<BaseDataWrapper> merge = Observable.mergeArray(observable);
        NetWorkObserver<BaseDataWrapper> baseSubscriber = merge.compose(new RxSchedulerTransformer<>())
                .subscribeWith(new NetWorkObserver<>("", observable.length));
        addDisposable(baseSubscriber);
        return baseSubscriber.getWrapper();
    }


    /**
     * 获取图片上传的Observable  带进度条
     * getUpLoadFilesObservable(url).subscribe(new LoadCallBack() {
     *
     * @Override protected void onProgress(String percent) {
     * <p>
     * }
     * @Override protected void onSuccess(Object obj) {
     * <p>
     * }
     * })
     */
    public Observable<Object> getUpLoadFilesObservable(String path) {
        UploadOnSubscribe uploadOnSubscribe = new UploadOnSubscribe();
        return Observable.merge(Observable.create(uploadOnSubscribe), UploadService.getUpLoadFilesService(BaseApp.getApplication().getBaseUrl()).ossUpload(UploadImageRetrofit.getPart(path, uploadOnSubscribe)))
                .compose(new RxSchedulerTransformer<>());
    }

    public Observable<Object> getUpLoadFilesObservable(String path, UploadService uploadService) {
        UploadOnSubscribe uploadOnSubscribe = new UploadOnSubscribe();
        return Observable.merge(Observable.create(uploadOnSubscribe), uploadService.ossUpload(UploadImageRetrofit.getPart(path, uploadOnSubscribe)))
                .compose(new RxSchedulerTransformer<>());
    }


//    public MutableLiveData<LiveDataWrapper<UploadImageBean>> upload(File headerFile) {
//        return send(HttpService.getUpLoadFilesService().ossUpload(UploadImageRetrofit.getMultipartBody_part(headerFile)));
//    }


    /**
     * 批量上传文件   并行接口
     * 接口不支持批量上传用合并请求的方式   不会有上传进度
     */
    public MutableLiveData<LiveDataWrapper<Object>> getUpLoadFilesObservable(@NonNull List<String> path, String baseUrl) {

        Observable[] obs = new Observable[path.size()];
        for (int i = 0; i < path.size(); i++) {
            obs[i] = UploadService.getUpLoadFilesService(baseUrl).ossUpload(UploadImageRetrofit.getMultipartBody_part(path.get(i)));
        }

        return sendMerge(obs);
    }


    /**
     * 单文件上传   不会有上传进度
     */
    public MutableLiveData<LiveDataWrapper<UploadImageBean>> getUpLoadFileObservable(String path, String baseUrl) {
        return send(UploadService.getUpLoadFilesService(baseUrl).ossUpload(UploadImageRetrofit.getMultipartBody_part(path)));
    }

    public MutableLiveData<LiveDataWrapper<UploadImageBean>> getUpLoadFileObservable(String path, UploadService uploadService) {
        return send(uploadService.ossUpload(UploadImageRetrofit.getMultipartBody_part(path)));
    }

    /**
     * apk文件下载
     */

    public void downLoadFileApk(String url, String absPath, JsDownloadListener listener) {
        new DownloadUtils(listener).download(url, absPath);
    }


    public String checkedIDCard(String idCardStr) {
        return VerifyUtils.checkedIDCard(idCardStr);
    }


    public String checkedNull(String str, String typeTxt) {
        if (str == null) return typeTxt + "不可为空";
        int len = str.length();
        if (len == 0) {
            return typeTxt + "不可为空";
        }
        return null;
    }


    /**
     * 检查错误信息
     *
     * @param err==null为正确
     * @param errStr
     * @return
     */
    public String checkedErrStr(String err, String... errStr) {
        if (err != null) {
            return err;
        }
        if (errStr.length > 0) {
            for (String s : errStr) {
                if (s != null) return s;
            }
        }
        return null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }


    /**
     * 银行卡验证
     *
     * @param cardNo
     * @return
     */
    public MutableLiveData<LiveDataWrapper<BankBean>> bank_discern_url(String cardNo) {
        return send(UploadService.getUpLoadFilesService(BaseConstant.bank_discern_url).bank_discern_url(BaseConstant.bank_discern_url, "utf-8", cardNo, "true"));
    }


}
