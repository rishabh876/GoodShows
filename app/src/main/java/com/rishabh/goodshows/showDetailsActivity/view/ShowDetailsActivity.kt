package com.rishabh.goodshows.showDetailsActivity.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.rishabh.goodshows.R
import com.rishabh.goodshows.app.GoodShowsApplication
import com.rishabh.goodshows.homeActivity.view.TvShowsAdapter
import com.rishabh.goodshows.models.TvShow
import com.rishabh.goodshows.network.NetworkConstants
import com.rishabh.goodshows.showDetailsActivity.presenter.ShowDetailsPresenter
import com.wang.avi.AVLoadingIndicatorView
import java.util.*
import javax.inject.Inject

class ShowDetailsActivity : MvpActivity<ShowDetailsPresenter.View, ShowDetailsPresenter>(),
        ShowDetailsPresenter.View,
        TvShowsAdapter.Listener {

    //region Views
    @BindView(R.id.backdrop_iv)
    lateinit var backdropImageView: ImageView
    @BindView(R.id.cover_iv)
    lateinit var coverImageView: ImageView
    @BindView(R.id.title_tv)
    lateinit var titleTv: TextView
    @BindView(R.id.desc_tv)
    lateinit var descriptionTv: TextView
    @BindView(R.id.star_rating_tv)
    lateinit var starRatingTv: TextView
    @BindView(R.id.year_tv)
    lateinit var yearTv: TextView
    @BindView(R.id.language_tv)
    lateinit var languageTv: TextView
    @BindView(R.id.similar_rv)
    lateinit var similarTvShowsRecyclerView: RecyclerView
    @BindView(R.id.progress_indicator)
    lateinit var progressIndicator: AVLoadingIndicatorView
    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar
    //endregion

    @Inject
    lateinit var showDetailsPresenter: ShowDetailsPresenter
    lateinit var adapter: TvShowsAdapter
    lateinit var clickedItemView: View

    companion object {
        const val TV_SHOW_KEY = "tv_show_key"
        fun getMyIntent(context: Context, tvShow: TvShow) =
                Intent(context, ShowDetailsActivity::class.java).apply { putExtra(TV_SHOW_KEY, tvShow) }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_details_activity)
        ButterKnife.bind(this)
        val tvShow = intent.extras.getSerializable(TV_SHOW_KEY) as TvShow
        initViews(tvShow)
        getPresenter().init(tvShow)
    }

    private fun initViews(tvShow: TvShow) {
        Glide.with(this)
                .load(NetworkConstants.BASE_URL_IMAGE + NetworkConstants.ImageSize.W780 + tvShow.backdropPath)
                .into(backdropImageView)

        Glide.with(this)
                .load(NetworkConstants.BASE_URL_IMAGE + NetworkConstants.ImageSize.W500 + tvShow.posterPath)
                .into(coverImageView)

        titleTv.text = tvShow.name
        descriptionTv.text = tvShow.overview
        starRatingTv.text = tvShow.voteAverage.toString()
        yearTv.text = tvShow.firstAirDate?.split("-")?.get(0)
        languageTv.text = Locale(tvShow.originalLanguage).displayLanguage

        initList()

        toolbar.setNavigationOnClickListener { onBackPressed() }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun initList() {
        adapter = TvShowsAdapter(this, R.layout.tv_show_horizontal_item_layout)
        similarTvShowsRecyclerView.adapter = adapter
        similarTvShowsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScrolled()
            }
        })
    }

    private fun onScrolled() {
        val lastItem = (similarTvShowsRecyclerView.layoutManager as LinearLayoutManager).itemCount
        val currentlyVisibleItem = (similarTvShowsRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        if (lastItem - currentlyVisibleItem < 5) {
            getPresenter().getMoreSimilarShows()
        }
    }

    override fun onTvShowClicked(tvShow: TvShow, itemView: View) {
        this.clickedItemView = itemView
        getPresenter().onTvShowClicked(tvShow)
    }

    override fun addSimilarTvShows(similarTvShows: List<TvShow>) {
        adapter.addItems(similarTvShows)
    }

    override fun showProgress() {
        progressIndicator.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressIndicator.visibility = View.GONE
    }

    override fun showFooterLoader() {
        adapter.showFooter()
    }

    override fun hideFooterLoader() {
        adapter.removeFooter()
    }

    override fun openTvShowDetailScreen(tvShow: TvShow) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)

        val intent = ShowDetailsActivity.getMyIntent(this, tvShow)
        ActivityCompat.startActivity(this, intent, options.toBundle())
        overridePendingTransition(0, 0);
    }

    override fun showError(errorMessage: String) {
        Snackbar.make(findViewById(R.id.parent_container), errorMessage, Snackbar.LENGTH_SHORT).show()
    }

    override fun createPresenter(): ShowDetailsPresenter {
        GoodShowsApplication.INSTANCE.appComponent.inject(this)
        return showDetailsPresenter
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }
}
