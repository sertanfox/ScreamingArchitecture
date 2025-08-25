package com.sertanfox.screamingarchitecture.pieces

import com.sertanfox.screamingarchitecture.presentation.fragments.PieceType

open class Piece(
    val isWhite: Boolean,
    val type: PieceType,
    var isEverMoved:Boolean = false
)

