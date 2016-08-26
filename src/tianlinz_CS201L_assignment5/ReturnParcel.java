package tianlinz_CS201L_assignment5;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

public class ReturnParcel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String operation;
	
	private boolean result;
	
	private Vector<String> vector1;
	private Vector<String> vector2;
	private Vector<String> vector3;
	private Vector<String> vector4;

	private String content1;
	private String content2;
	private String content3;
	private String content4;
	
	private HashMap<String, String> map;
	
	//Parcel to return bool value
	public ReturnParcel(String o, boolean r){
		operation = o;
		result = r;
	}
	
	//Parcel to return vector of strings
	public ReturnParcel(String o, Vector<String> f, Vector<String> i, Vector<String> three, Vector<String> four){
		operation = o;
		vector1 = f;
		vector2 = i;
		vector3 = three;
		vector4 = four;
	}
	
	//Parcel to return a string content
	public ReturnParcel(String o, String one, String two, String three, String four){
		operation = o;
		content1 = one;
		content2 = two;
		content3 = three;
		content4 = four;
	}
	
	public ReturnParcel(String o, HashMap<String, String> m){
		operation = o;
		map = m;
	}
	
	public String getOperation(){
		return operation;
	}
	
	public boolean getResult(){
		return result;
	}
	
	public Vector<String> getVector1(){
		return vector1;
	}
	
	public Vector<String> getVector2(){
		return vector2;
	}
	
	public Vector<String> getVector3(){
		return vector3;
	}

	public Vector<String> getVector4(){
		return vector4;
	}
	
	public String getContent2(){
		return content2;
	}
	
	public String getContent1(){
		return content1;
	}
	
	public String getContent3(){
		return content3;
	}
	
	public String getContent4(){
		return content4;
	}
	
	public HashMap<String, String> getMap(){
		return map;
	}
}
