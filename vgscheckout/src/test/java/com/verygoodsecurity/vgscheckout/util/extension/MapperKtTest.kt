package com.verygoodsecurity.vgscheckout.util.extension

import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutDataMergePolicy
import com.verygoodsecurity.vgscheckout.config.networking.request.core.VGSCheckoutHttpMethod
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutCardBrand
import com.verygoodsecurity.vgscheckout.config.ui.view.card.cardnumber.model.VGSCheckoutChecksumAlgorithm
import com.verygoodsecurity.vgscheckout.networking.client.HttpMethod
import com.verygoodsecurity.vgscheckout.collect.core.model.VGSCollectFieldNameMappingPolicy
import com.verygoodsecurity.vgscheckout.collect.view.card.BrandParams
import com.verygoodsecurity.vgscheckout.collect.view.card.CardBrand
import com.verygoodsecurity.vgscheckout.collect.view.card.CardType
import com.verygoodsecurity.vgscheckout.collect.view.card.validation.payment.ChecksumAlgorithm
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class MapperKtTest {

    @Test
    fun toCollectHTTPMethod_mappedCorrectly() {
        // Assert
        assertEquals(VGSCheckoutHttpMethod.POST.toInternal(), HttpMethod.POST)
        assertEquals(VGSCheckoutHttpMethod.DELETE.toInternal(), HttpMethod.DELETE)
        assertEquals(VGSCheckoutHttpMethod.GET.toInternal(), HttpMethod.GET)
        assertEquals(VGSCheckoutHttpMethod.PATCH.toInternal(), HttpMethod.PATCH)
        assertEquals(VGSCheckoutHttpMethod.PUT.toInternal(), HttpMethod.PUT)
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
        assertEquals(VGSCheckoutCardBrand.Elo().toCollectCardType(), CardType.ELO)
        assertEquals(VGSCheckoutCardBrand.VisaElectron().toCollectCardType(), CardType.VISA_ELECTRON)
        assertEquals(VGSCheckoutCardBrand.Maestro().toCollectCardType(), CardType.MAESTRO)
        assertEquals(
            VGSCheckoutCardBrand.Forbrugsforeningen().toCollectCardType(),
            CardType.FORBRUGSFORENINGEN
        )
        assertEquals(VGSCheckoutCardBrand.Dankort().toCollectCardType(), CardType.DANKORT)
        assertEquals(VGSCheckoutCardBrand.Visa().toCollectCardType(), CardType.VISA)
        assertEquals(VGSCheckoutCardBrand.Mastercard().toCollectCardType(), CardType.MASTERCARD)
        assertEquals(
            VGSCheckoutCardBrand.AmericanExpress().toCollectCardType(),
            CardType.AMERICAN_EXPRESS
        )
        assertEquals(VGSCheckoutCardBrand.Hipercard().toCollectCardType(), CardType.HIPERCARD)
        assertEquals(VGSCheckoutCardBrand.Dinclub().toCollectCardType(), CardType.DINCLUB)
        assertEquals(VGSCheckoutCardBrand.Discover().toCollectCardType(), CardType.DISCOVER)
        assertEquals(VGSCheckoutCardBrand.Unionpay().toCollectCardType(), CardType.UNIONPAY)
        assertEquals(VGSCheckoutCardBrand.JCB().toCollectCardType(), CardType.JCB)
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
        val brand = VGSCheckoutCardBrand.Custom(
            "",
            0,
            "",
            "## ## ## ##",
            arrayOf(8),
            arrayOf(3),
            VGSCheckoutChecksumAlgorithm.NONE
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
        val brand = VGSCheckoutCardBrand.Custom(
            "",
            0,
            "",
            "## ## ##",
            arrayOf(8),
            arrayOf(3),
            VGSCheckoutChecksumAlgorithm.NONE
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

        val checkoutBrand = VGSCheckoutCardBrand.Custom(
            "Test",
            10,
            "^111",
            "## ## ## ##",
            arrayOf(8),
            arrayOf(3),
            VGSCheckoutChecksumAlgorithm.NONE
        )
        // Act
        val result = checkoutBrand.toCollectCardBrand()
        // Assert
        assertEquals(result, expectedResult)
    }
}