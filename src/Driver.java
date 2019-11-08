import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;
public class Driver {
    public static final int UPBOUND = 0X0fffffff;
    public static final int MAXNUM = 1000;

    public static void main(String[] args) {
        try{
            RandomAccessFile file = new RandomAccessFile("file.data", "rw");
            Random random = new Random();
            for(int i = 0; i < MAXNUM; i++){
                int n = random.nextInt(UPBOUND);
                file.writeInt(n);
            }
            FileSorter fileSorter = new FileSorter(file);
            fileSorter.sort();
            System.out.println(fileSorter.getStringFile());
            System.out.println("Sorted Successful: " + fileSorter.isSorted());
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
