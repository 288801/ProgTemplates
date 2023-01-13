import java.io.*;
import java.util.*;


public class Main {

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        boolean oj = System.getProperty("ONLINE_JUDGE") != null;
//        if (!oj) {
//            inputStream = new FileInputStream(new File("input.txt"));
//            outputStream = new FileOutputStream(new File("output.txt"));
//        }
        InputReader in = new InputReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        solve(in, out);
        out.close();
    }

    public static long[] zFunc(String str){
        char[] arr = str.toCharArray();
        long[] z = new long[arr.length];
        z[0] = 0;
        int l = 0, r = 0;
        for(int i = 1; i < z.length; i++){
            if(i <= r){
                z[i] = Math.min(z[i-l], r-i+1);
            }
            while(z[i] + i < z.length && arr[(int)z[i]] == arr[(int)z[i] + i]){
                z[i]++;
            }
            if(r < z[i] + i - 1){
                l = i;
                r = i + (int)z[i] - 1;
            }
        }
        return z;
    }

    public static void solve(InputReader in, PrintWriter out) throws IOException {
        String str = in.next();
        long[] z = zFunc(str);
        for(long l : z){
            System.out.print(l + " ");
        }
    }



    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }
    }
}