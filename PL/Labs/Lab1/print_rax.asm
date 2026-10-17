section .data
codes:
    db '0123456789ABCDEF'

section .text
global _start
_start:
    mov rax, 0x1122334455667788
    mov rdi, 1 ; взаимодейтсвуем с stdout (1)
    mov rdx, 1 ; длинна выводимого слова
    mov rcx, 64 ; обозначает длину цикла чтобы выйти из него как rcx станет нулём

.loop:
    push rax
    sub rcx, 4
    
    sar rax, cl ; cl - младший байт rcx
    and rax, 0xf
    lea rsi, [codes + rax]
    mov rax, 1

    push rcx
    syscall
    pop rcx

    pop rax
    
    test rcx, rcx ; проверка на ноль(логическое И которое устанавливает флаги стостояния, но не меняет операнды)
    jnz .loop

    push 10
    mov rax, 1 ; rax хранит всегда хранит код какого-либо действия(системного вызова), в данном случае 1 - это `write`
    mov rdi, 1 ; rdi - номер потока stderr, out, input
    mov rsi, rsp ; rsi хранит адрес начала выводимого сообщения, а потом с помощью rdx вычисляется длина и он считывает нужное количество символов
    mov rdx, 1 ; кол-во символов
    syscall

    mov rax, 60
    xor rdi, rdi
    syscall