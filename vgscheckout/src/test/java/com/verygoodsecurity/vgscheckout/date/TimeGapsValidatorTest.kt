package com.verygoodsecurity.vgscheckout.date

import com.verygoodsecurity.vgscheckout.collect.view.date.validation.TimeGapsValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class TimeGapsValidatorTest {

    @Test
    fun test_positive_format_dd_MM_yyyy() {
        val validator = TimeGapsValidator("dd/MM/yyyy")

        assertTrue(validator.isValid("21/03/2021"))
        assertTrue(validator.isValid("24/09/1990"))
    }

    @Test
    fun test_positive_format_MM_yyyy() {
        val validator = TimeGapsValidator("MM/yyyy")

        assertTrue(validator.isValid("03/2021"))
        assertTrue(validator.isValid("09/1990"))
    }

    @Test
    fun test_positive_format_MM_yy() {
        val validator = TimeGapsValidator("MM/yy")

        assertTrue(validator.isValid("03/21"))
        assertTrue(validator.isValid("09/25"))
    }

    @Test
    fun test_positive_format_yyyy_MM_dd() {
        val validator = TimeGapsValidator("yyyy-MM-dd")

        assertTrue(validator.isValid("2025-09-24"))
        assertTrue(validator.isValid("1990-09-12"))
    }

    @Test
    fun test_negative_format_dd_yy_yyyy() {
        val validator = TimeGapsValidator("dd/MM/yyyy")

        assertFalse(validator.isValid("12/31/2021"))
        assertFalse(validator.isValid("aaa"))
        assertFalse(validator.isValid("12.03.2021"))
        assertFalse(validator.isValid("12.03/2021"))
    }

    @Test
    fun test_negative_format_MM_yyyy() {
        val validator = TimeGapsValidator("MM/yyyy")

        assertFalse(validator.isValid("03-2021"))
        assertFalse(validator.isValid("29/1990"))
        assertFalse(validator.isValid("29/25"))
    }

    @Test
    fun test_negative_format_MM_yy() {
        val validator = TimeGapsValidator("MM/yy")

        assertFalse(validator.isValid("23/21"))
        assertFalse(validator.isValid("09-25"))
    }

    @Test
    fun test_negative_format_yyyy_MM_dd() {
        val validator = TimeGapsValidator("yyyy-MM-dd")

        assertFalse(validator.isValid("2021/09/24"))
        assertFalse(validator.isValid("1990-29-12"))
    }

    @Test
    fun test_positive_format_yyyy_MM_dd_minDate() {
        val dataPattern = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(dataPattern, Locale.US)
        val calendar = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }
        val minDate = calendar.timeInMillis
        val validator = TimeGapsValidator(dataPattern, minDate)

        assertTrue(
            validator.isValid(
                dateFormat.format(calendar.apply { add(Calendar.YEAR, 1) }.timeInMillis)
            )
        )
    }

    @Test
    fun test_negative_format_yyyy_MM_dd_minDate() {
        val dataPattern = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(dataPattern, Locale.US)
        val calendar = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }
        val minDate = calendar.timeInMillis
        val validator = TimeGapsValidator(dataPattern, minDate)

        assertFalse(
            validator.isValid(
                dateFormat.format(calendar.apply { add(Calendar.YEAR, -1) }.timeInMillis)
            )
        )
    }

    @Test
    fun test_positive_format_yyyy_MM_dd_maxDate() {
        val dataPattern = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(dataPattern, Locale.US)
        val calendar = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }
        val maxDate = calendar.apply { add(Calendar.YEAR, 1) }.timeInMillis
        calendar.add(Calendar.YEAR, -1)
        val validator = TimeGapsValidator(dataPattern, maxDate = maxDate)

        assertTrue(
            validator.isValid(
                dateFormat.format(calendar.timeInMillis)
            )
        )
    }

    @Test
    fun test_negative_format_yyyy_MM_dd_maxDate() {
        val dataPattern = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(dataPattern, Locale.US)
        val calendar = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }
        val maxDate = calendar.apply { add(Calendar.YEAR, 1) }.timeInMillis
        calendar.add(Calendar.YEAR, -1)
        val validator = TimeGapsValidator(dataPattern, maxDate = maxDate)

        assertFalse(
            validator.isValid(
                dateFormat.format(calendar.apply { add(Calendar.YEAR, 2) }.timeInMillis)
            )
        )
    }


    @Test
    fun test_positive_format_yyyy_MM_dd_maxDate_and_minDate() {
        val dataPattern = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(dataPattern, Locale.US)
        val calendar = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }
        val minDate = calendar.timeInMillis
        val maxDate = calendar.apply { add(Calendar.YEAR, 2) }.timeInMillis
        calendar.add(Calendar.YEAR, -2)
        val validator = TimeGapsValidator(dataPattern, minDate, maxDate)

        assertTrue(
            validator.isValid(
                dateFormat.format(calendar.apply { add(Calendar.YEAR, 1) }.timeInMillis)
            )
        )
    }

    @Test
    fun test_negative_format_yyyy_MM_dd_maxDate_and_minDate() {
        val dataPattern = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(dataPattern, Locale.US)
        val calendar = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }
        val minDate = calendar.timeInMillis
        val maxDate = calendar.apply { add(Calendar.YEAR, 1) }.timeInMillis
        calendar.add(Calendar.YEAR, -1)
        val validator = TimeGapsValidator(dataPattern, minDate, maxDate)

        assertFalse(
            validator.isValid(
                dateFormat.format(calendar.apply { add(Calendar.YEAR, 1) }.timeInMillis)
            )
        )
        assertFalse(
            validator.isValid(
                dateFormat.format(calendar.apply { add(Calendar.YEAR, -2) }.timeInMillis)
            )
        )
    }
}