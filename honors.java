import java.util.*;


//Current Status: columns with the offset are being generated incorrectly.  this is throwing off the key solver.

public class honors {
    public static void  main(String [] args)
    {
        final double [] EnglishLetterFreq = {
           0.082, 0.015, 0.028, 0.043, 0.13, 0.022, 0.02, 0.061, 0.07, 0.0015, 0.0077, 0.04, 0.024, 0.067, 0.075, 0.019, 0.00095,
                0.06, 0.063, 0.091, 0.028, 0.0098, 0.024, 0.0015, 0.02, 0.00074
        };
        Scanner reader = new Scanner(System.in);
        final double englishIC = (double) 0.0667;
        System.out.println("Enter an encrypted Vigenere String: ");
        String temp = reader.nextLine();
        String enc = "";
        while(temp.length()>0)
        {
            enc+=temp;
            temp = reader.nextLine();
        }

        String cleanEnc = removeNonLetters(enc);
      //  System.out.println(cleanEnc);
        HashMap<Integer, Double> ics = new HashMap<>();
        ArrayList<Double> icVal = new ArrayList<>();
        ArrayList<Double> icValDiffList = new ArrayList<>();
        HashMap<Double, Integer> icValDiff = new HashMap<>();

        double tempIC;
        for(int i=1; i<30; i++)
        {

            tempIC
                    = 0;
            for(int j=0; j<i; j++)
            {
                tempIC += ic(column(j,cleanEnc, i)); //DO THIS FOR ALL OFFSETS FROM 0 to i
            }
            tempIC/=i;
          //  System.out.println(i + ": " + tempIC);
            ics.put(i, tempIC);
            icVal.add(tempIC);
            icValDiffList.add(Math.abs(tempIC-englishIC));
            icValDiff.put(Math.abs(tempIC-englishIC), i);
        }
        Collections.sort(icValDiffList);

 //       System.out.println(icValDiffList.toString());
  //      System.out.println(icValDiff.toString());
        Collections.sort(icVal);
      //  System.out.println(icVal.toString());
       // System.out.println(ics.toString());
        for(int i=0; i<icValDiffList.size(); i++)
        {
   //         System.out.println(icValDiff.get(icValDiffList.get(i)));
        }

        String key = "";
        String tempEnc;
        double freqSum;
        int [] letters = new int[26];
        HashMap<Double, Integer> freqSumMap = new HashMap<>();
        ArrayList<Double> freqSums = new ArrayList<>();
        for(int i=0; i<icValDiff.get(icValDiffList.get(0)); i++)
        {
            tempEnc = column(i, cleanEnc, icValDiff.get(icValDiffList.get(0)));
            freqSums.clear();
            freqSumMap.clear();
            for(int j=0; j<26; j++)
            {
                letters = new int[26];
       //         System.out.println(tempEnc);
                for(int k=0; k<tempEnc.length(); k++)
                {
                    letters[(tempEnc.charAt(k)-'a'+j)%26]++;
                }
                freqSum=0;
     //           System.out.println(Arrays.toString(letters));
                for(int k=0; k<26; k++)
                {
                    freqSum += Math.abs(EnglishLetterFreq[k] - (double)letters[k]/(double)tempEnc.length());
                }
       //         System.out.println(freqSum);
                freqSums.add(freqSum);
                freqSumMap.put(freqSum, j);
            }
            Collections.sort(freqSums);
            //System.out.println(freqSumMap);
            //System.out.println(freqSums);
            //System.out.println((char)(26-freqSumMap.get(freqSums.get(0)) + ((int)('a'))));
            key+=(char)((26-freqSumMap.get(freqSums.get(0)))%26 + ((int)('a')));
        }

        System.out.println("KEY: " + key);
        int letterAsNum;
        int keyLetAsNum;
        int newLetter;
        int offset = 0;
        for(int a=0; a<cleanEnc.length(); a++)
        {
            letterAsNum = cleanEnc.charAt(a) - 'a';
            keyLetAsNum = key.charAt((a - offset) % key.length()) - 'a';
            newLetter = (letterAsNum - keyLetAsNum + 26) % 26;
            newLetter += 'a';
            System.out.print((char)newLetter);

            /*if(!Character.isLetter(cleanEnc.charAt(a)))
            {
                System.out.print(cleanEnc.charAt(a));
                offset++;
            }
            else {

                letterAsNum = Character.isUpperCase(cleanEnc.charAt(a)) ? Character.toLowerCase(cleanEnc.charAt(a)) - 'a' : cleanEnc.charAt(a) - 'a';
               // System.out.println(enc.charAt(a) + " : " + letterAsNum);
                keyLetAsNum = key.charAt((a - offset) % key.length()) - 'a';
              //  System.out.println(letterAsNum + " - " + keyLetAsNum);
              //  System.out.println(letterAsNum - keyLetAsNum);
                newLetter = (letterAsNum - keyLetAsNum + 26) % 26;
                //System.out.println()
                newLetter += 'a';
                System.out.print(Character.isUpperCase(cleanEnc.charAt(a)) ? Character.toUpperCase((char)newLetter) : (char)newLetter);
            }
        */
        }
    }

    public static String removeNonLetters(String enc)
    {
        String result = "";
        for(int i=0; i<enc.length(); i++)
        {
            if(Character.isAlphabetic(enc.charAt(i)))
            {
                result+=Character.toLowerCase(enc.charAt(i));
            }
        }
        return result;
    }

    public static String column(int offset, String full, int length) {
        StringBuilder result = new StringBuilder();
        for(int i=offset; i<full.length(); i+=length){
            result.append(full.charAt(i));
        }
        return result.toString();
    }

    public static double ic(String enc) {
        int n = 0;
        double ic = 0;
        int charCount = 0;
        for (int i = 0; i < enc.length(); i++) {
            if (Character.isAlphabetic(enc.charAt(i))) {
                charCount++;
            }
        }

        for (int i = 0; i < 26; i++) {
            n = 0;
            for (int j = 0; j < enc.length(); j++) {

                if (Character.toLowerCase(enc.charAt(j)) == (char) (i + 'a')) {
                    n++;
                }
            }
            ic += ((double) ((n) * (n - 1))) / ((double) ((double) (charCount * (charCount - 1))));
        }
        return ic;
    }
}
