package com.rishabh.goodshows.homeActivity.view

import android.support.annotation.LayoutRes
import android.support.annotation.Nullable
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.rishabh.goodshows.R
import com.rishabh.goodshows.models.TvShow
import com.rishabh.goodshows.network.NetworkConstants

class TvShowsAdapter(private var listener: Listener,
                     @LayoutRes private val tvShowLayout: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEMS = 1
    private val FOOTER = 2
    private var isFooterVisible = false
    private var tvShowsList = ArrayList<TvShow>()
    private var isVertical = true

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        isVertical = ((recyclerView.layoutManager as? LinearLayoutManager)?.orientation != OrientationHelper.HORIZONTAL)
    }

    fun removeFooter() {
        if (isFooterVisible) {
            isFooterVisible = false
            notifyItemRemoved(itemCount)
        }
    }

    fun addItems(tvShowsList: List<TvShow>) {
        val position = this.tvShowsList.size
        this.tvShowsList.addAll(tvShowsList)
        notifyItemRangeInserted(position, tvShowsList.size)
    }

    fun showFooter() {
        if (!isFooterVisible) {
            isFooterVisible = true
            notifyItemInserted(itemCount - 1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (tvShowsList.size > position) {
            ITEMS
        } else {
            FOOTER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        return when (viewType) {
            ITEMS -> {
                val view = LayoutInflater.from(parent!!.context).inflate(tvShowLayout, parent, false)
                TvShowsViewHolder(view)
            }
            FOOTER -> {
                var footerLayout = R.layout.footer_progress
                if (!isVertical)
                    footerLayout = R.layout.horizontal_footer_progress

                val view = LayoutInflater.from(parent!!.context).inflate(footerLayout, parent, false)
                ProgressFooterViewHolder(view)
            }
            else -> {
                null
            }
        }
    }

    override fun getItemCount(): Int {
        var itemCount = tvShowsList.size
        if (isFooterVisible)
            itemCount++
        return itemCount
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is TvShowsViewHolder) {
            holder.bind(tvShowsList[position])
        }
    }

    interface Listener {
        fun onTvShowClicked(tvShow: TvShow, itemView: View)
    }

    inner class TvShowsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @BindView(R.id.tvshow_container)
        lateinit var tvShowContainer: ViewGroup
        @BindView(R.id.cover_iv)
        lateinit var coverImageView: ImageView
        @BindView(R.id.title_tv)
        lateinit var titleTv: TextView
        @BindView(R.id.star_rating_tv)
        lateinit var starRatingTv: TextView

        @BindView(R.id.desc_tv)
        @Nullable
        @JvmField
        var descTv: TextView? = null
        @BindView(R.id.year_tv)
        @Nullable
        @JvmField
        var yearTv: TextView? = null

        fun bind(tvShow: TvShow) {
            Glide.with(coverImageView)
                    .load(NetworkConstants.BASE_URL_IMAGE + NetworkConstants.ImageSize.W500 + tvShow.posterPath)
                    .into(coverImageView)

            titleTv.text = tvShow.name
            descTv?.text = tvShow.overview
            starRatingTv.text = tvShow.voteAverage.toString()
            yearTv?.text = tvShow.firstAirDate?.split("-")?.get(0)

            tvShowContainer.setOnClickListener { listener.onTvShowClicked(tvShow, itemView) }
        }

        init {
            ButterKnife.bind(this, view)
        }

    }

    inner class ProgressFooterViewHolder(view: View) : RecyclerView.ViewHolder(view)
}