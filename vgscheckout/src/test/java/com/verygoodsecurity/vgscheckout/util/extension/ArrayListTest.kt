package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.collect.util.extension.*
import org.junit.Assert.assertEquals
import org.junit.Test

class ArrayListTest {

    @Test
    fun arrayListOfNulls_arrayWithNullsCreated() {
        // Arrange
        val expectedResult = arrayListOf(null, null, null, null)
        // Act
        val result = arrayListOfNulls<Any>(3)
        // Arrange
        assertEquals(expectedResult, result)
    }

    @Test
    fun overwrite_differentSize_mergedSuccessfully() {
        // Arrange
        val target = arrayListOf(1, null, null, 3)
        val source = arrayListOf(null, 1, 4)
        val expectedResult = arrayListOf(1, 1, 4, 3)
        // Act
        val result = target overwrite source
        // Arrange
        assertEquals(expectedResult, result)
    }

    @Test
    fun overwrite_differentSizeDifferentValues_mergedSuccessfully() {
        // Arrange
        val target = arrayListOf(1, null, null, 3, 5)
        val source = arrayListOf(3, 2, null, 22)
        val expectedResult = arrayListOf(3, 2, null, 22, 5)
        // Act
        val result = target overwrite source
        // Arrange
        assertEquals(expectedResult, result)
    }

    @Test
    fun deepMerge_overwrite_mergedSuccessfully() {
        // Arrange
        val target = arrayListOf<Any?>(1, null, null, 3, 5)
        val source = arrayListOf<Any?>(3, 2, null, 22)
        val expectedResult = arrayListOf<Any?>(3, 2, null, 22, 5)
        // Act
        val result = target.deepMerge(source, ArrayMergePolicy.OVERWRITE)
        // Arrange
        assertEquals(result, expectedResult)
    }

    @Test
    fun deepMerge_merge_mergedSuccessfully() {
        // Arrange
        val target = arrayListOf<Any?>(1, null, null, 3, 5)
        val source = arrayListOf<Any?>(3, 2, null, 22)
        val expectedResult = arrayListOf<Any?>(3, 2, 1, 22, null, null, 3, 5)
        // Act
        val result = target.deepMerge(source, ArrayMergePolicy.MERGE)
        // Arrange
        assertEquals(expectedResult, result)
    }

    @Test
    fun deepMerge_mergeWithObjectInSource_firstReplacedMergedSuccessfully() {
        // Arrange
        val target = arrayListOf<Any?>(1, null, null, 3, 5)
        val source = arrayListOf<Any?>(mutableMapOf<String, Any>("test" to "test"))
        val expectedResult = arrayListOf(
            mutableMapOf<String, Any>("test" to "test"), 1, null, null, 3, 5
        )
        // Act
        val result = target.deepMerge(source, ArrayMergePolicy.MERGE)
        // Arrange
        assertEquals(expectedResult, result)
    }

    @Test
    fun deepMerge_mergeWithObjectInSource_firstAddedToEndMergedSuccessfully() {
        // Arrange
        val target = arrayListOf(mutableMapOf<String, Any>("test" to "test"), null, null, 3, 5)
        val source = arrayListOf<Any?>(1)
        val expectedResult = arrayListOf(
            1, mutableMapOf<String, Any>("test" to "test"), null, null, 3, 5
        )
        // Act
        val result = target.deepMerge(source, ArrayMergePolicy.MERGE)
        // Arrange
        assertEquals(expectedResult, result)
    }

    @Test
    fun deepMerge_twoObjectsWithSameKeysAndIndexes_sourceOverwriteTargetKey() {
        // Arrange
        val target = arrayListOf<Any?>(mutableMapOf<String, Any>("test" to "test"))
        val source = arrayListOf<Any?>(mutableMapOf<String, Any>("test" to "test1"))
        // Act
        val result = target.deepMerge(source, ArrayMergePolicy.MERGE)
        // Arrange
        assertEquals(source, result)
    }

    @Test
    fun deepMerge_twoObjectsWithDifferentKeysAndSameIndexes_objectsMerged() {
        // Arrange
        val target = arrayListOf<Any?>(mutableMapOf<String, Any>("test" to "test"))
        val source = arrayListOf<Any?>(mutableMapOf<String, Any>("test1" to "test1"))
        val expectedResult =
            arrayListOf(
                mapOf<String, Any>("test1" to "test1"),
                mapOf<String, Any>("test" to "test")
            )
        // Act
        val result = target.deepMerge(source, ArrayMergePolicy.MERGE)
        // Arrange
        assertEquals(expectedResult, result)
    }

    @Test
    fun deepMerge_twoObjectsWithSameKeys_objectsMerged() {
        // Arrange
        val target = arrayListOf<Any?>(mutableMapOf<String, Any>("test" to "test", "test1" to ""))
        val source = arrayListOf<Any?>(mutableMapOf<String, Any>("test1" to "test1"))
        val expectedResult =
            arrayListOf(mutableMapOf<String, Any>("test" to "test", "test1" to "test1"))
        // Act
        val result = target.deepMerge(source, ArrayMergePolicy.MERGE)
        // Arrange
        assertEquals(expectedResult, result)
    }

    @Throws
    @Test
    fun setSafe_incorrectIndex_itemAddedSuccessfully() {
        // Arrange
        val target = arrayListOf(1, null)
        val expectedResult = arrayListOf(1, 10)
        // Act
        target.setOrAdd( 1, 10)
        // Arrange
        assertEquals(expectedResult, target)
    }
}