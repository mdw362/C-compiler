.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$3, %eax
    cmpl	$0, %eax
    movl	$0, %eax
    sete	%al
    movl	%ebp, %esp
    popl	%ebp
    ret
