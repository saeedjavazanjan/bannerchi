package com.burhanrashid52.photoediting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import ir.cafebazaar.poolakey.Connection
import ir.cafebazaar.poolakey.ConnectionState
import ir.cafebazaar.poolakey.Payment
import ir.cafebazaar.poolakey.callback.PurchaseQueryCallback
import ir.cafebazaar.poolakey.config.PaymentConfiguration
import ir.cafebazaar.poolakey.config.SecurityCheck
import ir.cafebazaar.poolakey.exception.DynamicPriceNotSupportedException
import ir.cafebazaar.poolakey.request.PurchaseRequest

class PayActivity : AppCompatActivity() {
    var purchaseButton:AppCompatButton?=null
    var skuValueInput:AppCompatEditText?=null
    var dynamicPriceToken:AppCompatEditText?=null
    var subscribeButton:AppCompatButton?=null
    var queryPurchasedItemsButton:AppCompatButton?=null
    var querySubscribedItemsButton:AppCompatButton?=null
    var checkTrialSubscriptionButton:AppCompatButton?=null
    var consumeSwitch:SwitchCompat?=null
    var getSkuDetailInAppButton:AppCompatButton?=null
    var getSkuDetailSubscriptionButton:AppCompatButton?=null
    var serviceConnectionStatus:AppCompatTextView?=null

    private val paymentConfiguration = PaymentConfiguration(
        localSecurityCheck = SecurityCheck.Enable(rsaPublicKey ="MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDJYQVlmFb27fDWdlBa7MFycAsLuGh0ddMmx5iWYQs9gfOfvSyLuquJpnlKp/uSykHuqU2MNlBU8eBBV/S1e2mrEBoAlUySTXpBPfQiGfvcH0cpJaPrPdBNdteelUSZa8RCc7DjMdmBhDfcvMzQHDxn/VvHuW/sirh9/LkEQ58Tr0D02Hx5/6l9hTguWzntLKkUaapaAn1IKluSJi/lSZa4vVLqTOxFDIH5zPMpwu0CAwEAAQ==")
    )

    private val payment by lazy(LazyThreadSafetyMode.NONE) {
        Payment(context = this, config = paymentConfiguration)
    }

