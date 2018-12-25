.globl _main
_main:
    movq	$42, %rax
    push 	%rax
    movq	$2, %rax
    pop 	%rcx
    addq	%rcx, %rax
ret
