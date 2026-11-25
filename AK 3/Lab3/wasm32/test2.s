.data

result_buf: .word 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
op_stack:   .word 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0

    .org 0x88
input_buf:  .word 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
res_len:    .word 0
op_len:     .word 0
in_num:     .word 0
exit_flag:  .word 0

    .text

halt_error:
    .frame () $output_addr, $error_val
    locals.reserve 2
    i32.const 0x84
    local.set $output_addr
    i32.const -1
    local.set $error_val

    local.get $output_addr
    local.get $error_val
    i32.store

    i32.const exit_flag
    i32.const 1
    i32.store
    return

halt_overflow:
    .frame () $output_addr, $overflow_val
    locals.reserve 2
    i32.const 0x84
    local.set $output_addr
    i32.const 0xCCCCCCCC
    local.set $overflow_val

    local.get $output_addr
    local.get $overflow_val
    i32.store

    i32.const exit_flag
    i32.const 1
    i32.store
    return

print:
    .frame ($n)
    i32.const res_len
    i32.load
    i32.const 63        ; макс. длина вывода (64 - 1 байт под '\0')
    i32.ge_u
    if
        i32.const halt_overflow call 0, 0
        return
    end

    i32.const result_buf
    i32.const res_len
    i32.load
    i32.add
    local.get $n
    i32.store8

    i32.const res_len
    i32.const res_len
    i32.load
    i32.const 1
    i32.add
    i32.store
    return

; Закрытие числа пробелом
close_num:
    i32.const in_num
    i32.load
    if
        i32.const ' '
        i32.const print call 1, 0
        i32.const in_num
        i32.const 0
        i32.store
    end
    return

; Проверка на цифру
is_number:
    .frame ($n)
    local.get $n
    i32.const '0'
    i32.ge_s
    local.get $n
    i32.const '9'
    i32.le_s
    i32.and
    return

; Операции со стеком
push_op:
    .frame ($n)
    i32.const op_stack
    i32.const op_len
    i32.load
    i32.add
    local.get $n
    i32.store8

    i32.const op_len
    i32.const op_len
    i32.load
    i32.const 1
    i32.add
    i32.store
    return

pop_op:
    i32.const op_len
    i32.const op_len
    i32.load
    i32.const 1
    i32.sub
    i32.store

    i32.const op_stack
    i32.const op_len
    i32.load
    i32.add
    i32.load8_u
    return

peek_op:
    i32.const op_len
    i32.load
    i32.eqz
    if
        i32.const 0
        return
    end
    i32.const op_stack
    i32.const op_len
    i32.load
    i32.const 1
    i32.sub
    i32.add
    i32.load8_u
    return

; Приоритет операций
is_priority:
    .frame ($n)
    local.get $n
    i32.const '+'
    i32.eq
    local.get $n
    i32.const '-'
    i32.eq
    i32.or
    if
        i32.const 1
        return
    end

    local.get $n
    i32.const '*'
    i32.eq
    local.get $n
    i32.const '/'
    i32.eq
    i32.or
    if
        i32.const 2
        return
    end

    i32.const 0
    return

