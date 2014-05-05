package com.heymenu.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.heymenu.activity.Login;
import com.heymenu.tasks.LogoutTask;

import net.cloudmenu.emenu.R;

import cn.buding.common.asynctask.HandlerMessageTask;

/**
 * Created by nicholaszhao on 4/20/14.
 */
public class BackConfirmDialog extends Dialog {

    public BackConfirmDialog(final Activity activity) {
        super(activity, R.style.Theme_Dialog);
        setContentView(R.layout.dialog_back);

        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                activity.finish();
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
