package es.ucm.fdi.tp.project5.lateralpanel;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.project5.controller.PlayersMap;
import es.ucm.fdi.tp.project5.lateralpanel.AutomaticMovesPanel.IntelligentButtonListener;
import es.ucm.fdi.tp.project5.lateralpanel.AutomaticMovesPanel.RandomButtonListener;
import es.ucm.fdi.tp.project5.lateralpanel.PieceColorsPanel.ColorChangeListener;
import es.ucm.fdi.tp.project5.lateralpanel.PlayerModesPanel.PlayerModesChangeListener;
import es.ucm.fdi.tp.project5.lateralpanel.QuitRestartPanel.QuitButtonListener;
import es.ucm.fdi.tp.project5.lateralpanel.QuitRestartPanel.RestartButtonListener;
import es.ucm.fdi.tp.project5.utils.PieceColorMap;

/**
 * Panel that saves all the lateral panel option information
 */
@SuppressWarnings("serial")
public class LateralPanel extends JPanel {

	private PlayerModesPanel playerModesPanel;
	private PieceColorsPanel pieceColorsPanel;
	private AutomaticMovesPanel automaticMovesPanel;
	private QuitRestartPanel quitRestartPanel;
	private StatusMessagePanel statusMessagePanel;
	private PlayerInformationPanel playerInformationPanel;
	private Piece piecesArray[];

	public LateralPanel(List<Piece> pieces, PieceColorMap colorChooser,
			Board board, Piece viewPiece, PlayersMap playersMap, Piece turn,
			QuitButtonListener quitButtonListener,
			RestartButtonListener restartButtonListener,
			RandomButtonListener randomButtonListener,
			IntelligentButtonListener intelligentButtonListener,
			ColorChangeListener colorChangeListener,
			PlayerModesChangeListener playerModesChangeListener) {
		super(new GridLayout(0, 1));

		this.piecesArray = this.piecesListToArrayOfPieces(pieces);

		statusMessagePanel = new StatusMessagePanel();
		playerInformationPanel = new PlayerInformationPanel(pieces, board,
				colorChooser, viewPiece, playersMap);
		this.buildPieceColorPanel(pieces, colorChangeListener);
		this.buildQuitRestartPanel(viewPiece, quitButtonListener,
				restartButtonListener);

		this.add(statusMessagePanel);
		this.add(playerInformationPanel);
		this.add(pieceColorsPanel);
		this.buildAndAddPlayerModesPanel(piecesArray, viewPiece, playersMap,
				playerModesChangeListener);
		this.buildAndAddAutomaticMovesPanel(playersMap, randomButtonListener,
				intelligentButtonListener);
		this.add(quitRestartPanel);

	}

	public void updateTable(List<Piece> pieces, Board board, Piece viewPiece,
			PlayersMap playersMap, PieceColorMap colorChooser) {
		this.playerInformationPanel.updateTableInfo(board);
	}

	public void appendToStatusMessagePanel(String message) {
		this.statusMessagePanel.append(message);
	}

	public void disableAutomaticMoves(boolean disable) {
		if (automaticMovesPanel != null) {
			this.automaticMovesPanel.disablePanel(disable);
		}
	}

	private boolean buildPlayerModesPanel(Piece pieces[], Piece viewPiece,
			PlayersMap playersMap,
			PlayerModesChangeListener playerModesChangeListener) {
		if (playersMap.getAvailablePlayerModes() == 1) {
			return false;
		} else {
			playerModesPanel = new PlayerModesPanel(pieces,
					playerModesChangeListener, viewPiece,
					playersMap.getPlayerModesStringArray());
			return true;
		}
	}

	private Piece[] piecesListToArrayOfPieces(List<Piece> pieces) {
		Piece piecesArray[] = new Piece[pieces.size()];
		for (int i = 0; i < pieces.size(); i++) {
			piecesArray[i] = pieces.get(i);
		}
		return piecesArray;
	}

	private void buildAndAddPlayerModesPanel(Piece pieces[], Piece viewPiece,
			PlayersMap playersMap,
			PlayerModesChangeListener playerModesChangeListener) {
		if (this.buildPlayerModesPanel(pieces, viewPiece, playersMap,
				playerModesChangeListener))
			this.add(playerModesPanel);
	}

	private void buildPieceColorPanel(List<Piece> pieces,
			ColorChangeListener listener) {
		Piece piecesArray[] = this.piecesListToArrayOfPieces(pieces);
		pieceColorsPanel = new PieceColorsPanel(piecesArray, listener);
	}

	private boolean buildAutomaticMovesPanel(PlayersMap playersMap,
			RandomButtonListener randomButtonListener,
			IntelligentButtonListener intelligentButtonListener) {
		if (playersMap.getAvailablePlayerModes() == 1) {
			return false;
		} else {
			automaticMovesPanel = new AutomaticMovesPanel(randomButtonListener,
					intelligentButtonListener,
					playersMap.getPlayerModesStringArray());
			return true;
		}
	}

	private void buildAndAddAutomaticMovesPanel(PlayersMap playersMap,
			RandomButtonListener randomButtonListener,
			IntelligentButtonListener intelligentButtonListener) {
		if (this.buildAutomaticMovesPanel(playersMap, randomButtonListener,
				intelligentButtonListener))
			this.add(automaticMovesPanel);
	}

	private void buildQuitRestartPanel(Piece viewPiece,
			QuitButtonListener quitButtonListener,
			RestartButtonListener restartButtonListener) {
		quitRestartPanel = new QuitRestartPanel(quitButtonListener,
				restartButtonListener, viewPiece);
	}

	public void disableQuitButton(boolean disable) {
		this.quitRestartPanel.disableQuitButton(disable);
	}

	public void disableRestartButton(boolean disable) {
		this.quitRestartPanel.disableRestartButton(disable);
	}
}
