package com.sertanfox.screamingarchitecture.presentation.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.sertanfox.screamingarchitecture.R
import com.sertanfox.screamingarchitecture.pieces.Pawn
import com.sertanfox.screamingarchitecture.pieces.Position
import com.sertanfox.screamingarchitecture.pieces.Piece
import com.sertanfox.screamingarchitecture.databinding.FragmentHomePageBinding
import com.sertanfox.screamingarchitecture.pieces.Bishop
import com.sertanfox.screamingarchitecture.pieces.King
import com.sertanfox.screamingarchitecture.pieces.Knight
import com.sertanfox.screamingarchitecture.pieces.Queen
import com.sertanfox.screamingarchitecture.pieces.Rook
import com.sertanfox.screamingarchitecture.presentation.viewmodels.HomePageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageFragment : Fragment() {
    private lateinit var binding : FragmentHomePageBinding
    private val viewModel : HomePageViewModel by viewModels()
    //region Table Letters
    private val A = 0
    private val B = 1
    private val C = 2
    private val D = 3
    private val E = 4
    private val F = 5
    private val G = 6
    private val H = 7
    //endregion

    val board: Array<Array<Piece?>> = Array(8) { arrayOfNulls<Piece>(8) }
    var isWhiteTurn = true
    var selectedPiece: Piece? = null
    var selectedPiecePos: Position? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater,container,false)
        initDataBinding()
        setupChessBoard()
        setupPiecesTable()
        setupMovablesLayer()
        startTheGame()
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun startTheGame() {
        binding.piecesTable.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val boardSize = binding.piecesTable.width
                val tileSize = boardSize / 8

                val clickedCol = (event.x / tileSize).toInt()
                val clickedRow = (event.y / tileSize).toInt()
                handleClick(clickedRow, clickedCol)
            }
            true
        }
    }

    private fun handleClick(row: Int, col: Int) {
        val piece = board[row][col]

        if(selectedPiece == null){
            if(piece != null && isYourTurn(piece.isWhite)){
                selectedPiece = piece
                selectedPiecePos = Position(row,col)
                showMovableAreas(piece)
            }
        }
        else {
            if(row == selectedPiecePos!!.row && col == selectedPiecePos!!.col){
                //clearMovableAreas()
            }
        }

    }

    private fun clearMovableAreas() {
        for (row in 0 until 8) {
            for (col in 0 until 8) {
                val view = binding.movablesLayer.getChildAt(row*8+col) as? ImageView
                view?.setBackgroundResource(0)
            }
        }
    }

    private fun showMovableAreas(piece: Piece) {
        when(piece.type) {
            PieceType.PAWN -> {
                if(piece.isWhite){
                    val positionList: ArrayList<Position> = arrayListOf()
                    positionList.add(Position(selectedPiecePos!!.row-1,selectedPiecePos!!.col))
                    if(!piece.isEverMoved){
                        positionList.add(Position(selectedPiecePos!!.row-2,selectedPiecePos!!.col))
                    }
                    pointSquaresAsMovable(positionList)
                } else {

                }
            }
        }
    }

    private fun pointSquaresAsMovable(positions: ArrayList<Position>){
        for(position in positions){
            val view = binding.movablesLayer.getChildAt(position.row*8+position.col)
            view?.setBackgroundResource(R.drawable.white_dot)
        }
    }

    private fun passTurn(){
        isWhiteTurn != isWhiteTurn
    }

    private fun isYourTurn(isWhite:Boolean):Boolean{
        return isWhite == isWhiteTurn
    }

    //region Core Setup Functions
    private fun setupPiecesTable() {
        val boardSize = resources.displayMetrics.widthPixels
        val tileSize = boardSize / 8

        for (row in 0 until 8) {
            for (col in 0 until 8) {
                val square = ImageView(requireActivity())
                val params = GridLayout.LayoutParams().apply {
                    width = tileSize
                    height = tileSize
                    rowSpec = GridLayout.spec(row)
                    columnSpec = GridLayout.spec(col)
                }

                if(row < 2){
                    //siyah
                    if(row == 0){
                        if(col == A) {
                            board[row][col] = Rook(isWhite = false)
                            square.setImageResource(R.drawable.black_rook)
                        }
                        else if(col == B) {
                            board[row][col] = Knight(isWhite = false)
                            square.setImageResource(R.drawable.black_knight)
                        }
                        else if(col == C) {
                            board[row][col] = Bishop(isWhite = false)
                            square.setImageResource(R.drawable.black_bishop)
                        }
                        else if(col == D) {
                            board[row][col] = King(isWhite = false)
                            square.setImageResource(R.drawable.black_king)
                        }
                        else if(col == E) {
                            board[row][col] = Queen(isWhite = false)
                            square.setImageResource(R.drawable.black_queen)
                        }
                        else if(col == F) {
                            board[row][col] = Bishop(isWhite = false)
                            square.setImageResource(R.drawable.black_bishop)
                        }
                        else if(col == G) {
                            board[row][col] = Knight(isWhite = false)
                            square.setImageResource(R.drawable.black_knight)
                        }
                        else if(col == H){
                            board[row][col] = Rook(isWhite = false)
                            square.setImageResource(R.drawable.black_rook)
                        }
                    }
                    else if(row == 1) {
                        board[row][col] = Pawn(isWhite = false)
                        square.setImageResource(R.drawable.black_pawn)
                    }

                }
                else if(row > 5){
                    //beyaz
                    if(row == 7){
                       if(col == A) {
                           board[row][col] = Rook(isWhite = true)
                           square.setImageResource(R.drawable.white_rook)
                       }
                       else if(col == B) {
                           board[row][col] = Knight(isWhite = true)
                           square.setImageResource(R.drawable.white_knight)
                       }
                       else if(col == C) {
                           board[row][col] = Bishop(isWhite = true)
                           square.setImageResource(R.drawable.white_bishop)
                       }
                       else if(col == D) {
                           board[row][col] = King(isWhite = true)
                           square.setImageResource(R.drawable.white_king)
                       }
                       else if(col == E) {
                           board[row][col] = Queen(isWhite = true)
                           square.setImageResource(R.drawable.white_queen)
                       }
                       else if(col == F) {
                           board[row][col] = Bishop(isWhite = true)
                           square.setImageResource(R.drawable.white_bishop)
                       }
                       else if(col == G) {
                           board[row][col] = King(isWhite = true)
                           square.setImageResource(R.drawable.white_knight)
                       }
                       else if(col == H) {
                           board[row][col] = Rook(isWhite = true)
                           square.setImageResource(R.drawable.white_rook)
                       }

                    }
                    else if(row == 6) {
                        board[row][col] = Pawn(isWhite = true)
                        square.setImageResource(R.drawable.white_pawn)
                    }

                }

                square.layoutParams = params
                binding.piecesTable.addView(square)
            }
        }

    }

    @SuppressLint("UseKtx")
    private fun setupChessBoard(){
        val boardSize = resources.displayMetrics.widthPixels
        val tileSize = boardSize / 8

        for (row in 0 until 8) {
            for (col in 0 until 8) {
                val square = View(requireActivity())
                val params = GridLayout.LayoutParams().apply {
                    width = tileSize
                    height = tileSize
                    rowSpec = GridLayout.spec(row)
                    columnSpec = GridLayout.spec(col)
                }

                val isDarkSquare = (row + col) % 2 == 1
                square.setBackgroundColor(
                    if (isDarkSquare) Color.parseColor("#CC8B5A2B")
                    else Color.parseColor("#CCEEDDC3")
                )

                square.layoutParams = params
                binding.chessBoard.addView(square)
            }
        }
    }

    private fun setupMovablesLayer(){
        val boardSize = resources.displayMetrics.widthPixels
        val tileSize = boardSize / 8

        for (row in 0 until 8) {
            for (col in 0 until 8) {
                val square = View(requireActivity())
                val params = GridLayout.LayoutParams().apply {
                    width = tileSize
                    height = tileSize
                    rowSpec = GridLayout.spec(row)
                    columnSpec = GridLayout.spec(col)
                }

                square.setBackgroundColor(
                    Color.parseColor("#00FFFFFF")
                )

                square.layoutParams = params
                binding.movablesLayer.addView(square)
            }
        }
    }


    private fun initDataBinding(){
        binding.viewModel = viewModel
    }
    //endregion
}

class PieceType(val id: Int) {
    companion object Companion {
        val PAWN = PieceType(0)
        val ROOK = PieceType(1)
        val KNIGHT = PieceType(2)
        val BISHOP = PieceType(3)
        val QUEEN = PieceType(4)
        val KING = PieceType(5)
    }
}
