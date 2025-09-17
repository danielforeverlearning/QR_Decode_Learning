import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


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
	
		
	public static void main(String s[]) {
		//20221025_104402.jpg original image
		//original_map.txt created by eyeball-and-hand, no code yet
		//original_map_2.txt created by eyeball-and-hand, no code yet
		
		//Do not need this, but just save the code
		//Change_OriginalMap_Black0("original_map_2.txt", "original_map_3_black0.txt");
		
		//These codewords we got from stepping thru online javascript code
		//https://webqr.com/
		//DataCodewordsDecoder dec = new DataCodewordsDecoder();
		//dec.Decode_Correct_Sequential_Codewords("./bin/20221025_104402_codewords.txt");
		
		
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
		int ecc_per_block_6H = 28;
		int dc_per_block_6H  = 15;
		int num_of_blocks_6H = 4;
		RowColumnMapReader mymap = new RowColumnMapReader(41,41);
		mymap.Load("./bin/after_mask5.txt");
		mymap.FindCodewordsFromMap(num_of_blocks_6H, dc_per_block_6H,0,0);
		//mymap.DebugPrint_mybyte();
		//mymap.DebugPrint_codewords();
		ArrayList<Integer> datacodewords = mymap.Get_datacodewords();
		ArrayList<Integer> deinterleaved_datacodewords_01_thru_15 = new ArrayList<Integer>();
		ArrayList<Integer> deinterleaved_datacodewords_16_thru_30 = new ArrayList<Integer>();
		ArrayList<Integer> deinterleaved_datacodewords_31_thru_45 = new ArrayList<Integer>();
		ArrayList<Integer> deinterleaved_datacodewords_46_thru_60 = new ArrayList<Integer>();

		for (int ii=0; ii < datacodewords.size(); ii += 4)
		{
			deinterleaved_datacodewords_01_thru_15.add(datacodewords.get(ii));
			deinterleaved_datacodewords_16_thru_30.add(datacodewords.get(ii+1));
			deinterleaved_datacodewords_31_thru_45.add(datacodewords.get(ii+2));
			deinterleaved_datacodewords_46_thru_60.add(datacodewords.get(ii+3));
		}
		
		/*****
		DataCodewordsDecoder dec = new DataCodewordsDecoder();
		dec.Decode_Correct_Sequential_Codewords(deinterleaved_datacodewords_01_thru_15,
				                                deinterleaved_datacodewords_16_thru_30,
				                                deinterleaved_datacodewords_31_thru_45,
				                                deinterleaved_datacodewords_46_thru_60);
		*****/
		
		ErrorCorrectionCodewordsGeneration ecc1 = new ErrorCorrectionCodewordsGeneration(deinterleaved_datacodewords_01_thru_15, ecc_per_block_6H);
		ErrorCorrectionCodewordsGeneration ecc2 = new ErrorCorrectionCodewordsGeneration(deinterleaved_datacodewords_16_thru_30, ecc_per_block_6H);
		ErrorCorrectionCodewordsGeneration ecc3 = new ErrorCorrectionCodewordsGeneration(deinterleaved_datacodewords_31_thru_45, ecc_per_block_6H);
		ErrorCorrectionCodewordsGeneration ecc4 = new ErrorCorrectionCodewordsGeneration(deinterleaved_datacodewords_46_thru_60, ecc_per_block_6H);
		
		ArrayList<Integer> ecc_block1 = ecc1.Get_ECC_Decimal();
		ArrayList<Integer> ecc_block2 = ecc2.Get_ECC_Decimal();
		ArrayList<Integer> ecc_block3 = ecc3.Get_ECC_Decimal();
		ArrayList<Integer> ecc_block4 = ecc4.Get_ECC_Decimal();
		
		ArrayList<Integer> barefootbar = new ArrayList<Integer>();
		barefootbar.addAll(datacodewords);
		barefootbar.addAll(ecc_block1);
		barefootbar.addAll(ecc_block2);
		barefootbar.addAll(ecc_block3);
		barefootbar.addAll(ecc_block4);
		RowColumnMapWriter mapwriter = new RowColumnMapWriter("./bin/ver6-blank.txt", 41, 41);
		try {
		     mapwriter.FillMap(barefootbar);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		mapwriter.DumpMap("cow.txt");
		
		/*******************************************
		//"HELLO WORLD", 1-M ALPHANUMERIC, 16 data-codewords, 10 error-correction-codewords 
		String[] msg_polynomial = { "00100000", "01011011", "00001011", "01111000", 
				                    "11010001", "01110010", "11011100", "01001101", 
				                    "01000011", "01000000", "11101100", "00010001",
				                    "11101100", "00010001", "11101100", "00010001"  };
		ArrayList<Integer> msg_poly_coeffs = new ArrayList<Integer>();
		
		System.out.println();
		for (int ii=0; ii < msg_polynomial.length; ii++)
		{
		     int temp = ConvertBinaryByteStringToPositiveInteger(msg_polynomial[ii]);
		     msg_poly_coeffs.add(temp);
		     System.out.print(" " + temp);
		}
		System.out.println();
		ErrorCorrectionCodewordsGeneration ecc = new ErrorCorrectionCodewordsGeneration(msg_poly_coeffs, 10);
		*************************************************/

		System.out.println();
		System.out.println("DONE");
    }//main
}//class

 