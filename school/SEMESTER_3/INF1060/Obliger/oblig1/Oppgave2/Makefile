CC = gcc
CFLAGS = -Wall -Wextra -std=c99

oppgave2: oppgave2.c
	$(CC) $(CFLAGS) $(^) -o $@

run: oppgave2
	./oppgave2 


check: oppgave2
	valgrind --leak-check=full -v ./oppgave2

clean:
	rm -f oppgave2