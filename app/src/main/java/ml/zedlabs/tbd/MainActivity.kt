package ml.zedlabs.tbd

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.android.billingclient.api.*
import com.android.billingclient.api.Purchase.PurchaseState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ml.zedlabs.tbd.databinding.ActivityMainBinding
import ml.zedlabs.tbd.ui.profile.ProfileViewModel
import ml.zedlabs.tbd.ui.theme.AppThemeType
import ml.zedlabs.tbd.util.asApplication
import ml.zedlabs.tbd.util.changeStatusBarColor

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var billingClient: BillingClient

    private val profileViewModel: ProfileViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    companion object {
        const val P_ID_APP_SUB = "p_id_app_sub"

        enum class BillingAction {
            SHOW_PRODUCTS,
            CHECK_ACTIVE_SUB
        }
    }

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    lifecycleScope.launch {
                        // update the app if a purchase is found, use a top level
                        // boolean in the activity
                        handlePurchase(purchase)
                    }
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
            } else {
                // Handle any other error codes.
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.appTheme.collectLatest { value ->
                    when (value) {
                        AppThemeType.Dark.name -> {
                            changeStatusBarColor(
                                ContextCompat.getColor(this@MainActivity, R.color.bg_dark),
                                false
                            )
                            binding.root.background =
                                ResourcesCompat.getDrawable(resources, R.color.bg_dark, null)
                        }

                        AppThemeType.LightAlternate.name -> {
                            changeStatusBarColor(
                                ContextCompat.getColor(this@MainActivity, R.color.bg_light),
                                true
                            )
                            binding.root.background =
                                ResourcesCompat.getDrawable(resources, R.color.bg_light, null)
                        }
                        else -> {
                            changeStatusBarColor(
                                ContextCompat.getColor(this@MainActivity, R.color.bg_light),
                                true
                            )
                            binding.root.background =
                                ResourcesCompat.getDrawable(resources, R.color.bg_light, null)
                        }
                    }
                }
            }
        }

//        navController = Navigation.findNavController(this, R.id.fragment)
//        binding.bottomNavigation.setupWithNavController(navController)
        this.asApplication()?.logFirebase("APP_OPEN")
        createBillingClient(BillingAction.CHECK_ACTIVE_SUB)
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.movie_detail_screen, R.id.tv_detail_screen, R.id.chart_screen -> binding.bottomNavigation.visibility =
//                    View.GONE
//
//                else -> binding.bottomNavigation.visibility = View.VISIBLE
//            }
//        }

    }

    //returns where a successful instance of the billing client is created
    fun createBillingClient(billingAction: BillingAction) {
        billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    lifecycleScope.launch {
                        when (billingAction) {
                            BillingAction.SHOW_PRODUCTS -> processProductList()
                            BillingAction.CHECK_ACTIVE_SUB -> checkForActiveSub()
                        }
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                // show unable to connect to play services please try again later
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }


    // Checks for active sub and updates the shared pref state on each app launch
    suspend fun checkForActiveSub() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)

        // uses queryPurchasesAsync Kotlin extension function
        val purchasesResult = billingClient.queryPurchasesAsync(params.build())
        purchasesResult.purchasesList.forEach {
            if (it.purchaseState == PurchaseState.PURCHASED) {
                profileViewModel.setSubState(true)
            }
        }
        if (purchasesResult.purchasesList.isEmpty()) {
            profileViewModel.setSubState(false)
        }

    }


    // gets a list of all available product for purchase
    // in our case should only be a yearly sub defined int he companion in the MainActivity
    suspend fun processProductList() {
        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder()
                .setProductList(
                    mutableListOf(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(P_ID_APP_SUB)
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build()
                    )
                )
                .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
            // check billingResult
            // process returned productDetailsList
            for (currentProduct in productDetailsList) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK
                    && currentProduct.productId == P_ID_APP_SUB
                ) {
                    launchBilling(currentProduct)
                }
            }
        }
    }

    //should be called from some user interaction listener
    fun launchBilling(productDetails: ProductDetails) {
        val currentOfferToken: String = productDetails.subscriptionOfferDetails?.find {
            it.basePlanId == "base-p-id-app-sub"
        }?.offerToken ?: return
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                .setProductDetails(productDetails)
                // to get an offer token, call ProductDetails.subscriptionOfferDetails()
                // for a list of offers that are available to the user
                .setOfferToken(currentOfferToken)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        // Launch the billing flow
        val billingResult = billingClient.launchBillingFlow(this, billingFlowParams)
        if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
            Toast.makeText(this, "Unable to launch billing ", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState === Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                val ackPurchaseResult = withContext(Dispatchers.IO) {
                    billingClient.acknowledgePurchase(
                        acknowledgePurchaseParams.build()
                    ) {
                        // do nothing, play already shows the check screen
                        // set shared pref here
                        profileViewModel.setSubState(true)
                    }
                }
            }
        }
    }

//    fun navigateToListBottom() {
//        val view: View = binding.bottomNavigation.findViewById(R.id.list_bottom)
//        view.performClick()
//    }

}





























