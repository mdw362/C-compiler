.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$3, %eax
    neg         %eax
    cmpl	$0, %eax
    movl	$0, %eax
    sete	%al
    neg         %eax
    not         %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
