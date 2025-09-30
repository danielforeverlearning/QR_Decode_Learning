
import java.util.ArrayList;


public class BruteForceMaximumByteErrorDetectionCorrection3 {
    private Integer[] Received_DC_ECC_Block = null;
    private ErrorCorrection err_corrector = null;
    private int max_root_alpha_exp = 0;
    
    private Integer[] recv_copy = null;
    
    public BruteForceMaximumByteErrorDetectionCorrection3(Integer[] recv, int maxrootalphaexp)
    {
        Received_DC_ECC_Block = recv;
        err_corrector = new ErrorCorrection();
        max_root_alpha_exp = maxrootalphaexp;
        recv_copy = new Integer[recv.length];
    }//constructor
    
    private void ChangeRecvCopy(int index1, int value1, int index2, int value2)
    {
        for (int ii=0; ii < Received_DC_ECC_Block.length; ii++)
        {
            if (ii == index1)
                recv_copy[ii] = value1;
            else if (ii == index2)
                recv_copy[ii] = value2;
            else
                recv_copy[ii] = Received_DC_ECC_Block[ii];
        }
    }//ChangeRecvCopy
    
    public void Process()
    {
        ArrayList<ArrayList<Integer>> unk_results = null;
        ArrayList<String>  correctable_byte_arr = null;
        ArrayList<String> corrections = null;
        int index1 = 0;
        int index2 = 0;
        int value1 = 0;
        int value2 = 0;
        for (index1=0; index1 < Received_DC_ECC_Block.length; index1++)
        {
            for (value1=0; value1 <= 255; value1++)
            {
                System.out.println("index1==" + index1 + " value1==" + value1 + " double-loop-index2-value2");
                for (index2=0; index2 < Received_DC_ECC_Block.length; index2++)
                {
                    for (value2=0; value2 <= 255; value2++)
                    {
                        if (index1 == index2)
                            break;
                        else
                        {
                            this.ChangeRecvCopy(index1, value1, index2, value2);
                            unk_results = err_corrector.BuildUnknownResults(this.recv_copy, max_root_alpha_exp);
                            correctable_byte_arr = err_corrector.FindCorrectableByteLocations(unk_results, max_root_alpha_exp);
                            if (correctable_byte_arr.size() > 0)
                            {
                                    corrections = err_corrector.CorrectCorrectableBytes(correctable_byte_arr, max_root_alpha_exp, this.recv_copy.length);
                                    System.out.println("Process: index1=" + index1 + " value1=" + value1);
                                    System.out.println("Process: index2=" + index2 + " value2=" + value2);
                                    err_corrector.DebugPrint_Corrections(corrections);
                                    return;
                            }
                        }
                    }
                }
            }
        }
    }//Process
}//class
