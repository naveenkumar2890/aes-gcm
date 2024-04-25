package com.nav.aes_gcm_plugin

import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import android.annotation.TargetApi
import android.os.Build
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/** AesGcmPlugin */
class AesGcmPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "aes_gcm_plugin")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    }
    if (call.method == "gcmEncrypt") {
      var plainText : String? = call.argument("plainText");
      var key : String? = call.argument("key");
      var iv : String? = call.argument("iv");
      android.util.Log.d("AesGcmPlugin", "onMethodCall: "+plainText)
      android.util.Log.d("AesGcmPlugin", "onMethodCall: "+key)
      android.util.Log.d("AesGcmPlugin", "onMethodCall: "+iv)
      val encryptedText = encryptData(plainText,key,iv)
      result.success(encryptedText.toString())

    }
    if (call.method == "gcmDecrypt") {
      var encryptedText : String? = call.argument("encryptedText");
      var key : String? = call.argument("key");
      var iv : String? = call.argument("iv");
      val plainText = decryptData(encryptedText,key,iv)
      result.success(plainText.toString())
    }
    else {
      result.notImplemented()
    }
  }
  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

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
}
