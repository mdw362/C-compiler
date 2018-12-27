.globl _main
_main:
    movq	$2, %rax
    push        %rax
    movq	$2, %rax
    pop         %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    sete        %al
    push        %rax
    movq	$0, %rax
    pop         %rcx
    orq         %rcx, %rax
    movq        $0, %rax
    setne       %al
ret
