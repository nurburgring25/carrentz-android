package dev.burikk.carrentz.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;
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
import dev.burikk.carrentz.api.item.LovItem;
import dev.burikk.carrentz.api.merchant.MerchantApi;
import dev.burikk.carrentz.api.merchant.endpoint.vehicle.item.VehicleImageItem;
import dev.burikk.carrentz.api.merchant.endpoint.vehicle.item.VehicleItem;
import dev.burikk.carrentz.api.merchant.endpoint.vehicle.response.VehicleResourceResponse;
import dev.burikk.carrentz.bottomsheet.BottomSheets;
import dev.burikk.carrentz.bottomsheet.ConfirmationBottomSheet;
import dev.burikk.carrentz.bottomsheet.MessageBottomSheet;
import dev.burikk.carrentz.bottomsheet.SpinnerBottomSheet;
import dev.burikk.carrentz.helper.Formats;
import dev.burikk.carrentz.helper.Images;
import dev.burikk.carrentz.helper.Retrofits;
import dev.burikk.carrentz.helper.Strings;
import dev.burikk.carrentz.helper.Validators;
import dev.burikk.carrentz.helper.Views;
import dev.burikk.carrentz.protocol.MainProtocol;
import dev.burikk.carrentz.view.CustomCurrencyEditText;
import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;

/**
 * @author Muhammad Irfan
 * @since 18/01/2022 11.05
 */
