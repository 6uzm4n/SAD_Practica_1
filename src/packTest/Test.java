package packTest;
import java.io.IOException;

import Entrega1.*;
import Entrega2.*;
import Entrega2.fss.FssInfoGain;
import Entrega2.fss.MakeCompatibleFss;

public class Test {
	//For testingggg
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//GetRaw
		String[] array = new String[3];
		array[0]="-d";
		array[1]="C:/Users/olizy/Desktop/datasets/movies_reviews/train";
		array[2]="C:/Users/olizy/Desktop/train.arff";
		
		//TransformRaw
		String[] array1 = new String[5];
		array1[0]="C:/Users/olizy/Desktop/train.arff";
		array1[1]="C:/Users/olizy/Desktop/trainBoW.arff";
		array1[2]="BOW";
		array1[3]="Sparse";
		array1[4]="diccionario";
		
		//MakeCompatible
		String[] array2 = new String[3];
		array2[0]="diccionario";
		array2[1]="C:/Users/olizy/Desktop/dev.arff";
		array2[2]="C:/Users/olizy/Desktop/devBoW2.arff";
		
		//FssInfoGain
		String[] array3 = new String[2];
		array3[0]="C:/Users/olizy/Desktop/trainBoW.arff";
		array3[1]="C:/Users/olizy/Desktop/trainBoW_FSS2.arff";
		
		//MakeCompatibleFss
		String[] array4 = new String[3];
		array4[0]="C:/Users/olizy/Desktop/trainBoW_FSS2.arff";
		array4[1]="C:/Users/olizy/Desktop/devBoW.arff";
		array4[2]="C:/Users/olizy/Desktop/devBoW_FSS.arff";
		
		try {
		//	GetRaw.main(array);
		//	TransformRaw.main(array1);
		//	MakeCompatible.main(array2);
			
		//	FssInfoGain.main(array3);
			MakeCompatibleFss.main(array4);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
