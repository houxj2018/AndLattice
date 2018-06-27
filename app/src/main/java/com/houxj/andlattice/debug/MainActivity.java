package com.houxj.andlattice.debug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.houxj.andlattice.LEDDefinition;
import com.houxj.andlattice.LatticeCoding;
import com.houxj.andlattice.bean.CharInfo;
import com.houxj.andlattice.fonts.Ascii12Font;
import com.houxj.andlattice.fonts.FangSong12Font;
import com.houxj.andlattice.fonts.FangSongX12Font;
import com.houxj.andlattice.fonts.FontBase;
import com.houxj.andlattice.fonts.HZK12Font;
import com.houxj.andlattice.fonts.KaiTiM12Font;
import com.houxj.andlattice.fonts.LiShu12Font;
import com.houxj.andlattice.fonts.MiNiJKai12Font;
import com.houxj.andlattice.fonts.MiNiJQian12Font;
import com.houxj.andlattice.fonts.MiNiJSong12Font;
import com.houxj.andlattice.fonts.SongTi12Font;
import com.houxj.andlattice.fonts.SongTiX12Font;
import com.houxj.andlattice.fonts.TingShan12Font;
import com.houxj.andlattice.fonts.YeGenYou12Font;
import com.houxj.andlattice.fonts.YouYuan12Font;
import com.houxj.andlattice.utils.HTimer;
import com.houxj.andlattice.utils.JLogEx;
import com.houxj.andlattice.R;
import com.houxj.andlattice.view.LedView;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LedView mLedView;
    private EditText mEtInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JLogEx.setDebugEnable(true);
