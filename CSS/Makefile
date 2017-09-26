CC = gcc
CFLAGS = -Wall -Wextra -std=gnu99 -g

client: client.c
	$(CC) $(CFLAGS) $(^) -o $@

server: server.c
	$(CC) $(CFLAGS) $(^) -o $@

runclient: client
	./client localhost 4444

runserver: server
	./server prog.job 4444

checkclient: client
	valgrind ./client tormod-VirtualBox 4444

checkserver: server
	valgrind ./server haiku.job 4444

clean:
	rm -f client && rm -f server