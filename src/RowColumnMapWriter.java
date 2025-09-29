/*****************************************************************
Alignment Pattern Locations Table
                Center Module row and column (top right is 0,0)
QR Version 2	6	18					
QR Version 3	6	22					
QR Version 4	6	26					
QR Version 5	6	30
QR Version 6	6	34					
QR Version 7	6	22	38				
QR Version 8	6	24	42				
QR Version 9	6	26	46				
QR Version 10	6	28	50				
QR Version 11	6	30	54				
QR Version 12	6	32	58				
QR Version 13	6	34	62				
QR Version 14	6	26	46	66			
QR Version 15	6	26	48	70			
QR Version 16	6	26	50	74			
QR Version 17	6	30	54	78			
QR Version 18	6	30	56	82			
QR Version 19	6	30	58	86			
QR Version 20	6	34	62	90			
QR Version 21	6	28	50	72	94		
QR Version 22	6	26	50	74	98		
QR Version 23	6	30	54	78	102		
QR Version 24	6	28	54	80	106		
QR Version 25	6	32	58	84	110		
QR Version 26	6	30	58	86	114		
QR Version 27	6	34	62	90	118		
QR Version 28	6	26	50	74	98	122	
QR Version 29	6	30	54	78	102	126	
QR Version 30	6	26	52	78	104	130	
QR Version 31	6	30	56	82	108	134	
QR Version 32	6	34	60	86	112	138	
QR Version 33	6	30	58	86	114	142	
QR Version 34	6	34	62	90	118	146
QR Version 35	6	30	54	78	102	126	150
QR Version 36	6	24	50	76	102	128	154
QR Version 37	6	28	54	80	106	132	158
QR Version 38	6	32	58	84	110	136	162
QR Version 39	6	26	54	82	110	138	166
QR Version 40	6	30	58	86	114	142	170
***********************************************************************/






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
         
		System.out.println("FillMap all_codewords.size == " + all_codewords.size());
		while (ii < all_codewords.size())
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
					if (ii >= all_codewords.size())
						return;
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
			else if (map[xx][yy] == 'A' && xx <= 8) {
				//going up on the last few ec-codewords
				//last bit of this codeword is above the bottom-left-'A's
				char temp = map[xx][yy];
				while (temp == 'A') {
					yy--;
					temp = map[xx][yy];
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
				mydir = DIRECTION.GO_LEFT;
			}//going up on the last few ec-codewords
			
			else if (map[xx][yy] == 'T' && xx <= 8) {
				//almost done 
				xx--;
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
				mydir = DIRECTION.GO_LEFT;
			}//almost done

			
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
        
        public void Change_Remaining_E_To_0()
        {
            for (int yy=0; yy < row_count; yy++)
            {
		for (int xx=0; xx < col_count; xx++)
                {
                    if (map[xx][yy] == 'E')
                        map[xx][yy] = '0';
                }
            }
        }//Change_Remaining_E_To_0
	
}//class

