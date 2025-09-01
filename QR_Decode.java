import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;


public class QR_Decode  { 

	//  https://www.thonky.com/qr-code-tutorial/mask-patterns
	//  says 
	//  Mask Number 0: (row + column) mod 2 == 0
	//  Mask Number 1: (row) mod 2 == 0
	//  Mask Number 2: (column) mod 3 == 0
	//  Mask Number 3: (row + column) mod 3 == 0
	//  Mask Number 4: (floor(row/2) + floor(column/3)) mod 2 == 0
	//  Mask Number 5: ((row*column)mod2) + ((row*column)mod3) == 0
	//  Mask Number 6: (((row*column)mod2)+((row*column)mod3))mod2 == 0
	//  Mask Number 7: (((row+column)mod2)+((row*column)mod3))mod2 == 0
	//  which is not what the diagram in wikipedia says,
	//  furthermore the diagram in wikipedia uses white as bit==1 and black as bit==0
	//  to label the masks :
	//  Wikipedia diagram mask0: j%3             == 0
	//  Wikipedia diagram mask1: (i+j)%3         == 0
	//  Wikipedia diagram mask2: (i+j)%2         == 0
	//  Wikipedia diagram mask3: i%2             == 0
	//  Wikipedia diagram mask4: ((i*j)%3+i*j)%2 == 0
	//  Wikipedia diagram mask5: ((i*j)%3+i+j)%2 == 0
	//  Wikipedia diagram mask6: (i/2 + j/3)%2   == 0
	//  Wikipedia diagram mask7: (i*j)%2+(i*j)%3 == 0
	//
	//  Wikipedia masks same as QR_Format_Information.svg
	
	enum ENCODING_INDICATOR {
		NUMERIC,                          //0001 (10bits per 3 digits) 
		ALPHANUMERIC,                     //0010 (11bits per 2 characters)
		BYTE,                             //0100 ( 8bits per character)
		KANJI,                            //1000 (13bits per character)
		STRUCTURED_APPEND,                //0011 (used to split a message across multiple QR symbols)
		EXTENDED_CHANNEL_INTERPRETATION,  //0111 (select alternate character set or encoding)
		FNC1_1ST_POSITION,                //0101
		FNC1_2ND_POSITION,                //1001
		END_OF_MESSAGE,                   //0000 (terminator)
		UNKNOWN
	}
	
	
	
	public static void Write_Wikipedia_Mask_7() {
		try {
			FileWriter myWriter = new FileWriter("mask_7.txt");
			for (int ii=0; ii <= 40; ii++) {
			    for (int jj=0; jj <= 40; jj++) {
					int answer = (ii*jj)%2+(ii*jj)%3;
					if (answer == 0)
						myWriter.write("i"); //i means invert the color
					else
						myWriter.write("d"); //d means do not invert the color
				}
				myWriter.write('\n');
			}
			myWriter.close();
		}
		catch (IOException ex) {
			System.out.println("Write_Wikipedia_Mask_7: An exception occurred!");
			ex.printStackTrace();
		}
	}
	
	
	public static void Write_Wikipedia_Mask_6() {
		try {
			FileWriter myWriter = new FileWriter("mask_6.txt");
			for (int ii=0; ii <= 40; ii++) {
			    for (int jj=0; jj <= 40; jj++) {
					int answer = (ii/2 + jj/3)%2;
					if (answer == 0)
						myWriter.write("i"); //i means invert the color
					else
						myWriter.write("d"); //d means do not invert the color
				}
				myWriter.write('\n');
			}
			myWriter.close();
		}
		catch (IOException ex) {
			System.out.println("Write_Wikipedia_Mask_6: An exception occurred!");
			ex.printStackTrace();
		}
	}


