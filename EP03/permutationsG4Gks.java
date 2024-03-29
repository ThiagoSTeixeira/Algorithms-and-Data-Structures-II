// Program to print all permutations of a string in sorted order.
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* Following function is needed for library function qsort(). Refer
http://www.cplusplus.com/reference/clibrary/cstdlib/qsort/ */
int compare (const void *a, const void * b)
{ return ( *(char *)a - *(char *)b ); }

// A utility function two swap two characters a and b
void swap (char* a, char* b)
{
	char t = *a;
	*a = *b;
	*b = t;
}
void bubble(char str[], int l, int h)
{
    int j;

        for(j=l;j<h;j++)
        {
            if(str[j]<str[j+1])
            swap(&str[j],&str[j+1]);
        }
}

void reverse(char str[], int l, int h)
{
    //the smaller number which is taken to the rigth side  by swaping in previos method might come at wrong position(cosidering decresing sorting order)
    // so settle that number downwhere it finds its place
    bubble(str,l,h);
    
    //. Now Array will be strictly non-incresing from l to h. so just reverse it.
   while (l < h)
   {
       swap(&str[l], &str[h]);
       l++;
       h--;
   }
}
 
// This function finds the index of the smallest character
// which is greater than 'first' and is present in str[l..h]
int findCeil (char str[], char first, int l, int h)
{
	// initialize index of ceiling element
	int ceilIndex = l;

	// Now iterate through rest of the elements and find
	// the smallest character greater than 'first'
	for (int i = l+1; i <= h; i++)
	if (str[i] > first && str[i] < str[ceilIndex])
			ceilIndex = i;

	return ceilIndex;
}

// Print all permutations of str in sorted order
void sortedPermutations ( char str[] )
{
    static int p=0;
    // Get size of string
    int size = strlen(str);
 
    // Sort the string in increasing order
    qsort( str, size, sizeof( str[0] ), compare );
 
    // Print permutations one by one
    bool isFinished = false;
    while ( ! isFinished )
    {
        // print this permutation
        printf ("%d -->%s \n",++p, str);
 
        // Find the rightmost character which is smaller than its next
        // character. Let us call it 'first char'
        int i;
        for ( i = size - 2; i >= 0; --i )
           if (str[i] < str[i+1])
              break;
 
        // If there is no such chracter, all are sorted in decreasing order,
        // means we just printed the last permutation and we are done.
        if ( i == -1 )
            isFinished = true;
        else
        {
            // Find the ceil of 'first char' in right of first character.
            // Ceil of a character is the smallest character greater than it
            int ceilIndex = findCeil( str, str[i], i + 1, size - 1 );
 
            // Swap first and second characters
            swap( &str[i], &str[ceilIndex] );
 
            // reverse the string on right of 'first char'
            reverse( str, i + 1, size - 1 );
        }
    }
}

// Driver program to test above function
int main()
{
	char str[] = "abcba";
	sortedPermutations( str );
	return 0;
}
