
/**************************************************
The Berlekamp-Massey algorithm finds the shortest linear feedback shift register (LFSR) 
that generates a given sequence. 
It iteratively updates a connection polynomial and its associated length. 
Here is a pseudocode representation of the algorithm: 


Algorithm Berlekamp-Massey(sequence S)
  Input: A sequence S = (s_0, s_1, ..., s_{N-1}) over a finite field F.
  Output: The minimal connection polynomial C(x) and its linear complexity L.

  Initialize:
    C(x) = 1  (Current connection polynomial)
    L = 0     (Current linear complexity)
    B(x) = 1  (Previous connection polynomial when L was last updated)
    m = -1    (Index of the last position where L was updated)
    b = 1     (Discrepancy at the last position where L was updated)

  For n from 0 to N-1:
    Calculate discrepancy delta_n:
      delta_n = s_n  (If n < L, this is the initial term)
      For i from 1 to L:
        delta_n = delta_n + C_i * s_{n-i}  (C_i is the coefficient of x^i in C(x))

    If delta_n != 0:
      Temp_C(x) = C(x)
      C(x) = C(x) + (delta_n / b) * x^(n - m) * B(x)

      If 2 * L <= n:
        L = n + 1 - L
        B(x) = Temp_C(x)
        b = delta_n
        m = n

  Return C(x), L



Explanation of Variables:
S: The input sequence for which the minimal polynomial is to be found.
C(x): The current connection polynomial. This polynomial defines the recurrence relation for the sequence.
L: The current linear complexity, which is the degree of the current minimal connection polynomial.
B(x): A copy of C(x) from the last time L was updated. 
m: The index in the sequence where L was last updated.
b: The discrepancy calculated at position m.
n: The current index being processed in the sequence.
delta_n: The discrepancy at the current position n, calculated as the difference between the actual s_n and the value predicted by C(x).
Temp_C(x): A temporary variable to store the old C(x) before it is updated.



Key Steps:
(1) Initialization: Set initial values for C(x), L, B(x), m, and b.
(2) Iterate through the sequence: For each element s_n in the sequence:
    (a) Calculate Discrepancy: Compute delta_n, which indicates if the current C(x) correctly predicts s_n.
    (b) Update Polynomials: If delta_n is non-zero, indicating a prediction error, update C(x) using B(x) and delta_n.
    (c) Update Linear Complexity: If the update to C(x) results in a new, longer connection polynomial (2 * L <= n), then update L, B(x), b, and m to reflect this change.
(3) Return: After processing the entire sequence, C(x) and L represent the minimal connection polynomial and its linear complexity.




*************************************************/
public class Berlekamp_Massey_Algorithm {
    
}
