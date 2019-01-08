.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$2, %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
