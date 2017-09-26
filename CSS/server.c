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

/*
 * Sends a message/job to the client containing jobtype 'Q' and textlength
 * 0, to tell that there is no more jobs to read. Then waits for confirmation
 * that the client is disconnecting before finnishing it self
 *
 * INPUT:
 *		int client_socket: The socket comunicating with the client trough TCP.
 *
 * RETURN:
 *		returns 0 on success and exits program on failure.
 *
 */
int sendDoneSignal(int client_socket){
	char doneMessage[3] = {0};
	doneMessage[0] = 'Q';
	doneMessage[1] = (char)0;
	if(send(client_socket, doneMessage, sizeof(doneMessage), 0) == -1){
		perror("send()");
		close(client_socket);
		exit(EXIT_FAILURE);
	}
	return 0;
}


/*
 * This function reads jobs from the .job file, and sends them to the client
 * If there is no more jobs, it tells the client through "sendDoneSignal()"
 *
 * INPUT:
 *		int client_socket: The socket comunicating with the client trough TCP.
 *
 * RETURN:
 *		returns 0 on success
 *		returns -1 on failure. if it fails to send message. 
 *		returns 1 on succsess, but no more jobs exist in the file
 *
 */
FILE* inFile;
char jobbType;
unsigned char textLength;
int doneReading = 0;
int sendJob(int client_socket){
	//checks if the file is empty, and reads info from it.
	if(fread(&jobbType, sizeof(jobbType), 1, inFile) == 0){
		fprintf(stderr, "File is empty!!!\n");
		fclose(inFile);
		if(sendDoneSignal(client_socket) != 0){
			fprintf(stderr, "Error with \"sendDoneSignal()\"\n");
		}
		doneReading = 1;
		return 1;
	}

	//Checking for error in job file
	fread(&textLength, sizeof(textLength), 1, inFile);
	if(jobbType != 'Q' && (int)textLength == 0){
		fprintf(stderr, "Text length of 0 in file, exiting...\n");

		fclose(inFile);
		if(sendDoneSignal(client_socket) != 0){
			fprintf(stderr, "Error with \"sendDoneSignal()\"\n");
		}
		return -1;
	}

	char text[textLength + 1];
	if(fread(&text, textLength, 1, inFile) == 0){
		fprintf(stderr, "Length desciption dont match text length\n");
		fclose(inFile);
		if(sendDoneSignal(client_socket) != 0){
			fprintf(stderr, "Error with \"sendDoneSignal()\"\n");
		}
		return -1;	
	}
	text[textLength] = '\0';


	char combined[textLength + 3];
	combined[0] = jobbType;
	combined[1] = textLength;
	strcpy(combined + 2, text);


	printf("--------------------------------------\n");
	printf("JobType:       %c\n", jobbType);
	printf("String lenght: %d\n", (int)textLength);
	printf("%s\n", text);	
	printf("--------------------------------------\n");

	//making sure all the content is being sent, not just parts of it.
	size_t sent = 0;
	while(sent != strlen(combined)){
		int ret = send(client_socket, combined + sent, strlen(combined + sent), 0);
		if(ret <= 0){
			perror("send()");
			return -1;
		}
		else{
			sent += ret;
		}
	}
	return 0;

}

/*
 * Reads messages from the client, and executes tasks, 
 * depending on the message type.
 *
 * INPUT:
 *		int client_socket: The socket comunicating with the client trough TCP.
 *
 * RETURN:
 *		returns 0 on success
 *		returns -1 on failure. if it fails to receve, or send. 
 *
 */
int readMessage(int client_socket){
	char msg;
	while(1){
		usleep(30000);
		ssize_t ret = recv(client_socket, &msg, sizeof(char), 0);
		if(ret == -1){
			perror("recv()");
			return -1;
		}

		if(msg == 'G'){
			printf("\nSENDING JOB......\n");
			if(sendJob(client_socket) == -1){
				fprintf(stderr, "error in: sendJob()\n");
				return -1;
			}
				
		}

		else if(msg == 'T'){
			printf("Client exiting normaly...\n");
			sleep(1);
			printf("\nServer exiting...\n");
			sleep(1);
			close(client_socket);
			if(doneReading == 0){
				fclose(inFile);
			}
			
			return 0;
		}	
		else if(msg == 'E'){
			printf("\nClient terminates due to error\n");
			sleep(1);
			close(client_socket);
			fclose(inFile);
			return 0;
		}
		else{
			printf("Not correct input from user: Continue...\n");
			continue;
		}		
	}
	return 0;

}

