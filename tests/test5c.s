.globl _main
_main:
    pushq       %rbp
    movq        %rsp, %rbp
    movq	$4, %rax
    pushq        %rax
    movq	$6, %rax
    pushq        %rax
    movq	$1, %rax
    movq       %rax, (%rbp)
    movq	$2, %rax
    movq        %rax, -4(%rbp)
    movq	(%rbp), %rax
    pushq       %rax
    movq	-4(%rbp), %rax
    popq        %rcx
    addq        %rcx, %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
