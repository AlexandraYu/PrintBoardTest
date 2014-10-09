package com.example.printboardtest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class MainActivity extends Activity {
	PrinterClientHeader header = new PrinterClientHeader(); 
	private byte[] printAttriByte, printJobByte, fileByte, totalPrintJobByte, validateJobByte; 
	private int counter =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		header.setVersionNum((short)20);
//		header.setOperationId((short)11);
//		header.setRequestId((int)16);
		printAttriByte= new byte[] {
		0x02, 0x00, //Version: 2.0
		0x00, 0x0b, //Operation-id: Get-Printer-Attributes
		0x00, 0x00, 0x00, 0x10, //Request ID: 16
		0x01, //operation-attributes-tag
		//attributes-charset: utf-8
		0x47, //Tag: Character set
		0x00, 0x12, //Name length: 18
		0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x75, 0x74, 0x65, 0x73, 0x2d, 0x63, 0x68, 0x61, 0x72, 0x73, 0x65, 0x74, //Name: attributes-charset
		0x00, 0x05, //Value length: 5
		0x75, 0x74, 0x66, 0x2d, 0x38, //Value: utf-8 
		//attributes-natural-language: zh-tw
		0x48, //Tag: Natural language
		0x00, 0x1b, //Name length: 27
		0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x75, 0x74, 0x65, 0x73, 0x2d, 0x6e, 0x61, 0x74, 0x75, 0x72, 0x61, 0x6c, 0x2d, 0x6c, 0x61, 0x6e, 0x67, 0x75, 0x61, 0x67, 0x65, //Name: attributes-natural-language
		0x00, 0x05, //Value length: 5
		0x7a, 0x68, 0x2d, 0x74, 0x77, //Value: zh-tw
		//printer-uri: ipp://myPrint3B. local.:631/USB1_LQ
		0x45, //Tag: URI
		0x00, 0x0b, //Name length: 11
		0x70, 0x72, 0x69, 0x6e, 0x74, 0x65, 0x72, 0x2d, 0x75, 0x72, 0x69, //Name: printer-uri
		0x00, 0x22, //Value length: 34
		0x69, 0x70, 0x70, 0x3a, 0x2f, 0x2f, 0x6d, 0x79, 0x50, 0x72, 0x69, 0x6e, 0x74, 0x33, 0x42, 0x2e, 0x6c, 0x6f, 0x63, 0x61, 0x6c, 0x2e, 0x3a, 0x36, 0x33, 0x31, 0x2f, 0x55, 0x53, 0x42, 0x31, 0x5f, 0x4c, 0x51, //Value: ipp://myPrint3B. local.:631/USB1_LQ
		//requested-attributes: copies-supported
		0x44, //Tag: Keyword
		0x00, 0x14, //Name length: 20
		0x72, 0x65, 0x71, 0x75, 0x65, 0x73, 0x74, 0x65, 0x64, 0x2d, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x75, 0x74, 0x65, 0x73, //Name: requested-attributes
		0x00, 0x10, //Value length: 16
		0x63, 0x6f, 0x70, 0x69, 0x65, 0x73, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: copies-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x19, //Value length: 25
		0x64, 0x6f, 0x63, 0x75, 0x6d, 0x65, 0x6e, 0x74, 0x2d, 0x66, 0x6f, 0x72, 0x6d, 0x61, 0x74, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: document-format-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x14, //Value length: 20
		0x66, 0x69, 0x6e, 0x69, 0x73, 0x68, 0x69, 0x6e, 0x67, 0x73, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: finishing-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x17, //Value length:23
		0x6a, 0x70, 0x65, 0x67, 0x2d, 0x6b, 0x2d, 0x6f, 0x63, 0x74, 0x65, 0x74, 0x73, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: jpeg-k-octets-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x1a, //Value length: 26
		0x6a, 0x70, 0x65, 0x67, 0x2d, 0x78, 0x2d, 0x64, 0x69, 0x6d, 0x65, 0x6e, 0x73, 0x69, 0x6f, 0x6e, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: jpeg-x-dimension-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x1a,//Value length: 26
		0x6a, 0x70, 0x65, 0x67, 0x2d, 0x79, 0x2d, 0x64, 0x69, 0x6d, 0x65, 0x6e, 0x73, 0x69, 0x6f, 0x6e, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: jpeg-y-dimension-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x1d, //Value length: 29
		0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x62, 0x6f, 0x74, 0x74, 0x6f, 0x6d, 0x2d, 0x6d, 0x61, 0x72, 0x67, 0x69, 0x6e, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: media-bottom-margin-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x12, //Value length: 18
		0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x63, 0x6f, 0x6c, 0x2d, 0x64, 0x61, 0x74, 0x61, 0x62, 0x61, 0x73, 0x65, //Value: media-col-database-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x29, //Value length: 41
		0x6c, 0x61, 0x6e, 0x64, 0x73, 0x63, 0x61, 0x70, 0x65, 0x2d, 0x6f, 0x72, 0x69, 0x65, 0x6e, 0x74, 0x61, 0x74, 
		0x69, 0x6f, 0x6e, 0x2d, 0x72, 0x65, 0x71, 0x75, 0x65, 0x73, 0x74, 0x65, 0x64, 0x2d, 0x70, 0x72, 0x65, 0x66, 0x65, 0x72, 0x72, 0x65, 0x64, //Value: landscape-orientation-requested-preferred
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x13, //Value length: 19
		0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x63, 0x6f, 0x6c, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: media-col-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x1b, //Value length: 27
		0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x6c, 0x65, 0x66, 0x74, 0x2d, 0x6d, 0x61, 0x72, 0x67, 0x69, 0x6e, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: media-left-margin-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x0f, //Value length: 15
		0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x63, 0x6f, 0x6c, 0x2d, 0x72, 0x65, 0x61, 0x64, 0x79,//Value: media-col-ready
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x1c, //Value length: 28
		0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x72, 0x69, 0x67, 0x68, 0x74, 0x2d, 0x6d, 0x61, 0x72, 0x67, 0x69, 0x6e, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: media-right-margin-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x1a, //Value length: 26
		0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x74, 0x6f, 0x70, 0x2d, 0x6d, 0x61, 0x72, 0x67, 0x69, 0x6e, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: media-top-margin-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x14, //Value length: 20
		0x6f, 0x70, 0x65, 0x72, 0x61, 0x74, 0x69, 0x6f, 0x6e, 0x73, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: operation-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x16, //Value length: 22
		0x70, 0x64, 0x66, 0x2d, 0x6b, 0x2d, 0x6f, 0x63, 0x74, 0x65, 0x74, 0x73, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: pdf-k-octets-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x1a, //Value length: 26
		0x70, 0x72, 0x69, 0x6e, 0x74, 0x2d, 0x63, 0x6f, 0x6c, 0x6f, 0x72, 0x2d, 0x6d, 0x6f, 0x64, 0x65, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: print-color-mode-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x17, //Value length: 23
		0x70, 0x72, 0x69, 0x6e, 0x74, 0x2d, 0x71, 0x75, 0x61, 0x6c, 0x69, 0x74, 0x79, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: print-quality-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x17, //Value length: 23
		0x70, 0x72, 0x69, 0x6e, 0x74, 0x2d, 0x73, 0x63, 0x61, 0x6c, 0x69, 0x6e, 0x67, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: print-scaling-supported
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x17, //Value length: 23
		0x70, 0x72, 0x69, 0x6e, 0x74, 0x65, 0x72, 0x2d, 0x63, 0x68, 0x61, 0x72, 0x67, 0x65, 0x2d, 0x69, 0x6e, 0x66, 0x6f, 0x2d, 0x75, 0x72, 0x69, //Value: printer-charge-info-uri
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x20, //Value length: 32
		0x70, 0x72, 0x69, 0x6e, 0x74, 0x65, 0x72, 0x2d, 0x6d, 0x61, 0x6e, 0x64, 0x61, 0x74, 0x6f, 0x72, 0x79, 0x2d, 0x6a, 0x6f, 0x62, 0x2d, 0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x75, 0x74, 0x65, 0x73, //Value: printer-mandatory-job-attributes
		0x44, //Tag: Keyword
		0x00, 0x00, //Name length: 0
		0x00, 0x0f, //Value length: 15
		0x73, 0x69, 0x64, 0x65, 0x73, 0x2d, 0x73, 0x75, 0x70, 0x70, 0x6f, 0x72, 0x74, 0x65, 0x64, //Value: sides-supported
		0x03 //End of attributes
		}; 
		
		printJobByte = new byte[] {
		0x02, 0x00, //Version: 2.0
		0x00, 0x02, //Operation-id: Print-Job
		0x00, 0x00, 0x00, 0x03, //Request ID: 3
		0x01, //operation-attributes-tag
		//attributes-charset: utf-8
		0x47, //Tag: Character set
		0x00, 0x12, //Name length: 18
		0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x75, 0x74, 0x65, 0x73, 0x2d, 0x63, 0x68, 0x61, 0x72, 0x73, 0x65, 0x74, //Name: attributes-charset
		0x00, 0x05, //Value length: 5
		0x75, 0x74, 0x66, 0x2d, 0x38, //Value: utf-8 
		//attributes-natural-language: zh-tw
		0x48, //Tag: Natural language
		0x00, 0x1b, //Name length: 27
		0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x75, 0x74, 0x65, 0x73, 0x2d, 0x6e, 0x61, 0x74, 0x75, 0x72, 0x61, 0x6c, 0x2d, 0x6c, 0x61, 0x6e, 0x67, 0x75, 0x61, 0x67, 0x65, //Name: attributes-natural-language
		0x00, 0x05, //Value length: 5
		0x7a, 0x68, 0x2d, 0x74, 0x77, //Value: zh-tw
		//printer-uri: ipp://myPrint3B. local.:631/USB1_LQ
		0x45, //Tag: URI
		0x00, 0x0b, //Name length: 11
		0x70, 0x72, 0x69, 0x6e, 0x74, 0x65, 0x72, 0x2d, 0x75, 0x72, 0x69, //Name: printer-uri
		0x00, 0x22, //Value length: 34
		0x69, 0x70, 0x70, 0x3a, 0x2f, 0x2f, 0x6d, 0x79, 0x50, 0x72, 0x69, 0x6e, 0x74, 0x33, 0x42, 0x2e, 0x6c, 0x6f, 0x63, 0x61, 0x6c, 0x2e, 0x3a, 0x36, 0x33, 0x31, 0x2f, 0x55, 0x53, 0x42, 0x31, 0x5f, 0x4c, 0x51, //Value: ipp://myPrint3B. local.:631/USB1_LQ
		//requesting-user-name: mobile
		0x42, //Tag: Name without language
		0x00, 0x14, //Name length: 20
		0x72, 0x65, 0x71, 0x75, 0x65, 0x73, 0x74, 0x69, 0x6e, 0x67, 0x2d, 0x75, 0x73, 0x65, 0x72, 0x2d, 0x6e, 0x61, 0x6d, 0x65, //Name: requesting-user-name
		0x00, 0x06, //Value length: 6
		0x6d, 0x6f, 0x62, 0x69, 0x6c, 0x65, //Value: mobile
		//document-format: image/jpeg  ********
		0x49, //Tag: MIME media type
		0x00, 0x0f, //Name length: 15
		0x64, 0x6f, 0x63, 0x75, 0x6d, 0x65, 0x6e, 0x74, 0x2d, 0x66, 0x6f, 0x72, 0x6d, 0x61, 0x74, //Name: document-format
		0x00, 0x0a, //Value length: 10  **********
		0x69, 0x6d, 0x61, 0x67, 0x65, 0x2f, 0x6a, 0x70, 0x65, 0x67, //Value: image/jpeg  *******
		//job-name: Google
		0x42, //Tag: Name without language
		0x00, 0x08, //Name length: 8
		0x6a, 0x6f, 0x62, 0x2d, 0x6e, 0x61, 0x6d, 0x65, //Name: job-name
		0x00, 0x06, //Value length: 6
		0x47, 0x6f, 0x6f, 0x67, 0x6c, 0x65, //Value: Google
		0x02, //Job attributes
		//copies: 1
		0x21, //Tag: Integer
		0x00, 0x06, //Name length: 6
		0x63, 0x6f, 0x70, 0x69, 0x65, 0x73, //Name: copies
		0x00, 0x04, //Value length: 4
		0x00, 0x00, 0x00, 0x01, //Value: 1
		//media-col: 
		0x34, //Tag: Reserved (0x34)
		0x00, 0x09, //Name length: 9
		0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x63, 0x6f, 0x6c, //Name: media-col
		0x00, 0x00, //Value length: 0
		//Value: 
		0x4a, //Tag: Reserved (0x4a)
		0x00, 0x00, //Name length: 0; 
		0x00, 0x0a, //Value length: 10
		0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x73, 0x69, 0x7a, 0x65, //Value: media-size
		0x34, //Tag: Reserved(0x34)
		0x00, 0x00, //Name length: 0
		0x00, 0x00, //Value length: 0
		//Value:
		0x4a, //Tag: Reserved (0x4a)
		0x00, 0x00, //Name length: 0
		0x00, 0x0b, //Value length: 11
		0x78, 0x2d, 0x64, 0x69, 0x6d, 0x65, 0x6e, 0x73, 0x69, 0x6f, 0x6e, //Value: x-dimension
		0x21, //Tag: Integer
		0x00, 0x00, //Name lengh: 0
		0x00, 0x04, //Value length: 4
		0x00, 0x00, 0x52, 0x08, //Value: 21000
		0x4a, //Tag: Reserved (0x4a)
		0x00, 0x00, //Name length: 0
		0x00, 0x0b, //Value length; 11
		0x79, 0x2d, 0x64, 0x69, 0x6d, 0x65, 0x6e, 0x73, 0x69, 0x6f, 0x6e, //Value: y-dimension
		0x21, //Tag: Integer
		0x00, 0x00, //Name length: 0
		0x00, 0x04, //Value length: 4
		0x00, 0x00, 0x74, 0x03, //Value: 29699
		0x37, //Tag: Reserved (0x37)
		0x00, 0x00, //Name length: 0
		0x00, 0x00, //Value length: 0
		//Value: 
		0x4a, //Tag: Reserved (0x4a)
		0x00, 0x00, //Name length: 0
		0x00, 0x13, //Value length: 19
		0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x62, 0x6f, 0x74, 0x74, 0x6f, 0x6d, 0x2d, 0x6d, 0x61, 0x72, 0x67, 0x69, 0x6e, //Value: media-bottom-margin
		0x21, //Tag: Integer
		0x00, 0x00, //Name length: 0
		0x00, 0x04, //Value length: 4
		0x00, 0x00, 0x01, 0x2b, //Value: 299
		0x4a, //Tag: Reserved (0x4a)
		0x00, 0x00, //Name length: 0
		0x00, 0x11, //Value length: 17
		0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x6c, 0x65, 0x66, 0x74, 0x2d, 0x6d, 0x61, 0x72, 0x67, 0x69, 0x6e, //Value: media-left-margin
		0x21, //Tag: Integer
		0x00, 0x00, //Name length:0
		0x00, 0x04, //Value length: 4
		0x00, 0x00, 0x01, 0x3d, //Value: 317
		0x4a, //Tag: Reserved (0x4a)
		0x00, 0x00, //Name length: 0
		0x00, 0x12, //Value length: 18
		0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x72, 0x69, 0x67, 0x68, 0x74, 0x2d, 0x6d, 0x61, 0x72, 0x67, 0x69, 0x6e, //Value: media-right-margin
		0x21, //Tag: Integer
		0x00, 0x00, //Name length: 0
		0x00, 0x04, //Value length: 4
		0x00, 0x00, 0x01, 0x3d, //Value: 317
		0x4a, //Tag: Reserved (0x4a)
		0x00, 0x00, //Name length: 0
		0x00, 0x10, //Value length: 16
		0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x74, 0x6f, 0x70, 0x2d, 0x6d, 0x61, 0x72, 0x67, 0x69, 0x6e,//Value: media-top-margin
		0x21, //Tag: Integer
		0x00, 0x00, //Name length: 0
		0x00, 0x04, //Value length: 4
		0x00, 0x00, 0x00, (byte)0xb6, //Value: 182
		0x37, //Tag: Reserved (0x37)
		0x00, 0x00, //Name length: 0
		0x00, 0x00, //Value length: 0
		//Value: 
		//print-quality: 4 
		0x23, //Tag: Enum
		0x00, 0x0d, //Name length: 13
		0x70, 0x72, 0x69, 0x6e, 0x74, 0x2d, 0x71, 0x75, 0x61, 0x6c, 0x69, 0x74, 0x79, //Name: print-quality
		0x00, 0x04,//Value length: 4
		0x00, 0x00, 0x00, 0x04, //Value: 4
		//sides: one-sided
		0x44, //Tag: Keyword
		0x00, 0x05, //Name length: 5
		0x73, 0x69, 0x64, 0x65, 0x73, //Name: sides
		0x00, 0x09, //Value length: 9
		0x6f, 0x6e, 0x65, 0x2d, 0x73, 0x69, 0x64, 0x65, 0x64, //Value: one-sided
		0x03 //End of attributes
		}; 
		
		validateJobByte = new byte[] {
			0x02, 0x00, //Version: 2.0
			0x00, 0x04, //Operation-id: Validate-Job
			0x00, 0x00, 0x00, 0x01, //Request ID: 1
			0x01, //operation-attributes-tag
			//attributes-charset: utf-8
			0x47, //Tag: Character set
			0x00, 0x12, //Name length: 18
			0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x75, 0x74, 0x65, 0x73, 0x2d, 0x63, 0x68, 0x61, 0x72, 0x73, 0x65, 0x74, //Name: attributes-charset
			0x00, 0x05, //Value length: 5
			0x75, 0x74, 0x66, 0x2d, 0x38, //Value: utf-8 
			//attributes-natural-language: zh-tw
			0x48, //Tag: Natural language
			0x00, 0x1b, //Name length: 27
			0x61, 0x74, 0x74, 0x72, 0x69, 0x62, 0x75, 0x74, 0x65, 0x73, 0x2d, 0x6e, 0x61, 0x74, 0x75, 0x72, 0x61, 0x6c, 0x2d, 0x6c, 0x61, 0x6e, 0x67, 0x75, 0x61, 0x67, 0x65, //Name: attributes-natural-language
			0x00, 0x05, //Value length: 5
			0x7a, 0x68, 0x2d, 0x74, 0x77, //Value: zh-tw
			//printer-uri: ipp://myPrint3B. local.:631/USB1_LQ
			0x45, //Tag: URI
			0x00, 0x0b, //Name length: 11
			0x70, 0x72, 0x69, 0x6e, 0x74, 0x65, 0x72, 0x2d, 0x75, 0x72, 0x69, //Name: printer-uri
			0x00, 0x22, //Value length: 34
			0x69, 0x70, 0x70, 0x3a, 0x2f, 0x2f, 0x6d, 0x79, 0x50, 0x72, 0x69, 0x6e, 0x74, 0x33, 0x42, 0x2e, 0x6c, 0x6f, 0x63, 0x61, 0x6c, 0x2e, 0x3a, 0x36, 0x33, 0x31, 0x2f, 0x55, 0x53, 0x42, 0x31, 0x5f, 0x4c, 0x51, //Value: ipp://myPrint3B. local.:631/USB1_LQ
			//requesting-user-name: mobile
			0x42, //Tag: Name without language
			0x00, 0x14, //Name length: 20
			0x72, 0x65, 0x71, 0x75, 0x65, 0x73, 0x74, 0x69, 0x6e, 0x67, 0x2d, 0x75, 0x73, 0x65, 0x72, 0x2d, 0x6e, 0x61, 0x6d, 0x65, //Name: requesting-user-name
			0x00, 0x06, //Value length: 6
			0x6d, 0x6f, 0x62, 0x69, 0x6c, 0x65, //Value: mobile
			//document-format: image/jpeg  ********
			0x49, //Tag: MIME media type
			0x00, 0x0f, //Name length: 15
			0x64, 0x6f, 0x63, 0x75, 0x6d, 0x65, 0x6e, 0x74, 0x2d, 0x66, 0x6f, 0x72, 0x6d, 0x61, 0x74, //Name: document-format
//			0x00, 0x0a, //Value length: 10  **********
			0x00, 0x0f, //Value length: 15
//			0x69, 0x6d, 0x61, 0x67, 0x65, 0x2f, 0x6a, 0x70, 0x65, 0x67, //Value: image/jpeg  *******
			0x61, 0x70, 0x70, 0x6c, 0x69, 0x63, 0x61, 0x74, 0x69, 0x6f, 0x6e, 0x2f, 0x70, 0x64, 0x66, //Value: application/pdf
			0x02, //Job attributes
			//copies: 1
			0x21, //Tag: Integer
			0x00, 0x06, //Name length: 6
			0x63, 0x6f, 0x70, 0x69, 0x65, 0x73, //Name: copies
			0x00, 0x04, //Value length: 4
			0x00, 0x00, 0x00, 0x01, //Value: 1
			//media-col: 
			0x34, //Tag: Reserved (0x34)
			0x00, 0x09, //Name length: 9
			0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x63, 0x6f, 0x6c, //Name: media-col
			0x00, 0x00, //Value length: 0
			//Value: 
			0x4a, //Tag: Reserved (0x4a)
			0x00, 0x00, //Name length: 0; 
			0x00, 0x0a, //Value length: 10
			0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x73, 0x69, 0x7a, 0x65, //Value: media-size
			0x34, //Tag: Reserved(0x34)
			0x00, 0x00, //Name length: 0
			0x00, 0x00, //Value length: 0
			//Value:
			0x4a, //Tag: Reserved (0x4a)
			0x00, 0x00, //Name length: 0
			0x00, 0x0b, //Value length: 11
			0x78, 0x2d, 0x64, 0x69, 0x6d, 0x65, 0x6e, 0x73, 0x69, 0x6f, 0x6e, //Value: x-dimension
			0x21, //Tag: Integer
			0x00, 0x00, //Name lengh: 0
			0x00, 0x04, //Value length: 4
			0x00, 0x00, 0x52, 0x08, //Value: 21000
			0x4a, //Tag: Reserved (0x4a)
			0x00, 0x00, //Name length: 0
			0x00, 0x0b, //Value length; 11
			0x79, 0x2d, 0x64, 0x69, 0x6d, 0x65, 0x6e, 0x73, 0x69, 0x6f, 0x6e, //Value: y-dimension
			0x21, //Tag: Integer
			0x00, 0x00, //Name length: 0
			0x00, 0x04, //Value length: 4
			0x00, 0x00, 0x74, 0x03, //Value: 29699
			0x37, //Tag: Reserved (0x37)
			0x00, 0x00, //Name length: 0
			0x00, 0x00, //Value length: 0
			//Value: 
			0x4a, //Tag: Reserved (0x4a)
			0x00, 0x00, //Name length: 0
			0x00, 0x13, //Value length: 19
			0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x62, 0x6f, 0x74, 0x74, 0x6f, 0x6d, 0x2d, 0x6d, 0x61, 0x72, 0x67, 0x69, 0x6e, //Value: media-bottom-margin
			0x21, //Tag: Integer
			0x00, 0x00, //Name length: 0
			0x00, 0x04, //Value length: 4
			0x00, 0x00, 0x01, 0x2b, //Value: 299
			0x4a, //Tag: Reserved (0x4a)
			0x00, 0x00, //Name length: 0
			0x00, 0x11, //Value length: 17
			0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x6c, 0x65, 0x66, 0x74, 0x2d, 0x6d, 0x61, 0x72, 0x67, 0x69, 0x6e, //Value: media-left-margin
			0x21, //Tag: Integer
			0x00, 0x00, //Name length:0
			0x00, 0x04, //Value length: 4
			0x00, 0x00, 0x01, 0x3d, //Value: 317
			0x4a, //Tag: Reserved (0x4a)
			0x00, 0x00, //Name length: 0
			0x00, 0x12, //Value length: 18
			0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x72, 0x69, 0x67, 0x68, 0x74, 0x2d, 0x6d, 0x61, 0x72, 0x67, 0x69, 0x6e, //Value: media-right-margin
			0x21, //Tag: Integer
			0x00, 0x00, //Name length: 0
			0x00, 0x04, //Value length: 4
			0x00, 0x00, 0x01, 0x3d, //Value: 317
			0x4a, //Tag: Reserved (0x4a)
			0x00, 0x00, //Name length: 0
			0x00, 0x10, //Value length: 16
			0x6d, 0x65, 0x64, 0x69, 0x61, 0x2d, 0x74, 0x6f, 0x70, 0x2d, 0x6d, 0x61, 0x72, 0x67, 0x69, 0x6e,//Value: media-top-margin
			0x21, //Tag: Integer
			0x00, 0x00, //Name length: 0
			0x00, 0x04, //Value length: 4
			0x00, 0x00, 0x00, (byte)0xb6, //Value: 182
			0x37, //Tag: Reserved (0x37)
			0x00, 0x00, //Name length: 0
			0x00, 0x00, //Value length: 0
			//Value: 
			//print-quality: 4 
			0x23, //Tag: Enum
			0x00, 0x0d, //Name length: 13
			0x70, 0x72, 0x69, 0x6e, 0x74, 0x2d, 0x71, 0x75, 0x61, 0x6c, 0x69, 0x74, 0x79, //Name: print-quality
			0x00, 0x04,//Value length: 4
			0x00, 0x00, 0x00, 0x04, //Value: 4
			//sides: one-sided
			0x44, //Tag: Keyword
			0x00, 0x05, //Name length: 5
			0x73, 0x69, 0x64, 0x65, 0x73, //Name: sides
			0x00, 0x09, //Value length: 9
			0x6f, 0x6e, 0x65, 0x2d, 0x73, 0x69, 0x64, 0x65, 0x64, //Value: one-sided
			0x03 //End of attributes			
		};
		Thread threadGetPrinterAttri =new Thread(getPrinterAttributes); 
		threadGetPrinterAttri.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Thread threadValidateJob =new Thread(getJobAttributes); 
		threadValidateJob.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	    String inputString="255044462d312e330a25c4e5f2e5eba7f3a0d0c4c60a342030206f626a0a3c3c202f4c656e677468203520302052202f46696c746572202f466c6174654465636f6465203e3e0a73747265616d0a7801a5585b6f9c35107dff7e85b955bb91f0fa7ea98410d082ca538a16f280108a964d9bb2699a4d5afe3e676c8ffd6d36b4554994ae73321ecf9cb978dc1bf15cdc081b8477d27b93b030569ae883885e4bab9217fbad3813af852edffb17249d9497296827ae84f7b9c84f1ddb756c68dd1d9cb1132fc5c57fe8e9baa78fd5b33addee37db37776fcf77627f0977b40b42e1db1b618c1321d962ecb4b912ab67575a3cb986d7ab5fb6bbf3bbcb77db1fae77d7fbcbabedddfe7243fb95ccceab9ca0e18155f17ff83afcef768b99dd3180270f328bffb3dfc87f25134eb1d94c7d353051898e21c8e07d9c112d3af6c041200de1b977d011418ed851c2e62a1c6a2c2b3de63df44c859e14e82b1615b03239a954c80ea72669e0eb95d0d648a5a19ea1dd1caa94c0d2d9ce4e131173433428a53594919d5f8f5fe7b9077bbf5f23d855069fc108aba635a2bc5e1b64ebfa422c3efbfc8b2f9762fd4a3c5d23e84a42bafdc0f48f3b88f2e6f020ede8247178d2578fc639bd569c914ea96c44884a06a7d3848c3100a3b21da38ce998d352d9481933f60eace64df4263b4d79d356708a57706b6c6c8722240e55adb4024921cbe4743c38b4615472dd10de5b8b1521284541fc1daf0e0e85b5c9c678e069c30e0e1d5e0d8307469e323e317be4486774c80ee71ec286c3d009ce744ac67a4b89104ba5639589509da8ea8e19a022d768720eec0d56d02ea5f6c90ce30849d1c5c27136de61df0c514e07b199617025d89888942637214510c9ba91940b024a3e0c131881aa1e2cc64600875914406f1c4c5301a6a1489df6d4505c962122953b4686801f1df4c008014f7642c9cb94ac2d328440264ae75078e4549582bd4e1a5b7dea90953143272b8797563a1bf3cc0446c829369531728a3136bf64e5c944b757bdb1e6a93d0fd8883e311875989500d1ae9003f0244b97ac9f0e9059c0aad461c03aa6b3872b5d3b6251f2ff206284104fe576255b9b54a908ba71e7f693736727b8738f036c41aa72089a41f094337e6ad7f3eaf4fcee6ebb7f2d36b76275aac5ed86b6b3663ecd2474560a19164a3934216cbf2843000b23d0ad437c328d20a3125b74fd3f1a0bb1547ba5363e95c60ff23068bc3f4ca039e02b955b4663a8b02818c4ba8c3ff5cab4f5cafce024c567a059c7801b00b7db032b6a41d4d0224a2d26ba18318351b53216bd743e52af1e10a6b708c33c3214e346123162e6a18547e4438426062807d1c2a3d21d23553acb9cc24c8e1013e0778cac7c20c5024ae70ee50069b19b664894daa1130cd5190d242858d90d60049ad84e86a0aa3bc3d870184855de29a8066ca8d739f2cf60fa68ecd14dd4b118a50257547703c390a5a9eb02f11a0d1cf1c12813d1b280a86888858a4cc45fbbbf196bb79a32094350df698878784b48d53e906a43d1c552d961d003ddd4ec3ae6a556b8aae9c4aa3d13f319dda6dbd010d2d56d65a9ea63f588b1e13590aabdf3d06c802edc9427b52b20088e528bc92ca9c8588b3a8e9e21482dca3bdc8edae52922542a20db5a8491768c10959c8a8c91fb687106575f9723041326e90ab26a6f08644ade554d2403ba6b7256451d52dac384ae1a391d7cc8d330a02194d43515a93eaa54c995eacec086cb3890b4977cad54351388c6dac67b997015e23e53caba24fae25e1b9f4a1b37ad8d57ffc7e1147d6551707dd1dbf87bdf72dc818e5e04c694fe860f3052ce99f53737fa9b779eda099e1818de4d1950ca80eb04ba132e149bfb113fdd6af10277d18ffce2a269dbf71fb439bcd46a57a567888925ffbda8d3767d5e947fdb446feb44ffbb58bc5cd293c088c5dd12ea669f6ff80fb78fdb5f56079fd3e21f96982fe8ed2b16b289be6091eba345ffd36e39550bb62cc3bb377dd715436c653f928dfab66e9e165d6f17b9fd9315efffe2d5379bfda37ede65477f5b823e19c4e22d1b75d13cf99565fae2f6154337bc78ba395b0a1d65148bfddfdd90b325066530db8177cf9ad227bcb13bda91ef96e20fb1feb93eb54670bdc2c849ffa3c0216e0f2a24b5427f5c6fc42cd8ed51d582bdf02b655678b838a1fd6367c60b6ba6ded2c508f528b280777b68ea8f95f29b70fe227c8f1e64e284771f027ddf4e98c3af3f3654e8d5ccbae7ff02633a961a0a656e6473747265616d0a656e646f626a0a352030206f626a0a313534380a656e646f626a0a322030206f626a0a3c3c202f54797065202f50616765202f506172656e74203320302052202f5265736f7572636573203620302052202f436f6e74656e7473203420302052203e3e0a656e646f626a0a362030206f626a0a3c3c202f50726f63536574205b202f504446202f54657874202f496d61676542202f496d61676543202f496d61676549205d202f457874475374617465203c3c202f477331203237203020520a3e3e202f466f6e74203c3c202f54543320323620302052202f54543220313220302052203e3e202f584f626a656374203c3c202f496d31203720302052202f496d33203136203020520a2f496d32203920302052202f496d3420323120302052202f466d3120323320302052203e3e202f5061747465726e203c3c202f503120313320302052202f503220313820302052203e3e0a3e3e0a656e646f626a0a32332030206f626a0a3c3c202f4c656e67746820323420302052202f46696c746572202f466c6174654465636f6465202f54797065202f584f626a656374202f53756274797065202f466f726d202f466f726d547970650a31202f42426f78205b3534352037383420353533203738385d202f5265736f757263657320323520302052202f47726f7570203c3c202f53202f5472616e73706172656e6379202f43530a2f446576696365524742202f492074727565202f4b2066616c7365203e3e203e3e0a73747265616d0a7801658d390e80300c047b5eb1358515821dfb1ba9780147119092ff173808d1506c339ad1566454080bd41806465bb1e04220fed6f66e50d239b9a6c4628ad39991294f3da5281687f267702691823e176f5b70601b916fa2331a8e0a656e6473747265616d0a656e646f626a0a32342030206f626a0a39320a656e646f626a0a32352030206f626a0a3c3c202f50726f63536574205b202f504446205d203e3e0a656e646f626a0a31332030206f626a0a3c3c202f4c656e67746820313420302052202f46696c746572202f466c6174654465636f6465202f54797065202f5061747465726e202f5061747465726e547970652031202f5061696e74547970650a31202f54696c696e67547970652033202f42426f78205b30203020312032395d202f58537465702031202f5953746570203239202f4d6174726978205b302e383030313135362030203020302e39363536353638203439342e34363632203636392e383431375d0a2f5265736f757263657320313520302052203e3e0a73747265616d0a78012b5408542854d00f482d4a4e2d28294dcc5128ca040a182a1800a19125984ace55d0f7cc355570c9072a0e04006d2a0d5f0a656e6473747265616d0a656e646f626a0a31342030206f626a0a35310a656e646f626a0a31352030206f626a0a3c3c202f50726f63536574205b202f504446202f496d61676542202f496d61676543202f496d61676549205d202f584f626a656374203c3c202f496d3520323820302052203e3e203e3e0a656e646f626a0a31382030206f626a0a3c3c202f4c656e67746820313920302052202f46696c746572202f466c6174654465636f6465202f54797065202f5061747465726e202f5061747465726e547970652031202f5061696e74547970650a31202f54696c696e67547970652033202f42426f78205b30203020312032335d202f58537465702031202f5953746570203233202f4d6174726978205b302e383030313135362030203020302e39373430353337203439372e36363637203739372e303630315d0a2f5265736f757263657320323020302052203e3e0a73747265616d0a78012b5408542854d00f482d4a4e2d28294dcc5128ca040a182a1800a19131984ace55d0f7cc355370c9072a0e04006cba0d5a0a656e6473747265616d0a656e646f626a0a31392030206f626a0a35310a656e646f626a0a32302030206f626a0a3c3c202f50726f63536574205b202f504446202f496d61676542202f496d61676543202f496d61676549205d202f584f626a656374203c3c202f496d3620333020302052203e3e203e3e0a656e646f626a0a372030206f626a0a3c3c202f4c656e677468203820302052202f54797065202f584f626a656374202f53756274797065202f496d616765202f576964746820333636202f48656967687420313238202f496e746572706f6c6174650a74727565202f436f6c6f725370616365202f446576696365524742202f534d61736b20333220302052202f42697473506572436f6d706f6e656e742038202f46696c746572202f466c6174654465636f64650a3e3e0a73747265616d0a7801ed9d699715459ac70bd97129a0a82ab117a7a7cf1c4145141484a2286a67919d027740517677cf7c8c5ea67bf66e5b90e9977da665dff1ed746bb740ed0be07103a4f413e4fc9f88ccc8c8c8c8c8cc7bf3debab72a38ff73898cdc229f88fae5134f46465654d87fd602d602d602d602d602d602d602d602d602d602d602d6021958e08137877ffce6f08fde1e7ee02da6378767bf398c4caed96f0c33dd99fdc69dfb0ffaaa3978a7f6e0773507876bf70fd7be399c4139ec21ac05ac05caca020fbe39fc9377867ffcd6f04fde26fdd8d38fde1a26812a1e46249810496498d412493c1df8aef6c0773507eed4ec27bc58b0945573b085b51648670100e4a7effcf09377beff29c9c5884c1220253949e09f2824014cb86a0edcaede7fab7aff6d24d215d16e6d2d90ad053ed85e71f8058d3e78a1225edbb22d4bb91f0d0079f05d30e4fb079918460826dc21d1b825c97c1233496a80114fb3f6ddaeda7fabdccd68cb5f7616a8fc60fbd4ff79659ad09157a61dd9e9eaa39dd3840eef9c767887a2a9c80185ec3f660100e41fdefde11fdefd1ee218c1af9924142a615d1b844a84ee3f885089dabb099004fe89e790b06e8e8f11f0a47a1f746b16b4ff56a58da8d8c659140b1046fef8ca345984949dae04529020a4ec70c590421839623142f5f4f377877ff6de0f3f7b8f18a290448289172741c0e41d02486c0d230032fbc03022213cf4eaf76e1290043021edbd55b5e776edeeaf63cf6537b016c8d902c0c8943fbe0292c027f175e495a94776bafa68e754a1c33b810e5787764c3964bd1132fc83ff4c0cf9c7f7be0746b8c230615d1b044c86d1f1c9b9b2b063cd8161f60487054ce27c129724f04cf6922c4ff2b1bcddd760011f23e9496231c20dfbf3f787c110ae289220e89a2740c295488f83f7df161d1c045a459044f46e1492104ff6dcaadafbadedef84ed6973f2b1c0e43fbe32e58f3b5dfdcfce294247764e39b2c3d5473ba6081dde31e5f07668f2e1ed3636f24f60c8fbdffffc7d17235a9fa4100c916b5cf0242949c839b95905edb6f158d990369dbd05a610463c920886f004c30891e4d0588f8dc0150143648c0892785d9b1f1e7c3faf8e4cf2aaaddd8f8125c97c124112c693e4a7b05b5a0ba4b5c0e48f76306d9ffc11f91e011d7a7932d3c431fcb006fd141e15119d9a70d7e6c1f78ac41051b9d5fb6ed68027de5360feec46d3bb91483273df4db1bb4d580b646e81c947764c3eb2dd156022445479992bf39396cb01115c550022164590a468ae886234443f9293c46244b19e5dccdc02933eda31e9a3edae0e6f9f247468fba4432f73657ed2b2382010c17121e8a17823589b796435ad65aaf77dc71c121a4fc2c51fdc48bf372d46d25ad56e9f8305f418014f3c8c2091c361cb7d178111e17b8413238e116e648c6ec518922892a01f54ee7561cb5f1616d09344c2c818240930c2e3a8617a889c12c1086f63c0859624162365f137383a0a39f1a3974987833af4f2c4432fb9faf0a5d171a509af0288f01ec7b88357053d78026b4b0a23fcbac224b1184958e376b34c2c108311f0642c910488c0f0540c775760222f8e5488d55cdd0a492c46cce6b26b33b7c084432f05f4e14b133e7cd1d71f5e9cf08717333f69c91ef0a76fff205ec493e921d20fbe53ec07be096d2593c46224a1d1ec66195a60c2a197032292a8caf074a57c28bcfe2f3022270446f0da6fc9965f90c462a464eb6874172c162300cbe8b600bf3a3ec1089f1040c648209ddfeb780535a34712fba4a6a066b6078fb4c0f8432fb9faf0a5f11fbee8eb0f2f8ef714b9f3685981f008dedb15f38af050498021143c29d17e0daf0446128b91d1d222cbf03a0823c490488c8027657859e98acc275cc57c89324c64ff04e974472cfad6b653537493db13062ca032046e89e78a8844608751b7800988e46912c330410e3c965177ddf682ac05b2b4c05d7f7821461f3c9fe5f94aef583246445af64f30c373e995da96c85aa0b42c108b91bb463549f0b508fe0909c110254153c45b87a4b4daac2d4d295ae0ae0f5f7015764e3e781e1819dd24115fa241426188582cc111ada5d8926c99c6b605227d120f23a39824c22131f3646c37107bf5d602892c30ee83e735fafd73e324253a50196ee47e11ef2dff1b791aa4d8ae4d19d6ac2d72f12da062e4f7cfcb0ce1e9e297aa0867c4f720e8ab9afcf39a113001586cd7a60875614f310a2c30ee83177c114642fadde87c764318118ae6c928a8e2625ec2d0f3ada4e75a879e250d3edb42da461a80b6b60c76340d7434f57534e3b7b7a3a198654b7e2ee7c203ce270f3897663b17b91e702efe28a00bb31d570f60e3e4472ee896b8eb61a836e27b7c7c9470b0dddb65c4a7a8f99795d4ef2b499f4491bfd456bb3b7238848b91304078ceef9e1f5748920c6d6ba756f75c1b35bc6d6878acedb156d7bfb579b0a365706b335a5ddfe6e6a1f519b73a7cff0e23497c9848fe092ccf6be1476f47daada04da2bc0e3eb4bde1c6b6f6ebcfb65df334f46cdbd036a87590844a240d6c053d082050ff16a811eadbbca29f9471e5e66040e7c283ce27b5448f4bf7b35f240449a484cf100693f3b31de81cd385d9399c37ff5d0010e96bd49a6fc80a92a0b5f3ef3fe277f61b9acf3e9abfd4661efda87142248600238520c917cfb55d7bae3d61c3eba78647ad0e4dae6f5343264841d74698d4c013d026ff8a1ec54780fb01805c278ca03659856e6bbbb68d6e0a435ba11608f782c10e1267c8c0964688d72655e8e606d429537dcfc6fa4c2a37adc1c903217ad43a17ef8fd785fb1d45e7ef7784ceddef9cab0594d2962187edc9037987e6d211af7578832afda79001b784dd3745b34f4b123346507e3d49184038463224096e5ed4ea58c3636d0fadced8f0b634c9ad0e772edeeafa372defcacf45e10e89b02a4f04fc1366760027872a1e0bbbc099bcf12caa92d720552253cbb5ada4a1adcda48e2608dd99c12d8d1063c88a81cd5003aab27f13b41ceadbc855dfbbb1be77437dcffabaa219d0b950eb7c52430c11ba089e04856d649daf7514011d3a15f42af03903f18a7a8824be5b622009dabc8089e8e044f924b118a18bfdfd7301fdeeb98ab0b2308acb105ddbf31a1eb53da9e1f15647aeafdfeadc86976f939b7d30e093c848917992c5758fb6638021d7b7b6912b42de080748ab0f908ee6a12d50d3103aa4a406d2a686812d2bfa37ad1870c52a74e3f27e527ddf06ae65bd1b96f5aeaf23ad5bda55609e780c014682ba58e3b8aa762ed40418720139d5cef91a55e76a1c59676b1cae33d5ce99daccab5f9e6158810966d7a10fc8be2bf5d9113361121d1cb9a9874902a4f830f14225d5fb92dd4f0549c200a19c672bfefbd93cadf105b53de606fb6d8fdfb95a86d0f020dcbfa48687563748772edef078ab6b60ad6e79a8d52dcdcd1f66368c8409b7b6edda84ebfdc6d6b61bdb5a6f6c23869098074218e968be0e0f646b73781739a7773df343384336d4f77b0ce9dbb0ac6f0363c8faa5bdd0baa53deb96f43eb358de37c3b4f3c92ce7936a5797aa1d59c8bf50633e17f1e4dc2ce73ca8c2740e8b92ce563bb2ce549b8f966aed3fbe479f4d11d3810a92983ffe887e504292d41e4c060d6da1f500619e0918c2a5dd315926dadb8d67d1fcd4b6470d6fb3a9e1a1073de0deb6d8cdcb6d7868724aab7bba77f5d2646571b7627edd9dd96f40e4e0c99496d39624b255416c542253eb0ddc17482d44928ee66b7155291f0769b08255e2b2bef575104707a747cfda253d6b9f86ba9f81164343ebe72bbbe7b3e85ca8742ece722e49c2a2ab2ae74255f28333ffa4ca393f8ba822ebec2c47d1191cb932f991a3b6fcf9fbf45d7bf953299c247042a27691f379b8551b7415bd1b246a7286899e241e43f220096b7b74ff6277b140db8bbd79090b0c6cc29dab8e1a9edbeac8f5555bdd9ac5bdab9f12bbc426005e86110e13972761a45892084ba247f305ee085bb95a6e80211d10fc107229c566a912fdebeafad62d857ae17eac2511469e819e86bad72c164a55b9863210462ecd742e55a9ba988e21f2299c7355ced990800e55339dd379c10418913f262bdc9254b30ac339912384ec4e4a7f053249d0bbc911262a49820cc99524c0c8176088dbf6d00245db6b4adb1fa15b18e4b6baa551ad2ef9cd0bdfe90e9284238507a07c2fc59284ffc97cd9d1fa4547cb0d5fcdd7b7404dd08d8e26f9cf2a6dba772d2309c3482f7345184616f7c01b59b3c8d7ea455dedf9f67408239fcc24114c82ba30336dc9e5ed9d73339db3419d99e9c83a0d8c309dc811261c23619224f446e4d2ca308922090b9524f273e423fbf155d1970927023bc42f8015b85bc96def066f7b9b5363849f8c60b26e49f0fe45772e3439bfd5ad5e145f32b68520b08e27be8b82a86cc2038ee2cd863a1a1837383d5c805cdfdc747d73e38d4d190c02e95dbbb8f799a79916f7ace15ad4038cac7e2aa0954f7d964737877923339c4b9e2ece7084b2e87738e72a9db3337c9d99e1c83a3dc3114a7f3ac446f8d7ed4324c9715661c0c48d047a434ac45f041222e29a34d02a5a7f981b4ace7fa58eb8dea01b96a6eda5f5464419914093ebc3cd2bd4eaba57fbadae67552298c876433a822777661ffc4e2ec0184cd31d01d060ee87f8bdb6b9e9dae6c6a1cd8d5919a477cda25ef44fd710407a56434f41ddab9e94d5b5f2c9eef685b99d916164ba73c9d3c5e98e50fabfeba8323867a7bb3a33dd51747aba2329ea08da7c745e446c444ea07793aa5fa31c3c0949f0d5697c7b5ad9d1b4a870435e0443b84cfbabeb44931309afede57b0beb0346a8c9055a1dddb9fc5697a8b181ba0a4cf8a28a94314f926b9b1aaf736d7613c8b9b66905a4d67a7ecbbdab172118d2b30a7a12a20a5db950a86be5c2ae76526e30712edee75c44848409095ff7e5576a756fe7cc7dce994ad2e9a04e553ab24ea638efcfdef3a3ac82243c48a29e3ee5323aefc6decd1d0c92afdd9fe6662aa343a405435292048307fce6c71a216f7bd7b3687b7d145945930bb63adcade456d7be20d6a25a8cc8992e52c63649ae6f587e6d63c3b58d2b82424e36c38ce56a228cac164ec8c2ae550bbb562e10ea5cb9a0b3dd157822ef189b762e012311cace21e1c520800026a7433a759f23ebe47d4eb280091f3a2200c2136eac35d9f31ab37d667b0eb9dcf845ef86480225774b043d9050002216cd05f2d6c219beb691373fd102a9e165d8f6d0de98f7cb5bddc26e6a72c156d7164f126e2bd97ada3402b3de958db9ffbdaa446daababeb1be10e64055767b3705e6812ce86a774518697375a5f589e467772edced5cb8374ac98f937c4be7cc3dcee97b559dbad79175f25ee7e43d498ef9b377fda123e2610d4fe4106b0d9f1194e0374dd1fe7d8c2060c24992dc2d31008448b28d94ecdfd0fafa70c3633999b5bd5e7280c907965a9ddfe4d0f6d0e4aeb6ce37973760ae889e0e6c3ba649b2aeee1a6a13da10d47abc1a93596dcad584476fe8bc74b72f80badaa027843adb9ee86c7575b5f5f1ce9698fa15876524b9c7b9a0d5bd62b30c1314933975b773ea9e804ede0374a83a75b7f9bc6005868b2800e18bc847d4d4bc7bc2b568e7808989240712df4f85e3a1261843fe735b0594e01fee6243ebeb86d62fbb46628dd0fd5d86fc040748b449efca277bf89dcb6f75e126f7b8f9580a49c4a2b0a748988f335ad70eadad1b5ab78cebdaba654cf53c4199f9bdf164305a57eb13dd6d2424ba5a1f17ea247a90ae92e683249f35c4c3c4390787c4a04a4349f259c5480298783a79b7a3e8c4dd0e74729af3275319f0c1473184359cc8a784f2be185b255a3b12e26fc175485cb7e496bc4b645a0588e7877086242789d4fc443b7413ebd38d418d2aea50c3fc9ef68510ddb9da20c6906093eb42636b8e6966beb964d305d3dcbc512519c5f9744758b774689d0f93406dae5d56b86b47fd86eb143ce1186124990f9240579ae6998b41bec1f9a9cef9bb49619e9c9f66de3d9fb5cc2d99164f12c0e4f854c389c2f490730c3ba65d853f070113ff4f8333245507c72789e484c81849e893ac5dc29a1f5aa0d208b37148d077ee695bd0d34ee21861372fffce058624bc5bb9bc0da2c3b7a1c83f70276da58c82ed07508f6b97ba7291c2eb14bfc85f52d06bec69e3153abfabd555672b39215c575b1ebbda4cbadc1c479273c0c8b4489d33fd09e77f81cea9a9ce29c08409be87a213d31c4f51e742e785bfe12bd343a4b12a6ac71cf2d18be72409fc094824a94ed8c1e19110051dca625cf986d62e1e14cd4f4de4d5f6709fea6b5bd0bb12ae08c7c813ddeddc077ebc9b5a1d6f6f8f75c5f921f215043c37584ca0239490f71a23e9c1354b069f210d69b536dfb1a66633f6a2425b1e93d5d9f25867b3ababcdf3ae36cd8343025d6e78c47028e7ec54073089d464c3bef9af724e83218009d3c9a98eac13539d80f425c1c42362ae006d22ff428a23d4ee773b38813f048924350949a24023bcf81ff17192c1358b079f799ab7c0f0af2873aa445ffb4200a4b77d014418697b82e9f1ee56aef9dd68632df392749995f3d61cf0e2d292b9082f2192546714d7520a50b28bfdcf2c1a58b398e9e98135ae06d73ccd851c3c882f74e13b9be67536fbe2f400405c35cebbc2b522d22d611899e29c8bd619fddf6f8697e69c98ec9c9c423a11d4f1298eac6353b427a5777be953d4d1cab465a2e54706490e7c5773e0b6b6906a66181d22070ce152f751970756f1e6277ee5769868dca938229c109f1e1e40d0911100e96a99dfd59cce091107e789489284c092d486ca09ca767170f5a281d58b492e4fd4c408904430048946129164c5bccb2b1e8d32b37376920923204c11480287248c115045c608d2472769afe2276fffc027400bc3c4cdcf623c8938b57a0f95fe10f02780c1ae624b5342704324044044c2b47f055a57bf6881bc1d8aa6b87af160b211ec38038627312744f5437c8cc0ef85f7db1e135035169656d6ec8ff04924037ab194c48fc062cf5a0e1bf4af5cd4bf6a11dd1764893a5db5b82fef37e962cdd0d5309fdc1226f243183d18401ebdd2f8e865ae158f7ebee2d1ab0d0f6b8fe69c9deceadc6447ab2290847c92c9e499283a3ed991756cb2736c62f82a0449bc0915bf5712d97e1712a192004ca43f845c4822b8a126b686af54ce195845cd4f088b723b1c5c15ffbe7f6ffb53d49781dab8d09759d0d30a3d019137d2f2784f9a48885cbc70ba7aff2dd8478d9648d6935785771fad3920b9a8449e90eb91a78b401298b7b3e9519724418c2824f95c172a714e4f249f24ac737054bcfcc2930457e19c9ce89c9814d0f1498ea26393b46e09e68797bf4cad6084ad1aceb01d2254e29324f8879082242a37bc1e0de56f75652c74ffcaa7f8bd4c69875e6b8c24095e15ef6f5fd8d7fe24a4c7480b1b5a90b713a2149f1b877e99646e84d3e95e6252ce54568b03ab9e6455099e440ac02fc23575363e02985c851a5d5d697ce4ca0ad265263004babc5ce3933867266830a280a53824513082c5304688245a9fc49fd5997383ff8a2fcc624ec50c2b02f1402d49f85f4a75c261ae1a927800f9f7ad155cc642f7ad440b140255a476b8f2a981951a922012d2470246982b82b02a1ed0907858f589ded6c711c6379e36f79598de165d3f5f1e5260b730499099fb99ca6acffe950b23ebd1634b7148d2455d1b0d493846f0d48693e4f3fab961033b6727386726c6697c78c7cc739c13139de3411d9b88be8cafa31381915892087a2889ac86b9f20b7749a273485292c4a307f74304431291642160c2253545c19627451d794e0801c4c3083d9a91310286e41f0911678c4af81889400a77573893a30e32caf2cd95c86bb64824a150498c43f2f9f287ff5e1fe193c02d895171488290c8045fc726388a8e4e70a08f358551a0a15fccf43bb36192b87f02fb6f231a90a8a98b2e8c420f79d17820170b2bc1131f29a2592281c731fd6d3e3d3c8c3086b81881130215ca0909171f98d5c344068b970eef5ee41cf4b066eeb959b5f766157e25cddcfd6d9466ecfe26a0d7bf99e1ebeb19af43df54bef6a57c2118b7231c45af8ea8d6d003e5e299f22e854b5f6d7ce4ea0a5757563c7ca58174195afe3018c23032f7ef5a9fe4f404072a0192c0383e468014052358e424393a216c467cb24d7c6042c188c8cfb6834324d13924f833a94d4812991851e9f0a54a39d40215b99d17d63295556c11bd1812d1032a861322959792d57b6f57ef933a381e34f47819d10e4e4e18f9362d46609340254add4f992a482b962cd062e78a87394908238c248491442419ef9c967466bca353818aad1cd6397e97737cbcab63e31d5947c73b9e94bdb088799b656244a531615178dfdc7214920887047f11490f18450f9eff6f5b2b20e33f8e85403bd4d1031bb800f119321fee8af1d8855a59c348029808e919c208836d0a558eb8e3ba1891fc10f824337743066f24178ca020a89df84a4cf3527fdcc599d677267048fea6f749248cc84891d3a7341d0a5369725d47e8e0249119c2d31e46c013ede1a3e821f2f98727b4fba6cda4886b844392b46b83536a49c201227e8d2523bf82fb18497e5d3f646400225f47f5be5b927ca470b62860317c58593e66b6e91c3002c2e4e08df062cb951889946291e46a03756dcc0e49ee24397d57b6351575b448924818892289fb859ab7bc4fd568132040166e093c109924b24382461875756abe4c12818e40a243dd25b8cc9eb388ae0a4bc84871d1c1371831272458645a9a1520894c15a455b054ef4d16740a9f268f1c8a8d0465f6468c18419c44131b914be775368df7856291a4b361ae4b9288080982247f5ba67b7673fa2e2781e40b2f5c9a39247739c7423a7a9723495b00f75b906180b0efccba6b595abb7baa4c65ac2691847be3092324fc640168b0be8c9bd351f16f9e8cc5eaa127b6b282546191909eb6917742948bc0b360c9275148125adc7b2b059c95331565310146beae7ced6b4359bc4a0c575f20a7381d523cea0d045a43111238247fabd391e454229238170c96c86c957302b191188c381f473a480883c8c408a7dd899d93bb0dba2b43c3561d12376c987280b74a128f1e0223ff1ae3938024b2bc06e9b385d6269ee74a77ad85ca83a7910a26852a4716c70d746af0e0c67f52c3bd117a5ea33cac514edb8381c4813b026a30c0101e1eefcb6eb0b15200795190441b68e50ec9dfb53ec9c9710ed7a9718e4127e4b3152aed7c5ce11c1da7eae3714e5051a7073a382bccbf98db399fb12598f6599044eed724faaab85c74410c25018008c9db87d2bd2d8f01144c2046802a2e6190d9f25868bf91cfa8da737bd6de5b50229e94b65b1220890e232089d9e2bda1bad3df148af2b09e4822fa350dee935ff1f0971c9265733fd3fa248224e6c48971666b64b256439220430829474d25313344accd1926f8a88dc00812a25f83446a0bc80011e85012c683f6360b92709ee87f8b30decc584ccdcadadd5f7392847ff56c19896889a6dcba2c9f241118892749a01e236e0a702f9b6326b7d4952e751e2789d6217131326a48f2679371e06c80128218da04fbd804cd3192d63389ead754ef4bd9afe157a04043bb68bad68aaed679982aa4db754bf418e14e8bf13023b332cc10730ee03332058d3bab4b92688c4c37064970788c0cf47ccb984411422520898211df2159c61c129064a96e8ceb09bc3ae77570cc89c2874a549f24ec90fc799c732ca66ac3f4e0e8f07efd4fbfd11c2389632651de488e18c14568d1a1641aaf152db0bbf9315f4495a0dcb59153d3180f5fd89555bba977934a852d50ae4727921831124b12388d09ef0845188d2c48c247b4fa18e1fd1ac2c81cada91c224932153e54c24882508927844d14191d12718198b7d9e386ffb92bf5436f6f105230c02cf62bb4e48a48b111b953933a36228a8884020deda2bcbd2eed6344464a288d37b3747b8f705e2a8cf08d47b8c4bad3c762249624386a77f33cb72a03f7021086cbbb4134173ceae5922438369e075a294212d1b5e186494a92933a3b669a172089c2102cfe9994f01ffa38f733566801c22762f57f0f104fe0cc280767b307f8f1551e21f1a2acdfe53b684a8b0e3f734bc5bf6e51ca135eeca116284b7251a4fcd22409383c6b2f5732e7640f22b437c34618d91c8924fcb51af71700118a2d61c23b02362b7407e772c35ce5151b05238049d4e51049e09908292e8ac847e24f51c7c826df274918239c241fa73811c80056c824f1d121cd0f2f32f94b793507ef40b5987134f4668deb8db0d123190c72f0a1211ed6307a00204271970b44b86a9ed7159658cb1271071b81f5f4729c278f2a022f9a04068c6560f94c2fd423492446a6bff655ec097b5b1ff6ee08f2bd4093ee29b07b79b97e6e64bfa66eeea775faae0dbf40441e7c8cc8dc08a78fc79a24af0d4c2449e390884220a62aa68217c40827384302bfc1c1f0416f24fd631a512039412491a021d2bfdd5221246faf4b635ae6eea6477c9e04d1e1e53fdad5043d92c31cceba736699072c0892244dec292db78491c484912424814dbb5177ec5ee02145763529eddd291ecdb20242c7fa7cb94b12b822e48db0f088e8d7fcb5eea1d01e818ca4242970a8c48d90841d128611e77f03654ebe80b02ae80150280c09a0836de0e6446104330664f84e99400712021d81c4e624d7d8d508927056c4fc7646cfe59be44405da06a1d7a40cf1bc9799a5f450980d86f749227a342cf115309290241ef63d27537f53a0b59dcd0584c9e7f5fed4014abfe6d3a50fc5de8c9293c429a45ba2c65705523849f2eb5b31ff449aeb4c468748eb18228758b374ad03d09061b2b9e2b79e12fcf5627adece464cbf4992914239c877f5084dac07e6e866e04c7092c26e929624d87e66c9044c649268319290243031fc46b9063db604aa9536682c14493e6b78483cac5131523727d621e1ad24394c0ad4aaa800021d72223f87445b5a782988a6d61ca0be8f90f61b0a5e7cd57db3467bb41c337d9278dc100011896487ee6c044c5c5698135d8d0fc7de56929d33e3adf08a9cf2d25cec2276c9b810391d4e90240a23d377c5c749f899193a42dc08de20386a3a1b0a02134cabc83b35618c247148f855c493e4386622729593c96376a2ae8d0c109ee618c16f7e0e49ccb9d96ae5bd3c39beeabea097db08b4a8730b5ce8139b2a7ebb296a57251f9e861920cada128409054c82afdf26592c059870921830929c24a856cf87f49cc98063c933ddba2e4425e2851a4e127f38eb328ab2428647364a6bc4a200454c0211da02fc5d9b3092e6914df8ba12e6f0b0aa125c0d4c9791c39078c3b9350061f400407ee3c9b07b7095c20af32266d82b443b0c9628f5129ef056e5e099bc3e02d30ec8d71664089efcbab1114aec72256f6f4e77353c04bfd15c7d7c2d2ad17ca8b46b3faf7f288c11bc62038cfc75694ca0553997eb96788e87892771034d9523c72ed2589168872476f74c36303184c55a3173602627720f4224f1d021d34360e4371b939f2eb605d2fc9cd2149d9883a2046152f91a9f978c7eb53e09f225b9d397cdd8fd6d724365bea544128921124652f924285e6753520f33dba8177748646f8463841c92f433ec99e8a11026d3e738068c14c2ffd136273724e24d36227b236c069e5b78173ecb88ab4f0ccf03a19c8d01690b1a9189d0ab8b0b0e8d103af8fc9c81df888fa9459ca118d90c2691331c1a263fac1ca117733c92446264faaec0fccf498c98c427c1361946bd5c8c88376bd85856ead7e0798dee2d9b245741c34b14686817b31ba8a671484478246be7c76001191d22edcde2e5beff8ef9be0c4748b7ca2549101d01926c487740dccebc493803b8d065bad3ebad987ba52172c862dab363fbffab7be82f4bf23d60ce3099f9fa0838278c24268ce44012f800b130114e660e0e8352b3bc5f834123eeb8118191ba39a85065e3e48bf0014c24016764e51d303161a428e1116119410f9e501822de76c7dc1ad978260168089e6ca8f8cd868a7ff1240a972c8146656088470f77ce703e0d057d8360f95c3cfe4b7686c8add095fe74c99cbf2418721079086945e5f66183fb61583563f7cd223b27dac0881721f91218c98124b00455a5ec5b8ab4eebe900f4c3eab7be86ff573b418491b1e912ad04d124c645c98d379c0c4d4a949fc8a4db8fcb9e5001d4186601c9a3a150f7f8fac6acfad0c5e727749124487600825d6e77021685457e069f04f0c28bf6c061b1f20fceb03deef95e573a3be1c6d28064ef7d72573ffb21441396024970eb5e1e0088018a06158851dc122c391335ce593c48baf2a18c98d242821c144c70d7d664e1d557823ca2856ead1b04e4d3ede886cde7430c9a90f523ade08bf70cc0fe0f9211a86802a817757f76064667e01d800343c2704f490f5db5c600220000b82184ac29d7dc203085ed7126f6cf15149185410db35c6060008ee590088ab25b977a8e586a7a4ab5ecf1d26c493c2074f5c924463246792c0148089b9c72add32d27554e185922b121c0cef61644e6c0350aac9bc980e2647cd070baca523473fa9813b3452ffa2a63d0f30c49f55036f9fe5da3107225c9204d12130f2eb7515a4b5b999823c130613951b1100515edaa28149f573280a574753ed71618823bb5bcdf9eb120920ae37123f8e3ab70bc15ef02e123a27d8cc9fc14cfa165e6501e22795db8730cfb30e236e8f86f76bf86fced7ce77ec645f8e802b2271837551d997ade43b05dd14e29c133084d52f4545c28111dc1dd078f22cb076771a3066eedd602d9f51047088830031243c024dc4578b32024d7b993c13431a941e4d0443e09fb82fb1cedc9bd3dba9bfd601c4a5076788f7fb8bd586021b5671980448c2dc0fd9030903441e51106e662089ef84086fa4606d4fbebaaadd5f8bee0c2306a0a1e78616267c3e22fcede7e3a560dfcad70dc1558c1ed160241f9f4458801eccf14e6bb08bca3112a865dc2c96d3cc00a84a36d908e6407b04c1100aabd2c0b339068ca41a7e26ca963c4124c1e31b85279c1eda5f6ca9b81c82154a3e16a555c98b54b82d054c0c0c1118e12f8c608443ba79337eb19ef91b1e2b3400594bde88d02fd754fc7a4dc52fa0d454b9bc0caf76faef890b74f004e786f815be6e5286c039c9fb494daaaac4df722428242724b04d6042336f7eb3d7be9d01aabcfe15c141ab57d95a6c03c763d7379a0734c111232c36a2c748daf1240683802740849e1edcdb0cf655c3b52caa98e62c12cf68d04bcde3198da1c0da554412fce16bd161c80ca343cee118c9f5255f6d3973c8acda7f0ba24f27eca5e8c7ac3dc19088df9df15d11ffa5333ec63b394c223122a18333e4576b2b7ef54c506b2a7e05adae005b125305ce890c13c10d39211a98cc103130e953dffdf07d123ca9f96c71be4f7c72a82cec12cf132d3d0299fedbbbe2c519ddb8773e5b51d00f49cc90ca5d5f99bf4f91dbe5a3ff425e074707630bf012eb6a6aabf8d33a44b70ad59d315f1df1c4c00d79954c8c701a0cf95fe690fcc97cc2ecd732afe3267dea9aa1437467a29d100e169a78c707084fcbaf8a6058666c7f1c7ffec2d3d02622e9c1180280fc1218610291d2fce3bd63eedc728650d3f2026e00889e21baee0c189255603fcd15a8db329eb00e4e00119ed7a1cf8c040830e28d315312c93142bd1ba2c7ab5fa8652dc032f74f50958a9f8945f91e81b45ccb9e2b3287625fb90e39cbf06a4c3c094343c9e17e485c4425c3d2e250f45984fdb728a01afac64a1c40c80f896148e0dbd3d12fa82a18216884bd0ee18470f72304909c30228c49fe093df873c70f0400227c5df739a0ef7ef0d8083deac52d6c84fc1071094a02f1587440bc89cba2306202481e0ca1d76a2a5ffdb2f255ea2529052bda22ea147d58468c39f432aff7169ec4100a9e7b91f391f14062ade13f8509c7463840c00d24e0abf0c5e27a200008d0117e2293801ed17e08bc11d915218c049e542236a8b11b30421d13018a70424207b624f743f2405c576455c52f5756a4f4463485615988c2b9ad6b299a191b45203364098d3483d8939a920388f6a2c84b01551003218724861e2900c2bb33af7adc207a7c8d6737da32d8cc5166010a7778ee47426e545190e4164de115147d6f5af46b820c0152148cf0458cf71e65f62cbbcb01553cb070bcb05f86174d2f6697e8d1505f06c10d1227868546d9d57d460506430004333d88187b30fc2ce933178c1b51fc10c6103d465c98146b8c654666b387b116b016702d40c1103343d8c0d4744f6c3deb3247c5edd7b017db033d9ab0673243dbc7f18e66ffb716b016284d0b10430c4f6f8921f9ce098017f7d0d3094343c91123a646300a579a75644b652d50ca16e013f7b1272c919d9a6cdede6556c0dba60a3ae4c5c098a8119d81a794abcc96cd5aa0d42cc0fd043f222a42a35e22b7be8cf932a37a37418cd013c9a2bd916a2eb05d6b2d602d60b6005e7b3160a470df325066e35119e27d75ba10ef8e990d62d75a0b580ba4b5004d201c4d92c261849753c044c5883cbaf2b56fd25e94ddde5ac05aa09816a08729a1d11d2267e6eeeca6498cbe2ac0c4841120e535dd28b5e803da35d602d60245b68080863651b4e72634fc89bf852abb22fe184beb9314b95dd8d3590ba4b080f854811623c57148447189247a8cd0806db1994d580b580b949a05e407afba745107ab935b122049e08d8f52339d2d8fb580b500b7800875ea1842434fb141916d45a3d1fc1e8d4f12bce251e492d8d3590b580b24b400fe6ca318c2f3131e27c3cdd88bed3e40e4f74c333c8b3d94b580b5408616308f32054c323c57c243b10e8e4f12e96d531b714d6842bb99b540b12d60764846842430019f104362087b517dd757c5b68e3d9fb580b540320bc492a4688f80e5f2aa0cc1f43898e9c29244b6914d5b0b949205d4c160349c837fb3c08d9f8cc818f52049dcc93f0b3121702955852d8bb540195b404712c04456b14325988ecf23893481b07548cab895d9a28f7e0b88094082f4904952ec9770d18b713fc7c63a3594c6dc7df611f0e86f8cf60acbd802dee00d696469c021e14829ea408e3046a6efb2930697711bb3451f0b1648f05502363748b1e63f645d1bcf27f1be346d63ad63a129da6b2c6b0bd027a703a3d325e724985f848986f0b502fa9223ff90818791e9afda87bf65ddc46ce1c78a0530df321fbf21fdea7952e8ef92a80c2198e063b25f8e959ab0d7692d50ce16001fb4efb9486011a841f4b5501f3982ef410e4940f4a9947236ad2dbbb5c0d8b2003e34afe38600889cf826f3b16ad4a95131429faac7b71dc75635d8abb516287f0b784338646844a6b171563ca1106b0023c410aef237aabd026b81316701720c5efb46f02461229f611ef896b4d297110cb19d9a31d7feec058f220b104c76a58609310723c7d8875f638d8153b800f131e23b218c24f8e4f417b1c7b11b580b580b94b805e00fa863c3c458d32409844cbdcd10e88024c78398234961082d0235256e1f5b3c6b016b818416080c0ff3b020f8109308b042e686486b00329d98635d9184f56337b31628270b200642fe839924f1dce000d1d1c38dace29d1afb8ca69c1a862dabb5400e167079929418c2f1880708f5652c4372a812bb8bb540d95a00cf7c3da428b10e191d06c743ac4240d502a46cdb812db8b540761620aabc4a40881a07e23dcc75dd128e0eeb7e645703f648d602d602d602d602d602d602d602d602d602d602d602656c81ff072afa548e0a656e6473747265616d0a656e646f626a0a382030206f626a0a31313335310a656e646f626a0a31362030206f626a0a3c3c202f4c656e67746820313720302052202f54797065202f584f626a656374202f53756274797065202f496d616765202f5769647468203134202f486569676874203133202f496e746572706f6c6174650a74727565202f436f6c6f725370616365202f446576696365524742202f534d61736b20333420302052202f42697473506572436f6d706f6e656e742038202f46696c746572202f466c6174654465636f64650a3e3e0a73747265616d0a7801958e410a00200804fdffa7cb3264d805a10e91b3a31671ceb273b15e663d201eb58a9c08e784961d92700279b593cc66fa9487ed9cdc9ab4e72846fee6ae5f79ee95544afe44222f5bce6803da54f50b0a656e6473747265616d0a656e646f626a0a31372030206f626a0a38320a656e646f626a0a33302030206f626a0a3c3c202f4c656e67746820333120302052202f54797065202f584f626a656374202f53756274797065202f496d616765202f57696474682031202f486569676874203233202f496e746572706f6c6174650a74727565202f436f6c6f725370616365202f446576696365524742202f534d61736b20333620302052202f42697473506572436f6d706f6e656e742038202f46696c746572202f466c6174654465636f64650a3e3e0a73747265616d0a78011dc1c71100200c00a0fd77b5c596af2790f95d0e9bc52422069d46a5f000fa00411a0a656e6473747265616d0a656e646f626a0a33312030206f626a0a33360a656e646f626a0a32382030206f626a0a3c3c202f4c656e67746820323920302052202f54797065202f584f626a656374202f53756274797065202f496d616765202f57696474682031202f486569676874203239202f496e746572706f6c6174650a74727565202f436f6c6f725370616365202f446576696365524742202f534d61736b20333820302052202f42697473506572436f6d706f6e656e742038202f46696c746572202f466c6174654465636f64650a3e3e0a73747265616d0a78011dc2891180200c04c0f20890be0051ac14c5ff9ff1c8ce72ff71fd727ad0a6db7678c9d3b487dc4dc44dc75537b8208599c2240bf9a2fc58bb41b98c3fefd333d50a656e6473747265616d0a656e646f626a0a32392030206f626a0a36370a656e646f626a0a392030206f626a0a3c3c202f4c656e67746820313020302052202f54797065202f584f626a656374202f53756274797065202f496d616765202f5769647468203530202f486569676874203439202f496e746572706f6c6174650a74727565202f436f6c6f725370616365202f446576696365524742202f534d61736b20343020302052202f42697473506572436f6d706f6e656e742038202f46696c746572202f466c6174654465636f64650a3e3e0a73747265616d0a7801edd03101000000c2a0f54f6d0c1f884061c0800103060c183060c0800103060c183060c0800103060c183060c08081e7c0001cb600010a656e6473747265616d0a656e646f626a0a31302030206f626a0a35360a656e646f626a0a32312030206f626a0a3c3c202f4c656e67746820323220302052202f54797065202f584f626a656374202f53756274797065202f496d616765202f5769647468203234202f486569676874203234202f496e746572706f6c6174650a74727565202f436f6c6f725370616365202f446576696365524742202f42697473506572436f6d706f6e656e742038202f46696c746572202f466c6174654465636f6465203e3e0a73747265616d0a7801d594e94ec24014855f1897a88820b800b243d9053128a20129282a14acb20a165182424005894b62a2c6ed11bc0633690a3463fc65d234b73373be9edea59648c7f2af2e7da0f117c3eab5ea9499162842d3e6b840418eeb6373ee2241b67ec59c711dcdda935be956a5f509d7d9f5077df2b0143e95d8d2ba8d3a264abb5e935aa862fda50b61df03c906a03039626b2a4437d97214176acf23aab039dcc6418d6aa2f9f327a4e5048a455ae3bfc0e14c18e2c9d23d478e1e8dbebc72e50c8733694aeee53b48c80920ff98a9161a13f1e22d47de7d84c2c99d341402c78fd04851c7777d39b0a8f36654ab151c8e7cb9640f30833842fd8e69f30a87036744c47e34dbee453982a5697b061302c794de32e12f7038901c091133049af81c4be4664849a62b8f6cd4af9a19bd6bdec3c084220ed3781d5e0863561c41a0b2301d2a4f0a7120905929892d855974b5af2a32d32081118384b039a757efc1fd2698145b0f7946033cc32c43bb6e67fa548a0d240f2ee18700867bbdc99c39680cc824fb3c7f0c86c19bd49125c89ff1ff8efd8552f38d5fd8bb0b1fee26cbdd8e825651ba0e38a9e895f0ac185673505cf84b249881d3c423475bd06663dadd2f4de5012a0a656e6473747265616d0a656e646f626a0a32322030206f626a0a3435340a656e646f626a0a33322030206f626a0a3c3c202f4c656e67746820333320302052202f54797065202f584f626a656374202f53756274797065202f496d616765202f576964746820333636202f48656967687420313238202f436f6c6f7253706163650a2f44657669636547726179202f496e746572706f6c6174652074727565202f42697473506572436f6d706f6e656e742038202f46696c746572202f466c6174654465636f6465203e3e0a73747265616d0a7801ed5dedc1b23a0c750446600447600447600447e8068ec0088ed01118811118815bfa919c94162ae873d5577f48489393f4500296aaa7d3eff563e0c7c08f813f64e07c5137adfbc9bfb4d69dba36f51f66f08f84aadb1bb11cd8a6adeeda1fe54f1b08e715a603e5c3edf2b478ef045435cbd70b8756ad86c0e8c676ec9a77e2e929b95489337a3c3f053a01d27411c5a3a9d6e6954862361cae5502e473557f4a76a391ebfed6e2516d2e4a343bd3b1ab3f97dc38f3bf24bbbe03d73a7d21bc74231839f16bf8fe4bb215d0b846603b80a11547f51df524512e5f54b3cf106a8debf9e45bf23d34f149f9c1fb3898da97f40306b6c6629d0906e63eb75bc6f203d5c87601170ff7b0828a7d2df2c653c166d716b97d8411b2fd82846bae22c575aaea30a9e98bc83e61c79ecff67924fcfe81cb5d4b5ed35791fd5ab677927d3a357494be69649f5ecaf66eb24fa7b31fdddf45f62bd906b28707ca88ab678eed2f23fb856cc3c7a7e20ba4637a7eb76c7f1bd92f645bfb6260363b589b9d77b899e3f4ce2fa6649a9e9aa762e4fb0ee0af24fb6563bb61b2c7878bb6393adf38b25f764f520dcc76d947c868fc7f6119b1638869893a7c64f7c6a8c32e9c7697d7bb3b312bcfacdbe1767946ff4edef61dd7d7b0ad1976dfd0ded799b7f7625a9e38b6e11239edaada6f4fdbce045fc2360ced69cf0dc9cebebcbfdb2bd8c6a1bde75efbfd59db9be12bd8be0368bb37b1aff403629ef559b246cc5f21c16183cca0fe807c05ccfe004eb9ebb9555a0f36acd64a2c55290439845035aacb2d429a931aeb90854dd1bf055d7adba8bbb693fc83beab6665ccf680a9d2584fd456ed721dcad05d1e88700c617b6523cc8002336b95e472b74483759f5b39260ac923bd7e8020323d2fa976390ea50b510e2154cb151940911381ec1336521f22a15231d5ce2bbd38040bc9544750cfdd95cbddb02b461e5541b06308d7342f221124bb84ed0cd7b90e690c56d0e1dd267517228df7ebbc58375e18beb9f0e718022dc570d11556d090d924c8de66bb19bca759566d7a74b9857dab4e3c49f7e676a37753b9ed48034b43b93a77187e5a5ff8730c219cc37c868bd58ec9f9214c2ed543e50d7443ade2ec93c7ced8e084d4f43ab6695550cf89d90c69c4d9c413a321f4e32042e7881945785e2b605aaf21126f9d8f7b676d902aed5a46183ea68d0685698de96e9d877b5701e8d95b5a15948870c30cfa7326f44184ce05897b0f0f63537463668bbc82ef62849ce1f210358693c10227b85804d9a3a0f86dca5b1c70bed915a607113cd9931c852642a0cc767f91dd1adbc133e273ce9a9235fe722a4423a29a6d9fffa2e86d1a5bd09dc81ef2df87106a76a252e253ab293eb1909b38754f5c72310876481ce03f609b3aa4e28cc37e87dd4a7c9e3d8840d7a698cd393e359a1c62ea30ad90abdf86fad7447ab7abd9734003a831d3a4b0e56972ef4327680c318289b55cde9984e69d08a1ef6977aca51101cc59fc34a1f14d5de881dc86e6d9aa8526047c0ddbb710a281b09188036c9a62c38308745eab28aadf1d4282384562dbb821629b4eb63a0d79024c1cdc08f812b6e930278a26a74a84cef9607ec6e42802f5bce1782811be09adb0e13427135ea241796dfa6c31a63ab8992dd42fd0c6b104feee9d214468d720aa3198cddbab301d42d33e0426b316b0bcd387006670b3d648ac97639bb2958982af06d71beb413b4d992ac4d68f4b61144c1bcfe0d8ce64345610885b500b065e643b89d05117973e4ed3924554c5403fa133853aa316643a1c0601278010f0059f2539ec6a213137beb9c1cd0d3b111818e890229bc85282e4a007d9a312649a3e89bfb58b8053b60c01d263228d02d98d044887990c6c7014012ec00c1a49378e2d0e29ab4525a173411807ccea3a905fd704addb52831564db13f63870bb8106ac98541ab23e8a70b51db36f84190b0ddb8801c76ac1f63de8136cc317d6135f46d7c1d36e7375284eaf74ffc2e8cd964fcfb67005398c00e3b6cea630726cb461ad609bac63b6ab963b110f6b8bab1171398f80b177c81da3375bee3008e126f030822e49e1ce4698266b916d3e1324dbf058695035e290dc21e2c6043339150b340ab03664bc6b914938cb0e236886bd66229f4e8a8dd086b5c8360f0b641b86f5fd822028431c832daa169aed93cf906eb309318075b8e53e8ea019b5cba6c0c375421b7645b63b5213dbf58d46456e585bdc0bb95aa1c26087651e050563fbc4bd30a9f88f04c711b4ed977d1bb21de2834a04ceb6ec8a6c33a2376e41d36663cc0d1cc74267cf8155905c636731dd5b9333227d0bd6e1d6ff388206d49a62c5021915b03d90f1646060588fb77c001f905d67a98bd338b4af01bcd944aac13acc951c47800b60284f894c28b4c246d21a81f5a0ad2e0caf5b36c94a1a9ce37982ac936d084f2f0480df718fa4a898196db30e36b70a20677e1c41016afeb244468d8bebde496b04d68396d22b18d616e006ce462c2f25db640bfa14a79b9334a6e28c50b30fa14188732e74301a8541d0ce5b6e40ad93fbb6f47a7791ce77465d970ac87e94ed0e5371d151a3d6139a5b130822895ca1a4ab978c81d139386a8d3c76d963c83e2445ce3535ac0a25648b8eea5538dba83015678e9a7d08f208d4e92cc29013938fb2b4b123e634950f6b077017de9bb3473e6a2fbd708f9711a076e0747352e8b3757346c711f861c48c9539753b1f47c9d4bcd66eb805b5e585d7fbb7e81dcd2d73880d09315ab245edc6fcf6ecd380bd3f38a0d99a21cf20c8e728e9eb5298d58dafa2189d3a75c281766575a13422e8ce073808c1656c4075b3990eb2edcbc671047910e5838a90912f617c52fa064c3fd8caa3973957d878217508ba7370230407d0a856accf48c876e76c8e23181c512b13eb55ce7ebc2dca02a6cf39df505db3be4caad1dbc8aacc4d5821043788bce2d394cd82846cfb53f43882010f85c225b9a03b5cf0db90076dd3bd6a51fd7829d1e86ee49ac2150b88c04e22afedc28d6cfb72741c61ce866ef06c9a7dcd19ce8dbdd58eadd0da9d74af045ac1c53fc2c55ece017ccd8cac5677d379d5a816cb58926090c7e80d8e2358a0562432c2800c5f2d58d4ecd90fbd7c42f366447d0b0d65a246772343366500b9bc0604debca2346cedcbf60997c164efdf38c714826dbd0886cc2f2abb73a70933a5d178f7889c0f7e969477f08f0f4d716e9810c903cd7d4a4899bc44d9dd2c2570bf7d09318e2338245f2f204ffc71ebae0af1c416ac27686851bf7dca82a713659fcc538574f4851f29303e29e38279859694a80865a0663910f62078a88ad1294c10e84ca2b04e08edf3169ae455777894ab13ad6b0bf8b9f8105388c12fca4b7c1208b3a8c211773a4251acee4969043e0a6c8052122118d4b92fb4654b1c860e28f396c3cc16376c2a92a1e0b9105ba32842754eee1d9be44977c1a6a53c04149cae388ec081aa76f1cd461312a3b1ed2c857ce62db6c813ae643219bd8d1cd79207cb512e2f7995d35150b95b1388c28681d446d8858068a7dafc32be796946ed443beeb08d645b2caa5c3b5c08266471c6ce615ad1bcb193cdeb81a1790d2072b01d4748e5de85606b1d659b88ed065b765ce696a5fb21ba31baec9cc6a6d52bca102ca382731c416664f7607045e1c03824346f416dc43b36eda13bcc1630ce03b59b9de2bcea11db944c1af7680cc717ade30818c6cb9054fe2a074611db32a7e7d09daf6871fef9bc4e5420ac4d137bd2fee03196e3ff38020521011296858b2c8c004611db271a1bdea63fa36389bc1cdde98f5809ac95bce4fd52b66b370f91fa64d521fc2e8445ca88189f4c648c46a4f482c8c918e2744064dba46bd592ee150c01b99657986373367d251cc30e8d952668607b1c01c09c28ea5be6a3e4dad896dfb4b45dd3f522caac6875a6cb098829831101afb11da1266337a1f76d04ec760be8de408861ef98f1943987d12606887a654d97cf83e7c5dccbd248581d467072eec8938f11d00bf54e8ec85a96b830b2e5d7d001e8380280cd6288e8138fbeabee8db157cba4cf3db63bb9f7d35d16e07c9d8f69aa347afcd3e91246194315fc27161bc7f7241658fe93455c9ee8c714866597425ec7110292df0e98b29155d46e76c547c676d92e8740c01bcd7f6f9a57e77ef9686bca897e2c21b8cfdbfc22599384f9ed21b45da66534f2be6268d9a826e77bbaa47bd3e3081cd34882ca397dbd882ec67f6a88ca21801c90bcdea73923f8920e794de33df97f65f565f11794a253b473d68c65a4d1fcd895fc4598e142b669e13882c0155cceb9c53771d13dc3d85d9bc5b997a804d8cdf82c1609d04e3ba00fc9a336ff4f1b5e66bae1aea989859150222103ea3cf17b6e911fef1e47f0584da354789000990b3223b2bdd9a85bcec748abc3bbec0ec3a0ac768c138ca53b5e25445af30eacdf978ec52b8e0e239cdb9b2fa83203bb87d7ca34d9894f8dd9895cdd2c08c82b9adc747022cd5935ff5a541ecdb7a47e20aebf8931b5817100a1bea6265c4577da10bd986ce360267205c8bc33dcea0055b84d4f072f800dd15a5d8ac1ab8ba9408345e9cde57beda70a3379ee4228fcdfddc6c57c846cebd15cc32f3a9aff9855c94b5ca637a86e54fec4338cf57325afd0e13de58bb68737bc991fa454eefa13df33a76e3dfeb64bb5b9a6987f040ea90ef3df03abf97af901345ba62e43c8dd9ce29dfc2953c54d564a7ed4fd5bbe3f3a5aad89d0317189a08ffcde4a7d7467ffefe4aff4e138f33fbae197e6c231a9ffef8c3f373edc0fe73fd6dd02d1765b3eabffb9b4bc267398ca68572288cf95e3a75c8e563af4ff34691ab56b649b59132a37c6e1f2ffe4faf151b944ac932de9ce3fa8fc78425ed981864676f661188587d1ad49f9131e606020b6eb6d2faeddd979b56d907fd882f9db1eda86a63b1d9b7f98b3fd5de7a1dd9680d43fb64b68cad8c0d39973c644aabb40b754fff64a185081bce4a3d20402559e44db4fb5c1807e94ed504afa0de05f738281e151b64fdea1e89a9a08f84fab98ecd2ef247a8feb3f4ddbcece03db859fc5bd47d935756756dfea066c974deb35ce63f856425eda2fcd74974debf9f541ed4bb3fa56f08ed94ead3b5b76bbb70e654766e9fd8f6be8f6d99038d6db64f842f2bb466e5395b0a870cebaaf121642e5bf33fabbd916ac94ef602999ee5b743bebff7f854379ffdecb520ceeada5bdfed0b4efd5854fca462e42ce2ec5375d0a0f8bd52775efdd7215b5c42cd1ae330936c3645fb74cfb4f5dc2003c72777446eb811d062d5b6b4b307f365906965fb48896e256fc7fa76b95261be0d72018506e50cb77b394f166d6cb6b5c51ba79d322607f3b69061af71151d2bdd8eb9bb4f74ffb2803dbdfb34896f347c3fcec3d03f05bfb8b513da516befe983bc4409dfcd91d7353a87eb3d98788cd3ad717f39dd17061d4e6fb0972dd7cd6efd7f063e0c7c08f813761e03fbfd297b60a656e6473747265616d0a656e646f626a0a33332030206f626a0a333935300a656e646f626a0a33342030206f626a0a3c3c202f4c656e67746820333520302052202f54797065202f584f626a656374202f53756274797065202f496d616765202f5769647468203134202f486569676874203133202f436f6c6f7253706163650a2f44657669636547726179202f496e746572706f6c6174652074727565202f42697473506572436f6d706f6e656e742038202f46696c746572202f466c6174654465636f6465203e3e0a73747265616d0a780155c9b10dc3300c04c0efb4898c6ce2c2828bace4cce3ce69124e60c46ab4436acba5894f480101c2e21ff70492d42a09ed266aceca9b6b60b9005da17f453b5ba38ad59e2d81d76ef9d361128d56519f5689e5cb58d89b30f1dcb693ef6b708e72d4473f7069f42ddcffb970f6bd4598577c001b6236030a656e6473747265616d0a656e646f626a0a33352030206f626a0a3132310a656e646f626a0a33362030206f626a0a3c3c202f4c656e67746820333720302052202f54797065202f584f626a656374202f53756274797065202f496d616765202f57696474682031202f486569676874203233202f436f6c6f7253706163650a2f44657669636547726179202f496e746572706f6c6174652074727565202f42697473506572436f6d706f6e656e742038202f46696c746572202f466c6174654465636f6465203e3e0a73747265616d0a7801fbff1f2b0000131216ea0a656e6473747265616d0a656e646f626a0a33372030206f626a0a31320a656e646f626a0a33382030206f626a0a3c3c202f4c656e67746820333920302052202f54797065202f584f626a656374202f53756274797065202f496d616765202f57696474682031202f486569676874203239202f436f6c6f7253706163650a2f44657669636547726179202f496e746572706f6c6174652074727565202f42697473506572436f6d706f6e656e742038202f46696c746572202f466c6174654465636f6465203e3e0a73747265616d0a7801fbff1f0f0000b1791ce40a656e6473747265616d0a656e646f626a0a33392030206f626a0a31320a656e646f626a0a34302030206f626a0a3c3c202f4c656e67746820343120302052202f54797065202f584f626a656374202f53756274797065202f496d616765202f5769647468203530202f486569676874203439202f436f6c6f7253706163650a2f44657669636547726179202f496e746572706f6c6174652074727565202f42697473506572436f6d706f6e656e742038202f46696c746572202f466c6174654465636f6465203e3e0a73747265616d0a780163601805a321301a02b40a01ed502c401b9f6d58d40385a8ab837457e1b39f5a72a4bb6a34ac504260345d512b298e9a331a0203140200f4e955b10a656e6473747265616d0a656e646f626a0a34312030206f626a0a36310a656e646f626a0a32372030206f626a0a3c3c202f54797065202f457874475374617465202f636120302e38203e3e0a656e646f626a0a34332030206f626a0a3c3c202f4c656e67746820343420302052202f46696c746572202f466c6174654465636f6465203e3e0a73747265616d0a7801bd575d6f1b45147ddf5f71a108d6858ce77b66232194347928022915863c508490719a1627699cb4887fcfb9bb7367d74ea5260dc296bde3b377eec7993377d6d7f482aec9458ad92beb5da6609db2294432d6a86852a4cd8a4ee9924cffdebc62ebac83cad178baa010badebea9d8ba62a3d7f5568c359dd31969d5f94e9b2e7e68d4482083403a4f022178c1ee19a8d12a77de989410e8eee8418150a5513aea447d45935f1fad8886402924a58377cd481d0906a742e7c7a8bb53c7a4caed4053ea3e14086bad6cf076a8487e35c31ab13ae60707273fecff74fbcf7a45f393b5a1c3a367bd6c26379e5d5ddeae2e6fe9f0c772eb64b559aededebefb63dd6c5ec349471aef8eacd79420aae505cd9f5f243aba822b8e725c66c240650b51245664c816f95b9f55763a35155b578cbdc12e19e43fce1db173a04959a7033c6ab64cbdc70183c782f16cb14b5975ce0288ca60411022a91c9207e239113b22cb8a35d58a5d3915b447c871a655de76198838174032605703b69d55b5332a64c74988f36c550cb966800207609a54813827a9a64e937a73f55c91c2ca123bf5f42976ff5d6ab3d2da18f482e1da9436c1cbac59eb605a45b058188eccba40e0407b2cad0de8235e33c365116d7098653cd82c088a0906dc31e962e43b18f5d3c4514506362130a43c58814d5018a190ea48a425d146b17134c969b4924a063f4dad630cc6db7f2ae37bb554ada2769d35119b375a176ce7d1a2a277c625d77308e16ba779334ca42b1858e406daaf6ce8a06b569b0ec9355b3a65a4a7c3a17359f82a5698870e6f3b9e573d0952e2354c63c5acd2600c8878ca4ef9903903d91782f0bca886ac0a86ac24732c48f1549189e0f868b0f503295d73e366b5e1586275ed8d3fc133b6685226356829870b326e30c1d5c64e250b8b2e055aa0e12c160187d8e28cdacf3e7f32a3c51b3a5e60d5aa643f318ed379370eb2e238bf524bb3be92760f83df68f1fd24249a4cffe2b3ee5ea1b96b6e97e82ca84ad197121b94684b895fbd6c5fce9e7efdcd7f57a7cd41459ca464b47385d052289759e9c466e3171fb765047d0bf6885a4d5468e35d1fbe19d6538add53736dec98c303e86ca68a81925c4663323ae652a184707e74ff58c5586c89dd38ff2b93beaf7087c4103fb54208b3d91126cef90989f72b6e229847c824a1851a9325fc744fa43c56f800896c37152eccb91deebafdd1339f035a85fae96b317d9b31fc1c6c719c842e87b29db14ff0eebf8b5357bbc7f98c9b9da5f6b6b411b9be951b3708dc5bccb7ae4dfbb7584c07c123af5615d35762727567506fad67cd90c14a6c64f6b2ceba1048b2ab2125a9ef86c94d5bfd56939bdfc5f1e64f197dbbdc7c59e3bdaee82f33d0a722b5ef24a9b352c9cf625307376f04ba96c1f1f2744626a944ede6af9ac8e90cdd090c57e0fdf3e2f44826d6422b72306de6d7582e5e4243012741ffe74996b81c4a1acf48786a5c2c87652edf430b2b8bdd86b9b673abf1a7ca847dbfd5cbaa7b27ffcdf064a3bb1c8bfb8982769ad6932f46594ed2dcf553f62f167a374fa4a38b4f499490e6e8f5c5bf9992e8920a656e6473747265616d0a656e646f626a0a34342030206f626a0a313134330a656e646f626a0a34322030206f626a0a3c3c202f54797065202f50616765202f506172656e74203320302052202f5265736f757263657320343520302052202f436f6e74656e747320343320302052203e3e0a656e646f626a0a34352030206f626a0a3c3c202f50726f63536574205b202f504446202f54657874202f496d61676542202f496d61676543202f496d61676549205d202f466f6e74203c3c202f54543320323620302052202f5454350a343920302052202f54543620353020302052202f54543220313220302052203e3e202f584f626a656374203c3c202f496d3720343620302052203e3e202f50726f70657274696573203c3c0a2f506c3120353120302052203e3e203e3e0a656e646f626a0a34362030206f626a0a3c3c202f4c656e67746820343720302052202f54797065202f584f626a656374202f53756274797065202f496d616765202f57696474682039202f4865696768742039202f496e746572706f6c6174650a74727565202f436f6c6f725370616365202f446576696365524742202f534d61736b20353220302052202f42697473506572436f6d706f6e656e742038202f46696c746572202f466c6174654465636f64650a3e3e0a73747265616d0a78019b3e73eef4118600866a91de0a656e6473747265616d0a656e646f626a0a34372030206f626a0a31340a656e646f626a0a35322030206f626a0a3c3c202f4c656e67746820353320302052202f54797065202f584f626a656374202f53756274797065202f496d616765202f57696474682039202f4865696768742039202f436f6c6f7253706163650a2f44657669636547726179202f496e746572706f6c6174652074727565202f42697473506572436f6d706f6e656e742038202f46696c746572202f466c6174654465636f6465203e3e0a73747265616d0a7801636060606466620002ce397fffff3fa2c8c0b0f4ffc9948ab777d919ff5f60656030f8e3c1f4bf1ca4e05c26e3ff69409af5792cc3f35f410c1cb3ff2b33387ffdfffcf3ff1aa0b0b06f4ab8320303007e7a1d3f0a656e6473747265616d0a656e646f626a0a35332030206f626a0a38360a656e646f626a0a35312030206f626a0a3c3c202f54797065202f50726f70657274794c697374202f5374796c65203c3c202f54797065202f5374796c65202f53756274797065202f536861646f77202f4f6666736574205b202d382e3830313237310a30205d202f424d6174726978205b3120302030202d31203020305d202f436f6c6f725370616365202f446576696365524742202f436f6c6f72205b20302e3539323135363920302e3620302e363135363836330a31205d203e3e203e3e0a656e646f626a0a332030206f626a0a3c3c202f54797065202f5061676573202f4d65646961426f78205b302030203539352e32373536203834312e383631345d202f436f756e742032202f4b696473205b203220302052203432203020520a5d203e3e0a656e646f626a0a35342030206f626a0a3c3c202f54797065202f436174616c6f67202f5061676573203320302052202f56657273696f6e202f312e34203e3e0a656e646f626a0a35302030206f626a0a3c3c202f54797065202f466f6e74202f53756274797065202f5472756554797065202f42617365466f6e74202f4555514e4d412b417269616c4d54202f466f6e7444657363726970746f720a353520302052202f456e636f64696e67202f4d6163526f6d616e456e636f64696e67202f466972737443686172203332202f4c61737443686172203435202f576964746873205b203237380a302030203020302030203020302030203020302030203020333333205d203e3e0a656e646f626a0a35352030206f626a0a3c3c202f54797065202f466f6e7444657363726970746f72202f466f6e744e616d65202f4555514e4d412b417269616c4d54202f466c616773203332202f466f6e7442426f78205b2d363635202d333235203230303020313030365d0a2f4974616c6963416e676c652030202f417363656e7420393035202f44657363656e74202d323132202f43617048656967687420373136202f5374656d562030202f4c656164696e670a3333202f5848656967687420353139202f417667576964746820343431202f4d617857696474682032303030202f466f6e7446696c653220353620302052203e3e0a656e646f626a0a35362030206f626a0a3c3c202f4c656e67746820353720302052202f4c656e677468312036373336202f46696c746572202f466c6174654465636f6465203e3e0a73747265616d0a780185580b785355b65e7bef93475f342dd067da9c101aa0692d1498428b6dd22605a71628144d189094522908526c79e820845104c273b80e23f800755494ab9ca6c0a4804315751445b8cae0f8024467d4b983a0dfa78eaf9efb9f9380e0f8cd3d3bff5a6bafb5f66bed7576ce39c48828994224c8dd3cbfa98d32a9119ad780d2e6c51df2c3d6b7fe9748733236dedc367bfecbd7de574264828fe1b3d9f36ebfb9e4e1a08128c50b878ed696a659893b776f403d8afa2f5aa1481f6e465f2917511fd83abf6369e287cc43d4a71feac9f316343751064d445d46dd3cbf69699b74d67006f5c1a8cbb736cd6f39dd6f5233eae3b47adb82f60eb5841e47bd15f5c16db7b5b4159caa3f8cfa66a2a43f41c750b42b998ce40497a94f5ca3ab7f4238567180b2811cc313942d39298b48fd18f844e3bd73d44f34bbc6f93fd0565b9506a25df4349b434fd3617a9e5d44ab3dd44d7be96544cf4b0fd032ba9756630653a1594b93500cd0dfcbb2d5bd54420f23d60fd331f8de48cbe90065b02cf5535a41abc49b68b58a52680079109505b4815daf2ea2697446ba8bcae87aba95da5848f5ab1bd52dea1fe831ea162fab3f5012e55033ca31f533c35fd5f7a8182d7e47dbe80cdb92b08fdc182504cf07e936da2ea64b4c9dad7e8b19d86909e620513d1d633ddc85de5be86396c596891af4f2a8aaa82fc0cb4ad3a995b6d30136928de576c334b55e3d863d2ba6a5e8751b45683f4a949ea57758b2e1a2fa07f5226553115d87f5eca5d7598fe8fd61656f152266409486d0685816d09fe8cf748239d8737c8121d9506a701bee504f523f1a465330db27d0f2efec6bbe1c65857849aa55abb18babe8b75ab4e945fa80e5b0123681ddc087f005fc21711b9931e23094593407f1be0fbd9f662eb69f27f3e3e25169b7f49d31aff7acda47cf89fbe9417a8ea560a5326b67bf61a7d887bc86cfe0f7f373e25ee949e90d5313567d13cda70db49bbe66e96c146b60bf62ad6c195bcd7ecbb6b163ec04fb847b7823bf855f10ad62a17856aa46992cb54b7719ee31ac337ed2ebef7da1f77f7abf564bd57ba801f9b012b3ff1d3d849575d3717a1be50c9d630696c4faa0c8cccea6b05fa32c671bd8236c177b92edc52827d839f629fb827dc9bee38462e4b9dcce07a038f86d7c09bf973fc08fa39ce0ffe4df884c3140b8c448314604c402cc6ab5d88cb24f7c20e548c72515712e356c35ec30ec32ec363c6fb8684c36fdc64ce6d7be7ff487c21f4ef752ef9adeadbd91debdea07d41f7b988328d8680c66df843217fbbd1519b787de64c9885d0e2b6495ec7a4466069bcb16b2a588e4dd6c3b7b4c9ffb33ec10a2f416bb8039a770ab3ee76bf8485ecd27a0dcc45bf842be996fe17bf929fead308924912afa8b4231564c172da243dc2eb60a45bc26de17e7c457e27b14554a946cd200c929b9a4b1d20c6991f490f4b1f4b1619ae155c3df8c89c6f9c67b8c51e3e7a65f982a4d134d0da6e9a64da6fda693e620b2f308eda33fea776d9cb0b362a5f0897db4910f97b2f9ebfc75e4f30c9a25ea393295ef626bf89d6c2f1f68586aace0156c3c5d949c88f54b7c07ff8a57887a56c726d35c3e2cd69db19ff414a431d2113a2f1dc2da5e47cf4b8dc96c39bf604ca608233e1a07d38b62a8e412afd23be20c33490fd3bb5222cb64e7f9136222b2e059a9d2e027bb78809e110bd99db48ffb8812bf33af471e8f674fe15c6864a5ec5f4225c1c7238bcac4877417ddc2ff4ae7711fafa1dfb359d26cda48c3d932fa981ec75d31c470abb1d0d89fbdc2e74861de97ed252e3d89d58d66039930f4a3bbd974b1dd7881bf4d8be8b89448a7c57f63f6c7f933a25eba6898c45a7107dc49f7d0427525dd6ef04b6fb0d924d80d54209dc5e9b64c944a76f0153855a6e14cdb8fbbfb00ce018fa887260b99733df2620a4e88ed28f7e19c90904173708fdf8853ec75da6b6ce4519a6de8c370ea1049aff64ea2a9eae3b44d9d4db7aa5ba818e7c16a75197adc457fa34db48badeafd35b5513eee9cd3ec7a432d3f6ea8558b7998bfcd27f3ad57ef2fa25dc0b2e81f28cf60672a0d07292cbd4593a94a5daffe05d93d1827ec369a49bfa48fb0cacf30c238d143c37bc7f34eb556b461bd67a8417d42b5b1446a55e7d1043a448f990cd46472618f15f606d6fb6b6ae193d40ed1d23b0771d88428b811ad45387fd6ba6ba6347adc5595d78ea9281f3daa6ce488e1a5c386965c535ce42a1c327890b360a063805db6e5e7597373b2b33233faf7eb9b9e6649ed93929c94986036190d92e08c8a7c8edaa0ac38838ae4748c1b57acd51d4d50345da1082a3254b557fb28b2d6ae09a6ab3cddf0bcf9279eee98a7fbb227b3c863684c7191ec73c8ca31af438eb2a90d7ec81bbc8e80ac9cd7e57a5ddeaccb2990ed7634907d59ad5e596141d9a7d42e6e0dfb82dee222d6999458e3a869492c2ea2cec424884990944c475b27cbac64bac0337de59d9ccc2958a292e3f0fa946c079aa21b51e06b9aa54c6cf0fbbcb9767ba0b8486135cd8e990a39aa955497ee4235fa308ab14631e9c3c87314ac86d6c99d453de1f5510bcd0cba92673966354df32ba2097df8943417c6f52a99777c94f563159da7d7f8575f69cd15615fd61c59730e8757cbcace06ff156d73ed5a0f8100fa405b5e501b0cd762e8f5d8a9bac93246e3ab027e85adc290b2b6126d55b1f5b5387c9a26385756121cd58ed6f0dc20b62627acd0a4dbed919c1c77b77a96727c72b8d1efb02b55b98e4093d7dad98fc2936eefca76cbd9575b8a8b3a2d69b1c076f6498d0bc929570a2d087acca64bbabb26d54dba1c59a6cdc8719de2464635cb9889df81358dd248cb280a378fc206e00a30b452666147e6280935c1b0a55cd363894c3114581c72f84b420638cefff36a4d535c632cb07c499a51cb93cba9a6b0a64bb2e2722985855a8a986ab0a79863a55e1f595cb438ca1d8e368b0c86f0d144c4b629505e82f0dbedda06af8bba69262a4aa8c11fabcb34333742ee125740e141cdd273c9d27f8a66095db25c6e1e742093f7ea0f8cfd15b3f3f22fd592d1d7d75aaeb08cff606e89d9eb263bea1aa6fa655f3818cfdabac6ab6a31bb1650c40db6b8a4f4adf18b5c0e9d26f15ca15b9194d3a65e7641c59fac4805f819f5a49e1535999195ba86c9b58a25382e460389767bfc9ef9ff1a45d58b5a2b9dfdd82cbe0ca5dc159f686cda4ac555f5aba6971c16758d3872785de3d47038f12a1b522d36cbebe20c194f8d7ebb5ca3d014dc9905f845d59e511a02b98a1b2183a5117791ae0ee4c6ab5739e6c61b057069d9595c548b33331cae75c8b5e160b829aa86663a648b23dccd9fe7cf87db7c38ed628913550faccb556ad70710b156568edb835375a783ad69e874b33593a7fabb2d787758d3e88f70c66b82d5016d5b784da33f1e167dcfb4d4c71ee21f06c7b5f6808b17214126227b9a3dad0084e14fef7b59f47cef36d077244b3df04272795894cfe5f3e15be4ce6ee36d82d7b37ace9983788ea10d0ed952db862cd778cb47d32d7fa792faf3c386d24236bdef487b7f0f1fc2a2fbf6c5fac1907cd3e0f777f69b913ae64b73ae59d3d2f1cc92d0658e372d7d5644097a526b064cd554d93b9e6a2cf4ed9e6fefc03ab5c95f79092354da330b7f0aef2184a7fd0c2ad7bd047d81c7d007b1464e16fcebdf807fee56290f8370f8314a8ff765c4f33af9a6348caff7b83cb7cd699a57dfa87bc0491d84e7c89fbb0494822cddd428067739b36c270e89217416e06248c49567eb1683445ea4c2e68e0a47577affd2544fb190315e894e65d005c01ee03020d10c910fab0574051002f600878113801183e5eb56197c01b003380b18459eb046649bc5334864a36d36669e2a32e902a002826ca025c0046006b009d80118753f4db30058011c062e0246728bccc896e1987b66649dcebae6ce2bd5ab4db1eab4e97ab5ebc6408cd737c4b8f7ba985b79cc6dd88898fa9aea181f5414e3e905a52174de959852dae3c9101958640626de06caf80b94ca181ec9768afea4001c3b1cd3b8457ad74067e98ec3422226b8607885b2a93d824552d24a3d895ce517b0a936fe193f1fb3f0f35d7dd24a77787ec9cfd11ee03020f839940ff807b4829fd5620e5a05ec000e03c7810b80919f453983729a9fa654fe3e950055c00c60077018b80098f8fba016fe9e964f3ad5e42a80f3f7402dfc5d2ceb5dd054fe0ea477f83b6a0f7f335236bab45b175c2571c15610173273e3427a466994bf11f9660832ca899d46461d1403a892868b01918261b6a8c88a8c99638bf20fbb64976da767283f490ac031939318f924c9c0442008b4014648a7209da210b019d8092800b20cd402c8fc28f01a708a86026e602260e627221826ca8f479cd5364f065e46fe8c0f03367e8c6b1f136cfc35fe92ce5fe52feafc15f07ce88ff29722f936f224c14e686301b78097c06ee0cf750d4cb7a99e347e1811b481960055c0046006b00930f2c37c4064962d1d9d1ca4a3383f6c3c429feafc717ac44ceeb936b7b30609286bc4597e2d24901df20e27773bb76e435523ce8d5b2069c479f77a481a71deb11292469cf31643d28873d65c481a714e9d014923ce098d9040a2fca13f0e1c642b9b700b933da97c09a2b404515a82282d2109efba28f48da4cdf1fe48612122b6dded1a52680b1d60a1432c3489851e61a116165ace422b59680c0bddc4422e16b2b2503e0bb959e8201b855084987bef55d5d1ee2c163aca424fb3503b0b3959a8808506b290cccadc516e8f5c87bb0ecca7b32e8f76d3717bd7b595387d52b91d11b523e7ed38130e831e0754bde686933c20e69c9daff1015d8555b1fa35e5a50b3ce3f811343c826d38426700091b74046974049d1c4177a9a055c00ca007b800a88011de03b08e4d3a4d052d01aa8019c00ae00260d4a7730153e1b400549be21e7d6225a055c004adc68fa0681f13ecdceeceb3582d2ecb38b1c9ca52f3d9847c359f975146060ee5f434735a94a5ecff3ae55f5fa7508227816fe49b280f1bb139ce3745bec9b345d97d11e7419ba73ffb3de54bc83a369a9cac007c14b5ebf59164356bfa1164e5bbc14b23d61bd02c35e22cb21d607db456fb6ddf583fb27d6a8d72889f580fdade92a3128bd8fe02cdeefdb693d6b5b6574aa266680e39a30cec80acbb765b47d99e3eaabbae84617bc4b65c63fb6d775ac7da6eb1ea869698e1a676d4dca9b649cea9b671e8cf6b9d6973b7a3cffdb62aeb4db63131af915a9bfdb6a198822b261662b243acfaa08e7cbdc3296551d6ea2e326d35f94d13f0e5a1d45464b29b6ca63c53aea99f39dd6c31f731279b13cd66b3d12c9939beb5f48baa67dd2eed9fb29f51ffe33522a1f194a0cb169c304c3b6640f14461e6783d55fa8a3a5e37b99ad5293dcd54375356be9aec88b2c486a98ac151cd94f43aaa6bac5646b9eaa226759252e6aa534c137fe5ef646c63005a85af89323c454599aaa956e56aef38ddc458daaa0db91a1fbc6a43204059198babb2aad22bd346d77a7f86047565d0ebfaf1cafa517465b9f294ad7593fdca537901a55413d4bc409df25fda4b50373e5e5df479bbd9e71a0bf8bb4525fbc23749d38b4a6f2050176537e87e24b3cfe1878c01839f399f64cd8f64737ecc6f7bccaf00ede1375063f04b48a002ddaf202141f79398e6d7d93ed0e7ed1c08029f4c99da759ff64cf94a9fa305f02900814f46888eea3e4733429a8f52a97763b5c2251f042e0c5fc474172bcbd15df49977ea2e257197b5975dd6ea2389d86c741f8da09b94b3977c52cec2e78a40fe67b1a5dae5625d1581e6693ebc40061dbe1620a8ac5bdc9aa58466ca7267734033e03dce199cd9dcaaf1a61625e068f12acd0eafdc59a1b7fb89799a66ae70783b699aafd1df39cddde28d54b82b7c8e266fa06becc41165578db5f6f2582326fecc5813b5ce4668638dd5dbfd64ac32cd3c561bab4c1bab4c1b6bac7bac3e16e9393ed1df69a6ea005e7274dec5931291afc15c7ba03ac3d256a9276f853d6b79ee013cadeca224bcd825e353400aa0e575b1a7d8a399704f69a63eda5782b8296b79853df700db153759a04e735493ab6351fb22caf2cdf1c67eedb8a0ea58a46d458cba34ddcf5e70f1e185dfdbde4154a7144eae53aaf0ded76932411bf406a02bbfa44b4af2e1c526a6bc06ca72cd5188cb8e9a6e8ca64b48883bfe7b2ee873821ad1e9c683c6c12ee6ce671dd41e104a7e5d23c751d0381561c0dbe2013c4b697f12ed012cb01d1f10db2ff5a6ad439729a6212cbbfd123a16c5a5782c3ae25c776d7791abfd52482e75e7d282a5133d561d2e1c6dff07682ce99d0a656e6473747265616d0a656e646f626a0a35372030206f626a0a343536320a656e646f626a0a34392030206f626a0a3c3c202f54797065202f466f6e74202f53756274797065202f5472756554797065202f42617365466f6e74202f444346464b4f2b5354486569746954432d4d656469756d202f466f6e7444657363726970746f720a353820302052202f546f556e69636f646520353920302052202f466972737443686172203333202f4c61737443686172203335202f576964746873205b2031303030203130303020313030300a5d203e3e0a656e646f626a0a35392030206f626a0a3c3c202f4c656e67746820363020302052202f46696c746572202f466c6174654465636f6465203e3e0a73747265616d0a78015d903d6ec3300c85779d82633a04b69536ee2008285204f0d01fd4ed0164893604d49220cb836f5f4a4953a0c31b1ec98f786475ea9e3b671354efd1eb1e138cd699888b5fa3461870b28e351c8cd5e9ea4a4dcf2ab08ae07e5b12ce9d1b3d08c100aa0f42961437d83d193fe05daebd4583d1ba09765fa7be54fa35846f9cd125a899946070a4752f2abcaa19a12ae8be33d4b769db13f537f1b905044a44447389a4bdc125288d51b90999a86b29ce67c9d0997fadc30518c6eb246fa4c83ab65c492638274b6a5b7cc8f64096d41e1fefcbb65f2e2fce0fb805d66b8c94b57ca99c91e35987b747061f729ca21fd09176b50a656e6473747265616d0a656e646f626a0a36302030206f626a0a3234350a656e646f626a0a35382030206f626a0a3c3c202f54797065202f466f6e7444657363726970746f72202f466f6e744e616d65202f444346464b4f2b5354486569746954432d4d656469756d202f466c6167732034202f466f6e7442426f780a5b2d3638202d3134332031313433203930395d202f4974616c6963416e676c652030202f417363656e7420383630202f44657363656e74202d313430202f436170486569676874203731380a2f5374656d562030202f4c656164696e67203330202f5848656967687420353331202f417667576964746820393937202f4d617857696474682031313836202f466f6e7446696c65320a363120302052203e3e0a656e646f626a0a36312030206f626a0a3c3c202f4c656e67746820363220302052202f4c656e677468312031333132202f46696c746572202f466c6174654465636f6465203e3e0a73747265616d0a780145544b4c1b57147df7bd3736fe607b6ccf8c21b661186c038e8dcd600fffb4c1504a424203313f87048c0da4d0581525b48b8845b7698214a9529b7e94b6ab48699ab0a8d44895b268bbeb268b6cbaacd455d345db4d1bbbd73649af75e7bc73ed7bad7b74de102084d8c83e612491df5e29113b3989954798fefcee4e2b62359e561fc5d2fa7615316a7c7debdde26b370bf13a67ef6f1456d640bff305213c87b5f40616ccf7d90fc86f216fdfd8ded9737e0ebdc8bf45eedaba925fa1bb5044fe1372ebf6ca5e8958e91cf227c85bdf5ad92e3c7ffc2c88fc4fe49da5b70b25440ca1b50684007eaa612726e24254710f46285619c912426799154f66dc907854513530414be9592897e9046c956fc1e0b56bccfaefdf5374eff9c37d9c2090f9ca6d768329d8a313830c920221a1388b1841a6787ad28603ccccc1cc7188c823d4f068c8c39111308260a465333241937409d3ac8db094d81b8e044149a7f011043336d64648a23748152da5d15d9347f1d9df68907ccdf6e9c0ab1ece4002eff1d70de6f7e927da1de07922b6cbe1f668469286140775dadac4f21f87d5c83106c0b8371df4fa867c766a37596d0e6b5f368c3328a3170ebf567a7b62a2e68dc7631e381098ac37972dc7a2aac48f9d904cd41990e16972b22362eb7ff85e8315c0696b184f4cdfcffcf89512525509be57921e1cb47edad400d068135c2dc90813bc49b93c21b5a9edf202eac5c854e506fb98c54913099075d44afa7f6fbd07e5c25d65451b06511301759250395409d534a3aa78428ee536931935d24720a2a148a82833e423c574940cbb5069fae9627567cae3335dd30b9c5261e1cbf22fbdd3adea8c414d826cf4259d6336b5332e0797071d2165bee41c5b0b771427b0ebbcc5d1683787ba9ba22d6e181eb03307b73815b1985a6c699a8c028dedf8236a8b97bab4b09b3193ab432bfb3acf252e4dc2ef6537fa8cf30b9c83a88a9e543ae9ca38a349dd77fa03dddde27ce5c19b5c58e2ac7b69c0ab06032e85752d2d9e0f3571b098a9ad391ee27c8ed2933974657fe523768fc5c839d4a9e68011360c7537e1c22846d527a91758f34edd558117d2494712a27ef53eea470fd0cb8c5170a7cf6c8c39c252347ff1acbcfae1d0d883bdd583bebe83552e048e3789ac51e0cbd7d3e9ebcb57bfc90c1de47d130b45c3ea715ae1b3bbe21931964c48522219139fe9b9b9858412b2b947b3f92470615910e036620e71ac2b26a2bda0b181795b2f8180450e9f80097fc4a17f3113b79b647fd071eabb2bd5db59cbcb03bffe73d139f417b1b0dfb0427e56baf75f623fbae71eba078805ef6d3db08fe52a3b24c8ef541e55eef2abb549475fd680e3a42ce63ce614663f56fde41026e1319eaaffcb6916ef7235ea6f88daf1e50388fba86e220a2199d1f1f15367a333b31385cd9dcdd9d1d854616df39deacbee3fc336d6260a656e6473747265616d0a656e646f626a0a36322030206f626a0a313035310a656e646f626a0a32362030206f626a0a3c3c202f54797065202f466f6e74202f53756274797065202f5472756554797065202f42617365466f6e74202f4b4b58574e492b48656c766574696361202f466f6e7444657363726970746f720a363320302052202f456e636f64696e67202f4d6163526f6d616e456e636f64696e67202f466972737443686172203332202f4c6173744368617220313139202f576964746873205b203237380a302030203020302030203636372030203020302030203020302030203237382032373820353536203535362035353620302035353620353536203020302030203020323738203020300a353834203020353536203020363637203020302037323220363637203020302030203237382030203020302030203020302030203020302030203020373232203636372039343420300a3020302030203020302030203535362030203020302035303020353536203535362032373820353536203535362032323220323232203530302032323220383333203020353536203535360a353536203333332035303020323738203535362035303020373232205d203e3e0a656e646f626a0a36332030206f626a0a3c3c202f54797065202f466f6e7444657363726970746f72202f466f6e744e616d65202f4b4b58574e492b48656c766574696361202f466c616773203332202f466f6e7442426f78205b2d393531202d343831203134343520313132325d0a2f4974616c6963416e676c652030202f417363656e7420373730202f44657363656e74202d323330202f43617048656967687420373137202f5374656d562030202f584865696768740a353233202f4176675769647468202d343431202f4d617857696474682031353030202f466f6e7446696c653220363420302052203e3e0a656e646f626a0a36342030206f626a0a3c3c202f4c656e67746820363520302052202f4c656e67746831203134353732202f46696c746572202f466c6174654465636f6465203e3e0a73747265616d0a7801bd7b7b7c54c5d9ffccb9ec397bcd5e92ec25d9ec6e367bc9fd4642420259426e5c4550489020010201a55c84085668543010112f1444b42a5a21809810a22e522dc520a0bd8077a96db5a2a5fd34af6ffba2af0576f37ee76c88904f7ffdf9473fdd9367ce3c3373e6cc3cb779e639134209217ad24678327556d3f28524fd163f4a3e21842f99bfb46939379a1b4d88108fb237e7b7ae723f12a83b089cd5772c5cbe68e905e1b17709915b08d13817ddbe76e1a2c73cfb08316f22c4f5d796e6a605c2939d5a420a56e1f9e216146852a5b1c05f009ed6b274d59aceff32e8809f01fed3db97cd6fdaae7972172185acfd4d4b9bd62c971fd4e6033f05dcfd83a6a5cd8debeff821f00bc053972fbb6315ff89f03b4246a00fb26af9cae6e5073afd3dc0db30c62584cd2df663ef0b03583f1f01d8f3df124265800d10049400ea00f58016c01ac026c04e4027200c3805f8087001f02d219c0cb001828012401da01ed0025803d804d809e8048401a7001f012e00be050d65800d101c18fc615c64284f897b18ce78736d7dce303c77189e370c072daf7bbe60183e62185e340c1f350c876c5cd75fc5301cbcbeaebe6a185e3d0c1f3f0cbf79183e63183e73187eeb307cfe307cc130bc7918be7018be6818de320c5f3c0c5f320cbf6d187efb307ce9307cd9307cf9307cc5307ce530fc8e6138d3a36be565f530bc75187e27c3f1a383baa3232af22a7037a9bf469b20bc4aabef129e0844445b094532510f5568941c2c00611aaa5730038953ee841807efc36f266226966185f1837802492456622376e22049249938490a71a1ce4d3c249578491af1113f09902021e23162145f2341b18d38845cd66ae063c039768fde3cf0a5789218a34b07fec697e1f9230cb868453939461e244f902ecca613f92099437692d37409394267935ef2014d2139b09702ecc924f24b3a3070962c243f45fb55e438d94e0e61a641b29424a0762bf50ddc053c84fc3cb261e0598cae84dc4f5e23a5e8752be91fd8377018b5d3c8cd643f3980e7dfa65eee9060197871e03ce87823fadc809ab3039306ba40932c5249a6a27403799dfaf873032da0431946f724799aec26bf207fa5f7d2de819681d68133039f814736d0673aae75b4977ec67709f70f3c39f09781282811241978eb5cb28d3c87febb701d03cbabe96d7415dd46b77321ee5eae57d8285aa311d0219dd4e2aa23cbc82650e008e9237f27ffa05f7136dec8afe24f0c140dfc0fd1928998259b493369c5d58e6b2be67494aa681e1d47a7d275f4c7743b7d97cbe06ee6eab93bb935dc97fc147e36bf967f57b843e811b7883b55dae8d70347074e0ebc0f1e3bc92d6425598fd91d2767c8457289f2e82b99fa6819ada47370b5d127b82374373dc24da5c7e8196e3ffd03fd9c7e452f7322a7e312b84c6e15b78d3bc01de77ecd2fe6b7f38ff37fe0bf16c6889cb85bfc42e5937e1b9d17dd1cfdf540d9c06703df6205942143a5a0f114722b69c26c979311e44798c5415c5de05a1f39414e2bd7e73499f46305f907a866a60e5a4027e39a426fa00be962fa147d15d7ebca58bee1c0084ecd99382b97cc4de7e6714bb936ee7dae8d4fe233f809fc2cbe0bd729fe03fe327f5910058b9020d40ae3c91661a9b00bd71ea153e8117e23968a63c429e20cb14ddc2c6ee1e78b67c50f54eb555b553daaaf54ff2d05a549d232690bb8731a32fb8b415d89dd049a86d117901f90f9b48ace233bc08dddb4897440ba16d04da0d772121c68e4d7f3b55c1ea4e175f24348eb2eb28e6ce66793dd031ff1fbc987901466bbdac85ea19238c5c7c09d7b491ea4e8eab50d3c7f86bc00ad38002a119219bd59d13a8ff83ab49184d233d28301bf2fcd9bea71bb529cc9490ebbcd9a98106f319b8c7a9d56a396259528f01c2559d5de9ab9ee6effdc6ec1efadabcb66b8b709054dd714cced76a3a8e6fa36dd6ef65c13aaae6b1942cb85c35a86622d43432da9d15d4ecab3b3dcd55e77f7afaabcee309d75633df20f56791bdcddfd4a7eb2927f58c9eb91f778f080bbdad652e5eea673ddd5dd35ad2d1dd573abb2b3e8911088a0c9ce62862544b4ace36e32ae695d8b0d37d6a2badbe1adaaeeb67b91471defab6e5ad03df5c6faeaaa248fa70165289a568f7764672deec638c903ba05de050f844364de5c966b9a5ddfcd3735747373595fa6cc6eabb7aadb7ad717b6efd0abb9ea2dd7547673be9aa6e68e9aeed0dc07405c86ce6558d3166013a7bbd12db7b1a1be9b6e1c1c041be3128c940db7d95bcdc6357789bb5bedadf4b6742c990be29269f53d8e90a3dadb54d5d04da6d6f7d8437605c9ce3a625b5fe6c1ec8f648fcd1ecbee651edbfad8fd4ff7c5cadf39c6eeb6f57d9fe23e71da1001287b93773cc6d9ed9eafbcc48bc196b0a4b98474cc2f019df06ba098e6628c675c370799e17ddda26f7c5377dbf4abc368a98a0d6eee92aa1eb5ddc1e630b7b201ede77618478153686ff4ba3bbe2660a1b7ffafd797340d96a87cc6af09ab648c1e92956eda7435dfaa1006b36eb1795b187f5b159e02f7daaaaf2900ce48c3c6dc1ddf5d30716abda7dbdd808230c9cc9a1826eaa9f58728ddda10a6031bc3a4ca7904ab297feb1c546731515b5c85f703c9ce42418607b99c2c770d665dc364c5dde1ee18bfa0c35de36e8130093ee58e8ae68e865c50707a3de8446ec21b430d4943d9e6868651e82797f58347d0bca3013d2c19ec0177a528378246795913c115ffd4fa1bebbbdbaa92ba43550de002c4f7d8d4faee6390dc8606b4ca1f1a2946bc6eb16d70cc0518737e06ea0b63bd4c471fe8a2a1a383f539bddeebe93ed6d191d4c1f42d868729195e101a2c0813d60413af0ed3b6a9781637af278915783d5e0f86d5c0683a02227d55a2c2a4e85f53b87868dc787224465bac50b8e4df44e1d2ef43e151df8bc2654323bd8ec2e5187319a3f0e8ff1c85c75c47e18a7f4de1d0d0b831c8b1186d48a170e5bf89c2e3be0f85abbe1785ab87467a1d856b30e66a46e1daff1c85ebaea3f0f87f4de10943e3c6202762b413140a4ffa375178f2f7a1f094ef45e11b86467a1d85a762cc37300adff89fa3f0b4eb283cfd5f53f8a6a17163903763b43729149ef16fa2f0ccef43e1faef45e186a1915e47e159187303a3f02d43140e2575936bed70db30b34bfeed8679f6352487a70427b912319b33d8aff1d8452e0e93e9996122e7868900908d089e9c0130dc7884f0f4d58bc09093941c8f7af527244c5faaae0f139a9b748450fac6182ca24a01610584be36a6e10811e8113c4af1a8885c5e7ea1c9630a002a85ade12b7f145fbb342e2c4cbe7c18a36818f8bd18148f62d7ca7659ef87dafd59feec915923b3d7ea456b41b5aeaea0be600159625998b84658ad5f635a53b0a6487783be59bf9af213f4343b2f3b47af277ea3dfe72b369b4c3e9331e04f37fab3f372c50cbdc9c7f31a7fc098959da7b26530bc4c54d95469292457634b1ba1e58b0d1eea29d31427799246e6a4176784721ca3138a47dacb478769f361cfab85b6cccc291727f74f317e33f962bfc95c5a9a1b29cd6dec2715fd1515c0ada5b9006a36594bd99fb9b45dcec934cac7e46322a09134e6e735d24632b2b86884df9b2aa912e213ade6e29154a5640b397fd188e2c20238e8924aa263e8c842155744bd01547b535137b27824d799563663fcb2ccc47153c63e9319ed7d30b3f240d3bb15c7763ffe42c388bba82e29417bc0b4908a173aa655ddf9c417679ac7ece34f64544d1f3b67823f7bf4f2862b971656d0ed730a26debe7a3def7eb46aebd117d73eb864cc8dd163536fa87d301ab147dfe86c48affbdf27f62eaf5a88e8431758d846ce8133fe908566f01ad16a75d005c42e880b3cf39b63f4884ca96eaefa92544ceecfcf1b5998e0ed3a7bf61c36fc785411b2258a7c6586ac12b5d2c7b0a1e4b86433cf138ed75088206fcfb5bd472aca2bcac5f69ccc75c63eda480ba997beb3339ab393c90693d3d0c0c742b2b813518c64b222646d17698d9c5014272617497a7309bfcc56a24da9751a5bfb6ceff547147e602ce3d686469024bd9ffa1c7eb54ff4271a6c41124fcc419a242367542167d52504a9854362d7240789494092891f6589f2bb8734126ba2c928711e77c06f1a31d2ec31179b4670de54ce146f4d2ce44377cf9db93efac76874fde28a565ad4b167cdc1a7b7e5d6bd28eefce250f497d14f7e1efdaf4f8fd2b28b5db4e6d217dfd269176959f4fde8ef7ebbf1ed188dfa30c1f7c547b113f61e9269981686748220e90469874834b56a36a9bef723a5a4a2e2e2aff2f32c454c304c5e53df1bbbfc5b8ff1df74581af65cfa01ff8d42ef10f43945fc09a2327b42538a851a61a6789bf3072977a56ca0ed9c9c21cfb2df66bfdb7e77f24b7691a4d23821d960f748c9768112d11517976ad1145944b76bb52755e7f9915492b82cd51088bbc755929a56eb8d11f762bff1ebfef36057a49c497c4ce071872e9890301107d99305bbce67f26bcd862051c74b20aea0376a82544e4002fa1a8d0a7d41da6273052d1e5208c98bbca7c0cc34208ea985e449f04cd8f88b63f78c98b663dd915abff00a5fb99a06bff97c6dcd4b9be7952c70f0862be947a879f9b28945d36f5bb76dcbc48d475bcf44bf79ee85bb6a9b2715e7cf5cb21f74e11053804d139f444e4f668752d59c46d6430e5f37ab5412a7a2a22423ce2169b8d55af12b5e27097c985a5fa23bf4f20b9a30ad3f2cc6d51a94f97f7db13c82d957e0566e2a2da59839fe4a21b8c23ae389b8fc3c6a525393a788169aa00826eef96811fd75640bf7f0ce77df45886473e4cea848e774f35bafdcfa93e8b331fe570e7c02d96e236e7234945967dee4e24a753596999645166194acd34b44a7893318569b2d16b321ce6db648c462d5588b30b0d49043ff2383c1691e15270845ee934ebd492a712c2325eed45a4f8c5f5ff7f7c13af55744c0aaf38acd626366ec5294ed04312b9328cdcfb3816f419b8baa393f9f826015252eb7980cdea96d48a84b0812551212d91ee321531163b9b13c33f31ef0b1d192085d288859369514b080713c9859582024c4739ed4b440c4bc2e74d333bb5e696bdc98fbe452ee42e4e9d105d953179fa0e6cbd1feaee8ff18e9d25d6529bfbc7bc74feb426a9e7f31bad26ff144df783bfad6895f2a3cfc1c06a557f0286b5541c82166487c0601d3d43049225d2010bbac5ee0597377cc28814183660976c9086b60610c017c7e163fc1732eb24db1511c994d7e2f2ce38b957e03a104ea26bf92dc2a8138d412872e677b2aa6295d961bbf2927b993fb2311b039013c0608cb2ef70b96cbfd7c71676774fa81034cd69e84ac59c533e8cf4d9e0cd506cd75967a0b5628bdb858b756c7f9e538a33e214eabb62598f55ac16d9cc9ecaafbada4341535c7e51b5d7401cfabddb612b523d595efb67b52dff5ccafbb66e551d4aeff22b3088cb15f62a9613c651a6856b4cf614f1164a72f59748d250ec93696a6084963a95d4602dd639ac738461b7da006312b0b8e4a32d004ef88e2eb9591f69f3c19edbaf8fe89fe991be696f654dd31352d31b8ba7d6f284dec397346384da5cfba966c686bbc67fd435d2b6e48f58dad99f7f0ddd5f762e62988048f86dde7880651ca73a1a975b49eb6507e13ff98b053b34f13568735aa20ecbfa452514e56ab91688824d22d9417dcf11a8dcf8cb27851f44143a9562bf26a8da012a996a358385224394c1b426a84ae546a0d2f02eb0c99f57a26084fd1a734769d7eb767cb1cd0cc3ee5a26d7224625744a1a6ca462aac5868264714f5adb8aa0aa6d25c451b2662872d1c4bea16fa1ada736c588b58018f02beaf21f3aada18cbcb250064a0112b3ad5520b562bdec37b29bff50ffd1b3fe312ce6d8f1c7dfa97dcc3dc2ca6f0fcfc4be368385aa750e379d0653e72ccbf591472b59b7698b902599b12c79114ab2ce75b1c0ebdcf60b73b3ef0b46e8ef13cc2bc0d2c69910ac81d74d44f134dbe04bf4a122541e2254e12551aa35c40692212b5595b40a578441e15466780d58dbe02380eccc61a39afc7c47bdc58cde2252e9d72679ac7ae9a50e688fbf86fd1a74f71d369eedeedf54f44ef8f74ed4f082c6b78607a2d35d19ccb3b45cb87c7a367fff25a145ff9c051f804423fe6a045947d4a284d4a11042d9f82a55c2da768b4b28ed3e938a25acc95a91d065ef611bbde10a6dac39eed572754aef84fe741d05c9852b69294f7238fe931ab611a04da25e45ed9c6675e799fbffbf271ce25bed61baddc1f3574e1d5f851445d097d14084fac88e364beaab81b39998886329795cbcdcbb7609d3c7dfab4e2447064ffc0c7622edab3af19e521af550c8825465e43387194519dc82726c6ab7d3a878dfae2ed56db339eedcbafd5b9d858fb2bcab1c05165dd07558b4c8531bf80f7dba987ae2a6f7837724bfe5be3ef8f6e896ed9389e1b27be7665d5334b9e3938e7697ecb9593d1bf3d1afd866a1ea5717c29068b35492cc67854e4a150d5c3f419ca85e84d944ba4748df825e516092de22681b707391fbc2581304d10a9c8f12a688028c832a45de0f8a744429f52d9a5ad1077fb142313f7d252fcc5441e025f0e9187916f9f9c93c9847ae2b4b5211d45c897c7a24f3995d82ec3e7521226d2a471c58a956a8e3960d448f7d0dd7f885c7837f267f1b5cb4ee1f34b98508cf63e42a4154074744668bbaca66ba4b5ea35da767abf20d6d2895c155f274c962b359be576cd29ee247f523aa5d5d56b17492ddacddcfdfcfdd266ede3dc0e7ebbb44bbb8fdbc33f2fedd7c6c9b2a491b5763951335352696541c38d095607451f5668e2d3e9b46a81f25a8e17553a11df80355a5e920d2001c4fffe90cc0b17359cfa629b96d0fb7576fd75c470c408c26e43448115005560384196fef69c7e50a5578d783816d55da13833a59c20f2824a52cb6a0daccbae90c62c083c8a894edbbece289f0029c54c50ee44bb0cb88a4cbc71ed610a4dc0132fa33b019d281daad572ac3f4672f4201b8f296014ef8ad8e43e5b3bcbac93fbc081958d8d2b48e34a8b9a16e28f7ad5e0448426d0491fd14934e15c74fdd9e8c1e881b3d136b0e466e100834be384e397c7801b145fd98870c740337422ee255a03392158a1492e963f5391270155a4b999e930bebef0678429caf7bcbda1dcbd76bad3d629efb7f11364d313f13c1faf723a24bd335e9b242525598d0133e5039cc9e1d404acf66467984a873d2bd77da71fe593fb4b4b990e439ffb2b5886adb88aff6d977dba048d9f182c463f76467146c90e4c24bc871199d726eafd24ce8c446d53f9a940551ec535a431d72296662acb1549b47a73e8e0be09aec6c8420330aec8480a25ee83cfad5dc695eb5f9890b7e9d1e5f7d9bb52fefbe83b97a8f9bd64614af787f3efeb5cfaccee4f36dff9fe095af8253e1d8d8208919281737cbf781c76cc49ee0c158c34d41a661af60afb92449f1ccfc5398d44763a258b86735ab5628e25c7986e323b5cda00965757bb6765e5b5d38f9c8757c836811526f8c1b155d896acd6604b6cd3626ec948889df3134d92ecc704f1770f5b84cddff94c09d865586150bc456c5aa46884b9f09b4777afdbbde7ae4dfb68c7f4bcd1079fad7861d9e1e8a5af7e4f6fbdf0e1e9b7df38f316377244ca44ce7969ccf6f9f534fbd25fe84c70be6ee09ce0c0d7ac64f65d96ea426b1f931f77ec75f1a2818b13e3130ce6b884f8902e142fa73be844edcbfc49fa267f32e923f963f507ae8fbc17ac17bcda93a693666eb62c7ad2e276253ad34a559294e871264b1a67a2d6273d96bc37f995e40f93055f621cbc0cbb462799b04f70064447202d470ad8edfec07b9e3d8d83ebd77965017b2f12f35098ef99db382427b0fe46e6d02824ab215ea8093e15525150b9fc26a3d96831c61b0595ce979a94e6873fe5f4d314a7da2af98936c1e0a77a83d7e141918844b641aeb0cb00a1d9be0d02c4fc53e4323233eea12b1ac90aacd77052f101cc93420bd9a20801c216039b6b628210517f00bb72ecbcb9de0f4a8acdc62b5f890f3ff6e04d79f187a41bf2a7ad1d3bed54f42fd4f647ead206271cbcbb53a45ea1f6b69b6fbc7dc2b3cf9d682cae2d7b24676ab211da0a87865646fdab6bee3ddc41716887ad55c9503aabf80ebeb04e0e654a4e95c6c9d3b8f8d244bdcaacb1c38a19f4a674ab5932c7195c06ce7025de6eb35ff12c5a3f48c1c6d2be5c46a8ab0e2d73602afadf838a9999879d684d607aa14a601e2eaea2c2a297bc15bda6346bb25d3bcdddd3dbb37dbb58396236c7fd94a337bfb8f5ca02fec9ad9d18174f4647cbf80b901517c9c617f65742938be3c7cbe3d5f572837a936e5f52a7735f604fe691242d2c6c626abaa14f93ea7449822add69d7989d9ab81c2927474ce6731273b2d345479ece10d08ff10792edb979d728c8c5fe52260191f35f0faef8b010d01485ed31be6779838e14ad29cd67f47b53fc7e12742031690d1e1267d0e97dce543f0d24a5c34ee8cc1e85bb83d641f164a1454c738a0ae1d4a83ca9fe40a1b21319591c607a94c6384bae465b18d78b2877f79cc2a23de5cba3a70ffed5f08a3e30fabedf84fc7cf1ce752f462f53e9555af5d31fbd5ee3db76f7f11bb2a26785ca31de71ed570a7ed97aee89e7eb02e58fcef8ddb4a9ff4b9d544f73a2bb8ff5dcbaeba5d7bae66fe0b2153e6f0051994d4924d34359d01ad92a59e58010b0ac9656cbb245cf59120831395552824ea34fd7c0db48482789f037c25475d8332f6653187b079d3eb01916052125108a34c29f51b6541061f0798422aec86de80d15cebcf7cfd3b38fa4e4b72f7fb9573c1ef9e4464fe9730d4f456ee49e6b1d59bfeb83c82926871c4e31105a869582c5fb8a43c9d217028453c52b7b28c86dbac4b35dd4feef46d21729ef1b123b25c2c39c2ab68fdaf00a7e42c6e50fc4d794fdd9c0b9e8545aa2f46d2273582cf1285ec6bcb299882a52440b25164d6451c33357a3861c0b1272d7440d0dacc0407f3e144634b102935240380410891240a4907104c898ff05a6233816a025bdbdd167d7e6f7fa2bbaf54e97d07fe61f2304ef6ce1e5cb23578f9ac7f118461b78f32dc6a7257b430b1a383a4aa6760e06c0aa9a292e12d7aad648ede211fe347f0e512e51057744cd731bb81f436978ae14db1141c4c772d55233b82a4bf86c2eaad4b288250ce76d045ea591541a95438fc8423ad16203d2e3997784261245831943cbe19c7dc9dc3178bb152c544801cc298313f10b8179668de23ae331a35c2e2b7b0b98ab952035f302bc543279db0ed25f7f195d480f7d19ed79ec201ccb03f4647459641e97dc11fd81227b9b411cb6e7e2497a08523618abe3d2116113c46b58faddc6986d8a4ddecdbdbdb1301b6804f950f9845a9ce6d9182a9364c9a08ab3ca5683352e200760e2ebec33b48bb43aaf4fe3707aed1a4eb0fa3c4eab530f174d9594ece32d9a208c99293d3e4c698f231d8e020d610dccf14179ed816098eaaf15f2f3c68bd8c60e4a173c326cccfa95286a6c3b1b9378c45f61e2a0e06360b307059f99e9eb35a02734a26145db94acb4f2679b3f9a9271f4b6c94b1e7fc591be7ce1de5e2177e70d69a32bd26a664c7ff2a6ad9191dc85dba66edd1379843bbab460e253bf619ac1c31ffa58f0c01fd2615f66270f870a77ca3b8c8f273e2f74ca7b8cfb12c3f229f943e10bc39fe375a36495d326e99c66ad5db2db13b8409c23491d48b03b92c2540daf6870d55362c4df7944caf29645ac825f6b51638532717e2a599113f5c869e2757e68081239114e106f40a2ac612c61ce4f9ab96850dbe3130bcd58ad10512131c7e7d38d79935e7d7ec78ee77048ea4af47f7f17bd42cd7f52ada2717b76ccf9f1959e03e7f973d1bf462f4623d11769e6156aa021e6fbb4466f167cb0fb06440a5785b2f6c97bad5c5076279b0c2a678214a7323893b5a9062e6073a469728c399ef4d438bb37adddf35a6c7a10e8f3b1655d59c81109c4aaaecc31393189880ebfe027499898988884da0d7ec25b953929d3628e4f1a2245cc7eb1c8370247b430c6671c4c61fa1cf0074c5eeecdbdbe9a578f56fb904673ba8a43b7fcf0e5e82bab76ad9d9657d6bbf6dd77da661f3aba60d7dd33f7f087b68e0f9647ff8c393ebbe3d6a294f1111c4fe54879f466be1fb26c80df7057a8b0c45667abb775d2bd6267b22a289badbcd6e9962c2adee9d0261a24387889e909f10e443c9d88b95cb37ec1142bf31d9aeae04c935c3a3d8c979f4bc2fc742e242499870b92a21df4f0065d3c1617c3b233c8c0c1b5ca6b52bc3cb83885df06aa7b7e561bc81c1f5ebd973e744b41ce8197b29fbef340f4ef91d374fd9cbddd4d8f3dd0f8f4dbef7163c6a5d56cbfe4e7fc7537531d4e40513ae1aade738f629e267243c81fe0fdfa917cad2018642367509bd4ba802c423b4d1ad961a1cc872576b3254caba18831f782cd0da609b39b5cd117e963610ea67f83dac7b46ec8bf80ad3890f0d3db449bd39864dcf42854eb48f1131cff3acf75ad8cec6434af1cf8907f5998085f2297e6841e2a51ef1477981f8fdf99b03343154cf3058a3d359edab4dac08cb4998185698bfc6b756bf56b0dadde5569ab7cabfc7b523ab32c3c5c3b315bc8b11047429235d996901d9f138cd32e96fdbe621fe74bd56b844c8bedcd64a745129c39bb32b5b992da60e42492ebc975b86c89b680754cd02f05828e7c832b601c430239f6bcfc9e217f9445ce147fa4d4881c9b6e692e52acb28cc1f04d1413b44291e649349bf327e0f381c7e0f210b55ff2503e0b07d6107bf450a7196549f1360f75c7a57a8827d5a097031a0ff5fbd41a9a8d40a52a1d498a29d943ed894814b754d9d02889a20a57151cd1b8a1006ac09fcb5c51e55b903551f2c6dc52a6262ecabcd7f8d882f795ecabea5cb07374e08e87368f5df5db237fbf6d1cb75ff48f797ce1e2eae0943b8f572efef8f75f9d94e82b74eaacbc99336fa94e83279f9a31fe9e9d3fdb3aab657441ed94504d86dde2cccdaafef143673e7e86fb07ecbf75e02b4e2dce82159cf6923e4773cc80ef1115219f90586ae555068dc901f38e48793a493024c4f12e9ee3af24225a055f75703718b9de57cd65463d52de6f8c9c57161be6a1327dc7ee2d812980bf88b9ab9d2f1f38e04fc8d7a7c4bbc605d6cf7ae4117156f4fd6d91ea128b96725bd5f23d8bb813db14ffa56de073fef7b05bec34ea9cd0a870fca9784e6d91e3ed167b7c507527ff211667221a3444a5d788b0d136c966c316334793aed33a1c349d0df69dabde8df2218f893fd81fdbb95694338160a28f8f50d74679bc23957d02b862f2d11247de7d3fabf2f5eee7bc23166dfb627a360b55454aa78d98db39eb279ce1f2d9a74667dcf4f8b4cddc470e2cc9f03708ff172197c0bf0ae554d21308222c222d5c0bbf48d52e6c12f7924e4ec6494fae5a9820de2f6c164f0aa744797cf08e208b72624951b6010850840796f76263e416c2f4be57787ea999a39c887c284505af046f12552c44118b10c155d1b00811dfc5bd4a99d7b7e130ed52d963f1d04f3f1d8c887e171ec2b4cda512dc11e394f393a5d82d13e18c908f4b57c24fe9d7849fae760ee7a70be1a7a17eff59e049948c99f84364035bb0c6152cae0187e6139a42334f446f3f165d8d28df4ebee5f2595088e2bc2d117723a7a3eed0fa5a61bf1aeca735d2786d3bdf216fd4bcc5f5f16f4aa7e53735a7b5da85d212b959b358db2aad955b356bb51ba50ead86b5e56af93bc91a919f194c0c62a72d94d132e121fa90a0ba3684a45242489ac110d2138820f52182d48700d2130820319a5f1b4d1b1653bb1a3e6a6414d289a08d8413bb6644a9c4766326feae89273d10b2b07892a41244d67028a6f440c8c0624a5a1da6ad3c1a0bd119d7f5d944e60eb2e0929241bca96fa884c5e856ac5801ef30892b4c62b4d4829c1ffefaec5beffcb6377afae8b9778f46df06497bf949578ef0b597cff2a3afbc0182e225caeffd894bfe716b5cf9d7c4242bf86facb96d2cc3eef0e0cb10d57b07a8fa6a7b7657a547d371989c7edb7ca55ffbc8500d7b8cfdd24533a9e44ac1b919a401f72e9617ee202140dfe07d04eeaccde7c20b6436f24fa26d0ace823f8f3b6b7f1af9fdc88f901e243ed4e3a83729c3bd045087fa64dc470336d0930c10e1c0b958e437abf6930d28676d5b01e5dc7eb21975ec5d56e06dc86bd137e40a67a9eb10d2cc438cf538bd8053cac7f919fcbdfc7e214b088b0e718ed82abeae4a565d505d91d64b7f917bd544dda9fe56e3d7b46abed4cada0dda2bba1fe8fea80f1a6443bda127ae2eee1b8512e96407fcc7dbb09fe270b6de88cd28912e689c9800a33893e818e555ece4fdc489f533a78ccfac6bbebdb579d5e2f94d68c101f043cc0e81b97ff203e5d1bf1aab3b3b9f1f8fe835b37d4ee286e796a69cb9cf222538413d0a67396a9413e3e3c904e55cf8649ca9be0167d7a7e134fa4d38233e83ccc47f143490596c6f868d994871942693fe1cd9310846a7a35346a65b327f4e8fa2b3eb4b7e86175c5b427e4e5f2693c96832129f0aaf3ef60a998b37b292a4c18e70660b6daeedfac8b0aec9abf435106b5626ce8dc40e2e86c9786c182b004580ccccb136b07a0f7918f00c80278be903642d6033e071803094db07ec087da0479043afd2b5c4412784b482eba678bbcba6d1badec126bbf729d7c7b6cf8f523b3ec17c46ed3d7aa21eaba1cfd0a7c902e2a2cf23607617042548771d4ebfdd351755fbc872401b8057524af7f5a414b85ea75910547c23a57e9222d0975d7fcacf767d911fe6688feb78202ce0f68b1460a138d731e753ae9f3b17b95e071c8855ed4f478b975dfb9cb7bbb6a52042dce37a94ed957a5c8fc46eab9d78f465d7d2f41dae05f94afda41d61ee408fab14f533425a577189c755e43cefca0d84650a3cdb39c99591ff2b571a1e4433373af5854cae64e736d72854a538ab03a30047e97efa04c9a04ff4f826b85e4516d33d3c3ebd644798fef0705d30df17a677858aeb823bd2eb02bef4492e5f7a4d2080fc8c53d206e91669ac542065e2dc3bfc2029498a97cdb25136c83a592363471ca62ff454b85447e9015201b21c382cab642c4f2fa250384a0f2a85075f91059993891c1f1ef8b497a907768a077aa119142181032fab949c2a4c0f1e8e151d0cb9a0c1140790586a84b2b023232c85e72d7390f46efa6058453626b656d82acc634ca53555ffaf64ae527335553caf7f9ed8a8b37b078eb076ef7736e0b4303203ce86ab4db1a1fffffc56ad4683e6ca4cb6081c6e5dbe64a172fad95bdd3c1787a0bb1f68c569f4b6796ef7a125cb078f76fbe7ce9bdfc28edf3635772ff73657752ff156b90fb52acfb1e26baa17b2ea566fd521b2b0faa6fa430b43cd553dada1d66a9c6e6e383caf7265e375efda3cf4ae9595ffe45d95acb395ec5df394e786bdab9155cf63ef6a64ef6a64ef9a179aa7bc8b91a07af1f4ca3b56413a71421a279483d3bbc7df38ab1eff08d05015a67bd8b1e9d584fc1f46e8e02e0a656e6473747265616d0a656e646f626a0a36352030206f626a0a393734310a656e646f626a0a31322030206f626a0a3c3c202f54797065202f466f6e74202f53756274797065202f5472756554797065202f42617365466f6e74202f554e59554e562b5354486569746954432d4c69676874202f466f6e7444657363726970746f720a363620302052202f546f556e69636f646520363720302052202f466972737443686172203333202f4c61737443686172203538202f576964746873205b2031303030203130303020313030300a31303030203130303020313030302031303030203130303020313030302031303030203130303020313030302031303030203130303020313030302031303030203130303020313030300a313030302031303030203130303020313030302031303030203130303020313030302031303030205d203e3e0a656e646f626a0a36372030206f626a0a3c3c202f4c656e67746820363820302052202f46696c746572202f466c6174654465636f6465203e3e0a73747265616d0a78015d92cb6ae4301045f7fe0a2d9345b02dc9920dc6101202bdc8cc909ef9004b2a3586b46cd4ee45fffddc521e03b3388623a90a5dabeaa7c3f3212dbba87fe5d51f6917714921d365bd664fc2d16949552b4558fcfe6965cd9fe7adaa517cbc5d763a1f525cc5385642d46f28b9ecf926ee1ec3eae89ed77ee64079492771f7e7e958568ed76d7ba733a55d34d534894011ed5ee7edc77c265197d28743c0feb2df1e50f5efc4efdb4602374245fb7125bf06ba6cb3a73ca7135563d34ce3cbcb54510aff6d49f351e1e2e751d94e2363a46ea66a94120a8c6d065605053638c9aaa160e89b96b58382ceb686d5408195dab25a28d0d146d61e0a6c872f748002eb1db1ce50607b37b33a28d05107560f05369a723840c1605cb90641811d7cd10805c635dc4ae15f30bd6cf8560a5919d30fdc59212b639ce2ce0a59998e48b1222bd3695d5a21abe2bc9de6bc0a59193398d20a5915b01eff13bbc8ca68f28e1559997ec6cda1c8ca746e4067bcd3d783f093f1687d8f82bfe68c2928f35706841f7e49f43da2dbba7183c25f0bd0c64e0a656e6473747265616d0a656e646f626a0a36382030206f626a0a3430330a656e646f626a0a36362030206f626a0a3c3c202f54797065202f466f6e7444657363726970746f72202f466f6e744e616d65202f554e59554e562b5354486569746954432d4c69676874202f466c6167732034202f466f6e7442426f780a5b2d353935202d323930203131383220313232365d202f4974616c6963416e676c652030202f417363656e7420383630202f44657363656e74202d313430202f4361704865696768740a373738202f5374656d562030202f4c656164696e67203330202f5848656967687420353931202f417667576964746820393937202f4d617857696474682031323237202f466f6e7446696c65320a363920302052203e3e0a656e646f626a0a36392030206f626a0a3c3c202f4c656e67746820373020302052202f4c656e67746831203131383132202f46696c746572202f466c6174654465636f6465203e3e0a73747265616d0a7801657a09941c479966464466655665555665555e751f5957d7dd755757df774bea9625b524ebbe4f6449d8b27c8c6d6c308cedc1786d6cc618466073190f5e1f3c060fb063186cf02e18f02eccbcdd6579fbe02dc372cc0ec30cf831eed2fe9155dd96774b8ace2bf288f88feffbff3f18c4308cc8dccd10a672ec86231799ff841270e66bd0361dbb7c29065bf82181fe3d79f1d40d74bb7e7ceadc6d27dff7da18df3fcefff0f48923c751f5a99718a6f063e8d43c0d27b8d7c8ab0c5354e03879fa864bb79eb995fb061cb7e1f8dcb90bc78edcfbc3875e83e34fc371fb8623b75e6432f8381cd3fb63e78fdc70e2733fc245387e0b8eb3176f3c7111b6f02b25fb5b06c13ffa733236661cb67126c0b070cec6c88c0063723118ae698c9d51193f5cd3199e713012e3663c8c02e3f63106e36538b89330bb1806af1207ecf17085f1c5e5780b9acf6cd476a15e0f2fa273bd47bf70d75dc4f1d6ef97f1ad6b5fbc1b6e83deedab4f90e7c8ac75df7e980b353e86e3aa2f2161be1121b571d48a203d827889f023689cb4d4123125c4d7e000b524a2ca0af4aca73311381e4371d51c470d193a9450669ca0e3e8c9dea17075657632999e595a6dbb34a73be1f51037e19d3e69fe5dab5323ed786dfff90f9d9012caf7925b973a122204dba3a375bb9c2af985dea1591627f7768911e81eba6e1aa52e5c28df7c33ca624cd014341c5dac080e8c1507b6aba9084218e32942108acc4531467c301677f56e0f257507d1c2ba0d390ce5f4af871623e8406e7bd11372a437ef0289c11c1ebefa30f953fc7f06f337c32cc05c986a17374c5fc354393a4a6b480d990e1657071380fa13d05c9f005355341de6608c982abafae69b536fc1aff76dbb960cca312394311c2c9b19db7276c7e653cb63718eb5c7271a36870a43b5bf45c832216fe297befef5defe175facd476ac5c57763b71746cc74d3b51636c4a273038829595dadae7a369d581f5840c23523c7fb7bfb93b10b8beb1bff2e28bf0d9cc89ab9f20ef257966331c287cbcda6c49288c061f86e2204a3a14ae512f61905b5fba6abc614951af6abc2d8c2cf1aa259c30d12a41f1ca9c0cefc69b09e97dcca66747865c116da89bd59d2c263656a9ca4eec221cc148e438291b76b2765f7dbe9a56f46277a585fed7be798c1da6f2cb642d1268a67ebbe9ccb6b9b4e4e523cdf195f38f9d286d1a1dd6580e2187803d85ed3357724d050e9d9c68fc10233c0e32edeb6af1ea93e4abc4cbb4981b9877c3d8aae318f411997208c1302ccd33df16140c47830e7045b365d2361e94524d59e3b229bc4dd3617079b43e172d90a0d6bca633e1551d044fb0d9c827bdd1b9467ff84f3a22ad9294559a13513bc7b58feed99e151549e27dbe4ea17ab1d2fb437c28e8b323f861973f9cf29f234e25a22971458fe9120f7ac972671eae3bbc4e5194eda562bc9408b99f7906fd67aab8d32cee3d1f2c64336a7e47115d77f0bef387ea52802b6d3ff5c0c9b3f7a558d6a5493ebfcb29b32cb2f1227975166114dc5a296e32e17d137b665b11b03c3d5f9f39fa81037b6e4fb3d9a82f22bb1d36c20a3681c3e8be3fbe06ee0a3399ab9f25df207566993906b3b86eeafdd904bdedef80e8d50802ddb1f1b604e84b89640666bde112106fe32344f7812750cd06d83ef518d015a611fdacbc34de0c45466676349bdb673b91607374a1b4f9cca6baaed7379fde9cda72e88e9d5e76f98ea39b82f5edb3ed60784bc915d20fdcecd062b2ccba045d141c6abdd5d417eeff6c69746d6fd718df73dbaa3badcffab2e88b08113c01e60fbfc1ced3746f1c84f49a8db3d9d229446c9ce04f56139d133959b18fbd7ca7c0632c0a5cd20c1b917242e5f0c38865bff3d023dfc32c46e1613dae2f80ef1cbbfa2c798664c1c76a4c84c93215e612f3b7304b7195fa42cd3216707f293a4c4d47fdd1ea3ccc0e551e3a4519e84736fa121e76ad230d54d03a4a435702b7a00cd549d8c024438fbe2a461007271b11546b98b2a5d2037f43a52261c5f237e6c0df34649056d3f2c9e077ba545a600f799428e10639dbbbefaf2235af5f9389c297537fd67b4b4afbd3d54cefbf38124a458bbb9e2336c1c1172555b86cf72b634a541092b3b17052c632e7893573e1b198161712612f72f35a75f7527a39e6720d0b3e9f6ceb1207f7a7432d8d75da45dd99de1135aac5b4133b1da2867e17ccdb8dc5e253b3828d70a1726bae565aec56fde0b99cc9e92676c8b94644fc54aeeb1743f98992149e30029a1ad4af0b843d2cfedaf787f6947c364eb293d1512d5af747aa9db59f17769abedcb65c3438944caaaed4e6ca87ab7b2beef4c13abad8bd7f59155889c77279e71ca88339e28dd8582f8b7ddd1b0f13814bc5646fa952f1c952dcebb38982167123543857c1ac8d67e59484d0af00273a2c7b76ed579553f3e8d9cee1146811b5473152182facfd2a9ef3dbbd298527bcec424f9f65d90e02c5bb3be8c2648460d665003d208c0938fa2ac9314f327fc9004740fc38194396476d3535107c5f4ed4a8fac8090eb9d64ad82c906de590257340959699e01b5a0d0409ea30102d5c25563f50b26b3c5a826a8fbe61acfac0652353af5157aed1f78227afc9665f4b899ed26c26bcb2af6880e560a63f9904b920c22931c5854570e16874dc70fae38558ba1c91798e7d81b4a7746720510893682eaa3b453d968f926e22189d572a2a6be358941cd5ddd5e39fbb77cffb8f4c15420e6722117712cec673bef270459554a768734bba5fc907b8e4705cb33bf55829c1c60a71c369cc34b82f112e9186973b0f6e421f9effe084995b9a9932d7de7463b794d522f572d1101425bab030179174f406b6cb46dc783c9c51a8940032045f2c88d90e08ed3690bf1c577f770b21a3f4e2cd708e9ebf4398b26136bde796bf00e92d5073877f042d60200a671fbf696f81450ed9a96a76c1cbb21c6b63efa4e2a7775e8636cae2cb37c2eb58fa367bfebd4858fbcae829b3182897ca81c7394d5602b9247c9ae1742487c793b217c0cace5cbefa387937fe1d68468499606698a3ccbdcc7dcce7403ba82f8967400ec094ea80bf23a83682294453ea50020d1ec7f5863c4a85f64e9e01c4a2114740ea2ce0935008742884f4b499c336be591f43a370076aadfbf175bffeb61222024e055c137ded3a83b129d66b4131a17f2bde88e3a77b4fa093cf0d4d2aa0172e3d68fa0309bfeaa2ae56abf61e9e829981b995f2abf7bfebfc07ae4b4b742a27d73e806fa7adf7edc26c33ef83e95bbed298ab247d18c72f9fce6c99edf822adc9eb000aa63b91706374699823ba2603c4dbb8638f3c722fc6ac9aad7433d5c97cc40d92f3a54adda1e878bb984595de0f50fb07758c9d869653e86738628aae3b31eebda52652850806cf150c78e03be07fa2135efb7e716bae827fbeb6051da072145c6ed57325107583d5ba03912bd1801b939d7418cd7504b176ec29176806c89873f5feb5800477ca5fd3e9fb10928271035c3b5c45283f06f265c1deaf80bd8f032b9f66f631278071f77db965563ab578de72df5cff0465d01689ee4b07d1d9b66cdab77182fa02d4874f422c5c21fa37955ac01556332b536587129583864d5223da2c6743dfd072c900ef1bf68858727a78c11f4b2bf3bb9b096ffaa9bb17cf5dd7f27aeaa3d3b107eb2b9d9c9cbeffdcb147d2a93b8ec931adebf0c9f25d81a9623b98898665f47b7339eef6d9e2e76ebfbde292589bc2b9cd91d25a7bf84faebfa774cb3defedb83c307e818feafe95c3173a206cbcc8d93061f126cc0221dd074684176c1c675be0b8e1a5a180bf582cafbd74f6c5d5e0c4a1d51d35caadcdab1f25af901ad366b63067988bcc6718061caf35dc3e051db8aac154504eda9fcc75324ddd23ccc83822d712d9819323164a0e9cda35dc04ee8180401f4713035ccc005b597ff2fa035bf0d8c13ef58716bc131eaf4eac9ae953ab9453e851a784442131db2d4b91639df8add7a93e6ecbcd5182a709a9bdef7a41f16cfbd017059881250ebc47c970938fcf703697cfeb758801efc8cd5567da29f18aa06b4e5d448b81e99cd970dbcca9b43f2b37166b97e7243737fb27cde6fb57058fbcedfe5672ccf0a53c4ebf1e68964c5e0eab2eecb1399d1ec7ff44849de2881cd0fda0ed083b054e9e822fb4029ea51b231e5d2ede7bfdf7fc539a36117c75df1d15d970c66e4157b86cf1fbe6a4991e335fa82d6b228873ed4cfe42135dd4cd48d823abc2e4eb1f17248cbd029b28054a5a3db3100ecf0d95586e12647b6c3a9213e5802cb753d911dd0bfcb3920ccf9889b60adfadcd6ddd961445405b0e0b7250edf3f4e1ab1f225f2609e631e62f98e7c1df5de381c2ebb2a04c4ab14839501f082a33aab545993efda4f1d4400936422a646e08194446b966c3da227db0edd298340e3752650187daeff38eb7f7891b7401d590fa5608712a481ed709abc47d4e4ee2f8f77e211c7ce0945be5795e1087a387de1df20e6f39b772e1bea4f9811bbb978f2ffb3a3f6b3f735831b8cb4f6793982cb16c416d6e3e39eb3082773e91bbf13d012fa41fc68f7f3495fec8c9fb3f91ccee6ffd3650907a2fc7863c656538a12ede95f036b79e9d8757267c122bd9f85b7affa4cf1452438b41ccf03664b70bba598e17bdb94ccceef6e34fd71b2a0daf2491f3894f70ec34d89f9345768703e1b2093c16c830b8b769082f26581643640cb498c7dd59bb2bad9b17a69ed1f29a5e509e89751af5a0e2f704b636ad9b58501e7a0fa0b8752fc745e663fea47b2858d075e8314d1fbb1b5ecec2d3ad973f6a04c703d94037bc421c0e5e50cc60d211314dd94b610ff873e6eac721b62802ee0d3145e64e90ff4b0c041d14f300d9204540c5416ad4d401f168f6c004610db20729e845d63da325b75a3f1ced7b06884d0624e7edbd3e7081a401d0fa6a406a9cc5ab80416f881e78509f1b9baa666987dc8f86ad54451a1c4b866a0ca845abcfd2e108544923e5de6f90aff79b19b06c122894ea8944a35408606c0fd672881783091fdfef817c7f99ed94b29a962d75321cf6999a8bd8a57cb79cd1f54cb99b372b1953d3cc4cc56c2e364b91e8f0c875e3c3f323f568a85ced66ea8b195f2a9d0ff4e64b63c1f04c9d70beac442951a61d08b4338b9359753860e356384e1f892985f26a481ad24bdea4fe0b11b8a1e8f57b219b624445134d2df79ecd2e457e98eca8ebe016eb047aff1688ab8237a4d888e0752fdcb6824deada466897c0a88489c83b83e98d539b00481b14edb60048b62cd82388d2a12d809a0df03ce801967aa2ff16ca0e48983dff4b38c3b295ce682d04effb456a6fb1b8378562d337567cf6ca632f9bb3b1903e5ab9ccba39ec0896920e1b9bde5cd0a9ceb04cfceac7c8b7004b39c86e15994f82b6c0e49b6a4be16b8dd4065b7d1b4807d03a88ff89091cc622ccaa12c1c0a0a89b8000cbc6a7331b375be2250006d42d8c937eea83d49af5465fdd50ed6d249208be1e06ffc55f9217d6de6dd70dc3e1d6bc6a321ef37a1427078906af8844d62e69f20200f1cfc6f7cfb70281e6dc814934f29e3635430a92cd7b2689c3253bf23b76eeadfaed3ffaa09cef6c2a3bfc6a6553b7ac1ad3f18227e80fca5ed5367e2bdc44a9054b42adee64064d1f5c68069a9f3c02a48828119f979730d834da1e5dcc3ff2c8ceb147b09ccd984e4916b5527b3a971aabe555874820b62562ac33bc9600df8e47e8378c536eafd52236ceedf74735231909fb382edd2da7e54821ea63d3a1c6215f2291f4b90d51f4475386be58881a89b0df15de3c0ad6ef0d474c438f057437812f8cd434eba12c9b28402000f904e416b12b3064d97de0eaf3e475b07b13b8ee1ee62ee621e6cfadb8d91204b87798766a540422dd7e6822111f088dcfb4d6f351d7e02f8124e3ba1bb02c9ed0b48e65cdd485f42fad33589a915c8f6906f00f4a605933d005ca32f40d5742e903e80004c9eb78430322da05ff9d33d3ddde8a19e5b94a4c342e1eb4ebbee64a2be51231573780a8629eb71f482ce99baba5ab8c4d5674a712f7060b71cda6c515d1e6e29da293ff0fde587ad834ab99b8d71bcf544d73381df3fe99106a1478299a566c995631a3194395896271bc9c35d4647638718975ea9998cb2fe59ba2ec17dcb6df2885b019ad1abf9b3e904e1f982658356527b1dbec13bbd3e9dd13ad2d3173d7d4f4f56664b1860e368f1f584da9a9e15ddbb7e64ebf30e6f189d1996dc727ebbb875c18c90e2cea6948ca91bf773fa4d44720e616d9c4be33378d08024680f7ee603ed6fbc7754fd1a63ba033bd2d9206bc52897900897ceed7a963e8c079f80f1b84f66031daa93824aeb85ab4bb30e7bd909e49441253293c0160320960929f1259e2005ae098199c390edb710e7ee37019e2a361b0f92f93e620dfea865cb60e68f1ef988f83f54394d282dc9baf9f7fcdf4377d4a8cf441503cc852414e2fde8090d9ca9744881aefbbf93cb2ae0f622ac0082bc2a6918f853e8df8469a10a0c7d7f7f9030d05b7cf81bed2140d7aed67f0dbf3ca2bcdf5ffdb5d5165540bb88f3fb74534b253f54809f20bf1955aefaf5647c2a38d57c135425831b543d7af9bfe727169acee072bc181f6ccf67a6dc74c07922004fb1b638bc5de975cfe941fcc325e091229f0cd1bc07879d1e9761aab63bcc73779bee541cfc72ae54632906955aa26b6358a1313bd959999731313e8456fda6b84b361968cd05cc40415104c2e3a72c4d665c944707a664cc3a40d5e08de8ec393f9c24c1876e0ebc2b3b9dc7408765173c77b4eed2d41422c7bcb97d69e65c9da1207b11c22822017823290bb56497d40091bbabb2c1bbae105496dbafa1972851420e689d08887ebe3f93a9c03c652506dc4298a6f30f656df6d43400459b341c6d9ca888025f66535607cd7987c03456639c21bed85ddadcef5b37595e7b885de2cfa9ad562eeac2a07bce161cd293b05e24e484143f46b6fd04988a40c3bb63b9d471eacd51e3c32756eff75a9ecee93b76fde7cfbc9ddd9ecead19b16f6c53be6e74b611ac9d19fbffa29b39d98f8c32b38169e8eb9243e3657f60f25221ed14d6cb2901b9e8c0c857d361bcc989db345bd370127222cfa1450713c67b96d1029ec10f4a93e96cd5d7d8c3c85ff69a0d7e720d281d980dca8d5febf5c2be093e5df2c2a04d13f31c11559c9444a63a95e5a4163df73aeebb05581a0a519fcb35e05fd60a3fdef85b3bb179399dd276edbb474f9d86a36b3e3f04df33687c3e5d87576be1c24c061e4682217dafebeb162a03ab1a31becfdc1a54b4e222a71d993ae4e17f86030b17d65560964e3110fedaf24f39dac1af9b743bf3ef4cf2740832077324d070b451a62ed78742de803c512334aef49250b7414a1f47cbaab26d3b9505674db3808d9dd6e291849a87ac8c986f3553ff4b10f29e890921169f77093816a57f6ea47c8cba402f3e667cacc14e4a61f621ea7cc3165aa351f2d44bc9d24a3533681ae3947ac6a0c9c02d8e887d19430d294b565c67d16d032c1a7ac53d07e8a0d74105416341426b80f1583cc5b1fb52cbc02898032ab89343c6e0c991a00111507a215034a64217383358b79684d34f5c20b652910cb06829958d0e50ac25e201b0b00c5f747b3a1d0502c2849c1d85028948dfaa50977a1d10d75de158b9d6b87ba8d82fbdb768f57f7e4bc7e9fd789ceebe5f66cdeae061dc32f8c83a6e12db395f938f5c1e156a79ba8747539a2142f2c2e9e2efa33997cc89858d86496f8d53b4a5a3212947e2fb4d0aa680f36f550c6effc2892a2e5a818329a933cc70bee702217c94f420c8b91cd1b0b28a5573e885c54be188d50178630503fba051e012e04e125782f308231d07de8c4f9c2a18892d0a2f077ed3e359dcaeafea0130f2fc6268dade971092a0e9e5661449b0ef15a74285439330d778d82ce90ced6b15ac89b082a6cb0189f39af65f3b558e220eac84948af60879e8d3f31ba6fbea1cb00ab8b53a5a5307d2fe1dcf1d2f4b007a23384386a672253befa14f96bd2016d0943dd34c52c81c69c83dcc225e6639477407e9eaac27ac081a89c6c0acd97a5d4da183655aa5670e61d1108a4d3afbd87e6d339e8a6a7014eac7881f4ab7fc8cab4f75311f439b491f5106560c0b448dad7133861657f685ebf6fc8eb67f877dc8332f86fb4231393c7743ae5fad1c989a31aee7db7d7435f78e38d712c84478611ef486455fe4b6a56a5b2f2e6d527d4726feef5d7dbb9fcf3845bb4c8275262818f07f21e2accfd6fbcd13bffddef3eb574b0131104253952f2188e0ee7704a8e19bd634a017dee50d86b4802121c323ae94d05445df1d374a6ac43d2477078dd4b27e6b24e7ba8345b5542ae36dc01c2658927223e2a424a0d769d21f9513978727a3ad76effb1fdaa9c0bf4ca464275c8111992023e2fca01931c37260226380af4a19132f9eff5edc3fe42be184077e979481162ec4bebab46f1a57abd8d0c8a911314d6f482a65192ec89cda43d0eae7acb0acd347831f12d86eb31bbc8e995e6445a84eaafcc72b24e084ba6e97dc690aec72b91be3f5e81bcfc136414fc4a94793ff361d00cca2e2cabed673e077ad2670614b6c01dacd759a8bc20e34ecb2c105a58b68e54c8a1973078820d015b751a086c4b504db5ea5f706523950784055c1110ce14f8145a424ae730baedcd37ef9cdf6e81100e1d6db54e022263ec8ee7dbc8e7ad861a4bc37127589d109dea208794abfa851789d350f5accab19172a6a8f3f788db77089abc7b0b797eb360a348e68c55171bcebcd13d925ffaeafba484dc3252def90bfbb624733b7b23a2cfa7889f0835a4f88e3d7bb7bcbf0cd50dc51fd7ca4d0d6482f491a15c59a34f410fc5364120085f230592c3c9dea3feb8ea62950814545c01057de5242d24da6c1ebfcbc68a3ef7edcb67b372d8aeef1c5dfb8fdde3b53ffaabf9841d28228e2d435682cc40c05a9653b18e490163069a1c33d37a6a6b18163e08ccf8d5bf269fc7bf1e60659f031e634ef519607c40fb0038e93f0aa356a1b16fd894b2cb347b5eebe7792072b74cd712e37a640072a0755e64f62b29b0dda8da1380647ccb934ff6fef9339f697ff2938fa2eff41a6fb789dc626cf9ef1f7af027cb91851c299edae43cf795d1b1972f48d77f7e0e1c2049ed3e70a452397260579a668c093bffdc3ee981d7a667bff5a8ab535a1cadfb099ae835f0a60b172eac7d742ad93d3ed1fb31cc29a45920d702668976c5c0c8dbe0d8c6098726301b1c0e6b5d832a01c6c196ae77fc2ced0d5cb90d320143eb3d3346f3ce1c93befa29c8af94ad397b8af92cf32ccc96295b2e68bd0244c05d5938d7d750cb75f5a1efed9519adf5f06ac0c1a02bb5897e780430f9ceb51f70912a310028f4b352d4dadb85a70113df281c933c56a1e8043dd51aea9e3e8deebb72a55b9fc9190e3158dd36fdc21c675372aadd2b62dc3d0d092657d82306e478b39476ab90ee7238b3e59a5ede67a84b39b9109e3eaa11bc7273e2d0a814ced4938203b2748a13b978b7b3b3733c2fb77e2aa59407a4b83254caa42a511ff572ad5c6a38aec05e7c3895ac253441f0c56bd9ee952b8f101bb9829eeafdcb13a71f0350eb52703b945fa9fc31d2f43b44eec6404b5e3c27e58650170a7e6e55088f2dee6e02e422a89251394f02fc4d9da810e2c9052e6f7df5ccf9cfc15350b8131204164b3c1b4f50d379fc506e5b1686186d8e51eca499948374a7090d9da67b564ee5347a3fe7e4e621164ac27a8d6f9286e5a34c26c3e4990799879957993f52d902fe0cb00cca4283409662991ac21a051e88926a2add82afa219acbe40f97ebd116eef1f4387c18eb544678c3a3a9af782d2238064e39dd526fa70a4274a0816486c605518d71af41d565d912aca3a7851b580060f1c94ff377a70eba1363c84bad26b936e700857ad9bde119fab64df6db7d542f564ac1ca2fe08874b717d025c65ef6b972e6d7b1a7e7651f59ac1a87b61af691e5eaced89c576358777c662dbebcde6a1787c7fa7f7f3698e97e3b22fde9ef08aaa13661d2197e18f2a4a4857a1e2ebd2f4b092b77b0527b6732ef169a82be06cc84180e18773e9b4df9f01f2dafa48bbbc54d4b299219d5e0edb59b8dcfb4676d23026b3a5d160dec80608b799235a31ec18f2b6b76a4393867f2ecf71be9cec264e586ed0da3504efc6bc6af81d4a954657581f521c7e43bd79b5f7fbadd91574d86df822c1a49f659bac0d2f13aecdb25b5916feaeb0ec28cbce97ce8f3c663674bf3079eb1c92aad6cd3c1d0f8547051e0a8448c0b0f806bb219fb3bc8cee8b0c79a0838b239214a7c1fc286550954ab50afa0bfb30a1193704ff5687f0ad70720a72bb95d4482403a168efcf33472b95a319b4ba70bec9bbc8e28bbb073dae3f1ee15851b227348aab848941fcfe1a9984756805e67ad0531a6a0fa83154a921c203ba03e42a82615d4ebf124e4f928d841c087f43b5e80ed1411780b04301f4769bcb9d9eae89a1c0e8524a6033504d464b020b1f8257801c4022a110f5b2bf48ae9eb86d51508dcae13d2be1e12ca44c65094bacc847574602d34b5bd28ac10fcd4d8e45734be8abb6b1f2de7b8e6e49498a3dbef9a62fdc034b7478898ba77abf95cbb5aaeaadd687e51fd776aa501d712163faecea6c58098bce6c63c21cd9571260463d1c76390aab79259b8c3a200de04d650a017399819c6507ea5bcf11192cb7cc742076b9937981f939f38fccef11acc4a3094c892e9fb97639000d3c74adda0f8e692d8c1a433fed00a509b00d2b17ad68ea35410ea01ae0e046ee8beef7970f80612bb6f5cc385a5faaa635ad681b9ebbb13e8ad2574a522c360c2f8d90d4b55177ff69d70a06fac0a19aeeaf091a7c57747dd10be4ca33256cc5a8f0c5746582462abc470df96afb6bb2e6e2a0661b31240e720cc1d8a68ad36de7b014e93da2e5fdf548d9837a4ec58868a9f848b8f71b291a30fd19f77dc81ea8ef9a9bda56d6799766841454e87d558f1a3ed1e1d363069aebbd6e747467acb6a975e192177934afc7e135d7fe87dbec16b0479c1f817464285616ef05a5df0cfa0216475062c9cc74c37a33606402129e0dd626778d8dae4ed543c16a7753f503cbfedafc9185504a86729907529a6e8f4b1353a3c6f283b5cda3b5406f4e0818a612938e099a028953f1bf7a44c9e6f486d41d3fba11dbedb30fbef762d6e90e998a1db188433649f7ee849c310d94dc76c12d8ea58025234e8e05bdd28ec64a3c105da97d5e4fc4927ed35ccc4702e540d81836de48b4048a0f08e25a8c01f509aec2201a2cd7a0064cb000a57938076935b39e3278ef9087642bbe4aa3692cdf5f59fb5cae5bcef9831558538245b769c65d3452dcf6e48cb7daee7ea41f171052a78f9a46428d662ed263595184400d989d027c0e2cab6687380d2a7ebe841694937a501ff2fbb5827ebf13883c9d4ac1e7508312879d9a14a3ebc8525003ff5ba282d6df62455c96d652473fb07258f8744d52174ec28a506ae596eb1f282fc50d6be95dbf2b5daa9780531bb065410701e8b03c096a90fd5a6ba89ddb999ae644fef0fbf6b4154ef186a2de4079286a57920189976c76bbc3e63653696f7a6e767eeda7c93d5da75fd9764b62e258d29b1d1e4d0f1f88136fe354da6c15d33e41b24b4e2f2fc7aa9989bd89c491766e7bbc6af78723109012bccc72fb161fde1638f80f37eb7a4977c86e49d0615903c2fbbef5f2f373369e40d994886a2238fbe18f3c3a4b97bda0c08e0fce4a3e61eb170e424d84cef8bf174494f0633b6f139d2ec1a38b32381322f05f86eb8435f3070b439e4c2eebedfd54cf66f2c1c24a95fa58ba3618da3fac3e397bc83dfaaf8c9dfc829efebe5ebe7b631b804aebebb07a97ae38b11649c21edc43f65fbdc444d8efac3d7bf52c7bc57a12bd65fd178427ed82d6867618da09684568196863d04c6897075bba3f0c8d5e8b430b0c8e37c1760e5a165a19da0ab471686968496831681d6829786988f924b2a11bb1863f4dbecebec6bd645be1e78559fb41c7a478bdb3ea7cc57558bad5fd41cf5ecfbfc83ff1b57d7fa38ea8bfb4be3a888fd1d5ccf0a3b3f1fffe10ac7eee9fb7c16a3d66e7ca9e9d2bbbf23b56174f9cb9746675a6b8e5cca9d397fe2f893be8ec0a656e6473747265616d0a656e646f626a0a37302030206f626a0a383732320a656e646f626a0a37312030206f626a0a286950686f6e65204f5320372e302e342051756172747a20504446436f6e74657874290a656e646f626a0a37322030206f626a0a28443a32303134303230353037343235345a303027303027290a656e646f626a0a312030206f626a0a3c3c202f50726f647563657220373120302052202f4372656174696f6e4461746520373220302052202f4d6f644461746520373220302052203e3e0a656e646f626a0a787265660a302037330a303030303030303030302036353533352066200a30303030303531373335203030303030206e200a30303030303031363634203030303030206e200a30303030303234303036203030303030206e200a30303030303030303232203030303030206e200a30303030303031363434203030303030206e200a30303030303031373434203030303030206e200a30303030303033313631203030303030206e200a30303030303134373135203030303030206e200a30303030303135353835203030303030206e200a30303030303135383433203030303030206e200a30303030303030303030203030303030206e200a30303030303431373535203030303030206e200a30303030303032333631203030303030206e200a30303030303032363531203030303030206e200a30303030303032363730203030303030206e200a30303030303134373336203030303030206e200a30303030303135303231203030303030206e200a30303030303032373631203030303030206e200a30303030303033303531203030303030206e200a30303030303033303730203030303030206e200a30303030303135383632203030303030206e200a30303030303136353035203030303030206e200a30303030303031393838203030303030206e200a30303030303032333032203030303030206e200a30303030303032333231203030303030206e200a30303030303331323334203030303030206e200a30303030303231373239203030303030206e200a30303030303135323937203030303030206e200a30303030303135353636203030303030206e200a30303030303135303430203030303030206e200a30303030303135323738203030303030206e200a30303030303136353235203030303030206e200a30303030303230363637203030303030206e200a30303030303230363838203030303030206e200a30303030303230393939203030303030206e200a30303030303231303139203030303030206e200a30303030303231323230203030303030206e200a30303030303231323339203030303030206e200a30303030303231343430203030303030206e200a30303030303231343539203030303030206e200a30303030303231373130203030303030206e200a30303030303233303136203030303030206e200a30303030303231373736203030303030206e200a30303030303232393935203030303030206e200a30303030303233303939203030303030206e200a30303030303233323836203030303030206e200a30303030303233353031203030303030206e200a30303030303030303030203030303030206e200a30303030303239323933203030303030206e200a30303030303234313730203030303030206e200a30303030303233383133203030303030206e200a30303030303233353230203030303030206e200a30303030303233373934203030303030206e200a30303030303234313036203030303030206e200a30303030303234333731203030303030206e200a30303030303234363230203030303030206e200a30303030303239323732203030303030206e200a30303030303239383137203030303030206e200a30303030303239343736203030303030206e200a30303030303239373937203030303030206e200a30303030303330303732203030303030206e200a30303030303331323133203030303030206e200a30303030303331363632203030303030206e200a30303030303331393032203030303030206e200a30303030303431373334203030303030206e200a30303030303432353531203030303030206e200a30303030303432303532203030303030206e200a30303030303432353331203030303030206e200a30303030303432383037203030303030206e200a30303030303531363230203030303030206e200a30303030303531363431203030303030206e200a30303030303531363933203030303030206e200a747261696c65720a3c3c202f53697a65203733202f526f6f7420353420302052202f496e666f203120302052202f4944205b203c37353432323430313462316532663132613236646438353063336638623366393e0a3c37353432323430313462316532663132613236646438353063336638623366393e205d203e3e0a7374617274787265660a35313831300a2525454f460a";
//		fileByte = hexStringToByteArray(inputString); 
//		String inputString="255044462d312e";
//		fileByte = new BigInteger(inputString,16).toByteArray();
		File externalStorage = Environment.getExternalStorageDirectory();
		String fileDir = externalStorage.getAbsolutePath()+"/Download/pdfurl-guide.pdf";
		File file = new File(fileDir); 
		Log.d("Alex", "file is? "+file); 
		try {
			fileByte = convertFileToByte(file); //get the amount of bytes of that particular file
			Log.d("Alex", "picByte length is: "+fileByte.length); 
			Log.d("Alex", "picByte[0] is: "+fileByte[0]); 
			Log.d("Alex", "picByte[1] is: "+fileByte[1]); 
			Log.d("Alex", "picByte[2] is: "+fileByte[2]); 
			Log.d("Alex", "picByte[3] is: "+fileByte[3]); 
			Log.d("Alex", "picByte[4] is: "+fileByte[4]); 
			Log.d("Alex", "picByte[5] is: "+fileByte[5]); 

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("Alex", "Failed!!"); 
			e.printStackTrace();
		} 
		Log.d("Alex", "printJobByte length is: "+printJobByte.length);
		Log.d("Alex", "fileByte length is: "+fileByte.length); 
		Log.d("Alex", "totalPrintJobByte is: "+totalPrintJobByte);  
		totalPrintJobByte = new byte[printJobByte.length+fileByte.length];
		Log.d("Alex", "totalPrintJobByte length is: "+totalPrintJobByte.length); 
		int i, j=0; 
		for(i=0; i<printJobByte.length; i++) {
			totalPrintJobByte[i]=printJobByte[i]; 
			counter = i;
//			Log.d("Alex", "i is: "+i+" counter is: "+counter); 
		}
		Log.d("Alex", "i is: "+i+" counter is? "+counter); 
		for(counter=i; counter<(printJobByte.length+fileByte.length); counter++) {
			totalPrintJobByte[counter]=fileByte[j]; 
			j++; 
//			Log.d("Alex", "counter is: "+counter+" j is: "+j); 
		}
		Thread threadSendPrintJob =new Thread(sendPrintJob); 
		threadSendPrintJob.start();
	}
	
	public byte[] convertFileToByte(File file) throws IOException {
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r"); 
		try{
		long longLength = file.length(); 
		int length = (int) longLength; 
		if(length != longLength)
			throw new IOException("File size >= 2GB"); 
		byte[] byteData = new byte[length]; 
		randomAccessFile.readFully(byteData); 
		return byteData; 
		} finally {
			randomAccessFile.close(); 
		}
	}
	
	Runnable getPrinterAttributes = new Runnable () {
		public void run() {
//			while(true) {
			Log.d("Alex", "run getPrinterAttributes!"); 
			
			try{
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost=new HttpPost("http://192.168.0.100:631");
//				Uri myUri = Uri.parse("USB1_LQ");
				URI targetUri = URI.create("http://192.168.0.100:631/USB1_LQ");
				httpPost.setURI(targetUri);
				httpPost.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Custom user agent");
				httpPost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, true); 
				httpPost.setHeader("Content-Type", "application/ipp");
				httpPost.setEntity(new ByteArrayEntity(printAttriByte));

				HttpResponse httpResponse = httpClient.execute(httpPost);
			} catch (IOException e) {
					// TODO Auto-generated catch block
				Log.d("Alex", "IOException"); 
				e.printStackTrace();
			}
			
			
			/*
			try{
				URL myUrl = new URL("http://192.168.0.100:631"); 
				HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/ipp"); 
				connection.addRequestProperty(CoreProtocolPNames.USER_AGENT, "Custom user agent");
				connection.setDoOutput(true); 
				
				OutputStream outputStream = connection.getOutputStream();
				outputStream.write(printAttriByte);
				outputStream.flush();
				outputStream.close();
	            int responseCode = connection.getResponseCode();
	            if (responseCode == 200) 
	            {
	            	InputStream inputStream = connection.getInputStream();
	                String state = getStringFromInputStream(inputStream);
	                Log.d("Alex", "OK: ");
	            } 
	            else 
	            	Log.d("Alex", "failed" + responseCode);
	            
				} catch (MalformedURLException e) {
					Log.d("Alex", "MalformedURLException");
				} catch (IOException e) {
					Log.d("Alex", "IOException"); 
					e.printStackTrace(); 
				}
*/
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
		}
	};
	
	Runnable getJobAttributes = new Runnable () {
		public void run() {
//			while(true) {
			Log.d("Alex", "run getJobAttributes!"); 
			try{
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost=new HttpPost("http://192.168.0.100:631");
//				Uri myUri = Uri.parse("USB1_LQ");
				URI targetUri = URI.create("http://192.168.0.100:631/USB1_LQ");
				httpPost.setURI(targetUri);
				httpPost.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Custom user agent");
				httpPost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, true); 
				httpPost.setHeader("Content-Type", "application/ipp");
				httpPost.setEntity(new ByteArrayEntity(validateJobByte));

				HttpResponse httpResponse = httpClient.execute(httpPost);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d("Alex", "IOException"); 
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
		}
	};
	
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	Runnable sendPrintJob = new Runnable () {
		public void run() {
//			while(true) {
			Log.d("Alex", "run sendPrintJob!"); 
/*			
			try{
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost=new HttpPost("http://192.168.0.100:631");
//				Uri myUri = Uri.parse("USB1_LQ");
				URI targetUri = URI.create("http://192.168.0.100:631/USB1_LQ");
				httpPost.setURI(targetUri);
				httpPost.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Custom user agent");
				httpPost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, true); 
				httpPost.addHeader("Content-Type", "application/ipp");
			
				ByteArrayInputStream bs=new ByteArrayInputStream(totalPrintJobByte);
				InputStreamEntity et=new InputStreamEntity(bs, totalPrintJobByte.length);
				//httpPost.setEntity(new ByteArrayEntity(totalPrintJobByte));
				//et.setContentType("binary/octet-stream");
				et.setChunked(true);
				httpPost.setEntity(et);
				HttpResponse httpResponse = httpClient.execute(httpPost);
			} catch (IOException e) {
				Log.d("Alex", "FAILED!"); 
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
			*/
			
			try{
			URL myUrl = new URL("http://192.168.0.100:631"); 
			HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/ipp"); 
			connection.addRequestProperty(CoreProtocolPNames.USER_AGENT, "Custom user agent");
			connection.setDoOutput(true); 
			
			OutputStream outputStream = connection.getOutputStream();

			outputStream.write(totalPrintJobByte);
			outputStream.flush();
			outputStream.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) 
            {
            	InputStream inputStream = connection.getInputStream();
                String state = getStringFromInputStream(inputStream);
                Log.d("Alex", "OK: ");
            } 
            else 
            	Log.d("Alex", "failed" + responseCode);
            
			} catch (MalformedURLException e) {
				Log.d("Alex", "MalformedURLException");
			} catch (IOException e) {
				Log.d("Alex", "IOException"); 
				e.printStackTrace(); 
			}
			int printJobByteLen = printJobByte.length;
			
			Log.d("Alex", "printJobByteLen is: "+printJobByteLen); 
			String hexString = Integer.toHexString(printJobByteLen);
			Log.d("Alex", "hexString is: "+hexString); 
			
			byte[] printJobByteLenByteArray = hexString.getBytes();
			/*
			for (byte byteindividual: printJobByteLenByteArray) {
				Log.d("Alex", "byteindividual = "+byteindividual); 
			}
			*/
			
			byte[] delimiterArray = new byte[] {0x0d, 0x0a}; 
			byte[] tempArray = new byte[printJobByteLenByteArray.length + delimiterArray.length + printJobByte.length];
			System.arraycopy(printJobByteLenByteArray, 0, tempArray, 0, printJobByteLenByteArray.length);
			System.arraycopy(delimiterArray, 0, tempArray, printJobByteLenByteArray.length, delimiterArray.length);
			System.arraycopy(printJobByte, 0, tempArray, printJobByteLenByteArray.length+delimiterArray.length, printJobByte.length);
			for(int i=0; i<tempArray.length; i++) {
				Log.d("Alex", "tempArray["+i+"] is: "+tempArray[i]); 
			}
			
			int round = fileByte.length / 8192; //see how many chunks it needs to be cut
			Log.d("Alex", "round is: "+round); 
			int remainderByte = fileByte.length % 8192; //the last bit of the data
			Log.d("Alex", "remainderByte is: "+remainderByte); 
			byte[] chunkLenByteArray = Integer.toHexString(8192).getBytes(); //length in hex for each chunk's size
			for(byte bb: chunkLenByteArray) {
				Log.d("Alex", "checkLenByteArray is: "+bb); 
			}
			byte[] remainderByteLenByteArray = Integer.toHexString(remainderByte).getBytes();
			for(byte bbb: remainderByteLenByteArray) {
				Log.d("Alex", "remainderByteLenByteArray: "+bbb); 
			}
			int totalLength = (chunkLenByteArray.length + delimiterArray.length + 8192 + delimiterArray.length) * round + 
					remainderByteLenByteArray.length +remainderByte + delimiterArray.length + 1 + (delimiterArray.length) *2; 
			Log.d("Alex", "totalLength: "+totalLength); 
			byte[] bigByteArray = new byte[totalLength]; //hold the whole data, including the chunk delimiters
			Log.d("Alex", "bigByteArray created!");
			int count = 0; 
			int offset = 8192+chunkLenByteArray.length+4; //data body size, and the length, +4 b/c it has to give out 4 bytes for delimiters 
			for(int idx=0; idx<round; idx++) {
				//length of chunk first... 
				System.arraycopy(chunkLenByteArray, 0, bigByteArray, idx*(offset), chunkLenByteArray.length); 
				//2 delimiters
				Log.d("Alex", "chunk delimiter index: "+(idx*offset+chunkLenByteArray.length)); 
				Log.d("Alex", "chunk 2nd delimiter index: "+(idx*offset+1+chunkLenByteArray.length));
				bigByteArray[idx*offset+chunkLenByteArray.length]=0x0d; 
				bigByteArray[idx*offset+1+chunkLenByteArray.length]=0x0a; 
				//chunk data body
				Log.d("Alex", "starting chunk body, first index: "+(idx*offset+2+chunkLenByteArray.length)); //correct
				System.arraycopy(fileByte, 8192*idx, bigByteArray, idx*offset+2+chunkLenByteArray.length, 8192);
				//another 2 delimiters to mark the end of this chunk
				
				Log.d("Alex", "delimiter at the end, index: "+(idx*offset+2+8192+chunkLenByteArray.length)); //correct
				bigByteArray[idx*offset+2+8192+chunkLenByteArray.length] = 0x0d;
				bigByteArray[idx*offset+3+8192+chunkLenByteArray.length] = 0x0a;
				count = idx; 
				Log.d("Alex", "count is: "+count);
			}
			count++;
			//now the remainder part... 
			Log.d("Alex", "first index for remainder part: "+(count*offset)); //ok 98400
			Log.d("Alex", "remainder length: "+remainderByteLenByteArray.length); //ok 3
			//chunk length
			System.arraycopy(remainderByteLenByteArray, 0, bigByteArray, count*offset, remainderByteLenByteArray.length); 
			//2 delimiters...
			bigByteArray[count*offset+remainderByteLenByteArray.length]=0x0d; //98403
			bigByteArray[count*offset+1+remainderByteLenByteArray.length]=0x0a; //98404
			//data body...
			Log.d("Alex", "first index for remainder data body part: "+(count*offset+1+remainderByteLenByteArray.length+1)); //ok 98405

			System.arraycopy(fileByte, 8192*count, bigByteArray, count*offset+1+remainderByteLenByteArray.length+1, remainderByte);
			Log.d("Alex", "remainder's 2nd set of delimiter: "+(count*offset+1+remainderByteLenByteArray.length+1+remainderByte)); 
			bigByteArray[count*offset+1+remainderByteLenByteArray.length+1+remainderByte] = 0x0d; 
			bigByteArray[count*offset+1+remainderByteLenByteArray.length+1+remainderByte+1] = 0x0a; 
			//now the "end point" of data begins... 
			int endPoint = count*offset+1+remainderByteLenByteArray.length+1+remainderByte+1+1;
			Log.d("Alex", "what is endPoint? "+endPoint); 
			bigByteArray[endPoint]=0x30; 
			bigByteArray[endPoint+1]=0x0d; 
			bigByteArray[endPoint+2]=0x0a;
//			bigByteArray[endPoint+3]=0x0d;
//			bigByteArray[endPoint+4]=0x0a;
			
			for (int x=0; x<bigByteArray.length; x++) {
				
				Log.d("Alex", "oh yeh! bigByteArray is ["+x+"]: "+Integer.toHexString(bigByteArray[x])); 
				
			}

			/*
			outputStream.write(bigByteArray);
			outputStream.flush();
			outputStream.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) 
            {
            	InputStream inputStream = connection.getInputStream();
                String state = getStringFromInputStream(inputStream);
                Log.d("Alex", "OK: ");
            } 
            else 
            	Log.d("Alex", "failed" + responseCode);
            
			} catch (MalformedURLException e) {
				Log.d("Alex", "MalformedURLException");
			} catch (IOException e) {
				Log.d("Alex", "IOException"); 
				e.printStackTrace(); 
			}
			
			*/
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}			
		}
	};
	
	private static String getStringFromInputStream(InputStream is)
            throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    
    byte[] buffer = new byte[1024];
    int len = -1;
    
    while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
    }
    is.close();
    String state = os.toString();
    os.close();
    return state;
}
	
}
