.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$9, %eax
    pushl	%eax
    movl	(%ebp), %eax
    movl	$2, %eax
    pushl	%eax
    movl	$4, %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	(%ebp), %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
