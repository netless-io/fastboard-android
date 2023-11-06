package io.agora.board.fast.sample.cases.hione

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.herewhite.sdk.domain.WindowParams
import io.agora.board.fast.FastRoom
import io.agora.board.fast.Fastboard
import io.agora.board.fast.FastboardView
import io.agora.board.fast.extension.FastResource
import io.agora.board.fast.model.ControllerId
import io.agora.board.fast.model.DocPage
import io.agora.board.fast.model.FastRegion
import io.agora.board.fast.model.FastRoomOptions
import io.agora.board.fast.sample.Constants
import io.agora.board.fast.sample.R
import io.agora.board.fast.sample.cases.base.BaseActivity
import io.agora.board.fast.sample.misc.Repository

open class HiOneActivity : BaseActivity() {
    private val repository = Repository.get()
    private lateinit var fastboardView: FastboardView
    private lateinit var fastboard: Fastboard
    private lateinit var fastRoom: FastRoom

    private lateinit var hiOneLayout: HiOneLayout
    private lateinit var filesLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_one)

        initView()
        setupFastboard()
    }

    private fun initView() {
        hiOneLayout = findViewById(R.id.hi_one_layout)
        hiOneLayout.setHiOneLayoutListener(object : HiOneLayout.HiOneLayoutListener {
            override fun onCloudStorageClick() {
                filesLayout.isVisible = !filesLayout.isVisible
            }
        })

        filesLayout = findViewById(R.id.files_layout)
        findViewById<View>(R.id.insert_pptx).setOnClickListener {
            val uuid = "dc01ee126edc4ce7be8da3f7361a2f70"
            val prefix =
                "https://conversion-demo-cn.oss-cn-hangzhou.aliyuncs.com/demo/dynamicConvert"
            val title = "开始使用 Flat"
            fastRoom.insertPptx(uuid, prefix, title, null)

            filesLayout.isVisible = false
        }

        findViewById<View>(R.id.insert_static).setOnClickListener {
            val pages = Repository.get().getDocPages("8da4cdc71a9845d385a5b58ddfa10b7e")
            val title = "开始使用 Flat"
            fastRoom.insertStaticDoc(pages, title, null)

            filesLayout.isVisible = false
        }

        findViewById<View>(R.id.insert_image).setOnClickListener {
            val imageUrl =
                "https://flat-storage.oss-accelerate.aliyuncs.com/cloud-storage/2022-02/15/ebe8320a-a90e-4e03-ad3a-a5dc06ae6eda/ebe8320a-a90e-4e03-ad3a-a5dc06ae6eda.png"
            val width = 512.0
            val height = 512.0

            val pages = listOf(DocPage(imageUrl, width, height)).toTypedArray()
            fastRoom.insertStaticDoc(pages, "单图片", null)

            filesLayout.isVisible = false
        }
    }

    private fun setupFastboard() {
        fastboardView = findViewById(R.id.fastboard_view)
        fastboard = fastboardView.fastboard

        val roomOptions = FastRoomOptions(
            Constants.SAMPLE_APP_ID,
            intent.getStringExtra(Constants.KEY_ROOM_UUID),
            intent.getStringExtra(Constants.KEY_ROOM_TOKEN),
            repository.userId,
            FastRegion.CN_HZ
        )
        // window params
        val roomParams = roomOptions.roomParams.apply {
            windowParams = WindowParams()
                .setContainerSizeRatio(9f / 16)
                .setChessboard(false)
        }
        roomOptions.roomParams = roomParams

        fastRoom = fastboard.createFastRoom(roomOptions)

        //set whiteboard and FastboardView background
        fastRoom.setResource(object : FastResource() {
            override fun getBackgroundColor(darkMode: Boolean): Int {
                return Color.BLACK
            }
        })
        fastRoom.join { room ->
            hiOneLayout.attachRoom(room)
        }

        // hide all interactive controllers
        hideController()
    }

    private fun hideController() {
        fastboardView.uiSettings.hideRoomController(
            ControllerId.RedoUndo,
            ControllerId.PageIndicator,
            ControllerId.ToolBox
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        fastRoom.destroy()
    }
}