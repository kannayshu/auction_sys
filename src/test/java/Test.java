import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created By liuda on 2018/5/26
 */
public class Test {

    public static void main(String[] args) {
        Test test = new Test();
        Boolean bool = !!1;
        System.out.println(test.ReverseSentence("I am a student."));
    }
    public String ReverseSentence(String str) {
        char[] array = str.toCharArray();
        int j = array.length-1;
        int i = 0;
        reverse(i,j,array);
        j=0;
        while(j!=array.length-1){
            i = findBegin(j,array);
            j = findEnd(i,array);
            reverse(i,j,array);
            if(i==j&&i!=array.length-1)
                j++;
        }

        return new String(array);
    }
    private int findBegin(int i,char[] array){
        if(i==0)
            return 0;
        while(!(array[i]!=' '&&array[i-1]==' '))
            i++;
        return i;
    }
    private int findEnd(int i,char[] array){
        while(!(array[i]==' '||i==array.length-1))
            i++;
        return i==array.length-1?i:i-1;
    }
    private void reverse(int i,int j ,char[] array){
        while(j-i>0){
            swap(i++,j--,array);
        }
    }

    private void swap(int i ,int j ,char[] array){
        char temp = array[i];
        array[i]= array[j];
        array[j] = temp;
    }
    public static int FirstNotRepeatingChar(String str) {
        char[] array = str.toCharArray();
        Map<Character,Integer> map =new HashMap<>();
        for(char c :array){
            map.compute(c,(k,v)->(v==null?1:++v));
            System.out.println("k"+c+"v"+map.get(c));
        }
        for(int i=0;i<=array.length;i++){
            int num = map.get(array[i]);
            if(num==1)
                return i;
        }
        return -1;
    }

    static class StringComparator implements Comparator<String>{


        @Override
        public int compare(String o1, String o2) {
            String s1 = o1+o2;
            String s2 = o2+o1;
            return s1.compareTo(s2);
        }
    }
}
