.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$0, %eax
    pushl	%eax
    movl	$0, %eax
    pushl	%eax
    movl	$0, %eax
    movl	%eax,-4(%ebp)
    movl	-4(%ebp), %eax
    pushl	%eax
    movl	$10, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setl	%al
    cmpl	$0, %eax
    je          _post_loop0
_loop0:
    movl	-8(%ebp), %eax
    pushl	%eax
    movl	$1, %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	%eax,-8(%ebp)
_for_exp0:
    movl	-4(%ebp), %eax
    pushl	%eax
    movl	$1, %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	%eax,-4(%ebp)
    movl	-4(%ebp), %eax
    pushl	%eax
    movl	$10, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setl	%al
    cmpl	$0, %eax
    jne	_loop0
_post_loop0:
    movl	-8(%ebp), %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
