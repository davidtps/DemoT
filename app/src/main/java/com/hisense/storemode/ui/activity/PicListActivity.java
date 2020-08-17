package com.hisense.storemode.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.hisense.storemode.R;
import com.hisense.storemode.bean.UsbMountEvent;
import com.hisense.storemode.bean.UsbUnMountEvent;
import com.hisense.storemode.ui.adapter.FocusGridLayoutManager;
import com.hisense.storemode.ui.adapter.PicSelectListAdapter;
import com.hisense.storemode.ui.adapter.SpaceItemDecoration;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.FileUtil;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.ScreenUtils;
import com.hisense.storemode.utils.UsbUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class PicListActivity extends BaseNoCompatActivity {

    private static final String TAG = "PicListActivity";

    private RecyclerView mRvPic;
    private PicSelectListAdapter mAdapter;
    private boolean needRefreshPicList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pic_list;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mRvPic = findViewById(R.id.rv_pic);
        EventBus.getDefault().register(this);
        initData();
    }

    private void initData() {
        List<String> picList = getAllUsbPic();
        for (int i = 0; i < picList.size(); i++) {
            LogUtils.i(TAG, "pic path: " + picList.get(i));
        }
        FocusGridLayoutManager manager = new FocusGridLayoutManager(this, 5);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRvPic.setLayoutManager(manager);
        mRvPic.addItemDecoration(new SpaceItemDecoration(ScreenUtils.getDpToPx(19), ScreenUtils.getDpToPx(19)));
        mAdapter = new PicSelectListAdapter(this, picList);
        mRvPic.setAdapter(mAdapter);
        mAdapter.setPicSelectListener(new PicSelectListAdapter.PicSelectListener() {
            @Override
            public void onPicSelected(String path) {
                Intent intent = new Intent();
                intent.putExtra(ConstantConfig.SELECT_PIC_PATH_KEY, path);
                setResult(ConstantConfig.SELECT_PIC_FROM_USB_REQUEST_CODE, intent);
                finish();
            }
        });
    }

    @Subscribe
    public void onUsbMount(UsbMountEvent event) {
        LogUtils.d(TAG, "onUsbMount() UsbMountEvent");
        if (needRefreshPicList) {
            refreshPicList();
        }
    }

    @Subscribe
    public void onUsbUnMount(UsbUnMountEvent event) {
        LogUtils.d(TAG, "onUsbUnMount() UsbUnMountEvent");
        if (needRefreshPicList) {
            refreshPicList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        needRefreshPicList = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        needRefreshPicList = false;
    }

    private void refreshPicList() {
        List<String> picList = getAllUsbPic();
        if (mAdapter == null) {
            mAdapter = new PicSelectListAdapter(this, picList);
            mRvPic.setAdapter(mAdapter);
        } else {
            mAdapter.updatePicList(picList);
        }
    }


    private List<String> getAllUsbPic() {
        List<String> list = new ArrayList<>();
        List<UsbUtil.UsbPathAndLabel> mUsbFilePaths = UsbUtil.getUsbPath();
        // usb storage resource pic
        for (UsbUtil.UsbPathAndLabel usbFile : mUsbFilePaths) {
            // FileUtil.getFilesPathByType  mType:pic  /  file name:**
            List<String> paths = FileUtil.getFilesPathByType(usbFile.mPath, ConstantConfig.PIC_TYPE);
            if (paths.size() != 0) {
                for (String filePath : paths) {
                    if (FileUtil.isEmptyFile(filePath)) {
                        LogUtils.d(TAG, "getAllUsbPic()  usbVideoPath  usb pic file is empty 0kb" + filePath);
                        continue;
                    }
                    LogUtils.d(TAG, "getAllUsbPic()  usbVideoPath find usb pic file :" + filePath);
                    list.add(filePath);
                }
            } else {
                LogUtils.d(TAG, "getAllUsbPic()  no find usb pic file  ");
            }
        }
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
