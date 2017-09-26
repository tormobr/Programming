CC = gcc
CFLAGS = -Wall -Wextra -std=c99

oblig2: oblig2.c
	$(CC) $(CFLAGS) $^ -o $@

run: oblig2
	./oblig2 "out.dat"


check: oblig2
	valgrind --leak-check=full -v ./oblig2

clean:
	rm -f oblig2