
public class Tools {
	
	public Tools() {
		
	}//constructor
	
	public int ConvertBinaryByteStringToPositiveInteger(String binaryByteString) {
		int[] two_pow = {128, 64, 32, 16, 8, 4, 2, 1};
		int answer = 0;
		for (int ii=0; ii <= 7; ii++) {
			char mychar = binaryByteString.charAt(ii);
			if (mychar == '1')
				answer += two_pow[ii];
		}
        return answer;		
	}//ConvertBinaryByteStringToPositiveInteger

}//class

