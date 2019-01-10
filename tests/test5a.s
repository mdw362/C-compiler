.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$4, %eax
    pushl	%eax
    movl	-4(%ebp), %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
