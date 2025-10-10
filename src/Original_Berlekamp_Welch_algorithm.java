import java.util.ArrayList;
import java.util.Scanner;
import java.math.BigInteger;

/********************************************
The Berlekamp–Welch algorithm, also known as the Welch–Berlekamp algorithm, 
is named for Elwyn R. Berlekamp and Lloyd R. Welch. 
This is a decoder algorithm that efficiently corrects errors in Reed–Solomon codes for an RS(n, k), 
code based on the Reed Solomon original view where a message 
m1, ..., mk is used as coefficients of a polynomial F(ai)

The goal of the decoder is to recover the original encoding polynomial 

 ********************************************/



public class Original_Berlekamp_Welch_algorithm {
    
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
    
    private Integer[] GF7_MultiplicativeInverse_1 = { Integer.MIN_VALUE, Integer.MIN_VALUE, 4, 5, 2, 3, 6 };
    
    int GF;

    int[] b = null;          //DO NOT USE b[0]
    int recv_max_index = 0;
    
    int e_count = 0;
    int[] e = null;
    
    int[] a = null;          //DO NOT USE a[0]
    
    int[] answer_matrix = null;
    
    int[][] matrix = null;
    
    int q_max_index = 0;
    int[] q = null;
    
    //F is calculated after identity-matrix solved if solvable
    ArrayList<Integer> F = null;
    ArrayList<Integer> E = null;
    