//        JLogEx.setInfoEnable(true);
        setContentView(R.layout.activity_main);
        mLedView = (LedView)findViewById(R.id.ledView);
        mEtInput =(EditText)findViewById(R.id.et_input);
    }


    public void onClick(View view){
        if(view.getId() == R.id.but_hzk) {
            testCharValue();
            showLED();
        }else if(view.getId() == R.id.but_ascii){
            testAscii();
        }else if(view.getId() == R.id.but_fs){
            testFangSong();
        }else if(view.getId() == R.id.but_fsx){
            testFangSongX();
        }else if(view.getId() == R.id.but_kaishu){
            testKaiTi();
        }else if(view.getId() == R.id.but_lishu){
            testLiShu();
        }else if(view.getId() == R.id.but_songti){
            testSongTi();
        }else if(view.getId() == R.id.but_songtix){
            testSongTiX();
        }else if(view.getId() == R.id.but_youyuan){
            testYouYuan();
        }else if(view.getId() == R.id.but_ygyx){
            testYeGenYou();
        }else if(view.getId() == R.id.but_minij_kai){
            testMiniJKai();
        }else if(view.getId() == R.id.but_minij_qian){
            testMiniJQian();
        }else if(view.getId() == R.id.but_minij_song){
            testMiNiJSong();
        }else if(view.getId() == R.id.but_tingshan){
            testTingShan();
        }
    }

    private String getInput(){
        String input = mEtInput.getText().toString();
        if(input.length() == 0){
            input = "奇鹭人";
        }
        return input;
    }

    private void testMiniJKai(){
        FontBase MyFont = new MiNiJKai12Font(this);
        byte[] data = MyFont.getTextLattice(getInput());
        mLedView.setLatticeData(data);
    }

    private void testMiniJQian(){
        FontBase MyFont = new MiNiJQian12Font(this);
        byte[] data = MyFont.getTextLattice(getInput());
        mLedView.setLatticeData(data);
    }

    private void testMiNiJSong(){
        FontBase MyFont = new MiNiJSong12Font(this);
        byte[] data = MyFont.getTextLattice(getInput());
        mLedView.setLatticeData(data);
    }

    private void testTingShan(){
        FontBase MyFont = new TingShan12Font(this);
        byte[] data = MyFont.getTextLattice(getInput());
        mLedView.setLatticeData(data);
    }

    private void testYeGenYou(){
        YeGenYou12Font MyFont = new YeGenYou12Font(this);
        byte[] data = MyFont.getTextLattice(getInput());
        mLedView.setLatticeData(data);
    }

    private void testYouYuan(){
        YouYuan12Font MyFont = new YouYuan12Font(this);
        byte[] data = MyFont.getTextLattice(getInput());
        mLedView.setLatticeData(data);
    }

    private void testFangSong(){
        FangSong12Font MyFont = new FangSong12Font(this);
        byte[] data = MyFont.getTextLattice(getInput());

        mLedView.setLatticeData(data);
    }

    private void testFangSongX(){
        FangSongX12Font MyFont = new FangSongX12Font(this);
        byte[] data = MyFont.getTextLattice(getInput());

        mLedView.setLatticeData(data);
    }

    private void testSongTiX(){
        SongTiX12Font MyFont = new SongTiX12Font(this);
        byte[] data = MyFont.getTextLattice(getInput());

        mLedView.setLatticeData(data);
    }

    private void testSongTi(){
        SongTi12Font MyFont = new SongTi12Font(this);
        byte[] data = MyFont.getTextLattice(getInput());

        mLedView.setLatticeData(data);
    }

    private void testLiShu(){
        LiShu12Font MyFont = new LiShu12Font(this);
        byte[] data = MyFont.getTextLattice(getInput());

        mLedView.setLatticeData(data);
    }

    private void testKaiTi(){
        KaiTiM12Font MyFont = new KaiTiM12Font(this);
        byte[] data = MyFont.getTextLattice(getInput());

        mLedView.setLatticeData(data);
    }

    private void sendData(){
        LatticeCoding coding = new LatticeCoding.Builder(this)
//                .setWinking(true)
                .create();
        byte[] mydata = coding.createDataPack(getInput());
        mLedView.setDataPack(mydata);
        JLogEx.d("%02x",mydata);
    }

    private void testAscii(){
        Ascii12Font myFont = new Ascii12Font(this);
        byte[] data = myFont.getTextLattice("ABCDEF");
        mLedView.setLatticeData(data);
    }

    private void showLED(){
//        HZK12Font myFont = new HZK12Font(this);
//        byte[] data = myFont.getTextLattice("中国A");

//        Ascii12Font myFont = new Ascii12Font(this);
//        byte[] data = myFont.getTextLattice("ABCDEF");
//
//        mLedView.setLatticeData(data);

        LatticeCoding coding = new LatticeCoding.Builder(this)
                .setWinking(true)
                .create();
        byte[] mydata = coding.createDataPack(getInput());
        mLedView.setDataPack(mydata);
        JLogEx.d("%02x",mydata);
    }

    private void testCharValue(){
        String temp = "  AZaz啊再 34678";
        try {
            byte[] data = temp.getBytes("GB2312");
            byte[] asc = temp.getBytes("US-ASCII");
            StringBuilder builder = new StringBuilder();
            StringBuilder ascBuilder = new StringBuilder();
            for (int i=0; i< data.length; i++){
                builder.append(String.format(Locale.CHINA,"0x%X,",data[i]));
            }
            for (int i=0 ; i< asc.length; i++){
                ascBuilder.append(String.format(Locale.CHINA,"0x%X,",asc[i]));
            }
            JLogEx.d(builder.toString());
            JLogEx.d(ascBuilder.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void testMyChar(){
        char value = '啊';
        try {
            byte[] data = String.valueOf(value).getBytes("GBK");
            byte[] mydata = new byte[2];
            mydata[0] = (byte) ((value & 0xFF00) >> 8) ;
            mydata[1] = (byte) (value & 0x00FF);
            StringBuilder builder = new StringBuilder();
            StringBuilder ascBuilder = new StringBuilder();
            for (int i=0; i< data.length; i++){
                builder.append(String.format(Locale.CHINA,"0x%X,",data[i]));
            }
            for (int i=0 ; i< mydata.length; i++){
                ascBuilder.append(String.format(Locale.CHINA,"0x%X,",mydata[i]));
            }
            JLogEx.d(builder.toString());
            JLogEx.d(ascBuilder.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
