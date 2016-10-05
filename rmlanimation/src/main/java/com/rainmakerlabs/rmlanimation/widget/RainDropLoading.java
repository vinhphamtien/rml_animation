package com.rainmakerlabs.rmlanimation.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.eftimoff.androipathview.PathView;
import com.plattysoft.leonids.FixedParticleSystem;
import com.rainmakerlabs.rmlanimation.R;



/**
 * Created by vinh.phamtien on 10/4/2016.
 */
public class RainDropLoading extends Dialog {

    Handler j;
    Runnable r;
    Activity mContext;
    View anchorView;
    FixedParticleSystem rainDropAnimator;

    boolean drawFinished = false;

    public RainDropLoading(Context context) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        this.mContext = (Activity) context;
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.setContentView(R.layout.layout_dialog_rml_rain_drop);
        this.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        anchorView = this.findViewById(R.id.rain_drop);
        setCancelable(true);
    }

    public void resizeDialog(Dialog dialog,int currentHeight) {
        Rect displayRectangle = new Rect();
        Window window = dialog.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(
                displayRectangle);
        int targetHeight = (int) (displayRectangle
                .height() * 0.5f);
        WindowManager.LayoutParams layout = dialog.getWindow().getAttributes();
        layout.height =targetHeight + mContext.getResources().getDimensionPixelSize(R.dimen.transition_top);
        dialog.getWindow().setAttributes(layout);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        LinearLayout wrapLayout = (LinearLayout) findViewById(R.id.wrap_loading_dialog);
        resizeDialog(this,wrapLayout.getHeight());
    }

    @Override
    public void show() {
        super.show();
        final PathView pv = (PathView) findViewById(R.id.rain_drop);
        pv.setFillAfter(true);
        if(!drawFinished){
            pv.setVisibility(View.VISIBLE);
        }
        pv.getPathAnimator()
                .duration(700)
                .listenerStart(new PathView.AnimatorBuilder.ListenerStart() {
                    @Override
                    public void onAnimationStart() {
                        drawFinished = false;
                        pv.setVisibility(View.VISIBLE);
                    }
                })
                .listenerEnd(new PathView.AnimatorBuilder.ListenerEnd() {
                    @Override
                    public void onAnimationEnd() {
                        drawFinished = true;
                        rainDropStart();
                    }
                })
                .interpolator(new DecelerateInterpolator())
                .start();
    }


    private void safeStop(){
        if(j!=null){
            j.removeCallbacksAndMessages(null);
        }
        if(rainDropAnimator!=null){
            rainDropAnimator.superCancel();
        }
        findViewById(R.id.rain_drop).setVisibility(View.INVISIBLE);
        j = null;
        r = null;

    }

    private void rainDropStart() {
        if(j == null){
            j = new Handler();
        }


        refreshAnimator();

        r = new Runnable() {
            @Override
            public void run() {
                rainDropAnimator.cancel();
                refreshAnimator();
                rainDropAnimator.emitWithGravity(anchorView, Gravity.TOP, 3);

            }
        };

        j.post(new Runnable() {
            @Override
            public void run() {;
                r.run();
                j.postDelayed(r,4100);
            }
        });

    }

    private void refreshAnimator(){
        rainDropAnimator = new FixedParticleSystem(mContext, 12, R.drawable.ic_rain_drop, 3900)
                .setParentViewGroup((ViewGroup) anchorView.getParent())
                .setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f)
                .setAcceleration(0.00005f, 90);
    }

    @Override
    public void onDetachedFromWindow() {
        safeStop();
        super.onDetachedFromWindow();

    }
}
