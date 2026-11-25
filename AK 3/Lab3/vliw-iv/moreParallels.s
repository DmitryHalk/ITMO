    .data

input_addr:      .word  0x80
output_addr:     .word  0x84
overflow_val:    .word  0xCCCCCCCC

    .text

_start:
    lui s1, %hi(input_addr)        / lui s2, %hi(output_addr)       /              /
    addi s1, s1, %lo(input_addr)   / addi s2, s2, %lo(output_addr)  /              /
                                   /                                / lw s1, 0(s1) /
                                   /                                / lw s2, 0(s2) /
    addi t5, zero, 1               /                                / lw a2, 0(s1) / bge a2, t5, loop       ; a2 = N
                                   /                                /              / j empty

    .text
    .org 0x88

loop:
                                   /                                / lw a0, 0(s1) /                        ; a0 = x
    add t0, a0, a0                 / sub t2, zero, a0               / lw a1, 0(s1) /                        ; t0 = 2x, t2 = -x, a1 = y
    xor a3, a0, t0                 / add t1, a1, a1                 /              /
    xor a4, a1, t1                 / add t3, t0, a0                 /              /
    or a3, a3, a4                  / and a5, a0, t2                 /              / blt a3, zero, overflow
    add t4, t1, t1                 / addi t6, t1, 5                 /              / blt a5, zero, overflow ; t4 = 4y, t6 = 2y+5
    addi t5, t2, -7                / xor a5, t1, t4                 /              / blt t6, t1, overflow   ; t5 = -x-7
    xor a3, t0, t3                 / xor a4, a0, t3                 /              / blt t2, t5, overflow
    and a3, a3, a4                 / add a1, t5, t4                 /              /                        ; a1 = v
    or a3, a3, a5                  / add a0, t3, t6                 /              / blt a3, zero, overflow ; a0 = u
    xor a3, t3, a0                 / xor a4, t6, a0                 /              /
    xor a5, t5, a1                 / xor a6, t4, a1                 /              /
    and a3, a3, a4                 / and a5, a5, a6                 /              /
    or a3, a3, a5                  /                                /              / blt a3, zero, overflow
    addi a2, a2, -1                /                                / sw a0, 0(s2) /
                                   /                                / sw a1, 0(s2) / bnez a2, loop

exit:
                                   /                                /              / halt

overflow:
    lui t1, %hi(overflow_val)      /                                /              /
    addi t1, t1, %lo(overflow_val) /                                /              /
                                   /                                / lw t1, 0(t1) /
                                   /                                / sw t1, 0(s2) / j exit

empty:
    addi a1, zero, -1              /                                /              / beq a2, zero, exit
                                   /                                / sw a1, 0(s2) / j exit