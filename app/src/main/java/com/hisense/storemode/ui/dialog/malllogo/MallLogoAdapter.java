package com.hisense.storemode.ui.dialog.malllogo;

import android.content.Context;

import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hisense.storemode.R;
import com.hisense.storemode.utils.ConstantConfig;
import com.hisense.storemode.utils.GlideUtils;
import com.hisense.storemode.utils.LogUtils;
import com.hisense.storemode.utils.PreferenceUtils;
import com.hisense.storemode.utils.ScreenUtils;
import com.hisense.storemode.utils.ToastUtil;
import com.hisense.storemode.utils.UsbUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by xuchongxiang at 2019-04-24 10:18 PM
 */

public class MallLogoAdapter extends RecyclerView.Adapter<MallLogoAdapter.MallLogoViewHolder> {


    private static final String TAG = "MallLogoAdapter";
    public static final String SPLIT_FLAG = "#";
    private Context mContext;
    private List<MallLogoBean> mLogoList;
    private List<MallLogoBean> mBuiltInLogoList;
    private boolean mIsAddMallLogo = true;
    private MallLogoBean mAddLogoIcon;

    private MallLogoListener mListener;
    private int lastClickPosition = 0;

    public MallLogoAdapter(Context context) {
        this.mContext = context;
    }

    public MallLogoAdapter(Context mContext, List<MallLogoBean> logoList) {
        this.mContext = mContext;
        this.mLogoList = logoList;
        mBuiltInLogoList = new ArrayList<>();
    }

    public void setAddMallLogo(boolean mIsAddMallLogo) {
        this.mIsAddMallLogo = mIsAddMallLogo;
    }

    public void refreshLogoList() {
        if (mLogoList == null) {
            return;
        }
        if (mIsAddMallLogo) {//is add, need show built-in pic
            List<MallLogoBean> tempList = new ArrayList<>(mBuiltInLogoList);
            tempList.addAll(mLogoList);
            if (mAddLogoIcon != null) {
                tempList.add(mAddLogoIcon);
            }
            mBuiltInLogoList.clear();
            mLogoList.clear();
            mLogoList = tempList;
            notifyDataSetChanged();
        } else {//hide built-in pic
            mBuiltInLogoList.clear();
            List<MallLogoBean> tempList = new ArrayList<>();
            for (int i = 0; i < mLogoList.size(); i++) {
                MallLogoBean bean = mLogoList.get(i);
                LogUtils.d(TAG, "delete status   type:" + bean.type + " select status:" + bean.isSelect + "  file path:" + bean.logoLocation);
                if (bean.type == MallLogoBean.PIC_FROM_BUILT_IN) {
                    mBuiltInLogoList.add(bean);
                    continue;
                } else if (bean.type == MallLogoBean.PIC_GOTO_MEDIARCENTER) {
                    mAddLogoIcon = bean;
                    continue;
                }
                LogUtils.d(TAG, "add delete collection file  type:" + bean.type + " select status:" + bean.isSelect + "  file path:" + bean.logoLocation);
                tempList.add(bean);
            }
//            Iterator<MallLogoBean> iterator = mLogoList.iterator();
//            while (iterator.hasNext()) {
//                MallLogoBean bean = iterator.next();
//                if (bean.type == MallLogoBean.PIC_FROM_BUILT_IN) {
//                    mBuiltInLogoList.add(bean);
//                    continue;
//                } else if (bean.type == MallLogoBean.PIC_GOTO_MEDIARCENTER) {
//                    mAddLogoIcon = bean;
//                    continue;
//                }
//                tempList.add(bean);
//            }
            mLogoList.clear();
            mLogoList = tempList;

            notifyDataSetChanged();

        }
    }

