package com.amida.fhir_sink;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Bundle;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class App 
{
    @SuppressWarnings("resource")
	public static void main( String[] args )
    {
    	FhirContext r4 = FhirContext.forR4();
    	IParser jsonParser = r4.newJsonParser();
	    String serverBase = "http://localhost:8080/hapi-fhir-jpaserver/fhir";
	    IGenericClient client = r4.newRestfulGenericClient(serverBase);
    	FileInputStream file;
		try {
			// opening file input stream
			file = new FileInputStream("170.315_b1_toc_inp_ccd_r21_sample1_v5.json");
			
			// reading file
			String fileBody = new Scanner(file,"UTF-8").useDelimiter("\\A").next();
			
			// parsing json entries to fhir resource objects 
			Bundle bundle = jsonParser.parseResource(Bundle.class, fileBody);
			
			// bundle should already have BundleType specified
			bundle.setType(Bundle.BundleType.TRANSACTION);
			
			// POSTing bundle to jpa fhir server
			Bundle resp = client.transaction().withBundle(bundle).execute();
			
			// printing JSON response from server
			System.out.println(jsonParser.setPrettyPrint(true).encodeResourceToString(resp));
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
