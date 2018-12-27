package com.diverapp.diverapp.PDiver;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.support.constraint.Constraints.TAG;

public class BackgroundImageResize extends AsyncTask <Bitmap ,Integer,byte[]> {
    Bitmap mBitmap;
    Uri mUri;
    Context mContex;

    byte[] mUploadBytes;
    public BackgroundImageResize(Uri uri) {
        if (uri != null){
            this.mUri = uri;
        }
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected byte[] doInBackground(Bitmap... bitmaps) {
        System.out.println("doInBackground");
        Log.d(TAG, "doInBackground: startes");
        if (mBitmap == null) {
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(mContex.getContentResolver(), mUri);
            }catch (IOException e ){
                Log.d(TAG, "doInBackground: IOException: " +e.getMessage());
            }
        }
        byte[] bytes = null;
        Log.d(TAG, "doInBackground: number before " + mBitmap.getByteCount() / 1000000);
        bytes = getByteArrayFromBitmap(mBitmap,50);
        Log.d(TAG, "doInBackground: number before " + bytes.length / 1000000);
        return bytes;
    }
    @Override
    protected void onPostExecute(byte[] bytes) {
        super.onPostExecute(bytes);
        mUploadBytes = bytes;
        

    }
    public static byte[] getByteArrayFromBitmap(Bitmap bitmap , int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,byteArrayOutputStream);
        return  byteArrayOutputStream.toByteArray();
    }
}
