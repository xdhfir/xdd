package com.my.baselib.lib.manager;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraManager {
    private static final String TAG = CameraManager.class.getName();
    private Camera camera;
    private Camera.Parameters parameters;
    private AutoFocusManager autoFocusManager;
    private int requestedCameraId = 0;

    private boolean initialized;
    private boolean previewing;

    /**
     * 打开摄像头
     *
     * @param cameraId 摄像头id
     * @return Camera
     */
    public Camera open(int cameraId) {
        int numCameras = Camera.getNumberOfCameras();
        Log.e("numnum", numCameras + "");

        if (numCameras == 0) {
            Log.e(TAG, "No cameras!");
            return null;
        }
        boolean explicitRequest = cameraId >= 0;
        if (!explicitRequest) {
            // Select a camera if no explicit camera requested
            int index = 0;
            while (index < numCameras) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(index, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    break;
                }
                index++;
            }
            cameraId = index;
        }
        Camera camera;
        if (cameraId < numCameras) {
            Log.d(TAG, "Opening camera #" + cameraId);
            camera = Camera.open(cameraId);
        } else {
            if (explicitRequest) {
                Log.d(TAG, "Requested camera does not exist: " + cameraId);
                camera = null;
            } else {
                Log.d(TAG, "No camera facing back; returning camera #0");
                camera = Camera.open(0);
            }
        }
        return camera;
    }

    /**
     * 打开camera
     *
     * @param holder SurfaceHolder
     * @throws IOException IOException
     */
    public synchronized void openDriver(SurfaceHolder holder)
            throws IOException {
        Log.d(TAG, "openDriver");
        if (camera == null) {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        camera.setPreviewDisplay(holder);
        if (!initialized) {
            initialized = true;
            parameters = camera.getParameters();
            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
            List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
            List<String> focusModes = parameters.getSupportedFocusModes();

            Log.e("previewSizes", previewSizes.toString());
            Log.e("pictureSizes", pictureSizes.toString());
            Log.e("focusModes", focusModes.toString());

            parameters.setPreviewSize(previewSizes.get(0).width,previewSizes.get(0).height);
            parameters.setPictureFormat(ImageFormat.JPEG);
            parameters.setJpegQuality(100);
            parameters.setPictureSize(pictureSizes.get(0).width,pictureSizes.get(0).height);
            camera.setParameters(parameters);
        }
    }

    public List<Camera.Size> suportedPreviewSizes = new ArrayList<>();

    public List<Camera.Size> getOpSizes(){
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = 0;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = 0;
        List<Camera.Size> optimalSizes = new ArrayList<>();
        Camera.Size optimalSize = null;
        for (Camera.Size size : suportedPreviewSizes) {
            double ratio = size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) {
                continue;
            }
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
                if (optimalSizes.size() == 0) {
                    optimalSizes.add(optimalSize);
                } else {
                    if (optimalSize.width != optimalSizes.get(optimalSizes.size() - 1).width ||
                            optimalSize.height != optimalSizes.get(optimalSizes.size() - 1).height) {
                        optimalSizes.add(optimalSize);
                    }
                }
            }
        }
        return optimalSizes;
    }

    /**
     * camera是否打开
     *
     * @return camera是否打开
     */
    public synchronized boolean isOpen() {
        return camera != null;
    }

    /**
     * 关闭camera
     */
    public synchronized void closeDriver() {
        Log.d(TAG, "closeDriver");
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    /**
     * 开始预览
     */
    public synchronized void startPreview() {
        Log.d(TAG, "startPreview");
        Camera theCamera = camera;
        if (theCamera != null && !previewing) {
            theCamera.startPreview();
            previewing = true;
            autoFocusManager = new AutoFocusManager(camera);
        }
    }

    /**
     * 关闭预览
     */
    public synchronized void stopPreview() {
        Log.d(TAG, "stopPreview");
        if (autoFocusManager != null) {
            autoFocusManager.stop();
            autoFocusManager = null;
        }
        if (camera != null && previewing) {
            camera.stopPreview();
            previewing = false;
        }
    }

    /**
     * 打开闪光灯
     */
    public synchronized void openLight() {
        Log.d(TAG, "openLight");
        if (camera != null) {
            parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
        }
    }

    /**
     * 关闭闪光灯
     */
    public synchronized void offLight() {
        Log.d(TAG, "offLight");
        if (camera != null) {
            parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
        }
    }

    /**
     * 拍照
     *
     * @param shutter ShutterCallback
     * @param raw     PictureCallback
     * @param jpeg    PictureCallback
     */
    public synchronized void takePicture(final Camera.ShutterCallback shutter, final Camera.PictureCallback raw,
                                         final Camera.PictureCallback jpeg) {
        Log.d("camera", camera + "");
        camera.takePicture(shutter, raw, jpeg);
    }
}