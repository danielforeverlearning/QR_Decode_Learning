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
		DataCodewordsDecoder dec = new DataCodewordsDecoder();
		dec.Decode_Correct_Sequential_Codewords("./Example_BarefootBar/20221025_104402_codewords.txt");
		ArrayList<Integer> good_sequence = tool.IntegerTextFileToArrayList("./Example_BarefootBar/20221025_104402_codewords.txt");
                ArrayList<Integer> good_interleave = new ArrayList<Integer>();
                for (int ii=0; ii < good_sequence.size(); ii += 4)
                {
                    good_interleave.add(good_sequence.get(ii));
                    good_interleave.add(good_sequence.get(ii+1));
                    good_interleave.add(good_sequence.get(ii+2));
                    good_interleave.add(good_sequence.get(ii+3));
                }
                tool.ArrayListToIntegerTextFile("./Example_BarefootBar/good_interleave.txt", good_interleave);
		
		
		
		//lets just introduce 1 bit of 1 byte error and see if code works good
		//version 6-H
		//k == 15 d.c per block
		//n == 15 d.c per block + 28 e.c.c per block == 43 codewords per block
		//(n - k) == 28
		//Maximum number of incorrect codewords per block that can be fixed is 14.
		
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
                
                
                
                /****************************************************************************
                lets try to correct for real because it has bad branding area
                *****************************************************************************/
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
		int ecc_per_block_6H = 28;
		int dc_per_block_6H  = 15;
		int num_of_blocks_6H = 4;
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
                      
		/*****************************************
                 shoot tried all 4 blocks no joy
                 eyeballing branding area did not work
                 ok
                 we know correct data codewords all 60
                 generate correct ecc codewords
                 and we can just move on
                 ********************************************/
		ArrayList<Integer> barefootbar_correct = tool.IntegerTextFileToArrayList("./Example_BarefootBar/20221025_104402_codewords.txt");
                System.out.println("barefootbar_correct.size = " + barefootbar_correct.size());
                ArrayList<Integer> barefootbar_dc_block1 = new ArrayList<Integer>();
                ArrayList<Integer> barefootbar_dc_block2 = new ArrayList<Integer>();
                ArrayList<Integer> barefootbar_dc_block3 = new ArrayList<Integer>();
                ArrayList<Integer> barefootbar_dc_block4 = new ArrayList<Integer>();
                for (int ii=0; ii < 15; ii++)
                    barefootbar_dc_block1.add(barefootbar_correct.get(ii));
                for (int ii=15; ii < 30; ii++)
                    barefootbar_dc_block2.add(barefootbar_correct.get(ii));
                for (int ii=30; ii < 45; ii++)
                    barefootbar_dc_block3.add(barefootbar_correct.get(ii));
                for (int ii=45; ii < 60; ii++)
                    barefootbar_dc_block4.add(barefootbar_correct.get(ii));
                
                System.out.println("barefootbar_dc_block1.size = " + barefootbar_dc_block1.size());
                System.out.println("barefootbar_dc_block2.size = " + barefootbar_dc_block2.size());
                System.out.println("barefootbar_dc_block3.size = " + barefootbar_dc_block3.size());
                System.out.println("barefootbar_dc_block4.size = " + barefootbar_dc_block4.size());
                            
                ErrorCorrectionCodewordsGeneration eccgen1  = new ErrorCorrectionCodewordsGeneration(barefootbar_dc_block1, 28);
                ArrayList<Integer> barefootbar_ecc_block1 = eccgen1.Get_ECC_Decimal();
                
                ErrorCorrectionCodewordsGeneration eccgen2  = new ErrorCorrectionCodewordsGeneration(barefootbar_dc_block2, 28);
                ArrayList<Integer> barefootbar_ecc_block2 = eccgen2.Get_ECC_Decimal();
                
                ErrorCorrectionCodewordsGeneration eccgen3  = new ErrorCorrectionCodewordsGeneration(barefootbar_dc_block3, 28);
                ArrayList<Integer> barefootbar_ecc_block3 = eccgen3.Get_ECC_Decimal();
                
                ErrorCorrectionCodewordsGeneration eccgen4  = new ErrorCorrectionCodewordsGeneration(barefootbar_dc_block4, 28);
                ArrayList<Integer> barefootbar_ecc_block4 = eccgen4.Get_ECC_Decimal();
                
                ArrayList<Integer> barefootbar_good_branding_area = new ArrayList<Integer>();
                ArrayList<Integer> barefootbar_interleaved_dc = new ArrayList<Integer>();
                ArrayList<Integer> barefootbar_interleaved_ecc = new ArrayList<Integer>();
                for (int ii=0; ii < 15; ii++)
                {
                    barefootbar_interleaved_dc.add(barefootbar_dc_block1.get(ii));
                    barefootbar_interleaved_dc.add(barefootbar_dc_block2.get(ii));
                    barefootbar_interleaved_dc.add(barefootbar_dc_block3.get(ii));
                    barefootbar_interleaved_dc.add(barefootbar_dc_block4.get(ii));
                }
                for (int ii=0; ii < 28; ii++)
                {
                    barefootbar_interleaved_ecc.add(barefootbar_ecc_block1.get(ii));
                    barefootbar_interleaved_ecc.add(barefootbar_ecc_block2.get(ii));
                    barefootbar_interleaved_ecc.add(barefootbar_ecc_block3.get(ii));
                    barefootbar_interleaved_ecc.add(barefootbar_ecc_block4.get(ii));
                }
                barefootbar_good_branding_area.addAll(barefootbar_interleaved_dc);
                barefootbar_good_branding_area.addAll(barefootbar_interleaved_ecc);
                
                tool.ArrayListToIntegerTextFile("./Example_BarefootBar/barefootbar_interleaved_dc.txt", barefootbar_interleaved_dc);

