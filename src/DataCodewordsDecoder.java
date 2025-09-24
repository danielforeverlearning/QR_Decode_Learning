import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/***********************************************

length field after encoding-mode 4 bits
----------------------------------------
The following lists contain the sizes of the 
character count indicators for each mode and version. 
For example, if encoding HELLO WORLD in a version 1 QR code 
in alphanumeric mode, the character count indicator must be 9 bits long. 
The character count of HELLO WORLD is 11. 
In binary, 11 is 1011. 
Pad it on the left to make it 9 bits long: 000001011. 
Put this after the mode indicator from step 3 to 
get the following bit string: 0010 000001011

Versions 1 through 9
Numeric mode: 10 bits
Alphanumeric mode: 9 bits
Byte mode: 8 bits
Japanese mode: 8 bits

Versions 10 through 26
Numeric mode: 12 bits
Alphanumeric mode: 11 bits
Byte mode: 16
Japanese mode: 10 bits

Versions 27 through 40
Numeric mode: 14 bits
Alphanumeric mode: 13 bits
Byte mode: 16 bits
Japanese mode: 12 bits

Alphanumeric encoding table
------------------------------
0 0
1 1
2 2
3 3
4 4
5 5
6 6
7 7
8 8
9 9
A 10
B 11
C 12
D 13
E 14
F 15
G 16
H 17
I 18
J 19
K 20
L 21
M 22
N 23
O 24
P 25
Q 26
R 27
S 28
T 29
U 30
V 31
W 32
X 33
Y 34
Z 35
  36 (space)
$ 37
% 38
* 39
+ 40
- 41
. 42
/ 43
: 44

Alphanumeric Mode Encoding:
For alphanumeric mode encoding, 
I will use the example input of HELLO WORLD. 
For this example, I will use version 1. 
Remember, alphanumeric mode can only encode uppercase letters, 
not lowercase. Refer to the alphanumeric table 
for a list of the characters that can be encoded in alphanumeric mode.

(1)Break up into pairs
First, break up the string into pairs of characters: HE, LL, O , WO, RL, D

(2)Create a binary number for each pair
For alphanumeric mode, 
each alphanumeric character is represented by a number. 
Please refer to the alphanumeric table to find these numbers. 
The column on the left shows the alphanumeric character, 
and the column on the right shows the number that represents it.
For each pair of characters, 
get the number representation (from the alphanumeric table) of the first character 
and multiply it by 45. Then add that number to the number representation of the second character.

For example, the first pair in HELLO WORLD is HE.

H → 17
E → 14

Following the steps in the previous paragraph, 
multiply the first number by 45, then add that to the second number:
(45 * 17) + 14 = 779

Now convert that number into an 11-bit binary string, 
padding on the left with 0s if necessary.

779 → 01100001011

If you are encoding an odd number of characters, 
as we are here, take the numeric representation 
of the final character and convert it into a 6-bit binary string.

Next: Finish the Data Encoding Step
Follow the instructions on the data encoding page to add any remaining bits as necessary.
************************************************/


public class DataCodewordsDecoder {
	
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
	
	private Tools tool;
	
