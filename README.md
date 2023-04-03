# Simplified-Java-Verifier
Description:
Sjava (Simplified Java) is subset of the Java programming language with a simpler syntax and reduced set of features.
A tool that checks if a Java class file conforms to the format and rules specified by the Java Virtual Machine specification.
The program recieves an input Sjava file (a simplfied version of java) and returns:
  • 0 – if the code is legal.
  • 1 – if the code is illegal.
  • 2 – in case of IO errors (see errors).  

Files:
the program contain 3 packages:
main - includes the main verifier file.
parsing - includes the files responsible for parsing the input file.
scope - includes the files responsible checking if the scope is legally built in terms of its syntax and the variables in it.
tests - includes Sjava program 
