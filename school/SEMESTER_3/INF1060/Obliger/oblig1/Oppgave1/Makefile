CC = gcc
CFLAGS = -Wall -Wextra -std=c99
O = -o

hello: oppgave1.c
	$(CC) $(CFLAGS) $(^) -o $@

run: hello
	./hello "Hallo verden" "dette er et argument" "og dette er et annet"

clean:
	rm -f hello