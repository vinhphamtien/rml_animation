package com.rainmakerlabs.pulltorefresh;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;


/**
 * Created by vinh.phamtien on 9/26/2016.
 */
public class RmlLoadingDialog extends ProgressDialog {

    Activity liveAct ;

    public RmlLoadingDialog(Context context) {
        super(context);
        liveAct = (Activity) context;
    }

    public RmlLoadingDialog(Context context, int theme) {
        super(context, theme);
        liveAct = (Activity) context;
    }

    @Override
    public void show() {
        super.show();

        View v = new View(liveAct);

    }
}
