/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg322.pkg2022;

/**
 *
 * @author ehab
 */
/*
 * InvertedIndex - Given a set of text files, implement a program to create an 
 * inverted index. Also create a user interface to do a search using that inverted 
 * index which returns a list of files that contain the query term / terms.
 * The search index can be in memory. 
 * 

 */
import java.io.*;
import java.util.*;

//=====================================================================
class DictEntry3 {

    public int doc_freq = 0; // number of documents that contain the term
    public int term_freq = 0; //number of times the term is mentioned in the collection
    public HashSet<Integer> postingList;

    DictEntry3() {
        postingList = new HashSet<Integer>();
    }
}

//=====================================================================
class Index3 {

    //--------------------------------------------
    Map<Integer, String> sources;  // store the doc_id and the file name
    HashMap<String, DictEntry3> index; // THe inverted index
    //--------------------------------------------

    Index3() {
        sources = new HashMap<Integer, String>();
        index = new HashMap<String, DictEntry3>();
    }

    //---------------------------------------------
    public void printPostingList(HashSet<Integer> hset) {
        Iterator<Integer> it2 = hset.iterator();
        while (it2.hasNext()) {
            System.out.print(it2.next() + ", ");
        }
        System.out.println("");
    }
    
    public void printDictionary() {
        Iterator it = index.entrySet().iterator();
        /*while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            DictEntry3 dd = (DictEntry3) pair.getValue();
            System.out.print("** [" + pair.getKey() + "," + dd.doc_freq + "] <" + dd.term_freq + "> =--> ");
           //it.remove(); // avoids a ConcurrentModificationException
             printPostingList(dd.postingList);
        }*/
        System.out.println("------------------------------------------------------");
        System.out.println("*****    Number of terms = " + index.size());
        System.out.println("------------------------------------------------------");

    }

