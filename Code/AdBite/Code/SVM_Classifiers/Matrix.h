/*
 * MatrixTemp.h
 *
 *  Created on: Jan 19, 2012
 *      Author: Amey
 */

#ifndef MATRIXTEMP_H_
#define MATRIXTEMP_H_
#include<iostream>
#include<iomanip>
#include<math.h>
#include "SVM_constants.h"
using namespace std;

class Matrix {
public:
static void Addition(double **a, double **b, double **c, int m, int n)
{
for(int i=0; i<m; i++)
	for(int j=0; j<n; j++)
		c[i][j]=a[i][j]+b[i][j];

}
static void Multiply(SVM (&s), double **b, double (&c)[no_of_training_input][no_of_training_input])
{
for(int i=0;i<no_of_training_input; i++)
	for(int j=0; j<no_of_training_input; j++)
	{
		c[i][j]=0;
		for(int k=0; k<attributes+1; k++)
			c[i][j]+=s.x[i][k]*b[k][j];
	}

}

static double normOfVector(double*a, int m, int n){
if(n!=1)
	{
	cout<<"Yeda banavato kai! Vector ahe ka ha? :P";
	return 0;
	}
else
{
//Vector ahe..
double value=0;
for(int i=0; i<m; i++)
{
value+=a[i]*a[i];
}
return sqrt(value);
}
}
static void transposeOfMatrix(SVM (&s), double **c, int m, int n){
int i;
//Transpose code
for(i=0; i<m;i++)
	for(int j=0; j<n; j++)
		c[i][j]=s.x[j][i];
}

static void displayMatrix(SVM (&s), int m, int n)
{
	cout<<"Inside displayMatrix Function."<<endl;
for(int i=0; i<m; i++)
{
	cout<<i+1<<" : ";
	for(int j=0; j<n; j++)
		cout<<setw(10)<<s.x[i][j];
	cout<<endl;
}

}

static void displayMatrix2(SVM (&s), int m, int n)
{
	cout<<"Inside displayMatrix Function."<<endl;
for(int i=0; i<m; i++)
{
	cout<<i+1<<" : ";
	for(int j=0; j<n; j++)
		cout<<setw(15)<<s.kernel[i][j];
	cout<<endl;
}

}

};

#endif /* MATRIXTEMP_H_ */
