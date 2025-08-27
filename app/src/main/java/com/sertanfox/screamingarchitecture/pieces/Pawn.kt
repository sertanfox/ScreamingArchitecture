package com.sertanfox.screamingarchitecture.pieces

import com.sertanfox.screamingarchitecture.presentation.fragments.MoveRotation
import com.sertanfox.screamingarchitecture.presentation.fragments.PieceType

class Pawn(isWhite: Boolean) : Piece(isWhite, PieceType.PAWN) {
    init {
        if(isWhite)
            rotations.add(MoveRotation.UP)
        else
            rotations.add(MoveRotation.DOWN)
    }
}
