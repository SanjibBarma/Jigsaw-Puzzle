package ltd.v2.game1.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    int drawableId = R.drawable.hw_kv;
    String startTime = "";
    String endTime = "";
    boolean gameStatus = false;
    AlertDialog successDialog;
    View idBlockView;
    TextView tvGameVersion;
    String primaryBrand = "Hollywood";
    //boolean flag = false;
    String brandName;
//    BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            brandName = intent.getStringExtra("brandName");
//
//            if (brandName == null || brandName.equals("")){
//                Toast.makeText(context, "Broadcast not Received!", Toast.LENGTH_SHORT).show();
//            }else {
//                flag = true;
//            }
//        }
//    };
//    ParcelReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        ivTips = (ImageView) findViewById(R.id.iv_tips);
        idBlockView = (View) findViewById(R.id.idBlockView);
        puzzleLayout = (PuzzleLayout) findViewById(R.id.activity_swipe_card);
        tvGameVersion = (TextView) findViewById(R.id.tvGameVersion);

//        receiver = new ParcelReceiver();

//        IntentFilter filter = new IntentFilter("ltd.v2.game1");
////        registerReceiver(receiver, filter);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            registerReceiver(receiver, filter, RECEIVER_EXPORTED);
//        }else {
//            registerReceiver(receiver, filter);
//        }

        //String receivedParcel = getIntent().getStringExtra("brandName");
//        if (flag){
//            if (brandName.equals("Hollywood")){
//                Log.d("receivedString: ", brandName);
//                drawableId = R.drawable.hw_kv;
//            }else {
//                Log.d("receivedString: ", brandName);
//                drawableId = R.drawable.db_kv;
//            }
//        }else {
//            drawableId = R.drawable.db_kv;
//            Log.d("receivedString: ", "Parcel not received");
//        }
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        String version = pInfo.versionName;
        tvGameVersion.setText("Version - "+ version);

//        String[] imageResourceNames = {"db_kv", "hw_kv"};
//        // Select a random image resource name
//        Random random = new Random();
//        String randomImageName = imageResourceNames[random.nextInt(imageResourceNames.length)];
//        // Get the resource ID of the selected image
//        int resourceId = getResources().getIdentifier(randomImageName, "drawable", getPackageName());

        ivTips.setImageResource(drawableId);
        puzzleLayout.setImage(drawableId, squareRootNum);
//        puzzleLayout.setImage(resourceId, squareRootNum);
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
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        IntentFilter filter = new IntentFilter("ltd.v2.game1");
////        registerReceiver(receiver, filter);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            registerReceiver(receiver, filter, RECEIVER_EXPORTED);
//        }else {
//            registerReceiver(receiver, filter);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(receiver);
//    }
}