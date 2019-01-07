.globl _main
_main:
    pushq       %rbp
    movq        %rsp, %rbp
    movq	$5, %rax
    pushq        %rax
    movq	(%rbp), %rax
    pushq       %rax
    subq	$8, %rax
    xorq	%rdx, rdx
    movq	0x20, %rcx
    idivq	%rcx
    subq	%rdx, %rsp
    pushq	%rdx
    movq	(%rbp), %rax
    pushq       %rax
    callq       _fn
    addq        $0x4, %rsp
    popq	%rdx
    addq	%rdx, %rsp
    popq        %rcx
    addq        %rcx, %rax
    pushq        %rax
    movq	-4(%rbp), %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
.globl _fn
_fn:
    pushq       %rbp
    movq        %rsp, %rbp
    movq	$3, %rax
    movq      %rax, 8%(rbp)
    movq	8(%rbp), %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
