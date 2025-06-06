package M2;
import java.util.Arrays;

public class Problem3 {
    public static void main(String[] args) {
        // Don't edit anything here
        Integer[] a1 = new Integer[]{-1, -2, -3, -4, -5, -6, -7, -8, -9, -10};
        Integer[] a2 = new Integer[]{-1, 1, -2, 2, 3, -3, -4, 5};
        Double[] a3 = new Double[]{-0.01, -0.0001, -0.15};
        String[] a4 = new String[]{"-1", "2", "-3", "4", "-5", "5", "-6", "6", "-7", "7"};
        
        bePositive(a1);
        bePositive(a2);
        bePositive(a3);
        bePositive(a4);
    }
// <T> turns this into a generic so it can take in any datatype, it'll be passed as an Object so casting is required
    static <T> void bePositive(T[] arr) {
        System.out.println("Processing Array:" + Arrays.toString(arr));
        //your code should set the indexes of this array
        Object[] output = new Object[arr.length];
        //hint: use the arr variable; don't diretly use the a1-a4 variables
        //TODO convert each value to positive
        //set the result to the proper index of the output array
        //hint: don't forget to handle the data types properly, the result datatype should be the same as the original datatype
        for (int i = 0; i < arr.length; i++) { //this runs each index of each array
            if (arr[i] instanceof Number) { //checks if each index is a number
                Number num = (Number) arr[i]; //casts the idex to the number class
                if (num.doubleValue() < 0) { //checks if the idex is less that 0
                    if (arr[i] instanceof Integer) { //checks to see if the idex in the array is a int
                        output[i] = (T) ((Integer) (-num.intValue())); //assigns the index to output and casts it to generic
                    } else if (arr[i] instanceof Double) { //checks to see if the idex in the array is a double
                        output[i] = (T) ((Double) (-num.doubleValue())); //assigns the index to output and casts it to generic
                    }
                } else {
                    output[i] = arr[i]; //if the index is positive, it assigns it to output
                }
            } else if (arr[i] instanceof String) { //checks if each index is a string
                try {
                    double parsedValue = Double.parseDouble((String) arr[i]); //passes the string to double and assigns it to a parsedvalue
                    if (parsedValue < 0) {//checks if the parsed value is less than 0
                        output[i] = (T) ((Double) (-parsedValue));//assigns the parsed value to output and casts it to generic
                    } else {
                        output[i] = arr[i];//if the parsed value is positive, it assigns it to the output
                    }
                } catch (NumberFormatException e) { //checks if the index value is valid
                    output[i] = arr[i];//if the value is valid, it assigns it to output
                }
            } else {
                output[i] = arr[i];//it assigns the original element from the array to output
            }
        }
        //end edit section
        StringBuilder sb = new StringBuilder();
        for (Object i : output) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(String.format("%s (%s)", i, i.getClass().getSimpleName().substring(0, 1)));
        }
        System.out.println("Result: " + sb.toString());
    }
}