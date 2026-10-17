%include "lib.inc"

global find_word

section .text

; rdi - указатель на нуль-терминированную строку
; rsi - указатель на начало словаря
; rax - адрес вхождения, если ключ найден, иначе 0
find_word:
    push r12
    push r13
    mov r12, rdi            ; искомый ключ 
    mov r13, rsi            ; текущее вхождение
.loop:
    test r13, r13           ; если конец списка, то not_found
    jz .not_found

    mov rdi, r12
    lea rsi, [r13 + 8]      ; ключ лежит сразу после указателя на следующее вхождение
    call string_equals
    test rax, rax
    jnz .is_find

    mov r13, [r13]          ; переходим к следующему вхождению
    jmp .loop

.is_find:
    mov rax, r13
    jmp .end
.not_found:
    xor eax, eax
.end:
    pop r13
    pop r12
    ret
