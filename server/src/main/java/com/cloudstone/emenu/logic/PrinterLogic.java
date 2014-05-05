/**
 * @(#)PrinterLogic.java, Aug 13, 2013. 
 *
 */

package com.cloudstone.emenu.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.constant.Const;
import com.cloudstone.emenu.data.Bill;
import com.cloudstone.emenu.data.DishRecord;
import com.cloudstone.emenu.data.PrintComponent;
import com.cloudstone.emenu.data.PrintTemplate;
import com.cloudstone.emenu.data.PrinterConfig;
import com.cloudstone.emenu.data.User;
import com.cloudstone.emenu.data.Vip;
import com.cloudstone.emenu.data.vo.DishGroup;
import com.cloudstone.emenu.data.vo.OrderDishVO;
import com.cloudstone.emenu.data.vo.OrderVO;
import com.cloudstone.emenu.data.vo.RecordVO;
import com.cloudstone.emenu.exception.NotFoundException;
import com.cloudstone.emenu.storage.dao.IPrintComponentDb;
import com.cloudstone.emenu.storage.dao.IPrintTemplateDb;
import com.cloudstone.emenu.storage.dao.IPrinterConfigDb;
import com.cloudstone.emenu.util.CollectionUtils;
import com.cloudstone.emenu.util.DataUtils;
import com.cloudstone.emenu.util.PrinterUtils;
import com.cloudstone.emenu.util.VelocityRender;
import com.cloudstone.emenu.wrap.DishWraper;
import com.cloudstone.emenu.wrap.RecordWraper;

/**
 * @author xuhongfeng
 */
@Component
public class PrinterLogic extends BaseLogic {

    private static final Logger LOG = LoggerFactory.getLogger(PrinterLogic.class);

    @Autowired
    private DishWraper dishWraper;

    @Autowired
    private RecordWraper recordWraper;

    @Autowired
    private MenuLogic menuLogic;

    @Autowired
    private RecordLogic recordLogic;

    @Autowired
    private VipLogic vipLogic;

    @Autowired
    private VelocityRender velocityRender;

    @Autowired
    private IPrintComponentDb printComponentDb;

    @Autowired
    private IPrintTemplateDb printTemplateDb;

    @Autowired
    private IPrinterConfigDb printerConfigDb;

    public String[] listPrinters() {
        return PrinterUtils.listPrinters();
    }

    public void printBill(EmenuContext context, Bill bill, User user) throws Exception {
        String[] printers = listPrinters();
        for (String printer : printers) {
            PrinterConfig config = getPrinterConfig(context, printer);
            if (config != null && config.isWhenBill()) {
                for (int templateId : config.getBillTemplateIds()) {
                    LOG.info("print templateId :" + templateId);
                    printBill(context, bill, user, printer, templateId);
                }
            }
        }
    }

    public void printBill(EmenuContext context, Bill bill, User user, String printer, int templateId)
            throws Exception {
        PrintTemplate template = getTemplate(context, templateId);
        if (template != null) {
            String templateString = getTemplateString(context, template, 0, template.getCutType());
            List<DishRecord> cancelRecords = recordLogic.listCancelDishRecords(context,
                    bill.getOrderId());
            List<DishRecord> addRecords = recordLogic
                    .listAddDishRecords(context, bill.getOrderId());
            List<RecordVO> cancelRecordVOs = recordWraper.wrapRecord(context, cancelRecords);
            List<RecordVO> addRecordVOs = recordWraper.wrapRecord(context, addRecords);
            Vip vip = vipLogic.get(context, bill.getVipId());

            if (template.getCutType() == Const.CutType.PER_DISH
                    && bill.getOrder().getDishes().size() > 0) {
                for (OrderDishVO dish : bill.getOrder().getDishes()) {
                    List<OrderDishVO> dishes = new LinkedList<OrderDishVO>();
                    dishes.add(dish);
                    List<DishGroup> dishGroups = dishWraper.wrapDishGroup(context, dishes,
                            template.getChapterIds(), true);
                    if (!CollectionUtils.isEmpty(dishGroups)) {
                        String content = velocityRender.renderBill(bill, vip, user, dishGroups,
                                cancelRecordVOs, addRecordVOs, templateString);
                        PrinterUtils.print(printer, content, template.getFontSize());
                    }
                }
            } else {
                List<DishGroup> dishGroups = dishWraper.wrapDishGroup(context, bill.getOrder()
                        .getDishes(), template.getChapterIds(), true);
                if (!CollectionUtils.isEmpty(dishGroups)) {
                    String content = velocityRender.renderBill(bill, vip, user, dishGroups,
                            cancelRecordVOs, addRecordVOs, templateString);
                    PrinterUtils.print(printer, content, template.getFontSize());
                }
            }
        }
    }