    public static void Write_Wikipedia_Mask_5() {
		//i means invert the color
		//d means do not invert the color
		try {
			FileWriter myWriter = new FileWriter("./bin/mask_5.txt");
			for (int ii=0; ii <= 40; ii++) {
			    for (int jj=0; jj <= 40; jj++) {
					int answer = ((ii*jj)%3+ii+jj)%2;
					if (answer == 0)
						myWriter.write("i");
					else
						myWriter.write("d");
				}
				myWriter.write('\n');
			}
			myWriter.close();
		}
		catch (IOException ex) {
			System.out.println("Write_Wikipedia_Mask_5: An exception occurred!");
			ex.printStackTrace();
		}
	}
	
	
	public static void Write_Wikipedia_Mask_4() {
		try {
			FileWriter myWriter = new FileWriter("mask_4.txt");
			for (int ii=0; ii <= 40; ii++) {
			    for (int jj=0; jj <= 40; jj++) {
					int answer = ((ii*jj)%3+ii*jj)%2;
					if (answer == 0)
						myWriter.write("i"); //i means invert the color
					else
						myWriter.write("d"); //d means do not invert the color
				}
				myWriter.write('\n');
			}
			myWriter.close();
		}
		catch (IOException ex) {
			System.out.println("Write_Wikipedia_Mask_4: An exception occurred!");
			ex.printStackTrace();
		}
	}
	
	
	public static void Write_Wikipedia_Mask_3() {
		try {
			FileWriter myWriter = new FileWriter("mask_3.txt");
			for (int ii=0; ii <= 40; ii++) {
			    for (int jj=0; jj <= 40; jj++) {
					int answer = ii%2;
					if (answer == 0)
						myWriter.write("i"); //i means invert the color
					else
						myWriter.write("d"); //d means do not invert the color
				}
				myWriter.write('\n');
			}
			myWriter.close();
		}
		catch (IOException ex) {
			System.out.println("Write_Wikipedia_Mask_3: An exception occurred!");
			ex.printStackTrace();
		}
	}
	
	
	public static void Write_Wikipedia_Mask_2() {
		try {
			FileWriter myWriter = new FileWriter("mask_2.txt");
			for (int ii=0; ii <= 40; ii++) {
			    for (int jj=0; jj <= 40; jj++) {
					int answer = (ii+jj)%2;
					if (answer == 0)
						myWriter.write("i"); //i means invert the color
					else
						myWriter.write("d"); //d means do not invert the color
				}
				myWriter.write('\n');
			}
			myWriter.close();
		}
		catch (IOException ex) {
			System.out.println("Write_Wikipedia_Mask_2: An exception occurred!");
			ex.printStackTrace();
		}
	}
	
	
	public static void Write_Wikipedia_Mask_1() {
		try {
			FileWriter myWriter = new FileWriter("mask_1.txt");
			for (int ii=0; ii <= 40; ii++) {
			    for (int jj=0; jj <= 40; jj++) {
					int answer = (ii+jj)%3;
					if (answer == 0)
						myWriter.write("i"); //i means invert the color
					else
						myWriter.write("d"); //d means do not invert the color
				}
				myWriter.write('\n');
			}
			myWriter.close();
		}
		catch (IOException ex) {
			System.out.println("Write_Wikipedia_Mask_1: An exception occurred!");
			ex.printStackTrace();
		}
	}
	
