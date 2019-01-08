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
    call	_fnOne
    addl	$0x4, %esp
    popl	%edx
    addl	%edx, %esp
    pushl	%eax
    movl	%esp, %eax
    subl	$12, %eax
    xorl        %edx, rdx
    movl	0x20, %ecx
    idivl	%ecx
    subl	%edx, %esp
    pushl	%edx
    call	_fnTwo
    addl	$0x8, %esp
    popl	%edx
    addl	%edx, %esp
    pushl	%eax
    movl	-8(%ebp), %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
.globl _fnOne
_fnOne:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$1, %eax
    pushl	%eax
    movl	8(%ebp), %eax
    pushl	%eax
    movl	$55, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setg	%al
    cmpl	$0, %eax
    je          _branch0
    movl	8(%ebp), %eax
    pushl	%eax
    movl	$12, %eax
    popl	%ecx
    imul	%ecx, %eax
    movl	%eax, (%ebp)
    movl	(%ebp), %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
_branch0:
    movl	8(%ebp), %eax
    pushl	%eax
    movl	$4, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setl	%al
    cmpl	$0, %eax
    je          _branch1
    movl	$5, %eax
    movl	%eax, (%ebp)
    movl	(%ebp), %eax
    pushl	%eax
    movl	$1, %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	%eax, 8(%ebp)
    jmp	_post_conditional0
_branch1:
    movl	8(%ebp), %eax
    pushl	%eax
    movl	$30, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setg	%al
    cmpl	$0, %eax
    je          _branch2
    movl	$2, %eax
    movl	%eax, (%ebp)
    movl	(%ebp), %eax
    pushl	%eax
    movl	$9, %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	%eax, 8(%ebp)
    jmp	_post_conditional0
_branch2:
    movl	$7, %eax
    movl	%eax, 8(%ebp)
_post_conditional0:
    movl	$2, %eax
    pushl	%eax
    movl	8(%ebp), %eax
    pushl	%eax
    movl	-4(%ebp), %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
.globl _fnTwo
_fnTwo:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$0, %eax
    pushl	%eax
    movl	$0, %eax
    pushl	%eax
_loop0:
    movl	(%ebp), %eax
    pushl	%eax
    movl	8(%ebp), %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	%eax, (%ebp)
    movl	(%ebp), %eax
    pushl	%eax
    movl	12(%ebp), %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	%eax, (%ebp)
    movl	(%ebp), %eax
    pushl	%eax
    movl	$10, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setg	%al
    cmpl	$0, %eax
    je          _post_conditional1
    jmp	_post_loop0
_post_conditional1:
_for_exp0:
    jmp           _loop0
_post_loop0:
    movl	(%ebp), %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
