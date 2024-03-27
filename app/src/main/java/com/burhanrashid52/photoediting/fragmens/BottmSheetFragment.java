package com.burhanrashid52.photoediting.fragmens;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.appsearch.SetSchemaRequest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Registry;
import com.burhanrashid52.photoediting.Dialogs.LoginDialog;
import com.burhanrashid52.photoediting.DownloadPackageHelper;
import com.burhanrashid52.photoediting.FileUtils;
import com.burhanrashid52.photoediting.InAppBill;
import com.burhanrashid52.photoediting.PayActivity;
import com.burhanrashid52.photoediting.Preferences;
import com.burhanrashid52.photoediting.R;
import com.burhanrashid52.photoediting.activitys.PreviewActivity;
import com.burhanrashid52.photoediting.database.AppDataBase;
import com.burhanrashid52.photoediting.database.SavedModel;
import com.burhanrashid52.photoediting.database.TaskDao;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.util.Objects;

import ir.cafebazaar.poolakey.Connection;
import ir.cafebazaar.poolakey.ConnectionState;
import ir.cafebazaar.poolakey.Payment;
import ir.cafebazaar.poolakey.callback.ConnectionCallback;
import ir.cafebazaar.poolakey.callback.PurchaseCallback;
import ir.cafebazaar.poolakey.config.PaymentConfiguration;
import ir.cafebazaar.poolakey.config.SecurityCheck;
import ir.cafebazaar.poolakey.entity.PurchaseInfo;
import ir.cafebazaar.poolakey.request.PurchaseRequest;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;

@RequiresApi(api = Build.VERSION_CODES.O)
public class BottmSheetFragment extends BottomSheetDialogFragment {
    String name, designer, downloadCount, occasion, samplesUrl, headerUrl, packageUrl, type;
    int price, id;
    ImageView bookmark;
    TextView title, designerName, downloadCountTextview, priceTextview;
    public static Button downLoad;
    boolean saved;
    TaskDao taskDao;
    Bundle bundle;
    public static ProgressBar progressBar;
    InAppBill bazar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bazar=new InAppBill(getContext(),getActivity());
        taskDao = AppDataBase.getAppDataBase(getContext()).getTaskDao();

        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
     bundle=  getBundle();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog_fragment, container, false);
        findView(view);
        saveItemInFaves();
        title.setText(name);
        designerName.setText("طراح: "+designer);
        downloadCountTextview.setText("تا کنون " + downloadCount + " نفر این پکیج را دانلود کرده اند.");

        String unzipPath = FileUtils.getDataDir(getContext(), "Best_Design").getAbsolutePath();
        int endIndex = packageUrl.length();
        String substring = packageUrl.substring(29, endIndex - 4);
        File extracted = new File(unzipPath + "/" + substring);


        downLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialog loginDialog=new LoginDialog(getActivity());
                loginDialog.show();
            }
        });

      /*      if (price == 0) {
                priceTextview.setText("رایگان");
                downLoad.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        if (extracted.exists()) {
                            Toast.makeText(getContext(), "این فایل قبلا دانلود شده است.", Toast.LENGTH_SHORT).show();


                        } else {
                            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                requestStoragePermission();

                            } else {
                                DownloadPackageHelper downloadPackageHelper = new DownloadPackageHelper(getContext(), bundle);
                                downloadPackageHelper.downloading();
                            }
                        }
                    }
                });

            } else {
                priceTextview.setText(price + "تومان");
                downLoad.setText("خرید پکیج");
                downLoad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (extracted.exists()) {
                            Toast.makeText(getContext(), "این فایل قبلا دانلود شده است.", Toast.LENGTH_SHORT).show();


                        } else {
                            bazar.connectToBazar(bundle,price,name,String.valueOf(id));
                        }

                    }
                });

            }*/


        return view;
    }


    public void findView(View view) {
        title = view.findViewById(R.id.txtPackName);
        designerName = view.findViewById(R.id.txtDesigner);
        downLoad = view.findViewById(R.id.btnDownLoad);
        bookmark = view.findViewById(R.id.imgBookmark);
        downloadCountTextview = view.findViewById(R.id.txtCountDown);
        priceTextview = view.findViewById(R.id.txtPrice);
        progressBar = view.findViewById(R.id.prgDownLoad);

    }

    private void saveItemInFaves() {
        saved = taskDao.getCurrentSavedPackage(id);
        if (saved) {
            bookmark.setImageResource(R.drawable.ic_baseline_bookmark_added_24);
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    taskDao.deleteSavedItem(id);
                    bookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                    saveItemInFaves();

                }
            });

        } else {
            bookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SavedModel savedModel = new SavedModel();
                    savedModel.setId(id);
                    savedModel.setDesigner(designer);
                    savedModel.setName(name);
                    savedModel.setOcassion(occasion);
                    savedModel.setSamples(samplesUrl);
                    savedModel.setDowmloadCount(downloadCount);
                    savedModel.setHeaderURL(headerUrl);
                    savedModel.setType(type);
                    savedModel.setPackageURL(packageUrl);
                    savedModel.setPrice(price);
                    long saveResult = taskDao.savePackage(savedModel);
                    if (saveResult != -1) {
                        saveItemInFaves();
                        bookmark.setImageResource(R.drawable.ic_baseline_bookmark_added_24);

                    }
                }
            });
        }
    }


    private void requestStoragePermission() {

        reqPermission();


    }

    private void reqPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                DownloadPackageHelper downloadPackageHelper = new DownloadPackageHelper(getContext(), bundle);
                downloadPackageHelper.downloading();
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle("درخواست مجوز")
                        .setMessage("شما مجوز دسترسی به حافظه را رد کرده اید .آیا تمایل دارید به صورت دستی مجوز را فعال کنید؟ ")
                        .setPositiveButton("موافقم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                openAppSettings();
                            }
                        })
                        .setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();

                            }
                        })
                        .create()
                        .show();
            }

        }

    }

    public void openAppSettings() {

        Uri packageUri = Uri.fromParts("package", getContext().getPackageName(), null);

        Intent applicationDetailsSettingsIntent = new Intent();

        applicationDetailsSettingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        applicationDetailsSettingsIntent.setData(packageUri);
        applicationDetailsSettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        getContext().startActivity(applicationDetailsSettingsIntent);

    }


    @Override
    public void onDestroy() {
        if(bazar.connection){
         bazar.paymentConnection.disconnect();

        }
        super.onDestroy();
    }

    public  Bundle getBundle(){
        bundle = getArguments();
        name = bundle.getString("name");
        designer = bundle.getString("designer");
        downloadCount = bundle.getString("downloadCount");
        price = bundle.getInt("price");
        occasion = bundle.getString("occasion");
        samplesUrl = bundle.getString("samplesUrl");
        headerUrl = bundle.getString("headerUrl");
        packageUrl = bundle.getString("packageUrl");
        type = bundle.getString("type");
        id = bundle.getInt("id");

return bundle;

    }


}
