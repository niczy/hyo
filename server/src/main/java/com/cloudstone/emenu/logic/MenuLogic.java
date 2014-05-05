/**
 * @(#)MenuLogic.java, 2013-7-8. 
 *
 */
package com.cloudstone.emenu.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.constant.Const;
import com.cloudstone.emenu.data.Chapter;
import com.cloudstone.emenu.data.Dish;
import com.cloudstone.emenu.data.DishNote;
import com.cloudstone.emenu.data.DishTag;
import com.cloudstone.emenu.data.IdName;
import com.cloudstone.emenu.data.Menu;
import com.cloudstone.emenu.data.MenuPage;
import com.cloudstone.emenu.exception.BadRequestError;
import com.cloudstone.emenu.exception.DataConflictException;
import com.cloudstone.emenu.exception.DbNotFoundException;
import com.cloudstone.emenu.exception.NotFoundException;
import com.cloudstone.emenu.exception.PreconditionFailedException;
import com.cloudstone.emenu.storage.cache.CommonCache;
import com.cloudstone.emenu.storage.dao.IChapterDb;
import com.cloudstone.emenu.storage.dao.IDishDb;
import com.cloudstone.emenu.storage.dao.IDishNoteDb;
import com.cloudstone.emenu.storage.dao.IDishPageDb;
import com.cloudstone.emenu.storage.dao.IDishPageDb.DishPage;
import com.cloudstone.emenu.storage.dao.IDishTagDb;
import com.cloudstone.emenu.storage.dao.IMenuDb;
import com.cloudstone.emenu.storage.dao.IMenuPageDb;
import com.cloudstone.emenu.util.CollectionUtils;
import com.cloudstone.emenu.util.DataUtils;
import com.cloudstone.emenu.util.StringUtils;

/**
 * @author xuhongfeng
 */
@Component
public class MenuLogic extends BaseLogic {
    private static final Logger LOG = Logger.getLogger(MenuLogic.class);

    @Autowired
    private IMenuDb menuDb;
    @Autowired
    private IChapterDb chapterDb;
    @Autowired
    private IMenuPageDb menuPageDb;
    @Autowired
    private IDishDb dishDb;
    @Autowired
    private IDishPageDb dishPageDb;
    @Autowired
    private IDishTagDb dishTagDb;
    @Autowired
    private IDishNoteDb dishNoteDb;

    @Autowired
    private ImageLogic imageLogic;

    @Autowired
    private CommonCache commonCache;

    /* ---------- menu ---------- */
    public Menu addMenu(EmenuContext context, Menu menu) {
        Menu old = menuDb.getByName(context, menu.getName());
        if (old != null && !old.isDeleted()) {
            throw new DataConflictException("Dish exists.");
        }

        long now = System.currentTimeMillis();
        menu.setUpdateTime(now);
        if (old != null) {
            menu.setId(old.getId());
            menu.setCreatedTime(old.getCreatedTime());
            menuDb.updateMenu(context, menu);
        } else {
            menu.setCreatedTime(now);
            menuDb.addMenu(context, menu);
        }
        return menuDb.getMenu(context, menu.getId());
    }

    public void bindDish(EmenuContext context, int menuPageId, int dishId, int pos) {
        MenuPage page = getMenuPage(context, menuPageId);
        if (page == null) {
            throw new PreconditionFailedException("Requested menu doesn't exist.");
        }
        if (isDishInChapter(context, dishId, page.getChapterId())) {
            throw new PreconditionFailedException("Dish has already been added to this category.");
        }
        dishPageDb.add(context, menuPageId, dishId, pos);
        checkDishInMenu(context, dishId);
        commonCache.resetCategoryCache(dishId);
    }

    public void unbindDish(EmenuContext context, int menuPageId, int dishId, int pos) {
        dishPageDb.delete(context, menuPageId, pos);
        checkDishInMenu(context, dishId);
        commonCache.resetCategoryCache(dishId);
    }

    public Menu getMenu(EmenuContext context, int id) {
        return menuDb.getMenu(context, id);
    }

