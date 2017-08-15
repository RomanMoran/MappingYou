package romanmoran.com.mappingyou.utility;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import romanmoran.com.mappingyou.R;


/**
 * Created by roman on 14.07.2017.
 */

public class ProgressDialog {

    public static Dialog getWaitDialog(Context context, boolean isShow) {
        Dialog dialog = new Dialog(context,R.style.ThemeDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_wait);
        dialog.setCancelable(false);
        ImageView circleProgressBar = (ImageView) dialog.findViewById(R.id.circleProgressBar);
        initRotate(circleProgressBar);
        try {
            if (isShow) {
                try {
                    dialog.show();
                } catch (Exception ignored) {

                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dialog;
    }

    public static void closeDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private static void initRotate(ImageView circleProgressBar) {
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(700);
        circleProgressBar.startAnimation(anim);
    }

}
