import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Masking {
	
	public Masking()
	{
		
	}//constructor
	
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
	
	
	
	public void Write_Wikipedia_Mask_7() {
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
	
	
	public void Write_Wikipedia_Mask_6(int len) {
		try {
			FileWriter myWriter = new FileWriter("./mask6_" + len + ".txt");
			for (int ii=0; ii < len; ii++) {
			    for (int jj=0; jj < len; jj++) {
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


    public void Write_Wikipedia_Mask_5() {
		//i means invert the color
		//d means do not invert the color
		try {
			FileWriter myWriter = new FileWriter("./mask_5.txt");
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
	
	
	public void Write_Wikipedia_Mask_4() {
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
	
	
	public void Write_Wikipedia_Mask_3() {
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
	
	
	public void Write_Wikipedia_Mask_2() {
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
	
	
	public void Write_Wikipedia_Mask_1() {
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
	
	public void Write_Wikipedia_Mask_0() {
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
	
	
	public boolean Do_Mask(String input_filename, String mask_filename, String output_filename) {
		Scanner inputReader = null;
		Scanner maskReader = null;
		FileWriter myWriter = null;
		try {
			myWriter = new FileWriter(output_filename);
			
			File inputFile = new File(input_filename);
			File maskFile = new File(mask_filename);
			inputReader = new Scanner(inputFile);
			maskReader = new Scanner(maskFile);
			boolean inputNextLine = inputReader.hasNextLine();
			boolean maskNextLine = maskReader.hasNextLine();
			while (inputNextLine && maskNextLine) {
				String inputData = inputReader.nextLine();
				String maskData = maskReader.nextLine();
				
				int inputLen = inputData.length();
				int maskLen  = maskData.length();
				
				if (inputLen != maskLen) {
					System.out.println("Do_Mask: inputLen and maskLen NOT EQUAL FORCING QUIT!");
					inputReader.close();
					maskReader.close();
					myWriter.close();
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
						else if (inputChar == 'E')
							myWriter.write('T');
						else if (inputChar == 'T')
							myWriter.write('T');
						else if (inputChar == 'U')
							myWriter.write('U');
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
							System.out.println("Do_Mask: inputChar=" + inputChar);
							inputReader.close();
							maskReader.close();
							myWriter.close();
							return false;							
						}
					}
					else {
						System.out.println("Do_Mask: Unsupported character found in the mask file, FORCING QUIT");
						inputReader.close();
						maskReader.close();
						myWriter.close();
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
			inputReader.close();
			maskReader.close();
			if (myWriter != null)
			{
				try
				{
					myWriter.close();
				}
				catch (IOException ex2) {
					System.out.println("Do_Mask: IOException, failed close myWriter!");
					ex2.printStackTrace();
				}
			}
			return false;
		}
	}//Do_Mask

}//class
