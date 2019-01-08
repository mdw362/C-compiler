.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$3, %eax
    pushl	%eax
    movl	%esp, %eax
    subl	$8, %eax
    xorl        %edx, rdx
    movl	0x20, %ecx
    idivl	%ecx
    subl	%edx, %esp
    pushl	%edx
    movl	(%ebp), %eax
    pushl       %eax
    call	_func
    addl	$0x4, %esp
    popl	%edx
    addl	%edx, %esp
    movl	%ebp, %esp
    popl	%ebp
    ret
.globl _func
_func:
    pushl       %ebp
    movl        %esp, %ebp
    movl	8(%ebp), %eax
    pushl	%eax
    movl	$1, %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	%eax, 8(%ebp)
    movl	8(%ebp), %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
