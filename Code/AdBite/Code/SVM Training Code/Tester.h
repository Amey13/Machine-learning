/*
 * Tester.h
 
 *  Created on: Feb 17, 2012
 *      Author: Amey
 */

#ifndef TESTER_H_
#define TESTER_H_
#include "SVM_constants.h"
class Test {
public:
	static void testfunction(SVM (&s))
	{
	//This function calculates the output of each input and checks whether it`s been
	//classified correctly or not
	cout<<" inside Testing function! "<<endl;
	int correctad=0, wrongad=0, correctnad=0, wrongnad=0, i=0, j=0;
	double output=0, outputtemp=0;
	//calculating the output of each sample
	for(i=0; i<no_of_training_input; i++)
	{
		output=0, outputtemp=0;
		for(j=0;j<no_of_training_input;j++)
		{
			outputtemp+=(s.d[j][0]*s.kernel[j][i]*s.a[j]);
		}
	outputtemp+=s.b;

	if(outputtemp>0)
		output=1;
	else if(outputtemp<0)
		output=-1;
	//cout<<"Row no "<<i<<" SVM Ouput "<<output<<" and Expected output= "<<d[i][0]<<endl;
	//checking if the output is correct or not.
	if(output==s.d[i][0]){
		if(s.d[i][0]==-1)
		correctnad++;
		else if(s.d[i][0]==1)
		correctad++;
		}
	else if(output!=s.d[i][0])
	{
		if(s.d[i][0]==-1)
			wrongnad++;
		else if(s.d[i][0]==1)
			wrongad++;

	}

	}
	cout<<endl<<"Number of Correct AD Outputs = "<<correctad;
	cout<<endl<<"Number of wrong AD Outputs = "<<wrongad;
	cout<<endl<<"Number of Correct Non-AD Outputs = "<<correctnad;
	cout<<endl<<"Number of wrong Non-AD Outputs = "<<wrongnad<<endl;


	}

static void LtestfunctionForTestData(SVM (&s))
{
		//This function calculates the output of each input and checks whether it`s been
		//classified correctly or not
		cout<<" inside Testing function! "<<endl;
		int k=0;
		int correctad=0, wrongad=0, correctnad=0, wrongnad=0, i=0, j=0;
		double output=0, outputtemp=0;
		for(i=no_of_training_input+1; i<total_input_in_dataset; i++)
		{
			outputtemp=0;
			//calculating the output of each sample
			for(j=0; j<no_of_training_input; j++)
			{
			output=0;
			for(k=0; k<attributes+1; k++)
			{
				output+=s.x[j][k]*s.x[i][k];
			}
			outputtemp+=(s.d[j][0]*output*s.a[j]);
			}
			outputtemp+=s.b;
			if(outputtemp>0)
				output=1;
			else if(outputtemp<0)
				output=-1;
			//cout<<"Row no "<<i<<" SVM Ouput "<<output<<" and Expected output= "<<d[i][0]<<endl;
			//checking if the output is correct or not.
			if(output==s.d[i][0]){
					if(s.d[i][0]==-1)
						correctnad++;
					else if(s.d[i][0]==1)
						correctad++;
						}
			else if(output!=s.d[i][0]){
						if(s.d[i][0]==-1)
							wrongnad++;
						else if(s.d[i][0]==1)
							wrongad++;

					}
		}
		cout<<endl<<"Number of Correct AD Outputs = "<<correctad;
		cout<<endl<<"Number of wrong AD Outputs = "<<wrongad;
		cout<<endl<<"Number of Correct Non-AD Outputs = "<<correctnad;
		cout<<endl<<"Number of wrong Non-AD Outputs = "<<wrongnad<<endl;

}
};

#endif /* TESTER_H_ */
