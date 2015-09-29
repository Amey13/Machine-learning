/*
 * mysql_connection.cpp
 *
 *  Created on: Mar 26, 2012
 *      Author: Amey
*/
#ifndef mysql_connection_H_
#define mysql_connection_H_
using namespace std;
#include <Math.h>
#include <mysql++.h>
#include <iostream>
#include <iomanip>
#include <string>
#include "SVM_constants.h"
#include "SVMStruct.h"
using namespace mysqlpp;
using namespace std;
class classification {
public :
static double classify(SVM (&svariable), double (&x1)[features+1])
{
	int j=0, k=0;
	int output1=0;
	double outputtemp=0,output=0;
	//calculating the output of each sample
	for(j=0; j<no_of_training_input; j++)
				{
				output=0;
				for(k=0; k<(features+1); k++)
				{
					output+=svariable.x[j][k]*x1[k];
				}
				output=pow((1+output),2)+1;
				outputtemp+=(svariable.d[j][0]*output*svariable.a[j]);
				}
				outputtemp+=svariable.b;
				if(outputtemp>=0)
					output=1;
				else if(outputtemp<0)
					output=-1;
	return output;
}
static void inputsetter(SVM (&svariable), char* pageid)
{
	double x1[features+1]={0};
    // Get database access parameters from command line
	int linkid=0;
	const char* db = "adeater2", *server = "127.0.0.1", *user = "root", *pass = "123456789";
	char q[1000]="select * from linkfeature where linkid=";
	char q1[500]="select * from link where pageid=";
	char q5[500];
	char q6[500],q7[500];

	strcat(q1, pageid);
	char t[10];
    // Connect to the sample database.
    Connection conn(false);

    if (conn.connect(db, server, user, pass)) {
        // Retrieve a subset of the sample stock table set up by resetdb
        // and display it.
        Query query = conn.query(q1);
        cout<<q1<<endl;
        cout<<"kaka boss"<<pageid<<endl;
        //query.exec();
        cout<<"kaka1"<<endl;
        if (StoreQueryResult res = query.store()) {
        	cout<<"Links : "<<res.num_rows();
            for (size_t i = 0; i < res.num_rows(); ++i) {
            	cout<<"kaka"<<endl;
               linkid= res[i][0];
               x1[0]=res[i][8];
               x1[1]=res[i][9];
               x1[2]=res[i][10];
               x1[3]=res[i][7];
               cout<<"yetoy!"<<endl;
               if(x1[0]>svariable.maxcolumn1)
               {
            	   x1[0]=1;
               }
               else
            	   x1[0]=x1[0]/svariable.maxcolumn1;

               if(x1[1]>svariable.maxcolumn2)
                  {
                    x1[1]=1;
                  }
               else
                    x1[1]=x1[1]/svariable.maxcolumn2;
               x1[2]=x1[1]/x1[0];

               if(x1[2]>svariable.maxcolumn3)
              {
                 x1[2]=1;
              }
               else
                  x1[2]=x1[2]/svariable.maxcolumn3;
               strcpy(q,"select * from linkfeature where linkid =");
               strcat(q, itoa(linkid, t, 10));
               strcat(q, " ORDER BY featureid asc");
               cout<<"query q= "<<q<<endl;
               Query query1= conn.query(q);
               if (StoreQueryResult res1 = query1.store()){
            	   cout<<"num rows "<<res1.num_rows()<<endl;
					for(size_t i1 = 0; i1 < res1.num_rows(); ++i1)
						{
							//cout<<"inside kaka"<<endl;
							x1[4+i1]=res1[i1][2];
						}
               }

            x1[features]=1;

            /*
            strcpy(q6,"update link set type='ad' where linkid=");
                        	strcat(q6, itoa(linkid, t, 10));
            				Query q2=conn.query(q6);
                        	q2.execute();
                        	q2.exec();
              */
            if(classify(svariable, x1)==1)
           	{
            	strcpy(q6,"update link set type='ad' where linkid=");
            	strcat(q6, itoa(linkid, t, 10));
				Query q2=conn.query(q6);
            	q2.execute();
           	}
            else
            {
            	strcpy(q7,"update link set type='non_ad' where linkid=");
            	strcat(q7, itoa(linkid, t, 10));
            	Query q3=conn.query(q7);
	            q3.execute();
            }
            }
            strcpy(q5, "update page set classificationstatus='completed' where pageid=");
            strcat(q5,  pageid);
            Query q4=conn.query(q5);
			q4.execute();
        }
        else
        {
            cerr << "Failed to get item list: " << query.error() << endl;
        }
    }
    else {
        cerr << "DB connection failed: " << conn.error() << endl;
    }
}
};
#endif
