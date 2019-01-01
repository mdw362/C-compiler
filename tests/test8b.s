.globl _main
_main:
    pushq       %rbp
    movq        %rsp, %rbp
    movq        $0, %rax
    pushq       %rax
    movq	$0, %rax
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
    movq	$7, %rax
    popq        %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    setge       %al
    cmpq         $0, %rax
    jne          _post_conditional0
    jmp           _post_loop0
_post_conditional0:
_for_exp0:
    jmp           _loop0
_post_loop0:
    movq	-4(%rbp), %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
