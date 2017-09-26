# include <stdio.h>
# include <string.h>
# include <stdlib.h>

//------------------Oppgave 2b----------------------
int stringsum(char* s){
	unsigned int sum = 0;
	unsigned int value;

	//kjorer foorloop som gaar gjennom char for char i strengen
	//finner verdien til char i ascii tabell
	//trekker fra 64 eller 96 avhengig av stor eller liten bokstav
	//Legger til i sum variablen som returneres til slutt
	unsigned int i;
	for(i = 0; i < strlen(s); i++){
		value = s[i];
		if(value > 64 && value < 91){
			sum += value - 64;
		}
		else if(value > 96 && value < 123){
			sum += value - 96;
		}
		else{
			return -1;
		}

	}
	return sum;
}

//------------------Oppgave 2c----------------------
int distance_between(char* s, char c){
	unsigned int index1;
	unsigned int hit = 0;

	unsigned int i;
	for(i = 0; i < strlen(s); i++){
		if(s[i] == c && hit == 0){
			index1 = i;
			hit = 1;
		}
		else if(s[i] == c && hit == 1){
			return i - index1;
		}

	}
	return -1;
}

//------------------Oppgave 2d----------------------
char* string_between(char* s, char c){
	unsigned int lenbtwn = distance_between(s, c);
	unsigned int hit = 0;

	char* string;
	string = malloc(lenbtwn);

	unsigned int i;
	unsigned int counter = 0;
	for (i = 0; i < strlen(s); i++){
		if(s[i] == c && hit == 0){
			hit = 1;
		}
		else if(s[i] != c && hit == 1){
			string[counter] = s[i];
			counter ++;
		}
		else if(s[i] == c && hit == 1){
			return string;
		}	
	}
	return NULL;
}

//------------------Oppgave 2e----------------------


char** split(char* s){

	char* tmpString;
	char* search = " ";
	char** newArray;
	char str[strlen(s)];
	strcpy(str,s);

	//teller antall ord som er i strengen, antar at det er minst ett.
	unsigned int number_of_words = 1;
	unsigned int i;
	unsigned int stringlength = strlen(s);
	for (i = 0; i < stringlength; i++){
		if(s[i] == ' '){
			number_of_words++;
		}
	}

	newArray = malloc((number_of_words + 1) * sizeof(char*));
	
	//Deler opp strengen ord for ord og legger inn i ny array.
	tmpString = strtok(str, search);
	unsigned int j = 0;
	while(tmpString != NULL){
		newArray[j] = malloc((strlen(tmpString) + 1) * sizeof(char));
		
		strcpy(newArray[j], tmpString);
		tmpString = strtok(NULL, search);
		j += 1;
	}
	free(tmpString);
	newArray[number_of_words] = NULL;
	return newArray;
}



//------------------Oppgave 2g----------------------
void stringsum2(char* s, int* res){
	*res = stringsum(s);
}


//------------------MAIN----------------------
/*int main(){

	printf("\n-----------String sum------------\n");
	printf("%d\n", stringsum("abc"));
	printf("Feil bruk: %d\n", stringsum("hei!"));

	printf("\n-----------dist between------------\n");
	printf("%d\n", distance_between("a1234a", 'a'));
	printf("Feil bruk: %d\n", distance_between("a1234", 'a'));

	printf("\n-----------String between------------\n");
	printf("%s\n", string_between("a1234a", 'a'));
	printf("Feil bruk: %s\n", string_between("a1234", 'a'));

	printf("\n-----------split string------------\n");
	char** test = split("hallo hvordan gaar det med deg?");

	for(int i = 0; i < 6; i++){
		printf("%s\n", test[i]);
	}

	printf("\n-----------string sum2------------\n");
	int res;
	stringsum2("abcd", &res);
	printf("%d\n", res); 
	stringsum2("hei!", &res);
	printf("Feil bruk: %d\n", res); 

	//-------------------TESTING------------------

	return 0;
}*/
