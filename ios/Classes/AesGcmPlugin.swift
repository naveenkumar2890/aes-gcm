import Flutter
import CryptoKit
import Foundation
import CommonCrypto
//import Crypto
import CryptoTokenKit

public class AesGcmPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "aes_gcm_plugin", binaryMessenger: registrar.messenger())
    let instance = AesGcmPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
    case "getPlatformVersion":
      result("iOS " + UIDevice.current.systemVersion)
    case "gcmEncrypt":
         let arguments = call.arguments as? [String: Any]
        let plainText = arguments?["plainText"] as? String
        let key = arguments?["key"] as? String
        let iv = arguments?["iv"] as? String
        //result(AESGCMUtils.encryptData(plainText : "plainText", key : "Key", iv : "iv"))
        result(encryptData(plainText: plainText!, key: key!, iv: iv!))
        
    case "gcmDecrypt":
        let arguments = call.arguments as? [String: Any]
       let encryptedText = arguments?["encryptedText"] as? String
       let key = arguments?["key"] as? String
       let iv = arguments?["iv"] as? String
        result(decryptData(encryptedText: encryptedText!, key: key!, iv: iv!))

    default:
      result(FlutterMethodNotImplemented)

    }
  }
    
    func encryptData(plainText: String, key: String, iv: String) -> String? {
        guard let keyData = Data(base64Encoded: key),
              let ivData = iv.data(using: .utf8),
              let inputData = plainText.data(using: .utf8) else {
            return nil
        }
        if #available(iOS 13.0, *) {
            do {
                // Create a SymmetricKey from the base64-encoded key data
                let symmetricKey = SymmetricKey(data: keyData)
                
                
                // Initialize AES-GCM encryption with the key and nonce
                let sealedBox = try AES.GCM.seal(inputData, using: symmetricKey, nonce: AES.GCM.Nonce(data: ivData))
                
                // Obtain the ciphertext and authentication tag
                let ciphertext = sealedBox.ciphertext
                let authenticationTag = sealedBox.tag
                
                // Combine ciphertext and authentication tag into a single Data object
                var encryptedData = Data()
                encryptedData.append(ciphertext)
                encryptedData.append(authenticationTag)
                
                // Return base64-encoded string representation of the combined data
                return encryptedData.base64EncodedString()
                print("Encrypted data: \(encryptedData.base64EncodedString())")
            }
            catch {
                print("Encryption error: \(error.localizedDescription)")
                return nil
            }
        }
        return nil
    }
    
    
    func decryptData(encryptedText: String, key: String, iv: String) -> String? {
               guard let keyData = Data(base64Encoded: key),
                     let ivData = iv.data(using: .utf8),
                     let encryptedData = Data(base64Encoded: encryptedText) else {
                   return nil
               }
        if #available(iOS 13.0, *) {
            
            do {
                // Create a SymmetricKey from the base64-encoded key data
                let symmetricKey = SymmetricKey(data: keyData)
                
                // Split the encryptedData into ciphertext and authentication tag
                let tagLength = 16 // Size of the authentication tag in bytes (AES-GCM uses 128-bit authentication tag)
                let ciphertext = encryptedData.prefix(encryptedData.count - tagLength)
                let tag = encryptedData.suffix(tagLength)
                
                // Create a sealed box with ciphertext and authentication tag
                let sealedBox = try AES.GCM.SealedBox(nonce: AES.GCM.Nonce(data: ivData), ciphertext: ciphertext, tag: tag)
                
                // Decrypt the sealed box using the symmetric key
                let decryptedData = try AES.GCM.open(sealedBox, using: symmetricKey)
                
                // Convert decrypted data to a UTF-8 string
                guard let decryptedString = String(data: decryptedData, encoding: .utf8) else {
                    return nil
                }
                
                return decryptedString
            } catch {
                print("Decryption error: \(error.localizedDescription)")
                return nil
            }
        }
        return nil
           }
}
