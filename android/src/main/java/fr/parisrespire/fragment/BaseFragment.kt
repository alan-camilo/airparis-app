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
package fr.parisrespire.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import parisrespire.base.BaseViewModel
import parisrespire.base.State
import parisrespire.base.StateChangeListener

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

/**
 * Base [Fragment] used to map a [BaseViewModel] and handle its lifecycle events.
 */
open class BaseFragment<ST : State, VM : BaseViewModel<ST>> :
    Fragment(), StateChangeListener<ST> {

    @LayoutRes
    private var layoutId: Int = 0
    protected lateinit var viewModel: VM
    private var state: ST? = null
    private var isInitialized = false

    /**
     * Initialize all DataBinding variables.
     */
    protected fun initialize(
        @LayoutRes layoutId: Int,
        vm: VM
    ) {
        isInitialized = true
        this.layoutId = layoutId
        viewModel = vm
        viewModel.setStateChangeListener(this)
    }

    override fun onStateChange(state: ST) {
        this.state = state
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        instanceState: Bundle?
    ): View? {
        if (!isInitialized)
            throw UninitializedPropertyAccessException("initialize() not called!")
        return inflater.inflate(this.layoutId, container, false)
    }

    //region base.BaseViewModel lifecycle events
    override fun onStart() {
        super.onStart()
        viewModel.onActive()
    }

    override fun onStop() {
        viewModel.onInactive()
        super.onStop()
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }
    //endregion
}
