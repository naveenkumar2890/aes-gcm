import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:aes_gcm_plugin/aes_gcm_plugin.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _encryptedText = 'Unknown';
  String _plainText = 'Unknown';
  final _aesGcmPlugin = AesGcmPlugin();


  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String  encryptedText = 'Unknown';
    String  plainText = 'Your Plain Text';
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.

    try {
       encryptedText =
          await _aesGcmPlugin.gcmEncrypt("Your Plain Text ",  "12345678912345670648654658281111","101112345678") ?? 'Unknown Exception';
    }
    catch (e) {
      print(e);
    }
    try {
      plainText =
          await _aesGcmPlugin.gcmDecrypt(encryptedText, "12345678912345670648654658281111","101112345678") ?? 'Unknown Exception';
    }
    catch (e) {
      print(e);
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
       _encryptedText = encryptedText;
      _plainText = plainText;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.blue,
          title: const Text('AES-GCM Plugin example app'),
        ),
        body: Center(
          child: Text('Encrypted Text: $_encryptedText\n Plain Text:$_plainText\n',textAlign: TextAlign.center),
        ),
      ),
    );
  }
}
