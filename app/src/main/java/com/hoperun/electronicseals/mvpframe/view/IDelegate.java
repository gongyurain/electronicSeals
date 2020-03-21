package com.hoperun.electronicseals.mvpframe.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * @Description: 视图接口
 * @Author: gongyu
 * @CreateDate: 2020/3/21 17:05
 */

public interface IDelegate {

    void create(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle);

    void initData();

    void onDestroy();
}
