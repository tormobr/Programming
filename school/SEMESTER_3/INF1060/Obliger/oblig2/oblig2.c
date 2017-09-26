#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define RSIZE 256

struct router* routers[RSIZE];
int numRouters;
struct router{
	unsigned char routerID;
	unsigned char flagg;
	unsigned char namelength;
	char name[];
};

//print the info on a router
void print_info(struct router* r){

	printf("ID:           %u\n", r -> routerID);
	printf("Flagg:        %x\n", r -> flagg);
	printf("Name-length:  %u\n", r -> namelength);
	printf("Name:         %s\n", r -> name);
}

//changes the flag propertie of a router
void changeFlagg(struct router* r, int bit){
	unsigned char flagg = r -> flagg;

	unsigned char changeNumber = flagg >> 4;
	printf("%d\n", changeNumber);
	//if only The change number need to be updatet, not the info
	if(changeNumber == 15){
		printf("No further changes are posible\n");
		return;
	}
	if(bit == 5){
		changeNumber++; //updats the change number
		printf("asdfasdfasdkjfhasdfasdfASFASDFASDFASDFASDF\n");
	}
	else{
		flagg ^= (1 << bit); //moving the 1 bit to the update location
		changeNumber++; // updatesthe changes made
	}
	changeNumber = changeNumber << 4; // moves the changnumber to the "left side of byte"

	unsigned char tmp = flagg & 0xF; // 0xF = 0000 1111, sets the "right side" of byte to zeros
	flagg = tmp | changeNumber;
	r -> flagg = flagg;


}

//updates info on router
void updateInfo(struct router* r){
	printf("\nTo change the model, type:\t model  ->  'ENTER'\n");
	printf("To change the flagg, type:\t flagg  ->  'ENTER'\n");

	char* input;
	input = malloc(10);
	scanf("%s", input);

	if(strcmp("model", input) == 0){
		printf("Type new name:\n");
		char* newName;
		fgets (newName, 20, stdin);
		scanf ("%[^\n]%*c", newName);
		strcpy(r -> name, newName);
		printf("\nNew router info:\n\n");
		print_info(r);
		changeFlagg(r, 5);
		return;
	}
	else if(strcmp("flagg", input) == 0){
		printf("\nTo change propertie 1, type: 1");
		printf("\nTo change propertie 2, type: 2");
		printf("\nTo change propertie 3, type: 3\n");

		int bit;
		scanf(" %d", &bit);
		changeFlagg(r, bit);
	}
	free(input);
}

//adding a router to the map
void addRouter(){
	struct router* tmp = malloc(sizeof(struct router));

	//ID
	int inputID;
	unsigned char id;
	printf("Type id(decimal): \n");
	scanf(" %d", &inputID);
	id = inputID;
	printf("%u\n", id);

	//Flagg
	int inputFlagg;
	unsigned char flagg;
	printf("Type flagg(decimal): \n");
	scanf(" %d", &inputFlagg);
	flagg = inputFlagg;
	printf("%x\n", flagg);

	//Lenght
	int inputlength;
	unsigned char length;
	printf("Type length of model/name(decimal): \n");
	scanf(" %d", &inputlength);
	length = inputlength;
	printf("%u\n", length);

	//name
	char* name;
	name = malloc(length);
	printf("Type model/name(string): \n");
	scanf(" %s", name);
	printf("%s\n", name);

	//setting struct vars
	tmp -> routerID = id;
	tmp -> flagg = flagg;
	tmp -> namelength = length;
	strcpy(tmp -> name, name);

	//adding to array, and printing new info
	printf("New router created:\n\n");
	print_info(tmp);
	routers[tmp -> routerID] = tmp;
	numRouters++;

}

//deleting a router from the map
void deleteRouter(int index){

	free(routers[index]);
	routers[index] = NULL;

	numRouters--;
}

//Writing to file, when program ends
void writeToFile(){

	FILE* outFile = fopen("out.dat", "w");
	int i;
	fwrite(&numRouters, sizeof(numRouters), 1, outFile);

	char nextLine;

	fwrite(&nextLine, sizeof(nextLine), 1, outFile);
	for(i = 0; i < RSIZE; i++){
		if(routers[i] == NULL){
			continue;
		}

		fwrite(&routers[i] -> routerID, sizeof(char), 1, outFile);
		fwrite(&routers[i] -> flagg, sizeof(char), 1, outFile);
		fwrite(&routers[i] -> namelength, sizeof(char), 1, outFile);
		fwrite(&routers[i] -> name, routers[i] -> namelength, 1, outFile);

	}
	fclose(outFile);
}


