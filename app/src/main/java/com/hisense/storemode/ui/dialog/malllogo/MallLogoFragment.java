package com.hisense.storemode.ui.dialog.malllogo;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hisense.hianidraw.cache.SeriesUtils;
import com.hisense.storemode.R;
import com.hisense.storemode.StoreModeApplication;
import com.hisense.storemode.bean.MallLogoDeleteEvent;
import com.hisense.storemode.manager.resource.FileCopyCoverTask;
import com.hisense.storemode.ui.activity.OperationConfirmActivity;
import com.hisense.storemode.ui.activity.PicListActivity;
import com.hisense.storemode.ui.dialog.BaseDialogFragment;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.FileUtil;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.utils.ScreenUtils;
import com.hisense.storemode.utils.StringUtil;
import com.hisense.storemode.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Create by xuchongxiang at 2019-04-24 10:15 AM
 */

public class MallLogoFragment extends BaseDialogFragment implements MallLogoAdapter.MallLogoListener {

    private static final String TAG = "MallLogoFragment";
    private static final String ADD_MALL_LOGO_FULL_FLAG = "add_circle";
    private static final String ACTION_SELECT_MALL_LOGO = "com.hisense.storemode.select_logo";
    private static final String SELECT_MALL_LOGO_URI = "com.hisense.storemode.select_logo.uri";
    private static final String SELECT_MALL_LOGO_BROWSER_URI = "com.hisense.storemode.select_logo.browser_uri";
    private static final int ACTION_SELECT_MALL_LOGO_REQUEST_CODE = 10010;
    private static final String SPLIT_FLAG = "#";
    private static final String REPLACE_CHAR = ".";

