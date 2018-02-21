package com.rishabh.goodshows.homeActivity.view

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


class TvShowsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEMS = 1
    private val FOOTER = 2
    private var isFooterVisible = false
    private var tvShowsList = ArrayList<TvShow>()

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
                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.tv_show_list_item, parent, false)
                TvShowsViewHolder(view)
            }
            FOOTER -> {
                val view = LayoutInflater.from(parent!!.context).inflate(R.layout.footer_progress, parent, false)
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
        } else if (holder is ProgressFooterViewHolder) {
            holder.bind()
        }
    }

    inner class TvShowsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @BindView(R.id.imageView)
        lateinit var imageView: ImageView
        @BindView(R.id.title_tv)
        lateinit var titleTv: TextView
        @BindView(R.id.desc_tv)
        lateinit var descTv: TextView

        fun bind(tvShow: TvShow) {
            Glide.with(imageView)
                    .load(NetworkConstants.BASE_URL_IMAGE + NetworkConstants.ImageSize.W500 + tvShow.posterPath)
                    .into(imageView)
            titleTv.text = tvShow.name
            descTv.text = tvShow.overview
        }

        init {
            ButterKnife.bind(this, view)
        }
    }

    inner class ProgressFooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind() {

        }

        init {
            ButterKnife.bind(this, view)
        }
    }

}