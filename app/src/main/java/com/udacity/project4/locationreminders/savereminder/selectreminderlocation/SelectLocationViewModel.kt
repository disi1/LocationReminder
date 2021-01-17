package com.udacity.project4.locationreminders.savereminder.selectreminderlocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PointOfInterest
import java.util.*

class SelectLocationViewModel: ViewModel() {

    private val _selectedLocation = MutableLiveData<PointOfInterest>()
    val selectedLocation: LiveData<PointOfInterest>
        get() = _selectedLocation

    fun setSelectedLocation(poi: PointOfInterest) {
        _selectedLocation.value = poi
    }

    fun setSelectedLocation(latLng: LatLng) {
        val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude,
        )
        setSelectedLocation(PointOfInterest(latLng, null, snippet))
    }
}