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
package fr.parisrespire.util

import com.github.florent37.log.Logger
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import fr.parisrespire.mpp.base.INSEE_CODE_PREFERENCE
import fr.parisrespire.mpp.base.IO
import fr.parisrespire.mpp.base.UserPreference
import fr.parisrespire.mpp.data.CustomException
import fr.parisrespire.mpp.data.CustomHttpRequestTimeoutException
import fr.parisrespire.mpp.data.UIExceptionHandler
import fr.parisrespire.mpp.data.http.GeoApi
import kotlinx.coroutines.*


class InseeCodeProvider(private val uiExceptionHandler: UIExceptionHandler) {

    private val job = Job()
    private val scope = CoroutineScope(job + IO)
    private val _inseeCode = MutableLiveData<String?>(null)
    val inseeCode: LiveData<String?> = _inseeCode

    init {
        inseeCode.addObserver {
            // save it in UserPreferences
            if (it != null) {
                Logger.d("InseeCodeProvider", "inseecode=$it")
                UserPreference.set(INSEE_CODE_PREFERENCE, it)
            }
        }
    }

    fun getInseeCode(postalCode: String) {
        Logger.d("InseeCodeProvider", "getPostalCode")
        scope.launch {
            try {
                withTimeout(10_000) {
                    val result = GeoApi().requestInseeCode(postalCode)
                    _inseeCode.postValue(result.code)
                }
            } catch (exception: TimeoutCancellationException) {
                Logger.e("LocationProvider", exception.toString())
                uiExceptionHandler.showError(CustomHttpRequestTimeoutException(exception.message, exception.cause))
            } catch (exception: CustomException) {
                Logger.e("LocationProvider", exception.toString())
                uiExceptionHandler.showError(exception)
            }
        }
    }
}