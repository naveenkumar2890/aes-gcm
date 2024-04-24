
import 'package:aes_gcm_plugin/aes_gcm_plugin_method_channel.dart';

class AesGcmPlugin {

  MethodChannelAesGcmPlugin? _methodChannelAesGcmPlugin;

  AesGcmPlugin(){
    _methodChannelAesGcmPlugin = MethodChannelAesGcmPlugin();
  }

  Future<String?>? getPlatformVersion() {
    return _methodChannelAesGcmPlugin?.getPlatformVersion();

  }  Future<String?>? gcmEncrypt(String plainText,String  key,String iv) {
    return _methodChannelAesGcmPlugin?.gcmEncrypt(plainText, key, iv);
  }
  Future<String?>? gcmDecrypt(String encryptedText,String key,String iv) {
    return _methodChannelAesGcmPlugin?.gcmDecrypt(encryptedText, key, iv);
  }

}
