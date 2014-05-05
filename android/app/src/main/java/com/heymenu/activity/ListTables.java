package com.heymenu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.heymenu.dialog.GuestNumberDialog;
import com.heymenu.dialog.LogoutConfirmDialog;
import com.heymenu.tasks.LogoutTask;
import com.heymenu.util.IntentConsts;

import net.cloudmenu.emenu.R;
import net.cloudmenu.emenu.task.OccupyTableTask;
import net.cloudmenu.emenu.task.QueryOrderTask;
import net.cloudmenu.emenu.task.QueryTableInfoTask;
import net.cloudmenu.emenu.utils.GlobalValue;
import net.cloudmenu.emenu.utils.ProfileHolder;

import java.util.ArrayList;
import java.util.List;

import cn.buding.common.asynctask.HandlerMessageTask;
import cn.com.cloudstone.menu.server.thrift.api.TableInfo;
import cn.com.cloudstone.menu.server.thrift.api.TableStatus;

/**
 * Created by nicholaszhao on 4/13/14.
 */
public class ListTables extends Activity implements AdapterView.OnItemClickListener {

    private List<TableInfo> mTableInfos;
    private BaseAdapter mAdapter;
    private GridView mTablesView;
    private QueryTableInfoTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!ProfileHolder.getIns().isLogined(this)) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_list_tables);
        mTableInfos = new ArrayList<TableInfo>();
        mAdapter = new TableInfoAdapter(mTableInfos);
        mTablesView = (GridView) findViewById(R.id.gridview);

        mTablesView.setAdapter(mAdapter);
        mTablesView.setOnItemClickListener(this);
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogoutConfirmDialog(ListTables.this).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if (mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING)
            return;
        mTask = new QueryTableInfoTask(this);
        mTask.setForseRefresh(true);
        mTask.setCallback(new HandlerMessageTask.Callback() {
            @Override
            public void onSuccess(HandlerMessageTask task, Object t) {
                mTableInfos.clear();
                mTableInfos.addAll(mTask.getInfos());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(HandlerMessageTask task, Object t) {
            }
        });
        mTask.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int p, long arg3) {
        sendToMentu(p);
    }

    private void sendToMentu(int selectedTableIndex) {
        final TableInfo info = mTableInfos.get(selectedTableIndex);
        final String tableId = "" + info.getId();
        if (info != null && info.getStatus() != TableStatus.Empty) {

            final QueryOrderTask orderTask = new QueryOrderTask(this,
                    tableId);
            orderTask.setCallback(new HandlerMessageTask.Callback() {
                @Override
                public void onSuccess(HandlerMessageTask task, Object t) {
                    GlobalValue.getIns().setOrder(orderTask.getOrder());
                    ProfileHolder.getIns().setCurrentTableId(ListTables.this,
                            info.getId());
                    Intent intent = newMenuIntent(tableId);
                    ListTables.this.startActivity(intent);
                }

                @Override
                public void onFail(HandlerMessageTask task, Object t) {

                }
            });
            orderTask.execute();
        } else {
            GuestNumberDialog guestNumberDialog = new GuestNumberDialog(this, tableId);
            guestNumberDialog.show();
        }
    }

    private Intent newMenuIntent(String tableId) {
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra(IntentConsts.TABLE_ID, tableId);
        return intent;
    }

    private class TableInfoAdapter extends BaseAdapter {
        private List<TableInfo> mTableInfos;

        public TableInfoAdapter(List<TableInfo> info) {
            mTableInfos = info;
        }

        @Override
        public int getCount() {
            return mTableInfos.size();
        }

        @Override
        public Object getItem(int p) {
            return mTableInfos.get(p);
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int p, View view, ViewGroup parent) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) ListTables.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item_table_info, parent, false);
            }
            TextView tvTable = (TextView) view.findViewById(R.id.tv_table);
            TableInfo info = mTableInfos.get(p);
            tvTable.setText(info.getId());
            TableStatus status = info.getStatus();
            if (status != null) {
                switch (status) {
                    case Empty:
                        tvTable.setEnabled(true);
                        tvTable.setSelected(false);
                        break;
                    case Occupied:
                        tvTable.setEnabled(false);
                        tvTable.setSelected(false);
                        break;
                    case Ordered:
                        tvTable.setSelected(true);
                        break;
                }
            }
            return view;
        }

    }

}
