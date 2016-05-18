package es.ucm.fdi.tp.project6.network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.control.commands.PlayCommand;
import es.ucm.fdi.tp.basecode.bgame.control.commands.QuitCommand;
import es.ucm.fdi.tp.basecode.bgame.control.commands.RestartCommand;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.project6.controller.SwingController;

public class GameClient extends SwingController implements
		Observable<GameObserver> {

	private String host;
	private int port;
	private List<GameObserver> observers;
	private Piece localPiece;
	private GameFactory gameFactory;
	private Connection connectionToServer;
	private boolean gameOver;
	private AIAlgorithm aiPlayerAlg;

	public GameClient(String host, int port, AIAlgorithm aiPlayerAlg) throws Exception {
		super(null, null, null, null);//El random y el AI player no hay que pasarlos a null, pensar como pasarlos. 
		this.aiPlayerAlg=aiPlayerAlg;
		this.host = host;
		this.port = port;
		this.observers = new ArrayList<GameObserver>();
		connect();

	}

	private void connect() throws Exception {
		connectionToServer = new Connection(new Socket(host, port));
		connectionToServer.sendObject("Connect");
		Object response = connectionToServer.getObject();
		if (response instanceof Exception) {
			throw (Exception) response;
		}
		try {
			gameFactory = (GameFactory) connectionToServer.getObject();
			localPiece = (Piece) connectionToServer.getObject();
			this.setRandomPlayer(gameFactory.createRandomPlayer());
			this.setAiPlayer(gameFactory.createAIPlayer(aiPlayerAlg));
		} catch (Exception e) {
			throw new GameError("Unknown server response: " + e.getMessage());
		}
	}

	public GameFactory getGameFactory() {
		return gameFactory;
	}

	public Piece getPlayerPiece() {
		return localPiece;
	}

	public void addObserver(GameObserver o) {
		this.observers.add(o);
	}

	public void removeObserver(GameObserver o) {
		this.observers.remove(o);
	}

	public void makeMove(Player p) {
		forwardCommand(new PlayCommand(p));
	}

	public void stop() {
		forwardCommand(new QuitCommand());
	}

	public void restart() {
		forwardCommand(new RestartCommand());
	}

	private void forwardCommand(Command cmd) {
		if (gameOver == false) {
			try {
				connectionToServer.sendObject(cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void start() {

		this.observers.add(getAnonimusGameObserver());
		gameOver = false;
		while (!gameOver) {
			try {
				Response res = (Response) connectionToServer.getObject();
				for (GameObserver o : observers) {
					res.run(o);
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
				gameOver=true;
			}
		}
	}

	private GameObserver getAnonimusGameObserver() {
		return new GameObserver() {
			public void onGameStart(Board board, String gameDesc,
					List<Piece> pieces, Piece turn) {
			}

			public void onGameOver(Board board, State state, Piece winner) {
				GameClient.this.gameOver = true;
				try {
					GameClient.this.connectionToServer.stop();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			public void onMoveStart(Board board, Piece turn) {
			}

			public void onMoveEnd(Board board, Piece turn, boolean success) {
			}

			public void onChangeTurn(Board board, Piece turn) {
			}

			public void onError(String msg) {
			}
		};
	}
}
