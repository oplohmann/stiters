package stiters

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Plohmann, www.objectscape.org/stiters
 * Date: 16.04.12
 * Time: 17:24
 * To change this template use File | Settings | File Templates.
 */

import java.util.ArrayList
import java.util.NoSuchElementException
import java.util.Map
import java.util.HashMap
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import java.util.HashSet
import java.util.List
import java.util.TreeSet
import kotlin.nullable.forEach
import kotlin.nullable.reverse
import kotlin.nullable.filter
import java.util.Collection

open class Foo(param : Int) {
    val value = param
    public fun foo() : Int{
        return value
    }
}

class Bar(param : Int) : Foo(param) {
    public fun bar() : Int {
        return value
    }
}

fun main(args : Array<String>)
{
    val intList = ArrayList<Int>()
    intList.add(3)
    intList.add(7)
    intList.add(12)
    intList.add(23)
    intList.add(54)

    val intSet = HashSet<Int>()
    intSet.add(3)
    intSet.add(7)
    intSet.add(12)
    intSet.add(23)
    intSet.add(54)

    var selectResultSet = (intSet.select{ it < 12 }).toSet()

    var selectResultList : List<Int> = (intList.select{ it < 12 }).toList()
    println(selectResultList)  // prints [3, 7]
    assertEquals(2, selectResultList.size())
    assertTrue(selectResultList.contains(3))
    assertTrue(selectResultList.contains(7))

    selectResultList = (intList.reject{ it > 3 }).toList()
    println(selectResultList)  // prints [3]
    assertEquals(1, selectResultList.size())
    assertTrue(selectResultList.contains(3))

    selectResultList = (intList.collect{ it + 1 }).toList()
    println(selectResultList)  // prints [4, 8, 13, 24, 55]
    assertEquals(5, selectResultList.size())
    assertTrue(selectResultList.contains(4))
    assertTrue(selectResultList.contains(8))
    assertTrue(selectResultList.contains(13))
    assertTrue(selectResultList.contains(24))
    assertTrue(selectResultList.contains(55))

    var value = intList.detect{ it > 7 }
    assertEquals(12, value)

    var found = true;
    try {
        intList.detect{ it < 2 }
    }
    catch(e : NoSuchElementException) {
        found = false
    }
    assertFalse(found)


    var wasChanged = false
    var valueOrNull = intList.detect({ it > 5 }, { wasChanged = true ; null })
    assertEquals(7, valueOrNull)
    assertFalse(wasChanged)

    wasChanged = false
    var valueNotNull = intList.detectNotNull({ it > 54 }, { wasChanged = true ; 2 })
    assertEquals(2, valueNotNull)
    assertTrue(wasChanged)

    valueOrNull = intList.injectInto(1, { x, y -> x + y })
    println(valueOrNull)  // prints 100
    assertEquals(100, valueOrNull)

    var result = intList.anySatisfy{ it == 24 }
    assertFalse(result)

    result = intList.anySatisfy{ it == 23 }
    assertTrue(result)

    result = intList.allSatisfy{ it > 0 }
    assertTrue(result)

    result = intList.noneSatisfy{ it > 3 }
    assertFalse(result)

    var sum : Int = 0;
    var indexSum : Int = 0;
    intList.forEachWithIndex{ index, each -> indexSum += index; sum += each }
    assertEquals(10, indexSum)
    assertEquals(99, sum)

    var resultIntList = intList.collectWithIndex{ index, each -> each + index }
    println(resultIntList) // prints [3, 8, 14, 26, 58]
    assertEquals(5, resultIntList.size())
    assertEquals(3, resultIntList.get(0))
    assertEquals(8, resultIntList.get(1))
    assertEquals(14, resultIntList.get(2))
    assertEquals(26, resultIntList.get(3))
    assertEquals(58, resultIntList.get(4))

    resultIntList = intList.reversed();
    println(resultIntList) // prints [54, 23, 12, 7, 3]
    assertEquals(5, resultIntList.size())
    assertEquals(54, resultIntList.get(0))
    assertEquals(23, resultIntList.get(1))
    assertEquals(12, resultIntList.get(2))
    assertEquals(7, resultIntList.get(3))
    assertEquals(3, resultIntList.get(4))

    resultIntList = intList.forEachReversed{ it + 1 }
    println(resultIntList) // prints [55, 24, 13, 8, 4]
    assertEquals(5, resultIntList.size())
    assertEquals(55, resultIntList.get(0))
    assertEquals(24, resultIntList.get(1))
    assertEquals(13, resultIntList.get(2))
    assertEquals(8, resultIntList.get(3))
    assertEquals(4, resultIntList.get(4))

    val from : Int = 2;
    val to : Int = 3;
    resultIntList = ArrayList<Int>
    intList.forEach(from, to, { resultIntList.add(it) })
    println(resultIntList) // prints [12, 23]
    assertEquals(2, resultIntList.size())
    assertEquals(12, resultIntList.get(0))
    assertEquals(23, resultIntList.get(1))

    // Map

    var map : Map<String, Int> = HashMap<String, Int>
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 3);
    map.put("d", 4);

    println("X")
    map.filter{ println(it); true}
    println("Y")

    var resultStringList = ArrayList<String>
    map.forEachKey{resultStringList.add(it)}
    println(resultStringList) // prints [c, b, a, d] in undefined order
    assertEquals(map.size(), resultStringList.size())
    assertTrue(map.keySet().contains(resultStringList.get(0)))
    assertTrue(map.keySet().contains(resultStringList.get(1)))
    assertTrue(map.keySet().contains(resultStringList.get(2)))
    assertTrue(map.keySet().contains(resultStringList.get(3)))
    assertTrue(map.keySet().contains(resultStringList.get(3)))

    var keysSorted = TreeSet<String>()
    var keysSum = ""
    var valuesSum = 0
    map.forEachEntry{key, value -> keysSorted.add(key); valuesSum += value}
    for(ch in keysSorted)
        keysSum += ch
    assertEquals("abcd", keysSum)
    assertEquals(10, valuesSum)

    var resultMap = map.select{it > 2}
    assertEquals(2, resultMap.size())
    assertTrue(resultMap.containsKey("c"))
    assertTrue(resultMap.containsKey("d"))

    resultIntList = map.collect{it + 1};
    assertEquals(4, resultIntList.size())
    assertTrue(resultIntList.contains(2))
    assertTrue(resultIntList.contains(3))
    assertTrue(resultIntList.contains(4))
    assertTrue(resultIntList.contains(5))

    var str = "Koootlin"
    var resultStr = str.select{ it == 'o' }
    println(resultStr)  // prints "ooo"
    assertEquals("ooo", resultStr)

    str = "Kotlin"
    resultStr = str.collect{ Character.toUpperCase(it) }
    println(resultStr)  // prints "KOTLIN"
    assertEquals("KOTLIN", resultStr)

    str = "KkOoTtLlIiNn"
    resultStr = str.reject{ Character.isUpperCase(it) }
    println(resultStr)  // prints "kotlin"
    assertEquals("kotlin", resultStr)

    str = "look"
    resultStr = str.reversed()
    println(resultStr)  // prints kool
    assertEquals("kool", resultStr)

    str = "findfirst"
    var index = str.findFirst{ it == 'r' }
    assertEquals(6, index)
    index = str.findFirst{ it == 'x' }
    assertEquals(-1, index)

    str = "findfirst"
    index = str.findLast{ it == 'f' }
    assertEquals(4, index)
    index = str.findLast{ it == 'x' }
    assertEquals(-1, index)

    str = "the grey hound jumps over the lazy dog"
    var subStrings = str.toArrayOfSubstrings()
    assertEquals(8, subStrings.size)
    assertEquals("the", subStrings[0])
    assertEquals("jumps", subStrings[3])
    assertEquals("dog", subStrings[7])

    str = "the//grey//hound//jumps//over//the//lazy dog"
    subStrings = str.toArrayOfSubstrings("//")
    assertEquals(7, subStrings.size)
    assertEquals("the", subStrings[0])
    assertEquals("jumps", subStrings[3])
    assertEquals("lazy dog", subStrings[6])

    var barList : List<Bar> = ArrayList<Bar>
    barList.add(Bar(1))
    barList.add(Bar(2))

    var fooList : List<out Foo> = barList.filter{it.bar() > 1}
    var fooList2 : List<out Foo> = (barList.select{it.bar() > 1}).toList()
}