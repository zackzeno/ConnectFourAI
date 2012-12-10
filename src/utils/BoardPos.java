package utils;

import java.io.Serializable;

public class BoardPos implements Serializable {

	private static final long serialVersionUID = 6030627699400111096L;
	public int rowIndex;
	public int colIndex;
	
	public BoardPos(int r, int c) {
		rowIndex = r;
		colIndex = c;
	}
	
	public boolean equals(BoardPos other) {
		return (rowIndex == other.rowIndex && colIndex == other.colIndex);
	}
	
	public String toString() {
		return "(" + rowIndex + ", " + colIndex + ")"; 
	}
}
