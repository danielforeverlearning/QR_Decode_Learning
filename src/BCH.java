

/**********************************************************************
BCH codes:
In this section, we introduce a general class of error correction codes: 
the BCH codes, the parent family of modern Reed–Solomon codes, 
and the basic detection and correction mechanisms.

The formatting information is encoded with a BCH code which allows a 
certain number of bit-errors to be detected and corrected. 
BCH codes are a generalization of Reed–Solomon codes (modern Reed–Solomon codes are BCH codes). 
In the case of QR codes, the BCH code used for the format information is much simpler 
than the Reed–Solomon code used for the message data, 
so it makes sense to start with the BCH code for format information.

BCH error detection:
The process for checking the encoded information is similar to long division, 
but uses exclusive-or instead of subtraction. 
The format code should produce a remainder of zero when it is "divided" by the so-called generator of the code. 
QR format codes use the generator 10100110111. 
This process is demonstrated for the format information in the example code (000111101011001) below.

              000111101011001
               ^ 101001101110 
               --------------
                 010100110111
                ^ 10100110111
                -------------
                  00000000000

Here is a Python function which implements this calculation.

def qr_check_format(fmt):
   g = 0x537 # = 0b10100110111 in python 2.6+
   for i in range(4,-1,-1):
      if fmt & (1 << (i+10)):
         fmt ^= g << i
   return fmt

Python note: The range function may not be clear to non-Python programmers. 
It produces a list of numbers counting down from 4 to 0 
(the code has "-1" because the interval returned by "range" 
includes the start but not the end value). 
In C-derived languages, the for loop might be written as 
for (i = 4; i >= 0; i--); 
in Pascal-derived languages, for i := 4 downto 0.

Python note 2: The & operator performs bitwise and, while << is a left bit-shift. 
This is consistent with C-like languages.

This function can also be used to encode the 5-bit format information.

encoded_format = (format<<10) + qr_check_format(format<<10)

Readers may find it an interesting exercise to generalize this function to divide by different numbers. 
For example, larger QR codes contain 6 bits of version information with 12 error correction bits 
using the generator 1111100100101.

In mathematical formalism, these binary numbers are described as polynomials 
whose coefficients are integers mod 2. 
Each bit of the number is a coefficient of 1 term. For example:

10100110111 = 1 x10 + 0 x9 + 1 x8 + 0 x7 + 0 x6 + 1 x5 + 1 x4 + 0 x3 + 1 x2 + 1 x + 1 = x10 + x8 + x5 + x4 + x2 + x + 1

If the remainder produced by qr_check_format is not zero, 
then the code has been damaged or misread. The next step is to 
determine which format code is most likely the one that was intended (ie, lookup in our reduced dictionary).

BCH error correction:
Although sophisticated algorithms for decoding BCH codes exist, they are probably overkill in this case. 
Since there are only 32 possible format codes, it's much easier to simply try each one and pick the one 
that has the smallest number of bits different from the code in question 
(the number of different bits is known as the Hamming distance). 
This method of finding the closest code is known as exhaustive search, 
and is possible only because we have very few codes (a code is a valid message, 
and here there are only 32, all other binary numbers aren't correct).

(Note that Reed–Solomon is also based on this principle, 
but since the number of possible codewords is simply too big, 
we can't afford to do an exhaustive search, and that's why clever but complicated algorithms have been devised, 
such as Berlekamp-Massey.)

def hamming_weight(x):
   weight = 0
   while x > 0:
      weight += x & 1
      x >>= 1
   return weight

def qr_decode_format(fmt):
   best_fmt = -1
   best_dist = 15
   for test_fmt in range(0,32):
      test_code = (test_fmt<<10) ^ qr_check_format(test_fmt<<10)
      test_dist = hamming_weight(fmt ^ test_code)
      if test_dist < best_dist:
         best_dist = test_dist
         best_fmt = test_fmt
      elif test_dist == best_dist:
         best_fmt = -1
   return best_fmt

The function qr_decode_format returns -1 if the format code could not be unambiguously decoded. 
This happens when 2 or more format codes have the same distance from the input.

To run this code in Python, first start IDLE, Python's integrated development environment. 
You should see a version message and the interactive input prompt >>>. 
Open a new window, copy the functions qr_check_format, hamming_weight, and qr_decode_format into it, and save as qr.py  . 
Return to the prompt and type the lines following >>> below.

>>> from qr import *
>>> qr_decode_format(int("000111101011001",2))  # no errors
3
>>> qr_decode_format(int("111111101011001",2))  # 3 bit-errors
3
>>> qr_decode_format(int("111011101011001",2))  # 4 bit-errors
-1
You can also start Python by typing python at a command prompt.

In the next sections, we will study Finite Field Arithmetics and Reed–Solomon code, 
which is a subtype of BCH codes. The basic idea (ie, using a limited words dictionary with maximum separability) is the same, 
but since we will encode longer words (256 bytes instead of 2 bytes), with more symbols available 
(encoded on all 8bits, thus 256 different possible values), we cannot use this naive, exhaustive approach, 
because it would take way too much time: 
we need to use cleverer algorithms, and Finite Field mathematics will help us do just that, by giving us a structure.
***********************************************************************************************************************************/


public class BCH {
    
}
