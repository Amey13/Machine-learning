/*
 * SigmaSet.h
 *
 *  Created on: Jan 19, 2012
 *      Author: Amey
 */

#ifndef row_H
#define row_H
typedef struct
{
	float input[attributes];
	float output;
}row;
#endif
#ifndef SIGMASET_H_
#define SIGMASET_H_

#include<iostream>
#include<fstream>
#include<Math.h>
#include "SVM_constants.h"

class SigmaSet {

void setSigmaWithInputMatrix(SVM (&s), int q)
{
	using namespace std;
	cout<<"inside sigmasetter.cpp"<<endl;
	s.sigma=0;


	int no_of_points;

	double distances[1000]={0};
	int quanta=q;
	no_of_points=0;
	int xi, xj;
	bool flag[no_of_training_input];
	for(int f=0; f<no_of_training_input; f++)
		flag[f]=false;

	while(no_of_points<1000)
	{
	//randomize();
	xi=(int)ceil(rand()%no_of_training_input);

	while(flag[xi]!=false)
	{
		xi=(int)ceil(rand()%no_of_training_input);

	}
	xj=(xi+quanta)%no_of_training_input;

	for(int k=0; k<1558; k++)
	{
		distances[no_of_points]+=pow((s.x[xi][k]-s.x[xj][k]), 2);
	}
	//distances[no_of_points]=(double)sqrt(distances[no_of_points]);
	if(distances[no_of_points]==0 )
		cout<<"xi= "<<xi<<" and xj= "<<xj<<endl;

	no_of_points++;
	flag[xi]=true;
	}

	//finding sigma

	//sorting the distances
	//cout<<endl<<"Sorting Started"<<endl;
	double doubletemp=0;
	for(int i=0; i<1000; i++)
	{
		for(int j=0; j<1000; j++)
		{
		if(distances[j]>distances[i])
			{
				//cout<<endl<<"swapping "<<distances[j]<<"and "<<distances[j+1]<<endl;
				doubletemp=distances[i];
				distances[i]=distances[j];
				distances[j]=doubletemp;

			}
		}
	}

	//cout<<endl<<"Sorting Ended"<<endl;
	for(int l=0; l<1000; l++)
		cout<<distances[l]<<endl;

	s.sigma=(distances[(int)1000/2]+distances[(int)(1000/2)+1])/2;
	cout<<"Boss sigma="<<s.sigma;

}


};

#endif /* SIGMASET_H_ */
