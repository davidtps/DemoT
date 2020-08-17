package com.hisense.storemode.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.hisense.storemode.R;


/**
 * @author tianpengsheng
 * create at   5/16/19 2:37 PM
 */
public class AlertDialogUtils {

    private static AlertDialog.Builder builder;
    private static AlertDialog dialog;
    private static boolean isShowing;


    // 系统样式的对话框（leftStr/rightStr）
    public static void showDialogWithLeftRightText(Context context, String title, String msg, String leftstr,
                                                   String rightStr,
                                                   final ICallBack callBack) {

        try {
            if (isShowing) {
                return;
            }
            builder = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton(rightStr,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (callBack != null) {
                                        dialog.dismiss();
                                        callBack.OnConfirm();
                                    }
                                    isShowing = false;
                                }
                            })
                    .setNegativeButton(leftstr,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {


                                    if (callBack != null) {
                                        dialog.dismiss();
                                        callBack.OnCancel();
                                    }
                                    isShowing = false;
                                }

                            });
            builder.setCancelable(false);
//            dialog = builder.create();
//            dialog.show();
            builder.show();
            isShowing = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDialog(Context context, String title, String msg,
                                  final ICallBack callBack) {
        try {
            if (isShowing) {
                return;
            }
            builder = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton(context.getString(R.string.dialog_btn_confirm_tip),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (callBack != null) {
                                        dialog.dismiss();
                                        callBack.OnConfirm();
                                    }
                                    isShowing = false;
                                }
                            })
                    .setNegativeButton(context.getString(R.string.dialog_btn_cancel_tip),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    if (callBack != null) {
                                        dialog.dismiss();
                                        callBack.OnCancel();
                                    }
                                    isShowing = false;
                                }

                            });
            builder.setCancelable(false);
            dialog = builder.create();
            dialog.show();
            isShowing = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showConfirmDialog(Context context, String title, String msg,
                                         final ICallBack callBack) {
        try {
            builder = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton(context.getString(R.string.dialog_btn_confirm_tip),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (callBack != null) {
                                        dialog.dismiss();
                                        callBack.OnConfirm();
                                    }
                                }
                            });
            builder.setCancelable(true);
            dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对话框，自定义view展示
     *
     * @param view     自定义view
     * @param callBack
     */
    public static void showDialog(View view,
                                  final ICallBack callBack) {

        try {
            if (isShowing) {
                return;
            }
            builder = new AlertDialog.Builder(view.getContext())
//                    .setTitle(title)
                    .setView(view)
                    .setPositiveButton(view.getContext().getString(R.string.dialog_btn_confirm_tip),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (callBack != null) {
                                        dialog.dismiss();
                                        callBack.OnConfirm();
                                    }
                                    isShowing = false;
                                }
                            })
                    .setNegativeButton(view.getContext().getString(R.string.dialog_btn_cancel_tip),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {


                                    if (callBack != null) {
                                        dialog.dismiss();
                                        callBack.OnCancel();
                                    }
                                    isShowing = false;
                                }

                            });
            builder.setCancelable(false);
            dialog = builder.create();
            dialog.show();
            isShowing = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface ICallBack {
        void OnConfirm();

        void OnCancel();

    }


}
