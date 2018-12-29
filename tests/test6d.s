.globl _main
_main:
    pushq       %rbp
    movq        %rsp, %rbp
    movq	$4, %rax
    pushq        %rax
    movq	$9, %rax
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
    movq	$0, %rax
    movq        %rax, -4(%rbp)
    jmp          _post_conditional0
_branch0:
    movq	(%rbp), %rax
    pushq       %rax
    movq	$9, %rax
    popq        %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    setle       %al
    cmpq         $0, %rax
    je           _branch1
    movq	$23, %rax
    movq       %rax, (%rbp)
    movq	$2, %rax
    movq        %rax, -4(%rbp)
    jmp          _post_conditional2
_post_conditional2:
    jmp          _post_conditional0
_branch1:
    movq	$3, %rax
    movq       %rax, (%rbp)
    jmp          _post_conditional0
_post_conditional0:
    movq	(%rbp), %rax
    pushq       %rax
    movq	-4(%rbp), %rax
    popq        %rcx
    addq        %rcx, %rax
    movq	-4(%rbp), %rax
    pushq       %rax
    movq	(%rbp), %rax
    popq        %rcx
    imulq        %rcx, %rax
    movq        %rax, -4(%rbp)
    movq	(%rbp), %rax
    pushq       %rax
    movq	-4(%rbp), %rax
    popq        %rcx
    addq        %rcx, %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
