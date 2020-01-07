package com.ehualu.calabashandroid.interfaces;


import com.ehualu.calabashandroid.model.Video;
import java.util.List;

public interface VideoOnItemClickInterface {

    void onItemClick(Video t);

    void selectedAll();

    void notSelectedAll();

    void selectCount(List<Video> selectedList, int count);
}
