

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
	}//DoExample

}//class
