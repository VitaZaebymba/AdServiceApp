package com.vita_zaebymba.adserviceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vita_zaebymba.adserviceapp.accounthelper.AccountHelper
import com.vita_zaebymba.adserviceapp.activity.EditAdAct
import com.vita_zaebymba.adserviceapp.adapters.AdRcAdapter
import com.vita_zaebymba.adserviceapp.databinding.ActivityMainBinding
import com.vita_zaebymba.adserviceapp.dialoghelper.DialogConst
import com.vita_zaebymba.adserviceapp.dialoghelper.DialogHelper
import com.vita_zaebymba.adserviceapp.dialoghelper.GoogleAccConst
import com.vita_zaebymba.adserviceapp.model.Ad
import com.vita_zaebymba.adserviceapp.viewmodel.FirebaseViewModel


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AdRcAdapter.Listener {
    private lateinit var tvAccount: TextView

    private lateinit var rootElement: ActivityMainBinding
    private val dialogHelper = DialogHelper(this) // dialoghelper
    val mAuth = Firebase.auth
    val adapter = AdRcAdapter(this)
    private val firebaseViewModel: FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityMainBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        init()
        initRecyclerView()
        initViewModel()
        firebaseViewModel.loadAllAds()
        bottomMenuOnClick()
    }

    override fun onResume() {
        super.onResume()
        rootElement.toolbarMainContent.bNavView.selectedItemId = R.id.id_home // переброс на главную страницу с объявлениями
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE){
            //Log.d("MyLog", "Sign in Result")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data) // передаем интент аккаунта, по которому зашли
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null){
                    dialogHelper.accHelper.signInFirebaseWithGoogle(account.idToken!!)
                }

            } catch (e:ApiException){
                Log.d("MyLog", "Api error: ${e.message}")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)
    }

    private fun initViewModel(){
        firebaseViewModel.liveAdsData.observe(this) { // ждет обновлений, viewModel решает, можно ли обновлять данные и доступен ли адаптер
            adapter.updateAdapter(it!!)// новый список it
            rootElement.toolbarMainContent.tvEmpty.visibility =
                if (it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun init(){
        setSupportActionBar(rootElement.toolbarMainContent.toolbar)
        var toggle = ActionBarDrawerToggle(this, rootElement.drawerLayout, rootElement.toolbarMainContent.toolbar, R.string.open, R.string.close) //кнопка для бокового меню
        rootElement.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        rootElement.navView.setNavigationItemSelectedListener(this) //navigation view будет передавать нажатия на кнопки меню
        tvAccount = rootElement.navView.getHeaderView(0).findViewById(R.id.tvAccountEmail) //отображение текста в header
    }

    private fun bottomMenuOnClick() = with(rootElement){
        toolbarMainContent.bNavView.setOnItemSelectedListener {
            item ->
                when(item.itemId){
                    R.id.id_new_ad -> {
                        val i = Intent(this@MainActivity, EditAdAct::class.java) // активити нового элемента
                        startActivity(i)
                    }
                    R.id.id_my_ads -> {
                        firebaseViewModel.loadMyAds()
                        toolbarMainContent.toolbar.title = getString(R.string.ad_my_ads)
                    }
                    R.id.id_favourites -> {
                        firebaseViewModel.loadMyFavs()
                        toolbarMainContent.toolbar.title = getString(R.string.ad_favourites)
                    }
                    R.id.id_home -> {
                        firebaseViewModel.loadAllAds()
                        toolbarMainContent.toolbar.title = getString(R.string.def)
                    }
                }

            true
        }
    }


    private fun initRecyclerView(){ // показ объявлений на главное странице
        rootElement.apply {
            toolbarMainContent.rcView.layoutManager = LinearLayoutManager(this@MainActivity)
            toolbarMainContent.rcView.adapter = adapter
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.id_my_ads -> {
                Toast.makeText(this, "Pressed to ads", Toast.LENGTH_LONG).show()
            }
            R.id.id_favourites -> {
                Toast.makeText(this, "Pressed to favourites", Toast.LENGTH_LONG).show()
            }
            R.id.id_car -> {
                Toast.makeText(this, "Pressed to cars", Toast.LENGTH_LONG).show()
            }
            R.id.id_pc -> {
                Toast.makeText(this, "Pressed to pc", Toast.LENGTH_LONG).show()
            }
            R.id.id_phones -> {
                Toast.makeText(this, "Pressed to phones", Toast.LENGTH_LONG).show()
            }
            R.id.id_dm -> {
                Toast.makeText(this, "Pressed to dm", Toast.LENGTH_LONG).show()
            }
            R.id.id_witcher -> {
                Toast.makeText(this, "Pressed to witcher", Toast.LENGTH_LONG).show()
            }
            R.id.id_sign_up -> {

                dialogHelper.createSignDialog(DialogConst.SIGN_UP_STATE) // создание диалогового окна с регистрацией

            }
            R.id.id_sign_in -> {

                dialogHelper.createSignDialog(DialogConst.SIGN_IN_STATE)

            }
            R.id.id_sign_out -> {
                if (mAuth.currentUser?.isAnonymous == true) { // проверка выхода анонимного пользователя
                    rootElement.drawerLayout.closeDrawer(GravityCompat.START)
                    return true
                }
                uiUpdate(null)
                mAuth.signOut()
                dialogHelper.accHelper.signOutGoogle()
            }

        }
        rootElement.drawerLayout.closeDrawer(GravityCompat.START) //меню сворачивается после нажатия на раздел
        return true
    }

    fun uiUpdate(user:FirebaseUser?){ // шапка бокового меню
        if (user == null) {
            dialogHelper.accHelper.signInAnonymously(object: AccountHelper.Listener{ // Запускается при успешной анонимной регистрации
                override fun onComplete() {
                    tvAccount.text = getString(R.string.guest_acc)
                }

            })
        } else if(user.isAnonymous){
            tvAccount.text = getString(R.string.guest_acc)
        } else if(!user.isAnonymous) {
            tvAccount.text = user.email
        }

    }

    companion object{
        const val EDIT_STATE = "edit_state"
        const val ADS_DATA = "ads_data"
    }

    override fun onDeleteItem(ad: Ad) {
        firebaseViewModel.deleteItem(ad)
    }

    override fun onAdViewed(ad: Ad) {  // 2 шаг, объявление, на котрое нажали приходит на MainActivity
        firebaseViewModel.adViewed(ad)
    }

    override fun onFavClicked(ad: Ad) {
        firebaseViewModel.onFavClick(ad)
    }

}