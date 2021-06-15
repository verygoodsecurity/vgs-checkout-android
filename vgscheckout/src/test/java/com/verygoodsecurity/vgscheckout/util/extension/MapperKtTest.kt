package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHTTPMethod
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutBrandParams
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutCardType
import com.verygoodsecurity.vgscheckout.config.ui.view.cardnumber.model.VGSCheckoutChecksumAlgorithm
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.model.VGSCollectFieldNameMappingPolicy
import com.verygoodsecurity.vgscollect.view.card.BrandParams
import com.verygoodsecurity.vgscollect.view.card.CardBrand
import com.verygoodsecurity.vgscollect.view.card.CardType
import com.verygoodsecurity.vgscollect.view.card.validation.payment.ChecksumAlgorithm
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class MapperKtTest {

    @Test
    fun toCollectHTTPMethod_mappedCorrectly() {
        // Assert
        assertEquals(VGSCheckoutHTTPMethod.POST.toCollectHTTPMethod(), HTTPMethod.POST)
        assertEquals(VGSCheckoutHTTPMethod.DELETE.toCollectHTTPMethod(), HTTPMethod.DELETE)
        assertEquals(VGSCheckoutHTTPMethod.GET.toCollectHTTPMethod(), HTTPMethod.GET)
        assertEquals(VGSCheckoutHTTPMethod.PATCH.toCollectHTTPMethod(), HTTPMethod.PATCH)
        assertEquals(VGSCheckoutHTTPMethod.PUT.toCollectHTTPMethod(), HTTPMethod.PUT)
    }

    @Test
    fun toCollectMergePolicy_mappedCorrectly() {
        // Assert
        assertEquals(
            VGSCheckoutDataMergePolicy.FLAT_JSON.toCollectMergePolicy(),
            VGSCollectFieldNameMappingPolicy.FLAT_JSON
        )
        assertEquals(
            VGSCheckoutDataMergePolicy.NESTED_JSON.toCollectMergePolicy(),
            VGSCollectFieldNameMappingPolicy.NESTED_JSON
        )
        assertEquals(
            VGSCheckoutDataMergePolicy.NESTED_JSON_WITH_ARRAYS_OVERWRITE.toCollectMergePolicy(),
            VGSCollectFieldNameMappingPolicy.NESTED_JSON_WITH_ARRAYS_OVERWRITE
        )
        assertEquals(
            VGSCheckoutDataMergePolicy.NESTED_JSON_WITH_ARRAYS_MERGE.toCollectMergePolicy(),
            VGSCollectFieldNameMappingPolicy.NESTED_JSON_WITH_ARRAYS_MERGE
        )
    }

    @Test
    fun toCollectChecksumAlgorithm_mappedCorrectly() {
        // Assert
        assertEquals(
            VGSCheckoutChecksumAlgorithm.ANY.toCollectChecksumAlgorithm(),
            ChecksumAlgorithm.ANY
        )
        assertEquals(
            VGSCheckoutChecksumAlgorithm.LUHN.toCollectChecksumAlgorithm(),
            ChecksumAlgorithm.LUHN
        )
        assertEquals(
            VGSCheckoutChecksumAlgorithm.NONE.toCollectChecksumAlgorithm(),
            ChecksumAlgorithm.NONE
        )
    }

    @Test
    fun toCollectCardType_mappedCorrectly() {
        // Assert
        assertEquals(VGSCheckoutCardType.ELO.toCollectCardType(), CardType.ELO)
        assertEquals(VGSCheckoutCardType.VISA_ELECTRON.toCollectCardType(), CardType.VISA_ELECTRON)
        assertEquals(VGSCheckoutCardType.MAESTRO.toCollectCardType(), CardType.MAESTRO)
        assertEquals(
            VGSCheckoutCardType.FORBRUGSFORENINGEN.toCollectCardType(),
            CardType.FORBRUGSFORENINGEN
        )
        assertEquals(VGSCheckoutCardType.DANKORT.toCollectCardType(), CardType.DANKORT)
        assertEquals(VGSCheckoutCardType.VISA.toCollectCardType(), CardType.VISA)
        assertEquals(VGSCheckoutCardType.MASTERCARD.toCollectCardType(), CardType.MASTERCARD)
        assertEquals(
            VGSCheckoutCardType.AMERICAN_EXPRESS.toCollectCardType(),
            CardType.AMERICAN_EXPRESS
        )
        assertEquals(VGSCheckoutCardType.HIPERCARD.toCollectCardType(), CardType.HIPERCARD)
        assertEquals(VGSCheckoutCardType.DINCLUB.toCollectCardType(), CardType.DINCLUB)
        assertEquals(VGSCheckoutCardType.DISCOVER.toCollectCardType(), CardType.DISCOVER)
        assertEquals(VGSCheckoutCardType.UNIONPAY.toCollectCardType(), CardType.UNIONPAY)
        assertEquals(VGSCheckoutCardType.JCB.toCollectCardType(), CardType.JCB)
        assertEquals(VGSCheckoutCardType.UNKNOWN.toCollectCardType(), CardType.UNKNOWN)
    }

    @Test
    fun toCollectBrandParams_mappedCorrectly() {
        // Arrange
        val expectedResult = BrandParams(
            "## ## ## ##",
            ChecksumAlgorithm.NONE,
            arrayOf(8),
            arrayOf(3)
        )
        val brand = VGSCheckoutBrandParams(
            "## ## ## ##",
            VGSCheckoutChecksumAlgorithm.NONE,
            arrayOf(8),
            arrayOf(3)
        )
        // Act
        val result = brand.toCollectBrandParams()
        // Assert
        assertEquals(result, expectedResult)
    }

    @Test
    fun toCollectBrandParams_invalidParams_mappedIncorrectly() {
        // Arrange
        val expectedResult = BrandParams(
            "## ## ## ##",
            ChecksumAlgorithm.NONE,
            arrayOf(8),
            arrayOf(3)
        )
        val brand = VGSCheckoutBrandParams(
            "## ## ##",
            VGSCheckoutChecksumAlgorithm.NONE,
            arrayOf(8),
            arrayOf(3)
        )
        // Act
        val result = brand.toCollectBrandParams()
        // Assert
        assertNotEquals(result, expectedResult)
    }

    @Test
    fun toCollectCardBrand_mappedCorrectly() {
        // Arrange
        val expectedResult = CardBrand(
            "^111",
            "Test",
            10,
            BrandParams(
                "## ## ## ##",
                ChecksumAlgorithm.NONE,
                arrayOf(8),
                arrayOf(3)
            )
        )

        val checkoutBrand = VGSCheckoutCardBrand(
            "^111",
            "Test",
            10,
            VGSCheckoutBrandParams(
                "## ## ## ##",
                VGSCheckoutChecksumAlgorithm.NONE,
                arrayOf(8),
                arrayOf(3)
            )
        )
        // Act
        val result = checkoutBrand.toCollectCardBrand()
        // Assert
        assertEquals(result, expectedResult)
    }

    @Test
    fun toCollectCardBrand_invalidParams_mappedCorrectly() {
        // Arrange
        val expectedResult = CardBrand(
            "^111",
            "Test",
            10,
            BrandParams(
                "## ## ## ##",
                ChecksumAlgorithm.NONE,
                arrayOf(8),
                arrayOf(3)
            )
        )

        val checkoutBrand = VGSCheckoutCardBrand("^111", "Test", 10)
        // Act
        val result = checkoutBrand.toCollectCardBrand()
        // Assert
        assertNotEquals(result, expectedResult)
    }
}