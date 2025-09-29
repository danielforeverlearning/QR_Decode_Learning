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
                
                
                
                
                /*****
                System.out.println("***** TEST RowColumnMapWriter *****");
                RowColumnMapWriter testmapwriter = new RowColumnMapWriter("./Example_BarefootBar/ver6-blank.txt", 41, 41);
		try {
                    //DO NOT ADD EC-codewords they will be 'E' in the map
                    testmapwriter.FillMap(correct_interleaved_dc_only);
		}
		catch (Exception ex) {
                    ex.printStackTrace();
		}
                //DO NOT ADD EC-codewords they will be 'E' in the map
                testmapwriter.DumpMap("./Example_BarefootBar/test_map.txt");
                
                RowColumnMapReader testread = new RowColumnMapReader(41,41);
                testread.Load("./Example_BarefootBar/test_map.txt");
                testread.Find_Codewords(60);
                ArrayList<Integer> test_interleaved_array = testread.Get_codewords();
                tool.ArrayListToIntegerTextFile("./Example_BarefootBar/test_interleaved_array.txt", test_interleaved_array);

                
                ArrayList<Integer> after_mask5_block1 = new ArrayList<Integer>();
                ArrayList<Integer> after_mask5_block2 = new ArrayList<Integer>();
                ArrayList<Integer> after_mask5_block3 = new ArrayList<Integer>();
                ArrayList<Integer> after_mask5_block4 = new ArrayList<Integer>();
                for (int ii=0; ii < after_mask5_map_interleaved.size(); ii += 4)
                {
                    after_mask5_block1.add(after_mask5_map_interleaved.get(ii));
                    after_mask5_block2.add(after_mask5_map_interleaved.get(ii+1));
                    after_mask5_block3.add(after_mask5_map_interleaved.get(ii+2));
                    after_mask5_block4.add(after_mask5_map_interleaved.get(ii+3));
                }
                
                System.out.println("***** after_mask5_dec.Decode_Correct_Sequential_Codewords *****");
                DataCodewordsDecoder after_mask5_dec = new DataCodewordsDecoder();
                after_mask5_dec.Decode_Correct_Sequential_Codewords(after_mask5_block1, 
                                                                    after_mask5_block2, 
                                                                    after_mask5_block3, 
                                                                    after_mask5_block4);
                
                
                //First, try to make a good QR-Code 
                //then work on why can not error-correct-after-eyeballing-bad-branding-area

                ArrayList<Integer> good_msg_data_only = dec.Get_save_byteencdata_as_integer_NO_ENC_IND_NO_LENGTH();
                ArrayList<Integer> good_sequence_with_byteencindlen = tool.Add_ByteEncIndAndLength_To_GoodDataInSequence(good_msg_data_only, 60);
                
                
                
                ArrayList<Integer> good_interleave = new ArrayList<Integer>();
                for (int ii=0;  ii < 15; ii++)
                {
                    good_interleave.add(good_dc_block1.get(ii));
                    good_interleave.add(good_dc_block2.get(ii));
                    good_interleave.add(good_dc_block3.get(ii));
                    good_interleave.add(good_dc_block4.get(ii));
                }
                
                
                
		RowColumnMapWriter mapwriter = new RowColumnMapWriter("./Example_BarefootBar/ver6-blank.txt", 41, 41);
		try {
                    mapwriter.FillMap(good_interleave);
		}
		catch (Exception ex) {
                    ex.printStackTrace();
		}
		mapwriter.DumpMap("./Example_BarefootBar/good_interleave_map.txt");
		
                int ecc_per_block_6H = 28;
		int dc_per_block_6H  = 15;
		int num_of_blocks_6H = 4;
                RowColumnMapReader just_msg_map = new RowColumnMapReader(41,41);
		just_msg_map.Load("./Example_BarefootBar/good_interleave_map.txt");
		just_msg_map.Find_Codewords(num_of_blocks_6H * dc_per_block_6H);
                ArrayList<Integer> just_msg_interleaved = just_msg_map.Get_codewords();
		ArrayList<Integer> just_msg_block1 = new ArrayList<Integer>();
		ArrayList<Integer> just_msg_block2 = new ArrayList<Integer>();
		ArrayList<Integer> just_msg_block3 = new ArrayList<Integer>();
		ArrayList<Integer> just_msg_block4 = new ArrayList<Integer>();

		for (int ii=0; ii < just_msg_interleaved.size(); ii += 4)
		{
			just_msg_block1.add(just_msg_interleaved.get(ii));
			just_msg_block2.add(just_msg_interleaved.get(ii+1));
			just_msg_block3.add(just_msg_interleaved.get(ii+2));
			just_msg_block4.add(just_msg_interleaved.get(ii+3));
		}
                
                System.out.println("***** just_msg TEST *****");
		dec.Decode_Correct_Sequential_Codewords(just_msg_block1,
				                        just_msg_block2,
				                        just_msg_block3,
				                        just_msg_block4);
                
                *****/
                
                
                
                
