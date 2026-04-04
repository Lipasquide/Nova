package xyz.xenondevs.nova.resources.builder

import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import org.junit.jupiter.api.assertThrows

class PNGMetadataRemoverTest {

    @Test
    fun testLargeChunkNoOOM() {
        val out = ByteArrayOutputStream()
        val dataOut = DataOutputStream(out)

        // PNG Header
        dataOut.write(byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A))

        // IHDR chunk
        dataOut.writeInt(13)
        dataOut.writeBytes("IHDR")
        dataOut.write(ByteArray(13))
        dataOut.writeInt(0) // CRC

        // Malicious PLTE chunk with huge length (positive but larger than available data)
        dataOut.writeInt(0x70000000)
        dataOut.writeBytes("PLTE")

        val input = ByteArrayInputStream(out.toByteArray())
        val output = ByteArrayOutputStream()

        // This should not throw OutOfMemoryError anymore
        PNGMetadataRemover.remove(input, output)
    }

    @Test
    fun testNegativeChunkLength() {
        val out = ByteArrayOutputStream()
        val dataOut = DataOutputStream(out)

        // PNG Header
        dataOut.write(byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A))

        // IHDR chunk
        dataOut.writeInt(13)
        dataOut.writeBytes("IHDR")
        dataOut.write(ByteArray(13))
        dataOut.writeInt(0) // CRC

        // Malicious chunk with negative length (0x80000000)
        dataOut.writeInt(Int.MIN_VALUE)
        dataOut.writeBytes("TEST")

        val input = ByteArrayInputStream(out.toByteArray())
        val output = ByteArrayOutputStream()

        assertThrows<IllegalStateException> {
            PNGMetadataRemover.remove(input, output)
        }
    }
}
