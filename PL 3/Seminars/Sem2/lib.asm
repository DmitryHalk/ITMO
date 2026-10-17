; print_string.asm 
section .data
message:       db  'hello, world!', 10, 0
error_message: db  'hello, errors!', 10, 0
buffer: times 256 db 0

section .text
global _start

exit:
    mov  rax, 60
    xor  rdi, rdi          
    syscall

string_length:
    mov  rax, rdi
  .counter:
    cmp  byte [rdi], 0
    je   .end
    inc  rdi
    jmp  .counter
  .end:
    sub  rdi, rax
    mov  rax, rdi
    ret

print_string: ; принимает указатель в rdi 
    push rdi
    call string_length ; теперь в rax длина строки
    pop rdi
    mov rdx, rax ; длина выводимого соощения
    mov  rsi, rdi ; начало строки
    mov  rax, 1
    mov  rdi, 1
    syscall
    ret

parse_uint: ; передадиим в rdi указатель
    xor rax, rax ; число
    xor rdx, rdx ; длина
    .loop:
        mov cl, byte [rdi+rdx]
        cmp cl, 




_start:
    mov  rdi, message
    mov  rsi, 14  
    call print_string
    call exit