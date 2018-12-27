.globl _main
_main:
    movq	$29, %rax
    push        %rax
    movq	$2, %rax
    pop         %rcx
    cmpq        %rax, %rcx
    movq        $0, %rax
    setge       %al
ret
