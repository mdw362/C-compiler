.globl _main
_main:
    movq	$4, %rax
    push        %rax
    movq	$8, %rax
    pop         %rcx
    cmpq        $0, %rcx
    setne       %cl
    cmpq        $0, %rax
    movq        $0, %rax
    setne       $al
    andb        %cl, %al
ret
