package com.burhanrashid52.photoediting;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import ir.cafebazaar.poolakey.Connection;
import ir.cafebazaar.poolakey.Payment;
import ir.cafebazaar.poolakey.callback.ConnectionCallback;
import ir.cafebazaar.poolakey.callback.ConsumeCallback;
import ir.cafebazaar.poolakey.callback.PurchaseCallback;
import ir.cafebazaar.poolakey.callback.PurchaseQueryCallback;
import ir.cafebazaar.poolakey.config.PaymentConfiguration;
import ir.cafebazaar.poolakey.config.SecurityCheck;
import ir.cafebazaar.poolakey.entity.PurchaseInfo;
import ir.cafebazaar.poolakey.request.PurchaseRequest;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class InAppBill  {
 //   private static SharedPreferences sharedPref;
    Context context;
 public static FragmentActivity activity;
    //about bazar inAppBill
    SecurityCheck.Enable localSecurityCheck;
    PaymentConfiguration paymentConfiguration;
   public static Payment payment;
  public   Connection paymentConnection;
  public   Boolean connection=false;
public static String PURCHASE_TOKEN;


    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    public InAppBill(Context context, FragmentActivity activity) {
        this.context = context;
        this.activity = activity;
        preferences=activity.getSharedPreferences("MySharedPref",MODE_PRIVATE);
        localSecurityCheck=new SecurityCheck.Enable("MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDJYQVlmFb27fDWdlBa7MFycAsLuGh0ddMmx5iWYQs9gfOfvSyLuquJpnlKp/uSykHuqU2MNlBU8eBBV/S1e2mrEBoAlUySTXpBPfQiGfvcH0cpJaPrPdBNdteelUSZa8RCc7DjMdmBhDfcvMzQHDxn/VvHuW/sirh9/LkEQ58Tr0D02Hx5/6l9hTguWzntLKkUaapaAn1IKluSJi/lSZa4vVLqTOxFDIH5zPMpwu0CAwEAAQ==");
        paymentConfiguration=new PaymentConfiguration(localSecurityCheck,false);
        payment = new Payment(context,  paymentConfiguration);

    }



    public void connectToBazar(String packageName,
                               String UserId,
                               PurchaseDoneCallback callback
    ){
        paymentConnection = payment.connect(new Function1<ConnectionCallback, Unit>() {
            @Override
            public Unit invoke(ConnectionCallback connectionCallback) {
                connectionCallback.connectionSucceed(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        Toast.makeText(context, "ارتباط با بازار...", Toast.LENGTH_SHORT).show();
                        connection=true;
                        purchase(packageName,UserId,callback);
                        return null;
                    }
                });
                connectionCallback.connectionFailed(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        Toast.makeText(context, "خطای ارتباط با بازار...", Toast.LENGTH_SHORT).show();

                        return null;
                    }
                });
                return null;

            }

        });
    }
    public void purchase(String packageName,String userId,PurchaseDoneCallback callback){

        PurchaseRequest purchaseRequest=new PurchaseRequest(packageName,userId,"");
        payment.subscribeProduct(activity.getActivityResultRegistry(), purchaseRequest, new Function1<PurchaseCallback, Unit>() {
            @Override
            public Unit invoke(PurchaseCallback purchaseCallback) {
                purchaseCallback.failedToBeginFlow(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        Toast.makeText(context, "مشکلی رخ داده است.", Toast.LENGTH_SHORT).show();

                        return null;
                    }
                });
                purchaseCallback.purchaseCanceled(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        Toast.makeText(context, "خرید لغو شد.", Toast.LENGTH_SHORT).show();

                        return null;
                    }
                });
                purchaseCallback.purchaseFailed(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        Toast.makeText(context, "مشکلی رخ داده است.", Toast.LENGTH_SHORT).show();

                        return null;
                    }
                });
                purchaseCallback.purchaseSucceed(new Function1<PurchaseInfo, Unit>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public Unit invoke(PurchaseInfo purchaseInfo) {
                        Toast.makeText(context, "خرید با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                        callback.onPurchaseDone();
                        PURCHASE_TOKEN=purchaseInfo.getPurchaseToken();
                        editor = preferences.edit();
                        editor.putString("PURCHASE_TOKEN", PURCHASE_TOKEN);
                        editor.apply();

                      //  DownloadPackageHelper downloadPackageHelper = new DownloadPackageHelper(context, Current_bundle);
                      //  downloadPackageHelper.downloading();

                        return null;
                    }
                });
                purchaseCallback.purchaseFlowBegan(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        //  Toast.makeText(context, "", Toast.LENGTH_SHORT).show();

                        return null;
                    }
                });
                return null;
            }


        });



    /*    payment.purchaseProduct(activity.getActivityResultRegistry(), purchaseRequest, new Function1<PurchaseCallback, Unit>() {
            @Override
            public Unit invoke(PurchaseCallback purchaseCallback) {
                purchaseCallback.failedToBeginFlow(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        Toast.makeText(context, "مشکلی رخ داده است.", Toast.LENGTH_SHORT).show();

                        return null;
                    }
                });
                purchaseCallback.purchaseCanceled(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        Toast.makeText(context, "خرید لغو شد.", Toast.LENGTH_SHORT).show();

                        return null;
                    }
                });
                purchaseCallback.purchaseFailed(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        Toast.makeText(context, "مشکلی رخ داده است.", Toast.LENGTH_SHORT).show();

                        return null;
                    }
                });
                purchaseCallback.purchaseSucceed(new Function1<PurchaseInfo, Unit>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public Unit invoke(PurchaseInfo purchaseInfo) {
                        Toast.makeText(context, "خرید با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                         PURCHASE_TOKEN=purchaseInfo.getPurchaseToken();
                        editor = preferences.edit();
                        editor.putString("PURCHASE_TOKEN", PURCHASE_TOKEN);
                        editor.apply();

                        DownloadPackageHelper downloadPackageHelper = new DownloadPackageHelper(context, Current_bundle);
                        downloadPackageHelper.downloading();

                        return null;
                    }
                });
                purchaseCallback.purchaseFlowBegan(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                      //  Toast.makeText(context, "", Toast.LENGTH_SHORT).show();

                        return null;
                    }
                });
                return null;
            }
        });*/

    }

    public void connectForSubscribe(DataFilledCallback callback){


        paymentConnection = payment.connect(new Function1<ConnectionCallback, Unit>() {
            @Override
            public Unit invoke(ConnectionCallback connectionCallback) {
                connectionCallback.connectionSucceed(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {

                        getSubscribes(callback);
                        connection=true;
                        return null;
                    }
                });
                connectionCallback.connectionFailed(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        Toast.makeText(context, "خطای ارتباط با بازار...", Toast.LENGTH_SHORT).show();

                        return null;
                    }
                });
                return null;

            }

        });
    }


    public void getSubscribes(final DataFilledCallback dataFilledCallback){


        payment.getSubscribedProducts(new Function1<PurchaseQueryCallback, Unit>() {
            @Override
            public Unit invoke(PurchaseQueryCallback purchaseQueryCallback) {

                purchaseQueryCallback.queryFailed(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {

                        Toast.makeText(context,"اپلیکیشن بازار نصب نیست",Toast.LENGTH_SHORT).show();
                        return null;
                    }
                });
                purchaseQueryCallback.querySucceed(new Function1<List<PurchaseInfo>, Unit>() {
                    @Override
                    public Unit invoke(List<PurchaseInfo> purchaseInfos) {
                        dataFilledCallback.onDataFilled(purchaseInfos);

                        return null;
                    }
                });

                return null;
            }
        });

    }


   /* public static void consumeProduct(){
        preferences=activity.getSharedPreferences("MySharedPref",MODE_PRIVATE);
       String PurchaseToken=preferences.getString("PURCHASE_TOKEN",null);
        if (PurchaseToken!=null) {
            payment.consumeProduct(PurchaseToken, new Function1<ConsumeCallback, Unit>() {
                @Override
                public Unit invoke(ConsumeCallback consumeCallback) {
                    consumeCallback.consumeSucceed(new Function0<Unit>() {
                        @Override
                        public Unit invoke() {
                            return null;
                        }

                    });
                    consumeCallback.consumeFailed(new Function1<Throwable, Unit>() {
                        @Override
                        public Unit invoke(Throwable throwable) {
                            return null;
                        }
                    });
                    return null;
                }
            });
        }
    }*/

  /*  public static String getSavedPurchaseToken(){
        PURCHASE_TOKEN= sharedPref.getString("PURCHASE_TOKEN",null);
        return PURCHASE_TOKEN;
    }
    public static void setSavedPurchaseToken(String purchaseToken){
        SharedPreferences.Editor editor=sharedPref.edit();
        editor.putString("PURCHASE_TOKEN",purchaseToken);
        editor.apply();

    }*/

}
