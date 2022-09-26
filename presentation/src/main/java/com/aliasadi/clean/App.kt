package com.aliasadi.clean

import android.app.Application
import com.aliasadi.clean.di.DaggerInjector
import com.aliasadi.clean.di.core.CoreComponent
import com.aliasadi.clean.di.core.DaggerCoreComponent
import com.aliasadi.clean.di.core.module.AppModule
import com.aliasadi.clean.di.core.module.DataModule
import com.aliasadi.clean.di.core.module.DatabaseModule
import com.aliasadi.clean.di.core.module.NetworkModule
import com.aliasadi.clean.di.details.MovieDetailsModule
import com.aliasadi.clean.di.details.MovieDetailsSubComponent
import com.aliasadi.clean.di.favorites.FavoritesModule
import com.aliasadi.clean.di.favorites.FavoritesSubComponent
import com.aliasadi.clean.di.feed.FeedModule
import com.aliasadi.clean.di.feed.FeedSubComponent
import com.aliasadi.clean.di.main.MainModule
import com.aliasadi.clean.di.main.MainSubComponent
import com.aliasadi.clean.di.search.SearchModule
import com.aliasadi.clean.di.search.SearchSubComponent
import com.aliasadi.clean.ui.favorites.FavoritesFragment
import com.aliasadi.clean.ui.feed.FeedFragment
import com.aliasadi.clean.ui.main.MainActivity
import com.aliasadi.clean.ui.moviedetails.MovieDetailsFragment
import com.aliasadi.clean.ui.search.SearchActivity
import com.aliasadi.data.BuildConfig

/**
 * Created by Ali Asadi on 13/05/2020
 */
class App : Application(), DaggerInjector {

    private lateinit var coreComponent: CoreComponent

    override fun onCreate() {
        super.onCreate()
        coreComponent = DaggerCoreComponent.builder()
            .appModule(AppModule(applicationContext))
            .networkModule(NetworkModule(BuildConfig.BASE_URL))
            .databaseModule(DatabaseModule())
            .dataModule(DataModule())
            .build()
    }

    private fun createMoviesDetailsComponent(): MovieDetailsSubComponent = coreComponent.plus(MovieDetailsModule())
    private fun createFeedComponent(): FeedSubComponent = coreComponent.plus(FeedModule())
    private fun createFavoritesComponent(): FavoritesSubComponent = coreComponent.plus(FavoritesModule())
    private fun createMainComponent(): MainSubComponent = coreComponent.plus(MainModule())
    private fun createSearchComponent(): SearchSubComponent = coreComponent.plus(SearchModule())

    override fun <T> inject(view: T) {
        when (view) {
            is MainActivity -> createMainComponent().inject(view)
            is MovieDetailsFragment -> createMoviesDetailsComponent().inject(view)
            is FeedFragment -> createFeedComponent().inject(view)
            is FavoritesFragment -> createFavoritesComponent().inject(view)
            is SearchActivity -> createSearchComponent().inject(view)
        }
    }
}