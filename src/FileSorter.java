/**
 * This program sorts a binary file containing integers using radix sort
 * We sort integers in binary representation. Hence, we need two files, one for each digit
 * We make use of references to save seek time
 *
 * Authors: Christofer Berruz Chungata, Ervin Lara, Aarsh Kardani
 */

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileSorter {
    private RandomAccessFile one;
    private RandomAccessFile zero;
    private RandomAccessFile file;
    private RandomAccessFile filePointer, onePointer; //We use this references to swap between file and one
    private final int MASK = 0x00000001;
    private long oneCount, zeroCount;
    private long totalNumbers;
    public FileSorter(RandomAccessFile theFile){
        this.file = theFile;
        try{
            this.totalNumbers = file.length()/4;
            oneCount = zeroCount = 0;
            one = new RandomAccessFile("one.data", "rw");
            zero = new RandomAccessFile("zero.data", "rw");
            //Initializing the one and zero file
            for(int i = 0; i < totalNumbers; i++){
                one.writeInt(0);
                zero.writeInt(0);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Implementing Radix sort on a binary file
     * @throws IOException caused by reading or writing to a binary file
     */

    public void sort() throws IOException {
        for(int i = 0; i < 32; i++)
        {
            if(i%2 == 0){
                filePointer = file;
                onePointer = one;
            }else{
                filePointer = one;
                onePointer = file;
            }
            filePointer.seek(0);
            onePointer.seek(0);
            zero.seek(0);
            zeroCount = oneCount = 0;
            for(int j = 0; j < totalNumbers; j++){
               int n = filePointer.readInt();
               sortN(n,i);
            }
            //Merging
            onePointer.seek(oneCount*4);
            zero.seek(0L);
            for(int j = 0; j < zeroCount; j++){
                int x = zero.readInt();
                onePointer.writeInt(x);
            }
            //One is merged
        }
    }

    /**
     * Applies boolean mask to determine if an integer has a 0 or a 1 at an i given position
     * @param n integer to be categorized
     * @param i digit position to be evaluated
     * @throws IOException
     */

    public void sortN(int n, int i) throws IOException{
        if((n&(MASK<<i))==0) //We shift our mask to match the position of interes
        {
            zero.writeInt(n);
            zeroCount++;
        }else{
            onePointer.writeInt(n);
            oneCount++;
        }
    }

    /**
     * Provides a String representation of any RandomAccess File
     * @param file RandomAccess file to be String represented
     * @return String representation
     */

    public String fileView(RandomAccessFile file){
        StringBuilder out = new StringBuilder("");
        try{
            file.seek(0);
            while(true){
                out.append(file.readInt() + "\n");
            }
        }catch (IOException e){

        }
        return out.toString();
    }

    /**
     * Gets the toString of this.file
     * @return String
     */
    public String getStringFile(){
        return fileView(file);
    }

    /**
     * Checks whether this.file is sorted
     * @return boolean
     * @throws IOException
     */
    public boolean isSorted() throws IOException{
        file.seek(0);
        int prev = file.readInt();
        for(int i = 0; i < totalNumbers-1; i++){
            int cur = file.readInt();
            if(prev < cur){
                return false;
            }
            prev = cur;
        }
        return true;
    }
}
