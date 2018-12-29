.globl _main
_main:
    pushq       %rbp
    movq        %rsp, %rbp
    movq	$4, %rax
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
    jmp          _post_conditional2
_post_conditional2:
    jmp          _post_conditional0
_branch1:
    movq	(%rbp), %rax
    pushq       %rax
    movq	$20, %rax
    popq        %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    setg        %al
    cmpq         $0, %rax
    je           _branch2
    movq	$18, %rax
    movq       %rax, (%rbp)
    jmp          _post_conditional4
_post_conditional4:
    movq	(%rbp), %rax
    pushq       %rax
    movq	$1, %rax
    popq        %rcx
    addq        %rcx, %rax
    movq	$3, %rax
    movq       %rax, (%rbp)
    jmp          _post_conditional0
_branch2:
    movq	$3, %rax
    movq       %rax, (%rbp)
    jmp          _post_conditional0
_post_conditional0:
    movq	(%rbp), %rax
    movq        $0, %rax
    movq	(%rbp), %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
