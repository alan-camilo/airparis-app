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
package fr.parisrespire.mpp.data.http.model.util

import fr.parisrespire.mpp.data.http.model.Episode
import fr.parisrespire.mpp.data.http.model.IdxvilleInfo
import fr.parisrespire.mpp.data.http.model.Indice
import fr.parisrespire.mpp.data.http.model.IndiceJour

data class DataSetPollution(
    val mDayIndex: IndiceJour? = null,
    val mIndex: Indice? = null,
    val mPollutionEpisode: Episode? = null,
    val mIdxvilleInfo: IdxvilleInfo? = null
) {
    fun isComplete() =
        mDayIndex != null && mIndex != null && mPollutionEpisode != null && mIdxvilleInfo != null
}