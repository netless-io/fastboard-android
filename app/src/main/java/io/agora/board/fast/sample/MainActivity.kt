package io.agora.board.fast.sample

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import io.agora.board.fast.sample.misc.Repository
import io.agora.board.fast.sample.misc.TestCase

class MainActivity : AppCompatActivity() {
    private var testcaseRv: RecyclerView? = null
    private var adapter: TestCaseAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUi()
    }

    private fun setupUi() {
        testcaseRv = findViewById<RecyclerView>(R.id.testcase_rv)
        adapter = TestCaseAdapter(Repository.get().getTestCases())
        adapter!!.setOnItemClickListener(TestCaseAdapter.OnItemClickListener { testCase: TestCase? ->
            this.startTestCase(
                testCase!!
            )
        })
        testcaseRv!!.setAdapter(adapter)

        // 显示版本信息
        findViewById<TextView>(R.id.tv_version).text = "v${BuildConfig.VERSION_NAME} (${BuildConfig.BUILD_VERSION})"

        // startTestCase(Repository.get().getTestCases().get(4));
    }

    private fun startTestCase(testCase: TestCase) {
        if (testCase.isLive()) {
            val intent = Intent(this, testCase.clazz)
            intent.putExtra(Constants.KEY_ROOM_UUID, testCase.roomInfo.roomUUID)
            intent.putExtra(Constants.KEY_ROOM_TOKEN, testCase.roomInfo.roomToken)
            startActivity(intent)
        }
    }
}