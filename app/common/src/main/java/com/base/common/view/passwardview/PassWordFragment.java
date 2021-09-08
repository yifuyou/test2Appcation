package com.base.common.view.passwardview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;

import com.base.common.R;
import com.base.common.databinding.PasswordDialogBinding;
import com.base.common.utils.OnClickCheckedUtil;
import com.base.common.view.base.BaseDialogFragment;
import com.base.common.view.passwardview.grid.GridPasswordView;
import com.base.common.viewmodel.BaseViewModel;

public class PassWordFragment extends BaseDialogFragment<PasswordDialogBinding, BaseViewModel> {

    public static PassWordFragment newInstace(FinishInputPassword finishActionInf) {
        PassWordFragment passWordFragment = new PassWordFragment();
        passWordFragment.finishAction = finishActionInf;
        return passWordFragment;
    }

    @Override
    protected PasswordDialogBinding initDataBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.password_dialog, container, false);
    }


    private PasswordView mPassword;

    private FinishInputPassword finishAction;


    public void setFinishAction(FinishInputPassword finishAction) {
        this.finishAction = finishAction;
    }

    @Override
    public void initView() {
        super.initView();
        mPassword = binding.viewPassword;
        mPassword.getCloseImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mPassword.getForgetTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAction.clickForgetPassword();
                //Toast.makeText(getActivity(),"忘记密码",Toast.LENGTH_SHORT).show();
            }
        });

        mPassword.getPswView().setOnClickListener(new OnClickCheckedUtil() {
            @Override
            public void onClicked(View view) {

            }
        });



        mPassword.setPasswordViewEnabled(false);
        mPassword.getPswView().setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (mPassword.getPassword().length() == 6) {
                    dismiss();
                    finishAction.finishPassword(mPassword.getPassword());
                    //Toast.makeText(getActivity(),mPassword.getPassword(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });
    }


}
