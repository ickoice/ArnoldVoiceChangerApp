package com.radanlievristo.arnoldvoicechangerapp;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

public class UtilFunctions {

    public UtilFunctions(){}

    public static String writeResponseToDisk(ResponseBody body, Activity activity) {

        try {

            File audioFile = new File(activity.getApplicationContext().getExternalFilesDir(null) + File.separator + "whatArnoldSaid.wav");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {

                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(audioFile);

                while (true) {

                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.i("FileSizeDownloaded: ", String.valueOf(fileSizeDownloaded));

                }

                outputStream.flush();

                return audioFile.getAbsolutePath();

            } catch (IOException e) {
                return e.toString();
            } finally {

                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }

            }

        } catch (IOException e) {
            return e.toString();
        }

    }

    public static void playSound(Uri uri, Activity activity) {

        try {

            MediaPlayer mediaPlayer = new MediaPlayer();

            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );

            mediaPlayer.setDataSource(activity.getApplicationContext(), uri);

            mediaPlayer.prepare();

            mediaPlayer.start();

        } catch (IOException e) {
            Toast.makeText(activity.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }

}