	public static void Write_Wikipedia_Mask_0() {
		try {
			FileWriter myWriter = new FileWriter("mask_0.txt");
			for (int ii=0; ii <= 40; ii++) {
			    for (int jj=0; jj <= 40; jj++) {
					int answer = jj % 3;
					if (answer == 0)
						myWriter.write("i"); //i means invert the color
					else
						myWriter.write("d"); //d means do not invert the color
				}
				myWriter.write('\n');
			}
			myWriter.close();
		}
		catch (IOException ex) {
			System.out.println("Write_Wikipedia_Mask_0: An exception occurred!");
			ex.printStackTrace();
		}
	}
	
	
	public static boolean Do_Mask(String input_filename, String mask_filename, String output_filename) {
		try {
			FileWriter myWriter = new FileWriter(output_filename);
			
			File inputFile = new File(input_filename);
			File maskFile = new File(mask_filename);
			Scanner inputReader = new Scanner(inputFile);
			Scanner maskReader = new Scanner(maskFile);
			boolean inputNextLine = inputReader.hasNextLine();
			boolean maskNextLine = maskReader.hasNextLine();
			while (inputNextLine && maskNextLine) {
				String inputData = inputReader.nextLine();
				String maskData = maskReader.nextLine();
				
				int inputLen = inputData.length();
				int maskLen  = maskData.length();
				
				if (inputLen != maskLen) {
					System.out.println("Do_Mask: inputLen and maskLen NOT EQUAL FORCING QUIT!");
					return false;
				}
				
				for (int ii=0; ii < inputLen; ii++) {
					char inputChar = inputData.charAt(ii);
					char maskChar = maskData.charAt(ii);
					
					if (maskChar == 'd') //d means do NOT invert color
						myWriter.write(inputChar);
					else if (maskChar == 'i') { //i means invert color
						if (inputChar == 'A')
							myWriter.write('A');
						else if (inputChar == 'X')
							myWriter.write('X');
						else if (inputChar == 'Z')
							myWriter.write('Z');
						else if (inputChar == '0')
							myWriter.write('1');
						else if (inputChar == '1')
							myWriter.write('0');
						else {
							System.out.println("Do_Mask: Unsupported character found in the input file, FORCING QUIT!");
							return false;							
						}
					}
					else {
						System.out.println("Do_Mask: Unsupported character found in the mask file, FORCING QUIT");
						return false;
					}
				}
				
				myWriter.write('\n');
				inputNextLine = inputReader.hasNextLine();
				maskNextLine = maskReader.hasNextLine();
			}
			
			inputReader.close();
			maskReader.close();
			myWriter.close();
			
			System.out.println("Do_Mask: Success");
			return true;
		}
		catch (Exception ex) {
			System.out.println("Do_Mask: An exception occurred!");
			ex.printStackTrace();
			return false;
		}
	}//Do_Mask
	
	
	public static int ConvertBinaryByteStringToPositiveInteger(String binaryByteString) {
		int[] two_pow = {128, 64, 32, 16, 8, 4, 2, 1};
		int answer = 0;
		for (int ii=0; ii <= 7; ii++) {
			char mychar = binaryByteString.charAt(ii);
			if (mychar == '1')
				answer += two_pow[ii];
		}
        return answer;		
	}//ConvertBinaryByteStringToPositiveInteger
	
	
	public static boolean Change_OriginalMap_Black0(String input_filename, String output_filename) {
		try {
			FileWriter myWriter = new FileWriter(output_filename);		
			File inputFile = new File(input_filename);
			Scanner inputReader = new Scanner(inputFile);
			boolean inputNextLine = inputReader.hasNextLine();
			while (inputNextLine) {
				String inputData = inputReader.nextLine();
				int inputLen = inputData.length();
				
				for (int ii=0; ii < inputLen; ii++) {
					char inputChar = inputData.charAt(ii);
					
					if (inputChar == 'A')
						myWriter.write('A');
					else if (inputChar == 'X')
						myWriter.write('X');
					else if (inputChar == 'Z')
						myWriter.write('Z');
					else if (inputChar == '0')
						myWriter.write('1');
					else if (inputChar == '1')
						myWriter.write('0');
					else {
						System.out.println("Change_OriginalMap_Black0: Unsupported character found in the input file, FORCING QUIT!");
						return false;							
					}
				}
				
			    myWriter.write('\n');
			    inputNextLine = inputReader.hasNextLine();
			}
			
			inputReader.close();
			myWriter.close();
			
			System.out.println("Change_OriginalMap_Black0: Success");
			return true;
		}
		catch (Exception ex) {
			System.out.println("Change_OriginalMap_Black0: An exception occurred!");
			ex.printStackTrace();
			return false;
		}
	}//Change_OriginalMap_Black0
	
