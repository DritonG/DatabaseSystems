import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * Create initial runs using replacement selection.
 */
public class ReplacementSort
{
    private static int fileNumber = 0;
	
    /**
     * Create a new run file
     */
    private static PrintStream createNewRun()
    {
	StringBuffer fileName = new StringBuffer();
	String name = "run";
	int passNumber = 0;
		
	PrintStream newRun = null;
		
	// file name of the new run file.
	fileName.append(name).append("_").append(passNumber).
	    append("_").append(fileNumber);
		
	try {
	    newRun = new PrintStream (
				      new BufferedOutputStream (
								new FileOutputStream(fileName.toString())));
	} catch(FileNotFoundException fne) {
	    System.err.println(fne.toString());
	    System.exit(1);
	}
		
	fileNumber++;
		
	return newRun;
    }
	
	
    /**
     * This method implements the actual replacement sort algorithm
     * @param file The input file that is to be sorted.
     * @param bufferSize The size of the sorting buffer (number of tuples in
     * current set)
     * @author Stumpp Tobias, Andreas Schmid
     */
    public static void createPass0(final BufferedReader file, final int bufferSize)
	throws IOException
    {
        // create fixed size buffer, initialized with UNDEFINED on every index
        final Buffer inputBuffer = new Buffer(bufferSize);
        while (file.ready()) {
            // create fixed size output buffer
            final Buffer outputBuffer = new Buffer(bufferSize);

            // fills the inputBuffer on the first run, stop if the input file is at EOF
            while (file.ready()) {
                final int release = inputBuffer.fill(Integer.parseInt(file.readLine()));
                // moves the first minimum to the output buffer,
                // the minimum in the inputBuffer is already replaced
                if (release != Buffer.UNDEFINED) {
                    outputBuffer.fill(release);
                    break;
                }
            }
            // fills the outputBuffer as long as there is data to read
            while (file.ready()) {
                final int indexSmallestLarger = inputBuffer.getSmallestLarger(outputBuffer.max());
                if (indexSmallestLarger != Buffer.UNDEFINED) {
                    final int release = inputBuffer.replace(indexSmallestLarger, Integer.parseInt(file.readLine()));
                    outputBuffer.fill(release);
                } else {
                    break;
                }
            }

            // writes out the output buffer
            if (!outputBuffer.isEmpty()) {
                final PrintStream newRun = createNewRun();
                final List<Integer> numbers = outputBuffer.numbers();
                for (final int number : numbers) {
                    newRun.println(number);
                }
                newRun.close();
            }
        }
        // writes out the remaining numbers
        final PrintStream newRun = createNewRun();
        final List<Integer> numbers = inputBuffer.numbers();
        for (final int number : numbers) {
            newRun.println(number);
        }
        newRun.close();
    }

    private static class Buffer {
        private static final int UNDEFINED = -1;
        private static List<Integer> buffer;
        private static int index = 0;

        public Buffer(final int bufferSize) {
            final Integer[] initial = new Integer[bufferSize];
            Arrays.fill(initial, UNDEFINED);
            buffer = Arrays.asList(initial);
        }

        public int fill(final int number) {
            if (!isFull()) {
                buffer.set(index++, number);
                return UNDEFINED;
            } else {
                final int minimum = Collections.min(buffer);
                final int index = buffer.indexOf(minimum);
                buffer.set(index, number);
                
                return minimum;
            }
        }

        public int replace(final int index, final int number) {
            return buffer.set(index, number);
        }

        public boolean isEmpty() {
            return index == 0;
        }

        public boolean isFull() {
            return index == buffer.size();
        }

        public int getSmallestLarger(final int border) {
//            // Java 8 would be fine
//            final List<Integer> buffersLarger = buffer.stream()
//                    .filter(i -> (i > border))
//                    .collect(Collectors.toList());
            final List<Integer> buffersLarger = new ArrayList<Integer>(buffer);
            final ListIterator<Integer> iterator = buffersLarger.listIterator();
            while (iterator.hasNext()) {
                if (iterator.next() < border) {
                    iterator.remove();
                }
            }

            if (buffersLarger.isEmpty()) {
                return UNDEFINED;
            }

            final int minimum = Collections.min(buffersLarger);
            return buffer.indexOf(minimum);
        }

        public List<Integer> numbers() {
            final List<Integer> numbers = new ArrayList<Integer>(buffer);
            final ListIterator<Integer> iterator = numbers.listIterator();
            while (iterator.hasNext()) {
                if (iterator.next() == UNDEFINED) {
                    iterator.remove();
                }
            }

            return numbers;
        }

        public int max() {
            return Collections.max(buffer);
        }
    }
}