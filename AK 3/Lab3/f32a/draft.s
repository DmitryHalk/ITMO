.data

    .org 0x00
buffer:          .byte  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
counter:         .word  0x00
buf_ptr:         .word  0x00
input_addr:      .word  0x80
output_addr:     .word  0x84


    .text
.org             0x90

_start:

lit buffer
1 + !p buf_ptr \ инициализация buf_ptr указывает на место записи первого символа, без счётчика
@p input_addr a! @ \ в стеке первый символ


read_symbol:
    @p input_addr a! @
    @p buf_ptr a!
    !+ 
    a
    !p buf_ptr

    dup
    0x0A
    xor
    if output

    @p counter 1 +
    !p counter

    if check_overflow
    read_symbol ;

check_overflow:
    @p counter 31 inv 1 + +
    -if overflow    
    ;

overflow:
    -858993460
    @p output_addr a!
    !
    halt


output:
    @p counter
    !p buffer

    1 + !p buf_ptr \ инициализация buf_ptr указывает на место записи первого символа, без счётчика
    output_loop:
        @p counter 
        lit buf_ptr inv 1 + +
        if end \ проверяем кол-во выведенных символов
        
        @p buf_ptr a!
        @+
        a !p buf_ptr \ использую указаетль для вывода, вывожу и сдвигаю его на 1
        output_loop ;

end:
    halt