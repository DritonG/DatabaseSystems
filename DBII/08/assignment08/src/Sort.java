/*********************************************************
 * Replacement Sort in Java                              *
 * Stub Java File                                        *
 *                                                       *
 * (c) Manuel Mayr                                       *
 *********************************************************/

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Stub class which calls the sorting implementation
 */
public class Sort
{
    private static final int paramsCount = 2;
	
    private static final int paramBufSize = 0;
    private static final int paramFileToSort = 1; 
	
    /* Help message */
    private static final String helpText =
	"\nExternal Sorter (Assignment08)\n"
	+ "Vorlesung \"Database Systems 2\"\n"
	+ "\nSynopsis:\n"
	+ "\tUses an external sorting algorithm to sort files \n"
	+ "\nUsage:\n"
	+ "\tjava ExternalSort <buffer_size> <input_file>\n";

    public static void main(String[] args)
    {
	// We expect two parameters.
	if (args.length != paramsCount) {
	    System.err.println(helpText);
	    System.exit(1);
	}
		
	// buffer size (number of tuples)
	int bufSize = 0;
	String fileName = args[paramFileToSort];
	BufferedReader in = null;
		
	try {
	    bufSize = Integer.parseInt(args[paramBufSize]);
	} catch (NumberFormatException nfe) {
	    System.err.println("Could not parse buffer size");
	    System.exit(1);
	}
		
	try {
	    in = new BufferedReader (new FileReader(fileName));
	} catch(FileNotFoundException fe) {
	    System.err.println(fe.toString());
	    System.exit(1);
	}
		
	System.out.println("Replacement Sort with buffer size " + bufSize 
			   + " on input file " + fileName + "." );
		
	try {
	    // call the sorting routine
	    ReplacementSort.createPass0(in, bufSize);
	} catch(IOException io) {
	    System.err.println(io.toString());
	    System.exit(1);
	}
		
	System.exit(0);
    }
}
