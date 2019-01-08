.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$3, %eax
    neg         %eax
    not         %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
