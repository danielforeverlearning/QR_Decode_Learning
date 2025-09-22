
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

public class RowColumnMapReader {
	enum DIRECTION {
		GO_DIAGONAL,
		GO_LEFT,
		GO_HANDLE_1_Z
	}
	private int row_count;
	private int col_count;
	private char[][] map;
	private ArrayList<String> mybyte;
	private ArrayList<Integer> codewords;
	
	public RowColumnMapReader(int num_rows, int num_columns) {
		
		//top left is 0,0
		
		map       = new char[num_columns][num_rows];
		mybyte    = new ArrayList<String>();
		codewords = new ArrayList<Integer>();
		
		row_count = num_rows;
		col_count = num_columns;
	}//constructor
	
	public ArrayList<String> Get_mybyte()
	{
		return mybyte;
	}//Get_mybyte
	
	public ArrayList<Integer> Get_datacodewords()
	{
		return codewords;
	}//Get_datacodewords
	
	public void DebugPrint_mybyte()
	{
		System.out.println();
		System.out.print("length=" + mybyte.size() + " mybyte= ");
		for (int ii=0; ii < mybyte.size(); ii++)
			System.out.print(mybyte.get(ii) + " ");
		System.out.println();
	}//DebugPrint_mybyte
	
	public void DebugPrint_codewords()
	{
		System.out.println();
		System.out.print("length=" + codewords.size() + " codewords= ");
		for (int ii=0; ii < codewords.size(); ii++)
			System.out.print(codewords.get(ii) + " ");
		System.out.println();
	}//DebugPrint_codewords
	
	
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
	}//Load
	
	
	public void FindDataCodewordsFromMap(int NumBlocksGrp1, int NumDataCodewordsEachBlockGrp1, int NumBlocksGrp2, int NumDataCodewordsEachBlockGrp2)
	{
		int totalCodewords = (NumBlocksGrp1 * NumDataCodewordsEachBlockGrp1) + (NumBlocksGrp2 * NumDataCodewordsEachBlockGrp2);
		
		mybyte.clear();
		codewords.clear();
		
		int xx = col_count - 1;
		int yy = row_count - 1;
		int bitcount = 0;
		char[] temp = { '0', '1', '2', '3', '4', '5', '6', '7' };
		boolean goingup = true;
		int Z_count = 0;
		int Z_save_bitcount = 0;
		
		boolean reverse = false;
		DIRECTION mydir;
		
		while (true) {
			
			//usual case
			if (reverse == false)
			{
				if ((bitcount % 2) == 0) //even bit
					mydir = DIRECTION.GO_LEFT;
				else //odd bit
					mydir = DIRECTION.GO_DIAGONAL;
			}
			else
			{
				if ((bitcount % 2) == 0) //even bit
					mydir = DIRECTION.GO_DIAGONAL;
				else //odd bit
					mydir = DIRECTION.GO_LEFT;
			}
			
			if (map[xx][yy] != 'Z') {
				if (Z_count > 0) {
					if (Z_count == 1)
						mydir = DIRECTION.GO_HANDLE_1_Z; //force go vertical save
					Z_count = 0;
					bitcount = Z_save_bitcount;
				}
				temp[bitcount] = map[xx][yy];
			}
			else { //map[xx][yy] == 'Z'
				if (Z_count == 0) {
					Z_count = 1;
					Z_save_bitcount = bitcount;
				}
				else
					Z_count++;
			}
			
			if (mydir == DIRECTION.GO_LEFT) {
				//it is always the case xx >= 1 no need for code check
				xx--;
			}
			else if (mydir == DIRECTION.GO_DIAGONAL) {
				if (goingup) {
					if (yy == 0 || map[xx + 1][yy - 1] == 'A') {
						goingup = false;
						System.out.println("GOING DOWN");
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
						goingup = true;
						System.out.println("GOING UP");
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
			else if (mydir == DIRECTION.GO_HANDLE_1_Z) {
				if (goingup) {
					if (map[xx + 1][yy - 1] == 'Z')
						yy--; //go vertical
					else { //go diagonal
						yy--;
						xx++;
						
						reverse = true;
					}
				}
				else { //going down
					if (map[xx + 1][yy + 1] == 'Z')
						yy++; //go vertical
					else { //go diagonal
						yy++;
						xx++;
					}
				}
			}
			
			bitcount++;
			
			//save byte when bits are full in temp
			if (bitcount == 8) {
				bitcount = 0;
				
				if (Z_count == 0) {
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
					
					if (codewords.size() == totalCodewords)
						break;
					
				}//Z_count==0
			}//bitcount==8
		}//while
		
		System.out.println("GetCodewordsFromMap: DONE");
	}//FindDataCodewordsFromMap
}//RowColumnMap
