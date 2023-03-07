package com.unidapp.manager.api.model;

import java.util.*;

public class GeneralRest <T> {
	
	private ArrayList<T> answerList = new ArrayList<T>();
	private T genericObject;
	private String answer;
	private int code;
	private String variable;
	


	public GeneralRest(ArrayList<T> answerList,T genericObject , String answer, int code, String variable) {
		this.answerList = answerList;
		this.answer = answer;
		this.genericObject=genericObject;
		this.code= code;
		this.variable= variable;
	}
	
	public GeneralRest(T genericObject , String answer, int code) {
		this.answerList = new ArrayList<T>();
		this.answer = answer;
		this.genericObject=genericObject;
		this.code= code;
		this.variable= "";
	}
	
	public GeneralRest(ArrayList<T> answerList, String answer, int code) {
		this.answerList = answerList;
		this.answer = answer;
		this.genericObject= null;
		this.code= code;
		this.variable= "";
	}
	
	public GeneralRest(String answer, int code) {
		this.answerList = new ArrayList<T>();
		this.answer = answer;
		this.genericObject= null;
		this.code= code;
		this.variable= "";
	}
	
	public GeneralRest(T genericObject, String answer, int code, String variable) {
		this.answerList = new ArrayList<T>();
		this.answer = answer;
		this.genericObject= genericObject;
		this.code= code;
		this.variable= variable;
	}
	
	
	
	public GeneralRest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ArrayList<T> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(ArrayList<T> answerList) {
		this.answerList = answerList;
	}

	public T getGenericObject() {
		return genericObject;
	}

	public void setGenericObject(T genericObject) {
		this.genericObject = genericObject;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	}
