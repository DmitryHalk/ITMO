; global _start

; section .data
; message: db 'Hello WWWorld!!!!' ; передаваемое сообщение

; section .text

; print_newline:
;     push 0xA
;     mov rax, 1
;     mov rdi, 1
;     mov rsi, rsp
;     mov rdx, 1
;     syscall
;     pop rax
;     xor rax, rax
;     ret

; _start:
; mov rax, 1 ; Код системного вызова write 1
; mov rdi, 1 ; номер дескриптора с которым будет производиться действие(в данном случае write)
; mov rsi, message ; адресс начала сообщения
; mov rdx, 19 ; кол-во выводимых символов
; syscall

; call(print_newline)

; mov rax, 60 ; Код выхода
; xor rdi, rdi
; syscall



section .data
test_str: db "abcdef", 0
str1: db "Hello", 0
str2: db "Hello", 0

test_int_str: db "123b45abc", 0
uint: db "12345"

buffer: times 256 db 0

section .text
global _start

exit:
    mov rax, 60
    syscall

string_length: ; строку передадим в rdi
    xor rax, rax
    .loop:
        cmp byte [rdi+rax], 0
        je .end
        inc rax
        jmp .loop
    .end:
        ret

print_string: ; передаём указатель в rdi
    push rdi
    xor rax, rax
    mov rsi, rdi
    call string_length ; теперь в rax хранится длина строки
    mov rdx, rax
    mov rax, 1
    mov rdi, 1
    syscall
    pop rdi
    ret

print_char: ; передаём в rdi код символа
    push rdi
    mov rax, 1
    mov rdi, 1
    mov rsi, rsp
    mov rdx, 1
    syscall
    pop rdi
    ret


print_newline:
    push rax
    push 0xA
    mov rax, 1
    mov rdi, 1
    mov rsi, rsp
    mov rdx, 1
    syscall
    pop rax
    pop rax
    ret

print_uint: ; передадим число в rdi
    mov rcx, 0
    mov rax, rdi
    push 0 ; терминатор
    .loop_div:
        inc rcx

        mov rdi, 10
        xor rdx, rdx
        div rdi

        add rdx, '0'
        push rdx

        test rax, rax
        jne .loop_div
    
    mov rdx, buffer
    .loop_out:
        pop rax
        mov byte [rdx], al
        inc rdx
        
        test rax, rax
        jnz .loop_out
        
    .end:
        mov rdi, buffer
        call print_string
        ret

string_equals: ; передадим аргументы в rdi и rsi
    mov al, [rdi]
    mov dl, [rsi]
    cmp al, dl  
    jne .end_false

    test al, al
    je .end_true

    inc rdi
    inc rsi
    jmp string_equals

    .end_false:
        xor rax, rax 
        ret

    .end_true:
        mov rax, 1
        ret

print_int: ; передаём в rdi число ; добавить вывод знака
    test rdi, rdi
    jns .is_plus

    push rdi
    push "-"
    mov rax, 1
    mov rdx, 1
    mov rdi, 1
    mov rsi, rsp
    syscall
    pop rdi
    pop rdi

    neg rdi


    .is_plus:
        call print_uint
        ret

read_char: ; читает и вовращает сивол в rax
    push 0
    mov rax, 0
    mov rdi, 0
    mov rsi, rsp
    mov rdx, 1
    syscall

    pop rax
    test rax, rax
    jne .not_EOF
    xor rax, rax
    .not_EOF:
    ret 

read_word: ; передадим аргументы в rdi(длина буфера) и rsi(адресс буфера)
    lea rcx, [rdi+1]
    push rbx
    mov rbx, rsi
    .skip:
        push rsi
        push rcx
        call read_char
        pop rcx
        pop rsi
        ;скипаем пробельные символы и перенос строки, ну и таб
        cmp rax, 0x20
        je .skip
        cmp rax, 0xA
        je .skip
        cmp rax, 0x9
        je .skip

    ; добавить проверку на нуль строку и на превышение лимита
    jmp .write2
    .write:
        push rsi
        push rcx
        call read_char
        pop rcx
        pop rsi
    .write2:
        ; проверяем слово на окончание
        cmp rax, 0x20
        je .end_succes
        cmp rax, 0xA
        je .end_succes
        cmp rax, 0x9
        je .end_succes
        ; если слово не закончилось, то проверяем счётчик(т.е. длину буфера) и если буфер кончился, то идём в ошибку, иначе продолжам записывать
        sub rcx, 1
        test rcx, rcx
        je .end_error

        mov byte [rbx], al
        add rbx, 1
        jmp .write
        

    .end_error: ; по задани не ясно нужно при не хватки длины буфера докидывать нуль
        pop rbx
        xor rax, rax
        ret

    .end_succes:
        mov byte [rbx], 0 ; дописываем терминатора
        sub rbx, rsi ; проверить сохраняется ли rsi до этого момента
        mov rdx, rbx
        mov rax, rsi
        pop rbx
        ret

parse_uint: ; передадим указатель в rdi
    xor rax, rax
    xor rdx, rdx
    .loop:
        push [rdi+rdx]
        .test1:
            cmp byte [rsp], 47
            jg .test2
            jmp .end
        .test2:
            cmp byte [rsp], 58
            jl .write
            jmp .end
        .write:
            pop rcx
            and rcx, 0xf
            imul rax, 10
            add rax, rcx 
            inc rdx
            jmp .loop
    .end:
        pop rcx
        ret


parse_int:
    xor rax, rax
    mov al, byte [rdi]
    cmp rax, 45
    jne .parse_uint

    push 45
    inc rdi

    .parse_uint:
        call parse_uint

    pop rcx
    cmp rcx, 45
    jne .is_plus
    .is_minus:
        inc rdx
        neg rax
    .is_plus:
        ret

string_copy: ; передадим в rdi длину буфера, а в rsi указатель на буфер ; Плюс в rdx положим указатель н строку
    xor rax, rax
    .loop:
        test rdi, rdi
        je .end_false
        cmp byte [rdx], 0
        je .end_true
        mov cl, byte [rdx]
        mov byte [rsi], cl
        
        inc rsi
        inc rdx

        inc rax
        dec rdi

        jmp .loop

    .end_false:
        xor rax, rax ; в задании не сказано в каком регистре возвращать, поэтому вёрнём в rax
        ret
    .end_true:
        mov cl, byte [rdx]
        mov byte [rsi], cl
        ret

_start:


; mov rdi, 256
; mov rsi, buffer
; mov rdx, test_str
; call string_copy

; mov rdi, buffer
; call print_string

; mov rdi, 12345
; call print_uint

mov rsi, buffer
mov rdi, 256
call read_word

mov rdi, buffer
call print_string

call print_newline

mov rdi, 0
call exit


; mov rdi, test_int_str
; call parse_int




; mov rdi, buffer
; call print_string



;   test print_int
; mov rdi, 2345
; call print_int

; call print_newline


; mov rdi, 0
; call exit

; mov rdi, test_str
; call print_string

; mov rdi, 65
; call print_char

; call print_newline

; mov rdi, 12345
; call print_uint

; call print_newline

; mov rdi, str1
; mov rsi, str2
; call string_equals


