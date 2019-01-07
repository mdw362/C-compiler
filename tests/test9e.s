.globl _main
_main:
    pushq       %rbp
    movq        %rsp, %rbp
    movq	$3, %rax
    pushq        %rax
    movq	$5, %rax
    pushq        %rax
    subq	$12, %rax
    xorq	%rdx, rdx
    movq	0x20, %rcx
    idivq	%rcx
    subq	%rdx, %rsp
    pushq	%rdx
    callq       _fnOne
    addq        $0x8, %rsp
    popq	%rdx
    addq	%rdx, %rsp
    pushq        %rax
    movq	-8(%rbp), %rax
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
    je           _post_conditional0
    movq	$5, %rax
    movq       %rax, (%rbp)
_post_conditional_0
    movq	$2, %rax
    pushq        %rax
_loop0:
    movq	-4(%rbp), %rax
    pushq       %rax
    movq	$1, %rax
    popq        %rcx
    addq        %rcx, %rax
    movq        %rax, -4(%rbp)
    movq	-4(%rbp), %rax
    pushq       %rax
    movq	$12, %rax
    popq        %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    setl        %al
    cmpq          $0, %rax
    jne          _loop0
_post_loop0
    movq	-4(%rbp), %rax
    pushq       %rax
    movq	$4, %rax
    popq        %rcx
    addq        %rcx, %rax
    movq        %rax, -4(%rbp)
    movq	$10, %rax
    movq       %rax, (%rbp)
    movq	$10, %rax
    movq       %rax, (%rbp)
    movq	-4(%rbp), %rax
    pushq       %rax
    movq	(%rbp), %rax
    popq        %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    setl        %al
    cmpq          $0, %rax
    jne           _post_loop1
_loop1:
    movq	-4(%rbp), %rax
    pushq       %rax
    movq	$3, %rax
    popq        %rcx
    addq        %rcx, %rax
    movq        %rax, -4(%rbp)
_post_loop1:
    movq	8(%rbp), %rax
    pushq       %rax
    movq	-4(%rbp), %rax
    popq        %rcx
    addq        %rcx, %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
