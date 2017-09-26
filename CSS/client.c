#include <signal.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <string.h>
#include <netdb.h> 
#include <errno.h>
#include <sys/wait.h>


//global vars
static int pipe1[2];
static int pipe2[2];
static int sock;
static pid_t parrentpid;

/*
 * This function creates the socket fds. creates a struct
 * with the ip and port, converts the ip from "string" to binary
 * and try to connect to server.
 *
 * INPUT:
 *		char* IP:   The ip adress of the server
 *		char* port: The port number
 *
 *
 * RETURN:
 *		returns the socket on success
 *		returns -1 on failure. eg. wrong with ip-address,
 *		cant connect or with problems creating the actual socket.
 *
 */
int createSocket(char* IP, char* port){
	int portNumber = atoi(port);
	int sock;
	sock = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	if(sock == -1){
		perror("socket()");
		return -1;
	}

	struct sockaddr_in server_address;
	memset(&server_address, 0, sizeof(struct sockaddr_in));
	server_address.sin_family = AF_INET;
	server_address.sin_port = htons(portNumber);
    
	if (inet_pton(AF_INET, IP, &server_address.sin_addr.s_addr) != 1){
		perror("inet_pton()");
		return -1;
	}

	printf("trying to connect to: %s\n", IP);
	if (connect(sock, (struct sockaddr *)&server_address, sizeof(server_address)) != 0){
		perror("connect()");
		return -1;
	}

	printf("connected to %s : %d\n", IP, portNumber);
	return sock;
}


/* 
 * This function sends the message: 'E' to the server, if the client
 * disconnects due to an error. It also sends a char 'Q' to the 
 * children, telling them that the client is shuting down, preventing
 * "zombie" processes. Then exitst the program.
 *
 * INPUT:
 *		none, void
 *
 * RETURN:
 *		void function, doesnt return anything.
 *
 */
void sendError(){
	//Sending E to server, telling it client terminating due to error
	if(sock != -1){
		char msg = 'E';
		if(send(sock, &msg, sizeof(char), 0) == -1){
			perror("send()");
			exit(EXIT_FAILURE);
		}
	}
	
	//sending message to children telling them to exit/return
	char pipemsg = 'Q';
	write(pipe1[1], &pipemsg, sizeof(char));
	write(pipe2[1], &pipemsg, sizeof(char));
	wait(NULL);
	wait(NULL);
	printf("** both children terminated **\n");
}

/* 
 * This function takes the text/job from the server, and sends it
 * to the correct child depending on the jobtype
 *
 * INPUT:
 *		char string[]: the text/job from the server
 *
 * RETURN:
 *		returns 0 on success
 *		returns -1 on failure, if the jobtype is not valid
 */
int sendToChild(char string[]){
	char jobType;
	unsigned char textLength;

	jobType = string[0];
	textLength = string[1];
	char text[textLength];
	memset(text, 0, textLength);
	strcpy(text, string + 2);

	//Send to first children
	if(jobType == 'O'){
		write(pipe1[1], text, strlen(text));
	}

	//send to second children
	else if(jobType == 'E'){
		write(pipe2[1], text, strlen(text));

	}

	//send both children request to exit/return, since program is done.
	else if(jobType == 'Q'){
		write(pipe1[1], &jobType, sizeof(char));
		write(pipe2[1], &jobType, sizeof(char));
		wait(NULL); //waiting for one child
		wait(NULL); //waiting for the other
		printf("** Both children terminated **\n");
		sleep(1);
	}

	else{
		return -1;
	}

	return 0;
}
/* 
 * Displays the 'Menu' with options the user has
 *
 * INPUT:
 *		None
 *
 * RETURN:
 *		void function, has no return value.
 */
void displayInterface(){
	printf("-------------------------------\n");	
	printf("To get a new job press     'G'  ->   'ENTER'\n");
	printf("To get multiple jobs press 'M'  ->   'ENTER'\n");
	printf("To get all jobs press      'A'  ->   'ENTER'\n");
	printf("To exit the program press  'Q'  ->   'ENTER'\n");
	printf("-------------------------------\n");

}

/* 
 * Reads the message from server using 'recv()'.
 * Checks if the jobtype is 'Q', this means there is no more jobs,
 * and then sends a 'T' message back, and exits.
 *
 * INPUT:
 *		none,void
 *
 * RETURN:
 *		returns 0 on success
 *		returns -1 on failure. If the 'T' message fails to send, 
 * 		or if it fails to send the job to the children.
 */