    public void printCancelOrder(EmenuContext context, OrderVO order, User user) throws Exception {
        String[] printers = listPrinters();
        for (String printer : printers) {
            PrinterConfig config = getPrinterConfig(context, printer);
            if (config != null && config.isWhenCancel()) {
                for (int templateId : config.getCancelTemplateIds()) {
                    LOG.info("print templateId :" + templateId);
                    printOrder(context, order, user, printer, templateId);
                }
            }
        }
    }

    public void printAddOrder(EmenuContext context, OrderVO order, User user) throws Exception {
        String[] printers = listPrinters();
        for (String printer : printers) {
            PrinterConfig config = getPrinterConfig(context, printer);
            if (config != null && config.isWhenAdd()) {
                for (int templateId : config.getAddTemplateIds()) {
                    LOG.info("print templateId :" + templateId);
                    printOrder(context, order, user, printer, templateId);
                }
            }
        }
    }

    public void printOrder(EmenuContext context, OrderVO order, User user) throws Exception {
        String[] printers = listPrinters();
        for (String printer : printers) {
            PrinterConfig config = getPrinterConfig(context, printer);
            if (config != null && config.isWhenOrdered()) {
                for (int templateId : config.getOrderedTemplateIds()) {
                    LOG.info("print templateId :" + templateId);
                    printOrder(context, order, user, printer, templateId);
                }
            }
        }
    }

    public void printOrder(EmenuContext context, OrderVO order, User user, String printer,
                           int templateId) throws Exception {
        PrintTemplate template = getTemplate(context, templateId);
        if (template != null) {
            String templateString = getTemplateString(context, template, 1, template.getCutType());
            if (template.getCutType() == Const.CutType.PER_DISH && order.getDishes().size() > 0) {
                for (OrderDishVO dish : order.getDishes()) {
                    List<OrderDishVO> dishes = new LinkedList<OrderDishVO>();
                    dishes.add(dish);
                    List<DishGroup> dishGroups = dishWraper.wrapDishGroup(context, dishes,
                            template.getChapterIds(), false);
                    if (!CollectionUtils.isEmpty(dishGroups)) {
                        String content = velocityRender.renderOrder(order, user, dishWraper
                                        .wrapDishGroup(context, dishes, template.getChapterIds(), false),
                                templateString
                        );
                        PrinterUtils.print(printer, content, template.getFontSize());
                    }
                }
            } else {
                List<DishGroup> dishGroups = dishWraper.wrapDishGroup(context, order.getDishes(),
                        template.getChapterIds(), false);
                if (!CollectionUtils.isEmpty(dishGroups)) {
                    String content = velocityRender.renderOrder(order, user, dishGroups,
                            templateString);
                    PrinterUtils.print(printer, content, template.getFontSize());
                }
            }
        }
    }

    public PrintTemplate getTemplate(EmenuContext context, int id) {
        PrintTemplate template = printTemplateDb.get(context, id);
        checkChapterIds(context, template);
        return template;
    }

    public List<PrintComponent> listComponents(EmenuContext context) {
        List<PrintComponent> list = printComponentDb.listAll(context);
        DataUtils.filterDeleted(list);
        return list;
    }

    public PrintComponent addComponent(EmenuContext context, PrintComponent data) {
        printComponentDb.add(context, data);
        return printComponentDb.get(context, data.getId());
    }

    public PrintComponent updateComponent(EmenuContext context, PrintComponent data) {
        PrintComponent old = printComponentDb.get(context, data.getId());
        if (old == null || old.isDeleted()) {
            throw new NotFoundException("Can not find page component");
        }
        printComponentDb.update(context, data);
        return printComponentDb.get(context, data.getId());
    }

    public PrintComponent getComponent(EmenuContext context, int id) {
        return printComponentDb.get(context, id);
    }

    public void deleteComponent(EmenuContext context, int id) {
        PrintComponent old = printComponentDb.get(context, id);
        if (old == null || old.isDeleted()) {
            throw new NotFoundException("Can not find page component");
        }
        printComponentDb.delete(context, id);
        printTemplateDb.removeComponent(context, id);
    }

