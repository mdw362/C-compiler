.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$4, %eax
    pushl	%eax
    movl	$6, %eax
    pushl	%eax
    movl	$1, %eax
    movl	%eax, (%ebp)
    movl	$2, %eax
    movl	%eax,-4(%ebp)
    movl	(%ebp), %eax
    pushl	%eax
    movl	-4(%ebp), %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
