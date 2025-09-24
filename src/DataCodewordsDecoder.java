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
get the number representation (from the alphanumeric table) 
of the first character and multiply it by 45. 
Then add that number to the number representation of the second character.

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
	
	char[] Alphanumeric_encoding_table = { 
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z', ' ', '$', '%', '*',
			'+', '-', '.', '/', ':' };
	
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
	
	
	
	//so far only works if 1st encoding is byte
	//and there is no 2nd encoding
	//or 2nd encoding is byte or alphanumeric
	public boolean Decode_Correct_Sequential_Codewords(String input_filename) {
		try {
			
			File inputFile        = new File(input_filename);
			Scanner inputReader   = new Scanner(inputFile);
			

			boolean getIndicator  = true;
			int length            = 0;
			int indicator_count   = 0;
			ENCODING_INDICATOR encoding     = ENCODING_INDICATOR.UNKNOWN;
			
			String startOfByte      = "";
			int startOfByteLen      = 0;
			String inputData        = "";
			
			while (true) {
				
				if (getIndicator) {
					indicator_count++;
					getIndicator = false;
					if (startOfByteLen == 0) //getIndicator on byte boundary
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
						
					    System.out.println("INDICATOR" + indicator_count + " = " + padded8str.substring(0,4) + " , " + encoding);
						if ((encoding != ENCODING_INDICATOR.BYTE) && (encoding != ENCODING_INDICATOR.ALPHANUMERIC)) {
						    System.out.println("Sorry, so far only know ENCODING_INDICATOR.BYTE or ENCODING_INDICATOR.ALPHANUMERIC, exiting.....");
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
						number = Integer.parseInt(inputData);
						binaryString = Integer.toBinaryString(number);
					    if (binaryString.length() == 8)
							padded8str = binaryString;
						else
							padded8str = padded8str.substring(0, 8 - binaryString.length()) + binaryString;
						
						if (encoding == ENCODING_INDICATOR.BYTE)
						{
							binaryString = startOfLength + padded8str.substring(0,4);
							length = tool.ConvertBinaryByteStringToPositiveInteger(binaryString);
							System.out.println("length=" + length);
							startOfByte = padded8str.substring(4,8);
							startOfByteLen = 4;
						}
						else if (encoding == ENCODING_INDICATOR.ALPHANUMERIC)
						{
							binaryString = startOfLength + padded8str.substring(0,5);
							//binaryString is 9 bits long
							length = tool.ConvertBinaryByteStringToPositiveInteger(binaryString);
							System.out.println("length=" + length);
							startOfByte = padded8str.substring(5,8);
							startOfByteLen = 3;
						}
						else
						{
							System.out.println("Should never get here");
						    inputReader.close();
						    return false;
						}
					}//getIndicator on byte boundary
					else //getIndicator startOfByte has partial byte
					{
						if (startOfByteLen == 4) {
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
							if ((encoding != ENCODING_INDICATOR.BYTE) && (encoding != ENCODING_INDICATOR.ALPHANUMERIC)) {
							    System.out.println("Sorry, so far only know ENCODING_INDICATOR.BYTE or ENCODING_INDICATOR.ALPHANUMERIC, exiting.....");
							    inputReader.close();
							    return false;						
							}
							
							//assuming on byte boundary
							//getting length
							//ver 1 thru 9, byte encoding 8bits, alphanumeric 9bits
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
		                    String binaryString = Integer.toBinaryString(number);
						    if (binaryString.length() == 8)
								padded8str = binaryString;
							else
								padded8str = padded8str.substring(0, 8 - binaryString.length()) + binaryString;
						    
						    if (encoding == ENCODING_INDICATOR.BYTE)
							{
								length = tool.ConvertBinaryByteStringToPositiveInteger(padded8str);
								System.out.println("length=" + length);
								startOfByteLen = 0;
								startOfByte = "";
							}
							else if (encoding == ENCODING_INDICATOR.ALPHANUMERIC)
							{
								//need 1 more byte and take 1 bit
								String startOfLength = padded8str;
								padded8str = "00000000";
								try {
									inputData  = inputReader.nextLine();
								}
								catch (NoSuchElementException ex) {
									//EOF reached
									inputReader.close();
									break;
								}
							    number = Integer.parseInt(inputData);
			                    binaryString = Integer.toBinaryString(number);
							    if (binaryString.length() == 8)
									padded8str = binaryString;
								else
									padded8str = padded8str.substring(0, 8 - binaryString.length()) + binaryString;
							    startOfLength += padded8str.substring(0,1);
							    length = tool.ConvertBinaryByteStringToPositiveInteger(startOfLength);
								System.out.println("length=" + length);
								startOfByte = padded8str.substring(1,8);
								startOfByteLen = 7;
							}
						}//if (startOfByteLen == 4)
						else {
							System.out.println("did not code for startOfByteLen !=4 getting indicator");
							inputReader.close();
							return false;
						}
					}//getIndicator startOfByte has partial byte
				}//getIndicator
				else {
				    if (length > 0) {
				    	if (encoding == ENCODING_INDICATOR.BYTE)
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
							startOfByteLen = 4;
				    	}
				    	else if (encoding == ENCODING_INDICATOR.ALPHANUMERIC)
						{
				    		if (length >= 2) //pair of alphanumchars
				    		{
				    			int need_more_bits_len = 11 - startOfByteLen;
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
		                        String binaryString = Integer.toBinaryString(number);
						        if (binaryString.length() == 8)
								    padded8str = binaryString;
							    else
								    padded8str = padded8str.substring(0, 8 - binaryString.length()) + binaryString;
						
						        String str11bits = startOfByte + padded8str.substring(0, need_more_bits_len);
						        int twocharint = tool.ConvertBinaryByteStringToPositiveInteger(str11bits);
				    			
				    		}//pair of alphanumchars
				    		else
				    		{
				    			
				    		}//last alphanumchar
						}
						length--;
					}//length > 0
					else {
						getIndicator = true;
					}//length == 0
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
