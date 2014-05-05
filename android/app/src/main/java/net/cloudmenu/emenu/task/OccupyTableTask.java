package net.cloudmenu.emenu.task;

import android.content.Context;

import net.cloudmenu.emenu.net.RPCHelper;
import net.cloudmenu.emenu.utils.GlobalConfig;
import net.cloudmenu.emenu.utils.ProfileHolder;

import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;

import cn.buding.common.exception.ECode;
import cn.com.cloudstone.menu.server.thrift.api.AException;
import cn.com.cloudstone.menu.server.thrift.api.IWaiterService;
import cn.com.cloudstone.menu.server.thrift.api.PermissionDenyExcpetion;
import cn.com.cloudstone.menu.server.thrift.api.TableOccupiedException;
import cn.com.cloudstone.menu.server.thrift.api.UserNotLoginException;

/**
 * Created by nicholaszhao on 4/13/14.
 */

public class OccupyTableTask extends TBaseTask {
    private String mTableId;
    private int mNum;

    public OccupyTableTask(Context context, String tableId, int number) {
        super(context);
        setShowCodeMsg(true);
        mTableId = tableId;
        mNum = number;
    }

    @Override
    protected TServiceClient getClient() throws TException {
        return RPCHelper.getWaiterService(mContext);
    }

    @Override
    protected Object process(TServiceClient client) throws TException,
            AException {
        IWaiterService.Client iclient = (IWaiterService.Client) client;
        try {
            if (!GlobalConfig.isWorkWithoutNetWork(mContext)) {
                String sid = ProfileHolder.getIns().getCurrentSid(mContext);
                iclient.occupyTable(sid, mTableId, mNum);
            }
            return ECode.SUCCESS;
        } catch (UserNotLoginException e) {
            return e;
        } catch (PermissionDenyExcpetion e) {
            return e;
        } catch (TableOccupiedException e) {
            return e;
        }
    }

}