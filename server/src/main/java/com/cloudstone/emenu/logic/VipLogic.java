package com.cloudstone.emenu.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.Vip;
import com.cloudstone.emenu.data.VipHistory;
import com.cloudstone.emenu.exception.DataConflictException;
import com.cloudstone.emenu.storage.sqlitedb.VipDb;
import com.cloudstone.emenu.storage.sqlitedb.VipHistoryDb;
import com.cloudstone.emenu.util.DataUtils;
import com.cloudstone.emenu.util.MathUtil;

/**
 * @author carelife
 */
@Component
public class VipLogic extends BaseLogic {
    @Autowired
    private VipDb vipDb;
    @Autowired
    private VipHistoryDb vipHistoryDb;

    public List<Vip> getAll(EmenuContext context) {
        List<Vip> vips = vipDb.getAll(context);
        DataUtils.filterDeleted(vips);
        return vips;
    }

    public Vip get(EmenuContext context, int id) {
        return vipDb.get(context, id);
    }

    public Vip add(EmenuContext context, Vip vip) {
        Vip oldVip = vipDb.getByName(context, vip.getName());
        if (oldVip != null && !oldVip.isDeleted()) {
            throw new DataConflictException("用户名已存在");
        }
        long now = System.currentTimeMillis();
        vip.setUpdateTime(now);
        if (oldVip != null) {
            vip.setId(oldVip.getId());
            vip.setCreatedTime(oldVip.getCreatedTime());
            return vipDb.update(context, vip);
        } else {
            vip.setCreatedTime(now);
            return vipDb.add(context, vip);
        }
    }

    public Vip update(EmenuContext context, Vip vip) {
        Vip oldVip = vipDb.get(context, vip.getId());
        if (oldVip == null || oldVip.isDeleted()) {
            throw new DataConflictException("不存在用户");
        }
        long now = System.currentTimeMillis();
        vip.setUpdateTime(now);
        return vipDb.update(context, vip);
    }

    public boolean delete(EmenuContext context, int id) {
        Vip vip = vipDb.get(context, id);
        if (vip == null) {
            throw new DataConflictException("不存在该用户");
        }
        vip.setDeleted(true);
        Vip after = vipDb.update(context, vip);
        if (after != null) {
            return true;
        } else {
            return false;
        }
    }

    public double recharge(EmenuContext context, int id, double recharge) {
        Vip vip = vipDb.get(context, id);
        if (vip == null || vip.isDeleted()) {
            throw new DataConflictException("不存在该用户");
        }
        double leftMoney = MathUtil.round(
                MathUtil.add(vip.getMoney(), recharge), 2);
        leftMoney = vipDb.recharge(context, id, leftMoney);
        VipHistory vipHistory = new VipHistory();
        vipHistory.setVipid(id);
        vipHistory.setRecharge(recharge);
        vipHistory.setLeft(leftMoney);
        vipHistory.setOpTime(System.currentTimeMillis());
        addVipHistory(context, vipHistory);
        return leftMoney;
    }

    public List<String> listVipNames(EmenuContext context) {
        List<Vip> vips = getAll(context);
        List<String> names = new ArrayList<String>(vips.size());
        for (Vip vip : vips) {
            names.add(vip.getName());
        }
        return names;
    }

    // ----------------History----------------//
    public void addVipHistory(EmenuContext context, VipHistory vipHistory) {
        vipHistoryDb.add(context, vipHistory);
    }

    public List<VipHistory> getHistoryByVipId(EmenuContext context, int vipid) {
        List<VipHistory> vipHistoryList = vipHistoryDb.get(context, vipid);
        DataUtils.filterDeleted(vipHistoryList);
        return vipHistoryList;
    }

    public List<VipHistory> getAllHistory(EmenuContext context) {
        List<VipHistory> vipHistoryList = vipHistoryDb.getAll(context);
        DataUtils.filterDeleted(vipHistoryList);
        return vipHistoryList;
    }
}
