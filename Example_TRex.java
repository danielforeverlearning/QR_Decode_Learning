

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
          
          RowColumnMapReader mymap = new RowColumnMapReader(QRpixellen, QRpixellen);
  		  mymap.Load("./Example_TRex/rowcolmap.txt");
  		  mymap.DebugPrintFormatStr();
  		  
  		  //it is wikipediamask6 because you keep
  		  //mixing up black and white as 0 or 1 :(
  		  //(i/2 + j/3)%2=0
  		  //too lazy to change Masking class do it later :(
  		  //version6 is 41x41
  		  //this one is 37x37 so it is probably version5
  		  //and only level7 or higher has version information
  		  Masking mask = new Masking();
		  mask.Write_Wikipedia_Mask_6(QRpixellen);
		  
		  mymap.Change_3FindersToA_TimingToZ_AlignmentToZ(5);
  		  mymap.DumpMap("./Example_TRex/rowcolmap_AZ.txt");
  		  
		  
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
		  mymap.FindDataCodewordsFromMap(2, 43, 0, 0);
		  mymap.DebugPrint_codewords();
	
	}//DoExample

}//class
