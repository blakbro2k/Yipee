/**
 * Copyright 2024 See AUTHORS file.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asg.games.yipee.tools;

import asg.games.yipee.objects.AbstractYipeeObject;
import asg.games.yipee.objects.YipeeRoom;
import asg.games.yipee.objects.YipeeSeat;
import asg.games.yipee.objects.YipeeTable;
import asg.games.yipee.persistence.json.YipeeRoomDeserializer;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class Util {
    /**
     * An empty immutable {@code String} array.
     */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    /**
     * Represents a failed index search.
     *
     * @since 2.1
     */
    public static final int INDEX_NOT_FOUND = -1;

    private static final String LEFT_CURLY_BRACET_HTML = "&#123;";
    private static final String RIGHTT_CURLY_BRACET_HTML = "&#125;";

    private static final ObjectMapper json;
    private static final SimpleModule module = new SimpleModule();

    static {
        module.addDeserializer(YipeeRoom.class, new YipeeRoomDeserializer());
        json = JsonMapper.builder()
                .enable(StreamReadFeature.STRICT_DUPLICATE_DETECTION)
                .disable(StreamReadFeature.AUTO_CLOSE_SOURCE)
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();
        json.registerModule(module);
    }

    public static int getNextTableNumber(final YipeeRoom yokelRoom) {
        int tableIndex = -1;
        if (yokelRoom != null) {
            List<Integer> tables = iterableToList(yokelRoom.getTableIndexes());

            int size = size(tables);
            if (size > 0) {
                int[] indices = new int[size];
                for (int i = 0; i < size; i++) {
                    indices[i] = tables.get(i);
                }
                Arrays.sort(indices);
                int max = indices[size - 1];
                int num = findMissingNumber(indices, max);
                if(num == -1) {
                    return max + 1;
                }
                return num;
            } else if(size == 0) {
                return 0;
            }
        }
        return tableIndex;
    }

    /**
     * Function to find the missing number in a array
     * @param arr : given array
     * @param max : value of maximum number in the series
     */
    public static int findMissingNumber(int[] arr, int max){
        Arrays.sort(arr);

        int currentValue = 1;
        int missingValue = -1;
        boolean foundFirstMissing = false;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != currentValue) {
                for (int j = currentValue; j < arr[i]; j++) {
                    missingValue = j;
                    foundFirstMissing = true;
                    break;
                }
            }
            currentValue = arr[i] + 1;
            if(foundFirstMissing) {
                break;
            }
        }
        return missingValue;
    }


    public static <T> List<T> arrayToList(T[] o) {
        //int size = o.length;
        return new LinkedList<>(Arrays.asList(o));
    }

    public static <T> Iterable<T> safeIterable(Iterable<T> collection){
        return safeIterable(collection, false);
    }

    public static <T> Iterable<T> safeIterableArray(T[] collection){
        return safeIterable(arrayToList(collection), false);
    }

    public static <T> Iterable<T> safeIterable(final Iterable<T> collection, boolean newArray){
        if(collection != null){
            //TODO: fix hard code
            return newArray? new ArrayList<>() : collection;
        } else {
            return Collections.emptyList();
        }
    }

    public static <T> @NotNull List<T> iterableToList(final Iterable<T> iterable) {
        List<T> returnList = new ArrayList<T>();
        if(iterable != null) {
            for(T o : iterable) {
                returnList.add(o);
            }
        }
        return returnList;
    }

    public static float maxFloat(Float... floats) {
        float max = 0;
        if(floats != null){
            for(float f : floats){
                max = Math.max(max, f);
            }
        }
        return max;
    }


    public static <T> void flushIterator(Iterator<T> iterator) {
        while(iterator != null && iterator.hasNext()){
            iterator.remove();
        }
    }

    public static <T> void flushIterator(Class<T> clazz, Iterator<T> iterator) {
        while(iterator != null && iterator.hasNext()){
            iterator.remove();
        }
    }

    public static <Object> Collection<Object> getMapValues(Map<?, Object> map) {
        return map == null ? Collections.emptyList() : map.values();
    }

    public static <Object> Collection<Object> getMapKeys(Map<Object, ?> map) {
        return map == null ? Collections.emptyList() : map.keySet();
    }

    private static @NotNull Collection<String> toStringList(Collection<?> collection) {
        List<String> strings = new ArrayList<>();
        for(Object o : safeIterable(collection)) {
            if(o != null) {
                strings.add(o.toString());
            }
        }
        return strings;
    }

    public static <T> int size(Collection<T> collection) {
        int size = -1;
        if(collection != null) {
            size = collection.size();
        }
        return size;
    }

    public static void clearArrays(final Collection<?>... arrays) {
        if(arrays != null) {
            for (final Collection<?> array : arrays) {
                clearArray(array);
            }
        }
    }

    public static void clearArray(final Collection<?> array) {
        if (array != null) {
            array.clear();
        }
    }

    public static <T> Iterator<T> getArrayIterator(Class<T> clazz, List<T> boardIndexes) {
        return boardIndexes == null ? Collections.emptyIterator() : boardIndexes.iterator();
    }

    public static YipeeSeat getIndexOfSet(Set<YipeeSeat> seats, int seatNum) {
        int count = 0;

        for (YipeeSeat seat : safeIterable(seats)) {
            if (count == seatNum) {
                return seat;
            } else {
                count++;
            }
        }
        return null;
    }

    public static <T> T readValue(String jsonValue, Class<T> clazz) throws JsonProcessingException {
        return json.readValue(jsonValue, clazz);
    }

    public static String writeValueAsString(Object object) throws JsonProcessingException {
        return json.writeValueAsString(object);
    }


    public static class IDGenerator {
        private IDGenerator() {
        }

        public static String getID() {
            //return replace(GwtUUIDUtil.get() + "", "-","") ;
            return replace(UUID.randomUUID() + "", "-", "");
        }

        public static @NotNull List<String> getGroupOfIDs(int num) throws IllegalArgumentException {
            if(num > 0) {
                List<String> ids = new ArrayList<>();
                for(int i = 0; i < num; i++){
                    ids.add(getID());
                }
                return ids;
            }
            throw new IllegalArgumentException("Cannot create Group of ID's for number less than 1.");
        }
    }

    public static boolean containsAny(Collection<Object> c1, Collection<Object> c2, boolean newArray) {
        boolean containsAny = false;
        if(null != c1 && null != c2){
            for(Object o : safeIterable(c2, newArray)){
                if (!c1.contains(o)) {
                    continue;
                }
                containsAny = true;
                break;
            }
        }
        return containsAny;
    }

    /**
     * @param title
     * @return
     */
    public static String cleanTitle(String title) throws Exception{
        String ret = "";
        if (title != null) {
            ret = title.replace("#8211", "-")
                    .replace("#8217", "'")
                    .replace("#8220", "\"")
                    .replace("#8221", "\"")
                    .replace("#8230", "...")
                    .replace("#038", "&");
        }
        return ret;
    }

    public static boolean isEmpty(String text) {
        return text == null || text.isEmpty() || text.isBlank();
    }

    public static boolean isEmpty(Iterable<?> collection) {
        return collection == null || !collection.iterator().hasNext();
    }

    public static boolean isStaticArrayEmpty(Object[] array){
        return array == null || array.length < 1;
    }

    @Contract("null -> new")
    public static <T> String @NotNull [] toStringArray(List<T> collection) {
        if(collection != null){
            int size = collection.size();
            String[] c2 = new String[size];

            for(int c = 0; c < size; c++){
                T o = collection.get(c);
                if(o != null){
                    c2[c] = o.toString();
                }
            }
            return c2;
        }
        return new String[1];
    }

    /**
     * Creates a new Iterable Array object.
     *
     * @param //array
     * @param //<T>
     * @return
     */
    //@NotNull
    public static <T> Iterable<T> toIterable(Collection<T> collection) {
        if (isEmpty(collection)) {
            return Collections.emptyList();
        }
        return collection.stream().toList();
    }

    public static String getJsonString(AbstractYipeeObject o) throws JsonProcessingException {
        return json.writeValueAsString(o);
    }

    public static <T> T getObjectFromJsonString(Class<T> type, String jsonStr) throws JsonProcessingException {
        return json.readValue(jsonStr, type);
    }

    public static String jsonToString(String str){
        return replace(replace(str, "{",LEFT_CURLY_BRACET_HTML),"}", RIGHTT_CURLY_BRACET_HTML);
    }

    public static <T> List<T> jsonNodeToCollection(Class<T> inClass, JsonNode node) {
        List<T> collection = new ArrayList<>();
        if(node instanceof ArrayNode) {
            for (JsonNode o : node) {
                if (o != null) {
                    String localClassName = o.get(JsonTypeInfo.Id.CLASS.getDefaultPropertyName()).asText();
                    try {
                        Class<?> clazz = Class.forName(localClassName);
                        Object object = getObjectFromJsonString(clazz, o.toString());
                        if (inClass.isInstance(object)) {
                            collection.add(inClass.cast(object));
                        }
                    } catch (ClassNotFoundException | JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return collection;
    }

    public static <T> Map<Integer, YipeeTable> jsonNodeToTableIndexMap(Class<T> inClass, JsonNode node) {
        Map<Integer, YipeeTable> tableMap = new HashMap<>();
        List<YipeeTable> collection = jsonNodeToCollection(YipeeTable.class, node);
        for(YipeeTable table : collection) {
            if(table != null) {
                int tableNum = table.getTableNumber();
                tableMap.put(tableNum, table);
            }
        }
        return tableMap;
    }

    public static String stringToJson(String str){
        return replace(replace(str, LEFT_CURLY_BRACET_HTML,"{"), RIGHTT_CURLY_BRACET_HTML,"}");
    }

    public static String getStringValue(String[] objects, int index) {
        if(objects != null && index < objects.length){
            return objects[index];
        }
        return null;
    }

    public static boolean getBooleanValue(String[] objects, int index) {
        if(objects != null && index < objects.length){
            return otob(objects[index]);
        }
        return false;
    }


    public static boolean otob(Object o){
        if(o != null){
            return Boolean.parseBoolean(otos(o));
        }
        return false;
    }

    public static String otos(Object o){
        if(o != null){
            return o.toString();
        }
        return "";
    }

    public static int otoi(Object o){
        if(o != null){
            return Integer.parseInt(otos(o));
        }
        return -1;
    }

    public static long otol(Object o){
        if(o != null){
            return Long.parseLong(otos(o));
        }
        return -1;
    }

    public static float otof(Object o) {
        if (o != null) {
            return Float.parseFloat(otos(o));
        }
        return -1;
    }

    public static <T> Set<T> listToSet(List<T> list) {
        Set<T> set = new HashSet<>();
        for (T listItem : safeIterable(list)) {
            if (listItem != null) {
                set.add(listItem);
            }
        }
        return set;
    }

    public static List<String> getFileNames(File folder) {
        List<String> retFileNames = new ArrayList<>();
        if (folder != null) {
            File[] fileNames = folder.listFiles();

            if (fileNames != null) {
                for (File file : fileNames) {
                    if (file != null) {
                        if (file.isDirectory()) {
                            getFileNames(file);
                        } else {
                            retFileNames.add(file.getName());
                        }
                    }
                }
            }
        }
        return retFileNames;
    }

    public static List<String> getFileNames(String path){
        List<String> fileNames = new ArrayList<>();
        if(path != null){
            fileNames.addAll(getFileNames(new File(path)));
        }
        return fileNames;
    }

    /**
     * <p>Compares two CharSequences, returning {@code true} if they represent
     * equal sequences of characters, ignoring case.</p>
     *
     * <p>{@code null}s are handled without exceptions. Two {@code null}
     * references are considered equal. The comparison is <strong>case insensitive</strong>.</p>
     *
     * <pre>
     * StringUtils.equalsIgnoreCase(null, null)   = true
     * StringUtils.equalsIgnoreCase(null, "abc")  = false
     * StringUtils.equalsIgnoreCase("abc", null)  = false
     * StringUtils.equalsIgnoreCase("abc", "abc") = true
     * StringUtils.equalsIgnoreCase("abc", "ABC") = true
     * </pre>
     *
     * @param cs1  the first CharSequence, may be {@code null}
     * @param cs2  the second CharSequence, may be {@code null}
     * @return {@code true} if the CharSequences are equal (case-insensitive), or both {@code null}
     * @since 3.0 Changed signature from equalsIgnoreCase(String, String) to equalsIgnoreCase(CharSequence, CharSequence)
     */
    public static boolean equalsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        return regionMatches(cs1, true, 0, cs2, 0, cs1.length());
    }

    /**
     * Green implementation of regionMatches.
     *
     * @param cs the {@code CharSequence} to be processed
     * @param ignoreCase whether or not to be case insensitive
     * @param thisStart the index to start on the {@code cs} CharSequence
     * @param substring the {@code CharSequence} to be looked for
     * @param start the index to start on the {@code substring} CharSequence
     * @param length character length of the region
     * @return whether the region matched
     */
    static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
                                 final CharSequence substring, final int start, final int length)    {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        }
        int index1 = thisStart;
        int index2 = start;
        int tmpLen = length;

        // Extract these first so we detect NPEs the same as the java.lang.String version
        final int srcLen = cs.length() - thisStart;
        final int otherLen = substring.length() - start;

        // Check for invalid parameters
        if (thisStart < 0 || start < 0 || length < 0) {
            return false;
        }

        // Check that the regions are long enough
        if (srcLen < length || otherLen < length) {
            return false;
        }

        while (tmpLen-- > 0) {
            final char c1 = cs.charAt(index1++);
            final char c2 = substring.charAt(index2++);

            if (c1 == c2) {
                continue;
            }

            if (!ignoreCase) {
                return false;
            }

            // The real same check as in String.regionMatches():
            final char u1 = Character.toUpperCase(c1);
            final char u2 = Character.toUpperCase(c2);
            if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {
                return false;
            }
        }

        return true;
    }

    /**
     * <p>Checks if CharSequence contains a search CharSequence irrespective of case,
     * handling {@code null}. Case-insensitivity is defined as by
     * {@link String#equalsIgnoreCase(String)}.
     *
     * <p>A {@code null} CharSequence will return {@code false}.</p>
     *
     * <pre>
     * StringUtils.containsIgnoreCase(null, *) = false
     * StringUtils.containsIgnoreCase(*, null) = false
     * StringUtils.containsIgnoreCase("", "") = true
     * StringUtils.containsIgnoreCase("abc", "") = true
     * StringUtils.containsIgnoreCase("abc", "a") = true
     * StringUtils.containsIgnoreCase("abc", "z") = false
     * StringUtils.containsIgnoreCase("abc", "A") = true
     * StringUtils.containsIgnoreCase("abc", "Z") = false
     * </pre>
     *
     * @param str  the CharSequence to check, may be null
     * @param searchStr  the CharSequence to find, may be null
     * @return true if the CharSequence contains the search CharSequence irrespective of
     * case or false if not or {@code null} string input
     * @since 3.0 Changed signature from containsIgnoreCase(String, String) to containsIgnoreCase(CharSequence, CharSequence)
     */
    public static boolean containsIgnoreCase(final CharSequence str, final CharSequence searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        final int len = searchStr.length();
        final int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (regionMatches(str, true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>Splits the provided text into an array, separators specified.
     * This is an alternative to using StringTokenizer.</p>
     *
     * <p>The separator is not included in the returned String array.
     * Adjacent separators are treated as one separator.
     * For more control over the split use the StrTokenizer class.</p>
     *
     * <p>A {@code null} input String returns {@code null}.
     * A {@code null} separatorChars splits on whitespace.</p>
     *
     * <pre>
     * StringUtils.split(null, *)         = null
     * StringUtils.split("", *)           = []
     * StringUtils.split("abc def", null) = ["abc", "def"]
     * StringUtils.split("abc def", " ")  = ["abc", "def"]
     * StringUtils.split("abc  def", " ") = ["abc", "def"]
     * StringUtils.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
     * </pre>
     *
     * @param str  the String to parse, may be null
     * @param separatorChars  the characters used as the delimiters,
     *  {@code null} splits on whitespace
     * @return an array of parsed Strings, {@code null} if null String input
     */
    public static String[] split(final String str, final String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    /**
     * Performs the logic for the {@code split} and
     * {@code splitPreserveAllTokens} methods that return a maximum array
     * length.
     *
     * @param str  the String to parse, may be {@code null}
     * @param separatorChars the separate character
     * @param max  the maximum number of elements to include in the
     *  array. A zero or negative value implies no limit.
     * @param preserveAllTokens if {@code true}, adjacent separators are
     * treated as empty token separators; if {@code false}, adjacent
     * separators are treated as one separator.
     * @return an array of parsed Strings, {@code null} if null String input
     */
    private static String[] splitWorker(final String str, final String separatorChars, final int max, final boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()

        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        final List<String> list = new ArrayList<>();
        int sizePlus1 = 1;
        int i = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            final char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i));
        }
        return list.toArray(EMPTY_STRING_ARRAY);
    }

    /**
     * <p>Replaces all occurrences of a String within another String.</p>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtils.replace(null, *, *)        = null
     * StringUtils.replace("", *, *)          = ""
     * StringUtils.replace("any", null, *)    = "any"
     * StringUtils.replace("any", *, null)    = "any"
     * StringUtils.replace("any", "", *)      = "any"
     * StringUtils.replace("aba", "a", null)  = "aba"
     * StringUtils.replace("aba", "a", "")    = "b"
     * StringUtils.replace("aba", "a", "z")   = "zbz"
     * </pre>
     *
     * @see #replace(String text, String searchString, String replacement, int max)
     * @param text  text to search and replace in, may be null
     * @param searchString  the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @return the text with any replacements processed,
     *  {@code null} if null String input
     */
    public static String replace(final String text, final String searchString, final String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    /**
     * <p>Replaces a String with another String inside a larger String,
     * for the first {@code max} values of the search String.</p>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtils.replace(null, *, *, *)         = null
     * StringUtils.replace("", *, *, *)           = ""
     * StringUtils.replace("any", null, *, *)     = "any"
     * StringUtils.replace("any", *, null, *)     = "any"
     * StringUtils.replace("any", "", *, *)       = "any"
     * StringUtils.replace("any", *, *, 0)        = "any"
     * StringUtils.replace("abaa", "a", null, -1) = "abaa"
     * StringUtils.replace("abaa", "a", "", -1)   = "b"
     * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
     * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
     * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
     * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     *
     * @param text  text to search and replace in, may be null
     * @param searchString  the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @param max  maximum number of values to replace, or {@code -1} if no maximum
     * @return the text with any replacements processed,
     *  {@code null} if null String input
     */
    public static String replace(final String text, final String searchString, final String replacement, final int max) {
        return replace(text, searchString, replacement, max, false);
    }

    /**
     * <p>Replaces a String with another String inside a larger String,
     * for the first {@code max} values of the search String,
     * case sensitively/insensitively based on {@code ignoreCase} value.</p>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtils.replace(null, *, *, *, false)         = null
     * StringUtils.replace("", *, *, *, false)           = ""
     * StringUtils.replace("any", null, *, *, false)     = "any"
     * StringUtils.replace("any", *, null, *, false)     = "any"
     * StringUtils.replace("any", "", *, *, false)       = "any"
     * StringUtils.replace("any", *, *, 0, false)        = "any"
     * StringUtils.replace("abaa", "a", null, -1, false) = "abaa"
     * StringUtils.replace("abaa", "a", "", -1, false)   = "b"
     * StringUtils.replace("abaa", "a", "z", 0, false)   = "abaa"
     * StringUtils.replace("abaa", "A", "z", 1, false)   = "abaa"
     * StringUtils.replace("abaa", "A", "z", 1, true)   = "zbaa"
     * StringUtils.replace("abAa", "a", "z", 2, true)   = "zbza"
     * StringUtils.replace("abAa", "a", "z", -1, true)  = "zbzz"
     * </pre>
     *
     * @param text  text to search and replace in, may be null
     * @param searchString  the String to search for (case insensitive), may be null
     * @param replacement  the String to replace it with, may be null
     * @param max  maximum number of values to replace, or {@code -1} if no maximum
     * @param ignoreCase if true replace is case insensitive, otherwise case sensitive
     * @return the text with any replacements processed,
     *  {@code null} if null String input
     */
    private static String replace(final String text, String searchString, final String replacement, int max, final boolean ignoreCase) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        if (ignoreCase) {
            searchString = searchString.toLowerCase();
        }
        int start = 0;
        int end = ignoreCase ? indexOfIgnoreCase(text, searchString, start) : indexOf(text, searchString, start);
        if (end == INDEX_NOT_FOUND) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = Math.max(replacement.length() - replLength, 0);
        increase *= max < 0 ? 16 : Math.min(max, 64);
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != INDEX_NOT_FOUND) {
            buf.append(text, start, end).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = ignoreCase ? indexOfIgnoreCase(text, searchString, start) : indexOf(text, searchString, start);
        }
        buf.append(text, start, text.length());
        return buf.toString();
    }

    /**
     * <p>Case in-sensitive find of the first index within a CharSequence
     * from the specified position.</p>
     *
     * <p>A {@code null} CharSequence will return {@code -1}.
     * A negative start position is treated as zero.
     * An empty ("") search CharSequence always matches.
     * A start position greater than the string length only matches
     * an empty search CharSequence.</p>
     *
     * <pre>
     * StringUtils.indexOfIgnoreCase(null, *, *)          = -1
     * StringUtils.indexOfIgnoreCase(*, null, *)          = -1
     * StringUtils.indexOfIgnoreCase("", "", 0)           = 0
     * StringUtils.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
     * StringUtils.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
     * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
     * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
     * StringUtils.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
     * StringUtils.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
     * StringUtils.indexOfIgnoreCase("abc", "", 9)        = -1
     * </pre>
     *
     * @param str  the CharSequence to check, may be null
     * @param searchStr  the CharSequence to find, may be null
     * @param startPos  the start position, negative treated as zero
     * @return the first index of the search CharSequence (always &ge; startPos),
     *  -1 if no match or {@code null} string input
     * @since 2.5
     * @since 3.0 Changed signature from indexOfIgnoreCase(String, String, int) to indexOfIgnoreCase(CharSequence, CharSequence, int)
     */
    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return INDEX_NOT_FOUND;
        }
        if (startPos < 0) {
            startPos = 0;
        }
        final int endLimit = str.length() - searchStr.length() + 1;
        if (startPos > endLimit) {
            return INDEX_NOT_FOUND;
        }
        if (searchStr.length() == 0) {
            return startPos;
        }
        for (int i = startPos; i < endLimit; i++) {
            if (regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * <p>Finds the first index within a CharSequence, handling {@code null}.
     * This method uses {@link String#indexOf(String, int)} if possible.</p>
     *
     * <p>A {@code null} CharSequence will return {@code -1}.
     * A negative start position is treated as zero.
     * An empty ("") search CharSequence always matches.
     * A start position greater than the string length only matches
     * an empty search CharSequence.</p>
     *
     * <pre>
     * StringUtils.indexOf(null, *, *)          = -1
     * StringUtils.indexOf(*, null, *)          = -1
     * StringUtils.indexOf("", "", 0)           = 0
     * StringUtils.indexOf("", *, 0)            = -1 (except when * = "")
     * StringUtils.indexOf("aabaabaa", "a", 0)  = 0
     * StringUtils.indexOf("aabaabaa", "b", 0)  = 2
     * StringUtils.indexOf("aabaabaa", "ab", 0) = 1
     * StringUtils.indexOf("aabaabaa", "b", 3)  = 5
     * StringUtils.indexOf("aabaabaa", "b", 9)  = -1
     * StringUtils.indexOf("aabaabaa", "b", -1) = 2
     * StringUtils.indexOf("aabaabaa", "", 2)   = 2
     * StringUtils.indexOf("abc", "", 9)        = 3
     * </pre>
     *
     * @param seq  the CharSequence to check, may be null
     * @param searchSeq  the CharSequence to find, may be null
     * @param startPos  the start position, negative treated as zero
     * @return the first index of the search CharSequence (always &ge; startPos),
     *  -1 if no match or {@code null} string input
     * @since 2.0
     * @since 3.0 Changed signature from indexOf(String, String, int) to indexOf(CharSequence, CharSequence, int)
     */
    public static int indexOf(final CharSequence seq, final CharSequence searchSeq, final int startPos) {
        if (seq == null || searchSeq == null) {
            return INDEX_NOT_FOUND;
        }
        return charIndexOf(seq, searchSeq, startPos);
    }

    /**
     * Used by the indexOf(CharSequence methods) as a green implementation of indexOf.
     *
     * @param cs the {@code CharSequence} to be processed
     * @param searchChar the {@code CharSequence} to be searched for
     * @param start the start index
     * @return the index where the search sequence was found
     */
    static int charIndexOf(final CharSequence cs, final CharSequence searchChar, final int start) {
        if (cs instanceof String) {
            return ((String) cs).indexOf(searchChar.toString(), start);
        } else if (cs instanceof StringBuilder) {
            return ((StringBuilder) cs).indexOf(searchChar.toString(), start);
        } else if (cs instanceof StringBuffer) {
            return ((StringBuffer) cs).indexOf(searchChar.toString(), start);
        }
        return cs.toString().indexOf(searchChar.toString(), start);
    }

    /*public static Array<Vector2> getPolygonVertices(int n, float radius, int h, int k) {
        Array<Vector2> verts = GdxArrays.newArray();
        double angle_between_vertices = 2 * Math.PI / n;

        for (int i = n; i >= 0; i--) {
            double theta = i * angle_between_vertices;
            double x = h + radius * Math.cos(theta);
            double y = k + radius * Math.sin(theta);

            verts.add(new Vector2((float) x, (float) y));
        }
        return verts;
    }

    public static Vector2 rotatePoint(double x, double y, double Cx, double Cy, double theta) {
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);

        double newX = Cx + (x - Cx) * cosTheta - (y - Cy) * sinTheta;
        double newY = Cy + (x - Cx) * sinTheta + (y - Cy) * cosTheta;

        return new Vector2((float) newX, (float) newY);
    }*/
}
