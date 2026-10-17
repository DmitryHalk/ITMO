    .data
.org             0x88
    ; символы ><+-.,[]
overflow_val:    .word  0xCCCCCCCC
stack:           .word  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 ; 10 ячеек под стек (0x88..0xAF)
data:            .word  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
code:            .word  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 ; соклько нужно выделить под код?

input_addr:      .word  0x80
output_addr:     .word  0x84

    ; D1 - zero
    ; D2 - data_ptr
    ; D3 - bracket_count
    ; D5 - длина кода и ptr
    
    ; A0 - input_addr
    ; A1 - output_addr
    ; A2 - code
    ; A3 - data
    ; A7 - SP (растёт вниз от конца stack = начало data)


    .text

_start:
    movea.l  input_addr, A0
    movea.l  (A0), A0                        ; A0 <- 0x80
    movea.l  output_addr, A1
    movea.l  (A1), A1                        ; A1 <- 0x84

    movea.l  code, A2                        ; A2 <- адресс code с поинтером D1
    movea.l  data, A3                        ; A3 <- адрес data с поинтером D2
    movea.l  data, A7                        ; SP <- конец области stack
    move.l   0, D5

    jsr      read_line                       ; читаем код
    jsr      execute_code                    ; выполняем
    jmp      end

read_line:
    ; добавить проверку того что код вмещается
    cmp.l    64, D5
    bge      error_overflow

    move.b   (A0), D0                        ; первый символ -> D0
    cmp.b    0x0A, D0                        ; D0 - 0x0A -> NZVC
    beq      read_done

    ; символ переноса строки я не записываю, а просто вывожу что нужно п осчётчику code, т.е. D1
    move.b   D0, 0(A2, D5)                   ; первый символ -> addres(code + code_ptr)
    add.l    1, D5

    cmp.b    0x5B, D0
    beq      left_bracket
    cmp.b    0x5D, D0
    beq      right_bracket

    jmp      read_line

left_bracket:
    add.l    1, D3                           ; bracket_count += 1
    jmp      read_line

right_bracket:
    sub.l    1, D3
    cmp.l    0, D3
    blt      drain
    jmp      read_line

drain:
    move.b   (A0), D0
    cmp.b    0x0A, D0
    bne      drain
    jmp      error

read_done:
    cmp.l    0, D3
    bne      error
    rts

execute_code:
    cmp.l    D1, D5
    beq      exec_done
    move.b   0(A2, D1), D0
    add.l    1, D1

    ; теперь смотрю что за символ и делаю дейтсвия

    cmp.b    0x3E, D0                        ; '>'
    beq      cmd_right
    cmp.b    0x3C, D0                        ; '<'
    beq      cmd_left
    cmp.b    0x2B, D0                        ; '+'
    beq      cmd_plus
    cmp.b    0x2D, D0                        ; '-'
    beq      cmd_minus
    cmp.b    0x2E, D0                        ; '.'
    beq      cmd_out
    cmp.b    0x2C, D0                        ; ','
    beq      cmd_in
    cmp.b    0x5B, D0                        ; '['
    beq      cmd_open
    cmp.b    0x5D, D0                        ; ']'
    beq      cmd_close

    ; проверка на пробельные символы

    cmp.b    32, D0                          ; ' '
    beq      execute_code
    cmp.b    9, D0                           ; '\t'
    beq      execute_code
    cmp.b    10, D0                          ; '\n'
    beq      execute_code
    cmp.b    13, D0                          ; '\r'
    beq      execute_code

    jmp      error

exec_done:
    rts                                      

cmd_right:
    add.l    4, D2
    cmp.l    120, D2
    bge      error
    jmp      execute_code
cmd_left:
    sub.l    4, D2
    blt      error
    jmp      execute_code
cmd_plus:
    move.l   0(A3, D2), D0                   ; вытащили значение
    add.l    1, D0
    bvs      error_overflow
    move.l   D0, 0(A3, D2)                   ; закинули обратно
    jmp      execute_code
cmd_minus:
    move.l   0(A3, D2), D0                   ; вытащили значение
    sub.l    1, D0
    bvs      error_overflow
    move.l   D0, 0(A3, D2)                   ; закинули обратно
    jmp      execute_code
cmd_out:
    move.l   0(A3, D2), D0
    move.b   D0, (A1)
    jmp      execute_code
cmd_in:
    move.l   D0, -(A7)       ; push D0 

    move.l   0(A3, D2), D0   
    move.b   (A0), D0        
    move.l   D0, 0(A3, D2) 

    move.l   (A7)+, D0       ; pop D0 
    jmp      execute_code
cmd_open:
    move.l   0(A3, D2), D0
    cmp.l    0, D0
    bne      execute_code                    ; != 0 -> идём дальше
    jsr      jump_forward                    ; == 0 -> ищем парную ']'
    jmp      execute_code
cmd_close:
    move.l   0(A3, D2), D0                   ; memory[data_ptr]
    cmp.l    0, D0
    beq      execute_code                    ; == 0 -> идём дальше
    jsr      jump_back                       ; != 0 -> ищем парную '['
    jmp      execute_code


jump_back:
    move.l   1, D3                           ; bracket_count = 1
    sub.l    2, D1                           ; code_ptr-- (уходим от текущей ']')
loop_jb:
    cmp.l    0, D1
    blt      js_2                            ; если code_ptr < 0 — на выход
    cmp.l    0, D3
    ble      js_2                            ; если bracket_count <= 0 — на выход

    move.b   0(A2, D1), D0                   ; D0 = code[code_ptr]
    cmp.b    0x5D, D0                        ; ']'
    bne      check_lb
    add.l    1, D3                           ; bracket_count++
    jmp      loop_jb_next
check_lb:
    cmp.b    0x5B, D0                        ; '['
    bne      loop_jb_next
    sub.l    1, D3                           ; bracket_count--
loop_jb_next:
    sub.l    1, D1
    jmp      loop_jb

js_2:
    cmp.l    0, D3
    bgt      error                           ; если остались незакрытые — ошибка
    add.l    2, D1                           ; вернуть D1 на позицию '['
    rts


jump_forward:
    move.l   1, D3
loop_jf:
    ; while code_ptr < len(code) and bracket_count > 0:
    cmp.l    D1, D5
    bgt      check
    jmp      js_1
check:
    cmp.l    0, D3
    bgt      ctne_jf_loop
    jmp      js_1
ctne_jf_loop:
    move.b   0(A2, D1), D0
    cmp.b    0x5B, D0                        ; '['
    bne      check_right_bracket
    add.l    1, D3
    jmp      ctne_jf_loop2
check_right_bracket:
    cmp.b    0x5D, D0                        ; ']'
    bne      ctne_jf_loop2
    sub.l    1, D3
    jmp      ctne_jf_loop2
ctne_jf_loop2:
    add.l    1, D1
    jmp      loop_jf

js_1:
    cmp.l    0, D3
    bgt      error
    rts


write_out:
 ; D0 -> порт вывода, через стековый кадр
    link     A6, -4
    move.l   D0, -4(A6)
    move.l   -4(A6), D0
    move.l   D0, (A1)
    unlk     A6
    rts

error_overflow:
    movea.l  overflow_val, A4                ; A4 <- адрес
    move.l   (A4), D0                        ; D0 <- значение
    jsr      write_out
    jmp      end

error:
    move.l   -1, D0
    jsr      write_out

end:
    halt

