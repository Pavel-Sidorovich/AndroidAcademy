package com.pavesid.androidacademy.di

import javax.inject.Qualifier

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IODispatcher
