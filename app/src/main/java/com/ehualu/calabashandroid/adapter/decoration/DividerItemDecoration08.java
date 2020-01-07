package com.ehualu.calabashandroid.adapter.decoration;

import android.content.Context;

import com.yanyusong.y_divideritemdecoration.Y_Divider;
import com.yanyusong.y_divideritemdecoration.Y_DividerBuilder;
import com.yanyusong.y_divideritemdecoration.Y_DividerItemDecoration;

public class DividerItemDecoration08 extends Y_DividerItemDecoration {

    public DividerItemDecoration08(Context context) {
        super(context);
    }

    @Override
    public Y_Divider getDivider(int itemPosition) {
        Y_Divider divider = null;
        switch (itemPosition % 4) {
            case 0:
                divider = new Y_DividerBuilder()
                        .setRightSideLine(true, 0x00000000, 1.5f, 0, 0)
                        .setBottomSideLine(true, 0x00000000, 2, 0, 0)
                        .create();
                break;
            case 1:
            case 2:
                divider = new Y_DividerBuilder()
                        .setLeftSideLine(true, 0x00000000, 0.5f, 0, 0)
                        .setRightSideLine(true, 0x00000000, 1.5f, 0, 0)
                        .setBottomSideLine(true, 0x00000000, 2, 0, 0)
                        .create();
                break;
            case 3:
                divider = new Y_DividerBuilder()
                        .setLeftSideLine(true, 0x00000000, 0.5f, 0, 0)
                        .setBottomSideLine(true, 0x00000000, 2, 0, 0)
                        .create();
                break;
        }
        return divider;
    }
}
