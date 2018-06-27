package com.houxj.andlattice.debug;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

/**
 * Created by Administrator on 2018/6/15.
 */

public class MyTest {
    public static void readHZK12Data(Context context){
        try {
            InputStream in = context.getResources().getAssets().open("fonts/HZKSZ12");
            int lenght = in.available();
            byte[] HZKSZ12_FONT_DATA = new byte[lenght + 1];
            int read_len = in.read(HZKSZ12_FONT_DATA);
            in.close();
            int col = 16;
            StringBuilder builder = new StringBuilder();
            Log.i("hou", "" + read_len);
            for (int i=0; i< read_len; i++){
                if(i % col == 0 && i != 0){
                    builder.append("\r\n");
                }
                builder.append(String.format(Locale.CHINA, "0x%02X, ",HZKSZ12_FONT_DATA[i]));
            }
            //写入
            String filename = Environment.getExternalStorageDirectory().getAbsolutePath() +"/HZKSZ12.txt";
            Log.i("hou", "Write " + filename);
            OutputStream out = new FileOutputStream(filename);
            out.write(builder.toString().getBytes());
            out.close();
            Log.i("hou", "Write OK");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
