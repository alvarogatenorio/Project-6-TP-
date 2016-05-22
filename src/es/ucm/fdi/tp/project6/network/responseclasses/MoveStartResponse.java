package es.ucm.fdi.tp.project6.network.responseclasses;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class MoveStartResponse implements Response{
	private static final long serialVersionUID = 1L;
	private Board board;
	private Piece turn;

	public MoveStartResponse(Board board, Piece turn) {
		this.board = board;
		this.turn = turn;
	}

	public void run(GameObserver observer) {
		observer.onMoveStart(board, turn);
	}
}