//-------------------------------------MAIN----------------------------------------

int main(int argc, char *argv[]){
	printf("\n\n");
	int a = 5;
	char c = a;
	printf("%c\n", c);



	if(argc < 2){
		printf("file missing\n");
		
	}

	FILE* inFile = fopen(argv[1], "r");
	if(inFile == NULL){
		exit(EXIT_FAILURE);
	}


	//number of routers
 	fread(&numRouters, sizeof(numRouters), 1, inFile);
 	printf("Number of routers:   %d\n", numRouters);

 	//removing blank line
 	char nextLine;
 	fread(&nextLine, sizeof(nextLine), 1, inFile);
 	printf("nextLine: %c\n", nextLine);

 	//struct variables
 	unsigned char id;
 	unsigned char flagg;
 	unsigned char length;

 	//looping through file and creating structs from the data given
 	int i;
 	for(i = 0; i < numRouters; i++){
 		struct router* r = malloc(sizeof(struct router));

 		//ID
	 	fread(&id, sizeof(id), 1, inFile);
	 	r -> routerID = id;
		printf("id:  %u\n", r -> routerID);

		//flagg
		fread(&flagg, sizeof(flagg), 1, inFile);
		r -> flagg = flagg;
		printf("Flagg:  %x\n", r -> flagg);

		//length og model/name
		fread(&length, sizeof(length), 1, inFile);
		r -> namelength = length;
		printf("name length: %u\n", r -> namelength);

		//model/name
		fread(&r -> name, length, 1, inFile);
		r -> name[length - 1] = '\0';
		printf("%s\n", r -> name);

		routers[i] = r;
		printf("\n");


 	}
 	fclose(inFile);

 	printf("-----------------------------------\n");

 	for (i = 0; i < numRouters; i++){
 		printf("%u\n", routers[i] -> routerID);
 	}


 	//Userinterface
 	int run = 1;
 	while(run == 1){

	 	printf("To print info about one router, type: \t info   -> 'ENTER'\n");
	 	printf("To change info on a router, type:     \t mod    -> 'ENTER'\n");
	 	printf("To add a router, type:                \t add    -> 'ENTER'\n");
	 	printf("To delete a router, type:             \t delete -> 'ENTER'\n");
	 	printf("To quit the program, type:            \t quit   -> 'ENTER'\n\n");

	 	char* input;
	 	input = malloc(10);
	 	scanf("%s", input);

	 	//info on one router
	 	if(strcmp("info", input) == 0){
	 		printf("Type the router ID  ->  'ENTER'\n");
	 		int input;
	 		scanf("%d", &input);
	 		if(routers[input] == NULL){
	 			printf("No routers with this ID found...\n\n");
	 			continue;
	 		}
	 		printf("\nInfo about router with ID = %d: \n\n", input);
	 		print_info(routers[input]);


	 	}

	 	//change info on router
	 	else if(strcmp("mod", input) == 0){

	 		printf("Type ID for router you want to change: \n");
	 		int inputID;
	 		scanf("%d", &inputID);
	 		if(routers[inputID] == NULL){
	 			printf("No routers with this ID found...\n\n");
	 			continue;
	 		}
	 		updateInfo(routers[inputID]);

	 	}

	 	//adding a router
	 	else if(strcmp("add", input) == 0){
	 		addRouter();
	 	}
	 	else if(strcmp("delete", input) == 0){
	 		printf("Type ID for router you want to delete: \n");
	 		int inputID;
	 		scanf("%d", &inputID);
	 		if(routers[inputID] == NULL){
	 			printf("No routers with this ID found...\n\n");
	 			continue;
	 		}
	 		deleteRouter(inputID);
	 	}

	 	//ending the program and saving to file
	 	else if(strcmp("quit", input) == 0){
	 		run = 0;
	 		writeToFile();
	 	}
	 	free(input);
	 	printf("------------------------------------------------\n");
 	}


	return 0;
}