import java.util.ArrayList;

public class BruteForceMaximumByteErrorDetectionCorrection2 {
    
    private Integer[] Received_DC_ECC_Block = null;
    private ErrorCorrection err_corrector = null;
    private int max_root_alpha_exp = 0;
    
    private Integer[] recv_copy = null;
    
    public BruteForceMaximumByteErrorDetectionCorrection2(Integer[] recv, int maxrootalphaexp)
    {
        Received_DC_ECC_Block = recv;
        err_corrector = new ErrorCorrection();
        max_root_alpha_exp = maxrootalphaexp;
        recv_copy = new Integer[recv.length];
    }//constructor
    
    private void ChangeRecvCopy(int index, int value)
    {
        for (int ii=0; ii < Received_DC_ECC_Block.length; ii++)
        {
            if (ii == index)
                recv_copy[ii] = value;
            else
                recv_copy[ii] = Received_DC_ECC_Block[ii];
        }
    }//ChangeRecvCopy
    
    public void Process()
    {
        ArrayList<ArrayList<Integer>> unk_results = null;
        ArrayList<String>  correctable_byte_arr = null;
        ArrayList<String> corrections = null;
        for (int index=0; index < Received_DC_ECC_Block.length; index++)
        {
            for (int value=0; value <= 255; value++)
            {
                this.ChangeRecvCopy(index, value);
                unk_results = err_corrector.BuildUnknownResults(this.recv_copy, max_root_alpha_exp);
                correctable_byte_arr = err_corrector.FindCorrectableByteLocations(unk_results, max_root_alpha_exp);
                if (correctable_byte_arr.size() > 0)
                {
                        corrections = err_corrector.CorrectCorrectableBytes(correctable_byte_arr, max_root_alpha_exp, this.recv_copy.length);
                        System.out.println("Process: index=" + index + " value=" + value);
                        err_corrector.DebugPrint_Corrections(corrections);
                        return;
                }
            }
        }
    }//Process
    
}//class
