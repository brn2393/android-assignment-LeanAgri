package com.brn2393.assignment.leanagri.domain.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.brn2393.assignment.leanagri.data.Repository
import com.brn2393.assignment.leanagri.data.RepositoryImpl
import com.brn2393.assignment.leanagri.data.local.TmdbDatabase
import com.brn2393.assignment.leanagri.data.remote.RemoteAPI
import com.brn2393.assignment.leanagri.data.remote.factories.RetrofitFactory

abstract class BaseActivity : AppCompatActivity() {

    var remoteAPI: RemoteAPI? = null
    var repository: Repository? = null
    var tmdbDatabase: TmdbDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        remoteAPI = RetrofitFactory.getRetrofit().create(RemoteAPI::class.java)
        tmdbDatabase = TmdbDatabase.getInstance(this)
        repository = RepositoryImpl(this, remoteAPI!!, tmdbDatabase!!)
        initiate()
    }

    abstract fun initiate()
}