/*
 * SVMStruct.h
 *
 *  Created on: Apr 2, 2012
 *      Author: Amey
 */
//This is the file storing the SVM properties
#ifndef SVMSTRUCT_H_
#define SVMSTRUCT_H_

struct SVM{

double x[total_input_in_dataset][attributes+1];

double d[total_input_in_dataset][1];

double kernel[no_of_training_input][no_of_training_input];

double sigma;

double a[no_of_training_input];

double b;

double maxcolumn1, maxcolumn2, maxcolumn3;
};
SVM s;
#endif /* SVMSTRUCT_H_ */
