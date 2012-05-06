package stiters

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Plohmann, www.objectscape.org/stiters
 * Date: 22.03.12
 * Time: 18:28
 * To change this template use File | Settings | File Templates.
 */

import java.util.ArrayList
import java.util.Collection
import java.util.NoSuchElementException
import java.util.List
import java.util.Map
import java.rmi.NotBoundException
import java.util.HashMap
import java.util.Set
import java.util.StringTokenizer


/*
 * Evaluate the condition with the elements of the receiver collection.
 * If the condition evaluates to true for any element return true.
 * Otherwise return false.
 */
public inline fun <T> Collection<T>.anySatisfy(condition : (T) -> Boolean) : Boolean {
    for(item in this) {
        if(condition(item))
            return true;
    }
    return false;
}

/*
 * Evaluate the condition with the elements of the receiver collection.
 * If the condition evaluates to false for any element return false.
 * Otherwise return true.
 */
public inline fun <T> Collection<T>.allSatisfy(condition : (T) -> Boolean) : Boolean {
    for(item in this) {
        if(!condition(item))
            return false;
    }
    return true;
}

/*
 * Evaluate the condition with the elements of the receiver collection.
 * If the condition evaluates to false for all elements return true.
 * Otherwise return false.
 */
public inline fun <T> Collection<T>.noneSatisfy(condition : (T) -> Boolean) : Boolean {
    for(item in this) {
        if(condition(item))
            return false;
    }
    return true;
}

/*
 * Return a new instance of the same type as the receiver collection.
 * Return an ArrayList in case an instantiation exception occurs to
 * make sure that never null is returned.
 */
private inline fun <T> Collection<T>.newInstanceNotNull() : Collection<T>
{
    val newInstance = javaClass.newInstance()
    if(newInstance != null)
        return newInstance
    return ArrayList<T>
}

/*
 * Evaluate the condition with each of the receiver's elements as the argument.
 * Add elements into a new collection of the same type as the receiver for all
 * those elements for which the condition evaluates to true. Then return the
 * the new collection.
 */
public inline fun <T> Collection<T>.select(condition : (T) -> Boolean) : Collection<T>
{
    val result = newInstanceNotNull()
    for (item in this) {
        if (condition(item))
            result.add(item)
    }
    return result
}

/* quoted out until compiler fix has been created

public inline fun <T> Set<T>.select(fn : (T) -> Boolean) : Set<T>
{
    return super.select(fn).toSet()
}

public inline fun <T> List<T>.select(fn : (T) -> Boolean) : List<T>
{
    return super.select(fn).toList()
}
*/

/*
 * Evaluate the condition with each of the receiver's elements as the argument.
 * Add elements into a new collection of the same type as the receiver for all
 * those elements for which the condition evaluates to false. Then return the
 * the new collection.
 */
public inline fun <T> Collection<T>.reject(condition : (T) -> Boolean) : Collection<T> {
    val result = newInstanceNotNull()
    for (item in this) {
        if (!condition(item))
            result.add(item)
    }
    return result
}

/*
 * Evaluate the condition with each of the receiver's elements as the argument.
 * Collect the resulting values into a collection of the same type as the receiver
 * collection. Return the new collection.
 */
public inline fun <T> Collection<T>.collect(condition : (T) -> T) : Collection<T> {
    val result = newInstanceNotNull()
    for (item in this) {
        result.add(condition(item))
    }
    return result
}

/*
 * Evaluate the condition with each of the receiver's elements as the argument.
 * Return the first element for which the condition evaluates to true.
 */
public inline fun <T> Collection<T>.detect(condition : (T) -> Boolean) : T {
    for (item in this) {
        if (condition(item))
            return item
    }
    throw NoSuchElementException()
}

/*
 * Evaluate the condition with each of the receiver's elements as the argument.
 * Return the first element for which the condition evaluates to true. If none
 * evaluates to true, then evaluate the notFoundBlock which may return null.
 */
public inline fun <T> Collection<T>.detect(condition : (T) -> Boolean, notFoundBlock : () -> T?) : T?
{
    for (item in this) {
        if (condition(item))
            return item
    }
    return notFoundBlock()
}

/*
 * Evaluate the condition with each of the receiver's elements as the argument.
 * Return the first element for which the condition evaluates to true. If none
 * evaluates to true, then evaluate the notFoundBlock which must not return null.
 */
public inline fun <T> Collection<T>.detectNotNull(condition : (T) -> Boolean, notFoundBlock : () -> T) : T
{
    for (item in this) {
        if (condition(item))
            return item
    }
    return notFoundBlock()
}

/*
 * Accumulate a running value associated with evaluating the argument,
 * operation, with the current value of the argument, value, and the
 * receiver as operation arguments. For example, calculating the sum
 * of the numeric elements	of a collection could be done this way:
 * aCollection.injectInto(0, { subTotal, next -> subTotal + next }).
 */
