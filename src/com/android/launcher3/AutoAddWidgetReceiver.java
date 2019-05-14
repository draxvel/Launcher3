package com.android.launcher3;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.android.launcher3.compat.AppWidgetManagerCompat;
import com.android.launcher3.compat.LauncherAppsCompatVO;
import com.android.launcher3.dragndrop.PinItemDragListener;
import com.android.launcher3.model.WidgetItem;
import com.android.launcher3.userevent.nano.LauncherLogProto;
import com.android.launcher3.util.InstantAppResolver;
import com.android.launcher3.widget.PendingAddWidgetInfo;
import com.android.launcher3.widget.WidgetHostViewLoader;

public class AutoAddWidgetReceiver extends BroadcastReceiver {

    private static final String TAG = "AutoAddWidgetReceiver";

    public static final String ACTION_AUTOADDWIDGET = "com.android.launcher3.action.AUTOADDWIDGET";

    public static final String EXTRA_PACKAGE_NAME = "packageName";

    public static final String EXTRA_CLASS_NAME = "className";

    private LauncherApps.PinItemRequest mRequest;
    private LauncherAppState mApp;
    private InvariantDeviceProfile mIdp;

    // Widget request specific options.
    private LauncherAppWidgetHost mAppWidgetHost;
    private AppWidgetManagerCompat mAppWidgetManager;
    private PendingAddWidgetInfo mPendingWidgetInfo;
    private int mPendingBindWidgetId;
    private Bundle mWidgetOptions;


    public AutoAddWidgetReceiver() {
    }

    public Intent createShortcutResultIntent(@NonNull ShortcutInfo inShortcut, int userId) {
        // Find the default launcher activity
//        final int launcherUserId = mService.getParentOrSelfUserId(userId);
//        final ComponentName defaultLauncher = mService.getDefaultLauncher(launcherUserId);
//        if (defaultLauncher == null) {
//            Log.e(TAG, "Default launcher not found.");
//            return null;
//        }
//
//        // Make sure the launcher user is unlocked. (it's always the parent profile, so should
//        // really be unlocked here though.)
//        mService.throwIfUserLockedL(launcherUserId);
//
//        // Next, validate the incoming shortcut, etc.
//        final LauncherApps.PinItemRequest request = requestPinShortcutLocked(inShortcut, null,
//                Pair.create(defaultLauncher, launcherUserId));
//        return new Intent().putExtra(LauncherApps.EXTRA_PIN_ITEM_REQUEST, request);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "I'm in AutoAddWidgetReceiver", Toast.LENGTH_SHORT).show();

        if (ACTION_AUTOADDWIDGET.equals(intent.getAction())) {
            String packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
            String className = intent.getStringExtra(EXTRA_CLASS_NAME);
            Log.d(TAG, "onReceive: packageName "+packageName);
            Log.d(TAG, "onReceive: className "+className);



            mRequest = LauncherAppsCompatVO.getPinItemRequest(intent);
            if (mRequest == null) {
                Log.d(TAG, "onReceive: mRequest == null");
            }


            mApp = LauncherAppState.getInstance(context);
            mIdp = mApp.getInvariantDeviceProfile();


        }
    }

    private boolean setupWidget() {
        LauncherAppWidgetProviderInfo widgetInfo = LauncherAppWidgetProviderInfo
                .fromProviderInfo(this, mRequest.getAppWidgetProviderInfo(this));
        if (widgetInfo.minSpanX > mIdp.numColumns || widgetInfo.minSpanY > mIdp.numRows) {
            // Cannot add widget
            return false;
        }
        mWidgetCell.setPreview(PinItemDragListener.getPreview(mRequest));

        mAppWidgetManager = AppWidgetManagerCompat.getInstance(this);
        mAppWidgetHost = new LauncherAppWidgetHost(this);

        mPendingWidgetInfo = new PendingAddWidgetInfo(widgetInfo);
        mPendingWidgetInfo.spanX = Math.min(mIdp.numColumns, widgetInfo.spanX);
        mPendingWidgetInfo.spanY = Math.min(mIdp.numRows, widgetInfo.spanY);
        mWidgetOptions = WidgetHostViewLoader.getDefaultOptionsForWidget(this, mPendingWidgetInfo);

        WidgetItem item = new WidgetItem(widgetInfo, getPackageManager(), mIdp);
        mWidgetCell.getWidgetView().setTag(mPendingWidgetInfo);
        mWidgetCell.applyFromCellItem(item, mApp.getWidgetCache());
        mWidgetCell.ensurePreview();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void acceptWidget(int widgetId) {
        InstallShortcutReceiver.queueWidget(mRequest.getAppWidgetProviderInfo(this), widgetId, this);
        mWidgetOptions.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        mRequest.accept(mWidgetOptions);
    }
}
