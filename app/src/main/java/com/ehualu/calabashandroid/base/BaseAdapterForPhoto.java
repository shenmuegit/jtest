package com.ehualu.calabashandroid.base;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ehualu.calabashandroid.interfaces.PhotoOnItemClickInterface;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapterForPhoto<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    public ArrayList<String> checkedList = new ArrayList<>();//选中的文件列表
    public PhotoOnItemClickInterface photoItemClickInterface;

    public BaseAdapterForPhoto(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public void setListener(PhotoOnItemClickInterface photoItemClickInterface) {
        this.photoItemClickInterface = photoItemClickInterface;
    }

    public void addCheckList(String file) {
        if (!checkedList.contains(file)) {
            checkedList.add(file);
        }

        if (photoItemClickInterface != null) {
            photoItemClickInterface.selectCount(checkedList, checkedList.size());
        }
    }

    public boolean isChecked(String file) {
        return checkedList.contains(file);
    }

    public ArrayList<String> getCheckedList() {
        return checkedList;
    }

    public void removeCheckList(String file) {
        checkedList.remove(file);

        if (photoItemClickInterface != null) {
            photoItemClickInterface.selectCount(checkedList, checkedList.size());
        }
    }

    public void removeAllCheckedList() {
        checkedList.clear();
        if (photoItemClickInterface != null) {
            photoItemClickInterface.selectCount(checkedList, 0);
        }
    }

}