    public void setMallLogoListener(MallLogoListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MallLogoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item_mall_logo, parent, false);
        return new MallLogoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MallLogoViewHolder holder, int position) {
        MallLogoBean bean = mLogoList.get(position);

        if (bean == null) {
            return;
        }
        LogUtils.d(TAG, "onBindViewHolder() MallLogoBean:" + bean);

        if (mIsAddMallLogo) {
            setAddMallLogoData(holder, bean, position);
        } else {
            if (position == 0) {
                holder.itemView.requestFocus();
            }
            setDeleteMallLogoData(holder, bean);
        }
    }

    private void setAddMallLogoData(MallLogoViewHolder holder, MallLogoBean bean, int position) {

        if (position == lastClickPosition) {
            holder.itemView.requestFocus();
        }

        if (bean.isSelect) {
            holder.ivCheck.setVisibility(View.VISIBLE);
            LogUtils.d(TAG, "setAddMallLogoData " + bean);
        } else {
            holder.ivCheck.setVisibility(View.GONE);
        }

        holder.ivDelete.setVisibility(View.GONE);

        holder.ivLogo.getLayoutParams().width = ScreenUtils.getDpToPx(200);
        holder.ivLogo.getLayoutParams().height = ScreenUtils.getDpToPx(100);

        if (bean.type == MallLogoBean.PIC_GOTO_MEDIARCENTER) {
            holder.ivLogo.getLayoutParams().width = ScreenUtils.getDpToPx(60);
            holder.ivLogo.getLayoutParams().height = ScreenUtils.getDpToPx(60);
            holder.ivLogo.setImageResource(R.drawable.ic_add_circle);
            holder.ivDelete.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(v -> {
                gotoSelectMallLogoActivity();
            });
        } else {
            GlideUtils.loadImageForMallLogo(bean.logoLocation, holder.ivLogo);
            holder.itemView.setOnClickListener(v -> {
                lastClickPosition = position;
                if (bean.isSelect) {
                    //cancel
                    bean.isSelect = false;
                    LogUtils.d(TAG, "setDataAndListener: click un select uri: " + bean.toString());
                    int curSize = (int) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_SELECTED_MALL_LOGO_CUR_SIZE, 0);
                    if (curSize > 0) {
                        PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_SELECTED_MALL_LOGO_CUR_SIZE, curSize - 1);
                    }
                    deleteSelectLogoToSharePrefs(bean);
                    notifyDataSetChanged();
                } else {
                    //select
                    int curSize = (int) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_SELECTED_MALL_LOGO_CUR_SIZE, 0);
                    int maxSize = ConstantConfig.MALL_LOGO_ALLOW_SELECT_MAX_SIZE;
                    if (curSize + 1 <= maxSize) {
                        PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_SELECTED_MALL_LOGO_CUR_SIZE, curSize + 1);
                        bean.isSelect = true;
                        addSelectLogoToSharePrefs(bean);
                        LogUtils.d(TAG, "setDataAndListener: click select uri: " + bean.toString());
                        notifyDataSetChanged();
                    } else {
                        // TODO: 7/2/19  need toast?
                        ToastUtil.showToast("max select mall logo size is 4!");
                    }
                }

            });
        }
    }

    private void deleteSelectLogoToSharePrefs(MallLogoBean bean) {
        String selectLogo = (String) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_SELECTED_MALL_LOGO, "");

        String[] split = selectLogo.split(SPLIT_FLAG);
        StringBuilder sb = new StringBuilder();
        for (String logo : split) {
            if (!TextUtils.isEmpty(logo) && !logo.equals(bean.logoLocation)) {
                sb.append(logo).append(SPLIT_FLAG);
            }
        }

        PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_SELECTED_MALL_LOGO, sb.toString());
    }

    private void addSelectLogoToSharePrefs(MallLogoBean bean) {
        String selectLogo = (String) PreferenceUtils.getInstance().get(ConstantConfig.SHAREPREF_SELECTED_MALL_LOGO, "");

        if (TextUtils.isEmpty(selectLogo)) {
            PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_SELECTED_MALL_LOGO, bean.logoLocation);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(bean.logoLocation)
                    .append(SPLIT_FLAG)
                    .append(selectLogo);

            PreferenceUtils.getInstance().save(ConstantConfig.SHAREPREF_SELECTED_MALL_LOGO, sb.toString());
        }
    }

    private void setDeleteMallLogoData(MallLogoViewHolder holder, MallLogoBean bean) {
        LogUtils.d(TAG, "setDeleteMallLogoData() strUri:" + bean.toString());
        holder.ivCheck.setVisibility(View.GONE);
        holder.ivDelete.setVisibility(View.VISIBLE);
        if (bean.type == MallLogoBean.PIC_GOTO_MEDIARCENTER) {// last pic is add mall logo pic.
            holder.ivLogo.getLayoutParams().width = ScreenUtils.getDpToPx(60);
            holder.ivLogo.getLayoutParams().height = ScreenUtils.getDpToPx(60);
            holder.ivLogo.setImageResource(R.drawable.ic_add_circle);
            holder.ivDelete.setVisibility(View.GONE);
        } else {
            holder.ivLogo.getLayoutParams().width = ScreenUtils.getDpToPx(200);
            holder.ivLogo.getLayoutParams().height = ScreenUtils.getDpToPx(100);
            GlideUtils.loadImageForMallLogo(bean.logoLocation, holder.ivLogo);
        }
        if (bean.type == MallLogoBean.PIC_FROM_USB) {
            holder.itemView.setOnClickListener(null);
            holder.itemView.setOnClickListener(v -> {
                mListener.onDeleteMallLogo(bean.logoLocation);
            });
        }
    }

    private void gotoSelectMallLogoActivity() {
        if (mLogoList.size() >= ConstantConfig.MALL_LOGO_ADD_MAX_SIZE + 1) { //20 logos and a add pic
            ToastUtil.showToast(String.format(mContext.getString(R.string.mall_logo_max_pic_size)
                    , ConstantConfig.MALL_LOGO_ADD_MAX_SIZE));
            return;
        }
        if (UsbUtil.isHaveUsbDevice()) {
            if (mListener != null) {
                LogUtils.d(TAG, "gotoSelectMallLogoActivity: gotoSelectMallLogoActivity");
                mListener.gotoSelectMallLogoActivity();
            } else {
                LogUtils.d(TAG, "gotoSelectMallLogoActivity: null");
            }
        } else {
            ToastUtil.showToast(mContext.getString(R.string.usb_not_found));
        }
    }

    @Override
    public int getItemCount() {
        if (mLogoList != null) {
            LogUtils.d(TAG, "getItemCount()  count:" + mLogoList.size());
            return mLogoList.size();
//            if (mIsAddMallLogo) {
//            } else {
//                return mLogoList.size() - 1;
//            }
        }
        return 0;
    }

    public void upDateItemAdd(String path) {
        if (mLogoList == null) {
            return;
        }

        int builtInPicPosition = 0;
        //get built-in pic position
        for (MallLogoBean bean : mLogoList) {
            if (bean.type == MallLogoBean.PIC_FROM_USB || bean.type == MallLogoBean.PIC_GOTO_MEDIARCENTER) {
                break;
            }
            builtInPicPosition++;
        }
        LogUtils.d(TAG, "needAddToMallLogo builtInPicIndex: " + builtInPicPosition);
        mLogoList.add(builtInPicPosition, new MallLogoBean(MallLogoBean.PIC_FROM_USB, false, path));
        LogUtils.d(TAG, "needAddToMallLogo after add size: " + mLogoList.size());
//        List<MallLogoBean> tempList = new ArrayList<>(mLogoList);
//        mLogoList.clear();
//        mLogoList = tempList;
        notifyItemInserted(builtInPicPosition);
//        notifyDataSetChanged();
    }

    public void upDateItemDelete(String path) {
        if (mLogoList != null) {
            for (int i = 0; i < mLogoList.size(); i++) {
                MallLogoBean bean = mLogoList.get(i);
                if (bean.logoLocation.equals(path)) {
                    mLogoList.remove(bean);
                    notifyItemRemoved(i);
                    break;
                }
            }
//            Iterator<MallLogoBean> iterator = mLogoList.iterator();
//            while (iterator.hasNext()) {
//                MallLogoBean bean = iterator.next();
//                if (bean.logoLocation.equals(path)) {
////                    iterator.remove();
//                    LogUtils.d(TAG, "upDateItemDelete()  delete path:" + path);
//                    List<MallLogoBean> tempList = new ArrayList<>(mLogoList);
////                    mLogoList.clear();
//                    int index = mLogoList.indexOf(bean);
//                    mLogoList.remove(bean);
////                    mLogoList = tempList;
//                    for (int i = 0; i < mLogoList.size(); i++) {
//                        LogUtils.d(TAG, "upDateItemDelete()  finally should show picpath:" + mLogoList.get(i).logoLocation);
//                    }
//                    notifyItemRemoved(index);
////                    notifyDataSetChanged();
//                    return;
//                }
//            }
        }
    }

    static class MallLogoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_logo)
        protected ImageView ivLogo;
        @BindView(R.id.iv_check)
        protected ImageView ivCheck;
        @BindView(R.id.iv_delete)
        protected ImageView ivDelete;

        public MallLogoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface MallLogoListener {
        void gotoSelectMallLogoActivity();

        void onDeleteMallLogo(String strUri);
    }


}