/*****
                ErrorCorrectionCodewordsGeneration eccgen1  = new ErrorCorrectionCodewordsGeneration(good_dc_block1, 28);
                ArrayList<Integer> barefootbar_ecc_block1 = eccgen1.Get_ECC_Decimal();
                
                ErrorCorrectionCodewordsGeneration eccgen2  = new ErrorCorrectionCodewordsGeneration(good_dc_block2, 28);
                ArrayList<Integer> barefootbar_ecc_block2 = eccgen2.Get_ECC_Decimal();
                
                ErrorCorrectionCodewordsGeneration eccgen3  = new ErrorCorrectionCodewordsGeneration(good_dc_block3, 28);
                ArrayList<Integer> barefootbar_ecc_block3 = eccgen3.Get_ECC_Decimal();
                
                ErrorCorrectionCodewordsGeneration eccgen4  = new ErrorCorrectionCodewordsGeneration(good_dc_block4, 28);
                ArrayList<Integer> barefootbar_ecc_block4 = eccgen4.Get_ECC_Decimal();
                
                ArrayList<Integer> barefootbar_interleaved_ecc = new ArrayList<Integer>();
                for (int ii=0; ii < 28; ii++)
                {
                    barefootbar_interleaved_ecc.add(barefootbar_ecc_block1.get(ii));
                    barefootbar_interleaved_ecc.add(barefootbar_ecc_block2.get(ii));
                    barefootbar_interleaved_ecc.add(barefootbar_ecc_block3.get(ii));
                    barefootbar_interleaved_ecc.add(barefootbar_ecc_block4.get(ii));
                }


		//lets just introduce 1 bit of 1 byte error and see if code works good
		//version 6-H
		//k == 15 d.c per block
		//n == 15 d.c per block + 28 e.c.c per block == 43 codewords per block
		//(n - k) == 28
		//Maximum number of incorrect codewords per block that can be fixed is 14.
		
                //***************************************************************************************************
                //WARNING!!!!!
                //WARNING!!!!!
                //WARNING!!!!!
                //WARNING!!!!!
                //WARNING!!!!!
                //these correct_block_codewords include indicator-4bits-byte-encoding-indicator-and-8bits-length-58
                //i don't think this is correct way to have encoding-indicator-and-length included
                //i think correct way is just message-data only
                //idk idk too tired, just do make good qr-code first too tired
                //WARNING!!!!!
                //WARNING!!!!!
                //WARNING!!!!!
                //WARNING!!!!!
                //WARNING!!!!!
                //****************************************************************************************************
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
		
		//change 3rd data byte
		//changing from 135 to 130
		
		//QRcode-ver6H
		//k == 15 d.c per block
		//n == 15 d.c per block + 28 e.c.c per block
		//(n - k) == 28
		//Maximum number of corrupted symbols we can recover is 14 per block
		
		Integer[] received_block_codewords = 
			{ 67, 166, 130, 71, 71, 7, 51, 162, 242, 247, 119, 119, 114, 230, 134,
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

		ArrayList<ArrayList<Integer>> unk_results = err_corrector.BuildUnknownResults(received_block_codewords, max_root_alpha_exp);
		err_corrector.DebugPrint_UnknownResults(unk_results);
		
		ArrayList<String>  correctable_byte_arr = err_corrector.FindCorrectableByteLocations(unk_results, max_root_alpha_exp);
		System.out.println("correctable_byte_arr.size == " + correctable_byte_arr.size());
		System.out.println("correctable_byte_arr[0] == "   + correctable_byte_arr.get(0));
		
		ArrayList<String> corrections = err_corrector.CorrectCorrectableBytes(correctable_byte_arr, max_root_alpha_exp, received_block_codewords.length);
		err_corrector.DebugPrint_Corrections(corrections);
                
                
                
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
                Masking mask = new Masking();
		mask.Write_Wikipedia_Mask_5();
		boolean success = mask.Do_Mask("./Example_BarefootBar/original_map_3_branding_area_eyeballs_filled.txt", "./mask_5.txt", "./Example_BarefootBar/after_mask5_bad_branding_area.txt");
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
		ArrayList<Integer> dc_block1 = new ArrayList<Integer>();
		ArrayList<Integer> dc_block2 = new ArrayList<Integer>();
		ArrayList<Integer> dc_block3 = new ArrayList<Integer>();
		ArrayList<Integer> dc_block4 = new ArrayList<Integer>();

		for (int ii=0; ii < 60; ii += 4)
		{
			dc_block1.add(interleaved.get(ii));
			dc_block2.add(interleaved.get(ii+1));
			dc_block3.add(interleaved.get(ii+2));
			dc_block4.add(interleaved.get(ii+3));
		}
		
		ArrayList<Integer> ecc_block1 = new ArrayList<Integer>();
		ArrayList<Integer> ecc_block2 = new ArrayList<Integer>();
		ArrayList<Integer> ecc_block3 = new ArrayList<Integer>();
		ArrayList<Integer> ecc_block4 = new ArrayList<Integer>();
                
                for (int ii=60; ii < 172; ii += 4)
		{
			ecc_block1.add(interleaved.get(ii));
			ecc_block2.add(interleaved.get(ii+1));
			ecc_block3.add(interleaved.get(ii+2));
			ecc_block4.add(interleaved.get(ii+3));
		}
		
                //*****************************
                // data block1 and ecc_block1
                //*****************************
                ArrayList<Integer> recv_block1_al = new ArrayList<Integer>();
                recv_block1_al.addAll(dc_block1);
                recv_block1_al.addAll(ecc_block1);
                Integer[] recv_block1 = new Integer[recv_block1_al.size()];
                recv_block1_al.toArray(recv_block1);
                ArrayList<Integer> recv_block1_nonzero_syndrome = new ArrayList<Integer>(); 
		for (int ii=0; ii <= max_root_alpha_exp; ii++)
		{
		     int syndrome = err_corrector.CalculateSyndrome(recv_block1, ii);
		     if (syndrome != 0)
		    	 recv_block1_nonzero_syndrome.add(syndrome);
		}
		System.out.println("recv_block1_nonzero_syndrome.size = " + recv_block1_nonzero_syndrome.size());
                
                ArrayList<ArrayList<Integer>> unk_results_block1 = err_corrector.BuildUnknownResults(recv_block1, max_root_alpha_exp);
		err_corrector.DebugPrint_UnknownResults(unk_results_block1);
                System.out.println("recv_block1 CAN NOT BE FIXED");
                
                //*****************************
                // data block2 and ecc_block2
                //*******************************
                ArrayList<Integer> recv_block2_al = new ArrayList<Integer>();
                recv_block2_al.addAll(dc_block2);
                recv_block2_al.addAll(ecc_block2);
                Integer[] recv_block2 = new Integer[recv_block2_al.size()];
                recv_block2_al.toArray(recv_block2);
                ArrayList<Integer> recv_block2_nonzero_syndrome = new ArrayList<Integer>(); 
		for (int ii=0; ii <= max_root_alpha_exp; ii++)
		{
		     int syndrome = err_corrector.CalculateSyndrome(recv_block2, ii);
		     if (syndrome != 0)
		    	 recv_block2_nonzero_syndrome.add(syndrome);
		}
		System.out.println("recv_block2_nonzero_syndrome.size = " + recv_block2_nonzero_syndrome.size());
                
                ArrayList<ArrayList<Integer>> unk_results_block2 = err_corrector.BuildUnknownResults(recv_block2, max_root_alpha_exp);
		err_corrector.DebugPrint_UnknownResults(unk_results_block2);
                System.out.println("recv_block2 CAN NOT BE FIXED");

                //*****************************
                // data block3 and ecc_block3
                //*****************************
                ArrayList<Integer> recv_block3_al = new ArrayList<Integer>();
                recv_block3_al.addAll(dc_block3);
                recv_block3_al.addAll(ecc_block3);
                Integer[] recv_block3 = new Integer[recv_block3_al.size()];
                recv_block3_al.toArray(recv_block3);
                ArrayList<Integer> recv_block3_nonzero_syndrome = new ArrayList<Integer>(); 
		for (int ii=0; ii <= max_root_alpha_exp; ii++)
		{
		     int syndrome = err_corrector.CalculateSyndrome(recv_block3, ii);
		     if (syndrome != 0)
		    	 recv_block3_nonzero_syndrome.add(syndrome);
		}
		System.out.println("recv_block3_nonzero_syndrome.size = " + recv_block3_nonzero_syndrome.size());
                
                ArrayList<ArrayList<Integer>> unk_results_block3 = err_corrector.BuildUnknownResults(recv_block3, max_root_alpha_exp);
		err_corrector.DebugPrint_UnknownResults(unk_results_block3);
                System.out.println("recv_block3 CAN NOT BE FIXED");
                
    
                //*****************************
                //data block4 and ecc_block4
                //*****************************
                ArrayList<Integer> recv_block4_al = new ArrayList<Integer>();
                recv_block4_al.addAll(dc_block4);
                recv_block4_al.addAll(ecc_block4);
                Integer[] recv_block4 = new Integer[recv_block4_al.size()];
                recv_block4_al.toArray(recv_block4);
                ArrayList<Integer> recv_block4_nonzero_syndrome = new ArrayList<Integer>(); 
		for (int ii=0; ii <= max_root_alpha_exp; ii++)
		{
		     int syndrome = err_corrector.CalculateSyndrome(recv_block4, ii);
		     if (syndrome != 0)
		    	 recv_block4_nonzero_syndrome.add(syndrome);
		}
		System.out.println("recv_block4_nonzero_syndrome.size = " + recv_block4_nonzero_syndrome.size());
                
                ArrayList<ArrayList<Integer>> unk_results_block4 = err_corrector.BuildUnknownResults(recv_block4, max_root_alpha_exp);
		err_corrector.DebugPrint_UnknownResults(unk_results_block4);
                System.out.println("recv_block4 CAN NOT BE FIXED");
                
                //shoot all 4 blocks can not be fixed :(
                
                //starting at 1 byte at a time see if we can just fix 1 byte of a block
                //just see
                Integer[] test_block = new Integer[recv_block4_al.size()];
                recv_block4_al.toArray(test_block);
                boolean found_correctable_byte = false;
                for (int byteloc=0; byteloc < 43; byteloc++)
                {
                    int old_val = test_block[byteloc];
                    for (int byteval=0; byteval < 256; byteval++)
                    {
                        test_block[byteloc] = byteval;
                        ArrayList<ArrayList<Integer>> test_block_unk_results = err_corrector.BuildUnknownResults(test_block, max_root_alpha_exp);
                        //err_corrector.DebugPrint_UnknownResults(test_block_unk_results);

                        ArrayList<String>  test_block_correctable_byte_arr = err_corrector.FindCorrectableByteLocations(test_block_unk_results, max_root_alpha_exp);
                        System.out.println("byteloc=" + byteloc + " byteval=" + byteval);
                        if (test_block_correctable_byte_arr.size() > 0)
                        {
                            System.out.println("yay found correctable byte!!!!!");
                            System.out.println("byteloc=" + byteloc);
                            System.out.println("byteval=" + byteval);
                            found_correctable_byte = true;
                            break;
                        }
                    }
                    if (found_correctable_byte)
                        break;
                    else
                        test_block[byteloc] = old_val;
                }
*****/
                
	}//DoExample

}//class