//bug somewhere should be perfect
/*****
		RowColumnMapWriter mapwriter = new RowColumnMapWriter("./Example_BarefootBar/ver6-blank.txt", 41, 41);
		try {
                    mapwriter.FillMap(barefootbar_good_branding_area);
		}
		catch (Exception ex) {
                    ex.printStackTrace();
		}
		mapwriter.DumpMap("./Example_BarefootBar/barefootbar_good_branding_beforemask.txt");
		
                //boolean theend = mask.Do_Mask("./Example_BarefootBar/barefootbar_good_branding_beforemask.txt", "./mask_5.txt", "./Example_BarefootBar/barefootbar_good_branding_aftermask.txt");
		//if (!theend)
		//{
                //    System.out.println("ERROR");
                //    return;
		//}
		
                RowColumnMapReader correct_map = new RowColumnMapReader(41,41);
		correct_map.Load("./Example_BarefootBar/barefootbar_good_branding_beforemask.txt");
		correct_map.Find_Codewords(num_of_blocks_6H * dc_per_block_6H);
                ArrayList<Integer> interleaved_correct_map_data = correct_map.Get_codewords();
		ArrayList<Integer> deinterleaved_correct_map_block1 = new ArrayList<Integer>();
		ArrayList<Integer> deinterleaved_correct_map_block2 = new ArrayList<Integer>();
		ArrayList<Integer> deinterleaved_correct_map_block3 = new ArrayList<Integer>();
		ArrayList<Integer> deinterleaved_correct_map_block4 = new ArrayList<Integer>();

		for (int ii=0; ii < interleaved_correct_map_data.size(); ii += 4)
		{
			deinterleaved_correct_map_block1.add(interleaved_correct_map_data.get(ii));
			deinterleaved_correct_map_block2.add(interleaved_correct_map_data.get(ii+1));
			deinterleaved_correct_map_block3.add(interleaved_correct_map_data.get(ii+2));
			deinterleaved_correct_map_block4.add(interleaved_correct_map_data.get(ii+3));
		}
		dec.Decode_Correct_Sequential_Codewords(deinterleaved_correct_map_block1,
				                        deinterleaved_correct_map_block2,
				                        deinterleaved_correct_map_block3,
				                        deinterleaved_correct_map_block4);
*****/
                
	}//DoExample

}//class
