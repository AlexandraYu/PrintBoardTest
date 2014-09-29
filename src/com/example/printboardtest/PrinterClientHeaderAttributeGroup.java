package com.example.printboardtest;

public class PrinterClientHeaderAttributeGroup {
	private byte valueTag; 
	private short nameLength; 
	private String attName;
	private short valueLength;
	private String attValue; 
	
	PrinterClientHeaderAttributeGroup (byte vTag, short nLength, String aName, short vLength, String aValue) {
		valueTag = vTag; 
		nameLength = nLength; 
		attName = aName; 
		valueLength = vLength; 
		attValue = aValue; 
	}

}
