.globl _main
_main:
    movq	$5, %rax
    push 	%rax
    movq	$4, %rax
    pop 	%rcx
    imulq	%rcx, %rax
    push 	%rax
    movq	$5, %rax
    pop 	%rcx
    imulq	%rcx, %rax
    push 	%rax
    movq	$2, %rax
    pop 	%rcx
    idivq	%rcx, %rax
ret
