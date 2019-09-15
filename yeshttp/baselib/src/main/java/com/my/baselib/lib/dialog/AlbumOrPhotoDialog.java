package com.my.baselib.lib.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;


import com.my.baselib.R;

import java.io.File;


/**
 * 版本更新的彈窗，是否選擇更新
 */
public class AlbumOrPhotoDialog implements View.OnClickListener {
    public Activity activity;
    private final Dialog dialog;
    private final Button album;
    private final Button photo;
    public static final int TAKE_PHOTO = 1;
    public static final int PICK_PHOTO = 2;

    public AlbumOrPhotoDialog(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity, R.style.dialog);
        dialog.setContentView(R.layout.view_image_photo_dialog);
        dialog.setCanceledOnTouchOutside(false);
        album = (Button) dialog.findViewById(R.id.album_dialog_yes_or_no_dialog);
        photo = (Button) dialog.findViewById(R.id.photo_dialog_yes_or_no_dialog);
        album.setOnClickListener(this);
        photo.setOnClickListener(this);
    }

    public void showDialog() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismissDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.album_dialog_yes_or_no_dialog) {
            Intent intent1 = new Intent(Intent.ACTION_PICK, null);
            intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            activity.startActivityForResult(intent1, PICK_PHOTO);
            AlbumOrPhotoDialog.this.dismissDialog();

        } else if (i == R.id.photo_dialog_yes_or_no_dialog) {
            String path = Environment.getExternalStorageDirectory().getPath();
            String image = path + "/image.png";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(image)));
            activity.startActivityForResult(intent, TAKE_PHOTO);
            AlbumOrPhotoDialog.this.dismissDialog();
        }
    }
}