    public List<Menu> getAllMenu(EmenuContext context) {
        List<Menu> menus = menuDb.getAllMenu(context);
        DataUtils.filterDeleted(menus);
        return menus;
    }

    public Menu getCurrentMenu(EmenuContext context) {
        //TODO
        List<Menu> menus = getAllMenu(context);
        if (menus.size() > 0) {
            return menus.get(0);
        }
        return null;
    }

    public Menu updateMenu(EmenuContext context, Menu menu) {
        Menu old = menuDb.getByName(context, menu.getName());
        if (old != null && old.getId() != menu.getId() && !old.isDeleted()) {
            throw new DataConflictException("Dish exists.");
        }
        menu.setUpdateTime(System.currentTimeMillis());
        menuDb.updateMenu(context, menu);
        return menuDb.getMenu(context, menu.getId());
    }

    public void deleteMenu(EmenuContext context, final int id) {
        menuDb.deleteMenu(context, id);
        deleteChaptersByMenuId(context, id);
    }

    /* ---------- dish ---------- */
    public Dish addDish(EmenuContext context, Dish dish) {
        Dish old = dishDb.getByName(context, dish.getName());
        if (old != null && !old.isDeleted()) {
            throw new DataConflictException("Dish exists.");
        }
        long now = System.currentTimeMillis();
        dish.setUpdateTime(now);

        //save image
        String uriData = dish.getUriData();
        if (!StringUtils.isBlank(uriData)) {
            String imageId = imageLogic.saveDishImage(uriData);
            dish.setImageId(imageId);
        }

        dish.setPinyin(dish.getName());
        if (old != null) {
            dish.setId(old.getId());
            dish.setCreatedTime(old.getCreatedTime());
            dishDb.update(context, dish);
        } else {
            dish.setCreatedTime(now);
            dishDb.add(context, dish);
        }
        //get with uriData
        return getDish(context, dish.getId(), true);
    }

    public Dish updateDish(EmenuContext context, Dish dish) {
        Dish old = dishDb.getByName(context, dish.getName());
        if (old != null && old.getId() != dish.getId() && !old.isDeleted()) {
            throw new DataConflictException("Dish exists");
        }
        dish.setUpdateTime(System.currentTimeMillis());
        //save image
        if (!StringUtils.isBlank(dish.getUriData())) {
            String imageId = imageLogic.saveDishImage(dish.getUriData());
            dish.setImageId(imageId);
        }
        //save to sqlitedb
        dishDb.update(context, dish);
        //get with uriData
        return getDish(context, dish.getId(), true);
    }

    public List<IdName> getDishSuggestion(EmenuContext context) {
        List<IdName> names = dishDb.getDishSuggestion(context);
        DataUtils.filterDeleted(names);
        return names;
    }

    public List<Dish> getAllDish(EmenuContext context) {
        List<Dish> dishes = dishDb.getAll(context);
        DataUtils.filterDeleted(dishes);
        return dishes;
    }

    public List<Dish> getDishByMenuPageId(EmenuContext context, int menuPageId) {
        List<Dish> ret = new ArrayList<Dish>();
        MenuPage page = getMenuPage(context, menuPageId);
        if (page == null) {
            throw new NotFoundException("");
        }
        Dish[] dishes = new Dish[page.getDishCount()];
        List<DishPage> relation = dishPageDb.getByMenuPageId(context, menuPageId);
        DataUtils.filterDeleted(relation);
        for (DishPage r : relation) {
            int dishId = r.getDishId();
            int pos = r.getPos();
            if (pos >= page.getDishCount())
                break;
            dishes[pos] = getDish(context, dishId, false);
        }
        for (int i = 0; i < dishes.length; i++) {
            Dish dish = dishes[i];
            if (dish == null || dish.isDeleted()) {
                dish = Dish.getNullDish(i);
            }
            ret.add(dish);
        }
        return ret;
    }

    public void deleteDish(EmenuContext context, int id) {
        dishDb.delete(context, id);
        dishPageDb.deleteByDishId(context, id);
    }

