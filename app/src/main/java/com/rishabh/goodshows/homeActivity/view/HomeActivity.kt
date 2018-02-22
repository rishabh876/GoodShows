package com.rishabh.goodshows.homeActivity.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.rishabh.goodshows.R
import com.rishabh.goodshows.app.GoodShowsApplication
import com.rishabh.goodshows.homeActivity.presenter.HomePresenter
import com.rishabh.goodshows.models.TvShow
import com.wang.avi.AVLoadingIndicatorView
import javax.inject.Inject

class HomeActivity : MvpActivity<HomePresenter.View, HomePresenter>(), HomePresenter.View {

    @BindView(R.id.progress_indicator)
    lateinit var progressIndicator: AVLoadingIndicatorView
    @BindView(R.id.shows_list_rv)
    lateinit var showsListRv: RecyclerView

    private lateinit var adapter: TvShowsAdapter

    @Inject
    lateinit var homePresenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        ButterKnife.bind(this)
        initViews()
        getPresenter().init()
        showsListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScrolled()
            }
        })
    }

    private fun onScrolled() {
        val lastItem = (showsListRv.layoutManager as LinearLayoutManager).itemCount
        val currentlyVisibleItem = (showsListRv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        if (lastItem - currentlyVisibleItem < 5) {
            getPresenter().getMoreTvShows()
        }
    }

    private fun initViews() {
        adapter = TvShowsAdapter()
        showsListRv.adapter = adapter
    }

    override fun createPresenter(): HomePresenter {
        GoodShowsApplication.INSTANCE.appComponent.inject(this)
        return homePresenter
    }

    override fun showFooterLoader() {
        adapter.showFooter()
    }

    override fun hideFooterLoader() {
        adapter.removeFooter()
    }

    override fun showFullscreenProgress() {
        progressIndicator.visibility = View.VISIBLE
    }

    override fun hideFullscreenProgress() {
        progressIndicator.visibility = View.GONE
    }

    override fun addItems(tvShows: List<TvShow>) {
        adapter.addItems(tvShows)
    }

    override fun showError(errorMessage: String) {
        Snackbar.make(findViewById(R.id.parent_container), errorMessage, Snackbar.LENGTH_SHORT).show()
    }
}
