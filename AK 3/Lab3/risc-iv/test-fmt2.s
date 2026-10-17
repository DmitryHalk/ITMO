     ; a0 - temp io adress
     ; a1 - n - введённое число
     ; a2 - кол-во чисел
     ; a3 - total
     ; a4 - 2
    .text
_start:
    lui      sp, %hi(return_addr)            ; init stack
    addi     sp, sp, %lo(return_addr)

    lui      a0, %hi(input_addr)
    addi     a0, a0, %lo(input_addr)
    lw       a0, 0(a0)
    lw       a1, 0(a0)                       ;a1 <- n

    lui      a0, %hi(output_addr)
    addi     a0, a0, %lo(output_addr)
    lw       a0, 0(a0)

    ble      a1, zero, error                 ; n <= 0 -> error

    xor      a4, a4, a4
    addi     a4, a4, 2                       ; a4 = 2 ; для деления на 2

    ; вычисляем кол-во слагаемых
    addi     sp, sp, -4
    sw       a1, 0(sp)
    jal      ra, calc_count

    addi     sp, sp, 4

    ; вычисляем ариф прогрессию
    jal      ra, calc
    jal      ra, make_odd

    j        end

make_odd:
    rem      a5, a1, a4                      ; n % 2
    bne      a5, zero, odd                   ; if n%2 != 0 -> PC <- calc
    addi     a1, a1, -1                      ; a1 <- a1 - 1
odd:
    jr       ra

calc_count:
    lw       t0, 0(sp)

    div      a5, t0, a4
    add      a2, a2, a5
    rem      a5, t0, a4
    add      a2, a2, a5
    jr       ra

    .text
    .org     0x88

calc:
    addi     a1, a1, 1                       ; 1 + n -> a1
    div      a1, a1, a4                      ; a1 / 2
    mul      a3, a1, a2

    bgt      zero, a3, overflow

    ;write:
    sw       a3, 0(a0)
    jr       ra

overflow:
    lui      a1, %hi(overflow_val)
    addi     a1, a1, %lo(overflow_val)
    lw       a1, 0(a1)

    sw       a1, 0(a0)
    j        end

error:
    xor      a1, a1, a1
    addi     a1, a1, -1                      ; a1 <- -1

    sw       a1, 0(a0)                       ; -1 -> output-addr

end:
    halt

    .data
stack:           .word  0x00, 0x00
return_addr:     .word  0x00
overflow_val:    .word  0xCCCCCCCC
input_addr:      .word  0x80
output_addr:     .word  0x84

