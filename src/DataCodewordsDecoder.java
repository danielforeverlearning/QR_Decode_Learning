import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.NoSuchElementException;


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
			int indicator_count       = 0;
			ENCODING_INDICATOR encoding = ENCODING_INDICATOR.UNKNOWN;
			
			String startOfByte    = "";
			String inputData      = "";
			
			while (true) {
				
				if (getIndicator) {
					indicator_count++;
					if (indicator_count == 1) //on byte boundary
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
						
					    System.out.println("INDICATOR=" + padded8str.substring(0,4) + " , " + encoding);
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
					}//indicator_count==1 on byte boundary
					else //indicator_count > 1 need old startOfByte
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
						
					    System.out.println("INDICATOR=" + startOfByte.substring(0,4) + " , " + encoding);
						if ((encoding != ENCODING_INDICATOR.BYTE) && (encoding != ENCODING_INDICATOR.END_OF_MESSAGE)) {
						    System.out.println("Sorry, so far only know ENCODING_INDICATOR.BYTE or ENCODING_INDICATOR.END_OF_MESSAGE, exiting.....");
						    inputReader.close();
						    return false;						
						}
						
						
					}//indicator_count > 1 need old startOfByte
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
