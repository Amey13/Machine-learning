/*
 * SVMClassifier.cpp
 *
 *  Created on: Apr 2, 2012
 *      Author: Amey
 */
using namespace std;
#include <iostream>
#include <fstream>
#include "SVM_constants.h"
#include "SVMClassifier.h"
#include "SVMStruct.h"
SVMClassifier::SVMClassifier() {
	ifstream infile1;
	infile1.open("C:\\SVM\\SVM.data", ios::binary | ios::in);
	if(infile1.fail())
	{
	cout<<endl<<"SVM could not be loaded"<<endl;
	exit(1);
	}
	infile1.read((char *)(&svariable), sizeof(svariable));

}

SVMClassifier::~SVMClassifier() {
	// TODO Auto-generated destructor stub
}
