package com.hoperun.electronicseals.qrcodemoduel;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hoperun.electronicseals.view.activity.LoginActivity;
import com.hoperun.electronicseals.view.activity.MainActivity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by Administrator on 2017/7/3 0003.
 * Activity的基类
 */

public class QRCodeBaseActivity extends AppCompatActivity {

    MaterialDialog materialDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (materialDialog != null) {
            if (materialDialog.isShowing()) {
                materialDialog.dismiss();
            }
        }
        super.onDestroy();
    }

    /**
     * TODO 判断登录的状态 并进行检查
     */
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /******************************************************************************/
    /**
     * toast
     *
     * @param message 待显示的信息
     */
    public void toast(String message) {
        mHandler.sendMessage(mHandler.obtainMessage(HANDLER_TOAST, message));
    }

    /**
     * 显示确认对话框
     *
     * @param message 待显示的信息
     */
    public void showConfirmDialog(String message) {
        mHandler.sendMessage(mHandler.obtainMessage(HANDLER_CONFIRM_DIALOG, message));
    }

    /**
     * 显示进度对话框
     *
     * @param message 待显示的信息
     */
    public void showProgressDialog(String message) {
        mHandler.sendMessage(mHandler.obtainMessage(HANDLER_PROGRESS_DIALOG, message));
    }

    /**
     * 显示结束对话框，按确认键后退出Activity
     *
     * @param message 待显示的信息
     */
    public void showFinishDialog(String message) {
        mHandler.sendMessage(mHandler.obtainMessage(HANDLER_FINISH_DIALOG, message));
    }

    /**
     * 显示不可取消的不确定的进度对话框
     *
     * @param message 待显示的信息
     */
    public void showIndeterminateProgressDialog(String message) {
        mHandler.sendMessage(mHandler.obtainMessage(HANDLER_INDETERMINATE_PROGRESS_DIALOG, message));
    }

    /**
     * 显示结束对话框，按确认键后退到MainActivity界面
     *
     * @param message 待显示的信息
     */
    public void showFinishToMainDialog(String message) {
        mHandler.sendMessage(mHandler.obtainMessage(HANDLER_FINISH_TO_MAIN_DIALOG, message));
    }

    /**
     * 显示结束对话框，按确认键后退到login界面
     *
     * @param message 待显示的信息
     */
    public void showFinishToLoginDialog(String message) {
        mHandler.sendMessage(mHandler.obtainMessage(HANDLER_FINISH_TO_LOGIN_DIALOG, message));
    }

    /**
     * 取消对话框
     */
    public void dismissDialog() {
        mHandler.sendEmptyMessage(HANDLER_DISMISS_DIALOG);
    }
