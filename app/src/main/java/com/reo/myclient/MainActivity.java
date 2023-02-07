package com.reo.myclient;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler;

    InetAddress serverAddr;
    Socket socket;
    PrintWriter sendWriter;
    private String ip = "118.222.245.17";
    private int port = 8888;

    TextView textView;
    String UserID;

    Button connectbutton;
    Button chatbutton;
    TextView chatView;
    EditText message;
    String sendmsg;
    String read;

    @Override
    protected void onStop() { //액티비티를 종료하지 않고 다른 액티비티 실행(새로시작하는 활동이 화면전체를 차지하여 안드로이드 생명주기 중 onStop()콜백)
        super.onStop();
        try {
            sendWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //쓰레드안에서 UI를 수정할 수 없기때문에 Handler를 통해 Thread UI에 접근하기 위함
        mHandler = new Handler();
        textView = (TextView) findViewById(R.id.textView);
        chatView = (TextView) findViewById(R.id.chatView);
        message = (EditText) findViewById(R.id.message);
        Intent intent = getIntent();
        UserID = intent.getStringExtra("username");
        textView.setText(UserID);
        chatbutton = (Button) findViewById(R.id.chatbutton);

        new Thread() {//쓰레드를 생성합니다
            public void run() {
                try {
                    InetAddress serverAddr = InetAddress.getByName(ip);//호스트 이름을 문자열로 반환
                    socket = new Socket(serverAddr, port);              //클라이언트에서 소켓을 열어줍니다
                    sendWriter = new PrintWriter(socket.getOutputStream());
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (true) {
                        //클라이언트로부터 메시지를 입력받습니다.
                        read = input.readLine();

                        System.out.println("TTTTTTTT" + read);
                        if (read != null) {
                            mHandler.post(new msgUpdate(read)); //메시지를 작성하는 UI핸들러와 내용을 .post()합니다
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();//쓰레드를 실행시킵니다

        //Chatbutton 눌렸을때
        chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmsg = message.getText().toString();
                new Thread() {//스레드를 생성합니다
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sendWriter.println(UserID + ">" + sendmsg); //유저아이디>채팅내용을 출력합니다
                            sendWriter.flush();
                            message.setText(""); //메시지를 초기화시켜줍니다
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start(); //스레드를 실행시킵니다.
            }
        });
    }

    class msgUpdate implements Runnable { //메시지를 업데이트하는  쓰레드를 Implements Runnable로 추가 (다중상속을 제한받는 extends Thread보다 확장성 용의)
        private final String msg;

        public msgUpdate(String str) {
            this.msg = str;
        }

        @Override
        public void run() {
            chatView.setText(
                    chatView.getText().toString() + msg + "\n"); //메시지를 한줄씩 띄어서 표시해줍니다
        }
    }
}