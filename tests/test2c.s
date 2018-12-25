.globl _main
_main:
    movl	$3, %eax
    neg 	%eax
    cmpl	$0, %eax
    movl	$0, %eax
    sete	%al
    neg 	%eax
    not 	%eax
ret
