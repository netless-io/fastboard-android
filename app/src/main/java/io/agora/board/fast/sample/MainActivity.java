package io.agora.board.fast.sample;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import io.agora.board.fast.sample.cases.RoomActivity;
import io.agora.board.fast.sample.misc.Repository;
import io.agora.board.fast.sample.misc.TestCase;

public class MainActivity extends AppCompatActivity {
    private RecyclerView testcaseRv;
    private TestCaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUi();
    }

    private void setupUi() {
        testcaseRv = findViewById(R.id.testcase_rv);
        adapter = new TestCaseAdapter(Repository.get().getTestCases());
        testcaseRv.setAdapter(adapter);
        adapter.setOnItemClickListener(testCase -> {
            startTestCase(testCase);
        });
    }

    private void startTestCase(TestCase testCase) {
        if (testCase.isLive()) {
            Intent intent = new Intent(this, RoomActivity.class);
            intent.putExtra(Constants.KEY_ROOM_UUID, testCase.roomInfo.roomUUID);
            intent.putExtra(Constants.KEY_ROOM_TOKEN, testCase.roomInfo.roomToken);
            startActivity(intent);
        }
    }
}