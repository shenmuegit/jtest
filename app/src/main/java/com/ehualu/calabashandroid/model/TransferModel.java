package com.ehualu.calabashandroid.model;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-10 10:21:48
 * <p>
 * describe：传输的Model
 */
public class TransferModel {
    private String taskId;//唯一的表示一个任务
    private int icon;//图标
    private String name;//名称
    private int currentProgress;//当前上传、下载、备份提取进度
    private int currentSpeed;//当前上传、下载、备份提取速度
    private long Size;//大小
    private String time;//时间

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;//源文件路径
    private String targetPath;//上传目标路径

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getIcon() {
        return icon;
    }

    public TransferModel setIcon(int icon) {
        this.icon = icon;
        return this;
    }

    public String getName() {
        return name;
    }

    public TransferModel setName(String name) {
        this.name = name;
        return this;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public TransferModel setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
        return this;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public TransferModel setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
        return this;
    }

    public long getSize() {
        return Size;
    }

    public TransferModel setSize(long size) {
        Size = size;
        return this;
    }

    public String getTime() {
        return time;
    }

    public TransferModel setTime(String time) {
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        return "TransferModel{" +
                "icon=" + icon +
                ", name='" + name + '\'' +
                ", currentProgress='" + currentProgress + '\'' +
                ", currentSpeed='" + currentSpeed + '\'' +
                ", Size='" + Size + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
