import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import javax.imageio.ImageIO;

public class PNGReader {
	
	private String FilePath;
	private BufferedImage image;
	private int width;
	private int height;
	
	public PNGReader(String filepath)
	{
		FilePath = filepath;
	}//constructor
	
	public boolean Read()
	{
        try {
            // Create a File object representing the PNG file
            File file = new File(FilePath);

            // Read the image into a BufferedImage object
            image = ImageIO.read(file);

            // You can now work with the 'image' object.
            // For example, print its dimensions:
            if (image != null) {
                System.out.println("Image loaded successfully!");
                width = image.getWidth();
                height = image.getHeight();
                System.out.println("Width: " + width);
                System.out.println("Height: " + height);
            } else {
                System.out.println("Could not read the image. It might be an invalid format or the file does not exist.");
                return false;
            }

        } catch (IOException e) {
            System.err.println("Error reading the PNG file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }//Read
	
	public int FindPNGPixelDimensionToQRPixelDimension()
	{
		//QR_TRex a little difficult
		//because it has slanted edges and circles

		int yy=0;
		int start_black_xx = 0;
		int start_black_yy = 0;
		String blackstr = "";
		int blackint = 0;
		int end_black_xx=0;
		int end_black_yy=0;
		
		String white = Integer.toBinaryString(-1); //white
		while (blackstr.isEmpty()) {
            for (int xx = 0; xx < width; xx++) {
                int pixel = image.getRGB(xx, yy);
                if (pixel != -1) //xx==47 yy==40 first black-pixel
                {
                	blackint = pixel;
                	blackstr = Integer.toBinaryString(blackint);
                	start_black_xx = xx;
                	start_black_yy = yy;
                	break;
                }
            }//xx
            yy++;
        }//yy
		
		//found 1st black pixel
		//keep going right till white found
		for (int xx=start_black_xx; xx < width; xx++)
		{
			int pixel = image.getRGB(xx, start_black_yy);
			if (pixel != blackint) 
			{
				end_black_xx = xx;
				break;
			}
		}
		
		//take that length
		//go to middle
		//start going down till white
		int diff = end_black_xx - start_black_xx;
		int half_diff = (int)diff / 2;
		int middle_black_xx = start_black_xx + half_diff;
		for (yy=start_black_yy; yy < height; yy++)
		{
			int pixel = image.getRGB(middle_black_xx, yy);
			if (pixel == -1) //white
			{
				end_black_yy = yy;
				break;
			}
		}
		
		//assume square (height==width)
		int side = end_black_yy - start_black_yy;
        return side;
	}//FindPNGPixelDimensionToQRPixelDimension

}//class
