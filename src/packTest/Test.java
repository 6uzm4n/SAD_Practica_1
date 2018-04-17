package packTest;
import java.io.IOException;

import Entrega1.*;
import Entrega2.*;

public class Test {
	//For testingggg
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//GetRaw
		String[] array = new String[3];
		array[0]="-d";
		array[1]="C:/Users/olizy/Desktop/movies_reviews/dev";
		array[2]="C:/Users/olizy/Desktop/dev.arff";
		
		//TransformRaw
		String[] array1 = new String[5];
		array1[0]="C:/Users/olizy/Desktop/dev.arff";
		array1[1]="C:/Users/olizy/Desktop/devBoW_idf.arff";
		array1[2]="TFIDF";
		array1[3]="Sparse";
		array1[4]="diccionario";
		
		//MakeCompatible
		String[] array2 = new String[3];
		array2[0]="diccionario";
		array2[1]="C:/Users/olizy/Desktop/dev.arff";
		array2[2]="C:/Users/olizy/Desktop/devBoW2.arff";
		
		try {
		//	GetRaw.main(array);
			TransformRaw.main(array1);
		//	MakeCompatible.main(array2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
