.globl _main
_main:
    pushq       %rbp
    movq        %rsp, %rbp
    movq	8(%rbp), %rax
    pushq       %rax
    movq	12(%rbp), %rax
    popq        %rcx
    addq        %rcx, %rax
    pushq        %rax
    movq	(%rbp), %rax
    pushq       %rax
    subq	$8, %rax
    xorq	%rdx, rdx
    movq	0x20, %rcx
    idivq	%rcx
    subq	%rdx, %rsp
    pushq	%rdx
    movq	8(%rbp), %rax
    pushq       %rax
    callq       _func
    addq        $0x4, %rsp
    popq	%rdx
    addq	%rdx, %rsp
    popq        %rcx
    addq        %rcx, %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
.globl _func
_func:
    pushq       %rbp
    movq        %rsp, %rbp
    movq	8(%rbp), %rax
    pushq       %rax
    movq	$1, %rax
    popq        %rcx
    addq        %rcx, %rax
    movq      %rax, 8%(rbp)
    movq	8(%rbp), %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
