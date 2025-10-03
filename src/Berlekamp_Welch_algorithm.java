import java.util.ArrayList;
import java.util.Scanner;


/********************************************
The Berlekamp–Welch algorithm, also known as the Welch–Berlekamp algorithm, 
is named for Elwyn R. Berlekamp and Lloyd R. Welch. 
This is a decoder algorithm that efficiently corrects errors in Reed–Solomon codes for an RS(n, k), 
code based on the Reed Solomon original view where a message 
m1, ..., mk is used as coefficients of a polynomial F(ai)

The goal of the decoder is to recover the original encoding polynomial 

 ********************************************/



public class Berlekamp_Welch_algorithm {
    
    //n == 7 == message data bytes + E.C. bytes
    //k == 3 == message data bytes
    //so floor((n-k)/2)== maximum number of errors that can be corrected is 2
    
    //b is the received bytes, do not use b[0] so we will not be confused, b will have length==8
    //recv_max_index = b.length - 1;
    
    //e_count is the number of errors that can be corrected and detected
    //so if e_count==2 then we will have e0 and e1 and constrain e2==1 
    //so we will do
    //int[] e = new int[2]; // size is 2 for e[0] and e[1] and e[2]==1 constraint
    
    //a is the inputs, ai = i-1 : {0,1,2,3,4,5,6 ..... i-1}, do not use a[0] so we will not be confused
    //a will be same size of b
    //so
    //int[] a = new int[b.length];
    //it also uses recv_max_index
    
    //answer_matrix is rows==b.length x columns=1 represented by an array where answer_matrix[0] is not used so we will not be confused
    //int[] answer_matrix = new int[b.length];
    //it holds values -b[1]a[1]^2 .....-b[recv_max_index]a[recv_max_index]^2
    
    //int q_max_index = recv_max-index - e_count - 1
    //int[] q = new int[recv_max-index - e_count] we will use q[0]
    
    //so if e_count is 2
    //we calculate rows=recv_max_index x columns=1  matrix
    // e[0]
    // e[1]
    // q[0]
    // q[1]
    // q[2]
    // q[3]
    // q[4]
    //i guess we can call this matrix question_matrix
    
    //int[][] matrix is the matrix we try to get to the identity-matrix so that
    //
    //matrix x question_matrix == answer_matrix
    //
    //
    //matrix = b[1]  b[1]a[1]  -1  -a[1]  -a[1]^2  -a[1]^3  -a[1]^4
    //         b[2]  b[2]a[2]  -1  -a[2]  -a[2]^2  -a[2]^3  -a[2]^4
    //         b[3]  b[3]a[3]  -1  -a[3]  -a[3]^2  -a[3]^3  -a[3]^4
    //         b[4]  b[4]a[4]  -1  -a[4]  -a[4]^2  -a[4]^3  -a[4]^4
    //         b[5]  b[5]a[5]  -1  -a[5]  -a[5]^2  -a[5]^3  -a[5]^4
    //         b[6]  b[6]a[6]  -1  -a[6]  -a[6]^2  -a[6]^3  -a[6]^4
    //         b[7]  b[7]a[7]  -1  -a[7]  -a[7]^2  -a[7]^3  -a[7]^4
    //
    //int[][] matrix = new int[b.length][b.length]
    //and when we access matrix
    //we use
    //for (int row=1; row <= recv_max_index; row++)
    //for (int col=1; col <= recv_max_index; col++)
    //so do not use matrix[0][anything] or matrix[anything][0]
    
    int GF;

    int[] b = null;          //DO NOT USE b[0]
    int recv_max_index = 0;
    
    int e_count = 0;
    int[] e = null;
    
    int[] a = null;          //DO NOT USE a[0]
    
    int[] answer_matrix = null;
    
    int[][] matrix = null;
    