@SuppressLint("NonConstantResourceId")
public class VehicleFormActivity extends AppCompatActivity implements MainProtocol<Object>, SpinnerBottomSheet.SpinnerBottomSheetCallback, ViewListener {
    @BindView(R.id.coordinatorLayout)
    public CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.carouselView)
    public CarouselView carouselView;
    @BindView(R.id.btnTakePhoto)
    public MaterialButton btnTakePhoto;
    @BindView(R.id.btnBrowseImage)
    public MaterialButton btnBrowseImage;
    @BindView(R.id.actvStore)
    public AutoCompleteTextView actvStore;
    @BindView(R.id.actvVehicleType)
    public AutoCompleteTextView actvVehicleType;
    @BindView(R.id.edtLicenseNumber)
    public TextInputEditText edtLicenseNumber;
    @BindView(R.id.edtName)
    public TextInputEditText edtName;
    @BindView(R.id.edtDescription)
    public TextInputEditText edtDescription;
    @BindView(R.id.edtCostPerDay)
    public CustomCurrencyEditText edtCostPerDay;
    @BindView(R.id.edtLateReturnFinePerDay)
    public CustomCurrencyEditText edtLateReturnFinePerDay;
    @BindView(R.id.btnDelete)
    public MaterialButton btnDelete;
    @BindView(R.id.btnEdit)
    public MaterialButton btnEdit;
    @BindView(R.id.btnSave)
    public MaterialButton btnSave;

    public boolean editable;
    public VehicleItem vehicleItem;
    public VehicleResourceResponse vehicleResourceResponse;
    public Disposable disposable;
    public Uri uri;
    public List<Image> images;
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

    {
        this.images = new ArrayList<>();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_vehicle_form);

        ButterKnife.bind(this);

        this.extract();
        this.toolbar();
        this.widget();
        this.resource();
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
                                this,
                                uri
                        );

                        Image image = new Image();

                        image.setBitmap(bitmap);

                        this.images.add(image);
                        this.carouselView.setPageCount(this.images.size());
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
    public void result(Object request, Object data) {
        BottomSheets.message(
                this,
                "Data berhasil tersimpan.",
                new MessageBottomSheet.MessageBottomSheetCallback() {
                    @Override
                    public void dismiss() {
                        VehicleFormActivity.this.finish();
                    }

                    @Override
                    public void cancel() {
                        VehicleFormActivity.this.finish();
                    }
                }
        );
    }

    @Override
    public void result(Object data) {
        if (data instanceof VehicleResourceResponse) {
            this.vehicleResourceResponse = (VehicleResourceResponse) data;
        } else {
            BottomSheets.message(
                    this,
                    "Data berhasil terhapus.",
                    new MessageBottomSheet.MessageBottomSheetCallback() {
                        @Override
                        public void dismiss() {
                            VehicleFormActivity.this.finish();
                        }

                        @Override
                        public void cancel() {
                            VehicleFormActivity.this.finish();
                        }
                    }
            );
        }
    }

    @Override
    public void select(SpinnerAdapter.Item selectedItem) {
        int tag = (int) selectedItem.getTag();

        if (tag == R.id.actvStore) {
            this.actvStore.setText(selectedItem.getDescription());
            this.actvStore.setTag(selectedItem.getIdentifier());
        } else if (tag == R.id.actvVehicleType) {
            this.actvVehicleType.setText(selectedItem.getDescription());
            this.actvVehicleType.setTag(selectedItem.getIdentifier());
        }
    }

    @Override
    public View setViewForPosition(int position) {
        Image image = this.images.get(position);

        @SuppressLint("InflateParams")
        View customView = getLayoutInflater().inflate(R.layout.adapter_vehicle_form_carousel, null);

        ImageView imageView = customView.findViewById(R.id.imageView);

        imageView.setImageBitmap(image.getBitmap());

        MaterialButton btnDelete = customView.findViewById(R.id.btnDelete);

        Views.gone(btnDelete);

        if (this.editable) {
            Views.visible(btnDelete);

            btnDelete.setOnClickListener(view -> {
                this.images.remove(position);
                this.carouselView.setPageCount(this.images.size());
            });
        }

        MaterialButton btnThumbnail = customView.findViewById(R.id.btnThumbnail);

        Views.disable(btnThumbnail);

        if (image.isThumbnail()) {
            btnThumbnail.setIconTintResource(R.color.md_theme_primary);
        } else {
            btnThumbnail.setIconTintResource(R.color.md_theme_outline);
        }

        if (this.editable) {
            Views.enable(btnThumbnail);

            btnThumbnail.setOnClickListener(view -> {
                image.setThumbnail(!image.isThumbnail());

                if (image.isThumbnail()) {
                    btnThumbnail.setIconTintResource(R.color.md_theme_primary);
                } else {
                    btnThumbnail.setIconTintResource(R.color.md_theme_outline);
                }
            });
        }

        return customView;
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

    @OnClick(R.id.actvStore)
    public void store() {
        ArrayList<SpinnerAdapter.Item> items = new ArrayList<>();

        for (LovItem lovItem : this.vehicleResourceResponse.getStores()) {
            SpinnerAdapter.Item item = new SpinnerAdapter.Item();

            item.setIdentifier(lovItem.getIdentifier());
            item.setDescription(lovItem.getDescription());

            item.setTag(R.id.actvStore);

            items.add(item);
        }

        BottomSheets.spinner(this, "Cabang", items, this);
    }

    @OnClick(R.id.actvVehicleType)
    public void vehicleType() {
        ArrayList<SpinnerAdapter.Item> items = new ArrayList<>();

        for (LovItem lovItem : this.vehicleResourceResponse.getVehicleTypes()) {
            SpinnerAdapter.Item item = new SpinnerAdapter.Item();

            item.setIdentifier(lovItem.getIdentifier());
            item.setDescription(lovItem.getDescription());
            item.setTag(R.id.actvVehicleType);

            items.add(item);
        }

        BottomSheets.spinner(this, "Jenis Kendaraan", items, this);
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
                        VehicleFormActivity.this.disposable = MerchantApi.vehicleDelete(VehicleFormActivity.this, VehicleFormActivity.this.vehicleItem.getId());
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
                                VehicleItem vehicleItem = new VehicleItem();

                                vehicleItem.setStoreId(((Number) VehicleFormActivity.this.actvStore.getTag()).longValue());
                                vehicleItem.setVehicleTypeId(((Number) VehicleFormActivity.this.actvVehicleType.getTag()).longValue());
                                vehicleItem.setLicenseNumber(Strings.value(VehicleFormActivity.this.edtLicenseNumber.getText()));
                                vehicleItem.setName(Strings.value(VehicleFormActivity.this.edtName.getText()));
                                vehicleItem.setDescription(Strings.value(VehicleFormActivity.this.edtDescription.getText()));
                                vehicleItem.setCostPerDay((long) VehicleFormActivity.this.edtCostPerDay.getCurrencyDouble());
                                vehicleItem.setLateReturnFinePerDay((long) VehicleFormActivity.this.edtLateReturnFinePerDay.getCurrencyDouble());

                                List<MultipartBody.Part> parts = new ArrayList<>();

                                for (Image image : VehicleFormActivity.this.images) {
                                    String fileName;

                                    if (image.getId() != null) {
                                        fileName = image.getId() + ".jpg";
                                    } else {
                                        fileName = Long.toHexString(System.nanoTime()) + ".jpg";
                                    }

                                    if (image.isThumbnail()) {
                                        fileName = "**" + fileName;
                                    }

                                    parts.add(Retrofits.filePart("images", image.getBitmap(), fileName));
                                }

                                if (VehicleFormActivity.this.vehicleItem != null) {
                                    VehicleFormActivity.this.disposable = MerchantApi.vehiclePut(VehicleFormActivity.this, VehicleFormActivity.this.vehicleItem.getId(), parts, vehicleItem);
                                } else {
                                    VehicleFormActivity.this.disposable = MerchantApi.vehiclePost(VehicleFormActivity.this, parts, vehicleItem);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();

                                BottomSheets.message(VehicleFormActivity.this, "Gagal menyimpan data, silahkan coba kembali beberapa saat kemudian.");
                            }
                        }
                    }
            );
        }
    }

    private void extract() {
        Bundle bundle = this.getIntent().getBundleExtra("BUNDLE");

        if (bundle != null) {
            this.vehicleItem = (VehicleItem) bundle.getSerializable("VEHICLE_ITEM");
        }
    }

    private void toolbar() {
        this.setSupportActionBar(this.toolbar);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeButtonEnabled(true);

            if (this.vehicleItem != null) {
                if (this.editable) {
                    this.getSupportActionBar().setTitle("Ubah Data Kendaraan");
                } else {
                    this.getSupportActionBar().setTitle("Lihat Data Kendaraan");
                }
            } else {
                this.getSupportActionBar().setTitle("Tambah Data Kendaraan");
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void widget() {
        this.images = new ArrayList<>();

        Views.gone(
                this.btnDelete,
                this.btnEdit,
                this.btnSave,
                this.btnTakePhoto,
                this.btnBrowseImage
        );

        Views.disable(
                this.actvStore,
                this.actvVehicleType,
                this.edtLicenseNumber,
                this.edtName,
                this.edtDescription,
                this.edtCostPerDay,
                this.edtLateReturnFinePerDay
        );

        ((TextInputLayout) this.actvStore.getParent().getParent()).setEndIconOnClickListener(null);
        ((TextInputLayout) this.actvVehicleType.getParent().getParent()).setEndIconOnClickListener(null);

        this.carouselView.setViewListener(this);

        if (this.vehicleItem != null) {
            Views.visible(this.btnDelete);

            this.actvStore.setText(this.vehicleItem.getStoreName());
            this.actvStore.setTag(this.vehicleItem.getStoreId());
            this.actvVehicleType.setText(this.vehicleItem.getVehicleTypeName());
            this.actvVehicleType.setTag(this.vehicleItem.getVehicleTypeId());
            this.edtLicenseNumber.setText(this.vehicleItem.getLicenseNumber());
            this.edtName.setText(this.vehicleItem.getName());
            this.edtDescription.setText(this.vehicleItem.getDescription());
            this.edtCostPerDay.setText(Formats.getCurrencyFormat(this.vehicleItem.getCostPerDay(), false));
            this.edtLateReturnFinePerDay.setText(Formats.getCurrencyFormat(this.vehicleItem.getLateReturnFinePerDay(), false));

            for (VehicleImageItem vehicleImageItem : this.vehicleItem.getImages()) {
                Glide
                        .with(this)
                        .asBitmap()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .load(vehicleImageItem.getUrl())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Image image = new Image();

                                image.setId(vehicleImageItem.getId());
                                image.setBitmap(resource);
                                image.setThumbnail(vehicleImageItem.isThumbnail());

                                VehicleFormActivity.this.images.add(image);
                                VehicleFormActivity.this.carouselView.setPageCount(VehicleFormActivity.this.images.size());
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {}
                        });
            }
        } else {
            this.editable = true;
        }

        if (this.editable) {
            Views.visible(
                    this.btnSave,
                    this.btnTakePhoto,
                    this.btnBrowseImage
            );

            Views.enable(
                    this.actvStore,
                    this.actvVehicleType,
                    this.edtLicenseNumber,
                    this.edtName,
                    this.edtDescription,
                    this.edtCostPerDay,
                    this.edtLateReturnFinePerDay
            );

            ((TextInputLayout) this.actvStore.getParent().getParent()).setEndIconOnClickListener(view -> this.store());
            ((TextInputLayout) this.actvVehicleType.getParent().getParent()).setEndIconOnClickListener(view -> this.vehicleType());
        } else {
            Views.visible(this.vehicleItem != null, this.btnEdit);
        }
    }

    private void resource() {
        this.disposable = MerchantApi.vehicleResource(this);
    }

    private boolean valid() {
        List<Boolean> booleans = Arrays.asList(
                Validators.mandatory(this, this.actvStore, "Cabang"),
                Validators.mandatory(this, this.actvVehicleType, "Jenis kendaraan"),
                Validators.mandatory(this, this.edtLicenseNumber, "Nomor polisi"),
                Validators.mandatory(this, this.edtName, "Nama"),
                Validators.mandatory(this, this.edtDescription, "Deskripsi"),
                Validators.mandatory(this, this.edtCostPerDay, "Biaya per hari"),
                Validators.mandatory(this, this.edtLateReturnFinePerDay, "Denda telat pengembalian per hari"),
                Validators.maxLength(this.edtName, 64),
                Validators.maxLength(this.edtDescription, 256)
        );

        int thumbnailCount = 0;

        for (Image image : this.images) {
            if (image.isThumbnail()) {
                thumbnailCount++;
            }
        }

        if (thumbnailCount == 0) {
            BottomSheets.message(
                    this,
                    "Harus terdapat gambar yang dijadikan gambar utama."
            );

            return false;
        }

        if (thumbnailCount > 1) {
            BottomSheets.message(
                    this,
                    "Gambar utama tidak bisa lebih dari satu."
            );

            return false;
        }

        return !booleans.contains(false);
    }

    public static class Image {
        private Long id;
        private Bitmap bitmap;
        private boolean thumbnail;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public boolean isThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(boolean thumbnail) {
            this.thumbnail = thumbnail;
        }
    }
}