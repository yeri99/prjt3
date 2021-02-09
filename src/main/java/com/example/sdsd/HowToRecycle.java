package com.example.sdsd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.sdsd.R;

public class HowToRecycle extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_way);

        TextView kind = (TextView)findViewById(R.id.RecycleItem);
        TextView way = (TextView)findViewById(R.id.RecycleText);

        Intent intent = getIntent();

        String name = intent.getExtras().getString("name");
        kind.setText(name);

        switch (name){
            case "가스용기":
                way.setText("캔, 고철류 입니다. 구멍을 뚫어 배출해주세요.");
                break;
            case "계란":
                way.setText("일반 쓰레기 입니다.");
                break;
            case "신문지":
                way.setText("종이류 입니다. 4등분으로 접어주세요.");
                break;
            case "우유팩":
                way.setText("내용물을 비우고 물로 헹군 후 말리고 펼쳐서 끈으로 묶어 배출하세요.");
                break;
            case "유리병":
                way.setText("뚜껑 및 내용물을 제거해주세요.");
                break;
            case "형광등":
                way.setText("공동주택, 동 행정복지센터 분리배출함에 배출하세요.");
                break;
            default:
                way.setText("해당 단어는 검색되지 않습니다.");
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

}
