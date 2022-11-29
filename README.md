# QR_Decode_Learning
learning how to decode a QR code

(1) Sorry not done getting codewords, stepping thru zxing-ported-to-javascript-code-from-github-jsqrcode in web-browser debugger
decoder.js line 41 breakpoint
seems 
mycodeword[0]  == codewordBytes[0]
mycodeword[4]  == codewordBytes[1]
mycodeword[8]  == codewordBytes[2]
mycodeword[12] == codewordBytes[3]
mycodeword[16] == codewordBytes[4]
but then have to fix code that gets codewords