    public Original_Berlekamp_Welch_algorithm(int gf_val, int[] temp_b, int temp_e_count) throws  Exception
    {
        GF = gf_val;
        b = temp_b; //do not use b[0]
        recv_max_index = b.length - 1;
        
        e_count = temp_e_count;
        e = new int[e_count];
        
        q_max_index = recv_max_index - e_count - 1;
        q = new int[recv_max_index - e_count];// we will use q[0]
        
        Tools tool = new Tools();
        a = new int[b.length]; //DO NOT USE a[0]
        a[0] = Integer.MAX_VALUE; //DO NOT USE a[0] just try to mark it as not used
        for (int ii=1; ii <= recv_max_index; ii++)
        {
            a[ii] = ii-1; //0,1,2,3,4,5,6,7,8 ..... ii-1
        }
        
        //answer_matrix is rows==b.length x columns=1 represented by an array 
        //where answer_matrix[0] is not used so we will not be confused
        answer_matrix = new int[b.length];
        //it holds values -b[1]a[1]^2 .....-b[recv_max_index]a[recv_max_index]^2
        for (int ii=1; ii <= recv_max_index; ii++)
        {
            answer_matrix[ii] = GF_value(-1 * b[ii] * a[ii] * a[ii]);
        }
        
        
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
            matrix[row][1] = b[row];
            matrix[row][2] = GF_value(b[row] * a[row]);
            matrix[row][3] = GF_value(-1);
            matrix[row][4] = GF_value(-1 * a[row]);
            matrix[row][5] = GF_value(-1 * GF_pow(a[row],2));
            matrix[row][6] = GF_value(-1 * GF_pow(a[row],3));
            for (int col=7; col <= recv_max_index; col++)
                matrix[row][col] = GF_value(-1 * GF_pow(a[row],col-3));
        }//for row
    }//constructor
    
    
    private int GF_pow(int num, int exponent) throws Exception
    {
        int temp = 1;
        for (int ii=1; ii <= exponent; ii++)
            temp = GF_value(temp * num);
        return temp;
    }//GF_pow
    
    
    private int GF_value(int temp_decimal)
    {
        int answer = temp_decimal % GF;
        if (answer < 0)
            answer += GF;
        return answer;
    }//GF_value
    
    public void Debug_Print()
    {
        System.out.println();
        for (int row=1; row <= recv_max_index; row++)
        {
            System.out.print(" |");
            for (int col=1; col <= recv_max_index; col++)
            {
                String tempstr = Integer.toString(matrix[row][col]);
                if (tempstr.length() == 3)
                    tempstr = "  " + tempstr;
                else if (tempstr.length() == 2)
                    tempstr = "   " + tempstr;
                else if (tempstr.length() == 1)
                    tempstr = "    " + tempstr;
                System.out.print(tempstr);
            }
            
            System.out.printf("|   |%3d|", answer_matrix[row]);
            System.out.println();
        }
    }//Debug_Print
    
    
    public void MultiplyAndAdd(int row_mult, int mult_val, int row_add)
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
    }//MultiplyAndAdd
    
    
    public void Multiply_Row(int row, int val)
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
    }//Multiply_Row
    
    
    public void Add_RowA_By_RowB(int RowA, int RowB)
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
    }//Add_RowA_By_RowB
    
    
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
                Multiply_Row(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
            else if (temp[0].equals("a"))
                Add_RowA_By_RowB(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
            else if (temp[0].equals("ma"))
                MultiplyAndAdd(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3]));
            else if (temp[0].equals("x") == false)
            {
                System.out.println("BAD STRING ENTERED!!!!!");
                System.out.println();
            }
        }//while
    }//Manual_Console_Solve
    
    private boolean Check_Identity_Matrix()
    {
        for (int row=1; row <= recv_max_index; row++)
        {
            for (int col=1; col <= recv_max_index; col++)
            {
                if (matrix[row][col] != 1 && row==col)
                    return false;
                else if (matrix[row][col] != 0 && row!=col)
                    return false;
            }
        }
        return true;
    }//Check_Identity_Matrix    
    
    public boolean Robot_Solve() throws Exception
    {   
        int ii=1;
        Debug_Print();
        while (true)
        {   
            boolean good = false;
            if (GF == 7)
                good = GF7_Make_Column_Good_1_And_0s(ii);
            else if (GF == 256)
                good = GF256_Make_Column_Good_1_And_0s(ii);
            else
                throw new Exception("Robot_Solve: only GF(7) or GF(256) supported for now.");
            if (!good)
                return false;
            
            Debug_Print();
            
            ii++;
            if (ii==(recv_max_index + 1))
                break;
        }
        
        boolean tempbool = Check_Identity_Matrix();
        return tempbool;
    }//Robot_Solve
    
    
    public void Fill_QuestionMatrix_With_AnswerMatrix()
    {
        int ii=1; //answer_matrix[0] is not used
        
        for (int err=0; err < e_count; err++)
        {
            e[err] = answer_matrix[ii];
            ii++;
        }
        for (int qii=0; qii < q.length; qii++)
        {
            q[qii] = answer_matrix[ii];
            ii++;
        }
    }//Fill_QuestionMatrix_With_AnswerMatrix
    
    public void Debug_Print_Q_And_E_Functions()
    {
        System.out.println();
        System.out.print("Q(ai) = ");
        for (int qii=q_max_index; qii >= 0; qii--) //q uses q[0], q[0] is coefficient for x^0
        {
            System.out.print(q[qii] + "x^" + qii);
            if (qii != 0)
                System.out.print(" + ");
        }
        
        System.out.println();
        System.out.print("E(ai) = ");
        for (int eii=e_count; eii >= 0; eii--) //e uses e[0], e[0] is coefficient for x^0, e[e_count] is constrained to 1 and is the coefficient for x^e_count
        {
            if (eii == e_count)
                System.out.print("1x^" + eii + " + ");
            else
            {
                System.out.print(e[eii] + "x^" + eii);
                if (eii != 0)
                    System.out.print(" + ");
            }
        }
        System.out.println();
    }//Debug_Print_Q_And_E_Functions
    
    public void GF_Polynomial_Long_Division_To_Find_F_Function()
    {
        //q uses q[0], q[0] is coefficient for x^0
        //e uses e[0], e[0] is coefficient for x^0, e[e_count] is constrained to 1 and is the coefficient for x^e_count
        //F(ai) = Q(ai) / E(ai)
        //
        //example:
        //Q(ai) = 3x^4 + 1x^3 + 3x^2 + 3x + 4
        //E(ai) = 1x^2 + 2x + 4
        //
        //
        //F(ai) = 3x^2  2x    1
        //        --------------------------------
        //        3x^4  1x^3  3x^2  3x  4
        //        3x^4  6x^3  12x^2
        //        --------------------------------
        //              2x^3  5x^2  3x  4
        //              2x^3  4x^2  8x
        //              --------------------------
        //                    1x^2  2x  4
        //                    1x^2  2x  4
        //                    --------------------
        //                          
        F = new ArrayList<Integer>();
        E = new ArrayList<Integer>();
        ArrayList<Integer> dividend = new ArrayList<Integer>();
        
        for (int qii=q_max_index; qii >= 0; qii--) //q uses q[0], q[0] is coefficient for x^0
            dividend.add(q[qii]);
        
        for (int eii=e_count; eii >= 0; eii--) //e uses e[0], e[0] is coefficient for x^0, e[e_count] is constrained to 1 and is the coefficient for x^e_count
        {
            if (eii == e_count)
                E.add(1);
            else
                E.add(e[eii]);
        }
        
        int E_term_length = e_count + 1;
        
        while (dividend.size() >= E_term_length)
        {
            int mult=0;
            for (int ii=0; ii < E.size(); ii++)
            {
                if (ii==0)
                {
                    mult = dividend.get(ii);
                    F.add(mult);
                    dividend.set(ii, 0);
                }
                else
                {
                    int temp_dividend = dividend.get(ii);
                    int temp_E = E.get(ii);
                    int temp = mult * temp_E;
                    temp = temp_dividend - temp;
                    temp = GF_value(temp);
                    dividend.set(ii, temp);
                }
            }
            
            dividend.remove(0);
        }
        
        System.out.println();
        System.out.print("F(ai) = ");
        for (int ii=0; ii < F.size(); ii++)
        {
            System.out.print(F.get(ii) + "x^" + (F.size() - ii - 1));
            if (ii != (F.size() - 1))
                 System.out.print(" + ");
        }
        System.out.println();
        System.out.print("Remainder = ");
        for (int ii=0; ii < dividend.size(); ii++)
        {
            System.out.print(dividend.get(ii) + "x^" + (dividend.size() - ii - 1));
            if (ii != (dividend.size() - 1))
                System.out.print(" + ");
        }
    }//GF_Polynomial_Long_Division_To_Find_F_Function
    
    public ArrayList<String> Find_Error_Locations_And_Corrections() throws Exception
    {   
        if (GF==256)
            throw new Exception("Find_Error_Locations_And_Corrections: GF(256) is used but this function is for non-GF(256)");
        
        //error location found if E(ai)==0 and i==location of error in received-bytes
        //and correction is F(ai)
        ArrayList<String> locandcorrect = new ArrayList<String>();
        
        for (int ii=1; ii <= recv_max_index; ii++)
        {
            //calculate E(ai)
            int sum=0;
            for (int eterm=0; eterm < E.size(); eterm++)
            {
                int coeff = E.get(eterm);
                int x_exp = E.size() - eterm - 1;
                int x = a[ii];
                int temp = coeff * (int)Math.pow(x, x_exp);
                sum += temp;
            }
            int E_val = GF_value(sum);
            if (E_val==0) //found error location == ii
            {
                //find correction
                sum = 0;
                for (int f_term=0; f_term < F.size(); f_term++)
                {
                    int coeff = F.get(f_term);
                    int x_exp = F.size() - f_term - 1;
                    int x = a[ii];
                    int temp = coeff * (int)Math.pow(x, x_exp);
                    sum += temp;
                }
                int correction = GF_value(sum);
                locandcorrect.add(ii + "," + correction);
                
            }////found error location == ii
        }//ai loop
        
        return locandcorrect;
    }//Find_Error_Locations_And_Corrections
    
    
    private int FindRowBelowDiagWhoseColCellIs_1(int diag)
    {
        for (int row=diag+1; row <= recv_max_index; row++)
        {
            if (matrix[row][diag] == 1)
                return row;
        }
        return -1;
    }//FindRowBelowDiagWhoseColCellIs_1
    
    
    private boolean GF7_Make_Column_Good_1_And_0s(int ii)
    {
        boolean good = false;
        for (int row=1; row <= recv_max_index; row++)
        {
            if (row==ii && matrix[row][ii] == 1)
                good = true;
            else if (row!=ii && matrix[row][ii] == 0)
                good = true;
            else
            {
                good = false;
                break;
            }
        }
        
        if (good) //this column is good already
            return true;
        
        //if column all 0s, can not make it good
        int zero_count=0;
        for (int row=1; row <= recv_max_index; row++)
        {
            if (matrix[row][ii] == 0)
                zero_count++;
        }
        if (zero_count == recv_max_index)
            return false; //can not make it good
        
        
        //make matrix[ii][ii]==1
        if (matrix[ii][ii] != 1)
        {
            if (matrix[ii][ii] != 0)
            {
                 //if 2 then GF_value(8)==1
                 //if 3 then GF_value(15)==1
                 //if 4 then GF_value(8)==1
                 //if 5 then GF_value(15)==1
                 //if 6 then GF_value(36)==1
                 //so at some multiple of temp the GF_Value will be 1
                 int temp = matrix[ii][ii]; //temp!=1 and temp!=0
                 int mult = this.GF7_MultiplicativeInverse_1[temp];
                 try
                 {
                     Multiply_Row(ii, mult);
                 }
                 catch (Exception ex)
                 {
                     ex.printStackTrace();
                     return false;
                 }
            }
            else //matrix[ii][ii] == 0
            {
                //find another row after this row==ii
                //whose matrix[row][ii] != 0 and row <= recv_max_index
                //add that row here
                //then do little algorithm above
                int row = ii;
                while (matrix[row][ii] == 0 && row <= recv_max_index)
                    row++;
                if (row > recv_max_index)
                        return false;
                try
                {
                     Add_RowA_By_RowB(ii, row);
                }
                catch (Exception ex)
                {
                     ex.printStackTrace();
                     return false;
                }
                if (matrix[ii][ii] != 1) //massage to 1
                {
                    //if 2 then GF_value(8)==1
                    //if 3 then GF_value(15)==1
                    //if 4 then GF_value(8)==1
                    //if 5 then GF_value(15)==1
                    //if 6 then GF_value(36)==1
                    //so at some multiple of temp the GF_Value will be 1
                    int temp = matrix[ii][ii]; //temp!=1 and temp!=0
                    int mult = this.GF7_MultiplicativeInverse_1[temp];
                    try
                    {
                        Multiply_Row(ii, mult);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        return false;
                    }
                }//massage to 1
            }//matrix[ii][ii] == 0
        }//make matrix[ii][ii]==1
        
        //ok matrix[ii][ii]==1
        //make other elements in this column 0
        //but can not simple multiply by GF because it will make that whole row 0
        for (int row=1; row <= recv_max_index; row++)
        {
            if (row != ii)
            {
                if (matrix[row][ii] != 0)
                {
                    //make it 0
                    int temp = matrix[row][ii];
                    int delta = GF - temp;
                    //multiply good row by delta and add it to this row
                    try
                    {
                        MultiplyAndAdd(ii, delta, row);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        return false;
                    }
                }
            }
        }
        
        return true;
    }//GF7_Make_Column_Good_1_And_0s
    
    
    private boolean GF256_Make_Column_Good_1_And_0s(int ii)
    {
        boolean good = false;
        for (int row=1; row <= recv_max_index; row++)
        {
            if (row==ii && matrix[row][ii] == 1)
                good = true;
            else if (row!=ii && matrix[row][ii] == 0)
                good = true;
            else
            {
                good = false;
                break;
            }
        }
        
        if (good) //this column is good already
            return true;
        
        //if column all 0s, can not make it good
        int zero_count=0;
        for (int row=1; row <= recv_max_index; row++)
        {
            if (matrix[row][ii] == 0)
                zero_count++;
        }
        if (zero_count == recv_max_index)
            return false; //can not make it good
        
        
        //make matrix[ii][ii]==1
        if (matrix[ii][ii] != 1)
        {
            int row_exists = FindRowBelowDiagWhoseColCellIs_1(ii);
            if (row_exists != -1)
                this.Add_RowA_By_RowB(ii, row_exists);
            else if (matrix[ii][ii] != 0)
            {
                 //if 2 then GF_value(8)==1
                 //if 3 then GF_value(15)==1
                 //if 4 then GF_value(8)==1
                 //if 5 then GF_value(15)==1
                 //if 6 then GF_value(36)==1
                 //so at some multiple of temp the GF_Value will be 1
                 int temp = matrix[ii][ii]; //temp!=1 and temp!=0
                 temp *= (GF + 1);
                 try
                 {
                     Multiply_Row(ii, temp);
                 }
                 catch (Exception ex)
                 {
                     ex.printStackTrace();
                     return false;
                 }
            }
            else //matrix[ii][ii] == 0
            {
                //find another row after this row==ii
                //whose matrix[row][ii] != 0 and GF_value(matrix[row][ii] + matrix[ii][ii]) != 0 and row <= recv_max_index
                //add that row here
                //then do little algorithm above
                int row = ii+1;
                int sumgf = GF_value(matrix[row][ii] + matrix[ii][ii]);
                while (matrix[row][ii] == 0 && sumgf == 0)
                {
                    row++;
                    if (row > recv_max_index)
                        return false;
                }
                try
                {
                     Add_RowA_By_RowB(ii, row);
                }
                catch (Exception ex)
                {
                     ex.printStackTrace();
                     return false;
                }
                if (matrix[ii][ii] != 1) //massage to 1
                {
                    //if 2 then GF_value(8)==1
                    //if 3 then GF_value(15)==1
                    //if 4 then GF_value(8)==1
                    //if 5 then GF_value(15)==1
                    //if 6 then GF_value(36)==1
                    //so at some multiple of temp the GF_Value will be 1
                    int temp = matrix[ii][ii]; //temp!=1 and temp!=0
                    int temp_ii=1;
                    while (GF_value(temp) != 1)
                    {
                        temp_ii++;
                        temp = matrix[ii][ii] * temp_ii;
                    }
                    try
                    {
                        Multiply_Row(ii, temp_ii);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        return false;
                    }
                }//massage to 1
            }//matrix[ii][ii] == 0
        }//make matrix[ii][ii]==1
        
        //ok matrix[ii][ii]==1
        //make other elements in this column 0
        //but can not simple multiply by GF because it will make that whole row 0
        for (int row=1; row <= recv_max_index; row++)
        {
            if (row != ii)
            {
                if (matrix[row][ii] != 0)
                {
                    //make it 0
                    int temp = matrix[row][ii];
                    int delta = GF - temp;
                    //multiply good row by delta and add it to this row
                    try
                    {
                        MultiplyAndAdd(ii, delta, row);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        return false;
                    }
                }
            }
        }
        
        return true;
    }//GF256_Make_Column_Good_1_And_0s
    
    
    
    
}//class
