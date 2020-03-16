/*
This file is part of airparis.

airparis is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or any
later version.

airparis is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with airparis.  If not, see <https://www.gnu.org/licenses/>.
*/
package airparis.base

import kotlin.native.ref.WeakReference

class iOSWeakRef<T : Any>(val weakValue: WeakReference<T>) :
    WeakRef<T> {
    override val value: T?
        get() = weakValue.get()
}

actual fun <T : Any> buildWeakRef(value: T): WeakRef<T> {
    return iOSWeakRef(WeakReference(value))
}