    public List<PrintTemplate> listTemplate(EmenuContext context) {
        List<PrintTemplate> list = printTemplateDb.listAll(context);
        DataUtils.filterDeleted(list);
        checkChapterIds(context, list);
        return list;
    }

    private String getTemplateString(EmenuContext context, PrintTemplate template, int type,
                                     int cutType) {
        StringBuilder sb = new StringBuilder();
        int headerId = template.getHeaderId();
        int footerId = template.getFooterId();

        if (headerId != 0) {
            PrintComponent header = getComponent(context, headerId);
            if (header != null) {
                sb.append(header.getContent());
                sb.append(Const.DIVIDER);
            }
        }
        if (type == 0) {// Bill
            sb.append(Const.DISH_TEMPLATE);
        } else if (type == 1) {// Order
            if (cutType == Const.CutType.PER_DISH) {
                sb.append(Const.DISH_TEMPLATE_ORDER);
            } else {
                sb.append(Const.DISH_TEMPLATE);
            }
        }

        if (footerId != 0) {
            PrintComponent footer = getComponent(context, footerId);
            if (footer != null) {
                sb.append(Const.DIVIDER);
                sb.append(footer.getContent());
            }
        }
        return sb.toString();
    }

    private void checkChapterIds(EmenuContext context, List<PrintTemplate> templates) {
        for (PrintTemplate template : templates) {
            checkChapterIds(context, template);
        }
    }

    private void checkChapterIds(EmenuContext context, PrintTemplate template) {
        // check chapterId exists
        if (!CollectionUtils.isEmpty(template.getChapterIds())) {
            int[] chapterIds = menuLogic.getChapterIds(context);
            Arrays.sort(chapterIds);
            List<Integer> newIds = new LinkedList<Integer>();
            boolean needUpdate = false;
            for (int oldId : template.getChapterIds()) {
                if (Arrays.binarySearch(chapterIds, oldId) < 0) {
                    needUpdate = true;
                } else {
                    newIds.add(oldId);
                }
            }
            if (needUpdate) {
                template.setChapterIds(CollectionUtils.toIntArray(newIds));
                updateTemplate(context, template);
            }
        }
    }

    public PrintTemplate addTemplate(EmenuContext context, PrintTemplate template) {
        printTemplateDb.add(context, template);
        return printTemplateDb.get(context, template.getId());
    }

    public PrintTemplate updateTemplate(EmenuContext context, PrintTemplate template) {
        int headerId = template.getHeaderId();
        if (headerId != 0) {
            PrintComponent header = getComponent(context, headerId);
            if (header == null || header.isDeleted()) {
                throw new NotFoundException("该页眉不存在");
            }
        }
        int footerId = template.getFooterId();
        if (footerId != 0) {
            PrintComponent footer = getComponent(context, footerId);
            if (footer == null || footer.isDeleted()) {
                throw new NotFoundException("该页脚不存在");
            }
        }
        printTemplateDb.update(context, template);
        return printTemplateDb.get(context, template.getId());
    }

    public void deleteTemplate(EmenuContext context, int id) {
        printTemplateDb.delete(context, id);
        printerConfigDb.removeTemplate(context, id);
    }

    public PrinterConfig getPrinterConfig(EmenuContext context, String name) {
        PrinterConfig config = printerConfigDb.getConfig(context, name);
        if (config == null) {
            config = new PrinterConfig();
            config.setName(name);
            printerConfigDb.update(context, config);
        }
        return config;
    }

    public PrinterConfig getPrinterConfig(EmenuContext context, int id) {
        String[] printers = listPrinters();
        for (String p : printers) {
            if (p.hashCode() == id) {
                return getPrinterConfig(context, p);
            }
        }
        return null;
    }

    public PrinterConfig updatePrinterConfig(EmenuContext context, PrinterConfig config) {
        printerConfigDb.update(context, config);
        return printerConfigDb.getConfig(context, config.getName());
    }

    public List<PrinterConfig> listPrinterConfig(EmenuContext context) {
        String[] printers = listPrinters();
        List<PrinterConfig> configs = new ArrayList<PrinterConfig>();
        for (String name : printers) {
            configs.add(getPrinterConfig(context, name));
        }
        return configs;
    }
}
