CC = gcc
CFLAGS = -m32 

oblig3: testfile.c oblig3.s
	$(CC) $(CFLAGS) $(^) -o oblig3

run: oblig3
	./oblig3

clean:
	rm -f oblig3