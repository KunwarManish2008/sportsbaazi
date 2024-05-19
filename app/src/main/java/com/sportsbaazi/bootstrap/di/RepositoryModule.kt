package com.sportsbaazi.bootstrap.di

import com.sportsbaazi.bootstrap.data.repository.NewsRepository
import com.sportsbaazi.bootstrap.data.repository.NewsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindNewsRepo(
        newsRepository: NewsRepositoryImpl
    ): NewsRepository
}
