/*
 * Copyright (c) 2024 gematik GmbH
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the Licence);
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *     https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 */

package de.link4health.egk.tagobjects

import de.link4health.egk.command.EXPECTED_LENGTH_WILDCARD_SHORT
import org.bouncycastle.asn1.DEROctetString
import org.bouncycastle.asn1.DERTaggedObject

/**
 * Length object with TAG 97
 *
 * @param le extracted expected length from plain CommandApdu
 */
class LengthObject(le: Int) {

    companion object {
        private const val DO_97_TAG = 0x17
        private const val BYTE_MASK = 0xFF
        private const val BYTE_VALUE = 8
        private var leData = ByteArray(0)
    }

    /**
     * Represents a tagged object using the DERTaggedObject class.
     *
     * @property taggedObject The DERTaggedObject instance representing the tagged object.
     */
    val taggedObject: DERTaggedObject
        get() = DERTaggedObject(false, DO_97_TAG, DEROctetString(leData))

    init {
        if (le >= 0) {
            leData = when {
                le == EXPECTED_LENGTH_WILDCARD_SHORT -> {
                    byteArrayOf(0x00)
                }

                le > EXPECTED_LENGTH_WILDCARD_SHORT -> {
                    byteArrayOf(
                        (le shr BYTE_VALUE and BYTE_MASK).toByte(),
                        (le and BYTE_MASK).toByte(),
                    )
                }

                else -> {
                    byteArrayOf(le.toByte())
                }
            }
        }
    }
}
