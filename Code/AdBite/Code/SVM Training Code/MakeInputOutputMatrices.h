/*
 * MakeInputOutputMatricesTemp.h
 *
 *  Created on: Jan 19, 2012
 *      Author: Amey
 */

#ifndef MAKEINPUTOUTPUTMATRICESTEMP_H_
#define MAKEINPUTOUTPUTMATRICESTEMP_H_
#include<iostream.h>
#include<fstream.h>
#include "SVM_constants.h"
using namespace std;
class MakeInputOutputMatrices {
public:

static void setInputOutputMatrix(SVM (&s),char *path)
{
cout<<"Setting the Input and Output Matrices"<<endl;
row temp;
ifstream infile;
infile.open(path, ios::binary | ios::in);
int no_of_inputs=0;
while(infile && no_of_inputs<total_input_in_dataset)
{
//reading from the binary file and initializing input matrix rows
infile.read((char *)(&temp), sizeof(temp));
for(int i=0; i<(attributes+1); i++)
{
	if(temp.input[i]<0){
		s.x[no_of_inputs][i]=0;
	}
	else {
	s.x[no_of_inputs][i]=temp.input[i];
	}
	if(i==attributes)
		s.x[no_of_inputs][i]=1;

}

	s.d[no_of_inputs][0]=temp.output;

no_of_inputs++;
}

cout<<"Input and output matrix successfully created. Enjoy! "<<endl;
}


};

#endif /* MAKEINPUTOUTPUTMATRICESTEMP_H_ */