    public Dish getDish(EmenuContext context, int id) {
        return getDish(context, id, false);
    }

    public Dish getDish(EmenuContext context, int id, boolean withUriData) {
        Dish dish = dishDb.get(context, id);
        String imageId = dish.getImageId();
        if (withUriData && !StringUtils.isBlank(imageId)) {
            String uriData = imageLogic.getDishUriData(imageId);
            dish.setUriData(uriData);
        }
        return dish;
    }

    public Dish updateDishSoldout(EmenuContext context, int id, boolean soldout) {
        Dish dish = dishDb.get(context, id);
        if (dish == null || dish.isDeleted()) {
            throw new DbNotFoundException("Dish doesn't exit or has been deleted.");
        }
        dish.setSoldout(soldout);
        dishDb.update(context, dish);
        return getDish(context, dish.getId(), true);
    }

    public void updateDishesSoldout(EmenuContext context, boolean soldout) {
        List<Dish> dishes = dishDb.getAll(context);
        DataUtils.filterDeleted(dishes);
        for (Dish dish : dishes) {
            dish.setSoldout(soldout);
            dishDb.update(context, dish);
        }
    }

    /* ---------- chapter ---------- */
    public int[] getChapterIds(EmenuContext context) {
        return chapterDb.getAllChapterIds(context);
    }

    public Chapter addChapter(EmenuContext context, Chapter chapter) {
        Chapter old = chapterDb.getChapterByName(context, chapter.getName());
        if (old != null && old.getMenuId() == chapter.getMenuId() && !old.isDeleted()) {
            throw new DataConflictException("Category exists.");
        }
        List<Chapter> chapters = listChapterByMenuId(context, chapter.getMenuId());
        if (chapter.getOrdinal() == 0) {
            chapter.setOrdinal(chapters.size() + 1);
        }
        for (int i = chapter.getOrdinal(); i < chapters.size(); i++) {
            Chapter c = chapters.get(i);
            c.setOrdinal(i + 1);
            innerUpdateChapter(context, c);
        }

        long now = System.currentTimeMillis();
        chapter.setUpdateTime(now);
        if (old != null && old.getMenuId() == chapter.getMenuId()) {
            chapter.setId(old.getId());
            chapter.setCreatedTime(old.getCreatedTime());
            chapterDb.updateChapter(context, chapter);
        } else {
            chapter.setCreatedTime(now);
            chapterDb.addChapter(context, chapter);
        }
        return chapterDb.getChapter(context, chapter.getId());
    }

    public Chapter getChapter(EmenuContext context, int id) {
        return chapterDb.getChapter(context, id);
    }

    public String getCategory(EmenuContext context, int dishId) {
        return commonCache.getCategory(context, dishId);
    }

    public List<Chapter> listChapters(EmenuContext context, final int menuId, int dishId) {
        List<Chapter> chapters = chapterDb.listChapters(context, menuId, dishId);
        DataUtils.filterDeleted(chapters);
        return chapters;
    }

    public List<Chapter> listChapters(EmenuContext context, int[] ids) {
        List<Chapter> chapters = chapterDb.listChapters(context, ids);
        DataUtils.filterDeleted(chapters);
        return chapters;
    }

