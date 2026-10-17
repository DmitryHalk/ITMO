%include "lib.inc"
%include "dict.inc"


%define BUF_SIZE 256        ; 255 символов + нуль-терминатор

section .data
%include "words.inc"

msg_too_long:   db "Error: input is longer than 255 characters", 0
msg_not_found:  db "Error: key not found", 0

buffer: times BUF_SIZE db 0

section .text
global _start

; Читаем строку из stdin до перевода строки (или EOF) в буфер.
; Input: rdi - буфер, rsi - размер буфера (с учётом терминатора)
; Output: rax - длина строки, либо -1, если строка не помещается
read_line:
    push rbx
    push r12
    push r13
    mov r12, rdi
    mov r13, rsi
    xor ebx, ebx
.loop:
    call read_char          
    cmp rax, 0xA
    je .done
    test rax, rax           ; EOF
    je .done
    lea rcx, [r13 - 1]      ; последнее место зарезервировано под 0
    cmp rbx, rcx
    jae .too_long
    mov [r12 + rbx], al
    inc rbx
    jmp .loop
.done:
    mov byte [r12 + rbx], 0
    mov rax, rbx
    jmp .ret
.too_long:
    mov rax, -1
.ret:
    pop r13
    pop r12
    pop rbx
    ret

; Выводим нуль-терминированную строку в stderr (+ перевод строки)
; Input: rdi - указатель на строку
print_error:
    push rdi
    call string_length
    pop rsi
    mov rdx, rax
    mov eax, 1              ; sys_write
    mov edi, 2              ; stderr
    syscall
    push 0xA                ; перевод строки
    mov eax, 1
    mov edi, 2
    mov rsi, rsp
    mov edx, 1
    syscall
    add rsp, 8
    ret

_start:
    mov rdi, buffer
    mov rsi, BUF_SIZE
    call read_line
    cmp rax, 0
    jl .too_long

    mov rdi, buffer
    mov rsi, prev      ; последнее слово, добавленное через colon
    call find_word
    test rax, rax
    jz .not_found

    lea rdi, [rax + 8]
    push rax
    call string_length      ; длина ключа
    pop rdx
    lea rdi, [rdx + rax + 9]
    call print_string
    call print_newline
    xor edi, edi
    call exit

.too_long:
    mov rdi, msg_too_long
    call print_error
    mov edi, 1
    call exit

.not_found:
    mov rdi, msg_not_found
    call print_error
    mov edi, 1
    call exit
