FLAGS := -f elf64 -g
LIB_SRC  := assignment-1-io-library/lib.asm

.PHONY: all test clean
all: dict

dict: main.o dict.o lib.o
	ld -o $@ $^

lib.o: $(LIB_SRC)
	nasm $(FLAGS) -o $@ $<

dict.o: dict.asm dict.inc lib.inc
	nasm $(FLAGS) -o $@ $<

main.o: main.asm dict.inc lib.inc colon.inc words.inc
	nasm $(FLAGS) -o $@ $<

test: dict
	python3 test.py

clean:
	rm -f *.o dict
