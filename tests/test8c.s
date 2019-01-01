.globl _main
_main:
    pushq       %rbp
    movq        %rsp, %rbp
    movq	$0, %rax
    pushq        %rax
    movq	$0, %rax
    pushq        %rax
    movq	(%rbp), %rax
    pushq       %rax
    movq	$10, %rax
    popq        %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    setl        %al
    cmpq          $0, %rax
    jne           _post_loop0
_loop0:
    movq	(%rbp), %rax
    pushq       %rax
    movq	$7, %rax
    popq        %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    sete        %al
    cmpq         $0, %rax
    jne          _post_conditional0
    jmp           _loop0
_post_conditional0:
    movq	(%rbp), %rax
    pushq       %rax
    movq	$1, %rax
    popq        %rcx
    addq        %rcx, %rax
    movq       %rax, (%rbp)
_post_loop0:
_loop1:
    movq	-4(%rbp), %rax
    pushq       %rax
    movq	$2, %rax
    popq        %rcx
    addq        %rcx, %rax
    movq        %rax, -4(%rbp)
    movq	-4(%rbp), %rax
    pushq       %rax
    movq	$16, %rax
    popq        %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    setl        %al
    cmpq          $0, %rax
    jne          _loop1
_post_loop1
    movq	-4(%rbp), %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
