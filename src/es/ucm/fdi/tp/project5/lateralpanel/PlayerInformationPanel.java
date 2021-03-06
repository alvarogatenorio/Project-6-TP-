package es.ucm.fdi.tp.project5.lateralpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.project5.controller.PlayersMap;
import es.ucm.fdi.tp.project5.utils.PieceColorMap;
/**
 * PlayerInformationPanel Class
 * 
 * This Class contains the JPanel component which shows the pieces, their Player Mode and their number of pieces on game,
 *  in case it's needed for example in Ataxx or in Advanced Tic-Tac-Toe. 
 * 
 *
 */
@SuppressWarnings("serial")
public class PlayerInformationPanel extends JPanel {
	private static final String PANEL_NAME_TEXT = "Player Information";
	private static final String UNKNOWN = "-";
	private static final String columNames[] = {"Player", "Mode", "#Pieces"};

	private JScrollPane scrollPane;
	private MyTableModel tableModel;
	private JTable table;
	private Board board;
	
	/**
	 * 
	 * Constructor of this component. In this constructor is created everything is needed for the function part of the table. 
	 * Such as all the renderer of the color background of each row, 
	 * the scrollpane in case it's needed, the border and the resize modes. 
	 * 
	 * @param pieces The list of pieces on game. 
	 * @param board The board where the game is played. 
	 * @param colorChooser A hash map which relate a piece with his color. 
	 * @param viewPiece The Piece or "Player" in which view we are. In case the multiviews option is not on, it is null.
	 * @param controller The controller of the game on play. 
	 */

	public PlayerInformationPanel(List<Piece> pieces, Board board,
			PieceColorMap colorChooser, Piece viewPiece,
			PlayersMap playersMap) {

		super(new BorderLayout());
		this.board = board;
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), PANEL_NAME_TEXT));
		
		tableModel = new MyTableModel(pieces, columNames, this.board, viewPiece,
				playersMap);
		table = new JTable(tableModel);
		for (int i = 0; i < columNames.length; i++) {
			table.getColumnModel().getColumn(i).setHeaderValue(columNames[i]);
		}
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row,
					int column) {
				JComponent c = (JComponent) super.getTableCellRendererComponent(
						table, value, isSelected, hasFocus, row, column);
				c.setBackground(colorChooser.getColorFor(pieces.get(row)));
				return c;
			}

		});
		scrollPane = new JScrollPane(table);
		this.setMaximumSize(new Dimension(250, ((pieces.size()+1)*16)+16));
		this.setMinimumSize(new Dimension(150, (pieces.size()*16)+16));
		this.add(scrollPane);
	}
	/**
	 * Method used for update the Table information when needed.
	 * For example when the Player Mode, has been modified or when a move has ocurred and the number of pieces has changed. 
	 *
	 */

	public void updateTableInfo(Board board) {
		this.board = board;
		this.tableModel.setBoard(board);
		this.repaint();
	}
	
	/**
	 * 
	 * Static Class made for the construction of the JTable which provided the Table Model used to create it. 
	 *
	 */

	static class MyTableModel extends AbstractTableModel {

		private String[] columnNames;
		private List<Piece> pieces;
		private Board board;
		private Piece viewPiece;
		private PlayersMap playersMap;
		
		/**
		 * Constructor of the Table Model static class.
		 * @param pieces
		 * @param columnNames
		 * @param board
		 * @param viewPiece
		 * @param controller
		 */

		public MyTableModel(List<Piece> pieces, String[] columnNames,
				Board board, Piece viewPiece, PlayersMap playersMap) {
			this.pieces = pieces;
			this.columnNames = columnNames;
			this.board = board;
			this.viewPiece = viewPiece;
			this.playersMap = playersMap;
		}

		@Override
		public int getRowCount() {
			return pieces.size();
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Class getColumnClass(int col) {
			return String.class;
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public Object getValueAt(int row, int col) {
			switch (col) {
			case 0:
				return pieces.get(row);
			case 1:
				return stringMode(row);
			default:
				return stringPieceCount(row);
			}
		}

		/**
		 * Identifies what the table needs to show a number or a -.
		 * @param row
		 * @return The string to show in the cell (row, 3) of the table.
		 */
		
		public String stringPieceCount(int row) {
			Integer pieceCount = board.getPieceCount(pieces.get(row));
			if (pieceCount != null) {
				return pieceCount.toString();
			} else {
				return UNKNOWN;
			}
		}
		
		/**
		 * Identifies what the table needs to show a word or a -.
		 * @param row
		 * @return The string to show in the cell (row, 2) of the table.
		 */

		public String stringMode(int row) {
			if (viewPiece == null || viewPiece.equals(pieces.get(row))) {
				if (this.playersMap.isPlayerOfType(pieces.get(row), playersMap
						.getPlayerModeString(PlayersMap.RANDOM))) {
					return playersMap
							.getPlayerModeString(PlayersMap.RANDOM);
				} else if (this.playersMap.isPlayerOfType(pieces.get(row),
						playersMap.getPlayerModeString(
								PlayersMap.INTELLIGENT))) {
					return playersMap
							.getPlayerModeString(PlayersMap.INTELLIGENT);
				} else {
					return playersMap
							.getPlayerModeString(PlayersMap.MANUAL);
				}
			} else {
				return UNKNOWN;
			}

		}

		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
		public void setBoard(Board board){
			this.board = board;
		}
	}
}
