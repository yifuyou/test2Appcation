package com.base.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.base.common.R;


public class DialogUtils {
    static Dialog waitDialog;
    static Dialog progressDialog;//进度条弹窗  双弹窗

    static public Dialog waitingDialog(final Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return null;
        }
        if (waitDialog == null)
            waitDialog = new Dialog(activity, R.style.dialog_no_bg);

        waitDialog.setContentView(R.layout.dialog_waiting);
        waitDialog.setCancelable(false);
        waitDialog.setCanceledOnTouchOutside(false);
        waitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (waitDialog != null) {
                    waitDialog.cancel();
                    waitDialog = null;
                }
            }
        });
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!waitDialog.isShowing() && !activity.isFinishing())
                    waitDialog.show();
            }
        });
        return waitDialog;
    }

    public static void dismiss(Activity activity) {
        if (activity != null && !activity.isFinishing() && waitDialog != null && waitDialog.isShowing()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (waitDialog != null && waitDialog.isShowing() && !activity.isFinishing() && !activity.isDestroyed())
                        waitDialog.cancel();
                    waitDialog = null;
                }
            });
        }

        if (activity != null && !activity.isFinishing() && progressDialog != null && progressDialog.isShowing()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog != null && progressDialog.isShowing() && !activity.isFinishing() && !activity.isDestroyed())
                        progressDialog.cancel();
                    progressDialog = null;
                }
            });
        }
    }

    static public Dialog progressDialog(final Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return null;
        }
        if (progressDialog == null)
            progressDialog = new Dialog(activity, R.style.dialog_no_bg);

        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (progressDialog != null) {
                    progressDialog.cancel();
                    progressDialog = null;
                }
            }
        });
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!progressDialog.isShowing() && !activity.isFinishing())
                    progressDialog.show();
            }
        });
        return progressDialog;
    }


    public static void onDestroy() {
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.cancel();
            waitDialog = null;
        }

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
            progressDialog = null;
        }
    }


    /**
     * @param context
     * @param isTransparent
     * @param okListener
     * @param isCancelClickListener 是否监听取消按扭点击
     * @param mess_title_btnOkText
     * @return
     */

    public static AlertDialog showSimpleDialogInput(Activity context, boolean isTransparent, OnClickListener okListener, boolean isCancelClickListener, CharSequence... mess_title_btnOkText) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.dialog_input, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setCancelable(false);
        builder.setView(contentView);

        EditText tv_mess = contentView.findViewById(R.id.tv_mess);
        if (mess_title_btnOkText.length > 0 && tv_mess != null)
            tv_mess.setHint(mess_title_btnOkText[0]);

        TextView tv_title = contentView.findViewById(R.id.tv_title);
        if (mess_title_btnOkText.length > 1 && tv_title != null)
            tv_title.setText(mess_title_btnOkText[1]);


        TextView bt_ok = contentView.findViewById(R.id.bt_ok);
        if (mess_title_btnOkText.length > 2 && bt_ok != null)
            bt_ok.setText(mess_title_btnOkText[2]);


//        if (mess_title_btnOkText.length == 1 && tv_title != null) tv_title.setVisibility(View.GONE);

        final AlertDialog alertDialog = builder.create();
        TextView textView = contentView.findViewById(R.id.bt_cancel);
        if (textView != null) {
            if (mess_title_btnOkText.length > 3) {
                textView.setText(mess_title_btnOkText[3]);
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (okListener == null) {
                        alertDialog.dismiss();
                    } else {
                        if (isCancelClickListener) {
                            okListener.onClick(alertDialog, 0, "");
                        } else {
                            alertDialog.dismiss();
                        }
                    }
                }
            });
        }

        if (okListener != null) {
            if (bt_ok != null) {
                bt_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        okListener.onClick(alertDialog, 1, tv_mess.getText().toString().trim());
                    }
                });
            }
        }


        if (isTransparent) {
            alertDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        } else {
            //不添加则会有四个黑角
            alertDialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        }
        alertDialog.show();

        return alertDialog;
    }


    /**
     * @param context
     * @param isTransparent
     * @param resource
     * @param okListener
     * @param isCancelClickListener 是否监听取消按扭点击
     * @param mess_title_btnOkText
     * @return
     */

    public static AlertDialog getSimpleDialog(Activity context, boolean isTransparent, @LayoutRes int resource,
                                              DialogInterface.OnClickListener okListener, boolean isCancelClickListener, CharSequence... mess_title_btnOkText) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(resource, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setCancelable(false);
        builder.setView(contentView);

        TextView tv_mess = contentView.findViewById(R.id.tv_mess);
        if (mess_title_btnOkText.length > 0 && tv_mess != null)
            tv_mess.setText(mess_title_btnOkText[0]);

        TextView tv_title = contentView.findViewById(R.id.tv_title);
        if (mess_title_btnOkText.length > 1 && tv_title != null)
            tv_title.setText(mess_title_btnOkText[1]);


        TextView bt_ok = contentView.findViewById(R.id.bt_ok);
        if (mess_title_btnOkText.length > 2 && bt_ok != null)
            bt_ok.setText(mess_title_btnOkText[2]);


//        if (mess_title_btnOkText.length == 1 && tv_title != null) tv_title.setVisibility(View.GONE);

        final AlertDialog alertDialog = builder.create();
        TextView textView = contentView.findViewById(R.id.bt_cancel);
        if (textView != null) {
            if (mess_title_btnOkText.length > 3) {
                textView.setText(mess_title_btnOkText[3]);
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (okListener == null) {
                        alertDialog.dismiss();
                    } else {
                        if (isCancelClickListener) {
                            okListener.onClick(alertDialog, 0);
                        } else {
                            alertDialog.dismiss();
                        }
                    }
                }
            });
        }

        if (okListener != null) {
            if (bt_ok != null) {
                bt_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        okListener.onClick(alertDialog, 1);
                    }
                });
            }
        }


        if (isTransparent) {
            alertDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        } else {
            //不添加则会有四个黑角
            alertDialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        }
        alertDialog.show();

        return alertDialog;
    }


    /**
     * 弹出一个简单的对话框，有标题，内容 ，和两个按扭
     *
     * @param context
     * @param isTransparent        是否是透明背景
     * @param okListener           确定按扭的点击事件
     * @param mess_title_btnOkText 消息和标题，消息在前面   ,确认按扭文案
     * @return
     */
    public static void showSimpleDialog(Activity context, boolean isTransparent, DialogInterface.OnClickListener okListener, CharSequence... mess_title_btnOkText) {
        getSimpleDialog(context, isTransparent, R.layout.dialog_customer, okListener, false, mess_title_btnOkText);
    }

    public static void showSimpleDialog2(Activity context, boolean isTransparent, DialogInterface.OnClickListener okListener, CharSequence... mess_title_btnOkText) {
        getSimpleDialog(context, isTransparent, R.layout.dialog_customer, okListener, true, mess_title_btnOkText);
    }

    /**
     * 弹出一个简单消息对话框，有标题，内容 ，只有一个按扭  没有点击事件
     *
     * @param context
     * @param isTransparent        是否是透明背景
     * @param mess_title_btnOkText 消息和标题，消息在前面  ,确认按扭文案
     * @return
     */
    public static void showSingleDialog(Activity context, boolean isTransparent, DialogInterface.OnClickListener okListener, String... mess_title_btnOkText) {
        getSimpleDialog(context, isTransparent, R.layout.dialog_message, okListener, true, mess_title_btnOkText);
    }


   public interface OnClickListener {
        void onClick(DialogInterface dialog, int which, String text);
    }


}

