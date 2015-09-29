/*
 * launchProject.cpp
 *
 *  Created on: Jan 15, 2012
 *      Author: Amey
*/



#include<iostream>
#include "SVM_constants.h"
#include "SVMStruct.h"
#include "CleanDataset.h"
#include "MakeInputOutputMatrices.h"
#include "SigmaSet.h"
#include "MakeKernelMatrix.h"
#include "Matrix.h"
#include "SingleSMO.h"
#include "SMO_heuristic.h"
#include "Tester.h"


using namespace std;
int main()
{

int count=1;
double i=4;
freopen("smo linear kernel output for c=32 to 256.txt", "w", stdout);

MakeInputOutputMatrices::setInputOutputMatrix(s, "dataset2.dat");
//Matrix::displayMatrix(x, no_of_training_input, attributes);

cout<<"Cleaning input matrix with averages"<<endl;
CleanDataset cd;
cd.cleanWithAverages(s, 2);

cout<<"Cleaned Matrix"<<endl;
//Matrix::displayMatrix(s, no_of_training_input, attributes+1);

//SigmaSet s;
//s.setSigmaWithInputMatrix(s, 10);


//MakeKernelMatrix::makeLinearKernelMatrixBinaryFile(x);
cout<<endl<<"Kernel Matrix Binary File made."<<endl;
MakeKernelMatrix::setKernelMatrixUsingKernelFile(s,"linear kernel.DAT2(0-1)");

//Matrix::displayMatrix2(kernel, no_of_training_input, no_of_training_input);
cout<<" Setting Alphas"<<endl;
//SMO_heuristic::initilizeAlphas(a);
for(i=32, count=0; i<257; i=i*2)
{
cout<<"C value is "<<i<<endl;

SingleSMO::initilizeAlphas(s);
SingleSMO::setAlphas(s, i);

for(int k=0; k<no_of_training_input; k++)
{
//cout<<"alpha "<<k+1<<" = "<<a[k]<<endl;
if(s.a[k]!=0)
	count++;
}

cout<<"b = "<<s.b<<endl;
cout<<"Nsv= "<<count<<endl;
cout<<"Testing begins for Complete Data set: "<<endl;

Test::testfunction(s);
cout<<"##############################################"<<endl;
cout<<endl<<"Testing begins for Testing Data set: "<<endl;
Test::LtestfunctionForTestData(s);
}

cout<<endl<<"Done Bye bye!";
ofstream ofile;
ofile.open("linear SVM.data", ios::binary | ios::out);
ofile.write((char *)&s, sizeof(SVM));
cout<<"Kernel Matrix successfully written"<<endl;
return 0;
}

