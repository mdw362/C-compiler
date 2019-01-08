.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$29, %eax
    pushl	%eax
    movl	$2, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setge	%al
    movl	%ebp, %esp
    popl	%ebp
    ret
