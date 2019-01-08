.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$2, %eax
    pushl	%eax
    movl	$3, %eax
    popl	%ecx
    addl	%ecx, %eax
    pushl	%eax
    movl	$2, %eax
    pushl	%eax
    movl	$4, %eax
    popl	%ecx
    addl	%ecx, %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
