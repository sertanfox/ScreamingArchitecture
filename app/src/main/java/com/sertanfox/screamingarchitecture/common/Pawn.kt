package com.sertanfox.screamingarchitecture.common

import android.widget.GridLayout
import android.widget.ImageView
import com.sertanfox.screamingarchitecture.R

class Pawn(isWhite: Boolean, position: Position, board:GridLayout) : Rock(isWhite,position, board) {
    override fun move(newRow: Int, newCol: Int) {

        val newPosView = board.getChildAt(newRow*8+newCol) as? ImageView
        if(isWhite){
            newPosView?.setBackgroundResource(R.drawable.white_pawn)
        } else {
            newPosView?.setBackgroundResource(R.drawable.black_pawn)
        }

        val oldPosView = board.getChildAt(pos.row*8+pos.col) as? ImageView
        oldPosView?.setBackgroundResource(0)

        pos = Position(newRow,newCol)
        isMoved = true
    }

    override fun showMoves() {
        isSelected = true

        if(isMoved){
            if(isWhite)
                listOf(Position(pos.row,pos.col-1))
            else
                listOf(Position(pos.row,pos.col+1))
        } else{
            if(isWhite){
                movablePositions = listOf(Position(pos.row-1,pos.col),Position(pos.row-2,pos.col))
                for(pos in movablePositions){
                    val view = board.getChildAt(pos.row*8+pos.col) as? ImageView
                    view?.setBackgroundResource(R.drawable.white_dot)
                }
            }
            else{
                movablePositions = listOf(Position(pos.row+1,pos.col),Position(pos.row+2,pos.col))

                for(pos in movablePositions){
                    val view = board.getChildAt(pos.row*8+pos.col) as? ImageView
                    view?.setBackgroundResource(R.drawable.black_dot)
                }

            }
        }
    }

    override fun hideMoves() {
        isSelected = false
        for(pos in movablePositions){
            val view = board.getChildAt(pos.row*8+pos.col) as? ImageView
            view?.setBackgroundResource(0)
        }
    }

    override fun isMovable(row:Int, col:Int):Boolean{
        for(pos in movablePositions){
            if(pos.row == row && pos.col == col)
                return true
        }
        return false
    }
}
