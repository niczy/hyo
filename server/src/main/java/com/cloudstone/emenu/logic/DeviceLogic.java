/**
 * @(#)DeviceLogic.java, Aug 4, 2013. 
 *
 */
package com.cloudstone.emenu.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.Pad;
import com.cloudstone.emenu.exception.DataConflictException;
import com.cloudstone.emenu.storage.dao.IPadDb;
import com.cloudstone.emenu.util.DataUtils;

/**
 * @author xuhongfeng
 */
@Component
public class DeviceLogic extends BaseLogic {
    @Autowired
    private IPadDb padDb;
    @Autowired
    private ConfigLogic configLogic;
    @Autowired
    private ThriftLogic thriftLogic;

    public Pad getPad(EmenuContext context, int id) {
        return padDb.get(context, id);
    }

    public List<Pad> listAllPad(EmenuContext context) {
        List<Pad> pads = padDb.listAll(context);
        DataUtils.filterDeleted(pads);
        return pads;
    }

    public Pad addPad(EmenuContext context, Pad pad) {
        List<Pad> pads = padDb.listAll(context);
        Pad sameImei = null;
        Pad sameName = null;
        int count = 0;
        for (Pad p : pads) {
            if (p.getName().equals(pad.getName())) {
                sameName = p;
                if (!p.isDeleted()) {
                    throw new DataConflictException("Tablet name conflicts!");
                }
            }
            if (p.getImei().equals(pad.getImei())) {
                sameImei = p;
                if (!p.isDeleted()) {
                    throw new DataConflictException("Tablet IMEI conflicts!");
                }
            }
        }
        Pad old = sameImei != null ? sameImei : sameName;
        long now = System.currentTimeMillis();
        pad.setUpdateTime(now);
        if (old != null) {
            pad.setId(old.getId());
            padDb.update(context, pad);
        } else {
            pad.setCreatedTime(now);
            padDb.add(context, pad);
        }
        thriftLogic.onPadChanged();
        return getPad(context, pad.getId());
    }

    public Pad updatePad(EmenuContext context, Pad pad) {
        List<Pad> pads = padDb.listAll(context);
        for (Pad p : pads) {
            if (p.getName().equals(pad.getName())) {
                if (!p.isDeleted() && p.getId() != pad.getId()) {
                    throw new DataConflictException("Device name already exists");
                }
            }
            if (p.getImei().equals(pad.getImei()) && p.getId() != pad.getId()) {
                if (!p.isDeleted()) {
                    throw new DataConflictException("Device IMEI already exists");
                }
            }
        }
        long now = System.currentTimeMillis();
        pad.setUpdateTime(now);
        padDb.update(context, pad);
        thriftLogic.onPadChanged();
        return getPad(context, pad.getId());
    }

    public void deletePad(EmenuContext context, int id) {
        padDb.delete(context, id);
        thriftLogic.onPadChanged();
    }

    public Pad getPad(EmenuContext context, String imei) {
        List<Pad> pads = listAllPad(context);
        for (Pad pad : pads) {
            if (pad.getImei().equals(imei)) {
                return pad;
            }
        }
        return null;
    }
}
