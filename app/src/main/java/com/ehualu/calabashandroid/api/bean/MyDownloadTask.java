package com.ehualu.calabashandroid.api.bean;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.ehualu.calabashandroid.fragment.FileFragment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyDownloadTask extends AsyncTask<Void, Void, Void> {

    private long startIndex;
    private long endIndex;
    private String fileId;
    private long current;

    public MyDownloadTask(long startIndex, long endIndex, String fileId) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.fileId = fileId;
    }

    public long getCurrent() {
        return current;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://49.7.59.236:13800/calabash/file/_downloadFile?fileId=" + fileId + "&startIndex=" + startIndex + "&endIndex=" + endIndex).openConnection();
            InputStream inputStream = con.getInputStream();
            byte[] bs = new byte[500];
            int length = 0;
            RandomAccessFile raf = new RandomAccessFile(new File(Environment.getExternalStorageDirectory() + File.separator + "aaggg.mp3"), "rwd");
            while ((length = inputStream.read(bs)) != -1) {
                raf.write(bs, 0, length);
                current += length;
                publishProgress();
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        long total = 0;
        for (MyDownloadTask t : FileFragment.tasks) {
            total += t.getCurrent();
        }
        Log.e("当前进度:", "---------" + total * 100 / 4428404 + "%");
        super.onProgressUpdate(values);
    }
}
