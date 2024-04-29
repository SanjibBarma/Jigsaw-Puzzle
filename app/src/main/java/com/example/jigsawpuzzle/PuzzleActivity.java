package com.example.jigsawpuzzle;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PuzzleActivity extends AppCompatActivity implements Runnable{
    PuzzleLayout puzzleLayout;
    ImageView ivTips;
    int squareRootNum = 3;
    int drawableId = R.mipmap.mcqueen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        ivTips = (ImageView) findViewById(R.id.iv_tips);
        ivTips.setImageResource(drawableId);
        puzzleLayout = (PuzzleLayout) findViewById(R.id.activity_swipe_card);
        puzzleLayout.setImage(drawableId, squareRootNum);
        puzzleLayout.setOnCompleteCallback(new PuzzleLayout.OnCompleteCallback() {
            @Override
            public void onComplete() {
                Toast.makeText(PuzzleActivity.this, R.string.next, Toast.LENGTH_LONG).show();
                puzzleLayout.postDelayed(PuzzleActivity.this, 800);
            }
        });
    }

    @Override
    public void run() {
        //if want to continue to hard mode
        squareRootNum++;
        drawableId++;
        if(squareRootNum > 10){
            Toast.makeText(PuzzleActivity.this, R.string.complete, Toast.LENGTH_SHORT).show();
            showDialog();
        }else {
            ivTips.setImageResource(R.mipmap.mcqueen);
            puzzleLayout.setImage(R.mipmap.mcqueen, squareRootNum);
        }

    }

    private void showDialog() {
        new AlertDialog.Builder(PuzzleActivity.this)
                .setTitle(R.string.success)
                .setMessage(R.string.restart)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                squareRootNum = 3;
                                drawableId = R.mipmap.mcqueen;
                                ivTips.setImageResource(drawableId);
                                puzzleLayout.setImage(drawableId, squareRootNum);
                            }
                        }).setNegativeButton(R.string.exit,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
    }

}