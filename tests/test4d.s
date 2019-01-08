.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$2, %eax
    pushl	%eax
    movl	$2, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    sete	%al
    pushl	%eax
    movl	$0, %eax
    popl	%ecx
    orl	%ecx, %eax
    movl	$0, %eax
    setne	%al
    movl	%ebp, %esp
    popl	%ebp
    ret
