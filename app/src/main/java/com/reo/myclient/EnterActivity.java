package com.reo.myclient;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterActivity extends AppCompatActivity {
    EditText editText;
    Button enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //onCreate()활동 수명주기 콜백
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);//R은 res폴더를 의미하고.layout은 res의 내부 클래스,activity_enter는 말그대로 activity_enter.xml을 의미합니다.
        enterButton = (Button) findViewById(R.id.enterButton);
        editText = (EditText) findViewById(R.id.editText);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //인텐트(Intent)란 이러한 어플리케이션 구성요소(컴포넌트) 간에 작업 수행을 위한 정보를 전달하는 역할을 한다
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);//getApplicationContext()반대로 new Intent(this,...)는 activity context를 가르킨다
                String username = editText.getText().toString();
                intent.putExtra("username", username);
                startActivity(intent);//mainActivity.class를 실행시킵니다.
            }
        });

    }
}