/*
 * CleanData.h
 *
 *  Created on: Jan 19, 2012
 *      Author: AMEY
 */
#include<iostream>
using namespace std;
#ifndef CLEANDATA_H_
#define CLEANDATA_H_

#include "SVM_constants.h"
#include "SVMStruct.h"

#ifndef row_H
#define row_H
typedef struct
{
	double input[attributes];
	double output;
}row;
#endif
class CleanDataset {
public :

void cleanWithAverages(SVM (&s), int mode=1)
{
	if(mode==1){
		cout<<endl<<"inside cleanWithAverages with mode= "<<mode<<endl;
		double totalcolumn1=0;
		s.maxcolumn1=s.x[0][0];
		double avgcolumn1=0;
		double totalcolumn2=0;
		s.maxcolumn2=s.x[1][0];
		double avgcolumn2=0;

	for(int i=0; i<total_input_in_dataset; i++)
	{	//cout<<"Row "<<i<<endl;

				//cout<<"first column detected"<<endl;
				totalcolumn1+=s.x[i][0];
				if(s.maxcolumn1<s.x[i][0])
					s.maxcolumn1=s.x[i][0];


				//cout<<"second column detected"<<endl;
				totalcolumn2+=s.x[i][1];
				if(s.maxcolumn2<s.x[i][1])
					s.maxcolumn2=s.x[i][1];


		//cout<<"Row "<<i<<"ended";
	}
	cout<<"Maximum out of column1 data= "<<s.maxcolumn1<<"and total of column1 data is= "<<totalcolumn1<<endl;
	cout<<"Maximum out of column2 data= "<<s.maxcolumn2<<"and total of column2 data is= "<<totalcolumn2<<endl;

	avgcolumn1=totalcolumn1/total_input_in_dataset;
	avgcolumn2=totalcolumn2/total_input_in_dataset;

	cout<<"cleaning the matrix with averages";
	for(int ii=0; ii<total_input_in_dataset; ii++)
	{
		for(int jj=0; jj<attributes+1; jj++)
		{
			if(s.x[ii][jj]==0.0000000011)
			{
			if(jj==0)
				s.x[ii][jj]=avgcolumn1;
			else if(jj==1)
				s.x[ii][jj]=avgcolumn2;
			else if(jj==2)
				s.x[ii][jj]=s.x[ii][jj-1]/s.x[ii][jj-2];
			else s.x[ii][jj]=0;
			}
		}
	}
	cout<<endl<<"Cleaning ended";
	/*
	double s.maxcolumn3=s.x[0][2];
	for(int iiii=0; iiii<total_input_in_dataset; iiii++)
	{
		if(s.maxcolumn3<s.x[iiii][2])
					s.maxcolumn3=s.x[iiii][2];
	}



	cout<<endl<<"Now Normalizing the input data"<<endl;

	for(int iii=0; iii<total_input_in_dataset; iii++)
		{	s.x[iii][0]=s.x[iii][0]/s.maxcolumn1;
			s.x[iii][1]=s.x[iii][1]/s.maxcolumn2;
			s.x[iii][2]=s.x[iii][2]/s.maxcolumn3;
		}

	cout<<"Normalizing done!"<<endl;
	*/
	}

	else if(mode==2)
	{
		cout<<endl<<"inside cleanWithAverages with mode= "<<mode<<endl;

			//for Column1
			double totalcolumn11=0;//total of values of class 1-ad
			double totalcolumn12=0;//total of values of class 2-non ad
			s.maxcolumn1=s.x[0][0];
			double avgcolumn11=0;//average of values of class1-ad
			double avgcolumn12=0;//average of values of class2-non ad
			//for Column2
			double totalcolumn21=0;//total of values of class 1-ad
			double totalcolumn22=0;//total of values of class 2-non ad
			s.maxcolumn2=s.x[1][0];
			double avgcolumn21=0;//average of values of class1-ad
			double avgcolumn22=0;//average of values of class2-non ad


		for(int i1=0; i1<total_input_in_dataset; i1++)
		{	//cout<<"Row "<<i<<endl;

			//cout<<"first column detected"<<endl;
			if(s.d[i1][0]==1)
				totalcolumn11+=s.x[i1][0];
			else if(s.d[i1][0]==-1)
				totalcolumn12+=s.x[i1][0];

			if(s.maxcolumn1<s.x[i1][0])
				s.maxcolumn1=s.x[i1][0];


			//cout<<"second column detected"<<endl;
			if(s.d[i1][0]==1)
				totalcolumn21+=s.x[i1][1];
			else if(s.d[i1][0]==-1)
				totalcolumn22+=s.x[i1][1];

			if(s.maxcolumn2<s.x[i1][1])
				s.maxcolumn2=s.x[i1][1];


			//cout<<"Row "<<i<<"ended";
		}
		cout<<"Maximum out of column1 data= "<<s.maxcolumn1<<"and total of column1 ad data is= "<<totalcolumn11<<endl;
		cout<<"Maximum out of column1 data= "<<s.maxcolumn1<<"and total of column1 non ad data is= "<<totalcolumn11<<endl;
		cout<<"Maximum out of column2 data= "<<s.maxcolumn2<<"and total of column2 ad data is= "<<totalcolumn21<<endl;
		cout<<"Maximum out of column2 data= "<<s.maxcolumn2<<"and total of column2 non data is= "<<totalcolumn22<<endl;

		avgcolumn11=totalcolumn11/total_input_in_dataset;
		avgcolumn12=totalcolumn12/total_input_in_dataset;
		avgcolumn21=totalcolumn21/total_input_in_dataset;
		avgcolumn22=totalcolumn22/total_input_in_dataset;

		cout<<"cleaning the matrix with averages";
		for(int ii=0; ii<total_input_in_dataset; ii++)
		{
			for(int jj=0; jj<attributes+1; jj++)
			{
				if(s.x[ii][jj]==0.0000000011)
				{
				if(jj==0)
				{
					if(s.d[ii][0]==1)
						s.x[ii][jj]=avgcolumn11;
					else if(s.d[ii][0]==-1)
						s.x[ii][jj]=avgcolumn12;
				}
				else if(jj==1)
				{
					if(s.d[ii][0]==1)
						s.x[ii][jj]=avgcolumn21;
					else if(s.d[ii][0]==-1)
						s.x[ii][jj]=avgcolumn22;
				}
				else if(jj==2)
					s.x[ii][jj]=s.x[ii][jj-1]/s.x[ii][jj-2];
				else s.x[ii][jj]=0;
				}
			}
		}
		cout<<endl<<"Cleaning ended";

	}
	s.maxcolumn3=s.x[0][2];
	for(int iiii=0; iiii<total_input_in_dataset; iiii++)
		{
			if(s.maxcolumn3<s.x[iiii][2])
				s.maxcolumn3=s.x[iiii][2];
		}



	cout<<endl<<"Now Normalizing the input data"<<endl;

	for(int iii=0; iii<total_input_in_dataset; iii++)
		{
			s.x[iii][0]=s.x[iii][0]/s.maxcolumn1;
			s.x[iii][1]=s.x[iii][1]/s.maxcolumn2;
			s.x[iii][2]=s.x[iii][2]/s.maxcolumn3;
		}

	cout<<"Normalizing done!"<<endl;

}
};

#endif /* CLEANDATA_H_ */
