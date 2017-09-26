############################################
# Oblig 3 INF-2270
# Tormod Brændshøi/tormobr
# 02.05.2017
#				
#
	.extern	fwrite, fread
########################################################################################
##################################### DEL 1 ############################################
########################################################################################

.globl	writebyte
 # Navn:	writebyte
 # Synopsis:	Skriver en byte til en binÃ¦rfil.
 # C-signatur: 	void writebyte (FILE *f, unsigned char b)
 # Registre:	%EBP, %ESP, EAX
writebyte:
	push 	%EBP			# standar start
	movl 	%ESP,%EBP		#

	# pusher parametere på stacken
	pushl 	8(%EBP)			
	pushl	$1
	pushl	$1

	# pusher addressen på stacken
	leal 	12(%EBP), %EAX
	pushl 	%EAX

	call 	fwrite 			#kaller fwrite

	movl 	%EBP,%ESP		#standar avslutning
	popl 	%EBP
	ret


########################################################################################
##################################### DEL 2 ############################################
########################################################################################

.globl	writeutf8char
 # Navn:	writeutf8char
 # Synopsis:	Skriver et tegn kodet som UTF-8 til en binÃ¦rfil.
 # C-signatur: 	void writeutf8char (FILE *f, unsigned long u)
 # Registre:	EAX ; holder byte som skal skrives
 #				EDX ; Hele symbolet
 #				ECX ; antall shift som skal gjøres


writeutf8char:
	pushl	%EBP			# Standard funksjonsstart
	movl	%ESP,%EBP		

	cmpl 	$0x7F, 12(%EBP)		# sjekker om det er 1 byte
	jle		one_byte			# jumper til "one_byte"

	cmpl 	$0x7FF, 12(%EBP)	# sjekker om det er 2 byte
	jle		two_byte			# jumper til "two_byte"

	cmpl 	$0xFFFF, 12(%EBP)	# sjekker om det er 3 byte
	jle		three_byte			# jumper til "three_byte"
	jmp 	four_byte

	one_byte:					# Hvis det bare er en byte som skal skrives
		pushl	12(%EBP)		# pusher params til writebyte på stack
		pushl	8(%EBP)			
		call	writebyte 		# Kaller på writebyte
		jmp 	wu8_x			# jumper til wu8_x

	two_byte:
		movl	12(%EBP),%EAX	# flytter uni-code-tegnet i EAX
		movl	%EAX,%EDX		# flytter det inn i EDX for å lagre det
		movl	$6, %ECX		# Antall shift vi starter på med 2 byte
		shrl	%CL, %EAX		# shifter 6 til høyre
		orl		$0xc0, %EAX		# bitmasker, for å få den første byten

		
		jmp 	prep_loop		# Jumper inn i loop, som skriver bytes til fil


	# Gjør akkurat det sammen som two-byte, bare med 3 bytes i stedet.
	# Blir derfor litt mer shifting og bit-opperasjoner
	# Flytter derfor 12, og ikke 6 inn i ECX også
	three_byte:
		movl	12(%EBP), %EAX
		movl	%EAX,%EDX		
		movl	$12, %ECX		# Antall shift vi starter på med 3 byte
		shrl	%CL, %EAX
		orl		$0xE0, %EAX

		jmp 	prep_loop


	# Samme prinsipp, mover 18 inn i ECX, fordi vi må shifte mer.
	four_byte:
		movl	12(%EBP), %EAX
		movl	%EAX,%EDX
		movl	$18, %ECX		# Antall shift vi starter på med 4 byte

		shrl	%CL, %EAX
		orl		$0xF0, %EAX

		jmp 	prep_loop

	# Pusher param på stacken for det første byten, og kaller writebyte

	prep_loop:
		pushl	%ECX			# Legger antall shift på stacken
		pushl 	%EDX			# legger unicodetegnet på stacken
		pushl 	%EAX			# Byte som skal skrives, param.
		pushl	8(%EBP)			# fil, param
		call	writebyte 		# kaller writebyte
		jmp 	wu8_loop 		# jumper til loopen, for resterende bytes


	wu8_loop:
		addl	$8,%ESP			# Fjerner param fra stack
		popl    %EDX			# popper stacken i EDX
		popl	%ECX			# popper stacken i ECX(antall shift)
		movl	%EDX, %EAX		# mover EDX inn i EAX

		subl	$6, %ECX		# trekker fra 6 i antall shift
		cmpl	$0, %ECX		# sjekker om antall shift er mindere enn null
		jl  	wu8_x			# hvis ja, avslutt

		shrl	%CL, %EAX		# Shifter så mange ganger det trengs
		orl 	$0x80, %EAX		# legger til header
		andl	$0xbf, %EAX	

		#pusher til stacken, og gjør kall på writebyte	
		pushl	%ECX			
		pushl	%EDX
		pushl	%EAX
		pushl	8(%EBP) 
		call 	writebyte

		jmp 	wu8_loop		# looper på nytt

	wu8_x:
		movl    %EBP, %ESP		# Standard
		popl	%EBP		
		ret						# retur.


