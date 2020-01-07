package com.ehualu.calabashandroid.widget;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.List;

/**
 * author: houxiansheng
 * <p>
 * time：2019-12-23 14:37:26
 * <p>
 * describe：相机
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private static final int ORIENTATION = 90;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean previewRunning = false;
    public static final int FRONT = 1;//前置摄像头标记
    public static final int BACK = 2;//后置摄像头标记
    private int currentCameraType = -1;//当前打开的摄像头标记

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        getScreenMatrix(context);
    }

    private void getScreenMatrix(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();//获得窗口里面的屏幕
        mScreenWidth = display.getWidth();
        mScreenHeight = display.getHeight();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera = Camera.open(); //取第一个摄像头
            mCamera.setDisplayOrientation(ORIENTATION);
            currentCameraType = BACK;
            setCameraParams();
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();//预览
            previewRunning = true;
            mCamera.cancelAutoFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {
            setCameraParams();
            mCamera.setDisplayOrientation(ORIENTATION);
            mCamera.startPreview();
            mCamera.cancelAutoFocus();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            if (previewRunning) {
                mCamera.stopPreview();
                previewRunning = false;
            }
            mCamera.release();//释放摄像头
        }
    }

    /**
     * 拍照
     */
    public void takePicture(Camera.ShutterCallback mShutterCallback, Camera.PictureCallback rawPictureCallback,
                            Camera.PictureCallback jpegPictureCallback) {
        if (mCamera != null)
            mCamera.takePicture(mShutterCallback, rawPictureCallback, jpegPictureCallback);
    }

    /**
     * 关闭预览
     */
    public void stopPreview() {
        mCamera.stopPreview();

    }

    /**
     * 设置相机属性
     */
    private void setCameraParams() {
        Camera.Parameters parameters = mCamera.getParameters();
        // 获取摄像头支持的PictureSize列表
        List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();
        /**从列表中选取合适的分辨率*/
        Camera.Size picSize = getProperSize(pictureSizeList, ((float) mScreenHeight / mScreenWidth));
        if (null == picSize) {
            picSize = parameters.getPictureSize();
        }
        // 根据选出的PictureSize重新设置SurfaceView大小
        float w = picSize.width;
        float h = picSize.height;
        parameters.setPictureSize(picSize.width, picSize.height);
        this.setLayoutParams(new RelativeLayout.LayoutParams((int) (mScreenHeight * (h / w)), mScreenHeight));
        // 获取摄像头支持的PreviewSize列表
        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
        Camera.Size preSize = getProperSize(previewSizeList, ((float) mScreenHeight) / mScreenWidth);
        if (null != preSize) {
            parameters.setPreviewSize(preSize.width, preSize.height);
        }
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//自动对焦
        //图片旋转，保证保存图片时的方向都是竖向
        if (currentCameraType == BACK) {//后摄像头
            parameters.set("rotation", 90);
        } else if (currentCameraType == FRONT) {//前摄像头
            parameters.set("rotation", 270);
        }
        mCamera.setParameters(parameters);
    }

    /**
     * 获取当前拍照使用的是前后摄像头
     */
    public int getCurrentCameraType() {
        return currentCameraType;
    }

    /**
     * 选取合适的分辨率
     */
    private Camera.Size getProperSize(List<Camera.Size> pictureSizeList, float screenRatio) {
        Camera.Size result = null;
        for (Camera.Size size : pictureSizeList) {
            float currentRatio = ((float) size.width) / size.height;
            if (currentRatio - screenRatio == 0) {
                result = size;
                break;
            }
        }
        if (null == result) {
            for (Camera.Size size : pictureSizeList) {
                float curRatio = ((float) size.width) / size.height;
                if (curRatio == 4f / 3) {// 默认w:h = 4:3
                    result = size;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 打开相机
     */
    private Camera openCamera(int type) {
        int frontIndex = -1;
        int backIndex = -1;
        int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int cameraIndex = 0; cameraIndex < cameraCount; cameraIndex++) {
            Camera.getCameraInfo(cameraIndex, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                frontIndex = cameraIndex;
            } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                backIndex = cameraIndex;
            }
        }
        currentCameraType = type;
        if (type == FRONT && frontIndex != -1) {
            return Camera.open(frontIndex);
        } else if (type == BACK && backIndex != -1) {
            return Camera.open(backIndex);
        }
        return null;
    }

    /**
     * 前后摄像头切换
     */
    public void changeCamera() {
        if (previewRunning) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }

        if (currentCameraType == FRONT) {
            mCamera = openCamera(BACK);
        } else {
            mCamera = openCamera(FRONT);
        }

        setCameraParams();
        mCamera.setDisplayOrientation(ORIENTATION);

        if (mHolder == null) {
            mHolder = getHolder();
        }

        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCamera.startPreview();
        mCamera.cancelAutoFocus();
    }
}
