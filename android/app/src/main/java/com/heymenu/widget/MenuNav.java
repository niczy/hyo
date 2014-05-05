package com.heymenu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.cloudmenu.emenu.R;
import net.cloudmenu.emenu.utils.MenuUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicholaszhao on 4/21/14.
 */
public class MenuNav extends FrameLayout implements AdapterView.OnItemClickListener {

    public static final int SPECIAL_INDEX = -1;

    private ListView mListView;
    private boolean mHasSpecial;
    private CategoryAdapter mAdapter;
    private int mSelected = 0;
    private List<MenuUtils.GoodsCategory> mCategories;
    private CategoryChanged mOnCategoryChanged;
    private Button mSpecial;



    public MenuNav(Context context) {
        this(context, null);
    }

    public MenuNav(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuNav(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.widget_menu_nav, this);
        mListView = (ListView) findViewById(R.id.category_list);
        mSpecial = (Button) findViewById(R.id.special);

        mAdapter = new CategoryAdapter(context);
        mSpecial.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSpecial();
                mAdapter.notifyDataSetChanged();
            }
        });
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    public void setCategories(List<MenuUtils.GoodsCategory> categories, boolean hasSpecial) {
        mCategories = categories;
        mAdapter.setCategories(categories);
        mAdapter.notifyDataSetChanged();
        mHasSpecial = hasSpecial;
        if (hasSpecial) {
            mSpecial.setVisibility(VISIBLE);
            selectSpecial();
        } else {
            mSpecial.setVisibility(GONE);
        }
    }

    private void selectSpecial() {
        mSelected = -1;
        mSpecial.setTextColor(getContext().getResources().getColor(R.color.menu_highlight));
        mSpecial.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_special_pressed, 0, 0, 0);
        mSpecial.setBackgroundColor(getContext().getResources().getColor(android.R.color.white));
        if (mOnCategoryChanged != null) {
            mOnCategoryChanged.onCategoryChanged(SPECIAL_INDEX);
        }
    }

    public void setOnCategoryChanged(CategoryChanged categoryChanged) {
        this.mOnCategoryChanged = categoryChanged;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setCategorySelected(position);
        if (mOnCategoryChanged != null) {
            mOnCategoryChanged.onCategoryChanged(mCategories.get(position).getStart());
        }
    }

    public void onItemScrolled(int firstVisiableItem, int lastVisiableItem) {
        if (mCategories == null) {
            return;
        }
        if (mSelected != -1) {
            MenuUtils.GoodsCategory category = mCategories.get(mSelected);
            if (category.getStart() >= firstVisiableItem &&
                    category.getStart() <= lastVisiableItem) {
                return;
            }
        }
        int categoryIndex = 0;
        for (MenuUtils.GoodsCategory category : mCategories) {
            if (category.getStart() >=  firstVisiableItem) {
                setCategorySelected(categoryIndex);
                return;
            }
            categoryIndex++;
        }
    }

    private void setCategorySelected(int position) {
        mSpecial.setTextColor(getContext().getResources().getColor(android.R.color.white));
        mSpecial.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_special_normal, 0, 0, 0);
        mSpecial.setBackgroundColor(getContext().getResources().getColor(R.color.menu_highlight));
        mSelected = position;
        mAdapter.notifyDataSetChanged();
    }

    private class CategoryAdapter extends BaseAdapter {

        private Context mContext;
        private List<MenuUtils.GoodsCategory> mCategories = new ArrayList<MenuUtils.GoodsCategory>();

        public CategoryAdapter(Context mContext) {
            this.mContext = mContext;
        }

        public void setCategories(List<MenuUtils.GoodsCategory> categories) {
            mCategories.clear();
            mCategories.addAll(categories);
        }
        @Override
        public int getCount() {
            return mCategories.size();
        }

        @Override
        public Object getItem(int position) {
            return mCategories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mCategories.get(position).getStart();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) (mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                convertView = inflater.inflate(
                        R.layout.category_indicator, null);
            }
            TextView category = (TextView) convertView;
            category.setText(mCategories.get(position).getCategory());
            if (position == mSelected) {
                category.setTextColor(mContext.getResources().getColor(R.color.menu_highlight));
                category.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
            } else {
                category.setTextColor(mContext.getResources().getColor(android.R.color.white));
                category.setBackgroundColor(mContext.getResources().getColor(R.color.menu_highlight));
            }
            return category;
        }
    }

    public static interface  CategoryChanged {
        void onCategoryChanged(int categoryIndex);
    }
}
