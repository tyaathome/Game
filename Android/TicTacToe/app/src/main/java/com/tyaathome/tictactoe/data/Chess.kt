package com.tyaathome.tictactoe.data

/**
 * Author: tya
 * Date: 2023/05/24
 * Desc:
 */
data class Chess(var type: Int, var index: Int) {

    companion object {
        const val CROSS = 0
        const val CIRCLE = 1
        const val DRAW = 2
    }

}