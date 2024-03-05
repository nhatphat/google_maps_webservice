package com.example.google_maps_webservice

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import androidx.annotation.NonNull
import androidx.annotation.UiThread

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

import java.math.BigInteger
import java.security.MessageDigest

/** GoogleMapsWebservicePlugin */
class GoogleMapsWebservicePlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  private lateinit var context: Context

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    context = flutterPluginBinding.applicationContext
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "google_maps_webservice")
    channel.setMethodCallHandler(this)
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    when (call.method) {
      "getHeaders" -> {
        getHeaders(call, result)
      }
      else -> {
        result.notImplemented()
      }
    }
  }

  private fun getHeaders(call: MethodCall, result: Result) {
    try {
      val packageManager = context!!.packageManager
      val packageName = context!!.packageName
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
        packageManager.getPackageInfo(
          packageName,
          PackageManager.GET_SIGNING_CERTIFICATES
        ).signingInfo.apkContentsSigners.forEach { signature ->
          val sha1 = parseSignature(signature)
          result.success(
            formatHeader(packageName, sha1)
          )
        }
      } else {
        @Suppress("DEPRECATION")
        packageManager.getPackageInfo(
          packageName,
          PackageManager.GET_SIGNATURES
        ).signatures.forEach { signature ->
          val sha1 = parseSignature(signature)
          result.success(
            formatHeader(packageName, sha1)
          )
        }
      }
    } catch (e: Exception) {
      result.error("ERROR", e.toString(), null)
    }
  }

  private fun formatHeader(packageName: String, sha1: String): Map<String, String> {
    return mapOf(
      "X-Android-Package" to packageName,
      "X-Android-Cert" to sha1
    )
  }

  private fun parseSignature(signature: Signature): String {
    val md: MessageDigest = MessageDigest.getInstance("SHA1")
    md.update(signature.toByteArray())

    val bytes: ByteArray = md.digest()
    val bigInteger = BigInteger(1, bytes)
    val hex: String = String.format("%0" + (bytes.size shl 1) + "x", bigInteger)

    return hex
  }
}
