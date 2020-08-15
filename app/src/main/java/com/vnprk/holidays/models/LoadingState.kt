package com.vnprk.holidays.models

class LoadingState(val status: Status, val msg: String) {
    companion object {

        val LOADED: LoadingState
        val LOADING: LoadingState

        init {
            LOADED = LoadingState(Status.SUCCESS, "Success")
            LOADING = LoadingState(Status.RUNNING, "Running")
        }
    }

}