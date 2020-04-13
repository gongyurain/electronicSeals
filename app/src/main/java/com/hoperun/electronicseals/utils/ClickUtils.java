package com.hoperun.electronicseals.utils;

import android.util.Log;

public class ClickUtils {
    private static final int MIN_CLICK_DELAY_TIME = 2000;
    private static long lastClickTime = 0L;

    public static boolean isFastClick() {
        boolean flag = true;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = curClickTime;
        Log.e("gongyu", "sw  " + flag);
        return flag;
    }
}
