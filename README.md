# C-compiler
Compiles C code to 32-bit x86 Assembly.
Uses lexical analysis, AST parsing, and code generation.

To run:

    scalac Compiler.scala
    scala Compiler file.c

Supports basic C files that contain functions, if/else 
statements, loops, variables, and return statements. 
Currently only supports ints and only currently only 
compiles C code that matches a format similar to any 
of the C files in the tests directory.
