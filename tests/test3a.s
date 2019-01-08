.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$42, %eax
    pushl	%eax
    movl	$2, %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
