/*
 * MakeKernelMatrixTemp.h
 *
 *  Created on: Jan 19, 2012
 *      Author: Amey
 */

#ifndef MAKEKERNELMATRIXTEMP_H_
#define MAKEKERNELMATRIXTEMP_H_

#include<iostream>
#include<fstream>
#include "SVM_constants.h"
#include "Matrix.h"
#include <Math.h>
using namespace std;

typedef struct
{
double k[no_of_training_input][no_of_training_input];
}ker;
ker k1;
class MakeKernelMatrix{

public:
	static void MakeKernelMatrixBinaryFile(SVM (&s)) {
		//Making a Gaussian kernel and storing it in a binary file
		cout<<endl<<"Setting Kernel Matrix with sigma= "<<s.sigma<<endl;
		double exponent=0.0;
		double sigmasquare=s.sigma;
		int i, j,k;
		for(i=0;i<no_of_training_input; i++)
		{
			exponent=0.0;
			for(j=0; j<no_of_training_input; j++)
			{
			//cout<<"i= "<<i<<"and j="<<j<<endl;
				exponent=0.0;
				for(k=0; k<attributes; k++)
					exponent+=pow((s.x[i][k]-s.x[j][k]),2);
			k1.k[i][j]= exp((-1)*exponent/(2*sigmasquare));
			}
		}
		cout<<"Kernel Matrix successfully created!"<<endl;
		cout<<"Making Binary File of Kernel Matrix"<<endl;
		ofstream ofile;
		ofile.open("KERNEL_latest.DAT2(0-1)", ios::binary | ios::out);
		ofile.write((char *)&k1, sizeof(ker));
		cout<<"Kernel Matrix successfully written"<<endl;

	}
	static void makeLinearKernelMatrixBinaryFile(SVM (&s))
	{
		//Making a Linear kernel and storing it in a binary file
		double **transposex= new double*[attributes+1];
		for(int i=0; i<attributes+1; i++)
		{
			transposex[i]=new double[total_input_in_dataset];
			for(int j=0; j<total_input_in_dataset; j++)
				transposex[i][j]=0;
		}

		Matrix::transposeOfMatrix(s, transposex, attributes+1, total_input_in_dataset);
		Matrix::Multiply(s, transposex, k1.k);
		ofstream ofile;
		ofile.open("linear kernel.DAT2(0-1)", ios::binary | ios::out);
		ofile.write((char *)&k1, sizeof(ker));
		cout<<"Kernel Matrix successfully written"<<endl;
	}
	static void setKernelMatrixUsingKernelFile(SVM (&s), char *path)
	{
		ifstream infile1;
		infile1.open(path, ios::binary | ios::in);
		cout<<endl<<"Setting Kernel Matrix using "<<path<<endl;
		infile1.read((char *)(&k1), sizeof(k1));
		for(int i=0;i<no_of_training_input; i++)
		{
			for(int j=0;j<no_of_training_input; j++)
			{
				s.kernel[i][j]=k1.k[i][j];
			}
		}
	}

};


#endif /* MAKEKERNELMATRIXTEMP_H_ */