int readMessage(){
	char msg;
	char buf[260] = {0};

	int ret = recv(sock, buf, sizeof(buf), 0);
	if(ret == -1){
		perror("recv()");
		return -1;
	}

	//if there is no more jobs in the server
	if(buf[0] == 'Q'){
		printf("Closing program, no more to read!!!\n");

		//sending message that tells server client is disconnecting
		msg = 'T';
		if(send(sock, &msg, sizeof(char), 0) == -1){
			perror("send()");
			return -1;
		}

		//sending job to children
		if(sendToChild(buf) == -1){
			printf("Error in file!!\n");
			return -1;
		}
		return 1;
	}


	if(sendToChild(buf) == -1){
		printf("Error in file!!\n");
		return -1;
	}
	return 0;
}

/* 
 * Sends messag to server, eg. requesting for job
 *
 * INPUT:
 *		char msg: the message to be sent.
 *
 * RETURN:
 *		returns 0 on success
 *		returns -1 on failure. If it fails to send message.
 */
int sendMessage(char msg){
	if(send(sock, &msg, sizeof(char), 0) == -1){
		perror("send()");
		return -1;
	}
	return 0;
}

/* 
 * This is the user interface. uses a while(1) loop, and checks for
 * input from the user. Then checks the input is valid, and decides
 * what to do based on the input. calls other functions, suck as: 
 * sendMessage(), displayInterface() and readMessage().
 *
 * INPUT:
 *		none, void
 *
 * RETURN:
 *		returns 0 on success
 *		returns -1 on failure. If there is error with sending message or
 * 		reading message.
 */
int writeMessage(){
	char msg;
	char input[32] = {0};
	while(1){
		usleep(5000);
		displayInterface();

		//Takes input from user, and acts accordingly
		scanf("%s", input);
		msg = input[0];
		if(strlen(input) > 1 || !(msg == 'G' || msg == 'M' || msg == 'A' || msg == 'Q')){
			printf("Not valid input, try again!!\n");
			continue;
		}
		if(msg == 'Q'){
			printf("Client exiting...\n");
			if(sendToChild("Q0") == -1){
				printf("Error in file!!\n");
				return -1;
			}
			if(sendMessage('T') == -1){
				return -1;
			}
			return 0;

		}
		//Sending message to server, asking for one job
		else if(msg == 'G'){
			if(sendMessage(msg) == -1){
				return -1;
			}

			int ret = readMessage(sock);
			if(ret == -1){
				fprintf(stderr, "error in: readMessage()\n");
				return -1;
			}
			else if(ret == 1){
				return 0;
			}

		}

		/* if we ask for more than 1 job, we can specify amount, or get all*/
		else if(msg == 'M' || msg == 'A'){
			int number = 1;
			//asks for number of jobs if we dont want all
			if(msg == 'M'){
				printf("Hvor mange jobber vil du sende?\n");
				
				scanf("%d", &number);
			}

			/* Sening job requests, and reading messages from server. 
			 * Checking the return value of readMessage() in case the
			 * file is empty, and there is no more to read.
			 */
			int i = 0;
			while(i < number){
				if(sendMessage('G') == -1){
					return -1;
				}
				//Reding message from server
				int ret = readMessage(sock);

				if(ret == -1){
					fprintf(stderr, "error in: readMessage()\n");
					return -1;
				}
				else if(ret == 1){
					return 0;
				}
				//incread i if we want a certan number of jobs. Else "while(1)"
				if(msg == 'M'){
					i++;
				}
			}
		}
	}
	close(sock);
	return 0;
}

/* 
 * this funtion takes a hostname as parameter, and extract the correct ip
 * address from it, using the getaddrinfo() function.
 *
 * INPUT:
 *		char* hostname: the name of the host net. 
 *		char* the ip address we want fill "fill"
 *
 * RETURN:
 *		returns 0 on success
 *		returns -1 on failure.
 */
