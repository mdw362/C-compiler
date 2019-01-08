.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$4, %eax
    pushl	%eax
    movl	(%ebp), %eax
    pushl	%eax
    movl	$5, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setg	%al
    cmpl	$0, %eax
    je          _branch0
    movl	$7, %eax
    movl	%eax, (%ebp)
    jmp	_post_conditional0
_branch0:
    movl	$3, %eax
    movl	%eax, (%ebp)
_post_conditional0:
    movl	(%ebp), %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
