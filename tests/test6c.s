.globl _main
_main:
    pushq       %rbp
    movq        %rsp, %rbp
    movq	$4, %rax
    pushq        %rax
    movq	$8, %rax
    pushq        %rax
    movq	(%rbp), %rax
    pushq       %rax
    movq	$5, %rax
    popq        %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    setg        %al
    cmpq         $0, %rax
    je           _branch0
    movq	$7, %rax
    movq       %rax, (%rbp)
    movq	$12, %rax
    movq        %rax, -4(%rbp)
    jmp          _post_conditional
_branch0:
    movq	$3, %rax
    movq       %rax, (%rbp)
    movq	$2, %rax
    movq        %rax, -4(%rbp)
_post_conditional:
    movq	(%rbp), %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
