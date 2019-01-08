.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$4, %eax
    pushl	%eax
    movl	$8, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setg	%al
    pushl	%eax
    movl	$3, %eax
    pushl	%eax
    movl	$9, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setl	%al
    popl	%ecx
    orl	%ecx, %eax
    movl	$0, %eax
    setne	%al
    movl	%ebp, %esp
    popl	%ebp
    ret
