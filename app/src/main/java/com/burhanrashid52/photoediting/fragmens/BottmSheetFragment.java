package com.burhanrashid52.photoediting.fragmens;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.burhanrashid52.photoediting.Dialogs.BuySubscribeDialog;
import com.burhanrashid52.photoediting.Dialogs.DialogListener;
import com.burhanrashid52.photoediting.Dialogs.LoginDialog;
import com.burhanrashid52.photoediting.DownloadPackageHelper;
import com.burhanrashid52.photoediting.FileUtils;
import com.burhanrashid52.photoediting.InAppBill;
import com.burhanrashid52.photoediting.R;
import com.burhanrashid52.photoediting.database.AppDataBase;
import com.burhanrashid52.photoediting.database.SavedModel;
import com.burhanrashid52.photoediting.database.TaskDao;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class BottmSheetFragment extends BottomSheetDialogFragment implements DialogListener {
    String name, designer, downloadCount, occasion, samplesUrl, headerUrl, packageUrl, type;

    String PURCHASE_TOKEN="";
    int price, id;
    ImageView bookmark;
    TextView title, designerName, downloadCountTextview, priceTextview;
    public static Button downLoad;
    boolean saved;
    TaskDao taskDao;
    SharedPreferences sharedPreferences;

    String token;
    Bundle bundle;
    public static ProgressBar progressBar;
    InAppBill bazar;
    DownloadPackageHelper downloadPackageHelper;
    Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bazar=new InAppBill(context, Objects.requireNonNull(getActivity()));
        taskDao = AppDataBase.getAppDataBase(context).getTaskDao();
        sharedPreferences = Objects.requireNonNull(context).getSharedPreferences("MySharedPref",MODE_PRIVATE);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
     bundle=  getBundle();
     downloadPackageHelper = new DownloadPackageHelper(context, bundle);


    }


    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog_fragment, container, false);
        findView(view);
        saveItemInFaves();
        title.setText(name);
        designerName.setText(context.getString(R.string.designer)+designer);
        downloadCountTextview.setText( downloadCount + context.getString(R.string.download_count_text));
        PURCHASE_TOKEN=sharedPreferences.getString("PURCHASE_TOKEN","");
        token=sharedPreferences.getString("token","empty");

        String unzipPath = FileUtils.getDataDir(context, "Best_Design").getAbsolutePath();
        int endIndex = packageUrl.length();
        String substring = packageUrl.substring(29, endIndex - 4);
        File extracted = new File(unzipPath + "/" + substring);




            if (price == 0) {
                priceTextview.setText(context.getString(R.string.free));
            } else {
                priceTextview.setText(context.getString(R.string.premium));
                if (PURCHASE_TOKEN.equals("")){
                    downLoad.setText(context.getString(R.string.buySubscribe));

                }else{
                    downLoad.setText(context.getString(R.string.download_package));

                }
            /*   downLoad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (token.equals("empty")){
                            LoginDialog loginDialog=new LoginDialog(getActivity());
                            loginDialog.show();

                        }else {
                            if (extracted.exists()) {
                                Toast.makeText(context, "این فایل قبلا دانلود شده است.", Toast.LENGTH_SHORT).show();


                            } else {
                                if(PURCHASE_TOKEN.equals("")){
                                    BuySubscribeDialog buySubscribeDialog=new BuySubscribeDialog(context,getActivity());
                                    buySubscribeDialog.show();


                                }else{
                                    DownloadPackageHelper downloadPackageHelper = new DownloadPackageHelper(context, bundle);
                                    downloadPackageHelper.downloading();
                                }
                                //bazar.connectToBazar(bundle, price, name, String.valueOf(id));
                            }
                        }
                    }
                });*/

            }
                downLoad.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {

                        if (token.equals("empty")){
                            LoginDialog loginDialog=new LoginDialog(getActivity());
                            loginDialog.show();
                            onDestroyView();
                        }else{
                            if (extracted.exists()) {
                                Toast.makeText(context, context.getString(R.string.already_downloaded), Toast.LENGTH_SHORT).show();


                            } else {

                                if(price==0) {
                                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                        requestStoragePermission();

                                    } else {

                                        downloadPackageHelper.downloading();
                                    }
                                }else{
                                   if(PURCHASE_TOKEN.equals("")){
                                        BuySubscribeDialog buySubscribeDialog=new BuySubscribeDialog(context,getActivity());
                                        buySubscribeDialog.setDialogListener(BottmSheetFragment.this); // 'this' refers to the Fragment which implements the listener
                                       onDestroyView();
                                        buySubscribeDialog.show();


                                    }else{
                                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                            requestStoragePermission();

                                        } else {
                                            downloadPackageHelper.downloading();
                                        }
                                    }
                                }
                            }
                        }



                    }
                });




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

                DownloadPackageHelper downloadPackageHelper = new DownloadPackageHelper(context, bundle);
                downloadPackageHelper.downloading();
            } else {
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.Request_permission))
                        .setMessage(context.getString(R.string.permission_denied_text))
                        .setPositiveButton(context.getString(R.string.agree), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                openAppSettings();
                            }
                        })
                        .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
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

        Uri packageUri = Uri.fromParts("package", context.getPackageName(), null);

        Intent applicationDetailsSettingsIntent = new Intent();

        applicationDetailsSettingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        applicationDetailsSettingsIntent.setData(packageUri);
        applicationDetailsSettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(applicationDetailsSettingsIntent);

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


    @Override
    public void onDialogResult() {
        PURCHASE_TOKEN=sharedPreferences.getString("PURCHASE_TOKEN","");

            downLoad.setText(context.getString(R.string.download_package));


    }
}
