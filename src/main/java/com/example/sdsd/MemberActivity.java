package com.example.sdsd;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MemberActivity extends AppCompatActivity {
    private static final String TAG = "MemberInitActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);

        findViewById(R.id.checkButton).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        //finish();
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.checkButton:
                    profileUpdate();
                    break;
            }
        }
    };

    private void profileUpdate(){
        String name = ((EditText)findViewById(R.id.nameEditText)).getText().toString();
        String phoneNum = ((EditText)findViewById(R.id.phoneNumEditText)).getText().toString();
        String birth = ((EditText)findViewById(R.id.birthEditText)).getText().toString();
        String address = ((EditText)findViewById(R.id.addressEditText)).getText().toString();

        if(name.length() >0 && phoneNum.length() > 9 && birth.length() > 5 && address.length() > 0){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            MemberInfo memberInfo = new MemberInfo(name, phoneNum, birth, address);
            if(user != null){
                db.collection("users").document(user.getUid()).set(memberInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("회원정보 등록을 성공하였습니다.");
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("회원정보 등록에 실패하였습니다.");
                                Log.e(TAG, "Error writing document",e);
                            }
                        });
            }

        }else{
            startToast("회원 정보를 입력해주세요.");
        }
    }


    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


}