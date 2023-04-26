package com.example.niceshot.viewmodels

import android.content.ContentValues
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.niceshot.data.DataRepository
import com.example.niceshot.data.entities.Photo

class AddPhotoViewModel(private val dataRepository: DataRepository) : ViewModel() {


//    private fun getPhoto(photoId: Int) : Photo? {
//        return dataRepository.getPhoto(photoId)
//    }

//    suspend fun deletePhoto(photoId: Int) {
//        val photo = getPhoto(photoId)
//        if (photo != null) {
//            dataRepository.deletePhoto(photo)
//        }
//    }



    /**
     * Holds current photo ui state
     */
    var photoUiState by mutableStateOf(PhotoUiState())
        private set

    /**
     * Updates the [photoUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(photoDetails: PhotoDetails) {
        photoUiState =
            PhotoUiState(photoDetails = photoDetails, isEntryValid = validateInput(photoDetails))
    }

    /**
     * Inserts an [photo] in the Room database
     */
    suspend fun savePhoto() {
        if (validateInput()) {
            dataRepository.insertPhoto(photoUiState.photoDetails.toPhoto())
        }
    }

    private fun validateInput(uiState: PhotoDetails = photoUiState.photoDetails): Boolean {
        return with(uiState) {
            //        name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
            uri.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an photo.
 */
data class PhotoUiState(
    val photoDetails: PhotoDetails = PhotoDetails(),
    val isEntryValid: Boolean = false
)

data class PhotoDetails(
    var uri: String = "",
    var makeModel: String = "",
    var dateTime: String = "",
    var creatorId: Int = 0,
    var location: String = "",
    var caption: String = "",
    var aperture: String = "",
    var shutterSpeed: String = "",
    var dateUploaded: String = ""
)

/**
 * Extension function to convert [photoUiState] to [photo]. If the value of [photoUiState.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [photoUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun PhotoDetails.toPhoto(): Photo = Photo(
    uri = uri,
    makeModel = makeModel,
    dateTime = dateTime,
    creatorId = creatorId,
    location = location,
    caption = caption,
    aperture = aperture,
    shutterSpeed = shutterSpeed,
    dateUploaded = dateUploaded
)

/**
 * Extension function to convert [photo] to [photoUiState]
 */
fun Photo.toPhotoUiState(isEntryValid: Boolean = false): PhotoUiState = PhotoUiState(
    photoDetails = this.toPhotoDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [photo] to [photoDetails]
 */
fun Photo.toPhotoDetails(): PhotoDetails = PhotoDetails(
    uri = uri,
    makeModel = makeModel,
    dateTime = dateTime,
    creatorId = creatorId,
    location = location,
    caption = caption,
    aperture = aperture,
    shutterSpeed = shutterSpeed,
    dateUploaded = dateUploaded
)








fun getCamInfo(
    context: Context
): Array<CameraInstance> {
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val logicalCameras = cameraManager.cameraIdList
    var cameras = arrayOf<CameraInstance>()
    if (logicalCameras.isNotEmpty()) {
        for (x in logicalCameras) {
            var characteristics = cameraManager.getCameraCharacteristics(x.toString())
            val physicalCameraIds = characteristics.physicalCameraIds
            if (physicalCameraIds.size > 1) {
                for (i in physicalCameraIds) {
                    characteristics = cameraManager.getCameraCharacteristics(i.toString())
                    val focalLengths =
                        characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)!!
                    val apertures =
                        characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES)!!
                    val instance = CameraInstance(
                        focalLengths[0],
                        apertures[0]
                    )
                    cameras += instance
                }
            } else {
                characteristics = cameraManager.getCameraCharacteristics(x.toString())
                val focalLengths =
                    characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)!!
                val apertures =
                    characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES)!!
                val instance = CameraInstance(
                    focalLengths[0],
                    apertures[0]
                )
                cameras += instance
            }
        }
    }
    return cameras
}

class CameraInstance(
    val camFocalLength: Float,
    val camAperture: Float
)

fun validateImage(
    context: Context,
    uri: Uri,
    cameras: Array<CameraInstance>,
    //   image: Image?
): Image? {
    var valid = false

    val deviceMake: String = Build.MANUFACTURER
    val deviceModel: String = Build.MODEL

    val image = imageConstructor(context, uri)

    if (image != null) {
        if (image.mimeType == "image/jpeg") {
            if (image.make == deviceMake && image.model == deviceModel) {
                Log.d(ContentValues.TAG, "DEVICE MATCH")
                for (x in cameras) {
                    if (x.camFocalLength.toString() == image.focalLength.toString() && x.camAperture.toString() == image.aperture.toString()) {
                        valid = true
                    }
                }
            }
        }
    }

    if (image != null) {
        image.valid = valid
    }

    return image
}

fun imageConstructor(
    context: Context,
    uri: Uri
) : Image {

    var candidateImage: Image

    context.contentResolver.openInputStream(uri).use { inputStream ->
        val exif: ExifInterface? = inputStream?.let { ExifInterface(it) }
        val make = exif?.getAttribute(ExifInterface.TAG_MAKE)
        val model = exif?.getAttribute(ExifInterface.TAG_MODEL)
        val mimeType: String? = context.contentResolver.getType(uri)
        val imgFocalLengthRaw = exif?.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)
        var focalLength: String? = "0.0"
        if (imgFocalLengthRaw != null
            && imgFocalLengthRaw.count() >= 4
            && isNumeric(imgFocalLengthRaw.substring(0, 4))
        ) {
            val focalLengthNumber = imgFocalLengthRaw.substring(0, 4)
            focalLength = (focalLengthNumber.toFloat() / 1000).toString()
        }
        val aperture = exif?.getAttribute(ExifInterface.TAG_F_NUMBER)
        val shutterSpeed = exif?.getAttribute(ExifInterface.TAG_EXPOSURE_TIME)
        val dateTime = exif?.getAttribute(ExifInterface.TAG_DATETIME)
//        val gps = FloatArray(2)
//        exif?.getLatLong(gps)
//        Log.d(TAG, "${gps[0]}")
//        Log.d(TAG, "${exif?.getLatLong(gps)}")

        candidateImage = Image(
            make,
            model,
            mimeType,
            focalLength,
            aperture,
            shutterSpeed,
            dateTime
        )
    }
    return candidateImage
}

class Image(
    val make: String?,
    val model: String?,
    val mimeType: String?,
    val focalLength: String?,
    val aperture: String?,
    val shutterSpeed: String?,
    val dateTime: String?,
    var valid: Boolean = false
)

fun isNumeric(toCheck: String): Boolean {
    return toCheck.all { char -> char.isDigit() }
}