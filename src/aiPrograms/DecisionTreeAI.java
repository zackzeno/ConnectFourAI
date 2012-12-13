package aiPrograms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Random;

import enums.MoveResult;

import utils.AIUtils;
import utils.BoardSize;
import utils.MoveRecord;
import gameComponents.AIProgram;
import gameComponents.Board;
import gameComponents.MoveInfo;

public class DecisionTreeAI implements AIProgram {
	
	private BoardSize size;
	private boolean initiated;
	private Long[][] hashValues;
	private Random r;
	private HashMap<Long, MoveRecord> map;
	private int previousBoardValue;
	private long previousBoardHash;
	private int previousMoveCol;
	private static final Object ioLock = new Object();
	protected String saveFileName;
	
	public DecisionTreeAI(String loadFile, String saveFile, boolean logging) {
		initiated = false;
		r = new Random();
		if(loadFile != null) {
			loadFromFile(loadFile);
		}
		saveFileName = saveFile;
	}
	
	@Override
	public MoveInfo makeMove(int playerNum, Board board) {
		int currentBoardValue = AIUtils.minimax(playerNum, board, null, 1, 3, true, 1, 1);
		if(!initiated) {
			size = board.getSize();
			if(map == null) {
				map = new HashMap<Long, MoveRecord>();
				hashValues = new Long[size.cols][size.rows];
				for(int i = 0; i < size.cols; i++) {
					for(int j = 0; j < size.rows; j++) {
						long next;
						while(true) {
							next = r.nextLong();
							if(next < Long.MAX_VALUE / (2*size.cols*size.rows)) {
								break;
							}
						}
						hashValues[i][j] = next;
					}
				}
			}
			initiated = true;
		}
		if(previousBoardHash != -1) {
			if(currentBoardValue > previousBoardValue) {
				map.get(previousBoardHash).addMove(previousMoveCol, true);
			}
			else if(currentBoardValue < previousBoardValue) {
				map.get(previousBoardHash).addMove(previousMoveCol, false);
			}
		}
		long boardHash = hashBoard(board);
		int moveCol;
		MoveInfo result;
		if(map.containsKey(boardHash)) {
			moveCol = map.get(boardHash).getBestMove();
			//System.out.println("Move record found: \n" + map.get(boardHash).getRecordInfo());
		}
		else {
			map.put(boardHash, new MoveRecord(size.cols));
			moveCol = r.nextInt(size.cols);
			//System.out.println("Move record not found");
		}
		while(true) {
			result = board.makeMove(moveCol, playerNum);
			if(result.getResult() != MoveResult.INVALID) {
				break;
			}
			moveCol = r.nextInt(size.cols);
		}
		previousBoardHash = boardHash;
		previousMoveCol = moveCol;
		previousBoardValue = currentBoardValue;
		return result;
	}

	@Override
	public void startRound(int playerNum) {
		previousBoardHash = -1;
		previousBoardValue = 0;
		previousMoveCol = -1;
	}

	@Override
	public void endRound(int playerNum, int winnerNum) {
		if(playerNum == winnerNum) {
			map.get(previousBoardHash).addMove(previousMoveCol, true);
		}
		else if(winnerNum != 0) {
			map.get(previousBoardHash).addMove(previousMoveCol, false);
		}
	}

	@Override
	public void startGame(int playerNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endGame(int playerNum) {
		if(saveFileName != null) {
			saveToFile(saveFileName);
		}
	}

	private int hashBoard(Board b) {
		int hashValue = 0;
		for(int i = 0; i < size.cols; i++) {
			for(int j = 0; j < size.rows; j++) {
				int posVal = b.getPosState(i, j);
				hashValue += posVal*hashValues[i][j];
			}
		}
		return hashValue;
	}
	
	public void saveToFile(String filename) {
		synchronized(ioLock) {
			try {
				File f = new File("decs", filename);
				if(!f.exists()) {
					f.createNewFile();
				}
				FileOutputStream s = new FileOutputStream(f);
				ObjectOutputStream os = new ObjectOutputStream(s);
				os.writeObject(map);
				os.writeObject(hashValues);
				os.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadFromFile(String filename) {
		synchronized(ioLock) {
			try {
				File f = new File("decs", filename);
				FileInputStream s = new FileInputStream(f);
				ObjectInputStream os = new ObjectInputStream(s);
				map = (HashMap<Long, MoveRecord>)os.readObject();
				hashValues = (Long[][])os.readObject();
				os.close();
			}
			catch(IOException e) {
				System.exit(0);
			}
			catch(ClassNotFoundException e) {
				System.exit(0);
			}
		}
	}
}