public inline fun <T> Collection<T>.injectInto(value : T, operation : (T, T) -> T) : T {
    var nextValue = value
    for (item in this) {
        nextValue = operation(nextValue, item)
    }
    return nextValue
}

// List

/*
 * Return a new instance of the same type as the receiver collection which implements
 * the List interface. Return an ArrayList in case an instantiation exception occurs to
 * make sure that never null is returned.
 */
private inline fun <T> List<T>.newListInstanceNotNull() : List<T>
{
    val newInstance = javaClass.newInstance()
    if(newInstance != null)
        newInstance
    return ArrayList<T>
}

/*
 * Iterate over all elements of the receiver collection evaluating the operation
 * for each element passing the current loop counter to the operation.
 */
public inline fun <T> List<T>.forEachWithIndex(operation : (Int, T) -> Unit) {
    var i: Int = 0;
    for (item in this) {
        operation(i, item)
        i++
    }
}

/*
 * Iterate over the elements of the receiver collection in the range from-to range
 * evaluating the operation for each element passing the current loop counter to
 * the operation.
 */
public inline fun <T> List<T>.forEach(from : Int, to : Int, block : (T) -> Unit) {
    if(from < 0 && from >= size())
        throw IndexOutOfBoundsException("from value " + from + " out of bounds")
    if(to < 0 && to >= size())
        throw IndexOutOfBoundsException("to value " + to + " out of bounds")
    if(from > to)
        throw IllegalArgumentException("from greater than to")
    var i: Int = 0;
    for (item in this) {
        if(i > to)
            break
        if(i >= from)
            block(item)
        i++
    }
}

/*
 * Evaluate the condition with each of the receiver's elements as the argument passing
 * the current loop counter to the operation. Collect the resulting values into a
 * collection of the same type as the receiver collection. Return the new collection.
 */
public inline fun <T> List<T>.collectWithIndex(condition : (Int, T) -> T) : List<T> {
    var result = newListInstanceNotNull()
    var i = 0
    for (item in this) {
        result.add(condition(i, item))
        i++
    }
    return result
}

/*
 * Return a new collection of the same type as the receiver collection with the elements
 * order reversed.
 */
public inline fun <T> List<T>.reversed() : List<T> {
    var result = newListInstanceNotNull()
    for(item in this) {
        result.add(0, item)
    }
    return result
}

/*
 * Iterate over all elements of the receiver's collection starting from the last element
 * moving over each element up to the first one.
 */
public inline fun <T> List<T>.forEachReversed(fn : (T) -> T) : List<T> {
    var result = newListInstanceNotNull()
    for(item in reversed()) {
        result.add(fn(item))
    }
    return result
}

// Map

// Since Map does not implement Collection we have to implement all these
// methods for Maps another time ...

private inline fun <K, V> Map<K, V>.newMapInstanceNotNull() :  Map<K, V>
{
    val newInstance = javaClass.newInstance()
    if(newInstance != null)
        return newInstance
    return HashMap<K, V>
}

/*
 * Iterate over all values of the receiver's map evaluating the condition for
 * each value.
 */
public inline fun <K, V> Map<K, V>.forEach(condition : (V) -> Unit) : Unit {
    forEachValue(condition)
}

/*
 * Iterate over all keys of the receiver's map evaluating the condition for
 * each key.
 */
public inline fun <K, V> Map<K, V>.forEachKey(block : (K) -> Unit) : Unit {
    for(item in keySet()) {
        block(item)
    }
}

/*
 * Iterate over all values of the receiver's map evaluating the condition for
 * each value.
 */
public inline fun <K, V> Map<K, V>.forEachValue(block : (V) -> Unit) : Unit {
    for(item in values()) {
        block(item)
    }
}

/*
 * Iterate over all entries of the receiver's map evaluating the condition for
 * each entries' key and value.
 */
public inline fun <K, V> Map<K, V>.forEachEntry(block : (K, V) -> Unit) : Unit {
    for(item in entrySet()) {
        block(item.getKey(), item.getValue())
    }
}

/*
 * Evaluate the condition with each value of the map as the argument. Collect into a
 * new map, that only contains those entries for which the condition evaluates to
 * true.
 */
public inline fun <K, V> Map<K, V>.select(condition : (V) -> Boolean) : Map<K, V> {
    val result = newMapInstanceNotNull()
    for (item in entrySet()) {
        if (condition(item.getValue()))
            result.put(item.getKey(), item.getValue())
    }
    return result
}

/*
 * Evaluate the condition with each value of the map as the argument. Collect into a
 * new map, that only contains those entries for which the condition evaluates to
 * false.
 */
