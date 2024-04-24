import 'package:flutter/services.dart';

class MethodChannelAesGcmPlugin{

  final methodChannel = const MethodChannel('aes_gcm_plugin');

  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
  Future<String?> gcmEncrypt(String plainText, String key,String iv) async {
    final encryptedText = await methodChannel.invokeMethod<String>('gcmEncrypt', {'plainText': plainText,'key': key,'iv': iv });
    return encryptedText;
  }
  Future<String?> gcmDecrypt(String encryptedText, String key,String iv) async {
    final plainText = await methodChannel.invokeMethod<String>('gcmDecrypt', {'encryptedText': encryptedText,'key': key,'iv': iv });
    return plainText;
  }
}
