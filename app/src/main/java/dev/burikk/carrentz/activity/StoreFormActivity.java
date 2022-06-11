package dev.burikk.carrentz.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.i18n.phonenumbers.Phonenumber;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.burikk.carrentz.BuildConfig;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.adapter.SpinnerAdapter;
import dev.burikk.carrentz.api.merchant.MerchantApi;
import dev.burikk.carrentz.api.merchant.endpoint.store.item.StoreItem;
import dev.burikk.carrentz.bottomsheet.BottomSheets;
import dev.burikk.carrentz.bottomsheet.ConfirmationBottomSheet;
import dev.burikk.carrentz.bottomsheet.MessageBottomSheet;
import dev.burikk.carrentz.bottomsheet.SpinnerBottomSheet;
import dev.burikk.carrentz.common.City;
import dev.burikk.carrentz.common.DialCode;
import dev.burikk.carrentz.helper.Generals;
import dev.burikk.carrentz.helper.Images;
import dev.burikk.carrentz.helper.Retrofits;
import dev.burikk.carrentz.helper.Strings;
import dev.burikk.carrentz.helper.Validators;
import dev.burikk.carrentz.helper.Views;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;

/**
 * @author Muhammad Irfan
 * @since 18/01/2022 11.05
 */
@SuppressLint("NonConstantResourceId")
public class StoreFormActivity extends AppCompatActivity implements MainProtocol<Void>, SpinnerBottomSheet.SpinnerBottomSheetCallback {
    @BindView(R.id.coordinatorLayout)
    public CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.imageView)
    public ImageView imageView;
    @BindView(R.id.btnTakePhoto)
    public MaterialButton btnTakePhoto;
    @BindView(R.id.btnBrowseImage)
    public MaterialButton btnBrowseImage;
    @BindView(R.id.edtName)
    public TextInputEditText edtName;
    @BindView(R.id.actvDialCode)
    public AutoCompleteTextView actvDialCode;
    @BindView(R.id.edtPhoneNumber)
    public TextInputEditText edtPhoneNumber;
    @BindView(R.id.edtAddress)
    public TextInputEditText edtAddress;
    @BindView(R.id.actvCity)
    public AutoCompleteTextView actvCity;
    @BindView(R.id.btnDelete)
    public MaterialButton btnDelete;
    @BindView(R.id.btnEdit)
    public MaterialButton btnEdit;
    @BindView(R.id.btnSave)
    public MaterialButton btnSave;

    public boolean editable;
    public StoreItem storeItem;
    public Disposable disposable;
    public Uri uri;
    public ActivityResultLauncher<Uri> takePictureActivityResultLauncher = this.registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
        if (result) {
            CropImage.activity(this.uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
    });

    public ActivityResultLauncher<String[]> browseImageActivityResultLauncher = this.registerForActivityResult(new ActivityResultContracts.OpenDocument(), result -> CropImage.activity(result)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1, 1)
            .start(this));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_store_form);

        ButterKnife.bind(this);

        this.extract();
        this.toolbar();
        this.widget();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (this.disposable != null) {
            this.disposable.dispose();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);

            if (activityResult != null) {
                if (resultCode == RESULT_OK) {
                    Uri uri = activityResult.getUri();

                    try {
                        Bitmap bitmap = Images.bitmap(
                                StoreFormActivity.this,
                                uri
                        );

                        StoreFormActivity.this.imageView.setImageBitmap(bitmap);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }  else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    activityResult.getError().printStackTrace();

                    BottomSheets.message(this, "Gagal mendapatkan gambar, silahkan coba kembali.");
                }
            }
        }
    }

    @Override
    public ProgressBar getProgressBar() {
        return this.progressBar;
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return this;
    }

    @Override
    public void result(Object request, Void data) {
        BottomSheets.message(
                this,
                "Data berhasil tersimpan.",
                new MessageBottomSheet.MessageBottomSheetCallback() {
                    @Override
                    public void dismiss() {
                        StoreFormActivity.this.finish();
                    }

                    @Override
                    public void cancel() {
                        StoreFormActivity.this.finish();
                    }
                }
        );
    }

    @Override
    public void result(Void data) {
        BottomSheets.message(
                this,
                "Data berhasil terhapus.",
                new MessageBottomSheet.MessageBottomSheetCallback() {
                    @Override
                    public void dismiss() {
                        StoreFormActivity.this.finish();
                    }

                    @Override
                    public void cancel() {
                        StoreFormActivity.this.finish();
                    }
                }
        );
    }

    @Override
    public void select(SpinnerAdapter.Item selectedItem) {
        int tag = (int) selectedItem.getTag();

        if (tag == R.id.actvCity) {
            this.actvCity.setText(selectedItem.getDescription());
        } else if (tag == R.id.actvDialCode) {
            this.actvDialCode.setText(selectedItem.getSelectedDescription());
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    @OnClick(R.id.btnTakePhoto)
    public void takePhoto() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 666);

                return;
            }

            File file = new File(this.getFilesDir(), "photo.jpg");

            this.uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);

            this.takePictureActivityResultLauncher.launch(this.uri);
        } else {
            BottomSheets.message(
                    this,
                    "Kamera tidak ditemukan."
            );
        }
    }

    @OnClick(R.id.btnBrowseImage)
    public void browseImage() {
        this.browseImageActivityResultLauncher.launch(new String[]{"image/*"});
    }

    @OnClick(R.id.actvDialCode)
    public void dialCode() {
        ArrayList<SpinnerAdapter.Item> items = new ArrayList<>();

        for (DialCode dialCode : DialCode.DIAL_CODES) {
            SpinnerAdapter.Item item = new SpinnerAdapter.Item();

            item.setIdentifier(dialCode);
            item.setDescription(dialCode.getCountryCode() + " - " + dialCode.getCountryName() + " (" + dialCode.getDialCode() + ")");
            item.setSelectedDescription(dialCode.getDialCode());
            item.setTag(R.id.actvDialCode);

            items.add(item);
        }

        BottomSheets.spinner(this, "Kode Telepon", items, this);
    }

    @OnClick(R.id.actvCity)
    public void city() {
        ArrayList<SpinnerAdapter.Item> items = new ArrayList<>();

        for (String city : City.CITIES) {
            SpinnerAdapter.Item item = new SpinnerAdapter.Item();

            item.setIdentifier(city);
            item.setDescription(city);
            item.setTag(R.id.actvCity);

            items.add(item);
        }

        BottomSheets.spinner(this, "Kota", items, this);
    }

    @OnClick(R.id.btnDelete)
    public void delete() {
        BottomSheets.confirmation(
                this,
                "Apakah anda yakin ingin menghapus data ini?",
                "Tidak",
                "Hapus",
                new ConfirmationBottomSheet.ConfirmationBottomSheetCallback() {
                    @Override
                    public void negative() {}

                    @Override
                    public void positive() {
                        StoreFormActivity.this.disposable = MerchantApi.storeDelete(StoreFormActivity.this, StoreFormActivity.this.storeItem.getId());
                    }
                }
        );
    }

    @OnClick(R.id.btnEdit)
    public void edit() {
        this.editable = true;
        this.toolbar();
        this.widget();
    }

    @OnClick(R.id.btnSave)
    public void save() {
        if (this.valid()) {
            BottomSheets.confirmation(
                    this,
                    "Apakah anda yakin ingin menyimpan data ini?",
                    "Tidak",
                    "Simpan",
                    new ConfirmationBottomSheet.ConfirmationBottomSheetCallback() {
                        @Override
                        public void negative() {}

                        @Override
                        public void positive() {
                            try {
                                StoreItem storeItem = new StoreItem();

                                storeItem.setName(Strings.value(StoreFormActivity.this.edtName.getText()));
                                storeItem.setPhoneNumber(Strings.value(StoreFormActivity.this.actvDialCode.getText()) + Strings.value(StoreFormActivity.this.edtPhoneNumber.getText()));
                                storeItem.setAddress(Strings.value(StoreFormActivity.this.edtAddress.getText()));
                                storeItem.setCity(Strings.value(StoreFormActivity.this.actvCity.getText()));

                                MultipartBody.Part part = null;

                                StoreFormActivity.this.imageView.invalidate();

                                BitmapDrawable bitmapDrawable = (BitmapDrawable) StoreFormActivity.this.imageView.getDrawable();

                                if (bitmapDrawable != null) {
                                    Bitmap bitmap = bitmapDrawable.getBitmap();

                                    if (bitmap != null) {
                                        part = Retrofits.filePart("image", bitmap, Long.toHexString(System.nanoTime()) + ".jpg");
                                    }
                                }

                                if (StoreFormActivity.this.storeItem != null) {
                                    StoreFormActivity.this.disposable = MerchantApi.storePut(StoreFormActivity.this, StoreFormActivity.this.storeItem.getId(), part, storeItem);
                                } else {
                                    StoreFormActivity.this.disposable = MerchantApi.storePost(StoreFormActivity.this, part, storeItem);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();

                                BottomSheets.message(StoreFormActivity.this, "Gagal menyimpan data, silahkan coba kembali beberapa saat kemudian.");
                            }
                        }
                    }
            );
        }
    }

    private void extract() {
        Bundle bundle = this.getIntent().getBundleExtra("BUNDLE");

        if (bundle != null) {
            this.storeItem = (StoreItem) bundle.getSerializable("STORE_ITEM");
        }
    }

    private void toolbar() {
        this.setSupportActionBar(this.toolbar);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeButtonEnabled(true);

            if (this.storeItem != null) {
                if (this.editable) {
                    this.getSupportActionBar().setTitle("Ubah Data Cabang");
                } else {
                    this.getSupportActionBar().setTitle("Lihat Data Cabang");
                }
            } else {
                this.getSupportActionBar().setTitle("Tambah Data Cabang");
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void widget() {
        Views.gone(
                this.btnDelete,
                this.btnEdit,
                this.btnSave,
                this.btnTakePhoto,
                this.btnBrowseImage
        );

        Views.disable(
                this.edtName,
                this.actvDialCode,
                this.edtPhoneNumber,
                this.edtAddress,
                this.actvCity
        );

        ((TextInputLayout) this.actvCity.getParent().getParent()).setEndIconOnClickListener(null);
        ((TextInputLayout) this.actvDialCode.getParent().getParent()).setEndIconOnClickListener(null);

        if (this.storeItem != null) {
            Views.visible(this.btnDelete);

            this.edtName.setText(this.storeItem.getName());

            Phonenumber.PhoneNumber phoneNumber = Generals.getPhoneNumber(this.storeItem.getPhoneNumber());

            if (phoneNumber != null) {
                this.actvDialCode.setText(Generals.getDialCode(phoneNumber));
                this.edtPhoneNumber.setText(String.valueOf(phoneNumber.getNationalNumber()));
            }

            this.edtAddress.setText(this.storeItem.getAddress());
            this.actvCity.setText(this.storeItem.getCity());

            Glide
                    .with(this)
                    .asBitmap()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .load(this.storeItem.getImageUrl())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            StoreFormActivity.this.imageView.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        } else {
            this.editable = true;
            this.actvDialCode.setText("+62");
        }

        if (this.editable) {
            Views.visible(
                    this.btnSave,
                    this.btnTakePhoto,
                    this.btnBrowseImage
            );

            Views.enable(
                    this.edtName,
                    this.actvDialCode,
                    this.edtPhoneNumber,
                    this.edtAddress,
                    this.actvCity
            );

            ((TextInputLayout) this.actvCity.getParent().getParent()).setEndIconOnClickListener(view -> this.city());
            ((TextInputLayout) this.actvDialCode.getParent().getParent()).setEndIconOnClickListener(view -> this.dialCode());
        } else {
            this.editable = true;

            Views.visible(this.storeItem != null, this.btnEdit);
        }
    }

    private boolean valid() {
        List<Boolean> booleans = Arrays.asList(
                Validators.mandatory(this, this.edtName, "Nama usaha"),
                Validators.mandatory(this, this.actvDialCode, "Kode telepon"),
                Validators.mandatory(this, this.edtPhoneNumber, "Nomor ponsel"),
                Validators.mandatory(this, this.edtAddress, "Alamat usaha"),
                Validators.mandatory(this, this.actvCity, "Kota"),
                Validators.maxLength(this.edtName, 64),
                Validators.maxLength(this.edtAddress, 256)
        );

        return !booleans.contains(false);
    }
}