    //-----------------------------------------------
    public void buildIndex(String[] files) {
        int i = 0;
        for (String fileName : files) {
            try ( BufferedReader file = new BufferedReader(new FileReader(fileName))) {
                sources.put(i, fileName);
                String ln;
                while ((ln = file.readLine()) != null) {
                    String[] words = ln.split("\\W+");
                    for (String word : words) {
                        word = word.toLowerCase();
                        // check to see if the word is not in the dictionary
                        if (!index.containsKey(word)) {
                            index.put(word, new DictEntry3());
                        }
                        // add document id to the posting list
                        if (!index.get(word).postingList.contains(i)) {
                            index.get(word).doc_freq += 1; //set doc freq to the number of doc that contain the term 
                            index.get(word).postingList.add(i); // add the posting to the posting:ist
                        }
                        //set the term_fteq in the collection
                        index.get(word).term_freq += 1;
                    }
                }

            } catch (IOException e) {
                System.out.println("File " + fileName + " not found. Skip it");
            }
            i++;
        }
         printDictionary();
    }

    
    //----------------------------------------------------------------------------  
    HashSet<Integer> intersect(HashSet<Integer> pL1, HashSet<Integer> pL2) {
        HashSet<Integer> answer = new HashSet<Integer>();
        Iterator<Integer> itP1 = pL1.iterator();
        Iterator<Integer> itP2 = pL2.iterator();
        int docId1 = 0, docId2 = 0;
        
        if (itP1.hasNext()) 
            docId1 = itP1.next();        
        if (itP2.hasNext()) 
            docId2 = itP2.next();
        
        while (itP1.hasNext() && itP2.hasNext()) {

            if (docId1 == docId2) {
                answer.add(docId1);
                docId1 = itP1.next();
                docId2 = itP2.next();
            }
            else if (docId1 < docId2) {               
                if (itP1.hasNext()) 
                    docId1 = itP1.next();
                 else return answer;
                
            } else {
                if (itP2.hasNext()) 
                    docId2 = itP2.next();
                else return answer;
                
            }
          
        }
        if(pL1.size()<pL2.size())
        {
            while(itP2.hasNext())
            {
                if (docId1 == docId2) 
                {
                    answer.add(docId1);
                    break;
                }
                else
                    docId2 = itP2.next();
            }
        }
        else
        {
            while(itP1.hasNext())
            {
                if (docId1 == docId2) 
                {
                    answer.add(docId1);
                    break;
                }
                else
                    docId1 = itP1.next();
            }
        }
        //System.out.println("S "+answer);
        return answer;
    }
    //-----------------------------------------------------------------------   

            
    String[] rearrange(String[] words, int[] freq, int len) {
        boolean sorted = false;
        int temp;
        String sTmp;
        for (int i = 0; i < len - 1; i++) {
            freq[i] = index.get(words[i].toLowerCase()).doc_freq;
        }
        //-------------------------------------------------------
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < len - 1; i++) {
                if (freq[i] > freq[i + 1]) {
                    temp = freq[i];
                    sTmp = words[i];
                    freq[i] = freq[i + 1];
                    words[i] = words[i + 1];
                    freq[i + 1] = temp;
                    words[i + 1] = sTmp;
                    sorted = false;
                }
            }
        }
        return words;
    }

    //-----------------------------------------------------------------------                 
    public HashSet<Integer> OrOperation(HashSet<Integer> pL1, HashSet<Integer> pL2)
    {
        HashSet<Integer> answer = new HashSet<Integer>();
        answer.addAll(pL1);
        answer.addAll(pL2);
        
        return answer;
    }
    public HashSet<Integer> NotOperation(HashSet<Integer> pL1)
    {
        HashSet<Integer> answer = new HashSet<Integer>();
        Iterator<Integer> itP1 = pL1.iterator();
        int docId1 = 0, docId2 = 0;
        
        if (itP1.hasNext()) 
            docId1 = itP1.next();        
        
        for(Map.Entry<Integer, String> set :
             sources.entrySet())
        {
            if(docId1==set.getKey())
            {
                if(itP1.hasNext())
                    docId1 = itP1.next();
            }
            else
                answer.add(set.getKey());
        }
        
        return answer;
    }
    //-----------------------------------------------------------------------
    public void BooleanSearch(String Phrase)
    {
        String[] PhrasesList = Phrase.split(" ");
        HashSet<Integer> postingList = null;
        HashSet<Integer> postingList2 = null;
        for(int i=0 ; i < PhrasesList.length ; i++)
        {
            if(PhrasesList.length==1)
            {
                postingList = index.get(PhrasesList[i].toLowerCase()).postingList;
            }
            //System.out.println(PhrasesList[i]);
            if(PhrasesList[i].equalsIgnoreCase("!"))
            {            
                postingList = NotOperation(index.get(PhrasesList[i+1].toLowerCase()).postingList);
            }
            if(PhrasesList[i].equalsIgnoreCase("&"))
            {
                if(postingList == null)
                {
                     if(PhrasesList[i+1].equalsIgnoreCase("!"))
                     {
                        postingList2 = NotOperation(index.get(PhrasesList[i+2].toLowerCase()).postingList);
                        postingList = intersect(index.get(PhrasesList[i-1].toLowerCase()).postingList, postingList2);
                        i++;
                     }
                     else
                        postingList = intersect(index.get(PhrasesList[i-1].toLowerCase()).postingList, index.get(PhrasesList[i+1].toLowerCase()).postingList);
                }
                else
                {
                    if(PhrasesList[i+1].equalsIgnoreCase("!"))
                    {
                        postingList2 = NotOperation(index.get(PhrasesList[i+2].toLowerCase()).postingList);
                        //System.out.println("1: "+postingList);
                        postingList = intersect(postingList, postingList2);
                        //System.out.println("2: "+postingList);
                        i++;
                    }
                    else    
                    {
                        
                        postingList = intersect(postingList, index.get(PhrasesList[i+1].toLowerCase()).postingList);
                        
                    }
                }
            }
            if(PhrasesList[i].equalsIgnoreCase("|"))
            {
                if(postingList == null)
                {
                     if(PhrasesList[i+1].equalsIgnoreCase("!"))
                     {
                        postingList2 = NotOperation(index.get(PhrasesList[i+2].toLowerCase()).postingList);
                        postingList = OrOperation(index.get(PhrasesList[i-1].toLowerCase()).postingList, postingList2);
                        i++;
                     }
                     else
                        postingList = OrOperation(index.get(PhrasesList[i-1].toLowerCase()).postingList, index.get(PhrasesList[i+1].toLowerCase()).postingList);
                }
                else
                {
                    if(PhrasesList[i+1].equalsIgnoreCase("!"))
                    {
                        postingList2 = NotOperation(index.get(PhrasesList[i+2].toLowerCase()).postingList);
                        postingList = OrOperation(postingList, postingList2);
                        i++;
                    }
                    else    
                        postingList = OrOperation(postingList, index.get(PhrasesList[i+1].toLowerCase()).postingList);
                }
            }
                
        }
        
        //System.out.println(postingList);
        System.out.print("Related docs: ");
        for (int i = 0 ; i < postingList.size() ; i++)
        {
            System.out.print(sources.get(postingList.toArray()[i])+" ");
        }
        System.out.println("");
    }
    
    //-----------------------------------------------------------------------
}

//=====================================================================

public class InvertedIndex003 {

    public static void main(String args[]) throws IOException {
        Index3 index = new Index3();
        String phrase = "";
 /**/ 
        index.buildIndex(new String[]{
            "500.txt", // change it to your path e.g. "c:\\tmp\\100.txt"
            "501.txt",
            "502.txt",
            "503.txt",
            "504.txt",            
            "100.txt", // change it to your path e.g. "c:\\tmp\\100.txt"
            "101.txt",
            "102.txt",
            "103.txt",
            "104.txt",
            "105.txt",
            "106.txt",
            "107.txt",
            "108.txt",
            "109.txt"              
        });
        index.BooleanSearch("Agile & software & methodologies");
        index.BooleanSearch("Agile | software | methodologies");
        index.BooleanSearch("Agile & software | ! methodologies");
        
    }
}
