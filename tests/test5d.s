.globl _main
_main:
    pushq       %rbp
    movq        %rsp, %rbp
    movq	$9, %rax
    pushq        %rax
    movq	(%rbp), %rax
    movq	$2, %rax
    pushq       %rax
    movq	$4, %rax
    popq        %rcx
    addq        %rcx, %rax
    movq	(%rbp), %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
