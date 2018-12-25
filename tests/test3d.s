.globl _main
_main:
    movq	$4, %rax
    push 	%rax
    movq	$2, %rax
    pop 	%rcx
    idivq	%rcx, %rax
    push 	%rax
    movq	$3, %rax
    push 	%rax
    movq	$5, %rax
    pop 	%rcx
    imulq	%rcx, %rax
    pop 	%rcx
    addq	%rcx, %rax
ret
