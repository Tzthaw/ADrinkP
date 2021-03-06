package com.thawzintoe.ptut.adrinkp.mvp.views

import android.widget.ImageView
import com.mmgoogleexpert.ptut.shared.model.BaseView
import com.thawzintoe.ptut.adrinkp.vos.filterList.DrinksCategoryFilter

interface FilterView : BaseView {
    fun launchDetails(item:DrinksCategoryFilter,imageView: ImageView)
}