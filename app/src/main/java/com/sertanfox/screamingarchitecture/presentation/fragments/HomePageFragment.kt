package com.sertanfox.screamingarchitecture.presentation.fragments

import android.graphics.Color
import android.graphics.Interpolator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.sertanfox.screamingarchitecture.R
import com.sertanfox.screamingarchitecture.common.Pawn
import com.sertanfox.screamingarchitecture.common.Position
import com.sertanfox.screamingarchitecture.common.Rock
import com.sertanfox.screamingarchitecture.databinding.FragmentHomePageBinding
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

    val board: Array<Array<Rock?>> = Array(8) { arrayOfNulls<Rock>(8) }
    var isWhiteTurn = true
    var lastRock:Rock? = null
    var lastPos:Position = Position(0,0)
    var movablePoses:List<Position> = emptyList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater,container,false)
        initDataBinding()
        setupChessBoard()
        setupRocksTable()
        return binding.root
    }

    private fun setupRocksTable() {
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

                square.tag = Pair(row, col)
                square.setOnClickListener { view ->
                    val (clickedRow, clickedCol) = view.tag as Pair<Int, Int>

                    var rock = board[clickedRow][clickedCol]
                    if(rock != null){
                        if(rock.isWhite == isWhiteTurn){
                            if(!rock.isSelected){
                                if(!(lastPos.row == clickedRow && lastPos.col == clickedCol)){
                                    hideMoves()
                                }

                                rock.showMoves()
                                lastRock = rock
                                movablePoses = rock.movablePositions
                            }
                            else if(!isMovable(clickedRow, clickedCol))
                                hideMoves()
                            else {
                                rock.move(clickedRow,clickedCol)
                                passMoveTurn()
                            }
                        }
                        //TODO: else if(o taşı yiyebiliyor muyum?)
                        else {
                            hideMoves()
                        }
                    } else {
                        if(isAnyRockSelected()){
                            if(isMovable(clickedRow,clickedCol)){
                                hideMoves()
                                if(lastRock != null){
                                    lastRock?.move(clickedRow,clickedCol)
                                    board[lastRock!!.pos.row][lastRock!!.pos.col] = lastRock
                                    board[clickedRow][clickedCol] = lastRock
                                    passMoveTurn()
                                    movablePoses = emptyList()
                                }
                            }
                            else
                                hideMoves()
                        }
                    }
                }


                if(row < 2){
                    //siyah
                    if(row == 0){
                        if(col == A)
                            square.setImageResource(R.drawable.black_rook)
                        else if(col == B)
                            square.setImageResource(R.drawable.black_knight)
                        else if(col == C)
                            square.setImageResource(R.drawable.black_bishop)
                        else if(col == D)
                            square.setImageResource(R.drawable.black_king)
                        else if(col == E)
                            square.setImageResource(R.drawable.black_queen)
                        else if(col == F)
                            square.setImageResource(R.drawable.black_bishop)
                        else if(col == G)
                            square.setImageResource(R.drawable.black_knight)
                        else if(col == H)
                            square.setImageResource(R.drawable.black_rook)
                    }
                    else if(row == 1) {
                        board[row][col] = Pawn(isWhite = false, Position(row,col), binding.rocksTable)
                        square.setImageResource(R.drawable.black_pawn)
                    }

                }
                else if(row > 5){
                    //beyaz
                    if(row == 7){
                       if(col == A)
                           square.setImageResource(R.drawable.white_rook)
                       else if(col == B)
                           square.setImageResource(R.drawable.white_knight)
                       else if(col == C)
                           square.setImageResource(R.drawable.white_bishop)
                       else if(col == D)
                           square.setImageResource(R.drawable.white_king)
                       else if(col == E)
                           square.setImageResource(R.drawable.white_queen)
                       else if(col == F)
                           square.setImageResource(R.drawable.white_bishop)
                       else if(col == G)
                           square.setImageResource(R.drawable.white_knight)
                       else if(col == H)
                           square.setImageResource(R.drawable.white_rook)

                    }
                    else if(row == 6) {
                        board[row][col] = Pawn(isWhite = true, Position(row,col), binding.rocksTable)
                        square.setImageResource(R.drawable.white_pawn)
                    }

                }

                square.layoutParams = params
                binding.rocksTable.addView(square)
            }
        }

    }

    private fun passMoveTurn(){
        isWhiteTurn != isWhiteTurn
    }

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

    private fun initDataBinding(){
        binding.viewModel = viewModel
    }

    private fun isMovable(row:Int, col:Int):Boolean{
        for(pos in movablePoses){
            if(pos.row == row && pos.col == col)
                return true
        }
        return false
    }

    private fun hideMoves(){
        for(pos in movablePoses){
            val view = binding.rocksTable.getChildAt(pos.row*8+pos.col) as? ImageView
            view?.setBackgroundResource(0)
        }

        getSelectedRock()?.isSelected = false
        movablePoses = emptyList()
    }

    private fun isAnyRockSelected():Boolean {
        for(rows in board){
            for(rock in rows){
                if(rock != null && rock.isSelected)
                    return true
            }
        }

        return false
    }

    private fun getSelectedRock():Rock?{
        for(rows in board){
            for(rock in rows){
                if(rock != null && rock.isSelected)
                    return rock
            }
        }

        return null
    }
}