package com.mlsdev.rximagepicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RxImageConverters {

    public static Observable<File> uriToFile(final Context context, final Uri uri, final File file) {
        return Observable.fromCallable(new Callable<File>() {
                    @Override
                    public File call() throws Exception {
                        InputStream inputStream = context.getContentResolver().openInputStream(uri);
                        copyInputStreamToFile(inputStream, file);
                        return file;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Bitmap> uriToBitmap(final Context context, final Uri uri) {
        return Observable.fromCallable(new Callable<Bitmap>() {
                    @Override
                    public Bitmap call() throws Exception {
                        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static void copyInputStreamToFile(InputStream in, File file) throws IOException {
        OutputStream out = new FileOutputStream(file);
        byte[] buf = new byte[10 * 1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        in.close();
    }

}