    @BindView(R.id.rv_mall_logo)
    protected RecyclerView mMallLogoRecycler;
    @BindView(R.id.tv_edit)
    protected TextView mEditMallLogo;
    private List<String> mUsbMallLogosPathList = new ArrayList<>();
    private MallLogoAdapter mMallLogoAdapter;
    private String mMallLogos = "";
    private List<MallLogoBean> mMallLogoBeanList;
    private String mNeedDeletePath = "";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mall_logo;
    }

    @Override
    protected void onCreateView() {
        EventBus.getDefault().register(this);
        setDataToRecyclerView("");

        mEditMallLogo.setOnClickListener(v -> {
            if (mEditMallLogo.getText().equals(getResources().getString(R.string.btn_edit))) {
                if (mMallLogoAdapter != null) {
                    mMallLogoAdapter.setAddMallLogo(false);
                    mEditMallLogo.setText(getResources().getString(R.string.btn_done));
                    mMallLogoAdapter.refreshLogoList();
                }
            } else {
                if (mMallLogoAdapter != null) {
                    mMallLogoAdapter.setAddMallLogo(true);
                    mEditMallLogo.setText(getResources().getString(R.string.btn_edit));
                    mMallLogoAdapter.refreshLogoList();
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected WindowManager.LayoutParams getCustomLayoutParams(WindowManager.LayoutParams layoutParams) {
        layoutParams.width = ScreenUtils.mWidth;//- ScreenUtils.getDpToPx(30);
        layoutParams.height = ScreenUtils.mHeight;//- ScreenUtils.getDpToPx(30);
        layoutParams.gravity = Gravity.CENTER;
        return layoutParams;
    }

    private void setDataToRecyclerView(String path) {
        setOrUpdateAdapter(path);
    }

    private void setOrUpdateAdapter(String path) {
        if (mMallLogoAdapter == null) {
            mMallLogoRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 4, RecyclerView.VERTICAL, false));
            mMallLogoRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    outRect.bottom = ScreenUtils.getDpToPx(6);
                    outRect.left = ScreenUtils.getDpToPx(6);
                    outRect.right = ScreenUtils.getDpToPx(6);
                    outRect.top = ScreenUtils.getDpToPx(6);
                }
            });
            //get select mall logo
            Set<String> selectSet = new HashSet<>();
            String mSelectedMallLogo = (String) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_SELECTED_MALL_LOGO, "");
            if (!TextUtils.isEmpty(mSelectedMallLogo)) {
                String[] split = mSelectedMallLogo.split(SPLIT_FLAG);
                selectSet.clear();
                for (String logo : split) {
                    if (!TextUtils.isEmpty(logo)) {
                        selectSet.add(logo);
                    }
                }
            }
            mMallLogoBeanList = new ArrayList<>();
            //add built-in pic
            addBuiltInPic(selectSet);
            //get added usb pic
            mMallLogos = (String) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_MALL_LOGOS, "");

            if (!TextUtils.isEmpty(mMallLogos)) {
                String[] split = mMallLogos.split(SPLIT_FLAG);
                for (String logo : split) {
                    if (!TextUtils.isEmpty(logo)) {
                        mMallLogoBeanList.add(new MallLogoBean(MallLogoBean.PIC_FROM_USB, selectSet.contains(logo), logo));
                    }
                }
            }
            // add last pic
            mMallLogoBeanList.add(new MallLogoBean(MallLogoBean.PIC_GOTO_MEDIARCENTER, false, ADD_MALL_LOGO_FULL_FLAG));

            mMallLogoAdapter = new MallLogoAdapter(mContext, mMallLogoBeanList);
            mMallLogoAdapter.setMallLogoListener(this);
            mMallLogoRecycler.setAdapter(mMallLogoAdapter);
        } else {
            if (TextUtils.isEmpty(path)) {
                return;
            }

            if (!TextUtils.isEmpty(path) && needAddToMallLogo(path)) { // update
                StringBuilder sb = new StringBuilder();
                sb.append(path)
                        .append(SPLIT_FLAG)
                        .append(mMallLogos);

                PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_MALL_LOGOS, sb.toString());
                mMallLogoAdapter.upDateItemAdd(path);
            }
        }
    }

    private void addBuiltInPic(Set<String> selectSet) {
//        if (SeriesUtils.DEFAULT_MALL_LOGO != -1) {
//            String defaultPicUri = ConstantConfig.getStringUriFromDrawable(SeriesUtils.DEFAULT_MALL_LOGO);
//            LogUtils.d(TAG, "addBuiltInPic()  defaultPicUri string uri: " + defaultPicUri);
//            mMallLogoBeanList.add(new MallLogoBean(MallLogoBean.PIC_FROM_BUILT_IN, selectSet.contains(defaultPicUri), defaultPicUri));
//        }

        if (StringUtil.isNotEmpty(SeriesUtils.DEFAULT_MALL_LOGO)) {
            String[] mallLogoStrings = SeriesUtils.DEFAULT_MALL_LOGO.split("-");
            for (int i = 0; i < mallLogoStrings.length; i++) {
                String pic = ConstantConfig.getStringUriFromDrawable(Integer.parseInt((mallLogoStrings[i])));
                LogUtils.d(TAG, "addBuiltInPic()  defaultPicUri string uri: " + pic);
                mMallLogoBeanList.add(new MallLogoBean(MallLogoBean.PIC_FROM_BUILT_IN, selectSet.contains(pic), pic));

            }
        }

    }


    private boolean needAddToMallLogo(String path) {

        mMallLogos = (String) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_MALL_LOGOS, "");

        if (!TextUtils.isEmpty(mMallLogos)) {
            String[] split = mMallLogos.split(SPLIT_FLAG);
            for (String logo : split) {
                if (!TextUtils.isEmpty(logo) && logo.equals(path)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void gotoSelectMallLogoActivity() {
        if (SeriesUtils.SELECT_PIC_SELF_FROM_USB) {
            startActivityForResult(new Intent(mContext, PicListActivity.class), ConstantConfig.SELECT_PIC_FROM_USB_REQUEST_CODE);
        } else {
            Intent intent = new Intent(ACTION_SELECT_MALL_LOGO);
            startActivityForResult(intent, ACTION_SELECT_MALL_LOGO_REQUEST_CODE);
        }
    }

    @Subscribe
    public void onEventDeleteLogo(MallLogoDeleteEvent event) {
        if (FileUtil.isFileExist(mNeedDeletePath)) {
            File file = new File(mNeedDeletePath);
            boolean delete = file.delete();
            if (delete) {
                deleteLocalDataAndUpdateAdapter(mNeedDeletePath);
            } else {
                LogUtils.d(TAG, "onConfirmDelete: delete error");
            }
        } else {
            LogUtils.d(TAG, "onConfirmDelete: not exit");
        }

    }

    @Override
    public void onDeleteMallLogo(String path) {

        mNeedDeletePath = path;
        Intent intent = new Intent(getActivity(), OperationConfirmActivity.class);

        startActivity(intent);


//        AlertDialogUtils.showDialog(getActivity(), getString(R.string.delete_picture_dialog_title),
//                getString(R.string.delete_picture_dialog_content)
//                , new AlertDialogUtils.ICallBack() {
//                    @Override
//                    public void OnConfirm() {
//                        if (FileUtil.isFileExist(path)) {
//                            File file = new File(path);
//                            boolean delete = file.delete();
//                            if (delete) {
//                                deleteLocalDataAndUpdateAdapter(path);
//                            } else {
//                                LogUtils.d(TAG, "onConfirmDelete: delete error");
//                            }
//                        } else {
//                            LogUtils.d(TAG, "onConfirmDelete: not exit");
//                        }
//                    }
//
//                    @Override
//                    public void OnCancel() {
//                        LogUtils.d(TAG, "onDeleteMallLogo(): delete cancel");
//                    }
//                });
        LogUtils.d(TAG, "onDeleteMallLogo: onDeleteMallLogo");
//        this.mDeleteLogoUri = strUri;
//        DeleteMallLogoFragment f = new DeleteMallLogoFragment();
//        f.setDeleteMallLogoListener(this);
//        f.show(getChildFragmentManager(), "Delete");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == ConstantConfig.SELECT_PIC_FROM_USB_REQUEST_CODE) {
            String path = data.getStringExtra(ConstantConfig.SELECT_PIC_PATH_KEY);
            LogUtils.i(TAG, "onActivityResult path: " + path);
            copyFileToSDcard(path);
        }
        if (requestCode == ACTION_SELECT_MALL_LOGO_REQUEST_CODE && data != null) {

            String filePath = data.getStringExtra(SELECT_MALL_LOGO_URI);
            String browseString = data.getStringExtra(SELECT_MALL_LOGO_BROWSER_URI);

            if (filePath == null) {
                return;
            }
            if (!isFileFitForImage(filePath)) {
                ToastUtil.showToast(getString(R.string.mall_logo_limit));
                return;
            }
            String uriString = Uri.parse(filePath).toString();

            LogUtils.d(TAG, "onActivityResult: ori path: " + uriString);
            LogUtils.d(TAG, "onActivityResult: filePath path: " + filePath);
            LogUtils.d(TAG, "onActivityResult: browseString path: " + browseString);

            copyFileToSDcard(filePath);
        }
    }


    private void copyFileToSDcard(String filePath) {
        if (checkPicExist(filePath)) {
            ToastUtil.showToast(mContext.getString(R.string.mall_logo_pic_exist));
            return;
        }
        //if second in,will not work
        if (StringUtil.isNotEmpty(filePath) && mUsbMallLogosPathList != null && mUsbMallLogosPathList.contains(filePath)) {
            ToastUtil.showToast(mContext.getString(R.string.mall_logo_pic_exist));
            return;
        }

        Uri uri = Uri.parse(filePath);
        String fromPath = uri.getPath().replace(File.separator, REPLACE_CHAR);
        String oriPath = StoreModeApplication.sContext.getApplicationContext().getFilesDir().toPath()
                + File.separator + fromPath.substring(1, fromPath.length());
        String path = oriPath.replace(SPLIT_FLAG, REPLACE_CHAR);

//        Uri toPath = Uri.fromFile(new File(path));
        LogUtils.d(TAG, "copyFileToSDcard: path: " + path);
        LogUtils.d(TAG, "copyFileToSDcard: fromPath: " + fromPath);
        LogUtils.d(TAG, "copyFileToSDcard: uriPath: " + uri.getPath());
        LogUtils.d(TAG, "copyFileToSDcard: uritoString: " + uri.toString());

        FileCopyCoverTask task = new FileCopyCoverTask();
        task.execute(uri.getPath(), path);
        task.setLisener(new FileCopyCoverTask.FileCopyListener() {
            @Override
            public void onProcessUpdate(Integer process) {

            }

            @Override
            public void onCopySuccessed() {
                LogUtils.d(TAG, "onCopySuccessed: copySucced  toPath:" + path + "  fromPath:" + filePath);
                mUsbMallLogosPathList.add(filePath);
                ToastUtil.showToast(getString(R.string.add_mall_logo_success));
                setDataToRecyclerView(path);
            }

            @Override
            public void onCopyFailed() {
                LogUtils.d(TAG, "onCopyFailed: copyFailed");
            }
        });
    }

    private boolean checkPicExist(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return false;
        }
        String logos = (String) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_MALL_LOGOS, "");
        String[] addArr = logos.split(SPLIT_FLAG);
        for (String logo : addArr) {
            if (StringUtil.isEmpty(logo)) {
                continue;
            }
            if (logo.equals(filePath)) {
                return true;
            }
        }
        return false;
    }

    private boolean isFileFitForImage(String fileUri) {
        return fileUri.endsWith(".png") || fileUri.endsWith(".jpg");
    }

    private void deleteLocalDataAndUpdateAdapter(String path) {

        //update added usb pic
        String logos = (String) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_MALL_LOGOS, "");
        String[] addArr = logos.split(SPLIT_FLAG);
        StringBuilder addPic = new StringBuilder();
        for (String logo : addArr) {
            if (!TextUtils.isEmpty(logo) && !logo.equals(path)) {
                addPic.append(logo).append(SPLIT_FLAG);
            }
        }
        // update select pref
        StringBuilder selectPic = new StringBuilder();
        String mSelectedMallLogo = (String) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_SELECTED_MALL_LOGO, "");
        if (!TextUtils.isEmpty(mSelectedMallLogo)) {
            String[] selectArr = mSelectedMallLogo.split(SPLIT_FLAG);
            for (String logo : selectArr) {
                if (!TextUtils.isEmpty(logo) && !logo.equals(path)) {
                    selectPic.append(logo).append(SPLIT_FLAG);
                }
            }
        }

        PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_MALL_LOGOS, addPic.toString());
        PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_SELECTED_MALL_LOGO, selectPic.toString());


        mMallLogoAdapter.upDateItemDelete(path);
        LogUtils.d(TAG, "deleteLocalDataAndUpdateAdapter: delete success   path:" + path);
        ToastUtil.showToast(getString(R.string.delete_picture_succes_tips));
        deletePathRemoveListValue(path);
    }


    //remove mUsbMallLogosPathList path
    private void deletePathRemoveListValue(String path) {
        //   /data/user/0/com.hisense.storemode/files/storage.537C-1C4B.4k123 (1).jpg
        //   file:///storage/537C-1C4B/4k123 (1).jpg
        String[] temp = path.split("/");
        if (temp.length <= 0) {
            return;
        }
        String last = temp[temp.length - 1];

        StringBuffer upanPicPathBuffer = new StringBuffer();
        if (StringUtil.isNotEmpty(last)) {
            upanPicPathBuffer.append("file://");
            String[] upanPicPaths = last.split("\\.");
            for (int i = 0; i < upanPicPaths.length; i++) {
                if (i != upanPicPaths.length - 1) {
                    upanPicPathBuffer.append("/" + upanPicPaths[i]);
                } else {
                    upanPicPathBuffer.append(REPLACE_CHAR + upanPicPaths[i]);
                }
            }
        }
        String upanPicPath = upanPicPathBuffer.toString();
        int existIndex = mUsbMallLogosPathList.indexOf(upanPicPath);
        if (existIndex != -1)
            mUsbMallLogosPathList.remove(existIndex);
    }

}
