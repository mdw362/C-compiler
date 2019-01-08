.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$4, %eax
    pushl	%eax
    movl	$8, %eax
    popl	%ecx
    cmpl	$0, %ecx
    setne	%cl
    cmpl	$0, %eax
    movl	$0, %eax
    setne	$al
    andb	%cl, %al
    movl	%ebp, %esp
    popl	%ebp
    ret