    public boolean isDishInChapter(EmenuContext context, int dishId, int chapterId) {
        List<MenuPage> pages = listMenuPageByChapterId(context, chapterId);
        List<MenuPage> relations = listMenuPageByDishId(context, dishId);
        for (MenuPage p : pages) {
            for (MenuPage r : relations) {
                if (p.getId() == r.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<DishPage> listDishPage(EmenuContext context, int dishId) {
        List<DishPage> dishPages = dishPageDb.getByDishId(context, dishId);
        DataUtils.filterDeleted(dishPages);
        return dishPages;
    }

    public List<Chapter> getAllChapter(EmenuContext context) {
        List<Chapter> chapters = chapterDb.getAllChapter(context);
        DataUtils.filterDeleted(chapters);
        return chapters;
    }

    public Chapter updateChapter(EmenuContext context, Chapter chapter) {
        Chapter old = chapterDb.getChapterByName(context, chapter.getName());
        if (old != null && old.getId() != chapter.getId() && chapter.getMenuId() == old.getMenuId()
                && !old.isDeleted()) {
            throw new DataConflictException("Category exists.");
        }

        old = chapterDb.getChapter(context, chapter.getId());
        if (old == null || old.isDeleted()) {
            throw new BadRequestError();
        }

        if (old.getOrdinal() != chapter.getOrdinal()) {
            List<Chapter> chapters = listChapterByMenuId(context, chapter.getMenuId());
            if (chapter.getOrdinal() < old.getOrdinal()) {
                for (int i = chapter.getOrdinal(); i <= old.getOrdinal() - 1; i++) {
                    Chapter c = chapters.get(i - 1);
                    c.setOrdinal(c.getOrdinal() + 1);
                    innerUpdateChapter(context, c);
                }
            } else {
                for (int i = old.getOrdinal() + 1; i <= chapter.getOrdinal(); i++) {
                    Chapter c = chapters.get(i - 1);
                    c.setOrdinal(c.getOrdinal() - 1);
                    innerUpdateChapter(context, c);
                }
            }
        }
        return innerUpdateChapter(context, chapter);
    }

    private Chapter innerUpdateChapter(EmenuContext context, Chapter chapter) {
        chapter.setUpdateTime(System.currentTimeMillis());
        chapterDb.updateChapter(context, chapter);
        return chapterDb.getChapter(context, chapter.getId());
    }

    public void move(EmenuContext context, int chapterId, boolean up) {
        Chapter chapter = getChapter(context, chapterId);
        if (chapter == null || chapter.isDeleted()) {
            throw new NotFoundException("Category not found, id=" + chapterId);
        }
        if (up && chapter.getOrdinal() == 1) {
            throw new BadRequestError();
        }
        if (up) {
            chapter.setOrdinal(chapter.getOrdinal() - 1);
            updateChapter(context, chapter);
        } else {
            chapter.setOrdinal(chapter.getOrdinal() + 1);
            updateChapter(context, chapter);
            fixChapterOrdinal(context);
        }
    }

    public void deleteChapter(EmenuContext context, final int id) {
        innerDeleteChapter(context, id, true);
    }

    private void innerDeleteChapter(EmenuContext context, final int id, boolean updateOrdinal) {
        if (updateOrdinal) {
            Chapter old = getChapter(context, id);
            if (old != null) {
                List<Chapter> chapters = listChapterByMenuId(context, old.getMenuId());
                for (int i = old.getOrdinal() + 1; i <= chapters.size(); i++) {
                    Chapter c = chapters.get(i - 1);
                    c.setOrdinal(c.getOrdinal() - 1);
                    innerUpdateChapter(context, c);
                }
            }
        }

        chapterDb.deleteChapter(context, id);
        List<MenuPage> pages = listMenuPageByChapterId(context, id);
        for (MenuPage page : pages) {
            deleteMenuPage(context, page.getId());
        }
    }

    public void deleteChaptersByMenuId(EmenuContext context, int menuId) {
        List<Chapter> chapters = listChapterByMenuId(context, menuId);
        for (Chapter chapter : chapters) {
            innerDeleteChapter(context, chapter.getId(), false);
        }
    }

    public List<Chapter> listChapterByMenuId(EmenuContext context, int menuId) {
        List<Chapter> chapters = chapterDb.listChapters(context, menuId);
        DataUtils.filterDeleted(chapters);
        return chapters;
    }

    public void fixChapterOrdinal(EmenuContext context) {
        List<Menu> menus = getAllMenu(context);
        for (Menu menu : menus) {
            int menuId = menu.getId();
            List<Chapter> chapters = listChapterByMenuId(context, menuId);
            for (int i = 0; i < chapters.size(); i++) {
                Chapter c = chapters.get(i);
                c.setOrdinal(i + 1);
                innerUpdateChapter(context, c);
            }
        }
    }
    
    /* ---------- MenuPage ---------- */

    public List<MenuPage> listMenuPageByDishId(EmenuContext context, int dishId) {
        List<DishPage> relations = listDishPage(context, dishId);
        Set<Integer> pageIds = new HashSet<Integer>();
        for (DishPage r : relations) {
            pageIds.add(r.getMenuPageId());
        }
        List<MenuPage> pages = menuPageDb.listMenuPages(context, CollectionUtils.toIntArray(pageIds));
        DataUtils.filterDeleted(pages);
        return pages;
    }

    public List<MenuPage> listMenuPageByChapterId(EmenuContext context, int chapterId) {
        List<MenuPage> datas = menuPageDb.listMenuPages(context, chapterId);
        DataUtils.filterDeleted(datas);

        for (int i = 1; i <= datas.size(); i++) {
            MenuPage p = datas.get(i - 1);
            if (p.getOrdinal() != i) {
                p.setOrdinal(i);
                innerUpdateMenuPage(context, p);
            }
        }
        return datas;
    }

    private void innerUpdateMenuPage(EmenuContext context, MenuPage p) {
//        List<DishPage> relation = dishPageDb.getByMenuPageId(context, p.getId());
//        if(p.getDishCount() < relation.size()) {
//            throw new DataConflictException("已有菜数大于该页能容纳菜数");
//        }
        p.setUpdateTime(System.currentTimeMillis());
        menuPageDb.updateMenuPage(context, p);
    }

    public MenuPage addMenuPage(EmenuContext context, MenuPage page) {
        List<MenuPage> pages = listMenuPageByChapterId(context, page.getChapterId());
        for (int i = page.getOrdinal(); i <= pages.size(); i++) {
            MenuPage p = pages.get(i - 1);
            p.setOrdinal(i + 1);
            innerUpdateMenuPage(context, p);
        }
        long now = System.currentTimeMillis();
        page.setCreatedTime(now);
        page.setUpdateTime(now);
        menuPageDb.addMenuPage(context, page);
        return menuPageDb.getMenuPage(context, page.getId());
    }

    public void deleteMenuPage(EmenuContext context, int id) {
        MenuPage old = getMenuPage(context, id);
        if (old == null || old.isDeleted()) {
            throw new BadRequestError();
        }
        List<MenuPage> pages = listMenuPageByChapterId(context, old.getChapterId());
        for (int i = old.getOrdinal() + 1; i <= pages.size(); i++) {
            MenuPage p = pages.get(i - 1);
            p.setOrdinal(p.getOrdinal() - 1);
            innerUpdateMenuPage(context, p);
        }
        //delete page
        menuPageDb.deleteMenuPage(context, id);
        List<DishPage> relation = dishPageDb.getByMenuPageId(context, id);
        dishPageDb.deleteByMenuPageId(context, id);
        for (DishPage r : relation) {
            int dishId = r.getDishId();
            checkDishInMenu(context, dishId);
        }
    }

    public MenuPage getMenuPage(EmenuContext context, int id) {
        return menuPageDb.getMenuPage(context, id);
    }

    public MenuPage updateMenuPage(EmenuContext context, MenuPage page) {
        MenuPage old = getMenuPage(context, page.getId());
        if (old == null || old.isDeleted()) {
            throw new BadRequestError();
        }
        if (old.getOrdinal() != page.getOrdinal()) {
            List<MenuPage> pages = listMenuPageByChapterId(context, page.getChapterId());
            if (page.getOrdinal() < old.getOrdinal()) {
                for (int i = page.getOrdinal(); i <= old.getOrdinal() - 1; i++) {
                    MenuPage p = pages.get(i - 1);
                    p.setOrdinal(p.getOrdinal() + 1);
                    innerUpdateMenuPage(context, p);
                }
            } else {
                for (int i = old.getOrdinal() + 1; i <= page.getOrdinal(); i++) {
                    MenuPage p = pages.get(i - 1);
                    p.setOrdinal(p.getOrdinal() - 1);
                    innerUpdateMenuPage(context, p);
                }
            }
        }
        innerUpdateMenuPage(context, page);
        return menuPageDb.getMenuPage(context, page.getId());
    }

    /* ---------- DishTag ---------- */
    public List<DishTag> listAllDishTag(EmenuContext context) {
        List<DishTag> tags = dishTagDb.listAll(context);
        DataUtils.filterDeleted(tags);
        return tags;
    }

    public DishTag addDishTag(EmenuContext context, DishTag tag) {
        DishTag old = dishTagDb.getDishTagByName(context, tag.getName());
        if (old != null && !old.isDeleted()) {
            throw new DataConflictException("Tag exists.");
        }
        long now = System.currentTimeMillis();
        tag.setUpdateTime(now);
        if (old != null) {
            tag.setId(old.getId());
            tag.setCreatedTime(old.getCreatedTime());
            dishTagDb.updateDishTag(context, tag);
        } else {
            tag.setUpdateTime(now);
            dishTagDb.addDishTag(context, tag);
        }
        return dishTagDb.getDishTag(context, tag.getId());
    }

    public DishTag updateDishTag(EmenuContext context, DishTag tag) {
        DishTag old = dishTagDb.getDishTagByName(context, tag.getName());
        if (old != null && old.getId() != tag.getId() && !old.isDeleted()) {
            throw new DataConflictException("Tag exists.");
        }
        tag.setUpdateTime(System.currentTimeMillis());
        dishTagDb.updateDishTag(context, tag);
        return dishTagDb.getDishTag(context, tag.getId());
    }

    public void deleteDishTag(EmenuContext context, int id) {
        dishTagDb.deleteDishTag(context, id);
    }

    /* ---------- DishNote ---------- */
    public List<DishNote> listAllDishNote(EmenuContext context) {
        List<DishNote> notes = dishNoteDb.listAll(context);
        DataUtils.filterDeleted(notes);
        return notes;
    }

    public DishNote addDishNote(EmenuContext context, DishNote note) {
        DishNote old = dishNoteDb.getDishNoteByName(context, note.getName());
        if (old != null && !old.isDeleted()) {
            throw new DataConflictException("Dish exists.");
        }
        long now = System.currentTimeMillis();
        note.setUpdateTime(now);
        if (old != null) {
            note.setId(old.getId());
            note.setCreatedTime(old.getCreatedTime());
            dishNoteDb.updateDishNote(context, note);
        } else {
            note.setUpdateTime(now);
            dishNoteDb.addDishNote(context, note);
        }
        return dishNoteDb.getDishNote(context, note.getId());
    }

    public DishNote updateDishNote(EmenuContext context, DishNote note) {
        DishNote old = dishNoteDb.getDishNoteByName(context, note.getName());
        if (old != null && old.getId() != note.getId() && !old.isDeleted()) {
            throw new DataConflictException("Dish exists.");
        }
        note.setUpdateTime(System.currentTimeMillis());
        dishNoteDb.updateDishNote(context, note);
        return dishNoteDb.getDishNote(context, note.getId());
    }

    public void deleteDishNote(EmenuContext context, int id) {
        dishNoteDb.deleteDishNote(context, id);
    }

    private void checkDishInMenu(EmenuContext context, int dishId) {
        Dish dish = dishDb.get(context, dishId);
        if (dish != null) {
            int count = dishPageDb.countByDishId(context, dishId);
            int oldStatus = dish.getStatus();
            int status = count == 0 ? Const.DishStatus.STATUS_INIT : Const.DishStatus.STATUS_IN_MENU;
            if (status != oldStatus) {
                dish.setStatus(status);
                dishDb.update(context, dish);
            }
        }
    }

    public List<MenuPage> listMenuPageByMenuId(EmenuContext context, int menuId) {
        List<MenuPage> ret = new ArrayList<MenuPage>();
        List<Chapter> chapters = chapterDb.listChapters(context, menuId);
        for (Chapter chapter : chapters) {
            List<MenuPage> pages = menuPageDb.listMenuPages(context, chapter.getId());
            ret.addAll(pages);
        }
        return ret;
    }
}
