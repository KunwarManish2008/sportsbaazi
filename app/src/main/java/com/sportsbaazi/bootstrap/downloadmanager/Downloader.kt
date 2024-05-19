package com.sportsbaazi.bootstrap.downloadmanager

/**
 * Created by Manish Kumar on 19/05/24.
 */
interface Downloader {
    fun downloadFile(url: String): Long

}