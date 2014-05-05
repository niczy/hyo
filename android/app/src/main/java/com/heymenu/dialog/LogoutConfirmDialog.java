package com.heymenu.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.heymenu.activity.ListTables;
import com.heymenu.activity.Login;
import com.heymenu.activity.Menu;
import com.heymenu.tasks.LogoutTask;
import com.heymenu.util.IntentConsts;

import net.cloudmenu.emenu.R;
import net.cloudmenu.emenu.task.OccupyTableTask;

import cn.buding.common.asynctask.HandlerMessageTask;

/**
 * Created by nicholaszhao on 4/20/14.
 */
public class LogoutConfirmDialog extends Dialog {

    public LogoutConfirmDialog(final Context context) {
        super(context, R.style.Theme_Dialog);
        setContentView(R.layout.dialog_logout);

        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutTask task = new LogoutTask(context);
                task.setCallback(new HandlerMessageTask.Callback() {
                    @Override
                    public void onSuccess(HandlerMessageTask task, Object t) {
                        Intent intent = new Intent(context, Login.class);
                        context.startActivity(intent);
                        dismiss();
                        ((Activity) context).finish();
                    }

                    @Override
                    public void onFail(HandlerMessageTask task, Object t) {

                    }
                });
                task.execute();
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
