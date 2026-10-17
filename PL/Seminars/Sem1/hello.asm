global _start

section .data
message: db 'Hello WWWorld!!!!', 10

section .text
_start:
mov rax, 1
mov rdi, 1
mov rsi, message
mov rdx, 19
syscall

mov rax, 1
mov rdi, 2
mov rsi, message
mov rdx, 19
syscall

mov rax, 60
xor rdi, rdi
syscall