########################################################################################
##################################### DEL 3 ############################################
########################################################################################


.globl	readbyte
 # Navn:	readbyte
 # Synopsis:	Leser en byte fra en binÃ¦rfil.
 # C-signatur: 	int readbyte (FILE *f)
 # Registre:	%EBP, %ESP, EAX
	
#Blir veldig lik som writebyte, men litt annerledes.
#Må også sjekke returverdien, om den er -1.
readbyte:
	pushl 	%EBP					# Standard funksjonsstart
	movl 	%ESP,%EBP
		
	pushl 	$0
	movl 	%ESP,%EDX

	# Legger inn parametere (1, 1) til fread
	pushl 	8(%EBP)	
	pushl 	$1
	pushl 	$1	
	pushl 	%EDX
	call 	fread

	addl 	$16,%ESP				# fjerner parametere fra stack
	cmpl 	$0,%EAX					# comparer retur med 0
	jg 		rb_x					# jump hvis den er større til rb_x
	movl 	$-1,-4(%EBP)			# Mover -1, hvis den ikke er grater.
	rb_x: 	movl -4(%EBP),%EAX		# mover c inn i EAX


	movl %EBP,%ESP					# stadar avsluttning.
	popl %EBP		
	ret 							# retur


########################################################################################
##################################### DEL 4 ############################################
########################################################################################


.globl	readutf8char
 # Navn:	readutf8char
 # Synopsis:	Leser et Unicode-tegn fra en binÃ¦rfil.
 # C-signatur: 	long readutf8char (FILE *f)
 # Registre:	%EAX ; Returverdi til readbyte(en og en byte som leses inn)
 #				%EDX ; Legger sammen alle bytes, og lagres her
 #				%ECX ; Counter som styrer loopen, avhengig av antall bytes


readutf8char:
	pushl	%EBP			# Standard funksjonsstart
	movl	%ESP,%EBP		#

	pushl 	8(%EBP) 			# legger fil param på stacken
	call 	readbyte		
	addl 	$4, %ESP			# fjerner param fra stacken
	
	cmpl 	$-1, %EAX			# sjekker returverdien til readbyte
	je 		ru8_x				# avslutter hvis -1

	cmpl  	$0x80, %EAX			# sjekker om en er kun 1 byte
	jb 		ru8_x				# avslutter

	cmpl  	$0xe0, %EAX			# sjekker om det er to byte
	jb 		read_two			# hopper til "read_two"
	
	cmpl  	$0xf0, %EAX			# sjekker om det er 3 bytes
	jb 		read_three			# hopper til "read_three"  
	
	jmp 	read_four			# hopper til "read_four" hvis ingen over slår inn



	# Hvis det er 2 byte som skal leses
	read_two:
		movl 	%EAX, %EDX		# Flytter første byte over i EDX
		andl 	$0x1f, %EDX		# and'er med 0001 1111 for å fjerne header
		movl	$1, %ECX		# mover 1 inn i ECX, brukes som counter i loop
		jmp 	readbyte_loop	# kaller på loopen

	# Hvis det er 3 bytes som skal leses
	# Gjøres mer eller mindre det samme som i read_two
	# Forskjellen er header, og counter i ECX skal være 1 mer.
	read_three:
		movl 	%EAX, %EDX		
		andl 	$0xF, %EDX
		movl	$2, %ECX
		jmp 	readbyte_loop

	# Hvis det er 4 bytes som skal leses
	# Annen header, og counter økes med 1. Eller tilsvarende "read_three"
	 read_four:
		movl 	%EAX, %EDX
		andl 	$0x7, %EDX
		movl	$3, %ECX
		jmp 	readbyte_loop


	# Loop som leser inn en og en byte, og slår de sammen
	readbyte_loop:
		cmpl	$0, %ECX		# Sjekker om counter er 0
		je		ru8_x			# Jumper til avslutning
		decl	%ECX			# Trekker 1 fra counter

		shll 	$6, %EDX		# Shifter til venstre
		pushl	%ECX			# Pusher counter på stacken
		pushl 	%EDX			# Pusher unicode-tegnet på stacken
		pushl 	8(%EBP)			# pusher fil på stacken, param.
		call 	readbyte 		# Kaller på readbyte

		addl 	$4, %ESP		# Fjerner parametere fra stacken

		popl 	%EDX			# Henter tilbake verdi i EDX, unicode-tegn
		popl	%ECX			# Henter tilbake verdi i ECX, counter
		andl 	$0x3f, %EAX		# Fjerner header på den nye byten

		#Legger den nye byten til tegnet, og kjører loopen på neste byte
		orl	 	%EAX, %EDX		
		movl	%EDX, %EAX
		jmp 	readbyte_loop


	ru8_x:
		movl    %EBP, %ESP		# Standard avslutning
		popl	%EBP		
		ret