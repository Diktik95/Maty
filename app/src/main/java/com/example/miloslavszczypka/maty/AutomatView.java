package com.example.miloslavszczypka.maty;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by miloslavszczypka on 04.12.17.
 */

public class AutomatView extends View {
    TextView vyhraControl;
    TextView kreditControl;

    MediaPlayer startSound = MediaPlayer.create(getContext(), R.raw.start);
    MediaPlayer stopSound = MediaPlayer.create(getContext(), R.raw.stop);
    MediaPlayer vyhraSound = MediaPlayer.create(getContext(), R.raw.vyhra);

    int sazka;
    int kredit = 500;
    int vyhra = 0;

    int[] SETSTOP = {72,81,90};
    float[] SETINDEX = {0,0,0};

    int pocetValcu = 3;
    int pocetSymbolu = 9;
    int img_size = 200;

    Paint Styl;
    String[] valce = new String[3];
    String[] win = new String[3];
    Symbol[][] symboly = new Symbol[3][9];
    int FPS = 60;

    int[] linie;

    int[][][] vyherniLinie = {
            {{0,100}, {600,100}},
            {{0,300}, {600,300}},
            {{0,500}, {600,500}},
            {{0,0}, {600,600}},
            {{0,600}, {600, 0}}
    };

    boolean soundPlayed = false;
    boolean vykresluj = false;

    public AutomatView(Context context) {
        super(context);
    }

    public AutomatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutomatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Styl = new Paint();
        Styl.setColor(Color.rgb(0,0,255));
        Styl.setStrokeWidth(10);

        valce[0] = "BHLMPZTHZ";
        valce[1] = "MLKHBSBHM";
        valce[2] = "BBHKMLSBH";

        win[0] = "PHH";
        win[1] = "PPP";
        win[2] = "PHH";

