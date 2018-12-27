package com.diverapp.diverapp.PDiver;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddImageFromStudio  {

    List<String> imagePaths = new ArrayList();
    //    Activity activity;
//    Context context;
    Uri imageCaptureUri;
    int Pick_FROM_FILE = 3;
    static double THUMBNAIL_SIZE = 80;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public void pickProfileImage(Activity activity, Context mCtx) {
//        if (EasyPermissions.hasPermissions(mCtx, galleryPermissions)) {
//        } else {
//            EasyPermissions.requestPermissions(this, "Access for storage",
//                    101, galleryPermissions);
//        }
//        Intent pickerPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(pickerPhotoIntent, Pick_FROM_FILE);
//        System.out.println("imageView LoGOG");
    }

    public static Bitmap getThumbnail(Uri uri,Context context) throws FileNotFoundException, IOException {
        InputStream input = context.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig= Bitmap.Config.ARGB_8888;//
        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//        Bitmap bitmap = null;
//        String path;
//        RoundedBitmapDrawable roundedBitmapDrawableFactory = null;
//        if (requestCode == Pick_FROM_FILE) {
//            imageCaptureUri = data.getData();
//            path = getRealPathFromURI(imageCaptureUri);
//            System.out.println("path "+ path);
//            if (path == null) {
//                path = imageCaptureUri.getPath();
//            }
//            if (path != null) {
//                bitmap = BitmapFactory.decodeFile(path);
//                roundedBitmapDrawableFactory =
//                        RoundedBitmapDrawableFactory.create(getResources(), bitmap);
//                roundedBitmapDrawableFactory.setCircular(true);
////                mPath = path;
//                imagePaths.add(path);
//                System.out.println("imagePaths "+ imagePaths.size());
//            }
////            logo.setImageBitmap(bitmap);
////            logo.setImageDrawable(roundedBitmapDrawableFactory);
//            System.out.println("data " + data.getData());
//        }
//    }

//    public String getRealPathFromURI(Uri contentURI){
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = managedQuery(contentURI,proj,null,null,null);
//        if (cursor == null) return  null;
//        int colmunIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return  cursor.getString(colmunIndex);
//    }
}
