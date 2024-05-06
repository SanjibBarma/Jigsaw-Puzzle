package ltd.v2.game1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PuzzleActivity extends AppCompatActivity implements Runnable{
    PuzzleLayout puzzleLayout;
    ImageView ivTips;
    int squareRootNum = 3;
    int drawableId = R.mipmap.mcqueen;
    String startTime = "";
    String endTime = "";
    boolean gameStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        ivTips = (ImageView) findViewById(R.id.iv_tips);
        ivTips.setImageResource(drawableId);
        puzzleLayout = (PuzzleLayout) findViewById(R.id.activity_swipe_card);
        puzzleLayout.setImage(drawableId, squareRootNum);

        startTime = getCurrentTime();
        puzzleLayout.setOnCompleteCallback(new PuzzleLayout.OnCompleteCallback() {
            @Override
            public void onComplete() {
                gameStatus = true;
                Toast.makeText(PuzzleActivity.this, R.string.success, Toast.LENGTH_LONG).show();
                puzzleLayout.postDelayed(PuzzleActivity.this, 800);
            }
        });
    }

    @Override
    public void run() {
        showDialog();
        endTime = getCurrentTime();
    }

    private void showDialog() {
        new AlertDialog.Builder(PuzzleActivity.this)
                .setTitle(R.string.success)
                .setCancelable(false)
                //.setMessage(R.string.restart)
                .setPositiveButton(R.string.thanks,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                finishAffinity();
                            }
                        }).show();
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