	public static boolean Decode_Correct_Sequential_Codewords(String input_filename, String output_filename) {
		try {
			
			//FileWriter myWriter = new FileWriter(output_filename);		
			File inputFile        = new File(input_filename);
			Scanner inputReader   = new Scanner(inputFile);
			
			boolean getIndicator  = true;
			int length            = 0;
			ENCODING_INDICATOR encoding = ENCODING_INDICATOR.UNKNOWN;
			
			String startOfByte    = "";
			
			while (true) {
				
				if (getIndicator) {
					
					String padded8str = "00000000";
				    String inputData  = inputReader.nextLine();
				    int number = Integer.parseInt(inputData);
					//System.out.println("number=" + number);
                    String binaryString = Integer.toBinaryString(number);
					//System.out.println("binaryString=" + binaryString);
				    if (binaryString.length() == 8)
						padded8str = binaryString;
					else
						padded8str = padded8str.substring(0, 8 - binaryString.length()) + binaryString;
                    //System.out.println("padded8str=" + padded8str);
					
					if (padded8str.startsWith("0001"))
						encoding = ENCODING_INDICATOR.NUMERIC;
					else if (padded8str.startsWith("0010"))
						encoding = ENCODING_INDICATOR.ALPHANUMERIC;
					else if (padded8str.startsWith("0100"))
						encoding = ENCODING_INDICATOR.BYTE;
					else if (padded8str.startsWith("1000"))
						encoding = ENCODING_INDICATOR.KANJI;
					else if (padded8str.startsWith("0011"))
						encoding = ENCODING_INDICATOR.STRUCTURED_APPEND;
					else if (padded8str.startsWith("0111"))
						encoding = ENCODING_INDICATOR.EXTENDED_CHANNEL_INTERPRETATION;
					else if (padded8str.startsWith("0101"))
						encoding = ENCODING_INDICATOR.FNC1_1ST_POSITION;
					else if (padded8str.startsWith("1001"))
						encoding = ENCODING_INDICATOR.FNC1_2ND_POSITION;
					else if (padded8str.startsWith("0000"))
						encoding = ENCODING_INDICATOR.END_OF_MESSAGE;
					else
						encoding = ENCODING_INDICATOR.UNKNOWN;
					
				    System.out.println("INDICATOR=" + padded8str.substring(0,4) + " , " + encoding);
					if ((encoding != ENCODING_INDICATOR.BYTE) && (encoding != ENCODING_INDICATOR.END_OF_MESSAGE)) {
					    System.out.println("Sorry, so far only know ENCODING_INDICATOR.BYTE or ENCODING_INDICATOR.END_OF_MESSAGE, exiting.....");
                        break;						
					}
					
					//get length
					String startOfLength = padded8str.substring(4,8);
				    inputData = inputReader.nextLine();
					System.out.println("inputData=" + inputData);
					number = Integer.parseInt(inputData);
					System.out.println("number=" + number);
					binaryString = Integer.toBinaryString(number);
				    if (binaryString.length() == 8)
						padded8str = binaryString;
					else
						padded8str = padded8str.substring(0, 8 - binaryString.length()) + binaryString;
					System.out.println("padded8str=" + padded8str);
					binaryString = startOfLength + padded8str.substring(0,4);
					System.out.println("binaryString=" + binaryString);
					length = ConvertBinaryByteStringToPositiveInteger(binaryString);
					System.out.println("length=" + length);
					
					startOfByte = padded8str.substring(4,8);
					
					System.out.println("startOfByte=" + startOfByte);
					
					getIndicator = false;
				} 
				else {
				    if (length > 0) {
						String padded8str = "00000000";
				        String inputData  = inputReader.nextLine();
				        int number = Integer.parseInt(inputData);
					    //System.out.println("number=" + number);
                        String binaryString = Integer.toBinaryString(number);
					    //System.out.println("binaryString=" + binaryString);
				        if (binaryString.length() == 8)
						    padded8str = binaryString;
					    else
						    padded8str = padded8str.substring(0, 8 - binaryString.length()) + binaryString;
						//System.out.println("padded8str=" + padded8str);
						
						String mybyte = startOfByte + padded8str.substring(0,4);
						int ascii_decimal_int = ConvertBinaryByteStringToPositiveInteger(mybyte);
						char character = (char) ascii_decimal_int;
						
						System.out.println(" ascii_decimal_int=" + ascii_decimal_int + " = " + character);
						
						startOfByte = padded8str.substring(4,8);
						length--;
					}
					else {
						getIndicator = true;
						break;
					}
				}
			}//while
			
			inputReader.close();
			//myWriter.close();
			
			return true;
		}
		catch (Exception ex) {
			System.out.println("Decode_Correct_Sequential_Codewords: An exception occurred!");
			ex.printStackTrace();
			return false;
		}
	}//Decode_Correct_Sequential_Codewords
	
	
		
	public static void main(String s[]) {
		//20221025_104402.jpg original image
		//original_map.txt created by eyeball-and-hand, no code yet
		//original_map_2.txt created by eyeball-and-hand, no code yet
		
		//Do not need this, but just save the code
		//Change_OriginalMap_Black0("original_map_2.txt", "original_map_3_black0.txt");
		
		//These codewords we got from stepping thru online javascript code
		//https://webqr.com/
		//Decode_Correct_Sequential_Codewords("./bin/20221025_104402_codewords.txt", "");
		
		
		Write_Wikipedia_Mask_5();
		boolean success = Do_Mask("./bin/original_map_2.txt", "./bin/mask_5.txt", "./bin/after_mask5.txt");
		if (!success)
		{
			System.out.println("ERROR");
			return;
		}
		
		//see interleaving.jpg or 
		//https://www.thonky.com/qr-code-tutorial/error-correction-table
		//
		//20221025_104402.jpg
		//
		//Version and EC Level                                = 6-H
		//Number of Blocks in Group1                          = 4
		//Number of Data Codewords in Each of Group1's Blocks = 15
		//Number of Blocks in Group2                          = 0
		//Number of Data Codewords in Each of Group2's Blocks = 0
		RowColumnMap mymap = new RowColumnMap(41,41);
		mymap.Load("./bin/after_mask5.txt");
		mymap.GetCodewordsFromMap(4,15,0,0);
		
		System.out.println("DONE");
    }//main
}//class

 