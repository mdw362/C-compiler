.globl _main
_main:
    movq	$4, %rax
    push        %rax
    movq	$8, %rax
    pop         %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    setg        %al
    push        %rax
    movq	$3, %rax
    push        %rax
    movq	$9, %rax
    pop         %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    setl        %al
    pop         %rcx
    orq         %rcx, %rax
    movq        $0, %rax
    setne       %al
ret
