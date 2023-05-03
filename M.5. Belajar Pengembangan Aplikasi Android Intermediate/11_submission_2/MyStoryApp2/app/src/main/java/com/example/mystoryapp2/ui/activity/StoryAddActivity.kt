package com.example.mystoryapp2.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.mystoryapp2.R
import com.example.mystoryapp2.databinding.ActivityStoryAddBinding
import com.example.mystoryapp2.model.repository.RequestResult
import com.example.mystoryapp2.ui.viewmodel.StoryAddViewModel
import com.example.mystoryapp2.ui.viewmodel.ViewModelFactory
import com.example.mystoryapp2.util.ImageFileHelper
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class StoryAddActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityStoryAddBinding.inflate(layoutInflater) }
    private val viewModel: StoryAddViewModel by viewModels { ViewModelFactory.getInstance(this) }
    private var imageFile: File? = null
    private var currentImagePath: String = ""

    private var locationGranted = false
    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private var myLocation: Location? = null


    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != RESULT_OK) {
            return@registerForActivityResult
        }

        Toast.makeText(this, getString(R.string.toast_story_add_loading_image), Toast.LENGTH_SHORT)
            .show()

        val file = File(currentImagePath)
        ImageFileHelper.fixImageOrientation(file)
        loadImage(file)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != RESULT_OK) {
            return@registerForActivityResult
        }

        val intent = result.data ?: run {
            Toast.makeText(
                this, getString(R.string.toast_story_add_load_image_failed), Toast.LENGTH_SHORT
            ).show()
            return@registerForActivityResult
        }

        val uri = intent.data ?: run {
            Toast.makeText(
                this, getString(R.string.toast_story_add_load_image_failed), Toast.LENGTH_SHORT
            ).show()
            return@registerForActivityResult
        }

        Toast.makeText(this, getString(R.string.toast_story_add_loading_image), Toast.LENGTH_SHORT)
            .show()
        val file = ImageFileHelper.uriToFile(uri, this@StoryAddActivity)
        loadImage(file)
    }

    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        Log.d(javaClass.simpleName, "permission result: $permission")
        when {
            permission[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false   -> {
                // precise location granted
                Toast.makeText(
                    this,
                    getString(R.string.toast_story_add_location_granted_precise),
                    Toast.LENGTH_SHORT
                ).show()
                getMyLocation()
            }

            permission[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                // approximate location only
                Toast.makeText(
                    this,
                    getString(R.string.toast_story_add_location_granted_approximate),
                    Toast.LENGTH_SHORT
                ).show()
                getMyLocation()
            }

            else                                                                    -> {
                binding.switchAddLocation.isChecked = false
                binding.switchAddLocation.isEnabled = false

                Toast.makeText(
                    this, getString(R.string.toast_story_add_location_denied), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.activity_story_add_title)

        configureObserver()
        configureViews()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun onClick(v: View) {
        val b = binding
        when (v.id) {
            b.btnAddCamera.id      -> openCamera()
            b.btnAddGallery.id     -> openGallery()
            b.buttonAdd.id         -> uploadImage()
            b.switchAddLocation.id -> {
                if (b.switchAddLocation.isChecked) {
                    getMyLocation()
                } else {
                    myLocation = null
                    updateUploadButtonState()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this, getString(R.string.toast_story_add_permission_denied), Toast.LENGTH_SHORT
                ).show()
                setResult(RESULT_CANCELED)
                finish()
            }
        }
    }

    private fun configureViews() = binding.apply {
        btnAddCamera.setOnClickListener(this@StoryAddActivity)
        btnAddGallery.setOnClickListener(this@StoryAddActivity)
        buttonAdd.setOnClickListener(this@StoryAddActivity)
        switchAddLocation.setOnClickListener(this@StoryAddActivity)

        buttonAdd.isEnabled = false     // disable at start

        edAddDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int,
            ) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    edAddDescription.error = getString(R.string.toast_story_add_empty_description)
                    updateUploadButtonState()
                } else {
                    edAddDescription.error = null
                    updateUploadButtonState()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // do nothing
            }
        })
    }

    private fun configureObserver() = viewModel.uploadStatus.observe(this) { result ->
        when (result) {
            is RequestResult.Loading -> {
                showLoading(true)
                enableFields(false)
            }

            is RequestResult.Error   -> {
                Toast.makeText(
                    this,
                    getString(R.string.toast_story_add_failed, result.error),
                    Toast.LENGTH_SHORT
                ).show()
                showLoading(false)
                enableFields(true)
            }

            is RequestResult.Success -> {
                Toast.makeText(
                    this, getString(R.string.toast_story_add_success), Toast.LENGTH_SHORT
                ).show()
                showLoading(false)
                enableFields(true)

                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        ImageFileHelper.createCustomTempFile(application).also {
            val photoUri = FileProvider.getUriForFile(this, packageName, it)
            currentImagePath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun openGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, getString(R.string.image_chooser_title))
        launcherIntentGallery.launch(chooser)
    }

    private fun loadImage(file: File) {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                showLoading(true)
                enableFields(false)
            }

            ImageFileHelper.reduceImageSize(file)?.also {
                imageFile = it
                withContext(Dispatchers.Main) {
                    val (height, width) = binding.ivStoryAdd.run { height to width }
                    binding.ivStoryAdd.setImageBitmap(
                        ImageFileHelper.decodeSampledBitmap(
                            file, width, height
                        )
                    )
                }
            } ?: run {
                imageFile = null
                Toast.makeText(
                    this@StoryAddActivity,
                    getString(R.string.exception_image_too_big),
                    Toast.LENGTH_SHORT
                ).show()
            }

            withContext(Dispatchers.Main) {
                showLoading(false)
                enableFields(true)
            }
        }
    }

    private fun uploadImage() {
        val file = imageFile ?: run {
            Toast.makeText(
                this, getString(R.string.toast_story_add_empty_image), Toast.LENGTH_SHORT
            ).show()
            return
        }
        val description = binding.edAddDescription.text.toString()

        val (lat, lon) = if (binding.switchAddLocation.isChecked) {
            val location = myLocation
                ?: throw IllegalStateException("location is null where it should not be. check your myLocation null validation")
            Pair(location.latitude.toFloat(), location.longitude.toFloat())
        } else {
            Pair(null, null)
        }

        Log.d(
            javaClass.simpleName,
            "switchIsChecked: ${binding.switchAddLocation.isChecked} | location: $lat, $lon"
        )

        viewModel.uploadImage(description, file, lat, lon)

        imageFile = null  // detach
        updateUploadButtonState()

        Toast.makeText(
            this, getString(R.string.toast_story_add_upload_notice), Toast.LENGTH_SHORT
        ).show()
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLocation() {
        locationGranted = checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) || checkPermission(
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (!locationGranted) {
            requestLocationPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            )
            return
        }

        Toast.makeText(
            this, getString(R.string.toast_story_add_geting_location), Toast.LENGTH_SHORT
        ).show()

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                    CancellationTokenSource().token

                override fun isCancellationRequested() = false
            }).addOnSuccessListener { location ->
            if (location != null) {
                myLocation = location
                updateUploadButtonState()
                Log.d(
                    javaClass.simpleName,
                    "myCurrentLocation: (${location.latitude}, ${location.longitude})"
                )
            } else {
                Toast.makeText(
                    this, getString(R.string.toast_story_add_location_not_found), Toast.LENGTH_SHORT
                ).show()
                binding.switchAddLocation.isChecked = false
                updateUploadButtonState()
            }
        }
    }

    /**
     * switch | myLocation  -> locationStatus
     *    0          0               1
     *    0          1               1
     *    1          0               0          >>> switch checked and myLocation null
     *    1          1               1
     *
     */
    private fun updateUploadButtonState() {
        val locationStatus = !(binding.switchAddLocation.isChecked && myLocation == null)
        val descriptionStates = binding.edAddDescription.error == null && !binding.edAddDescription.text.isNullOrBlank()
        val fileStatus = imageFile != null
        binding.buttonAdd.isEnabled = locationStatus && descriptionStates && fileStatus

        Log.d(
            javaClass.simpleName,
            "locationGranted: $locationGranted | myLocation: ${myLocation != null} | switch: ${binding.switchAddLocation.isChecked} | " + "locationStatus: $locationStatus | descriptionStatus: $descriptionStates | fileStatus $fileStatus"
        )
    }

    private fun enableFields(enable: Boolean) {
        binding.apply {
            btnAddCamera.isEnabled = enable
            btnAddGallery.isEnabled = enable
        }
        updateUploadButtonState()
    }

    private fun showLoading(visible: Boolean) {
        binding.pbStoryAdd.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}