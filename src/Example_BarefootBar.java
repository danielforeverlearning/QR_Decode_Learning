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
                Tools tool = new Tools();
                
                //encoding == ENCODING_INDICATOR.BYTE (4-bits)
                //length == 58 (8-bits)
                //d.c (each 8-bits, so not on byte-boundary)
                //add at end
                //encoding == ENCODING_INDICATOR.TERMINATOR == 0000 (4-bits)
                //pad to 8-bits-byte
                //pad 236 and 17 as bytes till max length
                
		DataCodewordsDecoder dec = new DataCodewordsDecoder();
		dec.Decode_Correct_Sequential_Codewords("./Example_BarefootBar/20221025_104402_codewords.txt");
                ArrayList<Integer> correct_sequential = dec.Get_save_byteencdata_as_integer_NO_ENC_IND_NO_LENGTH();
                //correct_sequential has 58 length

                ArrayList<Integer> correct_block1_intonly = new ArrayList<Integer>();
                ArrayList<Integer> correct_block2_intonly = new ArrayList<Integer>();
                ArrayList<Integer> correct_block3_intonly = new ArrayList<Integer>();
                ArrayList<Integer> correct_block4_intonly = new ArrayList<Integer>();
                for (int ii=0;  ii < 15; ii++)
                    correct_block1_intonly.add(correct_sequential.get(ii));
                for (int ii=15; ii < 30; ii++)
                    correct_block2_intonly.add(correct_sequential.get(ii));
                for (int ii=30; ii < 45; ii++)
                    correct_block3_intonly.add(correct_sequential.get(ii));
                for (int ii=45; ii < 58; ii++)
                    correct_block4_intonly.add(correct_sequential.get(ii));
                
                tool.ArrayListTo_Integer_ASCII_TextFile("./Example_BarefootBar/correct_block1_intonly.txt", correct_block1_intonly);
                tool.ArrayListTo_Integer_ASCII_TextFile("./Example_BarefootBar/correct_block2_intonly.txt", correct_block2_intonly);
                tool.ArrayListTo_Integer_ASCII_TextFile("./Example_BarefootBar/correct_block3_intonly.txt", correct_block3_intonly);
                tool.ArrayListTo_Integer_ASCII_TextFile("./Example_BarefootBar/correct_block4_intonly.txt", correct_block4_intonly);
                
                //****************************************************
                //Verified:
                //correct_block1_intonly
                //correct_block2_intonly
                //correct_block3_intonly
                //correct_block4_intonly
                //****************************************************
                
                ArrayList<Integer> correct_dc_sequence_with_byteencind = tool.Add_ByteEncIndAndLength(correct_block1_intonly,
                                                                                                      correct_block2_intonly,
                                                                                                      correct_block3_intonly,
                                                                                                      correct_block4_intonly,
                                                                                                      60);
                tool.ArrayListToIntegerTextFile("./Example_BarefootBar/correct_dc_sequence_with_byteencind.txt", correct_dc_sequence_with_byteencind);
                
                //****************************************************
                //Verified:
                //correct_dc_sequence_with_byteencind
                //****************************************************
                
                ArrayList<Integer> correct_dc_block1 = new ArrayList<Integer>();
                ArrayList<Integer> correct_dc_block2 = new ArrayList<Integer>();
                ArrayList<Integer> correct_dc_block3 = new ArrayList<Integer>();
                ArrayList<Integer> correct_dc_block4 = new ArrayList<Integer>();
                for (int ii=0; ii < 15; ii++)
                    correct_dc_block1.add(correct_dc_sequence_with_byteencind.get(ii));
                for (int ii=15; ii < 30; ii++)
                    correct_dc_block2.add(correct_dc_sequence_with_byteencind.get(ii));
                for (int ii=30; ii < 45; ii++)
                    correct_dc_block3.add(correct_dc_sequence_with_byteencind.get(ii));
                for (int ii=45; ii < 60; ii++)
                    correct_dc_block4.add(correct_dc_sequence_with_byteencind.get(ii));
                
                ArrayList<Integer> correct_dc_interleaved = new ArrayList<Integer>();
                for (int ii=0; ii < 15; ii++)
                {
                    correct_dc_interleaved.add(correct_dc_block1.get(ii));
                    correct_dc_interleaved.add(correct_dc_block2.get(ii));
                    correct_dc_interleaved.add(correct_dc_block3.get(ii));
                    correct_dc_interleaved.add(correct_dc_block4.get(ii));
                }
                tool.ArrayListToIntegerTextFile("./Example_BarefootBar/correct_dc_interleaved.txt", correct_dc_interleaved);
                
                System.out.println("***** TEST RowColumnMapWriter *****");
                RowColumnMapWriter test_dc_only_mapwriter = new RowColumnMapWriter("./Example_BarefootBar/ver6-blank.txt", 41, 41);
		try {
                    //DO NOT ADD EC-codewords they will be 'E' in the map
                    test_dc_only_mapwriter.FillMap(correct_dc_interleaved);
		}
		catch (Exception ex) {
                    ex.printStackTrace();
		}
                //DO NOT ADD EC-codewords they will be 'E' in the map
                test_dc_only_mapwriter.DumpMap("./Example_BarefootBar/test_dc_only_mapwriter.txt");
                
                RowColumnMapReader test_dc_only_mapread = new RowColumnMapReader(41,41);
                test_dc_only_mapread.Load("./Example_BarefootBar/test_dc_only_mapwriter.txt");
                test_dc_only_mapread.Find_Codewords(60);
                ArrayList<Integer> test_dc_interleaved = test_dc_only_mapread.Get_codewords();
                tool.ArrayListToIntegerTextFile("./Example_BarefootBar/test_dc_interleaved.txt", test_dc_interleaved);
                
                //****************************************************
                //Verified:
                //correct_dc_interleaved == test_dc_interleaved
                //****************************************************
                
                ArrayList<Integer> test_dc_block1 = new ArrayList<Integer>();
                ArrayList<Integer> test_dc_block2 = new ArrayList<Integer>();
                ArrayList<Integer> test_dc_block3 = new ArrayList<Integer>();
                ArrayList<Integer> test_dc_block4 = new ArrayList<Integer>();
                for (int ii=0; ii < test_dc_interleaved.size(); ii += 4)
                {
                    test_dc_block1.add(test_dc_interleaved.get(ii));
                    test_dc_block2.add(test_dc_interleaved.get(ii+1));
                    test_dc_block3.add(test_dc_interleaved.get(ii+2));
                    test_dc_block4.add(test_dc_interleaved.get(ii+3));
                }

                System.out.println("***** test_dc_block1thru4_dec.Decode_Correct_Sequential_Codewords *****");
                DataCodewordsDecoder test_dc_block1thru4_dec = new DataCodewordsDecoder();
                test_dc_block1thru4_dec.Decode_Correct_Sequential_Codewords(test_dc_block1, 
                                                                            test_dc_block2, 
                                                                            test_dc_block3, 
                                                                            test_dc_block4);
                //****************************************************
                //Verified:
                //correct_dc_block1 == test_dc_block1
                //correct_dc_block2 == test_dc_block2
                //correct_dc_block3 == test_dc_block3
                //correct_dc_block4 == test_dc_block4
                //****************************************************
                
                ErrorCorrectionCodewordsGeneration eccgen1  = new ErrorCorrectionCodewordsGeneration(correct_dc_block1, 28);
                ArrayList<Integer> ecc_block1 = eccgen1.Get_ECC_Decimal();
                System.out.println("ecc_block1.size == " + ecc_block1.size());
                
                ErrorCorrectionCodewordsGeneration eccgen2  = new ErrorCorrectionCodewordsGeneration(correct_dc_block2, 28);
                ArrayList<Integer> ecc_block2 = eccgen2.Get_ECC_Decimal();
                System.out.println("ecc_block2.size == " + ecc_block2.size());
                
                ErrorCorrectionCodewordsGeneration eccgen3  = new ErrorCorrectionCodewordsGeneration(correct_dc_block3, 28);
                ArrayList<Integer> ecc_block3 = eccgen3.Get_ECC_Decimal();
                System.out.println("ecc_block3.size == " + ecc_block3.size());
                
                ErrorCorrectionCodewordsGeneration eccgen4  = new ErrorCorrectionCodewordsGeneration(correct_dc_block4, 28);
                ArrayList<Integer> ecc_block4 = eccgen4.Get_ECC_Decimal();
                System.out.println("ecc_block4.size == " + ecc_block4.size());
                
                ArrayList<Integer> ecc_interleaved = new ArrayList<Integer>();
                for (int ii=0; ii < 28; ii++)
                {
                    ecc_interleaved.add(ecc_block1.get(ii));
                    ecc_interleaved.add(ecc_block2.get(ii));
                    ecc_interleaved.add(ecc_block3.get(ii));
                    ecc_interleaved.add(ecc_block4.get(ii));
                }
                
                ArrayList<Integer> all_codewords = new ArrayList<Integer>();
                all_codewords.addAll(correct_dc_interleaved);
                all_codewords.addAll(ecc_interleaved);
                
                System.out.println("***** all_codewords_mapwriter *****");
                RowColumnMapWriter all_codewords_mapwriter = new RowColumnMapWriter("./Example_BarefootBar/ver6-blank.txt", 41, 41);
		try {
                    all_codewords_mapwriter.FillMap(all_codewords);
		}
		catch (Exception ex) {
                    ex.printStackTrace();
		}
                //Last 7 bits are still 'E' because it is not part of codewords
                all_codewords_mapwriter.Change_Remaining_E_To_0();
                all_codewords_mapwriter.DumpMap("./Example_BarefootBar/all_codewords_mapwriter.txt");
                
                //****************************************************
                //Verified:
                //after_mask5.txt == all_codewords_mapwriter.txt + branding_area_filled_in
                //****************************************************
                
                
                Masking mask = new Masking();
		boolean success = mask.Do_Mask("./Example_BarefootBar/all_codewords_mapwriter.txt", "./mask_5.txt", "./Example_BarefootBar/final_map.txt");
		if (!success)
		{
			System.out.println("ERROR");
			return;
		}
                
                
                /**********************************************************************
		QRcode-ver6H
                
                For QR code version 6, error correction level H, 
                the code has n = 28 total error-correction-codewords and 
                k = 15 data-codewords per block. 
                A Reed-Solomon code can correct up to 
                t = floor((n-k)/2) errors, 
                so in this case,
                t = floor((28-15)/2) = floor(6.5) = 6 errors per block.
                ***********************************************************************/
                
                //Code below only works for maximum-1-byte-error-correction
                
		Integer[] correct_block_codewords = 
			{ 67, 166, 135, 71, 71, 7, 51, 162, 242, 247, 119, 119, 114, 230, 134, //data codewords
			  1, 237, 236, 157, 0, 147, 103, 21, 108, 39, 188, 98, 145, 180, //error-correction codewords 
			  116, 192, 73, 140, 225, 5, 42, 103, 242, 71, 137, 132, 201, 134 };
		
                Boolean non_zero_syndrome_found = false;
                int max_root_alpha_exp = 28 - 1;
                ErrorCorrection err_corrector = new ErrorCorrection();
		for (int ii=0; ii <= max_root_alpha_exp; ii++)
		{
		     int syndrome = err_corrector.CalculateSyndrome(correct_block_codewords, ii);
		     if (syndrome != 0)
		    	 non_zero_syndrome_found = true;
		}
		System.out.println("correct_block_codewords: non_zero_syndrome_found = " + non_zero_syndrome_found);
		
                
                
                /******************************************
		just change 1 byte
		*******************************************/
                
		Integer[] received_block_codewords = 
			{ 67, 166, 135, 71, 71, 7, 51, 162, 242, 247, 119, 119, 0, 230, 134,
			  1, 237, 236, 157, 0, 147, 103, 21, 108, 39, 188, 98, 145, 180, 
			  116, 192, 73, 140, 225, 5, 42, 103, 242, 71, 137, 132, 201, 134 };
		     
		ArrayList<Integer> nonzero_syndrome = new ArrayList<Integer>(); 
		for (int ii=0; ii <= max_root_alpha_exp; ii++)
		{
		     int syndrome = err_corrector.CalculateSyndrome(received_block_codewords, ii);
		     if (syndrome != 0)
		    	 nonzero_syndrome.add(syndrome);
		}
		System.out.println("received_block_codewords: nonzero_syndrome.size = " + nonzero_syndrome.size());
        
		//now we know what we recv is corrupt but not how corrupt
		//try to find which byte/bytes is/are bad

                if (nonzero_syndrome.size() > 0)
                {
                    ArrayList<ArrayList<Integer>> unk_results = err_corrector.BuildUnknownResults(received_block_codewords, max_root_alpha_exp);
                    err_corrector.DebugPrint_UnknownResults(unk_results);

                    ArrayList<String>  correctable_byte_arr = err_corrector.FindCorrectableByteLocations(unk_results, max_root_alpha_exp);
                    System.out.println("correctable_byte_arr.size == " + correctable_byte_arr.size());
                    if (correctable_byte_arr.size() > 0)
                        System.out.println("correctable_byte_arr[0] == "   + correctable_byte_arr.get(0));

                    if (correctable_byte_arr.size() > 0)
                    {
                        ArrayList<String> corrections = err_corrector.CorrectCorrectableBytes(correctable_byte_arr, max_root_alpha_exp, received_block_codewords.length);
                        err_corrector.DebugPrint_Corrections(corrections);
                    }
                }
                
                /****************************************
                 just change 2 bytes from correct-array
                 ****************************************/
                System.out.println("***** just change 2 bytes *****");
                
                Integer[] received_2byte_errors_codewords = 
			{ 67, 166, 135, 71, 71, 7, 50, 162, 242, 247, 119, 119, 0, 230, 134,
			  1, 237, 236, 157, 0, 147, 103, 21, 108, 39, 188, 98, 145, 180, 
			  116, 192, 73, 140, 225, 5, 42, 103, 242, 71, 137, 132, 201, 134 };
                
                nonzero_syndrome.clear();
		for (int ii=0; ii <= max_root_alpha_exp; ii++)
		{
		     int syndrome = err_corrector.CalculateSyndrome(received_2byte_errors_codewords, ii);
		     if (syndrome != 0)
		    	 nonzero_syndrome.add(syndrome);
		}
		System.out.println("received_2byte_errors_codewords: nonzero_syndrome.size = " + nonzero_syndrome.size());
                if (nonzero_syndrome.size() > 0)
                {
                    BruteForceMaximumByteErrorDetectionCorrection2 myobj = new BruteForceMaximumByteErrorDetectionCorrection2(received_2byte_errors_codewords, max_root_alpha_exp);
                    myobj.Process();
                    System.out.println("Process2: DONE");
                }
                
                /****************************************
                 just change 3 bytes from correct-array
                 check timing about failure of
                 class BruteForceMaximumByteErrorDetectionCorrection2
                 function Process
                 ****************************************/
                System.out.println("***** just change 3 bytes *****");
                Integer[] received_3byte_errors_codewords = 
			{ 67, 166, 135, 71, 71, 7, 50, 162, 242, 247, 119, 119, 0, 230, 134,
			  1, 237, 236, 157, 0, 147, 103, 21, 108, 39, 188, 98, 145, 180, 
			  116, 192, 0, 140, 225, 5, 42, 103, 242, 71, 137, 132, 201, 134 };
                BruteForceMaximumByteErrorDetectionCorrection2 failobj = new BruteForceMaximumByteErrorDetectionCorrection2(received_3byte_errors_codewords, max_root_alpha_exp);
                failobj.Process();
                System.out.println("BruteForceMaximumByteErrorDetectionCorrection2 Process failure DONE");
                
                BruteForceMaximumByteErrorDetectionCorrection3 myobj3 = new BruteForceMaximumByteErrorDetectionCorrection3(received_3byte_errors_codewords, max_root_alpha_exp);
                myobj3.Process();
                System.out.println("Process3: DONE");
                //Dang that took a long-ass time like 1 minute per index1
                //Need a farm or something
                
 /*****               
                //****************************************************************************
                //lets try to correct for real because it has bad branding area
                //*****************************************************************************
                System.out.println("TRYING TO CORRECT BAD BRANDING AREA CORRECTLY:");
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
		success = mask.Do_Mask("./Example_BarefootBar/original_map_3_branding_area_eyeballs_filled.txt", "./mask_5.txt", "./Example_BarefootBar/after_mask5_bad_branding_area.txt");
		if (!success)
		{
			System.out.println("ERROR");
			return;
		}
		
		RowColumnMapReader mymap = new RowColumnMapReader(41,41);
		mymap.Load("./Example_BarefootBar/after_mask5_bad_branding_area.txt");
		mymap.Find_Codewords(172);
		//mymap.DebugPrint_mybyte();
		//mymap.DebugPrint_codewords();
		
		ArrayList<Integer> interleaved = mymap.Get_codewords();
		ArrayList<Integer> brand_dc_block1 = new ArrayList<Integer>();
		ArrayList<Integer> brand_dc_block2 = new ArrayList<Integer>();
		ArrayList<Integer> brand_dc_block3 = new ArrayList<Integer>();
		ArrayList<Integer> brand_dc_block4 = new ArrayList<Integer>();

		for (int ii=0; ii < 60; ii += 4)
		{
			brand_dc_block1.add(interleaved.get(ii));
			brand_dc_block2.add(interleaved.get(ii+1));
			brand_dc_block3.add(interleaved.get(ii+2));
			brand_dc_block4.add(interleaved.get(ii+3));
		}
		
		ArrayList<Integer> brand_ecc_block1 = new ArrayList<Integer>();
		ArrayList<Integer> brand_ecc_block2 = new ArrayList<Integer>();
		ArrayList<Integer> brand_ecc_block3 = new ArrayList<Integer>();
		ArrayList<Integer> brand_ecc_block4 = new ArrayList<Integer>();
                
                for (int ii=60; ii < 172; ii += 4)
		{
			brand_ecc_block1.add(interleaved.get(ii));
			brand_ecc_block2.add(interleaved.get(ii+1));
			brand_ecc_block3.add(interleaved.get(ii+2));
			brand_ecc_block4.add(interleaved.get(ii+3));
		}
		
                //*****************************
                // data block1 and ecc_block1
                //*****************************
                ArrayList<Integer> recv_block1_al = new ArrayList<Integer>();
                recv_block1_al.addAll(brand_dc_block1);
                recv_block1_al.addAll(brand_ecc_block1);
                Integer[] recv_block1 = new Integer[recv_block1_al.size()];
                recv_block1_al.toArray(recv_block1);
                ArrayList<Integer> recv_block1_nonzero_syndrome = new ArrayList<Integer>();
                ArrayList<Integer> recv_block1_zero_syndrome = new ArrayList<Integer>();
		for (int ii=0; ii <= max_root_alpha_exp; ii++)
		{
		     int syndrome = err_corrector.CalculateSyndrome(recv_block1, ii);
		     if (syndrome != 0)
		    	 recv_block1_nonzero_syndrome.add(syndrome);
                     else {
                         System.out.println("recv_block1: syndrome==0 for ii==" + ii);
                         recv_block1_zero_syndrome.add(ii);
                     }
		}
		System.out.println("recv_block1_nonzero_syndrome.size = " + recv_block1_nonzero_syndrome.size());
                System.out.println("recv_block1_zero_syndrome.size = " + recv_block1_zero_syndrome.size());
                
                ArrayList<ArrayList<Integer>> unk_results_block1 = err_corrector.BuildUnknownResults(recv_block1, max_root_alpha_exp);
		err_corrector.DebugPrint_UnknownResults(unk_results_block1);
                
                
                //*****************************
                // data block2 and ecc_block2
                //*******************************
                ArrayList<Integer> recv_block2_al = new ArrayList<Integer>();
                recv_block2_al.addAll(brand_dc_block2);
                recv_block2_al.addAll(brand_ecc_block2);
                Integer[] recv_block2 = new Integer[recv_block2_al.size()];
                recv_block2_al.toArray(recv_block2);
                ArrayList<Integer> recv_block2_nonzero_syndrome = new ArrayList<Integer>();
                ArrayList<Integer> recv_block2_zero_syndrome = new ArrayList<Integer>();
		for (int ii=0; ii <= max_root_alpha_exp; ii++)
		{
		     int syndrome = err_corrector.CalculateSyndrome(recv_block2, ii);
		     if (syndrome != 0)
		    	 recv_block2_nonzero_syndrome.add(syndrome);
                     else
                         recv_block2_zero_syndrome.add(ii);
		}
		System.out.println("recv_block2_nonzero_syndrome.size = " + recv_block2_nonzero_syndrome.size());
                System.out.println("recv_block2_zero_syndrome.size = " + recv_block2_zero_syndrome.size());
                
                ArrayList<ArrayList<Integer>> unk_results_block2 = err_corrector.BuildUnknownResults(recv_block2, max_root_alpha_exp);
		err_corrector.DebugPrint_UnknownResults(unk_results_block2);
                

                //*****************************
                // data block3 and ecc_block3
                //*****************************
                ArrayList<Integer> recv_block3_al = new ArrayList<Integer>();
                recv_block3_al.addAll(brand_dc_block3);
                recv_block3_al.addAll(brand_ecc_block3);
                Integer[] recv_block3 = new Integer[recv_block3_al.size()];
                recv_block3_al.toArray(recv_block3);
                ArrayList<Integer> recv_block3_nonzero_syndrome = new ArrayList<Integer>(); 
                ArrayList<Integer> recv_block3_zero_syndrome = new ArrayList<Integer>(); 
		for (int ii=0; ii <= max_root_alpha_exp; ii++)
		{
		     int syndrome = err_corrector.CalculateSyndrome(recv_block3, ii);
		     if (syndrome != 0)
		    	 recv_block3_nonzero_syndrome.add(syndrome);
                     else
                         recv_block3_zero_syndrome.add(ii);
		}
		System.out.println("recv_block3_nonzero_syndrome.size = " + recv_block3_nonzero_syndrome.size());
                System.out.println("recv_block3_zero_syndrome.size = " + recv_block3_zero_syndrome.size());
                
                ArrayList<ArrayList<Integer>> unk_results_block3 = err_corrector.BuildUnknownResults(recv_block3, max_root_alpha_exp);
		err_corrector.DebugPrint_UnknownResults(unk_results_block3);
                
    
                //*****************************
                //data block4 and ecc_block4
                //*****************************
                ArrayList<Integer> recv_block4_al = new ArrayList<Integer>();
                recv_block4_al.addAll(brand_dc_block4);
                recv_block4_al.addAll(brand_ecc_block4);
                Integer[] recv_block4 = new Integer[recv_block4_al.size()];
                recv_block4_al.toArray(recv_block4);
                ArrayList<Integer> recv_block4_nonzero_syndrome = new ArrayList<Integer>();
                ArrayList<Integer> recv_block4_zero_syndrome = new ArrayList<Integer>();
		for (int ii=0; ii <= max_root_alpha_exp; ii++)
		{
		     int syndrome = err_corrector.CalculateSyndrome(recv_block4, ii);
		     if (syndrome != 0)
		    	 recv_block4_nonzero_syndrome.add(syndrome);
                     else
                         recv_block4_zero_syndrome.add(ii);
		}
		System.out.println("recv_block4_nonzero_syndrome.size = " + recv_block4_nonzero_syndrome.size());
                System.out.println("recv_block4_zero_syndrome.size = " + recv_block4_zero_syndrome.size());
                
                ArrayList<ArrayList<Integer>> unk_results_block4 = err_corrector.BuildUnknownResults(recv_block4, max_root_alpha_exp);
		err_corrector.DebugPrint_UnknownResults(unk_results_block4);
                
                
*****/                
	}//DoExample

}//class
