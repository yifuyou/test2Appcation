package com.base.common.view.widget;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;
import androidx.documentfile.provider.DocumentFile;

import com.base.common.R;
import com.base.common.app.BaseConstant;
import com.base.common.databinding.UpgradeDialogFragmentBinding;
import com.base.common.model.http.downLoad.JsDownloadListener;
import com.base.common.permission.PermissionUtils;
import com.base.common.utils.UriPathUtils;
import com.base.common.utils.IOUtils;
import com.base.common.utils.LogUtil;
import com.base.common.utils.OnClickCheckedUtil;
import com.base.common.utils.SystemUtils;
import com.base.common.utils.UIUtils;
import com.base.common.view.base.BaseDialogFragment;
import com.base.common.viewmodel.BaseRxObserver;
import com.base.common.viewmodel.BaseViewModel;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

import io.reactivex.annotations.NonNull;

public class UpgradeDialogFragment extends BaseDialogFragment<UpgradeDialogFragmentBinding, BaseViewModel> {


    public static UpgradeDialogFragment getInstance(String url, String content, String versionName, boolean isForcedUpdating) {
        UpgradeDialogFragment fragment = new UpgradeDialogFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("content", content);
        args.putString("versionName", versionName);
        args.putBoolean("isForcedUpdating", isForcedUpdating);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected UpgradeDialogFragmentBinding initDataBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.upgrade_dialog_fragment, container, false);
    }


    private boolean isUpdate = true;
    private boolean isForcedUpdating = false;//是否强制更新

    @Override
    public void initView() {
        super.initView();
        Bundle args = getArguments();
        if (args != null) {
            isForcedUpdating = args.getBoolean("isForcedUpdating", false);
            String url = args.getString("url");
            String content = args.getString("content");
            String versionName = args.getString("versionName", "");//线上的版本号
            String mVersion_name = SystemUtils.getVersionName();// 当前的版本号

            IOUtils.initRootDirectory();
            if (isForcedUpdating) {
                binding.tvCancel.setVisibility(View.INVISIBLE);
            } else {
                binding.tvCancel.setVisibility(View.VISIBLE);
                binding.tvCancel.setOnClickListener(new OnClickCheckedUtil() {
                    @Override
                    public void onClicked(View view) {
                        dismissAllowingStateLoss();
                    }
                });
            }


            //检查本地是否已有下载好的包
            String absPath = BaseConstant.backupSaveDir + "/" + BaseConstant.APK_NAME;
            File file = new File(absPath);

            Uri uri = UriPathUtils.getUriFromFile(getContext(), file);

            DocumentFile documentFile = DocumentFile.fromSingleUri(UIUtils.getContext(), uri);


            if (documentFile.exists()) {
                String versionLocalName = SystemUtils.getApkVersionName(absPath);
                //如果线上的版本和本地的版本名一样,直接安装
                if (versionLocalName.compareTo(versionName) == 0) {
                    int nVersion_code = SystemUtils.getVersionCode();//当前应用的code
                    int versionLocal = SystemUtils.getApkVersionCode(absPath);//本地下载包的code
                    //如果当前版本号比本地的版本号低,已下载好，直接安装
                    if (nVersion_code < versionLocal) isUpdate = false;
                    else isUpdate = true;
                } else {
                    //版本不一致，先删除，再下载
                    IOUtils.deleteFile(absPath);
                    isUpdate = true;
                }
            } else {
                //有更新，需要下载
                if (mVersion_name.compareTo(versionName) < 0) {
                    isUpdate = true;
                }
            }

            if (isUpdate) {
                binding.btOk.setText(R.string.upgrade_now);
            } else {
                binding.btOk.setText(R.string.install);
            }

            binding.tvMess.setText(content);
            binding.btOk.setOnClickListener(new OnClickCheckedUtil() {
                @Override
                public void onClicked(View view) {

                    PermissionUtils.initStoragePermission(UpgradeDialogFragment.this).subscribe(new BaseRxObserver<Boolean>() {
                        @Override
                        public void onNext(@NonNull Boolean aBoolean) {
                            if (aBoolean) {
                                IOUtils.initRootDirectory();
                                if (isUpdate) {

                                    viewModel.downLoadFileApk(url, absPath, new JsDownloadListener() {
                                        @Override
                                        public void onStartDownload() {
                                            binding.btOk.setText(R.string.download_start);
                                            binding.btOk.setEnabled(false);
                                        }

                                        @Override
                                        public void onProgress(long downLoadSize, long fileSize) {
                                            float ddd = downLoadSize * 100.0f / fileSize;
                                            LogUtil.d("progress " + ddd);
                                            binding.btOk.setText(BigDecimal.valueOf(ddd).setScale(2, RoundingMode.HALF_UP).toString() + "%");
                                        }

                                        @Override
                                        public void onFinishDownload() {
                                            binding.btOk.setText(R.string.install);
                                            isUpdate = false;
                                            binding.btOk.setEnabled(true);
                                        }

                                        @Override
                                        public void onFail(String errorInfo) {
                                            LogUtil.d("progress " + errorInfo);
                                            UIUtils.showToastSafesClose(errorInfo);
                                            isUpdate = false;
                                            binding.btOk.setEnabled(true);
                                        }
                                    });

//                        downloadOnSubscribe.dispose();
                                } else {
                                    File file1 = new File(absPath);
                                    if (file1.exists()) {
                                        SystemUtils.install(UIUtils.getContext(), file1);
                                    }
                                }
                            }
                        }
                    });
                }
            });


        }

        binding.tvCancel.setOnClickListener(new OnClickCheckedUtil() {
            @Override
            public void onClicked(View view) {
                dismissAllowingStateLoss();
            }
        });
    }

    @Override
    protected int getHeight() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    @Override
    protected boolean isCancel() {
        return false;
    }
}
