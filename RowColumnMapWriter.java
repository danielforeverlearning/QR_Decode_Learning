
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;


public class RowColumnMapWriter {
	
	enum DIRECTION {
		GO_DIAGONAL,
		GO_LEFT,
		GO_HANDLE_1_Z
	}
	
	private int row_count;
	private int col_count;
	private char[][] map;

	public RowColumnMapWriter(String oldqrcodemapfilename, int num_rows, int num_columns)
	{
		map       = new char[num_columns][num_rows];
		row_count = num_rows;
		col_count = num_columns;
			
		try {
			File inputFile = new File(oldqrcodemapfilename);
			Scanner inputReader = new Scanner(inputFile);
			for (int yy=0; yy < row_count; yy++) {
				String inputData = inputReader.nextLine();
				for (int xx=0; xx < col_count; xx++) {
					char inputChar = inputData.charAt(xx);
					if (inputChar=='A' || 
						inputChar=='Z' || 
						inputChar=='T' ||
						inputChar=='U')
						map[xx][yy] = inputChar;
					else //X==brand, 0, 1
						map[xx][yy] = 'E'; //E==empty
				}
			}	
			inputReader.close();
		}
		catch (Exception ex) {
			System.out.println("RowColumnMapWriter constructor: An exception occurred!");
			ex.printStackTrace();
		}
	}//constructor
	
	public void DumpMap(String output_filename)
	{
		//top left is 0,0
		
		try {
			FileWriter myWriter = new FileWriter(output_filename);
			for (int yy=0; yy < row_count; yy++) {
				for (int xx=0; xx < col_count; xx++)
					myWriter.write(map[xx][yy]);
				
				myWriter.write('\n');
			}	
			myWriter.close();
		}
		catch (Exception ex) {
			System.out.println("DumpMap: An exception occurred!");
			ex.printStackTrace();
		}
	}//DumpMap
	

	public void FillMap(ArrayList<Integer> all_codewords) throws Exception
	{
		
		int xx = col_count - 1;
		int yy = row_count - 1;
		int bitcount = 0;

		boolean goingup = true;
		int Z_count = 0;
		int U_count = 0;
		
		boolean reverse = false;
		DIRECTION mydir;
		
		int ii=0;
		String padded8str   = "00000000";
		String binaryString = Integer.toBinaryString(all_codewords.get(0));
		if (binaryString.length() == 8)
			padded8str = binaryString;
		else
			padded8str = padded8str.substring(0, 8 - binaryString.length()) + binaryString;
         
		
		//while (ii < all_codewords.size())
		while (ii < 69)
		{
			//usual case
			if (reverse == false)
			{
				if (Z_count == 0 && U_count == 0)
				{
					if ((bitcount % 2) == 0) //even bit
						mydir = DIRECTION.GO_LEFT;
					else //odd bit
						mydir = DIRECTION.GO_DIAGONAL;
				}
				else if (Z_count >= 1)
				{
					if ((Z_count % 2) == 0)
						mydir = DIRECTION.GO_LEFT;
					else
						mydir = DIRECTION.GO_DIAGONAL;
				}
				else if (U_count >= 1)
				{
					if (U_count == 1) //saw 1 U now on 2nd U
						mydir = DIRECTION.GO_DIAGONAL;
					else
						mydir = DIRECTION.GO_LEFT;
				}
				else
					throw new Exception("reverse==false unhandled char found");
			}
			else
			{
				if (Z_count == 0 && U_count == 0)
				{
					if ((bitcount % 2) == 0) //even bit
						mydir = DIRECTION.GO_DIAGONAL;
					else //odd bit
						mydir = DIRECTION.GO_LEFT;
				}
				else if (Z_count >= 1)
				{
					if ((Z_count % 2) == 0)
						mydir = DIRECTION.GO_DIAGONAL;
					else
						mydir = DIRECTION.GO_LEFT;
				}
				else if (U_count >= 1)
				{
					if (U_count == 1) //saw 1 U now on 2nd U
						mydir = DIRECTION.GO_DIAGONAL;
					else
						mydir = DIRECTION.GO_LEFT;
				}
				else
					throw new Exception("reverse==true unhandled char found");
			}
			
			if (map[xx][yy] == 'E') {
				if (Z_count > 0) {
					if (Z_count == 1)
						mydir = DIRECTION.GO_HANDLE_1_Z; //force go vertical save
					Z_count = 0;
				}
				else if (U_count > 0) {
					//U_count should be 2
					if (U_count == 2)
						U_count = 0;
					else
						throw new Exception("U_count is not 2");
				}
				map[xx][yy] = padded8str.charAt(bitcount);
				bitcount++;
				if (bitcount == 8) {
					bitcount = 0;
					ii++;
					binaryString = Integer.toBinaryString(all_codewords.get(ii));
					padded8str   = "00000000";
					if (binaryString.length() == 8)
						padded8str = binaryString;
					else
						padded8str = padded8str.substring(0, 8 - binaryString.length()) + binaryString;
				}//bitcount==8
			}
			else if (map[xx][yy] == 'Z')
				Z_count++;
			else if (map[xx][yy] == 'U')
				U_count++;
			else {
				char temp = map[xx][yy];
				throw new Exception("unknown char in map");
			}

			
			if (mydir == DIRECTION.GO_LEFT) {
				//it is always the case xx >= 1 no need for code check
				xx--;
			}
			else if (mydir == DIRECTION.GO_DIAGONAL) {
				if (goingup) {
					if (yy == 0 || map[xx + 1][yy - 1] == 'A') {
						goingup = false;
						//System.out.println("GOING DOWN");
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
						//System.out.println("GOING UP");
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
		}//for
	}//FillMap
	
}//class

