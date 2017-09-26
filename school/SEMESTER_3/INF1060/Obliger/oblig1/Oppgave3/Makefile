CC = gcc
CFLAGS = -Wall -Wextra -std=c99

oppgave3: oppgave3.c
	$(CC) $(CFLAGS) $(^) -o $@

run: oppgave3
	./oppgave3 


check: oppgave3
	valgrind --leak-check=full -v ./oppgave3
clean:
	rm -f oppgave3