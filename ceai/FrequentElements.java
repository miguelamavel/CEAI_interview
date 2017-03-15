package ceai;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FrequentElements<T> {

    List<T> elementList;

    public FrequentElements(List<T> elementList) {
        this.elementList = elementList;
    }

    public List<T> findKMostFrequentElements(int k) {
        // Get counts of elements
        HashMap countingMap = (HashMap) elementList.stream()
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Get most frequent elements
        Long max = -1L;
        Iterator it = countingMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if ((Long)pair.getValue()>max)
                max = (Long)pair.getValue();
        }

        // Build list with arrays of elements by frequency
        ArrayList<T>[] elementsWithFrequencyAsIndex = (ArrayList<T>[])new ArrayList[max.intValue()];
        it = countingMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ArrayList<T> elements = elementsWithFrequencyAsIndex[((Long)pair.getValue()).intValue()-1];
            if(elements==null)
                elements = new ArrayList<>();
            elements.add((T) pair.getKey());
            elementsWithFrequencyAsIndex[((Long)pair.getValue()).intValue()-1] = elements;
            it.remove();
        }

        // Extract K most frequent by looking at it in reverse
        List<T> mostFrequentElements = new ArrayList<>();
        for(int i=elementsWithFrequencyAsIndex.length-1; i>=0 && mostFrequentElements.size()<k; i--){
            if (elementsWithFrequencyAsIndex[i]==null)
                continue;
            for (T element : elementsWithFrequencyAsIndex[i]) {
                mostFrequentElements.add(element);
                if (mostFrequentElements.size()>=k)
                    break;
            }
        }


        return mostFrequentElements;
    }
}
