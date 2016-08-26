package tianlinz_CS201L_assignment5;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

public class Parcel  implements Serializable{
	private static final long serialVersionUID = 1L;
	private boolean isQuery;
	private String operation;
	private String command;
	private String content1;
	private String content2;
	private String content3;
	private String content4;
	private Vector<String> vector1;
	private Vector<String> vector2;
	private HashMap<String, String> map;

	public boolean isQuery() {
		return isQuery;
	}

	public String getOperation() {
		return operation;
	}
	
	
	public String getCommand() {
		return command;
	}
	
	public String getContent1(){
		return content1;
	}
	
	public String getContent2(){
		return content2;
	}

	public String getContent3(){
		return content3;
	}
	
	public String getContent4(){
		return content4;
	}
	
	public Vector<String> getVector1(){
		return vector1;
	}
	
	public Vector<String> getVector2(){
		return vector2;
	}
	
	public HashMap<String, String> getMap(){
		return map;
	}
	
	public Parcel(Boolean isQuery, String operation, String command, String content1, String content2, String content3, String content4) {
		this.isQuery = isQuery;
		this.operation = operation;
		this.command = command;
		this.content1 = content1;
		this.content2 = content2;
		this.content3 = content3;
		this.content4 = content4;
	}
	
	public Parcel(Boolean isQuery, String operation, String command, Vector<String> vector1, Vector<String> vector2) {
		this.isQuery = isQuery;
		this.operation = operation;
		this.command = command;
		this.vector1 = vector1;
		this.vector2 = vector2;
	}
	
	public Parcel(Boolean isQuery, String operation, HashMap<String, String> m) {
		this.isQuery = isQuery;
		this.operation = operation;
		this.map = m;
	}
}
