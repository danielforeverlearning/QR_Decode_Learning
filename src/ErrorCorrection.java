
import java.util.ArrayList;

public class ErrorCorrection {

    private Tools tool = new Tools();

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
    public int CalculateSyndrome(Integer[] recv, int root_alpha_exp) {

        ArrayList<Integer> arr = new ArrayList<Integer>();
        for (int ii = 0; ii < recv.length; ii++) {
            int x_exp = recv.length - ii - 1;
            //when you calculate syndromes which should be 0 you use x=power of 2 in GF(256) example 2^0 2^1 2^2 2^3 2^4 2^5
            //it is 0 because when you made generator-polynomial
            //(x - 2^0)(x - 2^1)(x - 2^2)(x - 2^3).....

            int coeff_decimal = recv[ii];
            //alpha==2

            if (coeff_decimal != 0) {
                int coeff_alphaexp = tool.Table_Integer_To_Exponent_Of_Alpha()[coeff_decimal];

                //for now root is (x - 2^0) so x==2^0,      (2^a)^b == 2^(a*b)
                int exp_sum = coeff_alphaexp + (root_alpha_exp * x_exp);
                if (exp_sum >= 256) {
                    exp_sum %= 255;
                }
                int temp = tool.Table_Exponent_Of_Alpha_To_Integer()[exp_sum];

                arr.add(temp);
            }
        }

        int temp = 0;
        for (int ii = 0; ii < arr.size(); ii++) {
            temp ^= arr.get(ii);
        }

        return temp;
    }//CalculateSyndrome

    public int FindBadSyndromeCoeffs(Integer[] recv, int x_to_the, int unknown_ii) {
        int poly_exp = 0;
        ArrayList<Integer> arr = new ArrayList<Integer>();
        for (int ii = 0; ii < recv.length; ii++) {
            int x_exp = recv.length - ii - 1;
            if (ii == unknown_ii) {
                poly_exp = (x_to_the * x_exp);
                if (poly_exp >= 256) {
                    poly_exp %= 255;
                }
            } else {
                int coeff_decimal = recv[ii];
                //alpha==2

                if (coeff_decimal != 0) {
                    int coeff_alphaexp = tool.Table_Integer_To_Exponent_Of_Alpha()[coeff_decimal];

                    //for now root is (x - 2^0) so x==2^0,      (2^a)^b == 2^(a*b)
                    int exp_sum = coeff_alphaexp + (x_to_the * x_exp);
                    if (exp_sum >= 256) {
                        exp_sum %= 255;
                    }
                    int temp = tool.Table_Exponent_Of_Alpha_To_Integer()[exp_sum];

                    arr.add(temp);
                }
            }
        }//for

        int tempint = 0;
        for (int ii = 0; ii < arr.size(); ii++) {
            tempint ^= arr.get(ii);
        }

        int temp_exp = tool.Table_Integer_To_Exponent_Of_Alpha()[tempint];

        //remember it is GF256 math
        //(unknown_ss * 2^poly_exp) XOR 2^temp_exp == 0
        //(unknown_ss * 2^poly_exp)                == 2^temp_exp
        //
        //unknown_ss == 2^unknown_exp
        //
        //(2^unknown_exp * 2^poly_exp)             == 2^temp_exp
        //
        //unknown_exp + poly_exp == temp_exp
        int unknown_exp = temp_exp - poly_exp;
        if (unknown_exp < 0) {
            unknown_exp += 255;
        } else {
            unknown_exp %= 255;
        }
        return unknown_exp;
    }//FindBadSyndromeCoeffs

    public ArrayList<ArrayList<Integer>> BuildUnknownResults(Integer[] received_block_codewords, int max_root_alpha_exp) {
        int last_res = 0;
        ArrayList<ArrayList<Integer>> unk_results = new ArrayList<ArrayList<Integer>>();
        for (int unknown_ii = 0; unknown_ii < received_block_codewords.length; unknown_ii++) {
            ArrayList<Integer> results = new ArrayList<Integer>();
            for (int x_to_the = 0; x_to_the <= max_root_alpha_exp; x_to_the++) {
                int res = FindBadSyndromeCoeffs(received_block_codewords, x_to_the, unknown_ii);
                results.add(res);
                if (x_to_the == 0) {
                    last_res = res;
                } 
                else {
                    if (last_res != res) {
                        break;
                    }
                }
            }
            unk_results.add(results);
        }
        return unk_results;
    }//BuildUnknownResults

    public void DebugPrint_UnknownResults(ArrayList<ArrayList<Integer>> unk_results) {
        System.out.println();
        for (int unkpos = 0; unkpos < unk_results.size(); unkpos++) {
            System.out.print("unkpos" + unkpos + ":");
            ArrayList<Integer> results = unk_results.get(unkpos);
            for (int ii = 0; ii < results.size(); ii++) {
                System.out.printf("%5d ", results.get(ii));
            }
            System.out.println();
        }
        System.out.println();
    }//DebugPrint_UnknownResults

    public ArrayList<String> FindCorrectableByteLocations(ArrayList<ArrayList<Integer>> unk_results, int max_root_alpha_exp) {
        ArrayList<String> arr = new ArrayList<String>();
        for (int unknown_ii = 0; unknown_ii < unk_results.size(); unknown_ii++) {
            ArrayList<Integer> syndrome_arr = unk_results.get(unknown_ii);
            int syndrome_arr_size = syndrome_arr.size();
            if ((max_root_alpha_exp + 1) == syndrome_arr_size) {
                int ss = 0;
                int last_res = syndrome_arr.get(0);
                for (ss = 0; ss < syndrome_arr_size; ss++) {
                    int temp = syndrome_arr.get(ss);
                    if (temp != last_res) {
                        break;
                    }
                }
                if (ss == syndrome_arr_size) {
                    String str = unknown_ii + "," + last_res;
                    arr.add(str);
                }
            }
        }
        return arr;
    }//FindCorrectableByteIndices

    public ArrayList<String> CorrectCorrectableBytes(ArrayList<String> correctable_byte_arr, int max_root_alpha_exp, int recv_length) {
        ArrayList<String> corrections = new ArrayList<String>();
        for (int ii = 0; ii < correctable_byte_arr.size(); ii++) {
            String str = correctable_byte_arr.get(ii);
            String[] strarray = str.split(",");
            Integer byteloc = Integer.parseInt(strarray[0]);
            Integer unknown_good_coeff_as_alphaexp = Integer.parseInt(strarray[1]);
            int unknown_good_coeff_decimal = tool.Table_Exponent_Of_Alpha_To_Integer()[unknown_good_coeff_as_alphaexp];
            corrections.add(byteloc + "," + unknown_good_coeff_decimal);
        }
        return corrections;
    }//CorrectCorrectableBytes

    public void DebugPrint_Corrections(ArrayList<String> corrections) {
        for (int ii = 0; ii < corrections.size(); ii++) {
            String str = corrections.get(ii);
            String[] strarray = str.split(",");
            Integer byteloc = Integer.parseInt(strarray[0]);
            Integer coeff = Integer.parseInt(strarray[1]);
            System.out.println("DebugPrint_Corrections:byteloc=" + byteloc + " coeff=" + coeff);
        }
    }//DebugPrint_Corrections
}//class
