.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$0, %eax
    pushl	%eax
    movl	$5, %eax
    movl	%eax, (%ebp)
    movl	$3, %eax
    movl	%eax, (%ebp)
    movl	(%ebp), %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
