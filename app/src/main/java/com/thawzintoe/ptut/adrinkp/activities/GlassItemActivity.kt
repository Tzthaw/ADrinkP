package com.thawzintoe.ptut.adrinkp.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.thawzintoe.ptut.adrinkp.R
import com.thawzintoe.ptut.adrinkp.activities.base.BaseActivity
import com.thawzintoe.ptut.adrinkp.adapters.GlassItemsAdapter
import com.thawzintoe.ptut.adrinkp.components.EmptyViewPod
import com.thawzintoe.ptut.adrinkp.vos.categoryList.DrinksGlass
import com.thawzintoe.ptut.adrinkp.mvp.presenters.CategoryPresenter
import com.thawzintoe.ptut.adrinkp.mvp.views.CategoryView
import com.thawzintoe.ptut.adrinkp.utils.*
import com.thawzintoe.ptut.adrinkp.components.SmartScrollListener
import kotlinx.android.synthetic.main.activity_category_item.*
import kotlinx.android.synthetic.main.activity_search_detail.*
import kotlinx.android.synthetic.main.app_bar_category_item.*
import kotlinx.android.synthetic.main.content_category_item.*


class GlassItemActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, CategoryView {
    private lateinit var glassAdapter: GlassItemsAdapter
    private lateinit var categoryPresenter: CategoryPresenter
    private var mSmartScrollListener: SmartScrollListener? = null
    private var emptyViewPod:EmptyViewPod?=null
    override fun launchFilter(itemName: String) {
        startActivity(CategoryFilterActivity.newIntent(applicationContext, itemName, GLASS_NAME))
        overridePendingTransition(R.anim.enter, R.anim.exit)
    }
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, GlassItemActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_item)
        toolbar_title.text = GLASS_NAME
        setSupportActionBar(toolbar)
        setUpUIComponent()
        navigateTool()

        categoryPresenter.errorLD.observe(this,this@GlassItemActivity)
        categoryPresenter.drinkGlassLD!!.observe(this, Observer<List<DrinksGlass>> { drinkGlass ->
            swipeRefreshLayout.isRefreshing=false
            glassAdapter.setNewData(drinkGlass as MutableList<DrinksGlass>)
        })


    }
    private fun setUpUIComponent() {
        swipeRefreshLayout.isRefreshing=true
        categoryPresenter = ViewModelProviders.of(this).get(CategoryPresenter::class.java)
        categoryPresenter.initPresenter(this@GlassItemActivity)
        categoryPresenter.onFinishGlass("list")

        itemRecycler.layoutManager = LinearLayoutManager(applicationContext)
        glassAdapter = GlassItemsAdapter(applicationContext, categoryPresenter)
        itemRecycler.adapter = glassAdapter
        mSmartScrollListener = SmartScrollListener(object : SmartScrollListener.OnSmartScrollListener {
            override fun onListEndReach() {
                categoryPresenter.onFinishGlass("list")
            }
        })
        itemRecycler.addOnScrollListener(mSmartScrollListener)
        swipeRefreshLayout.setOnRefreshListener {
            categoryPresenter.onFinishGlass("list")
        }
    }

    private fun navigateTool(){
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.category_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings ->{
                profileDialog(this,"ADrink","Cocktail & Other Drink Recipes and Preview\n#toezinthaw@gmail.com")
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_category -> {
                startActivity(CategoryItemActivity.newIntent(this))
                finish()
            }
            R.id.nav_glass -> {
//                startActivity(GlassItemActivity.newIntent(this))
//                finish()
            }
            R.id.nav_ingredient -> {
                startActivity(IngredientItemActivity.newIntent(this))
                finish()
            }
            R.id.nav_random ->{
                startActivity(RandomDrinkActivity.newIntent(this))
            }
            R.id.nav_alcohol -> {
                startActivity(AlcoholItemActivity.newIntent(this))
                finish()
            }
            R.id.nav_search ->{
                startActivity(SearchCocktailActivity.newIntent(this))
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onChanged(error: Error?) {
        error?.let {
            when (it) {
                is EmptyError -> {
                    swipeRefreshLayout.isRefreshing = false
                    emptyViewPod=emptyLayout as EmptyViewPod
                    emptyViewPod!!.setEmptyData(R.drawable.empty_img,"Product Not Found")
                    emptyDetail.setBackgroundColor(resources.getColor(R.color.white))
                    emptyLayout.visibility = View.VISIBLE
                    itemRecycler.setEmptyView(emptyLayout)
                }
                is NetworkError ->{
                    swipeRefreshLayout.isRefreshing = false
                    emptyViewPod=emptyLayout as EmptyViewPod
                    emptyLayout.setBackgroundColor(resources.getColor(R.color.white))
                    emptyViewPod!!.setEmptyData(R.drawable.nointernet,"No Internet Connection")
                    emptyLayout.visibility = View.VISIBLE
                    itemRecycler.setEmptyView(emptyViewPod!!)
                    showNetworkError(rootLayout, applicationContext, error as NetworkError)
                }
            }
        }


    }
}