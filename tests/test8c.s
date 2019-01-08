.globl _main
_main:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$0, %eax
    pushl	%eax
    movl	$0, %eax
    pushl	%eax
    movl	(%ebp), %eax
    pushl	%eax
    movl	$10, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setl	%al
    cmpl	$0, %eax
    je          _post_loop0
_loop0:
    movl	(%ebp), %eax
    pushl	%eax
    movl	$7, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    sete	%al
    cmpl	$0, %eax
    je          _post_conditional0
    jmp           _loop0
_post_conditional0:
    movl	(%ebp), %eax
    pushl	%eax
    movl	$1, %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	%eax, (%ebp)
    movl	(%ebp), %eax
    pushl	%eax
    movl	$1, %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	%eax, (%ebp)
    movl	(%ebp), %eax
    pushl	%eax
    movl	$10, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setl	%al
    cmpl	$0, %eax
    jne         _loop0
_post_loop0:
_loop1:
    movl	-4(%ebp), %eax
    pushl	%eax
    movl	$2, %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	%eax,-4(%ebp)
    movl	-4(%ebp), %eax
    pushl	%eax
    movl	$16, %eax
    popl	%ecx
    cmpl	%eax, %ecx
    movl	$0, %eax
    setl	%al
    cmpl	$0, %eax
    jne         _loop1
_post_loop1:
    movl	-4(%ebp), %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
.globl _func
_func:
    pushl       %ebp
    movl        %esp, %ebp
    movl	$0, %eax
    pushl	%eax
    movl	(%ebp), %eax
    pushl	%eax
    movl	$1, %eax
    popl	%ecx
    addl	%ecx, %eax
    movl	%eax, (%ebp)
    movl	(%ebp), %eax
    movl	%ebp, %esp
    popl	%ebp
    ret