/* 
 * this funtion takes a hostname as parameter, and extract the correct ip
 * address from it, using the getaddrinfo() function.
 * Not being used in this program, since we use INADDR_ANY
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
		printf("Found ip: %s\n", ip);
	}
	freeaddrinfo(result);
	return 0;
}

/*
 * This function creates the socket fds. creates a struct
 * with the ip and port.
 * We listen for client connections, and try to accept them. also
 * creates a new socket(client_socket) wich we use to comunicate
 * with one client.
 *
 * INPUT:
 *		char* port: The port number
 *
 *
 * RETURN:
 *		returns 0 on success
 *		returns -1 on failure. eg. wrong with ip-address,
 *		problems with binding, listening or accepting connections.
 *
 */
int createSocket(char* port){
	int portNumber = atoi(port);
	int sock;

	sock = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	if(sock == -1){
		perror("socket()");
		return -1;
	}

	//Creates soccketaddr_in struck, and sets the variables correct.
	struct sockaddr_in server_address;
	memset(&server_address, 0, sizeof(struct sockaddr_in));
	server_address.sin_family = AF_INET;
	server_address.sin_addr.s_addr = INADDR_ANY;
	server_address.sin_port = htons(portNumber);

	//dont need to do this when using INADDR_ANY
/*	//converting ip-string from input to binary
	if (inet_pton(AF_INET, IP, &server_address.sin_addr.s_addr) != 1){
		perror("inet_pton()");
		close(sock);
		return -1;
	}*/
	int yes = 1;
	if (setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, &yes, sizeof(int))) {
		perror("setsockopt()");
		close(sock);
		return -1;
	}
	//binds a "name" to a socket
	if(bind(sock, (struct sockaddr *) &server_address, sizeof(server_address)) != 0){
		perror("bind()");
		close(sock);
		return -1;
	}
	printf("bound done right \n");

	//listining for potentian connections
	if(listen(sock, SOMAXCONN) != 0){
		perror("listen()");
		close(sock);
		return -1;	
	}

	//accepting client, and creating a client socket
	struct sockaddr_in client_address;
	memset(&client_address, 0, sizeof(client_address));
	socklen_t address_Length = sizeof(client_address);

	int client_socket = accept(sock, (struct sockaddr *)&client_address, &address_Length);
	if(client_socket == -1){
		perror("accept()");
		close(sock);
		return -1;
	}

	//prints info about the new connection
	printf("Client connected!! yee\n");
	char* clientIP = inet_ntoa(client_address.sin_addr);
	printf("user:  IP:%s\n", clientIP);
	close(sock);
	if(readMessage(client_socket) == -1){
		fprintf(stderr, "error in: readMessage()\n");
		close(client_socket);
		return -1;
	}

	return 0;
}
/*
 * Main function. check for propper run of program, call on different other
 * functions, and create som variables we need.
 *
 * INPUT:
 *		int argc: the number of arguments when running program.
 *		char* argv[]: all the arguments saved in "string" array.
 *
 *
 * RETURN:
 *		returns 0 on success
 *		exits program on failure.
 */
int main(int argc, char* argv[]){

	//ckecking for correct running of program
	if(argc != 3){
		printf("propper use: %s <Filename> <Port> \n", argv[0]);
		return 0;
	}

	//opening job file
	inFile = fopen(argv[1], "r");
	if(inFile == NULL){
		perror("fopen()");
		exit(EXIT_FAILURE);
	}
	int sockret;
	//char IP[100];
/*	if(hostnameToIp(hostname, IP) == -1){
		perror("error in: hostnameToIp()\n");
		exit(EXIT_FAILURE);
	}*/


	char* port = argv[2];
	sockret = createSocket(port);

	if(sockret == -1){
		fprintf(stderr, "Error in createSocket()\n");
		exit(EXIT_FAILURE);
	}
	return 0;
}
