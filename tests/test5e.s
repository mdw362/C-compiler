.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$3, %eax
    pushl	%eax
    movl	-4(%ebp), %eax
    pushl	%eax
    movl	$3, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setg	%al
    movl	%ebp, %esp
    popl	%ebp
    ret
