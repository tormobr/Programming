#include <stdio.h>
#include <stdlib.h>
#include <wchar.h>

#define FALSE 0
#define TRUE  1

typedef unsigned char byte;
typedef unsigned long unicode;
	
extern int  readbyte (FILE *f);
extern long readutf8char (FILE *f);
extern void writebyte (FILE *f, byte b);

/*void writebyte (FILE*f, byte b){
fwrite(&b, 1, 1, f);
}*/

extern void writeutf8char (FILE *f, unicode u);

void error (char *message)
{
  printf("\nERROR: %s\n", message);
  exit(1);
}

void dump_byte_seq (byte b[], int n_b)
{
  int i;

  printf("%d bytes {", n_b);
  for (i = 0;  i < n_b;  i++) {
    if (i > 0) printf(", ");
    printf("0x%02x", b[i]);
  }
  printf("}");
}

void dump_unicode_seq (unicode u[], int n_u)
{
  int i;

  printf("%d chars {", n_u);
  for (i = 0;  i < n_u;  i++) {
    if (i > 0) printf(", ");
    printf("0x%lx", u[i]);
  }
  printf("}");
}

void compare_byte_seqs (byte a[], int n_a, byte b[], int n_b)
{
  int ok = TRUE;

  if (n_a != n_b) {
    ok = FALSE;
  } else {
    int i;
    for (i = 0;  i < n_a;  i++) 
      if (a[i] != b[i]) ok = FALSE;
  }

  if (ok) {
    printf("OK\n");
  } else {
    printf("\n  Error: Result is ");  dump_byte_seq(a, n_a);
    printf("\n  but should be ");  dump_byte_seq(b, n_b);  printf("\n");
  }
}

void compare_unicode_seqs (unicode a[], int n_a, unicode b[], int n_b)
{
  int ok = TRUE;

  if (n_a != n_b) {
    ok = FALSE;
  } else {
    int i;
    for (i = 0;  i < n_a;  i++) 
      if (a[i] != b[i]) ok = FALSE;
  }

  if (ok) {
    printf("OK\n");
  } else {
    printf("\n  Error: Result is ");  dump_unicode_seq(a, n_a);
    printf("\n  but should be ");  dump_unicode_seq(b, n_b);  printf("\n");
  }
}

int read_test_byte (FILE *f)
{
  int status;
  byte c;

  status = fread(&c, 1, 1, f);
  if (status <= 0) return -1;
  return (int)c;
}

void test_byte_file (char *f_name, byte data[], int n_data)
{
  byte file_bytes[200];
  int n_file_bytes;
  FILE *f = fopen(f_name, "rb");
  if (f == NULL) error("Could not open file!");

  for (n_file_bytes = 0;  n_file_bytes < 200;  n_file_bytes++) {
    int b = read_test_byte(f);
    if (b < 0) break;
    file_bytes[n_file_bytes] = b;
  }
  fclose(f);

  compare_byte_seqs(file_bytes, n_file_bytes, data, n_data);
}

void create_byte_file (char *f_name, byte b_seq[], int n_b_seq)
{
  FILE *f = fopen(f_name, "wb");
  if (f == NULL) error("Could not create file!");

  fwrite(b_seq, n_b_seq, 1, f);
  fclose(f);
}


/* Test #1 */
byte b_seq_1[] = { 4, 0, 255, 17, 200 };

void test_1 (void)
{
  int n_bytes = sizeof(b_seq_1)/sizeof(b_seq_1[0]);
  int i;
  FILE *f = fopen("test1.txt", "wb");
  if (f == NULL) error("Could not create test1.txt!");

  for (i = 0;  i < n_bytes;  i++)
    writebyte(f, b_seq_1[i]);
  fclose(f);

  test_byte_file("test1.txt", b_seq_1, n_bytes);
}

void test_5 (void)
{
  byte data[200];
  int n_data = 0;
  int n_b_seq_1 = sizeof(b_seq_1)/sizeof(b_seq_1[0]);
  FILE *f;

  create_byte_file ("test5.txt", b_seq_1, n_b_seq_1);
  f = fopen("test5.txt", "rb");
  if (f == NULL) error("Could not read test5.txt!");
  while (n_data < 200) {
    int b = readbyte(f);
    if (b < 0) break;
    data[n_data++] = (byte)b;
  }
  fclose(f);

  compare_byte_seqs(data, n_data, b_seq_1, n_b_seq_1);
}

unicode u_seq_2[] = { 0x24, 0x20, 0x41, 0x3d, 0x32, 0x78 };  /* "$ A=2x" */
byte    b_seq_2[] = { '$', ' ', 'A', '=', '2', 'x' };

