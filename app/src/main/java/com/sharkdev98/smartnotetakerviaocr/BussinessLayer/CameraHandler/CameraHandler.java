package com.sharkdev98.smartnotetakerviaocr.BussinessLayer.CameraHandler;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.sharkdev98.smartnotetakerviaocr.BussinessLayer.Common.CommonBussinessLogic;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.io.IOException;

public class CameraHandler extends CommonBussinessLogic {

    final static int CAMERA_HANDLER_REQUEST_CODE = 100;
    public static String currentPhotoPath ;
    public static String currentPhotoThumbnailPath ;
    static String imageThumbnailName;
    public  Uri photoURI;
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 101;
    File photoFile;
    public File image;
    public static String imageFileName = "";

    @Nullable
    public Intent GetPictureIntent(Activity activity) {
        Intent PictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (PictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            photoFile = null;
            try {
                photoFile = CreateImageFileWithUniqueName(activity);
            }catch (IOException ex) {
                ShowMessage("Error", ex.getMessage(), activity);
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(activity,activity.getApplicationContext().getPackageName()+".fileprovider",photoFile);
                Log.d("DEBUG_MSG", photoURI.toString());
                PictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                return PictureIntent;
            }
        }
        return null;
    }

    public void deleteLastPhotoTaken(Context context) {


        String[] projection = new String[] {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE };

        final Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                null,null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        if (cursor != null) {
            cursor.moveToFirst();
//            cursor.moveToNext();
            int column_index_data =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            String image_path = cursor.getString(column_index_data);

            File file = new File(image_path);
            if (file.exists()) {
                if(file.delete() == true)
                {
//                    Log.i("Simple DCIM Image Delete", "Deleted Image " + image_path);
                }
                else
                {
//                    Log.i("Simple DCIM Image Delete", "Can't Delete Image " + image_path);
                    deleteFileFromMediaStore(context.getContentResolver(), file);
                }
            }
        }
    }

    public static void deleteFileFromMediaStore(final ContentResolver contentResolver, final File file) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            String canonicalPath;
            try {
                canonicalPath = file.getCanonicalPath();
            } catch (IOException e) {
                canonicalPath = file.getAbsolutePath();
            }
            final Uri uri = MediaStore.Files.getContentUri("external");
            final int result = contentResolver.delete(uri,
                    MediaStore.Files.FileColumns.DATA + "=?", new String[]{canonicalPath});
            if (result == 0) {
                final String absolutePath = file.getAbsolutePath();
                if (!absolutePath.equals(canonicalPath)) {
                    contentResolver.delete(uri,
                            MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
                }
            }
        }
    }

    public static int getCameraHandlerRequestCode() {
        return CAMERA_HANDLER_REQUEST_CODE;
    }

    public String getCurrentPhotoPath() {
        if(currentPhotoPath==null) return null;
        return currentPhotoPath;
    }

    public String getCurrentPhotoThumbnailPath() {
        if(currentPhotoThumbnailPath==null) return null;
        return currentPhotoThumbnailPath;
    }

    public Uri getPhotoURI() {
        if (photoURI == null) return null;
        return photoURI;
    }

    @Nullable
    public InputImage getImage(Activity activity) {
        try {
            return InputImage.fromFilePath(activity.getApplicationContext(),photoURI);
        } catch (Exception e) {
            return null;
        }
    }

    private File CreateImageFileWithUniqueName(Activity activity) throws IOException {
        String extention = ".jpg";
        imageFileName = "JPEG_" + getDateTimeString() + "_";
        String imageThumbnailName = imageFileName + "thumbnail";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(imageFileName,extention,storageDir);
        File image_thumbnail_name = File.createTempFile(imageThumbnailName,extention,storageDir);
        currentPhotoPath = image.getAbsolutePath();
        currentPhotoThumbnailPath = image_thumbnail_name.getAbsolutePath();
        return image;
    }





}
