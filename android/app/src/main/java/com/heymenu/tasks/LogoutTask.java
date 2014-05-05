package com.heymenu.tasks;

import android.content.Context;
import android.util.Log;

import net.cloudmenu.emenu.net.RPCHelper;
import net.cloudmenu.emenu.task.TBaseTask;
import net.cloudmenu.emenu.utils.GlobalConfig;
import net.cloudmenu.emenu.utils.ProfileHolder;

import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;

import cn.buding.common.exception.ECode;
import cn.com.cloudstone.menu.server.thrift.api.IProfileService;

/**
 * Created by nicholaszhao on 4/17/14.
 */
public class LogoutTask extends TBaseTask {
    private Context context;

    public LogoutTask(Context context) {
        super(context);
        this.context = context;
        setShowProgessDialog(true);
    }

    @Override
    protected TServiceClient getClient() throws TException {
        return RPCHelper.getProfileService(context);
    }

    @Override
    protected Object process(TServiceClient client) throws TException {
        IProfileService.Client iclient = (IProfileService.Client) client;
        try {
            if (!GlobalConfig.isWorkWithoutNetWork(mContext)) {
                String sid = ProfileHolder.getIns().getCurrentSid(mContext);
                iclient.logout(sid);
            }
        } catch (Throwable e) {
            Log.e("Logout", "failed", e);
        }
        ProfileHolder.getIns().logout(mContext);
        return ECode.SUCCESS;
    }

}