        for (int a = 0 ; a < pocetValcu ; a++) {
            for (int i = 0 ; i < pocetSymbolu ; i++) {
                Drawable img = getResources().getDrawable(vratDrawableID(valce[a].charAt(i)));
                symboly[a][i] = new Symbol(img, i*img_size, a*img_size, valce[a].charAt(i));
            }
        }
        //move.run();

    }

    public void Reset(boolean started, int saz) {
        soundPlayed = false;
        vykresluj = false;
        sazka = saz;
        kredit -= saz;
        kreditControl.setText(String.valueOf(kredit));
        if(!started) {
            move.run();
        }
        else {
            SETINDEX[0] = 0;
            SETINDEX[1] = 0;
            SETINDEX[2] = 0;
        }
        new callAPI().execute();
    }

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable move = new Runnable() {
        public void run() {
            Start();
            handler.postDelayed(this, 1000/FPS);
        }
    };

    private int vratDrawableID(char c) {
        int resource = 0;

        switch(c) {
            case 'B':
                resource = R.drawable.bar;
                break;
            case 'H':
                resource = R.drawable.hrozno;
                break;
            case 'T':
                resource = R.drawable.tresne;
                break;
            case 'M':
                resource = R.drawable.meloun;
                break;
            case 'L':
                resource = R.drawable.listek;
                break;
            case 'P':
                resource = R.drawable.podkova;
                break;
            case 'K':
                resource = R.drawable.koruna;
                break;
            case 'S':
                resource = R.drawable.svestka;
                break;
            case 'Z':
                resource = R.drawable.zvonek;
                break;
        }

        return resource;
    }

    protected void onDraw(Canvas canvas) {
        ClearIt(canvas);
        Styl.setColor(Color.rgb(255, 0, 0));
        for(int a = 0 ; a < pocetValcu ; a++) {
            for(int i = 0 ; i < pocetSymbolu ; i++) {
                Drawable x = symboly[a][i].img;
                x.setBounds(symboly[a][i].sloupec, symboly[a][i].pos, symboly[a][i].sloupec+img_size, symboly[a][i].pos+img_size);
                x.draw(canvas);
            }
        }
        if(vykresluj) {
            for (int i = 0; i < linie.length; i++) {
                //canvas.drawLine(0, 0, 20, 20, Styl);
                canvas.drawLine(vyherniLinie[linie[i] - 1][0][0], vyherniLinie[linie[i] - 1][0][1], vyherniLinie[linie[i] - 1][1][0], vyherniLinie[linie[i] - 1][1][1], Styl);
            }
        }
        //ClearIt(canvas);
    }

    private void ClearIt(Canvas canvas) {
        Styl.setColor(Color.rgb(255, 255, 255));
        canvas.drawRect(0, 0, 600, 600, Styl);
    }

    private void Start() {

        for(int a = 0 ; a < pocetValcu ; a++) {
            if(SETINDEX[a] == SETSTOP[a]) {
                if(a == 2) {
                    kreditControl.setText(String.valueOf(kredit));
                    vyhraControl.setText(String.valueOf(vyhra));
                    if(vyhra != 0) {
                        vykresluj = true;
                        if(soundPlayed == false) {
                            vyhraSound.start();
                            soundPlayed = true;
                        }
                    }

                }
                continue;
            }

            if(SETINDEX[a] == (a*pocetSymbolu) + 4 + 32) {
                for(int i = 0 ; i < win[a].length() ; i++) {

                    Drawable x = getResources().getDrawable(vratDrawableID(win[a].charAt(i)));
                    symboly[a][i].img = x;
                }
            }

            for(int i = 0 ; i < pocetSymbolu ; i++) {
                if(((pocetSymbolu - 3) * img_size * (-1)) + (img_size / 2) == symboly[a][i].pos) {
                    symboly[a][i].pos = 3 * img_size;
                }
                else {
                    symboly[a][i].pos -= img_size / 2;
                }
            }

            SETINDEX[a] += 0.5;
        }
        //System.out.println("a");
        invalidate();

    }

    public class callAPI extends AsyncTask<String, String, String> {
        String result;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String data = "?sazka=" + URLEncoder.encode(String.valueOf(sazka),"UTF-8");

                String link = "http://diktik.g6.cz/Automat/automat.php" + data;
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = br.readLine();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            JSONObject jsonObj = null;
            String[][] a = new String[3][3];;
            try {
                jsonObj = new JSONObject(s);

                JSONArray arrJson = jsonObj.getJSONArray("valce");
                System.out.println(arrJson);
                for (int i = 0 ; i < arrJson.length() ; i++) {
                    JSONArray second = arrJson.getJSONArray(i);
                    for (int j = 0 ; j < second.length() ; j++){
                        System.out.println(second.getString(j));
                        a[i][j] = second.getString(j);
                    }
                    win[i] = a[i][0] + a[i][1] + a[i][2];
                }
                for (int i = 0 ; i < win.length ; i++) {
                    System.out.println(win[i] + '\n');
                }

                vyhra = jsonObj.getInt("vyhra");
                kredit += vyhra;

                if(vyhra != 0) {
                    JSONArray arrLinie = jsonObj.getJSONArray("linie");
                    System.out.println(arrLinie);
                    linie = new int[arrLinie.length()];
                    for (int i = 0 ; i < arrLinie.length() ; i++) {
                        linie[i] = arrLinie.getInt(i);
                    }

                    for (int i = 0 ; i < linie.length ; i++) {
                        System.out.println(linie[i]);
                    }
                }
                //a = (String[][])jsonObj.get("valce");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(jsonObj);
            //System.out.println(a[0][0]);
        }
    }

    public void playStart() {
        startSound.start();
    }

    public void setControls(TextView k, TextView v) {
        kreditControl = k;
        vyhraControl = v;

        kreditControl.setText(String.valueOf(kredit));
        vyhraControl.setText(String.valueOf(0));
    }

    public int getKredit() {
        return kredit;
    }

}
