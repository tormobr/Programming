# include <stdio.h>
# include <string.h>
# include <stdlib.h>



//------------------Oppgave3a---------------------

struct datetime{
	int hour;
	int minut;
	int second;

	int year;
	int month;
	int day;
};

//------------------Oppgave3b---------------------

void init_datetime(struct datetime* dt, int h, int m, int s, int y, int mo, int d){
	dt->hour = h;
	dt->minut = m;
	dt->second = s;
	dt->year = y;
	dt->month = mo;
	dt->day = d;
}
//------------------Oppgave3c---------------------

void datetime_set_date(struct datetime* dt, int y, int mo, int d){
	dt->year = y;
	dt->month = mo;
	dt->day = d;
}

void datetime_set_time(struct datetime* dt, int h, int m, int s){
	dt->hour = h;
	dt->minut = m;
	dt->second = s;
}

//------------------Oppgave3d---------------------

void datetime_diff(struct datetime* dt_from, struct datetime* dt_to, struct datetime* res){
	int yeardiff = (dt_to->year) - (dt_from->year);
	int monthdiff = (dt_to->month) - (dt_from->month);
	int daydiff = (dt_to->day) - (dt_from->day);
	
	int hourdiff = (dt_to->hour) - (dt_from->hour);
	int minutdiff = (dt_to->minut) - (dt_from->minut);
	int seconddiff = (dt_to->second) - (dt_from->second);

	if(seconddiff < 0){
		minutdiff --;
		seconddiff = 60 + seconddiff;
	}
	if(minutdiff < 0){
		hourdiff --;
		minutdiff = 60 + minutdiff;
	}
	if(hourdiff < 0){
		daydiff --;
		hourdiff = 24 + hourdiff;
	}
	if(daydiff < 0){
		monthdiff --;
		daydiff = 30 + daydiff;
	}
	if(monthdiff < 0){
		yeardiff --;
		monthdiff = 12 + monthdiff;
	}

	init_datetime(res, hourdiff, minutdiff, seconddiff, yeardiff, monthdiff, daydiff);
}


int main(void){
	
	struct datetime date1;
	struct datetime date2;
	struct datetime res;

	init_datetime(&date1, 12, 11, 10, 2, 2, 3);
	init_datetime(&date2, 11, 10, 9, 2, 3, 2);

	datetime_diff(&date1, &date2, &res);
	printf("year: %d month: %d day: %d\n", res.year, res.month, res.day);

	//datetime_set_date(&date2, 3, 4, 1);

	datetime_diff(&date1, &date2, &res);
	printf("year: %d month: %d day: %d hour: %d minut: %d second: %d\n", res.year, res.month, res.day, res.hour, res.minut, res.second);
	return 0;
}