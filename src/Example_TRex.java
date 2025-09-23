import java.io.FileWriter;
import java.util.ArrayList;

public class Example_TRex {
	
	public Example_TRex() {
		
	}//constructor
	
	public void DoExample() throws Exception
	{
          PNGReader img = new PNGReader("./Example_TRex/QR_TRex.png");
          boolean readsuccess = img.Read();
          if (readsuccess == false)
        	  return;
          
          int side = img.FindPNGPixelDimensionToQRPixelDimension();
          
          //draw purple-lines inspect after
          boolean dumpsuccess = img.DumpWithPurpleLines("./Example_TRex/purplelines.png", side);
          if (dumpsuccess == false)
        	  return;
          
          img.SaveGoodSide(side);
          
          dumpsuccess = img.DumpToUntrimmedRowColumnMapTextFile("./Example_TRex/untrimmed_rowcolmap.txt");
          if (dumpsuccess == false)
        	  return;
          
          int QRpixellen = img.TrimRowColumnMapTextFile("./Example_TRex/untrimmed_rowcolmap.txt", "./Example_TRex/rowcolmap.txt");
          System.out.println("QRpixellen = " + QRpixellen);
          
          RowColumnMapReader nomaskyetmap = new RowColumnMapReader(QRpixellen, QRpixellen);
          nomaskyetmap.Load("./Example_TRex/rowcolmap.txt");
          nomaskyetmap.DebugPrintFormatStr();
  		  
  		  //it is wikipediamask6 because you keep
  		  //mixing up black and white as 0 or 1 :(
  		  //(i/2 + j/3)%2=0
  		  //too lazy to change Masking class do it later :(
  		  //version6 is 41x41
  		  //this one is 37x37 so it is probably version5
  		  //and only level7 or higher has version information
  		  Masking mask = new Masking();
		  mask.Write_Wikipedia_Mask_6(QRpixellen);
		  
		  nomaskyetmap.Change_3FindersToA_TimingToZ_AlignmentToZ(5);
		  nomaskyetmap.DumpMap("./Example_TRex/rowcolmap_AZ.txt");
  		  
		  
		  boolean success = mask.Do_Mask("./Example_TRex/rowcolmap_AZ.txt", "./bin/mask6_" + QRpixellen + ".txt", "./Example_TRex/rowcolmap_AZ_mask6.txt");
		  if (!success)
		  {
		        System.out.println("ERROR");
			    return;
		  }
		  
		  /*********************************************************************************************************************
		   Version 			Total Number of 		EC Codewords 	Number of Blocks 	Number of Data Codewords
		   and EC Level		Data Codewords  		Per Block		in Group 1			in Each of Group 1's Blocks
		   					for this
		   					Version and EC Level
		   
		   5-M				86						24				2					43
		  **********************************************************************************************************************/
		  RowColumnMapReader mymap = new RowColumnMapReader(QRpixellen, QRpixellen);
		  mymap.Load("./Example_TRex/rowcolmap_AZ_mask6.txt");
		  
		  //too tired, bugs in  below because did not repair code after making rowcolmapwriter
		  mymap.Find_DC_And_ECC_FromMap(134);
		  
		  
		  //https://www.signupgenius.com/go/60B0D49A5A923A1F58-58383001-hjahalloween
		  //https://www.signupgenius.com/go/60B0D49A5A923A1F58-58383001-hjahalloween
		  
		  ArrayList<Integer> interleaved_codewords = mymap.Get_codewords();
		  ArrayList<Integer> dc_block1  = new ArrayList<Integer>();
		  ArrayList<Integer> dc_block2  = new ArrayList<Integer>();
		  ArrayList<Integer> ecc_block1 = new ArrayList<Integer>();
		  ArrayList<Integer> ecc_block2 = new ArrayList<Integer>();
		  for (int ii=0; ii < interleaved_codewords.size(); ii += 2)
		  {
			  if (ii < 86)
			  {
				  dc_block1.add(interleaved_codewords.get(ii));
				  dc_block2.add(interleaved_codewords.get(ii+1));
			  }
			  else
			  {
				  ecc_block1.add(interleaved_codewords.get(ii));
				  ecc_block2.add(interleaved_codewords.get(ii+1));
			  }
		  }
		  
		  ArrayList<Integer> block1 = new ArrayList<Integer>();
		  block1.addAll(dc_block1);
		  block1.addAll(ecc_block1);
		  
		  ArrayList<Integer> block2 = new ArrayList<Integer>();
		  block2.addAll(dc_block2);
		  block2.addAll(ecc_block2);
		  
		  
		  /*
		  int max_root_alpha_exp = ecc_block1.size() - 1;
		  ArrayList<Integer> nonzero_syndrome = new ArrayList<Integer>(); 
			for (int ii=0; ii <= max_root_alpha_exp; ii++)
			{
			     int syndrome = ecc1.CalculateSyndrome(received_block_codewords, ii);
			     if (syndrome != 0)
			    	 nonzero_syndrome.add(syndrome);
			}
			System.out.println("received_block_codewords: nonzero_syndrome.size = " + nonzero_syndrome.size());
		  */
		  
		  
		  /****************
		  FileWriter myWriter = new FileWriter("./Example_TRex/check-dc-sequence.txt");
		  for (int ii=0; ii < deinterleaved_dc_block1.size(); ii++)
		  {
			  myWriter.write(deinterleaved_dc_block1.get(ii).toString());
			  myWriter.write('\n');
		  }
		  for (int ii=0; ii < deinterleaved_dc_block2.size(); ii++)
		  {
			  myWriter.write(deinterleaved_dc_block2.get(ii).toString());
			  myWriter.write('\n');
		  }
		  myWriter.close();
		  ********************/
		  
		  /**********************************************************
		  DataCodewordsDecoder dec = new DataCodewordsDecoder();
		  dec.Decode_Correct_Sequential_Codewords(deinterleaved_dc_block1,
				                                  deinterleaved_dc_block2);
		  **********************************************************/
	}//DoExample

}//class
