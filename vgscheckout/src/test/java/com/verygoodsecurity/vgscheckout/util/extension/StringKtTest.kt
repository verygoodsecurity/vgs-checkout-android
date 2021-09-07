package com.verygoodsecurity.vgscheckout.util.extension

import org.junit.Assert.*
import org.junit.Test

private const val VALID_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzA1MjIyODcsImlhdCI6MTYzMDUyMTk4NywianRpIjoiNWVjZGVyMzItOWM2My00YzAyLWE1MjMtYWE4MjRjYzI3NDRjIiwiaXNzIjoiaHR0cHM6Ly9hdXRoLnZlcnlnb29kc2VjdXJpdHkuY29tL2F1dGgvcmVhbG1zL3ZncyIsImF1ZCI6Im11bHRpcGxleGluZy1hcHAtdG50c2htbGpsYTciLCJzdWIiOiI3ZmZmZDFkZS1hM2UyLTRkMDAtYjAzZi00Mjg4MjJhMmFlOWYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJtdWx0aXBsZXhpbmctYXBwLXRudHh4eHh4eHgtVVM2YmRQR3BGaGhqYml0eVFQd3JrZWNOIiwic2Vzc2lvbl9zdGF0ZSI6IjQzMTJkODcyLTNmZWUtNDlhMy05OGNmLTc1YjAwMDA5N2U0NiIsImFjciI6IjEiLCJyZXNvdXJjZV9hY2Nlc3MiOnsibXVsdGlwbGV4aW5nLWFwcC10bnR4eHh4eHh4Ijp7InJvbGVzIjpbImZpbmFuY2lhbC1pbnN0cnVtZW50czp3cml0ZSJdfX0sInNjb3BlIjoidXNlcl9pZCBzZXJ2aWNlLWFjY291bnQiLCJzZXJ2aWNlX2FjY291bnQiOnRydWUsImNsaWVudElkIjoibXVsdGlwbGV4aW5nLWFwcC10bnR4eHh4eHh4LVVTNmJkUEdwRmhoamJpdHlRUHdya2VjTiIsImNsaWVudEhvc3QiOiIxNzYuMTAwLjEwNS4yMzUiLCJjbGllbnRBZGRyZXNzIjoiMTc2LjEwMC4xMDUuMjM1In0.CBnEWsQb4fOjpI2T1SUcZ2OWqWIE1omnGwUnrjQQ604"
private const val INVALID_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9eyJleHAiOjE2MzA1MjIyODcsImlhdCI6MTYzMDUyMTk4NywianRpIjoiNWVjZGVyMzItOWM2My00YzAyLWE1MjMtYWE4MjRjYzI3NDRjIiwiaXNzIjoiaHR0cHM6Ly9hdXRoLnZlcnlnb29kc2VjdXJpdHkuY29tL2F1dGgvcmVhbG1zL3ZncyIsImF1ZCI6Im11bHRpcGxleGluZy1hcHAtdG50c2htbGpsYTciLCJzdWIiOiI3ZmZmZDFkZS1hM2UyLTRkMDAtYjAzZi00Mjg4MjJhMmFlOWYiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJtdWx0aXBsZXhpbmctYXBwLXRudHh4eHh4eHgtVVM2YmRQR3BGaGhqYml0eVFQd3JrZWNOIiwic2Vzc2lvbl9zdGF0ZSI6IjQzMTJkODcyLTNmZWUtNDlhMy05OGNmLTc1YjAwMDA5N2U0NiIsImFjciI6IjEiLCJyZXNvdXJjZV9hY2Nlc3MiOnsibXVsdGlwbGV4aW5nLWFwcC10bnR4eHh4eHh4Ijp7InJvbGVzIjpbImZpbmFuY2lhbC1pbnN0cnVtZW50czp3cml0ZSIsInRyYW5zZmVyczp3cml0ZSJdfX0sInNjb3BlIjoidXNlcl9pZCBzZXJ2aWNlLWFjY291bnQiLCJzZXJ2aWNlX2FjY291bnQiOnRydWUsImNsaWVudElkIjoibXVsdGlwbGV4aW5nLWFwcC10bnR4eHh4eHh4LVVTNmJkUEdwRmhoamJpdHlRUHdya2VjTiIsImNsaWVudEhvc3QiOiIxNzYuMTAwLjEwNS4yMzUiLCJjbGllbnRBZGRyZXNzIjoiMTc2LjEwMC4xMDUuMjM1In0SPLLms7ed5aLOEo3LLQzU0r-D_eTAx-GNrdsDLCLCu4"
private const val EMPTY_JWT = ""

class StringKtTest {

    @Test
    fun decodeJwtPayload_validJWT_decodedPayloadReturned() {
        // Arrange
        val expected =
            "{\"exp\":1630522287,\"iat\":1630521987,\"jti\":\"5ecder32-9c63-4c02-a523-aa824cc2744c\",\"iss\":\"https://auth.verygoodsecurity.com/auth/realms/vgs\",\"aud\":\"multiplexing-app-tntshmljla7\",\"sub\":\"7fffd1de-a3e2-4d00-b03f-428822a2ae9f\",\"typ\":\"Bearer\",\"azp\":\"multiplexing-app-tntxxxxxxx-US6bdPGpFhhjbityQPwrkecN\",\"session_state\":\"4312d872-3fee-49a3-98cf-75b000097e46\",\"acr\":\"1\",\"resource_access\":{\"multiplexing-app-tntxxxxxxx\":{\"roles\":[\"financial-instruments:write\"]}},\"scope\":\"user_id service-account\",\"service_account\":true,\"clientId\":\"multiplexing-app-tntxxxxxxx-US6bdPGpFhhjbityQPwrkecN\",\"clientHost\":\"176.100.105.235\",\"clientAddress\":\"176.100.105.235\"}"
        // Act
        val decodeResult = VALID_JWT.decodeJwtPayload()
        //Assert
        assertEquals(expected, decodeResult)
    }

    @Test
    fun decodeJwtPayload_invalidJWT_nullReturned() {
        // Arrange
        val expected: String? = null
        // Act
        val decodeResult = INVALID_JWT.decodeJwtPayload()
        //Assert
        assertEquals(expected, decodeResult)
    }

    @Test
    fun decodeJwtPayload_emptyJWT_nullReturned() {
        // Arrange
        val expected: String? = null
        // Act
        val decodeResult = EMPTY_JWT.decodeJwtPayload()
        //Assert
        assertEquals(expected, decodeResult)
    }

    @Test
    fun toJson_validString_jsonReturned() {
        // Arrange
        val data = "{\"test\":\"test\"}"
        // Act
        val jsonResult = data.toJson()
        // Assert
        assertNotNull(jsonResult)
        assertTrue(jsonResult!!.has("test"))
        assertEquals("test", jsonResult.get("test"))
    }

    @Test
    fun toJson_emptyString_nullReturned() {
        // Arrange
        val data = ""
        // Act
        val jsonResult = data.toJson()
        // Assert
        assertNull(jsonResult)
    }

    @Test
    fun toJson_invalidString_nullReturned() {
        // Arrange
        val data = "\"test\":\"test\"}"
        // Act
        val jsonResult = data.toJson()
        // Assert
        assertNull(jsonResult)
    }
}