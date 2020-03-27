package com.hoperun.electronicseals.utils;

import java.util.Calendar;

public class ClickUtils {
    private static long lastClickTime = 0;
    private static final int CLICK_DELAY_TIME = 1000;

    public static boolean isFastClick() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime >= CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return false;
        } else {
            return true;
        }
    }
}
