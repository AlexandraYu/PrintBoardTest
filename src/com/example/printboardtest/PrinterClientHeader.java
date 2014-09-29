package com.example.printboardtest;

import java.nio.ByteBuffer;

import android.util.Log;

public class PrinterClientHeader {
	private short versionNumber;
	private short operationId;
	private int requestId;
	
	private byte beginAttGroupTag;
	
	private byte valueTag; 
	private short nameLength; 
	private String attName;
	private short valueLength;
	private String attValue; 
	
	private byte endOfAttTag; //occurs exactly once in an operation
	
	private String data; 
	
	private byte[] byteArray = new byte[8];
	
	PrinterClientHeader () {}
	/*
	printClientHeader (short verNum, short opId, int reqId, byte beginAttGroupTag, byte endOfAttTag, String d) {
		versionNumber = verNum; 
		operationId = opId; 
		requestId = reqId; 
		this.beginAttGroupTag = beginAttGroupTag; 
		this.endOfAttTag = endOfAttTag; 
		data = d; 
	}
	*/
	
	public void setVersionNum (short vNum) {
		versionNumber = vNum;
		byteArray[0] = (byte)(versionNumber>>8 & 0xFF);		
		byteArray[1] = (byte)(versionNumber & 0xFF);
		Log.d("Alex", "byteArray[0]= "+byteArray[0]+ ", byteArray[1]= "+byteArray[1]); 
	}
	public void setOperationId (short opId) {
		operationId = opId; 
		byteArray[2] = (byte)(operationId>>8 & 0xFF); 
		byteArray[3] = (byte)(operationId & 0xFF);
		Log.d("Alex", "byteArray[2]= "+byteArray[2]+ ", byteArray[3]= "+byteArray[3]);
	}
	public void setRequestId (int reqId) {
		requestId = reqId;
		byte[] bytes = ByteBuffer.allocate(4).putInt(requestId).array();
		int idx = 4 ;
		for (byte b : bytes) {
		   byteArray[idx] = b; 
		   idx++;
		}
		Log.d("Alex", "byteArray[4]= "+byteArray[4]+", byteArray[5]="+byteArray[5]+", byteArray[6]= "+byteArray[6]+", byteArray[7]= "+byteArray[7]);
	}
	public void setBeginAttGroupTag (byte bAttGroupTag) {
		beginAttGroupTag = bAttGroupTag; 
	}
	public void setValueTag (byte vTag) {
		valueTag = vTag; 
	}
	public void setNameLength (short nameLen) {
		nameLength = nameLen; 
	}
	public void setAttName (String aName) {
		attName = aName; 
	}
	public void setValueLength (short valueLen) {
		valueLength = valueLen; 
	}
	public void setAttValue (String aValue) {
		attValue = aValue; 
	}
	public void setEndOfAttTag (byte eAttTag) {
		endOfAttTag = eAttTag; 
	}
	public void setData (String d) {
		data = d; 
	}
	public short getVersionNum () {
		return versionNumber; 
	}
	public short getOperationId () {
		return operationId; 
	}
	public int getRequestId () {
		return requestId; 
	}
	public byte getBeginAttGroupTag () {
		return beginAttGroupTag; 
	}
	public byte getValueTag () {
		return valueTag; 
	}
	public short getNameLength () {
		return nameLength; 
	}
	public String getAttName () {
		return attName; 
	}
	public short getValueLength () {
		return valueLength; 
	}
	public String getAttValue () {
		return attValue; 
	}
	public byte getEndOfAttTag () {
		return endOfAttTag; 
	}
	public String getData () {
		return data; 
	}
	
	public void concatenateInfo() {
		
	}

}
