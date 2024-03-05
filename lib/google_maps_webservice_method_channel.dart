import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'google_maps_webservice_platform_interface.dart';

/// An implementation of [GoogleMapsWebservicePlatform] that uses method channels.
class MethodChannelGoogleMapsWebservice extends GoogleMapsWebservicePlatform {
  Map<String, String> _header = {};

  bool get isMobile {
    return Platform.isAndroid || Platform.isIOS;
  }

  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('google_maps_webservice');

  @override
  Future<Map<String, String>> getHeaders() async {
    // return cached header
    if (_header.isNotEmpty || kIsWeb || !isMobile) return _header;

    //
    final header = await methodChannel.invokeMethod('getHeaders');
    _header = header?.cast<String, String>() ?? {};
    return _header;
  }
}
