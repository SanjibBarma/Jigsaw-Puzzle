package ltd.v2.game1.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ltd.v2.game1.R;
import ltd.v2.game1.helper.PuzzleLayout;

public class PuzzleActivity extends AppCompatActivity implements Runnable{
    PuzzleLayout puzzleLayout;
    ImageView ivTips;
    int squareRootNum = 2;
    int drawableId = R.mipmap.mcqueen;
    String startTime = "";
    String endTime = "";
    boolean gameStatus = false;
    AlertDialog successDialog;
    View idBlockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        ivTips = (ImageView) findViewById(R.id.iv_tips);
        idBlockView = (View) findViewById(R.id.idBlockView);
        puzzleLayout = (PuzzleLayout) findViewById(R.id.activity_swipe_card);

        ivTips.setImageResource(drawableId);
        puzzleLayout.setImage(drawableId, squareRootNum);
        startTime = getCurrentTime();

        puzzleLayout.setOnCompleteCallback(new PuzzleLayout.OnCompleteCallback() {
            @Override
            public void onComplete() {
                gameStatus = true;
                idBlockView.setVisibility(View.VISIBLE);
                puzzleLayout.postDelayed(PuzzleActivity.this, 10);
            }
        });
    }

    @Override
    public void run() {
        showSuccessPopup();
        endTime = getCurrentTime();
    }

    private void showSuccessPopup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        successDialog = builder.create();
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View customView = layoutInflater.inflate(R.layout.congratulate_popup, null);
        LinearLayout saveData = (LinearLayout) customView.findViewById(R.id.saveData);
        saveData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                successDialog.dismiss();
                finishAffinity();
            }
        });
        successDialog.setCancelable(false);
        successDialog.setView(customView);
        successDialog.show();
        successDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    private void sendDataToBroadcast() {
        Intent intent = new Intent();
        intent.setAction("v2.ltd.ecrm3.app");
        intent.putExtra("start_time", startTime);
        if (endTime.equals("")){
            intent.putExtra("end_time", "");
        }else {
            intent.putExtra("end_time", endTime);
        }
        intent.putExtra("status", gameStatus);
        sendBroadcast(intent);
        Log.d("sendDataToBroadcast: ", "\nstartTime: "+startTime+"\nendTime: "+endTime+"\ngameStatus: "+gameStatus);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendDataToBroadcast();
    }

    public static String getCurrentTime() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String sdt = df.format(new Date(System.currentTimeMillis()));
        Log.d("CurrentTime", sdt);
        return sdt;
    }
}