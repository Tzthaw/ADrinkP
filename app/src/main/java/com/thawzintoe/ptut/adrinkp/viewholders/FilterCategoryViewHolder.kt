package com.thawzintoe.ptut.adrinkp.viewholders

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.thawzintoe.ptut.adrinkp.R
import com.thawzintoe.ptut.adrinkp.deligate.onTapFilterItem
import com.thawzintoe.ptut.adrinkp.utils.load
import com.thawzintoe.ptut.adrinkp.vos.filterList.DrinksCategoryFilter
import com.thawzintoe.ptut.adrinkp.viewholders.base.BaseViewHolder
import kotlinx.android.synthetic.main.content_filter_item.view.*

class FilterCategoryViewHolder(itemView: View,private val tapItem:onTapFilterItem):BaseViewHolder<DrinksCategoryFilter>(itemView) {
    private lateinit var drinksCategoryFilter: DrinksCategoryFilter
    private val filterName=itemView.filterName
    private val filterImage=itemView.filterImage

    override fun setData(data: DrinksCategoryFilter) {
        drinksCategoryFilter=data
        if(data.strDrink!!.isEmpty() && data.strDrinkThumb!!.isEmpty())
            itemView.visibility=View.GONE
        filterImage.load(data.strDrinkThumb!!)
        filterName.text=data.strDrink
    }

    override fun onClick(v: View?) {
        tapItem.tapFilter(drinksCategoryFilter,itemView.filterImage)
    }
}