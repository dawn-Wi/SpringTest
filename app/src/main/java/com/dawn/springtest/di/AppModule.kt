package com.dawn.springtest.di

import android.content.Context
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import com.dawn.springtest.remote.TestSpring
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.intellij.lang.annotations.PrintFormat
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideNavController(@ApplicationContext context: Context) =
        NavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            navigatorProvider.addNavigator(DialogNavigator())
        }

    @Singleton
    @Provides
    fun provideTestSpring():TestSpring{
        val baseUrl=""

        val gson = GsonBuilder()
            .registerTypeAdapter(
                LocalDate::class.java,
                JsonDeserializer<Any?> { json, typeOfT, context -> LocalDate.parse(json.asString) }
            )
            .registerTypeAdapter(
                LocalDate::class.java,
                JsonSerializer<LocalDate?> { localDate, typeOfT, context -> JsonPrimitive(localDate.toString()) }
            )
            .registerTypeAdapter(
                LocalDateTime::class.java,
                JsonDeserializer<Any?> { json, typeOfT, context -> LocalDateTime.parse(json.asString) }
            )
            .registerTypeAdapter(
                LocalDateTime::class.java,
                JsonSerializer<LocalDateTime?> { localDateTime, typeOfT, context ->
                    JsonPrimitive(
                        localDateTime.toString()
                    )
                }
            )
            .create()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TestSpring::class.java)
    }
}