import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class QR_Decode  { 
	
	
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
	
		
	public static void main(String s[]) throws Exception {
		try {
			//Example_BarefootBar ex1 = new Example_BarefootBar();
			//ex1.DoExample();
			
			Example_TRex ex2 = new Example_TRex();
			ex2.DoExample();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		System.out.println();
		System.out.println("DONE");
    }//main
}//class

 