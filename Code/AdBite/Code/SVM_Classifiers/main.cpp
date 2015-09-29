/*
 * main.cpp
 *
 *  Created on: Apr 2, 2012
 *      Author: Amey
 */
#include <iostream>
#include "SVM_constants.h"
#include "SVMClassifier.h"
#include "mysql_connection.h"

SVMClassifier s2;
int main(int argc, char** argv)
{
freopen("output.txt", "w", stdout);
cout<<"Argument Count : "<<argc<<endl;
if(argc>0)
{
cout<<argv[1]<<endl;
char* pageid=argv[1];
classification::inputsetter(s2.svariable, pageid);
}
else
{
cout<<"exit!";
}
}

