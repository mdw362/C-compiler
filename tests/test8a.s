.globl _main
_main:
    pushq       %rbp
    movq        %rsp, %rbp
    movq        $0, %rax
    pushq       %rax
    movq	$0, %rax
    pushq        %rax
    movq	$0, %rax
    movq       %rax, (%rbp)
    movq	(%rbp), %rax
    pushq       %rax
    movq	$10, %rax
    popq        %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    setl        %al
    cmpq         $0, %rax
    je            _post_loop0
_loop0:
    movq	-4(%rbp), %rax
    pushq       %rax
    movq	$1, %rax
    popq        %rcx
    addq        %rcx, %rax
    movq        %rax, -4(%rbp)
_for_exp0:
    movq	(%rbp), %rax
    pushq       %rax
    movq	$1, %rax
    popq        %rcx
    addq        %rcx, %rax
    movq       %rax, (%rbp)
    movq	(%rbp), %rax
    pushq       %rax
    movq	$10, %rax
    popq        %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    setl        %al
    cmpq          $0, %rax
    jne          _loop0
_post_loop0:
    movq	-4(%rbp), %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
