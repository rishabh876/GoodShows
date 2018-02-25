package com.rishabh.goodshows.popularShowsActivity.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.rishabh.goodshows.R
import com.rishabh.goodshows.app.GoodShowsApplication
import com.rishabh.goodshows.common.TvShowsAdapter
import com.rishabh.goodshows.popularShowsActivity.presenter.PopularShowsPresenter
import com.rishabh.goodshows.models.TvShow
import com.rishabh.goodshows.showDetailsActivity.view.ShowDetailsActivity
import com.wang.avi.AVLoadingIndicatorView
import javax.inject.Inject


class PopularTvShowsActivity : MvpActivity<PopularShowsPresenter.View, PopularShowsPresenter>(),
        PopularShowsPresenter.View,
        TvShowsAdapter.Listener {

    @BindView(R.id.progress_indicator)
    lateinit var progressIndicator: AVLoadingIndicatorView
    @BindView(R.id.shows_list_rv)
    lateinit var showsListRv: RecyclerView

    private lateinit var adapter: TvShowsAdapter
    private lateinit var clickedItemView: View

    @Inject
    lateinit var popularShowsPresenter: PopularShowsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popular_shows_activity)
        ButterKnife.bind(this)
        initViews()
        getPresenter().init()
    }

    private fun initViews() {
        adapter = TvShowsAdapter(this, R.layout.popular_tv_show_list_item)
        showsListRv.adapter = adapter
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

    override fun createPresenter(): PopularShowsPresenter {
        GoodShowsApplication.INSTANCE.appComponent.inject(this)
        return popularShowsPresenter
    }

    override fun onTvShowClicked(tvShow: TvShow, itemView: View) {
        this.clickedItemView = itemView
        getPresenter().onTvShowClicked(tvShow)
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

    override fun openTvShowDetailScreen(tvShow: TvShow) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
        startActivity(intent, options.toBundle())
        val intent = ShowDetailsActivity.getMyIntent(this, tvShow)
        ActivityCompat.startActivity(this, intent, options.toBundle())
        overridePendingTransition(0, 0);
    }
}
