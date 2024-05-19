package com.sportsbaazi.bootstrap.downloadmanager

import android.app.DownloadManager
import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.core.net.toUri

/**
 * Created by Manish Kumar on 19/05/24.
 */
class AndroidDownloader(
    private val context: Context
): Downloader {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("application/vnd.android.package-archive")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("sportsbaazi.apk")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "sportsbaazi.apk")
        return downloadManager.enqueue(request)
    }
}