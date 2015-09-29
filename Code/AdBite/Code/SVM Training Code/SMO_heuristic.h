/*
 * SMO_heuristic.h
 *
 *  Created on: Mar 5, 2012
 *      Author: BTECH-Com 2012
 */

#ifndef SMO_HEURISTIC_H_
#define SMO_HEURISTIC_H_


#ifndef alphaStore_H
#define alphaStore_H
typedef struct
{
	double alpha[no_of_training_input];
} alpha1;
#endif

class SMO_heuristic {
public:
	static void initilizeAlphas(SVM (&s))
	{
		for (int i=0;i<no_of_training_input;i++)
		{
			s.a[i]=0;//changing all alphas to zero
		}
	}
	double error(SVM (&s),int &i)
	{
		double error1=0, fun=0;
		for(int j=0;j<no_of_training_input;j++)
		{
		fun+=(s.d[j][0]*s.kernel[j][i]*s.a[j]);
		}
		fun+=s.b;
		error1=fun-s.d[i][0];
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


	static void setAlphas(SVM (&s), double &c)
	{
		 alpha1 al;
		 SMO_heuristic e;
		 int i=0,l=0;
	     int max_passes=2,nc_alphas=0 ;
		 int passes=0;
		 double tol=0.001,t=0,eta=0,b1=0,b2=0,c1,c2;
	     double ei=0,ej=0,a1=0,a2=0,L=0,H=0;
	     double temp1=0,temp2=0,temp3=0,temp4=0,Lobj=0, Hobj=0,eps=0.001, L1=0, H1=0, f1=0, f2=0;
	     int k=0, j=0;
	     double tmax=0, temp=0;

	     int cad=0, cnad=0, fad=0, fnad=0;

	     cout<<"Total ads = "<<cad<<endl<<"Total nad = "<<cnad<<endl;

	while(passes < max_passes)
	{
	      nc_alphas=0;
	      for(i=no_of_training_input-1;i>-1;i--)
	      {
	    	  ei=e.error(s,i);
	    	 //cout<<"error of "<<i<<"th input :"<<ei<<endl;
		  if((((s.d[i][0]*ei)<((-1)*tol)) && (s.a[i]<c)) || (((s.d[i][0]*ei)>tol) && (s.a[i]>0)))
		   {
			  for (j = -1, tmax = 0, k = 0; k < no_of_training_input; k++)
			{

				ej=0, temp=0;
			    ej = e.error(s,k);
				temp = fabs(ei - ej);
				if (temp > tmax)
				{
				  tmax = temp;
				  j = k;
				}

			}
			if (j>=0)
			{

		    ej=e.error(s,j);

	        a1=s.a[i];//storing old values of alphas
	        a2=s.a[j];//storing old values of alphas


		   //finding values of L and H
		   if(s.d[i][0]==s.d[j][0])
		   {
		     L= e.max(t,(a1+a2-c));
	         H= e.min(c,(a2+a1));
		   }
		   else if(s.d[i][0]!=s.d[j][0])
		   {
		   L= e.max(t,(a2-a1));
		   H= e.min(c, c+a2-a1);
	       }
	       //cout<<"L and H values "<<L <<"and "<< H<<endl;
	       if(L==H)
		   {
		    //cout<<"L and H r equal"<<endl;
		    //cout<<"rejected by L an H"<<endl;
		  	continue;
	       }
	       else
		   //finding value of eta
	       {
	        eta= 2*s.kernel[i][j]- s.kernel[i][i]- s.kernel[j][j];
	        //cout<<"value of eta :"<<eta<<endl;

	        if(eta>=0)
			{
	         //cout<<"rejected by eta"<<endl;
			       
	        	L1=a1 + s.d[i][0]*s.d[j][0]*(a2-L);
	        	H1=a1 + s.d[i][0]*s.d[j][0]*(a2-H);
	        	f1= s.d[i][0]*(ei+s.b) - a1*s.kernel[i][i] - s.d[i][0]*s.d[j][0]*a2*s.kernel[i][j];
	        	f2= s.d[j][0]*(ej+s.b) - a2*s.kernel[j][j] - s.d[i][0]*s.d[j][0]*a1*s.kernel[i][j];
	        	Lobj= -0.5*L1*L1*s.kernel[i][i] - 0.5*L*L*s.kernel[j][j] - s.d[i][0]*s.d[j][0]*L1*L*s.kernel[i][j] - L1*f1 - L*f2;
	        	Hobj= -0.5*H1*H1*s.kernel[i][i] - 0.5*H*H*s.kernel[j][j] - s.d[i][0]*s.d[j][0]*H1*H*s.kernel[i][j] - H1*f1 - H*f2;
				if (Lobj > Hobj+eps)
					s.a[j] = L;
				else if (Lobj < Hobj-eps)
					s.a[j] = H;
				else
					s.a[j] = a2;
	            continue;

			}
	        else
			{    //computing new values of a[j]
	        s.a[j]= a2-(((1.0)*s.d[j][0]*(ei -ej))/eta);
	        //conditions for new values of a[j]
	        if(s.a[j]>H)
	        {
	        s.a[j]=H;
			}
	        else if(s.a[j]<L)
			{
	        s.a[j]=L;
			}
	        else if(s.a[j]>=L && s.a[j]<=H)
	        {
	        	s.a[j]=s.a[j];
	        }

			if (fabs(s.a[j]-a2) < eps*(s.a[j]+a2+eps))
					continue;
			else
			{


			/*cout<<"finding new value of a[i]"<<endl;*/

			s.a[i]=a1+((s.d[i][0]*s.d[j][0])*(a2-s.a[j]));

			//finding values of b ,b1, b2;
			temp1=(s.d[i][0]*(s.a[i]-a1)*s.kernel[i][i]);
			temp2=(s.d[j][0]*(s.a[j]-a2)*s.kernel[i][j]);
			temp3=(s.d[i][0]*(s.a[i]-a1)*s.kernel[i][j]);
			temp4=(s.d[j][0]*(s.a[j]-a2)*s.kernel[j][j]);
			b1= s.b-ei-temp1-temp2;
			b2=s.b-ej-temp3-temp4;
			if(s.a[i]>0 && s.a[i]<c)
				s.b=b1;
			else if(s.a[j]>0 && s.a[j]<c)
				s.b=b2;
				else
				s.b=(b1+b2)/2.0;
	      nc_alphas = nc_alphas+1;

	        }
	        }
		   }
			}
			else {
				//cout<<endl<<"Heuristic failed"<<endl;
				continue;
			}

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

		 cout<<"updated values of alpha"<<endl;
	     for(l=0;l<no_of_training_input;l++)
	     {
	    	cout<<"input"<<(l+1)<<"is :"<<s.a[l]<<endl;
	     }

	}
};

#endif /* SMO_HEURISTIC_H_ */
