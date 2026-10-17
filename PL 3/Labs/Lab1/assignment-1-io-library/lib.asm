section .data


buffer: times 256 db 0

section .text
global exit
global string_length
global print_string
global print_char
global print_newline
global print_uint
global print_int
global string_equals
global read_char
global read_word
global parse_uint
global parse_int
global string_copy
 
; Принимает код возврата и завершает текущий процесс
exit:
    mov rax, 60
    syscall
    ; код возврата передадим в rdi

; Принимает указатель на нуль-терминированную строку, возвращает её длину
string_length: ; строку передадим в rdi
    xor rax, rax
    .loop:
        cmp byte [rdi+rax], 0
        je .end
        inc rax
        jmp .loop
    .end:
        ret

; Принимает указатель на нуль-терминированную строку, выводит её в stdout
print_string: ; передаём указатель в rdi
    push rbx
    mov rbx, rdi
    call string_length ; теперь в rax хранится длина строки
    mov rdx, rax
    mov rsi, rbx
    mov rax, 1
    mov rdi, 1
    syscall
    pop rbx
    ret




; Принимает код символа и выводит его в stdout
print_char: ; передаём в rdi код символа
    push rdi
    mov rax, 1
    mov rdi, 1
    mov rsi, rsp
    mov rdx, 1
    sub rsp, 8
    syscall
    add rsp, 8
    pop rdi
    ret



; Переводит строку (выводит символ с кодом 0xA)
print_newline:
    mov rdi, 0xA
    call print_char
    ret


; Выводит беззнаковое 8-байтовое число в десятичном формате 
; Совет: выделите место в стеке и храните там результаты деления
; Не забудьте перевести цифры в их ASCII коды.
print_uint: ; передадим число в rdi
    mov rax, rdi
    push 0 ; терминатор
    .loop_div:
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
        sub rsp, 8
        call print_string
        add rsp, 8
        ret

; Выводит знаковое 8-байтовое число в десятичном формате 
print_int: ; передаём в rdi число ; добавить вывод знака
    test rdi, rdi
    jns .is_plus

    push rdi
    mov rdi, '-'
    call print_char
    pop rdi

    neg rdi

    .is_plus:
        sub rsp, 8 ; для выравнивания
        call print_uint
        add rsp, 8
        ret


; Принимает два указателя на нуль-терминированные строки, возвращает 1 если они равны, 0 иначе
string_equals: ; передадим аргументы в rdi и rsi
    xor rcx, rcx

    .loop:
        mov al, [rdi+rcx]
        mov dl, [rsi+rcx]
        cmp al, dl  
        jne .end_false

        test al, al
        je .end_true

        inc rcx
        jmp .loop

    .end_false:
        xor rax, rax 
        ret

    .end_true:
        mov rax, 1
        ret

; Читает один символ из stdin и возвращает его. Возвращает 0 если достигнут конец потока
read_char: ; читает и вовращает символ в rax
    push 0
    mov rax, 0
    mov rdi, 0
    mov rsi, rsp
    mov rdx, 1
    syscall

    test rax, rax
    pop rax
    jne .not_EOF
    xor rax, rax
    .not_EOF:
    ret 


; Принимает: адрес начала буфера, размер буфера
; Читает в буфер слово из stdin, пропуская пробельные символы в начале, .
; Пробельные символы это пробел 0x20, табуляция 0x9 и перевод строки 0xA.
; Останавливается и возвращает 0 если слово слишком большое для буфера
; При успехе возвращает адрес буфера в rax, длину слова в rdx.
; При неудаче возвращает 0 в rax
; Эта функция должна дописывать к слову нуль-терминатор

read_word: ; передадим аргументы в rdi(адресс буфера) и rsi(длина буфера)
    lea rcx, [rsi]
    push rbx
    mov rbx, rdi
    .skip:
        push rdi
        push rcx
        call read_char
        pop rcx
        pop rdi
 
        test rax, rax ; проверка на EOF
        je .end_error
 
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
        push rdi
        push rcx
        call read_char
        pop rcx
        pop rdi
    .write2:
        test rax, rax ; чекаем eof
        je .end_succes
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
        xor rdx, rdx
        ret
 
    .end_succes:
        mov byte [rbx], 0 ; дописываем терминатора
        sub rbx, rdi ; rdi хранит исходный адрес буфера
        mov rdx, rbx
        mov rax, rdi
        pop rbx
        ret


 

; Принимает указатель на строку, пытается
; прочитать из её начала беззнаковое число.
; Возвращает в rax: число, rdx : его длину в символах
; rdx = 0 если число прочитать не удалось
parse_uint: ; передадим указатель в rdi
    xor rax, rax
    xor rdx, rdx
    .loop:
        movzx rcx, byte [rdi+rdx]
        push rcx
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




; Принимает указатель на строку, пытается
; прочитать из её начала знаковое число.
; Если есть знак, пробелы между ним и числом не разрешены.
; Возвращает в rax: число, rdx : его длину в символах (включая знак, если он был) 
; rdx = 0 если число прочитать не удалось
parse_int:
    xor rax, rax
    mov al, byte [rdi]
    cmp rax, 45
    je .is_sign
    cmp rax, 43
    je .is_sign
 
    jmp .no_sign

    .is_sign:
        push rax
        inc rdi
        jmp .parse_uint
 
    .no_sign:
        push 0
 
    .parse_uint:
        call parse_uint
        test rdx, rdx
        jz .not_num

    pop rcx
    test rcx, rcx
    jz .end
    cmp rcx, 45
    jne .is_plus
    .is_minus:
        inc rdx
        neg rax
        jmp .end
    .is_plus:
        inc rdx
    .end:
        ret

    .not_num:
        pop rcx
        ret
; Принимает указатель на строку, указатель на буфер и длину буфера
; Копирует строку в буфер
; Возвращает длину строки если она умещается в буфер, иначе 0 
string_copy: ; передадим в rdi указатель н строку, а в rsi указатель на буфер ; Плюс в rdx положим длину буфера
    xor rax, rax
    .loop:
        test rdx, rdx
        je .end_false
        cmp byte [rdi], 0
        je .end_true
        mov cl, byte [rdi]
        mov byte [rsi], cl
        
        inc rsi
        inc rdi
 
        inc rax
        dec rdx
 
        jmp .loop
 
    .end_false:
        xor rax, rax ; в задании не сказано в каком регистре возвращать, поэтому вёрнём в rax
        ret
    .end_true:
        mov cl, byte [rdi]
        mov byte [rsi], cl
        ret
    
