package com.example.snake;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.snake.engine.GameEngine;
import com.example.snake.enums.Direction;
import com.example.snake.enums.GameState;
import com.example.snake.views.SnakeView;

public class MainActivity extends AppCompatActivity  {
    private GameEngine gameEngine;
    private SnakeView snakeView;
    private final Handler handler = new Handler();
    private final long updateDelay = 125;
    private float prevX, prevY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameEngine = new GameEngine();
        gameEngine.InitGame();
        snakeView = (SnakeView)findViewById(R.id.snakeView);
        startUpdateHandler();
    }
    private void startUpdateHandler() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameEngine.update();

                if(gameEngine.getCurrentGameState() == GameState.Running) {
                    handler.postDelayed(this, updateDelay);
                }
                if(gameEngine.getCurrentGameState() == GameState.Lost) {
                    onGameLost();
                }
                snakeView.setSnakeViewMap(gameEngine.GetMap());
                snakeView.invalidate();
                snakeView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                prevX = motionEvent.getX();
                                prevY = motionEvent.getY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                float newX = motionEvent.getX();
                                float newY = motionEvent.getY();

                                // calc where we swiped
                                if (Math.abs(newX - prevX) > Math.abs( newY - prevY)){
                                    // left or right
                                    if (newX > prevX){
                                        // right
                                        gameEngine.UpdateDirection(Direction.East);
                                    }else {
                                        //left
                                        gameEngine.UpdateDirection(Direction.West);

                                    }
                                }else {
                                    // up or down
                                    if (newY < prevY){
                                        // up
                                        gameEngine.UpdateDirection(Direction.North);

                                    }else {
                                        //down
                                        gameEngine.UpdateDirection(Direction.South);

                                    }
                                }
                                break;
                        }
                        return true;
                    }
                });

            }
        }, updateDelay);
    }
    private void onGameLost() {
        Toast.makeText(this, "You Lost", Toast.LENGTH_SHORT).show();
    }


}
