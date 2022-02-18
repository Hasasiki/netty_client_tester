package com.hasasiki.netty_client_tester.utils;

import java.util.ArrayList;
import java.util.List;

public class ByteArraySearch {

    /// <summary>
    /// Empty array.
    /// </summary>
    static final int[] Empty = new int[0];

    /// <summary>
    /// Locate the pattern in the given array.
    /// </summary>
    /// <param name="self">Array to search.</param>
    /// <param name="candidate">Pattern to look for in array.</param>
    /// <returns>List of all the positions where the pattern exists.</returns>
    public static int[] Locate(byte[] self, byte[] candidate) {
        if (IsEmptyLocate(self, candidate)){
            return Empty;
        }
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < self.length; i++) {
            if (!IsMatch(self, i, candidate)) {
                continue;
            }
            list.add(i);
        }
        List<Integer> glist = new ArrayList<>();
        for (int i = 0; i < list.size()-1; i++) {
            if (list.get(i+1)-list.get(i)==1){
                glist.add(list.get(i));
                i++;
            }else {
                glist.add(list.get(i));
            }
        }
        int[] charge = new int[glist.size()];
        for (int i = 1; i < glist.size(); i++) {
                charge[i] = glist.get(i);
        }
        return glist.size() == 0 ? Empty : charge;
    }

    /// <summary>
    /// Return flag and position where the pattern matches.
    /// </summary>
    /// <param name="array">Array to search.</param>
    /// <param name="position">Position to search</param>
    /// <param name="candidate">Pattern to look for.</param>
    /// <returns>TRUE if the pattern matches the position.</returns>
    static boolean IsMatch(byte[] array, int position, byte[] candidate) {
        if (candidate.length > (array.length - position)) {
            return false;
        }

        for (int i = 0; i < candidate.length; i++) {
            if (array[position + i] != candidate[i]) {
                return false;
            }
        }
        return true;
    }

    /// <summary>
    /// Check for an empty array.
    /// </summary>
    /// <param name="array">Array to earch</param>
    /// <param name="candidate">Pattern to look for.</param>
    /// <returns>TRUE if empty.</returns>
    static boolean IsEmptyLocate(byte[] array, byte[] candidate) {
        return array == null
                || candidate == null
                || array.length == 0
                || candidate.length == 0
                || candidate.length > array.length;
    }
}
