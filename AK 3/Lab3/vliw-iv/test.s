    .data

input_addr:      .word  0x80
output_addr:     .word  0x84
overflow_val: .word 0xCCCCCCCC

    .text

_start:
    lui s1, %hi(input_addr)/ lui s2, %hi(output_addr)   /   /
    addi s1, s1, %lo(input_addr)/ addi s2, s2, %lo(output_addr)   /   /
    /   /lw s1, 0(s1)   /
    /   /lw  s2, 0(s2)   /

    addi t5 ,zero, 1/ /lw a2, 0(s1) / ; a2 = N
    / / / bge a2, t5, loop
    / / / j empty
    
    .text
    .org 0x88
    
    loop:
        addi a2, a2, -1/ / /
        / / / blt a2, zero, exit

        / / lw a0, 0(s1)/ ; => a0 = x
        / / lw a1, 0(s1)/ jal ra, calc_func; => a1 = y прыг на calc_func

        / / /jal ra, write_out
        / / / j loop
        

    ; input: a0=u, a1 =v
    ; output: 0x84<-a0 0x84<-a1 
    write_out:
        / / sw a0, 0(s2)/
        / / sw a1, 0(s2)/        

        / / / jalr zero, ra, 0



    ; input: a0 = x; a1 = y
    ; output: a0 = u; a1 = v
calc_func: 
    add t0, a0, a0 / add t1, a1, a1 / / 
    xor a3, a0, t0 / xor a4, a1, t1 / / 
    or a3, a3, a4 / sub t2, zero, a0 / / 
    and a4, a0, t2 / add t3, t0, a0 / / blt a3, zero, overflow
    add t4, t1, t1 / xor a3, t0, t3 / / blt a4, zero, overflow
    xor a4, a0, t3 / xor a5, t1, t4 / / 
    and a3, a3, a4 / addi t5, t2, -7 / / 
    or a3, a3, a5 / addi t6, t1, 5 / / 
    add a0, t3, t6 / add a1, t5, t4 / / blt a3, zero, overflow
    xor a3, t3, a0 / xor a4, t6, a0 / / blt t2, t5, overflow
    xor a5, t5, a1 / xor a6, t4, a1 / / blt t6, t1, overflow
    and a3, a3, a4 / and a5, a5, a6 / / 
    or a3, a3, a5 / / / 
    / / / blt a3, zero, overflow
    / / / jalr zero, ra, 0



    
    overflow:
        lui t1, %hi(overflow_val)/   /   /
        addi t1, t1, %lo(overflow_val)/   /   /
        / / lw t1, 0(t1)/ 

        / / sw t1, 0(s2)/

        / / /j exit


    empty:
        addi a1, zero, -1/ / /beq a2, zero, exit 
        / / sw a1, 0(s2)/
        / / / j exit
        
    exit:
        / / /halt