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


read_symbol:
    @p input_addr a! @ \ пушу в стек первый символ
    dup
    0x0A
    xor \ Проверяю что он не знак перегоса
    if reorder_start \ если это перенос то 

    @p counter  \иначе проверяем счётчик
    31 inv 1 + + -if overflow 

    to_lower \ преобразую заглавные в строчные, иначе не трогаю

    @p counter 1 + 
    !p counter \ счётчик + 1

    read_symbol ; \джамп

reorder_start:
    drop      \ снимаю '\n'

    @p counter
reorder_loop:
    dup
    if reorder_done \ дублирую потому что if снимает стэк
    swap \ меняю местами сч1тчик и первый символ
    >r \ закидываю в его в ретёрн стек
    -1 + \ декремент счётчика
    reorder_loop ;
reorder_done:
    drop \  убираю ноль из стека

    0 a!
    @p counter !+ \ записываю на нулевую позицию буфера счётчик
    a
    !p buf_ptr     \ передвигаю указатель

    @p counter \ счётчик в стек для выовда

write_loop:
    dup
    if write_done
    r> \ достаёт из ретёрн стека символ
    dup
    @p output_addr a!
    !                          \ вывод символа в порт
    @p buf_ptr a!
    !+                         \ запись символа в буфер, 
    a
    !p buf_ptr \ A++
    -1 + \ декремент счётчика
    write_loop ;

write_done:
    drop

    \ докидывает символы 0x5F до конца буфера, чтобы не было мусора
    @p buf_ptr a!
    34
    @p counter
    inv 1 + +
    >r 
fill_rest_loop:
    0x5F
    !+
    next fill_rest_loop

    end ;

overflow:
    -858993460
    @p output_addr a!
    !
    end ;


swap:
    over >r >r drop r> r>
    ;

to_lower:
    dup
    0x41 inv 1 + +
    dup
    -if greater_A
    drop
    ;

greater_A:
    26 inv 1 + +
    -if not_upper
    0x20 +
    ;

not_upper:
    ;

end:
    halt