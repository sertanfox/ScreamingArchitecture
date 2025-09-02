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
import androidx.core.graphics.toColorInt

//region Table Letters
private const val A = 0
private const val B = 1
private const val C = 2
private const val D = 3
private const val E = 4
private const val F = 5
private const val G = 6
private const val H = 7
//endregion

@AndroidEntryPoint
class HomePageFragment : Fragment() {
    private lateinit var binding : FragmentHomePageBinding
    private val viewModel : HomePageViewModel by viewModels()

    private var tempPosList: ArrayList<Position> = arrayListOf()
    private var movableAreas: ArrayList<Position> = arrayListOf()
    private val board: Array<Array<Piece?>> = Array(8) { arrayOfNulls(8) }
    private var isWhiteTurn = true
    private var selectedPiece: Piece? = null
    private var selectedPiecePos: Position? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater,container,false)
        initDataBinding()
        setupChessBoard()
        setupPiecesTable()
        setupMovablesLayer()
        setupPieceSelection()
        startTheGame()
        return binding.root
    }

    private fun setupPieceSelection() {
        binding.selectRook.setOnClickListener {
            hidePieceSelection()
            clearPieceSelection()
            passTurn()
        }
        binding.selectKnight.setOnClickListener {
            hidePieceSelection()
            clearPieceSelection()
            passTurn()
        }
        binding.selectBishop.setOnClickListener {
            hidePieceSelection()
            clearPieceSelection()
            passTurn()
        }
        binding.selectQueen.setOnClickListener {
            setQueenOnTable()
            hidePieceSelection()
            clearPieceSelection()
            passTurn()
        }
    }

    private fun setQueenOnTable() {
        if(selectedPiece!!.isWhite) {
            board[selectedPiecePos!!.row][selectedPiecePos!!.col] = Queen(isWhite = true)
            val index = selectedPiecePos!!.row*8+selectedPiecePos!!.col
            val view = binding.piecesTable.getChildAt(index)
            view?.setBackgroundResource(R.drawable.white_queen)
        }
        else {
            board[selectedPiecePos!!.row][selectedPiecePos!!.col] = Queen(isWhite = false)
            val index = selectedPiecePos!!.row*8+selectedPiecePos!!.col
            val view = binding.piecesTable.getChildAt(index)
            view?.setBackgroundResource(R.drawable.black_queen)
        }
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
        val selectedPos = Position(row,col)

        if(!isMovablesExists() && selectedPiece == null){ // A piece is not selected yet
            if(isPlayablePiece(piece)){
                selectNewPiece(piece!!,Position(row,col))
            }
        }
        else { // A piece already selected
            if(isMovable(selectedPos)){
                movePiece(selectedPos)
            }
            else {
                clearMovableAreas()
                if(isPlayablePiece(piece) && !isSamePiece(selectedPos)){
                    selectNewPiece(piece!!, selectedPos)
                }
                else {
                    clearSelectedPiece()
                }
            }
        }

    }

    private fun isSamePiece(position: Position):Boolean {
        return selectedPiecePos == position
    }

    private fun clearSelectedPiece(){
        selectedPiece = null
        selectedPiecePos = null
    }

    private fun movePiece(newPosition: Position){
        clearMovableAreas()
        clearPieceOldSquare()
        setPieceNewSquare(newPosition)
        if(isThereAnyPawnWins(newPosition)){
            showPieceSelection()
        } else {
            clearPieceSelection()
            passTurn()
        }
    }

    private fun isThereAnyPawnWins(position: Position): Boolean {
        val playedPiece = board[position.row][position.col]

        if(playedPiece!!.isWhite && position.row == 0)
            return true
        else if(!playedPiece.isWhite && position.row == 7){
            return true
        }

        return false
    }

    private fun clearPieceSelection() {
        selectedPiece = null
        selectedPiecePos = null
    }

    private fun clearPieceOldSquare() {
        val index = selectedPiecePos!!.row*8+selectedPiecePos!!.col
        val view = binding.piecesTable.getChildAt(index)
        view!!.setBackgroundResource(0)
        board[selectedPiecePos!!.row][selectedPiecePos!!.col] = null
    }

    private fun setPieceNewSquare(newPos:Position) {
        val index = newPos.row*8+newPos.col
        val view = binding.piecesTable.getChildAt(index)
        view?.setBackgroundResource(pieceTypeImage())
        selectedPiece?.isEverMoved = true
        board[newPos.row][newPos.col] = selectedPiece
        selectedPiece = board[newPos.row][newPos.col]
        selectedPiecePos = Position(newPos.row,newPos.col)
    }

    private fun pieceTypeImage(): Int {
        return if(selectedPiece!!.isWhite){
            when(selectedPiece!!.type){
                PieceType.PAWN -> {
                    R.drawable.white_pawn
                }
                PieceType.ROOK -> {
                    R.drawable.white_rook
                }
                PieceType.KNIGHT -> {
                    R.drawable.white_knight
                }
                PieceType.BISHOP -> {
                    R.drawable.white_bishop
                }
                PieceType.QUEEN -> {
                    R.drawable.white_queen
                }
                PieceType.KING -> {
                    R.drawable.white_king
                }
                else -> {
                    0
                }
            }
        }
        else {
            when(selectedPiece!!.type){
                PieceType.PAWN -> {
                    R.drawable.black_pawn
                }
                PieceType.ROOK -> {
                    R.drawable.black_rook
                }
                PieceType.KNIGHT -> {
                    R.drawable.black_knight
                }
                PieceType.BISHOP -> {
                    R.drawable.black_bishop
                }
                PieceType.QUEEN -> {
                    R.drawable.black_queen
                }
                PieceType.KING -> {
                    R.drawable.black_king
                }
                else -> {
                    0
                }
            }
        }
    }

    private fun selectNewPiece(piece: Piece, position: Position) {
        selectedPiece = piece
        selectedPiecePos = position
        showMovableAreas(piece)
    }

    private fun isPlayablePiece(piece: Piece?): Boolean {
        return piece != null && isYourPiece(piece.isWhite)
    }


    private fun isMovable(position: Position): Boolean {
        return movableAreas.contains(position)
    }

    private fun isMovablesExists():Boolean{
        return movableAreas.size > 0
    }

    private fun clearMovableAreas() {
        for (row in 0 until 8) {
            for (col in 0 until 8) {
                val view = binding.movablesLayer.getChildAt(row*8+col)
                view?.setBackgroundResource(0)
            }
        }

        movableAreas.clear()
    }

    private fun showMovableAreas(piece: Piece) {
        when(piece.type) {
            PieceType.PAWN -> {
                if(isPawnAllowedToMove()){
                    if(piece.isWhite){

                        val leftUpPiece = if(selectedPiecePos!!.col != 0)
                            board[selectedPiecePos!!.row-1][selectedPiecePos!!.col-1]
                        else
                            null

                        val rightUpPiece = if(selectedPiecePos!!.col != 7)
                            board[selectedPiecePos!!.row-1][selectedPiecePos!!.col+1]
                        else
                            null

                        tempPosList.add(Position(selectedPiecePos!!.row-1,selectedPiecePos!!.col))
                        if(!piece.isEverMoved && board[selectedPiecePos!!.row-2][selectedPiecePos!!.col] == null){
                            tempPosList.add(Position(selectedPiecePos!!.row-2,selectedPiecePos!!.col))
                        }

                        if(leftUpPiece != null && !isYourPiece(leftUpPiece.isWhite))
                            tempPosList.add(Position(selectedPiecePos!!.row-1, selectedPiecePos!!.col-1))

                        if(rightUpPiece != null && !isYourPiece(rightUpPiece.isWhite))
                            tempPosList.add(Position(selectedPiecePos!!.row-1, selectedPiecePos!!.col+1))

                        pointSquaresAsMovable(ArrayList(tempPosList))
                    } else {

                        val leftDownPiece = if(selectedPiecePos!!.col != 0)
                            board[selectedPiecePos!!.row+1][selectedPiecePos!!.col-1]
                        else
                            null

                        val rightDownPiece = if(selectedPiecePos!!.col != 7)
                            board[selectedPiecePos!!.row+1][selectedPiecePos!!.col+1]
                        else
                            null

                        tempPosList.add(Position(selectedPiecePos!!.row+1,selectedPiecePos!!.col))
                        if(!piece.isEverMoved && board[selectedPiecePos!!.row+2][selectedPiecePos!!.col] == null){
                            tempPosList.add(Position(selectedPiecePos!!.row+2,selectedPiecePos!!.col))
                        }

                        if(leftDownPiece != null && !isYourPiece(leftDownPiece.isWhite))
                            tempPosList.add(Position(selectedPiecePos!!.row+1, selectedPiecePos!!.col-1))

                        if(rightDownPiece != null && !isYourPiece(rightDownPiece.isWhite))
                            tempPosList.add(Position(selectedPiecePos!!.row+1, selectedPiecePos!!.col+1))

                        pointSquaresAsMovable(ArrayList(tempPosList))
                    }
                }
            }
        }
    }

    private fun showPieceSelection(){
        if(selectedPiece!!.isWhite){
            binding.selectRook.setBackgroundResource(R.drawable.white_rook)
            binding.selectKnight.setBackgroundResource(R.drawable.white_knight)
            binding.selectBishop.setBackgroundResource(R.drawable.white_bishop)
            binding.selectQueen.setBackgroundResource(R.drawable.white_queen)
        } else {
            binding.selectRook.setBackgroundResource(R.drawable.black_rook)
            binding.selectKnight.setBackgroundResource(R.drawable.black_knight)
            binding.selectBishop.setBackgroundResource(R.drawable.black_bishop)
            binding.selectQueen.setBackgroundResource(R.drawable.black_queen)
        }
        binding.pieceSelectionLayout.visibility = View.VISIBLE
        binding.selectPiece.visibility = View.VISIBLE
    }

    private fun hidePieceSelection() {
        binding.pieceSelectionLayout.visibility = View.GONE
        binding.selectPiece.visibility = View.GONE
    }

    private fun isPawnAllowedToMove():Boolean {
        if(selectedPiece!!.isWhite) {
            if(selectedPiecePos!!.row != 0){
                if(board[selectedPiecePos!!.row-1][selectedPiecePos!!.col] == null)
                    return true

                if(isThereAnyTakablePiece()){
                    return true
                }
            }
            else {
                return false
            }
        }
        else {
            if(selectedPiecePos!!.row != 7){

                if(board[selectedPiecePos!!.row+1][selectedPiecePos!!.col] == null)
                    return true

                if(isThereAnyTakablePiece()){
                    return true
                }

            }
            else {
                return false
            }
        }

        return false
    }

    private fun isThereAnyTakablePiece(): Boolean {
        if(selectedPiece!!.isWhite) {

            val leftUpPiece = if(selectedPiecePos!!.col != 0)
                board[selectedPiecePos!!.row-1][selectedPiecePos!!.col-1]
            else
                null

            val rightUpPiece = if(selectedPiecePos!!.col != 7)
                board[selectedPiecePos!!.row-1][selectedPiecePos!!.col+1]
            else
                null

            if(leftUpPiece != null && leftUpPiece.isWhite != isWhiteTurn)
                return true

            if(rightUpPiece != null && rightUpPiece.isWhite != isWhiteTurn)
                return true

        }
        else {
            val leftDownPiece = if(selectedPiecePos!!.col != 0)
                board[selectedPiecePos!!.row+1][selectedPiecePos!!.col-1]
            else
                null

            val rightDownPiece = if(selectedPiecePos!!.col != 7)
                board[selectedPiecePos!!.row+1][selectedPiecePos!!.col+1]
            else
                null

            if(leftDownPiece != null && leftDownPiece.isWhite != isWhiteTurn)
                return true

            if(rightDownPiece != null && rightDownPiece.isWhite != isWhiteTurn)
                return true
        }

        return false
    }

    private fun pointSquaresAsMovable(positions: ArrayList<Position>){
        clearMovableAreas()
        setMovableAreas(positions)
        for(position in positions){
            val view = binding.movablesLayer.getChildAt(position.row*8+position.col)
            if(isWhiteTurn)
                view?.setBackgroundResource(R.drawable.white_dot)
            else
                view?.setBackgroundResource(R.drawable.black_dot)
        }
        clearTempPosList()
    }

    private fun clearTempPosList(){
        tempPosList.clear()
    }

    private fun setMovableAreas(positionList: ArrayList<Position>){
        movableAreas = ArrayList(positionList)
    }

    private fun passTurn(){
        isWhiteTurn = !isWhiteTurn
    }

    private fun isYourPiece(isWhite:Boolean):Boolean{
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
                    //black
                    if(row == 0){
                        when (col) {
                            A -> {
                                board[row][col] = Rook(isWhite = false)
                                square.setBackgroundResource(R.drawable.black_rook)
                            }
                            B -> {
                                board[row][col] = Knight(isWhite = false)
                                square.setBackgroundResource(R.drawable.black_knight)
                            }
                            C -> {
                                board[row][col] = Bishop(isWhite = false)
                                square.setBackgroundResource(R.drawable.black_bishop)
                            }
                            D -> {
                                board[row][col] = King(isWhite = false)
                                square.setBackgroundResource(R.drawable.black_king)
                            }
                            E -> {
                                board[row][col] = Queen(isWhite = false)
                                square.setBackgroundResource(R.drawable.black_queen)
                            }
                            F -> {
                                board[row][col] = Bishop(isWhite = false)
                                square.setBackgroundResource(R.drawable.black_bishop)
                            }
                            G -> {
                                board[row][col] = Knight(isWhite = false)
                                square.setBackgroundResource(R.drawable.black_knight)
                            }
                            H -> {
                                board[row][col] = Rook(isWhite = false)
                                square.setBackgroundResource(R.drawable.black_rook)
                            }
                        }
                    }
                    else /*if(row == 1)*/ {
                        board[row][col] = Pawn(isWhite = false)
                        square.setBackgroundResource(R.drawable.black_pawn)
                    }

                }
                else if(row > 5){
                    //white
                    if(row == 7){
                        when (col) {
                            A -> {
                                board[row][col] = Rook(isWhite = true)
                                square.setBackgroundResource(R.drawable.white_rook)
                            }
                            B -> {
                                board[row][col] = Knight(isWhite = true)
                                square.setBackgroundResource(R.drawable.white_knight)
                            }
                            C -> {
                                board[row][col] = Bishop(isWhite = true)
                                square.setBackgroundResource(R.drawable.white_bishop)
                            }
                            D -> {
                                board[row][col] = King(isWhite = true)
                                square.setBackgroundResource(R.drawable.white_king)
                            }
                            E -> {
                                board[row][col] = Queen(isWhite = true)
                                square.setBackgroundResource(R.drawable.white_queen)
                            }
                            F -> {
                                board[row][col] = Bishop(isWhite = true)
                                square.setBackgroundResource(R.drawable.white_bishop)
                            }
                            G -> {
                                board[row][col] = King(isWhite = true)
                                square.setBackgroundResource(R.drawable.white_knight)
                            }
                            H -> {
                                board[row][col] = Rook(isWhite = true)
                                square.setBackgroundResource(R.drawable.white_rook)
                            }
                        }

                    }
                    else /*if(row == 6)*/ {
                        board[row][col] = Pawn(isWhite = true)
                        square.setBackgroundResource(R.drawable.white_pawn)
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
                    "#00FFFFFF".toColorInt()
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

class PieceType {
    companion object Companion {
        val PAWN = PieceType()
        val ROOK = PieceType()
        val KNIGHT = PieceType()
        val BISHOP = PieceType()
        val QUEEN = PieceType()
        val KING = PieceType()
    }
}

class MoveRotation {
    companion object Companion {
        val LEFT = MoveRotation()
        val RIGHT = MoveRotation()
        val UP = MoveRotation()
        val DOWN = MoveRotation()
        val LEFT_UP = MoveRotation()
        val RIGHT_UP = MoveRotation()
        val LEFT_DOWN = MoveRotation()
        val RIGHT_DOWN = MoveRotation()
    }
}