main:
    .frame () $i, $n, $priority, $input_addr, $output_addr, $max_buf_len, $newline, $null_term
    locals.reserve 8

    i32.const 0x80
    local.set $input_addr
    i32.const 0x84
    local.set $output_addr
    i32.const 64
    local.set $max_buf_len
    i32.const 0x0A
    local.set $newline
    i32.const 0x00
    local.set $null_term

    i32.const 0
    local.set $i

    ; чтение до конца строки в input_buf
    loop
        local.get $i
        local.get $max_buf_len
        i32.eq
        if
            i32.const halt_overflow call 0, 0
            return
        end

        local.get $input_addr
        i32.load8_u
        local.set $n

        i32.const input_buf
        local.get $i
        i32.add
        local.get $n
        i32.store8

        local.get $i
        i32.const 1
        i32.add
        local.set $i

        local.get $n
        local.get $newline
        i32.eq
        local.get $n
        local.get $null_term
        i32.eq
        i32.or
        i32.eqz
        br_if 0
    end

    ; разбор строки
    i32.const 0
    local.set $i

    loop
        i32.const input_buf
        local.get $i
        i32.add
        i32.load8_u
        local.set $n

        local.get $i
        i32.const 1
        i32.add
        local.set $i

        ; 1. Конец строки (0x0A или 0x00)
        local.get $n
        local.get $newline
        i32.eq
        local.get $n
        local.get $null_term
        i32.eq
        i32.or
        if
            i32.const close_num call 0, 0

            ; Выталкиваем остатки стека
            loop
                i32.const op_len
                i32.load
                if
                    i32.const pop_op call 0, 1
                    local.tee $n
                    i32.const '('
                    i32.eq
                    if
                        i32.const halt_error call 0, 0
                        return
                    end

                    local.get $n
                    i32.const print call 1, 0
                    i32.const ' '
                    i32.const print call 1, 0
                    br 0
                end
            end

            i32.const exit_flag
            i32.load
            if
                return
            end

            ; Удаление хвостового пробела
            i32.const res_len
            i32.load
            if
                i32.const res_len
                i32.const res_len
                i32.load
                i32.const 1
                i32.sub
                i32.store

                i32.const result_buf
                i32.const res_len
                i32.load
                i32.add
                local.get $null_term
                i32.store8
            end

            ; Выгрузка готовой строки в порт вывода
            i32.const 0
            local.set $i
            loop
                local.get $i
                i32.const res_len
                i32.load
                i32.lt_u
                if
                    local.get $output_addr
                    i32.const result_buf
                    local.get $i
                    i32.add
                    i32.load8_u
                    i32.store8

                    local.get $i
                    i32.const 1
                    i32.add
                    local.set $i
                    br 0
                end
            end
            return
        else
            ; 2. Пробел
            local.get $n
            i32.const ' '
            i32.eq
            if
                i32.const close_num call 0, 0
            else
                ; 3. Число
                local.get $n
                i32.const is_number call 1, 1
                if
                    local.get $n
                    i32.const print call 1, 0
                    i32.const in_num
                    i32.const 1
                    i32.store
                else
                    i32.const close_num call 0, 0

                    ; 4. Оператор (+, -, *, /)
                    local.get $n
                    i32.const is_priority call 1, 1
                    local.tee $priority
                    if
                        loop
                            i32.const peek_op call 0, 1
                            i32.const is_priority call 1, 1
                            local.get $priority
                            i32.ge_u
                            if
                                i32.const pop_op call 0, 1
                                i32.const print call 1, 0
                                i32.const ' '
                                i32.const print call 1, 0
                                br 0
                            end
                        end
                        local.get $n
                        i32.const push_op call 1, 0
                    else
                        ; 5. Скобка '('
                        local.get $n
                        i32.const '('
                        i32.eq
                        if
                            local.get $n
                            i32.const push_op call 1, 0
                        else
                            ; 6. Скобка ')'
                            local.get $n
                            i32.const ')'
                            i32.eq
                            if
                                loop
                                    i32.const op_len
                                    i32.load
                                    i32.eqz
                                    if
                                        i32.const halt_error call 0, 0
                                        return
                                    end

                                    i32.const pop_op call 0, 1
                                    local.tee $n

                                    i32.const '('
                                    i32.eq
                                    i32.eqz
                                    if
                                        local.get $n
                                        i32.const print call 1, 0
                                        i32.const ' '
                                        i32.const print call 1, 0
                                        br 0
                                    end
                                end
                            else
                                ; 7. Неизвестный символ
                                i32.const halt_error call 0, 0
                                return
                            end
                        end
                    end
                end
            end
        end
        br 0
    end
    return

_start:
    i32.const 0x800 ; начальный адрес стека вызовов
    sp.init
    i32.const main call 0, 0
    halt