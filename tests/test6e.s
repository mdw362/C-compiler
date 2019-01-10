.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$4, %eax
    pushl	%eax
    movl	-4(%ebp), %eax
    pushl	%eax
    movl	$5, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setg	%al
    cmpl	$0, %eax
    je          _branch0
    movl	$7, %eax
    movl	%eax,-4(%ebp)
    jmp	_post_conditional0
_branch0:
    movl	-4(%ebp), %eax
    pushl	%eax
    movl	$9, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setle	%al
    cmpl	$0, %eax
    je          _branch1
    movl	$23, %eax
    movl	%eax,-4(%ebp)
    jmp	_post_conditional0
_branch1:
    movl	-4(%ebp), %eax
    pushl	%eax
    movl	$20, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setg	%al
    cmpl	$0, %eax
    je          _post_conditional0
    movl	$18, %eax
    movl	%eax,-4(%ebp)
_post_conditional0:
    movl	-4(%ebp), %eax
    pushl	%eax
    movl	$1, %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	%eax,-4(%ebp)
    movl	$3, %eax
    movl	%eax,-4(%ebp)
    movl	$3, %eax
    movl	%eax,-4(%ebp)
    movl	-4(%ebp), %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