public inline fun <K, V> Map<K, V>.reject(condition : (V) -> Boolean) : Map<K, V> {
    val result = newMapInstanceNotNull()
    for (item in entrySet()) {
        if (!condition(item.getValue()))
            result.put(item.getKey(), item.getValue())
    }
    return result
}

/*
 * Evaluate the condition with each value of the map as the argument. Collect the
 * resulting values into a new collection and return it.
 */
public inline fun <K, V> Map<K, V>.collect(condition : (V) -> V) : List<V> {
    val result = ArrayList<V>
    for (item in entrySet()) {
        result.add(condition(item.getValue()))
    }
    return result
}

/*
 * Evaluate the condition with each of the receiver's map values elements as the argument.
 * Answer the first value for which the condition evaluates to true.
 */
public inline fun <K, V> Map<K, V>.detect(condition : (V) -> Boolean) : V {
    for (item in entrySet()) {
        if(condition(item.getValue()))
            return item.getValue()
    }
    throw NoSuchElementException()
}

/*
 * Evaluate the condition with each of the receiver's map values elements as the argument.
 * Answer the first value for which the condition evaluates to true. If no matching value
 * was found evaluate the ifNoneOperation and return the value returned by it.
 */
public inline fun <K, V> Map<K, V>.detect(condition : (V) -> Boolean, ifNoneOperation : () -> V) : V {
    for (item in entrySet()) {
        if(condition(item.getValue()))
            return item.getValue()
    }
    return ifNoneOperation()
}

/**
 * Convert the receiver collection to a Set. Nothing to do as this already is a Set, so just return this.
 * Prevents the receiver set from being copied unnecessarily.
 */

public inline fun <T> Set<T>.toSet() : Set<T> {
    return this
}

/**
 * Convert the receiver collection to a List. Nothing to do as this already is a List, so just return this.
 * Prevents the receiver list from being copied unnecessarily.
 *
public inline fun <T> List<T>.toList() : List<T> {
    return this
}
*/

// String

/*
 * Evaluate the condition with each character of the receiver string as the argument.
 * Add all those characters to the new string for which the condition evaluates to true.
 * Then return the the new string.
 */
public inline fun String.select(condition : (Char) -> Boolean) : String
{
    val selection = StringBuilder()

    for(ch in this) {
        if(condition(ch))
            selection.append(ch)
    }

    return String(selection)
}

/*
 * Evaluate the condition with each character of the receiver string as the argument. Collect the
 * resulting characters into a new string and return it.
 */
public inline fun String.collect(operation : (Char) -> Char) : String
{
    val selection = StringBuilder()

    for(ch in this)
        selection.append(operation(ch))

    return String(selection)
}

/*
 * Evaluate the condition with each character of the receiver string as the argument.
 * Add all those characters to the new string for which the condition evaluates to false.
 * Then return the the new string.
 */
public inline fun String.reject(condition : (Char) -> Boolean) : String
{
    val selection = StringBuilder()

    for(ch in this) {
        if(!condition(ch))
            selection.append(ch)
    }

    return String(selection)
}

/*
 * Look for the first character in the receiver string for which the condition evaluates to
 * true and return its index position. Returns -1 if the character was not found.
 */
public inline fun String.findFirst(condition : (Char) -> Boolean) : Int
{
    var index = 0;

    for(ch in this) {
        if(condition(ch))
            return index;
        index++
    }

    return -1
}

/*
 * Look for the last character in the receiver string for which the condition evaluates to
 * true and return its index position. Returns -1 if the character was not found.
 */
public inline fun String.findLast(condition : (Char) -> Boolean) : Int
{
    var index = 0;
    var lastHit = -1

    for(ch in this) {
        if(condition(ch))
            lastHit = index
        index++
    }

    return lastHit
}

/*
 * Answer an array of strings containing the substrings in the receiver string
 * separated by an space character.
 */
public inline fun String.toArrayOfSubstrings() : Array<String>
{
    return toArrayOfSubstrings(" ")
}

/*
 * Answer an array of strings containing the substrings in the receiver string
 * separated by the delimiter string.
 */
public inline fun String.toArrayOfSubstrings(delimiter : String) : Array<String>
{
    val tokens = ArrayList<String>
    val str = StringTokenizer(this, delimiter)
    while(str.hasMoreTokens()) {
        val nextToken = str.nextToken()
        if(nextToken != null)
            tokens.add(nextToken)
    }

    val iter = tokens.iterator()
    return Array<String>(tokens.size(), {iter.next()})
}

/*
 * Answer a copy of the receiver string with the character order reversed.
 */
public inline fun String.reversed() : String
{
    val builder = StringBuilder()

    for (ch in this)
        builder.insert(0, ch)

    return String(builder)
}
