import java.util.ArrayList;

/********************************************
The Berlekamp–Welch algorithm, also known as the Welch–Berlekamp algorithm, 
is named for Elwyn R. Berlekamp and Lloyd R. Welch. 
This is a decoder algorithm that efficiently corrects errors in Reed–Solomon codes for an RS(n, k), 
code based on the Reed Solomon original view where a message 
m1, ..., mk is used as coefficients of a polynomial F(ai)

The goal of the decoder is to recover the original encoding polynomial 

 ********************************************/



public class GF7_Matrix_Math {
    
    ArrayList<ArrayList<Integer>> Matrix = null;
    ArrayList<Integer> Result_Column = null;
    
    //top row is index 0
    public GF7_Matrix_Math(ArrayList<ArrayList<Integer>> matrix, ArrayList<Integer> result_column)
    {
        Matrix = matrix;
        Result_Column = result_column;
    }//constructor
    
    
}//class
