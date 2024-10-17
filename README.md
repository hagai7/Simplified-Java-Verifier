# Simplified-Java-Verifier
**Description:**<br />
Sjava (Simplified Java) is a subset of Java programming language with a simpler syntax and reduced set of features.
The verifier checks if a Java class file conforms to the format and rules specified by the subset.
The program recieves an input Sjava file (a simplfied version of java) and returns:<br />
- 0 – if the code is legal.
- 1 – if the code is illegal.
- 2 – in case of IO errors (see errors).  

**Files:**<br />
The program contain 3 packages written in Java:<br />
- main - includes the main verifier file.<br />
- parsing - includes the files responsible for parsing the input file and exception classes (also includes exception classes).<br />
- scope - includes the files responsible checking if the scope is legally built in terms of its syntax and the variables in it (also includes exception classes).<br />
- tests - includes input Sjava programs with valid or invalid syntax.<br />