	public DataCodewordsDecoder()
	{
		tool = new Tools();
	}//constructor
	
	
	public boolean DecodeCorrectSequentialCodewordsDespiteLengthField(String input_filename) {
		File inputFile        = null;
		Scanner inputReader   = null;
		int ii=0;
		try {
			inputFile        = new File(input_filename);
			inputReader      = new Scanner(inputFile);
			boolean getIndicator  = true;
			boolean EOF_reached   = false;
			int length            = 0;
			ENCODING_INDICATOR encoding = ENCODING_INDICATOR.UNKNOWN;
			
			String startOfByte    = "";
			
			while (EOF_reached == false) {
				
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
                        return false;						
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
					length = tool.ConvertBinaryByteStringToPositiveInteger(binaryString);
					System.out.println("length=" + length);
					
					startOfByte = padded8str.substring(4,8);
					
					System.out.println("startOfByte=" + startOfByte);
					
					getIndicator = false;
				} 
				else {
				    while (true) { //ignore length field go till EOF
						String padded8str = "00000000";
				        String inputData  = "";
				        try {
				        	inputData = inputReader.nextLine();
				        	ii++;
				        }
				        catch (NoSuchElementException ex) {
							//EOF reached
							inputReader.close();
							EOF_reached = true;
							break;
						}
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
						int ascii_decimal_int = tool.ConvertBinaryByteStringToPositiveInteger(mybyte);
						char character = (char) ascii_decimal_int;
						
						System.out.println(ii + ": ascii_decimal_int=" + ascii_decimal_int + " = " + character);
						if (ascii_decimal_int == 32) {
							System.out.println("SPACE FOUND ASSUME NOT 1 BYTE ASCII ENCODING ANYMORE");
							System.out.println("FOR TREX 21 BYTES AFTER SPACE WHICH IS / AND -hjahalloween");
							System.out.println("BUT 60B0D49A5A923A1F58-58383001 IS 27 BYTES LONG");
						}
						startOfByte = padded8str.substring(4,8);
					}//ignore length field go till EOF
				}//getIndicator==false
			}//while
		}
		catch (Exception ex) {
			System.out.println("DecodeCorrectSequentialCodewordsDespiteLengthField: An exception occurred!");
			ex.printStackTrace();
			return false;
		}
		return true;
	}//DecodeCorrectSequentialCodewordsDespiteLengthField
	

	public boolean Decode_Correct_Sequential_Codewords(String input_filename) {
		try {
			
			File inputFile        = new File(input_filename);
			Scanner inputReader   = new Scanner(inputFile);
			

			boolean getIndicator  = true;
			int length            = 0;
			int indicator_count   = 0;
			ENCODING_INDICATOR encoding = ENCODING_INDICATOR.UNKNOWN;
			boolean need_to_use_startOfByte = false;
			
			String startOfByte    = "";
			String inputData      = "";
			
			while (true) {
				
				if (getIndicator) {
					indicator_count++;
					if (need_to_use_startOfByte == false) //on byte boundary
					{
						String padded8str = "00000000";
						
						try {
							inputData  = inputReader.nextLine();
						}
						catch (NoSuchElementException ex) {
							//EOF reached
							inputReader.close();
							break;
						}
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
						
					    System.out.println("INDICATOR" + indicator_count + " = " + padded8str.substring(0,4) + " , " + encoding);
						if ((encoding != ENCODING_INDICATOR.BYTE) && (encoding != ENCODING_INDICATOR.END_OF_MESSAGE)) {
						    System.out.println("Sorry, so far only know ENCODING_INDICATOR.BYTE or ENCODING_INDICATOR.END_OF_MESSAGE, exiting.....");
						    inputReader.close();
						    return false;						
						}
						
						//get length
						String startOfLength = padded8str.substring(4,8);
						try {
							inputData  = inputReader.nextLine();
						}
						catch (NoSuchElementException ex) {
							//EOF reached
							inputReader.close();
							break;
						}
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
						length = tool.ConvertBinaryByteStringToPositiveInteger(binaryString);
						System.out.println("length=" + length);
						
						startOfByte = padded8str.substring(4,8);
						
						System.out.println("startOfByte=" + startOfByte);
						
						getIndicator = false;
					}//on byte boundary
					else //need_to_use_startOfByte
					{
						if (startOfByte.startsWith("0001"))
							encoding = ENCODING_INDICATOR.NUMERIC;
						else if (startOfByte.startsWith("0010"))
							encoding = ENCODING_INDICATOR.ALPHANUMERIC;
						else if (startOfByte.startsWith("0100"))
							encoding = ENCODING_INDICATOR.BYTE;
						else if (startOfByte.startsWith("1000"))
							encoding = ENCODING_INDICATOR.KANJI;
						else if (startOfByte.startsWith("0011"))
							encoding = ENCODING_INDICATOR.STRUCTURED_APPEND;
						else if (startOfByte.startsWith("0111"))
							encoding = ENCODING_INDICATOR.EXTENDED_CHANNEL_INTERPRETATION;
						else if (startOfByte.startsWith("0101"))
							encoding = ENCODING_INDICATOR.FNC1_1ST_POSITION;
						else if (startOfByte.startsWith("1001"))
							encoding = ENCODING_INDICATOR.FNC1_2ND_POSITION;
						else if (startOfByte.startsWith("0000"))
							encoding = ENCODING_INDICATOR.END_OF_MESSAGE;
						else
							encoding = ENCODING_INDICATOR.UNKNOWN;
						
						System.out.println("INDICATOR" + indicator_count + " = " + encoding);
						if ((encoding != ENCODING_INDICATOR.BYTE) && (encoding != ENCODING_INDICATOR.END_OF_MESSAGE)) {
						    System.out.println("Sorry, so far only know ENCODING_INDICATOR.BYTE or ENCODING_INDICATOR.END_OF_MESSAGE, exiting.....");
						    inputReader.close();
						    return false;						
						}
						
						
					}//need_to_use_startOfByte
				} 
				else {
				    if (length > 0) {
						String padded8str = "00000000";
						try {
							inputData  = inputReader.nextLine();
						}
						catch (NoSuchElementException ex) {
							//EOF reached
							inputReader.close();
							break;
						}
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
						int ascii_decimal_int = tool.ConvertBinaryByteStringToPositiveInteger(mybyte);
						char character = (char) ascii_decimal_int;
						
						System.out.println(" ascii_decimal_int=" + ascii_decimal_int + " = " + character);
						
						startOfByte = padded8str.substring(4,8);
						length--;
					}
					else {
						getIndicator = true;
						//if last encoding was byte you need to use startOfByte
						if (encoding == ENCODING_INDICATOR.BYTE)
							need_to_use_startOfByte = true;
					}
				}
			}//while
		}//try
		catch (Exception ex) {
			System.out.println("Decode_Correct_Sequential_Codewords: An exception occurred!");
			ex.printStackTrace();
			return false;
		}
		return true;
	}//Decode_Correct_Sequential_Codewords
	

	public boolean Decode_Correct_Sequential_Codewords(ArrayList<Integer> block1,
			                                           ArrayList<Integer> block2,
			                                           ArrayList<Integer> block3,
			                                           ArrayList<Integer> block4)
	{		
			ArrayList<Integer> data = new ArrayList<Integer>();
			data.addAll(block1);
			data.addAll(block2);
			data.addAll(block3);
			data.addAll(block4);
			boolean result = decode(data);
			return result;
	}//Decode_Correct_Sequential_Codewords
	
	
	public boolean Decode_Correct_Sequential_Codewords(ArrayList<Integer> block1,
                                                       ArrayList<Integer> block2)
	{		
			ArrayList<Integer> data = new ArrayList<Integer>();
			data.addAll(block1);
			data.addAll(block2);
			boolean result = decode(data);
			return result;
	}//Decode_Correct_Sequential_Codewords
	
			
	private boolean decode(ArrayList<Integer> data)
	{
		try {
			
			int ii                = 0;
			boolean getIndicator  = true;
			int length            = 0;
			ENCODING_INDICATOR encoding = ENCODING_INDICATOR.UNKNOWN;
			
			String startOfByte    = "";
			
			while (true) {
				
				if (getIndicator) {
					
					String padded8str = "00000000";
				    int number = data.get(ii);
				    ii++;
                    String binaryString = Integer.toBinaryString(number);
				    if (binaryString.length() == 8)
						padded8str = binaryString;
					else
						padded8str = padded8str.substring(0, 8 - binaryString.length()) + binaryString;
					
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
                        return false;				
					}
					
					//get length
					String startOfLength = padded8str.substring(4,8);
					number = data.get(ii);
					ii++;
					binaryString = Integer.toBinaryString(number);
				    if (binaryString.length() == 8)
						padded8str = binaryString;
					else
						padded8str = padded8str.substring(0, 8 - binaryString.length()) + binaryString;
					binaryString = startOfLength + padded8str.substring(0,4);
					length = tool.ConvertBinaryByteStringToPositiveInteger(binaryString);
					System.out.println("length=" + length);
					
					startOfByte = padded8str.substring(4,8);
					
					getIndicator = false;
				} 
				else {
				    if (length > 0) {
						String padded8str = "00000000";
				        int number = data.get(ii);
				        ii++;
                        String binaryString = Integer.toBinaryString(number);
				        if (binaryString.length() == 8)
						    padded8str = binaryString;
					    else
						    padded8str = padded8str.substring(0, 8 - binaryString.length()) + binaryString;
						
						String mybyte = startOfByte + padded8str.substring(0,4);
						int ascii_decimal_int = tool.ConvertBinaryByteStringToPositiveInteger(mybyte);
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
			
			return true;
		}
		catch (Exception ex) {
			System.out.println("decode: An exception occurred!");
			ex.printStackTrace();
			return false;
		}
	}//decode
	
	
	
}//class
