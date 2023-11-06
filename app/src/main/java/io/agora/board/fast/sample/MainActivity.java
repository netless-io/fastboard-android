package io.agora.board.fast.sample;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
        adapter.setOnItemClickListener(this::startTestCase);
        testcaseRv.setAdapter(adapter);

        // startTestCase(Repository.get().getTestCases().get(4));
    }

    private void startTestCase(TestCase testCase) {
        if (testCase.isLive()) {
            Intent intent = new Intent(this, testCase.clazz);
            intent.putExtra(Constants.KEY_ROOM_UUID, testCase.roomInfo.roomUUID);
            intent.putExtra(Constants.KEY_ROOM_TOKEN, testCase.roomInfo.roomToken);
            startActivity(intent);
        }
    }
}