#include<stdio.h>

int main(int argc, char* argv[]){

	//Sjekker om det finnes mer enn ett argument og skriver ut alle i en loop 
	if(argc >= 2){
		int i;
		for(i = 0; i < argc; i++){
			printf("%s\n", argv[i]);
		}
	}

	return 0;
}

