package com.sertanfox.screamingarchitecture.common

import android.widget.GridLayout

open class Rock(
    val isWhite: Boolean,
    var pos: Position,
    val board:GridLayout,
    ) {
    var isSelected: Boolean = false
    var isMoved: Boolean = false
    var movablePositions = emptyList<Position>()

    open fun move(newRow: Int, newCol: Int) {}

    open fun showMoves() {}

    open fun hideMoves() {}

    open fun isMovable(row:Int, col:Int):Boolean { return false }

}