    private lateinit var paymentConnection: Connection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)
        findView()
        startPaymentConnection()
        setViewClickListener()
    }

    private fun setViewClickListener() {
        purchase()
        subscribeButton?.setOnClickListener {
            if (paymentConnection.getState() == ConnectionState.Connected) {
                subscribeProduct(
                    productId = "package",
                    payload = "subscribePayload",
                    dynamicPriceToken = dynamicPriceToken?.text.toString()
                )
            }
        }
        queryPurchasedItemsButton?.setOnClickListener {
            if (paymentConnection.getState() == ConnectionState.Connected) {
                payment.getPurchasedProducts(handlePurchaseQueryCallback())
            }
        }
        querySubscribedItemsButton?.setOnClickListener {
            if (paymentConnection.getState() == ConnectionState.Connected) {
                payment.getSubscribedProducts(handlePurchaseQueryCallback())
            }
        }
        checkTrialSubscriptionButton?.setOnClickListener {
            onCheckTrialSubscriptionClicked()
        }
        setGetSkuDetailClickListener()
    }

    fun purchase(){
        purchaseButton?.setOnClickListener {
            if (paymentConnection.getState() == ConnectionState.Connected) {
                purchaseProduct(
                    productId ="package",
                    payload = "purchasePayload",
                    dynamicPriceToken = dynamicPriceToken?.text.toString()
                )
            }
        }
    }
    private fun subscribeProduct(
        productId: String,
        payload: String,
        dynamicPriceToken: String?
    ) {
        val purchaseRequest = PurchaseRequest(
            productId = "package",
            payload = payload,
            dynamicPriceToken = dynamicPriceToken
        )
        payment.subscribeProduct(
            registry = activityResultRegistry,
            request = purchaseRequest
        ) {
            purchaseFlowBegan {
                toast(R.string.general_purchase_flow_began_message)
            }
            failedToBeginFlow {
                // bazaar need to update, in this case we only launch purchase without discount
                if (it is DynamicPriceNotSupportedException) {
                    toast(R.string.general_purchase_failed_dynamic_price_token_message)
                    subscribeProduct(productId, payload, null)
                } else {
                    toast(R.string.general_purchase_failed_message)
                }
            }
            purchaseSucceed {
                toast(R.string.general_purchase_succeed_message)
                if (consumeSwitch!!.isChecked) {
                    consumePurchasedItem(it.purchaseToken)
                }
            }
            purchaseCanceled {
                toast(R.string.general_purchase_cancelled_message)
            }
            purchaseFailed {
                toast(R.string.general_purchase_failed_message)
            }
        }
    }

    private fun purchaseProduct(
        productId: String,
        payload: String,
        dynamicPriceToken: String?
    ) {
        payment.purchaseProduct(
            registry = activityResultRegistry,
            request = PurchaseRequest(
                productId = productId,
                payload = payload,
                dynamicPriceToken = dynamicPriceToken
            )
        ) {
            purchaseFlowBegan {
                toast(R.string.general_purchase_flow_began_message)
            }
            failedToBeginFlow {
                // bazaar need to update, in this case we only launch purchase without discount
                if (it is DynamicPriceNotSupportedException) {
                    toast(R.string.general_purchase_failed_dynamic_price_token_message)
                    purchaseProduct(productId, payload, null)
                } else {
                    toast(R.string.general_purchase_failed_message)
                }
            }
            purchaseSucceed {
                toast(R.string.general_purchase_succeed_message)
                if (consumeSwitch!!.isChecked) {
                    consumePurchasedItem(it.purchaseToken)
                }
            }
            purchaseCanceled {
                toast(R.string.general_purchase_cancelled_message)
            }
            purchaseFailed {
                toast(R.string.general_purchase_failed_message)
            }
        }
    }

    private fun setGetSkuDetailClickListener() {

        getSkuDetailInAppButton?.setOnClickListener {
            onGetSkuDetailInAppClicked()
        }

        getSkuDetailSubscriptionButton?.setOnClickListener {
            onGetSkuDetailSubscriptionClicked()
        }
    }

    private fun onGetSkuDetailSubscriptionClicked() {
        if (paymentConnection.getState() == ConnectionState.Connected) {
            payment.getSubscriptionSkuDetails(
                skuIds = listOf(skuValueInput?.text.toString())
            ) {
                getSkuDetailsSucceed {
                    toast(it.toString())
                }
                getSkuDetailsFailed {
                    toast(R.string.general_query_get_sku_detail_failed_message)
                }
            }
        }
    }

    private fun onCheckTrialSubscriptionClicked() {
        if (paymentConnection.getState() == ConnectionState.Connected) {
            payment.checkTrialSubscription {
                checkTrialSubscriptionSucceed {
                    toast(it.toString())
                }
                checkTrialSubscriptionFailed {
                    it.message?.let { message -> toast(message) }
                }
            }
        }
    }

    private fun onGetSkuDetailInAppClicked() {
        if (paymentConnection.getState() == ConnectionState.Connected) {
            payment.getInAppSkuDetails(
                skuIds = listOf(skuValueInput?.text.toString())
            ) {
                getSkuDetailsSucceed {
                    toast(it.toString())
                }
                getSkuDetailsFailed {
                    toast(R.string.general_query_get_sku_detail_failed_message)
                }
            }
        }
    }

    private fun startPaymentConnection() {
        paymentConnection = payment.connect {
            connectionSucceed {
                serviceConnectionStatus?.setText(R.string.general_service_connection_connected_text)
            }
            connectionFailed {
                serviceConnectionStatus?.setText(R.string.general_service_connection_failed_text)
            }
            disconnected {
                serviceConnectionStatus?.setText(R.string.general_service_connection_not_connected_text)
            }
        }
    }

    private fun handlePurchaseQueryCallback(): PurchaseQueryCallback.() -> Unit = {
        querySucceed { purchasedItems ->
            val productId = "package"
            purchasedItems.find { it.productId == productId }
                ?.also { toast(R.string.general_user_purchased_item_message) }
                ?: run { toast(R.string.general_user_did_not_purchased_item_message) }
        }
        queryFailed {
            toast(R.string.general_query_purchased_items_failed_message)
        }
    }

    private fun consumePurchasedItem(purchaseToken: String) {
        payment.consumeProduct(purchaseToken) {
            consumeSucceed {
                toast(R.string.general_consume_succeed_message)
            }
            consumeFailed {
                toast(R.string.general_consume_failed_message)
            }
        }
    }

    private fun toast(@StringRes message: Int) {
        toast(getString(message))
    }

    private fun toast(message: String) {
        Toast.makeText(this@PayActivity, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        paymentConnection.disconnect()
        super.onDestroy()
    }
    fun findView(){
         purchaseButton=findViewById(R.id.purchaseButton)
         skuValueInput=findViewById(R.id.skuValueInput)
         dynamicPriceToken=findViewById(R.id.dynamicPriceToken)
         subscribeButton=findViewById(R.id.subscribeButton)
        queryPurchasedItemsButton=findViewById(R.id.queryPurchasedItemsButton)
        querySubscribedItemsButton=findViewById(R.id.querySubscribedItemsButton)
        checkTrialSubscriptionButton=findViewById(R.id.checkTrialSubscriptionButton)
        consumeSwitch=findViewById(R.id.consumeSwitch)
        getSkuDetailInAppButton=findViewById(R.id.getSkuDetailInAppButton)
        getSkuDetailSubscriptionButton=findViewById(R.id.getSkuDetailSubscriptionButton)
        serviceConnectionStatus=findViewById(R.id.serviceConnectionStatus)

    }
}
