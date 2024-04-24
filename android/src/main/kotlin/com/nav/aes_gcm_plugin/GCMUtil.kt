import android.annotation.TargetApi
import android.os.Build
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object GCMUtil {
    @TargetApi(Build.VERSION_CODES.O)
    fun encryptData(plainText: String?, key: String?, iv: String?): String {
//        checkEncryptionArguments(plainText, key, iv)

        val pubKey = Base64.getDecoder().decode(key)
        val nonce = iv?.toByteArray(Charsets.UTF_8)
        val inputData = plainText?.toByteArray(Charsets.UTF_8)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val keySpec = SecretKeySpec(pubKey, "AES")
        val gcmParameterSpec = GCMParameterSpec(128, nonce)

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec)
        android.util.Log.d("GCMUtil", "encryptData: " + inputData.toString())
        val encryptedData = cipher.doFinal(inputData)

        return Base64.getEncoder().encodeToString(encryptedData)
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun decryptData(encryptedText: String?, key: String?, iv: String?): String {
//        checkEncryptionArguments(encryptedText, key, iv)

        val pubKey = Base64.getDecoder().decode(key)
        val nonce = iv?.toByteArray(Charsets.UTF_8)
        val encryptedData = Base64.getDecoder().decode(encryptedText)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val keySpec = SecretKeySpec(pubKey, "AES")
        val gcmParameterSpec = GCMParameterSpec(128, nonce)

        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec)
        val decryptedData = cipher.doFinal(encryptedData)

        return String(decryptedData, Charsets.UTF_8)
    }

/*    private fun checkEncryptionArguments(plainText: String?, key: String?, iv: String?) {
        require(plainText?.isNotEmpty()) { "PlainText cannot be empty." }
        require(key?.isNotEmpty()) { "Key cannot be empty." }
        require(iv?.isNotEmpty()) { "IV cannot be empty." }
    }*/
}