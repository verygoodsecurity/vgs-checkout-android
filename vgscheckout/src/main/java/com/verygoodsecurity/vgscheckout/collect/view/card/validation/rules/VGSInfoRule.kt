package com.verygoodsecurity.vgscheckout.collect.view.card.validation.rules

internal class VGSInfoRule private constructor(
    regex: String?,
    length: Array<Int>?
) : ValidationRule(regex, length) {

    /**
     * This class provides an API for set up rules for validation person name.
     */
    class ValidationBuilder {

        /** The Regex for validation input. */
        protected var regex: String? = null

        /** The minimum length of the person name which will support. */
        protected var minLength = -1

        /** The maximum length of the person name which will support. */
        protected var maxLength = -1

        /** Configure Regex for validation input. */
        fun setRegex(regex: String): ValidationBuilder {
            this.regex = regex
            return this
        }

        /** Configure minimum length of the name which will support. */
        fun setAllowableMinLength(length: Int): ValidationBuilder {
            if (maxLength == -1) {
                maxLength = 256
            }
            minLength = if (length > maxLength) {
                maxLength
            } else {
                length
            }
            return this
        }

        /** Configure maximum length of the name which will support. */
        fun setAllowableMaxLength(length: Int): ValidationBuilder {
            if (minLength == -1) {
                minLength = 1
            }
            if (length < minLength) {
                minLength = length
            }
            maxLength = length
            return this
        }

        private fun getRange(): Array<Int>? {
            return if (minLength != -1 && maxLength != -1) {
                (minLength..maxLength).toList().toTypedArray()
            } else {
                null
            }
        }

        /** Creates a rule. */
        fun build(): VGSInfoRule {
            return VGSInfoRule(
                regex,
                getRange()
            )
        }
    }

}