    public Berlekamp_Welch_algorithm(int gf_val, int[] temp_b, int temp_e_count)
    {
        GF = gf_val;
        b = temp_b; //do not use b[0]
        recv_max_index = b.length - 1;
        
        e_count = temp_e_count;
        e = new int[e_count];
        
        a = new int[b.length]; //do not use a[0]
        a[0] = Integer.MAX_VALUE; //do not use a[0] just try to mark it as not used
        for (int ii=1; ii <= recv_max_index; ii++)
            a[ii] = ii - 1;
        
        //answer_matrix is rows==b.length x columns=1 represented by an array 
        //where answer_matrix[0] is not used so we will not be confused
        answer_matrix = new int[b.length];
        //it holds values -b[1]a[1]^2 .....-b[recv_max_index]a[recv_max_index]^2
        for (int ii=1; ii <= recv_max_index; ii++)
            answer_matrix[ii] = GF_value(-1 * b[ii] * a[ii] * a[ii]);
        
        
        matrix = new int[b.length][b.length];
        //so do not use matrix[0][anything] or matrix[anything][0] just try to mark it as not used
        for (int ii=0; ii <= recv_max_index; ii++)
        {
            matrix[0][ii] = Integer.MAX_VALUE;
            matrix[ii][0] = Integer.MAX_VALUE;
        }
        
        
        //matrix = b[1]  b[1]a[1]  -1  -a[1]  -a[1]^2  -a[1]^3  -a[1]^4
        //         b[2]  b[2]a[2]  -1  -a[2]  -a[2]^2  -a[2]^3  -a[2]^4
        //         b[3]  b[3]a[3]  -1  -a[3]  -a[3]^2  -a[3]^3  -a[3]^4
        //         b[4]  b[4]a[4]  -1  -a[4]  -a[4]^2  -a[4]^3  -a[4]^4
        //         b[5]  b[5]a[5]  -1  -a[5]  -a[5]^2  -a[5]^3  -a[5]^4
        //         b[6]  b[6]a[6]  -1  -a[6]  -a[6]^2  -a[6]^3  -a[6]^4
        //         b[7]  b[7]a[7]  -1  -a[7]  -a[7]^2  -a[7]^3  -a[7]^4
        for (int row=1; row <= recv_max_index; row++)
        {
            matrix[row][1] = GF_value(b[row]);
            matrix[row][2] = GF_value(b[row] * a[row]);
            matrix[row][3] = GF_value(-1);
            matrix[row][4] = GF_value(-1 * a[row]);
            matrix[row][5] = GF_value(-1 * GF_pow(a[row],2));
            matrix[row][6] = GF_value(-1 * GF_pow(a[row],3));
            matrix[row][recv_max_index] = GF_value(-1 * GF_pow(a[row],4));
        }
        
    }//constructor
    
    
    private int GF_value(int temp_decimal)
    {
        int answer = temp_decimal % GF;
        if (answer < 0)
            answer += GF;
        return answer;
    }//GF_value
    
    private int GF_pow(int num, int exponent)
    {
        int temp = 1;
        for (int ii=1; ii <= exponent; ii++)
            temp = GF_value(temp * num);
        
        return temp;
    }//GF_pow
    
    public void Debug_Print()
    {
        System.out.println();
        for (int row=1; row <= recv_max_index; row++)
        {
            for (int col=1; col <= recv_max_index; col++)
            {
                System.out.print(matrix[row][col] + "  ");
                if (col == recv_max_index)
                    System.out.print("     " + answer_matrix[row]);
            }
            System.out.println();
        }
    }//Debug_Print
    
    
    public void MultiplyAndAdd_GF(int row_mult, int mult_val, int row_add)
    {
        for (int col=1; col <= recv_max_index; col++)
        {
            int temp = matrix[row_mult][col];
            temp *= mult_val;
            temp += matrix[row_add][col];
            temp = GF_value(temp);
            matrix[row_add][col] = temp;
        }
        
        int temp = answer_matrix[row_mult];
        temp *= mult_val;
        temp += answer_matrix[row_add];
        temp = GF_value(temp);
        answer_matrix[row_add] = temp;
    }//MultiplyAndAdd_GF
    
    
    public void Multiply_Row_GF(int row, int val)
    {
        for (int col=1; col <= recv_max_index; col++)
        {
            int temp = matrix[row][col];
            temp *= val;
            temp = GF_value(temp);
            matrix[row][col] = temp;
        }
        
        int temp = answer_matrix[row];
        temp *= val;
        temp = GF_value(temp);
        answer_matrix[row] = temp;
    }//Multiply_Row_GF
    
    
    public void Add_RowA_By_RowB_GF(int RowA, int RowB)
    {
        for (int col=1; col <= recv_max_index; col++)
        {
            int Aval = matrix[RowA][col];
            int Bval = matrix[RowB][col];
            int temp = Aval + Bval;
            temp = GF_value(temp);
            matrix[RowA][col] = temp;
        }
        
        int Aval = answer_matrix[RowA];
        int Bval = answer_matrix[RowB];
        int temp = Aval + Bval;
        temp = GF_value(temp);
        answer_matrix[RowA] = temp;
    }//Add_RowA_By_RowB_GF
    
    
    public void Manual_Console_Solve()
    {   
        Scanner scanner = new Scanner(System.in);
           
        String str = "";
        while (str.equals("x") == false)
        {
            Debug_Print();
            
            System.out.println();
            System.out.println("Enter m,x,y: Multiply row-x by y (GF7)");
            System.out.println("Enter a,x,y: Add row-x by row-y (GF7)");
            System.out.println("Enter ma,x,y,z: Multiply row-x by y then add it to row-z(GF7)");
            System.out.println("Enter x: Exit");
            System.out.println("Enter:");
            
            str = scanner.nextLine();
            
            String[] temp = str.split(",");
            if (temp[0].equals("m"))
                Multiply_Row_GF(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
            else if (temp[0].equals("a"))
                Add_RowA_By_RowB_GF(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
            else if (temp[0].equals("ma"))
                MultiplyAndAdd_GF(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3]));
            else if (temp[0].equals("x") == false)
            {
                System.out.println("BAD STRING ENTERED!!!!!");
                System.out.println();
            }
        }
        
    }//Manual_Console_Solve
    
    
}//class
