package es.ucm.fdi.tp.project6.network.responseclasses;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.project6.network.Response;

public class GameOverResponse implements Response{
	private static final long serialVersionUID = 1L;
	private Board board;
	private State state;
	private Piece winner;

	public GameOverResponse(Board board, State state, Piece winner) {
		this.board = board;
		this.state = state;
		this.winner = winner;
	}

	public void run(GameObserver observer) {
		observer.onGameOver(board, state, winner);
	}

}
