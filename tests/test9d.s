.globl _main
_main:
    pushq       %rbp
    movq        %rsp, %rbp
    movq	$3, %rax
    pushq        %rax
    subq	$8, %rax
    xorq	%rdx, rdx
    movq	0x20, %rcx
    idivq	%rcx
    subq	%rdx, %rsp
    pushq	%rdx
    movq	(%rbp), %rax
    pushq       %rax
    callq       _fnOne
    addq        $0x4, %rsp
    popq	%rdx
    addq	%rdx, %rsp
    pushq        %rax
    movq	-4(%rbp), %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
.globl _fnOne
_fnOne:
    pushq       %rbp
    movq        %rsp, %rbp
    movq	$1, %rax
    pushq        %rax
    movq	8(%rbp), %rax
    pushq       %rax
    movq	$55, %rax
    popq        %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    setg        %al
    cmpq         $0, %rax
    je           _branch0
    movq	8(%rbp), %rax
    pushq       %rax
    movq	$12, %rax
    popq        %rcx
    imulq        %rcx, %rax
    movq       %rax, (%rbp)
    movq	(%rbp), %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
_branch0
    movq	8(%rbp), %rax
    pushq       %rax
    movq	$4, %rax
    popq        %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    setl        %al
    cmpq         $0, %rax
    je           _branch1
    movq	$5, %rax
    movq       %rax, (%rbp)
    movq	(%rbp), %rax
    pushq       %rax
    movq	$1, %rax
    popq        %rcx
    addq        %rcx, %rax
    movq      %rax, 8%(rbp)
    jmp        _post_conditional0
_branch1
    movq	$7, %rax
    movq      %rax, 8%(rbp)
_post_conditional_0
    movq	$2, %rax
    pushq        %rax
    movq	8(%rbp), %rax
    pushq       %rax
    movq	-4(%rbp), %rax
    popq        %rcx
    addq        %rcx, %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret