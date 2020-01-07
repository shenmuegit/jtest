package com.ehualu.calabashandroid.interfaces;


import com.ehualu.calabashandroid.model.Video;

import java.util.List;

public interface PhotoOnItemClickInterface {

    void onItemClick(String t);

    void selectedAll();

    void notSelectedAll();

    void selectCount(List<String> selectedList, int count);
}
