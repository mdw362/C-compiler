.globl _main
_main:
    pushq       %rbp
    movq        %rsp, %rbp
    movq        $0, %rax
    pushq       %rax
    movq	$5, %rax
    movq       %rax, (%rbp)
    movq	$3, %rax
    movq       %rax, (%rbp)
    movq	(%rbp), %rax
    movq        %rbp, %rsp
    popq        %rbp
    ret
