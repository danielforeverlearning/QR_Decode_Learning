import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
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
	private int imagetype;
	
	private int start_black_xx;
	private int start_black_yy;
	
	private BufferedImage purpleimage;
	private int goodside;
	
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
                imagetype = image.getType();
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
	
	public boolean DumpWithPurpleLines(String filepath, int side)
	{
		int purple = 0xFF000000 ^ 0x009D0000 ^ 0x00000000 ^ 0x000000FF;
		purpleimage = new BufferedImage(width, height, imagetype);
		for (int yy=0; yy < height; yy++)
		{
			for (int xx=0; xx < width; xx++)
			{
				if (((xx%side==0) && (xx>0)) ||
				    ((yy%side==0) && (yy>0)))
					purpleimage.setRGB(xx, yy, purple);
				else
				{
					int pixel = image.getRGB(xx, yy);
					purpleimage.setRGB(xx, yy, pixel);
				}
			}
		}
		File outputFile = new File(filepath);
		try {
			ImageIO.write(purpleimage, "PNG", outputFile);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}//DumpWithPurpleLines
	
	public void SaveGoodSide(int side)
	{
		goodside = side;
	}//SaveGoodSide
	
	public int PurpleEdgedSquareBlackOrWhite(int startxx, int startyy)
	{
		int purple = 0xFF000000 ^ 0x009D0000 ^ 0x00000000 ^ 0x000000FF;
		int endyy = startyy + goodside;
		int endxx = startxx + goodside;
		int R_sum = 0;
		int G_sum = 0;
		int B_sum = 0;
		int count = 0;
		Tools tool = new Tools();
		for (int yy = startyy; yy < endyy; yy++)
		{
			for (int xx = startxx; xx < endxx; xx++)
			{
				int pixel = purpleimage.getRGB(xx, yy);
				if (pixel != purple)
				{
					String pixelstr = Integer.toBinaryString(pixel);
					String A_str = pixelstr.substring(0, 8);
					String R_str = pixelstr.substring(8, 16);
					String G_str = pixelstr.substring(16, 24);
					String B_str = pixelstr.substring(24);
					
					if (A_str.equals("11111111") == false)
						System.out.println("PurpleEdgedSquareBlackOrWhite: Alpha not equal to 11111111 found");
					
					R_sum += tool.ConvertBinaryByteStringToPositiveInteger(R_str);
					G_sum += tool.ConvertBinaryByteStringToPositiveInteger(G_str);
					B_sum += tool.ConvertBinaryByteStringToPositiveInteger(B_str);
					count++;
				}
			}
		}
		
		double R_avg = R_sum / count;
		double G_avg = G_sum / count;
		double B_avg = B_sum / count;
		
		if (R_avg >= 128 && G_avg >= 128 && B_avg >= 128)
			return 0; //white
		else
			return 1; //black
	}//PurpleEdgedSquareBlackOrWhite
	
	public boolean DumpToUntrimmedRowColumnMapTextFile(String filepath)
	{
		//This equation should handle slanted edges like TRex
		//1 is black, 0 is white
		int startQRxx = (int)start_black_xx / goodside;
		int startQRyy = (int)start_black_yy / goodside;
		try
		{
			FileWriter myWriter = new FileWriter(filepath);
			
			for (int yy = startQRyy * goodside; yy < height; yy += goodside)
			{
				for (int xx = startQRxx * goodside; xx < width; xx += goodside)
				{
					int blackorwhite = PurpleEdgedSquareBlackOrWhite(xx,yy);
					if (blackorwhite == 1)
						myWriter.write("1"); //black
					else
						myWriter.write("0"); //white
				}
				myWriter.write('\n');
			}
			myWriter.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
		return true;
	}//DumpToUntrimmedRowColumnMapTextFile
	
	public int TrimRowColumnMapTextFile(String src, String dest)
	{
		int QRpixellen = 0;
		FileWriter myWriter = null;
		File inputFile = null;
		Scanner inputReader = null;
		try
		{
			myWriter = new FileWriter(dest);
			inputFile = new File(src);
			inputReader = new Scanner(inputFile);
			
			String line = inputReader.nextLine();
			int xx = line.length() - 7;
			String str = line.substring(xx);
			//from the end of line find 1111111
			while (str.equals("1111111") == false)
			{
				xx--;
				str = line.substring(xx, xx+7);
			}
			
			QRpixellen = xx+7;
			myWriter.write(line.substring(0,QRpixellen));
			myWriter.write('\n');
			for (int yy=2; yy <= QRpixellen; yy++)
			{
				line = inputReader.nextLine();
				myWriter.write(line.substring(0,QRpixellen));
				myWriter.write('\n');
			}
			myWriter.close();
			inputReader.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return 0;
		}
		return QRpixellen;
	}//TrimRowColumnMapTextFile

}//class
