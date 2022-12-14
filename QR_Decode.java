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
	//  Ok tried what wikipedia said was mask5 and mask0
	
	
	public static void Write_Wikipedia_Mask_7() {
		try {
			FileWriter myWriter = new FileWriter("mask_7.txt");
			for (int ii=0; ii <= 40; ii++) {
			    for (int jj=0; jj <= 40; jj++) {
					int answer = (ii*jj)%2+(ii*jj)%3;
					if (answer == 0)
						myWriter.write("1");
					else
						myWriter.write("0");
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
						myWriter.write("1");
					else
						myWriter.write("0");
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
		try {
			FileWriter myWriter = new FileWriter("mask_5.txt");
			for (int ii=0; ii <= 40; ii++) {
			    for (int jj=0; jj <= 40; jj++) {
					int answer = ((ii*jj)%3+ii+jj)%2;
					if (answer == 0)
						myWriter.write("1");
					else
						myWriter.write("0");
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
						myWriter.write("1");
					else
						myWriter.write("0");
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
						myWriter.write("1");
					else
						myWriter.write("0");
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
						myWriter.write("1");
					else
						myWriter.write("0");
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
						myWriter.write("1");
					else
						myWriter.write("0");
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
						myWriter.write("1");
					else
						myWriter.write("0");
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
					
					if (maskChar == '0')
						myWriter.write(inputChar);
					else if (maskChar == '1') {
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
	}
		
	public static void main(String s[]) {
		//20221025_104402.jpg original image
		//original_map.txt created by eyeball-and-hand, no code yet
		//original_map_2.txt created by eyeball-and-hand, no code yet
		Write_Wikipedia_Mask_5();
		boolean success = Do_Mask("original_map_2.txt", "mask_5.txt", "after_mask5.txt");
		
		//After using javascript debugger in chromewebbrowser of good working code
		RowColumnMap mymap = new RowColumnMap(41,41);
		mymap.Load("after_mask5.txt");
		mymap.GetCodewordsFromMap();
    }//main
}//class

 