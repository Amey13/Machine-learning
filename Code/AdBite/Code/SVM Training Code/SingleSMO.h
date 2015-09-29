/*
 * SingleSMO.h
 *
 *  Created on: Mar 15, 2012
 *      Author: Amey 2012
 */
#ifndef SINGLESMO_H_
#define SINGLESMO_H_
#ifndef alphaStore_H
#define alphaStore_H
typedef struct
{
	double alpha[no_of_training_input];
} alpha1;
#endif
class SingleSMO {

public:
	static void initilizeAlphas(SVM (&s))
	{
		for (int i=0;i<no_of_training_input;i++)
		{
			s.a[i]=0;//changing all alphas to zero
		}
	}
	double error(SVM (&s), int &i)
		{

		//cout<<"Inside error function"<<endl;
		//cout<<"----------------------------"<<endl;
		double error1=0, fun=0;
		for(int j=0;j<no_of_training_input;j++)
		{
		fun+=(s.d[j][0]*s.kernel[j][i]*s.a[j]);
		}
		fun+=s.b;
		error1=fun-s.d[i][0];
		//cout<<"error function executed and now returning error"<<endl;
		return error1;
	}


	double max(double a,double b)
	{
		if(a>b)
		  return a;
		else
		  return b;
	}

	double min(double a,double b)
	{
		if(a<b)
		  return a;
		else
		  return b;
	}


static void setAlphas(SVM (&s), double &c1)
{
	alpha1 a1;
	SingleSMO e;
	int i=0,l=0;
	int max_passes=3,nc_alphas=0 ;
	int passes=0;
	double tol=0.0001;
	double ei=0;
	double Hii, tempij;
	double a1old=0, a1new=0;
	double c[no_of_training_input]={0};
	int cad=0, cnad=0, fad=0, fnad=0;
	for(int k=0; k<no_of_training_input; k++)
	{
	    		 c[k]=c1;
	}
	while(passes < max_passes)
	{
	      nc_alphas=0;//storing number of changed alphas through each pass

	    for(i=no_of_training_input;i!=-1;i--)//looping over all training samples
		 {
	    	 ei=e.error(s,i);
	    	 //cout<<"error of "<<i<<"th input :"<<ei<<endl;

	    	 //checking whether the ith sample violates KKT conditions
		  if((((s.d[i][0]*ei)<((-1)*tol)) && (s.a[i]<c[i])) || (((s.d[i][0]*ei)>tol) && (s.a[i]>0)))
		   {
			a1old=s.a[i];
			Hii=s.d[i][0]*s.d[i][0]*s.kernel[i][i];
			tempij=0;
			for( int j=0; j<no_of_training_input; j++)
			{
				if(j!=i)
					tempij+=s.a[j]*s.d[i][0]*s.d[j][0]*s.kernel[i][j];
			}
			a1new=(1-tempij)/Hii;

			//conditions for new values of a[i]
			//the alpha value is clipped to it`s maximum and minimum
	        if(a1new>c[i])
	        {
			a1new=c[i];
	        }
	        else if(a1new<0)
			{
			a1new=0;
			}

			if (fabs(a1new-a1old) < 10E-3)
					continue;
			s.a[i]=a1new;
			nc_alphas = nc_alphas+1;
		   }

		     else
		         continue;

		 }               /*end of for loop*/

		   if(nc_alphas==0)
			   passes=passes+1;
		   else
			   passes=0;
		  cout<<"Pass no. "<<passes<<" with no. of changed alpha= "<<nc_alphas<<endl;
		 }     /*end of while*/

		//setting threshold b value
	   int Nnsv=0;
	   double tempq=0, tempp=0;
	   for(int p=0; p<no_of_training_input; p++)
	   {
		   tempq=0;
		   if(s.a[p]>0 && s.a[p]<c[p])
		   {
			   Nnsv++;
			   for(int q=0; q<no_of_training_input; q++)
			   {
				if(s.a[q]>0)
				{
					tempq+=s.a[q]*s.d[q][0]*s.kernel[q][p];
				}
			   }
		   tempp+=s.d[p][0]-tempq;
		   }
	   }
	   s.b=tempp/Nnsv;
	   cout<<"value of b :"<<s.b<<endl;
}


};

#endif /* SINGLESMO_H_ */