//
//    public void startMainActivity(){
//        mHandler.sendEmptyMessage()
//    }

    private final int HANDLER_TOAST = 0;
    private final int HANDLER_CONFIRM_DIALOG = 1;
    private final int HANDLER_PROGRESS_DIALOG = 2;
    private final int HANDLER_FINISH_DIALOG = 3;
    private final int HANDLER_INDETERMINATE_PROGRESS_DIALOG = 4;
    private final int HANDLER_DISMISS_DIALOG = 5;
    private final int HANDLER_FINISH_TO_MAIN_DIALOG = 6;
    private final int HANDLER_FINISH_TO_LOGIN_DIALOG = 7;

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what >= HANDLER_CONFIRM_DIALOG && msg.what <= HANDLER_FINISH_TO_LOGIN_DIALOG) {
                if (materialDialog != null) {
                    if (materialDialog.isShowing()) {
                        materialDialog.dismiss();
                    }
                }
            }
            if (QRCodeBaseActivity.this.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case HANDLER_TOAST:
                    Toast.makeText(QRCodeBaseActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case HANDLER_CONFIRM_DIALOG:
                    materialDialog = new MaterialDialog.Builder(QRCodeBaseActivity.this)
                            .content((String) msg.obj)
                            .positiveText("确定")
                            .show();
                    break;
                case HANDLER_PROGRESS_DIALOG:
                    materialDialog = new MaterialDialog.Builder(QRCodeBaseActivity.this)
                            .content((String) msg.obj)
                            .progress(true, 0)
                            .show();
                    break;
                case HANDLER_FINISH_DIALOG:
                    materialDialog = new MaterialDialog.Builder(QRCodeBaseActivity.this)
                            .content((String) msg.obj)
                            .positiveText("确定")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    QRCodeBaseActivity.this.finish();
                                }
                            })
                            .show();
                    break;
                case HANDLER_INDETERMINATE_PROGRESS_DIALOG:
                    materialDialog = new MaterialDialog.Builder(QRCodeBaseActivity.this)
                            .content((String) msg.obj)
                            .progress(true, 0)
                            .cancelable(false)
                            .show();
                    break;
                case HANDLER_FINISH_TO_MAIN_DIALOG:
                    materialDialog = new MaterialDialog.Builder(QRCodeBaseActivity.this)
                            .content((String) msg.obj)
                            .positiveText("确定")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent intent = new Intent(QRCodeBaseActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
                    break;
                case HANDLER_FINISH_TO_LOGIN_DIALOG:
                    materialDialog = new MaterialDialog.Builder(QRCodeBaseActivity.this)
                            .content((String) msg.obj)
                            .positiveText("确定")
                            .cancelable(false)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent intent = new Intent(QRCodeBaseActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 监听是否点击了home键将客户端推到后台
     */
    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
//		private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
//		private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";	// 长按Home键 或者 activity切换键
//		private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";			// 短按Home键
//		private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";					// 锁屏
//		private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";				// samsung 长按Home键

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // android.intent.action.CLOSE_SYSTEM_DIALOGS
//				String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
//				if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
//					// 短按Home键
//				}
//				else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
//					// 长按Home键 或者 activity切换键
//				}
//				else if (SYS
// TEM_DIALOG_REASON_LOCK.equals(reason)) {
//					// 锁屏
//				}
//				else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
//					// samsung 长按Home键
//				}
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                unregisterReceiver(mHomeKeyEventReceiver);
            }
        }
    };

    // TODO: 2016-11-16  为了把标题居中，使用了自定义view。后续有更好的设置actionbar的属性，让标题可以居中更好
//    public void buildCustomActionBar(String title, boolean hasBackButton, boolean hasSettingButton) {
//            View viewTitleBar = getLayoutInflater().inflate(R.layout.actionbar_view, null);
//            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
//                    ActionBar.LayoutParams.MATCH_PARENT,
//                    ActionBar.LayoutParams.MATCH_PARENT,
//                    Gravity.CENTER);
//            ActionBar actionBar = getSupportActionBar();
//            actionBar.setCustomView(viewTitleBar, lp);
//            actionBar.setDisplayShowHomeEnabled(false); //去掉导航
//            actionBar.setDisplayShowTitleEnabled(false);//去掉标题
//            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//            actionBar.setDisplayShowCustomEnabled(true);
//            TextView tvTitle = (TextView) actionBar.getCustomView().findViewById(R.id.action_bar_title);
//            tvTitle.setText(title);
//            LinearLayout leftImageButton = (LinearLayout) actionBar.getCustomView().findViewById(R.id.action_bar_left_btn);
//            if (!hasBackButton) {
//                leftImageButton.setVisibility(View.INVISIBLE);
//            } else {
//                leftImageButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        QRCodeBaseActivity.this.finish();
//                    }
//                });
//            }
//            ImageButton rightButton = (ImageButton) actionBar.getCustomView().findViewById(R.id.action_bar_right_btn);
//            if (!hasSettingButton) {
//                rightButton.setVisibility(View.INVISIBLE);
//            } else {
//                rightButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                    Intent intent = new Intent(QRCodeBaseActivity.this, SettingActivity.class);
////                    intent.putExtra(getResources().getString(R.string.title_name), getResources().getString(R.string.action_settings));
//                        //                   startActivity(intent);
//                    }
//                });
//            }
//    }
}
