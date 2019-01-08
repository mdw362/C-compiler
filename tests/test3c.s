.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$5, %eax
    pushl	%eax
    movl	$4, %eax
    popl	%ecx
    imul	%ecx, %eax
    pushl	%eax
    movl	$5, %eax
    popl	%ecx
    imul	%ecx, %eax
    pushl	%eax
    movl	$2, %eax
    popl	%ecx
    idivl	%ecx, %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
