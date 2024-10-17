# Simplified Java Verifier
**Description:**<br />
Sjava (Simplified Java) is a subset of Java programming language with a simpler syntax and reduced set of features. The verifier checks if a Java class file conforms to the format and rules specified by the subset. 
The program recieves an input of Sjava file and returns:<br />
- 0 - if the code is legal.
- 1 - if the code is illegal.
- 2 - in case of IO errors (see errors).  

**Files:**<br />
The program contain 3 packages written in Java:<br />
- `main` - includes the main verifier file.<br />
- `parsing` - includes the files responsible for parsing the input file and input exception classes.<br />
- `scope` - includes files that validate the syntax and structure of scopes within the code (also includes related exception classes)..<br />
- `tests` - includes input exmaples of Sjava programs with valid or invalid syntax, for testing.<br />
