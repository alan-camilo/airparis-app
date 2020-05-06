/*
This file is part of Paris respire.

Paris respire is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or any 
later version.

Paris respire is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Paris respire.  If not, see <https://www.gnu.org/licenses/>.
*/
package fr.parisrespire.mpp.data

import com.github.florent37.log.Logger
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import fr.parisrespire.mpp.base.CITY_NAME_PREFERENCE
import fr.parisrespire.mpp.base.INSEE_CODE_PREFERENCE
import fr.parisrespire.mpp.base.IO
import fr.parisrespire.mpp.base.UserPreference
import fr.parisrespire.mpp.data.http.GeoApi
import kotlinx.coroutines.*


class InseeCodeProvider(private val uiExceptionHandler: UIExceptionHandler) {

    private val job = Job()
    private val scope = CoroutineScope(job + IO)
    private val _inseeCode = MutableLiveData<String?>(null)
    val inseeCode: LiveData<String?> = _inseeCode
    private val _cityName = MutableLiveData<String?>(null)
    val cityName: LiveData<String?> = _cityName

    init {
        inseeCode.addObserver { code ->
            // save it in UserPreferences
            if (code != null) {
                Logger.d("InseeCodeProvider", "inseecode=$code")
                UserPreference.set(INSEE_CODE_PREFERENCE, code)
            }
        }
        cityName.addObserver { name ->
            if (name != null) {
                Logger.d("InseeCodeProvider", "city name=$name")
                UserPreference.set(CITY_NAME_PREFERENCE, name)
            }
        }
    }

    fun getInseeCode(postalCode: String) {
        Logger.d("InseeCodeProvider", "getPostalCode")
        scope.launch {
            try {
                withTimeout(10_000) {
                    val result = GeoApi().requestInseeCode(postalCode)
                    if (result.nhits > 0) {
                        val code = result.records[0].fields.code_commune_insee
                        _inseeCode.postValue(code)
                        val name = result.records[0].fields.nom_de_la_commune
                        _cityName.postValue(name)
                    }
                }
            } catch (exception: TimeoutCancellationException) {
                Logger.e("InseeCodeProvider", exception.toString())
                uiExceptionHandler.showError(
                    CustomHttpRequestTimeoutException(
                        exception.message,
                        exception.cause
                    )
                )
            } catch (exception: CustomException) {
                Logger.e("InseeCodeProvider", exception.toString())
                uiExceptionHandler.showError(exception)
            }
        }
    }
}