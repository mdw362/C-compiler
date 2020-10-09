# C-compiler
Compiles C code to 32-bit x86 Assembly.
Created lexer, AST parser, and code generator from scratch. Code generation still in progress.

To run:

    scalac Compiler.scala
    scala Compiler file.c

Currently only supports very basic C files that contain functions, if/else 
statements, loops, variables, and return statements. 

