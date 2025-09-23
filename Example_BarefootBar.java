import java.util.ArrayList;

public class Example_BarefootBar {
	
	public Example_BarefootBar()
	{
		
	}//constructor
	
	public void DoExample() throws Exception
	{
		//20221025_104402.jpg original image
		//original_map.txt created by eyeball-and-hand, no code yet
		//original_map_2.txt created by eyeball-and-hand, no code yet
		
		//Do not need this, but just save the code
		//Change_OriginalMap_Black0("original_map_2.txt", "original_map_3_black0.txt");
		
		//These codewords we got from stepping thru online javascript code
		//https://webqr.com/
		//DataCodewordsDecoder dec = new DataCodewordsDecoder();
		//dec.Decode_Correct_Sequential_Codewords("./bin/20221025_104402_codewords.txt");
		
		Masking mask = new Masking();
		mask.Write_Wikipedia_Mask_5();
		boolean success = mask.Do_Mask("./Example_BarefootBar/original_map_2.txt", "./bin/mask_5.txt", "./Example_BarefootBar/after_mask5.txt");
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
		mymap.Load("./Example_BarefootBar/after_mask5.txt");
		mymap.FindDataCodewordsFromMap(num_of_blocks_6H, dc_per_block_6H,0,0);
		//mymap.DebugPrint_mybyte();
		//mymap.DebugPrint_codewords();
		
		ArrayList<Integer> interleaved_datacodewords = mymap.Get_datacodewords();
		ArrayList<Integer> deinterleaved_datacodewords_01_thru_15 = new ArrayList<Integer>();
		ArrayList<Integer> deinterleaved_datacodewords_16_thru_30 = new ArrayList<Integer>();
		ArrayList<Integer> deinterleaved_datacodewords_31_thru_45 = new ArrayList<Integer>();
		ArrayList<Integer> deinterleaved_datacodewords_46_thru_60 = new ArrayList<Integer>();

		for (int ii=0; ii < interleaved_datacodewords.size(); ii += 4)
		{
			deinterleaved_datacodewords_01_thru_15.add(interleaved_datacodewords.get(ii));
			deinterleaved_datacodewords_16_thru_30.add(interleaved_datacodewords.get(ii+1));
			deinterleaved_datacodewords_31_thru_45.add(interleaved_datacodewords.get(ii+2));
			deinterleaved_datacodewords_46_thru_60.add(interleaved_datacodewords.get(ii+3));
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
		
		ArrayList<Integer> interleaved_ecc = new ArrayList<Integer>();
		for (int ii=0; ii < ecc_block1.size(); ii++)
		{
			interleaved_ecc.add(ecc_block1.get(ii));
			interleaved_ecc.add(ecc_block2.get(ii));
			interleaved_ecc.add(ecc_block3.get(ii));
			interleaved_ecc.add(ecc_block4.get(ii));
		}
		int ecc_total_size = ecc_block1.size() + ecc_block2.size() + ecc_block3.size() + ecc_block4.size();
		if (ecc_total_size == interleaved_ecc.size())
		{
			ArrayList<Integer> barefootbar = new ArrayList<Integer>();
			barefootbar.addAll(interleaved_datacodewords);
			barefootbar.addAll(interleaved_ecc);
			RowColumnMapWriter mapwriter = new RowColumnMapWriter("./Example_BarefootBar/ver6-blank.txt", 41, 41);
			try {
				mapwriter.FillMap(barefootbar);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			mapwriter.DumpMap("./Example_BarefootBar/cow.txt");
			boolean theend = mask.Do_Mask("./Example_BarefootBar/cow.txt", "./bin/mask_5.txt", "./Example_BarefootBar/final.txt");
			if (!theend)
			{
				System.out.println("ERROR");
				return;
			}
		}
		else
			throw new Exception("ecc_total_size != interleaved_ecc.size, NOT FILLING MAP!!!!!");
		
		
		//lets just introduce 1 bit of 1 byte error and see if code works good
		//version 6-H
		//k == 15 d.c per block
		//n == 15 d.c per block + 28 e.c.c per block == 43 codewords per block
		//(n - k) == 28
		//Maximum number of incorrect codewords per block that can be fixed is 14.
		//
		//deinterleaved_datacodewords_01_thru_15 == 1 block of 15 d.c 
		//ecc_block1 == 1 block of 28 e.c.c
		
		System.out.println();
		System.out.println("deinterleaved_datacodewords_01_thru_15 length=" + deinterleaved_datacodewords_01_thru_15.size());
		for (int ii=0; ii < deinterleaved_datacodewords_01_thru_15.size(); ii++)
			System.out.print(deinterleaved_datacodewords_01_thru_15.get(ii) + " ");
		System.out.println();
		
		System.out.println();
		System.out.println("ecc_block1 length=" + ecc_block1.size());
		for (int ii=0; ii < ecc_block1.size(); ii++)
			System.out.print(ecc_block1.get(ii) + " ");
		System.out.println();
		
		int[] correct_block_codewords = 
			{ 67, 166, 135, 71, 71, 7, 51, 162, 242, 247, 119, 119, 114, 230, 134,
			  1, 237, 236, 157, 0, 147, 103, 21, 108, 39, 188, 98, 145, 180, 
			  116, 192, 73, 140, 225, 5, 42, 103, 242, 71, 137, 132, 201, 134 };
		
        Boolean non_zero_syndrome_found = false;
        int max_root_alpha_exp = ecc_block1.size() - 1;
		for (int ii=0; ii <= max_root_alpha_exp; ii++)
		{
		     int syndrome = ecc1.CalculateSyndrome(correct_block_codewords, ii);
		     if (syndrome != 0)
		    	 non_zero_syndrome_found = true;
		}
		System.out.println("correct_block_codewords: non_zero_syndrome_found = " + non_zero_syndrome_found);
		
		//change 2nd data byte only 1 bit to simulate 1 bit error in 1 byte of message
		//changing from 166 to 174
		
		//QRcode-ver6H
		//k == 15 d.c per block
		//n == 15 d.c per block + 28 e.c.c per block
		//(n - k) == 28
		//Maximum number of corrupted symbols we can recover is 14 per block
		
		int[] received_block_codewords = 
			{ 67, 174, 135, 71, 71, 7, 51, 162, 242, 247, 119, 119, 114, 230, 134,
			  1, 237, 236, 157, 0, 147, 103, 21, 108, 39, 188, 98, 145, 180, 
			  116, 192, 73, 140, 225, 5, 42, 103, 242, 71, 137, 132, 201, 134 };
		     
		max_root_alpha_exp = ecc_block1.size() - 1;
		ArrayList<Integer> nonzero_syndrome = new ArrayList<Integer>(); 
		for (int ii=0; ii <= max_root_alpha_exp; ii++)
		{
		     int syndrome = ecc1.CalculateSyndrome(received_block_codewords, ii);
		     if (syndrome != 0)
		    	 nonzero_syndrome.add(syndrome);
		}
		System.out.println("received_block_codewords: nonzero_syndrome.size = " + nonzero_syndrome.size());
        
		//now we know what we recv is corrupt but not how corrupt
		//try to find which byte/bytes is/are bad

		ArrayList<ArrayList<Integer>> unk_results = ecc1.BuildUnknownResults(received_block_codewords, max_root_alpha_exp);
		ecc1.DebugPrint_UnknownResults(unk_results);
		
		ArrayList<String>  correctable_byte_arr = ecc1.FindCorrectableByteLocations(unk_results, max_root_alpha_exp);
		System.out.println("correctable_byte_arr.size == " + correctable_byte_arr.size());
		System.out.println("correctable_byte_arr[0] == "   + correctable_byte_arr.get(0));
		
		ArrayList<String> corrections = ecc1.CorrectCorrectableBytes(correctable_byte_arr, max_root_alpha_exp, received_block_codewords.length);
		ecc1.DebugPrint_Corrections(corrections);
	}//DoExample

}//class
