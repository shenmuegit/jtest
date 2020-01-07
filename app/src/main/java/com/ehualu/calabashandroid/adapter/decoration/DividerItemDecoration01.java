package com.ehualu.calabashandroid.adapter.decoration;

import android.content.Context;

import com.yanyusong.y_divideritemdecoration.Y_Divider;
import com.yanyusong.y_divideritemdecoration.Y_DividerBuilder;
import com.yanyusong.y_divideritemdecoration.Y_DividerItemDecoration;

public class DividerItemDecoration01 extends Y_DividerItemDecoration {

    public DividerItemDecoration01(Context context) {
        super(context);
    }

    @Override
    public Y_Divider getDivider(int itemPosition) {
        Y_Divider divider = null;
        switch (itemPosition % 3) {
            case 0:
                divider = new Y_DividerBuilder()
                        .setRightSideLine(true, 0x00000000, 29, 0, 0)
                        .setBottomSideLine(true, 0x00000000, 24, 0, 0)
                        .create();
                break;
            case 1:
                divider = new Y_DividerBuilder()
                        .setRightSideLine(true, 0x00000000, 15, 0, 0)
                        .setLeftSideLine(true, 0x00000000, 15, 0, 0)
                        .setBottomSideLine(true, 0x00000000, 24, 0, 0)
                        .create();
                break;
            case 2:
                divider = new Y_DividerBuilder()
                        .setLeftSideLine(true, 0x00000000, 29, 0, 0)
                        .setBottomSideLine(true, 0x00000000, 24, 0, 0)
                        .create();
                break;
        }
        return divider;
    }
}
