package com.ehualu.calabashandroid.adapter.decoration;

import android.content.Context;

import com.yanyusong.y_divideritemdecoration.Y_Divider;
import com.yanyusong.y_divideritemdecoration.Y_DividerBuilder;
import com.yanyusong.y_divideritemdecoration.Y_DividerItemDecoration;

public class DividerItemDecoration02 extends Y_DividerItemDecoration {

    public DividerItemDecoration02(Context context) {
        super(context);
    }

    @Override
    public Y_Divider getDivider(int itemPosition) {
        Y_Divider divider = null;
        divider = new Y_DividerBuilder()
                .setBottomSideLine(true, 0x00000000, 14, 0, 0)
                .create();
        return divider;
    }
}
