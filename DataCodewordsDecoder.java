import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;


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
	

	public boolean Decode_Correct_Sequential_Codewords(String input_filename) {
		try {
			
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
			
			inputReader.close();
			
			return true;
		}
		catch (Exception ex) {
			System.out.println("Decode_Correct_Sequential_Codewords: An exception occurred!");
			ex.printStackTrace();
			return false;
		}
	}//Decode_Correct_Sequential_Codewords
	
	
	

	public boolean Decode_Correct_Sequential_Codewords(ArrayList<Integer> block1,
			                                           ArrayList<Integer> block2,
			                                           ArrayList<Integer> block3,
			                                           ArrayList<Integer> block4)
	{
		try {
			
			ArrayList<Integer> data = new ArrayList<Integer>();
			data.addAll(block1);
			data.addAll(block2);
			data.addAll(block3);
			data.addAll(block4);
			
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
			System.out.println("Decode_Correct_Sequential_Codewords: An exception occurred!");
			ex.printStackTrace();
			return false;
		}
	}//Decode_Correct_Sequential_Codewords
	
	
	
}//class