void test_2 (void)
{
  int n_u = sizeof(u_seq_2)/sizeof(u_seq_2[0]);
  int n_b = sizeof(b_seq_2)/sizeof(b_seq_2[0]);
  int i;
  FILE *f = fopen("test2.txt", "wb");
  if (f == NULL) error("Could not create test2.txt!");

  for (i = 0;  i < n_u;  i++) 
    writeutf8char(f, u_seq_2[i]);
  fclose(f);

  test_byte_file("test2.txt", b_seq_2, n_b);
}

unicode u_seq_3[] = { 0x35, 0xa2, 0x20, 0x429, 0x3c9 };  /* "5Â¢ Ð©Ï‰" */
byte    b_seq_3[] = { '5', 0xc2, 0xa2, ' ', 0xd0, 0xa9, 0xcf, 0x89 };

void test_3 (void)
{
  int n_u = sizeof(u_seq_3)/sizeof(u_seq_3[0]);
  int n_b = sizeof(b_seq_3)/sizeof(b_seq_3[0]);
  int i;
  FILE *f = fopen("test3.txt", "wb");
  if (f == NULL) error("Could not create test3.txt!");

  for (i = 0;  i < n_u;  i++) 
    writeutf8char(f, u_seq_3[i]);
  fclose(f);

  test_byte_file("test3.txt", b_seq_3, n_b);
}
unicode u_seq_4[] = { 0x20ac, 0x3d, 0x10348, 0x2658 };  /* "â‚¬=ðˆâ™˜" */
byte    b_seq_4[] = { 0xe2, 0x82, 0xac, '=', 0xf0, 0x90, 0x8d, 0x88,
                      0xe2, 0x99, 0x98};
void test_4 (void)
{
  int n_u = sizeof(u_seq_4)/sizeof(u_seq_4[0]);
  int n_b = sizeof(b_seq_4)/sizeof(b_seq_4[0]);
  int i;
  FILE *f = fopen("test4.txt", "wb");
  if (f == NULL) error("Could not create test4.txt!");

  for (i = 0;  i < n_u;  i++) 
    writeutf8char(f, u_seq_4[i]);
  fclose(f);

  test_byte_file("test4.txt", b_seq_4, n_b);
}

/* Test #6 */
void test_6 (void)
{
  unicode data[200];
  int n_data = 0;
  int n_b_seq_2 = sizeof(b_seq_2)/sizeof(b_seq_2[0]);
  int n_u_seq_2 = sizeof(u_seq_2)/sizeof(u_seq_2[0]);
  FILE *f;

  create_byte_file ("test6.txt", b_seq_2, n_b_seq_2);
  f = fopen("test6.txt", "rb");
  if (f == NULL) error("Could not read test6.txt!");
  while (n_data < 200) {
    long u = readutf8char(f);
    if (u < 0) break;
    data[n_data++] = (unicode)u;
  }
  fclose(f);

  compare_unicode_seqs(data, n_data, u_seq_2, n_u_seq_2);
}


/* Test #7 */
void test_7 (void)
{
  unicode data[200];
  int n_data = 0;
  int n_b_seq_3 = sizeof(b_seq_3)/sizeof(b_seq_3[0]);
  int n_u_seq_3 = sizeof(u_seq_3)/sizeof(u_seq_3[0]);
  FILE *f;

  create_byte_file ("test7.txt", b_seq_3, n_b_seq_3);
  f = fopen("test7.txt", "rb");
  if (f == NULL) error("Could not read test7.txt!");
  while (n_data < 200) {
    long u = readutf8char(f);
    if (u < 0) break;
    data[n_data++] = (unicode)u;
  }
  fclose(f);

  compare_unicode_seqs(data, n_data, u_seq_3, n_u_seq_3);
}


/* Test #8 */
void test_8 (void)
{
  unicode data[200];
  int n_data = 0;
  int n_b_seq_4 = sizeof(b_seq_4)/sizeof(b_seq_4[0]);
  int n_u_seq_4 = sizeof(u_seq_4)/sizeof(u_seq_4[0]);
  FILE *f;

  create_byte_file ("test8.txt", b_seq_4, n_b_seq_4);
  f = fopen("test8.txt", "rb");
  if (f == NULL) error("Could not read test8.txt!");
  while (n_data < 200) {
    long u = readutf8char(f);
    if (u < 0) break;
    data[n_data++] = (unicode)u;
  }
  fclose(f);

  compare_unicode_seqs(data, n_data, u_seq_4, n_u_seq_4);
}


int main (void)
{
  printf("Test 1 (write a byte):         ");  test_1();
  printf("Test 5 (read a byte):          ");  test_5();
  printf("Test 2 (write 1-byte utf-8):   ");  test_2();
  printf("Test 3 (write 2-byte utf-8):   ");  test_3();
  printf("Test 4 (write 3+4-byte utf-8): ");  test_4();
  printf("Test 6 (read 1-byte utf-8):    ");  test_6();
  printf("Test 7 (read 2-byte utf-8):    ");  test_7();
  printf("Test 8 (read 3+4-byte utf-8):  ");  test_8();
  return 0;
}