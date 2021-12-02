package io.agora.board.fast;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUi();
    }

    private void setupUi() {
        findViewById(R.id.join_room).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RoomActivity.class);
            startActivity(intent);
        });
    }
}