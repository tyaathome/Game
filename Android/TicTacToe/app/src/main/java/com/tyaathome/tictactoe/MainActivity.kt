package com.tyaathome.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Group
import androidx.core.view.get
import com.tyaathome.tictactoe.data.Chess
import com.tyaathome.tictactoe.data.ChessResult

class MainActivity : AppCompatActivity() {

    private var nextChessType = Chess.CROSS
    private val answerList = listOf(
        "012",
        "345",
        "678",
        "036",
        "147",
        "258",
        "048",
        "246"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val group = findViewById<Group>(R.id.buttons)
        group.setAllOnClickListener {
            if(it is ImageButton) {
                jumpTo(it)
            }
        }
        findViewById<View>(R.id.btn_reset).setOnClickListener {
            reset()
        }
    }

    /**
     * 落子
     */
    private fun jumpTo(btn: ImageButton) {
        val currentChessType = nextChessType
        val parent = findViewById<ViewGroup>(R.id.layout_root)
        var index = -1
        for(i in 0 until parent.childCount) {
            if(parent[i].id != View.NO_ID && parent[i].id == btn.id) {
                index = i
                break
            }
        }
        if(index == -1) {
            return
        }
        if(btn.tag != null) {
            Toast.makeText(this, "该处已落子", Toast.LENGTH_SHORT).show()
            return
        }
        val chess = Chess(currentChessType, index)
        btn.setImageResource(if(currentChessType == Chess.CROSS) R.drawable.ic_chess_cross else R.drawable.ic_chess_circle)
        btn.tag = chess
        when(val result = check(currentChessType)) {
            ChessResult.CROSS,
            ChessResult.CIRCLE,
            ChessResult.DRAW-> {
                showResultDialog(result)
            }
            ChessResult.CONTINUE -> {
                nextChessType = if(nextChessType == Chess.CROSS) Chess.CIRCLE else Chess.CROSS
            }
        }

    }

    /**
     * 获胜判断
     */
    private fun check(currentChessType: Int): ChessResult {
        var crossResult = ""
        var circleResult = ""
        var step = 0
        val parent = findViewById<ViewGroup>(R.id.layout_root)
        for(i in 0 until parent.childCount) {
            parent[i].tag?.let {
                if(it is Chess) {
                    when(it.type) {
                        Chess.CROSS -> crossResult += it.index.toString()
                        Chess.CIRCLE -> circleResult += it.index.toString()
                    }
                    step++
                }
            }
        }
        val result = if(currentChessType == Chess.CROSS) crossResult else  circleResult
        for(item in answerList) {
            if(result.contains(item)) {
                return if(currentChessType == Chess.CROSS) ChessResult.CROSS else ChessResult.CIRCLE
            }
        }
        // 处理平局
        if(step == 9) {
            return ChessResult.DRAW
        }
        return ChessResult.CONTINUE
    }

    private fun showResultDialog(type: ChessResult) {
        val message = when (type) {
            ChessResult.CROSS -> "叉叉获胜"
            ChessResult.CIRCLE -> "圆圈获胜"
            else -> "平局"
        }
        AlertDialog.Builder(this)
            .setTitle("结果")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, whitch ->
                reset()
                dialog.dismiss()
            }.show()
    }

    /**
     * 重置
     */
    private fun reset() {
        val parent = findViewById<ViewGroup>(R.id.layout_root)
        for(i in 0 until parent.childCount) {
            parent[i].apply {
                if(this is ImageButton) {
                    setImageResource(0)
                    tag = null
                }
            }
        }
        nextChessType = Chess.CROSS
    }

    private fun Group.setAllOnClickListener(listener: View.OnClickListener?) {
        referencedIds.forEach { id ->
            rootView.findViewById<View>(id).setOnClickListener(listener)
        }
    }

}