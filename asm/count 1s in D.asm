    MVI B, 00H
    MVI C, 08H
    MOV A, D
    BACK: RAR
    JNC SKIP
    INR B
    SKIP: DCR C
    JNZ BACK
    HLT 
    END