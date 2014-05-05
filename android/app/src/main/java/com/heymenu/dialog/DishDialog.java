package com.heymenu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.heymenu.core.OrderManager;
import com.heymenu.widget.AddDishButton;

import net.cloudmenu.emenu.R;
import net.cloudmenu.emenu.utils.MenuUtils;

import cn.buding.common.widget.AsyncImageView;
import cn.com.cloudstone.menu.server.thrift.api.Goods;

/**
 * Created by nicholaszhao on 4/15/14.
 */
public class DishDialog extends Dialog {
    private OrderManager mOrderManager;
    public DishDialog(Context context, Goods good, OrderManager orderManager) {
        super(context, R.style.Theme_Dialog);
        setContentView(R.layout.dialog_dish);
        mOrderManager = orderManager;
        AsyncImageView imageView = (AsyncImageView) findViewById(R.id.image);
        String imageUrl = MenuUtils.getGoodImgUrl(context, good);
        if (imageUrl != null) {
            imageView.postLoading(imageUrl);
        } else {
            imageView.setImageResource(R.drawable.image_missing);
        }

        TextView desc = (TextView) findViewById(R.id.desc);
        desc.setText(good.getIntroduction());
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(good.getName());
        TextView price = (TextView) findViewById(R.id.price);
        price.setText("$" + good.getPrice());
        AddDishButton addDishButton = (AddDishButton) findViewById(R.id.add_dish_button);
        addDishButton.setCount(mOrderManager.getOrderCountForGood(good.getId()));
        addDishButton.setGoodId(good.getId());
        addDishButton.setOnOrderChange(mOrderManager);
        findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
