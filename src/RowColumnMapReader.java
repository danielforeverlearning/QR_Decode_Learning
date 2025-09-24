
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
	
	public void DumpMap(String filepath)
	{
		try
		{
			FileWriter myWriter = new FileWriter(filepath);
			for (int yy=0; yy < row_count; yy++)
			{
				for (int xx=0; xx < col_count; xx++)
				{
					myWriter.write(map[xx][yy]);
				}
				myWriter.write('\n');
			}
			myWriter.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}//DumpMap
	
	public void Change_Finders_Timing_Alignment(int version)
	{
		//top left 9x9
		for (int yy=0; yy < 9; yy++)
		{
			for (int xx=0; xx < 9; xx++)
			{
				map[xx][yy] = 'A';
			}
		}
		
		//top right 8x9
		int startxx = col_count - 1;
		int endxx   = col_count - 8;
		for (int yy=0; yy < 9; yy++)
		{
			for (int xx=startxx; xx >= endxx; xx--)
			{
				map[xx][yy] = 'A';
			}
		}
		
		//bottom left 9x8
		int startyy = row_count - 1;
		int endyy   = row_count - 8;
		for (int yy=startyy; yy >= endyy; yy--)
		{
			for (int xx=0; xx < 9; xx++)
			{
				map[xx][yy] = 'A';
			}
		}
		
		//horizontal top timing 'U'
		for (int xx=0; xx < col_count; xx++)
		{
			if (map[xx][6] != 'A')
				map[xx][6] = 'U';
		}
		
		//vertical left timing 'T'
		for (int yy=0; yy < row_count; yy++)
		{
			if (map[6][yy] != 'A')
				map[6][yy] = 'T';
		}
		
		/***********************************************
		 Alignment Pattern Locations
		 
         Version			Center Module Row and Column
		 --------------------------------------------
		 QR Version 2		6	18					
		 QR Version 3		6	22					
		 QR Version 4		6	26					
		 QR Version 5		6	30	
		 QR Version 6		6	34
		 ****************************************************/
		 int align_center_xx = col_count - 7;
		 int align_center_yy = row_count - 7;
		 startxx = align_center_xx - 2;
		 endxx   = align_center_xx + 2;
		 startyy = align_center_yy - 2;
		 endyy   = align_center_yy + 2;
		 for (int yy=startyy; yy <= endyy ; yy++)
		 {
			 for (int xx=startxx; xx <= endxx ; xx++)
			 {
				 map[xx][yy] = 'Z';
			 }
		 }
		 
		
	}//Change_Finders_Timing_Alignment
	
	
	public void DebugPrintFormatStr()
	{
		char[] format1 = new char[15];
		format1[0]  = map[0][8];
		format1[1]  = map[1][8];
		format1[2]  = map[2][8];
		format1[3]  = map[3][8];
		format1[4]  = map[4][8];
		format1[5]  = map[5][8];
		
		format1[6]  = map[7][8];
		
		format1[7]  = map[8][8];
		format1[8]  = map[8][7];
		
		format1[9]  = map[8][5];
		format1[10] = map[8][4];
		format1[11] = map[8][3];
		format1[12] = map[8][2];
		format1[13] = map[8][1];
		format1[14] = map[8][0];
		
		char[] format2 = new char[15];
		format2[0]  = map[8][row_count-1];
		format2[1]  = map[8][row_count-2];
		format2[2]  = map[8][row_count-3];
		format2[3]  = map[8][row_count-4];
		format2[4]  = map[8][row_count-5];
		format2[5]  = map[8][row_count-6];
		format2[6]  = map[8][row_count-7];
		
		format2[14] = map[col_count-1][8];
		format2[13] = map[col_count-2][8];
		format2[12] = map[col_count-3][8];
		format2[11] = map[col_count-4][8];
		format2[10] = map[col_count-5][8];
		format2[9]  = map[col_count-6][8];
		format2[8]  = map[col_count-7][8];
		format2[7]  = map[col_count-8][8];
		
		char[] format_mask = { '1', '0', '1', '0', '1', '0', '0', '0', '0', '0', '1', '0', '0', '1', '0' };
		//Documentation file "QR_Format_Information.svg" shows format BEFORE DOING MASK :(
		
		//XOR with format_mask
		char[] format1_aftermask = new char[15];
		char[] format2_aftermask = new char[15];
		for (int ii=0; ii < 15; ii++)
		{
			if (format_mask[ii] == '0')
			{
				format1_aftermask[ii] = format1[ii];
				format2_aftermask[ii] = format2[ii];
			}
			else
			{
				if (format1[ii] == '1')
					format1_aftermask[ii] = '0';
				else
					format1_aftermask[ii] = '1';
				
				if (format2[ii] == '1')
					format2_aftermask[ii] = '0';
				else
					format2_aftermask[ii] = '1';
			}
		}//for
		
		if (format1_aftermask[0] == '0' && format1_aftermask[1] == '1')
			System.out.println("format1_aftermask: Error Correction Level == L");
		else if (format1_aftermask[0] == '0' && format1_aftermask[1] == '0')
			System.out.println("format1_aftermask: Error Correction Level == M");
		else if (format1_aftermask[0] == '1' && format1_aftermask[1] == '1')
			System.out.println("format1_aftermask: Error Correction Level == Q");
		else if (format1_aftermask[0] == '1' && format1_aftermask[1] == '0')
			System.out.println("format1_aftermask: Error Correction Level == H");
		
		if (format2_aftermask[0] == '0' && format2_aftermask[1] == '1')
			System.out.println("format2_aftermask: Error Correction Level == L");
		else if (format2_aftermask[0] == '0' && format2_aftermask[1] == '0')
			System.out.println("format2_aftermask: Error Correction Level == M");
		else if (format2_aftermask[0] == '1' && format2_aftermask[1] == '1')
			System.out.println("format2_aftermask: Error Correction Level == Q");
		else if (format2_aftermask[0] == '1' && format2_aftermask[1] == '0')
			System.out.println("format2_aftermask: Error Correction Level == H");
		
		System.out.println("format1_aftermask: Mask Pattern == " + format1_aftermask[2] + format1_aftermask[3] + format1_aftermask[4]);
		System.out.println("format2_aftermask: Mask Pattern == " + format2_aftermask[2] + format2_aftermask[3] + format2_aftermask[4]);  
		
		System.out.println("format1: Mask Pattern == " + format1[2] + format1[3] + format1[4]);
		System.out.println("format2: Mask Pattern == " + format2[2] + format2[3] + format2[4]);  
		
	}//DebugPrintFormatStr
	
	public ArrayList<String> Get_mybyte()
	{
		return mybyte;
	}//Get_mybyte
	
	public ArrayList<Integer> Get_codewords()
	{
		return codewords;
	}//Get_codewords
	
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
	
	
	public void Find_Codewords(int totalCodewords) throws Exception
	{	
		mybyte.clear();
		codewords.clear();
		
		int xx = col_count - 1;
		int yy = row_count - 1;
		int bitcount = 0;
		
		char[] temp = { '0', '1', '2', '3', '4', '5', '6', '7' };
		
		boolean goingup = true;
		int Z_count = 0;
		int U_count = 0;
		int Z_save_bitcount = 0;
		
		boolean reverse = false;
		DIRECTION mydir;
		
		while (true) {
			
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
			
			if (map[xx][yy] == '0' || map[xx][yy] == '1') {
				if (Z_count > 0) {
					if (Z_count == 1)
						mydir = DIRECTION.GO_HANDLE_1_Z; //force go vertical save
					Z_count = 0;
					bitcount = Z_save_bitcount;
				}
				else if (U_count > 0) {
					//U_count should be 2
					if (U_count == 2)
						U_count = 0;
					else
						throw new Exception("U_count is not 2");
				}
				temp[bitcount] = map[xx][yy];
			}
			else if (map[xx][yy] == 'Z') {
				if (Z_count == 0) {
					Z_count = 1;
					Z_save_bitcount = bitcount;
				}
				else
					Z_count++;
			}
			else if (map[xx][yy] == 'U')
				U_count++;
			else if (map[xx][yy] == 'A' && xx <= 8) {
				//going up on the last few ec-codewords
				//last bit of this codeword is above the bottom-left-'A's
				char mychar = map[xx][yy];
				while (mychar == 'A') {
					yy--;
					mychar = map[xx][yy];
				}
				temp[bitcount] = map[xx][yy];
				mydir = DIRECTION.GO_LEFT;
			}//going up on the last few ec-codewords
			else if (map[xx][yy] == 'T' && xx <= 8) {
				//almost done 
				xx--;
				temp[bitcount] = map[xx][yy];
				mydir = DIRECTION.GO_LEFT;
			}//almost done
			
			//cursor movement
			
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
		
		System.out.println("Find_Codewords: DONE");
	}//Find_Codewords
	
}//RowColumnMap
