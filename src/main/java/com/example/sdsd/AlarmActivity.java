package com.example.sdsd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AlarmActivity extends Activity {
    TextView timeText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        timeText = findViewById(R.id.alarmText);

        Intent intent = getIntent();
        String guName = intent.getExtras().getString("guText");
        String dongName = intent.getExtras().getString("dongText");

        switch (guName){
            case "남구":
            case "서구":
            case "달서구":
            case "동구":
                getInfo(guName, dongName, 1);
                break;
            case "수성구":
                getInfo(guName, dongName, 2);
                break;
            case "달성군":
            case "북구":
                getInfo(guName, dongName, 3);
                break;
            case "중구":
                getInfo(guName, dongName, 4);
                break;
            default:
                timeText.setText("위치 정보를 설정해주세요.");
                break;
        }

        findViewById(R.id.closeButton).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    //바깥레이어 클릭 막기
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
    //백버튼 막기
    @Override
    public void onBackPressed() {
        return;
    }

    @SuppressLint("SetTextI18n")
    public void getInfo(String gu, String dong, int c){

        AssetManager assetManager = getAssets();
        try {
            InputStream is = assetManager.open("jsons/trash-85596-default-rtdb-export.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while(line!=null){
                buffer.append(line+"\n");
                line = reader.readLine();
            }
            String jsonData = buffer.toString();

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject norTrash = jsonObject.getJSONObject("생활 쓰레기");
            JSONObject reTrash = jsonObject.getJSONObject("재활용품 쓰레기");
            JSONObject reTrashDay = reTrash.getJSONObject("배출일");

            switch (c){
                case 1:
                    String norInfo1 = norTrash.getString(gu);
                    JSONObject gu1 = reTrashDay.getJSONObject(gu);
                    String reInfo1 = gu1.getString(dong);
                    timeText.setText("생활 쓰레기차 시간 :\n" + norInfo1 + "\n\n재활용품 쓰레기차 시간 :\n" + reInfo1);
                    break;
                case 2:
                    JSONObject gu2 = norTrash.getJSONObject(gu);
                    String norInfo2 = gu2.getString(gu);
                    JSONObject gu22 = reTrashDay.getJSONObject(gu);
                    String reInfo2 = gu22.getString(dong);
                    timeText.setText("생활 쓰레기차 시간 :\n" + norInfo2 + "\n\n재활용품 쓰레기차 시간 :\n" + reInfo2);
                case 3:
                    String norInfo3 = norTrash.getString(gu);
                    timeText.setText("생활 쓰레기차 시간 :\n" + norInfo3);
                case 4:
                    String reInfo4 = reTrashDay.getString(gu);
                    timeText.setText("재활용품 쓰레기차 시간 :\n" + reInfo4);
            }
        }catch (IOException | JSONException e){e.printStackTrace();}

    }
}