int hostnameToIp(char * hostname , char* ip){
	//creating structs to store the host info.
	struct addrinfo hints;
	struct addrinfo *result, *rp;
	struct sockaddr_in *h;

	memset(&hints, 0, sizeof(hints));
	hints.ai_family = AF_UNSPEC; //allows IPv4 or IPv5
	hints.ai_socktype = SOCK_STREAM;


	if(getaddrinfo(hostname, "http", &hints, &result) != 0){
		perror("getaddrinfo()");
		return -1;
	}
	//loops through all addresses and connects to the first posible.
	for(rp = result; rp != NULL; rp = rp -> ai_next){
		h = (struct sockaddr_in *) rp -> ai_addr;
		strcpy(ip, inet_ntoa(h->sin_addr));
		printf("Found ip: %s\n", inet_ntoa(h->sin_addr));
	}
	freeaddrinfo(result);
	return 0;
}


/* 
 * A signal handler, that handles signal interupt, in this case ctrl+C
 *
 * INPUT:
 *		int signum: the signalnumber
 *
 * RETURN:
 *		void method, does not return anything.
 */
void sighandler(int signum){
	//if in parrent. Send signal to server and children to exit.
	if(getpid() == parrentpid && signum == SIGINT){
		printf("Handeling signal interupt\n");

		sendError();
		printf("Sending error message..\n");
		sleep(1);
		exit(EXIT_SUCCESS);
	}
}
/* 
 * Main method. This function creates the child processes using fork.
 * Also creates pipes used for comunication between the parrent and the
 * two children. this function calls the writeMessage() funciton that is
 * the user interface.
 *
 * INPUT:
 *		int argc: the number of arguments when running the program
 *		char* argv[]: the arguments saved as 'Strings' int array. 
 *
 * RETURN:
 *		returns 0 on success
 *		does not have any other return value. When error, or failure
 *		the program exites, after closing socket and terminating children.
 */
int main(int argc, char* argv[]){
	pid_t child1, child2;
	parrentpid = getpid();
	//ckecking for correct running of program
	if(argc != 3){
		printf("propper use: %s <Hostname> <Port> \n", argv[0]);
		return 0;
	}
	//Signals
	struct sigaction sa;
	memset(&sa, 0, sizeof(sa));
	sa.sa_handler = sighandler;
	if(sigaction(SIGINT, &sa, NULL)){
		perror("sigaction()");
		exit(EXIT_FAILURE);
	}
	//creates pipes
	if(pipe(pipe1) == -1){
		perror("pipe()");
		exit(EXIT_FAILURE); 
	}
	if(pipe(pipe2) == -1){
		perror("pipe()");
		exit(EXIT_FAILURE); 
	}
	child1 = fork();
	if(child1 == 0){	//in child 1
		close(pipe1[1]);
		char buf[260] = {0};
		while(1){
			usleep(1000);
			memset(&buf, 0, sizeof(buf));
			read(pipe1[0], buf, sizeof(buf) - 1);
			if(buf[0] == 'Q'){
				printf("Child 1 terminating\n");
				return 0;
			}
			printf("\n\nMessage printed from child 1:\n------------------------------------------\n");
			fprintf(stdout ,"%s\n\n", buf);

		}
		return 0;
	}
	else{	// in parrent
		child2 = fork();

		if(child2 == 0){	//in child 2
			close(pipe2[1]);
			char buf2[260] = {0};	
			while(1){
				usleep(1000);
				memset(&buf2, 0, sizeof(buf2));
				read(pipe2[0], buf2, sizeof(buf2) - 1);
				if(buf2[0] == 'Q'){
					printf("Child 2 terminating\n");
					return 0;
				}
				printf("\n\nMessage printed from child 2:\n------------------------------------------\n");
				fprintf(stderr ,"%s\n\n", buf2);
			}
			return 0;
		}

		//in parrent where we comunicate with server
		else{ 

			close(pipe1[0]);
			char* port = argv[2];
			char* hostname = argv[1];
			char IP[100];
			//char* ipconst = "127.0.0.1";
			if(hostnameToIp(hostname, IP) == -1){
				fprintf(stderr, "error in: hostnameToIp()\n");
				sendError(-1);
				exit(EXIT_FAILURE);
			}
			
			sock = createSocket(IP, port);


			if(sock == -1){
				fprintf(stderr, "error in: createSocket()\n");
				close(sock);
				exit(EXIT_FAILURE);
			}
			if(writeMessage(sock) == -1){
				printf("Error in writeMessage()\n");
				sendError(sock);
				close(sock);
				exit(EXIT_FAILURE);
			}
			close(sock);
			return 0;
		}
	}
	return 0;

}