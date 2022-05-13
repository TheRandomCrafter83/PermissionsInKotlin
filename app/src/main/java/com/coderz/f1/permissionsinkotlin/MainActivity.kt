package com.coderz.f1.permissionsinkotlin

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.coderz.f1.permissionsinkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val permissionsToCheck:Array<String> = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)

    //Permissions Launchers-------------------------------------------------------------------------
    private val getCameraPermission: ActivityResultLauncher<String> = registerForActivityResult(ActivityResultContracts.RequestPermission()){hasPermission ->
        if(!hasPermission) {
            showMessage("Camera Permission has been denied.")
            return@registerForActivityResult
        }
        showMessage("Camera Permission has been given.")
    }

    private val getReadWritePermission: ActivityResultLauncher<Array<String>> = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){permissionsMap->
        permissionsMap.entries.forEach { mutableEntry ->
            showMessage(mutableEntry.key + ": hasPermission = " + mutableEntry.value)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonRequestCameraPermission.setOnClickListener(buttonRequestCameraPermissionListener)
        binding.buttonRequestReadWriteFiles.setOnClickListener(buttonRequestReadWriteFileListener)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private val buttonRequestCameraPermissionListener: View.OnClickListener = View.OnClickListener {
        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) return@OnClickListener
        getCameraPermission.launch(Manifest.permission.CAMERA)
    }

    private val buttonRequestReadWriteFileListener:View.OnClickListener = View.OnClickListener {
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            getReadWritePermission.launch(permissionsToCheck)
            return@OnClickListener
        }
        showMessage("File permissions not needed on API30 and newer for App Owned Files")
    }

    private fun showMessage(message:String){
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }
}