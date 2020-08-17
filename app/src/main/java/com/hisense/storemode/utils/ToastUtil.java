package com.hisense.storemode.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.hisense.storemode.R;
import com.hisense.storemode.StoreModeApplication;

import androidx.annotation.NonNull;

/**
 * @author 作者  tianpengsheng
 * create at 创建时间  4/29/19 9:42 AM
 */
public class ToastUtil {

    public static void showToast(String str) {
        Toast.makeText(StoreModeApplication.sContext, str, Toast.LENGTH_LONG).show();
    }


    public static void showToastTime(final String str, final long mill, final Handler handler) {
        final ToastDialog dialog = new ToastDialog(StoreModeApplication.sContext, R.style.ToastTheme);
        dialog.setText(str);
        dialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, mill);
    }


    private static class ToastDialog extends Dialog {

        public ToastDialog(@NonNull Context context) {
            this(context, 0);
        }

        public ToastDialog(@NonNull Context context, int themeResId) {
            super(context, themeResId);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
            setContentView(view);
            final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.format = PixelFormat.TRANSLUCENT;
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            getWindow().setGravity(Gravity.BOTTOM);
            getWindow().setAttributes(params);
        }

        private void setText(String text) {
            TextView view = findViewById(R.id.tv_content);
            view.setText(text);
        }

    }
}
