.globl _main
_main:
    movq	$2, %rax
    push 	%rax
    movq	$3, %rax
    pop 	%rcx
    addq	%rcx, %rax
    push 	%rax
    movq	$2, %rax
    push 	%rax
    movq	$4, %rax
    pop 	%rcx
    addq	%rcx, %rax
    pop 	%rcx
    addq	%rcx, %rax
ret
