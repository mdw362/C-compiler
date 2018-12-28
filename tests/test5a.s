.globl _main
_main:
    pushq       %rbp
    movq        %rsp, %rbp
    movq	$4, %rax
    pushq        %rax
    movq	(%rbp), %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
