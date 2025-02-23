package ochat.wearendar.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import ochat.wearendar.backend.uploadImageToImgBB
import ochat.wearendar.ui.theme.MontserratFontFamily
import java.io.File

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CameraScreen() {
    var selectedPhotoFile by remember { mutableStateOf<File?>(null) }

    CameraContent(
        onPhotoSelected = { file ->
            selectedPhotoFile = file
            uploadImageToImgBB(file) { url ->
                Log.d("ULR", url!!)
            }
        }
    )
}

@Composable
fun CameraContent(onPhotoSelected: (File) -> Unit) {
    var showCamera by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let { onPhotoSelected(uriToFile(context, it)) }
        }
    )

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (showCamera) {
                CameraView(
                    onPhotoTaken = { file ->
                        showCamera = false
                        onPhotoSelected(file)
                    }
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            shape = RoundedCornerShape(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.width(180.dp).height(60.dp)
                        ) {
                            Text(
                                text = "Elegir Foto",
                                fontFamily = MontserratFontFamily,
                                fontStyle = FontStyle.Normal,
                                fontSize = 18.sp
                            )
                        }

                        Spacer(Modifier.size(32.dp))

                        Button(
                            onClick = { showCamera = true },
                            shape = RoundedCornerShape(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.width(180.dp).height(60.dp)
                        ) {
                            Text(
                                text = "Tomar Foto",
                                fontFamily = MontserratFontFamily,
                                fontStyle = FontStyle.Normal,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CameraView(onPhotoTaken: (File) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var hasCameraPermission by remember { mutableStateOf(false) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build()
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    val imageCaptureConfig = ImageCapture.Builder().build()
                    imageCapture = imageCaptureConfig

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCaptureConfig
                    )

                    preview.surfaceProvider = previewView.surfaceProvider
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 86.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.White, shape = CircleShape)
                        .clickable {
                            takePhoto(context, imageCapture!!, onPhotoTaken)
                        }
                        .align(Alignment.BottomCenter)
                )
            }
        } else {
            Text(
                text = "PERMISO DE CÁMARA REQUERIDO",
                fontFamily = MontserratFontFamily,
                fontStyle = FontStyle.Normal,
                fontSize = 20.sp
            )
        }
    }
}

fun takePhoto(context: Context, imageCapture: ImageCapture, onPhotoTaken: (File) -> Unit) {
    Toast.makeText(context, "Foto tomada", Toast.LENGTH_LONG).show()

    val photoFile = File.createTempFile("photo_", ".jpg", context.cacheDir)
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onPhotoTaken(photoFile)
            }

            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(context, "Error al guardar la foto: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    )
}

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val tempFile = File.createTempFile("selected_", ".jpg", context.cacheDir)
    inputStream?.use { input ->
        tempFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return tempFile
}