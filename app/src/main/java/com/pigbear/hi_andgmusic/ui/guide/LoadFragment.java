package com.pigbear.hi_andgmusic.ui.guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 杨阳洋 on 2017/9/28.
 * usg:
 * 1.可以懒加载的fragment
 */

public abstract class LoadFragment extends Fragment{
    /**
     * 判断视图是否已经初始化
     */
    protected boolean isInitView = false;

    /**
     * 是否可以加载数据
     */
    protected boolean isLoadData = false;

    private View view;
    private boolean canLoadData;

    public LoadFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(setContentView(), container, false);
        if (view != null) {
            isInitView = true;
            isCanLoadData();
        }
        return view;
    }

    /**设置视图*/
    protected abstract int setContentView();

    /**
     * 告诉系统,fragment可见
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    public void isCanLoadData() {
        if(!isInitView) {
            return;
        }
        //视图用户可见
        if(getUserVisibleHint()) {
            lazyLoadData();
            isLoadData = true;
        }else{
            if(isLoadData) {
                stopLoad();
            }
        }
    }

    /**
     * 停止加载数据
     */
    protected void stopLoad(){}

    /**
     * 预加载数据
     */
    protected abstract void lazyLoadData();

    /**子类传递过来的视图*/
    protected View getContentView(){
        return view;
    }

    protected <T extends View> T findViewById(int id){
        return (T)getContentView().findViewById(id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInitView = false;
        isLoadData = false;
    }
}
