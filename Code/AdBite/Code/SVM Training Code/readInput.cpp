/*
 * readInput.cpp
 *
 *  Created on: Jan 14, 2012
 *
 *      Author: Amey
 *
 *This file was created to make binary data file which is going
 *to be used as input to the input and output matrices.
 *
*/



#ifndef readInput_H
#define readInput_H
#include<iostream>
#include<fstream>
#include "SVM_constants.h"
using namespace std;
#ifndef row_H
#define row_H
typedef struct
{
        double input[attributes];
        double output;
}row;

#endif
class ReadInput
{
public:
static double convertToDouble(char *s)
    {
    return atof(s);
    }




void makeDatasetBinaryFile()
{

    freopen("output.txt", "w", stdout);
    row temp;
    int i=0, no_of_times=0;
    char rowtemp[3350];
    char *tokens=NULL;
    int ctokens=0;
    ifstream infile;
    //int cfirstthree;
    infile.open("H:\\ad.data", ios::in);
    if(infile.fail())
    {
    cout<<"ad.data failed to open";
    exit(1);
    }
    ofstream outfile;
    outfile.open("dataset2.dat", ios::out | ios::binary);//binary dataset to be created
    if(outfile.fail())
    {
    cout<<"dataset.dat could n`t be created";
    exit(1);
    }

    //1. scanning through the dataset
    //2. getting the whole line => making the row to be inserted in the binary file
    //3. inserting the row in the binary file
    while((infile.eof()==0))
    {
    no_of_times++;
    infile.getline(rowtemp, 3350);
    //cfirstthree=0;
    ctokens=0;
    i=0;
    tokens=strtok(rowtemp, ", ");
    while(tokens!= NULL)
    {
    ctokens++;
    if(strcmp(tokens,"ad.")==0)
		{
    	temp.output=1;
		}

    else
    	if(strcmp(tokens, "nonad.")==0)
    	{
   		temp.output=-1;
    	}
    else
    	if(strcmp(tokens, "?")==0)
    		temp.input[i]=0.0000000011;
    	else if(strcmp(tokens, "0")==0)
    		temp.input[i]=-1;
    	     else if(strcmp(tokens, "1")==0)
    	    	 temp.input[i]=1;
				 else temp.input[i]=convertToDouble(tokens);

    tokens=strtok(NULL, ", ");
    i++;
    }
    for(int j=0; j<attributes; j++)
    	cout<<" "<<temp.input[j];
    cout<<endl;
    outfile.write( (char*)&temp,sizeof(row));
    //cout<<"der"<<temp.output<<endl;




    }
cout<<"MIL GAYA BOSS!!";


outfile.close();
infile.close();
}

};

#endif

