
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList; // import the ArrayList class

public class RowColumnMap {
	
	
	private int row_count;
	private int col_count;
	private char[][] map;
	private ArrayList<String> mybyte;
	private ArrayList<Integer> codewords;
	
	public RowColumnMap(int num_rows, int num_columns) {
		
		//top left is 0,0
		
		map = new char[num_columns][num_rows];
		row_count = num_rows;
		col_count = num_columns;
	}
	
	public boolean Load(String input_filename) {
		
		try {
			File inputFile = new File(input_filename);
			Scanner inputReader = new Scanner(inputFile);
			for (int yy=0; yy < row_count; yy++) {
				String inputData = inputReader.nextLine();
				
				for (int xx=0; xx < col_count; xx++) {
					char inputChar = inputData.charAt(xx);
					map[xx][yy] = inputChar;
				}
			}
			
			inputReader.close();
			System.out.println("LoadMap: Success");
			return true;
		}
		catch (Exception ex) {
			System.out.println("LoadMap: An exception occurred!");
			ex.printStackTrace();
			return false;
		}
	}
	
	public void GetCodewordsFromMap() {
		mybyte    = new ArrayList<String>();
		codewords = new ArrayList<Integer>();
		int xx = col_count - 1;
		int yy = row_count - 1;
		int bitcount = 0;
		char[] temp = { '0', '1', '2', '3', '4', '5', '6', '7' };
		boolean goingup = true;
		
		while (true) {
			temp[bitcount] = map[xx][yy];
			
			if ((bitcount % 2) == 0) { //even bit
				//it is always the case xx >= 1 no need for code check
				xx--;
			}
			else { //odd bit
				if (goingup) {
					if (yy == 0 || map[xx + 1][yy - 1] == 'A') {
						if (bitcount == 7)
							goingup = false;
						xx--;
						if (xx < 0)
							break; //end getting codewords
					}
					else {
						yy--;
						xx++;
					}
				}
				else {
					if (yy == (row_count - 1) || map[xx + 1][yy + 1] == 'A') {
						if (bitcount == 7)
							goingup = true;
						xx--;
						if (xx < 0)
							break; //end getting codewords
					}
					else {
						yy++;
						xx++;
					}
				}
			}
			
			//save byte when bits are full in temp
			bitcount++;
			if (bitcount == 8) {
				bitcount = 0;
				
				//get an integer value
				int tempint = 0;
				for (int ii=0; ii <= 7; ii++) {
					if (temp[ii] == '1') {
						if (ii == 0)
							tempint += 128;
						else if (ii == 1)
							tempint += 64;
						else if (ii == 2)
							tempint += 32;
						else if (ii == 3)
							tempint += 16;
						else if (ii == 4)
							tempint += 8;
						else if (ii == 5)
							tempint += 4;
						else if (ii == 6)
							tempint += 2;
						else
							tempint += 1;
					}
				}
				
				String tempstr = new String(temp);
				System.out.println(tempstr + " = " + tempint);
				mybyte.add(tempstr);
				codewords.add(tempint);
			}
		}
		
		System.out.println("GetCodewordsFromMap: DONE");
	}
}//RowColumnMap