    .data

input_addr:      .word  0x80
output_addr:     .word  0x84
n:               .word  0x00
counter:         .word  0x00               ; кол-во единицы
result:          .word  0x02               ; чётное или не чётное
size_word:       .word  0x20
divider:         .word  0x02

    .text

_start:
    ; cla не нашёл инуструкции очистки аккумулятора
    load         input_addr
    load_acc
    store        n
summation:
    rem          divider
    add          counter
    store        counter

    load_imm     -1
    add          size_word
    store        size_word

    beqz         parity
    load         n

    div          divider
    store        n
    jmp          summation

parity:
    load_imm     0x01
    and          counter
    beqz         even

    store        result                      ; записать 1 в result
    jmp          end

even:
    store        result                      ; записать 0 в result

end:
    load         result
    store_ind    output_addr
    halt

