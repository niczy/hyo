package com.heymenu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.heymenu.activity.Menu;
import com.heymenu.util.IntentConsts;

import net.cloudmenu.emenu.R;
import net.cloudmenu.emenu.task.OccupyTableTask;

import cn.buding.common.asynctask.HandlerMessageTask;
import cn.com.cloudstone.menu.server.thrift.api.TableInfo;

/**
 * Created by nicholaszhao on 4/20/14.
 */
public class GuestNumberDialog  extends Dialog {

    private String mTableId;
    private TextView mGuestNumber;
    private TextView mNumberError;
    private TextView mTableName;
    public GuestNumberDialog(Context context, String tableId) {
        super(context, R.style.Theme_Dialog);
        setContentView(R.layout.dialog_guest_number);
        mTableId = tableId;
        mGuestNumber = (TextView) findViewById(R.id.guest_number);
        mNumberError = (TextView) findViewById(R.id.error);
        mTableName = (TextView) findViewById(R.id.table_name);
        mTableName.setText("Table " + tableId);
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumberError.setVisibility(View.GONE);
                String guestNumberStr = mGuestNumber.getText().toString();
                int guestNumber = 0;
                try {
                    guestNumber = Integer.parseInt(guestNumberStr);
                    occupyTable(guestNumber);
                    dismiss();
                } catch (Exception  e) {
                    mNumberError.setVisibility(View.VISIBLE);
                    mNumberError.setText("Please enter a valid number.");
                }
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public void occupyTable(int guestNumber) {
        OccupyTableTask task = new OccupyTableTask(getContext(), mTableId,
                guestNumber);
        task.setCallback(new HandlerMessageTask.Callback() {
            @Override
            public void onSuccess(HandlerMessageTask task, Object t) {
                Intent intent = newMenuIntent(mTableId);
                getContext().startActivity(intent);
            }

            @Override
            public void onFail(HandlerMessageTask task, Object t) {
            }
        });
        task.execute();
    }

    private Intent newMenuIntent(String tableId) {
        Intent intent = new Intent(getContext(), Menu.class);
        intent.putExtra(IntentConsts.TABLE_ID, tableId);
        return intent;
    }
}
