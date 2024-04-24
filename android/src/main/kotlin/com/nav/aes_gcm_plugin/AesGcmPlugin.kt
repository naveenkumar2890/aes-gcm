package com.nav.aes_gcm_plugin

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

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
      val encryptedText = GCMUtil.encryptData(plainText,key,iv)
      result.success(encryptedText.toString())

    }
    if (call.method == "gcmDecrypt") {
      var encryptedText : String? = call.argument("encryptedText");
      var key : String? = call.argument("key");
      var iv : String? = call.argument("iv");
      val plainText = GCMUtil.decryptData(encryptedText,key,iv)
      result.success(plainText.toString())
    }
    else {
      result.notImplemented()
    }
  }
  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
